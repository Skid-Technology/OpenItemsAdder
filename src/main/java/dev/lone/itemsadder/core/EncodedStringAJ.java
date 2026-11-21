package dev.lone.itemsadder.core;

class EncodedStringAJ {
   int dj;

   @Override
   public String toString() {
      byte[] var1 = new byte[14];
      this.dj = 1097094725;
      var1[0] = (byte)(this.dj >>> 4);
      this.dj = 1697619257;
      var1[1] = (byte)(this.dj >>> 24);
      this.dj = -1725013055;
      var1[2] = (byte)(this.dj >>> 8);
      this.dj = 875296458;
      var1[3] = (byte)(this.dj >>> 4);
      this.dj = -860682102;
      var1[4] = (byte)(this.dj >>> 11);
      this.dj = -1639502620;
      var1[5] = (byte)(this.dj >>> 12);
      this.dj = 241775858;
      var1[6] = (byte)(this.dj >>> 16);
      this.dj = 1861980950;
      var1[7] = (byte)(this.dj >>> 24);
      this.dj = -883163953;
      var1[8] = (byte)(this.dj >>> 1);
      this.dj = -1580788376;
      var1[9] = (byte)(this.dj >>> 23);
      this.dj = 1301993583;
      var1[10] = (byte)(this.dj >>> 21);
      this.dj = -89169489;
      var1[11] = (byte)(this.dj >>> 8);
      this.dj = -1279837954;
      var1[12] = (byte)(this.dj >>> 12);
      this.dj = 1187105854;
      var1[13] = (byte)(this.dj >>> 10);
      return new String(var1);
   }
}
