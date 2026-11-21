package dev.lone.itemsadder.gui;

import java.util.function.Consumer;
import org.bukkit.inventory.ItemStack;

public class GuiButton {
   private final ItemStack aKO;
   private final Consumer aKP;

   GuiButton(ItemStack var1, Consumer var2) {
      this.aKO = var1;
      this.aKP = var2;
   }

   public ItemStack dp() {
      return this.aKO;
   }

   public Consumer Mm() {
      return this.aKP;
   }
}
