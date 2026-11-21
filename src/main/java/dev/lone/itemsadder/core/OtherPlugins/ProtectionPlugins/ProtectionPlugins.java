package dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins;

import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins.Plugins.Flags;
import dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins.Plugins.Flags.Result;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.FurnitureItemEntity;
import dev.lone.itemsadder.util.BlockFaceUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ProtectionPlugins {
   public static boolean registered;
   public static boolean hasWorldGuard;

   public static boolean canUseItem(Player player, CustomItemDefinition item) {
      return !hasWorldGuard || Flags.DENIED_ITEMS == null || WorldGuardFlagsManager.canUseItem(player, item.getNamespacedID());
   }

   public static void register() {
      if (!registered) {
         registered = true;

         try {
            Class.forName("com.sk89q.worldguard.WorldGuard");
            hasWorldGuard = true;
            WorldGuardFlagsManager.registerFlags();
         } catch (ClassNotFoundException var1) {
         }
      }
   }

   public static boolean canSitOnFurniture(Player player, Entity clicked, Cancellable event) {
      if (hasWorldGuard) {
         Result var3 = WorldGuardFlagsManager.testEntityFlag(Flags.FURNITURE_SIT, player, clicked);
         if (var3 == Result.ALLOW) {
            return true;
         }

         if (var3 == Result.DENY) {
            return false;
         }

         if (var3 == Result.BYPASS) {
            return true;
         }
      }

      return !event.isCancelled();
   }

   public static boolean canSitOnFurniture(Player player, Entity vehicle) {
      if (hasWorldGuard) {
         Result var2 = WorldGuardFlagsManager.testEntityFlag(Flags.FURNITURE_SIT, player, vehicle);
         if (var2 == Result.ALLOW) {
            return true;
         }

         if (var2 == Result.DENY) {
            return false;
         }

         if (var2 == Result.BYPASS) {
            return true;
         }
      }

      return true;
   }

   public static boolean canSitOnVehicle(PlayerInteractAtEntityEvent e, ItemStack helmet) {
      String var2 = CustomItemDefinition.method8768(helmet);
      boolean var3 = var2 != null && var2.equals(e.getPlayer().getUniqueId().toString());
      if (var3) {
         if (hasWorldGuard) {
            Result var4 = WorldGuardFlagsManager.testEntityFlag(Flags.VEHICLE_PERSONAL_SIT, e.getPlayer(), e.getRightClicked());
            if (var4 == Result.ALLOW) {
               return true;
            }

            if (var4 == Result.DENY) {
               return false;
            }

            if (var4 == Result.BYPASS) {
               return true;
            }
         }
      } else if (hasWorldGuard) {
         Result var5 = WorldGuardFlagsManager.testEntityFlag(Flags.VEHICLE_SIT, e.getPlayer(), e.getRightClicked());
         if (var5 == Result.ALLOW) {
            return true;
         }

         if (var5 == Result.DENY) {
            return false;
         }

         if (var5 == Result.BYPASS) {
            return true;
         }
      }

      return !e.isCancelled();
   }

   public static boolean canSitOnVehicle(Player player, Entity vehicle) {
      boolean var2 = false;
      if (FurnitureItemEntity.method409(vehicle)) {
         ItemStack var3 = FurnitureItemEntity.A(vehicle);
         if (var3 != null) {
            String var4 = CustomItemDefinition.method8768(var3);
            var2 = var4 != null && var4.equals(player.getUniqueId().toString());
         }
      }

      if (var2) {
         Result var5 = WorldGuardFlagsManager.testEntityFlag(Flags.VEHICLE_PERSONAL_SIT, player, vehicle);
         if (var5 == Result.ALLOW) {
            return true;
         }

         if (var5 == Result.DENY) {
            return false;
         }

         if (var5 == Result.BYPASS) {
            return true;
         }
      } else {
         Result var6 = WorldGuardFlagsManager.testEntityFlag(Flags.VEHICLE_SIT, player, vehicle);
         if (var6 == Result.ALLOW) {
            return true;
         }

         if (var6 == Result.DENY) {
            return false;
         }

         if (var6 == Result.BYPASS) {
            return true;
         }
      }

      return true;
   }

   public static boolean canRemoveVehicle(Player player, Entity entity, ItemStack helmet) {
      String var3 = CustomItemDefinition.method8768(helmet);
      boolean var4 = var3 != null && var3.equals(player.getUniqueId().toString());
      if (var4) {
         if (hasWorldGuard) {
            Result var5 = WorldGuardFlagsManager.testEntityFlag(Flags.VEHICLE_PERSONAL_REMOVE, player, entity);
            if (var5 == Result.ALLOW) {
               return true;
            }

            if (var5 == Result.DENY) {
               return false;
            }

            if (var5 == Result.BYPASS) {
               return true;
            }
         }
      } else if (hasWorldGuard) {
         Result var6 = WorldGuardFlagsManager.testEntityFlag(Flags.VEHICLE_REMOVE, player, entity);
         if (var6 == Result.ALLOW) {
            return true;
         }

         if (var6 == Result.DENY) {
            return false;
         }

         if (var6 == Result.BYPASS) {
            return true;
         }
      }

      return BlockFaceUtils.simulateDamage(player, entity);
   }

   public static boolean canPlaceVehicle(Player player, Block block) {
      if (hasWorldGuard) {
         Result var2 = WorldGuardFlagsManager.testBlockFlag(Flags.VEHICLE_PLACE, player, block);
         if (var2 == Result.ALLOW) {
            return true;
         }

         if (var2 == Result.DENY) {
            return false;
         }

         if (var2 == Result.BYPASS) {
            return true;
         }
      }

      return BlockFaceUtils.simulatePlace(block, player);
   }

   public static boolean canInteract_placedBlock(PlayerInteractEvent e) {
      return canInteract_block(e, Flags.PLACED_BLOCK_INTERACT);
   }

   public static boolean canInteract_tradeMachineArmorstand(Player player, Entity entity, boolean def) {
      return canInteract_entity(player, entity, Flags.TRADE_MACHINES_USE, def);
   }

   public static boolean canInteract_tradeMachineArmorstand(PlayerInteractAtEntityEvent e) {
      return canInteract_entity(e, Flags.TRADE_MACHINES_USE);
   }

   public static boolean canInteract_furniture(PlayerInteractAtEntityEvent e) {
      return canInteract_furniture(e.getPlayer(), e.getRightClicked(), e.isCancelled());
   }

   public static boolean canInteract_furniture(PlayerInteractEvent e, Entity entity) {
      return canInteract_furniture(e.getPlayer(), entity, e.useInteractedBlock() == org.bukkit.event.Event.Result.DENY);
   }

   private static boolean canInteract_furniture(Player player, Entity entity, boolean denied) {
      if (hasWorldGuard) {
         State var3 = WorldGuardFlagsManager.furnitureInteractState(player, entity.getLocation());
         if (var3 == State.ALLOW) {
            return true;
         }

         if (var3 == State.DENY || !WorldGuardFlagsManager.canBuild(player, entity.getLocation(), com.sk89q.worldguard.protection.flags.Flags.INTERACT)) {
            return false;
         }
      }

      return !denied;
   }

   public static boolean canBreakFurniture(Player player, Entity entity) {
      return !hasWorldGuard || WorldGuardFlagsManager.canBuild(player, entity.getLocation(), com.sk89q.worldguard.protection.flags.Flags.BLOCK_BREAK);
   }

   private static boolean canInteract_block(PlayerInteractEvent e, StateFlag stateFlag) {
      if (hasWorldGuard) {
         Result var2 = WorldGuardFlagsManager.testBlockFlag(stateFlag, e.getPlayer(), e.getClickedBlock());
         if (var2 == Result.ALLOW) {
            return true;
         }

         if (var2 == Result.DENY) {
            return false;
         }

         if (var2 == Result.BYPASS) {
            return true;
         }
      }

      return e.useInteractedBlock() != org.bukkit.event.Event.Result.DENY;
   }

   public static boolean canInteract_tradeMachineBlock(CustomBlockInteractEvent e) {
      if (hasWorldGuard) {
         Result var1 = WorldGuardFlagsManager.testBlockFlag(Flags.TRADE_MACHINES_USE, e.getPlayer(), e.getBlockClicked());
         if (var1 == Result.ALLOW) {
            return true;
         }

         if (var1 == Result.DENY) {
            return false;
         }

         if (var1 == Result.BYPASS) {
            return true;
         }
      }

      return !e.isCancelled();
   }

   private static boolean canInteract_entity(PlayerInteractAtEntityEvent e, StateFlag stateFlag) {
      if (hasWorldGuard) {
         Result var2 = WorldGuardFlagsManager.testEntityFlag(stateFlag, e.getPlayer(), e.getRightClicked());
         if (var2 == Result.ALLOW) {
            return true;
         }

         if (var2 == Result.DENY) {
            return false;
         }

         if (var2 == Result.BYPASS) {
            return true;
         }
      }

      return !e.isCancelled();
   }

   private static boolean canInteract_entity(Player player, Entity entity, StateFlag stateFlag, boolean def) {
      if (hasWorldGuard) {
         Result var4 = WorldGuardFlagsManager.testEntityFlag(stateFlag, player, entity);
         if (var4 == Result.ALLOW) {
            return true;
         }

         if (var4 == Result.DENY) {
            return false;
         }

         if (var4 == Result.BYPASS) {
            return true;
         }
      }

      return !def;
   }

   public static boolean canRideEntitiesVanilla(Player player, Entity vehicle) {
      return !hasWorldGuard ? true : WorldGuardFlagsManager.canRideEntitiesVanilla(player, vehicle);
   }

   public static boolean isWorldguardInteractFlagAllow(Player player, Location location) {
      return !hasWorldGuard ? true : WorldGuardFlagsManager.isInteractFlagAllow(player, location);
   }
}
