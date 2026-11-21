package dev.lone.itemsadder.metrics;

class ActivationFieldProbe {
   int dj;

   ActivationFieldProbe(MetricsManager var1) {
   }

   @Override
   public String toString() {
      byte[] var1 = new byte[1];
      this.dj = -904355031;
      var1[0] = (byte)(this.dj >>> 14);
      return new String(var1);
   }
}
