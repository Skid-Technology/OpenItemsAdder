package dev.lone.itemsadder.entity;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

class NametagLine {
   @Nullable
   private final Component ur;
   @Nullable
   private final Component us;
   @Nullable
   private final Component text;
   private final float ut;
   private final boolean field8;

   private NametagLine(@Nullable Component var1, @Nullable Component var2, @Nullable Component var3, float var4, boolean var5) {
      this.ur = var1;
      this.us = var2;
      this.text = var3;
      this.ut = var4;
      this.field8 = var5;
   }

   @Nullable
   public Component background() {
      return this.ur;
   }

   @Nullable
   public Component kI() {
      return this.us;
   }

   public float kJ() {
      return this.ut;
   }

   public boolean kK() {
      return this.field8;
   }
}
