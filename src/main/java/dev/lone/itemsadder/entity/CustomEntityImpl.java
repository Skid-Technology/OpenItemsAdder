package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.BossBarColor;
import beer.devs.nativepackets.api.BossBarOverlay;
import beer.devs.nativepackets.api.EntityMetadataField;
import beer.devs.nativepackets.api.NativePacketFactory;
import beer.devs.nativepackets.api.NativeText;
import beer.devs.nativepackets.api.NativeValue;
import beer.devs.nativepackets.api.SchedulerAccess;
import beer.devs.nativepackets.api.EntityMetadataFields.Display;
import beer.devs.nativepackets.api.EntityMetadataFields.Display_TextDisplay;
import beer.devs.nativepackets.api.EntityMetadataFields.Interaction;
import com.google.gson.JsonObject;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.Citizens.CitizensHook;
import dev.lone.itemsadder.anim.AnimationFrame;
import dev.lone.itemsadder.anim.AnimationTrack;
import dev.lone.itemsadder.anim.EntityLifecycleState;
import dev.lone.itemsadder.anim.EntityLifecycleTracker;
import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.api.CustomEntityNametag;
import dev.lone.itemsadder.api.CustomEntityNametag.Snapshot;
import dev.lone.itemsadder.block.CustomBlockListener;
import dev.lone.itemsadder.command.PermissionRuleConsumer;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.internal.SwitchMap88;
import dev.lone.itemsadder.item.ItemMatcher;
import dev.lone.itemsadder.item.ItemThrowManager;
import dev.lone.itemsadder.item.ThrowInputState;
import dev.lone.itemsadder.network.ConnectionRecord;
import dev.lone.itemsadder.network.NativePacketsBridge;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.network.ViaVersionIntegration;
import dev.lone.itemsadder.physics.EntityBoundsSnapshot;
import dev.lone.itemsadder.physics.VoxelCollisionShape;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.DoubleVector3Dup2;
import dev.lone.itemsadder.util.DoubleVector3Record;
import dev.lone.itemsadder.util.FloatPair;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.PluginLibraryManager;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.SoundUtils;
import dev.lone.itemsadder.util.Utils;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import lombok.Generated;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class CustomEntityImpl {
   private static final boolean qE = Boolean.getBoolean("itemsadder.test.entitySnapshots");
   private static final NamespacedKey qF = NamespacedKey.minecraft("scale");
   private static final NamespacedKey qG = NamespacedKey.minecraft("generic.scale");
   private static final NamespacedKey qH = new NamespacedKey("itemsadder", "hidden_viewers");
   @Nullable
   private volatile Set qI;
   @Nullable
   private static final Attribute qJ = getInteractionRangeAttribute();
   public CustomMobSettings qK;
   @Nullable
   private Boolean qL;
   public long qM;
   public boolean qN;
   public boolean qO = true;
   public boolean qP;
   public boolean qQ;
   public boolean qR;
   public boolean qS;
   public boolean qT;
   private volatile boolean qU;
   long qV;
   volatile boolean qW;
   @Nullable
   private DisplayEntityKey qX;
   @Nullable
   private DisplayEntityKey qY;
   @Nullable
   private Location qZ;
   @Nullable
   private Object ra;
   @Nullable
   private Object rb;
   public final CustomEntityNametag rc = new CustomEntityNametag(this);
   @Nullable
   private volatile NametagLine rd;
   @Nullable
   private DisplayEntityKey re;
   @Nullable
   private DisplayEntityKey rf;
   @Nullable
   private Snapshot rg;
   @Nullable
   private Location rh;
   private final Set ri = new HashSet();
   public boolean rj;
   public CustomEntity rk;
   volatile Object rl;
   private int rm = -1;
   public Location rn;
   volatile boolean removed;
   private final EntityLifecycleTracker ro = new EntityLifecycleTracker();
   @Nullable
   private volatile CustomEntityManager rp;
   @Nullable
   public volatile EntityPartRenderer rq;
   private final boolean field106;
   @Nullable
   List rs;
   @Nullable
   World rt;
   boolean ru;
   private volatile int rv;
   private volatile boolean rw;
   private boolean rx;
   @Nullable
   private volatile ModelPhysicsLoop ry;
   @Nullable
   private volatile DoubleVector3Dup2 rz;
   @Nullable
   private Runnable rA;
   int rB;
   ModelInstance rC;
   @Nullable
   ModelDefinitionData rD;
   ModelDefinitionData rE;
   @Nullable
   ModelDefinitionData rF;
   @Nullable
   ModelDefinitionData rG;
   @Nullable
   ModelDefinitionData rH;
   @Nullable
   ModelDefinitionData rI;
   @Nullable
   ModelDefinitionData rJ;
   @Nullable
   ModelDefinitionData rK;
   @Nullable
   ModelDefinitionData rL;
   @Nullable
   ModelDefinitionData rM;
   @Nullable
   ModelDefinitionData rN;
   @Nullable
   ModelDefinitionData rO;
   public List rP = new ArrayList();
   public LinkedHashMap rQ = new LinkedHashMap();
   public LinkedHashMap rR = new LinkedHashMap();
   private long rS;
   @Nullable
   private volatile AnimationTrack rT;
   private int[] rU = new int[0];
   @Nullable
   private DisplayEntity rV;
   @Nullable
   private volatile HumanoidEquipmentRenderer rW;
   @Nullable
   private DisplayEntityKey rX;
   @Nullable
   private volatile ModelPartController rY;
   private boolean rZ = true;
   private volatile boolean sa;
   private float sb;
   private float sc;
   private double sd;
   @Nullable
   private Location se;
   public ModelPartDisplay sf;
   public ModelPartDisplay sg;
   public ModelPartDisplay sh;
   private final ItemStack[] si = new ItemStack[3];
   boolean sj;
   boolean sk = true;
   public EntityAnchor sl;
   @Nullable
   public AbstractItemDisplayEntity sm;
   final List sn = new ArrayList();
   final Map so = new HashMap();
   private final AtomicLong sp = new AtomicLong();
   private final ConcurrentMap sq = new ConcurrentHashMap();
   final EnumMap sr = new EnumMap<>(RotationPolicy.class);
   private volatile List field107 = List.of();
   private volatile Map st = Map.of();
   private final EnumMap su = new EnumMap<>(RotationPolicy.class);
   boolean sv;
   float sw;
   float sx;
   float sy;
   float sz;
   float sA;
   Location sB;
   boolean sC;
   boolean sD;
   boolean sE;
   boolean sF;
   boolean sG;
   boolean sH;
   boolean sI;
   int sJ;
   int sK = Integer.MIN_VALUE;
   float sL;
   boolean sM;
   boolean sN;
   boolean sO;
   boolean sP = true;
   private volatile EntityStateSnapshot sQ;
   @Nullable
   private UUID sR;
   @Nullable
   private UUID sS;
   private double sT;
   private double sU;
   private double sV;
   private boolean sW;
   @Nullable
   private EntityStateSnapshot sX;
   @Nullable
   String playerSkin;
   public boolean sY;
   public boolean sZ;
   private boolean ta;
   private boolean tb;
   double tc;
   private final UUID td = UUID.randomUUID();
   private final Map te = new HashMap();
   @Nullable
   private String tf;
   private float tg = Float.NaN;
   private int th;
   @Nullable
   private BodyRotationController ti;
   public double tj = 0.0;
   public double tk = 0.0;
   public double tl = 0.0;
   double tm;
   private boolean tn;
   private float to;
   private float tp = 1.0F;

   @Nullable
   private static Attribute getInteractionRangeAttribute() {
      for (String var3 : new String[]{"ENTITY_INTERACTION_RANGE", "PLAYER_ENTITY_INTERACTION_RANGE"}) {
         try {
            return (Attribute)Attribute.class.getField(var3).get(null);
         } catch (ReflectiveOperationException var5) {
         }
      }

      return null;
   }

   public void disableGravity(LivingEntity var1) {
      if (this.qL == null) {
         this.qL = var1.hasGravity();
      }

      var1.setGravity(false);
   }

   public void restoreGravity(LivingEntity var1) {
      if (this.qL != null) {
         var1.setGravity(this.qL);
         this.qL = null;
      }
   }

   public boolean isRemoved() {
      return this.removed;
   }

   public boolean ha() {
      return this.field106;
   }

   public boolean isDormant() {
      if (this.rZ && this.qK.vn.of()) {
         for (EntityViewer var2 : this.getViewers()) {
            if (var2.kW()) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean hasModelPart(String var1) {
      return this.rY != null && this.rY.aO(var1);
   }

   public boolean isModelReady() {
      return this.rY != null && this.rY.isReady();
   }

   void refreshModelParts() {
      if (this.rY != null) {
         this.rY.mo();
      }
   }

   public List getDamageableParts() {
      return this.rY == null ? List.of() : this.rY.getParts();
   }

   public void updateModelParts() {
      if (this.rY != null && !this.isRagdollParked()) {
         this.rY.mn();
      }
   }

   void closeModelController() {
      if (this.rY != null) {
         this.rY.close();
      }
   }

   private void createModelController() {
      BoneStateList var1 = this.qK.vo;
      this.rY = !this.field106 && this.sl.xo != null && var1 != null ? new ModelPartController(this, this.sl.xo, var1) : null;
   }

   public CustomEntityImpl(CustomMobSettings var1, boolean var2) {
      this(var1, var2, false);
   }

   CustomEntityImpl(CustomMobSettings var1, boolean var2, boolean var3) {
      this.field106 = var3;
      this.qK = var1;
      if (this.qK.vn.nX() != null) {
         this.rW = new HumanoidEquipmentRenderer(this);
      }

      this.rD = (ModelDefinitionData)var1.vn.AK.values().stream().findFirst().orElse(null);
      this.qR = true;
      this.rE = var1.vn.method473(AnimationState.IDLE);
      this.rM = var1.vn.method473(AnimationState.IDLE_ANGRY);
      this.rF = var1.vn.method473(AnimationState.WALK);
      this.rK = var1.vn.method473(AnimationState.WALK_ANGRY);
      this.rL = var1.vn.method473(AnimationState.JUMP);
      this.rG = var1.vn.method473(AnimationState.ATTACK);
      this.rH = var1.vn.method473(AnimationState.DEATH);
      this.rI = var1.vn.method473(AnimationState.FLY);
      this.rJ = var1.vn.method473(AnimationState.SWIM);
      this.rN = var1.vn.method473(AnimationState.SNEAK);
      this.rO = var1.vn.method473(AnimationState.SNEAK_WALK);

      for (Entry var5 : var1.vn.AL.entrySet()) {
         if (((CustomEntityDefinition)var5.getValue()).An == BonePurpose.HEAD) {
            this.rj = true;
            break;
         }
      }

      if (this.qK.vn.AM) {
         this.playerSkin = "ewogICJ0aW1lc3RhbXAiIDogMTY2NjMwMjkxNzkzOCwKICAicHJvZmlsZUlkIiA6ICJjOWRlZTM4MDUzYjg0YzI5YjZlZjA5YjJlMDM5OTc0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTQVJfRGVjZW1iZXI1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzMxZjQ3N2ViMWE3YmVlZTYzMWMyY2E2NGQwNmY4ZjY4ZmE5M2EzMzg2ZDA0NDUyYWIyN2Y0M2FjZGYxYjYwY2IiCiAgICB9CiAgfQp9";
      }

      this.sk = var2;
      this.updateViewDistance();
   }

   void applySettings(CustomMobSettings var1) {
      this.qK = var1;
      if (this.rW == null && this.qK.vn.nX() != null) {
         this.rW = new HumanoidEquipmentRenderer(this);
      }

      this.rD = (ModelDefinitionData)var1.vn.AK.values().stream().findFirst().orElse(null);
      this.rE = var1.vn.method473(AnimationState.IDLE);
      this.rM = var1.vn.method473(AnimationState.IDLE_ANGRY);
      this.rF = var1.vn.method473(AnimationState.WALK);
      this.rK = var1.vn.method473(AnimationState.WALK_ANGRY);
      this.rL = var1.vn.method473(AnimationState.JUMP);
      this.rG = var1.vn.method473(AnimationState.ATTACK);
      this.rH = var1.vn.method473(AnimationState.DEATH);
      this.rI = var1.vn.method473(AnimationState.FLY);
      this.rJ = var1.vn.method473(AnimationState.SWIM);
      this.rN = var1.vn.method473(AnimationState.SNEAK);
      this.rO = var1.vn.method473(AnimationState.SNEAK_WALK);
      this.rj = var1.vn.AL.values().stream().anyMatch(var0 -> var0.An == BonePurpose.HEAD);
      this.updateViewDistance();
   }

   LivingEntity spawnEntity(Location var1) {
      return this.spawnEntityWithConsumer(var1, null);
   }

   LivingEntity spawnEntityWithConsumer(Location var1, @Nullable Consumer var2) {
      Location var3 = var1.clone();
      World var4 = var3.getWorld();
      var4.getChunkAt(var3);
      LivingEntity var5 = (LivingEntity)var4.spawn(var3, this.qK.vw.getEntityClass(), var3x -> {
         if (!this.qK.vP && var3x instanceof Zombie var4x) {
            var4x.setAdult();
         }

         if (!this.qK.vP && var3x instanceof Slime var5x) {
            var5x.setSize(1);
         }

         ProjectileTypeHelper.getAdapter().method3237(var3x, var1.getYaw());
         this.qK.applyToEntity((LivingEntity)var3x, this.playerSkin, true);
         if (var2 != null) {
            var2.accept((LivingEntity)var3x);
         }
      });
      if (var5.getVehicle() instanceof LivingEntity var6) {
         var6.remove();
      }

      this.sl = new EntityAnchor(var5);
      this.refreshState();
      return this.attachToEntityWithFlags(var5, true, true);
   }

   LivingEntity attachToEntity(LivingEntity var1, boolean var2) {
      return this.attachToEntityWithFlags(var1, var2, false);
   }

   LivingEntity attachToEntityWithFlags(LivingEntity var1, boolean var2, boolean var3) {
      CustomEntityImpl var4 = this.wrap(var1, null, var2, var3);
      return var4.getBukkitEntity();
   }

   CustomEntityImpl spawn(@Nullable Location var1) {
      return this.wrap(null, var1, false, false);
   }

   CustomEntityImpl wrap(@Nullable LivingEntity var1, @Nullable Location var2, boolean var3, boolean var4) {
      if (this.field106 && var1 != null) {
         throw new IllegalStateException("Visual attachments cannot own Bukkit entities");
      }

      this.sj = var3;
      if (var1 != null) {
         this.sl = new EntityAnchor(var1);
      } else {
         this.sl = new EntityAnchor(var2);
      }

      if (var1 != null && this.qK.vn.AN && !this.qK.vn.AO) {
         this.qI = loadHiddenViewers(var1);
      }

      if (var1 instanceof Player) {
         this.sZ = Bukkit.getOnlinePlayers().contains(var1);
      }

      if (this.sZ) {
         this.sk = false;
         this.sj = false;
      }

      if (this.sl.xo != null) {
         this.sl.xo.setMetadata("ItemsAdderEntity", new FixedMetadataValue(Main.getInstance(), this.qK.vn.getNamespacedID()));
      }

      if (this.sl.isAttached()) {
         this.updateRemovePacket(this.sl.xo);
      }

      this.sB = this.sl.resolveLocation();
      this.rn = this.sB;
      if (!this.field106) {
         this.qY = new DisplayEntityKey(NativePacketsHolder.PE().Pz().allocateEntityId(this.sB.getWorld()), UUID.randomUUID());
         this.re = new DisplayEntityKey(NativePacketsHolder.PE().Pz().allocateEntityId(this.sB.getWorld()), UUID.randomUUID());
         this.rf = new DisplayEntityKey(NativePacketsHolder.PE().Pz().allocateEntityId(this.sB.getWorld()), UUID.randomUUID());
      }

      if (this.sk) {
         this.sendRemovePackets();
      }

      this.refreshState();
      if (!this.field106 && this.qK.vI) {
         this.sm = AbstractItemDisplayEntity.create(this.sl.resolveLocation().clone());
         if (!(this.sl.xo instanceof Player)) {
            this.resyncDisplays();
         }
      }

      this.createModelController();
      this.rC = new ModelInstance(this, this.rD);
      if (!this.sZ && var3) {
         if (var4) {
            this.qK.applyToEntity(this.sl.xo, this.playerSkin, false);
         }

         this.applySensitivity();
         if (this.sl.xo instanceof Mob var5) {
            ProjectileTypeHelper.getAdapter().method3247(var5, true);
            this.initBodyRotation();
            this.qK.wn.applyBehaviors(var5);
            this.qK.wn.applyBehaviorsDup2(var5);
         }

         this.sl.xo.setFireTicks(0);
         this.sl.xo.setVisualFire(false);
      }

      this.qM = System.currentTimeMillis();
      return this;
   }

   public void initBodyRotation() {
      CustomEntityModel var1 = this.getModel();
      this.ti = BodyRotationControllerFactory.create(this.sl.xo, var1.Bm);
   }

   public void disposeBodyRotation() {
      BodyRotationController var1 = this.ti;
      this.ti = null;
      if (var1 != null) {
         var1.matchesDup2(this.sl.xo);
      }
   }

   public void resetSensitivity() {
      if (this.tn && this.sl.isAttached()) {
         ProjectileTypeHelper.getAdapter().ae(this.sl.xo);
      }

      this.tn = false;
   }

   public void checkSensitivity() {
      FloatPair var1 = this.qK.vN.sensitivity();
      if (var1 != null
         && !this.sZ
         && this.sl.isAttached()
         && (Math.abs(this.sl.xo.getWidth() - var1.getFirst()) > 1.0E-5 || Math.abs(this.sl.xo.getHeight() - var1.getSecond()) > 1.0E-5)) {
         this.applySensitivity();
      }
   }

   public void applySensitivity() {
      if (!this.sZ) {
         if (this.sl.isAttached()) {
            FloatPair var1 = this.qK.vN.sensitivity();
            if (var1 != null) {
               ProjectileTypeHelper.getAdapter().method3259(this.sl.xo, var1.getFirst(), var1.getSecond());
               this.tn = true;
            } else if (this.qK.vn.oj()) {
               this.resetSensitivity();
               ProjectileTypeHelper.getAdapter().method3259(this.sl.xo, this.qK.vn.Bk.x, this.qK.vn.Bj.y + this.qK.vn.Bk.y);
            } else {
               this.resetSensitivity();
            }
         }
      }
   }

   public void syncEquipmentSlots() {
      if (this.sl.isAttached() && !this.rw && this.ry == null) {
         EntityEquipment var1 = this.sl.xo.getEquipment();
         if (var1 != null) {
            if (this.rW != null) {
               this.rW.syncEquipment(var1);
            }

            if (!this.usesHumanoidEquipment()) {
               if (this.sg != null && this.rC.EU != null && !this.rC.EU.zA) {
                  this.applyEquipmentToPart(this.sg, var1.getItemInMainHand(), EquipmentSlot.HAND);
               }

               if (this.sf != null && this.rC.EU != null && !this.rC.EU.zB) {
                  this.applyEquipmentToPart(this.sf, var1.getItemInOffHand(), EquipmentSlot.OFF_HAND);
               }

               if (this.sh != null && this.rC.EU != null && !this.rC.EU.field114) {
                  this.applyEquipmentToPart(this.sh, var1.getHelmet(), EquipmentSlot.HEAD);
               }
            } else {
               ModelDefinitionData var2 = this.rC.EU;
               if (this.sg != null) {
                  this.applyEquipmentToPartIndexed(this.sg, var2 != null && var2.zA ? null : var1.getItemInMainHand(), EquipmentSlot.HAND, 0);
               }

               if (this.sf != null) {
                  this.applyEquipmentToPartIndexed(this.sf, var2 != null && var2.zB ? null : var1.getItemInOffHand(), EquipmentSlot.OFF_HAND, 1);
               }

               if (this.sh != null) {
                  this.applyEquipmentToPartIndexed(
                     this.sh, (this.rW == null || !this.rW.lU()) && (var2 == null || !var2.field114) ? var1.getHelmet() : null, EquipmentSlot.HEAD, 2
                  );
               }
            }
         }
      }
   }

   boolean usesHumanoidEquipment() {
      return this.qK.vn.AN && !this.qK.vn.AO && (this.sg != null || this.sf != null || this.sh != null || this.rW != null);
   }

   public void tickEquipment() {
      if (this.rW != null) {
         this.rW.tick();
      }
   }

   void notifyEquipmentViewer(EntityViewer var1) {
      if (this.rW != null) {
         this.rW.method8616(var1);
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public boolean hasEquipmentSlot(EquipmentSlot var1) {
      return switch (SwitchMap88.tq[var1.ordinal()]) {
         case 1 -> this.sg != null;
         case 2 -> this.sf != null;
         case 3 -> this.sh != null;
         default -> false;
      };
   }

   private void applyEquipmentToPartIndexed(ModelPartDisplay var1, @Nullable ItemStack var2, EquipmentSlot var3, int var4) {
      ItemStack var5 = var2 != null && var2.getType() != Material.AIR ? var2 : null;
      if (!Objects.equals(this.si[var4], var5)) {
         this.si[var4] = var5 == null ? null : var5.clone();
         this.applyEquipmentToPart(var1, var5, var3);
      }
   }

   public void scheduleEquipmentSync() {
      if (this.sl.isAttached()) {
         SchedulerUtils.runOnMainThread(this::syncEquipmentSlots);
      }
   }

   private void applyEquipmentToPart(ModelPartDisplay var1, @Nullable ItemStack var2, EquipmentSlot var3) {
      if (var1.Hn instanceof DisplayEntity) {
         ItemStack var4 = var2 == null ? new ItemStack(Material.AIR) : var2.clone();
         boolean var5 = var4.getType() != Material.AIR;
         NativeValue var6 = NativePacketsHolder.PE().Py().wrapBukkitItem(var4);
         this.dispatchToManager(() -> var1.sendNativeValue(var6, var5));
      } else {
         var1.setEquipment(var2, var3);
      }
   }

   public void updateFireVisuals() {
      if (!this.qK.vn.AM) {
         if (this.sl.isAttached()) {
            try {
               this.rQ.forEach((var1, var2x) -> var2x.Hn.method3024(this.sn, this.sl.xo.getFireTicks() > 0));
            } catch (ConcurrentModificationException var2) {
            }
         }
      }
   }

   public List getViewers() {
      return this.field107;
   }

   public void dispatchToManager(Runnable var1) {
      if (!this.removed) {
         this.getEntityManager().submitToOwner(this, var1);
      }
   }

   void setEntityManager(CustomEntityManager var1) {
      this.rp = var1;
   }

   private CustomEntityManager getEntityManager() {
      CustomEntityManager var1 = this.rp;
      return var1 == null ? CustomEntityManager.getInstance() : var1;
   }

   public boolean isHiddenFromPlayer(Player var1) {
      return this.hasActiveViewerRequest(var1.getUniqueId());
   }

   public void setVisibleTo(Player var1, boolean var2) {
      Objects.requireNonNull(var1, "player");
      if (this.qK.vn.AN && !this.qK.vn.AO) {
         UUID var3 = var1.getUniqueId();
         LivingEntity var4 = this.getBukkitEntity();
         if (var4 == null) {
            this.dispatchToManager(() -> this.applyHiddenViewers(updateHiddenViewers(this.qI, var3, var2), var3));
         } else {
            Runnable var5 = () -> {
               if (!this.removed) {
                  Set var4x = updateHiddenViewers(loadHiddenViewers(var4), var3, var2);
                  if (var4x == null) {
                     var4.getPersistentDataContainer().remove(qH);
                  } else {
                     long[] var5x = new long[var4x.size() * 2];
                     int var6x = 0;

                     for (UUID var8 : var4x) {
                        var5x[var6x++] = var8.getMostSignificantBits();
                        var5x[var6x++] = var8.getLeastSignificantBits();
                     }

                     var4.getPersistentDataContainer().set(qH, PersistentDataType.LONG_ARRAY, var5x);
                  }

                  this.dispatchToManager(() -> this.applyHiddenViewers(var4x, var3));
               }
            };
            SchedulerAccess var6 = NativePacketsHolder.PE().IP();
            if (var6.ownsEntity(var4)) {
               var5.run();
            } else {
               var6.entity(Main.getInstance(), var4, var5, () -> {});
            }
         }
      } else {
         throw new IllegalStateException("Per-viewer visibility requires a V2 model");
      }
   }

   @Nullable
   private static Set loadHiddenViewers(LivingEntity var0) {
      long[] var1 = (long[])var0.getPersistentDataContainer().get(qH, PersistentDataType.LONG_ARRAY);
      if (var1 != null && var1.length != 0) {
         if (var1.length % 2 != 0) {
            throw new IllegalStateException("Invalid saved hidden viewer UUIDs");
         }

         HashSet var2 = new HashSet();

         for (byte var3 = 0; var3 < var1.length; var3 += 2) {
            var2.add(new UUID(var1[var3], var1[var3 + 1]));
         }

         return Set.copyOf(var2);
      } else {
         return null;
      }
   }

   @Nullable
   static Set updateHiddenViewers(@Nullable Set var0, UUID var1, boolean var2) {
      if (var0 == null && var2) {
         return null;
      }

      HashSet var3 = var0 == null ? new HashSet() : new HashSet(var0);
      if (var2) {
         var3.remove(var1);
      } else {
         var3.add(var1);
      }

      return var3.isEmpty() ? null : Set.copyOf(var3);
   }

   private void applyHiddenViewers(@Nullable Set var1, UUID var2) {
      this.qI = var1;
      this.refreshViewerVisibility(var2);
   }

   boolean isViewerAllowed(UUID var1) {
      return (this.qI == null || !this.qI.contains(var1)) && (this.rq == null || this.rq.xp.Hm.isViewerAllowed(var1));
   }

   private void refreshViewerVisibility(UUID var1) {
      EntityViewer var2 = (EntityViewer)this.so.get(var1);
      if (var2 != null) {
         var2.lg();
         if (!this.isViewerAllowed(var1)) {
            this.hideViewer(var2);
         } else if (var2.kW()) {
            var2.lc();
            this.showViewer(var2);
         }
      }

      if (this.rs != null) {
         for (CustomEntityImpl var4 : this.rs) {
            var4.refreshViewerVisibility(var1);
         }
      }
   }

   boolean hasActiveViewerRequest(UUID var1) {
      ViewerSyncRequest var2 = (ViewerSyncRequest)this.sq.get(var1);
      return var2 != null && var2.ls();
   }

   public void addViewer(Player var1, boolean var2) {
      if (!this.removed && var1.isOnline() && this.supportsViewer(var1)) {
         UUID var3 = var1.getUniqueId();
         EntityViewer var4 = new EntityViewer(var1, this);
         this.scheduleViewerSync(var3, true, var4, var2);
      }
   }

   public void scheduleViewerRemoval(Player var1) {
      UUID var2 = var1.getUniqueId();
      this.scheduleViewerSync(var2, false, null, false);
   }

   void scheduleViewerSync(UUID var1, boolean var2, @Nullable EntityViewer var3, boolean var4) {
      AtomicReference var5 = new AtomicReference();
      this.sq.compute(var1, (var5x, var6x) -> {
         if (this.removed) {
            return (ViewerSyncRequest)var6x;
         }

         long var7 = this.sp.incrementAndGet();
         ViewerSyncTask var9 = new ViewerSyncTask(var7, var2, var3, var4);
         var5.set(var9);
         return new ViewerSyncRequest(var7, var2, var9);
      });
      ViewerSyncTask var6 = (ViewerSyncTask)var5.get();
      if (var6 != null) {
         this.dispatchToManager(() -> this.executeViewerSync(var1, var6));
      }
   }

   public void activateViewer(Player var1) {
      UUID var2 = var1.getUniqueId();
      if (!this.hasActiveViewerRequest(var2)) {
         this.addViewer(var1, false);
      }

      this.dispatchToManager(() -> {
         if (this.hasActiveViewerRequest(var2) && !this.removed) {
            EntityViewer var2x = (EntityViewer)this.so.get(var2);
            if (var2x != null) {
               this.updateViewer(var2x);
            }
         }
      });
   }

   public void deactivateViewer(Player var1) {
      UUID var2 = var1.getUniqueId();
      this.dispatchToManager(() -> {
         EntityViewer var2x = (EntityViewer)this.so.get(var2);
         if (var2x != null) {
            this.hideViewer(var2x);
         }
      });
   }

   private void executeViewerSync(UUID var1, ViewerSyncTask var2) {
      AtomicBoolean var3 = new AtomicBoolean();
      this.sq.computeIfPresent(var1, (var3x, var4x) -> {
         if (!this.removed && var2.equals(var4x.task())) {
            var3.set(true);
            return var4x.ls() ? new ViewerSyncRequest(var4x.timestamp(), true, null) : null;
         } else {
            return (ViewerSyncRequest)var4x;
         }
      });
      if (var3.get() && !this.removed) {
         if (var2.lp()) {
            EntityViewer var4 = Objects.requireNonNull(var2.viewer()).kP();
            if (var4 != null) {
               this.registerViewer(var4, var2.lr());
            }
         } else {
            EntityViewer var5 = this.unregisterViewer(var1);
            if (var5 != null) {
               this.hideViewer(var5);
            }
         }
      }
   }

   public void addViewersInWorld(World var1) {
      this.addViewersInWorldWithFlag(var1, false);
   }

   public void addViewersInWorldWithFlag(World var1, boolean var2) {
      List var3 = List.copyOf(var1.getPlayers());
      if (this.ro.jX() != EntityLifecycleState.NEW) {
         var3.forEach(var2x -> this.addViewer(var2x, var2));
      } else {
         var3.forEach(var2x -> this.createViewer(var2x, var2));
      }
   }

   @Nullable
   public EntityViewer getOrCreateViewer(Player var1) {
      return this.createViewer(var1, false);
   }

   @Nullable
   public EntityViewer createViewer(Player var1, boolean var2) {
      this.flushIfActive();
      if (var1.isOnline() && this.supportsViewer(var1)) {
         this.putViewerRequest(var1.getUniqueId(), true);
         return this.registerViewer(new EntityViewer(var1, this), var2);
      } else {
         return null;
      }
   }

   private boolean supportsViewer(Player var1) {
      return !this.qK.vn.AM || !this.qK.vn.AN || ViaVersionIntegration.method4866(var1.getUniqueId()) >= 769;
   }

   private void putViewerRequest(UUID var1, boolean var2) {
      long var3 = this.sp.incrementAndGet();
      this.sq.put(var1, new ViewerSyncRequest(var3, var2, null));
   }

   private EntityViewer registerViewer(EntityViewer var1, boolean var2) {
      UUID var3 = var1.uz;
      EntityViewer var4 = (EntityViewer)this.so.get(var3);
      if (var4 != null && var4.method8550(var1)) {
         return var4;
      }

      if (var4 != null) {
         this.unregisterViewer(var3);
      }

      this.sn.add(var1);
      if (this.rq != null || !this.isViewerAllowed(var3)) {
         var1.uW = true;
      }

      this.so.put(var3, var1);
      this.sr.computeIfAbsent(var1.getRotationPolicy(), var0 -> new ArrayList<>()).add(var1);
      this.rQ.values().forEach(var1x -> {
         if (var1x.Hn instanceof AnimatedDisplayEntity var2x) {
            var2x.method3129(var1.getRotationPolicy());
         }
      });
      this.rebuildViewerCaches();
      if (var2) {
         this.updateViewer(var1);
      }

      return var1;
   }

   public void removeViewer(Player player) {
      this.flushIfActive();
      UUID var2 = player.getUniqueId();
      this.putViewerRequest(var2, false);
      this.unregisterViewer(var2);
   }

   @Nullable
   private EntityViewer unregisterViewer(UUID var1) {
      EntityViewer var2 = (EntityViewer)this.so.remove(var1);
      if (var2 != null) {
         this.updateDisplayFor(var2);
         this.despawnNametagFor(var2);
         this.clearNametagFor(var2);
      }

      this.ri.remove(var1);
      this.sn.remove(var2);
      if (var2 != null) {
         List var3 = (List)this.sr.get(var2.getRotationPolicy());
         if (var3 != null) {
            var3.remove(var2);
         }
      }

      this.rebuildViewerCaches();
      return var2;
   }

   public List getViewersByRotation(RotationPolicy var1) {
      return this.st.getOrDefault(var1, List.of());
   }

   private void rebuildViewerCaches() {
      this.field107 = List.copyOf(this.sn);
      EnumMap var1 = new EnumMap<>(RotationPolicy.class);
      this.sr.forEach((var1x, var2) -> var1.put(var1x, List.copyOf(var2)));
      this.st = Map.copyOf(var1);
   }

   public static void flushManagerQueue() {
      CustomEntityManager.requireOwnerThread();
   }

   public void flushIfActive() {
      if (this.ro.jX() != EntityLifecycleState.NEW) {
         flushManagerQueue();
      }
   }

   public void clearPendingSyncs() {
      this.su.clear();
   }

   public void scheduleBoneUpdate(int var1) {
      if (this.ro.method8549(var1)) {
         if (this.ro.tD == EntityLifecycleState.ACTIVE) {
            this.getEntityManager().registerNetId(this, var1);
         }
      }
   }

   void scheduleAllBoneUpdates() {
      this.rQ.forEach((var1, var2) -> {
         if (var2.Hn instanceof AnimatedDisplayEntity var3) {
            for (int var7 : var3.Od()) {
               this.scheduleBoneUpdate(var7);
            }
         } else {
            this.scheduleBoneUpdate(var2.Hn.aQn);
         }
      });
   }

   void advanceLifecycle() {
      this.ro.jS();
   }

   public boolean isLifecycleDone() {
      return this.ro.jT();
   }

   private boolean tryMarkRemoved() {
      if (!this.ro.hz()) {
         return false;
      }

      this.removed = true;
      this.sq.clear();
      return true;
   }

   void markRemoved() {
      this.removed = true;
      this.sq.clear();
   }

   int[] getLifecycleIds() {
      return this.ro.jV();
   }

   EntityLifecycleState getLifecycleState() {
      return this.ro.tD;
   }

   long getLifecycleTimestamp() {
      return this.ro.tE;
   }

   public int[] getRenderIds() {
      return this.rU;
   }

   public boolean hasRootDisplay() {
      return this.rV != null;
   }

   public float getViewerOffset(EntityViewer var1) {
      return var1.uX ? this.to : 0.0F;
   }

   public float getYawOffset() {
      return this.tp;
   }

   public void refreshPosition() {
      if (this.rV != null || this.rX != null) {
         Location var1 = this.getBaseLocation();
         this.updatePositionForViewers(var1.getX(), var1.getY(), var1.getZ());
      }
   }

   void refreshAllPositions() {
      if (this.rV != null || this.rX != null) {
         Location var1 = this.getBaseLocation();
         this.teleportInteraction(var1.getX(), var1.getY(), var1.getZ());
         if (this.rV != null) {
            this.moveRootAndParts(var1.getX(), var1.getY(), var1.getZ());
         }
      }
   }

   private void moveRootAndParts(double var1, double var3, double var5) {
      this.rV.method2998(var1, var3, var5);

      for (ModelPartDisplay var8 : this.rQ.values()) {
         if (var8.Ho.An != BonePurpose.MOUNT_PIVOT && var8.Ho.An != BonePurpose.LOCATOR_PIVOT && var8.Hn instanceof DisplayEntity var9) {
            var9.method2998(var1, var3, var5);
         }
      }
   }

   public void updatePositionForViewers(double var1, double var3, double var5) {
      if (this.rV != null || this.rX != null) {
         for (EntityViewer var8 : this.sn) {
            var8.kS();
         }

         boolean var30 = false;

         try {
            this.applyPositionUpdate(var1, var3, var5);
            var30 = true;
         } finally {
            try {
               for (EntityViewer var15 : this.sn) {
                  var15.method8552(var30);
               }
            } finally {
               for (EntityViewer var18 : this.sn) {
                  var18.method8552(false);
               }
            }
         }
      }
   }

   public void applyPositionUpdate(double var1, double var3, double var5) {
      this.teleportInteraction(var1, var3, var5);
      if (this.rV != null) {
         EntityViewer var7 = null;

         for (EntityViewer var9 : this.sn) {
            this.tryAttachViewerPacket(var9);
            if (var9.uX) {
               var7 = var9;
            }
         }

         this.moveRootAndParts(var1, var3, var5);
         this.rV.addMetadataPackets(this.sn);
         this.rV.method3091(false, null, this.sn, var7);
      }
   }

   public void refreshPassengerPackets() {
      if (this.rV != null) {
         for (EntityViewer var2 : this.sn) {
            this.sendPassengerPackets(var2);
         }
      }
   }

   void sendPassengerPackets(EntityViewer var1) {
      if (this.rV != null && !var1.uW && var1.kW()) {
         ArrayList var2 = new ArrayList();

         for (ModelPartDisplay var4 : this.rQ.values()) {
            if (var4.isVisibleToViewers()
               && var4.Ho.An != BonePurpose.LOCATOR_PIVOT
               && var4.Ho.An != BonePurpose.MOUNT_PIVOT
               && var4.Hn instanceof DisplayEntity) {
               ((DisplayEntity)var4.Hn).NG();
               if (var4.Hn instanceof AnimatedDisplayEntity var5) {
                  for (int var9 : var5.method3130(var1.getRotationPolicy())) {
                     var2.add(var9);
                  }
               } else {
                  var2.add(var4.Hn.aQn);
               }
            }
         }

         if (this.rW != null) {
            this.rW.method8614(var1, var2);
         }

         ConnectionRecord var10 = var1.getConnection();
         if (var10 != null) {
            NativePacketsHolder.PE()
               .method1871(var10, NativePacketsHolder.PE().Pv().passengers(this.rV.aQn, var2.stream().mapToInt(Integer::intValue).toArray()));
         }

         this.tryAttachViewerPacket(var1);
      }
   }

   private void tryAttachViewerPacket(EntityViewer var1) {
      if (!var1.uX && this.rV != null && !var1.uW && var1.kW() && this.tb && !this.ta && this.sZ && this.sl.xo == var1.li) {
         ConnectionRecord var2 = var1.getConnection();
         if (var2 != null) {
            var1.uX = NativePacketsHolder.PE().method1879(var2, this, this.rV.aQn);
            if (var1.uX) {
               var1.lb();
            }
         }
      }
   }

   private void detachViewerPacket(EntityViewer var1, @Nullable NativePacketsBridge var2, @Nullable ConnectionRecord var3) {
      if (var1.uX) {
         var1.uX = false;
         if (var2 != null && var3 != null) {
            var2.method1880(var3, this);
         }
      }
   }

   private int[] collectEntityIds(RotationPolicy var1) {
      int[] var2 = this.getEntityIdsFor(var1);
      int var3 = (this.rV == null ? 0 : 1) + (this.rX == null ? 0 : 1);
      if (var3 == 0) {
         return var2;
      }

      int[] var4 = Arrays.copyOf(var2, var2.length + var3);
      if (this.rV != null) {
         var4[var2.length] = this.rV.aQn;
      }

      if (this.rX != null) {
         var4[var4.length - 1] = this.rX.entityId();
      }

      return var4;
   }

   public int getInteractionEntityId() {
      return this.rX == null ? -1 : this.rX.entityId();
   }

   void spawnInteractionEntity() {
      if (this.rX != null && !this.ru) {
         this.scheduleBoneUpdate(this.rX.entityId());
         this.sa = true;

         for (EntityViewer var2 : this.sn) {
            if (!var2.uW) {
               this.sendInteractionSpawn(var2);
            }
         }
      }
   }

   private void sendInteractionSpawn(EntityViewer var1) {
      if (this.rX != null && this.sa && var1.kW()) {
         ConnectionRecord var2 = var1.getConnection();
         if (var2 != null) {
            Location var3 = this.se == null ? this.getLocation() : this.se;
            double var4 = this.se == null ? var3.getY() + this.sd : var3.getY();
            NativePacketFactory var6 = NativePacketsHolder.PE().Pv();
            boolean var7 = NativePacketsHolder.PE()
               .method1871(
                  var2,
                  var6.addEntity(
                     this.rX.entityId(),
                     this.rX.worldId(),
                     NativePacketsHolder.PE().Pz().nativeEntityType(EntityType.INTERACTION),
                     var3.getX(),
                     var4,
                     var3.getZ(),
                     0.0F,
                     0.0F,
                     0.0F,
                     0,
                     0.0,
                     0.0,
                     0.0
                  )
               );
            boolean var8 = NativePacketsHolder.PE()
               .method1871(
                  var2,
                  var6.metadata(
                     this.rX.entityId(),
                     new EntityMetadataField[]{Interaction.WIDTH, Interaction.HEIGHT},
                     new Object[]{Math.max(0.01F, this.sb), Math.max(0.01F, this.sc)}
                  )
               );
            if (var7 && var8) {
               var1.uK = false;
            }
         }
      }
   }

   private void teleportInteraction(double var1, double var3, double var5) {
      if (this.rX != null && !this.ru) {
         var3 += this.sd;
         boolean var7 = this.se == null || this.se.getX() != var1 || this.se.getY() != var3 || this.se.getZ() != var5;
         if (var7) {
            this.se = new Location(null, var1, var3, var5);
         }

         Object var8 = null;

         for (EntityViewer var10 : this.sn) {
            if (var7) {
               var10.uK = true;
            }

            ViewerVisibility var11 = var10.uV;
            double var12 = var11 == null ? var10.uQ : var11.fm();
            if (var10.uK && !(var12 < 0.0) && !(var12 > Math.max(30.0, var10.uU + 1.0)) && !var10.uW && var10.kW()) {
               ConnectionRecord var14 = var10.getConnection();
               if (var14 != null) {
                  if (var8 == null) {
                     var8 = NativePacketsHolder.PE().Pv().teleport(this.rX.entityId(), var1, var3, var5, 0.0F, 0.0F, false);
                  }

                  if (NativePacketsHolder.PE().method1871(var14, var8)) {
                     var10.uK = false;
                  }
               }
            }
         }
      }
   }

   private void updateInteractionSize(float var1, float var2, float var3) {
      if (this.rX != null) {
         float var4 = this.qK.vn.Bk == null ? var2 : this.qK.vn.Bk.x * var1;
         float var5 = this.qK.vn.Bk == null ? var3 : this.qK.vn.Bk.y * var1;
         double var6 = this.qK.vn.Bj == null ? 0.0 : this.qK.vn.Bj.y * var1;
         if (this.sb != var4 || this.sc != var5 || this.sd != var6) {
            this.sb = var4;
            this.sc = var5;
            this.sd = var6;
            if (this.sa) {
               Object var8 = NativePacketsHolder.PE()
                  .Pv()
                  .metadata(
                     this.rX.entityId(),
                     new EntityMetadataField[]{Interaction.WIDTH, Interaction.HEIGHT},
                     new Object[]{Math.max(0.01F, var4), Math.max(0.01F, var5)}
                  );

               for (EntityViewer var10 : this.sn) {
                  if (!var10.uW && var10.kW()) {
                     ConnectionRecord var11 = var10.getConnection();
                     if (var11 != null) {
                        NativePacketsHolder.PE().method1871(var11, var8);
                     }
                  }
               }
            }
         }
      }
   }

   public Map getModelPartIds(@Nullable Player var1) {
      EntityViewer var2 = var1 == null ? null : (EntityViewer)this.so.get(var1.getUniqueId());
      RotationPolicy var3 = var2 == null ? RotationPolicy.COMPATIBLE : var2.getRotationPolicy();
      LinkedHashMap var4 = new LinkedHashMap();
      this.rQ.forEach((var2x, var3x) -> var4.put(var2x, var3x.Hn instanceof AnimatedDisplayEntity var4x ? var4x.method3130(var3) : new int[]{var3x.Hn.aQn}));
      return Collections.unmodifiableMap(var4);
   }

   void captureAnimationTrack() {
      if (qE) {
         RotationPolicy var1 = this.field107.isEmpty() ? RotationPolicy.COMPATIBLE : ((EntityViewer)this.field107.getFirst()).getRotationPolicy();
         LinkedHashMap var2 = new LinkedHashMap();
         LinkedHashMap var3 = new LinkedHashMap();
         this.rQ
            .forEach(
               (var4, var5) -> {
                  Matrix4fc var6 = this.rC.bq(var5.Ho.getId());
                  if (var6 != null) {
                     var2.put(var4, matrixToList(var6));
                  }

                  if (var5.Hn instanceof DisplayEntity var7) {
                     List var10 = var7 instanceof AnimatedDisplayEntity var9 ? var9.method3131(var1) : List.of(var7.snapshot());
                     var3.put(
                        var4,
                        var10.stream()
                           .map(var0 -> new AnimationFrame(var0.jI(), matrixToList(var0.NR()), var0.jK(), var0.NS() == null ? null : matrixToList(var0.NS())))
                           .toList()
                     );
                  }
               }
            );
         this.rT = new AnimationTrack(
            this.qK.vn.ai(),
            ++this.rS,
            this.rC.getPlaybackRequest(),
            matrixToList(new Matrix4f().rotationY(-this.getBodyYaw())),
            Collections.unmodifiableMap(var2),
            Collections.unmodifiableMap(var3)
         );
      }
   }

   @Nullable
   public AnimationTrack getAnimationTrack() {
      return this.rT;
   }

   private static List matrixToList(Matrix4fc var0) {
      float[] var1 = var0.get(new float[16]);
      ArrayList var2 = new ArrayList(var1.length);

      for (float var6 : var1) {
         var2.add(var6);
      }

      return List.copyOf(var2);
   }

   int[] getEntityIdsFor(RotationPolicy var1) {
      return !this.qK.vn.nJ() ? this.rU : this.su.computeIfAbsent(var1, var2 -> {
         LinkedHashSet var3 = new LinkedHashSet();

         for (ModelPartDisplay var5 : this.rQ.values()) {
            if (var5.Hn instanceof AnimatedDisplayEntity var6) {
               for (int var10 : var6.method3130(var1)) {
                  var3.add(var10);
               }
            } else {
               var3.add(var5.Hn.aQn);
               var3.add(var5.Hn.aQm);
            }
         }

         return var3.stream().mapToInt(Integer::intValue).toArray();
      });
   }

   public ModelInstance getModelInstance() {
      return this.rC;
   }

   public Location getLocation() {
      return this.sl.resolveLocation();
   }

   public Location getBaseLocation() {
      return this.sB;
   }

   @Nullable
   public UUID getEntityUuid() {
      return this.sR;
   }

   @Nullable
   public Location getLastKnownPosition() {
      EntityStateSnapshot var1 = this.sQ;
      DoubleVector3Record var2 = var1 == null ? null : var1.kB();
      return var2 == null ? null : new Location(null, var2.getX(), var2.getY(), var2.getZ());
   }

   public boolean hS() {
      return this.sC;
   }

   public boolean hT() {
      return this.sD;
   }

   public World getWorld() {
      return this.getLocation().getWorld();
   }

   @Nullable
   public LivingEntity getBukkitEntity() {
      return this.sl.xo;
   }

   @Nullable
   public LivingEntity getRootBukkitEntity() {
      CustomEntityImpl var1 = this;

      while (var1.rq != null) {
         var1 = var1.rq.xp.Hm;
      }

      return var1.getBukkitEntity();
   }

   void transferToMob(Mob var1, CustomMobSettings var2, CustomEntityManager var3, UUID var4) {
      this.qW = true;
      ModelPartController var5 = this.rY;
      if (var5 != null) {
         var5.method8638(var1);
         var5.close();
      }

      this.disposeBodyRotation();
      this.sl = new EntityAnchor(var1);
      this.applySettings(var2);
      this.createModelController();
      if (this.sj && !this.sZ) {
         this.initBodyRotation();
      }

      this.refreshState();
      this.applySensitivity();
      ProjectileTypeHelper.getAdapter().method3247(var1, true);
      var2.wn.applyBehaviors(var1);
      var2.wn.applyBehaviorsDup2(var1);
      this.qM = System.currentTimeMillis();
      var3.submitToOwner(this, () -> {
         try {
            if (var5 != null) {
               for (EntityViewer var5x : this.sn) {
                  var5.sendToViewer(var5x);
               }
            }

            if (this.rW != null) {
               this.rW.close();
            }

            this.hideAllViewers();
            this.rQ.clear();
            this.rR.clear();
            this.rU = new int[0];
            this.su.clear();
            this.rV = null;
            this.rX = null;
            this.sa = false;
            var3.unregisterIds(this, var4);
            this.ro.jW();
            if (this.rC != null) {
               this.rC.aM();
            }

            List var14 = this.field107;
            Map var15 = this.st;
            this.field107 = List.of();
            this.st = Map.of();

            try {
               this.rC = new ModelInstance(this, this.rD);
            } finally {
               this.field107 = var14;
               this.st = var15;
            }

            if (this.rC != null) {
               this.spawnInteractionEntity();
               this.scheduleAllBoneUpdates();
               this.refreshAllViewers();
            }

            this.sendRemovePackets();
         } finally {
            this.qW = false;
         }
      });
   }

   public @UnknownNullability LivingEntity getLivingEntity() {
      return this.sl.xo;
   }

   public boolean hasBukkitEntity() {
      return this.sl.xo != null;
   }

   private void updateRemovePacket(LivingEntity var1) {
      int var2 = var1.getEntityId();
      if (var2 != this.rm) {
         this.rm = var2;
         this.rl = NativePacketsHolder.PE().Pv().removeEntities(new int[]{var2});
      }
   }

   public void sendRemovePackets() {
      if (this.sk && this.sl.isAttached()) {
         if (this.sn.size() == 0) {
            return;
         }

         for (EntityViewer var2 : this.sn) {
            ConnectionRecord var3 = var2.kU();
            if (var3 != null) {
               NativePacketsHolder.PE().method1871(var3, this.rl);
            }
         }
      }
   }

   public void sendRemovePacketTo(Player var1) {
      if (this.sk && this.sl.isAttached()) {
         NativePacketsHolder.PE().method1868(var1, this.rl);
      }
   }

   @Nullable
   public static Entity getOwnerEntity(Entity var0) {
      String var1 = (String)var0.getPersistentDataContainer().get(CustomEntityManager.oA, PersistentDataType.STRING);
      if (var1 == null) {
         return null;
      }

      UUID var2 = UUID.fromString(var1);
      return var0.getWorld().getEntity(var2);
   }

   public static boolean isCustomEntity(Entity var0) {
      return var0.getPersistentDataContainer().has(CustomEntityManager.oz, PersistentDataType.INTEGER);
   }

   public ModelPartDisplay getModelPart(String var1) {
      return (ModelPartDisplay)this.rQ.get(var1.toLowerCase());
   }

   public void setPlayerSkinRaw(String var1) {
      this.playerSkin = var1;
   }

   public void changePlayerSkin(String playerSkin) {
      this.applyPlayerSkin(playerSkin, true);
   }

   public void applyPlayerSkin(String var1, boolean var2) {
      if (var1 == null) {
         var1 = "ewogICJ0aW1lc3RhbXAiIDogMTY2NjMwMjkxNzkzOCwKICAicHJvZmlsZUlkIiA6ICJjOWRlZTM4MDUzYjg0YzI5YjZlZjA5YjJlMDM5OTc0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTQVJfRGVjZW1iZXI1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzMxZjQ3N2ViMWE3YmVlZTYzMWMyY2E2NGQwNmY4ZjY4ZmE5M2EzMzg2ZDA0NDUyYWIyN2Y0M2FjZGYxYjYwY2IiCiAgICB9CiAgfQp9";
      }

      var1 = resolveSkinTexture(var1);
      this.playerSkin = var1;
      if (this.sl.isAttached()) {
         CustomEntityManager.tagEntitySkin(this.sl.xo, var1);
      }

      this.rQ.forEach((var2x, var3x) -> var3x.setItemModel(this.playerSkin, var2));
   }

   public static boolean isMineskinUrl(String var0) {
      if (var0 == null) {
         return false;
      }

      try {
         URI var1 = URI.create(var0);
         String var2 = var1.getHost();
         return "https".equalsIgnoreCase(var1.getScheme())
            && var2 != null
            && (var2.equalsIgnoreCase("minesk.in") || var2.equalsIgnoreCase("mineskin.org") || var2.equalsIgnoreCase("www.mineskin.org"));
      } catch (IllegalArgumentException var3) {
         return false;
      }
   }

   public static String resolveSkinTexture(String var0) {
      if (!isMineskinUrl(var0)) {
         return var0;
      }

      String var1 = URI.create(var0).getPath();
      String var2 = var1 == null ? "" : var1.substring(var1.lastIndexOf(47) + 1);
      if (!var2.matches("[A-Za-z0-9-]{1,64}")) {
         return "ewogICJ0aW1lc3RhbXAiIDogMTY2NjMwMjkxNzkzOCwKICAicHJvZmlsZUlkIiA6ICJjOWRlZTM4MDUzYjg0YzI5YjZlZjA5YjJlMDM5OTc0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTQVJfRGVjZW1iZXI1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzMxZjQ3N2ViMWE3YmVlZTYzMWMyY2E2NGQwNmY4ZjY4ZmE5M2EzMzg2ZDA0NDUyYWIyN2Y0M2FjZGYxYjYwY2IiCiAgICB9CiAgfQp9";
      }

      try {
         JsonObject var3 = Main.method521("https://api.mineskin.org/get/uuid/" + var2, 5000);
         return var3.get("data").getAsJsonObject().get("texture").getAsJsonObject().get("value").getAsString();
      } catch (Exception var4) {
         Log.warn("Failed to fetch custom player texture: " + var0 + " (" + var4.getMessage() + ")");
         return "ewogICJ0aW1lc3RhbXAiIDogMTY2NjMwMjkxNzkzOCwKICAicHJvZmlsZUlkIiA6ICJjOWRlZTM4MDUzYjg0YzI5YjZlZjA5YjJlMDM5OTc0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTQVJfRGVjZW1iZXI1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzMxZjQ3N2ViMWE3YmVlZTYzMWMyY2E2NGQwNmY4ZjY4ZmE5M2EzMzg2ZDA0NDUyYWIyN2Y0M2FjZGYxYjYwY2IiCiAgICB9CiAgfQp9";
      }
   }

   @Nullable
   public String getPlayerSkin() {
      return this.playerSkin;
   }

   public void initializeModelParts(ModelInstance var1) {
      if (this.qI != null) {
         for (EntityViewer var3 : this.sn) {
            if (!this.isViewerAllowed(var3.uz)) {
               var3.uW = true;
               var3.uL = this.sm != null;
            }
         }
      }

      if ((this.qK.vn.AN || !this.qR) && this.qK.vn.nS() && this.sk && !this.qQ && this.sl.isAttached() && !this.sZ) {
         LivingEntity var10 = this.sl.xo;
         this.rX = new DisplayEntityKey(NativePacketsHolder.PE().Pz().allocateEntityId(var10.getWorld()), UUID.randomUUID());
         this.updateInteractionSize(this.tp, (float)var10.getWidth(), (float)var10.getHeight());
      }

      if (this.qK.vn.AN
         && !this.qK.vn.AO
         && !this.qS
         && NativePacketsHolder.aN()
         && this.qK.vn.AL.values().stream().filter(var0 -> var0.Aq).limit(2L).count() > 1L) {
         this.rV = new DisplayEntity(this.getLocation());
         this.rV.method3057(this.tc);
      }

      ArrayList var11 = new ArrayList();

      for (Entry var4 : this.qK.vn.AL.entrySet()) {
         CustomEntityDefinition var5 = (CustomEntityDefinition)var4.getValue();
         if (!Settings.get().emoteModernMethod || (!var5.field115 || !var5.getId().startsWith("dummy_")) && !var5.getId().equals("sus_6")) {
            if (Settings.get().logBonesSpawning) {
               Log.log("Initialized bone: " + var5.getId() + " | " + BlockFaceUtils.locationCoordsString(this.getLocation()) + "(" + var5.getOffset() + ")");
            }

            ModelPartDisplay var6 = new ModelPartDisplay(this, var5, var1);
            if (var6.Hn instanceof AnimatedDisplayEntity var7) {
               for (RotationPolicy var9 : this.st.keySet()) {
                  var7.method3129(var9);
               }
            }

            this.rQ.put(var5.getId(), var6);
            this.rR.put(var5.getId(), var6);
            var11.add(var6.Hn.aQn);
            var11.add(var6.Hn.aQm);
            if (var6.Ho.An == BonePurpose.LEFT_HAND_PIVOT) {
               this.sf = var6;
            } else if (var6.Ho.An == BonePurpose.RIGHT_HAND_PIVOT) {
               this.sg = var6;
            } else if (var6.Ho.An == BonePurpose.HAT_PIVOT) {
               this.sh = var6;
            }
         }
      }

      this.rU = var11.stream().mapToInt(Integer::intValue).toArray();
      this.rQ.forEach((var1x, var2) -> var2.resolveLinkedPart(this.rQ));
      this.sendRemovePackets();
      if (this.rV != null) {
         this.rV.addSpawnPackets(this.getViewers());
         this.refreshPassengerPackets();
      }
   }

   public void respawnAllViewers() {
      int var1 = 0;

      for (int var2 = this.sn.size(); var1 < var2; var1++) {
         EntityViewer var3 = (EntityViewer)this.sn.get(var1);
         if (!this.isViewerAllowed(var3.uz)) {
            var3.uW = true;
            var3.uL = this.sm != null;
         } else {
            this.clearNametagFor(var3);
            var3.uW = false;
            if (this.rV != null) {
               this.rV.spawnFor(var3);
            }

            this.sendInteractionSpawn(var3);
            this.rQ.forEach((var1x, var2x) -> {
               var2x.spawnForViewer(var3);
               if (var2x.Ho.An == BonePurpose.MOUNT_PIVOT && var2x.getRider() != null) {
                  var2x.getRider().sendPassengersUpdate();
               }
            });
            this.sendPassengerPackets(var3);
            this.spawnNametagFor(var3);
         }
      }

      this.sendRemovePackets();
   }

   public void refreshViewerFor(Player var1) {
      if (!CustomEntityManager.isOwnerThread()) {
         this.dispatchToManager(() -> this.refreshViewerFor(var1));
      } else {
         EntityViewer var2 = (EntityViewer)this.so.get(var1.getUniqueId());
         if (var2 == null) {
            var2 = this.getOrCreateViewer(var1);
         }

         if (var2 != null) {
            this.updateViewer(var2);
         }
      }
   }

   void refreshAllViewers() {
      int var1 = 0;

      for (int var2 = this.sn.size(); var1 < var2; var1++) {
         EntityViewer var3 = (EntityViewer)this.sn.get(var1);
         this.updateViewer(var3);
      }
   }

   void showViewer(EntityViewer var1) {
      if (this.isViewerAllowed(var1.uz)) {
         if (!var1.uW) {
            this.spawnDisplayFor(var1);
         } else if (this.rq == null || this.rq.isVisibleTo(var1)) {
            var1.uW = false;
            this.spawnDisplayFor(var1);
            if (this.rV != null) {
               this.rV.spawnFor(var1);
            }

            this.sendInteractionSpawn(var1);
            this.rQ.forEach((var1x, var2) -> var2.updateFor(var1, false));
            this.sendPassengerPackets(var1);
            this.spawnNametagFor(var1);
            this.sendRemovePackets();
         }
      }
   }

   void updateViewer(EntityViewer var1) {
      if (!CustomEntityManager.isOwnerThread()) {
         this.dispatchToManager(() -> this.updateViewer(var1));
      } else if (this.isViewerAllowed(var1.uz) && (!this.getModel().AN || var1.kW())) {
         this.updateDisplayFor(var1);
         this.notifyEquipmentViewer(var1);
         if (this.rY != null) {
            this.rY.sendToViewer(var1);
         }

         if (this.rq != null) {
            this.hideViewer(var1);
         } else {
            this.despawnNametagFor(var1);
            this.clearNametagFor(var1);
            if (this.rX != null) {
               ConnectionRecord var2 = var1.kU();
               if (var2 != null) {
                  NativePacketsHolder.PE().method1871(var2, NativePacketsHolder.PE().Pv().removeEntities(new int[]{this.rX.entityId()}));
               }
            }

            var1.uW = false;
            this.spawnDisplayFor(var1);
            if (this.rV != null) {
               this.detachViewerPacket(var1, NativePacketsHolder.PE(), var1.kU());
               this.rV.despawnFor(var1);
               this.rV.spawnFor(var1);
            }

            this.sendInteractionSpawn(var1);
            this.rQ.forEach((var1x, var2x) -> var2x.updateFor(var1, true));
            this.sendPassengerPackets(var1);
            this.spawnNametagFor(var1);
            this.sendRemovePackets();
            this.scheduleEquipmentSync();
         }
      } else {
         this.hideViewer(var1);
      }
   }

   public void hideViewer(EntityViewer var1) {
      this.updateDisplayFor(var1);
      this.notifyEquipmentViewer(var1);
      if (this.rY != null) {
         this.rY.sendToViewer(var1);
      }

      if (!var1.uW) {
         this.despawnNametagFor(var1);
         this.clearNametagFor(var1);
         var1.uW = true;
         ConnectionRecord var2 = var1.kU();
         if (var1.uX) {
            this.detachViewerPacket(var1, NativePacketsHolder.PE(), var2);
         }

         if (var2 != null) {
            NativePacketsHolder.PE().method1871(var2, NativePacketsHolder.PE().Pv().removeEntities(this.collectEntityIds(var1.getRotationPolicy())));
         }

         this.sendRemovePackets();
      }
   }

   public void hideViewerFor(Player var1) {
      EntityViewer var2 = (EntityViewer)this.so.get(var1.getUniqueId());
      if (var2 != null) {
         this.updateDisplayFor(var2);
         if (!var2.uW) {
            this.hideViewer(var2);
         }
      }
   }

   public void hideAllViewers() {
      int var1 = 0;

      for (int var2 = this.sn.size(); var1 < var2; var1++) {
         EntityViewer var3 = (EntityViewer)this.sn.get(var1);
         this.updateDisplayFor(var3);
         if (this.rY != null) {
            this.rY.sendToViewer(var3);
         }

         this.notifyEquipmentViewer(var3);
         if (!var3.uW) {
            this.despawnNametagFor(var3);
            this.clearNametagFor(var3);
            var3.uW = true;
            ConnectionRecord var4 = var3.getConnection();
            if (var3.uX) {
               this.detachViewerPacket(var3, NativePacketsHolder.PE(), var4);
            }

            if (var4 != null) {
               NativePacketsHolder.PE().method1871(var4, NativePacketsHolder.PE().Pv().removeEntities(this.collectEntityIds(var3.getRotationPolicy())));
            }
         }
      }
   }

   void unregisterFromManager() {
      LivingEntity var1 = this.getBukkitEntity();
      this.cleanupLifecycle(this.getEntityManager(), var1 == null ? null : var1.getUniqueId(), NativePacketsHolder.aN() ? NativePacketsHolder.PE() : null);
   }

   void cleanupLifecycle(CustomEntityManager var1, @Nullable UUID var2, @Nullable NativePacketsBridge var3) {
      if (this.tryMarkRemoved()) {
         boolean var4 = var3 != null && !var3.HF();
         if (this.rW != null) {
            this.rW.close();
         }

         for (int var5 = 0; var5 < this.sn.size(); var5++) {
            EntityViewer var6 = (EntityViewer)this.sn.get(var5);
            if (this.rY != null) {
               this.rY.sendToViewer(var6);
            }

            this.removeNametagPacket(var6, var3);
            this.clearNametagPackets(var6, var3);
            var6.uW = true;
            ConnectionRecord var7 = var4 ? var6.method8554(var3) : null;
            this.detachViewerPacket(var6, var3, var7);
            if (var7 != null) {
               var3.method1871(var7, var3.Pv().removeEntities(this.collectEntityIds(var6.getRotationPolicy())));
            }
         }

         if (this.sm != null && var4) {
            this.sm.appendStateUpdate(this.sn, var3);
         }

         this.rC.aM();
         if (this.ry != null) {
            this.ry.close();
         }

         this.ry = null;
         this.rA = null;
         this.rw = false;
         var1.unregisterIds(this, var2);
         this.rQ.clear();
         this.sn.clear();
         this.so.clear();
         this.sq.clear();
         this.qI = null;
         this.sr.clear();
         this.rebuildViewerCaches();
         this.su.clear();
         this.resetNametagState();
         this.rd = null;
         this.ri.clear();
         this.ro.jU();
      }
   }

   public boolean if_() {
      return this.qU;
   }

   public void method8449(boolean var1) {
      this.qU = var1;
   }

   void createNametagDisplay() {
      if (!this.field106) {
         if (this.qX != null) {
            for (EntityViewer var2 : this.sn) {
               this.despawnNametagFor(var2);
            }
         }

         this.resetNametagState();
         this.qZ = this.computeNametagLocation();
         this.qX = this.qY;
         this.rb = NativePacketsHolder.PE().Pz().nativeEntityType(EntityType.TEXT_DISPLAY);
         NativeText var4 = NativePacketsHolder.PE().Px().literal(formatNametagText(this.qK.vn.AN && !this.qK.vn.AO, this.qK.vn.getNamespacedID()));
         this.ra = NativePacketsHolder.PE()
            .Pv()
            .metadata(
               this.qX.entityId(),
               new EntityMetadataField[]{Display.VIEW_RANGE, Display.BILLBOARD_RENDER_CONSTRAINTS, Display_TextDisplay.TEXT},
               new Object[]{(float)Math.min(Float.MAX_VALUE, Math.max(1.0, this.tc / 64.0)), (byte)3, var4::nativeHandle}
            );

         for (EntityViewer var3 : this.sn) {
            if (!var3.uW) {
               this.spawnNametagFor(var3);
            }
         }
      }
   }

   void clearNametagDisplay() {
      if (this.qX != null) {
         for (EntityViewer var2 : this.sn) {
            this.despawnNametagFor(var2);
         }
      }

      this.resetNametagState();
   }

   private void resetNametagState() {
      this.qX = null;
      this.qZ = null;
      this.ra = null;
      this.rb = null;
   }

   void updateNametagPosition() {
      DisplayEntityKey var1 = this.qX;
      if (var1 != null) {
         Location var2 = this.computeNametagLocation();
         this.qZ = var2;
         Object var3 = NativePacketsHolder.PE().Pv().teleport(var1.entityId(), var2.getX(), var2.getY(), var2.getZ(), 0.0F, 0.0F, false);

         for (int var4 = 0; var4 < this.sn.size(); var4++) {
            EntityViewer var5 = (EntityViewer)this.sn.get(var4);
            if (!var5.uW && var5.kW() && var5.kZ() && isSameLocation(var2, var5.uO)) {
               ConnectionRecord var6 = var5.getConnection();
               if (var6 != null) {
                  NativePacketsHolder.PE().method1871(var6, var3);
                  var5.uO = var2.clone();
               }
            }
         }
      }
   }

   private void spawnNametagFor(EntityViewer var1) {
      if (this.qX != null) {
         Object var2 = NativePacketsHolder.PE()
            .Pv()
            .addEntity(this.qX.entityId(), this.qX.worldId(), this.rb, this.qZ.getX(), this.qZ.getY(), this.qZ.getZ(), 0.0F, 0.0F, 0.0F, 0, 0.0, 0.0, 0.0);
         ConnectionRecord var3 = var1.getConnection();
         if (var3 != null) {
            NativePacketsHolder.PE().method1871(var3, var2);
            NativePacketsHolder.PE().method1871(var3, this.ra);
            var1.uO = this.qZ.clone();
         }
      }
   }

   private void despawnNametagFor(EntityViewer var1) {
      this.removeNametagPacket(var1, NativePacketsHolder.aN() ? NativePacketsHolder.PE() : null);
   }

   private void removeNametagPacket(EntityViewer var1, @Nullable NativePacketsBridge var2) {
      ConnectionRecord var3 = var2 == null ? null : var1.method8553(var2);
      if (var3 == null) {
         var3 = var2 == null ? null : var1.method8554(var2);
      }

      if (this.qX != null && var3 != null && !var2.HF()) {
         var2.method1871(var3, var2.Pv().removeEntities(new int[]{this.qX.entityId()}));
      }

      var1.uO = null;
   }

   private Location computeNametagLocation() {
      double[] var1 = computeNametagOffset(this.sB.getX(), this.sB.getY(), this.sB.getZ(), this.sx);
      return new Location(this.sB.getWorld(), var1[0], var1[1], var1[2]);
   }

   static double[] computeNametagOffset(double var0, double var2, double var4, float var6) {
      return new double[]{var0 - Math.sin(var6) * 1.25, var2 + 1.25, var4 + Math.cos(var6) * 1.25};
   }

   static String formatNametagText(boolean var0, String var1) {
      return (var0 ? "[V2 item_display] " : "[LEGACY] ") + var1;
   }

   static boolean isSameLocation(Location var0, @Nullable Location var1) {
      return !var0.equals(var1);
   }

   public void playAnimation(String var1) {
      this.playAnimationByName(var1, 0, null);
   }

   public void playAnim(String name, int repeatCount) {
      this.playAnimationByName(name, repeatCount, null);
   }

   public void startAnimation(ModelDefinitionData var1, int var2) {
      if (!CustomEntityManager.isOwnerThread()) {
         this.dispatchToManager(() -> this.startAnimation(var1, var2));
      } else if (var1 != null) {
         if (!this.rC.method8926(var1)) {
            this.rC.qT();
            this.rC.method8912(var1, var2, null);
         }
      }
   }

   public void playAnimationDefinition(@Nullable ModelDefinitionData var1) {
      if (!CustomEntityManager.isOwnerThread()) {
         this.dispatchToManager(() -> this.playAnimationDefinition(var1));
      } else {
         if (var1 != null) {
            this.startAnimation(this.qK.vn.method475(var1), 0);
         }
      }
   }

   public void startAnimationWithCallback(ModelDefinitionData var1, int var2, @Nullable Runnable var3) {
      if (!CustomEntityManager.isOwnerThread()) {
         this.dispatchToManager(() -> this.startAnimationWithCallback(var1, var2, var3));
      } else if (var1 == null) {
         if (var3 != null) {
            var3.run();
         }
      } else if (this.rC.method8926(var1)) {
         if (var3 != null) {
            var3.run();
         }
      } else {
         this.rC.qT();
         this.rC.method8912(var1, var2, var3);
      }
   }

   public void playAnimationByName(String var1, int var2, @Nullable Runnable var3) {
      this.playAnimationInternal(var1, var2, true, var3);
   }

   public void playAnimationWithCallback(String var1, @Nullable Runnable var2) {
      this.playAnimationInternal(var1, 0, true, var2);
   }

   public void playAnimationInternal(String var1, int var2, boolean var3, @Nullable Runnable var4) {
      if (!CustomEntityManager.isOwnerThread()) {
         this.dispatchToManager(() -> this.playAnimationInternal(var1, var2, var3, var4));
      } else if (!var3 || !this.rC.bl(var1)) {
         this.rC.qT();
         ModelDefinitionData var5 = (ModelDefinitionData)this.qK.vn.AK.get(var1);
         if (var5 == null) {
            if (var4 != null) {
               var4.run();
            }
         } else {
            this.rC.method8912(var5, var2, var4);
         }
      }
   }

   public void playAnimationIfChanged(ModelDefinitionData var1) {
      if (!CustomEntityManager.isOwnerThread()) {
         this.dispatchToManager(() -> this.playAnimationIfChanged(var1));
      } else if (var1 != null) {
         if (!this.qK.vn.of() || !this.sG || var1.yM != AnimationState.JUMP || this.rC.EU != var1) {
            if (!this.rC.bl(var1.yK)) {
               this.rC.qT();
               this.rC.method8912(var1, 0, null);
            }
         }
      }
   }

   public void queueAnimation(ModelDefinitionData var1) {
      if (!CustomEntityManager.isOwnerThread()) {
         this.dispatchToManager(() -> this.queueAnimation(var1));
      } else if (var1 != null) {
         if (!this.rC.method8926(var1)) {
            if (this.isAlive()) {
               this.rC.method8924(var1);
            }
         }
      }
   }

   public void queueAnimationByName(String var1) {
      ModelDefinitionData var2 = (ModelDefinitionData)this.qK.vn.AK.get(var1);
      this.queueAnimation(var2);
   }

   public void disableAllAnimationTicks() {
      if (!CustomEntityManager.isOwnerThread()) {
         this.dispatchToManager(this::disableAllAnimationTicks);
      } else {
         this.rQ.forEach((var1, var2) -> var2.disableAnimationTicks(this.sn));
      }
   }

   public void playDefaultDamageEffect() {
      this.playDamageEffect(false);
   }

   public void playDamageEffect(boolean fire) {
      if (!CustomEntityManager.isOwnerThread()) {
         this.dispatchToManager(() -> this.playDamageEffect(fire));
      } else {
         if (this.qK.vn.AN) {
            this.sn.forEach(EntityViewer::lc);
         }

         this.playSoundOnEntity(this.qK.wj);
         if (this.qK.vn.AN && !this.qK.vn.AO && this.rC != null) {
            ModelDefinitionData var2 = (ModelDefinitionData)this.qK.vn.AK.get("hurt");
            if (var2 != null) {
               this.startAnimation(var2, 0);
            } else {
               this.rC.qJ();
            }
         }

         if (!this.qK.vn.AM) {
            this.rQ.forEach((var2x, var3) -> {
               if (fire) {
                  var3.Hn.method3024(this.sn, true);
               }

               var3.enableAnimationTicks(this.sn);
            });
            if (!this.ru || this.rw) {
               SchedulerUtils.runLaterSync(() -> {
                  if (!this.removed && (!this.ru || this.rw)) {
                     this.disableAllAnimationTicks();
                  }
               }, 10L);
            }
         }
      }
   }

   public void refreshFireEffects() {
      if (!CustomEntityManager.isOwnerThread()) {
         this.dispatchToManager(this::refreshFireEffects);
      } else if (!this.qK.vn.AM) {
         this.rQ.forEach((var1, var2) -> {
            if (var2.Hm.getLivingEntity() instanceof LivingEntity var3 && var3.getFireTicks() > 0) {
               var2.Hn.method3024(this.sn, true);
            }

            var2.enableAnimationTicks(this.sn);
         });
      }
   }

   public String getNamespacedId() {
      return this.qK.vn.getNamespacedID();
   }

   public CustomEntityModel getModel() {
      return this.qK.vn;
   }

   public boolean iq() {
      return this.sv;
   }

   public boolean ir() {
      return this.sF;
   }

   public boolean isAlive() {
      if (CustomEntityManager.isOwnerThread()) {
         return this.sP;
      } else {
         return this.sl.xo == null ? true : this.sl.xo.isValid() && !this.sl.xo.isDead();
      }
   }

   public boolean isInViewRange(Player var1) {
      if (!var1.getWorld().equals(this.sl.resolveLocation().getWorld())) {
         return false;
      } else if (this.qO && !this.getModel().AN) {
         Vector var2 = var1.getLocation().getDirection();
         Vector var3 = this.sl.resolveLocation().subtract(var1.getLocation()).toVector().normalize();
         return var2.distance(var3) <= 1.75;
      } else {
         return true;
      }
   }

   public void refreshState() {
      this.captureState();
      if (this.ro.jX() == EntityLifecycleState.NEW || CustomEntityManager.isOwnerThread()) {
         this.applyStateSnapshot();
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   void updateBossBar() {
      if (!this.field106) {
         if (this.qK.vu && this.qK.vw != EntityType.WITHER) {
            LivingEntity var1 = this.getBukkitEntity();
            if (var1 != null && var1.isValid() && !var1.isDead()) {
               NativePacketsBridge var2 = NativePacketsHolder.PE();
               String var3 = var1.getCustomName();
               String var4 = var3 != null && !var3.isBlank() ? var3 : this.qK.vr;
               float var5 = (float)Math.max(0.0, Math.min(1.0, var1.getHealth() / Math.max(1.0, var1.getMaxHealth())));
               if (!var4.equals(this.tf)) {
                  this.tf = var4;
                  if (!this.te.isEmpty()) {
                     Object var6 = var2.Pv().bossBarName(this.td, var2.Px().literal(var4));

                     for (ConnectionRecord var8 : this.te.values()) {
                        var2.method1871(var8, var6);
                     }
                  }
               }

               if (var5 != this.tg) {
                  this.tg = var5;
                  if (!this.te.isEmpty()) {
                     Object var18 = var2.Pv().bossBarProgress(this.td, var5);

                     for (ConnectionRecord var22 : this.te.values()) {
                        var2.method1871(var22, var18);
                     }
                  }
               }

               if (this.th-- <= 0) {
                  this.th = 3;
                  Location var19 = var1.getLocation();
                  double var21 = this.qK.field108 == null ? this.tc : this.qK.field108;
                  double var9 = var21 * var21;
                  HashSet var11 = new HashSet(this.te.keySet());
                  BossBarColor var12 = BossBarColor.valueOf(this.qK.vy.name());

                  BossBarOverlay var13 = switch (SwitchMap88.tr[this.qK.vx.ordinal()]) {
                     case 1 -> BossBarOverlay.PROGRESS;
                     case 2 -> BossBarOverlay.NOTCHED_6;
                     case 3 -> BossBarOverlay.NOTCHED_10;
                     case 4 -> BossBarOverlay.NOTCHED_12;
                     case 5 -> BossBarOverlay.NOTCHED_20;
                     default -> throw new MatchException(null, null);
                  };

                  for (Player var15 : var19.getWorld().getPlayers()) {
                     if (var15.isOnline() && var15.getLocation().distanceSquared(var19) <= var9) {
                        UUID var16 = var15.getUniqueId();
                        ConnectionRecord var17 = var2.cB(var15);
                        if (var17 != null) {
                           var11.remove(var16);
                           if (this.te.put(var16, var17) != var17) {
                              var2.method1871(var17, var2.Pv().bossBarAdd(this.td, var2.Px().literal(var4), var5, var12, var13, false, false, false));
                           }
                        }
                     }
                  }

                  if (!var11.isEmpty()) {
                     Object var23 = var2.Pv().bossBarRemove(this.td);

                     for (UUID var25 : var11) {
                        var2.method1871((ConnectionRecord)this.te.remove(var25), var23);
                     }
                  }
               }
            } else {
               this.clearBossBar();
            }
         }
      }
   }

   void clearBossBar() {
      if (!this.te.isEmpty() && NativePacketsHolder.aN()) {
         NativePacketsBridge var1 = NativePacketsHolder.PE();
         if (!var1.HF()) {
            Object var2 = var1.Pv().bossBarRemove(this.td);

            for (ConnectionRecord var4 : this.te.values()) {
               var1.method1871(var4, var2);
            }
         }
      }

      this.te.clear();
      this.tf = null;
      this.tg = Float.NaN;
      this.th = 0;
   }

   @Nullable
   private static Attribute getMaxHealthAttribute() {
      Attribute var0 = (Attribute)Registry.ATTRIBUTE.get(qF);
      return var0 == null ? (Attribute)Registry.ATTRIBUTE.get(qG) : var0;
   }

   public void updateNametag() {
      if (!this.field106) {
         LivingEntity var1 = this.getBukkitEntity();
         if (var1 != null && var1.isValid() && !var1.isDead() && !CustomEntityManager.getInstance().isNametagsEnabled()) {
            String var2 = var1.getCustomName();
            String var3 = resolveNametagName(this.qK, var2);
            if (var2 != null && var1.isCustomNameVisible()) {
               var1.setCustomNameVisible(false);
            }

            double var4 = 1.0;
            Attribute var6 = getMaxHealthAttribute();
            AttributeInstance var7 = var6 == null ? null : var1.getAttribute(var6);
            if (var7 != null && Double.isFinite(var7.getValue()) && var7.getValue() > 0.0) {
               var4 = var7.getValue();
            }

            if (this.qK.vU
               && this.qK.vn.of()
               && var1 instanceof Mob
               && var1.getPersistentDataContainer().get(CustomEntityManager.oC, PersistentDataType.STRING) == null) {
               var4 *= ProjectileTypeHelper.getAdapter().method3250(var1);
            }

            double var8 = var1.getHeight();
            if (this.qK.vt != null) {
               var8 = this.qK.vt * var4;
            } else if (this.qK.vn.Bk != null) {
               var8 = Math.max(var8, this.qK.vn.Bk.y * var4) + Math.max(0.2, Math.min(0.5, this.qK.vn.Bk.x * 0.25)) * var4;
            } else {
               var8 += 0.25 * var4;
            }

            float var10 = (float)Math.max(-Float.MAX_VALUE, Math.min(Float.MAX_VALUE, var8));
            this.setNametagLine(var3 == null ? null : Component.text(var3), var10, !var1.getPassengers().isEmpty());
         } else {
            this.clearNametagLine();
         }
      }
   }

   void setNametagLine(@Nullable Component var1, float var2, boolean var3) {
      Component var4 = this.rc.requestedText(var1);
      NametagLine var5 = this.rd;
      Component var6 = var5 != null && Objects.equals(var5.background(), var1) && Objects.equals(var5.kI(), var4)
         ? var5.text()
         : CustomEntity.internal_prepareNametag(this, var1, var4);
      this.rd = new NametagLine(var1, var4, var6, var2, var3);
   }

   @Nullable
   public Component resolveNametagText(@Nullable Component var1) {
      NametagLine var2 = this.rd;
      return var2 != null && Objects.equals(var2.kI(), var1) ? var2.text() : var1;
   }

   @Nullable
   public Component buildNametagComponent() {
      if (!this.field106 && !this.removed && this.sl != null) {
         LivingEntity var1 = this.getBukkitEntity();
         if (var1 != null && var1.isValid() && !var1.isDead() && !CustomEntityManager.getInstance().isNametagsEnabled()) {
            String var2 = resolveNametagName(this.qK, var1.getCustomName());
            return var2 == null ? null : Component.text(var2);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   @Nullable
   static String resolveNametagName(CustomMobSettings var0, @Nullable String var1) {
      boolean var2 = (var0.vw == EntityType.WITHER || var0.vw == EntityType.ENDER_DRAGON) && Objects.equals(var1, var0.vr);
      return var1 != null && !var1.isBlank() && !var2 ? var1 : (var0.vs ? var0.vr : null);
   }

   public void setNametagHiddenFor(Player var1, int[] var2) {
      UUID var3 = var1.getUniqueId();
      boolean var4 = var2.length > 0;
      this.dispatchToManager(() -> {
         if (var4) {
            this.ri.add(var3);
         } else {
            this.ri.remove(var3);
         }

         EntityViewer var3x = (EntityViewer)this.so.get(var3);
         if (var4 && var3x != null) {
            this.clearNametagFor(var3x);
         }
      });
   }

   void refreshNametagDisplay() {
      NametagLine var1 = this.rd;
      Snapshot var2 = var1 == null ? null : this.rc.renderSnapshot(var1.text());
      if (var2 != null && var2.text() != null && var2.visible() && NativePacketsHolder.aN()) {
         DisplayEntityKey var18 = this.re;
         DisplayEntityKey var19 = this.rf;
         if (var18 != null && var19 != null) {
            NativePacketsBridge var5 = NativePacketsHolder.PE();
            if (!var5.HF()) {
               Object var6 = this.rg != null && this.rg.teleportDuration() != var2.teleportDuration()
                  ? var5.Pv().metadata(var18.entityId(), Display.POS_ROT_INTERPOLATION_DURATION, var2.teleportDuration())
                  : null;
               Object var7 = null;
               if (!var2.equals(this.rg)) {
                  var7 = this.buildNametagMetadata(var5, var19.entityId(), var2);

                  for (EntityViewer var9 : this.sn) {
                     if (var9.uP && var9.getConnection() != null) {
                        var5.method1871(var9.getConnection(), var7);
                     }
                  }

                  this.rg = var2;
               }

               Location var20 = this.sB.clone().add(0.0, var1.kJ() + var2.offsetY(), 0.0);
               Location var21 = this.rh;
               boolean var10 = var21 == null
                  || var20.getWorld() != var21.getWorld()
                  || var20.getX() != var21.getX()
                  || var20.getY() != var21.getY()
                  || var20.getZ() != var21.getZ();
               Object var11 = null;
               double var12 = var2.viewRange() == null ? 32.0 : Math.min(this.tc, var2.viewRange().floatValue() * 64.0);

               for (EntityViewer var15 : this.sn) {
                  boolean var16 = !var1.kK() && !this.ri.contains(var15.uz) && !var15.uW && var15.kW() && var15.uQ >= 0.0 && var15.uQ < var12;
                  if (!var16) {
                     this.clearNametagPackets(var15, var5);
                  } else if (!var15.uP) {
                     if (var7 == null) {
                        var7 = this.buildNametagMetadata(var5, var19.entityId(), var2);
                     }

                     this.spawnNametagPackets(var15, var5, var2, var7, var20, var18, var19);
                  } else {
                     ConnectionRecord var17 = var15.method8553(var5);
                     if (var17 != null) {
                        if (var6 != null) {
                           var5.method1871(var17, var6);
                        }

                        if (var10) {
                           if (var11 == null) {
                              var11 = var5.Pv().teleport(var18.entityId(), var20.getX(), var20.getY(), var20.getZ(), 0.0F, 0.0F, false);
                           }

                           var5.method1871(var17, var11);
                        }
                     }
                  }
               }

               if (var10) {
                  this.rh = var20.clone();
               }
            }
         }
      } else {
         for (EntityViewer var4 : this.sn) {
            this.clearNametagFor(var4);
         }

         this.rg = null;
         this.rh = null;
      }
   }

   private void spawnNametagPackets(
      EntityViewer var1, NativePacketsBridge var2, Snapshot var3, Object var4, Location var5, DisplayEntityKey var6, DisplayEntityKey var7
   ) {
      ConnectionRecord var8 = var1.method8553(var2);
      if (var8 != null) {
         NativePacketFactory var9 = var2.Pv();
         var2.method1871(
            var8,
            var9.addEntity(
               var6.entityId(),
               var6.worldId(),
               var2.Pz().nativeEntityType(EntityType.ITEM_DISPLAY),
               var5.getX(),
               var5.getY(),
               var5.getZ(),
               0.0F,
               0.0F,
               0.0F,
               0,
               0.0,
               0.0,
               0.0
            )
         );
         var2.method1871(
            var8,
            var9.metadata(
               var6.entityId(),
               new EntityMetadataField[]{Display.VIEW_RANGE, Display.POS_ROT_INTERPOLATION_DURATION},
               new Object[]{(float)Math.min(Float.MAX_VALUE, Math.max(1.0, this.tc / 64.0)), var3.teleportDuration()}
            )
         );
         var2.method1871(
            var8,
            var9.addEntity(
               var7.entityId(),
               var7.worldId(),
               var2.Pz().nativeEntityType(EntityType.TEXT_DISPLAY),
               var5.getX(),
               var5.getY(),
               var5.getZ(),
               0.0F,
               0.0F,
               0.0F,
               0,
               0.0,
               0.0,
               0.0
            )
         );
         var2.method1871(var8, var4);
         var2.method1871(var8, var9.passengers(var6.entityId(), new int[]{var7.entityId()}));
         var1.uP = true;
      }
   }

   private Object buildNametagMetadata(NativePacketsBridge var1, int var2, Snapshot var3) {
      Transformation var4 = var3.transformation();
      return var1.Pv()
         .metadata(
            var2,
            new EntityMetadataField[]{
               Display_TextDisplay.TEXT,
               Display_TextDisplay.LINE_WIDTH,
               Display_TextDisplay.BACKGROUND_COLOR,
               Display_TextDisplay.TEXT_OPACITY,
               Display_TextDisplay.STYLE_FLAGS,
               Display.BILLBOARD_RENDER_CONSTRAINTS,
               Display.TRANSLATION,
               Display.LEFT_ROTATION,
               Display.SCALE,
               Display.RIGHT_ROTATION,
               Display.TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS,
               Display.TRANSFORMATION_INTERPOLATION_DURATION,
               Display.POS_ROT_INTERPOLATION_DURATION,
               Display.VIEW_RANGE,
               Display.SHADOW_RADIUS,
               Display.SHADOW_STRENGTH,
               Display.WIDTH,
               Display.HEIGHT,
               Display.BRIGHTNESS_OVERRIDE,
               Display.GLOW_COLOR_OVERRIDE,
               beer.devs.nativepackets.api.EntityMetadataFields.Entity.SHARED_FLAGS
            },
            new Object[]{
               (NativeValue)() -> AdventureText.toNative(var3.text()),
               var3.lineWidth(),
               var3.backgroundColor(),
               var3.textOpacity(),
               var3.flags(),
               var3.billboard(),
               var4::getTranslation,
               var4::getLeftRotation,
               var4::getScale,
               var4::getRightRotation,
               var3.interpolationDelay(),
               var3.interpolationDuration(),
               var3.teleportDuration(),
               var3.viewRange() == null ? (float)Math.min(Float.MAX_VALUE, Math.max(1.0, this.tc / 64.0)) : var3.viewRange(),
               var3.shadowRadius(),
               var3.shadowStrength(),
               var3.displayWidth(),
               var3.displayHeight(),
               var3.brightness(),
               var3.glowColor(),
               (byte)(var3.glowing() ? 64 : 0)
            }
         );
   }

   private void clearNametagFor(EntityViewer var1) {
      this.clearNametagPackets(var1, NativePacketsHolder.aN() ? NativePacketsHolder.PE() : null);
   }

   private void clearNametagPackets(EntityViewer var1, @Nullable NativePacketsBridge var2) {
      if (var1.uP) {
         var1.uP = false;
         if (var2 != null && !var2.HF() && this.re != null && this.rf != null) {
            ConnectionRecord var3 = var1.kU();
            if (var3 != null) {
               var2.method1871(var3, var2.Pv().removeEntities(new int[]{this.re.entityId(), this.rf.entityId()}));
            }
         }
      }
   }

   void clearNametagLine() {
      this.rd = null;
   }

   public void captureState() {
      if (this.rY != null) {
         this.rY.tick();
      }

      Location var1 = this.sl.resolveLocation().clone();
      World var2 = var1.getWorld();
      ModelPhysicsLoop var3 = this.ry;
      if (var3 != null && var2 != null) {
         var3.updateCollisionShape(var2);
      }

      LivingEntity var4 = this.sl.xo;
      if (var4 != null) {
         this.updateRemovePacket(var4);
      }

      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      boolean var9 = false;
      boolean var10 = false;
      boolean var11 = false;
      boolean var12 = false;
      boolean var13 = false;
      boolean var14 = false;
      boolean var15 = false;
      int var16 = 0;
      float var17 = 0.0F;
      boolean var18 = var4 == null || var4.isValid() && !var4.isDead();
      float var19 = (float)Math.toRadians(var1.getYaw());
      float var20 = (float)Math.toRadians(var1.getPitch());
      float var21 = var19;
      float var22 = var21;
      VisibilityFlags var23 = null;
      DoubleVector3Record var24 = null;
      double var25 = 0.0;
      if (var4 != null && var4.isValid()) {
         ThrowInputState var27 = var4 instanceof Player var28 ? (ThrowInputState)ItemThrowManager.afA.get(var28.getUniqueId()) : null;
         if (this.ti != null) {
            var5 = this.ti.iq();
            var19 = (float)Math.toRadians(this.ti.Nn());
            var21 = (float)Math.toRadians(this.ti.No());
         } else {
            var5 = var27 != null ? var27.iq() : Math.abs(var4.getVelocity().getX()) > 0.0 || Math.abs(var4.getVelocity().getZ()) > 0.0;
            if (!(var4 instanceof Player)) {
               var19 = (float)Math.toRadians(ProjectileTypeHelper.getAdapter().method3236(var4));
               var21 = var4 instanceof ArmorStand ? var19 : (float)Math.toRadians(ProjectileTypeHelper.getAdapter().ab(var4));
            }
         }

         var22 = !(var4 instanceof Player) && !(var4 instanceof ArmorStand) ? (float)Math.toRadians(ProjectileTypeHelper.getAdapter().ab(var4)) : var21;
         boolean var32 = this.countMountedRiders() > 0;
         if (var32 || this.qK.vL && this.hasRiders()) {
            var21 = var22 = (float)Math.toRadians(ProjectileTypeHelper.getAdapter().method3235(var4));
            if (var32) {
               var19 = var21;
            }
         }

         var9 = var4.isInWater();
         var6 = var4.getPose() == Pose.SWIMMING;
         var7 = var4.isGliding();
         var8 = var4.isOnGround();
         var10 = ProjectileTypeHelper.getAdapter().method3276(var4);
         var11 = ProjectileTypeHelper.getAdapter().ah(var4);
         var12 = var4 instanceof Mob var29 && ProjectileTypeHelper.getAdapter().method3257(var29);
         var16 = var4.getTicksLived();
         var17 = this.qK.vn.of() ? ProjectileTypeHelper.getAdapter().aj(var4) : ProjectileTypeHelper.getAdapter().ai(var4);
         var15 = Main.aFc && CitizensHook.isNPC(var4);
         var25 = var4.getVelocity().getY();
         Location var35 = ProjectileTypeHelper.getAdapter().method3275(var4);
         if (var35 != null) {
            var24 = new DoubleVector3Record(var35.getX(), var35.getY(), var35.getZ());
         }

         if (var4 instanceof Player var30) {
            var13 = CustomBlockListener.dn.method721(var30);
            var14 = var30.isSneaking();
            if (var27 != null) {
               var23 = new VisibilityFlags(var27.kL(), var27.kM(), var27.kN(), var27.kO());
            }
         }
      }

      float var31 = 1.0F;
      if (var4 instanceof Player var33 && (this.ta || this.tb) && Settings.get().emotesMatchPlayerScale) {
         Attribute var36 = getMaxHealthAttribute();
         AttributeInstance var37 = var36 == null ? null : var33.getAttribute(var36);
         if (var37 != null && Double.isFinite(var37.getValue()) && var37.getValue() > 0.0 && var37.getValue() <= Float.MAX_VALUE) {
            var31 = (float)var37.getValue();
         }
      } else if (var4 instanceof Mob
         && this.qK.vU
         && this.qK.vn.of()
         && var4.getPersistentDataContainer().get(CustomEntityManager.oC, PersistentDataType.STRING) == null) {
         var31 = ProjectileTypeHelper.getAdapter().method3250(var4);
      }

      if (this.rC != null && this.qK.vn.nW() != null && !this.field106) {
         this.rC
            .method8907(
               var2,
               var1,
               var18
                  && var4 != null
                  && var8
                  && !var10
                  && !var7
                  && !var6
                  && !var9
                  && !var4.isSwimming()
                  && !var4.isInsideVehicle()
                  && Math.abs(var25) < 0.15
                  && !this.isRagdolling()
                  && this.getDeathFadeAngle() == 0.0F
            );
      }

      this.sQ = new EntityStateSnapshot(
         var2 == null ? null : var2.getUID(),
         var1.getX(),
         var1.getY(),
         var1.getZ(),
         var1.getYaw(),
         var1.getPitch(),
         var5,
         var19,
         var20,
         var21,
         var20,
         var22,
         var6,
         var7,
         var8,
         var9,
         var10,
         var11,
         var12,
         var16,
         var17,
         var13,
         var14,
         var15,
         var18,
         var23,
         var25,
         var4 instanceof Player,
         var24,
         var4 instanceof Player ? var4.getHeight() : 0.0,
         var31,
         var4 == null ? 0.0F : (float)var4.getWidth(),
         var4 == null ? 0.0F : (float)var4.getHeight(),
         var4 instanceof Player var34 && var34.isBlocking()
      );
   }

   public boolean hasVisibleViewerWithin(double var1) {
      for (EntityViewer var4 : this.field107) {
         ViewerVisibility var5 = var4.uV;
         if (var5 != null && var5.visible() && var5.fm() >= 0.0 && var5.fm() <= var1) {
            return true;
         }
      }

      return false;
   }

   public void applyStateSnapshot() {
      EntityStateSnapshot var1 = this.sQ;
      if (var1 != null) {
         float var2 = (float)var1.kC();
         if (this.to != var2) {
            this.to = var2;

            for (EntityViewer var4 : this.sn) {
               if (var4.uX) {
                  var4.lb();
               }
            }
         }

         boolean var5 = Objects.equals(this.sR, var1.uuid());
         this.sR = var1.uuid();
         this.tp = var1.kD();
         this.updateInteractionSize(this.tp, var1.kE(), var1.kF());
         this.sB = var1.toLocation();
         this.sv = var1.kg();
         if (!this.ru || !this.rw) {
            this.sw = var1.kh();
            this.sy = var1.ki();
            this.sx = this.computeYawRadians(var1);
         }

         this.sz = var1.method26();
         this.sA = var1.kA() ? this.sx : var1.kl();
         this.sC = var1.km();
         this.sD = var1.kn();
         this.sE = var1.ko();
         if (this.qK.vn.of()) {
            this.sG = var1.kx()
               && !var1.kn()
               && !var1.km()
               && !var1.ko()
               && !var1.onGround()
               && var5
               && (this.sG || var1.kz() > 0.05 && (this.sF || var1.kp()));
         } else {
            this.sG = var1.kp() && !var1.ko();
         }

         this.sF = var1.onGround();
         this.sH = var1.kq();
         this.sI = var1.kr();
         this.sJ = var1.ks();
         this.sL = var1.kt();
         this.sM = var1.ku();
         this.sN = var1.kv();
         this.sO = var1.kw();
         this.sP = var1.kx();
         this.updateLagCompensation(var1);
         if (this.rq != null) {
            this.rq.mj();
            this.sR = this.rq.xp.Hm.getEntityUuid();
         }
      }
   }

   private float computeYawRadians(EntityStateSnapshot var1) {
      if (!var1.kA()) {
         this.sX = null;
         return var1.kj();
      }

      EntityStateSnapshot var2 = this.sX;
      if (var2 == var1) {
         return this.sx;
      }

      this.sX = var1;
      float var3 = var1.yaw();
      if (var2 != null && Objects.equals(var2.uuid(), var1.uuid())) {
         float var4 = (float)Math.toDegrees(this.sx);
         float var5 = var4;
         double var6 = var1.x() - var2.x();
         double var8 = var1.z() - var2.z();
         if (var6 * var6 + var8 * var8 > 0.0025000002) {
            float var10 = (float)Math.toDegrees(Math.atan2(var8, var6)) - 90.0F;
            float var11 = Math.abs(BlockFaceUtils.normalizeYaw(var3 - var10));
            var5 = var11 > 95.0F ? var10 - 180.0F : var10;
         }

         if (var1.kt() > 0.0F) {
            var5 = var3;
         }

         var4 += BlockFaceUtils.normalizeYaw(var5 - var4) * 0.3F;
         float var14 = BlockFaceUtils.normalizeYaw(var3 - var4);
         float var15 = var1.kG() ? 15.0F : 50.0F;
         var4 += var14 - Math.max(-var15, Math.min(var15, var14));
         return (float)Math.toRadians(var4);
      } else {
         return (float)Math.toRadians(var3);
      }
   }

   private void updateLagCompensation(EntityStateSnapshot var1) {
      if (Settings.get().disguisesLagCompensation && this.sZ && this.rC != null && this.rC.EU != null) {
         if (!var1.onGround() && !this.rC.EU.zJ && !this.rC.EU.zC) {
            this.tk = this.tl = this.tj = 0.0;
         } else {
            VisibilityFlags var2 = var1.ky();
            if (var2 != null) {
               if (this.sW && Objects.equals(this.sS, var1.uuid())) {
                  double var3 = var1.x() - this.sT;
                  double var5 = var1.y() - this.sU;
                  double var7 = var1.z() - this.sV;
                  this.tm = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7) * 20.0;
               }

               this.sS = var1.uuid();
               this.sT = var1.x();
               this.sU = var1.y();
               this.sV = var1.z();
               this.sW = true;
               double var11 = this.tm / 20.0;
               double var12 = var2.kL() ? var11 : (var2.kM() ? -var11 : 0.0);
               double var13 = var2.kN() ? var11 : (var2.kO() ? -var11 : 0.0);
               double var9 = Math.abs(var1.kz() - -0.0784000015258789) > 0.001 ? var1.kz() : 0.0;
               this.tk = this.tk + (var12 - this.tk) * 0.2;
               this.tl = this.tl + (var13 - this.tl) * 0.2;
               this.tj = this.tj + (var9 - this.tj) * 0.6;
            }
         }
      }
   }

   public float getBodyYaw() {
      return this.sx;
   }

   public float iE() {
      return this.sz;
   }

   public float iF() {
      return this.sA;
   }

   public float iG() {
      return this.sw;
   }

   public float iH() {
      return this.sy;
   }

   @Nullable
   public ModelDefinitionData getModelDefinition(String var1) {
      return (ModelDefinitionData)this.qK.vn.AK.get(var1);
   }

   public boolean isCurrentAnimationActive() {
      return this.rC.EU == null
         ? false
         : this.rC.method8904(this.rC.EU)
            || Objects.equals(this.getIdleAnimation(), this.rC.EU)
            || Objects.equals(this.getWalkAnimation(), this.rC.EU)
            || Objects.equals(this.getFlyAnimation(), this.rC.EU)
            || Objects.equals(this.getJumpAnimation(), this.rC.EU);
   }

   public boolean hasRiders() {
      return !this.rP.isEmpty();
   }

   @Nullable
   public ModelRiderController getFreeRider() {
      for (ModelRiderController var2 : this.rP) {
         if (!var2.isMounted()) {
            return var2;
         }
      }

      return null;
   }

   public int countMountedRiders() {
      int var1 = 0;

      for (ModelRiderController var3 : this.rP) {
         if (var3.isMounted()) {
            var1++;
         }
      }

      return var1;
   }

   @Deprecated
   public int[] getMountedEntityIds() {
      ArrayList var1 = new ArrayList();

      for (ModelRiderController var3 : this.rP) {
         if (var3.isMounted()) {
            var1.add(var3.MZ.getEntityId());
         }
      }

      return var1.stream().mapToInt(Integer::intValue).toArray();
   }

   public void removeFromManager() {
      CustomEntityManager.getInstance().unmountAllRiders(this);
   }

   @Nullable
   public ModelRiderController getRiderFor(String var1) {
      ModelPartDisplay var2 = (ModelPartDisplay)this.rQ.get(var1.toLowerCase());
      return var2 == null ? null : var2.getRider();
   }

   public boolean isAirborneAttached() {
      return this.sl != null && this.sl.xo != null && !this.sF;
   }

   public boolean isProjectileShooter() {
      return this.sl.xo != null && ProjectileTypeHelper.method3224(this.sl.xo) && this.qK.vC != null && this.qK.vC > 0.0F;
   }

   void killEntity() {
      if (this.sl.xo != null) {
         this.sl.xo.damage(this.sl.xo.getMaxHealth() + 1.0);
         this.unregisterFromManager();
      }
   }

   void removeWithParticles(boolean var1, boolean var2) {
      if (this.sl.xo != null && this.qK.vn.AN && !this.qK.vn.AO) {
         this.sl.xo.getPersistentDataContainer().remove(qH);
      }

      if (this.sl.xo != null && this.sj && !var2) {
         this.sl.xo.remove();
      }

      if (var1 && (this.sl.xo != null && this.sj || this.rw)) {
         Location var3 = this.sl.resolveLocation();
         DoubleVector3Dup2 var4 = this.getRagdollPosition();
         if (var4 != null) {
            var3 = new Location(var3.getWorld(), var4.getX(), var4.getY(), var4.getZ());
         }

         for (EntityViewer var6 : this.sn) {
            var6.li
               .spawnParticle(
                  Particle.POOF, var3, 8, Utils.randomFloat(0.02F, 0.3F), 1.0F + Utils.randomFloat(0.02F, 0.3F), Utils.randomFloat(0.02F, 0.3F), 0.0
               );
         }
      }
   }

   public List getAnimationsNames() {
      return this.qK.vn.getAnimationsNames();
   }

   public boolean isPastSpawnGrace() {
      if (this.qN) {
         return true;
      }

      this.qN = System.currentTimeMillis() - this.qM > 1000L;
      return this.qN;
   }

   public void pushMovementUpdate() {
      if (this.sm != null) {
         if (this.getModel().AN) {
            this.sm.Qf();
         }

         this.sm.appendMove(this.sB.getX(), this.sB.getY(), this.sB.getZ(), this.sn);
         if (this.getModel().AN) {
            this.sm.appendRemove(this.sn);
         }
      }
   }

   public void resyncDisplays() {
      if (this.sm != null) {
         for (EntityViewer var2 : this.sn) {
            this.spawnDisplayFor(var2);
         }
      }
   }

   private void spawnDisplayFor(EntityViewer var1) {
      if (this.sm != null && var1.uL && !var1.uW && var1.kW() && var1.getConnection() != null) {
         this.sm.appendSpawn(List.of(var1));
         var1.uL = false;
      }
   }

   private void updateDisplayFor(EntityViewer var1) {
      if (this.sm != null && !var1.uL) {
         this.sm.appendUpdate(List.of(var1));
         var1.uL = true;
      }
   }

   public void teleport(Location location) {
      this.sl.setLocation(location.clone());
      this.refreshState();
      if (CustomEntityManager.isOwnerThread()) {
         this.rC.qM();
      } else {
         this.dispatchToManager(this.rC::qM);
      }
   }

   public void updateViewDistance() {
      if (this.qK.vB != null) {
         this.tc = this.qK.vB;
      } else if (this.sl == null) {
         this.tc = 60.0;
      } else {
         this.tc = Math.min(70, Math.max(16, this.getWorld().getViewDistance() * 16));
      }
   }

   public double getViewDistance() {
      return this.tc;
   }

   @Nullable
   public ModelDefinitionData getCurrentAnimation() {
      return this.rC.EU;
   }

   public void markDying(int var1) {
      this.rv = var1;
      this.ru = true;
      if (this.rX != null) {
         this.sa = false;
         this.dispatchToManager(() -> {
            Object var1x = NativePacketsHolder.PE().Pv().removeEntities(new int[]{this.rX.entityId()});

            for (EntityViewer var3 : this.sn) {
               ConnectionRecord var4 = var3.kU();
               if (var4 != null) {
                  NativePacketsHolder.PE().method1871(var4, var1x);
               }
            }
         });
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public float getDeathFadeAngle() {
      if (this.qK.vn.of() && this.ru && this.qK.we && this.rH != null && !this.rw) {
         float var1 = switch (SwitchMap88.ts[this.qK.vw.ordinal()]) {
            case 1, 2, 3, 4 -> 180.0F;
            default -> 90.0F;
         };
         return computeDeathAngleCapped(Math.max(0, this.sJ - this.rv), var1);
      } else {
         return 0.0F;
      }
   }

   public static float computeDeathAngle(int var0) {
      return computeDeathAngleCapped(var0, 90.0F);
   }

   public static float computeDeathAngleCapped(int var0, float var1) {
      return (float)Math.toRadians(var1 * Math.min(1.0, Math.sqrt(Math.max(0, var0 - 1) * 1.6 / 20.0)));
   }

   public void playDeathThen(Runnable var1) {
      if (this.qK.we && this.rH != null) {
         if (this.qK.vn.of()) {
            this.startAnimationWithCallback(this.qK.vn.method475(this.rH), 0, null);
            SchedulerUtils.runLaterSync(var1, 20L);
         } else {
            this.startAnimationWithCallback(this.rH, 0, var1);
         }
      } else {
         var1.run();
      }
   }

   public boolean tryRagdoll(LivingEntity var1, Runnable var2) {
      return this.tryRagdollAt(var1, null, var2);
   }

   public boolean tryRagdollAt(LivingEntity var1, Location var2, Runnable var3) {
      boolean var4 = this.qK.wd || this.rx;
      this.rx = false;
      if (var4 && this.canRagdoll()) {
         Location var5 = var1.getLocation();
         double var6 = 0.0;
         double var8 = 0.0;
         if (var2 != null && var5.getWorld().equals(var2.getWorld())) {
            var6 = var5.getX() - var2.getX();
            var8 = var5.getZ() - var2.getZ();
         }

         return this.startRagdollSimulation(var5, var1.getVelocity(), var3, false, var6, var8);
      } else {
         return false;
      }
   }

   public boolean markRagdollPending() {
      if (!this.ru && this.canRagdoll()) {
         this.rx = true;
         return true;
      } else {
         return false;
      }
   }

   private boolean canRagdoll() {
      return !this.field106
         && PluginLibraryManager.isJoltLoaded()
         && !this.qK.vn.AM
         && this.qK.vn.of()
         && this.qK.vn.nY() != null
         && !this.qK.vn.AL.isEmpty()
         && this.qK.vn.AL.size() <= 128
         && !this.jE()
         && !this.removed
         && !this.rw;
   }

   public boolean startRagdollDetached(Location var1, Vector var2, Runnable var3) {
      return PluginLibraryManager.isJoltLoaded()
            && this.qK.vn.AM
            && this.qK.vn.AN
            && this.qK.vn.nY() != null
            && this.qK.vn.of()
            && !this.qK.vn.AL.isEmpty()
            && this.qK.vn.AL.size() <= 128
            && this.qP
            && !this.removed
            && !this.rw
         ? this.startRagdollSimulation(var1, var2, var3, true, 0.0, 0.0)
         : false;
   }

   private boolean startRagdollSimulation(Location var1, Vector var2, Runnable var3, boolean var4, double var5, double var7) {
      VoxelCollisionShape var9 = VoxelCollisionShape.method8991(var1.getWorld(), var1.getX(), var1.getY(), var1.getZ());
      this.rw = true;
      this.rA = var3;
      NativeValue var10 = this.usesHumanoidEquipment() ? NativePacketsHolder.PE().Py().wrapBukkitItem(new ItemStack(Material.AIR)) : null;
      this.getEntityManager().submitToOwner(this, () -> {
         try {
            this.ry = new ModelPhysicsLoop(this, var1.getX(), var1.getY(), var1.getZ(), var2.getX(), var2.getY(), var2.getZ(), var9, var1.getYaw(), var5, var7);
            if (var10 != null) {
               for (ModelPartDisplay var14 : new ModelPartDisplay[]{this.sg, this.sf, this.sh}) {
                  if (var14 != null) {
                     var14.sendNativeValue(var10, false);
                  }
               }
            }
         } catch (RuntimeException var15) {
            this.rA = null;
            Log.warn("Ragdoll disabled for " + this.qK.getNamespacedId() + ": " + var15.getMessage());
            SchedulerUtils.runOnMainThread(() -> {
               if (!this.removed) {
                  this.rw = false;
                  if (var4) {
                     var3.run();
                  } else {
                     var3.run();
                  }
               }
            });
         }
      });
      return true;
   }

   public boolean isRagdolling() {
      return this.rw;
   }

   public boolean isRagdollParked() {
      ModelPhysicsLoop var1 = this.ry;
      return var1 != null && var1.isParked();
   }

   public boolean isRagdollSettled() {
      ModelPhysicsLoop var1 = this.ry;
      return var1 != null && var1.isSettled();
   }

   @Nullable
   public DoubleVector3Dup2 getRagdollPosition() {
      ModelPhysicsLoop var1 = this.ry;
      return var1 == null ? this.rz : var1.getPosition();
   }

   public void tickRagdoll(long var1, EntityBoundsSnapshot[] var3) {
      ModelPhysicsLoop var4 = this.ry;
      if (var4 == null) {
         this.tickEquipment();
      } else {
         try {
            if (var4.tickSimulationWithBounds(this, var1, var3, this.sR)) {
               return;
            }
         } catch (RuntimeException var6) {
            Log.warn("Ragdoll stopped for " + this.qK.getNamespacedId(), var6);
         }

         this.rz = var4.getPosition();
         var4.close();
         this.ry = null;
         Runnable var5 = this.rA;
         this.rA = null;
         if (var5 != null) {
            SchedulerUtils.runOnMainThread(() -> {
               if (!this.removed) {
                  var5.run();
               }
            });
         }
      }
   }

   public void playDeathSound() {
      this.playSoundOnEntity(this.qK.wk);
   }

   public void playAttackSound() {
      this.playSoundOnEntity(this.qK.wi);
   }

   public void tickMobAttribute() {
      if (!this.field106) {
         MobAttributeEntry var1 = this.qK.wh;
         if (var1 != null) {
            double var2 = var1.amount() / 100.0;
            if (Utils.random.nextDouble() < var2 * this.rB++) {
               this.rB = -var1.operation();
               UUID var4 = this.sR;
               double var5 = this.sB.getX();
               double var7 = this.sB.getY();
               double var9 = this.sB.getZ();
               SchedulerUtils.runOnMainThread(() -> {
                  World var8 = var4 == null ? null : Bukkit.getWorld(var4);
                  if (var8 != null) {
                     SoundUtils.playSound(new Location(var8, var5, var7, var9), var1.name(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
                  }
               });
            }
         }
      }
   }

   private void playSoundOnEntity(String var1) {
      if (!this.field106) {
         if (var1 != null) {
            SoundUtils.playSound(this.getBaseLocation(), var1, SoundCategory.NEUTRAL, 1.0F, 1.0F);
         }
      }
   }

   public boolean isFlyingProjectile() {
      return this.getBukkitEntity() == null ? false : ProjectileTypeHelper.getAdapter().ah(this.getBukkitEntity());
   }

   @Nullable
   public ModelDefinitionData resolveCurrentAnimation() {
      if (this.sE && this.rJ != null) {
         return this.rJ;
      }

      if (this.sG && this.rL != null) {
         return this.rL;
      }

      if (this.isAirborneAttached() && (!this.qK.vL || !this.sE)) {
         if (this.rI != null) {
            return this.rI;
         }

         if (!this.qK.vn.of()) {
            return this.rE;
         }

         if (this.sG || this.iq() || this.rC != null && this.rC.EU != null && this.rC.EU.isWalkingState() && this.rC.EU.yM != AnimationState.SWIM) {
            return this.sH && this.rK != null ? this.rK : this.rF;
         }
      }

      boolean var1 = this.qK.vn.AN && !this.qK.vn.AO && this.rC != null && this.rC.qI();
      if (!this.iq() && !var1) {
         if (this.rC != null && this.rC.EU != null && this.rC.EU.isWalkingState() && this.rC.EU.yM != AnimationState.SWIM && this.rC.qI()) {
            return this.rC.EU;
         } else {
            return this.rM != null && this.sH ? this.rM : this.rE;
         }
      } else {
         return this.rK != null && this.sH ? this.rK : this.rF;
      }
   }

   boolean isStateSM() {
      return this.sM;
   }

   public boolean jk() {
      return this.sI;
   }

   public int getAnimationTicks() {
      return this.sJ;
   }

   public float jm() {
      return this.sL;
   }

   boolean jn() {
      return this.sN;
   }

   boolean jo() {
      return this.sO;
   }

   @Nullable
   public LivingEntity getAttackTarget() {
      if (this.getLivingEntity() instanceof Mob var1) {
         LivingEntity var3 = var1.getTarget();
         if (var3 != null && var3.isValid()) {
            return var3;
         }
      }

      return null;
   }

   public void dispatchMobEvent(Entity var1, MobEventType var2, PermissionRuleConsumer var3) {
      this.qK.wm.dispatch(var1, var2, var3);
   }

   public boolean matchesSlot(EquipmentSlot var1, ItemMatcher var2) {
      if (this.sl.xo != null && this.sl.xo.getEquipment() != null) {
         ItemStack var3 = this.sl.xo.getEquipment().getItem(var1);
         return var3.getType() == Material.AIR ? false : var2.matches(var3);
      } else {
         return false;
      }
   }

   @Generated
   public boolean isDying() {
      return this.ru;
   }

   @Generated
   public void setIdleAnimation(ModelDefinitionData var1) {
      this.rE = var1;
   }

   @Generated
   public ModelDefinitionData getIdleAnimation() {
      return this.rE;
   }

   @Generated
   public void setWalkAnimation(@Nullable ModelDefinitionData var1) {
      this.rF = var1;
   }

   @Nullable
   @Generated
   public ModelDefinitionData getWalkAnimation() {
      return this.rF;
   }

   @Generated
   public void setAttackAnimation(@Nullable ModelDefinitionData var1) {
      this.rG = var1;
   }

   @Nullable
   @Generated
   public ModelDefinitionData getAttackAnimation() {
      return this.rG;
   }

   @Generated
   public void setDeathAnimation(@Nullable ModelDefinitionData var1) {
      this.rH = var1;
   }

   @Nullable
   @Generated
   public ModelDefinitionData getDeathAnimation() {
      return this.rH;
   }

   @Generated
   public void setFlyAnimation(@Nullable ModelDefinitionData var1) {
      this.rI = var1;
   }

   @Nullable
   @Generated
   public ModelDefinitionData getFlyAnimation() {
      return this.rI;
   }

   @Generated
   public void setSwimAnimation(@Nullable ModelDefinitionData var1) {
      this.rJ = var1;
   }

   @Nullable
   @Generated
   public ModelDefinitionData getSwimAnimation() {
      return this.rJ;
   }

   @Generated
   public void setWalkAngryAnimation(@Nullable ModelDefinitionData var1) {
      this.rK = var1;
   }

   @Nullable
   @Generated
   public ModelDefinitionData getWalkAngryAnimation() {
      return this.rK;
   }

   @Generated
   public void setJumpAnimation(@Nullable ModelDefinitionData var1) {
      this.rL = var1;
   }

   @Nullable
   @Generated
   public ModelDefinitionData getJumpAnimation() {
      return this.rL;
   }

   @Generated
   public void setIdleAngryAnimation(@Nullable ModelDefinitionData var1) {
      this.rM = var1;
   }

   @Nullable
   @Generated
   public ModelDefinitionData getIdleAngryAnimation() {
      return this.rM;
   }

   @Generated
   public void setSneakAnimation(@Nullable ModelDefinitionData var1) {
      this.rN = var1;
   }

   @Nullable
   @Generated
   public ModelDefinitionData getSneakAnimation() {
      return this.rN;
   }

   @Generated
   public void setSneakWalkAnimation(@Nullable ModelDefinitionData var1) {
      this.rO = var1;
   }

   @Nullable
   @Generated
   public ModelDefinitionData getSneakWalkAnimation() {
      return this.rO;
   }

   @Generated
   public boolean jC() {
      return this.sE;
   }

   @Generated
   public boolean jD() {
      return this.ta;
   }

   @Generated
   public void method8502(boolean var1) {
      this.ta = var1;
   }

   @Generated
   public boolean jE() {
      return this.tb;
   }

   @Generated
   public void method8503(boolean var1) {
      this.tb = var1;
   }
}
