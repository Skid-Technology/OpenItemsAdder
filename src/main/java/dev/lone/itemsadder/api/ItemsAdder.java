package dev.lone.itemsadder.api;

import dev.lone.itemsadder.block.BlockRealizationMode;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.PlayerInteractionFacade;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemMaterialAction;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.item.PlantItemDefinition;
import dev.lone.itemsadder.loot.BlockToolDropRule;
import dev.lone.itemsadder.nms.NmsWorldService;
import dev.lone.itemsadder.util.SoundPlayer;
import dev.lone.itemsadder.world.CustomBlockChunkStoreBase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemsAdder {
   @Deprecated
   public static boolean areItemsLoaded() {
      return ContentManager.method8084() != null && ContentManager.method8084().field82;
   }

   @Deprecated
   public static boolean isCustomItem(ItemStack itemStack) {
      return CustomItemDefinition.isCustomItem(itemStack);
   }

   @Deprecated
   public static boolean isCustomItem(String customItemName) {
      return ContentManager.method8084().method8155(customItemName);
   }

   @Deprecated
   public static ItemStack getCustomItem(String namespacedId) {
      return ContentManager.method8084().method8179(namespacedId);
   }

   @Deprecated
   public static void placeCustomBlock(Location location, ItemStack itemStack) {
      String var2 = CustomItemDefinition.method8750(itemStack);
      CustomBlockDefinition var3 = ContentManager.method8084().method8160(var2);
      var3.placeBlockAt(location);
   }

   @Deprecated
   public static void removeCustomBlock(Location location) {
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8184(location);
      if (var1.isCustomBlock(location.getBlock())) {
         var1.removeCustomBlock(location.getBlock());
      }
   }

   @Deprecated
   public static ItemStack getCustomBlockByFaces(Material material, HashMap blockFaces) {
      return ContentManager.method8084().method8165(material, blockFaces);
   }

   @Deprecated
   public static boolean isCustomBlock(Block block) {
      if (CustomBlockDefinition.isCustomBlockType(block)) {
         CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8183(block);
         return var1.isCustomBlock(block);
      } else {
         return false;
      }
   }

   @Deprecated
   public static List getCustomBlockLoot(Block block, boolean includeSelfBlock) {
      return getCustomBlockLoot(block, new ItemStack(Material.DIAMOND_PICKAXE), includeSelfBlock);
   }

   @Deprecated
   public static List getCustomBlockLoot(Block block) {
      return getCustomBlockLoot(block, new ItemStack(Material.DIAMOND_PICKAXE), true);
   }

   @Deprecated
   public static List getCustomBlockLoot(Block block, ItemStack tool, boolean includeSelfBlock) {
      ArrayList var3 = new ArrayList();
      CustomBlockChunkStoreBase var4 = ContentManager.method8084().method8183(block);
      if (!var4.isCustomBlock(block)) {
         return var3;
      }

      CustomBlockDefinition var5 = ContentManager.method8084().method8160(var4.getCustomBlockId(block));
      if (var5 == null) {
         return var3;
      }

      if (!var5.matchesPlacementItem(tool)) {
         return var3;
      }

      List var6 = ContentManager.method8084().aJ.method1985(block, var5);
      if (var6 != null) {
         for (BlockToolDropRule var8 : var6) {
            if (var8.Bu().getNamespacedID().equals(var5.getNamespacedID()) && !var8.method1995(tool, block.getType())) {
               var3.addAll(var8.method2005(tool));
            }
         }
      }

      if (includeSelfBlock && var5.Dj()) {
         var3.add(var5.Ds());
      }

      return var3;
   }

   @Deprecated
   public static void placeCustomCrop(Location location, ItemStack seed) {
      String var2 = CustomItemDefinition.method8750(seed);
      CustomBlockChunkStoreBase var3 = ContentManager.method8084().method8183(location.getBlock());
      var3.setCropSeedId(location.getBlock(), var2);
      PlantItemDefinition var4 = (PlantItemDefinition)ContentManager.method8084().method8154(var2);
      var4.aC(location.getBlock());
   }

   @Deprecated
   public static boolean isCustomCrop(Block block) {
      if (PlantItemDefinition.aD(block)) {
         CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8183(block);
         return var1.getCropSeedId(block) != null;
      } else {
         return false;
      }
   }

   @Deprecated
   public static String getCustomSeedNameFromCrop(Block block) {
      CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8183(block);
      return var1.getCropSeedId(block);
   }

   @Deprecated
   @Nullable
   public static ItemStack getCustomBlock(Block block) {
      if (CustomBlockDefinition.isCustomBlockType(block)) {
         CustomBlockChunkStoreBase var1 = ContentManager.method8084().method8183(block);
         CustomBlockDefinition var2 = ContentManager.method8084().method8160(var1.getCustomBlockId(block));
         return var2 == null ? null : var2.Ds();
      } else {
         return new ItemStack(block.getType());
      }
   }

   @Deprecated
   public static boolean matchCustomItemName(ItemStack itemStack, String customItemName) {
      return CustomItemDefinition.method8780(itemStack, getCustomItem(customItemName));
   }

   @Deprecated
   public static boolean isFurniture(Entity entity) {
      if (entity == null) {
         return false;
      } else if (!(entity instanceof ArmorStand var1)) {
         return false;
      } else {
         ItemStack var2 = var1.getHelmet().clone();
         if (var2 == null) {
            return false;
         }

         if (!CustomItemDefinition.isCustomItem(var2)) {
            return false;
         }

         CustomItemDefinition var3 = ContentManager.method8084().method8149(var2);
         return var3.aoh.hasAction("furniture");
      }
   }

   @Deprecated
   public static String getCustomItemName(ItemStack itemStack) {
      return CustomItemDefinition.method8750(itemStack);
   }

   @Deprecated
   public static int getCustomItemUsages(ItemStack itemStack) {
      Integer var1 = CustomItemDefinition.method8765(itemStack);
      return var1 == null ? 0 : var1;
   }

   @Deprecated
   public static ItemStack setCustomItemDurability(ItemStack item, int durability) {
      if (!CustomItemDefinition.method8819(item)) {
         ItemStackUtils.method7235(item, durability);
         return item;
      } else {
         CustomItemDefinition.method8816(item, durability);
         return item;
      }
   }

   @Deprecated
   public static ItemStack setCustomItemMaxDurability(ItemStack item, int maxDurability) {
      if (!CustomItemDefinition.method8819(item)) {
         ItemStackUtils.method7235(item, maxDurability);
         return item;
      } else {
         CustomItemDefinition.method8815(item, maxDurability);
         return item;
      }
   }

   @Deprecated
   public static int getCustomItemDurability(ItemStack itemStack) {
      return CustomItemDefinition.method8819(itemStack) ? ItemStackUtils.aw(itemStack) : ItemStackUtils.aw(itemStack);
   }

   @Deprecated
   public static int getCustomItemMaxDurability(ItemStack itemStack) {
      return CustomItemDefinition.method8814(itemStack);
   }

   @Deprecated
   public static List getNamespacedBlocksNamesInConfig(String searchStr) {
      ArrayList var1 = new ArrayList();

      for (CustomBlockDefinition var3 : ContentManager.method8084().al) {
         if (searchStr == null
            || searchStr.isEmpty()
            || searchStr.equals("itemsadder")
            || "itemsadder".contains(searchStr)
            || var3.getNamespacedID().contains(searchStr)) {
            var1.add(var3.getNamespacedID());
         }
      }

      return var1;
   }

   @Deprecated
   public static List getNamespacedBlocksNamesInConfig() {
      return getNamespacedBlocksNamesInConfig(null);
   }

   @Deprecated
   public static BlockData getCustomBlockBlockData(ItemStack customBlock, boolean lightweight) {
      CustomBlockDefinition var2 = ContentManager.method8084().method8159(customBlock);
      return var2.aps != BlockRealizationMode.TILE ? var2.anF.getBlockData() : null;
   }

   public static boolean playTotemAnimation(Player player, String namespacedId) {
      CustomItemDefinition var2 = ContentManager.method8084().method8154(namespacedId);
      if (var2 == null) {
         return false;
      }

      SoundPlayer.method7390(Sound.ITEM_TOTEM_USE, player, SoundCategory.PLAYERS, 20L, () -> PlayerInteractionFacade.method5319(player, var2));
      return true;
   }

   @Deprecated
   public static void setLiquid(String namespacedID, Location location) {
      CustomItemDefinition var2 = ContentManager.method8084().method8154((String)ContentManager.method8084().by.get(namespacedID));
      if (var2 == null) {
         throw new RuntimeException("Unknown liquid bucket: " + namespacedID);
      }

      ItemMaterialAction var3 = (ItemMaterialAction)var2.aoh.getAction("liquid_bucket");
      if (var3 == null) {
         throw new RuntimeException("Custom item has lo 'liquid_bucket' behaviour: " + namespacedID);
      }

      Block var4 = location.getBlock();
      CustomBlockChunkStoreBase var5 = ContentManager.method8084().method8182(var4.getChunk());
      var5.setCustomBlockIdAndFlag(var4, namespacedID);
      var5.setSecondaryBlockId(var4, NmsWorldService.method3192(var4, Biome.PLAINS).getKey().toString());
      NmsWorldService.Om().method3194(var4, var3.AO().getNamespace(), var3.AO().getId());
      var4.setType(Material.WATER);
   }

   @Deprecated
   @Nullable
   public static String getLiquidName(Location location) {
      Block var1 = location.getBlock();
      CustomBlockChunkStoreBase var2 = ContentManager.method8084().method8182(var1.getChunk());
      return var2.isCustomBlock(var1) ? var2.getCustomBlockIdIfFlagged(var1) : null;
   }

   public static boolean hasKeepOnDeath(ItemStack itemStack) {
      CustomItemDefinition var1 = ContentManager.method8084().method8149(itemStack);
      return var1 == null ? false : var1.aoh.hasAction("keep_on_death");
   }

   public static boolean hasKeepOnDeath(String namespacedId) {
      CustomItemDefinition var1 = ContentManager.method8084().method8154((String)ContentManager.method8084().by.get(namespacedId));
      return var1 == null ? false : var1.aoh.hasAction("keep_on_death");
   }

   public static void applyResourcepack(Player player) {
      ContentManager.method8084().aL.delivery.sendPackTo(player);
   }

   public static List getAllItems() {
      if (ContentManager.method8084() != null && ContentManager.method8084().method8111()) {
         ArrayList var0 = new ArrayList();
         Map var1 = ContentManager.method8084().method8112();
         if (var1 == null) {
            return new ArrayList();
         }

         for (Entry var3 : var1.entrySet()) {
            var0.add(CustomStack.getInstance(((CustomItemDefinition)var3.getValue()).getNamespacedID()));
         }

         return var0;
      } else {
         return null;
      }
   }

   public static List getAllItems(String namespace) {
      if (ContentManager.method8084() != null && ContentManager.method8084().method8111()) {
         ArrayList var1 = new ArrayList();
         Map var2 = Bukkit.isPrimaryThread()
            ? (Map)ContentManager.method8084().af.get(namespace)
            : ContentManager.method8084()
               .method8112()
               .entrySet()
               .stream()
               .filter(entry -> ((CustomItemDefinition)entry.getValue()).aoi.equals(namespace))
               .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> a, LinkedHashMap::new));
         if (var2 == null) {
            return new ArrayList();
         }

         for (Entry var4 : var2.entrySet()) {
            var1.add(CustomStack.getInstance(((CustomItemDefinition)var4.getValue()).getNamespacedID()));
         }

         return var1;
      } else {
         return null;
      }
   }

   public static List getAllItems(Material material) {
      if (ContentManager.method8084() != null && ContentManager.method8084().method8111()) {
         ArrayList var1 = new ArrayList();
         List var2 = Bukkit.isPrimaryThread()
            ? (List)ContentManager.method8084().ag.get(material)
            : ContentManager.method8084().method8112().values().stream().filter(item -> item.getType() == material).toList();
         if (var2 == null) {
            return new ArrayList();
         }

         for (CustomItemDefinition var4 : var2) {
            var1.add(CustomStack.getInstance(var4.getNamespacedID()));
         }

         return var1;
      } else {
         return null;
      }
   }

   public static boolean isCustomRecipe(NamespacedKey namespacedKey) {
      return ContentManager.method8084().bp.dU(namespacedKey.toString());
   }

   public static boolean isCustomRecipe(String namespacedId) {
      return ContentManager.method8084().bp.dU(namespacedId);
   }

   public static List getAllRecipes() {
      return List.copyOf(ContentManager.method8084().bp.JY().values());
   }

   public static List getAllRecipes(String namespace) {
      return getAllRecipes().stream().filter(recipe -> recipe.getNamespacedKey().getNamespace().equals(namespace)).toList();
   }

   public static List getRecipesForItem(String namespacedId) {
      return ContentManager.method8084()
         .bp
         .JY()
         .values()
         .stream()
         .filter(recipe -> recipe.JS() != null && recipe.JS().getNamespacedID().equals(namespacedId))
         .map(recipe -> (CustomRecipe)recipe)
         .toList();
   }

   @Nullable
   public static String getPackUrl(boolean appendHash) {
      try {
         return ContentManager.method8084().aL.delivery.getUrl();
      } catch (Throwable var2) {
         return null;
      }
   }
}
