package dev.lone.itemsadder.resourcepack;

import com.google.gson.JsonObject;

class CachedAtlasEntry {
   private final byte[] bFW;
   private final JsonObject bFX;
   private final int bFY;
   private final int bFZ;
   private final long bGa;
   private final long bGb;
   private final boolean bGc;

   CachedAtlasEntry(byte[] var1, JsonObject var2, int var3, int var4, long var5, long var7, boolean var9) {
      this.bFW = var1;
      this.bFX = var2;
      this.bFY = var3;
      this.bFZ = var4;
      this.bGa = var5;
      this.bGb = var7;
      this.bGc = var9;
   }

   public byte[] png() {
      return this.bFW;
   }

   public JsonObject metadata() {
      return this.bFX;
   }

   public int originalFrames() {
      return this.bFY;
   }

   public int uniqueFrames() {
      return this.bFZ;
   }

   public long originalPixels() {
      return this.bGa;
   }

   public long outputPixels() {
      return this.bGb;
   }

   public boolean solidified() {
      return this.bGc;
   }
}
