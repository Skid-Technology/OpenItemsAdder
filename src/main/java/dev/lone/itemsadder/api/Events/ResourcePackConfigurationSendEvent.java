package dev.lone.itemsadder.api.Events;

import java.util.UUID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourcePackConfigurationSendEvent extends Event {
   private static final HandlerList HANDLERS_LIST = new HandlerList();
   private final UUID playerId;
   private final String playerName;
   private final String clientLanguage;
   private final UUID packId;
   private final String url;
   private final String hash;
   private final boolean required;
   private final boolean itemsAdderPack;

   public ResourcePackConfigurationSendEvent(
      UUID playerId, String playerName, @Nullable String clientLanguage, UUID packId, String url, String hash, boolean required, boolean itemsAdderPack
   ) {
      this.playerId = playerId;
      this.playerName = playerName;
      this.clientLanguage = clientLanguage;
      this.packId = packId;
      this.url = url;
      this.hash = hash;
      this.required = required;
      this.itemsAdderPack = itemsAdderPack;
   }

   public UUID getPlayerId() {
      return this.playerId;
   }

   public String getPlayerName() {
      return this.playerName;
   }

   @Nullable
   public String getClientLanguage() {
      return this.clientLanguage;
   }

   public UUID getPackId() {
      return this.packId;
   }

   public String getUrl() {
      return this.url;
   }

   public String getHash() {
      return this.hash;
   }

   public boolean isRequired() {
      return this.required;
   }

   public boolean isItemsAdderPack() {
      return this.itemsAdderPack;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS_LIST;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return HANDLERS_LIST;
   }
}
