package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.Version;
import beer.devs.fastnbt.nms.nbt.NItem;
import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.PlayerInputPacketView;
import beer.devs.nativepackets.api.SchedulerAccess;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins.ProtectionPlugins;
import dev.lone.itemsadder.block.BlockUtils;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.CustomMobSettings;
import dev.lone.itemsadder.entity.EntityEventSinkHolder;
import dev.lone.itemsadder.entity.EntityMathUtils;
import dev.lone.itemsadder.entity.ModelRiderController;
import dev.lone.itemsadder.entity.MountTrackingListener;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.network.ConnectionRecord;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.PlayerBossBar;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.world.InstrumentRegistry;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class ItemThrowManager extends ItemListenerBase {
   private final Plugin afu;
   private final ContentManager afv;
   private PacketListenerHandle afw;
   private PacketListenerHandle afx;
   private volatile boolean cV = true;
   public static final Map afy = new ConcurrentHashMap();
   public static final Map afz = new ConcurrentHashMap();
   public static final Map afA = new ConcurrentHashMap();
   private static final Set afB = ConcurrentHashMap.newKeySet();
   private static final Map afC = new ConcurrentHashMap();
   private static final Set afD = ConcurrentHashMap.newKeySet();
   private static final Map afE = new ConcurrentHashMap();

   public ItemThrowManager(Plugin var1, ContentManager var2) {
      this.afu = var1;
      this.afv = var2;
      EventRegistrar.registerIfAbsent(this, var1);
      var2.aK.method933(new ItemHandlerBinding(BlockPlaceEvent.class, this::method1461));
      var2.aK.method933(new ItemHandlerBinding(BlockMultiPlaceEvent.class, this::method1461));
      var2.aK.method933(new ItemHandlerBinding(PlayerInteractEvent.class, this::method1462));
      this.Bd();
      SchedulerUtils.runLater(this::Bc, 1L);
   }

   private void Bc() {
      if (this.cV) {
         for (ThrowTask var2 : afy.values()) {
            if (var2.agb.AX()) {
               var2.sK = EntityMathUtils.method8647(var2.age, var2.agb.AY(), var2.sK);
            }
         }
      }
   }

   private void Bd() {
      if (Version.isOlderThan(Version.v1_21_3)) {
         this.afw = NativePacketsHolder.PE()
            .method1888(
               Main.getInstance(),
               PacketSelector.exact(PacketTypes.PLAYER_INPUT),
               ListenerPriority.NORMAL,
               true,
               ListenerFailurePolicy.PASS_ORIGINAL,
               this::method1453
            );
      }

      if (Version.isAtLeast(Version.v1_21_3)) {
         afB.clear();
         Bukkit.getOnlinePlayers().forEach(ItemThrowManager::aQ);
         this.afx = NativePacketsHolder.PE()
            .method1888(
               Main.getInstance(),
               PacketSelector.exact(PacketTypes.PLAYER_INPUT),
               ListenerPriority.NORMAL,
               true,
               ListenerFailurePolicy.PASS_ORIGINAL,
               var1 -> {
                  UUID var2 = NativePacketsHolder.PE().method1883(var1);
                  if (this.cV && var2 != null && afB.contains(var2)) {
                     PlayerInputPacketView var3 = (PlayerInputPacketView)var1.view(PlayerInputPacketView.class);
                     float var4 = var3.forward();
                     float var5 = var3.sideways();
                     afA.compute(
                        var2,
                        (var3x, var4x) -> ThrowInputState.copyWithFlags(
                           var4x, var4 > 0.0F, var4 < 0.0F, var5 > 0.0F, var5 < 0.0F, var3.jumping(), var3.dismounting(), System.nanoTime()
                        )
                     );
                  }
               }
            );
         SchedulerUtils.scheduleRepeating(
            var1 -> {
               for (Entry var3 : afy.entrySet()) {
                  Player var4 = (Player)var3.getKey();
                  if (var4.getVehicle() != null
                     && (var4.getVehicle().getType() == EntityType.SLIME || var4.getVehicle() instanceof ArmorStand)
                     && afy.containsKey(var4)) {
                     if (!ContentManager.method8084().field83) {
                        SchedulerUtils.runOnMainThread(() -> {
                           var4.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 5));
                           aS(var4);
                        });
                     } else {
                        ThrowTask var5 = (ThrowTask)afy.get(var4);
                        if (var5 != null && var5.age != null) {
                           ThrowInputState var6 = (ThrowInputState)afA.get(var4.getUniqueId());
                           if (var6 != null) {
                              ArmorStand var7 = var5.age;
                              if (var5.agb.lv() > 0.0F
                                 && !var6.Bi()
                                 && !var6.Bk()
                                 && (var7.isInWater() || !var7.getLocation().subtract(0.0, 2.0, 0.0).getBlock().getType().isSolid())) {
                                 Vector var8 = var7.getVelocity();
                                 var8.setY(method1460(false, false, var5.agb.lv(), var7.isInWater()));
                                 var7.setVelocity(var8);
                              }

                              Location var24 = var7.getLocation().subtract(0.0, 1.0, 0.0);
                              if (var6.Bk()) {
                                 if (var5.agb.lv() <= 0.0F
                                    && (
                                       var24.getBlock().getType().isSolid()
                                          || var24.getBlock().getType() == Material.WATER
                                          || var24.getBlock().getType() == Material.LAVA
                                    )) {
                                    aS(var4);
                                    continue;
                                 }

                                 if (var5.agb.lv() > 0.0F) {
                                    Vector var9 = var4.getLocation().getDirection();
                                    var9 = var9.multiply(var5.agb.lv());
                                    var9.setY(-1);
                                    var7.setVelocity(var9);
                                 }
                              }

                              NItem var27 = var5.agf;
                              if (var5.agb.Ba() == -1 || var27.getInt("Fuel") > 0) {
                                 var5.agh = var7.getLocation().getYaw();
                                 ProjectileTypeHelper.method3218(var7, var4.getLocation().getYaw());
                                 if (var7.getPassenger() != null && var7.getPassenger().getType() == EntityType.SLIME) {
                                    ProjectileTypeHelper.method3218(var7.getPassenger(), var7.getLocation().getYaw());
                                 }

                                 if ((var6.kL() || var6.kM() || var6.Bi()) && var5.agb.Ba() != -1 && System.currentTimeMillis() - var5.agg >= 1000L) {
                                    var5.agg = System.currentTimeMillis();
                                    var27.setInt("Fuel", var27.getInt("Fuel") - 1);
                                    PlayerBossBar var10 = aR(var4);
                                    var10.show();
                                    var10.setTitle(
                                       Main.aFi.lj("fuel-indicator").replace("{value}", var27.getInt("Fuel") + "").replace("{max}", var5.agb.Ba() + "")
                                    );
                                    var27.save();
                                    var7.setHelmet(var27.getItem());
                                 }

                                 if (var6.kL()) {
                                    boolean var28 = var7.getLocation().subtract(0.0, 2.0, 0.0).getBlock().getType().isSolid();
                                    double var11 = var5.agb.AV();
                                    if (var5.agb.lv() > 0.0F && !var28) {
                                       var11 = var5.agb.lv();
                                    }

                                    Vector var13 = var7.getLocation().getDirection();
                                    if (var5.agb.lv() > 0.0F) {
                                       var13.setY(0.078);
                                    } else {
                                       var13.setY(-1);
                                    }

                                    var13 = var13.multiply(var11);
                                    if (var5.agb.lv() > 0.0F) {
                                       var13.setY(method1460(var6.Bi(), var6.Bk(), var5.agb.lv(), var7.isInWater()));
                                    }

                                    if (var5.agb.AX() && var5.agb.lv() <= 0.0F) {
                                       var13.setY(var7.getVelocity().getY());
                                    }

                                    var7.setVelocity(var13);
                                    if (var5.agb.lv() > 0.0F && !var28) {
                                       continue;
                                    }
                                 } else if (var6.kM()) {
                                    Vector var29 = var7.getLocation().getDirection();
                                    var29 = var29.multiply(-var5.agb.AV() / 2.0F);
                                    var29.setY(var7.getVelocity().getY());
                                    var7.setVelocity(var29);
                                 } else if (var5.agb.AS() > 0) {
                                    var5.method9650(var7.getLocation());
                                 }

                                 if (var6.Bi()) {
                                    if (var24.getBlock().getType().isSolid() && !var7.getLocation().subtract(0.0, 0.0, 0.0).getBlock().getType().isSolid()) {
                                       if (var5.agb.AW() > 0.0F) {
                                          Vector var33 = var7.getVelocity();
                                          var33.setY(1);
                                          var33 = var33.multiply(var5.agb.AW());
                                          var7.setVelocity(var33);
                                       }
                                    } else if (var5.agb.lv() > 0.0F) {
                                       Vector var31 = var4.getLocation().getDirection();
                                       var31.setY(1);
                                       var31 = var31.multiply(var5.agb.lv());
                                       var7.setVelocity(var31);
                                    }
                                 }
                              }
                           }
                        } else {
                           aS(var4);
                        }
                     }
                  }
               }

               if (CustomEntityManager.isInitialized() && !afA.isEmpty()) {
                  Map var16 = CustomEntityManager.getInstance().fM();
                  ArrayList var17 = new ArrayList();
                  synchronized (var16) {
                     for (Entry var21 : var16.entrySet()) {
                        if (var21.getKey() instanceof Player var23) {
                           var17.add(Map.entry(var23, (ModelRiderController)var21.getValue()));
                        }
                     }
                  }

                  for (Entry var20 : var17) {
                     ModelRiderController var22 = (ModelRiderController)var20.getValue();
                     NativePacketsHolder.PE().IP().entity(this.afu, var22.xo, () -> this.method1459((Player)var20.getKey(), var22), () -> {});
                  }
               }
            },
            1L,
            1L
         );
      }
   }

   private void method1453(PacketEvent var1) {
      UUID var2 = NativePacketsHolder.PE().method1883(var1);
      if (this.cV && var2 != null) {
         long var3 = var1.connection().id();
         Player var5 = NativePacketsHolder.PE().method1885(var2, var3);
         if (var5 != null) {
            ThrowTask var6 = (ThrowTask)afy.get(var5);
            ThrowTaskRef var7 = (ThrowTaskRef)afE.get(var5);
            if (var6 != null && var7 != null && var7.Bf() == var6 && var7.bl() == var3) {
               PlayerInputPacketView var8 = (PlayerInputPacketView)var1.view(PlayerInputPacketView.class);
               ThrowAimState var9 = new ThrowAimState(var2, var7, var8.jumping(), var8.dismounting(), var8.forward() > 0.0F, var8.forward() < 0.0F);
               afC.merge(
                  var5,
                  var9,
                  (var0, var1x) -> var0.taskRef() == var1x.taskRef()
                     ? new ThrowAimState(var1x.playerId(), var1x.taskRef(), var0.Bi() || var1x.Bi(), var0.Bj() || var1x.Bj(), var1x.kL(), var1x.kM())
                     : var1x
               );
               if (!afD.add(var5) || this.aO(var5)) {
                  if (afE.get(var5) == var7 && afy.get(var5) == var6) {
                     var1.cancel();
                  }
               }
            } else {
               if (var7 != null) {
                  afE.remove(var5, var7);
               }

               if (var6 != null && afD.add(var5)) {
                  this.aO(var5);
               }
            }
         }
      }
   }

   private boolean aO(Player var1) {
      try {
         NativePacketsHolder.PE().IP().entity(Main.getInstance(), var1, () -> {
            try {
               ThrowAimState var2 = (ThrowAimState)afC.remove(var1);
               if (var2 != null) {
                  this.method1454(var2);
               } else {
                  this.aP(var1);
               }
            } finally {
               afD.remove(var1);
               if (afC.containsKey(var1) && afD.add(var1)) {
                  this.aO(var1);
               }
            }
         }, () -> {
            afC.remove(var1);
            afD.remove(var1);
            afE.remove(var1);
         });
         return true;
      } catch (RuntimeException | LinkageError var3) {
         afC.remove(var1);
         afD.remove(var1);
         afE.remove(var1);
         return false;
      }
   }

   private void aP(Player var1) {
      if (this.cV && Version.isOlderThan(Version.v1_21_3)) {
         if (!NativePacketsHolder.PE().IP().ownsEntity(var1)) {
            afE.remove(var1);
         } else {
            ThrowTask var2 = (ThrowTask)afy.get(var1);
            Entity var3 = var1.getVehicle();
            ConnectionRecord var4 = NativePacketsHolder.PE().cB(var1);
            if (var2 != null
               && var3 != null
               && var4 != null
               && NativePacketsHolder.PE().method1873(var4)
               && var1.isOnline()
               && (var3.getType() == EntityType.SLIME || var3 instanceof ArmorStand)
               && var2.agd != null
               && var2.age != null
               && NativePacketsHolder.PE().IP().ownsEntity(var3)
               && NativePacketsHolder.PE().IP().ownsEntity(var2.agd)
               && NativePacketsHolder.PE().IP().ownsEntity(var2.age)
               && var2.age.isValid()
               && var3.getUniqueId().equals(var2.agd.getUniqueId())
               && var2.age.getWorld() == var1.getWorld()) {
               afE.put(var1, new ThrowTaskRef(var2, var4.bl(), var3.getUniqueId()));
            } else {
               afE.remove(var1);
            }
         }
      }
   }

   private void method1454(ThrowAimState var1) {
      Player var2 = NativePacketsHolder.PE().method1885(var1.playerId(), var1.taskRef().bl());
      SchedulerAccess var3 = NativePacketsHolder.PE().IP();
      if (this.cV && var2 != null && var3.ownsEntity(var2)) {
         ThrowTaskRef var4 = var1.taskRef();
         ThrowTask var5 = (ThrowTask)afy.get(var2);
         if (afE.get(var2) == var4 && var5 == var4.Bf()) {
            ArmorStand var6 = var5 == null ? null : var5.age;
            Entity var7 = var2.getVehicle();
            if (var6 == null
               || var7 == null
               || !var3.ownsEntity(var6)
               || !var3.ownsEntity(var7)
               || var7.getType() != EntityType.SLIME && !(var7 instanceof ArmorStand)
               || !var2.isOnline()
               || !var6.isValid()
               || var5.agd == null
               || !var3.ownsEntity(var5.agd)
               || !var7.getUniqueId().equals(var4.Bg())
               || !var7.getUniqueId().equals(var5.agd.getUniqueId())
               || var6.getWorld() != var2.getWorld()) {
               afE.remove(var2, var4);
            } else if (!ContentManager.method8084().field83) {
               var2.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 5));
               var2.leaveVehicle();
               var2.removeMetadata("onIAVehicle", Main.getInstance());
            } else {
               boolean var8 = var1.Bi();
               boolean var9 = var1.Bj();
               boolean var10 = var1.kL();
               boolean var11 = var1.kM();
               Location var12 = var6.getLocation().subtract(0.0, 1.0, 0.0);
               if (var5.agb.lv() > 0.0F && !var8 && !var9 && (var6.isInWater() || !var6.getLocation().subtract(0.0, 2.0, 0.0).getBlock().getType().isSolid())) {
                  Vector var13 = var6.getVelocity();
                  var13.setY(method1460(false, false, var5.agb.lv(), var6.isInWater()));
                  var6.setVelocity(var13);
               }

               if (var9) {
                  if (var5.agb.lv() <= 0.0F
                     && (var12.getBlock().getType().isSolid() || var12.getBlock().getType() == Material.WATER || var12.getBlock().getType() == Material.LAVA)) {
                     method1456(var2, true);
                     return;
                  }

                  if (var5.agb.lv() > 0.0F) {
                     Vector var18 = var2.getLocation().getDirection().multiply(var5.agb.lv());
                     var18.setY(-1);
                     var6.setVelocity(var18);
                  }
               }

               NItem var19 = var5.agf;
               if (var5.agb.Ba() == -1 || var19.getInt("Fuel") > 0) {
                  var5.agh = var6.getLocation().getYaw();
                  ProjectileTypeHelper.method3218(var6, var2.getLocation().getYaw());
                  if (var6.getPassenger() != null && var6.getPassenger().getType() == EntityType.SLIME) {
                     ProjectileTypeHelper.method3218(var6.getPassenger(), var6.getLocation().getYaw());
                  }

                  if ((var10 || var11 || var8) && var5.agb.Ba() != -1 && System.currentTimeMillis() - var5.agg >= 1000L) {
                     var5.agg = System.currentTimeMillis();
                     var19.setInt("Fuel", var19.getInt("Fuel") - 1);
                     PlayerBossBar var14 = aR(var2);
                     var14.show();
                     var14.setTitle(Main.aFi.lj("fuel-indicator").replace("{value}", var19.getInt("Fuel") + "").replace("{max}", var5.agb.Ba() + ""));
                     var19.save();
                     var6.setHelmet(var19.getItem());
                  }

                  if (var10) {
                     boolean var20 = var6.getLocation().subtract(0.0, 2.0, 0.0).getBlock().getType().isSolid();
                     double var15 = var5.agb.AV();
                     if (var5.agb.lv() > 0.0F && !var20) {
                        var15 = var5.agb.lv();
                     }

                     Vector var17 = var6.getLocation().getDirection();
                     var17.setY(var5.agb.lv() > 0.0F ? 0.078 : -1.0);
                     var17.multiply(var15);
                     if (var5.agb.lv() > 0.0F) {
                        var17.setY(method1460(var8, var9, var5.agb.lv(), var6.isInWater()));
                     }

                     if (var5.agb.AX() && var5.agb.lv() <= 0.0F) {
                        var17.setY(var6.getVelocity().getY());
                     }

                     var6.setVelocity(var17);
                     if (var5.agb.lv() > 0.0F && !var20) {
                        return;
                     }
                  } else if (var11) {
                     Vector var21 = var6.getLocation().getDirection().multiply(-var5.agb.AV() / 2.0F);
                     var21.setY(var6.getVelocity().getY());
                     var6.setVelocity(var21);
                  } else if (var5.agb.AS() > 0) {
                     var5.method9650(var6.getLocation());
                  }

                  if (var8) {
                     if (var12.getBlock().getType().isSolid() && !var6.getLocation().getBlock().getType().isSolid()) {
                        if (var5.agb.AW() > 0.0F) {
                           Vector var23 = var6.getVelocity();
                           var23.setY(1);
                           var6.setVelocity(var23.multiply(var5.agb.AW()));
                        }
                     } else if (var5.agb.lv() > 0.0F) {
                        Vector var22 = var2.getLocation().getDirection();
                        var22.setY(1);
                        var6.setVelocity(var22.multiply(var5.agb.lv()));
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void unregister() {
      this.cV = false;
      SchedulerUtils.cancelTasksFor(ItemThrowManager.class);
      if (this.afw != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.afw);
      }

      if (this.afx != null) {
         NativePacketsHolder.PE().method1889(Main.getInstance(), this.afx);
      }

      afB.clear();
      afA.clear();
      afC.clear();
      afD.clear();
      afE.clear();
      super.unregister();
   }

   private static void aQ(Player var0) {
      afB.add(var0.getUniqueId());
   }

   private static PlayerBossBar aR(Player var0) {
      PlayerBossBar var1 = (PlayerBossBar)afz.get(var0);
      if (var1 == null) {
         afz.put(var0, new PlayerBossBar(var0, "", BarColor.BLUE, BarStyle.SOLID));
         var1 = (PlayerBossBar)afz.get(var0);
      }

      return var1;
   }

   private static void ax(Player var0) {
      UUID var1 = var0.getUniqueId();
      afA.remove(var1);
      afC.remove(var0);
      afD.remove(var0);
      afE.remove(var0);
      ThrowTask var2 = (ThrowTask)afy.remove(var0);
      if (var2 != null) {
         var2.Bs();
      }

      if (afz.containsKey(var0)) {
         ((PlayerBossBar)afz.get(var0)).removeViewer();
      }

      afz.remove(var0);
   }

   void method1455(ItemThrowAction var1, ItemStack var2, Block var3, Player var4, EquipmentSlot var5) {
      if (!this.aY(var4)) {
         Location var6 = var3.getLocation();
         var6.setX(var6.getX() + 0.5);
         var6.setY(var6.getY());
         var6.setZ(var6.getZ() + 0.5);
         if (var1.At()) {
            var6.setYaw(BlockFaceUtils.yawToRotationDegrees(var4.getLocation().getYaw(), false));
         } else {
            var6.setYaw(var4.getLocation().getYaw());
         }

         ArmorStand var7 = (ArmorStand)var3.getWorld().spawn(var6, ArmorStand.class, var3x -> {
            var3x.setVisible(false);
            var3x.setAI(true);
            var3x.setRemoveWhenFarAway(false);
            var3x.setCanPickupItems(false);
            var3x.setInvulnerable(false);
            var3x.setSilent(true);
            var3x.setSmall(var1.Au());
            var3x.setGravity(true);
            var3x.setCollidable(true);
            ItemStack var4x;
            if (var1.aeT.getType() == var2.getType()) {
               var4x = var2.clone();
            } else {
               var4x = var1.aeT.Ds();
            }

            CustomItemDefinition.method8767(var4x, var4.getUniqueId().toString());
            NItem var5x = new NItem(var4x);
            if (!var5x.hasKey("Fuel")) {
               var5x.setInt("Fuel", var1.AZ());
            }

            var5x.save();
            SchedulerUtils.runLaterSync(() -> var3x.setHelmet(var4x), 2L);
            var1.method367(var3x);
         });
         var1.acY.apR.play(var4, var6);
         PlayerInteractionFacade.method5310(var4, var5);
         if (var4.getGameMode() != GameMode.CREATIVE) {
            var2.setAmount(var2.getAmount() - 1);
         }

         this.aZ(var4);
         var1.method361(var7, var1.aeT);
         this.afv.aV.aK(var4);
      }
   }

   static void method1456(Player var0, boolean var1) {
      Location var2;
      if (var0.getVehicle() != null) {
         var2 = var0.getVehicle().getLocation();
      } else {
         var2 = var0.getLocation().clone();
      }

      var2.add(0.0, 0.5, 0.0);
      ThrowTask var3 = (ThrowTask)afy.get(var0);
      ax(var0);
      if (var3 != null) {
         ArmorStand var4 = var3.age;
         var0.leaveVehicle();
         var0.teleport(var2);
         if (var1 && var4 != null) {
            ProjectileTypeHelper.method3217(var4, false);
         }

         SchedulerUtils.runLaterSync(() -> var3.agb.method367(var4), 20L);
      } else {
         var0.leaveVehicle();
         var0.teleport(var2);
      }
   }

   static void aS(Player var0) {
      Location var1;
      if (var0.getVehicle() != null) {
         var1 = var0.getVehicle().getLocation();
      } else {
         var1 = var0.getLocation().clone();
      }

      var1.add(0.0, 0.5, 0.0);
      SchedulerUtils.runOnMainThread(() -> {
         ThrowTask var2 = (ThrowTask)afy.get(var0);
         ax(var0);
         if (var2 != null) {
            ArmorStand var3 = var2.age;
            var0.leaveVehicle();
            var0.teleport(var1);
            if (var3 != null) {
               ProjectileTypeHelper.method3217(var3, false);
            }

            SchedulerUtils.runLaterSync(() -> var2.agb.method367(var3), 20L);
         }
      });
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   public void method1457(PlayerSwapHandItemsEvent var1) {
      Player var2 = var1.getPlayer();
      ThrowTask var3 = (ThrowTask)afy.get(var2);
      if (var3 != null && var3.agb.lv() > 0.0F) {
         var1.setCancelled(true);
         if (var3.agc.isOverLimit(System.nanoTime())) {
            aS(var2);
         }
      } else {
         ModelRiderController var4 = CustomEntityManager.isInitialized() ? CustomEntityManager.getInstance().getRiderOf(var2) : null;
         if (var4 != null && var4.getCustomEntity().qK.getFloatValue() > 0.0F) {
            var1.setCancelled(true);
            if (!var4.isLocked() && var4.Nb.isOverLimit(System.nanoTime())) {
               CustomEntityManager.getInstance().removePassenger(var2);
            }
         }
      }
   }

   private static boolean method1458(boolean var0, boolean var1, boolean var2, boolean var3) {
      return !var0 && !var1 && (var2 || var3);
   }

   private void method1459(Player var1, ModelRiderController var2) {
      SchedulerAccess var3 = NativePacketsHolder.PE().IP();
      LivingEntity var4 = var2.xo;
      if (this.cV && var3.ownsEntity(var1) && var2.MZ == var1 && var4.isValid()) {
         ThrowInputState var5 = (ThrowInputState)afA.get(var1.getUniqueId());
         if (var5 != null) {
            CustomMobSettings var6 = var2.getCustomEntity().qK;
            boolean var7 = var6.getFloatValue() > 0.0F;
            boolean var8 = var7 && var6.vL;
            boolean var9 = var7 && (!var8 || var4.isInWater());
            if (var8
               ? !var2.consumeThrowInput(var5)
               : var2.isLocked() || !var5.Bk() || (var2.canControl() || var7) && !method1458(var7, false, var4.isOnGround(), var6.vL)) {
               if (var2.canControl()) {
                  Vector var10 = var4.getVelocity();
                  float var11 = (var5.kN() ? 1 : 0) - (var5.kO() ? 1 : 0);
                  float var12 = (var5.kL() ? 1 : 0) - (var5.kM() ? 1 : 0);
                  float var13 = var6.vN.method67(ProjectileTypeHelper.getAdapter().method3235(var4), var1.getEyeLocation().getYaw(), var11, var5.iq());
                  var4.setRotation(var13, var4.getEyeLocation().getPitch());
                  if (var12 != 0.0F || var6.vN.mt() && var11 != 0.0F) {
                     double var14 = var9 && !var4.isOnGround() ? var6.getFloatValue() : var6.getDoubleValue();
                     Vector var16 = var6.vN.method68(var13, var14, var12, var11);
                     var10.setX(var16.getX()).setZ(var16.getZ());
                  }

                  if (var5.Bi() && var4.isOnGround() && !var8) {
                     var10.setY(0.5);
                  }

                  if (var9 && (!var4.isOnGround() || var8)) {
                     if (var5.Bk()) {
                        var10.setY(-var6.getFloatValue() * (var8 ? 1 : 4));
                     } else if (var5.Bi()) {
                        var10.setY(var6.getFloatValue() * (var8 ? 1 : 4));
                     } else if (var8) {
                        var10.setY(0);
                     }
                  }

                  if (!var7 && !var6.vL && var10.getY() <= 0.0) {
                     var10.setY(-1);
                  }

                  var4.setVelocity(var10);
               }
            } else {
               CustomEntityManager.getInstance().removePassenger(var1);
            }
         }
      }
   }

   private static double method1460(boolean var0, boolean var1, double var2, boolean var4) {
      if (var0) {
         return var2;
      } else {
         return var1 ? -1.0 : (var4 ? 0.0 : 0.078);
      }
   }

   @EventHandler
   void method711(PluginDisableEvent var1) {
      if (var1.getPlugin().getName().equals(this.afu.getName())) {
         for (Player var3 : Bukkit.getOnlinePlayers()) {
            if (var3.getVehicle() != null && afy.containsKey(var3)) {
               Location var4 = var3.getVehicle().getLocation();
               int var5 = InstrumentRegistry.getHeightAt(
                  var3.getVehicle().getLocation().getChunk(), var4.getBlockX() >> 4, var4.getBlockZ() >> 4, HeightMap.MOTION_BLOCKING
               );
               method1456(var3, var5 <= 1);
            }
         }
      }
   }

   @ItemEventHandler
   void method1461(BlockPlaceEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var2.aoh.hasAction("vehicle")) {
         var1.setCancelled(true);
         if (var1.getBlockAgainst() != null) {
            ItemThrowAction var4 = (ItemThrowAction)var2.aoh.getAction("vehicle");
            Block var5 = var1.getBlockPlaced();
            this.method1455(var4, var3, var5, var1.getPlayer(), var1.getHand());
         }
      }
   }

   @ItemEventHandler
   void method1462(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
         if (!var3.getType().isBlock()) {
            if (var2.aoh.hasAction("vehicle")) {
               var1.setCancelled(true);
               Block var4 = var1.getClickedBlock();
               if (var4 != null) {
                  Player var5 = var1.getPlayer();
                  Block var6 = var1.getClickedBlock().getRelative(var1.getBlockFace());
                  if (ProtectionPlugins.canPlaceVehicle(var5, var6)) {
                     if (!BlockFaceUtils.playerOverlapsBlock(var5, var6)) {
                        if (var1.getHand() != EquipmentSlot.OFF_HAND
                           || !ContentManager.method8084().hasBehaviour(var1.getPlayer().getItemInHand(), "furniture")) {
                           if (BlockUtils.findNearestEntityDefault(var6, EntityType.ARMOR_STAND) == null) {
                              ItemThrowAction var7 = (ItemThrowAction)var2.aoh.getAction("vehicle");
                              this.method1455(var7, var3, var6, var1.getPlayer(), var1.getHand());
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   void method1463(PlayerInteractAtEntityEvent var1) {
      if (!ItemStackUtils.method7251(var1)) {
         if (var1.getRightClicked() instanceof ArmorStand) {
            ArmorStand var2 = (ArmorStand)var1.getRightClicked();
            ItemStack var3 = var2.getHelmet();
            if (var3 != null) {
               CustomItemDefinition var4 = this.afv.method8149(var3);
               if (var4 != null) {
                  if (var4.aoh.hasAction("vehicle")) {
                     ItemThrowAction var5 = (ItemThrowAction)var4.aoh.getAction("vehicle");
                     if (var2.getLocation().getBlock().getType() != Material.WATER || !(var5.lv() <= 0.0F) || var5.AX()) {
                        if (ProtectionPlugins.canSitOnVehicle(var1, var3)) {
                           var1.setCancelled(true);
                           NItem var6 = new NItem(var3);
                           Player var7 = var1.getPlayer();
                           if (var5.Ba() != -1) {
                              PlayerBossBar var8 = aR(var7);
                              var8.show();
                              var8.setTitle(Main.aFi.lj("fuel-indicator").replace("{value}", var6.getInt("Fuel") + "").replace("{max}", var5.Ba() + ""));
                              var8.scheduleAutoHide();
                              ItemStack var9 = ItemStackUtils.dn(var7);
                              if (var5.isFuel(var9)) {
                                 int var10 = var5.getFuelAmount(var9);
                                 if (var6.getInt("Fuel") + var10 <= var5.Ba()) {
                                    ItemStack var11 = var5.getFuelReturnItem(var9);
                                    ItemStackUtils.dl(var7);
                                    var6.setInt("Fuel", var6.getInt("Fuel") + var10);
                                    var6.save();
                                    var2.setHelmet(var3);
                                    if (var11 != null) {
                                       ItemStackUtils.method7243(var7, var11);
                                    }

                                    return;
                                 }
                              }
                           }

                           if (var6.getInt("Fuel") != 0) {
                              if (!MountTrackingListener.getInstance().hasMount(var7)) {
                                 double var12 = var5.AU();
                                 if ((int)var12 - 2 != 0) {
                                    Slime var13 = (Slime)var7.getWorld().spawn(var7.getLocation(), Slime.class, var2x -> {
                                       EntityEventSinkHolder.dispatch(var2x, (int)var12 - 1);
                                       var2x.setPersistent(false);
                                       var2x.setAI(false);
                                       var2x.setSilent(true);
                                       var2x.setInvulnerable(true);
                                       var2x.setCustomNameVisible(false);
                                       var2x.setInvisible(true);
                                       var2x.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
                                       var2x.setCustomName("IASlimeVehicle");
                                    });
                                    var2.setPassenger(var13);
                                    var13.setPassenger(var7);
                                    ProjectileTypeHelper.getAdapter().method3265(var7, var13);
                                 } else {
                                    var2.setPassenger(var7);
                                    ProjectileTypeHelper.getAdapter().method3265(var7, var2);
                                 }

                                 ThrowTask var14 = new ThrowTask(var5, var2, var7.getVehicle(), new NItem(var3), System.currentTimeMillis());
                                 afy.put(var7, var14);
                                 this.aP(var7);
                                 var14.Br();
                                 ProjectileTypeHelper.method3217(var2, true);
                                 var5.method367(var2);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
   private void method1464(EntityDismountEvent var1) {
      if (var1.getEntity() instanceof Player var2) {
         ThrowTask var5 = (ThrowTask)afy.get(var2);
         ThrowInputState var4 = (ThrowInputState)afA.get(var2.getUniqueId());
         if (var5 != null && var5.agb.lv() > 0.0F && var5.agd == var1.getDismounted() && var4 != null && var4.Bk()) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   private void method1465(EntityDismountEvent var1) {
      if (var1.getEntity() instanceof Player var2 && !var1.isCancelled()) {
         afE.remove(var2);
      }

      if (var1.getEntity().getType() == EntityType.PLAYER && var1.getDismounted() instanceof ArmorStand && var1.getEntity() instanceof Player var5) {
         if (afy.containsKey(var5)) {
            method1456((Player)var1.getEntity(), false);
         }
      } else if (var1.getEntity().getType() == EntityType.SLIME) {
         if (var1.getEntity().getCustomName() != null && var1.getEntity().getCustomName().equals("IASlimeVehicle")) {
            Entity var7 = var1.getEntity();
            SchedulerUtils.runLaterSync(() -> {
               var7.eject();
               var7.remove();
            }, 1L);
         }
      } else if (var1.getEntity().getType() == EntityType.PLAYER
         && var1.getDismounted().getType() == EntityType.SLIME
         && var1.getDismounted().getCustomName() != null
         && var1.getDismounted().getCustomName().equals("IASlimeVehicle")) {
         Entity var8 = var1.getDismounted();
         Player var4 = (Player)var1.getEntity();
         SchedulerUtils.runLaterSync(() -> {
            var8.remove();
            method1456(var4, false);
         }, 1L);
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   void method1229(PlayerArmorStandManipulateEvent var1) {
      ArmorStand var2 = var1.getRightClicked();
      ItemStack var3 = var2.getHelmet();
      if (CustomItemDefinition.isCustomItem(var3)) {
         if (this.afv.method8149(var3).aoh.hasAction("vehicle")) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   void method1466(EntityDamageEvent var1) {
      if (var1.getEntity().getType() == EntityType.PLAYER) {
         if (var1.getCause() == DamageCause.FALL || var1.getCause() == DamageCause.SUFFOCATION) {
            if (var1.getEntity() instanceof Player var2 && afy.containsKey(var2) && var1.getEntity().getVehicle() != null) {
               var1.setCancelled(true);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   void method515(PlayerJoinEvent var1) {
      if (Version.isAtLeast(Version.v1_21_3)) {
         aQ(var1.getPlayer());
      }
   }

   @EventHandler(priority = EventPriority.MONITOR)
   void method516(PlayerQuitEvent var1) {
      afB.remove(var1.getPlayer().getUniqueId());
      ax(var1.getPlayer());
   }
}
