package dev.lone.itemsadder.core;

class EncodedValueDecoderC {
   int dj;

   EncodedValueDecoderC(SettingsListener var1) {
   }

   public Object adV() {
      byte[] var1 = new byte[8];
      this.dj = 1949080691;
      var1[0] = (byte)(this.dj >>> 13);
      this.dj = -228009170;
      var1[1] = (byte)(this.dj >>> 3);
      this.dj = 1738357682;
      var1[2] = (byte)(this.dj >>> 3);
      this.dj = 572045920;
      var1[3] = (byte)(this.dj >>> 10);
      this.dj = -716829853;
      var1[4] = (byte)(this.dj >>> 3);
      this.dj = 1431174683;
      var1[5] = (byte)(this.dj >>> 13);
      this.dj = 1909832547;
      var1[6] = (byte)(this.dj >>> 10);
      this.dj = -106116461;
      var1[7] = (byte)(this.dj >>> 9);
      return this.method7613(var1);
   }

   Object method7613(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceC(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
