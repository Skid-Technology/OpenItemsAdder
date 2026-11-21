package dev.lone.itemsadder.item;

import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.NativeSound;
import beer.devs.nativepackets.api.NativeValue;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.PlayerAction;
import beer.devs.nativepackets.api.PlayerActionPacketView;
import beer.devs.nativepackets.api.SoundPacketView;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.resourcepack.JsonAssetWithCallbacks;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.IntTableConstantsG;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.SoundUtils;
import dev.lone.itemsadder.world.BlockEffectsService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.GameEvent;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.GenericGameEvent;

public class BlockSoundListener implements IaListener {
   private final PacketListenerHandle agq;
   private final PacketListenerHandle agr;
   private volatile boolean cV = true;
   private volatile SoundPair[] ags = new SoundPair[0];
   private volatile Map agt = Map.of();
   Map agu = Collections.synchronizedMap(new HashMap());

   public BlockSoundListener() {
      if (Main.eo("BlockSounds")) {
         Log.error("Disabled BlockSounds to avoid issues. Please uninstall it.");
         Log.error("ItemsAdder now supports custom block sounds natively.");
         Log.error("To enable custom block sounds please run /iazip to regenerate the resourcepack.");
      }

      EventRegistrar.registerIfAbsent(this, Main.getInstance());
      this.agq = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(), PacketSelector.exact(PacketTypes.SOUND), ListenerPriority.NORMAL, false, ListenerFailurePolicy.PASS_ORIGINAL, var1 -> {
               if (this.cV && NativePacketsHolder.PE().method1883(var1) != null) {
                  SoundPacketView var2 = (SoundPacketView)var1.view(SoundPacketView.class);
                  NativeValue var3 = var2.sound();

                  for (SoundPair var7 : this.ags) {
                     if (var7.BE().nativeHandle() == var3.nativeHandle()) {
                        var2.sound(var7.BF());
                        return;
                     }
                  }
               }
            }
         );
      this.agr = NativePacketsHolder.PE()
         .method1888(
            Main.getInstance(), PacketSelector.exact(PacketTypes.PLAYER_ACTION), ListenerPriority.NORMAL, false, ListenerFailurePolicy.PASS_ORIGINAL, var1 -> {
               if (this.cV) {
                  PlayerActionPacketView var2 = (PlayerActionPacketView)var1.view(PlayerActionPacketView.class);
                  PlayerAction var3 = var2.action();
                  if (var3 == PlayerAction.START_DESTROY_BLOCK || var3 == PlayerAction.ABORT_DESTROY_BLOCK || var3 == PlayerAction.STOP_DESTROY_BLOCK) {
                     UUID var4 = NativePacketsHolder.PE().method1883(var1);
                     if (var4 != null) {
                        long var5 = var1.connection().id();
                        int var7 = var2.blockX();
                        int var8 = var2.blockY();
                        int var9 = var2.blockZ();
                        Player var10 = NativePacketsHolder.PE().method1885(var4, var5);
                        if (var10 != null) {
                           NativePacketsHolder.PE().IP().entity(Main.getInstance(), var10, () -> {
                              if (this.cV && NativePacketsHolder.PE().method1885(var4, var5) == var10) {
                                 switch (IntTableConstantsG.SWITCH_MAP[var3.ordinal()]) {
                                    case 1:
                                       this.agu.compute(var10, (var7xx, var8xx) -> {
                                          UUID var9x = var10.getWorld().getUID();
                                          if (var8xx == null) {
                                             return new PendingItemAction(var4, var5, var9x, var7, var8, var9);
                                          }

                                          var8xx.reschedule(var5, var9x, var7, var8, var9);
                                          return (PendingItemAction)var8xx;
                                       });
                                       break;
                                    case 2:
                                    case 3:
                                       this.agu.remove(var10);
                                 }
                              }
                           }, () -> {});
                        }
                     }
                  }
               }
            }
         );
      this.Bz();
   }

   @Override
   public void unregister() {
      this.cV = false;
      IaListener.super.unregister();
      NativePacketsHolder.PE().method1889(Main.getInstance(), this.agq);
      NativePacketsHolder.PE().method1889(Main.getInstance(), this.agr);
      this.agu.clear();
   }

   private void Bz() {
      this.ags = new SoundPair[0];
      this.agt = Map.of();
      JsonAssetWithCallbacks.loadWithCallbacks("internal/custom_blocks_sounds.json", var1 -> {
         ArrayList var2 = new ArrayList();
         HashMap var3 = new HashMap();
         JSONObject var4 = var1.asObject();

         for (Entry var6 : var4.entrySet()) {
            if (!((String)var6.getKey()).startsWith("internal.")) {
               String var7 = "minecraft:" + (String)var6.getKey();
               String var8 = "minecraft:internal." + (String)var6.getKey();
               NativeSound var9 = NativePacketsHolder.PE().Pv().sound(var7);
               var2.add(new SoundPair(var9, cd(var8)));

               for (Sound var13 : Sound.values()) {
                  if (var13.getKey().toString().equals(var7)) {
                     var3.put(var13, var8);
                     break;
                  }
               }
            }
         }

         this.ags = var2.toArray(SoundPair[]::new);
         this.agt = Map.copyOf(var3);
      }, var0 -> Log.error("Unable to read custom_blocks_sounds.json file from the plugin Jar. Try to redownload the plugin. Error: " + var0.getError()));
   }

   private static NativeSound cd(String var0) {
      Object var1 = NativePacketsHolder.PD().getField(var0);
      return () -> var1;
   }

   @EventHandler
   private void method1315(GenericGameEvent var1) {
      if (Settings.get().customBlockSoundsStep) {
         GameEvent var2 = var1.getEvent();
         if (var2 == GameEvent.STEP) {
            if (var1.getLocation().isWorldLoaded()) {
               if (var1.getEntity() instanceof Player var3) {
                  if (!var3.isSneaking()) {
                     Block var6 = method1326(var3);
                     boolean var5 = ContentManager.method8084().method8161(var6, var1x -> var1x.acY.apT.play(var6.getLocation(), SoundCategory.BLOCKS));
                     if (!var5) {
                        this.method1322(var6);
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler
   private void method1316(EntityDamageEvent var1) {
      if (Settings.get().customBlockSoundsNonStep) {
         if (var1.getCause() == DamageCause.FALL) {
            if (var1.getEntity() instanceof Player var2) {
               Block var5 = method1326(var2);
               boolean var4 = ContentManager.method8084().method8161(var5, var1x -> var1x.acY.apU.play(var5.getLocation(), SoundCategory.BLOCKS));
               if (!var4) {
                  this.method1323(var5);
               }
            }
         }
      }
   }

   @EventHandler
   private void method1317(PlayerAnimationEvent var1) {
      if (Settings.get().customBlockSoundsNonStep) {
         if (var1.getAnimationType() == PlayerAnimationType.ARM_SWING) {
            Player var2 = var1.getPlayer();
            PendingItemAction var3 = (PendingItemAction)this.agu.get(var2);
            if (var3 != null && var3.isExpired()) {
               var3.execute();
               ItemActionKey var4 = var3.getKey();
               World var5 = Bukkit.getWorld(var4.method20());
               if (var5 != null && NativePacketsHolder.PE().method1885(var4.bk(), var4.bl()) == var2 && var2.getWorld().getUID().equals(var4.method20())) {
                  Location var6 = new Location(var5, var4.method1(), var4.method2(), var4.method3());

                  try {
                     NativePacketsHolder.PE().IP().region(Main.getInstance(), var6, () -> this.method1318(var4, var6));
                  } catch (RuntimeException | LinkageError var8) {
                     Log.warn("Failed to schedule custom block hit sound", var8);
                  }
               }
            }
         }
      }
   }

   private void method1318(ItemActionKey var1, Location var2) {
      if (this.cV) {
         Player var3 = NativePacketsHolder.PE().method1885(var1.bk(), var1.bl());
         if (var3 != null && NativePacketsHolder.PE().IP().ownsEntity(var3)) {
            Block var4 = var2.getBlock();
            if (BlockFaceUtils.isChunkLoadedAt(var4)) {
               boolean var5 = ContentManager.method8084().method8161(var4, var2x -> var2x.acY.apS.play(var3, var4.getLocation()));
               if (!var5) {
                  this.method1321(var3, var4);
               }
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method546(BlockBreakEvent var1) {
      if (Settings.get().customBlockSoundsNonStep) {
         if (!EventRegistrar.isCustomBlockBreakEvent(var1)) {
            Block var2 = var1.getBlock();
            boolean var3 = ContentManager.method8084().method8161(var2, var1x -> var1x.acY.apS.play(var2.getLocation(), SoundCategory.BLOCKS));
            if (!var3) {
               this.method1319(var2);
            }
         }
      }
   }

   @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
   private void method536(BlockPlaceEvent var1) {
      if (Settings.get().customBlockSoundsNonStep) {
         if (!EventRegistrar.isCustomBlockPlaceEvent(var1)) {
            Block var2 = var1.getBlock();
            boolean var3 = ContentManager.method8084().method8161(var2, var1x -> var1x.acY.apR.play(var2.getLocation(), SoundCategory.BLOCKS));
            if (!var3) {
               this.method1320(var2);
            }
         }
      }
   }

   @EventHandler
   private void method516(PlayerQuitEvent var1) {
      this.agu.remove(var1.getPlayer());
   }

   private void method1319(Block var1) {
      this.method1324(var1, BlockEffectsService.getAdapter().getBreakSound(var1.getType()), 1.0F, 0.8F);
   }

   private void method1320(Block var1) {
      this.method1324(var1, BlockEffectsService.getAdapter().getPlaceSound(var1.getType()), 1.0F, 0.8F);
   }

   private void method1321(Player var1, Block var2) {
      this.method1325(var1, var2, BlockEffectsService.getAdapter().getHitSound(var2.getType()), 0.25F, 0.5F);
   }

   private void method1322(Block var1) {
      this.method1324(var1, BlockEffectsService.getAdapter().getStepSound(var1.getType()), 0.15F, 1.0F);
   }

   private void method1323(Block var1) {
      this.method1324(var1, BlockEffectsService.getAdapter().getFallSound(var1.getType()), 0.5F, 0.75F);
   }

   private void method1324(Block var1, Sound var2, float var3, float var4) {
      if (var2 != null) {
         String var5 = (String)this.agt.get(var2);
         if (var5 != null) {
            SoundUtils.playSound(var1.getLocation(), var5, SoundCategory.BLOCKS, var3, var4);
         }
      }
   }

   private void method1325(Player var1, Block var2, Sound var3, float var4, float var5) {
      if (var3 != null) {
         String var6 = (String)this.agt.get(var3);
         if (var6 != null) {
            SoundUtils.playSoundTo(var1, var2.getLocation(), var6, SoundCategory.BLOCKS, var4, var5);
         }
      }
   }

   private static Block method1326(Entity var0) {
      return var0.getLocation().subtract(0.0, 0.2F, 0.0).getBlock();
   }
}
