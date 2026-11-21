package dev.lone.itemsadder.entity;

enum MobVariant {
   VANILLA_ZOMBIE("vanilla_zombie"),
   V2_ZOMBIE("v2_zombie"),
   VANILLA_PIG("vanilla_pig"),
   V2_PIG("v2_pig"),
   VANILLA_TARGET("vanilla_target"),
   V2_TARGET("v2_target");

   final String key;

   MobVariant(String var3) {
      this.key = var3;
   }
}
