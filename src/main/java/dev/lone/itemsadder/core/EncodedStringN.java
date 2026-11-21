package dev.lone.itemsadder.core;

class EncodedStringN {
   int dj;

   EncodedStringN(UpdateChecker var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[17];
      this.dj = -180018978;
      var1[0] = (byte)(this.dj >>> 1);
      this.dj = 64797476;
      var1[1] = (byte)(this.dj >>> 14);
      this.dj = 2111753451;
      var1[2] = (byte)(this.dj >>> 5);
      this.dj = -403076220;
      var1[3] = (byte)(this.dj >>> 6);
      this.dj = -2057155536;
      var1[4] = (byte)(this.dj >>> 16);
      this.dj = 1233082838;
      var1[5] = (byte)(this.dj >>> 2);
      this.dj = 1835405892;
      var1[6] = (byte)(this.dj >>> 21);
      this.dj = -1248689301;
      var1[7] = (byte)(this.dj >>> 23);
      this.dj = 1733928605;
      var1[8] = (byte)(this.dj >>> 10);
      this.dj = -449779566;
      var1[9] = (byte)(this.dj >>> 9);
      this.dj = 519079388;
      var1[10] = (byte)(this.dj >>> 5);
      this.dj = 1716553307;
      var1[11] = (byte)(this.dj >>> 14);
      this.dj = -755468918;
      var1[12] = (byte)(this.dj >>> 8);
      this.dj = 111070045;
      var1[13] = (byte)(this.dj >>> 3);
      this.dj = -1381205916;
      var1[14] = (byte)(this.dj >>> 18);
      this.dj = 694755538;
      var1[15] = (byte)(this.dj >>> 1);
      this.dj = -1280929959;
      var1[16] = (byte)(this.dj >>> 19);
      return new String(var1);
   }
}
