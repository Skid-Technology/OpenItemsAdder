package dev.lone.itemsadder.util;

import net.kyori.adventure.text.Component;

public class ComponentText {
   public final String plain;
   public final Component component;

   public ComponentText(Component var1) {
      this.component = var1;
      this.plain = AdventureText.serializePlain(var1);
   }

   public static ComponentText of(Component var0) {
      return new ComponentText(var0);
   }

   public String getPlain() {
      return Utils.componentToLegacy(this.component);
   }
}
