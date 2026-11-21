package dev.lone.itemsadder.resourcepack;

import org.jetbrains.annotations.Nullable;

public class PackTextureRef {
   @Nullable
   private final String material;
   @Nullable
   private final String pattern;
   @Nullable
   private final String texture;
   @Nullable
   private final String palette;

   public PackTextureRef(@Nullable String var1, @Nullable String var2, @Nullable String var3, @Nullable String var4) {
      this.material = var1;
      this.pattern = var2;
      this.texture = var3;
      this.palette = var4;
   }
}
