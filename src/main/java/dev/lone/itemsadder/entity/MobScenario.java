package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.anim.BoneSpec;
import dev.lone.itemsadder.item.ProjectileHitRecord;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

final class MobScenario {
   final UUID Si;
   final UUID Sj;
   final UUID Sk;
   final String Sl;
   final boolean Sm;
   final boolean Sn;
   final RouteSegment So;
   final long Sp;
   final Set Sq;
   final ArrayDeque Sr;
   final ArrayDeque Ss;
   final ArrayDeque St;
   final EnumMap Su;
   final EnumMap Sv;
   final EnumMap Sw;
   final EntityDescriptor Sx;
   ScenarioState Sy;
   String Rn;
   @Nullable
   String Sz;
   int Rl;
   int SA;
   long SB;
   boolean SC;
   @Nullable
   Mob SD;
   @Nullable
   CustomEntityImpl SE;
   @Nullable
   Mob SF;
   @Nullable
   Villager SG;
   @Nullable
   Villager SH;
   @Nullable
   TextDisplay SI;
   @Nullable
   TextDisplay SJ;
   int SK;
   int SL;
   int SM;
   int SN;
   int SO;
   int SP;
   int SQ;
   private final EnumMap SR;
   private final EnumMap field130;
   private final EnumMap ST;

   MobScenario(EntityEventTracker var1, UUID var2, Player var3, String var4, boolean var5, RouteSegment var6, boolean var7) {
      this.SU = var1;
      this.Sp = System.currentTimeMillis();
      this.Sq = new LinkedHashSet();
      this.Sr = new ArrayDeque();
      this.Ss = new ArrayDeque();
      this.St = new ArrayDeque();
      this.Su = new EnumMap<>(MobVariant.class);
      this.Sv = new EnumMap<>(MobVariant.class);
      this.Sw = new EnumMap<>(MobVariant.class);
      this.Sy = ScenarioState.RUNNING;
      this.Rn = "idle";
      this.SR = new EnumMap<>(MobVariant.class);
      this.field130 = new EnumMap<>(MobVariant.class);
      this.ST = new EnumMap<>(MobVariant.class);
      this.Si = var2;
      this.Sj = var3.getUniqueId();
      this.Sk = var3.getWorld().getUID();
      this.Sl = var4;
      this.Sm = var5;
      this.So = var6;
      this.Sn = var7;
      this.Sx = new EntityDescriptor(
         "ia-entity-compare/v1",
         var2,
         Main.getInstance().getDescription().getVersion(),
         Bukkit.getVersion(),
         var4,
         var7 ? "minecraft:pig" : "minecraft:zombie",
         var7 ? "navigation" : "combat",
         var7 ? EntityEventTracker.Rg : EntityEventTracker.Re,
         "Bukkit world blocks; V2 lane offset removed before comparison",
         8.0,
         var7 ? EntityEventTracker.Rh : EntityEventTracker.Rf,
         var5,
         1600
      );
   }

   boolean isActive() {
      return this.Sy == ScenarioState.RUNNING;
   }

   void vc() {
      World var1 = Objects.requireNonNull(Bukkit.getWorld(this.Sk));
      this.SD = this.Sn ? (Mob)var1.spawn(this.So.Sg, Pig.class, this::method9140) : (Mob)var1.spawn(this.So.Sg, Zombie.class, this::method9139);
      this.SU.applyScenario(this, this.SD);
      this.SE = CustomEntityManager.getInstance().spawnCustomEntitySimple(this.Sl, this.So.Sh, true);
      if (this.SE != null && this.SE.getBukkitEntity() instanceof Mob var2 && this.Sn == (var2 instanceof Pig)) {
         this.SF = var2;
         this.SU.applyScenario(this, this.SF);
         if (this.Sn) {
            this.method9140((Pig)this.SF);
         } else {
            this.method9141((Zombie)this.SD, (Zombie)this.SF);
         }

         this.SD.setCustomNameVisible(true);
         this.SF.setCustomNameVisible(false);
         this.SI = this.spawnLabel(var1, this.So.Sg, "Vanilla " + (this.Sn ? "pig" : "zombie"));
         this.SJ = this.spawnLabel(var1, this.So.Sh, "V2 " + this.Sl);
         this.SU.applyScenario(this, this.SI);
         this.SU.applyScenario(this, this.SJ);
         this.method9153("phase", null, null, 0.0, false);
      } else {
         throw new IllegalStateException("V2 model did not spawn the expected " + (this.Sn ? "pig" : "zombie") + " base");
      }
   }

   private void method9139(Zombie var1) {
      var1.setAdult();
      var1.setPersistent(true);
      var1.setRemoveWhenFarAway(false);
      var1.setSilent(true);
      var1.setAI(true);
      var1.setAware(true);
      var1.setCanPickupItems(false);
      EntityEventTracker.method1859(var1.getEquipment());
   }

   private void method9140(Pig var1) {
      var1.setAdult();
      var1.setSaddle(false);
      var1.setPersistent(true);
      var1.setRemoveWhenFarAway(false);
      var1.setSilent(true);
      var1.setAI(true);
      var1.setAware(false);
   }

   private void method9141(Zombie var1, Zombie var2) {
      this.method9139(var2);

      for (Attribute var6 : Attribute.values()) {
         AttributeInstance var7 = var1.getAttribute(var6);
         AttributeInstance var8 = var2.getAttribute(var6);
         if (var7 != null && var8 != null) {
            var8.setBaseValue(var7.getBaseValue());
         }
      }

      var2.setHealth(Math.min(var1.getHealth(), var2.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
   }

   private TextDisplay spawnLabel(World var1, Location var2, String var3) {
      return (TextDisplay)var1.spawn(var2.clone().add(0.0, 2.6, 0.0), TextDisplay.class, var2x -> {
         var2x.setText(var3 + "\n" + this.Si);
         var2x.setBillboard(Billboard.CENTER);
         var2x.setPersistent(false);
         var2x.setSeeThrough(true);
         var2x.setShadowed(true);
      });
   }

   private void vd() {
      World var1 = Objects.requireNonNull(Bukkit.getWorld(this.Sk));
      this.SG = this.spawnVillager(var1, this.So.Sg.clone().add(this.So.Se.clone().multiply(10)), "vanilla");
      this.SH = this.spawnVillager(var1, this.So.Sh.clone().add(this.So.Se.clone().multiply(10)), "v2");
      this.SU.applyScenario(this, this.SG);
      this.SU.applyScenario(this, this.SH);
      this.bt("acquire");
   }

   private Villager spawnVillager(World var1, Location var2, String var3) {
      return (Villager)var1.spawn(var2, Villager.class, var2x -> {
         var2x.setAdult();
         var2x.setAI(false);
         var2x.setPersistent(true);
         var2x.setRemoveWhenFarAway(false);
         var2x.setSilent(true);
         var2x.setCanPickupItems(false);
         var2x.setCustomName("IACompare|" + this.Si + "|target|" + var3);
         var2x.setCustomNameVisible(true);
         AttributeInstance var3x = var2x.getAttribute(Attribute.GENERIC_MAX_HEALTH);
         if (var3x != null) {
            var3x.setBaseValue(200.0);
            var2x.setHealth(200.0);
         }

         EntityEventTracker.method1859(var2x.getEquipment());
      });
   }

   void tick() {
      this.Rl++;
      this.SA++;
      Player var1 = Bukkit.getPlayer(this.Sj);
      if (var1 == null || !var1.isOnline() || !var1.getWorld().getUID().equals(this.Sk)) {
         this.method9155("owner unavailable");
      } else if (!EntityEventTracker.isTracked(this.SD) || !EntityEventTracker.isTracked(this.SF)) {
         this.bu((this.Sn ? "pig" : "zombie") + " actor became invalid");
      } else if (this.Rl >= 700) {
         this.bu((this.Sn ? "navigation" : "combat") + " timed out before required evidence was observed");
      } else {
         this.SD.setFireTicks(0);
         this.SF.setFireTicks(0);
         this.vi();
         if (this.Rl % 5 == 0) {
            this.vj();
         }

         if (this.Sn) {
            this.ve();
         } else {
            if (this.Rl == 40) {
               this.vd();
            }

            if (this.SG != null && this.SH != null) {
               this.vg();
            }
         }

         this.vh();
      }
   }

   private void ve() {
      if (this.Rn.equals("idle") && this.SA >= 40) {
         this.bt("slow");
      }

      if (this.Rn.equals("slow")) {
         this.method9144(0.025);
         if (this.SA >= 60) {
            this.bt("normal");
         }
      } else if (this.Rn.equals("normal")) {
         this.method9144(0.06);
         if (this.SA >= 60) {
            this.bt("faster");
         }
      } else if (this.Rn.equals("faster")) {
         this.method9144(0.1);
         if (this.SA >= 60) {
            this.bt("stop");
         }
      } else if (this.Rn.equals("stop")) {
         this.method9144(0.0);
         if (this.SA >= 40) {
            this.bt("turns");
         }
      } else if (this.Rn.equals("turns")) {
         this.method9144(0.0);
         float var1 = this.So.Sd.getYaw();
         float var2 = var1 + (this.SA < 30 ? 35 : -35);
         float var3 = this.SA < 30 ? 20.0F : -15.0F;
         this.SD.setRotation(var2, var3);
         this.SF.setRotation(var2, var3);
         ProjectileTypeHelper.getAdapter().method3237(this.SD, var1);
         ProjectileTypeHelper.getAdapter().method3237(this.SF, var1);
         if (this.SA >= 60) {
            this.SD.setAware(true);
            this.SF.setAware(true);
            this.bt("normal_ai_prepare");
         }
      } else if (this.Rn.equals("normal_ai_prepare")) {
         if (this.SA >= 2 && this.SD.isOnGround() && this.SF.isOnGround()) {
            this.vf();
            this.bt("normal_ai");
         } else if (this.SA >= 40) {
            this.bu("pigs did not reach valid grounded navigation state");
         }
      } else if (!this.Rn.equals("normal_ai") || this.SA < 120 && (this.SA < 40 || this.SD.getPathfinder().hasPath() || this.SF.getPathfinder().hasPath())) {
         if (this.Rn.equals("settle")) {
            this.method9144(0.0);
            if (this.SA >= 40) {
               this.vk();
            }
         }
      } else {
         this.SD.getPathfinder().stopPathfinding();
         this.SF.getPathfinder().stopPathfinding();
         this.SD.setAI(false);
         this.SF.setAI(false);
         this.bt("settle");
      }
   }

   private void method9144(double var1) {
      Vector var3 = this.So.Se.clone().multiply(var1);
      this.SD.setVelocity(var3.clone().setY(this.SD.getVelocity().getY()));
      this.SF.setVelocity(var3.clone().setY(this.SF.getVelocity().getY()));
   }

   private void vf() {
      boolean var1 = this.SD.getPathfinder().moveTo(this.SD.getLocation().add(this.So.Se.clone().multiply(8)), 0.8);
      boolean var2 = this.SF.getPathfinder().moveTo(this.SF.getLocation().add(this.So.Se.clone().multiply(8)), 0.8);
      this.method9153("navigation_path_vanilla_" + var1 + "_v2_" + var2, null, null, 0.0, false);
   }

   private void vg() {
      if (this.Rn.equals("acquire") && this.SD.getTarget() == this.SG && this.SF.getTarget() == this.SH) {
         this.bt("chase");
      }

      if (this.Rn.equals("chase") && this.SK > 0 && this.SL > 0) {
         this.bt("stationary_attacks");
      }

      if (this.Rn.equals("stationary_attacks") && this.SO >= 2 && this.SP >= 2) {
         this.SG.setAI(true);
         this.SH.setAI(true);
         this.bt("controlled_flee");
      }

      if (this.Rn.equals("controlled_flee")) {
         Vector var1 = this.SA < 55
            ? this.So.Se.clone().multiply(0.045)
            : (
               this.SA < 110
                  ? this.So.Se.clone().multiply(0.035).add(this.So.Sf.clone().multiply(0.025))
                  : this.So.Se.clone().multiply(0.035).add(this.So.Sf.clone().multiply(-0.025))
            );
         this.SG.setVelocity(var1);
         this.SH.setVelocity(var1);
         if (this.SA >= 170 && this.SM > 0 && this.SN > 0) {
            this.bt("natural_flee");
         }
      }

      if (this.Rn.equals("natural_flee") && this.SA >= 80 && this.SK >= 3 && this.SL >= 3) {
         this.SG.remove();
         this.SH.remove();
         this.bt("lose_target");
      }

      if (this.Rn.equals("lose_target")) {
         BoneSpec var3 = this.SE.getModelInstance().getBoneSpec();
         boolean var2 = var3 == null || var3.rh() < 0.02;
         if (this.SD.getTarget() == null
            && this.SF.getTarget() == null
            && var2
            && this.method9149(MobVariant.VANILLA_ZOMBIE, this.SD.getLocation()) < 0.01
            && this.method9149(MobVariant.V2_ZOMBIE, this.SF.getLocation()) < 0.01) {
            this.SQ++;
         } else {
            this.SQ = 0;
         }

         if (this.SQ >= 20) {
            this.vk();
         }
      }
   }

   private void vh() {
      long var1 = System.currentTimeMillis();
      this.method9145(this.Sn ? MobVariant.VANILLA_PIG : MobVariant.VANILLA_ZOMBIE, this.SD, null, var1);
      this.method9145(this.Sn ? MobVariant.V2_PIG : MobVariant.V2_ZOMBIE, this.SF, this.SE, var1);
      if (EntityEventTracker.isTracked(this.SG)) {
         this.method9148(MobVariant.VANILLA_TARGET, this.SG, var1);
      }

      if (EntityEventTracker.isTracked(this.SH)) {
         this.method9148(MobVariant.V2_TARGET, this.SH, var1);
      }
   }

   private void method9145(MobVariant var1, Mob var2, @Nullable CustomEntityImpl var3, long var4) {
      double var6 = this.method9149(var1, var2.getLocation());
      Mob var8 = var2;
      UUID var9 = var8.getTarget() == null ? null : var8.getTarget().getUniqueId();
      UUID var10 = this.Sw.put(var1, var9);
      if (!Objects.equals(var10, var9)) {
         this.method9153("target_change", var2, var8.getTarget(), 0.0, false);
      }

      float var11 = this.Sn ? 0.0F : (var3 == null ? ProjectileTypeHelper.getAdapter().ai(var2) : var3.jm());
      if (var11 > 0.0F && this.method9146(var1) <= 0.0F) {
         this.method9153("attack_progress_start", var2, var8.getTarget(), 0.0, false);
      }

      this.method9147(var1, var11);
      TrackedEntityRecord var12 = new TrackedEntityRecord(
         this.Rl,
         var4,
         this.Rn,
         var1.key,
         var2.getUniqueId(),
         var9,
         var2.getX(),
         var2.getY(),
         var2.getZ(),
         var6,
         var2.getVelocity().getX(),
         var2.getVelocity().getY(),
         var2.getVelocity().getZ(),
         ProjectileTypeHelper.getAdapter().ab(var2),
         ProjectileTypeHelper.getAdapter().method3236(var2),
         ProjectileTypeHelper.getAdapter().method3257(var2),
         var11,
         var3 != null && var3.getCurrentAnimation() != null ? var3.getCurrentAnimation().yK : "",
         var3 != null && var3.resolveCurrentAnimation() != null ? var3.resolveCurrentAnimation().yK : "",
         var3 != null && var3.isCurrentAnimationActive(),
         var3 != null && var3.getWalkAnimation() != null ? var3.getWalkAnimation().yK : "",
         var3 == null ? null : var3.getModelInstance().getBoneSpec(),
         var3 == null ? Map.of() : var3.getModelPartIds(Bukkit.getPlayer(this.Sj))
      );
      this.field130.put(var1, var12);
      ArrayDeque var13 = this.Su.computeIfAbsent(var1, var0 -> new ArrayDeque());
      var13.addLast(var12);

      while (((TrackedEntityRecord)var13.getFirst()).index() < this.Rl - 8) {
         var13.removeFirst();
      }

      if (this.Sm) {
         this.method9150(this.Ss, var12, 1600);
      }
   }

   private float method9146(MobVariant var1) {
      return this.SR.getOrDefault(var1, 0.0F);
   }

   private void method9147(MobVariant var1, float var2) {
      this.SR.put(var1, var2);
   }

   private void method9148(MobVariant var1, Villager var2, long var3) {
      double var5 = this.method9149(var1, var2.getLocation());
      TrackedEntitySummary var7 = new TrackedEntitySummary(
         this.Rl, var3, this.Rn, var1.key, var2.getUniqueId(), var2.getX(), var2.getY(), var2.getZ(), var5, var2.getHealth(), var2.hasAI()
      );
      this.ST.put(var1, var7);
      if (this.Sm) {
         this.method9150(this.St, var7, 1600);
      }
   }

   private double method9149(MobVariant var1, Location var2) {
      Location var3 = this.Sv.put(var1, var2.clone());
      return var3 != null && var3.getWorld() == var2.getWorld() ? var3.distance(var2) : 0.0;
   }

   private void method9150(ArrayDeque var1, Object var2, int var3) {
      if (var1.size() >= var3) {
         this.SC = true;
      } else {
         var1.addLast(var2);
      }
   }

   boolean method9151(Entity var1, Entity var2) {
      return this.SD != null && this.SG != null && var1.getUniqueId().equals(this.SD.getUniqueId()) && var2.getUniqueId().equals(this.SG.getUniqueId())
         || this.SF != null && this.SH != null && var1.getUniqueId().equals(this.SF.getUniqueId()) && var2.getUniqueId().equals(this.SH.getUniqueId());
   }

   void method9152(Entity var1, Entity var2, double var3, boolean var5) {
      this.method9153("damage", var1, var2, var3, var5);
      if (!var5) {
         boolean var6 = var1.getUniqueId().equals(this.SD.getUniqueId());
         ArrayDeque var7 = (ArrayDeque)this.Su.get(var6 ? MobVariant.VANILLA_ZOMBIE : MobVariant.V2_ZOMBIE);
         boolean var8 = var7 != null && var7.stream().anyMatch(var1x -> var1x.index() >= this.Rl - 8 && var1x.rc() > 0.01);
         if (var6) {
            this.SK++;
            if (var8) {
               this.SM++;
            } else {
               this.SO++;
            }
         } else {
            this.SL++;
            if (var8) {
               this.SN++;
            } else {
               this.SP++;
            }
         }
      }
   }

   void method9153(String var1, @Nullable Entity var2, @Nullable Entity var3, double var4, boolean var6) {
      if (this.Sr.size() >= 256) {
         this.SC = true;
      } else {
         this.Sr
            .addLast(
               new ProjectileHitRecord(
                  ++this.SB,
                  this.Rl,
                  System.currentTimeMillis(),
                  this.Rn,
                  var1,
                  var2 == null ? null : var2.getUniqueId(),
                  var3 == null ? null : var3.getUniqueId(),
                  var4,
                  var6
               )
            );
      }
   }

   private void bt(String var1) {
      this.Rn = var1;
      this.SA = 0;
      this.method9153("phase", null, null, 0.0, false);
      this.vi();
   }

   private void vi() {
      if (EntityEventTracker.isTracked(this.SD)) {
         this.SD.setCustomName("IACompare|" + this.Si + "|vanilla|" + this.Rn);
      }

      if (EntityEventTracker.isTracked(this.SF)) {
         this.SF.setCustomName("IACompare|" + this.Si + "|v2|" + this.Rn);
      }
   }

   private void vj() {
      if (EntityEventTracker.isTracked(this.SI)) {
         this.SI.teleport(this.SD.getLocation().add(0.0, 2.6, 0.0));
      }

      if (EntityEventTracker.isTracked(this.SJ)) {
         this.SJ.teleport(this.SF.getLocation().add(0.0, 2.6, 0.0));
      }
   }

   private boolean isChunkLoaded(Chunk var1) {
      for (UUID var3 : this.Sq) {
         Entity var4 = var1.getWorld().getEntity(var3);
         if (var4 != null && var4.getLocation().getChunk().equals(var1)) {
            return true;
         }
      }

      return false;
   }

   private void vk() {
      this.Sy = ScenarioState.COMPLETE;
      this.bt("complete");
      this.es();
   }

   void bu(String var1) {
      if (this.isActive()) {
         this.Sz = var1;
         this.Sy = ScenarioState.FAILED;
         this.method9153("failed", null, null, 0.0, false);
         this.es();
      }
   }

   void method9155(String var1) {
      if (this.isActive()) {
         this.Sz = var1;
         this.Sy = ScenarioState.STOPPED;
         this.method9153("stopped", null, null, 0.0, false);
         this.es();
      }
   }

   private void es() {
      if (this.SE != null && CustomEntityManager.isInitialized()) {
         CustomEntityManager.getInstance().removeWithEffects(this.SE, false, false);
      }

      for (UUID var2 : List.copyOf(this.Sq)) {
         this.SU.Rk.remove(var2);
         Entity var3 = Bukkit.getEntity(var2);
         if (var3 != null && var3.isValid()) {
            var3.remove();
         }
      }

      this.Sq.clear();
   }

   MobScenarioSnapshot snapshot() {
      ArrayList var1 = new ArrayList(this.Ss);
      ArrayList var2 = new ArrayList(this.St);
      if (!this.Sm) {
         var1.addAll(this.field130.values());
         var2.addAll(this.ST.values());
      }

      return new MobScenarioSnapshot(
         this.Sx,
         this.Sy.name(),
         this.Rn,
         this.Rl,
         this.SK,
         this.SL,
         this.SM,
         this.SN,
         this.SO,
         this.SP,
         this.SC,
         List.copyOf(this.Sr),
         List.copyOf(var1),
         List.copyOf(var2),
         this.Sz
      );
   }

   List vm() {
      String var1 = this.Sy == ScenarioState.COMPLETE ? "PASS" : (this.Sy == ScenarioState.FAILED ? "FAIL" : "NOT_EVALUATED");
      String var2 = this.Sn
         ? "Navigation/automatic idle-walk-idle: " + var1
         : "Combat semantic/timing: "
            + var1
            + " — damage vanilla/V2="
            + this.SK
            + "/"
            + this.SL
            + ", stationary="
            + this.SO
            + "/"
            + this.SP
            + ", moving="
            + this.SM
            + "/"
            + this.SN;
      return List.of(
         "Session " + this.Si + " — " + this.Sy + " / " + this.Rn + " / tick " + this.Rl,
         "Actors: vanilla="
            + EntityEventTracker.method1858(this.SD)
            + ", v2="
            + EntityEventTracker.method1858(this.SF)
            + ", targets="
            + EntityEventTracker.method1858(this.SG)
            + "/"
            + EntityEventTracker.method1858(this.SH),
         var2,
         "Rendered geometry/interpolation: NOT_EVALUATED — attach correlated probe capture for session " + this.Si,
         "Capture: "
            + (this.Sm ? this.Ss.size() + " actor samples" : "summary only")
            + (this.SC ? " (TRUNCATED; cannot pass)" : "")
            + (this.Sz == null ? "" : "; " + this.Sz)
      );
   }
}
