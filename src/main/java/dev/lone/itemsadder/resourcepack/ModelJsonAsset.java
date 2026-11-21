package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import com.alibaba.fastjson2.JSONObject;
import java.util.Map.Entry;
import lombok.Generated;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

public class ModelJsonAsset extends VersionedJsonAsset {
   private final String bII;

   ModelJsonAsset(String var1, Version var2, boolean var3) {
      super("assets/minecraft/models/" + (var3 ? "block" : "item") + "/" + var1.toLowerCase() + ".json", var2);
      this.bII = "minecraft:item/" + var1.toLowerCase();
   }

   ModelJsonAsset(Material var1, Version var2, boolean var3) {
      super("assets/minecraft/models/" + (var3 ? "block" : "item") + "/" + var1.toString().toLowerCase() + ".json", var2);
      this.bII = "minecraft:item/" + var1.toString().toLowerCase();
   }

   public static ModelJsonAsset forName(String var0) {
      return forNameVersion(var0, Version.get());
   }

   public static ModelJsonAsset forNameVersion(String var0, Version var1) {
      ModelJsonAsset var2 = new ModelJsonAsset(var0, var1, false);
      return !var2.hasError() ? var2 : new ModelJsonAsset(var0, var1, true);
   }

   public static ModelJsonAsset forMaterial(Material var0) {
      return forMaterialVersion(var0, Version.get());
   }

   public static ModelJsonAsset forMaterialVersion(Material var0, Version var1) {
      return resolveForMaterial(var0, var1, false);
   }

   public static ModelJsonAsset resolveForMaterial(Material var0, Version var1, boolean var2) {
      ModelJsonAsset var3 = new ModelJsonAsset(var0, var1, false);
      if (!var3.hasError()) {
         return var3;
      }

      var3 = new ModelJsonAsset(var0, var1, true);
      if (!var3.hasError()) {
         return var3;
      }

      if (var1.ordinal() >= Version.v1_21_4.ordinal()) {
         MaterialJsonAsset var4 = new MaterialJsonAsset("assets/minecraft/items/" + var0.name().toLowerCase() + ".json", var1);
         if (!var4.hasError()) {
            JSONObject var5 = var4.asObject().getJSONObject("model");
            String var6 = var5 == null ? null : var5.getString("model");
            if (var6 != null && var6.startsWith("minecraft:")) {
               String var7 = var6.substring("minecraft:".length());
               boolean var8 = var7.startsWith("block/");
               if (var8 || var7.startsWith("item/")) {
                  ModelJsonAsset var9 = new ModelJsonAsset(var7.substring(var7.indexOf(47) + 1), var1, var8);
                  if (!var9.hasError()) {
                     return var9;
                  }
               }
            }
         }
      }

      if (var2) {
         for (Version var12 : VanillaAssetManager.ip) {
            ModelJsonAsset var13 = new ModelJsonAsset(var0, var12, false);
            if (!var13.hasError()) {
               return var13;
            }
         }
      }

      return var3;
   }

   public @UnknownNullability String getParent() {
      return ((JSONObject)this.bIG).getString("parent");
   }

   public boolean hasDisplay() {
      return ((JSONObject)this.bIG).containsKey("display");
   }

   public @UnknownNullability JSONObject getDisplay() {
      return ((JSONObject)this.bIG).getJSONObject("display");
   }

   public @UnknownNullability JSONObject getTextures() {
      return ((JSONObject)this.bIG).getJSONObject("textures");
   }

   public boolean hasTextures() {
      return ((JSONObject)this.bIG).containsKey("textures");
   }

   public int textureCount() {
      return !this.hasTextures() ? 0 : this.getTextures().size();
   }

   @Nullable
   public String firstTextureKey() {
      return !this.hasTextures() ? null : (String)((Entry)this.getTextures().entrySet().stream().findFirst().get()).getKey();
   }

   @Generated
   public String getItemReference() {
      return this.bII;
   }
}
