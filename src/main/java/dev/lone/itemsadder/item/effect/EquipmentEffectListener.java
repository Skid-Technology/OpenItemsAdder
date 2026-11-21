package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.BaseItemsAdderEvent;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent.Cause;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.PersistentDataHelper;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class EquipmentEffectListener implements IaListener {
   private static final NamespacedKey alN = new NamespacedKey(Main.getPlugin(), "prev_custom_items_equipment");
   private final Map alO = Collections.synchronizedMap(new HashMap());

   public void register() {
      EventRegistrar.registerListener(this, Main.getInstance());
      SchedulerUtils.scheduleRepeating(var1 -> {
         for (Player var3 : Bukkit.getServer().getOnlinePlayers()) {
            this.bx(var3);
         }
      }, 35L, 35L);
      new EffectInventoryListener().register();
   }

   public void applyEquipmentEffect(HumanEntity var1, CustomItemDefinition var2, EquipmentSlot var3) {
      Map var4 = (Map)this.alO.get(var1.getUniqueId());
      if (var4 != null) {
         var4.put(var3, var2);
      }
   }

   public void removeEquipmentEffect(HumanEntity var1, EquipmentSlot var2) {
      Map var3 = (Map)this.alO.get(var1.getUniqueId());
      if (var3 != null) {
         var3.remove(var2);
      }
   }

   private void bv(Player var1) {
      Map var2 = (Map)this.alO.get(var1.getUniqueId());
      this.alO.remove(var1.getUniqueId());
      if (var2 != null && !var2.isEmpty()) {
         HashMap var3 = new HashMap();
         var2.forEach((var1x, var2x) -> var3.put(var1x.name(), var2x != null ? var2x.aok : null));
         String var4 = JsonHelper.mapToJsonString(var3);
         PersistentDataHelper.setStringValue(var1, alN, var4);
      }
   }

   private void bw(Player var1) {
      String var2 = PersistentDataHelper.getStringValue(var1, alN);
      if (var2 != null) {
         HashMap var3 = new HashMap();
         this.alO.put(var1.getUniqueId(), var3);
         Map var4 = JsonHelper.parseMap(var2);
         var4.forEach((var1x, var2x) -> var3.put(EquipmentSlot.valueOf(var1x), ContentManager.method8084().method8154(var2x)));
         this.bx(var1);
      }
   }

   void bx(Player var1) {
      PlayerInventory var2 = var1.getInventory();
      Map var3 = this.alO.computeIfAbsent(var1.getUniqueId(), var0 -> new HashMap());
      ArrayList var4 = new ArrayList();

      for (EquipmentSlot var8 : ItemStackUtils.bMr) {
         ItemStack var9 = var2.getItem(var8);
         CustomItemDefinition var10 = ContentManager.method8084().method8149(var9);
         CustomItemDefinition var11 = (CustomItemDefinition)var3.get(var8);
         if (!CustomItemDefinition.method8782(var10, var11)) {
            var4.add(new EquipmentSwapTask(var8, var1, var11, var10));
         }
      }

      var4.forEach(EquipmentSwapTask::run);
      var3.clear();
      var3.putAll(this.snapshotEquipment(var2));
   }

   private Map snapshotEquipment(PlayerInventory var1) {
      HashMap var2 = new HashMap();

      for (EquipmentSlot var6 : ItemStackUtils.bMr) {
         var2.put(var6, ContentManager.method8084().method8149(var1.getItem(var6)));
      }

      return var2;
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onPlayerJoin(PlayerJoinEvent var1) {
      this.bw(var1.getPlayer());
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onPlayerQuit(PlayerQuitEvent var1) {
      this.bv(var1.getPlayer());
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onPlayerRespawn(PlayerRespawnEvent var1) {
      this.bx(var1.getPlayer());
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onPlayerDeath(PlayerDeathEvent var1) {
      this.alO.remove(var1.getEntity().getUniqueId());
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onItemsAdderEvent(BaseItemsAdderEvent var1) {
      for (Player var3 : Bukkit.getOnlinePlayers()) {
         this.bv(var3);
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
   private void onItemsAdderLoadData(ItemsAdderLoadDataEvent var1) {
      if (var1.getCause() == Cause.RELOAD) {
         for (Player var3 : Bukkit.getOnlinePlayers()) {
            this.bw(var3);
         }
      }
   }
}
