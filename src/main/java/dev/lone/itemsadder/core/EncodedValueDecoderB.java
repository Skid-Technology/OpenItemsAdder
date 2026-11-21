package dev.lone.itemsadder.core;

class EncodedValueDecoderB {
   int dj;

   public Object adV() {
      byte[] var1 = new byte[6];
      this.dj = -631495494;
      var1[0] = (byte)(this.dj >>> 2);
      this.dj = -2087568612;
      var1[1] = (byte)(this.dj >>> 3);
      this.dj = 906564197;
      var1[2] = (byte)(this.dj >>> 23);
      this.dj = 1971767795;
      var1[3] = (byte)(this.dj >>> 18);
      this.dj = -946947601;
      var1[4] = (byte)(this.dj >>> 7);
      this.dj = 463140254;
      var1[5] = (byte)(this.dj >>> 19);
      return this.method7598(var1);
   }

   Object method7598(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceB(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
