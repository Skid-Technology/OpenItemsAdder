package dev.lone.itemsadder.util;

import java.util.Set;
import org.jetbrains.annotations.Nullable;

public class LibraryOptions {
   public static final LibraryOptions DEFAULT = LibraryOptionsBuilder.DEFAULT.build();
   public final boolean mavenLocal;
   public final boolean mavenCentral;
   public final boolean aliyunMirror;
   @Nullable
   public Set excludedLibraries;

   private LibraryOptions(LibraryOptionsBuilder var1) {
      this.mavenLocal = var1.mavenLocal;
      this.mavenCentral = var1.mavenCentral;
      this.aliyunMirror = var1.aliyunMirror;
      this.excludedLibraries = var1.excludedLibraries;
   }

   public static LibraryOptionsBuilder toBuilder() {
      return new LibraryOptionsBuilder();
   }
}
