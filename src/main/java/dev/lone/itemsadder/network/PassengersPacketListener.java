package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.PassengersPacketView;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class PassengersPacketListener implements AutoCloseable {
   private final PacketListenerHandle UX;
   private final NativePacketsBridge UY;
   private final Object UZ;
   private volatile boolean cV = true;
   private volatile Set Va = Set.of();
   private CompletionStage Vb;

   PassengersPacketListener() {
      this.UY = NativePacketsHolder.PE();
      this.UZ = this;
      this.UX = this.UY
         .method1888(
            this.UZ,
            PacketSelector.exact(PacketTypes.PASSENGERS),
            ListenerPriority.HIGHEST,
            false,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var1 -> this.method1692(var1, (PassengersPacketView)var1.view(PassengersPacketView.class))
         );
   }

   private void method1692(PacketEvent var1, PassengersPacketView var2) {
      if (this.cV && !var1.isCancelled()) {
         int var3 = var2.vehicleId();
         if (this.Va.contains(var3) || CustomEntityManager.getInstance().pg.contains(var3)) {
            var1.setCancelled(true);
         } else if (CustomEntityManager.getInstance().hasEntityId(var3)) {
            int[] var4 = new int[var2.passengerCount()];

            for (int var5 = 0; var5 < var4.length; var5++) {
               var4[var5] = var2.passengerId(var5);
            }

            var1.sync((var2x, var3x) -> {
               CustomEntityImpl var4x = CustomEntityManager.getInstance().getById(var3);
               if (var4x != null && var3x.player() instanceof Player var5x) {
                  var4x.setNametagHiddenFor(var5x, var4);
               }
            });
         }
      }
   }

   void method1693(Set var1) {
      this.Va = Set.copyOf(var1);
   }

   CompletionStage gz() {
      if (this.Vb != null) {
         return this.Vb;
      }

      if (this.Va.isEmpty()) {
         this.close();
         return this.Vb = CompletableFuture.completedFuture(null);
      }

      Object var1 = this.UY.Pv().removeEntities(this.Va.stream().mapToInt(Integer::intValue).toArray());
      ArrayList var2 = new ArrayList();

      for (Player var4 : Bukkit.getOnlinePlayers()) {
         ConnectionRecord var5 = this.UY.cB(var4);
         if (var5 != null && var5.PC().isOpen()) {
            CompletableFuture var6 = new CompletableFuture();
            var2.add(var6);

            try {
               var5.PC().send(this, var1, var1x -> var6.complete(null));
            } catch (RuntimeException var8) {
               var6.completeExceptionally(var8);
            }
         }
      }

      return this.Vb = CompletableFuture.allOf(var2.toArray(CompletableFuture[]::new)).whenComplete((var1x, var2x) -> this.close());
   }

   @Override
   public void close() {
      this.cV = false;
      this.Va = Set.of();
      this.UY.method1889(this.UZ, this.UX);
   }
}
