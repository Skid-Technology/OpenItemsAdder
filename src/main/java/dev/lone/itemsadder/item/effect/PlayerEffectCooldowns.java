package dev.lone.itemsadder.item.effect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

final class PlayerEffectCooldowns {
   final Map alE = new HashMap();

   void setCooldown(UUID var1, String var2) {
      this.alE.put(var1, new CooldownEntry(var2));
   }

   CooldownEntry findCooldown(UUID var1, String var2) {
      CooldownEntry var3 = this.getOrCreateCooldown(var1, var2);
      if (var3 == null) {
         var3 = new CooldownEntry(var2);
         this.alE.put(var1, var3);
      }

      return var3;
   }

   @Nullable
   CooldownEntry getOrCreateCooldown(UUID var1, String var2) {
      CooldownEntry var3 = (CooldownEntry)this.alE.get(var1);
      return var3 != null && var3.alF.equals(var2) ? var3 : null;
   }

   @Nullable
   CooldownEntry getPlayerEntry(UUID var1) {
      return (CooldownEntry)this.alE.remove(var1);
   }

   void clear() {
      this.alE.clear();
   }
}
