package dev.lone.itemsadder.util;

import java.awt.Color;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;

public class ColoredLogger {
   private static final Map COLOR_CODES = new EnumMap(ChatColor.class);
   private static final ChatColor[] CHAT_COLORS = ChatColor.values();
   private static final String RGB_FORMAT = "\u001b[38;2;%d;%d;%dm";
   private static final Pattern HEX_PATTERN = Pattern.compile("§x(§[A-F0-9]){6}", 2);
   private Logger logger;
   private String prefix;

   public ColoredLogger() {
      this("");
   }

   public ColoredLogger(String var1) {
      this.prefix = colorize(var1);
      if (Bukkit.getServer() == null) {
         this.logger = Logger.getGlobal();
      } else {
         this.logger = Bukkit.getLogger();
      }
   }

   public ColoredLogger(String var1, Logger var2) {
      this.prefix = colorize(var1);
      this.logger = var2;
   }

   public void setPrefix(String var1) {
      this.prefix = colorize(var1);
   }

   public void setLogger(Logger var1) {
      this.logger = var1;
   }

   public void logColored(Level var1, String var2) {
      this.logger.log(var1, this.prefix + colorize(var2));
   }

   public void logColored(Level var1, String var2, Throwable var3) {
      this.logger.log(var1, this.prefix + colorize(var2), var3);
   }

   private static String colorize(String var0) {
      if (var0.isEmpty()) {
         return var0;
      }

      var0 = ChatColor.translateAlternateColorCodes('&', var0);
      String var1 = convertHexColors(var0);

      for (ChatColor var5 : CHAT_COLORS) {
         var1 = var1.replaceAll("(?i)" + var5.toString(), COLOR_CODES.getOrDefault(var5, ""));
      }

      return var1 + Ansi.ansi().reset().toString();
   }

   private static String convertHexColors(String var0) {
      Matcher var1 = HEX_PATTERN.matcher(var0);
      StringBuffer var2 = new StringBuffer();

      while (var1.find()) {
         String var3 = var1.group().replace("§", "").replace('x', '#');
         Color var4 = Color.decode(var3);
         int var5 = var4.getRed();
         int var6 = var4.getBlue();
         int var7 = var4.getGreen();
         String var8 = String.format("\u001b[38;2;%d;%d;%dm", var5, var7, var6);
         var1.appendReplacement(var2, var8);
      }

      var1.appendTail(var2);
      return var2.toString();
   }

   static {
      COLOR_CODES.put(ChatColor.BLACK, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.BLACK).boldOff().toString());
      COLOR_CODES.put(ChatColor.DARK_BLUE, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.BLUE).boldOff().toString());
      COLOR_CODES.put(ChatColor.DARK_GREEN, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.GREEN).boldOff().toString());
      COLOR_CODES.put(ChatColor.DARK_AQUA, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.CYAN).boldOff().toString());
      COLOR_CODES.put(ChatColor.DARK_RED, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.RED).boldOff().toString());
      COLOR_CODES.put(ChatColor.DARK_PURPLE, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.MAGENTA).boldOff().toString());
      COLOR_CODES.put(ChatColor.GOLD, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.YELLOW).boldOff().toString());
      COLOR_CODES.put(ChatColor.GRAY, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.WHITE).boldOff().toString());
      COLOR_CODES.put(ChatColor.DARK_GRAY, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.BLACK).bold().toString());
      COLOR_CODES.put(ChatColor.BLUE, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.BLUE).bold().toString());
      COLOR_CODES.put(ChatColor.GREEN, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.GREEN).bold().toString());
      COLOR_CODES.put(ChatColor.AQUA, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.CYAN).bold().toString());
      COLOR_CODES.put(ChatColor.RED, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.RED).bold().toString());
      COLOR_CODES.put(ChatColor.LIGHT_PURPLE, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.MAGENTA).bold().toString());
      COLOR_CODES.put(ChatColor.YELLOW, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.YELLOW).bold().toString());
      COLOR_CODES.put(ChatColor.WHITE, Ansi.ansi().a(Attribute.RESET).fg(org.fusesource.jansi.Ansi.Color.WHITE).bold().toString());
      COLOR_CODES.put(ChatColor.MAGIC, Ansi.ansi().a(Attribute.BLINK_SLOW).toString());
      COLOR_CODES.put(ChatColor.BOLD, Ansi.ansi().a(Attribute.UNDERLINE_DOUBLE).toString());
      COLOR_CODES.put(ChatColor.STRIKETHROUGH, Ansi.ansi().a(Attribute.STRIKETHROUGH_ON).toString());
      COLOR_CODES.put(ChatColor.UNDERLINE, Ansi.ansi().a(Attribute.UNDERLINE).toString());
      COLOR_CODES.put(ChatColor.ITALIC, Ansi.ansi().a(Attribute.ITALIC).toString());
      COLOR_CODES.put(ChatColor.RESET, Ansi.ansi().a(Attribute.RESET).toString());
   }
}
