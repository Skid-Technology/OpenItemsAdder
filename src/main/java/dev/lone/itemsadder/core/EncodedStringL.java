package dev.lone.itemsadder.core;

class EncodedStringL {
   int dj;

   @Override
   public String toString() {
      byte[] var1 = new byte[1];
      this.dj = -904355031;
      var1[0] = (byte)(this.dj >>> 14);
      return new String(var1);
   }
}
