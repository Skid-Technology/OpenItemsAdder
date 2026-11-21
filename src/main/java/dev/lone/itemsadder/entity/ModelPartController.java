package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.EntityMetadataField;
import beer.devs.nativepackets.api.NativePacketFactory;
import beer.devs.nativepackets.api.EntityMetadataFields.Interaction;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import dev.lone.itemsadder.api.CustomEntity;
import dev.lone.itemsadder.api.CustomEntity.DamageablePart;
import dev.lone.itemsadder.network.ConnectionRecord;
import dev.lone.itemsadder.network.NativePacketsBridge;
import dev.lone.itemsadder.network.NativePacketsHolder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.persistence.PersistentDataType;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class ModelPartController {
   private static final NamespacedKey xw = new NamespacedKey("itemsadder", "model_parts");
   private static final Map field112 = new ConcurrentHashMap();
   private final CustomEntityImpl xy;
   private final LivingEntity xz;
   private int xA;
   private final BoneState[] xB;
   private final Set[] xC;
   private final float[] xD;
   private final ModelPartEntity[] xE;
   private volatile Set xF = Set.of();
   private volatile PartOffsetRecord[] xG;
   private volatile PartSlot[] xH;
   private volatile boolean cz;
   private volatile boolean cV = true;
   private final Matrix4f xI = new Matrix4f();
   private final Vector3f xJ = new Vector3f();
   private final Map xK = new IdentityHashMap();
   private int xL = -1;
   private UUID xM;
   private Object xN;

   static void an(Player var0) {
      var0.getPersistentDataContainer().remove(xw);
   }

   ModelPartController(CustomEntityImpl var1, LivingEntity var2, BoneStateList var3) {
      this.xy = var1;
      this.xz = var2;
      this.xA = var2.getEntityId();
      this.xB = var3.getStates().toArray(BoneState[]::new);
      this.xD = new float[this.xB.length];
      this.xE = new ModelPartEntity[this.xB.length];
      this.xC = new Set[this.xB.length];
      this.xG = new PartOffsetRecord[this.xB.length];
      this.xH = new PartSlot[this.xB.length];

      for (int var4 = 0; var4 < this.xB.length; var4++) {
         this.xD[var4] = this.xB[var4].health() == null ? -1.0F : this.xB[var4].health();
         HashSet var5 = new HashSet();

         for (CustomEntityDefinition var7 : var1.qK.vn.AL.values()) {
            if (var7.getId().equals(this.xB[var4].bone()) || Arrays.asList(var7.Am).contains(this.xB[var4].bone())) {
               var5.add(var7.getId());
            }
         }

         this.xC[var4] = Set.copyOf(var5);
      }

      String var9 = (String)var2.getPersistentDataContainer().get(xw, PersistentDataType.STRING);
      if (var9 != null) {
         JSONObject var10 = JSONObject.parseObject(var9);

         for (int var11 = 0; var11 < this.xB.length; var11++) {
            if (var10.get(this.xB[var11].id().toString()) instanceof Number var8 && Double.isFinite(var8.doubleValue()) && var8.doubleValue() >= 0.0) {
               this.xD[var11] = var8.floatValue() == 0.0F
                  ? 0.0F
                  : (this.xB[var11].health() == null ? -1.0F : Math.min(this.xB[var11].health(), var8.floatValue()));
            }
         }
      }

      this.method8636();
      field112.put(this.xA, this);
   }

   public boolean aO(String var1) {
      return this.xF.contains(var1);
   }

   boolean isReady() {
      return !this.cz && this.cV;
   }

   public static LivingEntity asLiving(Entity var0) {
      ModelPartController var1 = (ModelPartController)field112.get(var0.getEntityId());
      return var1 != null && !var1.cz && uuidOf(var0) != null ? var1.xz : null;
   }

   public static UUID uuidOf(Entity var0) {
      ModelPartController var1 = (ModelPartController)field112.get(var0.getEntityId());
      if (var1 != null && !var1.cz) {
         for (int var2 = 0; var2 < var1.xB.length; var2++) {
            if (var1.xE[var2] != null
               && (var1.xE[var2].getLivingEntity().getEntityId() == var0.getEntityId() || var0.getEntityId() == var1.xA && var1.xE[var2].isForwardingDamage())) {
               return var1.xB[var2].id();
            }
         }
      }

      return null;
   }

   public List getParts() {
      ArrayList var1 = new ArrayList(this.xB.length);

      for (int var2 = 0; var2 < this.xB.length; var2++) {
         var1.add(
            new DamageablePart(
               this.xB[var2].id(),
               this.xB[var2].bone(),
               this.xB[var2].health() == null ? null : this.xD[var2],
               this.xD[var2] == 0.0F,
               !this.cz && this.xD[var2] != 0.0F && this.xE[var2] != null ? this.xE[var2].getLivingEntity() : null
            )
         );
      }

      return List.copyOf(var1);
   }

   private void method8636() {
      HashSet var1 = new HashSet();

      for (int var2 = 0; var2 < this.xB.length; var2++) {
         if (this.xD[var2] == 0.0F) {
            var1.addAll(this.xC[var2]);
         }
      }

      for (int var6 = 0; var6 < this.xB.length; var6++) {
         if (var1.contains(this.xB[var6].bone())) {
            this.xD[var6] = 0.0F;
         }
      }

      this.xF = Set.copyOf(var1);
      this.cV = false;

      for (float var5 : this.xD) {
         if (var5 != 0.0F) {
            this.cV = true;
            break;
         }
      }
   }

   private void method8637(int var1, double var2) {
      if (!this.cz && this.xD[var1] != 0.0F) {
         this.xD[var1] = (float)Math.max(0.0, var2);
         if (var2 <= 0.0) {
            this.method8636();

            for (int var4 = 0; var4 < this.xB.length; var4++) {
               if (this.xD[var4] == 0.0F && this.xE[var4] != null) {
                  this.xE[var4].remove();
               }
            }
         }

         this.method8638(this.xz);
         if (var2 <= 0.0) {
            this.xy.dispatchToManager(() -> {
               for (String var2x : this.xF) {
                  ModelPartDisplay var3 = this.xy.getModelPart(var2x);
                  if (var3 != null) {
                     var3.refreshVisibility();
                  }
               }
            });
         }

         if (var2 <= 0.0) {
            EntityDamageEvent var5 = this.xE[var1] == null ? null : this.xE[var1].getLivingEntity().getLastDamageCause();
            CustomEntity.internal_firePartBreak(this.xy, this.xB[var1].id(), this.xB[var1].bone(), var5 == null ? null : var5.getDamageSource());
         }
      }
   }

   void method8638(LivingEntity var1) {
      JSONObject var2 = new JSONObject();

      for (int var3 = 0; var3 < this.xB.length; var3++) {
         if (this.xD[var3] >= 0.0F) {
            var2.put(this.xB[var3].id().toString(), this.xD[var3]);
         }
      }

      var1.getPersistentDataContainer().set(xw, PersistentDataType.STRING, var2.toJSONString(new Feature[0]));
   }

   public void mn() {
      if (!this.cz) {
         PartOffsetRecord[] var1 = this.xG;
         PartOffsetRecord[] var2 = var1;
         Location var3 = this.xy.getBaseLocation();
         UUID var4 = this.xy.getEntityUuid();
         if (var4 != null) {
            for (int var5 = 0; var5 < this.xB.length; var5++) {
               BoneState var6 = this.xB[var5];
               ModelPartDisplay var7 = this.xy.getModelPart(var6.bone());
               PartOffsetRecord var8 = null;
               if (!this.xF.contains(var6.bone()) && var7 != null && var7.isVisibleToViewers()) {
                  var7.getScaledPartMatrix(this.xI);
                  this.xI.transformPosition(this.xJ.set(var6.x(), var6.y(), var6.z()));
                  float var9 = Math.abs(this.xI.m00()) * var6.rotX() + Math.abs(this.xI.m10()) * var6.rotZ() + Math.abs(this.xI.m20()) * var6.rotY();
                  float var10 = Math.abs(this.xI.m01()) * var6.rotX() + Math.abs(this.xI.m11()) * var6.rotZ() + Math.abs(this.xI.m21()) * var6.rotY();
                  float var11 = Math.abs(this.xI.m02()) * var6.rotX() + Math.abs(this.xI.m12()) * var6.rotZ() + Math.abs(this.xI.m22()) * var6.rotY();
                  float var12 = Math.max(0.01F, Math.max(var9, var11));
                  var10 = Math.max(0.01F, var10);
                  double var13 = var3.getX() + this.xJ.x;
                  double var15 = var3.getY() + this.xJ.y - var10 / 2.0F;
                  double var17 = var3.getZ() + this.xJ.z;
                  if (Float.isFinite(var12)
                     && Float.isFinite(var10)
                     && var12 > 0.0F
                     && var10 > 0.0F
                     && Double.isFinite(var13)
                     && Double.isFinite(var15)
                     && Double.isFinite(var17)) {
                     PartOffsetRecord var19 = var1[var5];
                     var8 = var19 != null
                           && var19.xO.equals(var4)
                           && var19.xP == var13
                           && var19.xQ == var15
                           && var19.xR == var17
                           && var19.xS == var12
                           && var19.xT == var10
                        ? var19
                        : new PartOffsetRecord(var4, var13, var15, var17, var12, var10);
                  }
               }

               if (var8 != var1[var5]) {
                  if (var2 == var1) {
                     var2 = (PartOffsetRecord[])var1.clone();
                  }

                  var2[var5] = var8;
               }
            }

            if (var2 != var1) {
               this.xG = var2;
            }
         }
      }
   }

   void tick() {
      if (!this.cz) {
         if (this.xz.isDead()) {
            this.close();
         } else if (this.xz.isValid()) {
            int var1 = this.xz.getEntityId();
            if (this.xA != var1) {
               field112.remove(this.xA, this);
               this.xA = var1;
               field112.put(this.xA, this);
            }

            if (this.xL != this.xz.getTicksLived()) {
               this.xL = -1;
               this.xM = null;
               this.xN = null;
            }

            PartOffsetRecord[] var2 = this.xG;
            PartSlot[] var3 = this.xH;
            PartSlot[] var4 = var3;
            UUID var5 = this.xz.getWorld().getUID();

            for (int var6 = 0; var6 < this.xB.length; var6++) {
               PartOffsetRecord var7 = this.xD[var6] == 0.0F ? null : var2[var6];
               if (var7 != null && !var7.xO.equals(var5)) {
                  var7 = null;
               }

               PartSlot var8 = var3[var6];
               PartSlot var9 = var8;
               if (var7 == null) {
                  this.method8639(var6);
                  var9 = null;
               } else {
                  if (this.xE[var6] != null && !this.xE[var6].getLivingEntity().getWorld().getUID().equals(var5)) {
                     this.method8639(var6);
                  }

                  if (this.xE[var6] == null) {
                     int var10 = var6;
                     ModelPartEntity var11 = ProjectileTypeHelper.getAdapter()
                        .method3232(this.xz, this.xB[var6].health(), this.xB[var6].scale(), var2x -> this.method8637(var10, var2x));
                     if (this.xB[var6].health() != null) {
                        var11.getLivingEntity().setHealth(this.xD[var6]);
                     }

                     var11.a_(var7.xP, var7.xQ, var7.xR, var7.xS, var7.xT);
                     if (!var11.isBroken()) {
                        var11.remove();
                        continue;
                     }

                     this.xE[var6] = var11;
                     field112.put(var11.getLivingEntity().getEntityId(), this);
                     var8 = null;
                  }

                  if (var8 == null || var8.xV != var7) {
                     ModelPartEntity var12 = this.xE[var6];
                     var12.a_(var7.xP, var7.xQ, var7.xR, var7.xS, var7.xT);
                     var9 = new PartSlot(var12.getLivingEntity().getEntityId(), var12.getLivingEntity().getUniqueId(), var7);
                  }
               }

               if (var9 != var3[var6]) {
                  if (var4 == var3) {
                     var4 = (PartSlot[])var3.clone();
                  }

                  var4[var6] = var9;
               }
            }

            if (var4 != var3) {
               this.xH = var4;
            }
         }
      }
   }

   private void method8639(int var1) {
      if (this.xE[var1] != null) {
         field112.remove(this.xE[var1].getLivingEntity().getEntityId(), this);
         this.xE[var1].remove();
         this.xE[var1] = null;
      }
   }

   void close() {
      if (!this.cz) {
         this.cz = true;
         field112.remove(this.xA, this);

         for (int var1 = 0; var1 < this.xB.length; var1++) {
            this.method8639(var1);
         }

         this.xH = new PartSlot[this.xB.length];
      }
   }

   void mo() {
      PartSlot[] var1 = this.xH;
      NativePacketsBridge var2 = NativePacketsHolder.PE();
      NativePacketFactory var3 = var2.Pv();

      for (EntityViewer var5 : this.xy.getViewers()) {
         if (!var5.uW && var5.kW()) {
            ConnectionRecord var6 = var5.getConnection();
            if (var6 != null) {
               PartSlot[] var7 = (PartSlot[])this.xK.get(var5);
               if (var7 != var1) {
                  ArrayList var8 = null;

                  for (int var9 = 0; var9 < var1.length; var9++) {
                     PartSlot var10 = var7 == null ? null : var7[var9];
                     PartSlot var11 = var1[var9];
                     if (var10 != var11) {
                        if (var8 == null) {
                           var8 = new ArrayList();
                        }

                        if (var10 != null && (var11 == null || var10.id != var11.id)) {
                           var8.add(var3.removeEntities(new int[]{var10.id}));
                        }

                        if (var11 != null) {
                           PartOffsetRecord var12 = var11.xV;
                           if (var10 != null && var10.id == var11.id) {
                              if (var11.xX == null) {
                                 var11.xX = var3.teleport(var11.id, var12.xP, var12.xQ, var12.xR, 0.0F, 0.0F, false);
                              }

                              var8.add(var11.xX);
                           } else {
                              if (var11.xW == null) {
                                 var11.xW = var3.addEntity(
                                    var11.id,
                                    var11.xU,
                                    var2.Pz().nativeEntityType(EntityType.INTERACTION),
                                    var12.xP,
                                    var12.xQ,
                                    var12.xR,
                                    0.0F,
                                    0.0F,
                                    0.0F,
                                    0,
                                    0.0,
                                    0.0,
                                    0.0
                                 );
                              }

                              var8.add(var11.xW);
                           }

                           if (var10 == null || var10.id != var11.id || var10.xV.xS != var12.xS || var10.xV.xT != var12.xT) {
                              if (var11.xY == null) {
                                 var11.xY = var3.metadata(
                                    var11.id, new EntityMetadataField[]{Interaction.WIDTH, Interaction.HEIGHT}, new Object[]{var12.xS, var12.xT}
                                 );
                              }

                              var8.add(var11.xY);
                           }
                        }
                     }
                  }

                  boolean var13 = true;
                  if (var8 != null && !var8.isEmpty()) {
                     for (Object var15 : var8) {
                        var13 &= var2.method1871(var6, var15);
                     }
                  }

                  if (var13) {
                     this.xK.put(var5, var1);
                  }
               }
            }
         }
      }
   }

   void sendToViewer(EntityViewer var1) {
      PartSlot[] var2 = (PartSlot[])this.xK.remove(var1);
      if (var2 != null && NativePacketsHolder.aN()) {
         ConnectionRecord var3 = var1.kU();
         if (var3 != null) {
            int[] var4 = Arrays.stream(var2).filter(Objects::nonNull).mapToInt(var0 -> var0.id).toArray();
            if (var4.length > 0) {
               NativePacketsHolder.PE().method1871(var3, NativePacketsHolder.PE().Pv().removeEntities(var4));
            }
         }
      }
   }

   public static void onDamageByEntity(EntityDamageByEntityEvent var0) {
      ModelPartController var1 = (ModelPartController)field112.get(var0.getEntity().getEntityId());
      if (var1 != null) {
         if (var1.cz) {
            var0.setCancelled(true);
         } else if (var0.getDamager() != var1.xz
            && !var1.xz.getPassengers().contains(var0.getDamager())
            && !(var0.getDamager() instanceof Player var2 && CustomEntityManager.getInstance().isRiderOf(var2, var1.xy))) {
            for (int var4 = 0; var4 < var1.xE.length; var4++) {
               if (var1.xB[var4].health() != null && var1.xE[var4] != null && var1.xE[var4].isForwardingDamage()) {
                  return;
               }
            }

            if (var1.xL == var1.xz.getTicksLived()
               && var0.getDamager().getUniqueId().equals(var1.xM)
               && (
                  var0.getCause() == DamageCause.ENTITY_SWEEP_ATTACK
                     || var0.getCause() == DamageCause.ENTITY_ATTACK && ProjectileTypeHelper.getAdapter().method3233(var0.getDamageSource()) == var1.xN
               )) {
               var0.setCancelled(true);
            }
         } else {
            var0.setCancelled(true);
         }
      }
   }

   public static void onDamageByEntityDup2(EntityDamageByEntityEvent var0) {
      ModelPartController var1 = (ModelPartController)field112.get(var0.getEntity().getEntityId());
      if (var1 != null && !var0.isCancelled()) {
         var1.xL = var1.xz.getTicksLived();
         var1.xM = var0.getDamager().getUniqueId();
         var1.xN = ProjectileTypeHelper.getAdapter().method3233(var0.getDamageSource());
      }
   }
}
