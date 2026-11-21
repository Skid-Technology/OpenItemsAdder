package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.util.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

public class PackManifest {
   public final String bIJ;
   public final JSONObject bIK;
   public boolean bIL;
   public final List bIM;
   public final List bIN = new ArrayList();

   public PackManifest(String var1, JSONObject var2) {
      this.bIJ = Utils.normalizeSlashes(var1);
      this.bIK = var2;
      this.bIM = new ArrayList();
   }

   public void addEntry(PackFileEntry var1) {
      this.bIM.add(var1);
   }

   public void addAssetEntry(String var1, String var2) {
      this.addEntry(new PackFileEntry(var1, var2));
   }

   public void addFileEntry(File var1, String var2) {
      this.addEntry(new PackFileEntry(var1, var2));
   }

   public String getAssetPath() {
      return this.bIJ.substring(this.bIJ.indexOf("assets") + "assets".length() + 1);
   }

   public Set collectTextureIds() {
      HashSet var1 = new HashSet();
      JSONObject var2 = this.bIK.getJSONObject("textures");
      if (var2 == null) {
         return var1;
      }

      for (Entry var4 : var2.entrySet()) {
         Object var5 = var4.getValue();
         String var6 = var5 instanceof JSONObject var7 ? var7.getString("sprite") : (String)var5;
         if (!PackFileEntry.isSpecialRef(var6)) {
            if (!var6.contains(":")) {
               var6 = "minecraft:" + var6;
            }

            var1.add(var6);
         }
      }

      return var1;
   }

   @Nullable
   public String getParent() {
      return this.bIK.getString("parent");
   }

   public String getItemId() {
      return normalizeModelId(this.bIJ);
   }

   public boolean isBlockModel() {
      String var1 = this.getItemId();
      return isBlockModelId(var1);
   }

   public static boolean isBlockModelId(String var0) {
      String var1 = var0.split(":")[1];
      return var1.startsWith("block/") || var1.startsWith("blocks/");
   }

   public static String toItemId(File var0) {
      String var1 = Utils.normalizeSlashes(var0.getPath());
      return normalizeModelId(var1);
   }

   public static String normalizeModelId(String var0) {
      var0 = Utils.normalizeSlashes(var0);
      if (var0.contains(":") && !var0.contains(":/") && !var0.contains(":\\")) {
         return var0;
      }

      int var1 = indexOfFolder(var0, "assets");
      String var2;
      if (var1 != -1) {
         var2 = var0.substring(var1 + "assets".length() + 1);
      } else {
         int var3 = indexOfFolder(var0, "contents");
         if (var3 == -1) {
            throw new IllegalArgumentException("Path does not contain 'assets' or 'contents' folder: " + var0);
         }

         var2 = var0.substring(var3 + "contents".length() + 1);
      }

      int var9 = var2.indexOf(47);
      if (var9 == -1) {
         throw new IllegalArgumentException("Cannot read namespace from path: " + var0);
      }

      String var4 = var2.substring(0, var9);
      int var5 = indexOfFolder(var2, "models");
      if (var5 == -1) {
         throw new IllegalArgumentException("Path does not contain 'models' folder after assets/contents: " + var0);
      }

      String var6 = var2.substring(var5 + "models".length() + 1);
      String var7 = var6.endsWith(".json") ? var6.substring(0, var6.length() - ".json".length()) : var6;
      return var4 + ":" + var7;
   }

   private static int indexOfFolder(String var0, String var1) {
      String var2 = "/" + var1 + "/";
      int var3 = var0.indexOf(var2);
      if (var3 != -1) {
         return var3 + 1;
      } else {
         return var0.startsWith(var1 + "/") ? 0 : -1;
      }
   }

   public void addSkipped(String var1) {
      this.bIN.add(var1);
   }

   public boolean isSkipped(String var1) {
      return this.bIN.contains(var1);
   }

   @Generated
   public List getSkipped() {
      return this.bIN;
   }
}
