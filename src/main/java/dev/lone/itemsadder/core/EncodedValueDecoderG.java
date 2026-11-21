package dev.lone.itemsadder.core;

class EncodedValueDecoderG {
   int dj;

   EncodedValueDecoderG(SettingsListener var1) {
   }

   public Object adV() {
      byte[] var1 = new byte[21];
      this.dj = 953147903;
      var1[0] = (byte)(this.dj >>> 14);
      this.dj = -726610005;
      var1[1] = (byte)(this.dj >>> 15);
      this.dj = 984986013;
      var1[2] = (byte)(this.dj >>> 7);
      this.dj = 1780195638;
      var1[3] = (byte)(this.dj >>> 11);
      this.dj = -98907320;
      var1[4] = (byte)(this.dj >>> 3);
      this.dj = -1061413899;
      var1[5] = (byte)(this.dj >>> 6);
      this.dj = 1511754078;
      var1[6] = (byte)(this.dj >>> 14);
      this.dj = 1034820398;
      var1[7] = (byte)(this.dj >>> 24);
      this.dj = -77078718;
      var1[8] = (byte)(this.dj >>> 16);
      this.dj = 213347120;
      var1[9] = (byte)(this.dj >>> 21);
      this.dj = -1365420156;
      var1[10] = (byte)(this.dj >>> 21);
      this.dj = 1665233830;
      var1[11] = (byte)(this.dj >>> 16);
      this.dj = -1836722278;
      var1[12] = (byte)(this.dj >>> 10);
      this.dj = 1946599088;
      var1[13] = (byte)(this.dj >>> 24);
      this.dj = 1420308898;
      var1[14] = (byte)(this.dj >>> 2);
      this.dj = -838933969;
      var1[15] = (byte)(this.dj >>> 21);
      this.dj = -1625508016;
      var1[16] = (byte)(this.dj >>> 14);
      this.dj = -1858703648;
      var1[17] = (byte)(this.dj >>> 19);
      this.dj = 918801229;
      var1[18] = (byte)(this.dj >>> 3);
      this.dj = 307440780;
      var1[19] = (byte)(this.dj >>> 5);
      this.dj = -472147711;
      var1[20] = (byte)(this.dj >>> 20);
      return this.method7628(var1);
   }

   Object method7628(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceG(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
