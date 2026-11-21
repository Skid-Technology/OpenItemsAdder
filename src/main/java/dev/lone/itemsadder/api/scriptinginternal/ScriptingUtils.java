package dev.lone.itemsadder.api.scriptinginternal;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.ServerCompat;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ScriptingUtils {
   public static void msg(Player player, String message, boolean minimessage) {
      if (minimessage) {
         msg(player, AdventureText.parseRichText(message));
      } else {
         player.sendMessage(message);
      }
   }

   public static void msg(Player player, String message) {
      player.sendMessage(message);
   }

   public static void msg(Player player, Component message) {
      if (ServerCompat.isPaper()) {
         player.sendMessage(message);
      } else {
         player.spigot().sendMessage(AdventureText.toBungee(message));
      }
   }

   public static void log(String message) {
      Bukkit.getConsoleSender().sendMessage(message);
   }

   public static void _runDelayed(long delay, Runnable task) {
      if (delay > 0L) {
         Bukkit.getScheduler().runTaskLater(Main.getPlugin(), task, delay);
      } else if (!Bukkit.isPrimaryThread()) {
         Bukkit.getScheduler().runTask(Main.getPlugin(), task);
      } else {
         task.run();
      }
   }

   public static Object cast(Class clazz, Object obj) {
      if (obj == null) {
         return null;
      } else if (clazz.isInstance(obj)) {
         return obj;
      } else {
         throw new ClassCastException("Cannot cast " + obj.getClass().getName() + " to " + clazz.getName());
      }
   }

   public static boolean hasData(PersistentDataContainer container, String namespace, String key) {
      return container.has(new NamespacedKey(namespace, key));
   }

   public static void removeData(PersistentDataContainer container, String namespace, String key) {
      container.remove(new NamespacedKey(namespace, key));
   }

   public static byte getDataByte(PersistentDataContainer container, String namespace, String key, byte def) {
      return (Byte)container.getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.BYTE, def);
   }

   public static void setDataByte(PersistentDataContainer container, String namespace, String key, byte value) {
      container.set(new NamespacedKey(namespace, key), PersistentDataType.BYTE, value);
   }

   public static short getDataShort(PersistentDataContainer container, String namespace, String key, short def) {
      return (Short)container.getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.SHORT, def);
   }

   public static void setDataShort(PersistentDataContainer container, String namespace, String key, short value) {
      container.set(new NamespacedKey(namespace, key), PersistentDataType.SHORT, value);
   }

   public static int getDataInt(PersistentDataContainer container, String namespace, String key, int def) {
      return (Integer)container.getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.INTEGER, def);
   }

   public static void setDataInt(PersistentDataContainer container, String namespace, String key, int value) {
      container.set(new NamespacedKey(namespace, key), PersistentDataType.INTEGER, value);
   }

   public static long getDataLong(PersistentDataContainer container, String namespace, String key, long def) {
      return (Long)container.getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.LONG, def);
   }

   public static void setDataLong(PersistentDataContainer container, String namespace, String key, long value) {
      container.set(new NamespacedKey(namespace, key), PersistentDataType.LONG, value);
   }

   public static float getDataFloat(PersistentDataContainer container, String namespace, String key, float def) {
      return (Float)container.getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.FLOAT, def);
   }

   public static void setDataFloat(PersistentDataContainer container, String namespace, String key, float value) {
      container.set(new NamespacedKey(namespace, key), PersistentDataType.FLOAT, value);
   }

   public static double getDataDouble(PersistentDataContainer container, String namespace, String key, double def) {
      return (Double)container.getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.DOUBLE, def);
   }

   public static void setDataDouble(PersistentDataContainer container, String namespace, String key, double value) {
      container.set(new NamespacedKey(namespace, key), PersistentDataType.DOUBLE, value);
   }

   public static boolean getDataBool(PersistentDataContainer container, String namespace, String key, boolean def) {
      return (Boolean)container.getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.BOOLEAN, def);
   }

   public static void setDataBool(PersistentDataContainer container, String namespace, String key, boolean value) {
      container.set(new NamespacedKey(namespace, key), PersistentDataType.BOOLEAN, value);
   }

   public static String getDataString(PersistentDataContainer container, String namespace, String key, String def) {
      return (String)container.getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.STRING, def);
   }

   public static void setDataString(PersistentDataContainer container, String namespace, String key, String value) {
      container.set(new NamespacedKey(namespace, key), PersistentDataType.STRING, value);
   }
}
