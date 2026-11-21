package dev.lone.itemsadder.util;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.resourcepack.AssetFolders;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

public final class FileListHelper {
   private FileListHelper() {
   }

   static List collectAssetRoots(String var0, String var1, String var2) {
      ArrayList var3 = new ArrayList();
      List var4 = listContentFolders(var0, var1);
      if (Settings.get().mergedResourcepackFolders != null && !Settings.get().mergedResourcepackFolders.isEmpty()) {
         File var5 = new File(Main.getPlugin().getDataFolder().getParentFile().getAbsolutePath());

         for (String var7 : Settings.get().mergedResourcepackFolders) {
            var7 = Utils.removePrefix(var7, "plugins/");
            File var8 = new File(var5, var7);
            if (var8.exists()) {
               var4.add(var8);
            }
         }
      }

      for (File var12 : var4) {
         if (Utils.countChildren(var12) > 0) {
            File var14 = new File(var12, "resourcepack/assets/");
            if (!var14.exists()) {
               var14 = new File(var12, "resourcepack/");
            }

            if (!var14.exists()) {
               var14 = new File(var12, "resource_pack/assets/");
            }

            if (!var14.exists()) {
               var14 = new File(var12, "resource_pack/");
            }

            if (!var14.exists()) {
               var14 = new File(var12, "data/resource_pack/assets/");
            }

            if (!var14.exists()) {
               var14 = new File(var12, "data/resource_pack/");
            }

            if (!var14.exists()) {
               var14 = new File(var12, "assets/");
            }

            if (!var14.exists()) {
               var14 = new File(var12, var12.getName());
            }

            for (String var9 : AssetFolders.KNOWN_FOLDERS) {
               if (new File(var12, var9).exists()) {
                  var14 = var12;
                  break;
               }
            }

            if (var14.exists() && Utils.countChildren(var14) > 0) {
               var3.add(var14);
            }
         }
      }

      File var11 = new File(var2);
      if (var11.exists()) {
         var3.add(var11);
      }

      return var3;
   }

   static List collectResourcePackDirs(String var0, String var1, String var2) {
      ArrayList var3 = new ArrayList();

      for (File var6 : listContentFolders(var0, var1)) {
         File var7 = new File(var6, "resourcepack/");
         if (var7.exists() && Utils.countChildren(var7) > 0) {
            var3.add(var7);
         } else {
            var7 = new File(var6, "resource_pack/");
            if (var7.exists() && Utils.countChildren(var7) > 0) {
               var3.add(var7);
            } else {
               var7 = new File(var6, "data/resource_pack/");
               if (var7.exists() && Utils.countChildren(var7) > 0) {
                  var3.add(var7);
               }
            }
         }
      }

      File var10 = new File(var2);
      if (var10.exists()) {
         var3.add(0, var10);
      }

      if (Settings.get().mergedResourcepackFolders != null && !Settings.get().mergedResourcepackFolders.isEmpty()) {
         File var11 = new File(Main.getPlugin().getDataFolder().getParentFile().getAbsolutePath());

         for (String var8 : Settings.get().mergedResourcepackFolders) {
            var8 = Utils.removePrefix(var8, "plugins/");
            File var9 = new File(var11, var8);
            if (var9.exists()) {
               var3.add(var9);
            }
         }
      }

      return var3;
   }

   static List listContentFolders(String var0, String var1) {
      ArrayList var2 = new ArrayList();
      File var3 = new File(var0);
      ArrayList var4 = Utils.listFilesOrEmpty(var3, File::isDirectory);

      for (int var5 = 0; var5 < var4.size(); var5++) {
         File var6 = (File)var4.get(var5);
         if (!Settings.get().contentsFoldersPriority.contains(var6.getName()) && Utils.countChildren(var6) != 0) {
            var2.add(var6);
         }
      }

      File var10 = new File(var1);
      if (var10.exists()) {
         var2.add(0, var10);
      }

      for (String var7 : Settings.get().contentsFoldersPriority) {
         for (File var9 : var4) {
            if (var9.getName().equals(var7)) {
               var2.add(0, var9);
            }
         }
      }

      return var2;
   }

   @Nullable
   static File findFirstExisting(List var0, String var1) {
      for (File var3 : var0) {
         File var4 = new File(var3, var1);
         if (var4.exists()) {
            return var4;
         }
      }

      return null;
   }

   @Nullable
   static File findAssetFile(List var0, List var1, String var2) {
      var2 = Utils.normalizeSlashes(var2);
      String var3 = Utils.removePrefix(var2, "assets/");

      for (File var5 : var0) {
         File var6 = new File(var5, var2);
         if (var6.exists()) {
            return var6;
         }

         var6 = new File(var5, var3);
         if (var6.exists()) {
            return var6;
         }
      }

      String var10 = null;
      String[] var11 = var3.split("/");
      if (var11.length > 0) {
         var10 = var11[0];
      }

      for (File var7 : var1) {
         if (var7.getName().equals(var10)) {
            File var8 = new File(var7, Utils.removePrefix(var3, var10 + "/"));
            if (var8.exists()) {
               return var8;
            }
         }
      }

      return null;
   }

   static List findAllAssetFiles(List var0, List var1, String var2) {
      ArrayList var3 = new ArrayList();
      var2 = Utils.normalizeSlashes(var2);
      String var4 = Utils.removePrefix(var2, "assets/");

      for (File var6 : var0) {
         File var7 = new File(var6, var2);
         if (var7.exists()) {
            var3.add(var7);
         }

         var7 = new File(var6, var4);
         if (var7.exists()) {
            var3.add(var7);
         }
      }

      String var11 = null;
      String[] var12 = var4.split("/");
      if (var12.length > 1) {
         var11 = var12[0];
      }

      for (File var8 : var1) {
         if (var8.getName().equals(var11)) {
            File var9 = new File(var8, Utils.removePrefix(var4, var11 + "/"));
            if (var9.exists()) {
               var3.add(var9);
            }
         }
      }

      return var3;
   }

   static List expandContentFolder(File var0) {
      ArrayList var1 = new ArrayList();
      File[] var2 = var0.listFiles(File::isDirectory);
      if (var2 == null) {
         return var1;
      }

      if (var0.getName().equals("assets")) {
         var1.addAll(List.of(var2));
         return var1;
      }

      boolean var3 = List.of(var2).stream().anyMatch(var0x -> AssetFolders.KNOWN_FOLDERS.contains(var0x.getName()));
      if (var3) {
         File var4 = new File(var0, "assets");
         if (var4.isDirectory()) {
            var1.add(var4);
         }

         var1.add(var0);
         return var1;
      } else {
         var1.addAll(List.of(var2));
         return var1;
      }
   }

   static List expandAllFolders(List var0) {
      ArrayList var1 = new ArrayList();

      for (File var3 : var0) {
         var1.addAll(expandContentFolder(var3));
      }

      return var1;
   }

   static List collectModelJsonFiles(List var0) {
      ArrayList var1 = new ArrayList();

      for (File var4 : expandAllFolders(var0)) {
         for (File var7 : FileUtils.listFiles(var4, new String[]{"json"}, true)) {
            if (Utils.normalizedPath(var7).contains("/models/")) {
               var1.add(var7);
            }
         }
      }

      return var1;
   }

   static boolean eitherExists(String var0, String var1) {
      return new File(var0).exists() || new File(var1).exists();
   }
}
