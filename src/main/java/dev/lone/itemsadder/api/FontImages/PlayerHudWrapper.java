package dev.lone.itemsadder.api.FontImages;

import dev.lone.itemsadder.core.NamespacedElement;
import org.jetbrains.annotations.Nullable;

public class PlayerHudWrapper {
   protected final NamespacedElement internal;
   protected final PlayerHudsHolderWrapper playerHUDsHolderWrapper;
   protected final int startOffsetX;

   public PlayerHudWrapper(PlayerHudsHolderWrapper playerHUDsHolderWrapper, String namespacedID) {
      this.playerHUDsHolderWrapper = playerHUDsHolderWrapper;
      this.internal = playerHUDsHolderWrapper.getInternal().bW(namespacedID);
      if (this.internal != null) {
         this.startOffsetX = this.internal.zA();
      } else {
         this.startOffsetX = 0;
      }
   }

   @Nullable
   public NamespacedElement getInternal() {
      return this.internal;
   }

   public boolean exists() {
      return this.internal != null;
   }

   public String getNamespacedID() {
      return this.internal.getNamespacedID();
   }

   public int getInitialOffsetX() {
      return this.startOffsetX;
   }

   public int getOffsetX() {
      return this.internal.zB();
   }

   public void setOffsetX(int offset) {
      this.internal.bn(offset);
   }

   public void setVisible(boolean value) {
      this.internal.setVisible(value);
   }

   public boolean isVisible() {
      return this.internal.isVisible();
   }
}
