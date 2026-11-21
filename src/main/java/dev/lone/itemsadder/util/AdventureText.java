package dev.lone.itemsadder.util;

import beer.devs.fastnbt.nms.Version;
import com.google.gson.JsonElement;
import dev.lone.itemsadder.text.NmsChatService;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.platform.bukkit.MinecraftComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.Style.Builder;
import net.kyori.adventure.text.format.TextDecoration.State;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.gson.legacyimpl.NBTLegacyHoverEventSerializer;
import net.kyori.adventure.text.serializer.json.JSONOptions;
import net.kyori.adventure.text.serializer.json.JSONOptions.HoverEventValueMode;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.option.OptionState;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdventureText {
   public static GsonComponentSerializer gsonSerializer;
   public static final MiniMessage miniMessage = MiniMessage.miniMessage();
   public static TextColor defaultColor = TextColor.color(255, 255, 255);
   private static final Style noItalicStyle = Style.style(var0 -> var0.decoration(TextDecoration.ITALIC, false));
   public static Style defaultStyle = Style.style(var0 -> {
      var0.color(defaultColor);
      var0.decoration(TextDecoration.ITALIC, false);
   });
   public static final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.builder()
      .hexColors()
      .useUnusualXRepeatedCharacterHexFormat()
      .build();
   public static final String EMPTY_JSON_TEXT = "{\"text\":\"\"}";
   public static final DecimalFormat shortNumberFormat = new DecimalFormat("#.##");
   public static final DecimalFormat preciseNumberFormat = new DecimalFormat("0.0#");
   private static BukkitAudiences audiences;

   public static String legacyToJson(@NotNull String var0) {
      return (String)gsonSerializer.serialize(fromLegacy(var0));
   }

   public static String jsonToLegacy(String var0) {
      return LegacyComponentSerializer.legacy('§').serialize(parseJson(var0));
   }

   public static String serializePlain(@NotNull Component var0) {
      return (String)gsonSerializer.serialize(var0);
   }

   @NotNull
   public static JsonElement toJsonElement(@NotNull Component var0) {
      return gsonSerializer.serializeToTree(var0);
   }

   @NotNull
   public static Component parseJson(String var0) {
      return !var0.startsWith("{") ? Component.text(var0).style(defaultStyle) : gsonSerializer.deserialize(var0);
   }

   public static Object toNative(Component var0) {
      if (var0 == null) {
         return null;
      } else {
         return ServerCompat.isPaper() ? NmsChatService.Nq().method2960(var0) : MinecraftComponentSerializer.get().serialize(var0);
      }
   }

   @Nullable
   public static Component fromNative(Object var0) {
      if (var0 == null) {
         return null;
      } else {
         return ServerCompat.isPaper() ? NmsChatService.Nq().method2959(var0) : MinecraftComponentSerializer.get().deserialize(var0);
      }
   }

   public static String nativeToString(Object nmsComponent) {
      return NmsChatService.Nq().method2964(nmsComponent);
   }

   public static BaseComponent[] toBungee(Component var0) {
      return Version.isAtLeast(Version.v26_2) ? AdventureToBungeeConverter.convertArray(var0) : BungeeComponentSerializer.legacy().serialize(var0);
   }

   public static Component fromLegacy(String var0) {
      if (Version.isAtLeast(Version.v26_2)) {
         return LegacyColorParser.parseLegacy(var0);
      }

      TextComponent var1 = BukkitComponentSerializer.legacy().deserialize(var0);
      return var1.decorationIfAbsent(TextDecoration.ITALIC, State.FALSE);
   }

   public static Component parseMiniMessage(String var0, boolean var1) {
      if (Version.isAtLeast(Version.v26_2)) {
         return var1 ? LegacyColorParser.parseLegacy(var0).style(noItalicStyle) : LegacyColorParser.parseLegacy(var0);
      } else {
         return (Component)(!var1
            ? BukkitComponentSerializer.legacy().deserialize(var0)
            : BukkitComponentSerializer.legacy().deserialize(var0).style(noItalicStyle));
      }
   }

   public static boolean looksLikeJson(String var0) {
      return var0.trim().startsWith("{");
   }

   public static String convertAmpersandCodes(String var0) {
      return var0.replaceAll("&(?=\\S)", "§");
   }

   public static TranslatableComponent translatable(String var0) {
      TranslatableComponent var1 = Component.translatable(var0);
      return (TranslatableComponent)var1.decorationIfAbsent(TextDecoration.ITALIC, State.FALSE);
   }

   public static @NotNull Builder resetDecorationIfMatches(Style var0, Builder var1, TextDecoration var2, State var3) {
      return var0.decoration(var2) == var3 ? var1.decoration(var2, State.NOT_SET) : var1.decoration(var2, var0.decoration(var2));
   }

   public static Component disableCommonDecorations(Component var0) {
      var0 = var0.compact();
      Style var1 = var0.style();
      Builder var2 = var1.toBuilder();
      var2 = resetDecorationIfMatches(var1, var2, TextDecoration.BOLD, State.FALSE);
      var2 = resetDecorationIfMatches(var1, var2, TextDecoration.UNDERLINED, State.FALSE);
      var2 = resetDecorationIfMatches(var1, var2, TextDecoration.STRIKETHROUGH, State.FALSE);
      var2 = resetDecorationIfMatches(var1, var2, TextDecoration.OBFUSCATED, State.FALSE);
      return var0.style(var2);
   }

   public static String toPlainText(Component var0) {
      return LegacyComponentSerializer.legacy('§').serialize(var0);
   }

   public static boolean hasColor(Component var0) {
      return var0.color() != null;
   }

   public static String joinPlain(String... var0) {
      Object var1 = Component.text("");

      for (String var5 : var0) {
         var1 = var1.append(Component.text(var5));
      }

      return serializePlain((Component)var1);
   }

   public static String plainTextOf(String var0) {
      return toPlainText(parseRichText(var0));
   }

   public static Component parseRichText(String var0) {
      if (var0.contains("%#")) {
         var0 = var0.replaceAll("%#([a-fA-F0-9]{6})%", "&#$1");
      }

      if (var0.contains("&#") || var0.contains("§#")) {
         var0 = convertAmpersandCodes(var0);
      }

      Component var1;
      try {
         var0 = convertAmpersandCodes(var0);
         var1 = miniMessage.deserialize(var0);
      } catch (ParsingException var3) {
         var1 = fromLegacy(var0);
      }

      return var1.colorIfAbsent(defaultColor).decorationIfAbsent(TextDecoration.ITALIC, State.FALSE);
   }

   public static String formatNumber(double var0, boolean var2) {
      return var2 ? preciseNumberFormat.format(var0) : shortNumberFormat.format(var0);
   }

   public static void initAudiences(Plugin var0) {
      audiences = BukkitAudiences.create(var0);
   }

   public static BukkitAudiences getAudiences() {
      if (audiences == null) {
         throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
      } else {
         return audiences;
      }
   }

   public static void closeAudiences() {
      if (audiences != null) {
         audiences.close();
         audiences = null;
      }
   }

   static {
      shortNumberFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
      preciseNumberFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
      if (Version.isAtLeast(Version.v26_2)) {
         gsonSerializer = GsonComponentSerializer.builder()
            .options(
               OptionState.optionState()
                  .value(JSONOptions.EMIT_RGB, Boolean.TRUE)
                  .value(JSONOptions.EMIT_COMPACT_TEXT_COMPONENT, Boolean.TRUE)
                  .value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, Boolean.TRUE)
                  .value(JSONOptions.VALIDATE_STRICT_EVENTS, Boolean.TRUE)
                  .build()
            )
            .build();
      } else {
         gsonSerializer = GsonComponentSerializer.builder()
            .legacyHoverEventSerializer(NBTLegacyHoverEventSerializer.get())
            .options(
               OptionState.optionState()
                  .value(JSONOptions.EMIT_RGB, Boolean.TRUE)
                  .value(JSONOptions.EMIT_HOVER_EVENT_TYPE, HoverEventValueMode.MODERN_ONLY)
                  .value(JSONOptions.EMIT_COMPACT_TEXT_COMPONENT, Boolean.TRUE)
                  .value(JSONOptions.EMIT_HOVER_SHOW_ENTITY_ID_AS_INT_ARRAY, Boolean.TRUE)
                  .value(JSONOptions.VALIDATE_STRICT_EVENTS, Boolean.TRUE)
                  .build()
            )
            .build();
      }
   }
}
