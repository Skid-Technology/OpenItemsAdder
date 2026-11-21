package dev.lone.itemsadder.config;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PackVersionInfo {
   private final String version;
   private final String aGE;
   private final long aGF;
   private final int aGG;
   private final int aGH;

   public PackVersionInfo(String var1, String var2, long var3, int var5, int var6) {
      this.version = var1;
      this.aGE = var2;
      this.aGF = var3;
      this.aGG = var5;
      this.aGH = var6;
   }

   public String Lk() {
      return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(this.aGF * 1000L));
   }

   public String Ll() {
      return this.aGE;
   }

   public long KD() {
      return this.aGF;
   }

   public int Lm() {
      return this.aGG;
   }

   public int Ln() {
      return this.aGH;
   }
}
