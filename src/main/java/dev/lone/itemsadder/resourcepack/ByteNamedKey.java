package dev.lone.itemsadder.resourcepack;

public class ByteNamedKey {
   private final String jh;
   private final byte ji;

   public ByteNamedKey(String var1, byte var2) {
      this.jh = var1;
      this.ji = var2;
   }

   public String namespacedId() {
      return this.jh;
   }

   public byte age() {
      return this.ji;
   }
}
