package dev.lone.itemsadder.util;

import java.util.Objects;
import java.util.Map.Entry;

class CustomHashMapNode implements Entry {
   final int bOC;
   final Object bOD;
   Object auj;
   CustomHashMapNode bOu;

   CustomHashMapNode(int var1, Object var2, Object var3, CustomHashMapNode var4) {
      this.bOC = var1;
      this.bOD = var2;
      this.auj = var3;
      this.bOu = var4;
   }

   @Override
   public final Object getKey() {
      return this.bOD;
   }

   @Override
   public final Object getValue() {
      return this.auj;
   }

   @Override
   public final String toString() {
      return this.bOD + "=" + this.auj;
   }

   @Override
   public final int hashCode() {
      return Objects.hashCode(this.bOD) ^ Objects.hashCode(this.auj);
   }

   @Override
   public final Object setValue(Object newValue) {
      Object var2 = this.auj;
      this.auj = newValue;
      return var2;
   }

   @Override
   public final boolean equals(Object o) {
      if (o == this) {
         return true;
      } else {
         return !(o instanceof Entry var2) ? false : Objects.equals(this.bOD, var2.getKey()) && Objects.equals(this.auj, var2.getValue());
      }
   }
}
