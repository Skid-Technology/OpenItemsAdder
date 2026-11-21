package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionGrantEffect extends ItemEffect {
   public final PotionEffectType ajP;
   public final int ajQ;
   public final int ajR;
   public final boolean ajS;
   public final boolean ajT;
   public final boolean ajU;

   public PotionGrantEffect(PotionEffectType var1, int var2, int var3, boolean var4, boolean var5, boolean var6) {
      this.ajP = var1;
      this.ajQ = var2;
      this.ajR = var3;
      this.ajS = var4;
      this.ajT = var5;
      this.ajU = var6;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      return var4.addPotionEffect(new PotionEffect(this.ajP, this.ajR, this.ajQ, this.ajS, this.ajT, this.ajU))
         ? EffectOutcome.failure()
         : EffectOutcome.success();
   }
}
