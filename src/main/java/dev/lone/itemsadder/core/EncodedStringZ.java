package dev.lone.itemsadder.core;

class EncodedStringZ {
   int dj;

   EncodedStringZ(UpdateChecker var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[17];
      this.dj = 648534103;
      var1[0] = (byte)(this.dj >>> 20);
      this.dj = 1642096153;
      var1[1] = (byte)(this.dj >>> 4);
      this.dj = 377205584;
      var1[2] = (byte)(this.dj >>> 11);
      this.dj = 281280237;
      var1[3] = (byte)(this.dj >>> 17);
      this.dj = -1568794256;
      var1[4] = (byte)(this.dj >>> 3);
      this.dj = -444211897;
      var1[5] = (byte)(this.dj >>> 6);
      this.dj = -1541382907;
      var1[6] = (byte)(this.dj >>> 6);
      this.dj = 436640220;
      var1[7] = (byte)(this.dj >>> 12);
      this.dj = -1663511283;
      var1[8] = (byte)(this.dj >>> 17);
      this.dj = 1154550692;
      var1[9] = (byte)(this.dj >>> 6);
      this.dj = 1423789329;
      var1[10] = (byte)(this.dj >>> 10);
      this.dj = -81873967;
      var1[11] = (byte)(this.dj >>> 19);
      this.dj = 204815842;
      var1[12] = (byte)(this.dj >>> 21);
      this.dj = -873407326;
      var1[13] = (byte)(this.dj >>> 9);
      this.dj = 433822274;
      var1[14] = (byte)(this.dj >>> 14);
      this.dj = -698409352;
      var1[15] = (byte)(this.dj >>> 20);
      this.dj = 1927437078;
      var1[16] = (byte)(this.dj >>> 24);
      return new String(var1);
   }
}
