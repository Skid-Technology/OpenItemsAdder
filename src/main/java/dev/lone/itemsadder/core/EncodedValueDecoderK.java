package dev.lone.itemsadder.core;

class EncodedValueDecoderK {
   int dj;

   EncodedValueDecoderK(SettingsListener var1) {
   }

   public Object adV() {
      byte[] var1 = new byte[2];
      this.dj = 776763722;
      var1[0] = (byte)(this.dj >>> 14);
      this.dj = 1080802687;
      var1[1] = (byte)(this.dj >>> 17);
      return this.method7632(var1);
   }

   Object method7632(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceK(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
