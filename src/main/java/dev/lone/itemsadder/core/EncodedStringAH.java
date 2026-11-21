package dev.lone.itemsadder.core;

class EncodedStringAH {
   int dj;

   @Override
   public String toString() {
      byte[] var1 = new byte[11];
      this.dj = -1972386269;
      var1[0] = (byte)(this.dj >>> 16);
      this.dj = -1770872716;
      var1[1] = (byte)(this.dj >>> 16);
      this.dj = -1494564658;
      var1[2] = (byte)(this.dj >>> 1);
      this.dj = -880585325;
      var1[3] = (byte)(this.dj >>> 22);
      this.dj = -756294894;
      var1[4] = (byte)(this.dj >>> 3);
      this.dj = -423329321;
      var1[5] = (byte)(this.dj >>> 2);
      this.dj = 735763306;
      var1[6] = (byte)(this.dj >>> 14);
      this.dj = 978694128;
      var1[7] = (byte)(this.dj >>> 10);
      this.dj = -1613076144;
      var1[8] = (byte)(this.dj >>> 14);
      this.dj = 976743105;
      var1[9] = (byte)(this.dj >>> 23);
      this.dj = -1265860422;
      var1[10] = (byte)(this.dj >>> 2);
      return new String(var1);
   }
}
