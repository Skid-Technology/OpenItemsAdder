package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.ConnectionHandle;
import beer.devs.nativepackets.api.FastPacketBatch;
import beer.devs.nativepackets.api.FastPacketCapability;
import beer.devs.nativepackets.api.NativePacketFactory;

public abstract class FastPacketAction {
   private Object aRZ;

   public abstract boolean method3162(ConnectionHandle var1);

   public abstract void method3163(FastPacketBatch var1);

   protected abstract Object method3164(NativePacketFactory var1);

   public final Object method3165(NativePacketFactory var1) {
      if (this.aRZ == null) {
         this.aRZ = this.method3164(var1);
      }

      return this.aRZ;
   }

   public static Object method3166(Object var0, NativePacketFactory var1) {
      return var0 instanceof FastPacketAction var2 ? var2.method3165(var1) : var0;
   }

   public static boolean method3167(NativePacketsBridge var0, ConnectionRecord var1, Object var2, Runnable var3) {
      if (!var0.method1873(var1)) {
         return false;
      }

      if (var2 instanceof FastPacketAction var4 && var1.PC().supportsFast(FastPacketCapability.MIXED_BATCH) && var4.method3162(var1.PC())) {
         try {
            return var1.PC().fastBatch(false, true, var2x -> {
               try {
                  var4.method3163(var2x);
               } catch (RuntimeException | Error var4x) {
                  var3.run();
                  throw var4x;
               }
            });
         } catch (RuntimeException | Error var6) {
            var3.run();
            throw var6;
         }
      } else {
         return var0.method1871(var1, method3166(var2, var0.Pv()));
      }
   }
}
