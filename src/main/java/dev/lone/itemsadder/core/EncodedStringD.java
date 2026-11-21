package dev.lone.itemsadder.core;

class EncodedStringD {
   int dj;

   EncodedStringD(UpdateChecker var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[8];
      this.dj = -1106558534;
      var1[0] = (byte)(this.dj >>> 2);
      this.dj = -92923543;
      var1[1] = (byte)(this.dj >>> 6);
      this.dj = 831173574;
      var1[2] = (byte)(this.dj >>> 3);
      this.dj = -2103768855;
      var1[3] = (byte)(this.dj >>> 1);
      this.dj = 1285697831;
      var1[4] = (byte)(this.dj >>> 24);
      this.dj = 538860243;
      var1[5] = (byte)(this.dj >>> 1);
      this.dj = 1747302968;
      var1[6] = (byte)(this.dj >>> 10);
      this.dj = 2066384013;
      var1[7] = (byte)(this.dj >>> 19);
      return new String(var1);
   }
}
