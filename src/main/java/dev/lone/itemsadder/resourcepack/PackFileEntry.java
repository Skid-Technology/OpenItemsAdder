package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.util.Utils;
import java.io.File;
import lombok.Generated;

public class PackFileEntry {
   public final String bIF;
   public String bwQ;

   public PackFileEntry(String var1, String var2) {
      if (var2.contains("#")) {
         throw new IllegalArgumentException("Special references are not accepted! '" + var2 + "'");
      }

      this.bwQ = var2;
      this.bIF = Utils.normalizeSlashes(var1);
   }

   public PackFileEntry(File var1, String var2) {
      this(var1.getAbsolutePath(), var2);
   }

   public PackFileEntry copy() {
      return new PackFileEntry(this.bIF, this.bwQ);
   }

   @Override
   public int hashCode() {
      return this.bwQ.hashCode();
   }

   public File toFile() {
      return new File(this.bIF);
   }

   public String getNamespace() {
      String[] var1 = this.bwQ.split(":");
      return var1.length == 1 ? "minecraft" : var1[0];
   }

   public String getName() {
      String[] var1 = this.bwQ.split(":");
      return var1.length == 1 ? this.bwQ : var1[1];
   }

   @Override
   public String toString() {
      return this.bIF + " | " + this.bwQ;
   }

   public String getTexturePath() {
      return "assets/" + this.getNamespace() + "/textures/" + this.getName() + ".png";
   }

   public boolean isSpecial() {
      return isSpecialRef(this.bwQ);
   }

   public static boolean isSpecialRef(String var0) {
      return var0.startsWith("#");
   }

   @Generated
   public String getRef() {
      return this.bwQ;
   }

   @Generated
   public void setRef(String var1) {
      this.bwQ = var1;
   }
}
