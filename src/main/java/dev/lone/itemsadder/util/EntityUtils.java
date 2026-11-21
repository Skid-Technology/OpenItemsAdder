package dev.lone.itemsadder.util;

import org.apache.commons.lang.WordUtils;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EntityUtils {
   public static String toDisplayName(EntityType var0) {
      return WordUtils.capitalizeFully(var0.toString()).replace("_", " ");
   }

   public static String toTranslationKey(EntityType var0) {
      return String.format("entity.minecraft.%s", var0.toString().toLowerCase());
   }

   public static boolean isValidBarStyle(String var0) {
      try {
         BarStyle.valueOf(var0);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static boolean isValidBarColor(String var0) {
      try {
         BarColor.valueOf(var0);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static boolean isValidEntityType(String var0) {
      try {
         EntityType.valueOf(var0);
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static boolean isWithinRange(LivingEntity var0, LivingEntity var1, double var2, float var4) {
      if (!var0.hasLineOfSight(var1)) {
         return false;
      } else if (BlockFaceUtils.simulateDamage(var0, var1)) {
         var1.damage(var4);
         return true;
      } else {
         return false;
      }
   }

   public static boolean isProjectileHitting(Projectile var0, LivingEntity var1, double var2, float var4) {
      if (BlockFaceUtils.simulateDamage(var0, var1)) {
         var1.damage(var4);
         return true;
      } else {
         return false;
      }
   }

   public static Transformation copyTransformation(Transformation transformation) {
      Vector3f var1 = transformation.getTranslation();
      Quaternionf var2 = transformation.getLeftRotation();
      Vector3f var3 = transformation.getScale();
      Quaternionf var4 = transformation.getRightRotation();
      Vector3f var5 = new Vector3f(var1);
      Quaternionf var6 = new Quaternionf(var2);
      Vector3f var7 = new Vector3f(var3);
      Quaternionf var8 = new Quaternionf(var4);
      return new Transformation(var5, var6, var7, var8);
   }

   public static Transformation identityTransformation() {
      return new Transformation(new Vector3f(0.0F, 0.0F, 0.0F), new Quaternionf(), new Vector3f(1.0F, 1.0F, 1.0F), new Quaternionf());
   }
}
