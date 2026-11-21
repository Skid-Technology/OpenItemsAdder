package dev.lone.itemsadder.anim;

public class BoneBinding {
   private final String Ei;
   private final String Ej;

   public BoneBinding(String var1, String var2) {
      this.Ei = var1;
      this.Ej = var2;
   }

   public String bone() {
      return this.Ei;
   }

   public String qa() {
      return this.Ej;
   }
}
