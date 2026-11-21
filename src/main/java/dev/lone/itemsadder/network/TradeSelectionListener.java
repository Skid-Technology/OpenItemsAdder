package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.MerchantOffersPacketView;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.SchedulerAccess;
import beer.devs.nativepackets.api.SelectTradePacketView;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.loot.MerchantOfferInterceptorHolder;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.PlayerInventory;

public class TradeSelectionListener implements Listener {
   PacketListenerHandle ic;
   PacketListenerHandle ie;
   Cache if_ = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.MINUTES).build();
   private volatile boolean cV;
   private static TradeSelectionListener ig;

   public static void method3372(boolean var0) {
      if (var0) {
         register();
      } else {
         unregister();
      }
   }

   public static void register() {
      if (ig == null) {
         ig = new TradeSelectionListener();
         ig.bX();
      }
   }

   public static void unregister() {
      if (ig != null) {
         ig.bY();
         ig = null;
      }
   }

   private void bX() {
      this.cV = true;
      this.ic = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.MERCHANT_OFFERS),
            ListenerPriority.NORMAL,
            false,
            ListenerFailurePolicy.PASS_ORIGINAL,
            this::method3373
         );
      this.ie = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(),
            PacketSelector.exact(PacketTypes.SELECT_TRADE),
            ListenerPriority.NORMAL,
            false,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var1 -> this.method3375(var1, (SelectTradePacketView)var1.view(SelectTradePacketView.class))
         );
      EventRegistrar.registerListener(this, Main.getInstance());
   }

   private void bY() {
      this.cV = false;
      if (this.ic != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.ic);
      }

      if (this.ie != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.ie);
      }

      this.if_.invalidateAll();
      EventRegistrar.unregisterListener(this);
   }

   private void method3373(PacketEvent var1) {
      long var2 = var1.connection().id();
      var1.sync(
         (var3, var4) -> {
            if (this.cV
               && var4.player() instanceof Player var5
               && var5.isOnline()
               && NativePacketsHolder.PE().method1885(var5.getUniqueId(), var2) == var5
               && var5.getOpenInventory().getTopInventory() instanceof MerchantInventory var6
               && this.method3374(var5, var6)) {
               MerchantOffersPacketView var9 = (MerchantOffersPacketView)var3.view(MerchantOffersPacketView.class);
               MerchantOfferInterceptorHolder.Qh().method5383(var5, var9);
            }
         }
      );
   }

   private boolean method3374(Player var1, MerchantInventory var2) {
      Settings var3 = Settings.get();
      if (ContentManager.method8084().bf.isMerchantOpen(var1)) {
         return var3.fixTradeGuiItemsAdderMachines;
      } else {
         return var2.getHolder() instanceof AbstractVillager ? var3.fixTradeGuiVanillaVillagers : var3.fixTradeGuiOtherPlugins;
      }
   }

   private void method3375(PacketEvent var1, SelectTradePacketView var2) {
      long var3 = var1.connection().id();
      int var5 = var2.tradeIndex();
      var1.sync((var4, var5x) -> {
         if (this.cV && var5x.player() instanceof Player var6 && var6.isOnline()) {
            UUID var14 = var6.getUniqueId();
            if (var6.getOpenInventory().getTopInventory() instanceof MerchantInventory var8) {
               Merchant var15 = var8.getMerchant();
               if (var5 >= 0 && var5 < var15.getRecipeCount() && MerchantOfferInterceptorHolder.bpV.asMap().containsKey(var6)) {
                  MerchantRecipe var10 = var15.getRecipe(var5);
                  ItemStack var11 = var10.getAdjustedIngredient1();
                  ItemStack var12 = var10.getIngredients().size() == 2 ? (ItemStack)var10.getIngredients().get(1) : null;
                  if (CustomItemDefinition.isCustomItem(var11) || CustomItemDefinition.isCustomItem(var12)) {
                     TradeSelectionRequest var13 = new TradeSelectionRequest(var14, var3, ItemOperationsService.getRegistryBridge().cx(var6), var5);
                     var4.cancel();
                     this.method3376(var13);
                  }
               }
            }
         }
      });
   }

   private void method3376(TradeSelectionRequest var1) {
      SchedulerAccess var2 = NativePacketsHolder.PE().IP();
      var2.globalLater(Main.getInstance(), () -> {
         if (this.cV) {
            Player var3 = NativePacketsHolder.PE().method1885(var1.bk(), var1.bl());
            if (var3 == null) {
               Log.warn("Skipped delayed trade selection replay because the player connection ended: " + var1.bk());
            } else {
               try {
                  var2.entity(Main.getInstance(), var3, () -> this.method3377(var1), () -> this.method3378(var1));
               } catch (RuntimeException | LinkageError var5) {
                  Log.warn("Failed to schedule delayed trade selection replay", var5);
               }
            }
         }
      }, 3L);
   }

   private void method3377(TradeSelectionRequest var1) {
      Player var2 = NativePacketsHolder.PE().method1885(var1.bk(), var1.bl());
      if (this.cV) {
         if (var2 == null) {
            Log.warn("Skipped delayed trade selection replay because the player connection changed: " + var1.bk());
         } else if (!NativePacketsHolder.PE().IP().ownsEntity(var2)) {
            Log.warn("Skipped delayed trade selection replay outside player ownership: " + var1.bk());
         } else if (var2.isOnline()
            && ItemOperationsService.getRegistryBridge().cx(var2) == var1.cb()
            && var2.getOpenInventory().getTopInventory() instanceof MerchantInventory var3
            && MerchantOfferInterceptorHolder.bpV.asMap().containsKey(var2)) {
            Merchant var8 = var3.getMerchant();
            int var5 = var1.ca();
            if (var5 >= 0 && var5 < var8.getRecipeCount()) {
               int var6 = this.method3379(var3, var8, var5);
               if (this.method3380(var2, var6) && var6 != var5) {
                  TradeSelection var7 = new TradeSelection(var6, var1.bl(), var1.cb());
                  this.if_.put(var1.bk(), var7);
                  this.method3381(var1.bk(), var7);
               }
            }
         }
      }
   }

   private void method3378(TradeSelectionRequest var1) {
      if (this.cV) {
         Log.warn("Skipped delayed trade selection replay because player ownership retired: " + var1.bk());
      }
   }

   private int method3379(MerchantInventory var1, Merchant var2, int var3) {
      if (!ItemStackUtils.aE(var1.getItem(2))) {
         return var3;
      }

      MerchantRecipe var4 = var2.getRecipe(var3);
      ItemStack var5 = var4.getAdjustedIngredient1();
      ItemStack var6 = var4.getIngredients().size() == 2 ? (ItemStack)var4.getIngredients().get(1) : null;
      ItemStack var7 = var4.getResult();
      List var8 = var2.getRecipes();
      int var9 = var3 + 1;

      for (int var10 = var8.size(); var9 < var10; var9++) {
         MerchantRecipe var11 = (MerchantRecipe)var8.get(var9);
         ItemStack var12 = var11.getAdjustedIngredient1();
         ItemStack var13 = var11.getIngredients().size() == 2 ? (ItemStack)var11.getIngredients().get(1) : null;
         if (this.method3384(var5, var12) && this.method3384(var6, var13) && this.method3384(var7, var11.getResult())) {
            return var9;
         }
      }

      return var3;
   }

   private boolean method3380(Player var1, int var2) {
      try {
         Object var3 = NativePacketsHolder.PE().Pv().selectTrade(var2);
         NativePacketsHolder.PE().method1877(var1, var3);
         return true;
      } catch (RuntimeException | LinkageError var4) {
         if (var1.isOnline()) {
            Log.warn("Failed to replay trade selection through NativePackets", var4);
         }

         return false;
      }
   }

   private void method3381(UUID var1, TradeSelection var2) {
      SchedulerAccess var3 = NativePacketsHolder.PE().IP();
      var3.globalLater(Main.getInstance(), () -> {
         Player var4 = NativePacketsHolder.PE().method1885(var1, var2.bl());
         if (this.cV) {
            if (var4 == null) {
               Log.warn("Skipped delayed trade slot update because the player connection ended: " + var1);
            } else {
               try {
                  var3.entity(Main.getInstance(), var4, () -> this.method3382(var1, var2), () -> this.method3383(var1));
               } catch (RuntimeException | LinkageError var6) {
                  Log.warn("Failed to schedule delayed trade slot update", var6);
               }
            }
         }
      }, 3L);
   }

   private void method3382(UUID var1, TradeSelection var2) {
      Player var3 = NativePacketsHolder.PE().method1885(var1, var2.bl());
      if (this.cV) {
         if (var3 == null) {
            Log.warn("Skipped delayed trade slot update because the player connection changed: " + var1);
         } else if (!NativePacketsHolder.PE().IP().ownsEntity(var3)) {
            Log.warn("Skipped delayed trade slot update outside player ownership: " + var1);
         } else if (var3.isOnline()
            && ItemOperationsService.getRegistryBridge().cx(var3) == var2.cb()
            && var3.getOpenInventory().getTopInventory() instanceof MerchantInventory var4) {
            Merchant var13 = var4.getMerchant();
            if (var2.ca() >= 0 && var2.ca() < var13.getRecipeCount()) {
               MerchantRecipe var6 = var13.getRecipe(var2.ca());
               ItemStack var7 = var6.getAdjustedIngredient1();
               ItemStack var8 = var6.getIngredients().size() == 2 ? (ItemStack)var6.getIngredients().get(1) : null;
               ItemStack var9 = var4.getItem(0);
               if (!ItemStackUtils.aE(var9) && !ItemStackUtils.aE(var4.getItem(2))) {
                  ItemStack var10 = var7.clone();
                  var10.setAmount(var9.getAmount());
                  ItemOperationsService.giveItemToPlayerInternal(var3, var10, 0, var2.cb());
                  if (var8 != null) {
                     ItemStack var11 = var4.getItem(1);
                     if (!ItemStackUtils.aE(var11)) {
                        ItemStack var12 = var8.clone();
                        var12.setAmount(var11.getAmount());
                        ItemOperationsService.giveItemToPlayerInternal(var3, var12, 1, var2.cb());
                     }
                  }
               }
            }
         }
      }
   }

   private void method3383(UUID var1) {
      if (this.cV) {
         Log.warn("Skipped delayed trade slot update because player ownership retired: " + var1);
      }
   }

   private boolean method3384(ItemStack var1, ItemStack var2) {
      if (ItemStackUtils.aE(var1) && ItemStackUtils.aE(var2)) {
         return true;
      }

      if (var1 != null && var2 != null) {
         if (var1.getAmount() < var2.getAmount()) {
            return false;
         } else {
            return !CustomItemDefinition.isCustomItem(var1) && !CustomItemDefinition.isCustomItem(var1)
               ? var1.isSimilar(var2)
               : CustomItemDefinition.method8780(var1, var2);
         }
      } else {
         return false;
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method3385(TradeSelectEvent var1) {
      if (this.cV
         && Settings.get().fixTradeGuiItemsAdderMachines
         && var1.getWhoClicked() instanceof Player var2
         && ContentManager.method8084().bf.isMerchantOpen(var2)) {
         int var5 = var1.getIndex();
         int var4 = ItemOperationsService.getRegistryBridge().cx(var2);
         NativePacketsHolder.PE().IP().entity(Main.getInstance(), var2, () -> this.method3386(var2, var5, var4), () -> {});
      }
   }

   private void method3386(Player var1, int var2, int var3) {
      if (this.cV
         && var1.isOnline()
         && ContentManager.method8084().bf.isMerchantOpen(var1)
         && ItemOperationsService.getRegistryBridge().cx(var1) == var3
         && var1.getOpenInventory().getTopInventory() instanceof MerchantInventory var4
         && var4.getSelectedRecipeIndex() == var2
         && var2 >= 0
         && var2 < var4.getMerchant().getRecipeCount()) {
         List var8 = var4.getMerchant().getRecipe(var2).getIngredients();
         boolean var6 = false;

         for (int var7 = 0; var7 < var8.size() && var7 < 2; var7++) {
            if (!CustomItemDefinition.isCustomItem((ItemStack)var8.get(var7)) && CustomItemDefinition.isCustomItem(var4.getItem(var7))) {
               var6 |= this.method3387(var1.getInventory(), var4, var2, var7);
            }
         }

         if (var6) {
            var1.updateInventory();
            TradeSelection var9 = (TradeSelection)this.if_.getIfPresent(var1.getUniqueId());
            if (var9 != null && var9.cb() == var3 && var9.ca() == var2) {
               this.method3381(var1.getUniqueId(), var9);
            }
         }
      }
   }

   private boolean method3387(PlayerInventory var1, MerchantInventory var2, int var3, int var4) {
      ItemStack var5 = var2.getItem(var4);

      for (int var6 = 0; var6 < 36; var6++) {
         int var7 = (var6 + 9) % 36;
         ItemStack var8 = var1.getItem(var7);
         if (!ItemStackUtils.aE(var8) && !CustomItemDefinition.isCustomItem(var8) && MerchantOfferInterceptorHolder.Qh().method5384(var2, var3, var4, var8)) {
            int var9 = Math.min(var8.getMaxStackSize(), var2.getMaxStackSize());
            if (var8.getAmount() <= var9) {
               ItemStack var10 = var8.clone();
               var1.setItem(var7, var5);
               var2.setItem(var4, var10);

               for (int var11 = 0; var11 < 36 && var10.getAmount() < var9; var11++) {
                  int var12 = (var11 + 9) % 36;
                  ItemStack var13 = var1.getItem(var12);
                  if (!ItemStackUtils.aE(var13) && var10.isSimilar(var13)) {
                     int var14 = Math.min(var9 - var10.getAmount(), var13.getAmount());
                     var10.setAmount(var10.getAmount() + var14);
                     ItemStack var15 = var13.clone();
                     var15.setAmount(var13.getAmount() - var14);
                     var1.setItem(var12, var15.getAmount() == 0 ? null : var15);
                  }
               }

               var2.setItem(var4, var10);
               return true;
            }
         }
      }

      ItemStack var16 = (ItemStack)var1.addItem(new ItemStack[]{var5.clone()}).get(0);
      var2.setItem(var4, var16);
      return var16 == null || var16.getAmount() != var5.getAmount();
   }

   @EventHandler
   private void method3388(InventoryClickEvent var1) {
      if (var1.getClickedInventory() != null) {
         if (var1.getView().getTopInventory() instanceof MerchantInventory) {
            if (!(var1.getWhoClicked() instanceof Player)) {
               return;
            }

            Player var2 = (Player)var1.getWhoClicked();
            UUID var3 = var2.getUniqueId();
            TradeSelection var4 = (TradeSelection)this.if_.getIfPresent(var3);
            if (var4 == null) {
               return;
            }

            if (NativePacketsHolder.PE().method1885(var3, var4.bl()) != var2 || ItemOperationsService.getRegistryBridge().cx(var2) != var4.cb()) {
               this.if_.invalidate(var3);
               return;
            }

            if (var1.getClickedInventory() instanceof MerchantInventory && var1.getSlotType() == SlotType.RESULT) {
               var2.updateInventory();
               this.method3380(var2, var4.ca());
            }

            this.method3381(var3, var4);
         }
      }
   }

   @EventHandler
   private void method3389(InventoryCloseEvent var1) {
      if (var1.getPlayer() instanceof Player var2) {
         this.if_.invalidate(var2.getUniqueId());
         MerchantOfferInterceptorHolder.bpV.invalidate(var2);
         MerchantOfferInterceptorHolder.Qh().cP(var2);
      }
   }
}
