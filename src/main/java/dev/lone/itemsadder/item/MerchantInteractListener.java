package dev.lone.itemsadder.item;

import dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins.ProtectionPlugins;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.util.EventRegistrar;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.Plugin;

public class MerchantInteractListener implements IaListener {
   private final ContentManager afs;
   final WeakHashMap aft = new WeakHashMap();

   public MerchantInteractListener(Plugin var1, ContentManager var2) {
      this.afs = var2;
      EventRegistrar.registerIfAbsent(this, var1);
   }

   public boolean isMerchantOpen(Player var1) {
      return this.aft.containsKey(var1);
   }

   public void openMerchant(Player var1, ItemMerchantAction var2) {
      this.aft.put(var1, var2);
   }

   @EventHandler(priority = EventPriority.MONITOR)
   void onInteractEntity(PlayerInteractAtEntityEvent var1) {
      if (!ItemStackUtils.method7251(var1)) {
         if (!var1.getPlayer().isSneaking()) {
            if (var1.getRightClicked() instanceof ArmorStand) {
               if (ProtectionPlugins.canInteract_tradeMachineArmorstand(var1)) {
                  ArmorStand var2 = (ArmorStand)var1.getRightClicked();
                  ItemStack var3 = var2.getHelmet();
                  if (var3 != null) {
                     if (CustomItemDefinition.isCustomItem(var3)) {
                        CustomItemDefinition var4 = ContentManager.method8084().method8149(var3);
                        if (var4 != null) {
                           if (var4.aoh.hasAction("furniture_trade_machine")) {
                              var1.setCancelled(true);
                              ItemActionKeyedDup2 var5 = (ItemActionKeyedDup2)var4.aoh.getAction("furniture_trade_machine");
                              if (PermissionNodes.method1631(var1.getPlayer(), var5.getPermission(), true)) {
                                 this.aft.put(var1.getPlayer(), var5);
                                 var5.aI(var1.getPlayer());
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

   @EventHandler(priority = EventPriority.MONITOR)
   void onInteract(PlayerInteractEvent var1) {
      if (!ItemStackUtils.method7250(var1)) {
         if (var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!var1.getPlayer().isSneaking()) {
               if (var1.useInteractedBlock() != Result.DENY) {
                  if (var1.getClickedBlock().getType() == Material.BARRIER) {
                     Entity var2 = ItemEntityAction.aC(var1.getPlayer());
                     if (var2 != null) {
                        if (ProtectionPlugins.canInteract_tradeMachineArmorstand(var1.getPlayer(), var2, var1.isCancelled())) {
                           ItemStack var3;
                           if (var2 instanceof ArmorStand) {
                              var3 = ((ArmorStand)var2).getHelmet();
                           } else if (var2 instanceof ItemFrame) {
                              var3 = ((ItemFrame)var2).getItem();
                           } else {
                              var3 = ((ItemDisplay)var2).getItemStack();
                           }

                           if (CustomItemDefinition.isCustomItem(var3)) {
                              CustomItemDefinition var4 = this.afs.method8149(var3);
                              if (var4.aoh.hasAction("furniture_trade_machine")) {
                                 var1.setCancelled(true);
                                 ItemActionKeyedDup2 var5 = (ItemActionKeyedDup2)var4.aoh.getAction("furniture_trade_machine");
                                 if (PermissionNodes.method1631(var1.getPlayer(), var5.getPermission(), true)) {
                                    this.aft.put(var1.getPlayer(), var5);
                                    var5.aI(var1.getPlayer());
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
   }

   @EventHandler(priority = EventPriority.MONITOR)
   void onCustomBlockInteract(CustomBlockInteractEvent var1) {
      if (!var1.getPlayer().isSneaking()) {
         if (!this.afs.aX.aY(var1.getPlayer())) {
            CustomBlockDefinition var2 = var1.internal;
            ItemActionKeyed var3 = (ItemActionKeyed)var2.aoh.getAction("block_trade_machine");
            if (var3 != null) {
               if (!ProtectionPlugins.canInteract_tradeMachineBlock(var1)) {
                  return;
               }

               if (!PermissionNodes.method1631(var1.getPlayer(), var3.getPermission(), true)) {
                  return;
               }

               this.aft.put(var1.getPlayer(), var3);
               var3.aI(var1.getPlayer());
            }
         }
      }
   }

   @EventHandler
   void onInventoryClose(InventoryCloseEvent var1) {
      if (var1.getInventory().getType() == InventoryType.MERCHANT) {
         this.aft.remove(var1.getPlayer());
      }
   }

   @EventHandler
   void onPlayerQuit(PlayerQuitEvent var1) {
      this.aft.remove(var1.getPlayer());
   }

   @EventHandler(ignoreCancelled = true)
   void onInventoryClick(InventoryClickEvent var1) {
      if (var1.getInventory().getType() == InventoryType.MERCHANT) {
         if (this.aft.containsKey((Player)var1.getWhoClicked())) {
            if (var1.getSlot() == 2) {
               ItemStack var2 = var1.getInventory().getStorageContents()[2];
               if (var2 != null) {
                  ItemStack var3 = var1.getInventory().getStorageContents()[0];
                  ItemStack var4 = var1.getInventory().getStorageContents()[1];
                  if (this.isValidTradeClick(var1, var3, var4)) {
                     var1.setCancelled(true);
                  } else {
                     var2 = this.method1401(var3, var2);
                     var2 = this.method1401(var4, var2);
                     var1.getInventory().setItem(2, var2);
                  }
               }
            }
         }
      }
   }

   private boolean isValidTradeClick(InventoryClickEvent var1, ItemStack var2, ItemStack var3) {
      MerchantInventory var4 = (MerchantInventory)var1.getInventory();
      MerchantRecipe var5 = var4.getSelectedRecipe();
      ItemStackWrapper var6 = new ItemStackWrapper((ItemStack)var5.getIngredients().get(0));
      if (var5.getIngredients().size() == 1) {
         if (var2 != null) {
            if (!this.matchesTradeOutput(var2, var6)) {
               return true;
            }
         } else if (var3 != null && !this.matchesTradeOutput(var3, var6)) {
            return true;
         }
      } else {
         if (!this.matchesTradeOutput(var2, var6)) {
            return true;
         }

         ItemStackWrapper var7 = new ItemStackWrapper((ItemStack)var5.getIngredients().get(1));
         if (!this.matchesTradeOutput(var3, var7)) {
            return true;
         }
      }

      return false;
   }

   private boolean matchesTradeOutput(ItemStack var1, ItemStackWrapper var2) {
      return var2.Eb() && !var2.method9991(var1) ? false : var2.Eb() || !CustomItemDefinition.isCustomItem(var1);
   }

   ItemStack method1401(ItemStack var1, ItemStack var2) {
      if (var1 != null && var1.getType() != Material.AIR) {
         CustomItemDefinition var3 = ContentManager.method8084().method8149(var2);
         if (var3 != null && var3.aor) {
            return var2;
         }

         for (Entry var5 : var1.getEnchantments().entrySet()) {
            if (var3 == null || var3.Dw() == null || !var3.Dw().contains(var5.getKey())) {
               var2.addUnsafeEnchantment((Enchantment)var5.getKey(), (Integer)var5.getValue());
            }
         }

         return var2;
      } else {
         return var2;
      }
   }
}
