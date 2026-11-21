package dev.lone.itemsadder.core;

class EncodedValueDecoderA {
   int dj;

   public Object adV() {
      byte[] var1 = new byte[2];
      this.dj = 1629629486;
      var1[0] = (byte)(this.dj >>> 24);
      this.dj = 120462621;
      var1[1] = (byte)(this.dj >>> 6);
      return this.method7597(var1);
   }

   Object method7597(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceA(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
