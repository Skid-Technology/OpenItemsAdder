package dev.lone.itemsadder.core;

class EncodedValueDecoderJ {
   int dj;

   EncodedValueDecoderJ(SettingsListener var1) {
   }

   public Object adV() {
      byte[] var1 = new byte[1];
      this.dj = 639169990;
      var1[0] = (byte)(this.dj >>> 20);
      return this.method7631(var1);
   }

   Object method7631(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceJ(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
