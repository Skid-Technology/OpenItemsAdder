package dev.lone.itemsadder.core;

class EncodedValueDecoderH {
   int dj;

   EncodedValueDecoderH(SettingsListener var1) {
   }

   public Object adV() {
      byte[] var1 = new byte[16];
      this.dj = 2081153767;
      var1[0] = (byte)(this.dj >>> 4);
      this.dj = -1585393086;
      var1[1] = (byte)(this.dj >>> 9);
      this.dj = 1637688185;
      var1[2] = (byte)(this.dj >>> 14);
      this.dj = 1254768252;
      var1[3] = (byte)(this.dj >>> 17);
      this.dj = 812505258;
      var1[4] = (byte)(this.dj >>> 16);
      this.dj = 1301654988;
      var1[5] = (byte)(this.dj >>> 2);
      this.dj = -1508026211;
      var1[6] = (byte)(this.dj >>> 20);
      this.dj = -14492893;
      var1[7] = (byte)(this.dj >>> 3);
      this.dj = -1607708984;
      var1[8] = (byte)(this.dj >>> 1);
      this.dj = -707684914;
      var1[9] = (byte)(this.dj >>> 10);
      this.dj = 1914225172;
      var1[10] = (byte)(this.dj >>> 24);
      this.dj = 390071792;
      var1[11] = (byte)(this.dj >>> 23);
      this.dj = -98137516;
      var1[12] = (byte)(this.dj >>> 15);
      this.dj = -801012291;
      var1[13] = (byte)(this.dj >>> 10);
      this.dj = 155405368;
      var1[14] = (byte)(this.dj >>> 11);
      this.dj = 1847313984;
      var1[15] = (byte)(this.dj >>> 24);
      return this.method7629(var1);
   }

   Object method7629(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceH(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
