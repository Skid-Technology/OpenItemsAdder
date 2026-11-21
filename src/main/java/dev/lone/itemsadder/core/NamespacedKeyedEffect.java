package dev.lone.itemsadder.core;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class NamespacedKeyedEffect extends ProgressEffect {
   final NamespacedKey act;

   public NamespacedKeyedEffect(NamespacedKey var1, int var2, float var3, int var4, int var5) {
      super(var2, var3, var4, var5);
      this.act = var1;
   }

   public NamespacedKeyedEffect Ae() {
      return new NamespacedKeyedEffect(this.act, this.acu, this.acv, this.acw, this.acx);
   }

   @Override
   boolean method9641(NamespacedElementWithEffects var1, Player var2, int var3) {
      return super.method9641(var1, var2, var3) && var2.getLocation().getBlock().getBiome().getKey() != this.act;
   }
}
