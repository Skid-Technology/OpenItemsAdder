package dev.lone.itemsadder.item.effect;

class AttributeSetRule {
   private final String[] alt;
   private final AttributeSetGroup[] alu;

   AttributeSetRule(String[] var1, AttributeSetGroup[] var2) {
      this.alt = var1;
      this.alu = var2;
   }

   AttributeSetGroup parseGroup(String[] var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 4; var3++) {
         if (this.alt[var3].equals(var1[var3])) {
            var2++;
         }
      }

      for (int var4 = var2; var4 > 0; var4--) {
         if (this.alu[var4] != null) {
            return this.alu[var4];
         }
      }

      return null;
   }

   public String[] slots() {
      return this.alt;
   }

   public AttributeSetGroup[] groups() {
      return this.alu;
   }
}
