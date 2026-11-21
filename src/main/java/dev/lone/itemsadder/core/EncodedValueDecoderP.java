package dev.lone.itemsadder.core;

class EncodedValueDecoderP {
   int dj;

   public Object adV() {
      byte[] var1 = new byte[2];
      this.dj = 1629629486;
      var1[0] = (byte)(this.dj >>> 24);
      this.dj = 120462621;
      var1[1] = (byte)(this.dj >>> 6);
      return this.method7637(var1);
   }

   Object method7637(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceP(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
