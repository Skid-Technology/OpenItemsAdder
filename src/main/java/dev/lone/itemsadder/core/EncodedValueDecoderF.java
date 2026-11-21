package dev.lone.itemsadder.core;

class EncodedValueDecoderF {
   int dj;

   EncodedValueDecoderF(SettingsListener var1) {
   }

   public Object adV() {
      byte[] var1 = new byte[11];
      this.dj = -1429607334;
      var1[0] = (byte)(this.dj >>> 17);
      this.dj = -1259869261;
      var1[1] = (byte)(this.dj >>> 23);
      this.dj = -1941493488;
      var1[2] = (byte)(this.dj >>> 12);
      this.dj = 1124281151;
      var1[3] = (byte)(this.dj >>> 11);
      this.dj = 895737314;
      var1[4] = (byte)(this.dj >>> 16);
      this.dj = 1562588066;
      var1[5] = (byte)(this.dj >>> 22);
      this.dj = 385573468;
      var1[6] = (byte)(this.dj >>> 11);
      this.dj = 706844609;
      var1[7] = (byte)(this.dj >>> 10);
      this.dj = -1290244354;
      var1[8] = (byte)(this.dj >>> 14);
      this.dj = 1808012385;
      var1[9] = (byte)(this.dj >>> 24);
      this.dj = 1977037342;
      var1[10] = (byte)(this.dj >>> 12);
      return this.method7627(var1);
   }

   Object method7627(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceF(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
