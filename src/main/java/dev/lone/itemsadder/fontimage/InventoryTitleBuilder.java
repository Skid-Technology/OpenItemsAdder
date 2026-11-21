package dev.lone.itemsadder.fontimage;

import dev.lone.itemsadder.config.Settings;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public final class InventoryTitleBuilder {
   private InventoryTitleBuilder() {
   }

   static Component Md() {
      return (Component)(!Settings.get().guiScrollableInventoryTitle ? Component.empty() : method2583(Component.translatable("itemsadder.container.inventory")));
   }

   static Component method2583(Component var0) {
      return ((TextComponent)((TextComponent)((TextComponent)((TextComponent)Component.empty()
                     .append(Component.text("\u2066\u2066").font(Key.key("_iainternal:inventory_title_advance"))))
                  .append(var0.font(Key.key("_iainternal:inventory_title")).color(NamedTextColor.WHITE)))
               .append(Component.text("\u2069\u2066").font(Key.key("_iainternal:inventory_title_advance"))))
            .append(var0.font(Key.key("_iainternal:inventory_title_advance"))))
         .append(Component.text("\u2069\u2069").font(Key.key("_iainternal:inventory_title_advance")));
   }
}
