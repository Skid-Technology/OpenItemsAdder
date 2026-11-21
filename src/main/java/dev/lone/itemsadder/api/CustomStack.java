package dev.lone.itemsadder.api;

import beer.devs.fastnbt.nms.nbt.NItem;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.EquipmentSlotType;
import dev.lone.itemsadder.item.ItemActionRegistry;
import dev.lone.itemsadder.item.ItemMerchantAction;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.item.ItemUseSession;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.NullableBox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.StreamSupport;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class CustomStack {
   CustomItemDefinition internalItem;
   protected ItemStack itemStack;
   protected NItem nbt;
   protected NullableBox cachedConfigSection;

   CustomStack(CustomItemDefinition customItem) {
      this(customItem, customItem);
   }

   CustomStack(CustomItemDefinition customItem, CustomItemDefinition itemStack) {
      this.internalItem = customItem;
      this.itemStack = itemStack.Ds();
      this.nbt = new NItem(itemStack);
   }

   CustomStack(CustomItemDefinition customItem, ItemStack itemStack) {
      this.internalItem = customItem;
      this.itemStack = itemStack.clone();
      this.nbt = new NItem(itemStack);
   }

   @Internal
   @Nullable
   public static CustomStack fromInternal(CustomItemDefinition customItem) {
      return customItem == null ? null : new CustomStack(customItem);
   }

   @Internal
   public CustomItemDefinition internal() {
      return this.internalItem;
   }

   protected void replace(ItemStack itemStack, CustomItemDefinition customItem) {
      this.itemStack = itemStack;
      this.internalItem = customItem;
      this.nbt = new NItem(itemStack);
   }

   public static Set getNamespacedIdsInRegistry() {
      ContentManager var0 = ContentManager.method8084();
      return var0 == null ? Set.of() : var0.method8112().keySet();
   }

   public static boolean isInRegistry(String namespacedId) {
      ContentManager var1 = ContentManager.method8084();
      return var1 != null && var1.method8112().containsKey(namespacedId);
   }

   @Nullable
   private static CustomStack getOriginal(String namespacedID) {
      ContentManager var1 = ContentManager.method8084();
      if (var1 != null && var1.method8111()) {
         CustomItemDefinition var2 = var1.method8154(namespacedID);
         return var2 == null ? null : new CustomStack(var2, var2);
      } else {
         return null;
      }
   }

   @Nullable
   public static CustomStack byItemStack(ItemStack itemStack) {
      ContentManager var1 = ContentManager.method8084();
      if (var1 != null && var1.method8111()) {
         CustomItemDefinition var2 = var1.method8149(itemStack);
         return var2 == null ? null : new CustomStack(var2, itemStack);
      } else {
         return null;
      }
   }

   @Nullable
   public static CustomStack getInstance(String namespacedID) {
      CustomStack var1 = getOriginal(namespacedID);
      if (var1 != null) {
         var1.itemStack = var1.internalItem.Ds();
      }

      return var1;
   }

   private void applyChanges() {
      this.nbt.refreshCopy();
      this.itemStack.setItemMeta(this.nbt.getItem().getItemMeta());
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public String getNamespace() {
      return this.internalItem.getNamespace();
   }

   public String getId() {
      return this.internalItem.getId();
   }

   public String getNamespacedID() {
      return this.internalItem.getNamespacedID();
   }

   public String getModelPath() {
      return this.internalItem.nC();
   }

   public String getConfigPath() {
      return this.internalItem.lH().GQ();
   }

   public FileConfiguration getConfig() {
      return this.internalItem.lH().abM();
   }

   @Nullable
   public String getPermission() {
      return this.internalItem.getPermissionSuffix();
   }

   public boolean hasPermission() {
      return this.internalItem.hasPermission();
   }

   public boolean matchNamespacedID(CustomStack other) {
      return this.internalItem.method8778(other.internalItem);
   }

   public boolean hasAutoGeneratedModel() {
      return this.internalItem.nB();
   }

   public List getTextures() {
      return this.internalItem.getTextures();
   }

   public boolean isBlockAllEnchants() {
      return this.internalItem.isBlockAllEnchants();
   }

   public List getBlockedEnchantments() {
      if (this.internalItem.isBlockAllEnchants()) {
         return StreamSupport.stream(Registry.ENCHANTMENT.spliterator(), false).toList();
      }

      ArrayList var1 = this.internalItem.Dw();
      return var1 == null ? List.of() : var1.stream().filter(Objects::nonNull).toList();
   }

   public boolean canEnchant(Enchantment enchantment) {
      Objects.requireNonNull(enchantment, "enchantment");
      ArrayList var2 = this.internalItem.Dw();
      return !this.internalItem.isBlockAllEnchants() && (var2 == null || !var2.contains(enchantment));
   }

   public boolean hasUsagesAttribute() {
      this.nbt.refreshCopy();
      return this.nbt.getCompound("itemsadder").hasKey("usages");
   }

   public void setUsages(int amount) {
      this.nbt.refreshCopy();
      this.nbt.getCompound("itemsadder").setInt("usages", amount);
      this.nbt.save();
      CustomItemDefinition.method8766(this.itemStack);
      this.applyChanges();
   }

   public void reduceUsages(int amount) {
      this.nbt.refreshCopy();
      this.nbt.getCompound("itemsadder").setInt("usages", this.nbt.getCompound("itemsadder").getInt("usages") - amount);
      this.nbt.save();
      CustomItemDefinition.method8766(this.itemStack);
      this.applyChanges();
   }

   public int getUsages() {
      this.nbt.refreshCopy();
      return this.nbt.getCompound("itemsadder").getInt("usages");
   }

   public boolean hasCustomDurability() {
      return CustomItemDefinition.method8819(this.itemStack);
   }

   public int getDurability() {
      return ItemStackUtils.aw(this.itemStack);
   }

   public void setDurability(int durability) {
      CustomItemDefinition.method8816(this.itemStack, durability);
      this.applyChanges();
   }

   public int getMaxDurability() {
      return CustomItemDefinition.method8814(this.itemStack);
   }

   public void setMaxDurability(int maxDurability) {
      CustomItemDefinition.method8815(this.itemStack, maxDurability);
      this.applyChanges();
   }

   public void setAttributeModifier(String attributeModifier, String slotStr, double value) {
      CustomItemDefinition.method8788(
         this.internalItem, this.itemStack, this.nbt, this.internalItem.getNamespacedID(), attributeModifier, slotStr, value, "add"
      );
      this.applyChanges();
   }

   @Deprecated
   public void setDisplayName(String displayName) {
      ItemMeta var2 = this.itemStack.getItemMeta();
      var2.setDisplayName(displayName);
      this.itemStack.setItemMeta(var2);
      this.applyChanges();
   }

   public String getDisplayName() {
      return this.internalItem.DE();
   }

   public Component itemName() {
      if (ItemStackUtils.am(this.itemStack.getType())) {
         return EnvironmentDetector.paperAdventure ? this.itemStack.getItemMeta().displayName() : AdventureText.fromNative(this.nbt.getCustomName());
      }

      if (EnvironmentDetector.paperAdventure) {
         return this.itemStack.getItemMeta().itemName();
      }

      Object var1 = this.nbt.getItemName();
      return var1 == null ? null : AdventureText.fromNative(var1);
   }

   public double getDamageMainhand() {
      return CustomItemDefinition.method8792(this.itemStack, EquipmentSlotType.MAINHAND);
   }

   public void updateAttackDamageLore(String slot) {
      if (Settings.get().fixAttackDamageLore) {
         CustomItemDefinition.method8802(this.itemStack, EquipmentSlotType.cP(slot));
         this.applyChanges();
      }
   }

   public void updateAttackSpeedLore(String slot) {
      if (Settings.get().fixAttackDamageLore) {
         CustomItemDefinition.method8805(this.itemStack, EquipmentSlotType.cP(slot));
         this.applyChanges();
      }
   }

   public boolean isBlock() {
      return this.internalItem instanceof CustomBlockDefinition;
   }

   public boolean drop(Location loc) {
      return LocationScanner.dropItem(loc, this.itemStack);
   }

   public ConfigurationSection getConfigSectionCopy() {
      return this.getConfigSectionCopy(true);
   }

   public ConfigurationSection getConfigSectionCopy(boolean merged) {
      if (this.cachedConfigSection != null) {
         return (ConfigurationSection)this.cachedConfigSection.value;
      }

      this.cachedConfigSection = NullableBox.of(getConfigSectionCopy(this.itemStack, merged));
      return (ConfigurationSection)this.cachedConfigSection.value;
   }

   public static boolean hasCooldown(Player player, ItemStack customItemStack) {
      return getCooldownMs(player, customItemStack) > 0L;
   }

   public static long getCooldownMs(Player player, ItemStack customItemStack) {
      HashMap var2 = (HashMap)ContentManager.method8084().aK.aiN.sessionsByPlayer.get(player.getUniqueId());
      if (var2 == null) {
         return -1L;
      }

      CustomItemDefinition var3 = ContentManager.method8084().method8149(customItemStack);
      if (var3 == null) {
         return -1L;
      }

      ItemUseSession var4 = (ItemUseSession)var2.get(var3.aol);
      return var4 == null ? -1L : var4.method10007();
   }

   public static boolean requiresStats(ItemStack customItemStack) {
      CustomItemDefinition var1 = ContentManager.method8084().method8149(customItemStack);
      return var1 == null ? false : var1.DK();
   }

   @Nullable
   public static Map getRequiredStats(ItemStack customItemStack) {
      CustomItemDefinition var1 = ContentManager.method8084().method8149(customItemStack);
      return var1 == null ? null : var1.DV();
   }

   public static boolean openTradeMenu(String namespacedId, Player player) {
      CustomItemDefinition var2 = ContentManager.method8084().method8154(namespacedId);
      if (var2 == null) {
         return false;
      }

      ItemMerchantAction var3 = getTradeMachineBehaviour(var2.aoh);
      if (var3 == null) {
         return false;
      }

      if (!PermissionNodes.method1631(player, var3.getPermission(), true)) {
         return false;
      }

      ContentManager.method8084().bf.openMerchant(player, var3);
      var3.aI(player);
      return true;
   }

   public static boolean isBlock(ItemStack itemStack) {
      CustomItemDefinition var1 = ContentManager.method8084().method8149(itemStack);
      return var1 == null ? false : var1 instanceof CustomBlockDefinition;
   }

   public static boolean isFurniture(ItemStack itemStack) {
      return hasBehaviour(itemStack, "furniture");
   }

   public static boolean isFurnitureConsumable(ItemStack itemStack) {
      return hasBehaviour(itemStack, "furniture_consumable");
   }

   public static boolean isFurnitureTradeMachine(ItemStack itemStack) {
      return hasBehaviour(itemStack, "furniture_trade_machine");
   }

   public static boolean isTradeMachine(ItemStack itemStack) {
      CustomItemDefinition var1 = ContentManager.method8084().method8149(itemStack);
      return var1 != null && getTradeMachineBehaviour(var1.aoh) != null;
   }

   public static boolean isFurnitureSittable(ItemStack itemStack) {
      return hasBehaviour(itemStack, "furniture_sit");
   }

   public static boolean isComplexFurniture(ItemStack itemStack) {
      return hasBehaviour(itemStack, "complex_furniture");
   }

   public static boolean isVehicle(ItemStack itemStack) {
      return hasBehaviour(itemStack, "vehicle");
   }

   private static boolean hasBehaviour(ItemStack itemStack, String behaviourName) {
      CustomItemDefinition var2 = ContentManager.method8084().method8149(itemStack);
      return var2 == null ? false : var2.aoh.hasAction(behaviourName);
   }

   @Nullable
   static ItemMerchantAction getTradeMachineBehaviour(ItemActionRegistry holder) {
      ItemMerchantAction var1 = (ItemMerchantAction)holder.getAction("furniture_trade_machine");
      return var1 != null ? var1 : (ItemMerchantAction)holder.getAction("block_trade_machine");
   }

   public static ConfigurationSection getConfigSectionCopy(ItemStack itemStack) {
      return getConfigSectionCopy(itemStack, true);
   }

   public static ConfigurationSection getConfigSectionCopy(ItemStack itemStack, boolean merged) {
      CustomItemDefinition var2 = ContentManager.method8084().method8149(itemStack);
      return var2 == null ? null : var2.aU(merged).bLU;
   }

   @Nullable
   public static ConfigurationSection getConfigSectionOfTemplateCopy(String namespacedId) {
      Map var1 = ContentManager.method8084().method8209();
      if (namespacedId.contains(":")) {
         return var1.containsKey(namespacedId) ? ((ConfigSectionWrapper)var1.get(namespacedId)).bLU : null;
      }

      for (Entry var3 : var1.entrySet()) {
         if (((String)var3.getKey()).endsWith(namespacedId)) {
            return ((ConfigSectionWrapper)var3.getValue()).bLU;
         }
      }

      return null;
   }
}
