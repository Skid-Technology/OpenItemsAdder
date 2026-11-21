package dev.lone.itemsadder.core;

class EncodedStringAF {
   int dj;

   @Override
   public String toString() {
      byte[] var1 = new byte[8];
      this.dj = -1290168256;
      var1[0] = (byte)(this.dj >>> 23);
      this.dj = 882585603;
      var1[1] = (byte)(this.dj >>> 23);
      this.dj = -1759813049;
      var1[2] = (byte)(this.dj >>> 11);
      this.dj = -1056756502;
      var1[3] = (byte)(this.dj >>> 11);
      this.dj = 221848139;
      var1[4] = (byte)(this.dj >>> 18);
      this.dj = -423457882;
      var1[5] = (byte)(this.dj >>> 17);
      this.dj = -615199218;
      var1[6] = (byte)(this.dj >>> 22);
      this.dj = 3642159;
      var1[7] = (byte)(this.dj >>> 3);
      return new String(var1);
   }
}
