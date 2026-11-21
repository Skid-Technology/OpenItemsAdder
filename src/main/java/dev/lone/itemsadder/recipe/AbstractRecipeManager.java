package dev.lone.itemsadder.recipe;

import beer.devs.fastnbt.nms.Version;
import beer.devs.nativepackets.api.PacketListenerHandle;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractRecipeManager implements RecipeManager, Listener {
   public static String boe = "zzzfake_";
   protected final Main bof;
   public final ContentManager bog;
   protected PacketListenerHandle boh;
   protected PacketListenerHandle boi;
   private RecipeJoinListener boj;
   private RecipeDiscoveryListener bok;
   public WeakHashMap/* @Nullable */ bol = new WeakHashMap();
   protected Map bom = new ConcurrentHashMap();

   public AbstractRecipeManager() {
      this.bof = Main.getInstance();
      this.bog = ContentManager.method8084();
   }

   public static void method1418(Player var0, AbstractCustomRecipe var1) {
      if (Settings.get().recipeNoPermMsgChat) {
         Log.sendPrefixed(var0, Main.aFi.lj("no-craft-permission"));
      }

      SchedulerUtils.runLaterSync(() -> method1420(var0, var1), 1L);
   }

   public static void method1419(Player var0, AbstractCustomRecipe var1) {
      if (Settings.get().recipeNoPermMsgChat) {
         Log.sendPrefixed(var0, Main.aFi.lj("no-craft-permission"));
      }

      method1420(var0, var1);
   }

   public static void method1420(Player var0, AbstractCustomRecipe var1) {
      if (var1.JV()) {
         RecipeManagerHolder.Qb().method839(var0, ((Keyed)var1.JT()).getKey());
      }

      if (Settings.get().recipeNoPermMsgRecipebook) {
         ItemStack var2 = var1.JR().clone();
         Component var3 = ItemStackUtils.as(var2);
         if (var3 == null) {
            var3 = Component.translatable(ItemOperationsService.getRegistryBridge().ag(var2));
         }

         var3 = var3.font(Key.key("minecraft:default"));
         Component var4 = ((TextComponent)((TextComponent)Component.text("!A@").color(TextColor.color(255, 255, 255))).decoration(TextDecoration.ITALIC, false))
            .font(Key.key("_iainternal:gui"));
         var4 = var4.append(var3);
         ItemStackUtils.method7223(var2, var4);
         byte var5 = 2;
         if (var1 instanceof CraftingRecipe) {
            var5 = 0;
         }

         if (var1 instanceof SmithingRecipe) {
            var5 = 3;
         }

         ItemOperationsService.giveItemToPlayer(var0, var2, var5);
      }
   }

   protected void PU() {
      if (Settings.get().unlockAllRecipesOnJoin) {
         this.boj = new RecipeJoinListener(this.bof);
      } else if (Settings.get().unlockRecipesAutomatically) {
         this.bok = new RecipeDiscoveryListener(this.bof, this);
      }
   }

   public void PV() {
      if (this.boh != null) {
         NativePacketsHolder.PE().method1889(this, this.boh);
         this.boh = null;
      }

      if (this.boi != null) {
         NativePacketsHolder.PE().method1889(this, this.boi);
         this.boi = null;
      }

      EventRegistrar.unregisterListener(this.boj);
      EventRegistrar.unregisterListener(this.bok);
      this.bol.clear();
      this.bom.clear();
      EventRegistrar.unregisterListener(this);
   }

   public void method839(Player player, @NotNull NamespacedKey recipeKey) {
      if (!Version.isAtLeast(Version.v1_21_3)) {
         Object var3 = NativePacketsHolder.PE().Pv().ghostRecipe(ItemOperationsService.getRegistryBridge().cx(player), recipeKey.toString());
         NativePacketsHolder.PE().method1868(player, var3);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method587(PlayerQuitEvent var1) {
      this.bol.remove(var1.getPlayer());
   }

   public abstract void method598(Set var1);

   public abstract void PW();

   public abstract NamespacedKey method600(Furnace var1);

   public abstract void method601(Furnace var1, NamespacedKey var2);

   public abstract Set method602(Set var1);

   @Nullable
   public abstract String method603(CraftingInventory var1);

   public void PX() {
   }

   public boolean method648(Recipe bukkitRecipe) {
      return Bukkit.addRecipe(bukkitRecipe);
   }

   public boolean method651(NamespacedKey recipeKey) {
      return Bukkit.removeRecipe(recipeKey);
   }

   public void PY() {
   }

   public boolean PZ() {
      return false;
   }

   @Nullable
   public ItemStack method770(CraftingInventory inventory) {
      return null;
   }

   public abstract boolean method604(ShapedRecipe var1, CraftingInventory var2);
}
