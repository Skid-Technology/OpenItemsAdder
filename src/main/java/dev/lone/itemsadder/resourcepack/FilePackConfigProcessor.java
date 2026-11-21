package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.io.File;
import org.jetbrains.annotations.Nullable;

public class FilePackConfigProcessor extends PackConfigProcessor {
   private final File bIu;

   public FilePackConfigProcessor(File var1) {
      super(var1.getAbsolutePath());
      this.bIu = var1;
   }

   @Nullable
   public PackManifest loadManifest() {
      try {
         return this.process(JsonHelper.readJsonFile(this.bIu));
      } catch (Throwable var2) {
         Log.error("Failed to validate model: " + Utils.shortPath(this.bIu));
         Log.error(var2.getMessage());
         return null;
      }
   }
}
