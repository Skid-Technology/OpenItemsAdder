package dev.lone.itemsadder.core;

class EncodedStringC {
   int dj;

   EncodedStringC(UpdateChecker var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[11];
      this.dj = 529960059;
      var1[0] = (byte)(this.dj >>> 12);
      this.dj = 1179889890;
      var1[1] = (byte)(this.dj >>> 7);
      this.dj = 1414766029;
      var1[2] = (byte)(this.dj >>> 2);
      this.dj = -1643527516;
      var1[3] = (byte)(this.dj >>> 13);
      this.dj = -249988104;
      var1[4] = (byte)(this.dj >>> 14);
      this.dj = 1162807556;
      var1[5] = (byte)(this.dj >>> 13);
      this.dj = -990833755;
      var1[6] = (byte)(this.dj >>> 3);
      this.dj = 1225992801;
      var1[7] = (byte)(this.dj >>> 14);
      this.dj = 1291212441;
      var1[8] = (byte)(this.dj >>> 4);
      this.dj = 1188695260;
      var1[9] = (byte)(this.dj >>> 1);
      this.dj = -646224514;
      var1[10] = (byte)(this.dj >>> 8);
      return new String(var1);
   }
}
