package dev.lone.itemsadder.loot;

import com.google.common.collect.ImmutableList;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.block.BlockInteractionKind;
import dev.lone.itemsadder.block.BlockSoundProfile;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.CustomMobSettings;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackPair;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.item.ItemThrowAction;
import dev.lone.itemsadder.item.ItemVariantHolder;
import dev.lone.itemsadder.item.PlantItemDefinition;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.EntityUtils;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.worldgen.BlockDecorator;
import dev.lone.itemsadder.worldgen.BlockPopulator;
import dev.lone.itemsadder.worldgen.SurfaceFeature;
import dev.lone.itemsadder.worldgen.SurfacePopulator;
import dev.lone.itemsadder.worldgen.TreeDecorator;
import dev.lone.itemsadder.worldgen.TreePopulator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class LootMerchantTrades {
   final ContentManager aMF;
   final CustomItemDefinition aMG;
   public final List aMH;
   public final List aMI;
   public final List aMJ;
   public final List aMK;
   public final List aML;
   public final List aMM;
   public final List aMN;
   public final List aMO;
   public final List aMP;
   public final List aMQ;
   public final List aMR = new ArrayList();

   public LootMerchantTrades(ContentManager var1, CustomItemDefinition var2) {
      this.aMF = var1;
      this.aMG = var2;
      this.aMH = new ArrayList();
      this.aMI = new ArrayList();
      this.aMJ = new ArrayList();
      this.aMK = new ArrayList();
      this.aML = new ArrayList();
      this.aMM = new ArrayList();
      this.aMN = new ArrayList();
      this.aMO = new ArrayList();
      this.aMP = new ArrayList();
      this.aMQ = new ArrayList();
      this.MV();
   }

   private static String[] method2738(String var0, int var1) {
      String var2 = var0.replaceAll("((?:\\w+\\s){" + --var1 + "}\\w+)(\\s)", "$1\n");
      return var2.split("\n");
   }

   private String[] method2739(Set var1, int var2) {
      String var3 = var1.toString().replace("[", "").replace("]", "").replace(",", "");
      String[] var4 = method2738(var3, var2);
      int var5 = 0;

      for (int var6 = var4.length; var5 < var6; var5++) {
         String var7 = var4[var5];
         var4[var5] = var7.replace(" ", ", ");
      }

      return var4;
   }

   public void MV() {
      this.aMR.clear();
      boolean var1 = false;

      for (BlockPopulator var3 : BlockPopulator.qj) {
         List var4 = (List)var3.aEc.get(this.aMG.aol);
         if (var4 != null) {
            for (BlockDecorator var6 : var4) {
               World var7 = var3.ez();
               if (var7 != null) {
                  if (!var1) {
                     this.aMH.add(Main.aFi.lj("worlds"));
                  }

                  var1 = true;
                  int var8 = this.aMH.size();
                  this.aMH.add(ChatColor.AQUA + var7.getName());
                  this.aMH.add(Main.aFi.lj("chance").replace("{value}", var6.aDA + ""));
                  this.aMH.add(Main.aFi.lj("amount").replace("{value}", var6.aDw + ""));
                  this.aMH.add(Main.aFi.lj("min-height").replace("{value}", var6.aDz + ""));
                  this.aMH.add(Main.aFi.lj("max-height").replace("{value}", var6.aDy + ""));
                  if (!var6.aDB.isEmpty()) {
                     this.aMH.add(Main.aFi.lj("biome"));

                     for (String var12 : this.method2739(var6.aDB, 3)) {
                        this.aMH.add(ChatColor.GRAY + "     " + var12);
                     }
                  }

                  this.method2741(var6.aDu.Ds(), this.aMH, var8);
               }
            }
         }
      }

      for (SurfacePopulator var22 : SurfacePopulator.qj) {
         List var31 = (List)var22.aEc.get(this.aMG.aol);
         if (var31 != null) {
            for (SurfaceFeature var48 : var31) {
               World var56 = var22.ez();
               if (var56 != null) {
                  if (!var1) {
                     this.aMI.add(Main.aFi.lj("worlds"));
                  }

                  var1 = true;
                  int var66 = this.aMI.size();
                  this.aMI.add(ChatColor.AQUA + var56.getName());
                  this.aMI.add(Main.aFi.lj("chance").replace("{value}", var48.aDA + ""));
                  this.aMI.add(Main.aFi.lj("amount").replace("{value}", var48.aDw + ""));
                  this.aMI.add(Main.aFi.lj("min-height").replace("{value}", var48.aDz + ""));
                  this.aMI.add(Main.aFi.lj("max-height").replace("{value}", var48.aDy + ""));
                  if (!var48.aDB.isEmpty()) {
                     this.aMI.add(Main.aFi.lj("biome"));

                     for (String var95 : this.method2739(var48.aDB, 3)) {
                        this.aMI.add(ChatColor.GRAY + "     " + var95);
                     }
                  }

                  this.method2741(var48.aDv != null ? var48.aDv.Ds() : var48.aDu.Ds(), this.aMI, var66);
               }
            }
         }
      }

      for (TreePopulator var23 : TreePopulator.qj) {
         for (TreeDecorator var41 : var23.aDJ) {
            World var49 = var23.ez();
            if (var49 != null && (this.aMG.method8778(var41.aDW) || this.aMG.method8778(var41.aDX))) {
               if (!var1) {
                  this.aMJ.add(Main.aFi.lj("worlds"));
               }

               var1 = true;
               int var57 = this.aMJ.size();
               this.aMJ.add(ChatColor.AQUA + var49.getName());
               this.aMJ.add(Main.aFi.lj("chance").replace("{value}", var41.aDA + ""));
               this.aMJ.add(Main.aFi.lj("amount").replace("{value}", var41.aDw + ""));
               this.aMJ.add(Main.aFi.lj("min-height").replace("{value}", var41.aDz + ""));
               this.aMJ.add(Main.aFi.lj("max-height").replace("{value}", var41.aDy + ""));
               if (var41.aDB.size() > 0) {
                  this.aMJ.add(Main.aFi.lj("biome"));

                  for (String var92 : this.method2739(var41.aDB, 3)) {
                     this.aMJ.add(ChatColor.GRAY + "     " + var92);
                  }
               }

               this.method2741(var41.aDW.Ds(), this.aMJ, var57);
            }
         }
      }

      for (PlantItemDefinition var24 : this.aMF.ab.values()) {
         for (ItemVariantHolder var42 : var24.apu) {
            if (var42.Fq() && this.aMG.method8778(var42.getDefinition())) {
               int var50 = this.aMK.size();
               this.aMK.add(ChatColor.AQUA + var24.getItemMeta().getDisplayName());
               this.aMK.add(Main.aFi.lj("chance").replace("{value}", var42.getScale() + ""));
               if (var42.Fr() == var42.Fs()) {
                  this.aMK.add(Main.aFi.lj("amount").replace("{value}", var42.Fr() + ""));
               } else {
                  this.aMK.add(Main.aFi.lj("amount").replace("{value}", var42.Fr() + "-" + var42.Fs()));
               }

               this.method2741(var24.Ds(), this.aMK, var50);
            }
         }
      }

      for (BlockSoundProfile var25 : this.aMF.au.values()) {
         if (var25.field45 != null) {
            for (LootDropRulesImplA var43 : var25.field45.values().stream().flatMap(Collection::stream).distinct().toList()) {
               for (ItemVariantHolder var58 : var43.Hn()) {
                  if (var58.Fq() && this.aMG.method8778(var58.getDefinition())) {
                     ArrayList var68 = new ArrayList();
                     var68.add(
                        ChatColor.GRAY
                           + String.join(
                              " / ",
                              var25.field45
                                 .entrySet()
                                 .stream()
                                 .filter(var1x -> ((ImmutableList)var1x.getValue()).contains(var43))
                                 .map(var0 -> Main.aFi.lj("ia-guide-action-" + ((BlockInteractionKind)var0.getKey()).name().toLowerCase(Locale.ROOT)))
                                 .toList()
                           )
                     );
                     int var77 = var43.kH == null ? 0 : var43.kH;
                     int var85 = var43.kI == null ? var25.jW : var43.kI;
                     if (var77 > 0 || var85 < var25.jW) {
                        var68.add(
                           ChatColor.GRAY + Main.aFi.lj("ia-guide-crop-age").replace("{value}", var77 == var85 ? Integer.toString(var77) : var77 + "–" + var85)
                        );
                     }

                     var68.add(Main.aFi.lj("chance").replace("{value}", var58.getScale() + ""));
                     this.method2742(var25.jQ.Ds(), var68, 0, var58);
                  }
               }
            }
         }
      }

      for (Entry var26 : this.aMF.aJ.ato.entrySet()) {
         for (MaterialToolDropRule var44 : (List)var26.getValue()) {
            for (ItemVariantHolder var59 : var44.Hn()) {
               if (var59.Fq() && this.aMG.method8778(var59.getDefinition())) {
                  int var69 = this.aMN.size();
                  if (EnvironmentDetector.useNativeAdventure) {
                     this.aMN.add(ChatColor.AQUA + "TRANSLATE_" + ItemStackUtils.ag(var44.Ep()));
                  } else {
                     this.aMN.add(ChatColor.AQUA + ItemStackUtils.af(var44.Ep()));
                  }

                  if (!var44.Hq().isEmpty()) {
                     this.aMN.add(Main.aFi.lj("biome"));

                     for (String var96 : this.method2739(var44.Hq(), 3)) {
                        this.aMN.add(ChatColor.GRAY + "     " + var96);
                     }
                  }

                  this.aMN.add(Main.aFi.lj("chance").replace("{value}", var59.getScale() + ""));
                  if (var59.Fr() == var59.Fs()) {
                     this.aMN.add(Main.aFi.lj("amount").replace("{value}", var59.Fr() + ""));
                  } else {
                     this.aMN.add(Main.aFi.lj("amount").replace("{value}", var59.Fr() + "-" + var59.Fs()));
                  }

                  this.method2741(new ItemStack(var44.Ep()), this.aMN, var69 + 1);
               }
            }
         }
      }

      for (Entry var27 : this.aMF.aJ.atp.entrySet()) {
         for (BlockToolDropRule var45 : (List)var27.getValue()) {
            for (ItemVariantHolder var60 : var45.Hn()) {
               if (var60.Fq() && this.aMG.method8778(var60.getDefinition())) {
                  int var70 = this.aMN.size();
                  this.aMN.add(ChatColor.AQUA + var45.Bu().getItemMeta().getDisplayName());
                  if (!var45.Hq().isEmpty()) {
                     this.aMN.add(Main.aFi.lj("biome"));

                     for (String var97 : this.method2739(var45.Hq(), 3)) {
                        this.aMN.add(ChatColor.GRAY + "     " + var97);
                     }
                  }

                  this.aMN.add(Main.aFi.lj("chance").replace("{value}", var60.getScale() + ""));
                  if (var60.Fr() == var60.Fs()) {
                     this.aMN.add(Main.aFi.lj("amount").replace("{value}", var60.Fr() + ""));
                  } else {
                     this.aMN.add(Main.aFi.lj("amount").replace("{value}", var60.Fr() + "-" + var60.Fs()));
                  }

                  this.method2741(var45.Bu().Ds(), this.aMN, var70 + 1);
               }
            }
         }
      }

      for (ItemDropApplier var28 : this.aMF.aJ.atq) {
         for (ItemVariantHolder var46 : var28.Hn()) {
            if (var46.Fq() && this.aMG.method8778(var46.getDefinition())) {
               int var54 = this.aMP.size();
               if (!var28.Hq().isEmpty()) {
                  this.aMP.add(Main.aFi.lj("biome"));

                  for (String var88 : this.method2739(var28.Hq(), 3)) {
                     this.aMP.add(ChatColor.GRAY + "     " + var88);
                  }
               }

               this.aMP.add(Main.aFi.lj("chance").replace("{value}", var46.getScale() + ""));
               this.method2742(new ItemStack(Material.FISHING_ROD), this.aMP, var54, var46);
            }
         }
      }

      for (LootTable var29 : this.aMF.aJ.atr) {
         for (ItemVariantHolder var47 : var29.Hn()) {
            if (var47.Fq() && this.aMG.method8778(var47.getDefinition())) {
               int var55 = this.aML.size();
               if (var29 instanceof EntityLootTable var62) {
                  Optional var72 = var29.dy("ItemsAdderEntity");
                  if (var72.isPresent() && CustomEntityManager.isInitialized()) {
                     CustomMobSettings var81 = CustomEntityManager.getInstance().getSettings((String)((MetadataDropTrigger)var72.get()).Ht());
                     if (var81 != null) {
                        Object var89 = ChatColor.stripColor(var81.vr);
                        this.aML.add(ChatColor.AQUA + var89);
                     }
                  }

                  this.method2745(var62);
               }

               if (var29 instanceof NamespacedLootEntry var63) {
                  this.aML.add(ChatColor.AQUA + var63.Eo());
                  this.method2744(var63);
               }

               if (!var29.Hq().isEmpty()) {
                  this.aML.add(Main.aFi.lj("biome"));

                  for (String var90 : this.method2739(var29.Hq(), 3)) {
                     this.aML.add(ChatColor.GRAY + "     " + var90);
                  }
               }

               this.aML.add(Main.aFi.lj("chance").replace("{value}", var47.getScale() + ""));

               for (NbtDropTrigger var74 : var29.Hv()) {
                  this.aML.add(Main.aFi.lj("nbt-rule").replace("{rule}", var74.Hx() + "").replace("{value}", var74.Hy() + ""));
               }

               this.method2742(this.method2743(var29), this.aML, var55, var47);
            }
         }
      }

      if (this.aMG.aoh.hasAction("vehicle")) {
         ItemThrowAction var21 = (ItemThrowAction)this.aMG.aoh.getAction("vehicle");
         assert var21 != null;

         for (ItemStackPair var39 : var21.aeQ) {
            if (var39.itemStack.hasItemMeta()) {
               this.aMQ
                  .add(
                     Main.aFi
                        .lj("fuel-item")
                        .replace("{type}", ChatColor.stripColor(var39.itemStack.getItemMeta().getDisplayName()))
                        .replace("{amount}", var39.aif + "")
                  );
            } else {
               this.aMQ
                  .add(
                     Main.aFi.lj("fuel-item").replace("{type}", ItemStackUtils.method7247(var39.itemStack, ChatColor.GRAY)).replace("{amount}", var39.aif + "")
                  );
            }

            this.method2741(
               var39.itemStack,
               List.of(Main.aFi.lj("ia-guide-vehicle-fuel"), Main.aFi.lj("ia-guide-fuel-refill").replace("{amount}", Integer.toString(var39.aif))),
               0
            );
         }
      }

      if (EnvironmentDetector.useNativeAdventure) {
         this.method2746(this.aMN, this.aMO);
         this.method2746(this.aML, this.aMM);
      }
   }

   public void method2740(CustomItemDefinition var1, MerchantRecipe var2) {
      ItemStack var3 = var1.Ds();
      var3.setAmount(1);
      ItemMeta var4 = var3.getItemMeta();
      ArrayList var5 = new ArrayList();
      var5.add(AdventureText.legacySerializer.deserialize(Main.aFi.lj("get-from-trade-machine")));
      var5.add(AdventureText.legacySerializer.deserialize(Main.aFi.lj("ia-guide-trade-cost")));

      for (ItemStack var7 : var2.getIngredients()) {
         Object var8 = ItemStackUtils.as(var7);
         if (var8 == null) {
            var8 = Component.translatable(var7.getType().translationKey());
         }

         var5.add(Component.text(var7.getAmount() + " × ", NamedTextColor.GRAY).append(var8.colorIfAbsent(NamedTextColor.WHITE)));
      }

      var5.add(AdventureText.legacySerializer.deserialize(Main.aFi.lj("amount").replace("{value}", Integer.toString(var2.getResult().getAmount()))));
      var4.lore(var5);
      var3.setItemMeta(var4);
      this.aMR.add(var3);
   }

   private void method2741(ItemStack var1, List var2, int var3) {
      this.method2742(var1, var2, var3, null);
   }

   private void method2742(ItemStack var1, List var2, int var3, ItemVariantHolder var4) {
      if (var1 != null) {
         ItemStack var5 = var1.clone();
         var5.setAmount(1);
         ArrayList var6 = new ArrayList();

         for (String var8 : var2.subList(var3, var2.size())) {
            if (!var8.contains("TRANSLATE_")) {
               var6.add(var8);
            }
         }

         if (var4 != null) {
            var6.add(Main.aFi.lj("amount").replace("{value}", var4.Fr() == var4.Fs() ? Integer.toString(var4.Fr()) : var4.Fr() + "-" + var4.Fs()));
         }

         ItemMeta var9 = var5.getItemMeta();
         var9.setLore(var6);
         var5.setItemMeta(var9);
         this.aMR.add(var5);
      }
   }

   private ItemStack method2743(LootTable var1) {
      EntityType var2 = null;
      if (var1 instanceof EntityLootTable var3) {
         var2 = var3.getType();
      } else if (var1 instanceof NamespacedLootEntry var4 && CustomEntityManager.isInitialized()) {
         CustomMobSettings var5 = (CustomMobSettings)CustomEntityManager.getInstance().oN.get(var4.Eo());
         if (var5 != null) {
            var2 = var5.vw;
         }
      }

      Material var6 = var2 == null ? null : Material.matchMaterial(var2.name() + "_SPAWN_EGG");
      return new ItemStack(var6 == null ? Material.EGG : var6);
   }

   private void method2744(NamespacedLootEntry var1) {
      if (CustomEntityManager.isInitialized()) {
         CustomMobSettings var2 = (CustomMobSettings)CustomEntityManager.getInstance().oN.get(var1.Eo());
         if (var2 != null) {
            this.aML.add(var2.vr);
         }
      }
   }

   private void method2745(EntityLootTable var1) {
      if (EnvironmentDetector.useNativeAdventure) {
         this.aML.add(ChatColor.AQUA + "TRANSLATE_" + EntityUtils.toTranslationKey(var1.getType()));
      } else {
         this.aML.add(ChatColor.AQUA + EntityUtils.toDisplayName(var1.getType()));
      }
   }

   private void method2746(List var1, List var2) {
      for (String var4 : var1) {
         if (var4.contains("TRANSLATE_")) {
            String var8 = ChatColor.stripColor(var4).replace("TRANSLATE_", "");
            BaseComponent[] var9 = TextComponent.fromLegacyText(var4);
            ChatColor var7 = var9[0].getColor();
            var9[0] = new TranslatableComponent(var8, new Object[0]);
            var9[0].setColor(var7);
            var9[0].setItalic(false);
            var2.add(var9);
         } else {
            BaseComponent[] var5 = TextComponent.fromLegacyText(var4);

            for (int var6 = 0; var6 < var5.length; var6++) {
               var5[var6].setItalic(false);
            }

            var2.add(var5);
         }
      }

      if (!var1.isEmpty()) {
         var1.clear();
         var1.add("dummy");
      }
   }
}
