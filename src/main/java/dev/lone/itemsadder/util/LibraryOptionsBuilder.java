package dev.lone.itemsadder.util;

import java.util.Set;
import org.jetbrains.annotations.Nullable;

public class LibraryOptionsBuilder {
   public static final LibraryOptionsBuilder DEFAULT = new LibraryOptionsBuilder();
   private boolean mavenLocal = true;
   private boolean mavenCentral = true;
   private boolean aliyunMirror = true;
   @Nullable
   private Set excludedLibraries = null;

   public LibraryOptionsBuilder mavenLocal(boolean var1) {
      this.mavenLocal = var1;
      return this;
   }

   public LibraryOptionsBuilder mavenCentral(boolean var1) {
      this.mavenCentral = var1;
      return this;
   }

   public LibraryOptionsBuilder aliyunMirror(boolean var1) {
      this.aliyunMirror = var1;
      return this;
   }

   public LibraryOptionsBuilder excludedLibraries(@Nullable Set var1) {
      this.excludedLibraries = var1;
      return this;
   }

   public LibraryOptions build() {
      return new LibraryOptions(this);
   }
}
