package dev.lone.itemsadder.loot;

import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.internal.SwitchMap4;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NbtTagType;
import dev.lone.itemsadder.world.BlockEffectsService;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public class NbtDropTrigger implements DropTrigger {
   final String aum;
   Object aun;
   String[] auo;
   NbtTagType auk;

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public NbtDropTrigger(String var1, String var2, String var3) {
      this.aum = var1;
      this.aun = var2;

      try {
         this.auk = NbtTagType.valueOf(StringUtils.capitalize(var3.toLowerCase()));
      } catch (Throwable var5) {
         Log.error("Unknown nbt.type '" + var3 + "' for nbt path '" + var1 + "'." + ChatColor.GRAY + " Allowed: string, int, float, double, byte, short");
         return;
      }

      switch (SwitchMap4.aup[this.auk.ordinal()]) {
         case 1:
         default:
            break;
         case 2:
            this.aun = Integer.parseInt(var2);
            break;
         case 3:
            this.aun = Float.parseFloat(var2);
            break;
         case 4:
            this.aun = Double.parseDouble(var2);
            break;
         case 5:
            this.aun = Byte.parseByte(var2);
            break;
         case 6:
            this.aun = Short.parseShort(var2);
      }

      this.auo = var1.split("\\.");
   }

   public String Hx() {
      return this.aum;
   }

   public Object Hy() {
      return this.aun;
   }

   public String[] Hz() {
      return this.auo;
   }

   public NbtTagType HA() {
      return this.auk;
   }

   @Override
   public boolean method2018(Entity var1) {
      return ProjectileTypeHelper.getAdapter().method3268(var1, this);
   }

   @Override
   public boolean aJ(Block var1) {
      return BlockEffectsService.getAdapter().matchesNbtDrop(var1, this);
   }
}
