package dev.lone.itemsadder.gui;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.config.CategoryTreeValidator;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.lang.TextFormatCache;
import dev.lone.itemsadder.network.ContainerPacketListener;
import dev.lone.itemsadder.recipe.RecipeLoader;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import dev.lone.itemsadder.text.LoreLines;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class GuiController {
   final Plugin aIK;
   final ContentManager aIL;
   static final int aIM = 45;
   final LinkedList aIN = new LinkedList();
   final Map aIO = new HashMap();
   private final Map aIP = new LinkedHashMap();
   public final LinkedHashMap aIQ = new LinkedHashMap();
   String aeA;
   private final String aIR;
   final Component aIS;
   public static CustomTexture aIT;
   final GuiIconRegistry aIU;
   public final HashMap aIV = new HashMap();
   private final Map aIW = new WeakHashMap();
   int aIX;

   public GuiController(Plugin var1, ContentManager var2, GuiIconRegistry var3) {
      this.aIK = var1;
      this.aIL = var2;
      this.aIU = var3;
      this.aeA = Main.aFi.lj("ia-title-menu-categories");
      this.aIS = AdventureText.fromLegacy(LoreLines.method2725(var3.aJU.jV("settings.category_view.title_color"), Main.aFi.lj("ia-search-results")));
      int var4 = var3.aJU.getInt("settings.categories_view.title_position_pixels");
      int var5 = var3.aJU.getInt("settings.categories_view.font_image.x_position_pixels");
      this.aeA = var3.aJU.jX("settings.categories_view.title_color") + this.aeA;
      this.aIR = this.aeA;
      if (aIT == null) {
         Log.error(
            ChatColor.RED + "GUI error: can't find font_image. Check ia_gui.yml, attribute: " + ChatColor.AQUA + "settings.categories_view.font_image.name"
         );
      } else {
         this.aeA = ChatColor.WHITE + TextFormatCache.method9634(aIT.getFormattedText(), var5) + TextFormatCache.method9634(this.aeA, -aIT.getWidth() + var4);
      }
   }

   public GuiPage eF(String var1) {
      return (GuiPage)this.aIO.get(var1);
   }

   public LinkedList LO() {
      return this.aIN;
   }

   public List LP() {
      return new ArrayList(this.aIO.keySet());
   }

   public void LQ() {
      for (CustomItemDefinition var2 : this.aIL.field92.values()) {
         if (var2 == null) {
            if (Settings.get().isLoggingEnabled("logging.categories.missing_items")) {
               Log.log(ChatColor.YELLOW + "Null item in the category item registry for ia_gui.yml");
            }
         } else if (!(var2 instanceof CustomBlockDefinition var3 && var3.anF != null && var3.anF.Fx()) && var2.DZ()) {
            RecipeLoader var4 = new RecipeLoader(this.aIU, this, var2, var2.lH().getBoolean("items." + var2.getId() + ".click_in_ia_gui", true));
            this.aIQ.put(var2.getNamespacedID(), var4);
         }
      }
   }

   public void LR() {
      for (Entry var2 : this.aIQ.entrySet()) {
         ((RecipeLoader)var2.getValue()).LR();
      }
   }

   public void LS() {
      int var1 = 0;
      YamlConfig var2 = this.aIU.aJU;

      for (String var4 : var2.getStringList("categories")) {
         String var5 = "categories." + var4;
         if (var2.getBoolean(var5 + ".enabled", true)) {
            String var6 = var2.getString(var5 + ".icon");
            ItemStack var7 = this.aIL.method8179(var6);
            Component var8 = null;
            if (var2.contains(var5 + ".name")) {
               String var9 = var2.getString(var5 + ".name", null);
               if (var9 != null) {
                  if (ContentManager.method8084().bz.hasKey(var9)) {
                     var8 = ContentManager.method8084().bz.getComponent(var9);
                  } else {
                     var8 = AdventureText.parseRichText(var9);
                  }
               }
            }

            Component var15 = null;
            if (var2.contains(var5 + ".title")) {
               String var10 = var2.getString(var5 + ".title", null);
               if (var10 != null) {
                  if (ContentManager.method8084().bz.hasKey(var10)) {
                     var15 = ContentManager.method8084().bz.getComponent(var10);
                  } else {
                     var15 = AdventureText.parseRichText(var10);
                  }
               }
            }

            if (var8 == null) {
               var8 = Component.empty().append(var15);
            }

            if (var15 == null) {
               var15 = var8;
            }

            if (var8 == null && var15 == null) {
               var15 = AdventureText.parseRichText(var4);
               var8 = AdventureText.parseRichText(var4);
            }

            GuiPage var16 = new GuiPage(
               var4, var2.getString(var5 + ".permission_suffix", null), var2.getString(var5 + ".permission", null), this, var7, var8, var15, var6, var2.DJ()
            );
            if (var2.getBoolean(var5 + ".show_all_items")) {
               var16.aJp.addAll(this.aIQ.values());
               var16.bs(true);
            } else {
               for (String var12 : var2.jZ(var5 + ".items")) {
                  CustomItemDefinition var13 = this.aIL.method8154(var12);
                  if (var13 == null) {
                     if (Settings.get().isLoggingEnabled("logging.categories.missing_items")) {
                        Log.log(ChatColor.YELLOW + "Item '" + ChatColor.AQUA + var12 + ChatColor.YELLOW + " 'not found for ia_gui.yml");
                     }
                  } else {
                     var16.method2525(var13.getNamespacedID(), (RecipeLoader)this.aIQ.get(var13.getNamespacedID()));
                  }
               }
            }

            if (var2.contains(var5 + ".hide_items")) {
               for (String var19 : var2.jZ(var5 + ".hide_items")) {
                  CustomItemDefinition var14 = ContentManager.method8084().method8154(var19);
                  if (var14 != null) {
                     var16.eG(var14.getNamespacedID());
                  } else if (Settings.get().isLoggingEnabled("logging.categories.missing_items")) {
                     Log.log(ChatColor.YELLOW + "Item '" + ChatColor.AQUA + var19 + ChatColor.YELLOW + " 'not found for ia_gui.yml");
                  }
               }
            }

            this.aIN.add(var16);
            this.aIO.put(var4, var16);
            var1++;
         }
      }

      if (Settings.get().isLoggingEnabled("logging.categories.file_loaded")) {
         Log.log(ChatColor.GREEN + "[Content] Loaded " + var1 + " categories from (" + var2.DJ() + ")");
      }
   }

   public void LT() {
      for (YamlConfig var2 : ContentManager.method8084().method8092()) {
         if (var2.contains("categories")) {
            int var3 = 0;

            for (String var5 : var2.getStringList("categories")) {
               String var6 = "categories." + var5;
               GuiPage var7;
               if (!this.aIO.containsKey(var5)) {
                  if (!var2.getBoolean(var6 + ".enabled", true)) {
                     continue;
                  }

                  String var8 = var2.getString(var6 + ".icon", null);
                  ItemStack var9 = this.aIL.method8179(var8);
                  Component var10 = null;
                  if (var2.contains(var6 + ".name")) {
                     String var11 = var2.getString(var6 + ".name", null);
                     if (var11 != null) {
                        if (ContentManager.method8084().bz.hasKey(var11)) {
                           var10 = ContentManager.method8084().bz.getComponent(var11);
                        } else {
                           var10 = AdventureText.parseRichText(var11);
                        }
                     }
                  }

                  Component var27 = null;
                  if (var2.contains(var6 + ".title")) {
                     String var12 = var2.getString(var6 + ".title", null);
                     if (var12 != null) {
                        if (ContentManager.method8084().bz.hasKey(var12)) {
                           var27 = ContentManager.method8084().bz.getComponent(var12);
                        } else {
                           var27 = AdventureText.parseRichText(var12);
                        }
                     }
                  }

                  if (var10 == null) {
                     var10 = var27;
                  }

                  if (var27 == null) {
                     var27 = var10;
                  }

                  if (var10 == null) {
                     var27 = AdventureText.parseRichText(var5);
                     var10 = AdventureText.parseRichText(var5);
                  }

                  var7 = new GuiPage(
                     var5,
                     var2.getString(var6 + ".permission_suffix", null),
                     var2.getString(var6 + ".permission", null),
                     this,
                     var9,
                     var10,
                     var27,
                     var8,
                     var2.DJ()
                  );
                  var7.method2530(var2.getString(var6 + ".font_image.name", null), var2.getInt(var6 + ".font_image.x_position_pixels", -1));
                  var7.method2531(var2.getInt(var6 + ".title_position_pixels", -1));
                  this.aIN.add(var7);
                  this.aIO.put(var5, var7);
                  this.aIP.put(var5, var2.getString(var6 + ".parent", null));
                  var3++;
               } else {
                  if (var2.getBoolean(var6 + ".skip_if_already", false)) {
                     if (Settings.get().isLoggingEnabled("logging.categories.single_skip")) {
                        Log.log(ChatColor.YELLOW + "Category '" + var5 + "' already exists. Skipped because skip_if_already=true (" + var2.DJ() + ")");
                     }
                     continue;
                  }

                  var7 = (GuiPage)this.aIO.get(var5);
                  if (!var7.LW()) {
                     String var17 = var2.getString(var6 + ".icon", null);
                     ItemStack var20 = this.aIL.method8179(var17);
                     var7.ab(var20);
                  }

                  if (Settings.get().isLoggingEnabled("logging.categories.single_loaded_overwrite")) {
                     Log.log(ChatColor.GREEN + "Category '" + var5 + "' already exists. Adding items to it (" + var2.DJ() + ")");
                  }
               }

               for (String var21 : var2.jZ(var6 + ".items")) {
                  if (var21.startsWith("*") || var21.endsWith("*")) {
                     for (Entry var28 : this.aIL.field92.entrySet()) {
                        CustomItemDefinition var31 = (CustomItemDefinition)var28.getValue();
                        if (!(var31 instanceof CustomBlockDefinition var13 && var13.anF != null && var13.anF.Fx())
                           && FilenameUtils.wildcardMatch(var31.aok, var21)
                           && !var7.eH(var31.getNamespacedID())) {
                           var7.method2525(var31.getNamespacedID(), (RecipeLoader)this.aIQ.get(var31.getNamespacedID()));
                        }
                     }
                  } else if (Utils.isValidRegex(var21)) {
                     Pattern var25 = Pattern.compile(var21);

                     for (Entry var32 : this.aIL.field92.entrySet()) {
                        CustomItemDefinition var33 = (CustomItemDefinition)var32.getValue();
                        if (!(var33 instanceof CustomBlockDefinition var14 && var14.anF != null && var14.anF.Fx())
                           && var25.matcher(var33.aok).matches()
                           && !var7.eH(var33.getNamespacedID())) {
                           var7.method2525(var33.getNamespacedID(), (RecipeLoader)this.aIQ.get(var33.getNamespacedID()));
                        }
                     }
                  } else {
                     CustomItemDefinition var24 = this.aIL.method8154(var21);
                     if (var24 == null) {
                        if (Settings.get().isLoggingEnabled("logging.categories.missing_items")) {
                           Log.log(ChatColor.YELLOW + "Item '" + ChatColor.AQUA + var21 + ChatColor.YELLOW + " 'not found for ia_gui.yml");
                        }
                     } else if (!(var24 instanceof CustomBlockDefinition var29 && var29.anF != null && var29.anF.Fx()) && !var7.eH(var24.getNamespacedID())) {
                        var7.method2525(var24.getNamespacedID(), (RecipeLoader)this.aIQ.get(var24.getNamespacedID()));
                     }
                  }
               }

               if (var2.contains(var6 + ".hide_items")) {
                  for (String var26 : var2.jZ(var6 + ".hide_items")) {
                     var7.eG(var26);
                  }
               }

               if (Settings.get().isLoggingEnabled("logging.categories.single_loaded")) {
                  Log.log(ChatColor.GREEN + "Loaded category '" + var5 + "' (" + var2.DJ() + ")");
               }
            }

            if (Settings.get().isLoggingEnabled("logging.categories.file_loaded")) {
               Log.log(ChatColor.GREEN + "[Content] Loaded " + ChatColor.AQUA + var3 + ChatColor.GREEN + " categories (" + var2.DJ() + ")");
            }
         }
      }

      if (Settings.get().isLoggingEnabled("logging.categories.icon_failed_load")) {
         for (GuiPage var16 : this.aIN) {
            if (!var16.LW()) {
               Log.warn("Failed to find icon '" + var16.aJu + "' for category '" + var16.aJm + "'. File: " + var16.aJv);
            }

            var16.aJu = null;
            var16.aJv = null;
         }
      }
   }

   public void LU() {
      if (Settings.get().isLoggingEnabled("logging.categories.total_loaded")) {
         Log.log(ChatColor.GREEN + "[Content] Loaded " + ChatColor.AQUA + this.aIN.size() + ChatColor.GREEN + " categories");
      }
   }

   public void bT(Player var1) {
      this.method2475(var1, false);
   }

   public void method2475(Player var1, boolean var2) {
      List var3 = this.aIN.stream().filter(var0 -> var0.aJh == null).toList();
      if (this.aIU.method2571(var1, aIT)) {
         ArrayList var9 = new ArrayList();

         for (GuiPage var11 : var3) {
            if (var11.method2527(var1)) {
               var11.LX();
               var9.add(new GuiButton(var11.LZ(), var3x -> var11.method2529(var1, var2)));
            }
         }

         ContainerPacketListener.method2603(
            var1,
            this.aIU.method2573(this.aIR, var9.size()),
            var9,
            PermissionNodes.bJ(var1) ? GuiIconRegistry.aKj : null,
            var2x -> this.bW(var1),
            this.aIW.getOrDefault(var1, 0),
            var2x -> this.aIW.put(var1, var2x),
            var0 -> {},
            this.aIU.bY(var1),
            true,
            this.aIU.method2574(this.aIR, var9.size(), true),
            null
         );
      } else {
         int var4;
         if (!this.aIV.containsKey(var1.getUniqueId())) {
            this.aIV.put(var1.getUniqueId(), 0);
            var4 = 0;
         } else {
            var4 = (Integer)this.aIV.get(var1.getUniqueId());
         }

         int var5 = var4 * 45;
         this.aIX = Utils.ceilDivide(var3.size(), 45.0F);
         CustomInventoryHolder var6 = new CustomInventoryHolder(54, this.aeA);

         for (int var7 = var5; var7 < var5 + 45 && var7 < var3.size(); var7++) {
            if (((GuiPage)var3.get(var7)).method2527(var1)) {
               ((GuiPage)var3.get(var7)).LX();
               int var8 = var7;
               var6.method2667(((GuiPage)var3.get(var7)).LZ(), var3x -> ((GuiPage)var3.get(var8)).method2529((Player)var3x.getWhoClicked(), var2));
            }
         }

         if (PermissionNodes.bJ(var1)) {
            var6.method2666(49, GuiIconRegistry.aKq, var2x -> this.bW(var1));
         }

         if (var4 > 0) {
            var6.method2666(52, GuiIconRegistry.aKo, var2x -> {
               this.bU(var1);
               this.bT(var1);
            });
         }

         if (var4 + 1 < this.aIX) {
            var6.method2666(53, GuiIconRegistry.aKp, var2x -> {
               this.bV(var1);
               this.bT(var1);
            });
         }

         var6.open(var1);
      }
   }

   void bU(Player var1) {
      if (this.aIV.containsKey(var1.getUniqueId())) {
         int var2 = (Integer)this.aIV.get(var1.getUniqueId());
         if (var2 - 1 >= 0) {
            this.aIV.put(var1.getUniqueId(), var2 - 1);
         }
      } else {
         this.aIV.put(var1.getUniqueId(), 0);
      }
   }

   void bV(Player var1) {
      if (this.aIV.containsKey(var1.getUniqueId())) {
         int var2 = (Integer)this.aIV.get(var1.getUniqueId());
         if (var2 + 1 < this.aIX) {
            this.aIV.put(var1.getUniqueId(), var2 + 1);
         }
      } else {
         this.aIV.put(var1.getUniqueId(), 0);
      }
   }

   public List method2476(String var1, Player var2) {
      var1 = var1.toLowerCase();
      boolean var3 = PermissionNodes.bK(var2);
      ArrayList var4 = new ArrayList();

      for (Entry var6 : this.aIQ.entrySet()) {
         try {
            CustomItemDefinition var7 = ((RecipeLoader)var6.getValue()).Mc();
            if (var7.getNamespacedID().toLowerCase().contains(var1) || ChatColor.stripColor(var7.getItemMeta().getDisplayName().toLowerCase()).contains(var1)) {
               String var8 = var7.getPermissionSuffix();
               if (var8 == null && var3 || PermissionNodes.method1621(var2, var8)) {
                  var4.add((RecipeLoader)var6.getValue());
               }
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }
      }

      return var4;
   }

   public void bW(Player var1) {
      this.method2477(var1, "");
   }

   void method2477(Player var1, String var2) {
      Main.getInstance().aFj.method1823(var1, Arrays.asList(var2, "", "", "")).MN().method2727((var1x, var2x) -> {
         String var3 = LoreLines.method2724(var2x);
         if (var3.isEmpty()) {
            Bukkit.getScheduler().runTaskLater(this.aIK, () -> this.bT(var1x), 0L);
            return true;
         } else {
            GuiPage var4 = new GuiPage("__internal_search_results", null, null, this, null, this.aIS, this.aIS, null, null);
            var4.aJd = var3;
            var4.aJp.addAll(this.method2476(var3, var1x));
            Bukkit.getScheduler().runTaskLater(this.aIK, () -> {
               if (var4.aJp.isEmpty()) {
                  this.method2478(var1x, var4, var3);
               } else {
                  var4.method2529(var1x, false);
               }
            }, 0L);
            return true;
         }
      }).Mn();
   }

   void method2478(Player var1, GuiPage var2, String var3) {
      var2.aJn = AdventureText.legacySerializer
         .deserialize(LoreLines.method2725(this.aIU.aJU.jV("settings.category_view.title_color"), Main.aFi.lj("ia-search-empty-title")));
      ItemStack var4 = new ItemStack(Material.PAPER);
      ItemMeta var5 = var4.getItemMeta();
      var5.setDisplayName(Main.aFi.lj("ia-search-no-results").replace("{query}", var3));
      var5.setLore(Collections.singletonList(Main.aFi.lj("ia-search-retry")));
      var4.setItemMeta(var5);
      boolean var6 = PermissionNodes.bJ(var1);
      Consumer var7 = var3x -> {
         if (PermissionNodes.bJ(var1)) {
            this.method2477(var1, var3);
         }
      };
      if (!this.aIU.method2571(var1, aIT)) {
         CustomInventoryHolder var10 = new CustomInventoryHolder(54, var2.getTitle());
         var10.method2666(22, var4, var7);
         if (var6) {
            var10.method2666(48, GuiIconRegistry.aKq, var7);
         }

         var10.method2666(49, GuiIconRegistry.aKn, var2x -> this.bT(var1));
         var10.open(var1);
      } else {
         ArrayList var8 = new ArrayList(23);

         for (int var9 = 0; var9 < 22; var9++) {
            var8.add(new GuiButton(new ItemStack(Material.AIR), null));
         }

         var8.add(new GuiButton(var4, var7));
         ContainerPacketListener.method2603(
            var1,
            this.aIU.method2573(Utils.componentToLegacy(var2.aJn), 1),
            var8,
            var6 ? GuiIconRegistry.aKj : null,
            var7,
            0,
            var0 -> {},
            var0 -> {},
            this.aIU.bY(var1),
            true,
            this.aIU.method2574(Utils.componentToLegacy(var2.aJn), 1, true),
            var2x -> this.bT(var1)
         );
      }
   }

   public void LV() {
      boolean var1 = GuiIconRegistry.aKr;
      boolean var2 = GuiIconRegistry.aKs;
      if (var2) {
         ArrayList var3 = new ArrayList();

         for (String var5 : GuiIconRegistry.aKt) {
            GuiPage var6 = (GuiPage)this.aIO.get(var5);
            if (var6 != null && this.aIN.contains(var6)) {
               var3.add(var6);
            }
         }

         ArrayList var10 = new ArrayList();

         for (GuiPage var18 : this.aIN) {
            if (!var3.contains(var18)) {
               var10.add(var18);
            }
         }

         if (var1) {
            var10.sort(Comparator.comparing(var0 -> var0.aJc.toLowerCase()));
         }

         var3.addAll(var10);
         this.aIN.clear();
         this.aIN.addAll(var3);
      } else if (var1) {
         this.aIN.sort(Comparator.comparing(var0 -> var0.aJc.toLowerCase()));
      }

      if (!GuiIconRegistry.aKt.contains("all")) {
         ArrayList var8 = new ArrayList();
         ListIterator var11 = this.aIN.listIterator();

         while (var11.hasNext()) {
            GuiPage var15 = (GuiPage)var11.next();
            if (var15.LY()) {
               var8.add(var15);
               var11.remove();
            }
         }

         this.aIN.addAll(var8);
      }

      LinkedHashMap var9 = new LinkedHashMap();

      for (GuiPage var16 : this.aIN) {
         var16.aJh = null;
         var16.aJi.clear();
         var9.put(var16.aJc, (String)this.aIP.get(var16.aJc));
      }

      Map var13 = CategoryTreeValidator.method2545(var9, Log::warn);

      for (GuiPage var19 : this.aIN) {
         String var7 = (String)var13.get(var19.aJc);
         if (var7 != null) {
            var19.aJh = (GuiPage)this.aIO.get(var7);
            var19.aJh.aJi.add(var19);
         }
      }
   }
}
