package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.Nullable;

public class CommandEffect extends ItemEffect {
   public final String ajh;
   public boolean iU;
   private final boolean aji;
   @Nullable
   private final String ajj;

   public CommandEffect(String var1, boolean var2, boolean var3, @Nullable String var4) {
      this.ajh = var1;
      this.iU = var2;
      this.aji = var3;
      this.ajj = var4;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      String var5 = resolveCommand(var1, var4, var4, var4.getLocation(), this.ajh);
      if (this.iU) {
         if (!CommandPermissionCheck.hasPermission(var5, this)) {
            return EffectOutcome.success();
         }
      } else {
         var4.performCommand(var5);
      }

      return EffectOutcome.failure();
   }

   @Override
   public EffectOutcome applyOnInteract(PlayerInteractEvent var1, CustomItemDefinition var2, ItemStack var3) {
      Player var4 = var1.getPlayer();
      Location var5 = Utils.clickedLocation(var1);
      if (var5 == null) {
         if (var1.getAction() != Action.LEFT_CLICK_AIR && var1.getAction() != Action.RIGHT_CLICK_AIR) {
            return EffectOutcome.success();
         }

         var5 = var4.getEyeLocation().add(var4.getLocation().getDirection().multiply(5));
      }

      String var6 = resolveCommand(var1, var4, var4, Utils.clickedLocation(var1), this.ajh);
      if (Main.aER) {
         var6 = PlaceholderAPI.setPlaceholders(var4, var6);
      }

      if (this.iU) {
         if (!CommandPermissionCheck.hasPermission(var6, this)) {
            return EffectOutcome.success();
         }
      } else {
         var4.performCommand(var6);
      }

      return EffectOutcome.failureWith(var3);
   }

   @Override
   public EffectOutcome applyOnDamage(EntityDamageByEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.canExecuteFor(var1, (Player)var1.getDamager(), var1.getEntity()) ? EffectOutcome.failure() : EffectOutcome.success();
   }

   @Override
   public EffectOutcome applyOnInteractEntity(PlayerInteractEntityEvent var1, CustomItemDefinition var2, ItemStack var3) {
      return this.canExecuteFor(var1, var1.getPlayer(), var1.getRightClicked()) ? EffectOutcome.failure() : EffectOutcome.success();
   }

   @Override
   public EffectOutcome applyOnProjectileHit(ProjectileHitEvent var1, CustomItemDefinition var2, ItemStack var3) {
      if (var1.getHitEntity() == null) {
         if (var1.getHitBlock() != null && var1.getEntity().getShooter() instanceof Player var6) {
            return this.canExecuteAt(var1, var6, null, var1.getHitBlock().getLocation()) ? EffectOutcome.failure() : EffectOutcome.success();
         } else {
            return EffectOutcome.success();
         }
      } else {
         if (var1.getEntity().hasMetadata("playerName")) {
            Player var4 = Bukkit.getServer().getPlayer(((MetadataValue)var1.getEntity().getMetadata("playerName").getFirst()).asString());
            if (var4 != null && !this.canExecuteFor(var1, var4, var1.getHitEntity())) {
               return EffectOutcome.success();
            }
         }

         return EffectOutcome.failure();
      }
   }

   private boolean canExecuteFor(Event var1, Player var2, Entity var3) {
      return this.canExecuteAt(var1, var2, var3 instanceof Player var4 ? var4 : null, var3.getLocation());
   }

   private boolean canExecuteAt(Event var1, Player var2, @Nullable Player var3, Location var4) {
      String var5 = resolveCommand(var1, var2, var3, var4, this.ajh);
      if (Main.aER) {
         var5 = PlaceholderAPI.setPlaceholders(var2, var5);
      }

      if (this.iU) {
         return CommandPermissionCheck.hasPermission(var5, this);
      }

      var2.performCommand(var5);
      return true;
   }

   public static String resolveCommand(@Nullable Event var0, @Nullable Player var1, @Nullable Player var2, @Nullable Location var3, String var4) {
      if (var0 != null) {
         var4 = StringUtils.replace(var4, "{debug-event-name}", var0.getEventName());
      }

      if (var1 != null) {
         Location var5 = var1.getLocation();
         var4 = StringUtils.replace(var4, "{player}", var1.getName());
         var4 = StringUtils.replace(var4, "{player-x}", var5.getX() + "");
         var4 = StringUtils.replace(var4, "{player-y}", var5.getY() + "");
         var4 = StringUtils.replace(var4, "{player-z}", var5.getZ() + "");
         var4 = StringUtils.replace(var4, "{player-yaw}", var5.getYaw() + "");
         var4 = StringUtils.replace(var4, "{player-inverse-yaw}", var5.getYaw() + 180.0F + "");
         var4 = StringUtils.replace(var4, "{player-pitch}", var5.getPitch() + "");
         var4 = StringUtils.replace(var4, "{player-world}", var5.getWorld().getName());
      }

      if (var2 != null) {
         var4 = StringUtils.replace(var4, "{target-player}", var2.getName());
      }

      if (var3 != null) {
         var4 = StringUtils.replace(var4, "{world}", var3.getWorld().getName());
         var4 = StringUtils.replace(var4, "{target-world}", var3.getWorld().getName());
         var4 = StringUtils.replace(var4, "{target-x}", var3.getX() + "");
         var4 = StringUtils.replace(var4, "{target-y}", var3.getY() + "");
         var4 = StringUtils.replace(var4, "{target-z}", var3.getZ() + "");
         var4 = StringUtils.replace(var4, "{target-yaw}", var3.getYaw() + "");
         var4 = StringUtils.replace(var4, "{target-inverse-yaw}", var3.getYaw() + 180.0F + "");
         var4 = StringUtils.replace(var4, "{target-pitch}", var3.getPitch() + "");
      }

      if (Main.aER) {
         var4 = PlaceholderAPI.setPlaceholders(var1, var4);
      }

      return var4;
   }

   public static String replacePlaceholders(Player var0, String var1) {
      return resolveCommand(null, var0, var0, var0.getLocation(), var1);
   }
}
