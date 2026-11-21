package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import org.bukkit.Material;

public class MaterialJsonAsset extends VersionedJsonAsset {
   public MaterialJsonAsset(String var1) {
      super(var1);
   }

   public MaterialJsonAsset(String var1, Version var2) {
      super(var1, var2);
   }

   public MaterialJsonAsset(Material var1) {
      super("assets/minecraft/items/" + var1.toString().toLowerCase() + ".json");
   }

   public static MaterialJsonAsset forVersion(String var0, Version var1) {
      return new MaterialJsonAsset("assets/minecraft/items/" + var0 + ".json", var1);
   }
}
