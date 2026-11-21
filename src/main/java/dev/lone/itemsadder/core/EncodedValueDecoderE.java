package dev.lone.itemsadder.core;

class EncodedValueDecoderE {
   int dj;

   EncodedValueDecoderE(SettingsListener var1) {
   }

   public Object adV() {
      byte[] var1 = new byte[27];
      this.dj = 97298183;
      var1[0] = (byte)(this.dj >>> 21);
      this.dj = -1142863173;
      var1[1] = (byte)(this.dj >>> 6);
      this.dj = 170703952;
      var1[2] = (byte)(this.dj >>> 1);
      this.dj = 344097945;
      var1[3] = (byte)(this.dj >>> 12);
      this.dj = -951871294;
      var1[4] = (byte)(this.dj >>> 6);
      this.dj = -2129474102;
      var1[5] = (byte)(this.dj >>> 2);
      this.dj = 538462535;
      var1[6] = (byte)(this.dj >>> 14);
      this.dj = 1178465500;
      var1[7] = (byte)(this.dj >>> 20);
      this.dj = -1695754583;
      var1[8] = (byte)(this.dj >>> 22);
      this.dj = 1694584367;
      var1[9] = (byte)(this.dj >>> 11);
      this.dj = -605983156;
      var1[10] = (byte)(this.dj >>> 19);
      this.dj = -2069728810;
      var1[11] = (byte)(this.dj >>> 18);
      this.dj = 1438178755;
      var1[12] = (byte)(this.dj >>> 9);
      this.dj = 1956762926;
      var1[13] = (byte)(this.dj >>> 10);
      this.dj = 1819768309;
      var1[14] = (byte)(this.dj >>> 24);
      this.dj = 596488398;
      var1[15] = (byte)(this.dj >>> 10);
      this.dj = -901545651;
      var1[16] = (byte)(this.dj >>> 3);
      this.dj = 993943568;
      var1[17] = (byte)(this.dj >>> 15);
      this.dj = -905195438;
      var1[18] = (byte)(this.dj >>> 22);
      this.dj = -1923691312;
      var1[19] = (byte)(this.dj >>> 12);
      this.dj = 1915956263;
      var1[20] = (byte)(this.dj >>> 11);
      this.dj = -665871501;
      var1[21] = (byte)(this.dj >>> 22);
      this.dj = -1374103788;
      var1[22] = (byte)(this.dj >>> 9);
      this.dj = 110402347;
      var1[23] = (byte)(this.dj >>> 15);
      this.dj = -1973860186;
      var1[24] = (byte)(this.dj >>> 2);
      this.dj = -1150957960;
      var1[25] = (byte)(this.dj >>> 13);
      this.dj = 1298344466;
      var1[26] = (byte)(this.dj >>> 8);
      return this.method7615(var1);
   }

   Object method7615(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceE(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
