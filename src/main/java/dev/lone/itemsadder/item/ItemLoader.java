package dev.lone.itemsadder.item;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.effect.SoundCategoryEffect;
import dev.lone.itemsadder.recipe.AnvilRecipe;
import dev.lone.itemsadder.recipe.AnvilRecipeListener;
import dev.lone.itemsadder.recipe.CookingRecipe;
import dev.lone.itemsadder.recipe.CraftingManager;
import dev.lone.itemsadder.recipe.CraftingRecipe;
import dev.lone.itemsadder.recipe.FurnaceRecipeListener;
import dev.lone.itemsadder.recipe.ItemStackOutput;
import dev.lone.itemsadder.recipe.RecipeIngredient;
import dev.lone.itemsadder.recipe.RecipeManagerHolder;
import dev.lone.itemsadder.recipe.RecipeOutput;
import dev.lone.itemsadder.recipe.RecipeRegistration;
import dev.lone.itemsadder.recipe.ShapedIngredient;
import dev.lone.itemsadder.recipe.SmithingCopyOptions;
import dev.lone.itemsadder.recipe.SmithingIngredient;
import dev.lone.itemsadder.recipe.SmithingRecipe;
import dev.lone.itemsadder.recipe.SmithingRecipeListener;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.ObjectPair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Nullable;

public class ItemLoader {
   boolean asq;
   List asr = Collections.synchronizedList(new ArrayList());
   List ass = Collections.synchronizedList(new ArrayList());
   List ast = Collections.synchronizedList(new ArrayList());

   public void loadRecipes(ContentManager var1, Map var2, @Nullable AnvilRecipeListener var3) {
      this.loadRecipesExcluding(var1, var2, var3, Set.of());
   }

   public void loadRecipesExcluding(ContentManager var1, Map var2, @Nullable AnvilRecipeListener var3, Set var4) {
      this.asq = true;
      if (Settings.get().recipesEnabled) {
         for (YamlConfig var6 : var2.values()) {
            String var7 = var6.getString("info.namespace");
            List var8 = var6.getStringList("recipes");
            if (var8.isEmpty()) {
               if (Settings.get().isLoggingEnabled("logging.recipes.loading_empty")) {
                  Log.log(ChatColor.YELLOW + "    No recipes found in file " + var6.GQ());
               }
            } else {
               if (Settings.get().recipesCrafting) {
                  for (String var11 : var6.getStringList("recipes.crafting_table")) {
                     ConfigSectionWrapper var12 = var6.kc("recipes.crafting_table." + var11);
                     if (!isRecipeExcluded(var12, var4)) {
                        if (!var12.method7078("enabled", true)) {
                           if (Settings.get().isLoggingEnabled("logging.recipes.crafting.loading_disabled")) {
                              Log.log("    Ignored disabled crafting_table recipe: " + var11);
                           }
                        } else {
                           if (Settings.get().isLoggingEnabled("logging.recipes.crafting.loading")) {
                              Log.log(ChatColor.WHITE + "Loading crafting_table recipe: " + var11);
                           }

                           int var13 = var12.method7076("result.amount", 1);
                           boolean var14 = var12.method7078("shapeless", false);
                           String var15 = var12.jO("result.item");
                           ItemStack var16 = var1.method8179(var15);
                           if (var16 == null) {
                              Log.log(ChatColor.YELLOW + "[Recipes] '" + var11 + "'. Result item '" + var15 + "' not found. File " + var6.DJ());
                           } else {
                              int var17 = ItemStackUtils.ax(var16);
                              if (var13 >= 1 && var13 <= var17) {
                                 var16.setAmount(var13);
                                 if (var14) {
                                    ConfigurationSection var59 = var12.kb("ingredients");
                                    if (var59 == null) {
                                       Log.error("[Recipes] '" + var11 + "'. Missing 'ingredients' attribute. File: " + var6.DJ());
                                    } else {
                                       ArrayList var62 = new ArrayList();
                                       boolean var65 = false;
                                       boolean var68 = false;

                                       for (String var72 : var59.getKeys(false)) {
                                          ObjectPair var76 = resolveRecipeIngredient(var1, var59, var72, var11, var6);
                                          RecipeOutput var82 = (RecipeOutput)var76.first;
                                          String var90 = (String)var76.second;
                                          if (var82 != null) {
                                             ShapedIngredient var98 = new ShapedIngredient(var72.charAt(0), var82);

                                             for (ShapedIngredient var108 : var62) {
                                                if (var108.method2276(var98.getItem())) {
                                                   Log.error("[Recipes] '" + var11 + "'. Duplicate ingredient '" + var90 + "'. File: " + var6.DJ());
                                                   var68 = true;
                                                }
                                             }

                                             var62.add(var98);
                                          } else {
                                             Log.error("[Recipes] '" + var11 + "'. Ingredient '" + var90 + "' not found. File: " + var6.DJ());
                                             var65 = true;
                                          }
                                       }

                                       if (!var65 && !var68) {
                                          HashMap var71 = new HashMap();
                                          if (var12.jI("return_items.replace")) {
                                             ConfigurationSection var73 = var12.kb("return_items.replace");

                                             for (String var83 : var73.getKeys(false)) {
                                                ItemStack var91 = var1.method8179(var83);
                                                if (var91 == null) {
                                                   Log.error("[Recipes] '" + var11 + "'. Replace item not found '" + var83 + "'. File: " + var6.DJ());
                                                } else {
                                                   ItemStack var99 = var1.method8179(var73.getString(var83));
                                                   if (var99 == null) {
                                                      Log.error(
                                                         "[Recipes] '"
                                                            + var11
                                                            + "'. Replacement item not found '"
                                                            + var73.getString(var83)
                                                            + "'. File: "
                                                            + var6.DJ()
                                                      );
                                                   } else {
                                                      var71.put(var91, var99);
                                                   }
                                                }
                                             }
                                          }

                                          HashMap var74 = new HashMap();
                                          if (var12.jI("return_items.decrement_durability")) {
                                             ConfigurationSection var78 = var12.kb("return_items.decrement_durability");

                                             for (String var92 : var78.getKeys(false)) {
                                                var74.put(var1.method8179(var78.getString(var92 + ".item")), var78.getInt(var92 + ".amount"));
                                             }
                                          }

                                          HashMap var79 = new HashMap();
                                          if (var12.jI("return_items.increment_durability")) {
                                             ConfigurationSection var85 = var12.kb("return_items.increment_durability");

                                             for (String var100 : var85.getKeys(false)) {
                                                var79.put(var1.method8179(var85.getString(var100 + ".item")), var85.getInt(var100 + ".amount"));
                                             }
                                          }

                                          try {
                                             CraftingRecipe var86 = new CraftingRecipe(
                                                var6,
                                                var12.abQ(),
                                                var12.bc("permission", null),
                                                var71,
                                                var74,
                                                var79,
                                                new SoundCategoryEffect(
                                                   var12.bc("return_items.play_sound.name", ""),
                                                   var12.method7102("return_items.play_sound.volume", 0.3F),
                                                   var12.method7102("return_items.play_sound.pitch", 1.0F),
                                                   var12.bc("return_items.play_sound.category", null),
                                                   var12.method7078("return_items.play_sound.stop_previous", true)
                                                ),
                                                var16,
                                                var62
                                             );
                                             this.asr.add(new RecipeRegistration(var86, var7, var11));
                                          } catch (Throwable var31) {
                                             Log.error("[Recipes] '" + var11 + "'. Failed to load. File: " + var6.DJ(), var31);
                                          }
                                       }
                                    }
                                 } else {
                                    int var18 = 0;

                                    for (String var20 : var12.bL(false)) {
                                       if (var20.startsWith("pattern")) {
                                          ArrayList var21 = new ArrayList();
                                          ConfigurationSection var22 = var12.kb("ingredients");
                                          if (var22 == null) {
                                             Log.error("[Recipes] '" + var11 + "'. Missing 'ingredients' attribute. File: " + var6.DJ());
                                          } else {
                                             boolean var23 = false;

                                             for (String var25 : var22.getKeys(false)) {
                                                ObjectPair var26 = resolveRecipeIngredient(var1, var22, var25, var11, var6);
                                                RecipeOutput var27 = (RecipeOutput)var26.first;
                                                String var28 = (String)var26.second;
                                                if (var27 != null) {
                                                   var21.add(new ShapedIngredient(var25.charAt(0), var27));
                                                } else {
                                                   Log.error("[Recipes] '" + var11 + "'. Ingredient not found '" + var28 + "'. File: " + var6.DJ());
                                                   var23 = true;
                                                }
                                             }

                                             if (!var23) {
                                                HashMap var75 = new HashMap();
                                                if (var12.jI("return_items.replace")) {
                                                   ConfigurationSection var80 = var12.kb("return_items.replace");

                                                   for (String var94 : var80.getKeys(false)) {
                                                      ItemStack var101 = var1.method8179(var94);
                                                      if (var101 == null) {
                                                         Log.error("[Recipes] '" + var11 + "'. Replace item not found '" + var94 + "'. File: " + var6.DJ());
                                                      } else {
                                                         ItemStack var29 = var1.method8179(var80.getString(var94));
                                                         if (var29 == null) {
                                                            Log.error(
                                                               "[Recipes] '"
                                                                  + var11
                                                                  + "'. Replacement item not found '"
                                                                  + var80.getString(var94)
                                                                  + "'. File: "
                                                                  + var6.DJ()
                                                            );
                                                         } else {
                                                            var75.put(var101, var29);
                                                         }
                                                      }
                                                   }
                                                }

                                                HashMap var81 = new HashMap();
                                                if (var12.jI("return_items.decrement_durability")) {
                                                   ConfigurationSection var88 = var12.kb("return_items.decrement_durability");

                                                   for (String var102 : var88.getKeys(false)) {
                                                      var81.put(var1.method8179(var88.getString(var102 + ".item")), var88.getInt(var102 + ".amount"));
                                                   }
                                                }

                                                HashMap var89 = new HashMap();
                                                if (var12.jI("return_items.increment_durability")) {
                                                   ConfigurationSection var96 = var12.kb("return_items.increment_durability");

                                                   for (String var106 : var96.getKeys(false)) {
                                                      var89.put(var1.method8179(var96.getString(var106 + ".item")), var96.getInt(var106 + ".amount"));
                                                   }
                                                }

                                                try {
                                                   String[] var97 = var12.jZ(var20).toArray(new String[0]);
                                                   CraftingRecipe var104 = new CraftingRecipe(
                                                      var6,
                                                      var12.abQ(),
                                                      var12.bc("permission_suffix", var12.bc("permission", null)),
                                                      var75,
                                                      var81,
                                                      var89,
                                                      new SoundCategoryEffect(
                                                         var12.bc("return_items.play_sound.name", ""),
                                                         var12.method7102("return_items.play_sound.volume", 0.3F),
                                                         var12.method7102("return_items.play_sound.pitch", 1.0F),
                                                         var12.bc("return_items.play_sound.category", null),
                                                         var12.method7078("return_items.play_sound.stop_previous", true)
                                                      ),
                                                      var16,
                                                      var97,
                                                      var21
                                                   );
                                                   String var107 = var11 + "_" + var18;
                                                   this.asr.add(new RecipeRegistration(var104, var7, var107));
                                                   var18++;
                                                } catch (Throwable var30) {
                                                   Log.error("[Recipes] '" + var11 + "'. Failed to load. File: " + var6.DJ(), var30);
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              } else {
                                 Log.error("[Recipes] '" + var11 + "'. Invalid result amount " + var13 + "; expected 1.." + var17 + ". File: " + var6.DJ());
                              }
                           }
                        }
                     }
                  }
               }

               if (Settings.get().recipesCooking) {
                  for (String var38 : var6.getStringList("recipes.cooking")) {
                     ConfigSectionWrapper var41 = var6.kc("recipes.cooking." + var38);
                     if (!isRecipeExcluded(var41, var4)) {
                        if (!var41.method7078("enabled", true)) {
                           if (Settings.get().isLoggingEnabled("logging.recipes.cooking.loading_disabled")) {
                              Log.log("    Ignored disabled cooking recipe: " + var38);
                           }
                        } else {
                           if (Settings.get().isLoggingEnabled("logging.recipes.cooking.loading")) {
                              Log.log(ChatColor.WHITE + "Loading cooking recipe: " + var38);
                           }

                           for (String var47 : var41.jZ("machines")) {
                              String var50 = var41.jO("result.item");
                              ItemStack var53 = var1.method8179(var50);
                              if (var53 == null) {
                                 Log.log(ChatColor.YELLOW + "[Recipes] '" + var38 + "'. Result item '" + var50 + "' not found. File " + var6.DJ());
                              } else {
                                 var53.setAmount(var41.method7076("result.amount", 1));
                                 String var56 = var41.bc("ingredient.item", "");
                                 Object var60 = var1.method8174(var56, var6.DJ());
                                 if (var60 != null) {
                                    CookingRecipe var63 = new CookingRecipe(
                                       var6,
                                       var41.abQ(),
                                       var41.bc("permission_suffix", var41.bc("permission", "")),
                                       var53,
                                       new RecipeIngredient(var60),
                                       var41.method7102("exp", -1.0F),
                                       var41.method7076("cook_time", 200)
                                    );
                                    Material var66 = YamlConfig.method7060(var47, null);
                                    if (var66 == null) {
                                       Log.error("[Recipes] '" + var38 + "'. Invalid furnace type '" + var47 + "'. File: " + var6.DJ());
                                    } else {
                                       var63.method1590(var66);
                                       this.ass.add(new RecipeRegistration(var63, var7, var38));
                                    }
                                 } else {
                                    Log.log(ChatColor.YELLOW + "[Recipes] '" + var38 + "'. Ingredient item '" + var56 + "' not found. File " + var6.DJ());
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               if (Settings.get().recipesSmithing) {
                  for (String var39 : var6.getStringList("recipes.smithing")) {
                     ConfigSectionWrapper var42 = var6.kc("recipes.smithing." + var39);
                     if (!isRecipeExcluded(var42, var4)) {
                        assert var42 != null;
                        if (!var42.method7078("enabled", true)) {
                           if (Settings.get().isLoggingEnabled("logging.recipes.smithing.loading_disabled")) {
                              Log.log("    Ignored disabled smithing recipe: " + var39);
                           }
                        } else {
                           if (Settings.get().isLoggingEnabled("logging.recipes.smithing.loading")) {
                              Log.log(ChatColor.WHITE + "Loading smithing recipe: " + var39);
                           }

                           String var45 = var42.jO("result.item");
                           ItemStack var48 = var1.method8179(var45);
                           if (var48 == null) {
                              Log.log(ChatColor.YELLOW + "[Recipes] '" + var39 + "'. Result item '" + var45 + "' not found. File " + var6.DJ());
                           } else {
                              var48.setAmount(var42.method7076("result.amount", 1));
                              String var51 = var42.bc("template", "");
                              Object var54 = var1.method8174(var51, null);
                              if (var54 == null) {
                                 Log.warn("[Recipes] '" + var39 + "'. Template item '" + var51 + "' not found. File " + var6.DJ());
                              }

                              String var57 = var42.bc("base", "");
                              Object var61 = var1.method8174(var57, null);
                              if (var61 == null) {
                                 Log.warn("[Recipes] '" + var39 + "'. Base item '" + var57 + "' not found. File " + var6.DJ());
                              }

                              String var64 = var42.bc("addition", "");
                              Object var67 = var1.method8174(var64, null);
                              if (var67 == null) {
                                 Log.warn("[Recipes] '" + var39 + "'. Addition item '" + var57 + "' not found. File " + var6.DJ());
                              }

                              SmithingRecipe var69 = null;
                              if (var61 != null && var67 != null) {
                                 var69 = new SmithingRecipe(
                                    var6,
                                    var42.abQ(),
                                    var42.bc("permission_suffix", var42.bc("permission", "")),
                                    var48,
                                    new SmithingIngredient(var54),
                                    new SmithingIngredient(var61),
                                    new SmithingIngredient(var67),
                                    new SmithingCopyOptions(
                                       var42.method7078("result.move_attributes.name", false),
                                       var42.method7078("result.move_attributes.lore", false),
                                       var42.method7078("result.move_attributes.enchants", true),
                                       var42.method7078("result.move_attributes.durability", false),
                                       var42.method7078("result.move_attributes.custom_model_data", false),
                                       var42.method7078("result.move_attributes.material", false),
                                       var42.method7078("result.move_attributes.other_nbt", true)
                                    )
                                 );
                              }

                              if (var69 != null) {
                                 this.ast.add(new RecipeRegistration(var69, var7, var39));
                              }
                           }
                        }
                     }
                  }
               }

               if (Settings.get().recipesAnvil) {
                  for (String var40 : var6.getStringList("recipes.anvil_repair")) {
                     ConfigSectionWrapper var43 = var6.kc("recipes.anvil_repair." + var40);
                     if (!isRecipeExcluded(var43, var4)) {
                        if (!var43.method7078("enabled", true)) {
                           if (Settings.get().isLoggingEnabled("logging.recipes.anvil.loading_disabled")) {
                              Log.log("    Ignored disabled anvil_repair recipe: " + var40);
                           }
                        } else {
                           if (Settings.get().isLoggingEnabled("logging.recipes.anvil.loading")) {
                              Log.log(ChatColor.WHITE + "Loading anvil_repair recipe: " + var40);
                           }

                           String var46 = var43.jO("item");
                           ItemStack var49 = var1.method8179(var46);
                           if (var49 == null) {
                              Log.warn("[Recipes] '" + var40 + "'. Result item '" + var46 + "' not found. File: " + var6.DJ());
                           } else {
                              String var52 = var43.jO("ingredient");
                              ItemStack var55 = var1.method8179(var52);
                              if (var55 == null) {
                                 Log.warn("[Recipes] '" + var40 + "'. Ingredient item '" + var52 + "' not found. File: " + var6.DJ());
                              } else {
                                 AnvilRecipe var58 = new AnvilRecipe(var6, var43.abQ(), var43.bc("permission_suffix", var43.bc("permission", "")), var49, var55);
                                 var3.method907(var58, var7, var40);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   static boolean isRecipeExcluded(ConfigSectionWrapper var0, Set var1) {
      if (var1.isEmpty()) {
         return false;
      }

      for (String var3 : List.of("result.item", "ingredient.item", "template", "base", "addition", "item", "ingredient")) {
         String var4 = var0.jO(var3);
         if (var4 != null && var1.contains(var4)) {
            return true;
         }
      }

      ConfigurationSection var6 = var0.kb("ingredients");
      if (var6 != null) {
         for (String var8 : var6.getKeys(false)) {
            String var5 = var6.isConfigurationSection(var8) ? var6.getString(var8 + ".item") : var6.getString(var8);
            if (var5 != null && var1.contains(var5)) {
               return true;
            }
         }
      }

      return false;
   }

   public void registerLoadedRecipes(@Nullable CraftingManager var1, @Nullable FurnaceRecipeListener var2, @Nullable SmithingRecipeListener var3) {
      if (!this.asq) {
         throw new RuntimeException("bro you should first preload the recipes.");
      }

      int var4 = this.asr.size() + this.ass.size() + this.ast.size();
      Log.log("Registering " + var4 + " recipes...");
      if (Settings.get().recipesCrafting) {
         RecipeRegistration.method2220(var1, this.asr);
      }

      if (Settings.get().recipesCooking) {
         RecipeRegistration.method2220(var2, this.ass);
      }

      if (var3 != null && Settings.get().recipesSmithing) {
         RecipeRegistration.method2220(var3, this.ast);
      }

      this.asr.clear();
      this.ass.clear();
      this.ast.clear();
      ContentManager.method8084().bp.JX();
      RecipeManagerHolder.Qb().method598(ContentManager.method8084().bp.Kb());
      RecipeManagerHolder.Qb().PW();
      RecipeManagerHolder.Qb().PY();
      Log.log("Registered " + var4 + " recipes.");
   }

   private static ObjectPair resolveRecipeIngredient(ContentManager var0, ConfigurationSection var1, String var2, String var3, YamlConfig var4) {
      RecipeOutput var6 = null;
      String var5;
      if (var1.get(var2) instanceof MemorySection) {
         var5 = var1.getString(var2 + ".item", null);
         if (var5 == null) {
            Log.error("[Recipes] '" + var3 + "'. Missing 'item' property for ingredient '" + var2 + "'. File: " + var4.DJ());
            return ObjectPair.of(var6, var5);
         }

         Material var7 = (Material)BukkitRegistryLookup.enumOrNull(var5, Material.class);
         if (var7 == null) {
            Log.error("[Recipes] '" + var3 + "'. Item '" + var5 + "' not found for ingredient '" + var2 + "'. File: " + var4.DJ());
            return ObjectPair.of(var6, var5);
         }

         if (var7 == Material.POTION || var7 == Material.SPLASH_POTION) {
            String var8 = var1.getString(var2 + ".potion_type", null);
            if (var8 != null) {
               PotionType var9 = YamlConfig.jE(var8);
               if (var9 == null) {
                  Log.error("[Recipes] '" + var3 + "'. Invalid potion type '" + var8 + "' for ingredient '" + var2 + "'. File: " + var4.DJ());
                  return ObjectPair.of(var6, var5);
               }

               ItemStack var10 = new ItemStack(var7);
               PotionMeta var11 = (PotionMeta)var10.getItemMeta();
               var11.setBasePotionType(var9);
               var10.setItemMeta(var11);
               var6 = new ItemStackOutput(var10);
            }
         }
      } else {
         var5 = var1.getString(var2);
      }

      if (var6 == null) {
         var6 = var0.method8176(var5);
      }

      return ObjectPair.of(var6, var5);
   }
}
