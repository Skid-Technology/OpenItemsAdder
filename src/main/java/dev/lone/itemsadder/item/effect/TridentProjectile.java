package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.TridentPickupMode;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Generated;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.jetbrains.annotations.Nullable;

class TridentProjectile {
   public final ItemDisplay amD;
   private final Trident amE;
   private final int amF;
   private final UUID amG;
   @Nullable
   public final LivingEntity amH;
   private volatile UUID jD;
   @Nullable
   private volatile UUID amI;
   private volatile TridentPickupMode amJ = TridentPickupMode.NONE;
   private int amK = 0;
   private final AtomicBoolean amL = new AtomicBoolean();
   private final AtomicBoolean amM = new AtomicBoolean();
   private final ConcurrentMap amN = new ConcurrentHashMap();

   public TridentProjectile(Trident var1, ItemDisplay var2, @Nullable LivingEntity var3) {
      this.amE = var1;
      this.amD = var2;
      this.amH = var3;
      this.amF = var1.getEntityId();
      this.amG = var1.getUniqueId();
      this.jD = var1.getWorld().getUID();
   }

   public synchronized boolean by(Player var1) {
      if (this.amJ == TridentPickupMode.PICKING_UP) {
         return false;
      }

      this.amJ = TridentPickupMode.PICKING_UP;
      this.amI = var1.getUniqueId();
      return true;
   }

   public synchronized void CQ() {
      if (this.amJ == TridentPickupMode.NONE) {
         this.amJ = TridentPickupMode.LOYAL_PICKING_UP;
      }
   }

   public int getTicks() {
      return this.amK++;
   }

   boolean CS() {
      return this.amL.compareAndSet(false, true);
   }

   void CT() {
      this.amL.set(false);
   }

   boolean CU() {
      return this.amM.compareAndSet(false, true);
   }

   @Nullable
   synchronized AtomicBoolean method9943(TridentRef var1) {
      AtomicBoolean var2 = new AtomicBoolean();
      return this.amN.putIfAbsent(var1, var2) == null ? var2 : null;
   }

   synchronized void method9944(TridentRef var1) {
      this.amN.remove(var1);
   }

   boolean method9945(TridentRef var1, AtomicBoolean var2) {
      return this.amN.get(var1) == var2 && !var2.get();
   }

   void method9946(TridentRef var1, AtomicBoolean var2, boolean var3) {
      if (var3) {
         var2.set(true);
      } else {
         this.amN.remove(var1, var2);
      }
   }

   boolean CV() {
      return this.amN.values().stream().anyMatch(var0 -> !var0.get());
   }

   @Nullable
   @Generated
   public UUID getOwnerId() {
      return this.amI;
   }

   @Generated
   public TridentPickupMode getPickupMode() {
      return this.amJ;
   }
}
