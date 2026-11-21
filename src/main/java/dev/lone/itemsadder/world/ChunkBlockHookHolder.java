package dev.lone.itemsadder.world;

import dev.lone.itemsadder.nms.NmsRegistry;
import org.apache.commons.lang.StringUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

public class ChunkBlockHookHolder {
   public final ChunkBlockHook hook = (ChunkBlockHook)NmsRegistry.method2777(ChunkBlockHook.class);
   private static ChunkBlockHookHolder instance;

   ChunkBlockHookHolder() {
   }

   public static ChunkBlockHookHolder getInstance() {
      if (instance == null) {
         instance = new ChunkBlockHookHolder();
      }

      return instance;
   }

   public static boolean isAntiXrayEnabled(World world) {
      return getInstance().hook.isAntiXrayEnabled(world);
   }

   public Object getBiomeKeyAt(Block block) {
      return this.hook.getBiomeKeyAt(block);
   }

   public Object toMinecraftKey(NamespacedKey key) {
      return this.hook.toMinecraftKey(key);
   }

   @Nullable
   public static NamespacedKey parseNamespacedKey(String var0) {
      NamespacedKey var1 = getInstance().hook.parseBiomeKey(var0);
      if (var1 != null) {
         return var1;
      }

      if (var0.contains(":")) {
         String[] var2 = var0.split(":");
         if (var2.length == 2) {
            if (StringUtils.isAllLowerCase(var2[0]) && StringUtils.isAllLowerCase(var2[1])) {
               return new NamespacedKey(var2[0], var2[1]);
            }

            return null;
         }
      }

      return null;
   }
}
