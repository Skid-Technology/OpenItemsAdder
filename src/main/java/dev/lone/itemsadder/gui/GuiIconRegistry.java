package dev.lone.itemsadder.gui;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.fontimage.InventoryTitleBuilder;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemActionBase;
import dev.lone.itemsadder.item.ItemMerchantAction;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.lang.TextFormatCache;
import dev.lone.itemsadder.loot.LootMerchantTrades;
import dev.lone.itemsadder.network.ContainerPacketListener;
import dev.lone.itemsadder.recipe.RecipeLoader;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import dev.lone.itemsadder.util.AdventureText;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.Plugin;

public class GuiIconRegistry {
   final YamlConfig aJU;
   final ContentManager aJV;
   final HashMap aJW;
   public final GuiController aJX;
   private final CustomTexture aJY;
   private final CustomTexture aJZ;
   private final Map aKa = new HashMap();
   private final ScrollTrackManager aKb = new ScrollTrackManager();
   private final ScrollTrackManager aKc = new ScrollTrackManager();
   final Map aKd = new HashMap();
   final CustomTexture aKe;
   private final CustomTexture aKf;
   private final CustomTexture aKg;
   private final CustomTexture aKh;
   static ItemStack aKi;
   static ItemStack aKj;
   static ItemStack aKk;
   static ItemStack aKl;
   static ItemStack aKm;
   public static ItemStack aKn;
   public static ItemStack aKo;
   public static ItemStack aKp;
   public static ItemStack aKq;
   public static boolean aKr = true;
   public static boolean aKs = false;
   public static List aKt = new ArrayList();

   public GuiIconRegistry(Plugin var1, ContentManager var2) {
      this.aJU = new YamlConfig(var1, true, "ia_gui.yml", true, true);
      this.aJV = var2;
      ContainerPacketListener.LE();
      this.aJY = (CustomTexture)var2.aE.Yp.get("_iainternal:scrollable_parchment_panel");
      this.aJZ = (CustomTexture)var2.aE.Yp.get("_iainternal:scrollable_parchment_single_panel");

      for (String var4 : List.of("no_recipe", "crafting", "cooking", "anvil_repair")) {
         this.aKa.put("_iainternal:" + var4, (CustomTexture)var2.aE.Yp.get("_iainternal:parchment_v4_" + var4 + "_preview"));
      }

      CustomTexture var13 = (CustomTexture)var2.aE.Yp.get("_iainternal:parchment_v4_smithing_preview");
      this.aKa.put("_iainternal:smithing", var13);
      this.aKa.put("_iainternal:smithing_template", var13);
      if (this.aJY != null) {
         this.aKb
            .method2596(
               var1,
               new NamespacedKey("_iainternal", "gui/scrollable/scrollbar/transparent"),
               new NamespacedKey("_iainternal", "gui/scrollable/scrollbar/thumb"),
               new NamespacedKey("_iainternal", "gui/scrollable/scrollbar/page_thumb")
            );
      }

      this.aKe = (CustomTexture)var2.aE.Yp.get("_iainternal:parchment_v4_guide_rail");
      this.aKf = (CustomTexture)var2.aE.Yp.get("_iainternal:parchment_v4_player_inventory");
      this.aKg = (CustomTexture)var2.aE.Yp.get("_iainternal:parchment_controls_show_inventory");
      this.aKh = (CustomTexture)var2.aE.Yp.get("_iainternal:parchment_controls_hide_inventory");
      this.aKc
         .method2596(
            var1,
            new NamespacedKey("_iainternal", "gui/scrollable/scrollbar/transparent"),
            new NamespacedKey("_iainternal", "gui/scrollable/scrollbar/thumb"),
            new NamespacedKey("_iainternal", "gui/scrollable/scrollbar/page_thumb")
         );

      for (Material var5 : List.of(Material.FURNACE, Material.BLAST_FURNACE, Material.SMOKER)) {
         this.aKd.put(var5, (CustomTexture)var2.aE.Yp.get("_iainternal:parchment_v4_" + var5.name().toLowerCase(Locale.ROOT)));
      }

      aKr = this.aJU.getBoolean("settings.categories_view.order.alphabetical");
      aKs = this.aJU.getBoolean("settings.categories_view.order.force_override.enabled");
      if (aKs) {
         aKt = this.aJU.jZ("settings.categories_view.order.force_override.list");
      }

      aKq = this.method2582("settings.categories_view.icons.search.item", "ia-button-search");
      aKn = this.method2582("settings.category_view.icons.back.item", "ia-button-back");
      aKi = this.method2579("_iainternal:dark_gui_back", "ia-button-back");
      aKo = this.method2582("settings.category_view.icons.previous_page.item", "ia-button-previous-page");
      aKp = this.method2582("settings.category_view.icons.next_page.item", "ia-button-next-page");
      RecipeLoader.aJJ = this.method2582("settings.crafting_preview.icons.loot_blocks.item", "ia-button-loot-blocks");
      RecipeLoader.aJK = this.method2582("settings.crafting_preview.icons.blocks_populators.item", "ia-button-blocks-populators");
      RecipeLoader.aJL = this.method2582("settings.crafting_preview.icons.surface_populators.item", "ia-button-surface-decorators");
      RecipeLoader.aJM = this.method2582("settings.crafting_preview.icons.trees_populators.item", "ia-button-trees-populators");
      RecipeLoader.aJN = this.method2582("settings.crafting_preview.icons.seed_result.item", "ia-button-seed-result");
      RecipeLoader.aJO = this.method2582("settings.crafting_preview.icons.loot_mobs.item", "ia-button-loot-mobs");
      RecipeLoader.aJP = this.method2582("settings.crafting_preview.icons.loot_fishing.item", "ia-button-loot-fishing");
      RecipeLoader.aJQ = this.method2582("settings.crafting_preview.icons.vehicle_fuel.item", "ia-button-vehicle-fuel-items");
      RecipeLoader.aJR = this.method2582("settings.crafting_preview.icons.get_item.item", "ia-button-get-item");
      RecipeLoader.aJS = this.method2582("settings.crafting_preview.icons.previous_recipe.item", "ia-button-previous-recipe");
      RecipeLoader.aJT = this.method2582("settings.crafting_preview.icons.next_recipe.item", "ia-button-next-recipe");
      aKj = this.method2580("settings.categories_view.icons.search.item", "icon_search", "search", "ia-button-search");
      aKk = this.method2580("settings.crafting_preview.icons.previous_recipe.item", "icon_left_blue", "previous", "ia-button-previous-recipe");
      aKl = this.method2580("settings.crafting_preview.icons.next_recipe.item", "icon_right_blue", "next", "ia-button-next-recipe");
      aKm = this.method2580("settings.crafting_preview.icons.get_item.item", "icon_arrow_chest", "get", "ia-button-get-item");
      RecipeLoader.aJD = (CustomTexture)var2.aE.Yp.get(this.aJU.getString("settings.crafting_preview.no_recipe_font_image.name"));
      RecipeLoader.aJE = (CustomTexture)var2.aE.Yp.get(this.aJU.getString("settings.crafting_preview.craft_font_image.name"));
      if (RecipeLoader.aJD == null) {
         RecipeLoader.aJD = RecipeLoader.aJE;
      }

      RecipeLoader.aJF = (CustomTexture)var2.aE.Yp.get(this.aJU.getString("settings.crafting_preview.cook_font_image.name"));
      RecipeLoader.aJG = (CustomTexture)var2.aE.Yp.get(this.aJU.getString("settings.crafting_preview.anvil_repair_font_image.name"));
      RecipeLoader.aJH = (CustomTexture)var2.aE.Yp.get(this.aJU.getString("settings.crafting_preview.smithing_font_image.name"));
      if ("_iainternal:smithing".equals(this.aJU.getString("settings.crafting_preview.smithing_font_image.name"))) {
         RecipeLoader.aJH = var2.aE.Yp.getOrDefault("_iainternal:smithing_template", RecipeLoader.aJH);
      }

      GuiPage.aIY = (CustomTexture)var2.aE.Yp.get(this.aJU.getString("settings.category_view.font_image.name"));
      GuiPage.aIZ = this.aJU.getInt("settings.category_view.title_position_pixels");
      GuiPage.aJa = this.aJU.getInt("settings.category_view.font_image.x_position_pixels");
      GuiController.aIT = (CustomTexture)var2.aE.Yp.get(this.aJU.getString("settings.categories_view.font_image.name"));
      this.aJW = new HashMap();

      for (Entry var17 : var2.field92.entrySet()) {
         this.aJW.put((String)var17.getKey(), new LootMerchantTrades(var2, (CustomItemDefinition)var17.getValue()));
      }

      for (CustomItemDefinition var18 : var2.field92.values()) {
         for (ItemActionBase var7 : var18.aoh.aiy.values()) {
            if (var7 instanceof ItemMerchantAction var8) {
               for (MerchantRecipe var10 : var8.merchantRecipes) {
                  CustomItemDefinition var11 = var2.method8149(var10.getResult());
                  LootMerchantTrades var12 = var11 == null ? null : (LootMerchantTrades)this.aJW.get(var11.getNamespacedID());
                  if (var12 != null) {
                     var12.method2740(var18, var10);
                  }
               }
            }
         }
      }

      this.aJX = new GuiController(var1, var2, this);
      this.aJX.LQ();
      this.aJX.LT();
      this.aJX.LS();
      this.aJX.LU();
      this.aJX.LV();
   }

   boolean method2571(Player var1, CustomTexture var2) {
      return ContainerPacketListener.method2599(var1)
         && this.aJY != null
         && this.aJZ != null
         && var2 != null
         && var2.getNamespacedID().equals("_iainternal:blank_menu")
         && !this.aKb.ca(var1).equals("_iainternal:gui/scroll_track");
   }

   CustomTexture method2572(Player var1, CustomTexture var2) {
      return ContainerPacketListener.method2599(var1) && var2 != null && !this.aKc.ca(var1).equals("_iainternal:gui/scroll_track")
         ? (CustomTexture)this.aKa.get(var2.getNamespacedID())
         : null;
   }

   Component method2573(String var1, int var2) {
      return this.method2574(var1, var2, false);
   }

   Component method2574(String var1, int var2, boolean var3) {
      CustomTexture var4 = var2 > 54 ? this.aJY : this.aJZ;
      TextComponent var5 = AdventureText.legacySerializer
         .deserialize(
            ChatColor.WHITE + TextFormatCache.method9634(var4.getFormattedText(), -24) + (var3 ? this.method2576(var4) : "") + this.method2577(var4, var3)
         );
      Component var6 = var5.append(var3 ? TextFormatCache.method9635(InventoryTitleBuilder.Md(), 14 - var4.getWidth()) : this.method2578(var4, false, false));
      return var6.append(
         TextFormatCache.method9635(method2575(AdventureText.legacySerializer.deserialize(var1)).colorIfAbsent(NamedTextColor.WHITE), -var4.getWidth() + 14)
      );
   }

   static Component method2575(Component var0) {
      TextColor var1 = var0.color();
      if (var1 != null && (var1.value() == 0 || var1.value() == 4210752 || var1.value() == 5592405)) {
         var0 = var0.color(NamedTextColor.WHITE);
      }

      return var0.children(var0.children().stream().map(GuiIconRegistry::method2575).toList());
   }

   String method2576(CustomTexture var1) {
      return this.aKf == null
         ? ""
         : ChatColor.WHITE
            + TextFormatCache.method9634(this.aKf.getFormattedText(), 13 - var1.getWidth())
            + TextFormatCache.method9636("", -this.aKf.getWidth());
   }

   String method2577(CustomTexture var1, boolean var2) {
      CustomTexture var3 = var2 ? this.aKh : this.aKg;
      return var3 == null
         ? ""
         : ChatColor.WHITE + TextFormatCache.method9634(var3.getFormattedText(), 122 - var1.getWidth()) + TextFormatCache.method9636("", -var3.getWidth());
   }

   Component method2578(CustomTexture var1, boolean var2, boolean var3) {
      if (var2 && var3) {
         return Component.empty();
      }

      Component var4 = Main.aFi.method7070(var2 ? "ia-panel-how-to-get" : "ia-panel-menu");
      return TextFormatCache.method9635(InventoryTitleBuilder.method2583(var4), 14 - var1.getWidth());
   }

   String bY(Player var1) {
      return this.aKb.ca(var1);
   }

   String bZ(Player var1) {
      return this.aKc.ca(var1);
   }

   private ItemStack method2579(String var1, String var2) {
      ItemStack var3 = this.aJV.method8179(var1);
      if (var3 == null) {
         return aKn.clone();
      }

      var3 = ItemOperationsService.getRegistryBridge().ai(var3);
      ItemStackUtils.method7222(var3, Main.aFi.lj(var2));
      return var3;
   }

   private ItemStack method2580(String var1, String var2, String var3, String var4) {
      return ("_iainternal:" + var2).equals(this.aJU.getString(var1)) ? this.method2579("_iainternal:dark_gui_" + var3, var4) : this.method2582(var1, var4);
   }

   public static boolean method2581(InventoryClickEvent var0) {
      ItemStack var1 = var0.getCurrentItem();
      if (var1 == null) {
         return false;
      }

      if (var0.getWhoClicked() instanceof Player var2 && var0.isShiftClick() && PermissionNodes.method1631(var2, "ia.admin.iaget", true)) {
         CustomItemDefinition var5 = ContentManager.method8084().method8149(var1);
         if (var5 != null) {
            ItemStack var6 = var5.Ds();
            var6.setAmount(1);
            ItemStackUtils.method7243(var2, var6);
            return true;
         }

         if (!var1.hasItemMeta()) {
            ItemStack var4 = var1.clone();
            var4.setAmount(1);
            ItemStackUtils.method7243(var2, var4);
            return true;
         }
      }

      return false;
   }

   public LootMerchantTrades ad(ItemStack var1) {
      return CustomItemDefinition.isCustomItem(var1) ? (LootMerchantTrades)this.aJW.get(CustomItemDefinition.method8750(var1)) : null;
   }

   ItemStack method2582(String var1, String var2) {
      ItemStack var3 = this.aJV.method8179(this.aJU.getString(var1));
      if (var3 == null) {
         var3 = new ItemStack(Material.STONE);
      }

      var3 = ItemOperationsService.getRegistryBridge().ai(var3);
      ItemStackUtils.method7222(var3, Main.aFi.lj(var2));
      return var3;
   }
}
