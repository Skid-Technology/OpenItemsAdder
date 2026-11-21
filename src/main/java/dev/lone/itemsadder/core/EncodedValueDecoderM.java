package dev.lone.itemsadder.core;

class EncodedValueDecoderM {
   int dj;

   EncodedValueDecoderM(SettingsListener var1) {
   }

   public Object adV() {
      byte[] var1 = new byte[31];
      this.dj = 1557482475;
      var1[0] = (byte)(this.dj >>> 22);
      this.dj = 487017010;
      var1[1] = (byte)(this.dj >>> 22);
      this.dj = 928765869;
      var1[2] = (byte)(this.dj >>> 14);
      this.dj = -506907982;
      var1[3] = (byte)(this.dj >>> 18);
      this.dj = 1637993511;
      var1[4] = (byte)(this.dj >>> 24);
      this.dj = -1638478098;
      var1[5] = (byte)(this.dj >>> 9);
      this.dj = 1509495598;
      var1[6] = (byte)(this.dj >>> 3);
      this.dj = 1332949087;
      var1[7] = (byte)(this.dj >>> 1);
      this.dj = -1868984491;
      var1[8] = (byte)(this.dj >>> 10);
      this.dj = -623016430;
      var1[9] = (byte)(this.dj >>> 10);
      this.dj = -708431361;
      var1[10] = (byte)(this.dj >>> 7);
      this.dj = -1072889424;
      var1[11] = (byte)(this.dj >>> 13);
      this.dj = -40573294;
      var1[12] = (byte)(this.dj >>> 18);
      this.dj = -1210294529;
      var1[13] = (byte)(this.dj >>> 9);
      this.dj = -1153776507;
      var1[14] = (byte)(this.dj >>> 23);
      this.dj = 2093090935;
      var1[15] = (byte)(this.dj >>> 17);
      this.dj = -1993939652;
      var1[16] = (byte)(this.dj >>> 9);
      this.dj = -315129608;
      var1[17] = (byte)(this.dj >>> 21);
      this.dj = 469553417;
      var1[18] = (byte)(this.dj >>> 22);
      this.dj = -1670946838;
      var1[19] = (byte)(this.dj >>> 12);
      this.dj = 778417894;
      var1[20] = (byte)(this.dj >>> 1);
      this.dj = 964630438;
      var1[21] = (byte)(this.dj >>> 19);
      this.dj = 2137446593;
      var1[22] = (byte)(this.dj >>> 6);
      this.dj = 1984271892;
      var1[23] = (byte)(this.dj >>> 4);
      this.dj = -1311181145;
      var1[24] = (byte)(this.dj >>> 14);
      this.dj = 1260006292;
      var1[25] = (byte)(this.dj >>> 14);
      this.dj = 515042021;
      var1[26] = (byte)(this.dj >>> 15);
      this.dj = 331727905;
      var1[27] = (byte)(this.dj >>> 13);
      this.dj = 367434683;
      var1[28] = (byte)(this.dj >>> 6);
      this.dj = 1050193587;
      var1[29] = (byte)(this.dj >>> 14);
      this.dj = -432582734;
      var1[30] = (byte)(this.dj >>> 12);
      return this.method7634(var1);
   }

   Object method7634(byte[] var1) {
      try {
         Class var2 = Class.forName((String)new EncodedValueSourceM(this).adV());
         return var2.getConstructor(byte[].class).newInstance(var1);
      } catch (Throwable var3) {
         return null;
      }
   }
}
