package dev.lone.itemsadder.item;

import dev.lone.itemsadder.util.AdventureText;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;

public class BookMetaUtils {
   public static void setBookAuthor(ItemStack var0, String var1) {
      var0.setType(Material.WRITTEN_BOOK);
      BookMeta var2 = (BookMeta)var0.getItemMeta();
      var2.setAuthor(var1);
      var0.setItemMeta(var2);
   }

   public static void setBookGeneration(ItemStack var0, Generation var1) {
      var0.setType(Material.WRITTEN_BOOK);
      BookMeta var2 = (BookMeta)var0.getItemMeta();
      var2.setGeneration(var1);
      var0.setItemMeta(var2);
   }

   public static void setBookTitle(ItemStack var0, String var1) {
      var0.setType(Material.WRITTEN_BOOK);
      BookMeta var2 = (BookMeta)var0.getItemMeta();
      var2.setTitle(var1);
      var0.setItemMeta(var2);
   }

   public static void applyToBookTitle(BookMeta var0, UnaryOperator var1) {
      String var2 = var0.getTitle();
      if (var2 != null) {
         TextComponent var3 = AdventureText.legacySerializer.deserialize(var2);
         Component var4 = (Component)var1.apply(var3);
         String var5 = PlainTextComponentSerializer.plainText().serialize(var4);
         if (!var5.equals(PlainTextComponentSerializer.plainText().serialize(var3))) {
            var0.setTitle(AdventureText.toPlainText(var4));
         }
      }
   }

   public static void setBookPages(ItemStack var0, List var1) {
      var0.setType(Material.WRITTEN_BOOK);
      BookMeta var2 = (BookMeta)var0.getItemMeta();
      var2.setPages(var1.stream().map(var0x -> ChatColor.translateAlternateColorCodes('&', var0x)).collect(Collectors.toList()));
      var0.setItemMeta(var2);
   }
}
