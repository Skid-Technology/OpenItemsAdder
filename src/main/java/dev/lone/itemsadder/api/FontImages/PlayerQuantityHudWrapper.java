package dev.lone.itemsadder.api.FontImages;

import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.command.RuleSource;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespacedElementWithEffects;
import dev.lone.itemsadder.util.Log;

@Deprecated
public class PlayerQuantityHudWrapper extends PlayerHudWrapper {
   boolean warnedAboutApiPermission = false;
   protected final NamespacedElementWithEffects internalQuantityHud = (NamespacedElementWithEffects)this.internal;

   public PlayerQuantityHudWrapper(PlayerHudsHolderWrapper playerHUDsHolderWrapper, String namespacedID) {
      super(playerHUDsHolderWrapper, namespacedID);
   }

   public NamespacedElementWithEffects getInternal() {
      return (NamespacedElementWithEffects)this.internal;
   }

   public void setFloatValue(float value) {
      if (PermissionNodes.method1618(this.playerHUDsHolderWrapper.getPlayer(), this.internalQuantityHud, RuleSource.API, true)) {
         if (!this.warnedAboutApiPermission) {
            Log.warn(
               "Player "
                  + this.playerHUDsHolderWrapper.getPlayer().getName()
                  + " has HUDs API bypass permission. HUD value won't change. More info: https://itemsadder.devs.beer/plugin-usage/permissions"
            );
            this.warnedAboutApiPermission = true;
         }
      } else {
         this.warnedAboutApiPermission = false;
         ContentManager.method8084().aH.method876(this.playerHUDsHolderWrapper.getPlayer(), this.internalQuantityHud.abF, value);
      }
   }

   public float getFloatValue() {
      return this.internalQuantityHud.zP();
   }
}
