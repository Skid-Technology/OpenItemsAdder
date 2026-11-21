package dev.lone.itemsadder.gui;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import dev.lone.itemsadder.lang.TextFormatCache;
import java.util.Objects;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class ScrollableGuiTitle {
   private static final LegacyComponentSerializer aLP = LegacyComponentSerializer.legacySection();
   private volatile GuiTitleLayout aLQ;

   public ScrollableGuiTitle(String var1) {
      this(aLP.deserialize(Objects.requireNonNull(var1, "title")));
   }

   public ScrollableGuiTitle(Component var1) {
      this.aLQ = new GuiTitleLayout(Objects.requireNonNull(var1, "title"), null, 0, -8);
   }

   public synchronized void method2718(Component var1) {
      GuiTitleLayout var2 = this.aLQ;
      this.aLQ = new GuiTitleLayout(Objects.requireNonNull(var1, "title"), var2.aLS, var2.aLT, var2.aLU);
   }

   public synchronized void eI(String var1) {
      this.method2718(MiniMessage.miniMessage().deserialize(Objects.requireNonNull(var1, "title")));
   }

   public synchronized void method2719(FontImageWrapper var1, int var2, int var3) {
      this.method2721(var1 == null ? null : new TitleFontSpec(var1.getString(), var1.getColor(), var1.getWidth()), var2, var3);
   }

   synchronized void method2720(String var1, TextColor var2, int var3, int var4, int var5) {
      this.method2721(new TitleFontSpec(var1, var2, var3), var4, var5);
   }

   private void method2721(TitleFontSpec var1, int var2, int var3) {
      this.aLQ = new GuiTitleLayout(this.aLQ.aLR, var1, var2, var3);
   }

   public synchronized void method2722(FontImageWrapper var1) {
      this.method2719(var1, 0, -8);
   }

   public Component MG() {
      GuiTitleLayout var1 = this.aLQ;
      if (var1.aLS == null) {
         return var1.aLR;
      }

      Component var2 = ((TextComponent)((TextComponent)((TextComponent)Component.text(var1.aLS.aLV).color(var1.aLS.aLW))
               .decoration(TextDecoration.ITALIC, false))
            .decoration(TextDecoration.BOLD, false))
         .font(Key.key("minecraft:default"));
      var2 = TextFormatCache.method9635(var2, var1.aLU);
      return ((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)Component.empty().color(NamedTextColor.WHITE))
                     .decoration(TextDecoration.ITALIC, false))
                  .decoration(TextDecoration.BOLD, false))
               .font(Key.key("minecraft:default")))
            .append(var2))
         .append(TextFormatCache.method9635(var1.aLR.colorIfAbsent(TextColor.color(4210752)), -var1.aLS.aLX + var1.aLT));
   }
}
