package dev.lone.itemsadder.util;

import dev.lone.itemsadder.block.BlockRealizationMode;
import org.bukkit.Material;

public class MaterialTableEntry {
   private final int id;
   private final BlockRealizationMode type;
   private final Material material;
   private final String state;
   private final int minClientVersion;

   public MaterialTableEntry(int var1, BlockRealizationMode var2, Material var3, String var4, int var5) {
      this.id = var1;
      this.type = var2;
      this.material = var3;
      this.state = var4;
      this.minClientVersion = var5;
   }

   public boolean requiresVersion20() {
      return this.minClientVersion == 20;
   }

   public boolean requiresVersion26() {
      return this.minClientVersion == 26;
   }

   public boolean isHarpNoteBlock() {
      return this.material == Material.NOTE_BLOCK && this.state.startsWith("[instrument=harp,");
   }

   public String toBlockStateString() {
      return "minecraft:" + this.material.name().toLowerCase() + this.state;
   }

   public String getStateProperties() {
      return this.state.substring(1, this.state.length() - 1);
   }
}
