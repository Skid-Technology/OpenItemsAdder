package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.util.ServerCompat;
import java.lang.reflect.Field;
import org.bukkit.entity.LivingEntity;

public abstract class EntityFieldSwapper {
   public static void swapFields(LivingEntity var0, Field var1, Field var2, Object var3, Object var4) {
      ServerCompat.writeField(var1, var3, var4);
      ServerCompat.writeField(var2, var4, var3);
   }
}
