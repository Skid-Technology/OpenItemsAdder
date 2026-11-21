package dev.lone.itemsadder.core;

class EncodedStringY {
   int dj;

   EncodedStringY(UpdateChecker var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[12];
      this.dj = 916565878;
      var1[0] = (byte)(this.dj >>> 20);
      this.dj = -1613203304;
      var1[1] = (byte)(this.dj >>> 14);
      this.dj = 1974381805;
      var1[2] = (byte)(this.dj >>> 1);
      this.dj = 290887875;
      var1[3] = (byte)(this.dj >>> 1);
      this.dj = 1656272918;
      var1[4] = (byte)(this.dj >>> 10);
      this.dj = -1577054770;
      var1[5] = (byte)(this.dj >>> 5);
      this.dj = -149812534;
      var1[6] = (byte)(this.dj >>> 1);
      this.dj = -1495647710;
      var1[7] = (byte)(this.dj >>> 7);
      this.dj = 1394035599;
      var1[8] = (byte)(this.dj >>> 6);
      this.dj = 441804016;
      var1[9] = (byte)(this.dj >>> 16);
      this.dj = 1581075191;
      var1[10] = (byte)(this.dj >>> 10);
      this.dj = 2089308968;
      var1[11] = (byte)(this.dj >>> 6);
      return new String(var1);
   }
}
