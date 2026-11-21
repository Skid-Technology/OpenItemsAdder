package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.nbt.NItem;
import dev.lone.itemsadder.nms.ContainerClickAdapter;
import dev.lone.itemsadder.nms.InventoryAdapter;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemRegistryBridge {
   @Nullable
   default InventoryAdapter OG() {
      return this.OH();
   }

   @Nullable
   default ContainerClickAdapter OH() {
      return null;
   }

   void method3426(Player var1, Component var2);

   int cx(Player var1);

   String method3427(Material var1);

   default String ag(ItemStack var1) {
      return this.method3427(var1.getType());
   }

   void method3428(ItemStack var1, String var2);

   @Nullable
   Double ah(ItemStack var1);

   void method3429(NItem var1, String var2);

   ItemStack ai(ItemStack var1);

   void method3430(@NotNull ItemStack var1, @NotNull ItemStack var2);

   default void method3431(ItemStack bukkitItemStack, float seconds, String group) {
      throw new UnsupportedOperationException("This method is not supported in this version of Minecraft!");
   }

   default boolean aj(ItemStack bukkitItemStack) {
      return false;
   }

   void method3432(
      ItemStack var1, int var2, float var3, boolean var4, int var5, @Nullable String var6, @Nullable String var7, @Nullable Boolean var8, @Nullable List var9
   );

   default boolean ak(ItemStack itemStack) {
      return false;
   }

   default void method3433(
      ItemStack bukkitItemStack,
      EquipmentSlot bukkitEquipmentSlot,
      @Nullable NamespacedKey bukkitAssetId,
      @Nullable String equipSoundName,
      @Nullable NamespacedKey bukkitCameraOverlay,
      @Nullable List allowedEntitiesBukkit,
      @Nullable Boolean dispensable,
      @Nullable Boolean swappable,
      @Nullable Boolean damageOnHurt,
      @Nullable Boolean glider
   ) {
      throw new UnsupportedOperationException("This method is not supported in this version of Minecraft!");
   }

   default void method3434(ItemStack bukkitItemStack, @Nullable NamespacedKey bukkitTooltipStyleId) {
      throw new UnsupportedOperationException("This method is not supported in this version of Minecraft!");
   }

   void al(ItemStack var1);

   default void method3435(ItemStack bukkitItemStack, @Nullable NamespacedKey jukeboxSongId) {
      throw new UnsupportedOperationException("This method is not supported in this version of Minecraft!");
   }

   boolean method3436(ItemStack var1, ItemStack var2);

   default void method3437(ItemStack customItem, String modernItemModel) {
      throw new UnsupportedOperationException("This method is not supported in this version of Minecraft!");
   }

   default void method3438(ItemStack bukkitItemStack, Color color) {
      throw new UnsupportedOperationException("This method is not supported in this version of Minecraft!");
   }

   default void method3439(ItemStack bukkitItemStack, List colors) {
      throw new UnsupportedOperationException("This method is not supported in this version of Minecraft!");
   }

   default void method3440(ItemStack bukkitItemStack, List strings) {
      throw new UnsupportedOperationException("This method is not supported in this version of Minecraft!");
   }

   @Nullable
   default String method3441(ItemStack item, EquipmentSlot slot) {
      return null;
   }

   @Nullable
   default Integer am(ItemStack item) {
      return null;
   }

   default void method3442(ItemStack bukkitItemStack, List hasLayer2) {
      throw new UnsupportedOperationException("This method is not supported in this version of Minecraft!");
   }
}
