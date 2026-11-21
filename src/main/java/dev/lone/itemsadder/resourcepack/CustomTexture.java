package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespacedId;
import dev.lone.itemsadder.lang.TextFormatCache;
import dev.lone.itemsadder.util.FileVersionReader;
import dev.lone.itemsadder.util.ImageWriterHelper;
import dev.lone.itemsadder.util.Log;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.ShadowColor;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.Nullable;

public class CustomTexture extends NamespacedId implements Cloneable {
   public static final int FORMAT_VERSION = 1;
   private static final int VANILLA_GLYPH_WIDTH = 16;
   private static final int VANILLA_GLYPH_HEIGHT = 16;
   public final boolean loadFailed;
   private String resourcePath;
   String fileName;
   String namespace;
   int yPosition = 0;
   int height = 9;
   int width = 0;
   String displayChar;
   public final boolean hasShadow;
   @Nullable
   public final Integer shadowColor;
   public boolean YG;
   String YH;
   boolean YI;
   boolean YJ;
   String permissionNode;
   String legacyId;
   boolean isPrivateUseChar;

   @Nullable
   public static BufferedImage readFontImage(String var0) {
      return var0 == null ? null : readPngImage(new File(var0));
   }

   @Nullable
   public static BufferedImage readPngImage(File var0) {
      if (var0 != null && var0.exists()) {
         ImageInputStream var1 = ImageIO.createImageInputStream(var0);
         Iterator var2 = ImageIO.getImageReaders(var1);
         if (!var2.hasNext()) {
            Log.error("Error initializing PNG image reader. Something's wrong with your Java installation.");
            return null;
         }

         ImageReader var3 = (ImageReader)var2.next();
         ImageReadParam var4 = var3.getDefaultReadParam();

         BufferedImage var5;
         try {
            var3.setInput(var1, false, true);
            var5 = var3.read(0, var4);
         } finally {
            var3.dispose();
            var1.close();
         }

         return var5;
      } else {
         return null;
      }
   }

   public CustomTexture(
      YamlConfig var1,
      String var2,
      String var3,
      String var4,
      int var5,
      int var6,
      String var7,
      boolean var8,
      boolean var9,
      Integer var10,
      boolean var11,
      @Nullable String var12,
      boolean var13
   ) {
      super(var2, var3);
      this.namespace = var2;
      this.hasShadow = var9;
      this.shadowColor = var10;
      this.YG = var11;
      this.YH = var12;
      if (var4 != null && !var4.isEmpty() && !var4.equals(" ")) {
         if (var4.contains(":")) {
            String[] var14 = var4.split(":");
            if (var14.length != 2) {
               Log.error("Bad path for font_image '" + this.getNamespacedID() + "'. It contains " + var14.length + " ':' characters. Only one is allowed.");
               this.loadFailed = true;
               return;
            }

            this.namespace = var14[0];
            var4 = var14[1];
         }

         if (!var4.endsWith(".png")) {
            var4 = var4 + ".png";
         }

         this.legacyId = ":" + var3 + ":";
         this.yPosition = var5;
         this.permissionNode = var7;
         this.YI = var8;
         this.YJ = var13;
         this.fileName = var4;
         this.resourcePath = "assets/" + this.namespace + "/textures/" + var4;
         File var17 = ContentManager.method8198(this.resourcePath);
         if (var17 != null && var17.exists()) {
            try {
               FileVersionReader var15 = new FileVersionReader(var17);
               this.width = var15.majorVersion;
               this.height = var15.minorVersion;
            } catch (Throwable var16) {
               Log.error("Error calculating font_image '" + this.getNamespacedID() + "' dimensions. File: " + this.resourcePath, var16);
               this.loadFailed = true;
               return;
            }
         } else {
            if (!this.namespace.equals("minecraft")) {
               Log.error("Image not found for font_image '" + this.getNamespacedID() + "'. " + this.resourcePath);
               this.loadFailed = true;
               return;
            }

            if (!ContentManager.method8084().aL.generator.vanillaTextureExists(this.resourcePath)) {
               Log.warn("Failed to add font glyph entry. Vanilla resource '" + this.resourcePath + "' does not exist. File: " + var1.DJ());
               this.loadFailed = true;
               return;
            }

            this.height = 16;
            this.width = 16;
         }

         if (var6 != 0) {
            this.width = scaleValue(this.width, this.height, var6);
            this.height = var6;
         }

         if (var5 > this.height) {
            Log.error(
               ChatColor.RED
                  + "Error loading font_image '"
                  + this.getNamespacedID()
                  + "': &6y_position &cmust be lower or equal to &6scale_ratio&c (or &6png height&c)."
            );
            Log.error("&6y_position: " + var5 + " &c| &6scale_ratio: " + var6 + " &c| &6png height: " + this.height + "&c. ");
            this.loadFailed = true;
         } else {
            if (Settings.get().isLoggingEnabled("logging.font_images.loading")) {
               Log.log("    size: " + this.width + "x" + this.height);
            }

            this.loadFailed = false;
         }
      } else {
         Log.error("Blank path for font_image '" + this.getNamespacedID() + "'. Please specify one.");
         this.loadFailed = true;
      }
   }

   public void fixTransparentPixels() {
      try {
         File var1 = ContentManager.method8198(this.resourcePath);
         BufferedImage var2 = readPngImage(var1);
         fixCornerPixelsAndSave(var1, var2);
      } catch (Throwable var3) {
         Log.error("Failed to fix transparent pixels of font image '" + this.getNamespacedID() + "'. File: " + this.resourcePath, var3);
      }
   }

   @Nullable
   public File getFontImageFile() {
      return ContentManager.method8198(this.resourcePath);
   }

   public CustomTexture copy() {
      try {
         return (CustomTexture)super.clone();
      } catch (CloneNotSupportedException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public void setDisplayChar(String var1) {
      this.displayChar = var1;
      this.isPrivateUseChar = isSurrogateChar(var1.charAt(0));
   }

   public String getFullId() {
      return this.namespace + ":" + this.fileName;
   }

   @Override
   public String getNamespace() {
      return this.namespace;
   }

   public String getFileName() {
      return this.fileName;
   }

   public int getYPosition() {
      return this.yPosition;
   }

   public int getHeight() {
      return this.height;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int var1) {
      this.width = var1;
   }

   public String getFormattedText() {
      return this.getDisplayText(true);
   }

   public String getDisplayText(boolean var1) {
      return var1 ? TextFormatCache.method9637(this.displayChar, -1) : this.displayChar;
   }

   public boolean xe() {
      return this.YI;
   }

   public boolean xf() {
      return this.YJ;
   }

   @Nullable
   public String getCustomText() {
      return this.YH;
   }

   public void setCustomText(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         this.YH = var1;
      }
   }

   @Nullable
   public String getPermission() {
      return this.permissionNode != null && !this.permissionNode.isEmpty() ? PermissionNodes.es(this.permissionNode) : null;
   }

   public boolean hasPermission(Permissible var1) {
      return PermissionNodes.method1628(var1, this.getPermission());
   }

   @Deprecated
   public String getLegacyId() {
      return this.legacyId;
   }

   public boolean isPrivateUse() {
      return this.isPrivateUseChar;
   }

   public static boolean isSurrogatePairAt(String var0, int var1) {
      return var1 > var0.length() - 1 ? false : isSurrogateChar(var0.charAt(var1)) && isSurrogateChar(var0.charAt(var1 + 1));
   }

   public static boolean isSurrogateChar(char var0) {
      return var0 >= '\ud7fc' && var0 <= '\udfff';
   }

   @Deprecated
   public static int findRightmostOpaqueColumn(BufferedImage var0) {
      byte var1 = 0;
      byte var2 = 0;
      int var3 = var0.getWidth();
      int var4 = var0.getHeight();

      int var5;
      for (var5 = var3 - 1; var5 >= 0; var5--) {
         int var6 = var1 * var3 + var5;

         for (int var7 = 0; var7 < var4; var7++) {
            int var8 = var2 * var4 + var7;
            if (new Color(var0.getRGB(var6, var8), true).getAlpha() != 0) {
               return var5 + 1;
            }
         }
      }

      return var5 + 1;
   }

   public static BufferedImage fixCornerPixelsAndSave(File var0, BufferedImage var1) {
      ensureMinAlpha(var1, 0, 0);
      ensureMinAlpha(var1, var1.getWidth() - 1, var1.getHeight() - 1);
      ImageWriterHelper.writeImage(var1, "PNG", var0);
      return var1;
   }

   public static void ensureMinAlpha(BufferedImage var0, int var1, int var2) {
      Color var3 = new Color(var0.getRGB(var1, var2), true);
      if (var3.getAlpha() < 5) {
         var3 = new Color(var3.getRed(), var3.getGreen(), var3.getBlue(), 5);
         var0.setRGB(var1, var2, var3.getRGB());
      }
   }

   public static int scaleValue(int var0, int var1, int var2) {
      return (int)((double)var2 * var0 / var1 + 0.5);
   }

   public static String escapeForJs(String var0) {
      return StringEscapeUtils.escapeJavaScript(var0);
   }

   public Builder applyShadow(Builder var1) {
      if (!Version.isAtLeast(Version.v1_21_4)) {
         return var1;
      }

      if (this.hasShadow) {
         if (this.shadowColor != null) {
            var1 = (Builder)var1.shadowColor(ShadowColor.shadowColor(ShadowColor.shadowColor(this.shadowColor), 255));
         }
      } else {
         var1 = (Builder)var1.shadowColor(ShadowColor.shadowColor(0, 0, 0, 0));
      }

      return var1;
   }
}
