package dev.lone.itemsadder.core;

class EncodedStringR {
   int dj;

   EncodedStringR(UpdateChecker var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[9];
      this.dj = -21665862;
      var1[0] = (byte)(this.dj >>> 8);
      this.dj = 935342742;
      var1[1] = (byte)(this.dj >>> 7);
      this.dj = 1775337482;
      var1[2] = (byte)(this.dj >>> 18);
      this.dj = -1417341618;
      var1[3] = (byte)(this.dj >>> 2);
      this.dj = -601806039;
      var1[4] = (byte)(this.dj >>> 3);
      this.dj = 112577190;
      var1[5] = (byte)(this.dj >>> 10);
      this.dj = 993906467;
      var1[6] = (byte)(this.dj >>> 23);
      this.dj = 1497714380;
      var1[7] = (byte)(this.dj >>> 22);
      this.dj = -130478731;
      var1[8] = (byte)(this.dj >>> 15);
      return new String(var1);
   }
}
