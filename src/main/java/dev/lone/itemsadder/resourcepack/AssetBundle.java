package dev.lone.itemsadder.resourcepack;

import java.nio.file.Path;
import java.util.Map;

public class AssetBundle {
   private final Path axH;
   private final String axI;
   private final Map axJ;
   private final Map axK;
   private final Map axL;
   private final Map axM;

   public AssetBundle(Path var1, String var2, Map var3, Map var4, Map var5, Map var6) {
      this.axH = var1;
      this.axI = var2;
      this.axJ = var3;
      this.axK = var4;
      this.axL = var5;
      this.axM = var6;
   }

   public Path source() {
      return this.axH;
   }

   public String root() {
      return this.axI;
   }

   public Map textures() {
      return this.axJ;
   }

   public Map frames() {
      return this.axK;
   }

   public Map definitions() {
      return this.axL;
   }

   public Map displays() {
      return this.axM;
   }
}
