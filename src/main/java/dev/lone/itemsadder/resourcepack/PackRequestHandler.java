package dev.lone.itemsadder.resourcepack;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public abstract class PackRequestHandler {
   String bJf;
   boolean bJg;

   public PackRequestHandler(String var1) {
      this.bJf = var1;
   }

   public boolean isBusy() {
      return this.bJg;
   }

   @Nullable
   public abstract String uploadPack(ResourcePackGenerator var1, @Nullable Player var2);
}
