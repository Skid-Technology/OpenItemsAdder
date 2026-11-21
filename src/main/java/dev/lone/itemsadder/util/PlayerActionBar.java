package dev.lone.itemsadder.util;

import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.ModelDefinitionData;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.entity.SoundEffectSpec;
import dev.lone.itemsadder.event.AbstractPlayerTrigger;
import dev.lone.itemsadder.network.EntityMetadataListener;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

public class PlayerActionBar {
   public Player player;
   public CustomEntityImpl renderer;
   private final boolean walkMode;
   public AtomicBoolean stopped;
   @Nullable
   private Float savedWalkSpeed;
   @Nullable
   private Float savedFlySpeed;
   private boolean savedAllowFlight;
   private boolean savedFlying;
   @Nullable
   BukkitTask soundTask;
   @Nullable
   SoundEffectSpec soundSpec;

   public PlayerActionBar(Player var1, CustomEntityImpl var2, boolean var3) {
      this.player = var1;
      this.renderer = var2;
      this.walkMode = var3;
      this.stopped = new AtomicBoolean();
   }

   public void start() {
      if (this.renderer.rC != null && this.renderer.rC.EU != null && this.renderer.rC.EU.zD) {
         AbstractPlayerTrigger.Pb().method5132(this.player, this.player.getLocation().add(0.0, 1.5, 0.0));
      }

      EntityMetadataListener.Ng().cf(this.player);
      this.renderer.resyncDisplays();
      if (this.walkMode) {
         ModelDefinitionData var1 = this.renderer.rC.EU;
         if (var1 != null) {
            if (!var1.zC) {
               SchedulerUtils.runOnMainThread(() -> PlayerInteractionFacade.cK(this.player));
            } else {
               this.savedWalkSpeed = this.player.getWalkSpeed();
               this.savedFlySpeed = this.player.getFlySpeed();
               if (var1.zM != null) {
                  this.player.setWalkSpeed(var1.zM);
               }

               if (var1.zN != null) {
                  this.player.setFlySpeed(var1.zN);
               }

               this.soundSpec = var1.getSound();
               if (this.soundSpec != null) {
                  if (this.soundSpec.pitch() > 0.0F) {
                     this.soundTask = SchedulerUtils.scheduleRepeating(
                        var1x -> SoundUtils.playSound(
                           this.player.getLocation(), this.soundSpec.sound(), this.soundSpec.category(), this.soundSpec.volume(), this.soundSpec.method66()
                        ),
                        0L,
                        (long)(this.soundSpec.pitch() * 20.0F)
                     );
                  } else {
                     SoundUtils.playSound(
                        this.player.getLocation(), this.soundSpec.sound(), this.soundSpec.category(), this.soundSpec.volume(), this.soundSpec.method66()
                     );
                  }
               }
            }
         }
      }

      if (!this.walkMode && this.renderer.qK.getFloatValue() > 0.0F) {
         if (this.savedFlySpeed == null) {
            this.savedFlySpeed = this.player.getFlySpeed();
         }

         this.savedAllowFlight = this.player.getAllowFlight();
         this.savedFlying = this.player.isFlying();
         this.player.setFlySpeed(this.renderer.qK.getFloatValue());
         this.player.setAllowFlight(true);
         this.player.setFlying(true);
      }
   }

   public void stop() {
      AbstractPlayerTrigger.Pb().bc(this.player);
      if (this.savedWalkSpeed != null) {
         this.player.setWalkSpeed(this.savedWalkSpeed);
      }

      if (this.savedFlySpeed != null) {
         this.player.setFlySpeed(this.savedFlySpeed);
      }

      EntityMetadataListener.Ng().cg(this.player);
      if (!this.stopped.get()) {
         this.stopped.set(true);
         if (this.walkMode) {
            PlayerInteractionFacade.cJ(this.player);
         }

         if (!this.walkMode && this.renderer.qK.getFloatValue() > 0.0F) {
            this.player.setFlying(this.savedFlying);
            this.player.setAllowFlight(this.savedAllowFlight);
         }

         if (this.soundTask != null) {
            this.soundTask.cancel();
            this.soundTask = null;
            if (this.soundSpec != null) {
               SoundUtils.playSound(this.player.getLocation(), this.soundSpec.category(), this.soundSpec.sound());
            }
         }

         this.player.updateInventory();
      }
   }
}
