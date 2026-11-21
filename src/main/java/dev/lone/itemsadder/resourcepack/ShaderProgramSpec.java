package dev.lone.itemsadder.resourcepack;

import java.util.List;
import java.util.Set;

class ShaderProgramSpec {
   private final List bFs;
   private final List bFt;
   private final boolean bFu;
   private final int bFv;
   private final int bFw;
   private final Set bFx;
   private final List bFy;

   private ShaderProgramSpec(List var1, List var2, boolean var3, int var4, int var5, Set var6, List var7) {
      this.bFs = var1;
      this.bFt = var2;
      this.bFu = var3;
      this.bFv = var4;
      this.bFw = var5;
      this.bFx = var6;
      this.bFy = var7;
   }

   public List diagnostics() {
      return this.bFs;
   }

   public List imports() {
      return this.bFt;
   }

   public boolean hasMain() {
      return this.bFu;
   }

   public int versions() {
      return this.bFv;
   }

   public int glslVersion() {
      return this.bFw;
   }

   public Set versionLines() {
      return this.bFx;
   }

   public List tokens() {
      return this.bFy;
   }
}
