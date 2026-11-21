package dev.lone.itemsadder.core;

class EncodedValueDecoderI {
   int dj;

   EncodedValueDecoderI(SettingsListener var1) {
   }

   public Object adV() {
      byte[] var1 = new byte[11];
      this.dj = 718810521;
      var1[0] = (byte)(this.dj >>> 5);
      this.dj = 377704367;
      var1[1] = (byte)(this.dj >>> 11);
      this.dj = 746362119;
      var1[2] = (byte)(this.dj >>> 21);
      this.dj = -1984776749;
      var1[3] = (byte)(this.dj >>> 15);
      this.dj = 818788468;
      var1[4] = (byte)(this.dj >>> 10);
      this.dj = -680275718;
      var1[5] = (byte)(this.dj >>> 16);
      this.dj = 1213816539;
      var1[6] = (byte)(this.dj >>> 14);
      this.dj = 1254750696;
      var1[7] = (byte)(this.dj >>> 17);
      this.dj = 2135738369;
      var1[8] = (byte)(this.dj >>> 6);
      this.dj = 1932431452;
      var1[9] = (byte)(this.dj >>> 13);
      this.dj = -688328350;
      var1[10] = (byte)(this.dj >>> 20);
      return this.method7630(var1);
   }

   Object method7630(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceI(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
