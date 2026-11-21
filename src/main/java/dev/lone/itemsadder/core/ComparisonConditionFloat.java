package dev.lone.itemsadder.core;

import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.util.IntTableConstantsB;
import dev.lone.itemsadder.util.Log;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class ComparisonConditionFloat extends ComparisonCondition {
   public final float ack;
   public final float acl;

   public ComparisonConditionFloat(int var1, float var2, float var3, ComparisonOperator var4) {
      super(var1, var3, var4);
      this.ack = var2;
      this.acl = var3;
   }

   @Override
   public ConditionAction Ab() {
      return new ComparisonConditionFloat(this.acn, this.ack, this.acl, this.acq);
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
            switch (IntTableConstantsB.SWITCH_MAP[this.acq.ordinal()]) {
               case 1:
                  if (var4 != this.acl) {
                     return;
                  }
               case 2:
                  if (!(var4 < this.acl)) {
                     return;
                  }
               case 3:
                  if (!(var4 > this.acl)) {
                     return;
                  }
               case 4:
                  if (!(var4 <= this.acl)) {
                     return;
                  }
               case 5:
                  if (!(var4 >= this.acl)) {
                     return;
                  }
               default:
                  EntityDamageEvent var5 = new EntityDamageEvent(var2, DamageCause.STARVATION, this.ack);
                  var2.setLastDamageCause(var5);
                  if (!var5.isCancelled()) {
                     var2.damage(this.ack);
                  }

                  if (Settings.get().isLoggingEnabled("logging.huds.triggers")) {
                     Log.log("DamagePlayerByValue " + this.ack);
                  }
            }
         }
      }
   }
}
