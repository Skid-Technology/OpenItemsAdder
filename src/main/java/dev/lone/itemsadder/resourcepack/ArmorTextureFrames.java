package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.LeatherColorDefinition;
import dev.lone.itemsadder.util.Log;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

class ArmorTextureFrames {
   public static final int bHF = 32;
   public static final int bHG = 64;
   @Nullable
   public LeatherColorDefinition bHH;
   public BufferedImage[] bHI = new BufferedImage[2];
   public BufferedImage[] bHJ = new BufferedImage[2];
   public int bHK;

   public ArmorTextureFrames(BufferedImage var1, BufferedImage var2) {
      this.bHI[0] = var1;
      this.bHI[1] = var2;
   }

   public ArmorTextureFrames(@Nullable LeatherColorDefinition var1, int var2) {
      this.bHH = var1;
      this.bHK = var2;
      if (var1 == null) {
         this.bHH = null;
      } else {
         this.bHI[0] = loadTexture(var1.apF, var1);
         this.bHI[1] = loadTexture(var1.apG, var1);
         this.bHJ[0] = loadTexture(var1.apH, var1);
         this.bHJ[1] = loadTexture(var1.apI, var1);
         if (hasSizeMismatch(this.bHI[0], this.bHJ[0])) {
            Log.error(
               ChatColor.RED + "Error: layer_1 and emissive_1 texture have different size for item " + var1.getNamespacedID() + ". File: " + var1.apD.DJ()
            );
            this.bHI[0] = null;
            this.bHJ[0] = null;
         }

         if (hasSizeMismatch(this.bHI[1], this.bHJ[1])) {
            Log.error(
               ChatColor.RED + "Error: layer_2 and emissive_2 texture have different size for item " + var1.getNamespacedID() + ". File: " + var1.apD.DJ()
            );
            this.bHI[1] = null;
            this.bHJ[1] = null;
         }
      }
   }

   public ArmorTextureFrames(BufferedImage var1, BufferedImage var2, BufferedImage var3, BufferedImage var4, int var5) {
      this.bHK = var5;
      this.bHI[0] = var1;
      this.bHI[1] = var2;
      this.bHJ[0] = var3;
      this.bHJ[1] = var4;
      if (hasSizeMismatch(this.bHI[0], this.bHJ[0])) {
         Log.error(ChatColor.RED + "Error: layer_1 and emissive_1 texture have different size!");
         this.bHI[0] = null;
         this.bHJ[0] = null;
      }

      if (hasSizeMismatch(this.bHI[1], this.bHJ[1])) {
         Log.error(ChatColor.RED + "Error: layer_2 and emissive_2 texture have different size!");
         this.bHI[1] = null;
         this.bHJ[1] = null;
      }
   }

   public boolean isMissing() {
      return this.bHI == null ? true : this.bHI[0] == null || this.bHI[1] == null;
   }

   public boolean hasEmissive(int var1) {
      return this.bHJ == null ? false : this.bHJ[var1 - 1] != null;
   }

   @Nullable
   private static BufferedImage loadTexture(String var0, @Nullable LeatherColorDefinition var1) {
      return loadTextureFile(var0, var1, true);
   }

   @Nullable
   static BufferedImage loadTextureFile(String var0, @Nullable LeatherColorDefinition var1, boolean var2) {
      if (var0 == null) {
         return null;
      }

      File var3 = ContentManager.method8198(var0);
      if (var3 != null && var3.exists()) {
         try {
            BufferedImage var4 = ImageIO.read(var3);
            if (var4 == null) {
               if (var2) {
                  logTextureError("is not a readable PNG", var0, var1, null);
               }

               return null;
            } else {
               BufferedImage var5 = new BufferedImage(var4.getWidth(), var4.getHeight(), 2);
               var5.getGraphics().drawImage(var4, 0, 0, null);
               return var5;
            }
         } catch (IOException var6) {
            if (var2) {
               logTextureError("could not be read", var0, var1, var6.getMessage());
            }

            return null;
         }
      } else {
         if (var2) {
            logTextureError("not found", var0, var1, null);
         }

         return null;
      }
   }

   private static void logTextureError(String var0, String var1, @Nullable LeatherColorDefinition var2, @Nullable String var3) {
      String var4 = var2 == null ? "" : " for '" + var2.getNamespacedID() + "' (config: " + var2.apD.DJ() + ")";
      String var5 = var3 == null ? "" : ": " + var3;
      Log.error(ChatColor.RED + "Armor PNG '" + var1 + "' " + var0 + var4 + var5);
   }

   public BufferedImage getLayer(int var1) {
      return this.bHI[var1 - 1];
   }

   public BufferedImage getEmissiveLayer(int var1) {
      return this.bHJ[var1 - 1];
   }

   private static boolean hasSizeMismatch(BufferedImage var0, BufferedImage var1) {
      return var0 != null && var1 != null ? var0.getWidth() != var1.getWidth() || var0.getHeight() != var1.getHeight() : false;
   }

   private static BufferedImage scaleImage(@Nullable BufferedImage var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      }

      if (var0.getWidth() == var1 && var0.getHeight() == var2) {
         return var0;
      }

      Image var3 = var0.getScaledInstance(var1, var2, 1);
      BufferedImage var4 = new BufferedImage(var1, var2, 2);
      var4.getGraphics().drawImage(var3, 0, 0, null);
      return var4;
   }

   public static BufferedImage scaleToWidth(@Nullable BufferedImage var0, int var1) {
      if (var0 == null) {
         return null;
      }

      int var2 = var1 / var0.getWidth();
      return scaleImage(var0, var1, var0.getHeight() * var2);
   }

   public BufferedImage getScaledLayer(int var1, ArmorTextureSize var2) {
      return scaleToWidth(this.bHI[var1 - 1], var2.width);
   }

   @Nullable
   public BufferedImage getScaledEmissiveLayer(int var1, ArmorTextureSize var2) {
      return scaleToWidth(this.bHJ[var1 - 1], var2.width);
   }

   public int getLayerWidth(int var1) {
      BufferedImage var2 = this.bHI[var1 - 1];
      return var2 == null ? 0 : var2.getWidth();
   }

   public int getFrameCount(int var1) {
      return this.getFrameCountChecked(var1, true);
   }

   public int getFrameCountChecked(int var1, boolean var2) {
      BufferedImage var3 = this.bHI[var1 - 1];
      if (var3 == null) {
         return 0;
      }

      int var4 = this.getTextureScaleChecked(var1, var2);
      return var3.getHeight() / var4;
   }

   public int getTextureScale(int var1) {
      return this.getTextureScaleChecked(var1, true);
   }

   public int getTextureScaleChecked(int var1, boolean var2) {
      BufferedImage var3 = this.bHI[var1 - 1];
      if (var3 == null) {
         return 1;
      }

      int var4 = var3.getWidth() / 64;
      return var3.getHeight() / var4 / 32;
   }

   public BufferedImage getLayerImage(int var1) {
      return this.bHI[var1 - 1];
   }

   public BufferedImage getCroppedLayer(int var1) {
      return this.getCroppedLayerChecked(var1, true);
   }

   public BufferedImage getCroppedLayerChecked(int var1, boolean var2) {
      BufferedImage var3 = this.bHI[var1 - 1];
      return cropImage(var3, var3.getWidth(), this.getFrameCountChecked(var1, var2));
   }

   public BufferedImage getCroppedEmissiveLayer(int var1) {
      return this.getCroppedEmissiveLayerChecked(var1, true);
   }

   public BufferedImage getCroppedEmissiveLayerChecked(int var1, boolean var2) {
      BufferedImage var3 = this.bHJ[var1 - 1];
      return cropImage(var3, var3.getWidth(), this.getFrameCountChecked(var1, var2));
   }

   private static BufferedImage cropImage(BufferedImage var0, int var1, int var2) {
      return var0.getSubimage(0, 0, var1, var2);
   }
}
