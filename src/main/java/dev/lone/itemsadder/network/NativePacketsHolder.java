package dev.lone.itemsadder.network;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.entity.PacketDisplayBridge;
import dev.lone.itemsadder.entity.RotationPolicy;
import dev.lone.itemsadder.nms.NmsRegistry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class NativePacketsHolder {
   public PacketDisplayBridge blL;
   private final NativePacketsBridge blM = NativePacketsBridge.ah(Main.getInstance());
   static NativePacketsHolder blN;

   NativePacketsHolder() {
      this.blL = (PacketDisplayBridge)NmsRegistry.method2777(PacketDisplayBridge.class);
   }

   public static PacketDisplayBridge PD() {
      return blN.blL;
   }

   public static NativePacketsBridge PE() {
      return blN.blM;
   }

   public static boolean aN() {
      return blN != null && blN.blL != null;
   }

   public static void init() {
      blN = new NativePacketsHolder();
      blN.blM.method1878(blN.blL.getPassengerRegistry());
      Bukkit.getOnlinePlayers().forEach(var0 -> blN.blM.method1881(var0, RotationPolicy.fromId(ViaVersionIntegration.method4866(var0.getUniqueId()))));
   }

   public static void ax(Player var0) {
      blN.blM.cE(var0);
   }

   public static void es() {
      method5248(CompletableFuture.completedFuture(null));
   }

   public static CompletionStage method5248(CompletionStage var0) {
      NativePacketsHolder var1 = blN;
      return var1 == null ? var0 : var0.whenComplete((var1x, var2) -> {
         var1.blM.close();
         if (blN == var1) {
            blN = null;
         }
      });
   }

   public static void method5249(Player var0, Entity var1) {
      method5250(var0, var1.getEntityId());
   }

   public static void al(Entity var0) {
      Object var1 = PE().Pv().removeEntities(new int[]{var0.getEntityId()});

      for (Player var3 : Bukkit.getOnlinePlayers()) {
         PE().method1868(var3, var1);
      }
   }

   public static void method5250(Player player, int id) {
      Object var2 = PE().Pv().removeEntities(new int[]{id});
      PE().method1868(player, var2);
   }
}
