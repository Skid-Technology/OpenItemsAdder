package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.util.ServerCompat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import org.bukkit.ChatColor;

final class PackReport {
   private static final int MAX_LISTED_ENTRIES = 25;
   private final Map resultGroups = new TreeMap<>(Comparator.comparing(PackAssetResult::UF));
   private final Map unreferencedEntries = new TreeMap<>(Comparator.comparing(PackAssetEntry::UF));

   void addResult(Version var1, PackResolutionResult var2) {
      if (var2.kind() == PackIssueCategory.PRE_EXISTING_WARNING) {
         PackValidationError var3 = var2.tree();
         PackAssetResult var4;
         if (var3 == null) {
            var4 = PackAssetResult.method119(var2);
         } else {
            var4 = PackAssetResult.method120(var2, var3);
         }

         PackAssetIndexBuilder var5 = this.resultGroups.computeIfAbsent(var4, PackAssetIndexBuilder::new);
         String var6 = versionName(var1);
         if (var6 != null) {
            var5.bxg.add(var6);
         }

         if (var3 != null) {
            var5.method6039(var3.root(), chainWithoutRoot(var3));
         }
      }
   }

   void addUnreferencedModels(Collection var1, Map var2) {
      ArrayList var3 = new ArrayList(var2.size());
      var2.forEach((var1x, var2x) -> var3.add(new PackAssetEntry(var1x, var2x, PackOrigin.UNKNOWN, ItemContextBuilder.modelJsonPath(var1x), null, true)));
      this.addUnreferencedEntries(var1, var3);
   }

   void addUnreferencedEntries(Collection var1, Collection var2) {
      TreeSet var3 = new TreeSet();
      var1.forEach(var1x -> {
         String var2x = versionName(var1x);
         if (var2x != null) {
            var3.add(var2x);
         }
      });
      var2.stream().filter(var0 -> var0.bwZ.isExternal()).forEach(var2x -> this.unreferencedEntries.computeIfAbsent(var2x, var0 -> new TreeSet()).addAll(var3));
   }

   boolean isEmpty() {
      return this.resultGroups.isEmpty() && this.unreferencedEntries.isEmpty();
   }

   boolean hasUnreferenced() {
      return !this.unreferencedEntries.isEmpty();
   }

   boolean hasWarnings() {
      return !this.resultGroups.isEmpty();
   }

   String formatForLog() {
      return this.format(ServerCompat.isDevMode());
   }

   String format(boolean var1) {
      StringBuilder var2 = new StringBuilder();
      if (!this.resultGroups.isEmpty()) {
         this.appendWarningGroups(var2, var1);
      }

      if (!this.unreferencedEntries.isEmpty()) {
         if (!var2.isEmpty()) {
            var2.append("\n\n");
         }

         this.appendUnreferencedSection(var2, var1);
      }

      if (var1 && !var2.isEmpty()) {
         var2.insert(0, ChatColor.YELLOW);
      }

      return var1 ? highlightItemContextPaths(var2.toString()) : shortenItemContextPaths(var2.toString());
   }

   String buildDiagnosticsSummary(String var1) {
      TreeSet var2 = new TreeSet();
      TreeSet var3 = new TreeSet();
      TreeSet var4 = new TreeSet();
      this.resultGroups.values().forEach(var1x -> var2.addAll(var1x.bxh.keySet()));
      this.resultGroups
         .keySet()
         .stream()
         .filter(var0 -> var0.bxk && !var0.bxl.isBlank())
         .map(PackAssetResult::UQ)
         .map(PackReport::shortenItemContextPaths)
         .forEach(var3::add);
      this.resultGroups
         .keySet()
         .stream()
         .filter(PackAssetResult::UM)
         .map(PackAssetResult::uM)
         .map(ItemContextBuilder::modelJsonPath)
         .map(PackReport::shortenItemContextPaths)
         .forEach(var4::add);
      long var5 = this.resultGroups.keySet().stream().filter(PackAssetResult::UM).count();
      StringBuilder var7 = new StringBuilder("[Pack] Resource-pack diagnostics: ");
      if (!this.resultGroups.isEmpty()) {
         var7.append(this.resultGroups.size()).append(" warning group").append(this.resultGroups.size() == 1 ? "" : "s");
         if (var5 > 0L) {
            var7.append(" (").append(var5).append(" missing model").append(var5 == 1L ? "" : "s").append(")");
         }

         if (!var2.isEmpty()) {
            var7.append(" affecting ").append(var2.size()).append(" custom entr").append(var2.size() == 1 ? "y" : "ies");
         }
      }

      if (!this.unreferencedEntries.isEmpty()) {
         if (!this.resultGroups.isEmpty()) {
            var7.append("; ");
         }

         var7.append(this.unreferencedEntries.size())
            .append(" unreferenced model file")
            .append(this.unreferencedEntries.size() == 1 ? "" : "s")
            .append(" (informational)");
      }

      var7.append(". Details: ").append(var1);
      if (!var4.isEmpty()) {
         var7.append("\n  Expected missing model files (").append(var4.size()).append("):");
         var4.forEach(var1x -> var7.append("\n    - ").append(var1x));
      }

      if (!var3.isEmpty()) {
         var7.append("\n  Affected declaration files (").append(var3.size()).append("):");
         var3.forEach(var1x -> var7.append("\n    - ").append(var1x));
      }

      return var7.toString();
   }

   static String shortenItemContextPaths(String var0) {
      return var0.replaceAll("assets/_iainternal/models/item_context/([^/\\s]+)/", "assets/$1/models/")
         .replaceAll("_iainternal:item_context/([^/\\s]+)/", "$1:");
   }

   static String highlightItemContextPaths(String var0) {
      return var0.replaceAll("(_iainternal:item_context/[^\\s']+)", ChatColor.GRAY + "$1" + ChatColor.YELLOW);
   }

   private void appendWarningGroups(StringBuilder var1, boolean var2) {
      TreeSet var3 = new TreeSet();
      TreeSet var4 = new TreeSet();
      TreeSet var5 = new TreeSet();
      this.resultGroups.values().forEach(var1x -> var3.addAll(var1x.bxg));
      this.resultGroups.values().forEach(var1x -> var4.addAll(var1x.bxh.keySet()));
      this.resultGroups.keySet().stream().filter(PackAssetResult::UM).map(PackAssetResult::uM).forEach(var5::add);
      var1.append("[Pack] Found ")
         .append(this.resultGroups.size())
         .append(" grouped resource-pack warning section")
         .append(this.resultGroups.size() == 1 ? "" : "s");
      if (!var4.isEmpty()) {
         var1.append(" affecting ").append(var4.size()).append(" custom entr").append(var4.size() == 1 ? "y" : "ies");
      }

      appendVersionCount(var1, var3);
      var1.append(".\n");
      if (!var5.isEmpty()) {
         var1.append("  Unique missing models: ").append(var5.size()).append('\n');
      }

      for (PackAssetIndexBuilder var7 : this.resultGroups.values()) {
         PackAssetResult var8 = var7.bxf;
         var1.append("\n  ").append(var8.Ll()).append('\n');
         if (var8.bxk) {
            var1.append("    ").append(var8.UM() ? "Used by" : "Affected custom entries").append(" (").append(var7.bxh.size()).append("):\n");
            appendReferenceChains(var1, var7.bxh);
            var1.append("\n    Declared from: ").append(var8.bxl).append('\n').append("    Minecraft material: ").append(materialNameOf(var8.bxl)).append('\n');
            appendVersions(var1, "    Affected ", var7.bxg, var2);
            var1.append("\n    What this means:\n      ")
               .append(describeImpact(var8))
               .append('\n')
               .append("\n    How to fix:\n      ")
               .append(describeFix(var8))
               .append('\n');
            appendTechnicalDetails(var1, var2, "    ", "Source classification: " + var8.bxj, "Validation reason: " + var8.bxn);
         } else {
            var1.append("    What this means:\n")
               .append("      ItemsAdder found a resource-pack reference it could not validate.\n")
               .append("    How to fix:\n")
               .append("      Check the referenced model or texture ID, its namespace/path, and the file that supplies it.\n");
         }

         if (!var8.bxk) {
            appendTechnicalDetails(var1, var2, "    ", "Source classification: " + var8.bxj, "Validation message: " + var8.bxo);
         }
      }
   }

   private void appendUnreferencedSection(StringBuilder var1, boolean var2) {
      TreeSet var3 = new TreeSet();
      this.unreferencedEntries.values().forEach(var3::addAll);
      long var4 = this.unreferencedEntries.keySet().stream().filter(var0 -> !var0.bwY).count();
      long var6 = this.unreferencedEntries.size() - var4;
      var1.append("[Pack] Found ")
         .append(this.unreferencedEntries.size())
         .append(" model file")
         .append(this.unreferencedEntries.size() == 1 ? "" : "s")
         .append(" with no static item or block reference.\n");
      appendVersions(var1, "  Affected ", var3, var2);
      EnumMap var8 = new EnumMap<>(PackOrigin.class);
      this.unreferencedEntries.keySet().forEach(var1x -> var8.merge(var1x.bwZ, 1L, Long::sum));
      ArrayList var9 = new ArrayList();
      var9.add("Fallback contexts: item " + var4 + ", block " + var6);

      for (PackOrigin var13 : PackOrigin.values()) {
         Long var14 = (Long)var8.get(var13);
         if (var14 != null) {
            var9.add("Source " + var13.getLabel() + ": " + var14);
         }
      }

      appendTechnicalDetails(var1, var2, "  ", var9.toArray(String[]::new));
      var1.append("\n  These files exist in the generated pack; they are not missing or invalid by themselves.\n")
         .append("  Check them only if a configured item or block was expected to use them.\n");
      this.appendUnusedEntityModels(var1, var3, var2);
      this.appendOriginSection(
         var1,
         "Unreferenced model files from resource packs",
         "No item declaration or blockstate in the pack references these files.",
         var3,
         var2,
         PackOrigin.USER_CONTENT_PACK,
         PackOrigin.EXTERNAL_RESOURCE_PACK,
         PackOrigin.UNKNOWN
      );
      this.appendOriginSection(
         var1,
         "Models supplied by plugins or pack events",
         "No action is needed if the integration selects these models at runtime.",
         var3,
         var2,
         PackOrigin.EVENT_STREAM_OR_WRITER
      );
   }

   private void appendUnusedEntityModels(StringBuilder var1, Set var2, boolean var3) {
      List var4 = this.unreferencedEntries.entrySet().stream().filter(var0 -> ((PackAssetEntry)var0.getKey()).bxe).toList();
      if (!var4.isEmpty()) {
         var1.append("\n  Unused custom entity model files (")
            .append(var4.size())
            .append(")\n")
            .append("    These files are inside a configured custom entity model folder, but its current .metadata does not use them.\n")
            .append("    You can delete them if they are no longer used by another plugin or runtime integration.\n");
         appendGroupedEntries(var1, var4, var2, var3);
      }
   }

   private void appendOriginSection(StringBuilder var1, String var2, String var3, Set var4, boolean var5, PackOrigin... var6) {
      ArrayList var7 = new ArrayList();

      for (PackOrigin var11 : var6) {
         this.unreferencedEntries
            .entrySet()
            .stream()
            .filter(var1x -> !((PackAssetEntry)var1x.getKey()).bxe && ((PackAssetEntry)var1x.getKey()).bwZ == var11)
            .forEach(var7::add);
      }

      if (!var7.isEmpty()) {
         var1.append("\n  ").append(var2).append(" (").append(var7.size()).append(")\n").append("    ").append(var3).append('\n');
         appendGroupedEntries(var1, var7, var4, var5);
      }
   }

   private static void appendGroupedEntries(StringBuilder var0, List var1, Set var2, boolean var3) {
      TreeMap var4 = new TreeMap();
      var1.forEach(var1x -> var4.computeIfAbsent(namespaceOf(((PackAssetEntry)var1x.getKey()).bwX), var0xx -> new ArrayList()).add(var1x));

      for (Entry var6 : var4.entrySet()) {
         var0.append("      ").append((String)var6.getKey()).append(" (").append(((List)var6.getValue()).size()).append("):\n");
         ((List)var6.getValue()).forEach(var3x -> appendEntryDetails(var0, (PackAssetEntry)var3x.getKey(), (Set)var3x.getValue(), var2, var3));
      }
   }

   private static void appendEntryDetails(StringBuilder var0, PackAssetEntry var1, Set var2, Set var3, boolean var4) {
      var0.append("        - ").append(var1.bwX).append('\n').append("          File: ").append(var1.bxb).append('\n');
      if (var1.bwZ.isItemsAdderOwned()) {
         var0.append("          Status: normal ItemsAdder internal support model; no fix is needed.\n");
      } else if (var1.bwZ == PackOrigin.VANILLA_CLIENT_ASSET) {
         var0.append("          Status: normal Vanilla client model; no fix is normally needed.\n");
      }

      String var5 = overlayOf(var1.bxa);
      if (var5 != null || !var2.equals(var3)) {
         var0.append("          Applies to: ");
         if (var5 != null) {
            var0.append("overlay ").append(var5);
         }

         if (!var2.isEmpty()) {
            if (var5 != null) {
               var0.append(", ");
            }

            var0.append("versions ").append(String.join(", ", var2));
         }

         var0.append('\n');
      }

      if (var4) {
         var0.append("          ")
            .append(ChatColor.GRAY)
            .append("Context: ")
            .append(var1.bwY ? "block" : "item")
            .append("; source: ")
            .append(var1.bwZ.getLabel())
            .append("; reason: ")
            .append(var1.bxc)
            .append(ChatColor.YELLOW)
            .append('\n');
      }
   }

   private static void appendTechnicalDetails(StringBuilder var0, boolean var1, String var2, String... var3) {
      if (var1) {
         var0.append(var2).append(ChatColor.GRAY).append("Technical details:\n");

         for (int var4 = 0; var4 < var3.length; var4++) {
            var0.append(var2).append("  ").append(var3[var4]);
            if (var4 == var3.length - 1) {
               var0.append(ChatColor.YELLOW);
            }

            var0.append('\n');
         }
      }
   }

   private static String overlayOf(String var0) {
      int var1 = var0.indexOf("/assets/");
      return var1 > 0 ? var0.substring(0, var1) : null;
   }

   private static void appendVersionCount(StringBuilder var0, Set var1) {
      if (!var1.isEmpty()) {
         var0.append(" across ").append(var1.size()).append(" Vanilla asset version").append(var1.size() == 1 ? "" : "s");
      }
   }

   private static void appendVersions(StringBuilder var0, String var1, Set var2, boolean var3) {
      if (var3 && !var2.isEmpty()) {
         var0.append(ChatColor.GRAY).append(var1).append("Versions: ").append(String.join(", ", var2)).append(ChatColor.YELLOW).append('\n');
      }
   }

   private static void appendReferenceChains(StringBuilder var0, Map var1) {
      var1.entrySet().stream().limit(25L).forEach(var1x -> {
         var0.append("      - ").append((String)var1x.getKey()).append('\n');
         if (((Set)var1x.getValue()).size() == 1) {
            appendReferenceChain(var0, "        Reference chain:", (List)((Set)var1x.getValue()).iterator().next());
         } else {
            var0.append("        Reference chains (").append(((Set)var1x.getValue()).size()).append("):\n");
            int var2x = 1;

            for (List var4 : (Set)var1x.getValue()) {
               appendReferenceChain(var0, "          " + var2x++ + ".", var4);
            }
         }
      });
      int var2 = var1.size() - Math.min(var1.size(), 25);
      if (var2 > 0) {
         var0.append("      - ").append(var2).append(" additional custom entries omitted to keep this console warning readable\n");
      }
   }

   private static void appendReferenceChain(StringBuilder var0, String var1, List var2) {
      if (!var2.isEmpty()) {
         var0.append(var1).append('\n');
         var0.append("          ").append((String)var2.getFirst()).append('\n');
         var2.stream().skip(1L).forEach(var1x -> var0.append("          -> ").append(var1x).append('\n'));
      }
   }

   private static String describeImpact(PackAssetResult var0) {
      if (var0.UM()) {
         return "The custom entries above eventually reference a model that was not found in the generated resource pack.";
      } else if (var0.bxn.startsWith("item model has unresolved face texture slots")) {
         return "This model contains texture slots that ItemsAdder could not resolve.";
      } else if (var0.bxn.contains("is not selected by the effective item or block atlas")) {
         return "This model uses a texture that is not included in the selected item or block atlas.";
      } else if (var0.bxn.startsWith("cannot read texture value")) {
         return "ItemsAdder could not read one of this model's texture values.";
      } else {
         return var0.bxn.contains("has no selected pack entry")
            ? "This model references a texture file that was not found in the generated resource pack."
            : "ItemsAdder found a model or texture reference it could not validate.";
      }
   }

   private static String describeFix(PackAssetResult var0) {
      if (var0.UM()) {
         String var1 = var0.bxm.substring(var0.bxm.indexOf(58) + 1);
         String var2 = var1.contains("/")
            ? ""
            : "\n      If this came from a parent value like '"
               + var1
               + "', it resolves in the minecraft namespace; use the intended namespace/path or remove a self-parent reference.";
         return "Check for a typo in '"
            + var0.bxm
            + "'.\n      Make sure '"
            + ItemContextBuilder.modelJsonPath(var0.bxm)
            + "' exists with the correct namespace and path.\n      If another plugin or pack event supplies it, verify that the integration adds the file."
            + var2;
      } else if (var0.bxn.startsWith("cannot read texture value")) {
         return "Check the model JSON for an empty or invalid texture value.";
      } else {
         return var0.bxn.contains("atlas")
            ? "Check the texture ID, namespace/path, and texture file.\n      Also check the item or block atlas source that should include it."
            : "Check the referenced model or texture ID, its namespace/path, and the declaration or integration that supplies it.";
      }
   }

   private static List chainWithoutRoot(PackValidationError var0) {
      ArrayList var1 = new ArrayList(var0.chain());
      if (!var1.isEmpty() && ItemContextBuilder.normalizeId((String)var1.getFirst()).equals(ItemContextBuilder.normalizeId(var0.root()))) {
         var1.removeFirst();
      }

      return List.copyOf(var1);
   }

   private static String versionName(Version var0) {
      return var0 == Version.UNKNOWN ? null : var0.getName();
   }

   private static String materialNameOf(String var0) {
      int var1 = var0.lastIndexOf(47);
      int var2 = var0.lastIndexOf(46);
      return var0.substring(var1 + 1, var2 > var1 ? var2 : var0.length());
   }

   private static String namespaceOf(String var0) {
      int var1 = var0.indexOf(58);
      return var1 < 0 ? "minecraft" : var0.substring(0, var1);
   }
}
