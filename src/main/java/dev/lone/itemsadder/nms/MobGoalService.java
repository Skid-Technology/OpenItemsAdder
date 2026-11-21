package dev.lone.itemsadder.nms;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public final class MobGoalService {
   private final MobGoalFactory bdS = (MobGoalFactory)NmsRegistry.method2777(MobGoalFactory.class);
   private static MobGoalService bdT;

   private static MobGoalService OU() {
      if (bdT == null) {
         bdT = new MobGoalService();
      }

      return bdT;
   }

   public static MobGoalFactory OV() {
      return OU().bdS;
   }

   public static MethodHandle method4851(Class var0, Class var1) {
      try {
         MethodHandle var6;
         try {
            var6 = MethodHandles.publicLookup().unreflect(var0.getMethod("setTarget", var1, TargetReason.class));
         } catch (NoSuchMethodException var4) {
            var6 = MethodHandles.publicLookup().unreflect(var0.getMethod("setTarget", var1, TargetReason.class, boolean.class));
            var6 = MethodHandles.insertArguments(var6, 3, true);
         }

         return var6.asType(MethodType.methodType(boolean.class, Object.class, Object.class, TargetReason.class));
      } catch (ReflectiveOperationException var5) {
         throw new IllegalStateException("Cannot resolve native mob target setter", var5);
      }
   }

   public static boolean method4852(MethodHandle var0, Object var1, Object var2, TargetReason var3) {
      try {
         return (boolean)var0.invokeExact((Object)var1, (Object)var2, (TargetReason)var3);
      } catch (RuntimeException | Error var5) {
         throw var5;
      } catch (Throwable var6) {
         throw new IllegalStateException("Cannot set native mob target", var6);
      }
   }

   MobGoalService() {
   }
}
