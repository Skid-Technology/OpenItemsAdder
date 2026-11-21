package dev.lone.itemsadder.core;

class EncodedStringAE {
   int dj;

   @Override
   public String toString() {
      byte[] var1 = new byte[10];
      this.dj = 353154566;
      var1[0] = (byte)(this.dj >>> 7);
      this.dj = 2032259494;
      var1[1] = (byte)(this.dj >>> 2);
      this.dj = -1554058824;
      var1[2] = (byte)(this.dj >>> 2);
      this.dj = -763521414;
      var1[3] = (byte)(this.dj >>> 10);
      this.dj = -1627526856;
      var1[4] = (byte)(this.dj >>> 2);
      this.dj = -1364596157;
      var1[5] = (byte)(this.dj >>> 21);
      this.dj = 1572238111;
      var1[6] = (byte)(this.dj >>> 18);
      this.dj = 784604510;
      var1[7] = (byte)(this.dj >>> 17);
      this.dj = -1604953452;
      var1[8] = (byte)(this.dj >>> 12);
      this.dj = -2038531637;
      var1[9] = (byte)(this.dj >>> 2);
      return new String(var1);
   }
}
