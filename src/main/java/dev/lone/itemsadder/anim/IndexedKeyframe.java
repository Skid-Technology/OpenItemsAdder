package dev.lone.itemsadder.anim;

class IndexedKeyframe {
   private final int Ou;
   private final int[] Ov;
   private final int Ow;
   private final float Ox;
   private final float Oy;
   private final float Oz;

   private IndexedKeyframe(int var1, int[] var2, int var3, float var4, float var5, float var6) {
      this.Ou = var1;
      this.Ov = var2;
      this.Ow = var3;
      this.Ox = var4;
      this.Oy = var5;
      this.Oz = var6;
   }

   public int tM() {
      return this.Ou;
   }

   public int[] tN() {
      return this.Ov;
   }

   public int tO() {
      return this.Ow;
   }

   public float oz() {
      return this.Ox;
   }

   public float oA() {
      return this.Oy;
   }

   public float oB() {
      return this.Oz;
   }
}
