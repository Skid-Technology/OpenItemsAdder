package dev.lone.itemsadder.resourcepack;

import org.jetbrains.annotations.Nullable;

public class PackOverrideEntry {
   private final String texture;
   private final boolean dyeable;
   @Nullable
   private final Integer undyed;

   public PackOverrideEntry(String var1, boolean var2, @Nullable Integer var3) {
      this.texture = var1;
      this.dyeable = var2;
      this.undyed = var3;
   }
}
