package dev.lone.itemsadder.fontimage;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.resourcepack.CustomTexture;
import dev.lone.itemsadder.text.NmsChatService;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FontImageChatRenderer implements IaListener {
   static final int aGX = 13;
   static final int aGY = 32767;
   private static final float aGZ = 6.0F;
   private final Key aHa = Key.key(Settings.get().iaImageBookPlaceholderFont);
   private final int aHb = Settings.get().iaImageBookMaxLineLength * 13;

   public void bM(Player var1) {
      SchedulerUtils.runSync(() -> NmsChatService.Nq().method2965(var1, Book.book(Component.text("ItemsAdder"), Component.text("ItemsAdder"), this.bN(var1))));
   }

   private List bN(Player var1) {
      ArrayList var2 = new ArrayList();
      int var3 = 0;
      int var4 = 0;
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();

      for (Entry var9 : ContentManager.method8084().aE.Yp.entrySet()) {
         if (((CustomTexture)var9.getValue()).xe()) {
            if (((CustomTexture)var9.getValue()).hasPermission(var1)) {
               var5.add((CustomTexture)var9.getValue());
            } else {
               var6.add((CustomTexture)var9.getValue());
            }
         }
      }

      var7.addAll(var5);
      var7.addAll(var6);
      Object var17 = Component.empty();

      for (CustomTexture var10 : var7) {
         String var11;
         if (var10.hasPermission(var1)) {
            var11 = " " + var10.getLegacyId();
         } else {
            var11 = " " + Main.aFi.lj("no-permission-short");
         }

         int var12 = (int)Math.ceil(var10.getWidth() / 6.0F);
         int var13 = Math.max(0, Settings.get().iaImageBookMaxLineLength - var12);
         var11 = ChatColor.BLACK + method1391(var11, var13);
         int var14 = var12 + eA(var11);
         Component var15 = Component.text(ChatColor.WHITE + var10.getFormattedText())
            .append(this.method1392(Component.text(var11 + "\n"), var10).font(this.aHa));
         Component var16 = var17.append(var15).compact();
         if (var3 <= 0 || var4 + var14 <= this.aHb && var3 < 13 && (!Version.isAtLeast(Version.v26_2) || !method1390(var16))) {
            var17 = var16;
            var3++;
            var4 += var14;
         } else {
            var2.add(var17);
            var17 = var15.compact();
            var3 = 1;
            var4 = var14;
         }
      }

      if (var3 > 0 || var2.isEmpty()) {
         var2.add(var17);
      }

      return var2;
   }

   static boolean method1390(Component var0) {
      return ((String)GsonComponentSerializer.gson().serialize(var0)).length() > 32767;
   }

   static String method1391(String var0, int var1) {
      String var2 = ChatColor.stripColor(var0);
      if (var2.length() > var1) {
         if (var1 <= 3) {
            var2 = ".".repeat(var1);
         } else {
            var2 = var2.substring(0, var1 - 3) + "...";
         }
      }

      return ChatColor.getLastColors(var0) + var2;
   }

   private static int eA(String var0) {
      return ChatColor.stripColor(var0).length();
   }

   private Component method1392(Component var1, CustomTexture var2) {
      return Settings.get().fontImagesChatHoverSuggestion
         ? var1.hoverEvent(
            HoverEvent.showText(
               AdventureText.parseRichText(ChatPacketListener.aGK.replace("{id}", var2.dl).replace("{placeholder}", var2.getLegacyId()))
                  .replaceText(
                     (TextReplacementConfig)TextReplacementConfig.builder()
                        .match("\\{font_image\\}")
                        .replacement(var1x -> var1x.content(var2.getDisplayText(false)))
                        .build()
                  )
            )
         )
         : var1;
   }
}
