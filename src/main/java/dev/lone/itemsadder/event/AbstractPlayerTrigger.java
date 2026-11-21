package dev.lone.itemsadder.event;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.nms.NmsRegistry;
import dev.lone.itemsadder.util.EventRegistrar;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPlayerTrigger implements Listener {
   private static AbstractPlayerTrigger bkx;
   static final double bky = 4.0;
   protected static final double bkz = 50.0;
   protected final Object2ObjectOpenHashMap bkA = new Object2ObjectOpenHashMap();
   private final Set bkB = ConcurrentHashMap.newKeySet();
   private volatile boolean bkC;
   private volatile long cW;

   public static void register() {
      if (bkx != null) {
         throw new IllegalStateException("OrbitCamera is already registered!");
      }

      bkx = (AbstractPlayerTrigger)NmsRegistry.method2777(AbstractPlayerTrigger.class);
      bkx.bX();
   }

   public static AbstractPlayerTrigger Pb() {
      if (bkx == null) {
         throw new IllegalStateException("OrbitCamera is not registered yet!");
      } else {
         return bkx;
      }
   }

   public static void aM() {
      if (bkx != null) {
         EventRegistrar.unregisterListener(bkx);
         bkx.Pc();
         bkx = null;
      }
   }

   protected abstract void Pc();

   protected abstract void bX();

   public abstract boolean method5132(Player var1, Location var2);

   public void bc(Player var1) {
      this.bkB.remove(var1.getUniqueId());
      PlayerTriggerContext var2 = (PlayerTriggerContext)this.bkA.remove(var1);
      if (var2 != null) {
         this.method5137(var2);
      }
   }

   public void cy(Player var1) {
      PlayerTriggerContext var2 = (PlayerTriggerContext)this.bkA.get(var1);
      if (var2 != null) {
         var2.Pk().setWorld(null);
      }

      this.bc(var1);
   }

   public void method5133(Player var1, Location var2) {
      PlayerTriggerContext var3 = (PlayerTriggerContext)this.bkA.get(var1);
      if (var3 != null && var2.getWorld() == var3.bkE.getWorld()) {
         var3.bkE.setX(var2.getX());
         var3.bkE.setY(var2.getY());
         var3.bkE.setZ(var2.getZ());
         Location var4 = var1.getLocation();
         float var5 = var3.method5206(var4.getYaw());
         float var6 = var3.method5207(var4.getPitch());
         Location var7 = method5144(var3.bkE, var5, var6);
         var7.setDirection(var3.bkE.toVector().subtract(var7.toVector()));
         NativePacketsHolder.PE()
            .method1868(
               var1,
               NativePacketsHolder.PE().Pv().teleport(var3.bkF.getEntityId(), var7.getX(), var7.getY(), var7.getZ(), var7.getYaw(), var7.getPitch(), false)
            );
      }
   }

   protected final void method5134(Player var1, PlayerTriggerContext var2) {
      this.bkA.put(var1, var2);
      this.bkB.add(var1.getUniqueId());
   }

   protected final void Pd() {
      PlayerTriggerContext[] var1 = (PlayerTriggerContext[])this.bkA.values().toArray(new PlayerTriggerContext[0]);
      this.bkB.clear();
      this.bkA.clear();

      for (PlayerTriggerContext var5 : var1) {
         this.method5137(var5);
      }
   }

   protected final synchronized long Pe() {
      this.bkC = true;
      return ++this.cW;
   }

   protected final synchronized void Pf() {
      this.bkC = false;
      this.cW++;
   }

   protected final boolean method5135(@Nullable UUID var1, long var2) {
      return this.bkC && this.cW == var2 && var1 != null && this.bkB.contains(var1);
   }

   protected final Main Pg() {
      return Main.getInstance();
   }

   @Nullable
   protected final PlayerTriggerContext method5136(PlayerTriggerKey var1, Object var2) {
      return this.method5135(var1.bk(), var1.ai())
            && var2 instanceof Player var3
            && var3 == NativePacketsHolder.PE().method1885(var1.bk(), var1.bl())
            && var3.isOnline()
         ? (PlayerTriggerContext)this.bkA.get(var3)
         : null;
   }

   protected abstract void method5137(PlayerTriggerContext var1);

   @EventHandler
   public void method5138(EntityDamageEvent var1) {
      if (var1.getEntity() instanceof Player var2) {
         this.bc(var2);
      }
   }

   @EventHandler
   public void method5139(PlayerChangedWorldEvent var1) {
      this.bc(var1.getPlayer());
   }

   @EventHandler
   public void method5140(PlayerTeleportEvent var1) {
      this.bc(var1.getPlayer());
   }

   @EventHandler
   public void method5141(PlayerDeathEvent var1) {
      this.bc(var1.getEntity());
   }

   @EventHandler
   public void method5142(PlayerQuitEvent var1) {
      this.bc(var1.getPlayer());
   }

   @EventHandler
   public void method5143(PlayerToggleSneakEvent var1) {
      if (!var1.getPlayer().isDead()) {
         this.bc(var1.getPlayer());
      }
   }

   protected static boolean cz(Player var0) {
      Location var1 = var0.getEyeLocation();

      for (double var2 = -1.0; var2 <= 1.0; var2++) {
         for (double var4 = -1.0; var4 <= 1.0; var4++) {
            for (double var6 = -1.0; var6 <= 1.0; var6++) {
               Location var8 = var1.clone().add(var2, var4, var6);
               Block var9 = var8.getBlock();
               if (var9.getType().isSolid()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   protected static Location method5144(Location var0, float var1, float var2) {
      double var3 = Math.toRadians(var1) - (Math.PI / 2);
      double var5 = Math.toRadians(90.0 + var2);
      var5 = Math.max(0.001, Math.min(3.1405926535897932, var5));
      double var7 = Math.sin(var5);
      double var9 = Math.cos(var5);
      double var11 = Math.cos(var3);
      double var13 = Math.sin(var3);
      double var15 = var0.getX() + 4.0 * var7 * var11;
      double var17 = var0.getY() + 4.0 * var9;
      double var19 = var0.getZ() + 4.0 * var7 * var13;
      Location var21 = new Location(var0.getWorld(), var15, var17, var19);
      return method5145(var0, var21);
   }

   private static Location method5145(Location var0, Location var1) {
      Vector var2 = var1.toVector().subtract(var0.toVector());
      double var3 = var2.length();
      if (var3 < 1.0E-6) {
         return var1;
      }

      var2.normalize();
      double var5 = 0.2;
      Location var7 = var0.clone();

      for (double var8 = var5; var8 <= var3; var8 += var5) {
         Location var10 = var0.clone().add(var2.clone().multiply(var8));
         if (!ab(var10)) {
            break;
         }

         var7 = var10.clone();
      }

      return var7;
   }

   private static boolean ab(Location var0) {
      var0 = var0.clone();
      var0.add(0.0, 1.6, 0.0);
      double var1 = 0.05;
      double var3 = 0.3 + var1;
      BoundingBox var5 = new BoundingBox(var0.getX() - var3, var0.getY() - var3, var0.getZ() - var3, var0.getX() + var3, var0.getY() + var3, var0.getZ() + var3);
      int var6 = (int)Math.floor(var5.getMinX());
      int var7 = (int)Math.floor(var5.getMaxX());
      int var8 = (int)Math.floor(var5.getMinY());
      int var9 = (int)Math.floor(var5.getMaxY());
      int var10 = (int)Math.floor(var5.getMinZ());
      int var11 = (int)Math.floor(var5.getMaxZ());

      for (int var12 = var6; var12 <= var7; var12++) {
         for (int var13 = var8; var13 <= var9; var13++) {
            for (int var14 = var10; var14 <= var11; var14++) {
               Block var15 = var0.getWorld().getBlockAt(var12, var13, var14);
               if (var15.getType().isSolid()) {
                  BoundingBox var16 = var15.getBoundingBox();
                  if (var16.overlaps(var5)) {
                     return false;
                  }
               }
            }
         }
      }

      return true;
   }

   protected static float method5146(float var0) {
      while (var0 > 180.0F) {
         var0 -= 360.0F;
      }

      while (var0 < -180.0F) {
         var0 += 360.0F;
      }

      return var0;
   }

   public abstract void method5147(Object var1, Player var2);

   public abstract void method5148(Player var1, int... var2);
}
