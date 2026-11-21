package dev.lone.itemsadder.core;

class EncodedStringJ {
   int dj;

   @Override
   public String toString() {
      byte[] var1 = new byte[3];
      this.dj = -684355423;
      var1[0] = (byte)(this.dj >>> 10);
      this.dj = -740772264;
      var1[1] = (byte)(this.dj >>> 4);
      this.dj = -266487770;
      var1[2] = (byte)(this.dj >>> 14);
      return new String(var1);
   }
}
