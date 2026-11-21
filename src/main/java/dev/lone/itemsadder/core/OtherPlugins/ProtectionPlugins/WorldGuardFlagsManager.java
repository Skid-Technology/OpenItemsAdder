package dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins.Plugins.Flags;
import dev.lone.itemsadder.Core.OtherPlugins.ProtectionPlugins.Plugins.Flags.Result;
import dev.lone.itemsadder.util.Log;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WorldGuardFlagsManager {
   private static boolean areFlagsRegistered;
   private static FlagRegistry registry;

   public static void registerFlags() {
      try {
         if (!ProtectionPlugins.hasWorldGuard) {
            return;
         }

         if (areFlagsRegistered) {
            return;
         }

         areFlagsRegistered = true;
         registry = WorldGuard.getInstance().getFlagRegistry();
         Flags.FURNITURE_SIT = registerFlag("ia-furniture-sit");
         Flags.VEHICLE_SIT = registerFlag("ia-vehicle-sit", false);
         Flags.VEHICLE_PLACE = registerFlag("ia-vehicle-place", false);
         Flags.VEHICLE_REMOVE = registerFlag("ia-vehicle-remove", false);
         Flags.VEHICLE_PERSONAL_SIT = registerFlag("ia-vehicle-personal-sit");
         Flags.VEHICLE_PERSONAL_REMOVE = registerFlag("ia-vehicle-personal-remove");
         Flags.TRADE_MACHINES_USE = registerFlag("ia-trade-machine-use");
         Flags.PLACED_BLOCK_INTERACT = registerFlag("ia-placed-block-interact");
         Flags.PLACED_FURNITURE_INTERACT = registerFlag("ia-placed-furniture-interact", false);
         Flags.DENIED_ITEMS = registerSetFlag("ia-denied-items");
      } catch (Exception var1) {
         Log.warn("Failed to register flags with WorldGuard. Please check if you have the latest version of WorldGuard installed.", var1);
      }
   }

   static StateFlag registerFlag(String name) {
      return registerFlag(name, true);
   }

   static StateFlag registerFlag(String name, boolean def) {
      Flag var2 = registry.get(name);
      if (var2 instanceof StateFlag) {
         return (StateFlag)var2;
      }

      StateFlag var3 = new StateFlag(name, def);
      registry.register(var3);
      return var3;
   }

   private static SetFlag registerSetFlag(String name) {
      if (registry.get(name) instanceof SetFlag var3) {
         return var3;
      } else {
         SetFlag var2 = new SetFlag(name, new StringFlag(name + "-entry"));
         registry.register(var2);
         return var2;
      }
   }

   public static boolean canUseItem(Player player, String namespacedId) {
      Location var2 = player.getLocation();
      LocalPlayer var3 = WorldGuardPlugin.inst().wrapPlayer(player);
      if (hasBypass(var3, var2)) {
         return true;
      }

      Set var4 = (Set)WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().queryValue(BukkitAdapter.adapt(var2), var3, Flags.DENIED_ITEMS);
      return isItemAllowed(var4, namespacedId);
   }

   static boolean isItemAllowed(Set denied, String namespacedId) {
      return denied == null || !denied.contains("*") && !denied.contains(namespacedId);
   }

   public static Result testEntityFlag(StateFlag stateFlag, Player player, Entity entity) {
      return test(stateFlag, player, entity.getLocation());
   }

   public static Result testBlockFlag(StateFlag stateFlag, Player player, Block block) {
      return test(stateFlag, player, block.getLocation());
   }

   private static boolean hasBypass(LocalPlayer localPlayer, Location location) {
      return WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, BukkitAdapter.adapt(location.getWorld()));
   }

   public static boolean canBuild(Player player, Location location, StateFlag actionFlag) {
      LocalPlayer var3 = WorldGuardPlugin.inst().wrapPlayer(player);
      return hasBypass(var3, location)
         || WorldGuard.getInstance()
            .getPlatform()
            .getRegionContainer()
            .createQuery()
            .testBuild(BukkitAdapter.adapt(location), var3, new StateFlag[]{actionFlag});
   }

   public static State furnitureInteractState(Player player, Location location) {
      LocalPlayer var2 = WorldGuardPlugin.inst().wrapPlayer(player);
      return hasBypass(var2, location)
         ? State.ALLOW
         : WorldGuard.getInstance()
            .getPlatform()
            .getRegionContainer()
            .createQuery()
            .queryState(BukkitAdapter.adapt(location), var2, new StateFlag[]{Flags.PLACED_FURNITURE_INTERACT});
   }

   public static Result test(StateFlag stateFlag, Player player, Location location) {
      RegionQuery var3 = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
      LocalPlayer var4 = WorldGuardPlugin.inst().wrapPlayer(player);
      com.sk89q.worldedit.util.Location var5 = BukkitAdapter.adapt(location);
      if (var3.getApplicableRegions(var5).getRegions().isEmpty()) {
         return Result.NO_REGION;
      } else if (hasBypass(var4, location)) {
         return Result.BYPASS;
      } else {
         return var3.testState(var5, var4, new StateFlag[]{stateFlag}) ? Result.ALLOW : Result.DENY;
      }
   }

   public static boolean canRideEntitiesVanilla(Player player, Entity vehicle) {
      RegionQuery var2 = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
      Location var3 = vehicle.getLocation();
      LocalPlayer var4 = WorldGuardPlugin.inst().wrapPlayer(player);
      State var5 = var2.queryState(BukkitAdapter.adapt(var3), var4, new StateFlag[]{com.sk89q.worldguard.protection.flags.Flags.RIDE});
      Log.devLog("WorldGuard RIDE flag: " + var5);
      return var5 != State.DENY;
   }

   public static boolean isInteractFlagAllow(Player player, Location loc) {
      RegionQuery var2 = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
      LocalPlayer var3 = WorldGuardPlugin.inst().wrapPlayer(player);
      State var4 = var2.queryState(BukkitAdapter.adapt(loc), var3, new StateFlag[]{com.sk89q.worldguard.protection.flags.Flags.INTERACT});
      Log.devLog("WorldGuard INTERACT flag: " + var4);
      return var4 != null;
   }
}
