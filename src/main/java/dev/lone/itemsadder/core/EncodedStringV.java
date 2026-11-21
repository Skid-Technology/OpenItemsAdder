package dev.lone.itemsadder.core;

class EncodedStringV {
   int dj;

   EncodedStringV(UpdateChecker var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[1];
      this.dj = -699280593;
      var1[0] = (byte)(this.dj >>> 3);
      return new String(var1);
   }
}
