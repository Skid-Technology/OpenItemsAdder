package dev.lone.itemsadder.core;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPlayerValueProvider {
   final Plugin dF;
   final ContentManager dG;

   protected AbstractPlayerValueProvider(Plugin var1, ContentManager var2) {
      this.dF = var1;
      this.dG = var2;
   }

   public void method701(Player var1) {
   }

   public boolean method702(Player var1) {
      return true;
   }

   public abstract boolean method703(Player var1, String var2);

   public abstract void method704(Player var1, String var2, float var3);

   public float method1429(Player var1, String var2, float var3) {
      Float var4 = this.method705(var1, var2, var3);
      if (var4 == null) {
         var4 = var3;
      }

      return var4;
   }

   @Nullable
   public abstract Float method705(Player var1, String var2, @Nullable Float var3);

   void method706(UUID var1) {
   }

   public void bC() {
   }

   public void method707(boolean var1) {
   }
}
