package dev.lone.itemsadder.metrics;

import beer.devs.fastnbt.ApiMetrics;
import beer.devs.fastnbt.ApiMetrics.AdvancedPie;
import beer.devs.fastnbt.ApiMetrics.SimplePie;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.network.ViaVersionIntegration;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MetricsManager implements Listener {
   private static ApiMetrics dh;
   private boolean di;

   public void register() {
      dh = new ApiMetrics(Main.getInstance(), 6);
      dh.addCustomChart(new AdvancedPie("client_versions", () -> Main.config.getBoolean("metrics") ? bq() : Map.of()));
      Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
   }

   @EventHandler
   private void method1908(ItemsAdderLoadDataEvent var1) {
      if (Main.config.getBoolean("metrics") && !this.di) {
         this.di = true;
         dh.addCustomChart(new SimplePie("activations", () -> {
            boolean var1x = FieldUtils.getDeclaredField(Main.class, new ActivationFieldProbe(this).toString(), true) != null;
            return var1x ? "Yes" : "No";
         }));
         dh.addCustomChart(new SimplePie("servers_using_custom_entities", () -> {
            if (Settings.get().customEntitiesEnabled && CustomEntityManager.isInitialized()) {
               int var0 = CustomEntityManager.getInstance().oN.size();
               if (var0 >= 1 && var0 <= 5) {
                  return "1-5";
               } else if (var0 >= 6 && var0 <= 10) {
                  return "6-10";
               } else if (var0 >= 11 && var0 <= 15) {
                  return "11-15";
               } else if (var0 >= 16 && var0 <= 20) {
                  return "16-20";
               } else if (var0 >= 21 && var0 <= 30) {
                  return "21-30";
               } else {
                  return var0 >= 31 && var0 <= 50 ? "31-50" : "50+";
               }
            } else {
               return "0";
            }
         }));
         dh.addCustomChart(new SimplePie("servers_using_emotes", () -> Settings.get().emotesEnabled ? "true" : "false"));
         dh.addCustomChart(new SimplePie("pack_hosting_method", () -> ContentManager.method8084().aL.delivery.getName()));
         dh.addCustomChart(new SimplePie("servers_using_viaversion", () -> Main.viaVersionPresent ? "true" : "false"));
         dh.addCustomChart(new SimplePie("servers_using_complex_furniture", () -> {
            if (!Settings.get().customEntitiesEnabled) {
               return "0";
            } else {
               Collection var0 = ContentManager.method8084().method8145("complex_furniture");
               int var1x = var0 == null ? 0 : var0.size();
               if (var1x >= 1 && var1x <= 5) {
                  return "1-5";
               } else if (var1x >= 6 && var1x <= 10) {
                  return "6-10";
               } else if (var1x >= 11 && var1x <= 15) {
                  return "11-15";
               } else if (var1x >= 16 && var1x <= 20) {
                  return "16-20";
               } else if (var1x >= 21 && var1x <= 30) {
                  return "21-30";
               } else {
                  return var1x >= 31 && var1x <= 50 ? "31-50" : "50+";
               }
            }
         }));
         dh.addCustomChart(new SimplePie("servers_using_legacy_armors", () -> {
            if (ContentManager.method8084().ad.isEmpty()) {
               return "0";
            } else {
               int var0 = ContentManager.method8084().ad.size();
               if (var0 <= 5) {
                  return "1-5";
               } else if (var0 <= 10) {
                  return "6-10";
               } else if (var0 <= 15) {
                  return "11-15";
               } else if (var0 <= 20) {
                  return "16-20";
               } else if (var0 <= 30) {
                  return "21-30";
               } else {
                  return var0 <= 50 ? "31-50" : "50+";
               }
            }
         }));
         dh.addCustomChart(new SimplePie("servers_using_equipments", () -> {
            if (ContentManager.method8084().aw.isEmpty()) {
               return "0";
            } else {
               int var0 = ContentManager.method8084().aw.size();
               if (var0 <= 5) {
                  return "1-5";
               } else if (var0 <= 10) {
                  return "6-10";
               } else if (var0 <= 15) {
                  return "11-15";
               } else if (var0 <= 20) {
                  return "16-20";
               } else if (var0 <= 30) {
                  return "21-30";
               } else {
                  return var0 <= 50 ? "31-50" : "50+";
               }
            }
         }));
      }
   }

   static Map bq() {
      if (!Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) {
         return Map.of();
      }

      try {
         return ViaVersionIntegration.co();
      } catch (RuntimeException | LinkageError var1) {
         return Map.of();
      }
   }

   public static void br() {
      if (dh != null) {
         dh.shutdown();
      }
   }
}
