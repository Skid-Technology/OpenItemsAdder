package dev.lone.itemsadder.config;

import dev.lone.itemsadder.resourcepack.PackWarningSummary;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;

public final class YamlDuplicateKeyChecker {
   private static final Object bNu = new Object();
   private static final Logger bNv = Logger.getLogger("org.bukkit.configuration.file.YamlConstructor");
   private static final Set bNw = new HashSet();

   private YamlDuplicateKeyChecker() {
   }

   public static void reset() {
      synchronized (bNw) {
         bNw.clear();
      }
   }

   static void method7470(YamlConfiguration var0, File var1) {
      if (!var1.exists()) {
         throw new FileNotFoundException(var1.toString());
      }

      List var2 = List.of();

      try (BufferedReader var3 = Files.newBufferedReader(var1.toPath(), StandardCharsets.UTF_8)) {
         var2 = method7471(var3);
      } catch (RuntimeException var26) {
      }

      synchronized (bNu) {
         long var4 = Thread.currentThread().getId();
         Filter var6 = bNv.getFilter();
         Filter var7 = var3x -> Thread.currentThread().getId() == var4 && var3x.getMessage() != null && var3x.getMessage().startsWith("duplicate keys found")
            ? false
            : var6 == null || var6.isLoggable(var3x);

         try {
            bNv.setFilter(var7);
         } catch (SecurityException var22) {
            var0.load(var1);
            method7474(var1, var2);
            return;
         }

         try {
            var0.load(var1);
         } finally {
            try {
               if (bNv.getFilter() == var7) {
                  bNv.setFilter(var6);
               }
            } catch (SecurityException var20) {
            }
         }
      }

      method7474(var1, var2);
   }

   static List method7471(Reader var0) {
      ArrayList var1 = new ArrayList();
      Set var2 = Collections.newSetFromMap(new IdentityHashMap());

      for (Node var4 : new Yaml().composeAll(var0)) {
         method7473(var4, "", var1, var2);
      }

      return var1;
   }

   public static boolean method7472(File var0) {
      try (BufferedReader var1 = Files.newBufferedReader(var0.toPath(), StandardCharsets.UTF_8)) {
         return !method7471(var1).isEmpty();
      }
   }

   private static void method7473(Node var0, String var1, List var2, Set var3) {
      if (var0 != null && var3.add(var0)) {
         if (var0 instanceof MappingNode var4) {
            HashMap var6 = new HashMap();

            for (NodeTuple var8 : var4.getValue()) {
               Node var9 = var8.getKeyNode();
               String var10 = var1;
               if (var9 instanceof ScalarNode var11) {
                  String var12 = var11.getValue();
                  var10 = var1.isEmpty() ? var12 : var1 + "." + var12;
                  int var13 = var9.getStartMark().getLine() + 1;
                  Integer var14 = var6.putIfAbsent(var12, var13);
                  if (var14 != null) {
                     var2.add(new YamlKeyLocation(var10, var14, var13));
                  }
               }

               method7473(var8.getValueNode(), var10, var2, var3);
            }
         } else if (var0 instanceof SequenceNode var5) {
            for (int var15 = 0; var15 < var5.getValue().size(); var15++) {
               method7473((Node)var5.getValue().get(var15), var1 + "[" + var15 + "]", var2, var3);
            }
         }
      }
   }

   private static void method7474(File var0, List var1) {
      if (!var1.isEmpty()) {
         String var2 = Utils.shortPathOf(var0.getPath());
         String var3 = var0.getAbsolutePath() + "\u0000" + var0.lastModified() + "\u0000" + var1;
         synchronized (bNw) {
            if (!bNw.add(var3)) {
               return;
            }
         }

         String var7 = String.join(", ", var1.stream().limit(8L).map(var0x -> "'" + var0x.bNx + "' (lines " + var0x.bNy + " and " + var0x.bNz + ")").toList());
         String var5 = "Found "
            + var1.size()
            + " duplicate YAML key"
            + (var1.size() == 1 ? "" : "s")
            + " in "
            + var2
            + ". Later values override earlier ones: "
            + var7
            + (var1.size() > 8 ? ", +" + (var1.size() - 8) + " more" : "");
         Log.warn(var5);
         PackWarningSummary.addWarning("Duplicate YAML keys", var5);
      }
   }
}
