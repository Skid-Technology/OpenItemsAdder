package dev.lone.itemsadder.util;

import org.jetbrains.annotations.Nullable;

public class NullableFloatVector3 {
   @Nullable
   Float x;
   @Nullable
   Float y;
   @Nullable
   Float z;

   NullableFloatVector3(@Nullable Float var1, @Nullable Float var2, @Nullable Float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }
}
