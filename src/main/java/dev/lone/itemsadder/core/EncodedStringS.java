package dev.lone.itemsadder.core;

class EncodedStringS {
   int dj;

   EncodedStringS(UpdateChecker var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[16];
      this.dj = 2104291878;
      var1[0] = (byte)(this.dj >>> 13);
      this.dj = -1716079986;
      var1[1] = (byte)(this.dj >>> 7);
      this.dj = 172300104;
      var1[2] = (byte)(this.dj >>> 4);
      this.dj = -720795771;
      var1[3] = (byte)(this.dj >>> 20);
      this.dj = -13933188;
      var1[4] = (byte)(this.dj >>> 11);
      this.dj = -812263824;
      var1[5] = (byte)(this.dj >>> 10);
      this.dj = 217947450;
      var1[6] = (byte)(this.dj >>> 21);
      this.dj = 213660842;
      var1[7] = (byte)(this.dj >>> 7);
      this.dj = 939975566;
      var1[8] = (byte)(this.dj >>> 12);
      this.dj = 2105494917;
      var1[9] = (byte)(this.dj >>> 8);
      this.dj = -570007141;
      var1[10] = (byte)(this.dj >>> 8);
      this.dj = 1567547645;
      var1[11] = (byte)(this.dj >>> 16);
      this.dj = -217434811;
      var1[12] = (byte)(this.dj >>> 19);
      this.dj = -751246032;
      var1[13] = (byte)(this.dj >>> 19);
      this.dj = -1248245825;
      var1[14] = (byte)(this.dj >>> 14);
      this.dj = -416791027;
      var1[15] = (byte)(this.dj >>> 20);
      return new String(var1);
   }
}
