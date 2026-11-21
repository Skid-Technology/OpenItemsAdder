package dev.lone.itemsadder.util;

import net.kyori.adventure.text.Component;

public class ComponentResult {
   public final Component component;
   public final boolean changed;

   public ComponentResult(Component var1, boolean var2) {
      this.component = var1;
      this.changed = var2;
   }

   public ComponentResult(Component var1) {
      this.component = var1;
      this.changed = true;
   }
}
