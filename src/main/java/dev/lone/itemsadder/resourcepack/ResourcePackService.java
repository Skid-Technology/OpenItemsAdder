package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.internal.SwitchMap75;
import dev.lone.itemsadder.network.EquipmentPacketInterceptor;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.network.ResourcePackListener;
import dev.lone.itemsadder.util.Log;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class ResourcePackService {
   public final PackDownloadGate downloadGate;
   public ResourcePackGenerator generator;
   public PackDeliveryBase delivery;
   public final ResourcePackManager manager;
   private final EquipmentPacketInterceptor equipmentInterceptor;
   @Nullable
   public ResourcePackListener listener = null;
   private final Map pushStatuses = newStatusMap();

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public ResourcePackService(Plugin var1) {
      Settings var2 = Settings.get();
      this.manager = new ResourcePackManager(this, var1.getDataFolder().toPath().resolve("output/client_packs"));
      switch (SwitchMap75.bBX[var2.packHostingMode.ordinal()]) {
         case 1:
            this.delivery = new HostedPackDeliveryImpl(var1, this);
            break;
         case 2:
            this.delivery = new JoinGatedPackDelivery(var1, this);
            break;
         case 3:
            this.delivery = new SelfHostedPackDelivery(var1, this);
            break;
         case 4:
            this.delivery = new CdnPackDelivery(var1, this);
            break;
         case 5:
            this.delivery = new ProgressPackDelivery(var1, this);
            break;
         case 6:
            this.delivery = new ExternalPackDelivery(var1, this);
            Log.log("[Pack] 'no-host' selected. Pack upload and delivery are managed manually or by another plugin.");
            break;
         default:
            this.delivery = new PlayerJoinPackDelivery(var1, this);
      }

      this.generator = new ResourcePackGenerator(this);
      if (var2.perClientVersionDelivery && this.delivery instanceof SelfHostedPackDelivery) {
         Log.warn("[Pack] per_client_version is unavailable with a manual external URL. Using universal; no client packs will be generated.");
      }

      this.downloadGate = new PackDownloadGate();
      this.downloadGate.register();
      if (this.listener != null) {
         this.listener.unregister();
      }

      if (var2.packHostingMode != PackHostingMode.NO_HOST) {
         this.listener = new ResourcePackListener(this);
         this.listener.register(var1);
      }

      this.equipmentInterceptor = var2.showFallbackArmorOnPackNotLoaded
            && Version.isAtLeast(Version.v1_21_3)
            && !(this.delivery instanceof ExternalPackDelivery)
         ? new EquipmentPacketInterceptor(this)
         : null;
      if (Main.Lc()) {
         for (Player var4 : Bukkit.getOnlinePlayers()) {
            if (!this.pushStatuses.containsKey(var4.getUniqueId())) {
               Log.warn("Setting player resourcepack status to installed because the plugin was reloaded.");
               this.setPlayerStatus(var4, PackPushStatus.INSTALLED);
            }
         }
      }
   }

   public void setPlayerStatus(Player var1, PackPushStatus var2) {
      this.setPlayerStatusById(var1.getUniqueId(), var2);
   }

   public void setPlayerStatusById(UUID var1, PackPushStatus var2) {
      boolean var3 = this.isInstalledById(var1);
      putStatus(this.pushStatuses, var1, var2);
      if (this.equipmentInterceptor != null && Settings.get().showFallbackArmorOnPackNotLoaded && var3 != this.isInstalledById(var1)) {
         Player var4 = NativePacketsHolder.PE().method1884(var1);
         if (var4 != null) {
            this.equipmentInterceptor.cQ(var4);
         }
      }
   }

   static Map newStatusMap() {
      return Collections.synchronizedMap(new WeakHashMap());
   }

   static void putStatus(Map var0, UUID var1, PackPushStatus var2) {
      synchronized (var0) {
         var0.remove(var1);
         if (var2 != PackPushStatus.NONE) {
            var0.put(var1, var2);
         }
      }
   }

   public void clearPlayerStatus(Player var1) {
      this.pushStatuses.remove(var1.getUniqueId());
   }

   public PackPushStatus getPlayerStatus(Player var1) {
      return this.pushStatuses.getOrDefault(var1.getUniqueId(), PackPushStatus.NONE);
   }

   public boolean isInstalled(Player var1) {
      return this.isInstalledById(var1.getUniqueId());
   }

   boolean isInstalledById(UUID var1) {
      PackPushStatus var2 = (PackPushStatus)this.pushStatuses.get(var1);
      return var2 == null ? false : var2 == PackPushStatus.INSTALLED;
   }

   public void stop() {
      this.generator.cancelGeneration();
      if (this.listener != null) {
         this.listener.unregister();
      }

      if (this.equipmentInterceptor != null) {
         this.equipmentInterceptor.close();
      }

      this.manager.close();
      this.delivery.stop();
      this.downloadGate.unregister();
   }

   public void resetAfterFailure() {
      this.generator.clearGenerationState();
   }
}
