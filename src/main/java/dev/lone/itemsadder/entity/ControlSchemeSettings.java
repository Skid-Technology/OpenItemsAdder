package dev.lone.itemsadder.entity;

import java.util.List;

public class ControlSchemeSettings {
   private final List arW;
   private final boolean arX;

   public ControlSchemeSettings(List var1, boolean var2) {
      this.arW = var1;
      this.arX = var2;
   }

   public List schemes() {
      return this.arW;
   }

   public boolean enabled() {
      return this.arX;
   }
}
