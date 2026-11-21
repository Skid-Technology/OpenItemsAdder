package dev.lone.itemsadder.api.FontImages;

import dev.lone.itemsadder.core.NamespacedTextElement;
import java.util.List;

public class PlayerCustomHudWrapper extends PlayerQuantityHudWrapper {
   private final NamespacedTextElement internalCustomHud = (NamespacedTextElement)this.internal;

   public PlayerCustomHudWrapper(PlayerHudsHolderWrapper playerHUDsHolderWrapper, String namespacedID) {
      super(playerHUDsHolderWrapper, namespacedID);
   }

   public void clearFontImagesAndRefresh() {
      this.internalCustomHud.zT();
      this.playerHUDsHolderWrapper.getInternal().recalculateOffsets();
   }

   public void setFontImages(List fontImageWrappers) {
      this.clearFontImagesAndRefresh();

      for (int var2 = 0; var2 < fontImageWrappers.size(); var2++) {
         FontImageWrapper var3 = fontImageWrappers.get(var2);
         this.internalCustomHud.method506(var3.getInternal());
      }
   }

   public void removeFontImageByIndex(int index) {
      this.internalCustomHud.bp(index);
   }

   public void addFontImageToIndex(FontImageWrapper fontImageWrapper, int index) {
      if (fontImageWrapper.getInternal() != null) {
         this.internalCustomHud.method507(index, fontImageWrapper.getInternal());
      }
   }

   public void addFontImage(FontImageWrapper fontImageWrapper) {
      if (fontImageWrapper.getInternal() != null) {
         this.internalCustomHud.method506(fontImageWrapper.getInternal());
      }
   }

   public int getFontImagesCount() {
      return this.internalCustomHud.zU();
   }
}
