package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.Version;
import com.google.common.collect.ImmutableMap;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.ExecutableItems.ExecutableItemsHook;
import dev.lone.itemsadder.Core.OtherPlugins.enchants.AeWrapper;
import dev.lone.itemsadder.Core.OtherPlugins.enchants.AeWrapper.PendingEnchantException;
import dev.lone.itemsadder.Core.OtherPlugins.mmoitems.MmoItemsHook;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.block.CustomBlockStateSettings;
import dev.lone.itemsadder.block.InventoryHookLoader;
import dev.lone.itemsadder.config.ConfigFlagParser;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.ItemConfigFactory;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.config.YamlConfigurationBuilder;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespacedElement;
import dev.lone.itemsadder.core.NamespacedElementWithEffects;
import dev.lone.itemsadder.core.NamespacedId;
import dev.lone.itemsadder.entity.EntityTypeFilter;
import dev.lone.itemsadder.fontimage.EmojiReplacer;
import dev.lone.itemsadder.internal.SwitchMap1;
import dev.lone.itemsadder.resourcepack.ResourcePackWriter;
import dev.lone.itemsadder.text.ColorTagParser;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.BossBarManager;
import dev.lone.itemsadder.util.BossBarSpec;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.PluginException;
import dev.lone.itemsadder.util.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;
import org.jetbrains.annotations.NotNull;

public class ItemDefinitionLoader {
   private final ContentManager asd;
   private final Set ase = new HashSet();
   HashMap asf = new HashMap();
   public boolean asg = false;

   public ItemDefinitionLoader(ContentManager var1) {
      this.asd = var1;
   }

   public void cD() {
      ItemLoadSession var1 = this.FY();

      while (var1.hasRemainingFiles()) {
         var1.loadNextFile();
      }

      var1.finish();
   }

   public ItemLoadSession FY() {
      this.ase.clear();
      ArrayList var1 = new ArrayList(this.asd.method8092());
      HashMap var2 = new HashMap();

      for (YamlConfig var4 : var1) {
         String var5 = var4.getString("info.namespace");
         this.method10115(var4, var5);
         List var6 = var4.getStringList("items");
         if (!var6.isEmpty()) {
            for (String var8 : var6) {
               ConfigSectionWrapper var9 = var4.kc("items." + var8);
               assert var9 != null;
               if (var9.method7078("enabled", true)) {
                  boolean var10 = var9.method7078("template", false);
                  if (var10) {
                     var2.put(var5 + ":" + var8, var9);
                  }
               }
            }
         }
      }

      this.asd.method8210(var2);
      return new ItemLoadSession(this, var1, var2);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void method10113(YamlConfig var1, Map var2) {
      String var3 = var1.getString("info.namespace");
      this.asd.as.add(var3);
      List var4 = var1.getStringList("items");
      if (var4.isEmpty()) {
         if (Settings.get().isLoggingEnabled("logging.items.loading_empty")) {
            Log.log(ChatColor.YELLOW + "    No item found in file " + var1.GQ());
         }
      } else {
         BossBarManager.setTitleKeepProgress("Items '" + var3 + "'...");
         boolean var5 = false;
         boolean var6 = false;
         LinkedHashMap var7 = new LinkedHashMap();

         for (String var9 : var4) {
            ConfigSectionWrapper var10 = var1.kc("items." + var9);
            assert var10 != null;
            if (!var10.method7078("enabled", true)) {
               if (Settings.get().isLoggingEnabled("logging.items.loading_disabled")) {
                  Log.log("    Ignored disabled item: " + var9 + ", namespace: " + var3);
               }
            } else {
               boolean var11 = var10.method7078("template", false);
               if (!var11) {
                  if (Settings.get().isLoggingEnabled("logging.items.loading")) {
                     Log.log("    Loading item: " + var9);
                  }

                  var10 = method10116(var1, var10, var2, var3, var9);
                  if (var10.jI("behaviours.furniture") && var10.method7078("behaviours.furniture.enabled", true)) {
                     EntityType var12 = var10.method7109("behaviours.furniture.entity", EntityType.ARMOR_STAND, null);
                     boolean var13 = var10.method7078("behaviours.furniture.small_hitbox", var10.method7078("behaviours.furniture.small", false));
                     EntityTypeFilter var14 = (EntityTypeFilter)this.asd.field93.get(var3 + ":" + var9);
                     if (var14 != null && var12 != null && (var14.entityType() != var12 || var12 == EntityType.ARMOR_STAND && var14.included() != var13)) {
                        String var15 = "items." + var9 + ".behaviours.furniture.display_transformation";
                        boolean var16 = false;
                        if (var14.entityType() == EntityType.ITEM_DISPLAY
                           && var10.jI("behaviours.furniture.display_transformation")
                           && !var10.jI("behaviours.furniture.display_transformation.adjust_legacy_model")
                           && !var10.jI("behaviours.furniture.display_transformation.source_entity")) {
                           var1.method7048(var15 + ".source_entity", "item_display");
                           var16 = true;
                        } else if (!var10.jI("behaviours.furniture.display_transformation")) {
                           String var17 = switch (SwitchMap1.asi[var14.entityType().ordinal()]) {
                              case 1 -> var14.included() ? "small_armor_stand" : "armor_stand";
                              case 2, 3 -> "item_frame";
                              case 4 -> "item_display";
                              default -> null;
                           };
                           if (var17 != null) {
                              var1.method7048(var15 + ".source_entity", var17);
                              var16 = true;
                           }
                        }

                        if (var16) {
                           var6 = true;
                           var10 = method10116(var1, var1.kc("items." + var9), var2, var3, var9);
                        }
                     }
                  }

                  if (!ResourcePackWriter.hasValidPathChars(var9)) {
                     Log.error("[Items] Non [a-z0-9_.-] character in custom item name '" + var3 + ":" + var9 + "' in yml file: " + var1.DJ());
                     ResourcePackWriter.printAllowedCharacters();
                  } else if ((var10.hA("resource") ? 1 : 0) + (var10.hA("graphics") ? 1 : 0) + (var10.hA("item_model") ? 1 : 0) > 1) {
                     Log.error(
                        "[Items] '"
                           + var3
                           + ":"
                           + var9
                           + "'. Error: 'resource', 'graphics' and 'item_model' properties are mutually exclusive. Please use only one of them. File: "
                           + var1.DJ()
                     );
                  } else {
                     Material var38 = var10.method7105("resource.material", var10.jS("material"));
                     String var39 = var10.bc("name", var10.bc("display_name", dm(var9)));
                     Component var40 = this.asd.bz.getComponent(var39);
                     CustomItemDefinition var41 = null;
                     if (ArmorItemHelper.isLegacyArmorConfig(var10)) {
                        try {
                           ArmorConversionResult var42 = ArmorItemHelper.createLegacyArmor(var38, var10, var1, var3, var9);
                           var41 = var42.Gf();
                           var5 |= var42.Gg();
                           var38 = var42.Gh();
                        } catch (IllegalArgumentException var36) {
                           var7.put(var3 + ":" + var9, var36.getMessage());
                           this.ase.add(var3 + ":" + var9);
                           continue;
                        }
                     }

                     boolean var43 = false;
                     boolean var44 = false;
                     if (var38 == null) {
                        var38 = Material.PAPER;
                        var43 = true;
                        var44 = true;
                     }

                     if (var41 == null) {
                        if (!var38.isItem()) {
                           Log.error(
                              "[Items] '"
                                 + var3
                                 + ":"
                                 + var9
                                 + "' - Error: Material "
                                 + ChatColor.AQUA
                                 + var38
                                 + ChatColor.RED
                                 + " is not an obtainable material. File: "
                                 + var1.DJ()
                           );
                           continue;
                        }

                        if (var38 == Material.BOW) {
                           var41 = new SimpleItemDefinition(
                              var1,
                              var3,
                              var9,
                              var38,
                              var10.method7078("specific_properties.bow.glow_arrows", false),
                              var10.method7078("specific_properties.bow.fire_arrows", false),
                              var10.method7080("specific_properties.bow.arrow_damage", -1.0),
                              var10.method7080("specific_properties.bow.arrow_speed", 1.0)
                           );
                        } else if (var38 == Material.CROSSBOW) {
                           var41 = new SimpleItemDefinitionB(
                              var1,
                              var3,
                              var9,
                              var38,
                              var10.method7078("specific_properties.crossbow.glow_arrows", false),
                              var10.method7078("specific_properties.crossbow.fire_arrows", false),
                              var10.method7080("specific_properties.crossbow.arrow_damage", -1.0),
                              var10.method7080("specific_properties.crossbow.arrow_speed", 1.0),
                              var10.method7078("specific_properties.crossbow.multishot", false),
                              var10.method7078("specific_properties.crossbow.firework", false)
                           );
                        } else if (ItemConfigFactory.method10049(var10)) {
                           try {
                              if (!var44 && var38 == Material.PAPER && Settings.get().useBlockMaterialInInventory) {
                                 var43 = true;
                              }

                              var41 = ItemConfigFactory.method10050(var10, var1, var3, var9, var38, var43);
                              var38 = var41.getType();
                           } catch (IllegalStateException var35) {
                              Log.error(var35.getMessage());
                              continue;
                           }
                        } else if (!var10.jI("specific_properties.seed.crop_block")) {
                           var41 = new CustomItemDefinition(var1, var3, var9, var38);
                        } else {
                           var41 = new PlantItemDefinition(var1, var3, var9, var38, var10.jS("specific_properties.seed.crop_block"));
                           this.asd.ab.put(var41.aok, (PlantItemDefinition)var41);
                           ArrayList var18 = new ArrayList();

                           for (String var20 : var10.jK("specific_properties.seed.results")) {
                              ItemVariantHolder var21 = new ItemVariantHolder(
                                 var10.method7076("specific_properties.seed.results." + var20 + ".min_amount", 1),
                                 var10.method7076("specific_properties.seed.results." + var20 + ".max_amount", 1),
                                 var10.method7102("specific_properties.seed.results." + var20 + ".chance", 100.0F),
                                 var10.method7078("specific_properties.seed.results." + var20 + ".ignore_fortune", false)
                              );
                              ((PlantItemDefinition)var41).method8837(var21);
                              String var22 = var10.jO("specific_properties.seed.results." + var20 + ".item");
                              var18.add(var22);
                           }

                           this.asf.put(var41.getNamespacedID(), var18);
                        }

                        if (ConfigFlagParser.method10054(var10)) {
                           ConfigFlagParser.method10055(var41, var10, var1, var3, var9);
                        }
                     }

                     if (ItemAttributeLoader.method10102(var10) && !ItemRuleLoader.method10071(var10)) {
                        try {
                           ItemAttributeLoader.method10103(var41, var10, var1, var3, var9);
                        } catch (IllegalArgumentException var34) {
                           var7.put(var3 + ":" + var9, var34.getMessage());
                           this.ase.add(var3 + ":" + var9);
                           continue;
                        }
                     }

                     if (var10.jI("tooltip_style")) {
                        try {
                           if (!Version.isAtLeast(Version.v1_21_3)) {
                              throw new PluginException(
                                 "[Items] '" + var3 + ":" + var9 + "'. Error: 'tooltip_style' is only available in 1.21.2+. File: " + var1.DJ()
                              );
                           }

                           NamespacedKey var45 = YamlConfig.method7036(
                              var10.jO("tooltip_style"),
                              var3,
                              false,
                              var3x -> Log.error(
                                 "[Items] '" + var3 + ":" + var9 + "'. Error: Invalid 'tooltip_style' value '" + var3x + "'. File: " + var1.DJ()
                              )
                           );
                           if (var45 != null) {
                              File var50 = ContentManager.method8198(ContentManager.method8194(null, var45 + "_background"));
                              if (var50 == null) {
                                 throw new PluginException(
                                    "Can't find background '" + var45 + "' texture file for item '" + var41.getNamespacedID() + "'. File: " + var1.DJ()
                                 );
                              }

                              File var53 = ContentManager.method8198(ContentManager.method8194(null, var45 + "_frame"));
                              if (var53 == null) {
                                 throw new PluginException(
                                    "Can't find frame '" + var45 + "' texture file for item '" + var41.getNamespacedID() + "'. File: " + var1.DJ()
                                 );
                              }

                              var41.method8797(var45);
                              ContentManager.method8084().ac().add(var45);
                           }
                        } catch (PluginException var32) {
                           Log.error(var32.getMessage());
                        }
                     }

                     if (var10.jI("jukebox_disc")) {
                        if (!Version.isAtLeast(Version.v1_21_1)) {
                           Log.error("[Items] '" + var3 + ":" + var9 + "'. Error: 'jukebox_playable' is only available in 1.21.1+. File: " + var1.DJ());
                           continue;
                        }

                        NamespacedKey var46 = YamlConfig.method7036(
                           var10.jO("jukebox_disc.song"),
                           var3,
                           false,
                           var3x -> Log.error(
                              "[Items] '" + var3 + ":" + var9 + "'. Error: Invalid 'jukebox_disc.song' value '" + var3x + "'. File: " + var1.DJ()
                           )
                        );
                        if (var46 != null) {
                           var41.method8798(var46);
                        }
                     }

                     var41.method8793(var10);
                     var41.method8774(var40);
                     var41.method8773(var40);
                     if (ItemNbtLoader.hasNbtComponents(var10)) {
                        ItemNbtLoader.loadNbtComponents(var41, var10, var1);
                     }

                     if ((!ItemConfigValidator.method10069(var10) || ItemConfigValidator.method10070(var41, var10, var1, var3, var9))
                        && ItemConfigResolver.method10108(var41, var10, var1, var3, var9)) {
                        if (ItemRuleLoader.method10071(var10)) {
                           try {
                              ItemRuleLoader.method10072(var41, var10, var1, var3, var9);
                           } catch (IllegalArgumentException var33) {
                              Log.error("[Items] '" + var3 + ":" + var9 + "'. Error: " + var33.getMessage());
                              continue;
                           }
                        }

                        if (PotionItemLoader.hasPotionEffects(var41)) {
                           PotionItemLoader.loadPotionEffects(var41, var10, var1);
                        } else if (var41.getType() == Material.LEATHER_HORSE_ARMOR) {
                           String var47 = var10.bc("specific_properties.leather_horse_armor.color", "WHITE");
                           int var51 = Utils.colorToRgbInt(var47);
                           if (var51 == -1) {
                              Log.error("[Items] '" + var41.getNamespacedID() + "'. Missing leather_horse_armor.color '" + var47 + "'. File: " + var1.DJ());
                           }

                           var41.aoy = var10.method7078("specific_properties.leather_horse_armor.can_be_recolored_in_crafting_table", false);
                           LeatherArmorMeta var54 = (LeatherArmorMeta)var41.getItemMeta();
                           var54.addAttributeModifier(
                              Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", 0.0, Operation.ADD_NUMBER, EquipmentSlot.BODY)
                           );
                           var54.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
                           if (var51 != -1) {
                              var54.setColor(Color.fromRGB(var51));
                           }

                           var54.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_DYE});
                           var41.setItemMeta(var54);
                        } else if (var41.getType() == Material.WRITTEN_BOOK) {
                           BookMetaUtils.setBookTitle(var41, AdventureText.toPlainText(var40));
                           BookMetaUtils.setBookAuthor(var41, var10.bc("book.author", var10.bc("specific_properties.book.author", " ")));
                           BookMetaUtils.setBookGeneration(
                              var41, Generation.valueOf(var10.bc("book.generation", var10.bc("specific_properties.book.generation", "TATTERED")))
                           );
                           String var48 = var10.jI("book.pages") ? "book.pages" : "specific_properties.book.pages";
                           if (var10.jI(var48)) {
                              BookMetaUtils.setBookPages(var41, var10.kO(var48));
                           } else {
                              BookMetaUtils.setBookPages(var41, new ArrayList());
                           }
                        }

                        var41.cJ(var10.bc("permission_suffix", var10.jO("permission")));
                        var41.method8833(var10.method7076("events_cooldown", var10.method7076("events_settings.cooldown.ticks", -1)));
                        String var49 = var10.bc("events_settings.cooldown.indicator", "TITLE");
                        HudDisplayMode var52 = (HudDisplayMode)BukkitRegistryLookup.enumOrDefault(var49, HudDisplayMode.class, HudDisplayMode.VANILLA);
                        if (var52 == HudDisplayMode.VANILLA && Version.isOlderThan(Version.v1_21_3)) {
                           var52 = HudDisplayMode.HIDDEN;
                        }

                        if (!Main.config.getBoolean("huds.enabled")) {
                           var52 = HudDisplayMode.HIDDEN;
                        }

                        switch (SwitchMap1.asj[var52.ordinal()]) {
                           case 1:
                              if (Main.config.getBoolean("huds.enabled")) {
                                 Float var56 = null;
                                 String var63 = var10.jO("events_settings.cooldown.custom_hud");
                                 if (var63 == null) {
                                    var63 = "_iainternal:small_cooldown_bar";
                                 }

                                 NamespacedElement var71 = (NamespacedElement)this.asd.aF.abm.get(var63);
                                 if (var71 instanceof NamespacedElementWithEffects) {
                                    var56 = ((NamespacedElementWithEffects)var71).zS();
                                 }

                                 if (var71 != null) {
                                    var41.method8806(new BossBarSpec(var52, var63, var56));
                                 } else {
                                    Log.error("[Items] '" + var41.getNamespacedID() + "'. Unknown cooldown hud '" + var63 + "'. File: " + var1.DJ());
                                 }
                              }
                              break;
                           case 2:
                              BarColor var55 = (BarColor)var10.method7097(
                                 "events_settings.cooldown.bossbar.color", BarColor.class, Settings.get().cooldownBarColor
                              );
                              BarStyle var62 = (BarStyle)var10.method7097(
                                 "events_settings.cooldown.bossbar.style", BarStyle.class, Settings.get().cooldownBarStyle
                              );
                              var41.method8806(new BossBarSpec(var52, var55, var62));
                              break;
                           default:
                              var41.method8806(new BossBarSpec(var52));
                        }

                        var41.cG(var10.bc("all_actions_permission", null));
                        if (var10.jI("events_needed_player_stats")) {
                           ConfigurationSection var57 = var10.kb("events_needed_player_stats");

                           for (String var72 : var57.getKeys(false)) {
                              var41.method8759(var72, var57.getString(var72));
                           }
                        }

                        if (var10.method7078("drop.glow.enabled", false)) {
                           String var58 = var10.bc("drop.glow.color", null);
                           if (var58 != null) {
                              var41.cN(var58);
                           }
                        }

                        var41.aX(var10.method7078("drop.show_name", false));
                        if (var10.jI("enchants")) {
                           HashMap var59 = new HashMap();

                           for (String var23 : (ArrayList)var10.jZ("enchants")) {
                              String[] var24 = var23.split(":");
                              Enchantment var25 = null;
                              int var26 = 1;
                              String var27 = var24[0];
                              if (var24.length == 1) {
                                 var25 = BukkitRegistryLookup.enchantment(var27);
                              } else if (var24.length == 2) {
                                 if (Utils.isNumeric(var24[1])) {
                                    var25 = BukkitRegistryLookup.enchantment(var27);
                                    var26 = Utils.parseIntOr(var24[1], 1);
                                 } else {
                                    var25 = Enchantment.getByKey(new NamespacedKey(var27, var24[1]));
                                 }
                              } else if (var24.length == 3) {
                                 var25 = Enchantment.getByKey(new NamespacedKey(var27, var24[1]));
                                 var26 = Utils.parseIntOr(var24[2], 1);
                              }

                              if (var25 == null) {
                                 if (Main.aFb && Settings.get().aeCustomEnchantsInItemConfigs) {
                                    try {
                                       AeWrapper.requireCustomEnchant(var27);
                                       AeWrapper.applyEnchant(var41, var27, var26);
                                       var41.DC();
                                    } catch (PendingEnchantException var30) {
                                       throw var30;
                                    } catch (RuntimeException | LinkageError var31) {
                                       throw new IllegalStateException(
                                          "[Items] '"
                                             + var41.getNamespacedID()
                                             + "'. Cannot apply AdvancedEnchantments enchant '"
                                             + var27
                                             + "'. File: "
                                             + var1.DJ(),
                                          var31
                                       );
                                    }
                                 } else {
                                    Log.error("[Items] '" + var41.getNamespacedID() + "'. Unknown vanilla/custom enchant '" + var27 + "'. File: " + var1.DJ());
                                 }
                              } else {
                                 var59.put(var25, var26);
                              }
                           }

                           var41.addUnsafeEnchantments(var59);
                           var41.method8832(ImmutableMap.copyOf(var59));
                           var41.DC();
                        }

                        if (var10.jI("lore")) {
                           ArrayList var60 = new ArrayList();

                           for (String var74 : var10.jZ("lore")) {
                              String var77 = this.asd.bz.getPlainText(var74);
                              var60.add(var77);
                           }

                           ItemStackUtils.method7226(var41.Dt(), var60);
                        }

                        boolean var61 = false;
                        if (var10.jI("item_flags")) {
                           HashSet var67 = new HashSet();

                           for (String var78 : var10.jZ("item_flags")) {
                              ItemFlag var83 = BukkitRegistryLookup.itemFlag(var78);
                              if (var83 != null) {
                                 var67.add(var83);
                              } else {
                                 Log.error("[Items] '" + var41.getNamespacedID() + "'.  Unknown item flag '" + var78 + "'. File: " + var1.DJ());
                              }
                           }

                           if (var67.contains(ItemFlag.HIDE_ATTRIBUTES)) {
                              var61 = true;
                           }

                           var41.method8754(var67);
                        }

                        if (!var10.jI("attribute_modifiers")) {
                           if (Settings.get().isLoggingEnabled("logging.items.attribute_modifiers_loading")) {
                              Log.log(ChatColor.YELLOW + "    No attribute_modifiers found for item " + var41.getNamespacedID());
                           }
                        } else {
                           ConfigurationSection var68 = var10.kb("attribute_modifiers");
                           if (var68 != null) {
                              method10119(var41, var68);
                           }
                        }

                        if (var61 && !Version.isAtLeast(Version.v1_21_5)) {
                           ItemOperationsService.getRegistryBridge().al(var41);
                        }

                        if (var10.method7078("auto_update.enabled", Settings.get().itemAutoUpdate)) {
                           ItemUpdatePolicyBuilder var69 = ItemUpdatePolicy.builder()
                              .enabled(var10.method7078("auto_update.fix_unstackable_items_on_click", Settings.get().fixUnstackableItemsOnClick))
                              .mode(
                                 (UpdateMode)var10.method7097("auto_update.enchantment_mode", UpdateMode.class, Settings.get().itemAutoUpdateEnchantmentMode)
                              );
                           var41.method8830(var69.build());
                        }

                        var41.method8794(var10.method7077("max_stack_size", null));
                        var41.aR(var10.method7078("fire_resistant", false));
                        var41.aS(var10.method7078("hide_tooltip", false));
                        var41.by(var10.method7076("durability.repair_cost", 0));
                        Boolean var70 = var10.method7104("glint", var10.method7104("enchantment_glint_override", null));
                        if (var70 != null) {
                           var41.aT(var70);
                        }

                        int var76 = var10.method7076("durability.usages", -1);
                        if (var76 != -1) {
                           var41.method8777(var76, var76);
                           var41.DB();
                        }

                        MmoItemsHook.injectMmoItem(
                           var41, var10.bc("mmoitem.type", null), var10.bc("mmoitem.id", null), var10.method7078("mmoitem.use_mmoitem_displayname", false)
                        );
                        if (Main.aFa) {
                           String var79 = var10.bc("executableitem.id", null);
                           if (var79 != null) {
                              try {
                                 ItemStack var84 = ExecutableItemsHook.getExecutableItem(var79);
                                 var41.method8747(var84);
                              } catch (NullPointerException var29) {
                                 Log.error(
                                    "[Items] '"
                                       + var41.getNamespacedID()
                                       + "'. Error loading ExecutableItems. Please check if the ExecutableItem of id '"
                                       + var79
                                       + "' exists in ExecutableItems plugin. File: "
                                       + var1.DJ()
                                 );
                              }
                           }
                        }

                        if (!var41.DI() && (!var41.DF() || var41.nz() == 0)) {
                           if (!var10.jI("resource.model_id") && !var10.jI("resource.custom_model_data")) {
                              var41.bv(CustomBlockStateSettings.getInstance().allocateStateId(var38, var3, var9));
                           } else {
                              var41.bv(
                                 CustomBlockStateSettings.getInstance()
                                    .registerCustomModelData(
                                       var38, var3, var9, var10.method7076("resource.custom_model_data", var10.jP("resource.model_id")), false, var1.DJ()
                                    )
                              );
                           }
                        }

                        if (Settings.get().isLoggingEnabled("logging.items.custom_model_data")) {
                           Log.log("      Assigned custom_model_data: " + var41.nz());
                        }

                        if (var10.jI("blocked_enchants")) {
                           ArrayList var80 = new ArrayList();

                           for (String var87 : (ArrayList)var10.jZ("blocked_enchants")) {
                              if (var87.equalsIgnoreCase("all")) {
                                 var41.aW(true);
                                 break;
                              }

                              var80.add(Enchantment.getByName(var87));
                           }

                           var41.method8831(var80);
                        }

                        var41.aY(var10.method7078("show_in_ia_gui", true));
                        if (Settings.get().fontImagesItemNameLore) {
                           EmojiReplacer.method2342(var41);
                           if (var41.getType() == Material.WRITTEN_BOOK) {
                              BookMeta var81 = (BookMeta)var41.getItemMeta();
                              BookMetaUtils.applyToBookTitle(var81, EmojiReplacer::method2333);
                              var41.setItemMeta(var81);
                           }
                        }

                        if (Settings.get().textEffectsItemNameLore) {
                           ColorTagParser.method2363(var41);
                           if (var41.getType() == Material.WRITTEN_BOOK) {
                              BookMeta var82 = (BookMeta)var41.getItemMeta();
                              BookMetaUtils.applyToBookTitle(var82, ColorTagParser::method2358);
                              var41.setItemMeta(var82);
                           }
                        }

                        CustomItemDefinition.method8811(var41);
                        this.asd.field92.put(var41.getNamespacedID(), var41);
                        this.asd.field95.put(var41.getId(), var41);
                        this.asd.field94.put(var41.aol, var41);
                        this.asd.ag.computeIfAbsent(var41.getType(), var0 -> new ArrayList<>()).add(var41);
                        this.asd.ah.computeIfAbsent(var41.getType(), var0 -> new HashMap<>()).put(var41.getNamespacedID(), var41);
                        this.asd.af.computeIfAbsent(var41.getNamespace(), var0 -> new HashMap<>()).put(var41.getId(), var41);
                        if (Settings.get().isLoggingEnabled("logging.items.loaded")) {
                           Log.log("Loaded item: " + var41.getNamespacedID());
                        }
                     }
                  }
               }
            }
         }

         if (!var7.isEmpty()) {
            Log.error(method10114(var1.DJ(), var7));
         }

         if (var5 || var6) {
            var1.save();
            var1.reload();
         }
      }
   }

   public Set FZ() {
      HashSet var1 = new HashSet(this.ase);
      var1.removeAll(this.asd.field92.keySet());
      return var1;
   }

   static String method10114(String var0, Map var1) {
      String var2 = String.join(", ", var1.keySet().stream().limit(3L).toList());
      if (var1.size() > 3) {
         var2 = var2 + " (+" + (var1.size() - 3) + " more)";
      }

      String var3 = String.join("; ", var1.values().stream().distinct().limit(2L).toList());
      return "[Items] Skipped " + var1.size() + " malformed armor/equipment item(s). File: " + var0 + ". Items: " + var2 + ". " + var3;
   }

   private void method10115(YamlConfig var1, String var2) {
      if (var1.contains("liquids")) {
         if (Settings.get().liquidsEnabled) {
            for (String var4 : var1.getStringList("liquids")) {
               ConfigurationSection var5 = var1.kb("liquids." + var4);
               int var6 = Utils.colorToRgbInt(var5.getString("color"));
               ColoredNamespacedId var7 = new ColoredNamespacedId(var2, var4, var6);
               this.asd.ay.put(var7.getNamespacedID(), var7);
               if (Settings.get().isLoggingEnabled("logging.liquids.loading")) {
                  Log.log("Loaded liquid: " + var7.getNamespacedID());
               }
            }
         } else {
            Log.warn("Liquids load skipped. Enable them in config.yml if you want to load them. File: " + var1.DJ());
         }
      }
   }

   @NotNull
   public static ConfigSectionWrapper method10116(YamlConfig var0, ConfigSectionWrapper var1, Map var2, String var3, String var4) {
      String var5 = var1.bc("variant_of", null);
      if (var5 != null) {
         var5 = NamespacedId.method385(var5, var3);
         ConfigSectionWrapper var6 = (ConfigSectionWrapper)var2.get(var5);
         if (var6 == null) {
            Log.error("[Items] '" + var3 + ":" + var4 + "'. Error: Unknown template '" + var5 + "'. File: " + var0.DJ());
            throw new IllegalArgumentException("Unknown template  '" + var5 + "'. File: " + var0.DJ());
         }

         try {
            YamlConfiguration var7 = YamlConfigurationBuilder.method7477(var6);
            YamlConfiguration var8 = YamlConfigurationBuilder.method7477(var1);
            var7.getValues(true).forEach((var1x, var2x) -> {
               if (!var8.contains(var1x)) {
                  var8.set(var1x, var2x);
               }
            });
            var8.set("template", null);
            if ((var7.contains("graphics.texture") || var7.contains("graphics.textures"))
               && (var8.contains("graphics.model") || var8.contains("graphics.models"))) {
               var8.set("graphics.texture", null);
               var8.set("graphics.textures", null);
            }

            if ((var7.contains("graphics.model") || var7.contains("graphics.models"))
               && (var8.contains("graphics.texture") || var8.contains("graphics.textures"))) {
               var8.set("graphics.model", null);
               var8.set("graphics.models", null);
            }

            return new ConfigSectionWrapper(var8);
         } catch (InvalidConfigurationException var9) {
            throw new RuntimeException(var9);
         }
      } else {
         return var1;
      }
   }

   public void Ga() {
      for (CustomItemDefinition var2 : this.asd.field96.values()) {
         var2.aoh.loadBehaviours(this.asd);
      }

      for (CustomItemDefinition var7 : this.asd.field96.values()) {
         var7.aog.loadEvents(this.asd);
      }

      for (CustomItemDefinition var8 : this.asd.field92.values()) {
         var8.aoh.loadBehaviours(this.asd);
      }

      for (CustomItemDefinition var9 : this.asd.field92.values()) {
         var9.aog.loadEvents(this.asd);
         this.method10118(var9);
         this.method10117(var9);
      }

      for (CustomBlockDefinition var10 : new ArrayList(ContentManager.method8084().al)) {
         InventoryHookLoader.loadDirectionalHooks(var10);
      }
   }

   public void method10117(CustomItemDefinition var1) {
      if (var1 instanceof PlantItemDefinition) {
         if (!this.asf.containsKey(var1.getNamespacedID())) {
            Log.log(ChatColor.YELLOW + "Seed '" + var1.getNamespacedID() + "' has no results (file '" + var1.DJ() + "' )");
            return;
         }

         int var2 = 0;

         for (String var4 : (List)this.asf.get(var1.getNamespacedID())) {
            ItemStack var5 = this.asd.method8179(var4);
            if (var5 != null) {
               ((ItemVariantHolder)((PlantItemDefinition)var1).Eg().get(var2)).setItemStack(var5);
               var2++;
            } else {
               Log.log(ChatColor.YELLOW + "Seed '" + var1.getNamespacedID() + "' has unknown item result '" + var4 + "'. File: " + var1.DJ());
            }
         }

         ((PlantItemDefinition)var1).Eg().removeIf(var0 -> var0.getItemStack() == null);
      }
   }

   public void method10118(CustomItemDefinition var1) {
      if ((
            var1.DS().jI("behaviours.block.placed_model.break_particles_material")
               || var1.DS().jI("specific_properties.block.placed_model.break_particles_material")
         )
         && var1 instanceof CustomBlockDefinition) {
         String var2 = var1.DS()
            .bc("behaviours.block.placed_model.break_particles_material", var1.DS().bc("specific_properties.block.placed_model.break_particles_material", ""));
         assert var2 != null;
         if (BukkitRegistryLookup.hasEnum(var2.toUpperCase(), Material.class)) {
            if (Material.valueOf(var2.toUpperCase()).isBlock()) {
               ((CustomBlockDefinition)var1).anF.method10025(Material.valueOf(var2.toUpperCase()));
            } else {
               Log.error(
                  "[Items] '"
                     + var1.getNamespacedID()
                     + "'. Invalid break_particles_material '"
                     + var2
                     + "' for item '"
                     + var1.getNamespacedID()
                     + "'. It's an ITEM, not a BLOCK. Please use a BLOCK material name. File: "
                     + var1.DJ()
               );
            }
         } else if (this.asd.method8155(var2)) {
            ((CustomBlockDefinition)var1).anF.method10026(this.asd.method8154(var2));
         } else {
            Log.error(
               "[Items] '"
                  + var1.getNamespacedID()
                  + "'. Invalid break_particles_material '"
                  + var2
                  + "' for item '"
                  + var1.getNamespacedID()
                  + "' please make sure it's a vanilla material or a custom item. File: "
                  + var1.DJ()
            );
         }
      }
   }

   static void method10119(CustomItemDefinition var0, @NotNull ConfigurationSection var1) {
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = var0.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES);

      for (String var6 : var1.getKeys(false)) {
         ConfigurationSection var7 = var1.getConfigurationSection(var6);

         for (String var10 : var7.getKeys(true)) {
            if (method10120(var0, var6, var10, var7)) {
               if (var10.contains("attackDamage") || var10.contains("attack_damage")) {
                  var3 = true;
               }

               if (var10.contains("attackSpeed") || var10.contains("attack_speed")) {
                  var2 = true;
               }
            }
         }
      }

      if (Settings.get().fixAttackDamageLore && !var4 && ItemStackUtils.ap(var0) && (var3 || var2)) {
         ItemOperationsService.getRegistryBridge().al(var0);
         CustomItemDefinition.method8803(var0, EquipmentSlotType.MAINHAND);
      }
   }

   static boolean method10120(CustomItemDefinition var0, String var1, String var2, ConfigurationSection var3) {
      if (!var2.endsWith("operation") && !var2.endsWith("value")) {
         double var4;
         String var6;
         if (var3.get(var2) instanceof ConfigurationSection var7) {
            if (!var7.contains("value")) {
               return false;
            }

            var4 = var7.getDouble("value", 0.0);
            var6 = var7.getString("operation", "add");
         } else {
            var4 = var3.getDouble(var2, 0.0);
            var6 = "add";
         }

         var0.method8786(var2, var1, var4, var6);
         return true;
      } else {
         return false;
      }
   }

   private static String dm(String var0) {
      String[] var1 = var0.split("_");
      StringBuilder var2 = new StringBuilder();

      for (String var6 : var1) {
         if (!var6.isEmpty()) {
            var2.append(Character.toUpperCase(var6.charAt(0)));
            if (var6.length() > 1) {
               var2.append(var6.substring(1).toLowerCase());
            }

            var2.append(" ");
         }
      }

      if (!var2.isEmpty()) {
         var2.setLength(var2.length() - 1);
      }

      return var2.toString();
   }
}
