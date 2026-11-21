package dev.lone.itemsadder.item;

public class ItemCommandAction extends ItemActionBase {
   public static final String aev = "music_disc";
   public final String aew;
   public final String aex;

   public ItemCommandAction(CustomItemDefinition var1, String var2, String var3) {
      super(var1);
      this.aew = var2;
      this.aex = var3;
   }
}
