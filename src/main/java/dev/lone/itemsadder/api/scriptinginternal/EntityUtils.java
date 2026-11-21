package dev.lone.itemsadder.api.scriptinginternal;

import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;

public class EntityUtils {
   private static PersistentDataContainer getContainer(Entity entity) {
      return entity == null ? null : entity.getPersistentDataContainer();
   }

   public static boolean hasData(Entity entity, String namespace, String key) {
      PersistentDataContainer var3 = getContainer(entity);
      return ScriptingUtils.hasData(var3, namespace, key);
   }

   public static void removeData(Entity entity, String namespace, String key) {
      PersistentDataContainer var3 = getContainer(entity);
      ScriptingUtils.removeData(var3, namespace, key);
   }

   public static void setDataByte(Entity entity, String namespace, String key, byte value) {
      PersistentDataContainer var4 = getContainer(entity);
      ScriptingUtils.setDataByte(var4, namespace, key, value);
   }

   public static byte getDataByte(Entity entity, String namespace, String key, byte def) {
      PersistentDataContainer var4 = getContainer(entity);
      return ScriptingUtils.getDataByte(var4, namespace, key, def);
   }

   public static void setDataShort(Entity entity, String namespace, String key, short value) {
      PersistentDataContainer var4 = getContainer(entity);
      ScriptingUtils.setDataShort(var4, namespace, key, value);
   }

   public static short getDataShort(Entity entity, String namespace, String key, short def) {
      PersistentDataContainer var4 = getContainer(entity);
      return ScriptingUtils.getDataShort(var4, namespace, key, def);
   }

   public static void setDataInt(Entity entity, String namespace, String key, int value) {
      PersistentDataContainer var4 = getContainer(entity);
      ScriptingUtils.setDataInt(var4, namespace, key, value);
   }

   public static int getDataInt(Entity entity, String namespace, String key, int def) {
      PersistentDataContainer var4 = getContainer(entity);
      return ScriptingUtils.getDataInt(var4, namespace, key, def);
   }

   public static void setDataLong(Entity entity, String namespace, String key, long value) {
      PersistentDataContainer var5 = getContainer(entity);
      ScriptingUtils.setDataLong(var5, namespace, key, value);
   }

   public static long getDataLong(Entity entity, String namespace, String key, long def) {
      PersistentDataContainer var5 = getContainer(entity);
      return ScriptingUtils.getDataLong(var5, namespace, key, def);
   }

   public static void setDataFloat(Entity entity, String namespace, String key, float value) {
      PersistentDataContainer var4 = getContainer(entity);
      ScriptingUtils.setDataFloat(var4, namespace, key, value);
   }

   public static float getDataFloat(Entity entity, String namespace, String key, float def) {
      PersistentDataContainer var4 = getContainer(entity);
      return ScriptingUtils.getDataFloat(var4, namespace, key, def);
   }

   public static void setDataDouble(Entity entity, String namespace, String key, double value) {
      PersistentDataContainer var5 = getContainer(entity);
      ScriptingUtils.setDataDouble(var5, namespace, key, value);
   }

   public static double getDataDouble(Entity entity, String namespace, String key, double def) {
      PersistentDataContainer var5 = getContainer(entity);
      return ScriptingUtils.getDataDouble(var5, namespace, key, def);
   }

   public static void setDataBool(Entity entity, String namespace, String key, boolean value) {
      PersistentDataContainer var4 = getContainer(entity);
      ScriptingUtils.setDataBool(var4, namespace, key, value);
   }

   public static boolean getDataBool(Entity entity, String namespace, String key, boolean def) {
      PersistentDataContainer var4 = getContainer(entity);
      return ScriptingUtils.getDataBool(var4, namespace, key, def);
   }

   public static void setDataString(Entity entity, String namespace, String key, String value) {
      PersistentDataContainer var4 = getContainer(entity);
      ScriptingUtils.setDataString(var4, namespace, key, value);
   }

   public static String getDataString(Entity entity, String namespace, String key, String def) {
      PersistentDataContainer var4 = getContainer(entity);
      return ScriptingUtils.getDataString(var4, namespace, key, def);
   }
}
