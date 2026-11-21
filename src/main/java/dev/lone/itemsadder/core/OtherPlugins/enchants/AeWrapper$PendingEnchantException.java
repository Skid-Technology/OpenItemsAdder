package dev.lone.itemsadder.Core.OtherPlugins.enchants;

public final class AeWrapper$PendingEnchantException extends IllegalStateException {
   public final String enchant;

   private AeWrapper$PendingEnchantException(String enchant) {
      super("AdvancedEnchantments has not registered enchant '" + enchant + "'");
      this.enchant = enchant;
   }
}
