package dev.lone.itemsadder.worldgen;

import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.EventRegistrar;
import java.util.List;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.event.Listener;

public class WorldGenerationManager implements Listener {
   static WorldGenerationListener asW;
   private static boolean asX;

   public static void reload() {
      GH();
      GI();
      jS();
   }

   public static void GH() {
      GJ();

      for (YamlConfig var1 : ContentManager.method8084().method8092()) {
         BlockDecorator.method2318(var1);
         TreeDecorator.method2318(var1);
      }

      asX = true;
   }

   public static void GI() {
      for (YamlConfig var1 : ContentManager.method8084().method8092()) {
         SurfaceFeature.method2318(var1);
         AttachedDecorator.method2318(var1);
      }
   }

   public static void jS() {
      if (!asX) {
         throw new IllegalStateException("World populators were not prepared");
      }

      method1958(BlockPopulator.qj);
      method1958(TreePopulator.qj);
      method1958(SurfacePopulator.qj);
      method1958(CavePopulator.qj);
      asW = new WorldGenerationListener();
      asX = false;
   }

   public static void GJ() {
      Co();
   }

   public static void Co() {
      asX = false;
      if (asW != null) {
         EventRegistrar.unregisterListener(asW);
         asW = null;
      }

      for (BlockPopulator var1 : BlockPopulator.qj) {
         var1.KR();
      }

      BlockPopulator.qj.clear();

      for (TreePopulator var5 : TreePopulator.qj) {
         var5.KR();
      }

      TreePopulator.qj.clear();

      for (SurfacePopulator var6 : SurfacePopulator.qj) {
         var6.KR();
      }

      SurfacePopulator.qj.clear();

      for (CavePopulator var7 : CavePopulator.qj) {
         var7.KR();
      }

      CavePopulator.qj.clear();
      CavePopulator.aEe.clear();
      BlockDecorator.aDN.clear();
      TreeDecorator.aDN.clear();
      SurfaceFeature.aDT.clear();
      AttachedDecorator.aDN.clear();
   }

   static void method1957(List var0) {
      var0.removeIf(var0x -> {
         var0x.KR();
         return true;
      });
   }

   static void method1958(List var0) {
      var0.forEach(var0x -> var0x.start());
   }

   static void method1959(World var0, List var1) {
      var1.stream().filter(var1x -> var0.equals(var1x.ez())).forEach(AbstractPopulator::start);
   }

   static void method1960(World var0, Map var1) {
      var1.forEach((var1x, var2) -> var2.method2315(var0));
   }

   static void method1961(World var0, Map var1) {
      var1.forEach((var1x, var2) -> var2.method2316(var0));
   }

   static void method1962(World var0, List var1) {
      var0.getPopulators().removeIf(var0x -> var0x instanceof AbstractPopulator);
      var1.removeIf(var1x -> {
         if (!var0.equals(var1x.ez())) {
            return false;
         }

         var1x.KR();
         return true;
      });
   }

   public static void method1963(World var0) {
      method1962(var0, BlockPopulator.qj);
      method1962(var0, TreePopulator.qj);
      method1962(var0, SurfacePopulator.qj);
      method1962(var0, CavePopulator.qj);
      CavePopulator.aEe.remove(var0.getUID());
   }
}
