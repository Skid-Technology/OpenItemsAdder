package dev.lone.itemsadder.core;

class EncodedStringM {
   int dj;

   @Override
   public String toString() {
      byte[] var1 = new byte[20];
      this.dj = 837970175;
      var1[0] = (byte)(this.dj >>> 23);
      this.dj = -1686145436;
      var1[1] = (byte)(this.dj >>> 19);
      this.dj = 229612365;
      var1[2] = (byte)(this.dj >>> 6);
      this.dj = -1849305844;
      var1[3] = (byte)(this.dj >>> 13);
      this.dj = 115713651;
      var1[4] = (byte)(this.dj >>> 17);
      this.dj = 734678411;
      var1[5] = (byte)(this.dj >>> 19);
      this.dj = -308747962;
      var1[6] = (byte)(this.dj >>> 21);
      this.dj = 13855158;
      var1[7] = (byte)(this.dj >>> 17);
      this.dj = -2083667564;
      var1[8] = (byte)(this.dj >>> 13);
      this.dj = -242121447;
      var1[9] = (byte)(this.dj >>> 10);
      this.dj = 532950711;
      var1[10] = (byte)(this.dj >>> 5);
      this.dj = -1457800077;
      var1[11] = (byte)(this.dj >>> 7);
      this.dj = 401682158;
      var1[12] = (byte)(this.dj >>> 8);
      this.dj = 55165540;
      var1[13] = (byte)(this.dj >>> 10);
      this.dj = -5755511;
      var1[14] = (byte)(this.dj >>> 5);
      this.dj = -957679516;
      var1[15] = (byte)(this.dj >>> 17);
      this.dj = 377444131;
      var1[16] = (byte)(this.dj >>> 20);
      this.dj = 527280887;
      var1[17] = (byte)(this.dj >>> 13);
      this.dj = -1117769546;
      var1[18] = (byte)(this.dj >>> 7);
      this.dj = 1726300602;
      var1[19] = (byte)(this.dj >>> 2);
      return new String(var1);
   }
}
