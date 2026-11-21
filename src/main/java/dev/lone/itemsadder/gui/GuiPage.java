package dev.lone.itemsadder.gui;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.ItemOperationsService;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.lang.TextFormatCache;
import dev.lone.itemsadder.network.ContainerPacketListener;
import dev.lone.itemsadder.recipe.RecipeLoader;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class GuiPage {
   public static CustomTexture aIY;
   public static int aIZ;
   public static int aJa;
   static final int aJb = 45;
   public final String aJc;
   @Nullable
   String aJd;
   @Nullable
   private final String aJe;
   @Nullable
   private final String aJf;
   final GuiController aJg;
   @Nullable
   GuiPage aJh;
   final List aJi = new ArrayList();
   public CustomTexture aIT;
   boolean aJj;
   boolean aJk;
   ItemStack aJl;
   Component aJm;
   Component aJn;
   String aJo;
   final ArrayList aJp = new ArrayList();
   final Map aJq = new HashMap();
   public final HashMap aJr = new HashMap();
   int aIX;
   private int aJs;
   private int aJt;
   @Nullable
   public String aJu;
   @Nullable
   public String aJv;

   public GuiPage(
      String var1,
      @Nullable String var2,
      @Nullable String var3,
      GuiController var4,
      @Nullable ItemStack var5,
      Component var6,
      Component var7,
      @Nullable String var8,
      @Nullable String var9
   ) {
      this.aJc = var1;
      this.aJe = var2;
      this.aJf = var3;
      this.aJg = var4;
      this.aJu = var8;
      this.aJv = var9;
      this.aJm = ((TextComponent)Component.empty().color(AdventureText.defaultColor)).append(var6);
      this.ab(var5);
      TextColor var10 = var4.aIU.aJU.jW("settings.category_view.title_color").color();
      if (var7.color() == AdventureText.defaultColor) {
         this.aJn = var7.color(var10);
      } else {
         this.aJn = var7;
      }

      if (this.aJn.font() != null) {
         Log.warn("[Category] '" + var1 + "' 'title' has a custom font. This is not supported, the font will be ignored.");
      }

      this.method2530(null, -1);
   }

   String getTitle() {
      if (this.aJo != null) {
         return this.aJo;
      } else {
         String var1 = Utils.componentToLegacy(this.aJn);
         if (this.aIT == null) {
            Log.error(
               ChatColor.RED + "GUI error: can't find font_image. Check ia_gui.yml, attribute: " + ChatColor.AQUA + "settings.category_view.font_image.name"
            );
            this.aJo = Utils.richTextToLegacy(var1);
            return this.aJo;
         } else {
            this.aJo = ChatColor.WHITE
               + TextFormatCache.method9634(this.aIT.getFormattedText(), this.aJs)
               + TextFormatCache.method9634(var1, -this.aIT.getWidth() + this.aJt);
            return this.aJo;
         }
      }
   }

   public boolean LW() {
      return this.aJk;
   }

   public void LX() {
      if (!this.aJl.getItemMeta().hasLore()) {
         ArrayList var1 = new ArrayList();
         var1.add(Main.aFi.lj("items-count").replace("{count}", this.aJp.size() + this.aJi.size() + ""));
         ItemStackUtils.method7227(this.aJl, var1);
      }
   }

   public boolean LY() {
      return this.aJj;
   }

   public void bs(boolean var1) {
      this.aJj = var1;
   }

   public ItemStack LZ() {
      return this.aJl;
   }

   public void ab(@Nullable ItemStack var1) {
      if (!this.aJk) {
         if (var1 != null && var1.getType() != Material.AIR && var1.getAmount() != 0) {
            this.aJl = var1;
            this.aJk = true;
         } else {
            this.aJl = new ItemStack(Material.STONE);
            this.aJk = false;
         }

         this.aJl = ItemOperationsService.getRegistryBridge().ai(this.aJl);
         ItemStackUtils.method7223(this.aJl, this.aJm);
      }
   }

   public ArrayList Ma() {
      return this.aJp;
   }

   public void method2525(String var1, RecipeLoader var2) {
      this.aJp.add(var2);
      this.aJq.put(var1, var2);
   }

   public void eG(String var1) {
      method2526(this.aJq, this.aJp, var1);
   }

   static void method2526(Map var0, List var1, String var2) {
      if (var2.indexOf(42) < 0 && var2.indexOf(63) < 0) {
         Object var3 = var0.remove(var2);
         var1.remove(var3);
      } else {
         var0.entrySet().removeIf(var2x -> {
            if (!FilenameUtils.wildcardMatch((String)var2x.getKey(), var2)) {
               return false;
            }

            var1.remove(var2x.getValue());
            return true;
         });
      }
   }

   @Deprecated
   @Nullable
   public String getPermission() {
      if (this.aJf != null) {
         return this.aJf;
      } else {
         return this.aJe == null ? null : "ia.menu.seecategory." + this.aJe;
      }
   }

   public boolean method2527(CommandSender var1) {
      for (GuiPage var2 = this; var2 != null; var2 = var2.aJh) {
         String var3 = var2.getPermission();
         if (var3 != null && !var1.hasPermission(var3)) {
            return false;
         }
      }

      return true;
   }

   private void method2528(Player var1, boolean var2) {
      if (this.aJh == null) {
         this.aJg.method2475(var1, var2);
      } else {
         this.aJh.method2529(var1, var2);
      }
   }

   public void method2529(Player var1, boolean var2) {
      ArrayList var3 = new ArrayList();

      for (GuiPage var5 : this.aJi) {
         if (var5.method2527(var1)) {
            var5.LX();
            var3.add(new GuiButton(var5.LZ(), var3x -> {
               if (var5.method2527(var1)) {
                  var5.method2529(var1, var2);
               }
            }));
         }
      }

      for (RecipeLoader var12 : this.aJp) {
         if (var12 != null && var12.Mc() != null) {
            String var6 = var12.Mc().getPermissionSuffix();
            if (var6 == null || PermissionNodes.method1621(var1, var6)) {
               var3.add(new GuiButton(var12.Mc(), var4 -> {
                  if (!GuiIconRegistry.method2581(var4)) {
                     var12.method2548(null, this, var1, var2);
                  }
               }));
            }
         }
      }

      boolean var11 = this.aJh != null || !var2;
      if (!this.aJg.aIU.method2571(var1, this.aIT)) {
         ScrollState var13 = this.aJr.computeIfAbsent(var1.getUniqueId(), var1x -> new ScrollState(var2));
         this.aIX = Utils.ceilDivide(var3.size(), 45.0F);
         var13.aJw = Math.min(var13.aJw, Math.max(0, this.aIX - 1));
         int var15 = var13.Mb();
         CustomInventoryHolder var7 = new CustomInventoryHolder(54, this.getTitle());

         for (int var8 = var15; var8 < var15 + 45 && var8 < var3.size(); var8++) {
            GuiButton var9 = (GuiButton)var3.get(var8);
            var7.method2667(var9.dp(), var9.Mm());
         }

         if (var11) {
            var7.method2666(49, GuiIconRegistry.aKn, var3x -> this.method2528(var1, var2));
         }

         if (var13.aJw > 0) {
            var7.method2666(52, GuiIconRegistry.aKo, var3x -> {
               this.bU(var1);
               this.method2529(var1, var2);
            });
         }

         if (var13.aJw + 1 < this.aIX) {
            var7.method2666(53, GuiIconRegistry.aKp, var3x -> {
               this.bV(var1);
               this.method2529(var1, var2);
            });
         }

         var7.open(var1);
      } else if (this.aJd != null && this.aJp.isEmpty()) {
         this.aJg.method2478(var1, this, this.aJd);
      } else {
         ScrollState var14 = this.aJr.computeIfAbsent(var1.getUniqueId(), var1x -> new ScrollState(var2));
         boolean var16 = this.aJd != null && PermissionNodes.bJ(var1);
         ContainerPacketListener.method2603(
            var1,
            this.aJg.aIU.method2573(Utils.componentToLegacy(this.aJn), var3.size()),
            var3,
            var16 ? GuiIconRegistry.aKj : null,
            var16 ? var2x -> this.aJg.method2477(var1, this.aJd) : var3x -> this.method2528(var1, var2),
            var14.aJx,
            var1x -> var14.aJx = var1x,
            var0 -> {},
            this.aJg.aIU.bY(var1),
            true,
            this.aJg.aIU.method2574(Utils.componentToLegacy(this.aJn), var3.size(), true),
            var3x -> this.method2528(var1, var2)
         );
      }
   }

   void bU(Player var1) {
      ScrollState var2 = (ScrollState)this.aJr.get(var1.getUniqueId());
      if (var2 != null) {
         int var3 = var2.aJw;
         if (var3 - 1 >= 0) {
            var2.aJw--;
         }
      } else {
         this.aJr.put(var1.getUniqueId(), new ScrollState());
      }
   }

   void bV(Player var1) {
      ScrollState var2 = (ScrollState)this.aJr.get(var1.getUniqueId());
      if (var2 != null) {
         int var3 = var2.aJw;
         if ((var3 + 1) * 45 < this.aJp.size() + this.aJi.size()) {
            var2.aJw++;
         }
      } else {
         this.aJr.put(var1.getUniqueId(), new ScrollState());
      }
   }

   public boolean eH(String var1) {
      return this.aJq.containsKey(var1);
   }

   public void method2530(String var1, int var2) {
      if (var1 == null) {
         this.aIT = aIY;
      } else {
         this.aIT = (CustomTexture)ContentManager.method8084().aE.Yp.get(var1);
      }

      if (var2 == -1) {
         this.aJs = aJa;
      } else {
         this.aJs = var2;
      }
   }

   public void method2531(int var1) {
      if (var1 == -1) {
         this.aJt = aIZ;
      } else {
         this.aJt = var1;
      }
   }
}
