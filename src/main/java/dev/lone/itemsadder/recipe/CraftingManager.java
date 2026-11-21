package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.CustomCrafting.CustomCraftingWrapper;
import dev.lone.itemsadder.Core.OtherPlugins.mmoitems.MmoItemsHook;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.DebounceFlag;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftingManager implements RecipeType, IaListener {
   public final List aCk = new ArrayList();
   public final Map aCl = new HashMap();
   public final Map aCm = new HashMap();
   private final ContentManager aCn;
   private final WeakHashMap aCo = new WeakHashMap();

   public CraftingManager(Plugin var1, ContentManager var2) {
      this.aCn = var2;
      EventRegistrar.registerIfAbsent(this, var1);
      RecipeManagerHolder.Qb().PX();
   }

   public String Kc() {
      return "Crafting";
   }

   public void method744(CraftingRecipe var1, String var2, String var3) {
      try {
         NamespacedKey var4 = new NamespacedKey(var2.toLowerCase(), var3.toLowerCase());

         try {
            var4 = this.method1432(this.aCm, var1, var4, var2, var3);
         } catch (RecipeException var10) {
         }

         var1.method1582(var4);
         if (!var1.aCg) {
            ShapedRecipe var12 = new ShapedRecipe(var4, var1.JR());
            var12.shape(var1.Ko());

            for (ShapedIngredient var17 : var1.aBY) {
               var12.setIngredient(var17.Kk(), var17.aBU);
               this.aCn.bp.method2224(var1, var17);
            }

            var12.setCategory(var1.JW());
            var1.method1594(var12);
            var1.method1583(var12);
            NamespacedKey var15 = new NamespacedKey(AbstractRecipeManager.boe + var4.getNamespace(), var4.getKey());
            ShapedRecipe var18 = new ShapedRecipe(var15, var1.JR());
            var18.shape(var1.Ko());

            for (ShapedIngredient var9 : var1.aBY) {
               var18.setIngredient(var9.Kk(), var9.aBU.getItemStack().getType());
            }

            RecipeRegistry.method2236(var18);
            this.aCk.add(var1);
         } else {
            ShapelessRecipe var5 = new ShapelessRecipe(var4, var1.JR());

            for (ShapedIngredient var7 : var1.aBY) {
               var5.addIngredient(var7.aBU);
               this.aCn.bp.method2224(var1, var7);
            }

            var1.method1595(var5);
            var1.method1583(var5);
            ShapelessRecipe var13 = new ShapelessRecipe(new NamespacedKey(AbstractRecipeManager.boe + var4.getNamespace(), var4.getKey()), var1.JR());

            for (ShapedIngredient var8 : var1.aBY) {
               var13.addIngredient(var8.aBU.getItemStack().getType());
            }

            RecipeRegistry.method2236(var13);
            this.aCk.add(var1);
         }

         this.aCm.put(var1.getNamespacedKey().toString(), var1);
         this.aCl.put(var2, var1);
         this.aCn.bp.method2222(var1);
         this.aCn.bp.method2225(var1);
         if (Settings.get().isLoggingEnabled("logging.recipes.crafting.loading")) {
            if (var1.JS() != null) {
               Log.log("Registered crafting recipe " + var1.getNamespacedKey() + " for: " + var1.JS().getNamespacedID());
            } else {
               Log.log("Registered crafting recipe " + var1.getNamespacedKey() + " for: " + var1.JR().getType());
            }
         }
      } catch (Throwable var11) {
         if (var11.getMessage().contains("Symbol does not appear in the shape")) {
            Log.error("Error loading recipe '" + var2 + ":" + var3 + "'. " + var11.getMessage());
         } else {
            Log.error(
               "Error loading recipe '"
                  + var2
                  + ":"
                  + var3
                  + "'. Please check shape characters and materials. You may have missed one or used a wrong character",
               var11
            );
         }
      }
   }

   @Override
   public void unregister() {
      IaListener.super.unregister();
      this.aCk.clear();
      this.aCm.clear();
      this.aCl.clear();
   }

   public List method745(CustomItemDefinition var1) {
      return this.aCn.bp.method2229(CraftingRecipe.class, var1);
   }

   public List method746(CustomItemDefinition var1) {
      return this.aCn.bp.method2230(CraftingRecipe.class, var1);
   }

   public Set method747(CustomItemDefinition var1) {
      LinkedHashSet var2 = new LinkedHashSet(this.method745(var1));
      var2.addAll(this.method746(var1));
      return var2;
   }

   public static boolean method748(ItemStack[] var0) {
      for (int var1 = 1; var1 < var0.length; var1++) {
         if (CustomItemDefinition.method8748(var0[var1])) {
            return true;
         }
      }

      return false;
   }

   public static boolean method749(Recipe var0, ItemStack var1) {
      return var0 instanceof Keyed && ((Keyed)var0).getKey().getKey().equals("armor_dye") && CustomItemDefinition.method8748(var1)
         ? var1.getType() != Material.LEATHER_HORSE_ARMOR
         : false;
   }

   public static boolean method750(Recipe var0, @NotNull CraftingInventory var1) {
      if (var0 instanceof Keyed) {
         if (var1.getResult() == null || var1.getResult().getType() != Material.LEATHER_HORSE_ARMOR) {
            return false;
         }

         if (((Keyed)var0).getKey().getKey().equals("armor_dye")) {
            CustomItemDefinition var2 = ContentManager.method8084().method8154(CustomItemDefinition.method8749(var1.getResult()));
            if (var2 != null) {
               return var2.aoy;
            }
         }
      }

      return false;
   }

   @EventHandler(priority = EventPriority.MONITOR)
   private void method751(PrepareItemCraftEvent var1) {
      Recipe var2 = var1.getRecipe();
      CraftingInventory var3 = var1.getInventory();
      InventoryView var4 = var1.getView();
      ItemStack var5 = var3.getResult();
      if (method749(var2, var5)) {
         var3.setResult(ItemStackUtils.bMp);
      } else {
         if (var2 instanceof Keyed var6 && var6.getKey().getNamespace().equals("minecraft") && var6.getKey().getKey().equals("repair_item")) {
            int var7 = 0;
            CustomItemDefinition var8 = null;
            if (var5 != null && var5.hasItemMeta() && var5.getItemMeta() instanceof Damageable var9) {
               boolean var20 = false;
               ItemStack[] var11 = var4.getTopInventory().getContents();

               for (int var12 = 1; var12 < var11.length; var12++) {
                  ItemStack var13 = var11[var12];
                  if (ContentManager.method8084().isCustomItem(var13)) {
                     var20 = true;
                     break;
                  }
               }

               if (var20) {
                  for (int var21 = 1; var21 < var11.length; var21++) {
                     ItemStack var23 = var11[var21];
                     if (!ItemStackUtils.aE(var23)) {
                        CustomItemDefinition var14 = ContentManager.method8084().method8149(var23);
                        if (var14 == null) {
                           var3.setResult(ItemStackUtils.bMp);
                           return;
                        }

                        if (var8 != null && var14.aol != var8.aol) {
                           var3.setResult(ItemStackUtils.bMp);
                           return;
                        }

                        var8 = var14;
                        if (++var7 > 2) {
                           var3.setResult(ItemStackUtils.bMp);
                           return;
                        }
                     }
                  }

                  if (var7 == 2) {
                     int var22 = var9.getDamage();
                     ItemStack var24 = var8.Ds();
                     if (var24.hasItemMeta() && var24.getItemMeta() instanceof Damageable var25) {
                        var25.setDamage(var22);
                        var24.setItemMeta(var25);
                        var3.setResult(var24);
                     }
                  }
               }
            }
         }

         if (var2 instanceof Keyed var16) {
            if ((var16.getKey().getNamespace().startsWith(AbstractRecipeManager.boe) || this.method752(var2))
               && !method748(var4.getTopInventory().getContents())) {
               ItemStack var17 = RecipeManagerHolder.Qb().method770(var3);
               if (var17 != null) {
                  var3.setResult(var17);
                  return;
               }
            }

            if (!var16.getKey().getNamespace().startsWith(AbstractRecipeManager.boe) && !this.method752(var2)) {
               if (var2.getResult().equals(var3.getResult())) {
                  if (method748(var4.getTopInventory().getContents())) {
                     CraftingRecipe var19 = this.method755(var3, false, var16);
                     if (var19 != null) {
                        var3.setResult(var19.JR());
                     } else {
                        var3.setResult(ItemStackUtils.bMp);
                     }
                  }
               }
            } else {
               CraftingRecipe var18 = this.method754(var3, var16);
               if (var18 != null) {
                  var3.setResult(var18.JR());
               } else {
                  if (var16.getKey().getNamespace().startsWith(AbstractRecipeManager.boe)) {
                     var3.setResult(ItemStackUtils.bMp);
                     return;
                  }

                  if (method748(var4.getTopInventory().getContents())) {
                     if (method750(var2, var3)) {
                        return;
                     }

                     if (Main.aEX && var16.getKey().getNamespace().equals("craftenhance")) {
                        return;
                     }

                     if (Main.aEW && CustomCraftingWrapper.inst().isCustomCraftingRecipe(var4)) {
                        return;
                     }

                     var3.setResult(ItemStackUtils.bMp);
                  }
               }
            }
         }
      }
   }

   private boolean method752(Recipe var1) {
      return this.aCm.containsKey(((Keyed)var1).getKey().toString());
   }

   private boolean method753(CraftingInventory var1) {
      for (String var3 : RecipeManagerHolder.Qb().method602(this.aCl.keySet())) {
         CraftingRecipe var4 = (CraftingRecipe)this.aCm.get(var3);
         if (var4 != null && var4.method1597(var1)) {
            return true;
         }
      }

      return false;
   }

   @Nullable
   private CraftingRecipe method754(CraftingInventory var1, Keyed var2) {
      return this.method755(var1, false, var2);
   }

   @Nullable
   private CraftingRecipe method755(CraftingInventory var1, boolean var2, @Nullable Keyed var3) {
      if (!var2 || var1.getResult() != null && var1.getResult().getType() != Material.AIR) {
         if (var3 != null && this.aCm.containsKey(var3.getKey().toString())) {
            CraftingRecipe var4 = (CraftingRecipe)this.aCm.get(var3.getKey().toString());
            if (var4.method1597(var1)) {
               return var4;
            }
         }

         String var8 = RecipeManagerHolder.Qb().method603(var1);
         if (var8 != null) {
            CraftingRecipe var5 = (CraftingRecipe)this.aCm.get(var8);
            if (var5 != null && var5.method1597(var1)) {
               return var5;
            }
         }

         for (String var6 : RecipeManagerHolder.Qb().method602(this.aCl.keySet())) {
            CraftingRecipe var7 = (CraftingRecipe)this.aCm.get(var6);
            if (var7 != null && var7.method1597(var1)) {
               return var7;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method742(InventoryClickEvent var1) {
      Inventory var2 = var1.getView().getTopInventory();
      if (var2.getType() == InventoryType.STONECUTTER) {
         boolean var22 = var1.getRawSlot() == 0
            && (
               CustomItemDefinition.method8748(var1.getCursor())
                  || var1.getClick() == ClickType.NUMBER_KEY
                     && CustomItemDefinition.method8748(var1.getWhoClicked().getInventory().getItem(var1.getHotbarButton()))
                  || var1.getClick() == ClickType.SWAP_OFFHAND && CustomItemDefinition.method8748(var1.getWhoClicked().getInventory().getItemInOffHand())
            );
         if (var22
            || var1.isShiftClick() && !ItemStackUtils.method7217(var1) && CustomItemDefinition.method8748(var1.getCurrentItem())
            || var1.getRawSlot() == 1 && CustomItemDefinition.method8748(var2.getItem(0))) {
            var1.setCancelled(true);
         }
      } else {
         if (var2.getType() == InventoryType.CRAFTER) {
            if (ItemStackUtils.method7217(var1)) {
               if (CustomItemDefinition.method8748(var1.getCursor())) {
                  var1.setCancelled(true);
               }
            } else if (CustomItemDefinition.method8748(var1.getCurrentItem())) {
               var1.setCancelled(true);
            }
         }

         if (var1.getInventory().getType() == InventoryType.WORKBENCH || var1.getInventory().getType() == InventoryType.CRAFTING) {
            if (var1.getInventory() instanceof CraftingInventory var3) {
               Player var23 = (Player)var1.getWhoClicked();
               if (var1.getSlotType() == SlotType.RESULT) {
                  if (!method748(var1.getView().getTopInventory().getContents()) && RecipeManagerHolder.Qb().method770(var3) != null) {
                     return;
                  }

                  CraftingRecipe var5 = this.method755(var3, true, null);
                  if (var5 == null) {
                     return;
                  }

                  this.bF(var23);
                  ItemStack var6 = var23.getItemOnCursor();
                  if (var3.getResult() != null
                     && var6.getType() != Material.AIR
                     && var6.getAmount() + var3.getResult().getAmount() > var3.getResult().getType().getMaxStackSize()
                     && var1.getClick() != ClickType.SHIFT_LEFT
                     && var1.getClick() != ClickType.SHIFT_RIGHT) {
                     var1.setCancelled(true);
                     return;
                  }

                  if (var1.getClick() == ClickType.NUMBER_KEY) {
                     ItemStack var7 = var23.getInventory().getItem(var1.getHotbarButton());
                     if (var7 != null && var7.getType() != Material.AIR) {
                        var1.setCancelled(true);
                        return;
                     }
                  }

                  if (!PermissionNodes.method1620(var1.getView().getPlayer(), var5)) {
                     var1.setCancelled(true);
                     var3.setResult(ItemStackUtils.bMp);
                     AbstractRecipeManager.method1418(var23, var5);
                     return;
                  }

                  boolean var24 = false;
                  boolean var8 = false;
                  boolean var9 = true;
                  boolean var10 = false;
                  ArrayList var11 = new ArrayList();
                  ItemStack[] var12 = new ItemStack[var3.getMatrix().length];

                  for (int var13 = 0; var13 < var3.getMatrix().length; var13++) {
                     if (var3.getMatrix()[var13] != null) {
                        var12[var13] = var3.getMatrix()[var13];
                     }
                  }

                  if (!var5.Kq().isEmpty()) {
                     for (int var25 = 0; var25 < var12.length; var25++) {
                        if (var12[var25] != null && var12[var25].getType() != Material.AIR) {
                           boolean var14 = CustomItemDefinition.method8819(var12[var25]);
                           boolean var15 = CustomItemDefinition.method8821(var12[var25]);
                           boolean var16 = CustomItemDefinition.method8820(var12[var25]);
                           boolean var17 = ItemStackUtils.method7242(var12[var25]);

                           for (Entry var19 : var5.Kq().entrySet()) {
                              RecipeResult var20 = (RecipeResult)var19.getValue();
                              if (var20.aCi) {
                                 if (var20.aCj.method8779(var12[var25])) {
                                    if (var14) {
                                       CustomItemDefinition var21 = var20.aCj;
                                       if (var15) {
                                          if (var21.DN()) {
                                             var12[var25] = ItemStackUtils.acf();
                                          } else {
                                             var12[var25] = CustomItemDefinition.method8824(var12[var25], var20.aCh, false);
                                          }
                                       } else if (var16) {
                                          var3.setResult(null);
                                          if (var21.DN()) {
                                             var12[var25] = ItemStackUtils.acf();
                                          }

                                          var8 = true;
                                          var9 = false;
                                          var10 = true;
                                       } else {
                                          var12[var25] = CustomItemDefinition.method8824(var12[var25], var20.aCh, true);
                                       }
                                    } else {
                                       if (var17) {
                                          var3.setResult(null);
                                          return;
                                       }

                                       ItemStackUtils.method7237(var12[var25], var20.aCh);
                                    }

                                    var11.add(var25);
                                 }
                              } else if (((ItemStack)var19.getKey()).getType() == var12[var25].getType()) {
                                 if (var17) {
                                    var3.setResult(null);
                                    return;
                                 }

                                 ItemStackUtils.method7237(var12[var25], var20.aCh);
                                 var11.add(var25);
                              }
                           }
                        }
                     }

                     var24 = true;
                  }

                  if (!var5.Kr().isEmpty()) {
                     for (int var26 = 0; var26 < var12.length; var26++) {
                        if (var12[var26] != null && var12[var26].getType() != Material.AIR) {
                           for (Entry var33 : var5.Kr().entrySet()) {
                              if (((RecipeResult)var33.getValue()).aCi) {
                                 if (((RecipeResult)var33.getValue()).aCj.method8779(var12[var26])) {
                                    ItemStackUtils.method7236(var12[var26], ((RecipeResult)var33.getValue()).aCh);
                                    var11.add(var26);
                                 }
                              } else if (((ItemStack)var33.getKey()).getType() == var12[var26].getType()) {
                                 ItemStackUtils.method7236(var12[var26], ((RecipeResult)var33.getValue()).aCh);
                                 var11.add(var26);
                              }
                           }
                        }
                     }

                     var24 = true;
                  }

                  if (!var10 && !var5.Kp().isEmpty()) {
                     for (int var27 = 0; var27 < var12.length; var27++) {
                        if (var12[var27] != null && var12[var27].getType() != Material.AIR) {
                           for (Entry var34 : var5.Kp().entrySet()) {
                              boolean var35 = false;
                              if (CustomItemDefinition.isCustomItem((ItemStack)var34.getKey())) {
                                 if (CustomItemDefinition.method8780((ItemStack)var34.getKey(), var12[var27])) {
                                    var35 = true;
                                 }
                              } else if (((ItemStack)var34.getKey()).getType() == var12[var27].getType()) {
                                 var35 = true;
                              }

                              if (var35) {
                                 if (var12[var27].getAmount() > 1) {
                                    ItemStack var36 = var12[var27].clone();
                                    var12[var27].setAmount(1);
                                    var36.setAmount(var36.getAmount() - 1);
                                    ItemStackUtils.method7243(var23, var36);
                                 }

                                 ItemStack var37 = ((ItemStack)var34.getValue()).clone();
                                 var37.setAmount(var12[var27].getAmount());
                                 var12[var27] = var37;
                                 var11.add(var27);
                              }
                           }
                        }
                     }

                     var24 = true;
                  }

                  if (var9 && var5.Ks() != null) {
                     var5.Ks().applyEffect(var1, null, null, var23);
                  }

                  if (var24) {
                     var1.setCancelled(true);
                     if (!var8) {
                        for (int var28 = 0; var28 < var12.length; var28++) {
                           if (var12[var28] != null && !var11.contains(var28)) {
                              var12[var28].setAmount(var12[var28].getAmount() - 1);
                           }
                        }
                     }

                     if (var10) {
                        var3.setResult(null);
                        this.method757(var23, var5.aCb, var12, var3.getResult());
                     }

                     if (var1.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        if (var3.getResult() != null) {
                           ItemStack var29 = var3.getResult().clone();
                           MmoItemsHook.injectMmoItem(var29);
                           var3.setResult(null);
                           ItemStackUtils.method7243(var23, var29);
                        }
                     } else if (var3.getResult() != null) {
                        if (var6.getType() != Material.AIR) {
                           if (!var6.isSimilar(var3.getResult())) {
                              return;
                           }

                           MmoItemsHook.injectMmoItem(var6);
                           var6.setAmount(var6.getAmount() + var3.getResult().getAmount());
                           var23.setItemOnCursor(var6);
                        } else {
                           ItemStack var30 = var3.getResult().clone();
                           MmoItemsHook.injectMmoItem(var30);
                           var3.setResult(null);
                           var23.setItemOnCursor(var30);
                        }
                     }

                     var3.setMatrix(var12);
                     this.method757((Player)var1.getWhoClicked(), var5.aCb, var12, var3.getResult());
                  }

                  RecipeManagerHolder.method5356(var23, var5, false);
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
   private void method756(InventoryMoveItemEvent var1) {
      if (var1.getDestination().getType() == InventoryType.CRAFTER && CustomItemDefinition.method8748(var1.getItem())) {
         var1.setCancelled(true);
         Location var2 = var1.getDestination().getLocation();
         if (var2 != null && var2.getBlock().getType() == Material.CRAFTER) {
            var2.getBlock().setType(Material.AIR, false);
            LocationScanner.dropItem(var2, new ItemStack(Material.CRAFTER));
            BlockFaceUtils.playersWithin(var2, 5).forEach(var0 -> Log.sendLocalized(var0, "warning-crafter-broken-hopper"));
         }
      }
   }

   private void method757(Player var1, boolean var2, ItemStack[] var3, ItemStack var4) {
      SchedulerUtils.submitAsync(() -> {
         for (int var4x = 0; var4x < (var2 ? 4 : var3.length); var4x++) {
            PlayerInteractionFacade.method5314(var1, var3[var4x], var4x + 1);
         }

         if (var4 == null) {
            PlayerInteractionFacade.method5314(var1, ItemStackUtils.bMp, 0);
         } else {
            PlayerInteractionFacade.method5314(var1, var4, 0);
         }
      });
   }

   @EventHandler
   private void method655(InventoryCloseEvent var1) {
      if ((var1.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING || var1.getPlayer().getOpenInventory().getType() == InventoryType.WORKBENCH)
         && var1.getPlayer().getOpenInventory().getTopInventory() instanceof CraftingInventory) {
         CraftingInventory var2 = (CraftingInventory)var1.getPlayer().getOpenInventory().getTopInventory();
         if (this.bG((Player)var1.getPlayer())) {
            Log.broadcastToOps(
               ChatColor.RED
                  + "Warning: '"
                  + var1.getPlayer().getName()
                  + "' probably tried to trigger a crafting duplication glitch "
                  + ChatColor.GOLD
                  + "(cancelled automatically)."
            );
            var2.setContents(new ItemStack[var2.getContents().length]);
            this.method758((Player)var1.getPlayer(), false);
         }
      }
   }

   @EventHandler
   private void method516(PlayerQuitEvent var1) {
      this.method758(var1.getPlayer(), false);
   }

   private void method758(Player var1, boolean var2) {
      if (var2) {
         this.aCo.put(var1, new DebounceFlag());
      } else {
         this.aCo.remove(var1);
      }
   }

   private void bF(Player var1) {
      this.method758(var1, true);
   }

   public boolean bG(Player var1) {
      DebounceFlag var2 = (DebounceFlag)this.aCo.get(var1);
      return var2 != null && var2.tryFire();
   }

   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   private void method759(InventoryDragEvent var1) {
      if (var1.getView().getTopInventory().getType() == InventoryType.STONECUTTER
         && var1.getRawSlots().contains(0)
         && CustomItemDefinition.method8748(var1.getOldCursor())) {
         var1.setCancelled(true);
      }
   }
}
