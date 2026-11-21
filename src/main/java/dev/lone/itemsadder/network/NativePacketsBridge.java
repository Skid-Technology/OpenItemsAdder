package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ConnectionHandle;
import beer.devs.nativepackets.api.EntityAccess;
import beer.devs.nativepackets.api.FastPacketSender;
import beer.devs.nativepackets.api.ItemAccess;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.LoginPacketView;
import beer.devs.nativepackets.api.MinecraftVersion;
import beer.devs.nativepackets.api.NativePacketFactory;
import beer.devs.nativepackets.api.NativePackets;
import beer.devs.nativepackets.api.NativePacketsApi;
import beer.devs.nativepackets.api.NativeTextFactory;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListener;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketType;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.SchedulerAccess;
import beer.devs.nativepackets.api.TypedPacketListener;
import beer.devs.nativepackets.api.WorldAccess;
import dev.lone.itemsadder.entity.PassengerRegistry;
import dev.lone.itemsadder.entity.RotationPolicy;
import io.netty.channel.Channel;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public final class NativePacketsBridge implements AutoCloseable {
   private final NativePacketsApi blv;
   private final Object blw;
   private final Map blx = new IdentityHashMap();
   private final ConcurrentMap bly = new ConcurrentHashMap();
   private final ConcurrentMap blz = new ConcurrentHashMap();
   private final AtomicLong blA = new AtomicLong();
   private final ConcurrentMap blB = new ConcurrentHashMap();
   private final Set blC = ConcurrentHashMap.newKeySet();
   private volatile PassengerRegistry blD;
   private volatile boolean cz;

   private NativePacketsBridge(Object var1, NativePacketsApi var2) {
      this.blw = Objects.requireNonNull(var1, "pluginOwner");
      this.blv = Objects.requireNonNull(var2, "api");
   }

   static NativePacketsBridge ah(Object var0) {
      return method1866(var0, NativePackets::api);
   }

   static NativePacketsBridge method1866(Object var0, Supplier var1) {
      return new NativePacketsBridge(var0, (NativePacketsApi)Objects.requireNonNull(var1, "apiSupplier").get());
   }

   public void method1867(Player var1, Object var2) {
      this.method1875(var1, var2, false);
   }

   public void method1868(Player var1, Object var2) {
      this.method1875(var1, var2, true);
   }

   public ConnectionHandle cA(Player var1) {
      return this.cF(var1);
   }

   public void method1869(ConnectionHandle var1, Object var2) {
      Objects.requireNonNull(var1, "connection").sendSilently(var2);
   }

   @Nullable
   public ConnectionRecord cB(Player var1) {
      PlayerConnectionRecord var2 = (PlayerConnectionRecord)this.blz.get(Objects.requireNonNull(var1, "player").getUniqueId());
      return var2 != null && var2.dJ() == var1 ? var2.kQ() : null;
   }

   @Nullable
   public ConnectionRecord method1870(UUID var1) {
      PlayerConnectionRecord var2 = (PlayerConnectionRecord)this.blz.get(Objects.requireNonNull(var1, "playerId"));
      return var2 == null ? null : var2.kQ();
   }

   public boolean method1871(ConnectionRecord var1, Object var2) {
      if (!this.method1873(var1)) {
         return false;
      }

      var1.PC().sendSilently(var2);
      return true;
   }

   public boolean method1872(ConnectionRecord var1, List var2) {
      if (!var2.isEmpty() && this.method1873(var1)) {
         ConnectionHandle var3 = var1.PC();
         var3.eventLoop().execute(() -> {
            if (this.method1873(var1)) {
               boolean var4 = false;

               try {
                  int var5 = var2.size() - 1;

                  for (int var6 = 0; var6 < var5; var6++) {
                     var3.writeSilently(var2.get(var6));
                  }

                  var3.sendSilently(var2.get(var5));
                  var4 = true;
               } finally {
                  if (!var4 && var3.isOpen()) {
                     var3.flush();
                  }
               }
            }
         });
         return true;
      } else {
         return false;
      }
   }

   public boolean method1873(@Nullable ConnectionRecord var1) {
      if (!this.cz && var1 != null && var1.PC().isOpen()) {
         PlayerConnectionRecord var2 = (PlayerConnectionRecord)this.blz.get(var1.bk());
         return var2 != null && var2.kQ() == var1 && this.bly.get(var1.bl()) == var2;
      } else {
         return false;
      }
   }

   public boolean HF() {
      return this.cz;
   }

   public boolean method1874(ConnectionHandle var1) {
      return var1 != null && var1.isOpen();
   }

   private void method1875(Player var1, Object var2, boolean var3) {
      try {
         ConnectionHandle var4 = this.cF(var1);
         if (var3) {
            var4.sendSilently(var2);
         } else {
            var4.send(var2);
         }
      } catch (IllegalStateException var5) {
         if (this.cC(var1)) {
            throw var5;
         }
      }
   }

   public void method1876(Player var1, Object var2) {
      this.cF(var1).receive(var2);
   }

   public void method1877(Player var1, Object var2) {
      this.cF(var1).receiveSilently(var2);
   }

   public MinecraftVersion Pu() {
      return this.blv.platform().minecraftVersion();
   }

   public NativePacketFactory Pv() {
      return this.blv.packets();
   }

   public FastPacketSender Pw() {
      return this.blv.fast();
   }

   public NativeTextFactory Px() {
      return this.blv.textFactory();
   }

   public boolean cC(Player var1) {
      try {
         return this.cF(var1).isOpen();
      } catch (IllegalStateException var3) {
         return false;
      }
   }

   public SchedulerAccess IP() {
      return this.blv.utilities().scheduler();
   }

   public ItemAccess Py() {
      return this.blv.utilities().items();
   }

   public EntityAccess Pz() {
      return this.blv.utilities().entities();
   }

   public WorldAccess PA() {
      return this.blv.utilities().worlds();
   }

   public void cD(Player var1) {
      this.method1881(var1, RotationPolicy.COMPATIBLE);
   }

   void method1878(PassengerRegistry var1) {
      this.blD = var1;
      if (var1 != null) {
         this.method1888(this, PacketSelector.exact(PacketTypes.LOGIN), ListenerPriority.MONITOR, false, ListenerFailurePolicy.PASS_ORIGINAL, var2 -> {
            if (var2.connection().nativeChannel() instanceof Channel var4) {
               long var5 = var2.connection().id();
               PassengersChannelHandler var7 = (PassengersChannelHandler)this.blB.remove(var5);
               if (var7 != null) {
                  var7.close();
               }

               int var8 = ((LoginPacketView)var2.view(LoginPacketView.class)).playerId();
               PassengersChannelHandler var9 = new PassengersChannelHandler(var4, var8, var1, this.Pv());
               this.blB.put(var5, var9);
               var9.PG();
               this.blC.add(var5);
            }
         });
      }
   }

   public boolean method1879(ConnectionRecord var1, Object var2, int var3) {
      PassengersChannelHandler var4 = (PassengersChannelHandler)this.blB.get(var1.bl());
      return this.method1873(var1) && this.blC.contains(var1.bl()) && var4 != null && var4.method5254(var2, var3);
   }

   public void method1880(ConnectionRecord var1, Object var2) {
      PassengersChannelHandler var3 = (PassengersChannelHandler)this.blB.get(var1.bl());
      if (this.method1873(var1) && var3 != null) {
         var3.ak(var2);
      }
   }

   public void method1881(Player var1, RotationPolicy var2) {
      Player var3 = Objects.requireNonNull(var1, "player");
      UUID var4 = var3.getUniqueId();
      ConnectionHandle var5 = this.cF(var3);
      ConnectionRecord var6 = new ConnectionRecord(var4, var5.id(), this.blA.incrementAndGet(), var5);
      PlayerConnectionRecord var7 = new PlayerConnectionRecord(var6, var3, Objects.requireNonNull(var2, "geometryProfile"));
      PlayerConnectionRecord var8 = this.blz.put(var4, var7);
      if (var8 != null) {
         this.bly.remove(var8.kQ().bl(), var8);
         if (var8.kQ().bl() != var6.bl()) {
            this.method1882(var8.kQ().bl());
         }
      }

      PlayerConnectionRecord var9 = this.bly.put(var7.kQ().bl(), var7);
      if (var9 != null && var9 != var8) {
         this.blz.remove(var9.kQ().bk(), var9);
      }
   }

   public void cE(Player var1) {
      Player var2 = Objects.requireNonNull(var1, "player");
      PlayerConnectionRecord var3 = (PlayerConnectionRecord)this.blz.get(var2.getUniqueId());
      if (var3 != null && var3.dJ() == var2 && this.blz.remove(var3.kQ().bk(), var3)) {
         this.bly.remove(var3.kQ().bl(), var3);
         this.method1882(var3.kQ().bl());
      }
   }

   private void method1882(long var1) {
      this.blC.remove(var1);
      PassengersChannelHandler var3 = (PassengersChannelHandler)this.blB.remove(var1);
      if (var3 != null) {
         var3.close();
      }
   }

   @Nullable
   public UUID method1883(PacketEvent var1) {
      PlayerConnectionRecord var2 = (PlayerConnectionRecord)this.bly.get(Objects.requireNonNull(var1, "event").connection().id());
      return var2 != null && this.blz.get(var2.kQ().bk()) == var2 ? var2.kQ().bk() : null;
   }

   @Nullable
   public Player method1884(UUID var1) {
      PlayerConnectionRecord var2 = (PlayerConnectionRecord)this.blz.get(Objects.requireNonNull(var1, "playerId"));
      return var2 == null ? null : var2.dJ();
   }

   @Nullable
   public Player method1885(UUID var1, long var2) {
      PlayerConnectionRecord var4 = (PlayerConnectionRecord)this.blz.get(Objects.requireNonNull(var1, "playerId"));
      return var4 != null && var4.kQ().bl() == var2 ? var4.dJ() : null;
   }

   public RotationPolicy method1886(UUID var1) {
      PlayerConnectionRecord var2 = (PlayerConnectionRecord)this.blz.get(Objects.requireNonNull(var1, "playerId"));
      return var2 == null ? RotationPolicy.COMPATIBLE : var2.kV();
   }

   public synchronized PacketListenerHandle method1887(Object var1, PacketType var2, ListenerPriority var3, boolean var4, TypedPacketListener var5) {
      this.PB();
      Object var6 = Objects.requireNonNull(var1, "owner");
      return this.method1890(var6, this.blv.listeners().register(var6, var2, var3, var4, var5));
   }

   public synchronized PacketListenerHandle method1888(
      Object var1, PacketSelector var2, ListenerPriority var3, boolean var4, ListenerFailurePolicy var5, PacketListener var6
   ) {
      this.PB();
      Object var7 = Objects.requireNonNull(var1, "owner");
      return this.method1890(var7, this.blv.listeners().register(var7, var2, var3, var4, var5, var6));
   }

   public synchronized void ai(Object var1) {
      Object var2 = Objects.requireNonNull(var1, "owner");
      method1891((List)this.blx.remove(var2));
      this.blv.listeners().unregisterOwner(var2);
   }

   public synchronized void method1889(Object var1, PacketListenerHandle var2) {
      Object var3 = Objects.requireNonNull(var1, "owner");
      PacketListenerHandle var4 = Objects.requireNonNull(var2, "handle");
      List var5 = (List)this.blx.get(var3);
      if (var5 != null && var5.remove(var4) && var5.isEmpty()) {
         this.blx.remove(var3);
      }

      var4.close();
   }

   @Override
   public synchronized void close() {
      if (!this.cz) {
         this.cz = true;
         this.blx.values().forEach(NativePacketsBridge::method1891);
         this.blx.clear();
         this.blB.values().forEach(PassengersChannelHandler::close);
         this.blB.clear();
         this.blC.clear();
         this.bly.clear();
         this.blz.clear();
         this.blv.listeners().unregisterOwner(this.blw);
      }
   }

   private ConnectionHandle cF(Player var1) {
      return this.blv.connection(Objects.requireNonNull(var1, "player"));
   }

   private PacketListenerHandle method1890(Object var1, PacketListenerHandle var2) {
      PacketListenerHandle var3 = Objects.requireNonNull(var2, "handle");
      this.blx.computeIfAbsent(var1, var0 -> new ArrayList<>()).add(var3);
      return var3;
   }

   private void PB() {
      if (this.cz) {
         throw new IllegalStateException("NativePackets bridge is closed");
      }
   }

   private static void method1891(List var0) {
      if (var0 != null) {
         var0.forEach(PacketListenerHandle::close);
      }
   }
}
