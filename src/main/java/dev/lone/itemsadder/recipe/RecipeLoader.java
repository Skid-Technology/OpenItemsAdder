package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.fontimage.InventoryTitleBuilder;
import dev.lone.itemsadder.gui.CustomInventoryHolder;
import dev.lone.itemsadder.gui.GuiButton;
import dev.lone.itemsadder.gui.GuiController;
import dev.lone.itemsadder.gui.GuiIconRegistry;
import dev.lone.itemsadder.gui.GuiPage;
import dev.lone.itemsadder.gui.ScrollableGuiInventory;
import dev.lone.itemsadder.gui.SlotLayout;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.lang.TextFormatCache;
import dev.lone.itemsadder.loot.LootMerchantTrades;
import dev.lone.itemsadder.network.ContainerPacketListener;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import dev.lone.itemsadder.text.PendingMessageStore;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeLoader {
   GuiController aJg;
   GuiIconRegistry bB;
   private final CustomItemDefinition aJz;
   CustomItemDefinition aJA;
   public ArrayList aJB;
   boolean aJC;
   public static CustomTexture aJD;
   public static CustomTexture aJE;
   public static CustomTexture aJF;
   public static CustomTexture aJG;
   public static CustomTexture aJH;
   public final HashMap aJI = new HashMap();
   public static ItemStack aJJ;
   public static ItemStack aJK;
   public static ItemStack aJL;
   public static ItemStack aJM;
   public static ItemStack aJN;
   public static ItemStack aJO;
   public static ItemStack aJP;
   public static ItemStack aJQ;
   public static ItemStack aJR;
   public static ItemStack aJS;
   public static ItemStack aJT;

   public RecipeLoader(GuiIconRegistry var1, GuiController var2, CustomItemDefinition var3, boolean var4) {
      this.aJz = var3;
      this.aJC = !var4;
      this.bB = var1;
      this.aJg = var2;
      this.aJA = var3.Dq();
   }

   public void LR() {
      this.aJB = new ArrayList(RecipeType.method1431(this.aJz, true));
   }

   public CustomItemDefinition Mc() {
      return this.aJA;
   }

   private static void method2547(CustomInventoryHolder var0, boolean var1, int var2, ItemStack var3, Consumer var4) {
      if (var4 == null) {
         var0.setItem(var2, var3);
      } else {
         var0.method2666(var2, var3, var2x -> {
            if (!var1 || var2x.getClick() == ClickType.LEFT || var2x.getClick() == ClickType.RIGHT) {
               var4.accept(var2x);
            }
         });
      }
   }

   public void method2548(RecipeLoader var1, GuiPage var2, Player var3, boolean var4) {
      if (!this.aJC) {
         int var5;
         if (!this.aJI.containsKey(var3.getUniqueId())) {
            this.aJI.put(var3.getUniqueId(), 0);
            var5 = 0;
         } else {
            var5 = (Integer)this.aJI.get(var3.getUniqueId());
         }

         AbstractCustomRecipe var6 = (AbstractCustomRecipe)this.aJB.get(var5);
         LootMerchantTrades var7 = this.bB.ad(var6.JR());
         int var8 = this.aJg.aIU.aJU.getInt("settings.crafting_preview.title_position_pixels");
         String var9 = this.aJg.aIU.aJU.jV("settings.crafting_preview.title_color");
         String var10 = ItemStackUtils.method7249(var6.JR(), var9);
         var10 = var10.replace(ChatColor.WHITE.toString(), var9);
         CustomItemDefinition var11 = ContentManager.method8084().method8149(var6.JR());
         Component var12 = var11 == null ? null : var11.itemName();
         boolean var13 = var12 instanceof TranslatableComponent;
         int var15 = 0;
         CustomTexture var14;
         switch (var6) {
            case CraftingRecipe var18:
               if (var6.JU()) {
                  var14 = aJE;
                  var15 = this.aJg.aIU.aJU.getInt("settings.crafting_preview.craft_font_image.x_position_pixels");
               } else {
                  var14 = aJD;
                  var15 = this.aJg
                     .aIU
                     .aJU
                     .getInt(
                        "settings.crafting_preview.no_recipe_font_image.x_position_pixels",
                        this.aJg.aIU.aJU.getInt("settings.crafting_preview.craft_font_image.x_position_pixels")
                     );
               }
               break;
            case CookingRecipe var19:
               var14 = aJF;
               var15 = this.aJg.aIU.aJU.getInt("settings.crafting_preview.cook_font_image.x_position_pixels");
               break;
            case AnvilRecipe var20:
               var14 = aJG;
               var15 = this.aJg.aIU.aJU.getInt("settings.crafting_preview.anvil_repair_font_image.x_position_pixels");
               break;
            case SmithingRecipe var21:
               var14 = aJH;
               var15 = this.aJg.aIU.aJU.getInt("settings.crafting_preview.smithing_font_image.x_position_pixels");
               break;
            default:
               throw new RuntimeException("Unknown recipe type!");
         }

         CustomTexture var42 = this.bB.method2572(var3, var14);
         boolean var43 = var42 != null;
         int var44 = var43 ? (var6.JU() ? 18 : 9) : 0;
         if (var43) {
            var14 = var42;
            var15 = -24;
            var8 = 14;
         }

         ArrayList var45 = new ArrayList();

         for (int var46 = 0; var46 < this.aJB.size(); var46++) {
            AbstractCustomRecipe var48 = (AbstractCustomRecipe)this.aJB.get(var46);
            boolean var22 = var43
               && var48 instanceof CookingRecipe var23
               && var45.stream().anyMatch(var2x -> this.aJB.get(var2x) instanceof CookingRecipe var3x && method2549(var23, var3x));
            if (!var22) {
               var45.add(var46);
            }
         }

         int var47 = var45.indexOf(var5);
         if (var47 < 0 && var6 instanceof CookingRecipe var49) {
            for (int var53 = 0; var53 < var45.size(); var53++) {
               if (this.aJB.get((Integer)var45.get(var53)) instanceof CookingRecipe var51 && method2549(var49, var51)) {
                  var47 = var53;
                  break;
               }
            }
         }

         int var50 = this.aJg.aIU.aJU.getInt("settings.cut_titles_with_length_grether_than", 25);
         int var52 = var43 && var45.size() > 1 ? Math.max(8, var50 - ("[" + (var47 + 1) + "/" + var45.size() + "] ").length()) : var50;
         int var54 = ChatColor.stripColor(var10).length();
         if (var54 > var52) {
            int var55 = 0;
            int var25 = 0;

            while (var55 < var10.length()) {
               if (var10.charAt(var55) == 167) {
                  var55++;
               } else {
                  var25++;
               }

               if (var25 >= var52) {
                  break;
               }

               var55++;
            }

            var10 = var10.substring(0, var55) + "...";
         }

         ArrayList var56 = new ArrayList();
         ArrayList var57 = new ArrayList();
         Component var26 = null;
         if (var14 != null) {
            Object var27 = var13 ? var12.color(null) : AdventureText.legacySerializer.deserialize(var10);
            Object var28 = var13 ? AdventureText.legacySerializer.deserialize(var9).append((Component)var27) : var27;
            if (var43) {
               var28 = GuiIconRegistry.method2575((Component)var28).colorIfAbsent(NamedTextColor.WHITE);
            }

            Component var29 = TextFormatCache.method9635(Component.text(var14.getFormattedText()).color(AdventureText.defaultColor), var15);
            Component var30 = var43
               ? var29.append(AdventureText.legacySerializer.deserialize(this.bB.method2576(var14)))
                  .append(AdventureText.legacySerializer.deserialize(this.bB.method2577(var14, true)))
                  .append(TextFormatCache.method9635(InventoryTitleBuilder.Md(), 14 - var14.getWidth()))
               : var29;
            if (var43) {
               var29 = var29.append(this.bB.method2578(var14, true, var7 == null || var7.aMR.isEmpty()))
                  .append(AdventureText.legacySerializer.deserialize(this.bB.method2577(var14, false)));
            }

            if (var43 && var7 != null && SlotLayout.cd(var7.aMR.size()) && this.bB.aKe != null) {
               CustomTexture var31 = this.bB.aKe;
               var29 = var29.append(
                     TextFormatCache.method9635(Component.text(var31.getFormattedText()).color(AdventureText.defaultColor), 148 - var14.getWidth() + 14)
                  )
                  .append(Component.text(TextFormatCache.method9636("", -var31.getWidth())));
            }

            Component var75 = var29.append(TextFormatCache.method9635((Component)var28, -var14.getWidth() + var8));
            if (var43 && var6 instanceof CookingRecipe var32) {
               for (AbstractCustomRecipe var34 : this.aJB) {
                  if (var34 instanceof CookingRecipe var35 && method2549(var32, var35)) {
                     CustomTexture var36 = (CustomTexture)this.bB.aKd.get(var35.Kj());
                     if (var36 != null) {
                        Component var37 = TextFormatCache.method9635(
                              Component.text(var36.getFormattedText()).color(AdventureText.defaultColor), 66 - var14.getWidth() + 14
                           )
                           .append(Component.text(TextFormatCache.method9636("", -var36.getWidth())));
                        Component var38 = TextFormatCache.method9635((Component)var28, -var14.getWidth() + var8);
                        Component var39 = var29.append(var37).append(var38);
                        if (!var56.contains(var39)) {
                           var56.add(var39);
                           var57.add(var30.append(var37).append(var38));
                        }
                     }
                  }
               }
            }

            if (var56.isEmpty()) {
               var56.add(var75);
               var57.add(var30.append(TextFormatCache.method9635((Component)var28, -var14.getWidth() + var8)));
            }

            if (!var43 && !var13) {
               var10 = AdventureText.legacySerializer.serialize(var75);
            } else {
               var26 = (Component)var56.getFirst();
            }
         } else {
            Log.error("GUI error: can't find font_image for recipe GUI. Check ia_gui.yml file.");
            var10 = " ";
         }

         String var58 = var26 == null ? var10 : "IA_CUST_GUI";
         CustomInventoryHolder var59 = var43 ? new ScrollableGuiInventory(var58) : new CustomInventoryHolder(54, var58);
         switch (var6) {
            case CraftingRecipe var76:
               if (!var76.aCg && var76.aCa != null) {
                  for (int var79 = 0; var79 < var76.aCa.length; var79++) {
                     String var82 = var76.aCa[var79];

                     for (int var85 = 0; var85 < var82.length(); var85++) {
                        ItemStack var87 = var76.method1593(var82.charAt(var85));
                        if (var87 != null && !var87.getType().isAir()) {
                           var59.method2666(var44 + 1 + var85 + var79 * 9, var87, var4x -> {
                              if (!GuiIconRegistry.method2581(var4x)) {
                                 this.method2552(var4x.getCurrentItem(), var2, this, var3, var4);
                              }
                           });
                        }
                     }
                  }
               } else {
                  List var78 = var76.Kn();
                  int var81 = 0;

                  for (int var84 = 0; var81 < var78.size(); var84++) {
                     for (int var86 = 0; var86 < 3 && var81 < var78.size(); var86++) {
                        var59.method2666(
                           var44 + var86 + 1 + var84 * 9,
                           ((ShapedIngredient)var78.get(var81)).getItem(),
                           var4x -> this.method2552(var4x.getCurrentItem(), var2, this, var3, var4)
                        );
                        var81++;
                     }
                  }
               }

               if (!var76.JU()) {
                  var59.setItem(var44 + 13, var6.JR());
               } else {
                  var59.method2666(var44 + 16, var6.JR(), var4x -> this.method2552(var4x.getCurrentItem(), var2, this, var3, var4));
               }
               break;
            case CookingRecipe var77:
               var59.method2666(var44 + 2, var77.Kg().getItemStack(), var4x -> this.method2552(var4x.getCurrentItem(), var2, this, var3, var4));
               var59.setItem(var44 + 20, new ItemStack(Material.COAL));
               if (!var43) {
                  var59.setItem(10, new ItemStack(var77.Kj()));
               }

               var59.method2666(var44 + (var43 ? 15 : 14), var6.JR(), var4x -> this.method2552(var4x.getCurrentItem(), var2, this, var3, var4));
               break;
            case AnvilRecipe var80:
               var59.method2666(var44 + 10, var80.Kd(), var4x -> this.method2552(var4x.getCurrentItem(), var2, this, var3, var4));
               var59.method2666(var44 + 13, var80.Ke(), var4x -> this.method2552(var4x.getCurrentItem(), var2, this, var3, var4));
               var59.method2666(var44 + 16, var6.JR(), var4x -> this.method2552(var4x.getCurrentItem(), var2, this, var3, var4));
               break;
            case SmithingRecipe var83:
               var59.method2666(var43 ? 28 : 1, var83.aCw.getItem(), var4x -> this.method2552(var4x.getCurrentItem(), var2, this, var3, var4));
               var59.method2666(var43 ? 29 : 10, var83.aCx.getItem(), var4x -> this.method2552(var4x.getCurrentItem(), var2, this, var3, var4));
               var59.method2666(var43 ? 30 : 13, var83.aCy.getItem(), var4x -> this.method2552(var4x.getCurrentItem(), var2, this, var3, var4));
               var59.method2666(var44 + 16, var6.JR(), var4x -> this.method2552(var4x.getCurrentItem(), var2, this, var3, var4));
               break;
            default:
               throw new RuntimeException("Unknown recipe type!");
         }

         if (!var43 && var7 != null) {
            int var61 = 37;
            if (!var7.aMN.isEmpty()) {
               ItemStack var65 = aJJ.clone();
               if (EnvironmentDetector.useNativeAdventure) {
                  ItemStackUtils.method7228(var65, var7.aMO);
               } else {
                  ItemStackUtils.method7227(var65, var7.aMN);
               }

               method2547(var59, var43, var61, var65, null);
               var61++;
            }

            if (!var7.aMH.isEmpty()) {
               ItemStack var66 = aJK.clone();
               ItemStackUtils.method7227(var66, var7.aMH);
               method2547(var59, var43, var61, var66, null);
               var61++;
            }

            if (!var7.aMI.isEmpty()) {
               ItemStack var67 = aJL.clone();
               ItemStackUtils.method7227(var67, var7.aMI);
               method2547(var59, var43, var61, var67, null);
               var61++;
            }

            if (!var7.aMJ.isEmpty()) {
               ItemStack var68 = aJM.clone();
               ItemStackUtils.method7227(var68, var7.aMJ);
               method2547(var59, var43, var61, var68, null);
               var61++;
            }

            if (!var7.aMK.isEmpty()) {
               ItemStack var69 = aJN.clone();
               ItemStackUtils.method7227(var69, var7.aMK);
               method2547(var59, var43, var61, var69, null);
               var61++;
            }

            if (!var7.aML.isEmpty()) {
               ItemStack var70 = aJO.clone();
               if (EnvironmentDetector.useNativeAdventure) {
                  ItemStackUtils.method7228(var70, var7.aMM);
               } else {
                  ItemStackUtils.method7227(var70, var7.aML);
               }

               method2547(var59, var43, var61, var70, null);
               var61++;
            }

            if (!var7.aMP.isEmpty()) {
               ItemStack var71 = aJP.clone();
               ItemStackUtils.method7227(var71, var7.aMP);
               method2547(var59, var43, var61, var71, null);
               var61++;
            }

            if (!var7.aMQ.isEmpty()) {
               ItemStack var72 = aJQ.clone();
               ItemStackUtils.method7227(var72, var7.aMQ);
               method2547(var59, var43, var61, var72, null);
               var61++;
            }
         }

         Consumer var63 = var6x -> {
            if (var1 != null) {
               var1.method2548(null, var2, var3, var4);
            } else if (var2 != null) {
               var2.method2529(var3, var4);
            } else if (var43) {
               this.aJg.bT(var3);
            } else {
               var3.closeInventory();
            }
         };
         if (!var43) {
            method2547(var59, false, 49, GuiIconRegistry.aKn, var63);
         }

         if (var47 > 0) {
            int var73 = (Integer)var45.get(var47 - 1);
            method2547(var59, var43, 52, var43 ? method2550(GuiIconRegistry.aKk, var47, var45.size()) : aJS, var6x -> {
               this.aJI.put(var3.getUniqueId(), var73);
               this.method2548(var1, var2, var3, var4);
            });
         }

         if (var47 >= 0 && var47 < var45.size() - 1) {
            int var74 = (Integer)var45.get(var47 + 1);
            method2547(var59, var43, 53, var43 ? method2550(GuiIconRegistry.aKl, var47, var45.size()) : aJT, var6x -> {
               this.aJI.put(var3.getUniqueId(), var74);
               this.method2548(var1, var2, var3, var4);
            });
         }

         if (var3.hasPermission("ia.admin.iaget")) {
            method2547(
               var59,
               var43,
               var43 ? (!var6.JU() ? 31 : (var6 instanceof CookingRecipe ? 42 : 43)) : (!var6.JU() ? 31 : 34),
               var43 ? GuiIconRegistry.aKm : aJR,
               var2x -> ItemStackUtils.method7243(var3, var6.getResultItemStackCopy())
            );
         }

         if (var26 != null) {
            PendingMessageStore.method2753(var3, var26);
         }

         if (var43) {
            ContainerPacketListener.method2604(
               var3, var59, var7 == null ? List.of() : var7.aMR.stream().map(var0 -> new GuiButton(var0, null)).toList(), this.bB.bZ(var3), var56, var57, var63
            );
         } else {
            var59.open(var3);
         }

         var3.setCompassTarget(var3.getLocation());
      }
   }

   private static boolean method2549(CookingRecipe var0, CookingRecipe var1) {
      return var0 == var1 || var0.vp != null && var0.aBm != null && var0.vp == var1.vp && Objects.equals(var0.aBm, var1.aBm);
   }

   private static ItemStack method2550(ItemStack var0, int var1, int var2) {
      if (var0 != null && !var0.getType().isAir()) {
         ItemStack var3 = var0.clone();
         ItemMeta var4 = var3.getItemMeta();
         ArrayList var5 = var4.hasLore() ? new ArrayList(var4.getLore()) : new ArrayList();
         var5.add(ChatColor.GRAY + "[" + (var1 + 1) + "/" + var2 + "]");
         var4.setLore(var5);
         var3.setItemMeta(var4);
         return var3;
      } else {
         return var0;
      }
   }

   static Component method2551(Component var0, Component var1, Component var2, int var3) {
      return var0.append(TextFormatCache.method9635(var1.append(var2.color(null)), var3));
   }

   void method2552(ItemStack var1, GuiPage var2, RecipeLoader var3, Player var4, boolean var5) {
      if (CustomItemDefinition.isCustomItem(var1)) {
         ((RecipeLoader)this.aJg.aIQ.get(CustomItemDefinition.method8750(var1))).method2548(var3, var2, var4, var5);
      }
   }

   void bU(Player var1) {
      if (this.aJI.containsKey(var1.getUniqueId())) {
         int var2 = (Integer)this.aJI.get(var1.getUniqueId());
         if (var2 - 1 >= 0) {
            this.aJI.put(var1.getUniqueId(), var2 - 1);
         }
      } else {
         this.aJI.put(var1.getUniqueId(), 0);
      }
   }

   void bV(Player var1) {
      if (this.aJI.containsKey(var1.getUniqueId())) {
         int var2 = (Integer)this.aJI.get(var1.getUniqueId());
         if (var2 < this.aJB.size() - 1) {
            this.aJI.put(var1.getUniqueId(), var2 + 1);
         }
      } else {
         this.aJI.put(var1.getUniqueId(), 0);
      }
   }
}
