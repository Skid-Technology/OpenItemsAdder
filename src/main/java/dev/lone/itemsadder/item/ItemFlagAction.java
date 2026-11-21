package dev.lone.itemsadder.item;

public class ItemFlagAction extends ItemActionBase {
   public static final String aeh = "furniture_sit";
   final double aei;
   final boolean aej;
   final boolean aek;
   final boolean ael;
   final boolean aem;

   public ItemFlagAction(CustomItemDefinition var1, double var2, boolean var4, boolean var5, boolean var6, boolean var7) {
      super(var1);
      this.aei = var2;
      this.aej = var4;
      this.aek = var5;
      this.ael = var6;
      this.aem = var7;
   }

   public double AJ() {
      return this.aei;
   }

   public boolean AK() {
      return this.aek;
   }

   public boolean AL() {
      return this.aej;
   }

   public boolean AM() {
      return this.ael;
   }

   public boolean AN() {
      return this.aem;
   }
}
