package dev.lone.itemsadder.core;

class EncodedStringT {
   int dj;

   EncodedStringT(UpdateChecker var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[13];
      this.dj = -916903667;
      var1[0] = (byte)(this.dj >>> 14);
      this.dj = 139965626;
      var1[1] = (byte)(this.dj >>> 7);
      this.dj = -1779115802;
      var1[2] = (byte)(this.dj >>> 1);
      this.dj = 271266181;
      var1[3] = (byte)(this.dj >>> 2);
      this.dj = 1656861403;
      var1[4] = (byte)(this.dj >>> 24);
      this.dj = 365795156;
      var1[5] = (byte)(this.dj >>> 13);
      this.dj = 424289401;
      var1[6] = (byte)(this.dj >>> 22);
      this.dj = -635278015;
      var1[7] = (byte)(this.dj >>> 2);
      this.dj = 1374869702;
      var1[8] = (byte)(this.dj >>> 9);
      this.dj = -1002919465;
      var1[9] = (byte)(this.dj >>> 2);
      this.dj = -1444515113;
      var1[10] = (byte)(this.dj >>> 12);
      this.dj = 834065406;
      var1[11] = (byte)(this.dj >>> 9);
      this.dj = -283698758;
      var1[12] = (byte)(this.dj >>> 2);
      return new String(var1);
   }
}
