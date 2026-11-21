package dev.lone.itemsadder.api.scriptinginternal;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

public class ItemsUtils {
   public static boolean isCustom(ItemStack itemStack) {
      if (itemStack == null) {
         return false;
      } else {
         return itemStack.getType() == Material.AIR ? false : ContentManager.method8084().isCustomItem(itemStack);
      }
   }

   @Nullable
   public static CustomStack customStack(String namespacedId) {
      return customStack(namespacedId, 1);
   }

   @Nullable
   public static CustomStack customStack(String namespacedId, int amount) {
      CustomItemDefinition var2 = ContentManager.method8084().method8154(namespacedId);
      if (var2 == null) {
         return null;
      }

      CustomStack var3 = CustomStack.fromInternal(var2);
      var3.getItemStack().setAmount(amount);
      return var3;
   }

   public static ItemStack newStack(Material material) {
      return newStack(material, 1);
   }

   public static ItemStack newStack(Material material, int amount) {
      ItemStack var2 = new ItemStack(material);
      var2.setAmount(amount);
      return var2;
   }

   public static boolean hasData(ItemStack itemStack, String namespace, String key) {
      return itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(namespace, key));
   }

   public static void removeData(ItemStack itemStack, String namespace, String key) {
      ItemMeta var3 = itemStack.getItemMeta();
      PersistentDataContainer var4 = var3.getPersistentDataContainer();
      if (var4.has(new NamespacedKey(namespace, key))) {
         var4.remove(new NamespacedKey(namespace, key));
         itemStack.setItemMeta(var3);
      }
   }

   public static byte getDataByte(ItemStack itemStack, String namespace, String key, byte def) {
      return (Byte)itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.BYTE, def);
   }

   public static void setDataByte(ItemStack itemStack, String namespace, String key, byte value) {
      ItemMeta var4 = itemStack.getItemMeta();
      var4.getPersistentDataContainer().set(new NamespacedKey(namespace, key), PersistentDataType.BYTE, value);
      itemStack.setItemMeta(var4);
   }

   public static short getDataShort(ItemStack itemStack, String namespace, String key, short def) {
      return (Short)itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.SHORT, def);
   }

   public static void setDataShort(ItemStack itemStack, String namespace, String key, short value) {
      ItemMeta var4 = itemStack.getItemMeta();
      var4.getPersistentDataContainer().set(new NamespacedKey(namespace, key), PersistentDataType.SHORT, value);
      itemStack.setItemMeta(var4);
   }

   public static int getDataInt(ItemStack itemStack, String namespace, String key, int def) {
      return (Integer)itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.INTEGER, def);
   }

   public static void setDataInt(ItemStack itemStack, String namespace, String key, int value) {
      ItemMeta var4 = itemStack.getItemMeta();
      var4.getPersistentDataContainer().set(new NamespacedKey(namespace, key), PersistentDataType.INTEGER, value);
      itemStack.setItemMeta(var4);
   }

   public static long getDataLong(ItemStack itemStack, String namespace, String key, long def) {
      return (Long)itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.LONG, def);
   }

   public static void setDataLong(ItemStack itemStack, String namespace, String key, long value) {
      ItemMeta var5 = itemStack.getItemMeta();
      var5.getPersistentDataContainer().set(new NamespacedKey(namespace, key), PersistentDataType.LONG, value);
      itemStack.setItemMeta(var5);
   }

   public static float getDataFloat(ItemStack itemStack, String namespace, String key, float def) {
      return (Float)itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.FLOAT, def);
   }

   public static void setDataFloat(ItemStack itemStack, String namespace, String key, float value) {
      ItemMeta var4 = itemStack.getItemMeta();
      var4.getPersistentDataContainer().set(new NamespacedKey(namespace, key), PersistentDataType.FLOAT, value);
      itemStack.setItemMeta(var4);
   }

   public static double getDataDouble(ItemStack itemStack, String namespace, String key, double def) {
      return (Double)itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.DOUBLE, def);
   }

   public static void setDataDouble(ItemStack itemStack, String namespace, String key, double value) {
      ItemMeta var5 = itemStack.getItemMeta();
      var5.getPersistentDataContainer().set(new NamespacedKey(namespace, key), PersistentDataType.DOUBLE, value);
      itemStack.setItemMeta(var5);
   }

   public static boolean getDataBool(ItemStack itemStack, String namespace, String key, boolean def) {
      return (Boolean)itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.BOOLEAN, def);
   }

   public static void setDataBool(ItemStack itemStack, String namespace, String key, boolean value) {
      ItemMeta var4 = itemStack.getItemMeta();
      var4.getPersistentDataContainer().set(new NamespacedKey(namespace, key), PersistentDataType.BOOLEAN, value);
      itemStack.setItemMeta(var4);
   }

   public static String getDataString(ItemStack itemStack, String namespace, String key, String def) {
      return (String)itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey(namespace, key), PersistentDataType.STRING, def);
   }

   public static void setDataString(ItemStack itemStack, String namespace, String key, String value) {
      ItemMeta var4 = itemStack.getItemMeta();
      var4.getPersistentDataContainer().set(new NamespacedKey(namespace, key), PersistentDataType.STRING, value);
      itemStack.setItemMeta(var4);
   }

   public static boolean hasData(CustomStack customStack, String namespace, String key) {
      return hasData(customStack.getItemStack(), namespace, key);
   }

   public static byte getDataByte(CustomStack customStack, String namespace, String key, byte def) {
      return getDataByte(customStack.getItemStack(), namespace, key, def);
   }

   public static void setDataByte(CustomStack customStack, String namespace, String key, byte value) {
      setDataByte(customStack.getItemStack(), namespace, key, value);
   }

   public static short getDataShort(CustomStack customStack, String namespace, String key, short def) {
      return getDataShort(customStack.getItemStack(), namespace, key, def);
   }

   public static void setDataShort(CustomStack customStack, String namespace, String key, short value) {
      setDataShort(customStack.getItemStack(), namespace, key, value);
   }

   public static int getDataInt(CustomStack customStack, String namespace, String key, int def) {
      return getDataInt(customStack.getItemStack(), namespace, key, def);
   }

   public static void setDataInt(CustomStack customStack, String namespace, String key, int value) {
      setDataInt(customStack.getItemStack(), namespace, key, value);
   }

   public static long getDataLong(CustomStack customStack, String namespace, String key, long def) {
      return getDataLong(customStack.getItemStack(), namespace, key, def);
   }

   public static void setDataLong(CustomStack customStack, String namespace, String key, long value) {
      setDataLong(customStack.getItemStack(), namespace, key, value);
   }

   public static float getDataFloat(CustomStack customStack, String namespace, String key, float def) {
      return getDataFloat(customStack.getItemStack(), namespace, key, def);
   }

   public static void setDataFloat(CustomStack customStack, String namespace, String key, float value) {
      setDataFloat(customStack.getItemStack(), namespace, key, value);
   }

   public static double getDataDouble(CustomStack customStack, String namespace, String key, double def) {
      return getDataDouble(customStack.getItemStack(), namespace, key, def);
   }

   public static void setDataDouble(CustomStack customStack, String namespace, String key, double value) {
      setDataDouble(customStack.getItemStack(), namespace, key, value);
   }

   public static boolean getDataBool(CustomStack customStack, String namespace, String key, boolean def) {
      return getDataBool(customStack.getItemStack(), namespace, key, def);
   }

   public static void setDataBool(CustomStack customStack, String namespace, String key, boolean value) {
      setDataBool(customStack.getItemStack(), namespace, key, value);
   }

   public static String getDataString(CustomStack customStack, String namespace, String key, String def) {
      return getDataString(customStack.getItemStack(), namespace, key, def);
   }

   public static void setDataString(CustomStack customStack, String namespace, String key, String value) {
      setDataString(customStack.getItemStack(), namespace, key, value);
   }
}
