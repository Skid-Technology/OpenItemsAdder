package dev.lone.itemsadder.command;

import dev.lone.itemsadder.core.NamespacedElement;
import dev.lone.itemsadder.recipe.AbstractCustomRecipe;
import dev.lone.itemsadder.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.Nullable;

public class PermissionNodes {
   public static String aFs = "%%__NONCE__%%";
   public static final String aFt = "ia.user.ia";
   public static final String aFu = "ia.user.ia.categories";
   public static final String aFv = "ia.user.ia.categories.others";
   public static String aFw = "ia.user.iacraft";
   public static final String aFx = "ia.user.iarecipe";
   public static final String aFy = "ia.user.iarecipe.others";
   private static final String aFz = "ia.user.recipe.";
   private static final String aFA = "ia.user.ia.seeitem.";
   private static final String aFB = "ia.user.ia.seeitem.*";
   private static final String aFC = "ia.user.block.break.";
   private static final String aFD = "ia.user.block.place.";
   private static final String aFE = "ia.user.furniture.break.";
   private static final String aFF = "ia.user.furniture.place.";
   private static final String aFG = "ia.user.complex_furniture.break.";
   private static final String aFH = "ia.user.complex_furniture.place.";
   public static final String aFI = "ia.user.image.chat";
   public static final String aFJ = "ia.user.image.command";
   public static final String aFK = "ia.user.image.sign";
   public static final String aFL = "ia.user.image.book";
   public static final String aFM = "ia.user.image.anvil";
   public static final String aFN = "ia.user.image.gui";
   public static final String aFO = "ia.user.image.hints";
   private static final String aFP = "ia.user.image.use.";
   public static final String aFQ = "ia.user.iatexture";
   public static final String aFR = "ia.user.iatexture.all";
   public static final String aFS = "ia.user.ia.search";
   public static final String aFT = "ia.user.text_effect.use.";
   public static final String aFU = "ia.user.text_effect.chat";
   public static final String aFV = "ia.user.text_effect.command";
   public static final String aFW = "ia.user.text_effect.sign";
   public static final String aFX = "ia.user.text_effect.book";
   public static final String aFY = "ia.user.text_effect.anvil";
   public static final String aFZ = "ia.admin.iaget";
   public static final String aGa = "ia.admin.iagive";
   public static final String aGb = "ia.admin.iaremove";
   public static final String aGc = "ia.admin.iatag";
   public static final String aGd = "ia.admin.iatag.show-in-console";
   public static final String aGe = "ia.admin.iarepair";
   public static final String aGf = "ia.admin.iadurability";
   public static final String aGg = "ia.admin.iablock";
   public static final String aGh = "ia.admin.ialiquid";
   public static final String aGi = "ia.admin.iareload";
   public static final String aGj = "ia.admin.iazip";
   public static String aGk = "ia.admin.playerstat";
   public static final String aGl = "ia.admin.iaplayerstat.read";
   public static final String aGm = "ia.admin.iaplayerstat.write";
   public static final String aGn = "ia.admin.iainfo";
   public static final String aGo = "ia.admin.iaspawntree";
   public static final String aGp = "ia.admin.iacolor.others";
   public static String aGq = "ia.admin.edit";
   public static final String aGr = "ia.resourcepack.bypasskick";
   public static final String aGs = "ia.admin.bypassblockplaceloot";
   public static final String aGt = "ia.user.iaemote.others";
   private static final String aGu = "ia.user.iaemote.use.";
   private static final String aGv = "ia.user.hud.show.";
   private static final String aGw = "ia.user.hud.bypass.triggers.";
   private static final String aGx = "ia.user.hud.bypass.commands.";
   private static final String aGy = "ia.user.hud.bypass.api.";
   public static final String aGz = "ia.admin.crops.others";

   public static boolean method1616(HumanEntity var0, NamespacedElement var1) {
      return var0.hasPermission("ia.user.hud.show.*") || var0.hasPermission("ia.user.hud.show." + var1.getNamespacedID());
   }

   public static boolean method1617(HumanEntity var0, NamespacedElement var1, RuleSource var2) {
      return method1618(var0, var1, var2, false);
   }

   public static boolean method1618(HumanEntity var0, NamespacedElement var1, RuleSource var2, boolean var3) {
      if (!var3) {
         return switch (var2) {
            case TRIGGERS -> var0.hasPermission("ia.user.hud.bypass.triggers." + var1.getNamespacedID());
            case COMMANDS -> var0.hasPermission("ia.user.hud.bypass.commands." + var1.getNamespacedID());
            case API -> var0.hasPermission("ia.user.hud.bypass.api." + var1.getNamespacedID());
         };
      } else {
         return switch (var2) {
            case TRIGGERS -> var0.hasPermission("ia.user.hud.bypass.triggers.*") || var0.hasPermission("ia.user.hud.bypass.triggers." + var1.getNamespacedID());
            case COMMANDS -> var0.hasPermission("ia.user.hud.bypass.commands.*") || var0.hasPermission("ia.user.hud.bypass.commands." + var1.getNamespacedID());
            case API -> var0.hasPermission("ia.user.hud.bypass.api.*") || var0.hasPermission("ia.user.hud.bypass.api." + var1.getNamespacedID());
         };
      }
   }

   public static boolean method1619(HumanEntity var0, RuleSource var1) {
      return switch (var1) {
         case TRIGGERS -> var0.hasPermission("ia.user.hud.bypass.triggers.*");
         case COMMANDS -> var0.hasPermission("ia.user.hud.bypass.commands.*");
         case API -> var0.hasPermission("ia.user.hud.bypass.api.*");
      };
   }

   public static void er(String var0) {
      String var1 = "Allow the player to see the hud. True by default.";
      String var2 = "ia.user.hud.show." + var0;
      if (Bukkit.getPluginManager().getPermission(var2) == null) {
         Bukkit.getPluginManager().addPermission(new Permission(var2, var1, PermissionDefault.TRUE));
      }
   }

   public static boolean method1620(HumanEntity var0, AbstractCustomRecipe var1) {
      return var0.hasPermission("ia.user.recipe." + var1.getPermissionSuffix()) || var0.hasPermission("ia.user.craft." + var1.getPermissionSuffix());
   }

   public static boolean method1621(HumanEntity var0, String var1) {
      return var0.hasPermission("ia.user.ia.seeitem." + var1) || var0.hasPermission("ia.user.ia." + var1);
   }

   public static boolean bJ(Player var0) {
      return var0.hasPermission("ia.user.ia.search") || var0.hasPermission("ia.user.iasearchgui");
   }

   public static boolean method1622(HumanEntity var0, @Nullable String var1) {
      return var1 == null ? true : method1633(var0, "ia.user.block.break." + var1);
   }

   public static boolean method1623(HumanEntity var0, @Nullable String var1) {
      return var1 == null ? true : method1633(var0, "ia.user.block.place." + var1);
   }

   public static boolean method1624(HumanEntity var0, @Nullable String var1) {
      return var1 == null ? true : method1633(var0, "ia.user.furniture.break." + var1);
   }

   public static boolean method1625(HumanEntity var0, @Nullable String var1) {
      return var1 == null ? true : method1633(var0, "ia.user.furniture.place." + var1);
   }

   public static boolean method1626(HumanEntity var0, @Nullable String var1) {
      return var1 == null ? true : method1633(var0, "ia.user.complex_furniture.break." + var1);
   }

   public static boolean method1627(HumanEntity var0, @Nullable String var1) {
      return var1 == null ? true : method1633(var0, "ia.user.complex_furniture.place." + var1);
   }

   public static boolean bK(Player var0) {
      return var0.hasPermission("ia.user.ia.seeitem.*") || var0.hasPermission("ia.user.ia.*");
   }

   public static String es(String var0) {
      return "ia.user.image.use." + var0;
   }

   public static boolean method1628(Permissible var0, @Nullable String var1) {
      return var1 == null || var0.isOp() || var0.hasPermission(es("*")) || var0.hasPermission(var1);
   }

   public static String et(String var0) {
      return "ia.user.iaemote.use." + var0.replace(":", ".");
   }

   public static boolean method1629(Permissible var0, String var1, boolean var2) {
      if (var1 == null) {
         return var2;
      } else {
         return var0 instanceof Player ? var0.hasPermission(var1) : true;
      }
   }

   public static boolean method1630(Permissible var0, String var1) {
      return method1629(var0, var1, true);
   }

   public static boolean method1631(Permissible var0, String var1, boolean var2) {
      if (var1 == null) {
         return true;
      }

      if (var0 instanceof Player var3) {
         if (var0.hasPermission(var1)) {
            return true;
         }

         if (var2) {
            Log.sendLocalizedWith(var3, "no-permission-detail", var1x -> var1x.replace("{permission}", var1));
         } else {
            Log.sendLocalized(var3, "no-permission");
         }

         return false;
      } else {
         return var0.hasPermission(var1);
      }
   }

   public static boolean method1632(Permissible var0, String var1) {
      return method1631(var0, var1, false);
   }

   public static boolean method1633(Permissible var0, String var1) {
      if (var0 instanceof Player var2) {
         if (var0.hasPermission(var1)) {
            return var0.hasPermission(var1);
         }

         Log.sendLocalized(var2, "no-permission-short");
         return false;
      } else {
         return var0.hasPermission(var1);
      }
   }
}
