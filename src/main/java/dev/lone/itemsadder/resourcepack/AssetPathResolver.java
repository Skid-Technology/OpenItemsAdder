package dev.lone.itemsadder.resourcepack;

public final class AssetPathResolver {
   private AssetPathResolver() {
   }

   static String textureAssetPath(String var0, String var1) {
      if (var1 == null) {
         return null;
      }

      var1 = var1.replace(".png", "");
      String var2;
      if (var1.contains(":")) {
         String[] var3 = var1.split(":");
         var2 = var3[0];
         var1 = var3[1];
      } else {
         var2 = var0;
      }

      return var2 == null ? "assets/minecraft/textures/" + var1 + ".png" : "assets/" + var2 + "/textures/" + var1 + ".png";
   }

   static String modelAssetPath(String var0, String var1) {
      if (var1 == null) {
         return null;
      }

      var1 = var1.replace(".json", "");
      String var2;
      if (var1.contains(":")) {
         String[] var3 = var1.split(":");
         var2 = var3[0];
         var1 = var3[1];
      } else {
         var2 = var0;
      }

      return var2 == null ? "assets/minecraft/models/" + var1 + ".json" : "assets/" + var2 + "/models/" + var1 + ".json";
   }

   static String humanoidEquipmentTexturePath(int var0, String var1, String var2) {
      if (var2 == null) {
         return null;
      }

      var2 = var2.replace(".png", "");
      String var3;
      if (var2.contains(":")) {
         String[] var4 = var2.split(":");
         var3 = var4[0];
         var2 = var4[1];
      } else {
         var3 = var1;
      }

      if (var3 == null) {
         var3 = "minecraft";
      }

      return "assets/" + var3 + "/textures/entity/equipment/humanoid" + (var0 == 1 ? "_leggings" : "") + "/" + var2 + ".png";
   }
}
