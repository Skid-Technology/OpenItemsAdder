package dev.lone.itemsadder.resourcepack;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public class PackTemplateGroup {
   private final String texture;
   @Nullable
   private final List colors;

   public PackTemplateGroup(String var1, @Nullable List var2) {
      this.texture = var1;
      this.colors = var2;
   }
}
