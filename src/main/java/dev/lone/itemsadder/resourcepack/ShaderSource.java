package dev.lone.itemsadder.resourcepack;

import java.util.List;

class ShaderSource {
   private final ShaderFileKind bEY;
   private final String bEZ;
   private final String bFa;
   private final List bFb;
   private final boolean bFc;
   private final int bFd;

   ShaderSource(ShaderFileKind var1, String var2, String var3, List var4, boolean var5, int var6) {
      this.bEY = var1;
      this.bEZ = var2;
      this.bFa = var3;
      this.bFb = var4;
      this.bFc = var5;
      this.bFd = var6;
   }

   public ShaderFileKind stage() {
      return this.bEY;
   }

   public String resourcePath() {
      return this.bEZ;
   }

   public String source() {
      return this.bFa;
   }

   public List lines() {
      return this.bFb;
   }

   public boolean complete() {
      return this.bFc;
   }

   public int glslVersion() {
      return this.bFd;
   }
}
