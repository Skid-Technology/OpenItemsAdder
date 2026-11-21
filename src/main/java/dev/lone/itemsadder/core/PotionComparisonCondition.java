package dev.lone.itemsadder.core;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.item.effect.PotionGrantEffect;
import dev.lone.itemsadder.util.IntTableConstantsC;
import dev.lone.itemsadder.util.Log;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PotionComparisonCondition extends ComparisonCondition {
   public final PotionGrantEffect acr;

   public PotionComparisonCondition(int var1, float var2, PotionGrantEffect var3, ComparisonOperator var4) {
      super(var1, var2, var4);
      this.acr = var3;
   }

   public PotionComparisonCondition Ad() {
      return new PotionComparisonCondition(this.acn, this.aco, this.acr, this.acq);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void method9640(NamespacedElementWithEffects var1, Player var2, int var3) {
      if (var2.getGameMode() != GameMode.CREATIVE) {
         this.acp += var3;
         if (this.acp >= this.acn) {
            this.acp = 0;
            float var4 = ContentManager.method8084().aG.method1429(var2, var1.zQ(), var1.zR());
            switch (IntTableConstantsC.SWITCH_MAP[this.acq.ordinal()]) {
               case 1:
                  if (var4 != this.aco) {
                     return;
                  }
               case 2:
                  if (!(var4 < this.aco)) {
                     return;
                  }
               case 3:
                  if (!(var4 > this.aco)) {
                     return;
                  }
               case 4:
                  if (!(var4 <= this.aco)) {
                     return;
                  }
               case 5:
                  if (!(var4 >= this.aco)) {
                     return;
                  }
               default:
                  this.acr.applyEffect(null, null, null, var2);
                  if (Settings.get().isLoggingEnabled("logging.huds.triggers")) {
                     Log.log("PotionEffectPlayerByValueOverTime " + this.acr.ajP.getName());
                  }
            }
         }
      }
   }
}
