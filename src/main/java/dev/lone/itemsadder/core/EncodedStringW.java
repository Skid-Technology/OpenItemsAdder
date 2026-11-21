package dev.lone.itemsadder.core;

class EncodedStringW {
   int dj;

   EncodedStringW(UpdateChecker var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[1];
      this.dj = 1504929954;
      var1[0] = (byte)(this.dj >>> 22);
      return new String(var1);
   }
}
