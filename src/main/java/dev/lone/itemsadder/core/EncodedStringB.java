package dev.lone.itemsadder.core;

class EncodedStringB {
   int dj;

   EncodedStringB(UpdateChecker var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[10];
      this.dj = 853428666;
      var1[0] = (byte)(this.dj >>> 17);
      this.dj = -644075565;
      var1[1] = (byte)(this.dj >>> 14);
      this.dj = 1823197287;
      var1[2] = (byte)(this.dj >>> 21);
      this.dj = -801989751;
      var1[3] = (byte)(this.dj >>> 6);
      this.dj = -1257596572;
      var1[4] = (byte)(this.dj >>> 11);
      this.dj = -858306757;
      var1[5] = (byte)(this.dj >>> 12);
      this.dj = 661826728;
      var1[6] = (byte)(this.dj >>> 16);
      this.dj = 811984613;
      var1[7] = (byte)(this.dj >>> 16);
      this.dj = 2137481526;
      var1[8] = (byte)(this.dj >>> 8);
      this.dj = 1130994079;
      var1[9] = (byte)(this.dj >>> 19);
      return new String(var1);
   }
}
