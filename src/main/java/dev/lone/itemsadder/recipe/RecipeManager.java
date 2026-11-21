package dev.lone.itemsadder.recipe;

import java.util.List;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public interface RecipeManager {
   void register();

   void unregister();

   Object method595(NamespacedKey var1);

   Object method596(NamespacedKey var1);

   void method597(Player var1, List var2);
}
