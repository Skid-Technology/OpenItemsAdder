package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.core.EffectChainRule;
import lombok.Generated;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

@Deprecated
public abstract class ItemEffect implements ItemEffectHandler {
   private String iJ;
   public long akP = -1L;
   private boolean iN;
   public String iL;
   @Nullable
   EffectChainRule iM;

   public ItemEffect bu(int var1) {
      this.akP = var1;
      return this;
   }

   public boolean cp() {
      return this.akP > 0L;
   }

   public ItemEffect cu(String var1) {
      if (var1 == null) {
         this.iL = null;
         return this;
      }

      if (!var1.startsWith("!") && !var1.startsWith("-")) {
         this.iN = false;
         this.iL = var1;
      } else {
         this.iN = true;
         this.iL = var1.substring(1);
      }

      return this;
   }

   public boolean cq() {
      return this.iL != null;
   }

   @Nullable
   public EffectChainRule Cu() {
      return this.iM;
   }

   public ItemEffect method9775(EffectChainRule var1) {
      this.iM = var1;
      return this;
   }

   public boolean method9776(Entity var1) {
      if (this.iL == null) {
         return true;
      } else {
         return this.iN ? !var1.hasPermission(this.iL) : var1.hasPermission(this.iL);
      }
   }

   @Generated
   public String cr() {
      return this.iJ;
   }

   @Generated
   public void cv(String var1) {
      this.iJ = var1;
   }

   @Generated
   public long Cv() {
      return this.akP;
   }
}
