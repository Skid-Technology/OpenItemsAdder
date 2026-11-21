package dev.lone.itemsadder.gui;

import java.util.concurrent.atomic.AtomicBoolean;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

final class GuiSessionEntry {
   final GuiSessionRegistry aIE;
   final Player aIF;
   final CustomInventoryHolder aIG;
   ScrollTrackButton aIt;
   final boolean aIH;
   final Object aII;
   Component aIu;
   final AtomicBoolean aIJ = new AtomicBoolean();
   int aIw;

   GuiSessionEntry(GuiSessionRegistry var1, Player var2, CustomInventoryHolder var3, int var4, boolean var5, Object var6) {
      this.aIE = var1;
      this.aIF = var2;
      this.aIG = var3;
      this.aIw = var4;
      this.aIH = var5;
      this.aII = var6;
      this.aIu = var1.aIB.MG();
      this.aIt = var1.aIz.Mi();
   }

   void close() {
      if (GuiSessionRegistry.aIy.remove(this.aIF.getUniqueId(), this)) {
         if (this.aIF.getOpenInventory().getTopInventory() == this.aIG.getInventory()) {
            this.aIF.closeInventory();
         }
      }
   }
}
