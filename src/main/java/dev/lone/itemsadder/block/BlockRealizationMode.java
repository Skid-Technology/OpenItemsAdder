package dev.lone.itemsadder.block;

import org.bukkit.Material;

public enum BlockRealizationMode {
   REAL("REAL", "brown_mushroom_block", "mushroom_stem", "red_mushroom_block"),
   REAL_NOTE("REAL_NOTE", "note_block"),
   REAL_TRANSPARENT("REAL_TRANSPARENT", "chorus_plant"),
   TILE("TILE"),
   REAL_WIRE("REAL_WIRE", "tripwire"),
   FIRE("FIRE", "fire"),
   ENTITY("ENTITY");

   private final String text;
   private final String[] vanillaBlockstateNames;

   BlockRealizationMode(String var3, String... var4) {
      this.text = var3;
      this.vanillaBlockstateNames = var4;
   }

   @Override
   public String toString() {
      return this.text;
   }

   public String[] getVanillaBlockstateNames() {
      return this.vanillaBlockstateNames;
   }

   public Material resolveMaterial(Material var1) {
      return this == REAL_WIRE ? Material.STRING : var1;
   }

   public boolean isBlockBacked() {
      return this != ENTITY;
   }

   public boolean isFullyRealized() {
      return this == REAL || this == REAL_NOTE || this == REAL_TRANSPARENT;
   }

   public static boolean isRealizableMaterial(Material var0) {
      return var0 != null && var0.isBlock() && var0.isItem() && var0.isSolid() && var0.isOccluding() && !var0.isInteractable() && !var0.hasGravity();
   }
}
