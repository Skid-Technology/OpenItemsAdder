package dev.lone.itemsadder.api;

import net.kyori.adventure.text.Component;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class CustomEntityNametag$Snapshot {
   @Nullable
   private final Component text;
   private final double offsetY;
   private final boolean visible;
   private final int lineWidth;
   private final int backgroundColor;
   private final byte textOpacity;
   private final byte flags;
   private final byte billboard;
   private final Transformation transformation;
   private final int interpolationDelay;
   private final int interpolationDuration;
   private final int teleportDuration;
   @Nullable
   private final Float viewRange;
   private final float shadowRadius;
   private final float shadowStrength;
   private final float displayWidth;
   private final float displayHeight;
   private final int brightness;
   private final int glowColor;
   private final boolean glowing;

   public CustomEntityNametag$Snapshot(
      @Nullable Component text,
      double offsetY,
      boolean visible,
      int lineWidth,
      int backgroundColor,
      byte textOpacity,
      byte flags,
      byte billboard,
      Transformation transformation,
      int interpolationDelay,
      int interpolationDuration,
      int teleportDuration,
      @Nullable Float viewRange,
      float shadowRadius,
      float shadowStrength,
      float displayWidth,
      float displayHeight,
      int brightness,
      int glowColor,
      boolean glowing
   ) {
      this.text = text;
      this.offsetY = offsetY;
      this.visible = visible;
      this.lineWidth = lineWidth;
      this.backgroundColor = backgroundColor;
      this.textOpacity = textOpacity;
      this.flags = flags;
      this.billboard = billboard;
      this.transformation = transformation;
      this.interpolationDelay = interpolationDelay;
      this.interpolationDuration = interpolationDuration;
      this.teleportDuration = teleportDuration;
      this.viewRange = viewRange;
      this.shadowRadius = shadowRadius;
      this.shadowStrength = shadowStrength;
      this.displayWidth = displayWidth;
      this.displayHeight = displayHeight;
      this.brightness = brightness;
      this.glowColor = glowColor;
      this.glowing = glowing;
   }
}
