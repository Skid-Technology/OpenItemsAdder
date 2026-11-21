package dev.lone.itemsadder.core;

class EncodedValueDecoderO {
   int dj;

   EncodedValueDecoderO(SettingsListener var1) {
   }

   public Object adV() {
      byte[] var1 = new byte[10];
      this.dj = 226822810;
      var1[0] = (byte)(this.dj >>> 1);
      this.dj = -2010282492;
      var1[1] = (byte)(this.dj >>> 10);
      this.dj = -630336681;
      var1[2] = (byte)(this.dj >>> 22);
      this.dj = -1569825295;
      var1[3] = (byte)(this.dj >>> 16);
      this.dj = 1797112550;
      var1[4] = (byte)(this.dj >>> 4);
      this.dj = -1874671630;
      var1[5] = (byte)(this.dj >>> 9);
      this.dj = 711749029;
      var1[6] = (byte)(this.dj >>> 16);
      this.dj = 211706342;
      var1[7] = (byte)(this.dj >>> 8);
      this.dj = 1779525071;
      var1[8] = (byte)(this.dj >>> 2);
      this.dj = 1805346218;
      var1[9] = (byte)(this.dj >>> 19);
      return this.method7636(var1);
   }

   Object method7636(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceO(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
