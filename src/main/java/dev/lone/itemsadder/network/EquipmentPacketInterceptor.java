package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ContainerContentPacketView;
import beer.devs.nativepackets.api.EntityEquipmentPacketView;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.NativeEquipment;
import beer.devs.nativepackets.api.NativeEquipmentSlot;
import beer.devs.nativepackets.api.NativeValue;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListener;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketType;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.SetSlotPacketView;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.resourcepack.ResourcePackService;
import dev.lone.itemsadder.util.ItemMetaReflection;
import dev.lone.itemsadder.util.TrackedSet;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.UnaryOperator;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class EquipmentPacketInterceptor implements AutoCloseable {
   private static final NamespacedKey bsl = NamespacedKey.minecraft("diamond");
   private static final NativeEquipmentSlot[] bsm = new NativeEquipmentSlot[]{
      NativeEquipmentSlot.HEAD, NativeEquipmentSlot.CHEST, NativeEquipmentSlot.LEGS, NativeEquipmentSlot.FEET
   };
   private final ResourcePackService bsn;
   private final UnaryOperator bso;
   private final PacketListenerHandle bsp;
   private final PacketListenerHandle bsq;
   private final PacketListenerHandle bsr;
   private final TrackedSet bss = new TrackedSet();
   private volatile boolean cV = true;

   EquipmentPacketInterceptor(ResourcePackService var1) {
      this.bsn = var1;
      this.bso = NativePacketsHolder.PE().Py().equippableAssetOverride("itemsadder", "minecraft:diamond");
      ItemMetaReflection.ensureInitialized();
      this.bsp = this.method1812(PacketTypes.ENTITY_EQUIPMENT, this::method1814);
      this.bsq = this.method1812(PacketTypes.SET_SLOT, this::method1815);
      this.bsr = this.method1812(PacketTypes.CONTAINER_CONTENT, this::method1816);
   }

   private PacketListenerHandle method1812(PacketType var1, PacketListener var2) {
      return NativePacketsHolder.PE()
         .method1888(Main.getInstance(), PacketSelector.exact(var1), ListenerPriority.HIGHEST, false, ListenerFailurePolicy.PASS_ORIGINAL, var2);
   }

   private boolean method1813(PacketEvent var1) {
      if (this.cV && !var1.isCancelled() && Settings.get().showFallbackArmorOnPackNotLoaded) {
         UUID var2 = NativePacketsHolder.PE().method1883(var1);
         return var2 != null && !this.bsn.isInstalledById(var2);
      } else {
         return false;
      }
   }

   private void method1814(PacketEvent var1) {
      if (this.method1813(var1)) {
         EntityEquipmentPacketView var2 = (EntityEquipmentPacketView)var1.view(EntityEquipmentPacketView.class);

         for (int var3 = 0; var3 < var2.equipmentCount(); var3++) {
            if (av(var2.slotNativeHandle(var3))) {
               NativeValue var4 = this.aw(var2.itemNativeHandle(var3));
               if (var4 != null) {
                  var2.itemNativeHandle(var3, var4.nativeHandle());
               }
            }
         }
      }
   }

   private void method1815(PacketEvent var1) {
      if (this.method1813(var1)) {
         SetSlotPacketView var2 = (SetSlotPacketView)var1.view(SetSlotPacketView.class);
         if (var2.containerId() == 0 && var2.slot() >= 5 && var2.slot() <= 8) {
            NativeValue var3 = this.aw(var2.item().nativeHandle());
            if (var3 != null) {
               var2.item(var3);
            }
         }
      }
   }

   private void method1816(PacketEvent var1) {
      if (this.method1813(var1)) {
         ContainerContentPacketView var2 = (ContainerContentPacketView)var1.view(ContainerContentPacketView.class);
         if (var2.containerId() == 0 && var2.itemCount() >= 9) {
            for (int var3 = 5; var3 <= 8; var3++) {
               NativeValue var4 = this.aw(var2.item(var3).nativeHandle());
               if (var4 != null) {
                  var2.item(var3, var4);
               }
            }
         }
      }
   }

   private static boolean av(Object var0) {
      if (var0 instanceof Enum var1) {
         switch (var1.name()) {
            case "HEAD":
            case "CHEST":
            case "LEGS":
            case "FEET":
               return true;
         }
      }

      return false;
   }

   private NativeValue aw(Object var1) {
      Object var2 = this.bso.apply(var1);
      return var2 == var1 ? null : () -> var2;
   }

   private static boolean ao(ItemStack var0) {
      return var0 != null && !var0.getType().isAir() && var0.hasItemMeta() && CustomItemDefinition.method8748(var0) ? method1817(var0.getItemMeta()) : false;
   }

   private static boolean method1817(ItemMeta var0) {
      if (!ItemMetaReflection.hasEquippable(var0)) {
         return false;
      }

      NamespacedKey var1 = ItemMetaReflection.getEquippableModel(ItemMetaReflection.getEquippable(var0));
      return var1 != null && !var1.equals(bsl);
   }

   void cQ(Player var1) {
      if (this.cV && this.bss.addTracked(var1)) {
         NativePacketsHolder.PE().IP().entity(Main.getInstance(), var1, () -> {
            this.bss.removeTracked(var1);
            if (this.cV && var1.isOnline()) {
               var1.updateInventory();
               this.method1818(var1, var1);
               int var2 = Bukkit.getViewDistance() * 16;

               for (Entity var4 : var1.getNearbyEntities(var2, var2, var2)) {
                  if (var4 instanceof LivingEntity var5) {
                     this.method1818(var1, var5);
                  }
               }
            }
         }, () -> this.bss.removeTracked(var1));
      }
   }

   private void method1818(Player var1, LivingEntity var2) {
      Runnable var3 = () -> {
         if (this.cV && var2.isValid()) {
            EntityEquipment var3x = var2.getEquipment();
            if (var3x != null) {
               ItemStack[] var4 = new ItemStack[]{var3x.getHelmet(), var3x.getChestplate(), var3x.getLeggings(), var3x.getBoots()};
               boolean var5 = false;

               for (int var6 = 0; var6 < var4.length; var6++) {
                  if (ao(var4[var6])) {
                     var4[var6] = var4[var6].clone();
                     var5 = true;
                  } else {
                     var4[var6] = null;
                  }
               }

               if (var5) {
                  int var8 = var2.getEntityId();
                  Runnable var7 = () -> {
                     if (this.cV && var1.isOnline()) {
                        ArrayList var4x = new ArrayList(4);

                        for (int var5x = 0; var5x < var4.length; var5x++) {
                           if (var4[var5x] != null) {
                              var4x.add(new NativeEquipment(bsm[var5x], NativePacketsHolder.PE().Py().item(var4[var5x])));
                           }
                        }

                        NativePacketsHolder.PE().method1867(var1, NativePacketsHolder.PE().Pv().equipment(var8, var4x));
                     }
                  };
                  if (NativePacketsHolder.PE().IP().ownsEntity(var1)) {
                     var7.run();
                  } else {
                     NativePacketsHolder.PE().IP().entity(Main.getInstance(), var1, var7, () -> {});
                  }
               }
            }
         }
      };
      if (NativePacketsHolder.PE().IP().ownsEntity(var2)) {
         var3.run();
      } else {
         NativePacketsHolder.PE().IP().entity(Main.getInstance(), var2, var3, () -> {});
      }
   }

   @Override
   public void close() {
      this.cV = false;
      this.bss.clear();
      NativePacketsHolder.PE().method1889(Main.getInstance(), this.bsp);
      NativePacketsHolder.PE().method1889(Main.getInstance(), this.bsq);
      NativePacketsHolder.PE().method1889(Main.getInstance(), this.bsr);
   }
}
