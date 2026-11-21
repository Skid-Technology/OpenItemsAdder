package dev.lone.itemsadder.core;

import org.jetbrains.annotations.Nullable;

public class EffectChainRule {
   public final EffectChainMode acR;
   @Nullable
   public final String acS;

   public static boolean bY(String var0) {
      return "flow".equals(var0);
   }

   public EffectChainRule(EffectChainMode var1, @Nullable String var2) {
      this.acR = var1;
      this.acS = var2;
   }
}
