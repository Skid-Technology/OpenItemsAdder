package dev.lone.itemsadder.loot;

import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NbtTagType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;

public class MetadataDropTrigger implements DropTrigger {
   String yK;
   Object auj;
   NbtTagType auk;

   public MetadataDropTrigger(String var1, Object var2, String var3) {
      this.yK = var1;
      this.auj = var2;
      String var4 = StringUtils.capitalize(var3.toLowerCase());

      try {
         this.auk = NbtTagType.valueOf(var4);
      } catch (IllegalArgumentException var6) {
         Log.error(
            "Unknown metadata.type '" + var3 + "' for metadata of name '" + var1 + "'." + ChatColor.GRAY + " Allowed: string, int, float, double, byte, short"
         );
      }
   }

   public String getName() {
      return this.yK;
   }

   public Object Ht() {
      return this.auj;
   }

   @Override
   public boolean method2018(Entity var1) {
      return !var1.hasMetadata(this.yK) ? false : ((MetadataValue)var1.getMetadata(this.yK).get(0)).value().equals(this.auj);
   }

   @Override
   public boolean aJ(Block var1) {
      return false;
   }
}
