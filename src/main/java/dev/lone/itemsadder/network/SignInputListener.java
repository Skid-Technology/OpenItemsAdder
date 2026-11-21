package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.UpdateSignPacketView;
import dev.lone.itemsadder.text.SignLines;
import dev.lone.itemsadder.util.BlockPos;
import dev.lone.itemsadder.util.Log;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SignInputListener implements AutoCloseable {
   private static final int aLY = 9;
   private static final int aLZ = 4;
   private static final String aMa = "{\"text\":\"%s\"}";
   private static final String aMb = "minecraft:sign";
   private final Plugin aMc;
   private final Map aMd;
   private final Map aMe;
   private final PacketListenerHandle aMf;
   private volatile boolean cV = true;

   public SignInputListener(Plugin var1) {
      this.aMc = var1;
      this.aMd = new ConcurrentHashMap();
      this.aMe = new ConcurrentHashMap();
      this.aMf = NativePacketsHolder.PE()
         .method1888(var1, PacketSelector.exact(PacketTypes.UPDATE_SIGN), ListenerPriority.NORMAL, true, ListenerFailurePolicy.CANCEL_PACKET, var1x -> {
            if (this.cV) {
               UUID var2 = NativePacketsHolder.PE().method1883(var1x);
               if (var2 != null) {
                  long var3 = var1x.connection().id();
                  UpdateSignPacketView var5 = (UpdateSignPacketView)var1x.view(UpdateSignPacketView.class);
                  SignLines var6 = new SignLines(var5.line(0), var5.line(1), var5.line(2), var5.line(3));
                  var1x.sync((var5x, var6x) -> this.method1824(var5x, var6x.player(), var2, var3, var6));
               }
            }
         });
   }

   public SignInputRequest method1823(Player var1, List var2) {
      Objects.requireNonNull(var1, "player");
      Objects.requireNonNull(var2, "text");
      SignInputRequest var3 = new SignInputRequest(this, var1, var2);
      var3.method2726(var3x -> {
         this.aMe.put(var1, var3x);
         this.aMd.putIfAbsent(var1, var3);
      });
      return var3;
   }

   private void method1824(PacketEvent var1, Object var2, UUID var3, long var4, SignLines var6) {
      if (this.cV && var2 instanceof Player var7 && NativePacketsHolder.PE().method1885(var3, var4) == var7) {
         SignInputRequest var8 = (SignInputRequest)this.aMd.get(var7);
         BlockPos var9 = (BlockPos)this.aMe.get(var7);
         if (var8 != null && var9 != null) {
            var1.cancel();
            this.aMd.remove(var7, var8);
            this.aMe.remove(var7, var9);

            try {
               boolean var10 = var8.aMj.test(var7, var6.MO());
               if (!var10 && var8.aMk) {
                  Bukkit.getScheduler().runTaskLater(this.aMc, () -> {
                     if (this.cV && NativePacketsHolder.PE().method1885(var3, var4) == var7) {
                        var8.Mn();
                     }
                  }, 2L);
               }
            } catch (RuntimeException | LinkageError var14) {
               Log.warn("Failed to handle sign-menu response", var14);
            } finally {
               var7.sendBlockChange(new Location(var7.getWorld(), var9.x(), var9.y(), var9.z()), Material.AIR.createBlockData());
            }
         }
      }
   }

   @Override
   public void close() {
      if (this.cV) {
         this.cV = false;
         NativePacketsHolder.PE().method1889(this.aMc, this.aMf);
         this.aMd.clear();
         this.aMe.clear();
      }
   }
}
