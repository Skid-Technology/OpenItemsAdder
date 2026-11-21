package dev.lone.itemsadder.util;

import java.awt.Color;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Utils {
   public static Random random = new Random();
   private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("%#([A-Fa-f0-9]){6}%");
   private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");
   static LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder().hexColors().character('§').build();

   public static String formatNumberShort(double var0) {
      return formatNumber(var0, false);
   }

   public static String formatNumber(double var0, boolean var2) {
      return AdventureText.formatNumber(var0, var2);
   }

   public static float randomFloat(float var0, float var1) {
      return random.nextFloat() * (var1 - var0) + var0;
   }

   public static double randomDouble() {
      return random.nextDouble();
   }

   public static int randomInt(int var0, int var1) {
      return var1 == var0 ? var1 : random.nextInt(var1 - var0 + 1) + var0;
   }

   public static int randomIntWith(Random var0, int var1, int var2) {
      return var2 == var1 ? var2 : var0.nextInt(var2 - var1 + 1) + var1;
   }

   public static boolean rollPercent(int var0) {
      int var1 = randomInt(1, 100);
      return var1 <= var0;
   }

   public static boolean hasPermissionOrOp(Player var0, String var1) {
      return var0.hasPermission(var1) ? true : var0.isOp();
   }

   public static String sectionToAmpersand(String var0) {
      return var0.replace("§", "&");
   }

   public static String toSectionCodes(String var0) {
      for (Matcher var1 = HEX_COLOR_PATTERN.matcher(var0); var1.find(); var1 = HEX_COLOR_PATTERN.matcher(var0)) {
         ChatColor var2 = ChatColor.of(var1.group().substring(1, var1.group().length() - 1));
         Object var3 = var0.substring(0, var1.start());
         String var4 = var0.substring(var1.end());
         var0 = var3 + var2 + var4;
      }

      return var0.replace("&", "§");
   }

   public static boolean hasLegacyCodes(String var0) {
      return var0.contains("§([0-fk-or])") || var0.contains("&([0-fk-or])");
   }

   public static boolean isNumeric(String var0) {
      return var0 == null ? false : NUMBER_PATTERN.matcher(var0).matches();
   }

   public static boolean isNumericAt(String[] var0, int var1) {
      return var1 > var0.length - 1 ? false : isNumeric(var0[var1]);
   }

   private static Color parseHex(String var0) {
      try {
         return new Color(Integer.valueOf(var0.substring(1, 3), 16), Integer.valueOf(var0.substring(3, 5), 16), Integer.valueOf(var0.substring(5, 7), 16));
      } catch (Throwable var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static org.bukkit.Color parseBukkitColor(String var0) {
      if (var0 == null) {
         return null;
      }

      if (var0.endsWith(".0")) {
         var0 = var0.replace(".0", "");
      }

      var0 = var0.replace(".", "");

      while (var0.length() < 7) {
         var0 = var0 + "0";
      }

      if (!var0.startsWith("#")) {
         var0 = "#" + var0;
      }

      Color var1 = parseHex(var0);
      return org.bukkit.Color.fromRGB(var1.getRed(), var1.getGreen(), var1.getBlue());
   }

   public static String intToHex(int var0) {
      Color var1 = new Color(var0);
      return rgbToHex(var1.getRed(), var1.getGreen(), var1.getBlue());
   }

   public static String bukkitColorToHex(org.bukkit.Color var0, boolean var1) {
      Color var2 = new Color(var0.asRGB());
      return rgbToHexWith(var2.getRed(), var2.getGreen(), var2.getBlue(), var1);
   }

   public static String rgbToHex(int var0, int var1, int var2) {
      return rgbToHexWith(var0, var1, var2, true);
   }

   public static String rgbToHexWith(int var0, int var1, int var2, boolean var3) {
      return var3 ? String.format("#%02x%02x%02x", var0, var1, var2) : String.format("%02x%02x%02x", var0, var1, var2);
   }

   public static org.bukkit.Color intToBukkitColor(int var0) {
      Color var1 = new Color(var0);
      return org.bukkit.Color.fromRGB(var1.getRed(), var1.getGreen(), var1.getBlue());
   }

   public static int ceilDivide(float var0, float var1) {
      return (int)Math.ceil(var0 / var1);
   }

   public static ItemStack withDisplayName(ItemStack var0, String var1) {
      ItemMeta var2 = var0.getItemMeta();
      var2.setDisplayName(var1);
      var0.setItemMeta(var2);
      return var0;
   }

   public static int parseIntOr(String var0, int var1) {
      try {
         return Integer.parseInt(var0);
      } catch (Throwable var3) {
         return var1;
      }
   }

   public static float parseFloatOr(String var0, float var1) {
      try {
         return Float.parseFloat(var0);
      } catch (Throwable var3) {
         return var1;
      }
   }

   public static boolean parseBoolOr(@NotNull String var0, boolean var1) {
      if (var0.equalsIgnoreCase("true") || var0.equalsIgnoreCase("yes") || var0.equalsIgnoreCase("1")) {
         return true;
      } else {
         return !var0.equalsIgnoreCase("false") && !var0.equalsIgnoreCase("no") && !var0.equalsIgnoreCase("0") ? var1 : false;
      }
   }

   public static org.bukkit.Color lookupBukkitColor(String var0) {
      try {
         return (org.bukkit.Color)FieldUtils.readStaticField(org.bukkit.Color.class, var0);
      } catch (Exception var4) {
         try {
            if (!isNumeric(var0)) {
               if (var0 == null) {
                  return null;
               }

               if (var0.endsWith(".0")) {
                  var0 = var0.replace(".0", "");
               }

               var0 = var0.replace(".", "");

               while (var0.length() < 7) {
                  var0 = var0 + "0";
               }

               if (!var0.startsWith("#")) {
                  var0 = "#" + var0;
               }

               Color var2 = parseHex(var0);
               return org.bukkit.Color.fromRGB(var2.getRed(), var2.getGreen(), var2.getBlue());
            }

            org.bukkit.Color.fromBGR(Integer.parseInt(var0));
         } catch (Throwable var3) {
         }

         return null;
      }
   }

   public static int colorToRgbInt(String var0) {
      if (var0 == null) {
         return -1;
      }

      var0 = var0.toUpperCase();

      try {
         return ((org.bukkit.Color)FieldUtils.readStaticField(org.bukkit.Color.class, var0)).asRGB();
      } catch (Exception var4) {
         try {
            if (isNumeric(var0)) {
               return Integer.parseInt(var0);
            }

            if (var0 == null) {
               return -1;
            }

            if (var0.endsWith(".0")) {
               var0 = var0.replace(".0", "");
            }

            var0 = var0.replace(".", "");

            while (var0.length() < 7) {
               var0 = var0 + "0";
            }

            if (!var0.startsWith("#")) {
               var0 = "#" + var0;
            }

            Color var2 = parseHex(var0);
            return org.bukkit.Color.fromRGB(var2.getRed(), var2.getGreen(), var2.getBlue()).asRGB();
         } catch (Throwable var3) {
            return -1;
         }
      }
   }

   public static boolean inRange(int var0, int var1, int var2, boolean var3) {
      return var3 ? var0 >= var1 && var0 <= var2 : var0 > var1 && var0 < var2;
   }

   public static boolean inRangeInclusive(int var0, int var1, int var2) {
      return inRange(var0, var1, var2, true);
   }

   public static String timestampNow() {
      return formatTimestamp(new Date());
   }

   public static String formatTimestamp(Date var0) {
      return new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(var0);
   }

   public static void printCallerChain() {
      try {
         StackTraceElement[] var0 = new Throwable().fillInStackTrace().getStackTrace();
         StringBuilder var1 = new StringBuilder();

         for (int var2 = 1; var2 < var0.length; var2++) {
            var1.append(var0[var2].getMethodName()).append(":").append(var0[var2].getLineNumber()).append("->");
         }

         System.out.println(var1);
      } catch (Throwable var3) {
      }
   }

   public static String callerChain(int var0, int var1) {
      try {
         StackTraceElement[] var2 = new Throwable().fillInStackTrace().getStackTrace();
         StringBuilder var3 = new StringBuilder();

         for (int var4 = var0; var4 < var2.length && var4 < var1; var4++) {
            var3.append(var2[var4].getMethodName() + ":" + var2[var4].getLineNumber()).append("->");
         }

         return var3.toString();
      } catch (Throwable var5) {
         return null;
      }
   }

   public static int toArgb(int var0, int var1, int var2) {
      return new Color(var0, var1, var2, 255).getRGB();
   }

   public static boolean ensureDirectory(String var0, boolean var1) {
      File var2 = new File(var0);
      if (var2.exists()) {
         return true;
      }

      try {
         Files.createDirectories(var2.toPath());
         return true;
      } catch (Throwable var4) {
         if (var1) {
            var4.printStackTrace();
         }

         return false;
      }
   }

   public static Set keysForValue(Map var0, Object var1) {
      return var0.entrySet().stream().filter(var1x -> Objects.equals(var1x.getValue(), var1)).map(Entry::getKey).collect(Collectors.toSet());
   }

   public static String relativePath(File var0) {
      return normalizePluginPath(var0.getAbsolutePath());
   }

   public static String normalizePluginPath(String var0) {
      if (var0 == null) {
         return null;
      }

      String var1 = var0.replace("\\", "/");
      int var2 = var1.indexOf("plugins/");
      if (var2 != -1) {
         var1 = var1.substring(var2);
      }

      int var3 = var1.indexOf("/");
      if (var3 != -1) {
         var1 = var1.substring(var3 + 1);
      }

      var3 = var1.indexOf("/");
      if (var3 != -1) {
         var1 = var1.substring(var3 + 1);
      }

      return var1;
   }

   public static String shortPath(File var0) {
      return shortPathOf(var0.getAbsolutePath());
   }

   public static String shortPathOf(String var0) {
      if (var0 == null) {
         return null;
      }

      String var1 = var0.replace("\\", "/");
      int var2 = var1.indexOf("plugins/");
      if (var2 != -1) {
         var1 = var1.substring(var2);
      }

      return var1.startsWith("plugins/ItemsAdder/") ? var1.replace("plugins/ItemsAdder/", "") : var1;
   }

   public static ArrayList listFilesOrEmpty(File var0, FileFilter var1) {
      File[] var2 = var0.listFiles(var1);
      return var2 != null ? new ArrayList<>(Arrays.asList(var2)) : new ArrayList();
   }

   public static int countChildren(File var0) {
      String[] var1 = var0.list();
      return var1 == null ? 0 : var1.length;
   }

   public static String worldRelativePath(File var0) {
      String var1 = Bukkit.getServer().getWorldContainer().getAbsolutePath();
      if (var1.endsWith(".")) {
         var1 = var1.substring(0, var1.length() - 2);
      }

      File var2 = new File(var1).getParentFile();
      return var2 == null ? var0.getAbsolutePath() : var0.getAbsolutePath().replace(var2.getAbsolutePath(), "");
   }

   public static Path toPath(String var0) {
      return new File(var0).toPath();
   }

   public static String normalizeSlashes(String var0) {
      var0 = var0.replace('\\', '/');
      int var1 = var0.startsWith("/") ? 1 : 0;

      for (int var2 = var1 + 1; var2 < var0.length(); var2++) {
         if (var0.charAt(var2) == '/' && var0.charAt(var2 - 1) == '/' && (var2 < 2 || var0.charAt(var2 - 2) != ':')) {
            StringBuilder var3 = new StringBuilder(var0.length()).append(var0, var1, var2);

            while (var2 < var0.length()) {
               char var4 = var0.charAt(var2);
               int var5 = var3.length();
               if (var4 != '/' || var5 <= 0 || var3.charAt(var5 - 1) != '/' || var5 >= 2 && var3.charAt(var5 - 2) == ':') {
                  var3.append(var4);
               }

               var2++;
            }

            return var3.toString();
         }
      }

      return var1 == 0 ? var0 : var0.substring(1);
   }

   public static String normalizedPath(File var0) {
      return normalizeSlashes(var0.getAbsolutePath());
   }

   public static String removePrefix(String var0, String var1) {
      return var0.startsWith(var1) ? var0.substring(var1.length()) : var0;
   }

   public static String removeSuffix(String var0, String var1) {
      return var0.replaceFirst(var1 + "$", "");
   }

   public static String lastSegment(String var0) {
      return var0.substring(var0.lastIndexOf(47) + 1);
   }

   public static String utf8Roundtrip(String var0) {
      return new String(var0.getBytes(StandardCharsets.UTF_8));
   }

   public static Object[] copyArray(Object[] var0) {
      return Arrays.copyOf(var0, var0.length);
   }

   public static String extensionOf(File var0) {
      return FilenameUtils.getExtension(var0.getAbsolutePath());
   }

   public static String fileNameOrBase(File var0, boolean var1) {
      return var1 ? var0.getName() : FilenameUtils.getBaseName(var0.getAbsolutePath());
   }

   public static String humanReadableBytes(long var0) {
      if (-1000L < var0 && var0 < 1000L) {
         return var0 + " B";
      }

      StringCharacterIterator var2 = new StringCharacterIterator("kMGTPE");

      while (var0 <= -999950L || var0 >= 999950L) {
         var0 /= 1000L;
         var2.next();
      }

      return String.format("%.1f %cB", var0 / 1000.0, var2.current());
   }

   public static int clampInt(int var0, int var1, int var2) {
      return var0 < var1 ? var1 : Math.min(var0, var2);
   }

   public static String substringFrom(String var0, String var1) {
      return var0.substring(var0.indexOf(var1));
   }

   public static boolean isValidRegex(String var0) {
      String[] var1 = var0.split(":");
      if (var1.length == 2 && !var1[0].endsWith("\\")) {
         return false;
      }

      try {
         Pattern.compile(var0);
         return true;
      } catch (PatternSyntaxException var3) {
         return false;
      }
   }

   public static boolean isPowerOfTwo(int var0) {
      double var1 = Math.log(var0) / Math.log(2.0);
      return Math.ceil(var1) == Math.floor(var1);
   }

   public static String joinComma(List var0) {
      StringJoiner var1 = new StringJoiner(",");
      var0.forEach(var1x -> var1.add(var1x.toString()));
      return var1.toString();
   }

   public static boolean containsString(String[] var0, String var1) {
      for (String var5 : var0) {
         if (var5.equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public static String richTextToLegacy(String var0) {
      Component var1 = AdventureText.parseRichText(var0);
      return LEGACY_SERIALIZER.serialize(var1);
   }

   public static String componentToLegacy(Component var0) {
      return LEGACY_SERIALIZER.serialize(var0);
   }

   public static String readStreamAsString(InputStream var0) {
      return new String(var0.readAllBytes(), StandardCharsets.UTF_8);
   }

   public static int getPotionAmplifier(Player var0, PotionEffectType var1) {
      PotionEffect var2 = var0.getPotionEffect(var1);
      return var2 == null ? 0 : var2.getAmplifier();
   }

   @Nullable
   public static Location clickedLocation(PlayerInteractEvent var0) {
      return var0.getClickedBlock() != null && var0.getClickedPosition() != null ? var0.getClickedBlock().getLocation().add(var0.getClickedPosition()) : null;
   }

   public static String sha256Hex(String var0) {
      try {
         MessageDigest var1 = MessageDigest.getInstance("SHA-256");
         byte[] var2 = var1.digest(var0.getBytes(StandardCharsets.UTF_8));
         StringBuilder var3 = new StringBuilder();

         for (byte var7 : var2) {
            String var8 = Integer.toHexString(255 & var7);
            if (var8.length() == 1) {
               var3.append('0');
            }

            var3.append(var8);
         }

         return var3.toString();
      } catch (NoSuchAlgorithmException var9) {
         throw new RuntimeException("SHA-256 algorithm not found", var9);
      }
   }

   public static Collection listResourceEntries(String var0, Predicate var1) {
      ArrayList var2 = new ArrayList();
      URL var3 = Utils.class.getClassLoader().getResource(var0);
      if (var3 == null) {
         return var2;
      }

      if (var3.getProtocol().equals("jar")) {
         String var4 = var3.getPath().substring(5, var3.getPath().indexOf("!"));

         try (JarFile var5 = new JarFile(URLDecoder.decode(var4, StandardCharsets.UTF_8))) {
            String var6 = var0.endsWith("/") ? var0 : var0 + "/";
            Enumeration var7 = var5.entries();

            while (var7.hasMoreElements()) {
               String var8 = ((JarEntry)var7.nextElement()).getName();
               if (var8.startsWith(var6) && !var8.equals(var6)) {
                  String var9 = var8.substring(var6.length());
                  if (!var9.isEmpty() && !var8.endsWith("/") && var1.test(var9)) {
                     var2.add(var8);
                  }
               }
            }
         }
      }

      return var2;
   }
}
