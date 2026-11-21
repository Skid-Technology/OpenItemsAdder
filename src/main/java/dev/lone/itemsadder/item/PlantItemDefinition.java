package dev.lone.itemsadder.item;

import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.util.LocationScanner;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class PlantItemDefinition extends CustomItemDefinition {
   public final Material apt;
   public final List apu;

   public PlantItemDefinition(YamlConfig var1, String var2, String var3, Material var4, Material var5) {
      super(var1, var2, var3, var4);
      this.apt = var5;
      this.apu = new ArrayList();
   }

   public List Eg() {
      return this.apu;
   }

   public ItemVariantHolder method8837(ItemVariantHolder var1) {
      this.apu.add(var1);
      return var1;
   }

   public void aC(Block var1) {
      var1.setType(this.apt);
   }

   public List method8838(ItemStack var1) {
      ArrayList var2 = new ArrayList();

      for (ItemVariantHolder var4 : this.apu) {
         if (var4.gC()) {
            var2.add(var4.method10017(var1, true));
         }
      }

      return var2;
   }

   public void method8839(ItemStack var1, Location var2) {
      for (ItemVariantHolder var4 : this.apu) {
         if (var4.gC()) {
            LocationScanner.dropItem(var2, var4.method10017(var1, true));
         }
      }
   }

   public void method8840(Location var1) {
      for (ItemVariantHolder var3 : this.apu) {
         if (var3.gC()) {
            LocationScanner.dropItem(var1, var3.buildItem(true));
         }
      }
   }

   public static boolean aD(Block var0) {
      return var0 == null
         ? false
         : var0.getType() == Material.BEETROOTS
            || var0.getType() == Material.CARROTS
            || var0.getType() == Material.NETHER_WART
            || var0.getType() == Material.POTATOES
            || var0.getType() == Material.SWEET_BERRY_BUSH
            || var0.getType() == Material.WHEAT;
   }
}
