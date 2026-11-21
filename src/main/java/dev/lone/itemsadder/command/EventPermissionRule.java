package dev.lone.itemsadder.command;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.util.Utils;
import lombok.Generated;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.Nullable;

public class EventPermissionRule extends AbstractCommandPermissionRule {
   public final String iT;
   public boolean iU;
   private final boolean iV;
   @Nullable
   private final String iW;

   @Override
   public PermissionRuleOutcome method4941(Event var1, CustomEntityImpl var2, Player var3) {
      String var4 = this.iT;
      var4 = StringUtils.replace(var4, "{debug-event-name}", var1.getEventName());
      if (var3 != null) {
         var4 = StringUtils.replace(this.iT, "{player}", var3.getName());
         if (Main.aER) {
            var4 = PlaceholderAPI.setPlaceholders(var3, var4);
         }
      }

      if (this.iU) {
         if (!EventPermissionRuleUtils.method5373(var4, this)) {
            return PermissionRuleOutcome.cy();
         }
      } else {
         if (var3 == null) {
            return PermissionRuleOutcome.cy();
         }

         var3.performCommand(var4);
      }

      return PermissionRuleOutcome.cz();
   }

   @Override
   public PermissionRuleOutcome method4942(Event var1, CustomEntityImpl var2) {
      if (var1 instanceof PlayerEvent var3) {
         return this.method4949(var3.getPlayer(), var2.getLivingEntity()) ? PermissionRuleOutcome.cz() : PermissionRuleOutcome.cy();
      } else {
         return PermissionRuleOutcome.cy();
      }
   }

   @Override
   public PermissionRuleOutcome method4943(PlayerInteractEvent var1, CustomEntityImpl var2) {
      Player var3 = var1.getPlayer();
      Location var4 = Utils.clickedLocation(var1);
      if (var4 == null) {
         if (var1.getAction() != Action.LEFT_CLICK_AIR && var1.getAction() != Action.RIGHT_CLICK_AIR) {
            return PermissionRuleOutcome.cy();
         }

         var4 = var3.getEyeLocation().add(var3.getLocation().getDirection().multiply(5));
      }

      String var5 = StringUtils.replace(this.iT, "{player}", var3.getName());
      var5 = StringUtils.replace(var5, "{target-player}", var3.getName());
      var5 = StringUtils.replace(var5, "{world}", var4.getWorld().getName());
      var5 = StringUtils.replace(var5, "{target-world}", var4.getWorld().getName());
      var5 = StringUtils.replace(var5, "{target-x}", var4.getX() + "");
      var5 = StringUtils.replace(var5, "{target-y}", var4.getY() + "");
      var5 = StringUtils.replace(var5, "{target-z}", var4.getZ() + "");
      var5 = StringUtils.replace(var5, "{target-yaw}", var3.getLocation().getYaw() + "");
      var5 = StringUtils.replace(var5, "{target-inverse-yaw}", var3.getLocation().getYaw() + 180.0F + "");
      var5 = StringUtils.replace(var5, "{target-pitch}", var3.getLocation().getPitch() + "");
      if (Main.aER) {
         var5 = PlaceholderAPI.setPlaceholders(var3, var5);
      }

      if (this.iU) {
         if (!EventPermissionRuleUtils.method5373(var5, this)) {
            return PermissionRuleOutcome.cy();
         }
      } else {
         var3.performCommand(var5);
      }

      return PermissionRuleOutcome.cz();
   }

   @Override
   public PermissionRuleOutcome method4944(EntityDamageByEntityEvent var1, CustomEntityImpl var2) {
      if (var1.getDamager() instanceof Player var3) {
         return this.method4949(var3, var1.getEntity()) ? PermissionRuleOutcome.cz() : PermissionRuleOutcome.cy();
      } else {
         return PermissionRuleOutcome.cy();
      }
   }

   @Override
   public PermissionRuleOutcome method4945(EntityDamageEvent var1, CustomEntityImpl var2) {
      return this.method4948(var1.getEntity()) ? PermissionRuleOutcome.cz() : PermissionRuleOutcome.cy();
   }

   @Override
   public PermissionRuleOutcome method4946(PlayerInteractEntityEvent var1, CustomEntityImpl var2) {
      return this.method4949(var1.getPlayer(), var1.getRightClicked()) ? PermissionRuleOutcome.cz() : PermissionRuleOutcome.cy();
   }

   @Override
   public PermissionRuleOutcome method4947(ProjectileHitEvent var1, CustomEntityImpl var2) {
      if (var1.getHitEntity() == null) {
         return PermissionRuleOutcome.cy();
      }

      if (var1.getEntity().hasMetadata("playerName")) {
         Player var3 = Bukkit.getServer().getPlayer(((MetadataValue)var1.getEntity().getMetadata("playerName").get(0)).asString());
         if (var3 != null && !this.method4949(var3, var1.getHitEntity())) {
            return PermissionRuleOutcome.cy();
         }
      }

      return PermissionRuleOutcome.cz();
   }

   private boolean method4948(Entity var1) {
      String var2 = this.iT;
      var2 = StringUtils.replace(var2, "{world}", var1.getLocation().getWorld().getName());
      var2 = StringUtils.replace(var2, "{target-world}", var1.getLocation().getWorld().getName());
      var2 = StringUtils.replace(var2, "{target-x}", var1.getLocation().getBlockX() + "");
      var2 = StringUtils.replace(var2, "{target-y}", var1.getLocation().getBlockY() + "");
      var2 = StringUtils.replace(var2, "{target-z}", var1.getLocation().getBlockZ() + "");
      var2 = StringUtils.replace(var2, "{target-yaw}", var1.getLocation().getYaw() + "");
      var2 = StringUtils.replace(var2, "{target-inverse-yaw}", var1.getLocation().getYaw() + 180.0F + "");
      var2 = StringUtils.replace(var2, "{target-pitch}", var1.getLocation().getPitch() + "");
      return this.iU ? EventPermissionRuleUtils.method5373(var2, this) : false;
   }

   private boolean method4949(Player var1, Entity var2) {
      String var3 = StringUtils.replace(this.iT, "{player}", var1.getName());
      if (var2.getType() == EntityType.PLAYER) {
         var3 = StringUtils.replace(var3, "{target-player}", var2.getName());
      }

      var3 = StringUtils.replace(var3, "{world}", var2.getLocation().getWorld().getName());
      var3 = StringUtils.replace(var3, "{target-world}", var2.getLocation().getWorld().getName());
      var3 = StringUtils.replace(var3, "{target-x}", var2.getLocation().getBlockX() + "");
      var3 = StringUtils.replace(var3, "{target-y}", var2.getLocation().getBlockY() + "");
      var3 = StringUtils.replace(var3, "{target-z}", var2.getLocation().getBlockZ() + "");
      var3 = StringUtils.replace(var3, "{target-yaw}", var2.getLocation().getYaw() + "");
      var3 = StringUtils.replace(var3, "{target-inverse-yaw}", var2.getLocation().getYaw() + 180.0F + "");
      var3 = StringUtils.replace(var3, "{target-pitch}", var2.getLocation().getPitch() + "");
      if (Main.aER) {
         var3 = PlaceholderAPI.setPlaceholders(var1, var3);
      }

      if (this.iU) {
         return EventPermissionRuleUtils.method5373(var3, this);
      }

      var1.performCommand(var3);
      return true;
   }

   @Generated
   protected EventPermissionRule(EventPermissionRuleBuilder var1) {
      super(var1);
      this.iT = var1.iT;
      this.iU = var1.iU;
      this.iV = var1.iV;
      this.iW = var1.iW;
   }

   @Generated
   public static EventPermissionRuleBuilder cE() {
      return new EventPermissionRuleBuilderImpl();
   }
}
