package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

class PackBuildContext {
   private final ResourcePackGenerator byP;
   private final Path byQ;
   private final String byR;
   private final List byS;
   private final List byT;
   private final List byU;
   private final File byV;
   private final boolean byW;
   private final PackEntryGroups byX;

   PackBuildContext(ResourcePackGenerator var1, Path var2, String var3, List var4, List var5, List var6, File var7, boolean var8, PackEntryGroups var9) {
      this.byP = var1;
      this.byQ = var2;
      this.byR = var3;
      this.byS = var4;
      this.byT = var5;
      this.byU = var6;
      this.byV = var7;
      this.byW = var8;
      this.byX = var9;
   }

   static PackBuildContext method126(ContentManager var0, ResourcePackGenerator var1) {
      return new PackBuildContext(
         var1,
         Main.getInstance().getDataFolder().toPath(),
         ResourcePackWriter.minecraftVersion(),
         var0.field87.stream().map(var0x -> var0x.toPath().resolve("minecraft/atlases")).toList(),
         List.copyOf(var0.field88),
         List.copyOf(ContentManager.method8203()),
         ContentManager.method8193("pack.png"),
         Main.config.getBoolean("resource_pack.font_images.optimize_using_sheets"),
         PackEntryGroups.VN()
      );
   }

   public ResourcePackGenerator Vx() {
      return this.byP;
   }

   public Path Vy() {
      return this.byQ;
   }

   public String minecraftVersion() {
      return this.byR;
   }

   public List VG() {
      return this.byS;
   }

   public List VH() {
      return this.byT;
   }

   public List VI() {
      return this.byU;
   }

   public File VJ() {
      return this.byV;
   }

   public boolean VK() {
      return this.byW;
   }

   public PackEntryGroups VL() {
      return this.byX;
   }
}
