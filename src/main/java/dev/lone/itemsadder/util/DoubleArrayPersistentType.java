package dev.lone.itemsadder.util;

import java.nio.ByteBuffer;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class DoubleArrayPersistentType implements PersistentDataType {
   public Class getPrimitiveType() {
      return byte[].class;
   }

   public Class getComplexType() {
      return double[].class;
   }

   public byte[] encodeArray(double[] var1, PersistentDataAdapterContext var2) {
      ByteBuffer var3 = ByteBuffer.allocate(var1.length * 8);

      for (double var7 : var1) {
         var3.putDouble(var7);
      }

      return var3.array();
   }

   public double[] decodeArray(byte[] var1, PersistentDataAdapterContext var2) {
      if (var1.length % 8 == 0 && var1.length <= 256) {
         ByteBuffer var3 = ByteBuffer.wrap(var1);
         double[] var4 = new double[var1.length / 8];

         for (int var5 = 0; var5 < var4.length; var5++) {
            var4[var5] = var3.getDouble();
         }

         return var4;
      } else {
         return new double[0];
      }
   }
}
