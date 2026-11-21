package dev.lone.itemsadder.core;

class EncodedStringAA {
   int dj;

   EncodedStringAA(UpdateChecker var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[19];
      this.dj = 1395475563;
      var1[0] = (byte)(this.dj >>> 13);
      this.dj = 512609034;
      var1[1] = (byte)(this.dj >>> 3);
      this.dj = -1640268745;
      var1[2] = (byte)(this.dj >>> 15);
      this.dj = -1798556448;
      var1[3] = (byte)(this.dj >>> 13);
      this.dj = -1969799549;
      var1[4] = (byte)(this.dj >>> 15);
      this.dj = 1453187689;
      var1[5] = (byte)(this.dj >>> 20);
      this.dj = -19140001;
      var1[6] = (byte)(this.dj >>> 14);
      this.dj = 774780947;
      var1[7] = (byte)(this.dj >>> 16);
      this.dj = -90027555;
      var1[8] = (byte)(this.dj >>> 8);
      this.dj = 1306238097;
      var1[9] = (byte)(this.dj >>> 21);
      this.dj = -1193352953;
      var1[10] = (byte)(this.dj >>> 9);
      this.dj = -1163719295;
      var1[11] = (byte)(this.dj >>> 23);
      this.dj = -2132153837;
      var1[12] = (byte)(this.dj >>> 17);
      this.dj = 349314237;
      var1[13] = (byte)(this.dj >>> 22);
      this.dj = -610463531;
      var1[14] = (byte)(this.dj >>> 14);
      this.dj = 536602400;
      var1[15] = (byte)(this.dj >>> 4);
      this.dj = -997536908;
      var1[16] = (byte)(this.dj >>> 9);
      this.dj = -1239740564;
      var1[17] = (byte)(this.dj >>> 20);
      this.dj = 2023963496;
      var1[18] = (byte)(this.dj >>> 3);
      return new String(var1);
   }
}
