package dev.lone.itemsadder.util;

import dev.lone.itemsadder.config.PatternMatcher;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class StringListMatcher {
   @Nullable
   private final List patterns;
   public final boolean matchesHand;

   public StringListMatcher(@Nullable List var1) {
      this.patterns = var1;
      boolean var2 = false;

      for (PatternMatcher var4 : var1) {
         String var5 = var4.pattern;
         if (var5.equalsIgnoreCase("hand") || var5.equalsIgnoreCase("empty") || var5.toLowerCase().contains("hand")) {
            var2 = true;
            break;
         }
      }

      this.matchesHand = var2;
   }

   public boolean matchesName(@Nullable String var1) {
      if (this.patterns == null || this.patterns.isEmpty()) {
         return true;
      }

      if (var1 != null && !var1.isEmpty() && !var1.equalsIgnoreCase("air")) {
         var1 = var1.toLowerCase();
         Iterator var2 = this.patterns.iterator();
         if (var2.hasNext()) {
            PatternMatcher var3 = (PatternMatcher)var2.next();
            if (!var3.bo().contains(":") && var1.contains(":")) {
               var1 = var1.substring(var1.indexOf(":") + 1);
            }
            return switch (var3.bn()) {
               case REGEX -> var1.matches(var3.bo());
               case WILDCARD -> FilenameUtils.wildcardMatch(var1, var3.bo());
               case EXACT -> var1.equals(var3.bo());
            };
         } else {
            return false;
         }
      } else {
         return this.matchesHand;
      }
   }

   public boolean matchesHeldItem(@Nullable Player var1) {
      if (var1 == null) {
         return true;
      }

      if (this.patterns != null && !this.patterns.isEmpty()) {
         ItemStack var2 = var1.getInventory().getItemInMainHand();
         if (var2.getType().isAir()) {
            return this.matchesHand;
         } else if (ContentManager.method8084().isCustomItem(var2)) {
            CustomItemDefinition var3 = ContentManager.method8084().method8149(var2);
            return var3 != null ? this.matchesName(var3.getNamespacedID()) : false;
         } else {
            return this.matchesName(var2.getType().toString().toLowerCase(Locale.ROOT));
         }
      } else {
         return true;
      }
   }
}
