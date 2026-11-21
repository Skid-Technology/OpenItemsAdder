package dev.lone.itemsadder.core;

import dev.lone.itemsadder.util.ReflectiveValueHolder;

public class EncodedStringAC {
   int dj;

   EncodedStringAC(ReflectiveValueHolder var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[14];
      this.dj = -522547080;
      var1[0] = (byte)(this.dj >>> 14);
      this.dj = -1017002411;
      var1[1] = (byte)(this.dj >>> 16);
      this.dj = 1128651603;
      var1[2] = (byte)(this.dj >>> 10);
      this.dj = 1393270789;
      var1[3] = (byte)(this.dj >>> 19);
      this.dj = 788364189;
      var1[4] = (byte)(this.dj >>> 24);
      this.dj = -1241402234;
      var1[5] = (byte)(this.dj >>> 23);
      this.dj = -1511149398;
      var1[6] = (byte)(this.dj >>> 7);
      this.dj = -1751717338;
      var1[7] = (byte)(this.dj >>> 12);
      this.dj = -849162436;
      var1[8] = (byte)(this.dj >>> 9);
      this.dj = -1481065793;
      var1[9] = (byte)(this.dj >>> 10);
      this.dj = 252000077;
      var1[10] = (byte)(this.dj >>> 10);
      this.dj = 1213680695;
      var1[11] = (byte)(this.dj >>> 5);
      this.dj = 1827224075;
      var1[12] = (byte)(this.dj >>> 7);
      this.dj = -1747118384;
      var1[13] = (byte)(this.dj >>> 1);
      return new String(var1);
   }
}
