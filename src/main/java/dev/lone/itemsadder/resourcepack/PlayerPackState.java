package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.network.ResourcePackListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

final class PlayerPackState {
   private Map packHashes = Map.of();
   private final Map statuses = new HashMap();
   private Set requiredPlayers = Set.of();
   private UUID currentPlayer;
   private boolean waitingForInstall;
   private boolean installCompleted;

   synchronized Set refreshPacks(UUID var1, Map var2) {
      return this.applyPackState(var1, var2, Set.of());
   }

   synchronized Set applyPackState(UUID var1, Map var2, Set var3) {
      this.waitingForInstall = this.isInstalledWith(var1, (String)var2.get(var1))
         && var3.stream()
            .anyMatch(var1x -> this.statuses.get(var1x) == PackPushStatus.SERVER_SENT || this.statuses.get(var1x) == PackPushStatus.PLAYER_DOWNLOADING);
      this.currentPlayer = var1;
      HashSet var4 = new HashSet(this.packHashes.keySet());
      var4.removeAll(var2.keySet());
      this.statuses.keySet().retainAll(var2.keySet());
      var2.forEach(
         (var3x, var4x) -> {
            if (var3x.equals(var1)
               || !var4x.equals(this.packHashes.get(var3x))
               || !this.statuses.containsKey(var3x)
               || !var3.contains(var3x) && this.statuses.get(var3x) != PackPushStatus.INSTALLED) {
               this.statuses.put(var3x, PackPushStatus.SERVER_SENT);
            }
         }
      );
      this.packHashes = Map.copyOf(var2);
      this.requiredPlayers = Set.copyOf(var3);
      return var4;
   }

   synchronized boolean isInstalledWith(UUID var1, String var2) {
      return var2.equals(this.packHashes.get(var1)) && this.statuses.get(var1) == PackPushStatus.INSTALLED;
   }

   synchronized void reset(UUID var1) {
      HashMap var2 = new HashMap(this.packHashes);
      if (var1 == null) {
         var2.clear();
      } else {
         var2.remove(var1);
      }

      this.packHashes = Map.copyOf(var2);
      this.statuses.keySet().retainAll(var2.keySet());
      this.waitingForInstall = false;
      this.installCompleted = false;
   }

   synchronized boolean hasPack(UUID var1) {
      return this.packHashes.containsKey(var1);
   }

   synchronized boolean isRequiredPresent(UUID var1) {
      return this.requiredPlayers.contains(var1) && this.hasPack(var1);
   }

   synchronized boolean markInstalled(UUID var1) {
      if (var1.equals(this.currentPlayer) && this.waitingForInstall) {
         this.statuses.put(var1, PackPushStatus.INSTALLED);
         this.waitingForInstall = false;
         this.installCompleted = true;
         return true;
      } else {
         return false;
      }
   }

   synchronized boolean consumeInstallCompletion() {
      if (this.installCompleted
         && !this.requiredPlayers
            .stream()
            .anyMatch(var1 -> this.statuses.get(var1) == PackPushStatus.SERVER_SENT || this.statuses.get(var1) == PackPushStatus.PLAYER_DOWNLOADING)) {
         this.installCompleted = false;
         return true;
      } else {
         return false;
      }
   }

   synchronized boolean tryStartDownload(UUID var1, String var2) {
      if (this.isRequiredPresent(var1)
         && var2.equals(this.packHashes.get(var1))
         && this.statuses.get(var1) == PackPushStatus.SERVER_SENT
         && !this.statuses
            .entrySet()
            .stream()
            .anyMatch(var1x -> !this.requiredPlayers.contains(var1x.getKey()) && var1x.getValue() != PackPushStatus.INSTALLED)) {
         this.statuses.put(var1, PackPushStatus.PLAYER_DOWNLOADING);
         return true;
      } else {
         return false;
      }
   }

   synchronized PackPushStatus updateStatus(UUID var1, String var2) {
      if (!this.hasPack(var1)) {
         return null;
      }

      PackPushStatus var3 = ResourcePackListener.gN(var2);
      if (var3 == null) {
         return null;
      }

      PackPushStatus var4 = (PackPushStatus)this.statuses.get(var1);
      if (var4 != PackPushStatus.DECLINED
         && var4 != PackPushStatus.FAILED_DOWNLOAD
         && (var4 != PackPushStatus.INSTALLED || var3 != PackPushStatus.PLAYER_DOWNLOADING)) {
         this.statuses.put(var1, var3);
      }

      return this.aggregateStatus();
   }

   synchronized PackPushStatus aggregateStatus() {
      List var1 = this.statuses.entrySet().stream().filter(var1x -> !this.requiredPlayers.contains(var1x.getKey())).map(Entry::getValue).toList();
      if (var1.contains(PackPushStatus.DECLINED)) {
         return PackPushStatus.DECLINED;
      } else if (var1.contains(PackPushStatus.FAILED_DOWNLOAD)) {
         return PackPushStatus.FAILED_DOWNLOAD;
      } else {
         return !var1.isEmpty() && var1.stream().allMatch(var0 -> var0 == PackPushStatus.INSTALLED)
            ? PackPushStatus.INSTALLED
            : PackPushStatus.PLAYER_DOWNLOADING;
      }
   }
}
