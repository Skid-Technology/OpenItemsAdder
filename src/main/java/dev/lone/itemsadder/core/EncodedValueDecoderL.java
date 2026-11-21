package dev.lone.itemsadder.core;

class EncodedValueDecoderL {
   int dj;

   EncodedValueDecoderL(SettingsListener var1) {
   }

   public Object adV() {
      byte[] var1 = new byte[1];
      this.dj = 454485339;
      var1[0] = (byte)(this.dj >>> 19);
      return this.method7633(var1);
   }

   Object method7633(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceL(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
