package dev.lone.itemsadder.command;

import dev.lone.itemsadder.entity.CustomEntityImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface EventRuleHandler {
   PermissionRuleOutcome method4941(Event var1, CustomEntityImpl var2, Player var3);

   PermissionRuleOutcome method4942(Event var1, CustomEntityImpl var2);

   default PermissionRuleOutcome method4943(PlayerInteractEvent var1, CustomEntityImpl var2) {
      return this.method4941(var1, var2, var1.getPlayer());
   }

   default PermissionRuleOutcome method4944(EntityDamageByEntityEvent var1, CustomEntityImpl var2) {
      return this.method4941(var1, var2, (Player)var1.getDamager());
   }

   default PermissionRuleOutcome method4945(EntityDamageEvent var1, CustomEntityImpl var2) {
      return this.method4942(var1, var2);
   }

   default PermissionRuleOutcome method4950(EntityDeathEvent var1, CustomEntityImpl var2) {
      return this.method4941(var1, var2, var1.getEntity().getKiller());
   }

   default PermissionRuleOutcome method4946(PlayerInteractEntityEvent var1, CustomEntityImpl var2) {
      return this.method4941(var1, var2, var1.getPlayer());
   }

   default PermissionRuleOutcome method4947(ProjectileHitEvent var1, CustomEntityImpl var2) {
      return this.method4942(var1, var2);
   }
}
