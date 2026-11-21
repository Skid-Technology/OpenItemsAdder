package dev.lone.itemsadder.core;

class EncodedStringG {
   int dj;

   @Override
   public String toString() {
      byte[] var1 = new byte[10];
      this.dj = 1831312202;
      var1[0] = (byte)(this.dj >>> 24);
      this.dj = 1395445431;
      var1[1] = (byte)(this.dj >>> 19);
      this.dj = -1690066039;
      var1[2] = (byte)(this.dj >>> 11);
      this.dj = 341458873;
      var1[3] = (byte)(this.dj >>> 14);
      this.dj = 156121057;
      var1[4] = (byte)(this.dj >>> 7);
      this.dj = 1319313769;
      var1[5] = (byte)(this.dj >>> 11);
      this.dj = 73574876;
      var1[6] = (byte)(this.dj >>> 5);
      this.dj = -814344700;
      var1[7] = (byte)(this.dj >>> 12);
      this.dj = 1556757354;
      var1[8] = (byte)(this.dj >>> 3);
      this.dj = -323948345;
      var1[9] = (byte)(this.dj >>> 21);
      return new String(var1);
   }
}
