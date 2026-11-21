package dev.lone.itemsadder.world;

import dev.lone.itemsadder.nms.NmsRegistry;
import dev.lone.itemsadder.util.Log;
import java.util.concurrent.atomic.AtomicBoolean;

public class TreeFeatureHookHolder {
   private final TreeFeatureHook hook = (TreeFeatureHook)NmsRegistry.method2777(TreeFeatureHook.class);
   public static TreeFeatureHookHolder instance;
   public static final AtomicBoolean featuresInjected = new AtomicBoolean();

   TreeFeatureHookHolder() {
   }

   private static TreeFeatureHookHolder getInstance() {
      if (instance == null) {
         instance = new TreeFeatureHookHolder();
      }

      return instance;
   }

   public static TreeFeatureHook getHook() {
      return getInstance().hook;
   }

   public static void restoreDefaultBlockstateFeatures() {
      try {
         if (instance != null) {
            instance.hook.restoreDefaultBlockstateFeatures();
         }
      } catch (Throwable var4) {
         Log.warn("Failed to restore force-default-blockstate tree features: " + var4.getMessage());
      } finally {
         featuresInjected.set(false);
         instance = null;
      }
   }
}
