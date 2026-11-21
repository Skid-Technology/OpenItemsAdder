package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.ColoredItemDefinition;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.LeatherColorDefinition;
import dev.lone.itemsadder.util.IntVector3;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

public class ArmorTextureBuilder {
   private static ArmorTextureBuilder bHL;
   private static final int bHM = new Color(0, 0, 0, 0).getRGB();
   private static final String bHN = "assets/minecraft/textures/models/armor/leather_layer_1.png";
   public int bHO;
   ArmorTextureSize bHP = new ArmorTextureSize();
   ArmorTextureSize bHQ = new ArmorTextureSize();
   public ByteArrayOutputStream bHR;
   public ByteArrayOutputStream bHS;
   public BufferedImage[] bHT = new BufferedImage[2];
   public BufferedImage[] bHU = new BufferedImage[2];
   int bHV = 0;
   private final String bHW;
   private final String bHX;
   private final String bHY;
   public HashMap bHZ = new HashMap();
   public ArrayList bIa = new ArrayList();

   private ArmorTextureBuilder() {
      this.bHW = "assets/minecraft/optifine/";
      this.bHX = this.bHW + "cit/ia_generated_armors/";
      this.bHY = this.bHW + "anim/ia_generated_armors/";
   }

   public static ArmorTextureBuilder getInstance(boolean var0) {
      if (var0 || bHL == null) {
         bHL = new ArmorTextureBuilder();
      }

      return bHL;
   }

   public void resetLayers() {
      this.bHT[0] = null;
      this.bHT[1] = null;
      this.bHU[0] = null;
      this.bHU[1] = null;
   }

   public void reset() {
      this.resetLayers();
      this.clearState();
   }

   public void generateArmorAtlas() {
      if (Settings.get().isLoggingEnabled("logging.armors.generation")) {
         Log.log(ChatColor.AQUA + "Generating custom vanilla armors");
      }

      this.reset();
      HashMap var1 = new HashMap();

      for (Entry var3 : ContentManager.method8084().field92.entrySet()) {
         LeatherColorDefinition var4 = ColoredItemDefinition.method8842((CustomItemDefinition)var3.getValue());
         if (var4 != null && !var1.containsKey(var4.apE.asRGB())) {
            this.addSourceFile(var4.apF);
            this.addSourceFile(var4.apG);
            this.addSourceFile(var4.apH);
            this.addSourceFile(var4.apI);
            ArmorTextureFrames var5 = new ArmorTextureFrames(var4, var4.apE.asRGB());
            this.updateAtlasMaxSize(var5);
            var1.put(var4.apE.asRGB(), var5);
         }
      }

      File var21 = new File(ContentManager.field74, "assets/minecraft/textures/models/armor/leather_layer_1.png");
      File var22 = new File(ContentManager.field74, "assets/minecraft/textures/models/armor/leather_layer_2.png");
      if (var21.exists() && var22.exists()) {
         try {
            BufferedImage var6 = ImageIO.read(var21);
            BufferedImage var23 = new BufferedImage(var6.getWidth(), var6.getHeight(), 2);
            var23.getGraphics().drawImage(var6, 0, 0, null);
            BufferedImage var7 = ImageIO.read(var21);
            BufferedImage var25 = new BufferedImage(var7.getWidth(), var7.getHeight(), 2);
            var25.getGraphics().drawImage(var7, 0, 0, null);
            int var8 = var23.getHeight() / 32;
            if (var8 > 1) {
               Log.error("A custom armor atlas file was found. ItemsAdder doesn't support manually created armors atlas textures with height not equal to 32.");
            } else {
               int var9 = var23.getWidth() / 64;
               Log.warn("A custom armor atlas file was found, now loading " + var9 + " textures from it.");
               Log.warn("WARNING: ItemsAdder won't automatically generate Optifine CIT configurations for these armors.");
               LinkedList var10 = new LinkedList();
               LinkedList var11 = new LinkedList();

               for (byte var12 = 0; var12 < var23.getWidth(); var12 += 64) {
                  var10.add(var23.getSubimage(var12, 0, 64, 32));
               }

               for (byte var30 = 0; var30 < var25.getWidth(); var30 += 64) {
                  var11.add(var25.getSubimage(var30, 0, 64, 32));
               }

               for (int var31 = 0; var31 < var9; var31++) {
                  BufferedImage var13 = (BufferedImage)var10.get(var31);
                  BufferedImage var14 = (BufferedImage)var11.get(var31);
                  BufferedImage var15 = null;
                  BufferedImage var16 = null;
                  int var17 = var13.getRGB(0, 0);
                  if (var31 + 1 < var9) {
                     if (this.hasEmissiveMarker(var13)) {
                        var15 = (BufferedImage)var10.get(var31 + 1);
                     }

                     if (this.hasEmissiveMarker(var14)) {
                        var16 = (BufferedImage)var11.get(var31 + 1);
                     }
                  }

                  if (var17 != 0) {
                     ArmorTextureFrames var18 = new ArmorTextureFrames(var13, var14, var15, var16, var17);
                     this.updateAtlasMaxSize(var18);
                     var1.put(var17, var18);
                  } else {
                     this.bHU[0] = var13;
                     this.bHU[1] = var14;
                  }
               }
            }
         } catch (IOException var20) {
            var20.printStackTrace();
         }
      }

      if (Settings.get().isLoggingEnabled("logging.armors.generation")) {
         Log.log(ChatColor.AQUA + "Armor textures maxSize: " + this.bHQ);
      }

      if (this.bHQ.isEmpty()) {
         this.bHQ = ArmorTextureSize.defaultSize();
      }

      this.prepareLayerSizes();
      if (this.bHV == 0) {
         this.bHV = Objects.requireNonNull(this.getBaseLayer(1, true)).getHeight();
      }

      this.bHO = this.bHV / 2;

      for (Entry var26 : var1.entrySet()) {
         ArmorTextureFrames var27 = (ArmorTextureFrames)var26.getValue();
         if (!var27.isMissing()) {
            BufferedImage var28 = var27.getScaledLayer(1, this.bHQ);
            BufferedImage var29 = var27.getScaledLayer(2, this.bHQ);
            if (this.bHP.height < var28.getHeight()) {
               this.bHP.height = var28.getHeight();
            }

            if (this.bHP.height < var29.getHeight()) {
               this.bHP.height = var29.getHeight();
            }

            this.bHP.width = this.bHP.width + this.bHQ.width;
            if (var27.bHJ[0] != null) {
               this.bHP.width = this.bHP.width + this.bHQ.width;
            }
         }
      }

      try {
         this.bHR = this.encodeAtlas(1, var1);
         this.bHS = this.encodeAtlas(2, var1);
      } catch (Throwable var19) {
         Log.error("Failed to generate armor atlas.");
         var19.printStackTrace();
      }
   }

   private ByteArrayOutputStream encodeAtlas(int var1, HashMap var2) {
      BufferedImage var3 = this.renderAtlas(var2, var1);
      if (var3 != null) {
         ByteArrayOutputStream var4 = new ByteArrayOutputStream();
         ImageIO.write(var3, "PNG", var4);
         if (Settings.get().isLoggingEnabled("logging.armors.generation")) {
            Log.log(ChatColor.GREEN + "Generated atlas " + var1);
         }

         return var4;
      } else {
         return null;
      }
   }

   private void updateAtlasMaxSize(ArmorTextureFrames var1) {
      for (int var2 = 0; var2 < var1.bHI.length; var2++) {
         BufferedImage var3 = var1.bHI[var2];
         if (var3 != null) {
            if (var3.getWidth() > this.bHQ.width) {
               this.bHQ.width = var3.getWidth();
            }

            if (var3.getHeight() > this.bHQ.height) {
               this.bHQ.height = var3.getHeight();
            }

            if (var1.getFrameCount(1) > this.bHV) {
               this.bHV = var1.getFrameCount(var2 + 1);
            }
         }
      }
   }

   private boolean hasEmissiveMarker(BufferedImage var1) {
      int var2 = var1.getRGB(2, 0);
      return new Color(var2).getRed() != 0;
   }

   private void prepareLayerSizes() {
      this.getBaseLayer(1, true);
      this.getBaseLayer(2, true);
   }

   private BufferedImage renderAtlas(HashMap var1, int var2) {
      if ((this.bHP.width == 0 || this.bHP.height == 0) && Settings.get().isLoggingEnabled("logging.armors.generation")) {
         Log.log(ChatColor.YELLOW + "No custom armor. Size: 0x0, Generating base layers.");
      }

      if (this.bHP.height == 0) {
         this.bHP.height = Objects.requireNonNull(this.getBaseLayer(var2, true)).getHeight();
      }

      BufferedImage var3 = this.getDefaultLayer(var2);
      if (var3 == null) {
         throw new IllegalArgumentException("Default vanilla armor layer is null!");
      }

      BufferedImage var4 = this.getBaseLayer(var2, true);
      if (var4 == null) {
         throw new IllegalArgumentException("Default vanilla armor base layer is null!");
      }

      BufferedImage var5 = new BufferedImage(this.bHP.width + var3.getWidth() + var4.getWidth(), this.bHP.height, 2);
      if (Settings.get().isLoggingEnabled("logging.armors.generation")) {
         Log.log(ChatColor.AQUA + "Armors atlas size: " + this.bHP);
      }

      Graphics2D var6 = var5.createGraphics();
      int var7 = 0;
      var6.drawImage(var3, 0, 0, null);
      setPixel(var5, var7, 1, Utils.toArgb(255, 255, 255));
      var7 += var3.getWidth();
      var6.drawImage(var4, var7, 0, null);
      setTopRowPixel(var5, var7, Utils.toArgb(160, 101, 64));
      setPixel(var5, var7, 1, bHM);
      setTopRowPixel(var5, var7 + 2, new Color(0, 1, 0).getRGB());
      var7 += var4.getWidth();

      for (ArmorTextureFrames var9 : var1.values()) {
         BufferedImage var10 = var9.getScaledLayer(var2, this.bHQ);
         BufferedImage var11 = var9.getScaledEmissiveLayer(var2, this.bHQ);
         if (var10 == null) {
            if (Settings.get().isLoggingEnabled("logging.armors.generation")) {
               if (var9.bHH == null) {
                  Log.log(ChatColor.YELLOW + "No layer texture, skip render for unknown item.");
               } else {
                  Log.log(ChatColor.YELLOW + "No layer texture, skip render for item " + var9.bHH.getNamespacedID());
               }
            }
         } else {
            var6.drawImage(var10, var7, 0, null);
            if (var9.bHH != null) {
               setMetaPixel(var5, var7, var9.bHH.apE.getRed(), var9.bHH.apE.getGreen(), var9.bHH.apE.getBlue());
               int var12 = var9.getTextureScale(var2);
               setMetaPixel(var5, var7 + 1, var12, var9.bHH.apL, var9.bHH.apM ? 1 : 0);
               setMetaPixel(var5, var7 + 2, var11 != null ? var9.bHH.apJ : 0, var9.bHH.apK ? 1 : 0, 0);
               if (Settings.get().isLoggingEnabled("logging.armors.generation")) {
                  Log.log(ChatColor.GREEN + "Added custom layer texture for render: " + var9.bHH.getNamespacedID());
               }
            } else if (Settings.get().isLoggingEnabled("logging.armors.generation")) {
               Log.log(ChatColor.GREEN + "Added custom layer texture from manually created atlas texture. RGB: " + var9.bHK);
            }

            var7 += var10.getWidth();
            if (var11 != null) {
               var6.drawImage(var11, var7, 0, null);
               var7 += var11.getWidth();
               if (Settings.get().isLoggingEnabled("logging.armors.generation")) {
                  Log.log(ChatColor.GREEN + "Added custom emissive layer texture for render: " + var9.bHH.getNamespacedID());
               }
            }
         }
      }

      var6.dispose();
      if (this.bHP.width > 16384) {
         Log.warn(ChatColor.YELLOW + "##########################");
         Log.warn(ChatColor.YELLOW + "                          ");
         Log.warn(ChatColor.YELLOW + "         Warning         ");
         Log.warn(ChatColor.YELLOW + "                          ");
         Log.warn(ChatColor.YELLOW + "##########################");
         Log.warn(ChatColor.RED + "The generated armors texture is too big " + this.bHP + "!");
         Log.warn(ChatColor.RED + "Please use smaller armor textures.");
         Log.warn(ChatColor.RED + "Most graphics cards can't support very high sized textures.");
         Log.warn(
            ChatColor.RED
               + "If your armors are not animated and you are on 1.21.2+, consider using the new component method: http://a.devs.beer/ia-armors-comp-big"
         );
         Log.warn("                          ");
         Log.warn("                          ");
         Log.warn("                          ");
      }

      return var5;
   }

   private void addSourceFile(@Nullable String var1) {
      if (var1 != null) {
         this.bIa.add(var1);
      }
   }

   private static void setPixel(BufferedImage var0, int var1, int var2, int var3) {
      var0.setRGB(var1, var2, var3);
   }

   private static void setTopRowPixel(BufferedImage var0, int var1, int var2) {
      var0.setRGB(var1, 0, var2);
   }

   private static void setMetaPixel(BufferedImage var0, int var1, int var2, int var3, int var4) {
      var0.setRGB(var1, 0, Utils.toArgb(var2, var3, var4));
   }

   @Nullable
   public static Integer findFreeRgbKey(int var0, HashMap var1) {
      int var2 = var0;
      boolean var3 = false;

      while (var1.containsKey(var0)) {
         if (!var3) {
            if (++var0 >= 16777215) {
               var0 = var2;
               var3 = true;
            }
         } else if (--var0 <= 0) {
            return null;
         }
      }

      return var0;
   }

   private void clearState() {
      this.bHR = null;
      this.bHS = null;
      this.bHP = new ArmorTextureSize();
      this.bHQ = new ArmorTextureSize();
      this.bHO = 0;
      this.bHV = 0;
   }

   public boolean isGenerated() {
      return this.bHR != null && this.bHS != null;
   }

   @Nullable
   public BufferedImage getDefaultLayer(int var1) {
      try {
         if (this.bHT[var1 - 1] == null) {
            if (Settings.get().armorOverwriteLeatherLayers) {
               this.bHT[var1 - 1] = loadResourceImage("internal/textures/models/armor/leather_layer_" + var1 + ".png", this.bHQ);
            } else {
               this.bHT[var1 - 1] = loadResourceImage("internal/textures/models/armor/leather_layer_" + var1 + "_base.png", this.bHQ);
            }
         }

         return this.bHT[var1 - 1];
      } catch (IOException var3) {
         Log.error("Failed to load default armor layer texture: " + var3.getMessage());
         return null;
      }
   }

   @Nullable
   public BufferedImage getBaseLayer(int var1, boolean var2) {
      try {
         if (this.bHU[var1 - 1] == null) {
            BufferedImage var3 = ArmorTextureFrames.loadTextureFile("assets/minecraft/textures/models/armor/leather_layer_" + var1 + ".png", null, false);

            try {
               if (var3 == null) {
                  this.bHU[var1 - 1] = loadResourceImage("internal/textures/models/armor/leather_layer_" + var1 + "_base.png", this.bHQ);
               } else if (var2) {
                  this.bHU[var1 - 1] = ArmorTextureFrames.scaleToWidth(var3, this.bHQ.width);
               } else {
                  this.bHU[var1 - 1] = var3;
               }
            } catch (IllegalArgumentException var5) {
               this.bHU[var1 - 1] = loadResourceImage("internal/textures/models/armor/leather_layer_" + var1 + "_base.png", this.bHQ);
            }
         }

         return this.bHU[var1 - 1];
      } catch (IOException var6) {
         Log.error("Failed to load brown armor base layer texture: " + var6.getMessage());
         return null;
      }
   }

   @Nullable
   private static BufferedImage loadInternalTexture(String var0) {
      return loadResourceImage(var0, null);
   }

   @Nullable
   public static BufferedImage loadResourceImage(String var0, @Nullable ArmorTextureSize var1) {
      InputStream var3 = Main.getInstance().getResource(var0);
      if (var3 == null) {
         return null;
      }

      BufferedImage var2 = ImageIO.read(new MemoryCacheImageInputStream(var3));
      if (var1 != null) {
         var2 = ArmorTextureFrames.scaleToWidth(var2, var1.width);
      }

      return var2;
   }

   private void storeImage(BufferedImage var1, String var2) {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      ImageIO.write(var1, "PNG", var3);
      this.bHZ.put(var2, var3);
   }

   public void generateOptifineArmors() {
      if (Settings.get().isLoggingEnabled("logging.armors.generation")) {
         Log.log(ChatColor.AQUA + "Generating custom Optifine armors");
      }

      try {
         this.generateBaseLeatherCit();
         this.generateCustomBaseCit();
      } catch (Exception var12) {
         Log.error("[Optifine] Failed to generate vanilla leather armor textures.");
         var12.printStackTrace();
      }

      int var1 = 0;

      for (Entry var3 : ContentManager.method8084().field92.entrySet()) {
         if (var3.getValue() instanceof ColoredItemDefinition) {
            ColoredItemDefinition var4 = (ColoredItemDefinition)var3.getValue();
            LeatherColorDefinition var5 = var4.De();
            if (var5 != null) {
               ArmorTextureFrames var6 = new ArmorTextureFrames(var5, var5.apE.asRGB());
               if (!var6.isMissing()) {
                  StringBuilder var7 = new StringBuilder();
                  var7.append("nbt.display.color=" + var4.getColor()).append("\n");
                  var7.append("type=armor").append("\n");
                  var7.append("items=").append(var4.getType().toString().toLowerCase()).append("\n");
                  String var8 = Utils.bukkitColorToHex(var5.apE, false) + "_1.png";
                  String var9 = Utils.bukkitColorToHex(var5.apE, false) + "_2.png";
                  if (!var5.apK) {
                     var7.append("texture.leather_layer_1_overlay=").append(var8).append("\n");
                     var7.append("texture.leather_layer_2_overlay=").append(var9).append("\n");
                  }

                  var7.append("texture.leather_layer_1=").append(var8).append("\n");
                  var7.append("texture.leather_layer_2=").append(var9).append("\n");
                  var7.append("weight=9999").append("\n");
                  if (this.writeCitFile(var7.toString(), this.bHX + var4.aoi + "_" + var4.aoj + ".properties", var4)) {
                     try {
                        this.storeImage(var6.getCroppedLayerChecked(1, false), this.bHX + var8);
                        this.storeImage(var6.getCroppedLayerChecked(2, false), this.bHX + var9);
                        if (var6.hasEmissive(1)) {
                           this.storeImage(var6.getCroppedEmissiveLayerChecked(1, false), this.bHX + var8.replace(".png", "_e.png"));
                        }

                        if (var6.hasEmissive(2)) {
                           this.storeImage(var6.getCroppedEmissiveLayerChecked(2, false), this.bHX + var9.replace(".png", "_e.png"));
                        }

                        var1++;
                     } catch (IOException var11) {
                        Log.error("Failed to save Optifine custom armor images: " + var4.getNamespacedID());
                        var11.printStackTrace();
                     }
                  }

                  this.writeAnimationCit(var6, false, var8, 1, var4, var5);
                  this.writeAnimationCit(var6, true, var8, 1, var4, var5);
                  this.writeAnimationCit(var6, false, var9, 2, var4, var5);
                  this.writeAnimationCit(var6, true, var9, 2, var4, var5);
               }
            }
         }
      }

      if (Settings.get().isLoggingEnabled("logging.armors.generation")) {
         Log.log(ChatColor.GREEN + "Generated " + var1 + " custom Optifine armors");
      }
   }

   private void generateBaseLeatherCit() {
      ArmorTextureFrames var1 = new ArmorTextureFrames(
         loadInternalTexture("internal/textures/models/armor/leather_layer_1_base.png"),
         loadInternalTexture("internal/textures/models/armor/leather_layer_2_base.png")
      );
      StringBuilder var2 = new StringBuilder();
      var2.append("type=armor").append("\n");
      var2.append("items=").append("leather_helmet leather_chestplate leather_leggings leather_boots").append("\n");
      String var3 = "base_1.png";
      String var4 = "base_2.png";
      var2.append("texture.leather_layer_1=").append(var3).append("\n");
      var2.append("texture.leather_layer_2=").append(var4).append("\n");
      this.writeBaseCit(var1, var2, "base_leather", var3, var4);
   }

   private void generateCustomBaseCit() {
      ArmorTextureFrames var1 = new ArmorTextureFrames(
         loadInternalTexture("internal/textures/models/armor/leather_layer_1.png"), loadInternalTexture("internal/textures/models/armor/leather_layer_2.png")
      );
      StringBuilder var2 = new StringBuilder();
      var2.append("type=armor").append("\n");
      var2.append("items=").append("leather_helmet leather_chestplate leather_leggings leather_boots").append("\n");
      String var3 = "custom_base_1.png";
      String var4 = "custom_base_2.png";
      var2.append("texture.leather_layer_1=").append(var3).append("\n");
      var2.append("texture.leather_layer_2=").append(var4).append("\n");
      var2.append("nbt.itemsadder.namespace=pattern:*").append("\n");
      var2.append("weight=9997").append("\n");
      this.writeBaseCit(var1, var2, "custom_base_leather", var3, var4);
   }

   private void writeBaseCit(ArmorTextureFrames var1, StringBuilder var2, String var3, String var4, String var5) {
      if (this.writeCitFile(var2.toString(), this.bHX + var3 + ".properties", null)) {
         try {
            this.storeImage(var1.getCroppedLayerChecked(1, false), this.bHX + var4);
            this.storeImage(var1.getCroppedLayerChecked(2, false), this.bHX + var5);
         } catch (IOException var7) {
            Log.error("Failed to save Optifine default vanilla armor images");
            var7.printStackTrace();
         }
      }
   }

   private boolean writeCitFile(String var1, String var2, @Nullable CustomItemDefinition var3) {
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();

      try {
         var4.write(var1.getBytes(StandardCharsets.UTF_8));
         this.bHZ.put(var2, var4);
         return true;
      } catch (IOException var6) {
         if (var3 != null) {
            Log.error("Failed to write Optifine CIT for armor " + var3.getNamespacedID() + ". File: " + var3.DJ());
         } else {
            Log.error("Failed to write Optifine CIT for armor. Destination: " + var2);
         }

         var6.printStackTrace();
         return false;
      }
   }

   private void writeAnimationCit(ArmorTextureFrames var1, boolean var2, String var3, int var4, CustomItemDefinition var5, LeatherColorDefinition var6) {
      if (!var2 || var1.hasEmissive(var4)) {
         StringBuilder var7 = new StringBuilder();
         int var8 = (int)(24.0F / (var6.apL / 25.0F));
         var7.append("duration=").append(var8).append("\n");
         if (var6.apM) {
            var7.append("interpolate=").append("true").append("\n");
         }

         var7.append("h=").append(var1.getFrameCountChecked(var4, false)).append("\n");
         var7.append("w=").append(var1.getLayerWidth(var4)).append("\n");
         var7.append("x=0").append("\n");
         var7.append("y=0").append("\n");
         if (var2) {
            var3 = var3.replace(".png", "_e.png");
         }

         var7.append("from=").append("./").append(var3).append("\n");
         var7.append("to=").append("minecraft:optifine/cit/ia_generated_armors/").append(var3).append("\n");
         String var9 = var5.aoi + "_" + var5.aoj + "_" + var4 + (var2 ? "_e" : "") + "_animation.properties";
         if (this.writeCitFile(var7.toString(), this.bHY + var9, var5)) {
            ByteArrayOutputStream var10 = new ByteArrayOutputStream();

            try {
               ImageIO.write(var2 ? var1.getEmissiveLayer(var4) : var1.getLayer(var4), "PNG", var10);
               this.bHZ.put(this.bHY + var3, var10);
            } catch (IOException var12) {
               Log.error("Failed to save Optifine custom armor images: " + var5.getNamespacedID());
               var12.printStackTrace();
            }
         }
      }
   }

   public void clear() {
      this.bHZ.clear();
      this.bHR = null;
      this.bHS = null;
   }

   @Nullable
   public IntVector3 getAtlasDimensions() {
      return this.readAtlasDimensions(Log::error);
   }

   @Nullable
   IntVector3 readAtlasDimensions(Consumer var1) {
      if (this.bHR == null) {
         var1.accept("Cannot calculate custom armor atlas dimensions: 'assets/minecraft/textures/models/armor/leather_layer_1.png' was not generated.");
         return null;
      }

      try (ByteArrayInputStream var2 = new ByteArrayInputStream(this.bHR.toByteArray())) {
         BufferedImage var3 = ImageIO.read(var2);
         if (var3 == null) {
            var1.accept("Cannot calculate custom armor atlas dimensions: 'assets/minecraft/textures/models/armor/leather_layer_1.png' is not a readable PNG.");
            return null;
         } else {
            return new IntVector3(var3.getWidth(), var3.getHeight(), 0);
         }
      } catch (IOException var7) {
         var1.accept("Cannot read custom armor atlas 'assets/minecraft/textures/models/armor/leather_layer_1.png': " + var7.getMessage());
         return null;
      }
   }
}
