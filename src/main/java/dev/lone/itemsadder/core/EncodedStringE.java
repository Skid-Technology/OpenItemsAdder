package dev.lone.itemsadder.core;

class EncodedStringE {
   int dj;

   EncodedStringE(UpdateChecker var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[16];
      this.dj = 735241436;
      var1[0] = (byte)(this.dj >>> 12);
      this.dj = -1259008241;
      var1[1] = (byte)(this.dj >>> 23);
      this.dj = -668040230;
      var1[2] = (byte)(this.dj >>> 13);
      this.dj = -914516846;
      var1[3] = (byte)(this.dj >>> 10);
      this.dj = -201943625;
      var1[4] = (byte)(this.dj >>> 2);
      this.dj = -1620132964;
      var1[5] = (byte)(this.dj >>> 3);
      this.dj = -1583250352;
      var1[6] = (byte)(this.dj >>> 10);
      this.dj = -139306437;
      var1[7] = (byte)(this.dj >>> 15);
      this.dj = -81779096;
      var1[8] = (byte)(this.dj >>> 19);
      this.dj = 1499498939;
      var1[9] = (byte)(this.dj >>> 22);
      this.dj = 1914728032;
      var1[10] = (byte)(this.dj >>> 24);
      this.dj = 546879488;
      var1[11] = (byte)(this.dj >>> 10);
      this.dj = 166022400;
      var1[12] = (byte)(this.dj >>> 8);
      this.dj = -2070650511;
      var1[13] = (byte)(this.dj >>> 8);
      this.dj = -1135948490;
      var1[14] = (byte)(this.dj >>> 5);
      this.dj = -1850990371;
      var1[15] = (byte)(this.dj >>> 1);
      return new String(var1);
   }
}
