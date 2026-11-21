package dev.lone.itemsadder.ai;

import dev.lone.itemsadder.entity.MobGoalSettings;
import dev.lone.itemsadder.entity.MobInventoryHelper;
import dev.lone.itemsadder.entity.MobPersistentKeys;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

final class MobGoalItemCollect extends AbstractMobGoal {
   private final BlockSearchScanner abb;
   private final BlockSearchScanner abc;
   private Item abd;
   private Player li;
   private Block aaU;
   private boolean abe;

   MobGoalItemCollect(Mob var1, MobGoalSettings var2, NavigatorAdapter var3) {
      super(var1, var2, var3);
      this.abb = new BlockSearchScanner(
         (int)var2.getDouble("range"),
         var2.has("vertical_range") ? var2.getInt("vertical_range") : 3,
         var2.has("scan_budget") ? var2.getInt("scan_budget") : 128
      );
      this.abc = new BlockSearchScanner((int)var2.getDouble("delivery_range"), 3, 128);
      if (var2.getString("delivery").equals("home")) {
         NamespacedKey var4 = new NamespacedKey("itemsadder", "entity_spawn_home_position");
         if (!var1.getPersistentDataContainer().has(var4, PersistentDataType.INTEGER_ARRAY)) {
            Location var5 = var1.getLocation();
            var1.getPersistentDataContainer().set(var4, PersistentDataType.INTEGER_ARRAY, new int[]{var5.getBlockX(), var5.getBlockY(), var5.getBlockZ()});
            var1.getPersistentDataContainer()
               .set(new NamespacedKey("itemsadder", "entity_spawn_home_world"), PersistentDataType.STRING, var1.getWorld().getUID().toString());
         }
      }
   }

   @Override
   protected boolean yX() {
      this.abd = null;
      this.li = null;
      this.aaU = null;
      this.abe = MobInventoryHelper.hasInventory(this.aam);
      if (this.abe) {
         List var1 = MobInventoryHelper.method9582(this.aam);
         switch (this.aan.getString("delivery")) {
            case "owner":
               this.li = MobPersistentKeys.method9621(this.aam);
               break;
            case "player":
               this.li = MobPersistentKeys.method9616(this.aam, this.aan.getDouble("delivery_range"), List.of("PLAYER"))
                  .stream()
                  .filter(var0 -> var0 instanceof Player var1x && var1x.getGameMode() != GameMode.SPECTATOR)
                  .map(var0 -> (Player)var0)
                  .filter(var1x -> var1.stream().anyMatch(var1xx -> MobInventoryHelper.method9590(var1x.getInventory(), var1xx) > 0))
                  .findFirst()
                  .orElse(null);
               break;
            case "container":
               this.aaU = this.abc
                  .method9629(
                     this.aam,
                     var2 -> this.aan.getSet("destination_blocks").contains(var2.getType())
                        && method4572(var2) != null
                        && var1.stream().anyMatch(var1xx -> MobInventoryHelper.method9590(method4572(var2), var1xx) > 0)
                  );
               break;
            case "home":
               return this.zy() != null;
            default:
               throw new IllegalStateException("Unknown delivery mode");
         }

         return this.li != null || this.aaU != null;
      } else {
         if (!MobPersistentKeys.method9611(this.aam)) {
            return false;
         }

         if (this.aan.zq().equals("transport_items")) {
            this.aaU = this.abb
               .method9629(
                  this.aam,
                  var1x -> this.aan.getSet("source_blocks").contains(var1x.getType())
                     && method4572(var1x) != null
                     && Arrays.stream(method4572(var1x).getStorageContents()).anyMatch(this.aan.zo())
               );
         } else {
            this.abd = this.aam
               .getNearbyEntities(this.aan.getDouble("range"), this.aan.getDouble("range"), this.aan.getDouble("range"))
               .stream()
               .filter(
                  var1x -> var1x instanceof Item var2
                     && var2.getPickupDelay() <= 0
                     && (var2.getOwner() == null || var2.getOwner().equals(this.aam.getUniqueId()))
                     && this.aan.zo().test(var2.getItemStack())
                     && this.aam.getLocation().distanceSquared(var2.getLocation()) <= this.aan.getDouble("range") * this.aan.getDouble("range")
               )
               .map(var0 -> (Item)var0)
               .sorted(Comparator.comparingDouble(var1x -> var1x.getLocation().distanceSquared(this.aam.getLocation())))
               .filter(this.aam::hasLineOfSight)
               .findFirst()
               .orElse(null);
         }

         return this.abd != null || this.aaU != null;
      }
   }

   private Location zy() {
      PersistentDataContainer var1 = this.aam.getPersistentDataContainer();
      int[] var2 = (int[])var1.get(new NamespacedKey("itemsadder", "entity_spawn_home_position"), PersistentDataType.INTEGER_ARRAY);
      String var3 = (String)var1.get(new NamespacedKey("itemsadder", "entity_spawn_home_world"), PersistentDataType.STRING);
      return var2 != null && var2.length == 3 && this.aam.getWorld().getUID().toString().equals(var3)
         ? new Location(this.aam.getWorld(), var2[0], var2[1], var2[2])
         : null;
   }

   private static Inventory method4572(Block var0) {
      return var0 != null && var0.getState() instanceof InventoryHolder var1 ? var1.getInventory() : null;
   }

   @Override
   protected boolean yZ() {
      return this.abd != null
         ? this.abd.isValid() && this.abd.getWorld() == this.aam.getWorld()
         : (
            this.li != null
               ? MobPersistentKeys.method9612(this.li) && this.li.getWorld() == this.aam.getWorld()
               : (
                  this.aaU != null
                     ? this.aaU.getWorld() == this.aam.getWorld()
                        && this.aam.getWorld().isChunkLoaded(this.aaU.getX() >> 4, this.aaU.getZ() >> 4)
                        && method4572(this.aaU) != null
                     : this.abe && this.zy() != null
               )
         );
   }

   @Override
   protected void zb() {
      Location var1 = this.abd != null
         ? this.abd.getLocation()
         : (this.li != null ? this.li.getLocation() : (this.aaU != null ? this.aaU.getLocation().add(0.5, 0.5, 0.5) : this.zy()));
      if (var1 == null
         || this.aam.getLocation().distanceSquared(var1) > Math.pow(this.abe ? this.aan.getDouble("delivery_range") : this.aan.getDouble("range"), 2.0)) {
         this.aap = true;
      } else if (!this.method4570(var1)) {
         this.method4571(var1);
      } else {
         this.aao.stop();
         if (this.ms % this.aan.getInt("interval") == 0) {
            if (this.abd != null) {
               MobInventoryHelper.method9587(this.aam, this.abd, this.aan.zo(), this.aan.getInt("capacity"), this.aan.getInt("transfer_amount"));
            } else if (!this.abe) {
               MobInventoryHelper.method9588(this.aam, method4572(this.aaU), this.aan.zo(), this.aan.getInt("capacity"), this.aan.getInt("transfer_amount"));
            } else if (this.li != null) {
               MobInventoryHelper.method9589(this.aam, this.li.getInventory(), this.aan.getInt("transfer_amount"));
            } else if (this.aaU != null) {
               MobInventoryHelper.method9589(this.aam, method4572(this.aaU), this.aan.getInt("transfer_amount"));
            } else {
               MobInventoryHelper.clearInventory(this.aam);
            }

            this.bL("execute");
            this.aap = true;
         }
      }
   }
}
