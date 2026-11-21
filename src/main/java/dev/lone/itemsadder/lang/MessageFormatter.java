package dev.lone.itemsadder.lang;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.fontimage.EmojiReplacer;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import dev.lone.itemsadder.util.CustomHashMap;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextComponent.Builder;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.plugin.Plugin;

public class MessageFormatter {
   final ContentManager Yl;
   public final int Ym;
   public final List Yn = new ArrayList();
   public final List Yo = new ArrayList();
   public final LinkedHashMap Yp = new LinkedHashMap();
   public final HashMap Yq = new HashMap();
   private final CustomHashMap Yr = new CustomHashMap();
   private final CustomHashMap Ys = new CustomHashMap();
   public final YamlConfig Yt;

   public MessageFormatter(Plugin var1, ContentManager var2) {
      this.Yl = var2;
      this.Yn.clear();

      for (int var3 = 57344; var3 < 63488; var3++) {
         this.Yn.add((char)var3);
      }

      for (int var4 = 63536; var4 <= 63743; var4++) {
         this.Yn.add((char)var4);
      }

      for (int var5 = 10240; var5 <= 10495; var5++) {
         this.Yn.add((char)var5);
      }

      this.Ym = this.Yn.size();
      this.Yt = new YamlConfig(var1, true, "storage/font_images_unicode_cache.yml", false, false);
      if (!this.Yt.isLoaded()) {
         throw new IllegalStateException("Cannot load storage/font_images_unicode_cache.yml");
      }

      Map var6 = this.Yt.jL("");
      if (var6 != null && var6.size() > 0) {
         var6.forEach((var1x, var2x) -> {
            if (var2x instanceof Integer) {
               this.Yt.jJ(var1x);
               this.Yt.method7048(var1x, String.valueOf(this.Yn.get((Integer)var2x)));
            }
         });
      }
   }

   public void es() {
      this.Yp.clear();
      this.Yq.clear();
      this.Yr.clear();
      this.Ys.clear();
      this.Yt.reload();
   }

   public void wS() {
      for (YamlConfig var2 : this.Yl.method8092()) {
         List var3 = var2.getStringList("font_images");
         if (!var3.isEmpty()) {
            String var4 = var2.getString("info.namespace");

            for (String var6 : var3) {
               if (Settings.get().isLoggingEnabled("logging.font_images.detected")) {
                  Log.log("    Detected font image: " + var6);
               }

               if (var2.getBoolean("font_images." + var6 + ".enabled", true)) {
                  String var7 = null;
                  if (var2.contains("font_images." + var6 + ".manual_character")) {
                     var7 = var2.getString("font_images." + var6 + ".manual_character");
                  } else if (var2.contains("font_images." + var6 + ".symbol")) {
                     var7 = var2.getString("font_images." + var6 + ".symbol");
                     if (Objects.equals(var7, "")) {
                        Log.error("Error: font_image '" + var6 + "' has empty symbol. Please use a valid unicode character or remove the 'symbol'  property.");
                        var7 = null;
                     }
                  }

                  if (var7 != null) {
                     this.method9490(var4, var6, var7);
                  } else {
                     this.method9491(var4, var6);
                  }

                  Integer var8 = null;
                  Color var9 = Utils.parseBukkitColor(var2.getString("font_images." + var6 + ".shadow.color", null));
                  if (var9 != null) {
                     var8 = var9.asRGB();
                  }

                  CustomTexture var10 = new CustomTexture(
                     var2,
                     var4,
                     var6,
                     var2.getString("font_images." + var6 + ".path"),
                     var2.getInt("font_images." + var6 + ".y_position", 0),
                     var2.getInt("font_images." + var6 + ".scale_ratio", 0),
                     var2.getString("font_images." + var6 + ".permission_suffix", var2.getString("font_images." + var6 + ".permission", null)),
                     var2.getBoolean("font_images." + var6 + ".show_in_gui", false),
                     var2.getBoolean("font_images." + var6 + ".shadow.enabled", var2.getBoolean("font_images." + var6 + ".shadow", true)),
                     var8,
                     var2.getBoolean("font_images." + var6 + ".chat.hover.enabled", true),
                     var2.getString("font_images." + var6 + ".chat.hover.override_text", null),
                     var2.getBoolean("font_images." + var6 + ".suggest_in_command", true)
                  );
                  if (!var10.loadFailed) {
                     String var11 = this.method9489(var4, var6);
                     if (var11.length() > 2
                        || var11.length() == 2 && (!CustomTexture.isSurrogateChar(var11.charAt(0)) || !CustomTexture.isSurrogateChar(var11.charAt(1)))) {
                        var11 = this.method9489(var4, var6);
                        Log.error(
                           "Error: font_image '"
                              + var6
                              + "' has a bad symbol. Some emojis are not supported, and you can use only 1 character.\nForcing usage of another unicode character.File: "
                              + var2.DJ()
                        );
                     }

                     var10.setDisplayChar(var11);
                     this.Yp.put(var10.getNamespacedID(), var10);
                     this.Yq.put(var10.getId(), var10);
                     if (var10.isPrivateUse()) {
                        char var12 = var10.getDisplayText(false).charAt(0);
                        char var13 = var10.getDisplayText(false).charAt(1);
                        if (!this.Yr.containsKey(var12)) {
                           this.Yr.put(var12, new HashMap());
                        }

                        ((HashMap)this.Yr.get(var12)).put(var13, var10);
                     } else {
                        this.Ys.put(var10.getDisplayText(false).charAt(0), var10);
                     }
                  }
               }
            }
         } else if (Settings.get().isLoggingEnabled("logging.font_images.loading_empty")) {
            Log.log(ChatColor.YELLOW + "    No font_images found in file " + var2.GQ());
         }
      }

      this.Yp.forEach((var1, var2x) -> {
         if (var2x.xf()) {
            this.Yo.add(var2x.getLegacyId());
         }

         if (var2x.getCustomText() != null) {
            var2x.setCustomText(EmojiReplacer.replaceFontImages(var2x.getCustomText()));
         }
      });
   }

   public YamlConfig wT() {
      return this.Yt;
   }

   private int method9488(String var1, String var2) {
      if (this.Yt.contains(var1 + ":" + var2)) {
         return this.Yt.getInt(var1 + ":" + var2);
      }

      Map var3 = this.Yt.jL("");
      if (var3 != null && var3.size() != 0) {
         var3 = var3.entrySet()
            .stream()
            .sorted(Entry.comparingByValue(Collections.reverseOrder()))
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (var0, var1x) -> var0, LinkedHashMap::new));
         int var4 = -1;

         for (int var5 = 0; var5 < Integer.MAX_VALUE; var5++) {
            if (!var3.containsValue(var5)) {
               var4 = var5;
               break;
            }
         }

         this.Yt.method7048(var1 + ":" + var2, var4);
         return var4;
      } else {
         this.Yt.method7048(var1 + ":" + var2, 0);
         return 0;
      }
   }

   private String method9489(String var1, String var2) {
      if (this.Yt.contains(var1 + ":" + var2)) {
         return this.Yt.getString(var1 + ":" + var2);
      }

      Map var3 = this.Yt.jL("");
      if (var3 != null && var3.size() != 0) {
         HashMap var11 = new HashMap();

         for (Entry var6 : var3.entrySet()) {
            String var7 = (String)var6.getKey();
            Object var8 = var6.getValue();

            for (int var9 = 0; var9 < this.Yn.size(); var9++) {
               Character var10 = (Character)this.Yn.get(var9);
               if (var10.toString().equals(var8)) {
                  var11.put(var7, var9);
                  break;
               }
            }
         }

         Map var12 = var11.entrySet()
            .stream()
            .sorted(Entry.comparingByValue(Collections.reverseOrder()))
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (var0, var1x) -> var0, LinkedHashMap::new));
         int var13 = -1;

         for (int var14 = 0; var14 < Integer.MAX_VALUE; var14++) {
            if (!var12.containsValue(var14)) {
               var13 = var14;
               break;
            }
         }

         String var15 = ((Character)this.Yn.get(var13)).toString();
         this.Yt.method7048(var1 + ":" + var2, var15);
         return var15;
      } else {
         String var4 = ((Character)this.Yn.get(0)).toString();
         this.Yt.method7048(var1 + ":" + var2, var4);
         return var4;
      }
   }

   public void method9490(String var1, String var2, String var3) {
      if (this.Yt.contains(var1 + ":" + var2)) {
         this.Yt.jJ(var1 + ":" + var2);
      }

      this.Yt.method7048(var1 + ":" + var2, var3);
   }

   public void method9491(String var1, String var2) {
      if (this.Yt.contains(var1 + ":" + var2)) {
         String var3 = this.Yt.getString(var1 + ":" + var2);

         for (int var4 = 0; var4 < this.Yn.size(); var4++) {
            Character var5 = (Character)this.Yn.get(var4);
            if (var5.toString().equals(var3)) {
               return;
            }
         }

         this.Yt.jJ(var1 + ":" + var2);
      }
   }

   private void bA(String var1) {
      this.Yt.method7048(var1, null);
   }

   public int wU() {
      List var1 = this.Yt.getStringList("");
      return var1 != null && var1.size() != 0 ? var1.size() : 0;
   }

   private int wV() {
      ArrayList var1 = new ArrayList();

      for (String var3 : this.Yt.getStringList("")) {
         var1.add(this.Yt.getInt(var3));
      }

      return Collections.max(var1);
   }

   public CustomTexture bB(String var1) {
      return (CustomTexture)this.Yq.get(var1.toLowerCase().replace(":", ""));
   }

   @Deprecated
   @Nullable
   public CustomTexture bC(String var1) {
      for (CustomTexture var3 : this.Yp.values()) {
         if (var3.getDisplayText(false).equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public TextComponent bD(String var1) {
      Builder var2 = Component.text();
      int var3 = 0;
      int var4 = 0;

      while (var4 < var1.length()) {
         int var5 = var4 + Character.charCount(var1.codePointAt(var4));
         CustomTexture var6 = this.method9494(var1, var4);
         if (var6 != null && (!var6.hasShadow || var6.shadowColor != null)) {
            if (var3 < var4) {
               var2.append(Component.text(var1.substring(var3, var4)));
            }

            var2.append(var6.applyShadow(Component.text().content(var1.substring(var4, var5))));
            var3 = var5;
         }

         var4 = var5;
      }

      if (var3 < var1.length()) {
         var2.append(Component.text(var1.substring(var3)));
      }

      return (TextComponent)var2.build();
   }

   public String bE(String var1) {
      int var2 = 0;
      var1 = var1.replace(":", "");
      if (Utils.isNumeric(var1)) {
         return null;
      }

      var1 = var1.replace("offset_", "").replace("", "");

      try {
         var2 = Integer.parseInt(var1);
      } catch (NumberFormatException var4) {
         return null;
      }

      if (var2 == 0) {
         return null;
      } else {
         return var2 >= 0 ? (String)TextFormatCache.abS.get(Math.abs(var2)) : (String)TextFormatCache.abT.get(Math.abs(var2));
      }
   }

   @Nullable
   private CustomTexture method9492(char var1) {
      return CustomTexture.isSurrogateChar(var1) ? null : (CustomTexture)this.Ys.get(var1);
   }

   @Nullable
   private CustomTexture method9493(char var1, char var2) {
      HashMap var3 = (HashMap)this.Yr.get(var1);
      return var3 == null ? null : (CustomTexture)var3.get(var2);
   }

   @Nullable
   public CustomTexture method9494(String var1, int var2) {
      char var3 = var1.charAt(var2);
      if (var3 >= '\ud7fc' && var3 <= '\udfff') {
         if (var2 + 1 > var1.length() - 1) {
            return null;
         }

         char var4 = var1.charAt(var2 + 1);
         if (var4 >= '\ud7fc' && var4 <= '\udfff') {
            return this.method9493(var3, var4);
         }
      }

      return this.method9492(var3);
   }
}
