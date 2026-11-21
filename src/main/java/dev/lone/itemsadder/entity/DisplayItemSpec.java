package dev.lone.itemsadder.entity;

import org.bukkit.Material;

class DisplayItemSpec {
   private final String VP;
   private final String VQ;
   private final int VR;
   private final int VS;
   private final Material VT;

   DisplayItemSpec(String var1, String var2, int var3, int var4, Material var5) {
      this.VP = var1;
      this.VQ = var2;
      this.VR = var3;
      this.VS = var4;
      this.VT = var5;
   }

   public String wk() {
      return this.VP;
   }

   public String wl() {
      return this.VQ;
   }

   public int wm() {
      return this.VR;
   }

   public int wn() {
      return this.VS;
   }

   public Material material() {
      return this.VT;
   }
}
