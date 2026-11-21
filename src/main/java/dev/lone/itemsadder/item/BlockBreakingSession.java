package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.api.CustomBlockActionEvent;
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import dev.lone.itemsadder.block.BlockRealizationMode;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.text.NmsChatService;
import dev.lone.itemsadder.util.BlockFaceUtils;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import java.util.UUID;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class BlockBreakingSession {
   private final Player agU;
   Block aaU;
   CustomBlockDefinition agk;
   ItemStack agV;
   CustomBlockChunkStoreBase agl;
   Vector agW;
   double agX;
   int agY = -1;
   private boolean agZ;
   BukkitTask aha;
   private volatile long ahb;
   private volatile boolean aap;
   private final UUID ahc;
   private final int ahd;
   private final int ahe;
   private final int ahf;
   private final int ahg;

   public BlockBreakingSession(Block var1, Player var2, CustomBlockDefinition var3, CustomBlockChunkStoreBase var4) {
      this.aaU = var1;
      this.agU = var2;
      this.agk = var3;
      this.agl = var4;
      this.agW = var1.getLocation().toVector();
      this.ahc = var1.getWorld().getUID();
      this.ahd = var1.getX();
      this.ahe = var1.getY();
      this.ahf = var1.getZ();
      this.ahg = -var2.getEntityId();
   }

   public void startBreaking() {
      this.sendBreakStage(-1);
      this.agV = this.agU.getInventory().getItemInMainHand();
      float var1 = this.computeBreakTime();
      this.agZ = this.agk.matchesPlacementItem(this.agV);
      if (var1 <= 0.01F) {
         this.finishBreaking();
      } else {
         this.agX = 0.0;
         this.ahb = System.nanoTime();
         if (!this.agZ) {
            this.sendWrongToolMessage(this.agU);
         }

         this.aha = SchedulerUtils.scheduleRepeating(var1x -> {
            if (System.nanoTime() - this.ahb > 500000000L) {
               NativePacketsHolder.PE().IP().entity(Main.getInstance(), this.agU, () -> {
                  if (!this.aap && System.nanoTime() - this.ahb > 500000000L) {
                     BlockDigListener.BR().method1281(this.agU, this);
                  }
               }, this::cancelDigTask);
            }
         }, 0L, 1L);
      }
   }

   public boolean updateBreaking() {
      if (this.aap) {
         return true;
      }

      if (!this.agV.equals(this.agU.getInventory().getItemInMainHand())) {
         this.stopDigging(false);
         this.startBreaking();
         return false;
      }

      long var1 = System.nanoTime();
      long var3 = Math.max(0L, Math.min(var1 - this.ahb, 500000000L));
      this.ahb = var1;
      float var5 = this.computeBreakTime();
      if (var5 <= 0.01F) {
         this.finishBreaking();
         return true;
      }

      if (Float.isFinite(var5)) {
         this.agX += var3 / (var5 * 1.0E9);
      }

      if (this.agX >= 1.0) {
         this.finishBreaking();
         return true;
      } else {
         this.agY = (int)(this.agX * 10.0);
         this.sendBreakStage(this.agY);
         return false;
      }
   }

   public void cancelDigging() {
      this.stopDigging(true);
   }

   public void stopDigging(boolean var1) {
      this.cancelDigTask();
      this.sendBreakStage(-1);
      if (var1) {
         BlockDigListener.BR().ax(this.agU);
      }
   }

   public void cancelDigTask() {
      if (this.aha != null) {
         this.aha.cancel();
      }
   }

   public void finishBreaking() {
      if (!this.aap) {
         Location var1 = this.aaU.getLocation();
         this.aap = true;
         this.cancelDigTask();
         if (!BlockDigListener.BR().method1282(var1) && this.agl.getCustomBlockIdAtLocation(var1) != null) {
            BlockDigListener.BR().method1283(var1, this);
            SchedulerUtils.runOnMainThread(() -> {
               if (PermissionNodes.method1622(this.agU, this.agk.getBreakPermissionSuffix()) && BlockFaceUtils.simulateBreak(this.aaU, this.agU)) {
                  CustomBlockActionEvent var2 = new CustomBlockActionEvent(this.agU, this.aaU, this.agk, this.agl);
                  var2.aI(this.agZ);
                  BlockInteractResult var3 = this.resolveBreakResult(this.agU, var2);
                  if (var3 == BlockInteractResult.CANCEL) {
                     if (this.aaU.getType().getHardness() == 0.0F) {
                        this.agk.placeBlockWithPhysics(this.aaU, false);
                     }
                  } else {
                     CustomBlockBreakEvent var4 = new CustomBlockBreakEvent(this.agU, this.aaU, this.agk);
                     var4.setCancelled(var2.isCancelled());
                     if (EventRegistrar.callEvent(var4) && EventRegistrar.callEvent(var2)) {
                        SchedulerUtils.runOnMainThread(() -> CustomItemDefinition.method8825(1, this.agU));
                     }
                  }
               }

               BlockDigListener.BR().method1284(var1, this);
               this.resetAfterBreak();
            });
         } else {
            this.resetAfterBreak();
         }
      }
   }

   private void resetAfterBreak() {
      SchedulerUtils.runLaterAsync(() -> this.sendBreakStage(-1), 2L);
      BlockDigListener.BR().ax(this.agU);
   }

   private BlockInteractResult resolveBreakResult(Player var1, CustomBlockActionEvent var2) {
      BlockInteractResult var3 = BlockInteractResult.RIGHT_TOOL;
      if (var1.getGameMode() == GameMode.CREATIVE) {
         var3 = BlockInteractResult.CREATIVE_ALLOW;
      }

      if (!Settings.get().field37 && !Settings.get().cancelBreakWrongToolActionbar) {
         var3 = BlockInteractResult.WRONG_BUT_DO_NOTHING;
      }

      if (!this.agZ) {
         if (this.sendWrongToolMessage(var1)) {
            var3 = BlockInteractResult.MSG;
         }

         if (Settings.get().field37) {
            if (var2 != null) {
               var2.setCancelled(true);
            }

            var3 = BlockInteractResult.CANCEL;
         }

         if (var2 != null) {
            var2.aI(false);
         }
      }

      return var3;
   }

   private boolean sendWrongToolMessage(Player var1) {
      if (Settings.get().cancelBreakWrongToolActionbar) {
         NmsChatService.method2951(var1, Main.aFi.lj("cannot-break-with-this-tool"));
         return true;
      } else {
         return false;
      }
   }

   private void sendBreakStage(int var1) {
      if (this.agk.aps == BlockRealizationMode.ENTITY) {
         ContentManager.method8084().aq.updateBlockBucket(this.ahc, this.ahd, this.ahe, this.ahf, this.ahg, var1);
      } else {
         sendBreakPacket(this.agU, this.agW, var1, this.ahg);
      }
   }

   public static void sendBreakPacket(Player var0, Vector var1, int var2, int var3) {
      PlayerInteractionFacade.sendBreakPacket(var0, var1, var2, var3);
   }

   public float computeBreakTime() {
      float var1 = this.agk.anI;
      float var2 = 0.0F;
      float var3 = 1.0F;
      this.agZ = this.agk.matchesPlacementItem(this.agV);
      if (this.agZ) {
         var2 = var1 * 0.75F;
      } else {
         var2 = var1 * 5.0F;
      }

      if (this.agZ) {
         var3 = ItemStackUtils.aC(this.agV) / 1.5F;
      }

      if (!this.agZ && !ItemStackUtils.ab(this.agV.getType())) {
         var3 = 0.2F;
      }

      float var4 = this.getAttributeOrDefault("mining_efficiency", Float.NaN);
      if (Float.isNaN(var4)) {
         if (this.agZ && this.agV.hasItemMeta()) {
            int var5 = this.agV.getItemMeta().getEnchantLevel(Enchantment.EFFICIENCY);
            if (var5 > 0) {
               var3 += var5 * var5 + 1;
            }
         }
      } else if (var3 > 1.0F) {
         var3 += var4;
      }

      if (this.agU.hasPotionEffect(PotionEffectType.HASTE)) {
         var3 *= (float)(1.0 + 0.2 * (this.agU.getPotionEffect(PotionEffectType.HASTE).getAmplifier() + 1));
      }

      if (this.agU.hasPotionEffect(PotionEffectType.MINING_FATIGUE)) {
         int var9 = this.agU.getPotionEffect(PotionEffectType.MINING_FATIGUE).getAmplifier();
         float var10000;
         if (Version.get() == Version.v26_3) {
            var10000 = (float)Math.pow(0.3, var9 + 1);
         } else {
            switch (var9) {
               case 0:
                  var10000 = 0.3F;
                  break;
               case 1:
                  var10000 = 0.09F;
                  break;
               case 2:
                  var10000 = 0.0027F;
                  break;
               default:
                  var10000 = 8.1E-4F;
            }
         }

         float var6 = var10000;
         var3 *= var6;
      }

      var3 *= this.getAttributeOrDefault("block_break_speed", this.getAttributeOrDefault("player_block_break_speed", 1.0F));
      if (this.agU.isUnderWater()) {
         float var10 = this.getAttributeOrDefault("submerged_mining_speed", -1.0F);
         if (var10 < 0.0F) {
            var10 = this.agU.getInventory().getHelmet() != null && this.agU.getInventory().getHelmet().containsEnchantment(Enchantment.AQUA_AFFINITY)
               ? 1.0F
               : 0.2F;
         }

         var3 *= var10;
      }

      if (!this.agU.isOnGround()) {
         var3 /= 5.0F;
      }

      return var3 <= 0.0F ? Float.POSITIVE_INFINITY : var2 / var3;
   }

   private float getAttributeOrDefault(String var1, float var2) {
      Attribute var3 = (Attribute)Registry.ATTRIBUTE.get(NamespacedKey.minecraft(var1));
      AttributeInstance var4 = var3 == null ? null : this.agU.getAttribute(var3);
      return var4 == null ? var2 : (float)var4.getValue();
   }
}
