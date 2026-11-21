package dev.lone.itemsadder.entity;

class VisibilityFlags {
   private final boolean uv;
   private final boolean uw;
   private final boolean ux;
   private final boolean uy;

   private VisibilityFlags(boolean var1, boolean var2, boolean var3, boolean var4) {
      this.uv = var1;
      this.uw = var2;
      this.ux = var3;
      this.uy = var4;
   }

   public boolean kL() {
      return this.uv;
   }

   public boolean kM() {
      return this.uw;
   }

   public boolean kN() {
      return this.ux;
   }

   public boolean kO() {
      return this.uy;
   }
}
