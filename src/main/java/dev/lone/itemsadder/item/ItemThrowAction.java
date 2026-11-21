package dev.lone.itemsadder.item;

import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class ItemThrowAction extends ItemEntityAction {
   public static final NamespacedKey aeE = new NamespacedKey("itemsadder", "vehicle");
   public static final String aeF = "vehicle";
   final float aeG;
   final float aeH;
   float aeI = 0.0F;
   float aeJ = 0.0F;
   float aeK = 0.0F;
   final boolean aeL;
   final double aeM;
   Vector aeN;
   int aeO = 60;
   int aeP = 300;
   public final List aeQ = new ArrayList();
   int aeR;
   Vector aeS;

   public ItemThrowAction(
      CustomItemDefinition var1,
      boolean var2,
      boolean var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      int var9,
      int var10,
      boolean var11,
      double var12
   ) {
      super(var1, EntityType.ARMOR_STAND, var2, var3);
      this.aeG = var4;
      this.aeH = var5;
      this.aeI = var6;
      this.aeJ = var7;
      this.aeK = var8;
      this.aeL = var11;
      this.aeM = var12;
      this.aeO = var9;
      this.aeP = var10;
      this.aeN = new Vector(0.5, 0.5, 0.5);
   }

   public void setSmokeEffect(int var1, double var2, double var4, double var6) {
      this.aeR = var1;
      this.aeS = new Vector(var2, var4, var6);
   }

   public Vector AR() {
      return this.aeS;
   }

   public int AS() {
      return this.aeR;
   }

   public float AT() {
      return this.aeG;
   }

   public float AU() {
      return this.aeH;
   }

   public float AV() {
      return this.aeI;
   }

   public float AW() {
      return this.aeJ;
   }

   public float lv() {
      return this.aeK;
   }

   public boolean AX() {
      return this.aeL;
   }

   public double AY() {
      return this.aeM;
   }

   public int AZ() {
      return this.aeO;
   }

   public int Ba() {
      return this.aeP;
   }

   public ItemStackPair addFuelOption(ItemStackPair var1) {
      this.aeQ.add(var1);
      return var1;
   }

   public List Bb() {
      return this.aeQ;
   }

   public boolean isFuel(ItemStack var1) {
      for (ItemStackPair var3 : this.aeQ) {
         if (var3.itemStack.isSimilar(var1)) {
            return true;
         }
      }

      return false;
   }

   public int getFuelAmount(ItemStack var1) {
      for (ItemStackPair var3 : this.aeQ) {
         if (var3.itemStack.isSimilar(var1)) {
            return var3.aif;
         }
      }

      return 0;
   }

   @Nullable
   public ItemStack getFuelReturnItem(ItemStack var1) {
      for (ItemStackPair var3 : this.aeQ) {
         if (var3.itemStack.isSimilar(var1)) {
            return var3.aie;
         }
      }

      return null;
   }

   public static void method396(Entity var0) {
      ItemThrowAction var1 = (ItemThrowAction)c(var0, "vehicle");
      if (var1 != null) {
         var1.method367(var0);
      }
   }

   @Override
   public void method367(Entity var1) {
      ProjectileTypeHelper.method3220(var1, this.aeG);
      ProjectileTypeHelper.getAdapter().method3259(var1, this.xS, this.ut);
   }

   @Override
   public void method361(Entity var1, CustomItemDefinition var2) {
      super.method361(var1, var2);
      var1.getPersistentDataContainer().set(acV, PersistentDataType.STRING, "vehicle");
      var1.setCustomName("ItemsAdder_vehicle");
      var1.setCustomNameVisible(false);
   }

   public static boolean method397(Entity var0) {
      String var1 = (String)var0.getPersistentDataContainer().get(acV, PersistentDataType.STRING);
      return var1 != null && var1.equals("vehicle");
   }
}
