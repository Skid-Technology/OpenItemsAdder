package dev.lone.itemsadder.api;

import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.util.ToggleState;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class CustomPlayer extends CustomEntity {
   private String playerName;

   CustomPlayer(CustomEntityImpl renderer) {
      super(renderer);
   }

   CustomPlayer(CustomEntityImpl renderer, String playerName) {
      this(renderer);
      this.playerName = playerName;
   }

   public String getPlayerName() {
      return this.playerName;
   }

   @Override
   public boolean playAnimation(String emoteName) {
      ToggleState var2 = CustomEntityManager.method8320(this.renderer, emoteName);
      return var2.pM;
   }

   @Override
   public void stopAnimation() {
      this.renderer.getModelInstance().qN();
   }

   public static CustomPlayer spawn(String playerSkin, Location location) {
      if (CustomEntityManager.isInitialized() && CustomEntityManager.getInstance().fG()) {
         CustomEntityImpl var2 = CustomEntityManager.getInstance().spawnPlayerActor(null, playerSkin, location, "_iainternal:player", null);
         return new CustomPlayer(var2, playerSkin);
      } else {
         throw new RuntimeException("Entities API not available. Check if it's disabled in config.yml of ItemsAdder");
      }
   }

   @Nullable
   public static CustomPlayer byAlreadySpawned(Entity entity) {
      if (CustomEntityManager.isInitialized() && CustomEntityManager.getInstance().fG()) {
         CustomEntityImpl var1 = CustomEntityManager.getInstance().getByEntity(entity);
         return var1 != null && var1.qK.vn.AM ? new CustomPlayer(var1) : null;
      } else {
         throw new RuntimeException("Entities API not available. Check if it's disabled in config.yml of ItemsAdder");
      }
   }

   public static void playEmote(Player player, String emoteName) {
      CustomEntityManager.getInstance().method8315(player, emoteName);
   }

   public static void stopEmote(Player player) {
      CustomEntityManager.getInstance().method8327(player);
   }
}
