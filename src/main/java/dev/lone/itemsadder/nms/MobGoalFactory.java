package dev.lone.itemsadder.nms;

import dev.lone.itemsadder.entity.EntityTypePairRule;
import java.util.function.Function;
import java.util.function.Predicate;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.Nullable;

public interface MobGoalFactory {
   void method4593(Mob var1);

   void method4594(Mob var1, boolean var2, int var3, String var4, Function var5);

   void method4595(Mob var1);

   void method4596(Mob var1, Predicate var2, Runnable var3);

   void method4597(Mob var1, boolean var2, int var3, @Nullable EntityType var4, float var5, float var6, boolean var7, @Nullable Predicate var8);

   void method4598(Mob var1, boolean var2, int var3);

   void method4599(Mob var1, boolean var2, int var3, double var4, int var6);

   void method4600(Mob var1, boolean var2, int var3, @Nullable EntityType var4, int var5, boolean var6, boolean var7, @Nullable Predicate var8);

   void method4601(Mob var1, boolean var2, int var3, double var4, boolean var6);

   void method4602(Mob var1, boolean var2, int var3, double var4, int var6, float var7);

   void method4603(Mob var1, boolean var2, int var3, @Nullable EntityType var4, float var5, double var6, double var8, @Nullable Predicate var10);

   void method4604(Mob var1, boolean var2, int var3, double var4, boolean var6, Predicate var7);

   int method4605(Mob var1, boolean var2, String var3, @Nullable Integer var4, boolean var5);

   void method4606(Mob var1, boolean var2, int var3, String var4, double var5, float var7, float var8, int var9);

   default void method4607(
      Mob bukkitMob,
      boolean replaceVanilla,
      int priority,
      @Nullable EntityTypePairRule[] ignorePredicates,
      @Nullable EntityTypePairRule[] alertIgnorePredicates
   ) {
   }

   void method4608(Mob var1, boolean var2, int var3);

   void method4609(Mob var1, boolean var2, int var3);

   void method4610(Mob var1, boolean var2, int var3, Predicate var4, double var5, int var7);

   void method4611(Mob var1, boolean var2, int var3, boolean var4);

   void method4612(Mob var1, boolean var2, int var3);

   void method4613(Mob var1, boolean var2, int var3, Predicate var4);

   void method4614(Mob var1, int var2, int var3, int var4);

   void method4615(Mob var1, int var2, boolean var3, float var4);
}
