package dev.lone.itemsadder.core;

class EncodedValueDecoderD {
   int dj;

   EncodedValueDecoderD(SettingsListener var1) {
   }

   public Object adV() {
      byte[] var1 = new byte[14];
      this.dj = 1906836120;
      var1[0] = (byte)(this.dj >>> 3);
      this.dj = 1544779873;
      var1[1] = (byte)(this.dj >>> 22);
      this.dj = -245648686;
      var1[2] = (byte)(this.dj >>> 1);
      this.dj = -442074179;
      var1[3] = (byte)(this.dj >>> 12);
      this.dj = -1394629013;
      var1[4] = (byte)(this.dj >>> 17);
      this.dj = 370623684;
      var1[5] = (byte)(this.dj >>> 12);
      this.dj = -89564219;
      var1[6] = (byte)(this.dj >>> 19);
      this.dj = -2052474105;
      var1[7] = (byte)(this.dj >>> 10);
      this.dj = 1827960951;
      var1[8] = (byte)(this.dj >>> 24);
      this.dj = -425234542;
      var1[9] = (byte)(this.dj >>> 8);
      this.dj = -1351447439;
      var1[10] = (byte)(this.dj >>> 5);
      this.dj = -1766666895;
      var1[11] = (byte)(this.dj >>> 20);
      this.dj = 1835380541;
      var1[12] = (byte)(this.dj >>> 16);
      this.dj = -1826398346;
      var1[13] = (byte)(this.dj >>> 19);
      return this.method7614(var1);
   }

   Object method7614(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceD(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
