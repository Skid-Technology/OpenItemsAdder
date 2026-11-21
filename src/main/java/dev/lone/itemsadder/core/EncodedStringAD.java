package dev.lone.itemsadder.core;

import dev.lone.itemsadder.util.ReflectiveValueHolder;

public class EncodedStringAD {
   int dj;

   EncodedStringAD(ReflectiveValueHolder var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[6];
      this.dj = -222749479;
      var1[0] = (byte)(this.dj >>> 15);
      this.dj = 467161821;
      var1[1] = (byte)(this.dj >>> 14);
      this.dj = -71289015;
      var1[2] = (byte)(this.dj >>> 7);
      this.dj = -756342161;
      var1[3] = (byte)(this.dj >>> 11);
      this.dj = -1399880833;
      var1[4] = (byte)(this.dj >>> 3);
      this.dj = -1261848857;
      var1[5] = (byte)(this.dj >>> 7);
      return new String(var1);
   }
}
