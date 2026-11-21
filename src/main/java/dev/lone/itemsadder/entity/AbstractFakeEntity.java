package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.EntityMetadataField;
import beer.devs.nativepackets.api.FastPacketSender;
import beer.devs.nativepackets.api.NativeEquipment;
import beer.devs.nativepackets.api.NativeEquipmentSlot;
import beer.devs.nativepackets.api.NativeItem;
import beer.devs.nativepackets.api.EntityMetadataFields.ArmorStand;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.lone.itemsadder.block.CustomBlockStateSettings;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.internal.SwitchMap38;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.nms.NmsRegistry;
import dev.lone.itemsadder.util.DoubleVector3;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.util.FloatVector3Base;
import dev.lone.itemsadder.util.ImagePixelUtils;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.MathUtils;
import dev.lone.itemsadder.util.Rect2d;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;

public abstract class AbstractFakeEntity {
   public static Cache aPC = CacheBuilder.newBuilder().expireAfterWrite(60L, TimeUnit.MINUTES).build();
   public static final double aPD = 1.975;
   public static final double aPE = 0.562;
   public static final double aPF = 0.4375;
   @Deprecated
   public static final double aPG = 1.5375;
   public static final double aPH = 1.8;
   public static final double aPI = 0.9;
   public static final double aPJ = 1.2;
   public static final double aPK = 1.413;
   public static float aPL = 0.68F;
   public static float aPM = 3.0F;
   public static final List aPN = List.of(
      Rect2d.of(new Vector2d(8.0, 8.0), new Vector2d(8.0, 8.0)),
      Rect2d.of(new Vector2d(24.0, 8.0), new Vector2d(8.0, 8.0)),
      Rect2d.of(new Vector2d(0.0, 8.0), new Vector2d(8.0, 8.0)),
      Rect2d.of(new Vector2d(16.0, 8.0), new Vector2d(8.0, 8.0)),
      Rect2d.of(new Vector2d(8.0, 0.0), new Vector2d(8.0, 8.0)),
      Rect2d.of(new Vector2d(16.0, 0.0), new Vector2d(8.0, 8.0))
   );
   public static final List aPO = List.of(
      Rect2d.of(new Vector2d(40.0, 8.0), new Vector2d(8.0, 8.0)),
      Rect2d.of(new Vector2d(56.0, 8.0), new Vector2d(8.0, 8.0)),
      Rect2d.of(new Vector2d(32.0, 8.0), new Vector2d(8.0, 8.0)),
      Rect2d.of(new Vector2d(48.0, 8.0), new Vector2d(8.0, 8.0)),
      Rect2d.of(new Vector2d(40.0, 0.0), new Vector2d(8.0, 8.0)),
      Rect2d.of(new Vector2d(48.0, 0.0), new Vector2d(8.0, 8.0))
   );
   public static final List aPP = List.of(
      Rect2d.of(new Vector2d(20.0, 20.0), new Vector2d(8.0, 12.0)),
      Rect2d.of(new Vector2d(32.0, 20.0), new Vector2d(8.0, 12.0)),
      Rect2d.of(new Vector2d(28.0, 20.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(16.0, 20.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(20.0, 16.0), new Vector2d(8.0, 4.0)),
      Rect2d.of(new Vector2d(28.0, 16.0), new Vector2d(8.0, 4.0))
   );
   public static final List aPQ = List.of(
      Rect2d.of(new Vector2d(20.0, 36.0), new Vector2d(8.0, 12.0)),
      Rect2d.of(new Vector2d(32.0, 36.0), new Vector2d(8.0, 12.0)),
      Rect2d.of(new Vector2d(28.0, 36.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(16.0, 36.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(20.0, 32.0), new Vector2d(8.0, 4.0)),
      Rect2d.of(new Vector2d(28.0, 32.0), new Vector2d(8.0, 4.0))
   );
   public static final List aPR = List.of(
      Rect2d.of(new Vector2d(36.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(44.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(40.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(32.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(36.0, 48.0), new Vector2d(4.0, 4.0)),
      Rect2d.of(new Vector2d(40.0, 48.0), new Vector2d(4.0, 4.0))
   );
   public static final List aPS = List.of(
      Rect2d.of(new Vector2d(52.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(60.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(56.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(48.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(52.0, 48.0), new Vector2d(4.0, 4.0)),
      Rect2d.of(new Vector2d(56.0, 48.0), new Vector2d(4.0, 4.0))
   );
   public static final List aPT = List.of(
      Rect2d.of(new Vector2d(36.0, 52.0), new Vector2d(3.0, 12.0)),
      Rect2d.of(new Vector2d(43.0, 52.0), new Vector2d(3.0, 12.0)),
      Rect2d.of(new Vector2d(32.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(39.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(36.0, 48.0), new Vector2d(3.0, 4.0)),
      Rect2d.of(new Vector2d(39.0, 48.0), new Vector2d(3.0, 4.0))
   );
   public static final List aPU = List.of(
      Rect2d.of(new Vector2d(52.0, 52.0), new Vector2d(3.0, 12.0)),
      Rect2d.of(new Vector2d(59.0, 52.0), new Vector2d(3.0, 12.0)),
      Rect2d.of(new Vector2d(48.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(55.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(52.0, 48.0), new Vector2d(3.0, 4.0)),
      Rect2d.of(new Vector2d(55.0, 48.0), new Vector2d(3.0, 4.0))
   );
   public static final List aPV = List.of(
      Rect2d.of(new Vector2d(44.0, 20.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(52.0, 20.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(48.0, 20.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(40.0, 20.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(44.0, 16.0), new Vector2d(4.0, 4.0)),
      Rect2d.of(new Vector2d(48.0, 16.0), new Vector2d(4.0, 4.0))
   );
   public static final List aPW = List.of(
      Rect2d.of(new Vector2d(44.0, 36.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(52.0, 36.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(48.0, 36.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(40.0, 36.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(44.0, 32.0), new Vector2d(4.0, 4.0)),
      Rect2d.of(new Vector2d(48.0, 35.0), new Vector2d(4.0, 4.0))
   );
   public static final List aPX = List.of(
      Rect2d.of(new Vector2d(44.0, 20.0), new Vector2d(3.0, 12.0)),
      Rect2d.of(new Vector2d(51.0, 20.0), new Vector2d(3.0, 12.0)),
      Rect2d.of(new Vector2d(40.0, 20.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(47.0, 20.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(44.0, 16.0), new Vector2d(3.0, 4.0)),
      Rect2d.of(new Vector2d(47.0, 16.0), new Vector2d(3.0, 4.0))
   );
   public static final List aPY = List.of(
      Rect2d.of(new Vector2d(44.0, 36.0), new Vector2d(3.0, 12.0)),
      Rect2d.of(new Vector2d(51.0, 36.0), new Vector2d(3.0, 12.0)),
      Rect2d.of(new Vector2d(40.0, 36.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(47.0, 36.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(44.0, 32.0), new Vector2d(3.0, 4.0)),
      Rect2d.of(new Vector2d(47.0, 35.0), new Vector2d(3.0, 4.0))
   );
   public static final List aPZ = List.of(
      Rect2d.of(new Vector2d(20.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(28.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(16.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(24.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(20.0, 48.0), new Vector2d(4.0, 4.0)),
      Rect2d.of(new Vector2d(24.0, 48.0), new Vector2d(4.0, 4.0))
   );
   public static final List aQa = List.of(
      Rect2d.of(new Vector2d(4.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(12.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(8.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(0.0, 52.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(4.0, 48.0), new Vector2d(4.0, 4.0)),
      Rect2d.of(new Vector2d(8.0, 48.0), new Vector2d(4.0, 4.0))
   );
   public static final List aQb = List.of(
      Rect2d.of(new Vector2d(4.0, 20.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(12.0, 20.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(0.0, 20.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(8.0, 20.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(4.0, 16.0), new Vector2d(4.0, 4.0)),
      Rect2d.of(new Vector2d(8.0, 16.0), new Vector2d(4.0, 4.0))
   );
   public static final List aQc = List.of(
      Rect2d.of(new Vector2d(4.0, 36.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(12.0, 36.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(0.0, 36.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(8.0, 36.0), new Vector2d(4.0, 12.0)),
      Rect2d.of(new Vector2d(4.0, 32.0), new Vector2d(4.0, 4.0)),
      Rect2d.of(new Vector2d(8.0, 32.0), new Vector2d(4.0, 4.0))
   );
   protected final HashMap aQd = new HashMap();
   protected final HashMap aQe = new HashMap();
   public boolean aQf;
   protected DoubleVector3 aQg;
   protected DoubleVector3 aQh;
   protected DoubleVector3 aQi;
   protected DoubleVector3 aQj;
   protected DoubleVector3 aQk;
   protected DoubleVector3 aQl;
   public int aQm;
   public int aQn;
   public Location aQo;
   protected boolean aQp = true;
   protected boolean aQq = true;
   protected boolean aQr = true;
   public boolean aQs = false;
   @Nullable
   protected String playerSkin;
   protected final boolean aQt;
   protected volatile boolean aQu = false;
   protected volatile int arJ = 16777215;
   static Constructor aON;
   public EquipmentSlot aQv;
   protected double aQw;
   static double aQx = 0.001;
   static float aQy = 1.0E-5F;

   public static AbstractFakeEntity create(Location var0, EquipmentSlot var1, boolean var2, boolean var3, boolean var4) {
      if (aON == null) {
         aON = NmsRegistry.method2779(AbstractFakeEntity.class, false, Location.class, EquipmentSlot.class, boolean.class, boolean.class, boolean.class);
         if (aON == null) {
            throw new RuntimeException("Failed to get constructor for the client entity.");
         }
      }

      try {
         AbstractFakeEntity var5 = (AbstractFakeEntity)aON.newInstance(var0, var1, var2, var3, var4);
         var5.aQv = var1;
         return var5;
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException var6) {
         Log.error("Error getting constructor for ClientEntity", var6);
         return null;
      }
   }

   protected AbstractFakeEntity(Location var1, boolean var2) {
      this.aQt = var2;
      this.aQo = var1.clone();
      this.aQo.setPitch(0.0F);
      this.aQo.setYaw(0.0F);
   }

   protected static ItemStack createSkull(String var0) {
      ItemStack var1 = new ItemStack(Material.PLAYER_HEAD);
      if (var1.getType() == Material.PLAYER_HEAD) {
         if (var0.length() > 16) {
            ItemOperationsService.getRegistryBridge().method3428(var1, var0);
         } else {
            SkullMeta var2 = (SkullMeta)var1.getItemMeta();
            var2.setOwningPlayer(Bukkit.getOfflinePlayer(var0));
            var1.setItemMeta(var2);
         }
      }

      return var1;
   }

   public void Nr() {
      this.aQd.clear();
      this.aQe.clear();
   }

   public double[] Ns() {
      return new double[]{this.aQl.pW, this.aQl.pX, this.aQl.pY};
   }

   public double[] Nt() {
      return new double[]{this.aQk.pW, this.aQk.pX, this.aQk.pY};
   }

   public Location getLocation() {
      return new Location(this.aQo.getWorld(), this.aQo.getX(), this.aQo.getY() + 1.9, this.aQo.getZ());
   }

   public Location Nv() {
      return this.aQo;
   }

   public double[] Nw() {
      double[] var1 = this.Nt();
      var1[1] += 1.5375;
      return var1;
   }

   public abstract void setAngles(float var1, float var2, float var3);

   public boolean method2994(float yaw) {
      if (toPackedAngle(yaw) == toPackedAngle(this.aQo.getYaw())) {
         return false;
      }

      this.aQo.setYaw(yaw);
      this.aQq = true;
      return true;
   }

   public void method2995(double var1, double var3, double var5, FloatVector3Base var7) {
      double var8 = var1 + var7.x;
      double var10 = var3 + var7.y;
      double var12 = var5 + var7.z;
      this.method2998(var8, var10, var12);
   }

   public static boolean method2996(double var0, double var2) {
      return MathUtils.absDouble(var2 - var0) <= aQx;
   }

   public static boolean method2997(float var0, float var1) {
      return MathUtils.absFloat(var1 - var0) <= aQy;
   }

   public void method2998(double var1, double var3, double var5) {
      this.method2999(var1, var3, var5, false);
   }

   public void method2999(double var1, double var3, double var5, boolean var7) {
      if (var7 || !method2996(var1, this.aQl.pW) || !method2996(var3, this.aQl.pX) || !method2996(var5, this.aQl.pY)) {
         this.aQl.method3161(var1, var3, var5);
         this.aQk.method3161(var1, var3 + this.aQw + 0.12, var5);
         this.aQo.setX(var1);
         this.aQo.setY(var3);
         this.aQo.setZ(var5);
         this.aQp = true;
      }
   }

   public void method3000(List var1, int var2) {
      this.addTickPackets(var1, var2);
   }

   public void method3001(List var1, int var2) {
      this.addUpdatePackets(var1, var2);
   }

   public abstract void addSpawnPackets(List var1);

   public abstract void spawnFor(EntityViewer var1);

   public abstract void despawnFor(EntityViewer var1);

   public abstract void addUpdatePackets(List var1, int var2);

   public abstract void updateFor(EntityViewer var1, int var2);

   public abstract void addTickPackets(List var1, int var2);

   public abstract void sendEquipment(@Nullable Player var1, List var2, ItemStack var3);

   private void method3009(EntityViewer var1, NativeItem var2) {
      NativeEquipmentSlot var3 = this.Nx();
      Object var4 = NativePacketsHolder.PE().Pv().equipment(this.aQn, List.of(new NativeEquipment(var3, var2)));
      NativePacketsHolder.PE().method1868(var1.li, var4);
   }

   private void method3010(FastPacketSender var1, EntityViewer var2, NativeItem var3) {
      try {
         try {
            var1.sendEntityEquipment(var2.li, this.aQn, this.Nx(), var3);
         } catch (UnsupportedOperationException var5) {
            this.method3009(var2, var3);
         }
      } catch (IllegalStateException var6) {
         method3011(var2, var6);
      }
   }

   private static void method3011(EntityViewer var0, IllegalStateException var1) {
      if (NativePacketsHolder.PE().cC(var0.li)) {
         throw var1;
      }
   }

   protected final void method3012(List var1, NativeItem var2) {
      FastPacketSender var3 = NativePacketsHolder.PE().Pw();

      for (EntityViewer var5 : var1) {
         if (!var5.method8561()) {
            this.method3010(var3, var5, var2);
         }
      }
   }

   protected final void method3013(EntityViewer var1, NativeItem var2) {
      if (!var1.method8561()) {
         this.method3010(NativePacketsHolder.PE().Pw(), var1, var2);
      }
   }

   protected final void method3014(List var1, NativeItem var2) {
      FastPacketSender var3 = NativePacketsHolder.PE().Pw();

      for (EntityViewer var5 : var1) {
         if (var5.kW()) {
            this.method3010(var3, var5, var2);
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private NativeEquipmentSlot Nx() {
      return switch (SwitchMap38.aQz[this.aQv.ordinal()]) {
         case 1 -> NativeEquipmentSlot.MAIN_HAND;
         case 2 -> NativeEquipmentSlot.OFF_HAND;
         case 3 -> NativeEquipmentSlot.FEET;
         case 4 -> NativeEquipmentSlot.LEGS;
         case 5 -> NativeEquipmentSlot.CHEST;
         case 6 -> NativeEquipmentSlot.HEAD;
         default -> throw new IllegalArgumentException("Unsupported equipment slot: " + this.aQv);
      };
   }

   public abstract void addMetadataPackets(List var1);

   protected final void method3016(List var1, float var2, float var3, float var4) {
      EntityMetadataField var5 = this.aQv == EquipmentSlot.HAND ? ArmorStand.RIGHT_ARM_POSE : ArmorStand.HEAD_POSE;
      FastPacketSender var6 = NativePacketsHolder.PE().Pw();

      for (EntityViewer var8 : var1) {
         if (var8.kW() && var8.kX()) {
            try {
               var6.sendArmorStandPose(var8.li, this.aQn, var5, var2, var3, var4);
            } catch (IllegalStateException var10) {
               method3011(var8, var10);
            }
         }
      }
   }

   public abstract void addEquipmentPackets(boolean var1, @Nullable LivingEntity var2, List var3);

   protected final void method3018(List var1, float var2, DoubleVector3 var3, DoubleVector3 var4) {
      boolean var5 = this.aQq;
      boolean var6 = this.aQp;
      if (var5 || var6) {
         int var7 = this.aQn;
         double var8 = var4.pW;
         double var10 = var4.pX;
         double var12 = var4.pY;
         int var14 = this.aQm;
         double var15 = var3.pW;
         double var17 = var3.pX;
         double var19 = var3.pY;
         FastPacketSender var21 = NativePacketsHolder.PE().Pw();

         for (EntityViewer var23 : var1) {
            if (var23.kW() && var23.kZ()) {
               try {
                  if (var5 && var6) {
                     var21.batch(var23.li, var15x -> {
                        var15x.entityTeleport(var7, var8, var10, var12, var2, 0.0F, false);
                        var15x.entityTeleport(var14, var15, var17, var19, 0.0F, 0.0F, false);
                     });
                  } else if (var5) {
                     var21.sendEntityTeleport(var23.li, var7, var8, var10, var12, var2, 0.0F, false);
                  } else {
                     var21.sendEntityTeleport(var23.li, var14, var15, var17, var19, 0.0F, 0.0F, false);
                  }
               } catch (IllegalStateException var25) {
                  method3011(var23, var25);
               }
            }
         }
      }
   }

   public void Ny() {
      DoubleVector3 var1 = this.aQl.acy();
      DoubleVector3 var2 = this.aQk.acy();
      method3025(var1, this.aQg, aPL);
      method3025(var2, this.aQh, aPL);
      this.aQg = var1.acy();
      this.aQh = var2.acy();
      var1 = this.aQl.acy();
      var2 = this.aQk.acy();
      method3025(var1, this.aQi, aPM);
      method3025(var2, this.aQj, aPM);
      this.aQi = var1.acy();
      this.aQj = var2.acy();
   }

   public abstract void addTeleportPackets(List var1);

   protected final void method3020(List var1, byte var2) {
      FastPacketSender var3 = NativePacketsHolder.PE().Pw();

      for (EntityViewer var5 : var1) {
         if (var5.kW() && var5.kX()) {
            try {
               var3.sendEntitySharedFlags(var5.li, this.aQn, var2);
            } catch (IllegalStateException var7) {
               method3011(var5, var7);
            }
         }
      }
   }

   public abstract void onViewerAdded(Player var1);

   public abstract void sendPacket(Player var1, Object var2);

   public static byte toPackedAngle(float var0) {
      return (byte)(var0 * 256.0F / 360.0F);
   }

   public void method3023(List var1, LivingEntity var2) {
      Object var3 = NativePacketsHolder.PE().Pv().passengers(this.aQn, new int[]{var2.getEntityId()});

      for (int var4 = 0; var4 < var1.size(); var4++) {
         EntityViewer var5 = (EntityViewer)var1.get(var4);
         if (var5.kW()) {
            NativePacketsHolder.PE().method1867(var5.li, var3);
         }
      }
   }

   public void method3024(List var1, boolean var2) {
      if (var2 != this.aQf) {
         this.aQf = var2;
         this.addTeleportPackets(var1);
      }
   }

   public void eS(@Nullable String var1) {
      this.playerSkin = var1;
   }

   public void Nz() {
      this.aQr = true;
      this.aQp = true;
      this.aQq = true;
   }

   public void NA() {
      this.aQr = true;
   }

   public void setColor(int color) {
      this.arJ = color;
      this.Nr();
   }

   public boolean getEnchanted() {
      return this.aQu;
   }

   public void setEnchanted(boolean enchanted) {
      this.aQu = enchanted;
      this.Nr();
   }

   public static void method3025(DoubleVector3 var0, DoubleVector3 var1, float var2) {
      var0.pW = var1.pW + (var0.pW - var1.pW) / var2;
      var0.pX = var1.pX + (var0.pX - var1.pX) / var2;
      var0.pY = var1.pY + (var0.pY - var1.pY) / var2;
   }

   private static ItemStack createTextureItem(BufferedImage var0, NamespacedKey var1, List var2, @Nullable List var3) {
      Log.devLog(var1.asString());
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();

      for (Rect2d var7 : var2) {
         BufferedImage var8 = var0.getSubimage((int)var7.getMin().x, (int)var7.getMin().y, (int)var7.getMax().x, (int)var7.getMax().y);
         int[] var9 = ImagePixelUtils.toArgbArray(var8);

         for (int var13 : var9) {
            int var14 = var13 >> 24 & 0xFF;
            if (var14 > 0) {
               int var15 = var13 >> 16 & 0xFF;
               int var16 = var13 >> 8 & 0xFF;
               int var17 = var13 & 0xFF;
               var4.add(Color.fromRGB(var15, var16, var17));
            } else {
               var4.add(Color.BLACK);
            }
         }
      }

      if (var3 != null) {
         for (Rect2d var20 : var3) {
            BufferedImage var21 = var0.getSubimage((int)var20.getMin().x, (int)var20.getMin().y, (int)var20.getMax().x, (int)var20.getMax().y);
            int[] var22 = ImagePixelUtils.toArgbArray(var21);

            for (int var26 : var22) {
               int var27 = var26 >> 24 & 0xFF;
               if (var27 > 0) {
                  int var28 = var26 >> 16 & 0xFF;
                  int var29 = var26 >> 8 & 0xFF;
                  int var30 = var26 & 0xFF;
                  var4.add(Color.fromRGB(var28, var29, var30));
                  var5.add(true);
               } else {
                  var4.add(Color.RED);
                  var5.add(false);
               }
            }
         }
      }

      ItemStack var19 = new ItemStack(Material.PLAYER_HEAD);
      ItemOperationsService.getRegistryBridge().method3437(var19, var1.toString());
      ItemOperationsService.getRegistryBridge().method3439(var19, var4);
      ItemOperationsService.getRegistryBridge().method3442(var19, var5);
      return var19;
   }

   @NotNull
   public static PlayerTextures getTextures(Player var0) {
      Object var1;
      if (EnvironmentDetector.paperAdventure) {
         var1 = var0.getPlayerProfile();
      } else {
         try {
            var1 = (PlayerProfile)var0.getClass().getMethod("getPlayerProfile").invoke(var0);
         } catch (ReflectiveOperationException var3) {
            var1 = Bukkit.createPlayerProfile(var0.getUniqueId());
         }
      }

      if (!var1.isComplete() || var1.getTextures().isEmpty()) {
         var1 = Bukkit.createPlayerProfile(var0.getUniqueId());
      }

      return var1.getTextures();
   }

   public static String getSkinUrl(Player var0) {
      try {
         URL var1 = getTextures(var0).getSkin();
         if (var1 == null) {
            return "";
         }

         JsonObject var2 = new JsonObject();
         var2.addProperty("url", var1.toString());
         JsonObject var3 = new JsonObject();
         var3.add("SKIN", var2);
         JsonObject var4 = new JsonObject();
         var4.add("textures", var3);
         return Base64.getEncoder().encodeToString(var4.toString().getBytes(StandardCharsets.UTF_8));
      } catch (RuntimeException var5) {
         Log.warn("Could not read V2 player model skin for " + var0.getName(), var5);
         return "";
      }
   }

   @Nullable
   public static String eT(String var0) {
      if (var0.isEmpty()) {
         return null;
      }

      if (var0.startsWith("https://textures.minecraft.net/texture/")) {
         return var0;
      }

      if (var0.length() < 100) {
         Player var5 = Bukkit.getPlayer(var0);
         if (var5 == null) {
            throw new IllegalStateException("Invalid Base64 value and player not found: " + var0);
         }

         URL var6 = getTextures(var5).getSkin();
         return var6 != null ? var6.toString() : null;
      } else {
         String var1 = new String(Base64.getDecoder().decode(var0));
         JsonObject var2 = JsonParser.parseString(var1).getAsJsonObject();
         JsonObject var3 = var2.getAsJsonObject("textures");
         if (var3 != null && var3.has("SKIN")) {
            JsonObject var4 = var3.getAsJsonObject("SKIN");
            return var4.get("url").getAsString();
         } else {
            throw new IllegalStateException("SKIN object not present in textures");
         }
      }
   }

   public static BufferedImage downloadImage(String var0) {
      try (HttpClient var1 = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5L)).build()) {
         HttpResponse var2 = var1.send(HttpRequest.newBuilder(URI.create(var0)).timeout(Duration.ofSeconds(8L)).GET().build(), BodyHandlers.ofByteArray());
         if (var2.statusCode() != 200 || ((byte[])var2.body()).length > 1048576) {
            throw new IllegalStateException("Invalid skin response: HTTP " + var2.statusCode());
         } else {
            BufferedImage var3 = ImageIO.read(new ByteArrayInputStream((byte[])var2.body()));
            if (var3 == null) {
               throw new IllegalStateException("Invalid skin PNG");
            } else {
               return var3;
            }
         }
      }
   }

   public ItemStack method3027(int var1, @NotNull String var2) {
      return method3028(var1, var2);
   }

   public static ItemStack method3028(int var0, @NotNull String var1) {
      if (!Settings.get().emoteModernMethod) {
         ItemStack var8 = createSkull(var1);
         ItemStackUtils.method7259(var8, var0);
         return var8;
      }

      PlayerSkinData var3;
      try {
         var3 = (PlayerSkinData)aPC.getIfPresent(var1);
         if (var3 == null) {
            boolean var5 = false;
            String var6 = eT(var1);
            BufferedImage var4;
            if (var6 != null) {
               var4 = downloadImage(var6);
               if (var4.getHeight() == 64) {
                  var5 = ImagePixelUtils.hasTransparentMarker(var4);
               } else {
                  var4 = CustomEntityManager.getSteveSkinImage();
               }
            } else {
               var4 = CustomEntityManager.getSteveSkinImage();
            }

            var3 = new PlayerSkinData(var1, var4, var5);
            aPC.put(var1, var3);
         }
      } catch (Exception var7) {
         Log.warn("Could not load player skin; using the default skin", var7);
         var3 = new PlayerSkinData(var1, CustomEntityManager.getSteveSkinImage(), false);
         aPC.put(var1, var3);
      }

      String var9 = Settings.get().emotesUseArmorStands ? "player_model/" : "player_model_v2/";
      ItemStack var2;
      if (var0 == CustomBlockStateSettings.bvr) {
         var2 = createTextureItem(var3.aQB, new NamespacedKey("_iainternal", var9 + "head"), aPN, aPO);
      } else if (var0 == CustomBlockStateSettings.bvt) {
         var2 = createTextureItem(var3.aQB, new NamespacedKey("_iainternal", var9 + "torso"), aPP, aPQ);
      } else if (var0 == CustomBlockStateSettings.bvs) {
         if (!var3.aQC) {
            var2 = createTextureItem(var3.aQB, new NamespacedKey("_iainternal", var9 + "left_arm"), aPR, aPS);
         } else {
            var2 = createTextureItem(var3.aQB, new NamespacedKey("_iainternal", var9 + "left_arm_slim"), aPT, aPU);
         }
      } else if (var0 == CustomBlockStateSettings.bvw) {
         if (!var3.aQC) {
            var2 = createTextureItem(var3.aQB, new NamespacedKey("_iainternal", var9 + "right_arm"), aPV, aPW);
         } else {
            var2 = createTextureItem(var3.aQB, new NamespacedKey("_iainternal", var9 + "right_arm_slim"), aPX, aPY);
         }
      } else if (var0 == CustomBlockStateSettings.bvu) {
         var2 = createTextureItem(var3.aQB, new NamespacedKey("_iainternal", var9 + "left_leg"), aPZ, aQa);
      } else if (var0 == CustomBlockStateSettings.bvv) {
         var2 = createTextureItem(var3.aQB, new NamespacedKey("_iainternal", var9 + "right_leg"), aQb, aQc);
      } else {
         var2 = createSkull(var1);
      }

      return var2;
   }

   @Generated
   public int getColor() {
      return this.arJ;
   }
}
