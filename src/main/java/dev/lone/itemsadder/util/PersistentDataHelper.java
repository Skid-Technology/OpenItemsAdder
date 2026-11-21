package dev.lone.itemsadder.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public class PersistentDataHelper {
   public static void setLocation(PersistentDataHolder var0, NamespacedKey var1, Location var2) {
      var0.getPersistentDataContainer()
         .set(
            var1,
            PersistentDataType.STRING,
            var2.getWorld().getName() + "," + var2.getX() + "," + var2.getY() + "," + var2.getZ() + "," + var2.getYaw() + "," + var2.getPitch()
         );
   }

   public static Location getLocation(PersistentDataHolder var0, NamespacedKey var1) {
      String var2 = (String)var0.getPersistentDataContainer().get(var1, PersistentDataType.STRING);
      if (var2 != null) {
         String[] var3 = var2.split(",");
         return new Location(
            Bukkit.getServer().getWorld(var3[0]),
            Double.parseDouble(var3[1]),
            Double.parseDouble(var3[2]),
            Double.parseDouble(var3[3]),
            Float.parseFloat(var3[4]),
            Float.parseFloat(var3[5])
         );
      } else {
         return null;
      }
   }

   public static void removeKey(PersistentDataHolder var0, NamespacedKey var1) {
      var0.getPersistentDataContainer().remove(var1);
   }

   public static byte[] compress(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         GZIPOutputStream var2 = new GZIPOutputStream(var1);
         var2.write(var0.getBytes(StandardCharsets.UTF_8));
         var2.close();
         return var1.toByteArray();
      } else {
         return null;
      }
   }

   public static String decompress(byte[] var0) {
      if (var0 != null && var0.length != 0) {
         ByteArrayInputStream var1 = new ByteArrayInputStream(var0);
         GZIPInputStream var2 = new GZIPInputStream(var1);
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         byte[] var4 = new byte[256];

         int var5;
         while ((var5 = var2.read(var4)) >= 0) {
            var3.write(var4, 0, var5);
         }

         return var3.toString(StandardCharsets.UTF_8);
      } else {
         return null;
      }
   }

   @Nullable
   public static String getCompressedString(PersistentDataHolder var0, NamespacedKey var1) {
      if (var0.getPersistentDataContainer().has(var1)) {
         try {
            return decompress((byte[])var0.getPersistentDataContainer().get(var1, PersistentDataType.BYTE_ARRAY));
         } catch (IOException var3) {
            Log.error("Failed to get compressed persistent string from " + var0, var3);
            return null;
         }
      } else {
         return null;
      }
   }

   public static void setCompressedString(PersistentDataHolder var0, NamespacedKey var1, String var2) {
      try {
         var0.getPersistentDataContainer().set(var1, PersistentDataType.BYTE_ARRAY, compress(var2));
      } catch (IOException var4) {
         Log.error("Failed to set compressed persistent string from " + var0, var4);
      }
   }

   @Nullable
   public static String getStringValue(PersistentDataHolder var0, NamespacedKey var1) {
      return var0.getPersistentDataContainer().has(var1) ? (String)var0.getPersistentDataContainer().get(var1, PersistentDataType.STRING) : null;
   }

   public static void setStringValue(PersistentDataHolder var0, NamespacedKey var1, String var2) {
      var0.getPersistentDataContainer().set(var1, PersistentDataType.STRING, var2);
   }

   public static boolean getBooleanOr(Entity var0, NamespacedKey var1, boolean var2) {
      Boolean var3 = (Boolean)var0.getPersistentDataContainer().get(var1, PersistentDataType.BOOLEAN);
      return var3 != null ? var3 : var2;
   }
}
