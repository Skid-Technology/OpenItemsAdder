package dev.lone.itemsadder.recipe;

public class RecipeRegistration {
   private final AbstractCustomRecipe aBj;
   private final String aBk;
   private final String aBl;

   public RecipeRegistration(AbstractCustomRecipe var1, String var2, String var3) {
      this.aBj = var1;
      this.aBk = var2;
      this.aBl = var3;
   }

   public void method2219(RecipeType var1) {
      var1.method760(this.aBj, this.aBk, this.aBl);
   }

   public static void method2220(RecipeType var0, Iterable var1) {
      for (RecipeRegistration var3 : var1) {
         var3.method2219(var0);
      }
   }
}
