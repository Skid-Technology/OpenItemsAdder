package dev.lone.itemsadder.entity;

public class CustomEntityRef {
   private final int wo;
   private final String wp;

   public CustomEntityRef(int var1, String var2) {
      this.wo = var1;
      this.wp = var2;
   }

   public int index() {
      return this.wo;
   }

   public String namespacedId() {
      return this.wp;
   }
}
