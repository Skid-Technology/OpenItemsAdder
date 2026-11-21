package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.NamespacedId;
import dev.lone.itemsadder.nms.NmsWorldService;

public class ColoredNamespacedId extends NamespacedId {
   int arJ;

   public ColoredNamespacedId(String var1, String var2, int var3) {
      super(var1, var2);
      this.arJ = var3;
   }

   public int getColor() {
      return this.arJ;
   }

   @Deprecated
   public Object FQ() {
      return NmsWorldService.Om().method3193(this.dk, this.dl);
   }
}
