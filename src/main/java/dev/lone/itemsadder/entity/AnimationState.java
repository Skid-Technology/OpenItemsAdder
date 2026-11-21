package dev.lone.itemsadder.entity;

import org.jetbrains.annotations.Nullable;

public enum AnimationState {
   OTHER("other"),
   IDLE("idle"),
   IDLE_ANGRY("idle_angry"),
   WALK("walk"),
   WALK_ANGRY("walk_angry"),
   JUMP("jump"),
   ATTACK("attack"),
   DEATH("death"),
   FLY("fly"),
   SWIM("swim"),
   SNEAK("sneak"),
   SNEAK_WALK("sneak_walk");

   private final String str;

   AnimationState(String var3) {
      this.str = var3;
   }

   public static AnimationState fromString(@Nullable String var0) {
      if (var0 == null) {
         return OTHER;
      }

      for (AnimationState var4 : values()) {
         if (var4.str.equals(var0)) {
            return var4;
         }
      }

      return OTHER;
   }

   @Override
   public String toString() {
      return this.str;
   }
}
