package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.Utils;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class PackEntryGroups {
   private final List byY;
   private final List byZ;

   private PackEntryGroups(List var1, List var2) {
      this.byY = var1;
      this.byZ = var2;
   }

   private static PackEntryGroups VN() {
      if (Main.getInstance() == null) {
         return new PackEntryGroups(List.of(), List.of());
      }

      try {
         Path var0 = ResourcePackWriter.realPathOrAbsolute(Main.getInstance().getDataFolder().toPath());
         List var1 = List.of(
            ResourcePackWriter.realPathOrAbsolute(var0.resolve("contents")),
            ResourcePackWriter.realPathOrAbsolute(var0.resolve("data/resource_pack")),
            ResourcePackWriter.realPathOrAbsolute(var0.resolve("data/items_packs"))
         );
         ArrayList var2 = new ArrayList();
         Path var3 = var0.getParent();
         if (var3 != null && Settings.get().mergedResourcepackFolders != null) {
            for (String var5 : Settings.get().mergedResourcepackFolders) {
               String var6 = Utils.removePrefix(var5, "plugins/");
               var2.add(ResourcePackWriter.realPathOrAbsolute(var3.resolve(var6)));
            }
         }

         return new PackEntryGroups(var1, List.copyOf(var2));
      } catch (RuntimeException var7) {
         return new PackEntryGroups(List.of(), List.of());
      }
   }

   public List VO() {
      return this.byY;
   }

   public List VP() {
      return this.byZ;
   }
}
