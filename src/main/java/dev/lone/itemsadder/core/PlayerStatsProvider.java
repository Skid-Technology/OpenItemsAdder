package dev.lone.itemsadder.core;

import java.util.UUID;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class PlayerStatsProvider extends AbstractPlayerValueProvider {
   private final String dH = "ia_player_stats";

   public PlayerStatsProvider(Plugin var1, ContentManager var2) {
      super(var1, var2);
   }

   @Override
   public boolean method703(Player var1, String var2) {
      return var1.getPersistentDataContainer().has(new NamespacedKey("ia_player_stats", var2));
   }

   @Override
   public void method704(Player var1, String var2, float var3) {
      var1.getPersistentDataContainer().set(new NamespacedKey("ia_player_stats", var2), PersistentDataType.FLOAT, var3);
   }

   @Nullable
   @Override
   public Float method705(Player var1, String var2, @Nullable Float var3) {
      NamespacedKey var4 = new NamespacedKey("ia_player_stats", var2);
      return !var1.getPersistentDataContainer().has(var4, PersistentDataType.FLOAT)
         ? var3
         : (Float)var1.getPersistentDataContainer().getOrDefault(var4, PersistentDataType.FLOAT, 0.0F);
   }

   @Override
   void method706(UUID var1) {
   }

   @Override
   public void bC() {
   }

   @Override
   public void method707(boolean var1) {
   }
}
