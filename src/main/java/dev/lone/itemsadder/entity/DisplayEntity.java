package dev.lone.itemsadder.entity;

import beer.devs.fastnbt.nms.Version;
import beer.devs.nativepackets.api.EntityMetadataField;
import beer.devs.nativepackets.api.NativeValue;
import beer.devs.nativepackets.api.EntityMetadataFields.Display;
import beer.devs.nativepackets.api.EntityMetadataFields.Display_ItemDisplay;
import beer.devs.nativepackets.api.EntityMetadataFields.Entity;
import dev.lone.itemsadder.anim.InterpolationCache;
import dev.lone.itemsadder.anim.TransformSnapshot;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.item.ItemVariantKey;
import dev.lone.itemsadder.network.ConnectionRecord;
import dev.lone.itemsadder.network.FastMetadataPacket;
import dev.lone.itemsadder.network.FastPacketAction;
import dev.lone.itemsadder.network.FastPositionSync;
import dev.lone.itemsadder.network.FastRelativeMove;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.network.NativeValueCache;
import dev.lone.itemsadder.network.ViaVersionIntegration;
import dev.lone.itemsadder.util.DoubleVector3;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class DisplayEntity extends AbstractFakeEntity {
   private static final boolean aQF = Boolean.getBoolean("itemsadder.test.entitySnapshots");
   static final double aQG = 0.495;
   static final EntityMetadataField[] aQH = new EntityMetadataField[]{
      Display.TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS,
      Display.TRANSFORMATION_INTERPOLATION_DURATION,
      Display.POS_ROT_INTERPOLATION_DURATION,
      Display.TRANSLATION,
      Display.LEFT_ROTATION,
      Display.SCALE,
      Display.RIGHT_ROTATION
   };
   static final EntityMetadataField[] aQI = new EntityMetadataField[]{
      Entity.SHARED_FLAGS,
      Display.VIEW_RANGE,
      Display.TRANSFORMATION_INTERPOLATION_START_DELTA_TICKS,
      Display.TRANSFORMATION_INTERPOLATION_DURATION,
      Display.POS_ROT_INTERPOLATION_DURATION,
      Display.TRANSLATION,
      Display.LEFT_ROTATION,
      Display.SCALE,
      Display.RIGHT_ROTATION,
      Display.SHADOW_RADIUS,
      Display.SHADOW_STRENGTH,
      Display_ItemDisplay.ITEM_STACK,
      Display_ItemDisplay.ITEM_DISPLAY
   };
   private final UUID aQJ = UUID.randomUUID();
   private final Object aQK;
   private final NativeValueCache aQL;
   @Nullable
   private final CustomEntityImpl aQM;
   @Nullable
   private ItemVariantKey aQN;
   private final Matrix4f aQO = new Matrix4f();
   private final Matrix4f aQP = new Matrix4f();
   private final Matrix4f aQQ = new Matrix4f();
   private boolean aQR;
   private long aQS;
   private TransformDecomposition aQT = TransformDecomposition.decompose(new Matrix4f());
   private boolean aQU;
   private long aQV;
   private NativeValue aQW;
   private final Location aQX;
   private final boolean aQY;
   private byte aQZ;
   private float aRa;
   private float aRb;
   private float aRc;
   private float aRd;
   private boolean aRe;
   protected final Matrix4f aRf = new Matrix4f();
   private final Matrix4f aRg = new Matrix4f();
   private volatile BoneMatrixView aRh;
   private int aRi;
   private Matrix4fc aRj;
   private int aRk = -1;
   private boolean aRl;
   private final InterpolationCache aRm = new InterpolationCache();
   private final InterpolationCache aRn = new InterpolationCache();
   private final boolean aRo;
   private final int[] aRp = new int[34];
   private float awW = 1.0F;
   private final Map aRq = new WeakHashMap();
   protected boolean aRr;
   protected boolean aRs;

   public void NG() {
      this.aRs = true;
   }

   public void method3057(double var1) {
      this.awW = (float)Math.min(Float.MAX_VALUE, Math.max(1.0, var1 / 64.0));
      this.aRn.tU();
   }

   protected float method3058(EntityViewer var1) {
      return this.aRs && this.aQM != null ? this.aQM.getViewerOffset(var1) : 0.0F;
   }

   public DisplayEntity(Location var1) {
      this(var1, false);
   }

   public DisplayEntity(Location var1, boolean var2) {
      this(var1, var2, null);
   }

   public DisplayEntity(Location var1, boolean var2, @Nullable CustomEntityModel var3) {
      this(var1, var2, var3, null);
   }

   public DisplayEntity(Location var1, boolean var2, @Nullable CustomEntityModel var3, @Nullable CustomEntityImpl var4) {
      super(var1, false);
      this.aQY = var2;
      this.aQZ = (byte)(var2 ? 5 : 0);
      this.aQL = var3 == null ? new NativeValueCache() : var3.oe();
      this.aQM = var4;
      this.aRo = var4 != null && var3 != null && var3.AN && !var3.AO;
      if (var4 != null) {
         this.method3057(var4.getViewDistance());
      }

      this.aQK = NativePacketsHolder.PE().Pz().nativeEntityType(EntityType.ITEM_DISPLAY);
      this.aQn = this.aQm = NativePacketsHolder.PE().Pz().allocateEntityId(var1.getWorld());
      this.aQW = this.aQL.method3152(-1, 16777215, false);
      this.aQX = new Location(null, var1.getX(), var1.getY(), var1.getZ(), var1.getYaw(), var1.getPitch());
      this.aQk = this.aQl = new DoubleVector3(var1.getX(), var1.getY(), var1.getZ());
      this.aQh = this.aQg = this.aQj = this.aQi = this.aQk.acy();
      this.aQv = var2 ? EquipmentSlot.HEAD : null;
   }

   public void method3059(byte var1) {
      this.aQZ = var1;
      this.NP();
   }

   public void method3060(Matrix4f var1, int var2) {
      this.aRl = false;
      this.method3061(var1);
      this.method3067(var1, var2 == 0 ? 0 : -1);
   }

   public void method3061(Matrix4f var1) {
      this.aRf.set(var1);
      this.aRh = null;
   }

   public void method3062(BoneMatrixView var1, int var2) {
      this.method3063(var1, var2, null);
   }

   void method3063(BoneMatrixView var1, int var2, Matrix4fc var3) {
      this.aRi = var2;
      this.aRj = var3;
      this.aRh = var1;
   }

   protected Matrix4f getTransformMatrix() {
      if (this.aRh != null) {
         this.method3064(this.aRf);
         this.aRh = null;
      }

      return this.aRf;
   }

   public Matrix4f method3064(Matrix4f var1) {
      BoneMatrixView var2 = this.aRh;
      if (var2 == null) {
         return var1.set(this.aRf);
      }

      var2.getMatrixInto(this.aRi, var1);
      if (this.aRj != null) {
         var1.mul(this.aRj);
      }

      return var1;
   }

   public void method3065(Matrix4f var1, int var2) {
      if (var2 >= 1 && var2 <= 8) {
         this.aRl = false;
         this.method3067(var1, var2);
      } else {
         throw new IllegalArgumentException("Invalid planned interpolation duration");
      }
   }

   public boolean NI() {
      if (!this.aRl && !this.aRr && !this.aQs) {
         for (EntityViewer var2 : this.aQM.getViewers()) {
            if (!var2.kW() || !var2.kX() || !var2.kZ() || var2.li() != 1 || var2.la() || var2.ld()) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   protected int method3066(EntityViewer var1) {
      return this.aRk >= 0 ? this.aRk : var1.lj();
   }

   protected int NJ() {
      return this.aRk < 0 && this.aQM != null && this.aQM.countMountedRiders() > 0 ? -1 : 0;
   }

   protected void NK() {
      if (this.aRk > 0) {
         this.method3067(this.getTransformMatrix(), -1);
      }

      this.aRl = true;
   }

   private int cu(int var1) {
      return this.aRk > 0 ? 2 : var1;
   }

   private void method3067(Matrix4f var1, int var2) {
      boolean var3 = !this.aRe || !this.aQO.equals(var1);
      if (var3 || this.aRk != var2) {
         this.aRk = var2;
         this.aQO.set(var1);
         this.aRe = true;
         this.aRm.tU();
         this.NP();
         if (var3) {
            this.aQU = true;
            this.aQV++;
         }

         this.aQr = true;
      }
   }

   private void NL() {
      if (this.aQU) {
         this.aQT = TransformDecomposition.decomposeInto(this.aQP.set(this.aQO).scale(-1.0F, 1.0F, -1.0F), this.aQT);
         this.aQU = false;
      }
   }

   public TransformSnapshot snapshot() {
      if (!aQF) {
         throw new IllegalStateException("Entity diagnostic snapshots are disabled");
      } else {
         return new TransformSnapshot(this.aQn, this.aQO, this.aQS, this.aQR ? this.aQQ : null);
      }
   }

   private void method3068() {
      if (aQF) {
         this.aQQ.set(this.aQO);
         this.aQR = true;
         this.aQS++;
      }
   }

   @Override
   public Location Nv() {
      if (this.aQY) {
         this.aQX.setX(this.aQl.pW);
         this.aQX.setY(this.aQl.pX);
         this.aQX.setZ(this.aQl.pY);
         return this.aQX;
      }

      Matrix4f var1 = this.aRf;
      if (this.aRh != null) {
         var1 = this.method3064(this.aRg);
      }

      this.aQX.setX(this.aQl.pW + var1.m30());
      this.aQX.setY(this.aQl.pX + var1.m31());
      this.aQX.setZ(this.aQl.pY + var1.m32());
      return this.aQX;
   }

   @Override
   public Location getLocation() {
      return !this.aQY ? this.Nv().clone() : new Location(this.aQo.getWorld(), this.aQl.pW, this.aQl.pX + 1.9, this.aQl.pY);
   }

   @Override
   public double[] Nw() {
      if (this.aQY) {
         return new double[]{this.aQl.pW, this.aQl.pX + 0.495 + 1.5375, this.aQl.pY};
      }

      Location var1 = this.Nv();
      return new double[]{var1.getX(), var1.getY(), var1.getZ()};
   }

   @Override
   public void setAngles(float x, float y, float z) {
      if (this.aQY) {
         this.aRb = x;
         this.aRc = y;
         this.aRd = z;
         this.NO();
      }
   }

   @Override
   public boolean method2994(float yaw) {
      if (!this.aQY) {
         return super.method2994(yaw);
      }

      if (y(this.aRa) == y(yaw)) {
         return false;
      }

      this.aRa = yaw;
      this.NO();
      return true;
   }

   private void NO() {
      this.method3060(buildTransform(this.aRa, this.aRb, this.aRc, this.aRd), 1);
   }

   static Matrix4f buildTransform(float var0, float var1, float var2, float var3) {
      float var4 = y(var0) * 0.024543693F;
      return new Matrix4f()
         .translation(0.0F, 0.5F, 0.0F)
         .rotateY((float) Math.PI - var4)
         .scale(-1.0F, -1.0F, 1.0F)
         .translate(0.0F, -1.501F, 0.0F)
         .translate(0.0F, 0.0625F, 0.0F)
         .rotateZ((float)Math.toRadians(var3))
         .rotateY((float)Math.toRadians(var2))
         .rotateX((float)Math.toRadians(var1))
         .translate(0.0F, -0.25F, 0.0F)
         .rotateY((float) Math.PI)
         .scale(0.625F, -0.625F, -0.625F);
   }

   @Override
   public void method2999(double var1, double var3, double var5, boolean var7) {
      if (var7 || !i(var1, this.aQl.pW) || !i(var3, this.aQl.pX) || !i(var5, this.aQl.pY)) {
         this.aQl.method3161(var1, var3, var5);
         this.aQk.method3161(var1, var3, var5);
         this.aQo.setX(var1);
         this.aQo.setY(var3);
         this.aQo.setZ(var5);
         this.aQp = true;
      }
   }

   @Override
   public void addSpawnPackets(List viewers) {
      for (EntityViewer var3 : viewers) {
         if (var3.kW()) {
            this.method3070(var3);
         }
      }
   }

   @Override
   public void spawnFor(EntityViewer viewer) {
      if (viewer.kW()) {
         this.method3070(viewer);
      }
   }

   protected void method3070(EntityViewer var1) {
      ConnectionRecord var2 = var1.getConnection();
      if (var2 != null) {
         this.aRq.remove(var1);
         Object var3 = NativePacketsHolder.PE()
            .Pv()
            .addEntity(this.aQn, this.aQJ, this.aQK, this.aQl.pW, this.aQl.pX, this.aQl.pY, 0.0F, 0.0F, 0.0F, 0, 0.0, 0.0, 0.0);
         NativePacketsHolder.PE().method1871(var2, var3);
         if (FastPacketAction.method3167(NativePacketsHolder.PE(), var2, new FastPositionSync(this.aQn, this.aQl.pW, this.aQl.pX, this.aQl.pY, false), var1::lb)
            )
          {
            DisplayDeltaTracker var4 = this.aRq.computeIfAbsent(var1, var0 -> new DisplayDeltaTracker());
            var4.method3161(this.aQl.pW, this.aQl.pX, this.aQl.pY);
            var4.aRG = true;
         }

         if (FastPacketAction.method3167(NativePacketsHolder.PE(), var2, this.method3081(0, this.method3058(var1)), var1::lb)) {
            this.method3085(var1, this.aQV, this.method3058(var1), 0, this.aQT);
            this.method3068();
         }

         CustomEntityManager.recordStat(3L);
      }
   }

   @Override
   public void despawnFor(EntityViewer viewer) {
      this.aRq.remove(viewer);
      ConnectionRecord var2 = viewer.kU();
      if (var2 != null && NativePacketsHolder.PE().method1871(var2, NativePacketsHolder.PE().Pv().removeEntities(new int[]{this.aQn}))) {
         CustomEntityManager.recordStat(1L);
      }
   }

   @Override
   public void addUpdatePackets(List viewers, int customModelData) {
      this.method3073(customModelData, false);
      this.method3078(viewers);
   }

   @Override
   public void updateFor(EntityViewer viewer, int customModelData) {
      this.method3073(customModelData, false);
      this.method3079(viewer);
   }

   @Override
   public void addTickPackets(List viewers, int customModelData) {
      this.method3073(customModelData, true);
      this.method3078(viewers);
   }

   void method3071(List var1, int var2) {
      this.method3074(var2, cv(this.arJ));
      this.method3078(var1);
   }

   static int cv(int var0) {
      int var1 = ((var0 >>> 8 & 0xFF) * 178 + 127) / 255;
      int var2 = ((var0 & 0xFF) * 178 + 127) / 255;
      return var0 & 0xFF0000 | var1 << 8 | var2;
   }

   @Override
   public void sendEquipment(@Nullable Player basePlayer, List viewers, ItemStack item) {
      this.method3072(viewers, NativePacketsHolder.PE().Py().wrapBukkitItem(item));
   }

   public void method3072(List var1, NativeValue var2) {
      this.method3076(var2);
      this.method3078(var1);
   }

   protected void method3073(int var1, boolean var2) {
      this.method3074(var1, var2 ? 16744062 : this.arJ);
   }

   protected void method3074(int var1, int var2) {
      ItemVariantKey var3 = new ItemVariantKey(var1, var2, this.aQu);
      this.aQN = var3;
      NativeValue var4 = this.aQL.method3152(var1, var2, this.aQu);
      if (var4 != null) {
         this.method3076(var4);
      } else {
         if (this.aQM != null) {
            SchedulerUtils.runOnMainThread(() -> {
               NativeValue var4x = this.aQL.method3152(var1, var2, this.aQu);
               this.aQM.dispatchToManager(() -> {
                  if (method3075(this.aQN, var3)) {
                     this.method3076(var4x);
                     this.aQM.getViewers().forEach(EntityViewer::lc);
                     this.method3078(this.aQM.getViewers());
                  }
               });
            });
         }
      }
   }

   static boolean method3075(@Nullable ItemVariantKey var0, ItemVariantKey var1) {
      return var1.equals(var0);
   }

   protected void af(ItemStack var1) {
      this.method3076(NativePacketsHolder.PE().Py().wrapBukkitItem(var1));
   }

   protected void method3076(NativeValue var1) {
      this.aQW = var1;
      this.NP();
   }

   private static ItemStack method3077(int var0, int var1, boolean var2) {
      if (var0 == -1) {
         return new ItemStack(Material.AIR);
      }

      ItemStack var3 = new ItemStack(Material.POTION);
      PotionMeta var4 = (PotionMeta)var3.getItemMeta();
      var4.setColor(Color.fromRGB(var1));
      var3.setItemMeta(var4);
      if (var2) {
         var3.addUnsafeEnchantment(Enchantment.SHARPNESS, 1);
      }

      ItemStackUtils.method7259(var3, var0);
      return var3;
   }

   protected void method3078(List var1) {
      for (EntityViewer var3 : var1) {
         this.method3079(var3);
      }

      this.aQr = false;
      this.aRr = false;
   }

   protected void method3079(EntityViewer var1) {
      this.NK();
      if (var1.kW()) {
         if (var1.kX()) {
            this.method3080(var1);
         } else {
            var1.lb();
         }
      }
   }

   protected void method3080(EntityViewer var1) {
      int var2 = this.method3066(var1);
      float var3 = this.method3058(var1);
      this.method3083(var1, var3 == 0.0F ? this.cw(var2) : this.method3081(var2, var3), var2);
   }

   protected Object cw(int var1) {
      int var2 = this.NJ();
      return this.aRn.method3151(var1, 127, var2, var2x -> this.method3082(var2x, 0.0F, var2));
   }

   protected Object cx(int var1) {
      return this.method3081(var1, 0.0F);
   }

   protected Object method3081(int var1, float var2) {
      return this.method3082(var1, var2, this.NJ());
   }

   protected Object method3082(int var1, float var2, int var3) {
      this.NL();
      return new FastMetadataPacket(
         this.aQn, var1, this.cu(var1), var3, 127, this.aQT, var2, true, this.aQW, (byte)(this.aQf ? 1 : 0), this.awW, this.aQZ, NativePacketsHolder.PE().Pu()
      );
   }

   protected void method3083(EntityViewer var1, @Nullable Object var2, int var3) {
      long var4 = this.aQV;
      float var6 = this.method3058(var1);
      this.NL();
      TransformDecomposition var7 = this.aQT;
      if (var2 != null) {
         var1.method8551(var2, () -> {
            this.method3085(var1, var4, var6, var3, var7);
            this.method3068();
            CustomEntityManager.recordStat(1L);
         });
      }
   }

   protected void method3084(EntityViewer var1) {
      int var2 = this.method3066(var1);
      float var3 = this.method3058(var1);
      this.NL();
      int var4 = 127;
      if (this.method3086(var1)) {
         if (var3 == 0.0F) {
            var4 = this.aRp[var2] == 0 ? 127 : this.aRp[var2];
         } else {
            DisplayDeltaTracker var5 = (DisplayDeltaTracker)this.aRq.get(var1);
            if (var5 != null) {
               var4 = var5.computeDelta(this.aQT, var3);
            }
         }
      }

      int var11 = var4;
      int var6 = this.NJ();
      Object var7 = var3 == 0.0F
         ? this.aRm.method3151(var2, var11, var6, var3x -> this.method3090(var3x, 0.0F, var11, var6))
         : this.method3090(var2, var3, var11, var6);
      long var8 = this.aQV;
      TransformDecomposition var10 = this.aQT;
      if (var7 != null) {
         var1.method8551(var7, () -> {
            this.method3085(var1, var8, var3, var2, var10);
            this.method3068();
            CustomEntityManager.recordStat(1L);
         });
      }
   }

   private void method3085(EntityViewer var1, long var2, float var4, int var5, TransformDecomposition var6) {
      DisplayDeltaTracker var7 = this.aRq.computeIfAbsent(var1, var0 -> new DisplayDeltaTracker());
      var7.GD = var5 == 0 || var7.GD && var7.aQV == var2 && Float.floatToIntBits(var7.aRI) == Float.floatToIntBits(var4);
      var7.aQV = var2;
      var7.aRH = this.cu(var5);
      var7.aRI = var4;
      if (this.aRo) {
         if (!var7.aRJ) {
            var7.aRK = Version.get() == Version.v26_2 && ViaVersionIntegration.method4866(var1.li.getUniqueId()) == Version.v26_2.getProtocol();
         }

         var7.applyDelta(var6, var4);
      }
   }

   private boolean method3086(EntityViewer var1) {
      if (!this.aRo) {
         return false;
      }

      DisplayDeltaTracker var2 = (DisplayDeltaTracker)this.aRq.get(var1);
      return var2 != null && var2.aRK;
   }

   protected void method3087(List var1) {
      if (this.aRo) {
         Arrays.fill(this.aRp, 0);

         for (EntityViewer var3 : var1) {
            if (var3.kW() && var3.kX() && !this.aRr && !var3.la() && this.method3058(var3) == 0.0F && (this.aQs || this.aQr || this.method3088(var3))) {
               int var4 = this.method3066(var3);
               int var5 = 127;
               if (this.method3086(var3)) {
                  this.NL();
                  DisplayDeltaTracker var6 = (DisplayDeltaTracker)this.aRq.get(var3);
                  if (var6 != null) {
                     var5 = var6.computeDelta(this.aQT, 0.0F);
                  }
               }

               this.aRp[var4] = this.aRp[var4] | var5;
            }
         }
      }
   }

   boolean method3088(EntityViewer var1) {
      if (!var1.ld()) {
         return false;
      }

      DisplayDeltaTracker var2 = (DisplayDeltaTracker)this.aRq.get(var1);
      return var2 == null || !var2.GD || var2.aQV != this.aQV || Float.floatToIntBits(var2.aRI) != Float.floatToIntBits(this.method3058(var1));
   }

   private Object method3089(int var1, float var2, int var3) {
      return this.method3090(var1, var2, var3, this.NJ());
   }

   private Object method3090(int var1, float var2, int var3, int var4) {
      this.NL();
      return new FastMetadataPacket(
         this.aQn, var1, this.cu(var1), var4, var3, this.aQT, var2, false, null, (byte)0, 0.0F, (byte)0, NativePacketsHolder.PE().Pu()
      );
   }

   protected void NP() {
      this.aRn.tU();
   }

   protected void NQ() {
      this.aQr = this.aRr = false;
   }

   @Override
   public void addMetadataPackets(List viewers) {
      this.method3087(viewers);

      for (EntityViewer var3 : viewers) {
         if (var3.kW()) {
            if (!var3.kX()) {
               if (this.aRr) {
                  var3.lb();
               } else if (this.aQr) {
                  var3.le();
               }
            } else if (this.aRr || var3.la()) {
               this.method3080(var3);
            } else if (this.aQs || this.aQr || this.method3088(var3)) {
               this.method3084(var3);
            }
         }
      }

      this.aQr = this.aRr = false;
   }

   @Override
   public void addEquipmentPackets(boolean fixClientDelay, @Nullable LivingEntity baseEntity, List viewers) {
      this.method3091(fixClientDelay, baseEntity, viewers, null);
   }

   public void method3091(boolean var1, @Nullable LivingEntity var2, List var3, @Nullable EntityViewer var4) {
      boolean var5 = Version.isAtLeast(Version.v1_21_3);
      int var6 = this.aQn;
      double var7 = this.aQl.pW;
      double var9 = this.aQl.pX;
      double var11 = this.aQl.pY;
      FastPositionSync var13 = null;

      for (EntityViewer var15 : var3) {
         DisplayDeltaTracker var16 = (DisplayDeltaTracker)this.aRq.get(var15);
         DisplayDeltaTracker var17 = var16 != null && var16.aRG ? var16 : null;
         boolean var18 = var17 == null || !i(var7, var17.pW) || !i(var9, var17.pX) || !i(var11, var17.pY);
         double var19 = var17 == null
            ? 0.0
            : (var7 - var17.pW) * (var7 - var17.pW) + (var9 - var17.pX) * (var9 - var17.pX) + (var11 - var17.pY) * (var11 - var17.pY);
         boolean var21 = var19 > 4.0;
         if (var15 != var4 && var15.kW() && (var15.kZ() || var21) && (this.aQs || this.aQp || var18 || var15.la() || var15.ld())) {
            if (var19 > 4096.0) {
               var15.kR();
            } else {
               int var22 = this.method3066(var15);
               int var23 = this.cu(var22);
               if (var16 == null || var16.aRH != var23) {
                  Object var24 = this.method3089(var22, this.method3058(var15), 4);
                  var15.method8551(var24, () -> this.aRq.computeIfAbsent(var15, var0 -> new DisplayDeltaTracker()).aRH = var23);
               }

               FastPacketAction var31 = null;
               if (var5 && var17 != null && !var21) {
                  long var25 = Math.round(var7 * 4096.0) - Math.round(var17.pW * 4096.0);
                  long var27 = Math.round(var9 * 4096.0) - Math.round(var17.pX * 4096.0);
                  long var29 = Math.round(var11 * 4096.0) - Math.round(var17.pY * 4096.0);
                  if (var25 >= -32768L && var25 <= 32767L && var27 >= -32768L && var27 <= 32767L && var29 >= -32768L && var29 <= 32767L) {
                     var31 = new FastRelativeMove(var6, (short)var25, (short)var27, (short)var29);
                  }
               }

               if (var31 == null) {
                  if (var13 == null) {
                     var13 = new FastPositionSync(var6, var7, var9, var11, var5);
                  }

                  var31 = var13;
               }

               var15.method8551(var31, () -> {
                  DisplayDeltaTracker var8 = this.aRq.computeIfAbsent(var15, var0 -> new DisplayDeltaTracker());
                  var8.method3161(var7, var9, var11);
                  var8.aRG = true;
                  CustomEntityManager.recordStat(1L);
               });
            }
         }
      }

      this.aQp = this.aQq = false;
   }

   @Override
   public void Nz() {
      super.Nz();
      this.aRr = true;
      this.NK();
   }

   @Override
   public void addTeleportPackets(List viewers) {
      this.NP();
      this.method3020(viewers, (byte)(this.aQf ? 1 : 0));
   }

   @Override
   public void onViewerAdded(Player player) {
   }

   @Override
   public void sendPacket(Player player, Object packet) {
      NativePacketsHolder.PE().method1868(player, FastPacketAction.method3166(packet, NativePacketsHolder.PE().Pv()));
   }

   @Override
   public void Nr() {
   }
}
