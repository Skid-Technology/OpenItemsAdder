package dev.lone.itemsadder.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;

public class ComponentLabel {
   public final Component component;
   public final String key;
   public Object nativeComponent;

   ComponentLabel(String var1) {
      this(var1, 11184810);
   }

   ComponentLabel(String var1, int var2) {
      this.component = ((TranslatableComponent)Component.translatable(var1).color(TextColor.color(var2)))
         .decorationIfAbsent(TextDecoration.ITALIC, State.FALSE);
      this.key = var1;
      this.nativeComponent = AdventureText.toNative(this.component);
   }

   public static Component withLabelStyle(Component var0) {
      return var0.color(TextColor.color(43520)).decorationIfAbsent(TextDecoration.ITALIC, State.FALSE);
   }
}
