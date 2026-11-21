package dev.lone.itemsadder.item;

import dev.lone.itemsadder.item.effect.CommandEffect;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class EffectPlaybackSet {
   private final List aqI;
   private final float aqJ;

   public EffectPlaybackSet(List var1, float var2) {
      this.aqI = var1;
      this.aqJ = var2;
   }

   public void method72(@Nullable Player var1, Location var2) {
      double var3 = Math.random();
      if (!(var3 * 100.0 > this.aqJ)) {
         this.aqI.forEach(var1x -> {
            String var2x = CommandEffect.replacePlaceholders(var1, var1x.aqK);
            if (var1x.aqL) {
               EffectPlaybackRegistry.method10013(var2x, var1x);
            } else if (var1 != null) {
               var1.performCommand(var2x);
            }
         });
      }
   }

   public List Fj() {
      return this.aqI;
   }

   public float gF() {
      return this.aqJ;
   }
}
