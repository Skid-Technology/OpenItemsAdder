package dev.lone.itemsadder.util;

import dev.lone.itemsadder.item.HudDisplayMode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.jetbrains.annotations.Nullable;

public class BossBarSpec {
   public final HudDisplayMode displayMode;
   @Nullable
   public final String title;
   @Nullable
   public final Float progress;
   private BarColor color;
   private BarStyle style;

   public BossBarSpec(HudDisplayMode var1, @Nullable String var2, @Nullable Float var3) {
      this.displayMode = var1;
      this.title = var2;
      this.progress = var3;
   }

   public BossBarSpec(HudDisplayMode var1) {
      this(var1, (String)null, (Float)null);
   }

   public BossBarSpec(HudDisplayMode var1, BarColor var2, BarStyle var3) {
      this.displayMode = var1;
      this.color = var2;
      this.style = var3;
      this.title = null;
      this.progress = null;
   }

   public BarColor getColor() {
      return this.color;
   }

   public BarStyle getStyle() {
      return this.style;
   }
}
