package dev.lone.itemsadder.entity;

import org.jetbrains.annotations.Nullable;

public enum AnimationVariable {
   NONE,
   AGE,
   AGGRESSIVE,
   ATTACK_PROGRESS;

   static AnimationVariable fromString(@Nullable String var0) {
      if (var0 != null && !var0.isEmpty()) {
         return switch (var0) {
            case "age" -> AGE;
            case "aggressive" -> AGGRESSIVE;
            case "attack_progress" -> ATTACK_PROGRESS;
            default -> throw new IllegalArgumentException("Unknown semantic animation driver: " + var0);
         };
      } else {
         return NONE;
      }
   }
}
