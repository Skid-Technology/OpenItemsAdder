package dev.lone.itemsadder.api;

import dev.lone.itemsadder.api.CustomEntityNametag.Snapshot;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.internal.SwitchMap90;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class CustomEntityNametag {
   private final CustomEntityImpl renderer;
   @Nullable
   private Component text;
   private double offsetY;
   private boolean visible = true;
   private int lineWidth = 200;
   @Nullable
   private Color backgroundColor;
   private byte textOpacity = -1;
   private boolean shadowed;
   private boolean seeThrough;
   private boolean defaultBackground;
   private TextAlignment alignment = TextAlignment.CENTER;
   private Billboard billboard = Billboard.CENTER;
   private Transformation transformation = identity();
   private int interpolationDelay;
   private int interpolationDuration;
   private int teleportDuration = 2;
   @Nullable
   private Float viewRange;
   private float shadowRadius;
   private float shadowStrength = 1.0F;
   private float displayWidth;
   private float displayHeight;
   @Nullable
   private Brightness brightness;
   @Nullable
   private Color glowColorOverride;
   private boolean glowing;

   @Internal
   public CustomEntityNametag(CustomEntityImpl renderer) {
      this.renderer = Objects.requireNonNull(renderer);
   }

   @Nullable
   public Component getDefaultText() {
      return this.renderer.buildNametagComponent();
   }

   @Nullable
   public synchronized Component getText() {
      return this.renderer.resolveNametagText(this.requestedText(this.getDefaultText()));
   }

   public synchronized void setText(Component text) {
      this.text = Objects.requireNonNull(text, "text");
   }

   public void setText(String text) {
      this.setText(Component.text(Objects.requireNonNull(text, "text")));
   }

   public synchronized void resetText() {
      this.text = null;
   }

   public synchronized double getOffsetY() {
      return this.offsetY;
   }

   public synchronized void setOffsetY(double offsetY) {
      if (Double.isFinite(offsetY) && !(Math.abs(offsetY) > Float.MAX_VALUE)) {
         this.offsetY = offsetY;
      } else {
         throw new IllegalArgumentException("offsetY must be finite and fit in a float");
      }
   }

   public synchronized boolean isVisible() {
      return this.visible;
   }

   public synchronized void setVisible(boolean visible) {
      this.visible = visible;
   }

   public synchronized int getLineWidth() {
      return this.lineWidth;
   }

   public synchronized void setLineWidth(int width) {
      if (width <= 0) {
         throw new IllegalArgumentException("lineWidth must be positive");
      }

      this.lineWidth = width;
   }

   @Nullable
   public synchronized Color getBackgroundColor() {
      return this.backgroundColor;
   }

   public synchronized void setBackgroundColor(@Nullable Color color) {
      this.backgroundColor = color;
   }

   public synchronized byte getTextOpacity() {
      return this.textOpacity;
   }

   public synchronized void setTextOpacity(byte opacity) {
      this.textOpacity = opacity;
   }

   public synchronized boolean isShadowed() {
      return this.shadowed;
   }

   public synchronized void setShadowed(boolean shadowed) {
      this.shadowed = shadowed;
   }

   public synchronized boolean isSeeThrough() {
      return this.seeThrough;
   }

   public synchronized void setSeeThrough(boolean seeThrough) {
      this.seeThrough = seeThrough;
   }

   public synchronized boolean isDefaultBackground() {
      return this.defaultBackground;
   }

   public synchronized void setDefaultBackground(boolean enabled) {
      this.defaultBackground = enabled;
   }

   public synchronized TextAlignment getAlignment() {
      return this.alignment;
   }

   public synchronized void setAlignment(TextAlignment alignment) {
      this.alignment = Objects.requireNonNull(alignment);
   }

   public synchronized Billboard getBillboard() {
      return this.billboard;
   }

   public synchronized void setBillboard(Billboard billboard) {
      this.billboard = Objects.requireNonNull(billboard);
   }

   public synchronized Transformation getTransformation() {
      return copy(this.transformation);
   }

   public synchronized void setTransformation(Transformation transformation) {
      Transformation var2 = copy(Objects.requireNonNull(transformation));
      if (var2.getTranslation().isFinite()
         && var2.getScale().isFinite()
         && var2.getLeftRotation().isFinite()
         && var2.getRightRotation().isFinite()
         && var2.getLeftRotation().lengthSquared() != 0.0F
         && var2.getRightRotation().lengthSquared() != 0.0F) {
         this.transformation = var2;
      } else {
         throw new IllegalArgumentException("Transformation must contain finite values and nonzero rotations");
      }
   }

   public synchronized int getInterpolationDelay() {
      return this.interpolationDelay;
   }

   public synchronized void setInterpolationDelay(int ticks) {
      this.interpolationDelay = ticks;
   }

   public synchronized int getInterpolationDuration() {
      return this.interpolationDuration;
   }

   public synchronized void setInterpolationDuration(int ticks) {
      if (ticks < 0) {
         throw new IllegalArgumentException("interpolationDuration must be nonnegative");
      }

      this.interpolationDuration = ticks;
   }

   public synchronized int getTeleportDuration() {
      return this.teleportDuration;
   }

   public synchronized void setTeleportDuration(int ticks) {
      if (ticks >= 0 && ticks <= 59) {
         this.teleportDuration = ticks;
      } else {
         throw new IllegalArgumentException("teleportDuration must be between 0 and 59");
      }
   }

   @Nullable
   public synchronized Float getViewRange() {
      return this.viewRange;
   }

   public synchronized void setViewRange(@Nullable Float range) {
      if (range != null) {
         nonnegative(range, "viewRange");
      }

      this.viewRange = range;
   }

   public synchronized float getShadowRadius() {
      return this.shadowRadius;
   }

   public synchronized void setShadowRadius(float radius) {
      this.shadowRadius = nonnegative(radius, "shadowRadius");
   }

   public synchronized float getShadowStrength() {
      return this.shadowStrength;
   }

   public synchronized void setShadowStrength(float strength) {
      this.shadowStrength = nonnegative(strength, "shadowStrength");
   }

   public synchronized float getDisplayWidth() {
      return this.displayWidth;
   }

   public synchronized void setDisplayWidth(float width) {
      this.displayWidth = nonnegative(width, "displayWidth");
   }

   public synchronized float getDisplayHeight() {
      return this.displayHeight;
   }

   public synchronized void setDisplayHeight(float height) {
      this.displayHeight = nonnegative(height, "displayHeight");
   }

   @Nullable
   public synchronized Brightness getBrightness() {
      return this.brightness;
   }

   public synchronized void setBrightness(@Nullable Brightness brightness) {
      this.brightness = brightness;
   }

   @Nullable
   public synchronized Color getGlowColorOverride() {
      return this.glowColorOverride;
   }

   public synchronized void setGlowColorOverride(@Nullable Color color) {
      this.glowColorOverride = color;
   }

   public synchronized boolean isGlowing() {
      return this.glowing;
   }

   public synchronized void setGlowing(boolean glowing) {
      this.glowing = glowing;
   }

   public synchronized void reset() {
      this.text = null;
      this.offsetY = 0.0;
      this.visible = true;
      this.lineWidth = 200;
      this.backgroundColor = null;
      this.textOpacity = -1;
      this.shadowed = this.seeThrough = this.defaultBackground = this.glowing = false;
      this.alignment = TextAlignment.CENTER;
      this.billboard = Billboard.CENTER;
      this.transformation = identity();
      this.interpolationDelay = this.interpolationDuration = 0;
      this.teleportDuration = 2;
      this.viewRange = null;
      this.shadowRadius = this.displayWidth = this.displayHeight = 0.0F;
      this.shadowStrength = 1.0F;
      this.brightness = null;
      this.glowColorOverride = null;
   }

   private static float nonnegative(float value, String name) {
      if (Float.isFinite(value) && !(value < 0.0F)) {
         return value;
      } else {
         throw new IllegalArgumentException(name + " must be finite and nonnegative");
      }
   }

   private static Transformation identity() {
      return new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(1.0F), new Quaternionf());
   }

   private static Transformation copy(Transformation value) {
      return new Transformation(
         new Vector3f(value.getTranslation()),
         new Quaternionf(value.getLeftRotation()),
         new Vector3f(value.getScale()),
         new Quaternionf(value.getRightRotation())
      );
   }

   @Internal
   public synchronized Snapshot snapshot(@Nullable Component defaultText) {
      return this.renderSnapshot(this.requestedText(defaultText));
   }

   @Internal
   @Nullable
   public synchronized Component requestedText(@Nullable Component defaultText) {
      return this.text != null ? this.text : defaultText;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Internal
   public synchronized Snapshot renderSnapshot(@Nullable Component preparedText) {
      byte var2 = (byte)(
         (this.shadowed ? 1 : 0)
            | (this.seeThrough ? 2 : 0)
            | (this.defaultBackground ? 4 : 0)
            | (this.alignment == TextAlignment.LEFT ? 8 : (this.alignment == TextAlignment.RIGHT ? 16 : 0))
      );

      return new Snapshot(
         preparedText,
         this.offsetY,
         this.visible,
         this.lineWidth,
         this.backgroundColor == null ? 1073741824 : this.backgroundColor.asARGB(),
         this.textOpacity,
         var2,
         switch (SwitchMap90.$SwitchMap$org$bukkit$entity$Display$Billboard[this.billboard.ordinal()]) {
            case 1 -> 0;
            case 2 -> 1;
            case 3 -> 2;
            case 4 -> 3;
            default -> throw new MatchException(null, null);
         },
         copy(this.transformation),
         this.interpolationDelay,
         this.interpolationDuration,
         this.teleportDuration,
         this.viewRange,
         this.shadowRadius,
         this.shadowStrength,
         this.displayWidth,
         this.displayHeight,
         this.brightness == null ? -1 : this.brightness.getBlockLight() << 4 | this.brightness.getSkyLight() << 20,
         this.glowColorOverride == null ? -1 : this.glowColorOverride.asRGB(),
         this.glowing
      );
   }
}
