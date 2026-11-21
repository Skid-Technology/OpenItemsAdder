package dev.lone.itemsadder.core;

class EncodedValueDecoderN {
   int dj;

   EncodedValueDecoderN(SettingsListener var1) {
   }

   public Object adV() {
      byte[] var1 = new byte[4];
      this.dj = -1422510068;
      var1[0] = (byte)(this.dj >>> 15);
      this.dj = 784021080;
      var1[1] = (byte)(this.dj >>> 4);
      this.dj = 1368965851;
      var1[2] = (byte)(this.dj >>> 9);
      this.dj = 1768544990;
      var1[3] = (byte)(this.dj >>> 6);
      return this.method7635(var1);
   }

   Object method7635(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceN(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
