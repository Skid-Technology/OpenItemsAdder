package dev.lone.itemsadder.Core.OtherPlugins.mmoitems;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.Log;
import net.Indyuce.mmoitems.MMOItems;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class MmoItemsHook {
   public static void injectMmoItem(ItemStack item) {
      if (Main.aEU) {
         if (CustomItemDefinition.isCustomItem(item)) {
            String var1 = MMOItems.getTypeName(item);
            if (var1 != null) {
               String var2 = MMOItems.getID(item);
               if (var2 != null) {
                  var1 = var1.toUpperCase();
                  var2 = var2.toUpperCase();
                  Component var3 = ItemStackUtils.as(item);

                  try {
                     int var4 = ItemStackUtils.method7258(item);
                     Color var12 = null;
                     if (item.getItemMeta() instanceof LeatherArmorMeta) {
                        LeatherArmorMeta var6 = (LeatherArmorMeta)item.getItemMeta();
                        var12 = var6.getColor();
                     }

                     ItemStack var13 = MMOItems.plugin.getItem(MMOItems.plugin.getTypes().get(var1), var2);
                     ItemStackUtils.method7221(item, var13);
                     ItemStackUtils.method7259(item, var4);
                     if (var12 != null) {
                        LeatherArmorMeta var7 = (LeatherArmorMeta)item.getItemMeta();
                        var7.setColor(var12);
                        item.setItemMeta(var7);
                     }
                  } catch (Exception var9) {
                     try {
                        CustomItemDefinition var5 = ContentManager.method8084().method8149(item);
                        Log.error(
                           "Error loading MMOItems integration for item "
                              + var5.getNamespacedID()
                              + ". Please check if the MMOItem of type '"
                              + var1
                              + "' and id '"
                              + var2
                              + "' exists in MMOItems plugin. File: "
                              + var5.DJ()
                        );
                     } catch (Exception var8) {
                        var8.printStackTrace();
                     }
                  }

                  ItemStackUtils.method7223(item, var3);
                  CustomItemDefinition.method8812(item);
               }
            }
         }
      }
   }

   public static void injectMmoItem(CustomItemDefinition item, String mmoItemType, String mmoItemId, boolean useMmoitemDisplayName) {
      if (Main.aEU && mmoItemType != null && mmoItemId != null) {
         String var4 = item.DE();
         mmoItemType = mmoItemType.toUpperCase();
         mmoItemId = mmoItemId.toUpperCase();

         try {
            ItemStack var5 = MMOItems.plugin.getItem(MMOItems.plugin.getTypes().get(mmoItemType), mmoItemId);
            item.method8747(var5);
            if (!useMmoitemDisplayName) {
               item.cI(var4);
            }
         } catch (NullPointerException var6) {
            Log.error(
               "Error loading MMOItems integration for item "
                  + item.getNamespacedID()
                  + ". Please check if the MMOItem of type '"
                  + mmoItemType
                  + "' and id '"
                  + mmoItemId
                  + "' exists in MMOItems plugin. File: "
                  + item.DJ()
            );
         } catch (Throwable var7) {
            Log.error(
               "Error loading MMOItems integration for item "
                  + item.getNamespacedID()
                  + ". Please check if MMOItem is up to date and running. File: "
                  + item.DJ()
            );
         }
      }
   }
}
