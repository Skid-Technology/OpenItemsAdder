package dev.lone.itemsadder.core;

class EncodedStringA {
   int dj;

   @Override
   public String toString() {
      byte[] var1 = new byte[8];
      this.dj = 1244554385;
      var1[0] = (byte)(this.dj >>> 8);
      this.dj = -205376861;
      var1[1] = (byte)(this.dj >>> 7);
      this.dj = -2019713312;
      var1[2] = (byte)(this.dj >>> 14);
      this.dj = -2037359325;
      var1[3] = (byte)(this.dj >>> 9);
      this.dj = -1661506410;
      var1[4] = (byte)(this.dj >>> 8);
      this.dj = -507563469;
      var1[5] = (byte)(this.dj >>> 18);
      this.dj = 732188109;
      var1[6] = (byte)(this.dj >>> 5);
      this.dj = 330455379;
      var1[7] = (byte)(this.dj >>> 6);
      return new String(var1);
   }
}
