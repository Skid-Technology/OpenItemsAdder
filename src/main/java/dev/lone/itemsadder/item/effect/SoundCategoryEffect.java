package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SoundCategoryEffect extends ItemEffect {
   public final String ajJ;
   public final float ajK;
   public final float ajL;
   public final SoundCategory ajM;
   public final boolean ajN;

   public SoundCategoryEffect(String var1, float var2, float var3, @Nullable String var4, @Nullable Boolean var5) {
      this.ajJ = var1;
      this.ajK = var2;
      this.ajL = var3;
      this.ajM = BukkitRegistryLookup.soundCategory(var4);
      this.ajN = var5 != null && var5;
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      if (this.ajN) {
         NativePacketsHolder.PD().playSoundDup2(this.ajJ, this.ajK, this.ajL, this.ajM, var4.getLocation());
      } else {
         NativePacketsHolder.PD().playSound(this.ajJ, this.ajK, this.ajL, this.ajM, var4.getLocation());
      }

      return EffectOutcome.failure();
   }
}
