package dev.lone.itemsadder.block;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.config.YamlDuplicateKeyChecker;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.internal.SwitchMap78;
import dev.lone.itemsadder.resourcepack.PackWarningSummary;
import dev.lone.itemsadder.util.IntArrayOps;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.MaterialTable;
import dev.lone.itemsadder.util.MaterialTableEntry;
import dev.lone.itemsadder.util.Utils;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

public class CustomBlockStateSettings {
   private static CustomBlockStateSettings bvh;
   private static final int bvi = MaterialTable.getEntriesForMode(BlockRealizationMode.REAL).size();
   private static final int bvj = MaterialTable.getEntriesForMaterial(Material.CHORUS_PLANT).size();
   private static final int bvk = MaterialTable.getEntriesForMaterial(Material.TRIPWIRE).size();
   private static final int bvl = MaterialTable.getEntriesForMaterial(Material.FIRE).size();
   private static final int bvm = (int)MaterialTable.getEntriesForMaterial(Material.NOTE_BLOCK).stream().filter(MaterialTableEntry::requiresVersion20).count();
   private static final int bvn = (int)MaterialTable.getEntriesForMaterial(Material.NOTE_BLOCK).stream().filter(MaterialTableEntry::requiresVersion26).count();
   private static final int bvo = MaterialTable.getEntriesForMaterial(Material.NOTE_BLOCK).size() - bvm - bvn - 49;
   public static final String bvp = "resource_pack.custom_blocks.enable_1_20_states_350";
   public static final String bvq = "resource_pack.custom_blocks.enable_26_1_states_200";
   public static int bvr;
   public static int bvs;
   public static int bvt;
   public static int bvu;
   public static int bvv;
   public static int bvw;
   private final ContentManager bvx;
   public YamlConfig bvy;
   public YamlConfig bvz;
   public YamlConfig bvA;
   public YamlConfig bvB;
   public YamlConfig bvC;
   public YamlConfig bvD;
   private final Map bvE = new LinkedHashMap();
   private final Map bvF = new EnumMap(Material.class);
   public final Map bvG = new HashMap();
   private int bvH;
   private int bvI;
   private int bvJ;
   private int bvK;
   private int bvL;
   private Map bvM = Collections.emptyMap();
   private Map bvN;
   private final Map bvO = new TreeMap();
   private final Set bvP = new TreeSet();
   private volatile boolean bvQ;

   public CustomBlockStateSettings() {
      bvh = this;
      this.bvx = ContentManager.method8084();
   }

   public static CustomBlockStateSettings getInstance() {
      if (bvh == null) {
         bvh = new CustomBlockStateSettings();
      }

      return bvh;
   }

   public void init() {
      this.resetContentLoad();
      Main var1 = Main.getInstance();
      this.bvy = new YamlConfig(var1, true, "storage/items_ids_cache.yml", false, false, '~');
      this.bvz = new YamlConfig(var1, true, "storage/real_blocks_ids_cache.yml", false, false);
      this.bvA = new YamlConfig(var1, true, "storage/real_blocks_note_ids_cache.yml", false, false);
      this.bvB = new YamlConfig(var1, true, "storage/real_transparent_blocks_ids_cache.yml", false, false);
      this.bvC = new YamlConfig(var1, true, "storage/real_wire_ids_cache.yml", false, false);
      this.bvD = new YamlConfig(var1, true, "storage/custom_fires_ids_cache.yml", false, false);
      this.bvE.clear();

      for (Material var3 : MaterialTable.getTableMaterials()) {
         this.bvE.put(var3, new YamlConfig(var1, true, "storage/blockstates/" + var3.name().toLowerCase(Locale.ROOT) + ".yml", false, false, '~'));
      }

      for (YamlConfig var6 : List.of(this.bvy, this.bvz, this.bvA, this.bvB, this.bvC, this.bvD)) {
         validateCacheFile(var6);
      }

      for (YamlConfig var7 : this.bvE.values()) {
         validateCacheFile(var7);
      }

      this.validateBlockStates();
      this.migrateReservedNoteStates();
      this.loadLegacyIds();
      this.loadPlayerHeadIds();
   }

   private static void validateCacheFile(YamlConfig var0) {
      if (var0.getFile().exists()) {
         try {
            new YamlConfiguration().load(var0.getFile());
            if (YamlDuplicateKeyChecker.method7472(var0.getFile())) {
               throw new IllegalStateException("Duplicate keys in persistent ID cache: " + var0.getFile());
            }
         } catch (IOException | InvalidConfigurationException var2) {
            throw new IllegalStateException("Invalid persistent ID cache: " + var0.getFile(), var2);
         }
      }
   }

   private boolean isStateAllowed(MaterialTableEntry var1) {
      if (var1.isHarpNoteBlock()) {
         return false;
      } else {
         return var1.requiresVersion20()
            ? Settings.get().customBlocksEnable120States350 && Version.isAtLeast(Version.v1_20_R2)
            : !var1.requiresVersion26() || Settings.get().customBlocksEnable261States200 && Version.isAtLeast(Version.v26_1_1);
      }
   }

   private void validateBlockStates() {
      this.bvF.clear();

      for (Entry var2 : this.bvE.entrySet()) {
         Material var3 = (Material)var2.getKey();
         YamlConfig var4 = (YamlConfig)var2.getValue();
         BitSet var5 = new BitSet();
         this.bvF.put(var3, var5);

         for (Entry var7 : var4.abM().getValues(false).entrySet()) {
            if (!(var7.getValue() instanceof String var8)) {
               throw new IllegalStateException("Invalid block-state cache entry " + (String)var7.getKey() + " in " + var4.getFile());
            }

            MaterialTableEntry var10 = MaterialTable.findByState(var3, var8);
            if (var10 == null) {
               throw new IllegalStateException("Invalid block state " + var8 + " for " + (String)var7.getKey() + " in " + var4.getFile());
            }

            if (!var10.isHarpNoteBlock() && !this.isStateAllowed(var10)) {
               throw new IllegalStateException(
                  "Block state "
                     + var8
                     + " for "
                     + (String)var7.getKey()
                     + " in "
                     + var4.getFile()
                     + (
                        var10.requiresVersion20()
                           ? " requires resource_pack.custom_blocks.enable_1_20_states_350 and server 1.20.2+"
                           : " requires resource_pack.custom_blocks.enable_26_1_states_200 and server 26.1.1+"
                     )
               );
            }

            if (var5.get(var10.id())) {
               throw new IllegalStateException("Duplicate block state " + var8 + " in " + var4.getFile());
            }

            var5.set(var10.id());
         }
      }

      if (Settings.get().customBlocksEnable120States350 && !Version.isAtLeast(Version.v1_20_R2)) {
         throw new IllegalStateException("resource_pack.custom_blocks.enable_1_20_states_350 requires server 1.20.2+");
      }

      if (Settings.get().customBlocksEnable261States200 && !Version.isAtLeast(Version.v26_1_1)) {
         throw new IllegalStateException("resource_pack.custom_blocks.enable_26_1_states_200 requires server 26.1.1+");
      }
   }

   private void loadLegacyIds() {
      this.migrateLegacyIds(this.bvz, BlockRealizationMode.REAL);
      this.migrateLegacyIds(this.bvB, BlockRealizationMode.REAL_TRANSPARENT);
      this.migrateLegacyIds(this.bvA, BlockRealizationMode.REAL_NOTE);
      this.migrateLegacyIds(this.bvC, BlockRealizationMode.REAL_WIRE);
      this.migrateLegacyIds(this.bvD, BlockRealizationMode.FIRE);
   }

   private void migrateReservedNoteStates() {
      YamlConfig var1 = (YamlConfig)this.bvE.get(Material.NOTE_BLOCK);

      for (String var3 : var1.abM().getKeys(false)) {
         MaterialTableEntry var4 = MaterialTable.findByState(Material.NOTE_BLOCK, var1.getString(var3));
         if (var4 != null && var4.isHarpNoteBlock()) {
            MaterialTableEntry var5 = this.findFreeState(Material.NOTE_BLOCK);
            if (var5 == null) {
               throw new IllegalStateException("No free note block state to migrate " + var3 + " from " + var4.state());
            }

            var1.method7048(var3, var5.state());
            ((BitSet)this.bvF.get(Material.NOTE_BLOCK)).set(var5.id());
            Log.warn("Reassigned reserved vanilla note state of " + var3 + " from " + var4.state() + " to " + var5.state());
         }
      }
   }

   private void migrateLegacyIds(YamlConfig var1, BlockRealizationMode var2) {
      if (var1.getFile().exists()) {
         for (String var4 : var1.abM().getKeys(true)) {
            Object var5 = var1.abM().get(var4);
            if (!(var5 instanceof ConfigurationSection)) {
               if (!(var5 instanceof Number var6)) {
                  throw new IllegalStateException("Invalid legacy block ID for " + var4 + " in " + var1.getFile());
               }

               long var7 = var6.longValue();
               if (var7 < -2147483648L || var7 > 2147483647L || var6.doubleValue() != var7) {
                  throw new IllegalStateException("Invalid legacy block ID for " + var4 + " in " + var1.getFile());
               }

               int var9 = (int)var7;
               Material var10 = legacyIdToMaterial(var2, var9);
               if (var10 == null) {
                  throw new IllegalStateException("Unknown legacy block ID " + var9 + " for " + var4 + " in " + var1.getFile());
               }

               YamlConfig var11 = (YamlConfig)this.bvE.get(var10);
               MaterialTableEntry var12 = MaterialTable.findForState(var2, var10, var9);
               if (!var11.contains(var4)) {
                  MaterialTableEntry var13 = var12;
                  if (var13 == null || !this.isStateAllowed(var13) || ((BitSet)this.bvF.get(var10)).get(var13.id())) {
                     var13 = this.findFreeState(var10);
                     if (var13 == null) {
                        throw new IllegalStateException("No free block state to migrate " + var4 + " in " + var1.getFile());
                     }

                     Log.warn("Reassigned invalid or duplicate legacy block ID " + var9 + " of " + var4 + " to " + var13.state());
                  }

                  var11.method7048(var4, var13.state());
                  ((BitSet)this.bvF.get(var10)).set(var13.id());
               }
            }
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Nullable
   private static Material legacyIdToMaterial(BlockRealizationMode var0, int var1) {
      return switch (SwitchMap78.bvR[var0.ordinal()]) {
         case 1 -> var1 >= 0 && var1 < 64
            ? Material.BROWN_MUSHROOM_BLOCK
            : (var1 >= 64 && var1 < 128 ? Material.RED_MUSHROOM_BLOCK : (var1 >= 128 && var1 < 192 ? Material.MUSHROOM_STEM : null));
         case 2 -> var1 >= 192 && var1 < 256 ? Material.CHORUS_PLANT : null;
         case 3 -> var1 >= 256 && var1 <= 1006 ? Material.NOTE_BLOCK : null;
         case 4 -> var1 >= 1007 && var1 <= 1135 ? Material.TRIPWIRE : null;
         case 5 -> var1 >= 0 && var1 <= bvl ? Material.FIRE : null;
         default -> null;
      };
   }

   @Nullable
   private MaterialTableEntry findFreeState(Material var1) {
      BitSet var2 = (BitSet)this.bvF.get(var1);

      for (MaterialTableEntry var4 : MaterialTable.getEntriesForMaterial(var1)) {
         if (this.isStateAllowed(var4) && !var2.get(var4.id()) && (var1 != Material.NOTE_BLOCK || !this.bvG.containsKey(var4.id()))) {
            return var4;
         }
      }

      return null;
   }

   private void deleteLegacyCaches() {
      for (YamlConfig var2 : List.of(this.bvz, this.bvA, this.bvB, this.bvC, this.bvD)) {
         try {
            Files.deleteIfExists(var2.getFile().toPath());
         } catch (IOException var4) {
            Log.warn("Converted block-state cache, but could not delete old file " + var2.getFile() + ": " + var4);
         }
      }
   }

   public void beginContentLoad() {
      this.bvN = new HashMap();
      this.bvO.clear();
      this.bvP.clear();
      this.bvQ = false;
   }

   public void resetContentLoad() {
      this.bvN = null;
      this.bvO.clear();
      this.bvP.clear();
      this.bvQ = false;
   }

   public void commitLoadedIds() {
      if (this.bvN != null) {
         this.bvM = Map.copyOf(this.bvN);
         this.bvN = null;
         this.bvQ = true;
         this.logIdCollisions();
      }
   }

   public boolean isLoadCompleted() {
      return this.bvQ;
   }

   private void recordSeenId(YamlConfig var1, String var2, int var3) {
      if (this.bvN != null) {
         this.bvN.put(cacheKey(var1, var2), var3);
      }
   }

   private boolean matchesPreviousId(YamlConfig var1, String var2, int var3) {
      return Objects.equals(this.bvM.get(cacheKey(var1, var2)), var3);
   }

   private static String cacheKey(YamlConfig var0, String var1) {
      return var0.DJ() + "\u0000" + var1;
   }

   public void logUsedIds() {
      this.bvH = this.countRealIds();
      this.bvJ = this.countChorusIds();
      this.bvI = this.countNoteIds();
      this.bvK = this.countWireIds();
      this.bvL = this.countFireIds();
      int var1 = this.bvx.aE.wU();
      if (Settings.bK()) {
         int var2 = 0;

         for (CustomBlockDefinition var4 : ContentManager.method8084().al) {
            if (var4.anF != null && var4.anF.Fx()) {
               var2++;
            }
         }

         Log.log(ChatColor.GRAY + "Used Blocks IDs:");
         if (Settings.get().customBlocksMushroom) {
            Log.log(ChatColor.GRAY + " - REAL " + ChatColor.AQUA + this.bvH + ChatColor.GRAY + "/" + ChatColor.AQUA + bvi);
         }

         if (Settings.get().customBlocksNote) {
            Log.log(
               ChatColor.GRAY
                  + " - REAL_NOTE "
                  + ChatColor.AQUA
                  + this.bvI
                  + ChatColor.GRAY
                  + "/"
                  + ChatColor.AQUA
                  + (bvo + (Settings.get().customBlocksEnable120States350 ? bvm : 0) + (Settings.get().customBlocksEnable261States200 ? bvn : 0))
            );
         }

         if (Settings.get().customBlocksChorus) {
            Log.log(ChatColor.GRAY + " - REAL_TRANSPARENT " + ChatColor.AQUA + this.bvJ + ChatColor.GRAY + "/" + ChatColor.AQUA + bvj);
         }

         if (Settings.get().customBlocksWire) {
            Log.log(ChatColor.GRAY + " - REAL_WIRE " + ChatColor.AQUA + this.bvK + ChatColor.GRAY + "/" + ChatColor.AQUA + bvk);
         }

         if (Settings.get().customBlocksFire) {
            Log.log(ChatColor.GRAY + " - FIRE " + ChatColor.AQUA + this.bvL + ChatColor.GRAY + "/" + ChatColor.AQUA + bvl);
         }

         if (var2 > 0) {
            Log.log(ChatColor.GRAY + " - Directional states: " + ChatColor.AQUA + var2);
         }
      }

      Log.log(ChatColor.GRAY + "Used Font Images: " + ChatColor.AQUA + var1 + ChatColor.GRAY + "/" + ChatColor.AQUA + this.bvx.aE.Ym);
   }

   public void refreshCachedIds() {
      if (!this.bvQ) {
         this.loadPlayerHeadIds();
      } else {
         this.saveAllCaches();
      }
   }

   public void reloadPersistentIds() {
      this.loadPlayerHeadIds();
   }

   public void commitContentIds() {
      this.commitContentIdsWith(null);
   }

   public void commitContentIdsWith(YamlConfig var1) {
      if (this.bvN == null) {
         throw new IllegalStateException("No content ID load to commit");
      }

      this.writeCaches(var1);
      this.commitLoadedIds();
      this.deleteLegacyCaches();
   }

   private void saveAllCaches() {
      this.writeCaches(null);
   }

   private void writeCaches(YamlConfig var1) {
      LinkedHashMap var2 = new LinkedHashMap();
      var2.put(this.bvy.getFile().toPath(), this.bvy.abM().saveToString());

      for (YamlConfig var4 : this.bvE.values()) {
         var2.put(var4.getFile().toPath(), var4.abM().saveToString());
      }

      if (var1 != null) {
         var2.put(var1.getFile().toPath(), var1.abM().saveToString());
      }

      try {
         atomicWriteFiles(var2);
      } catch (IOException var5) {
         throw new UncheckedIOException("Could not commit content IDs", var5);
      }

      this.loadPlayerHeadIds();
   }

   static void atomicWriteFiles(Map var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      LinkedHashMap var2 = new LinkedHashMap();
      ArrayList var3 = new ArrayList();
      boolean var4 = true;

      try {
         for (Entry var28 : var0.entrySet()) {
            Path var30 = ((Path)var28.getKey()).toAbsolutePath();
            Files.createDirectories(var30.getParent());
            Path var31 = Files.createTempFile(var30.getParent(), ".ia-ids-", ".tmp");
            var1.put(var30, var31);
            Files.writeString(var31, (CharSequence)var28.getValue(), StandardCharsets.UTF_8);
            if (Files.exists(var30)) {
               Path var9 = Files.createTempFile(var30.getParent(), ".ia-ids-", ".backup");
               var2.put(var30, var9);
               Files.copy(var30, var9, StandardCopyOption.REPLACE_EXISTING);
            }
         }

         for (Entry var29 : var1.entrySet()) {
            Files.move((Path)var29.getValue(), (Path)var29.getKey(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            var3.add((Path)var29.getKey());
         }
      } catch (IOException var24) {
         IOException var5 = var24;
         Collections.reverse(var3);

         for (Path var7 : var3) {
            try {
               Path var8 = (Path)var2.get(var7);
               if (var8 == null) {
                  Files.deleteIfExists(var7);
               } else {
                  Files.move(var8, var7, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
               }
            } catch (IOException var23) {
               var4 = false;
               var5.addSuppressed(new IOException("ID recovery backup retained: " + var2.get(var7), var23));
            }
         }

         throw var5;
      } finally {
         for (Path var12 : var1.values()) {
            try {
               Files.deleteIfExists(var12);
            } catch (IOException var22) {
            }
         }

         if (var4) {
            for (Path var33 : var2.values()) {
               try {
                  Files.deleteIfExists(var33);
               } catch (IOException var21) {
               }
            }
         }
      }
   }

   private void loadPlayerHeadIds() {
      bvr = this.bvy.getInt("PLAYER_HEAD" + this.bvy.abP() + "__manually_handled:_iainternal__player_phead_0");
      bvs = this.bvy.getInt("PLAYER_HEAD" + this.bvy.abP() + "__manually_handled:_iainternal__player_parm_left_3");
      bvt = this.bvy.getInt("PLAYER_HEAD" + this.bvy.abP() + "__manually_handled:_iainternal__player_pbody_2");
      bvu = this.bvy.getInt("PLAYER_HEAD" + this.bvy.abP() + "__manually_handled:_iainternal__player_pleg_left_1");
      bvv = this.bvy.getInt("PLAYER_HEAD" + this.bvy.abP() + "__manually_handled:_iainternal__player_pleg_right_5");
      bvw = this.bvy.getInt("PLAYER_HEAD" + this.bvy.abP() + "__manually_handled:_iainternal__player_parm_right_4");
   }

   public static int getPlayerHeadModelId(String var0) {
      switch (var0) {
         case "phead_0":
         case "parm_left_3":
         case "pbody_2":
         case "pleg_left_1":
         case "pleg_right_5":
         case "parm_right_4":
            return getInstance().allocateStateId(Material.PLAYER_HEAD, "__manually_handled", "_iainternal__player_" + var0);
         default:
            return -1;
      }
   }

   public int resolveStateId(Material var1, BlockRealizationMode var2, String var3, String var4) {
      return var2 == BlockRealizationMode.TILE ? this.allocateStateId(var1, var3, var4) : this.resolveStateIdForRealization(var2, var3, var4);
   }

   public int resolveStateIdForRealization(BlockRealizationMode var1, String var2, String var3) {
      String var4 = var2 + ":" + var3;

      for (Material var6 : MaterialTable.getTableMaterials()) {
         if (((MaterialTableEntry)MaterialTable.getEntriesForMaterial(var6).get(0)).type() == var1) {
            YamlConfig var7 = (YamlConfig)this.bvE.get(var6);
            if (var7.contains(var4)) {
               MaterialTableEntry var8 = MaterialTable.findByState(var6, var7.getString(var4));
               if (var8 != null && var1 == BlockRealizationMode.REAL_NOTE && this.bvG.containsKey(var8.id())) {
                  Log.warn("Custom block " + var4 + " collides with manually supplied note_block.json state " + var8.state());
                  var7.jJ(var4);
                  ((BitSet)this.bvF.get(var6)).clear(var8.id());
                  break;
               }

               if (var8 != null) {
                  this.recordSeenId(var7, var4, var8.id());
                  return var8.id();
               }
            }
         }
      }

      for (Material var10 : MaterialTable.getTableMaterials()) {
         if (((MaterialTableEntry)MaterialTable.getEntriesForMaterial(var10).get(0)).type() == var1) {
            MaterialTableEntry var11 = this.findFreeState(var10);
            if (var11 != null) {
               YamlConfig var12 = (YamlConfig)this.bvE.get(var10);
               var12.method7048(var4, var11.state());
               ((BitSet)this.bvF.get(var10)).set(var11.id());
               this.recordSeenId(var12, var4, var11.id());
               return var11.id();
            }
         }
      }

      return -1;
   }

   public int allocateStateId(Material var1, String var2, String var3) {
      String var4 = var1.toString();
      int var5 = this.getStartingCustomModelData(var1);
      String var6 = var4 + this.bvy.abP() + var2 + ":" + var3;
      if (this.bvy.contains(var6)) {
         int var9 = this.bvy.getInt(var6);
         this.recordSeenId(this.bvy, var6, var9);
         return var9;
      }

      List var7 = this.bvy.jN(var4);
      int var8 = IntArrayOps.findFirstFreeIndex(var7 == null ? Collections.emptyList() : var7, var5, Integer.MAX_VALUE);
      if (var8 == -1) {
         return -1;
      }

      this.bvy.method7048(var6, var8);
      this.recordSeenId(this.bvy, var6, var8);
      return var8;
   }

   private int getStartingCustomModelData(Material var1) {
      return Main.config
         .getInt("resource_pack.custom_model_data_starting_value." + var1, Main.config.getInt("resource_pack.custom_model_data_starting_value.ALL"));
   }

   public int registerCustomModelData(Material var1, String var2, String var3, int var4, boolean var5, String var6) {
      String var7 = var1.toString();
      String var8 = var2 + ":" + var3;
      String var9 = var7 + this.bvy.abP() + var8;
      boolean var10 = this.bvy.contains(var9);
      if (var10 && this.bvy.getInt(var9) == var4) {
         this.recordSeenId(this.bvy, var9, var4);
         return var4;
      }

      Map var11 = this.bvy.jL(var7);
      if (var11 != null && var11.containsValue(var4)) {
         String var12 = String.valueOf(Utils.keysForValue(var11, var4));
         String var13 = var1 + "/" + var4 + " reserved by " + var12;
         this.bvO.computeIfAbsent(var13, var0 -> new TreeSet<>()).add(var8);
         this.bvP.add(var6);
         PackWarningSummary.addWarning(
            "Custom model data collisions",
            "Material/CMD: " + var1 + "/" + var4 + "\nRequested by: " + var8 + "\nFile: " + var6 + "\nReserved in storage/items_ids_cache.yml by: " + var12
         );
         if (var10) {
            this.recordSeenId(this.bvy, var9, this.bvy.getInt(var9));
         }

         return -1;
      } else {
         this.bvy.method7048(var9, var4);
         this.recordSeenId(this.bvy, var9, var4);
         if (var5) {
            this.bvy.save();
         }

         return var4;
      }
   }

   private void logIdCollisions() {
      if (!this.bvO.isEmpty()) {
         int var1 = this.bvO.values().stream().mapToInt(Set::size).sum();
         StringBuilder var2 = new StringBuilder("[Items] ")
            .append(var1)
            .append(" item")
            .append(var1 == 1 ? "" : "s")
            .append(" could not be loaded because ")
            .append(this.bvO.size())
            .append(" manually assigned custom_model_data value")
            .append(this.bvO.size() == 1 ? " is" : "s are")
            .append(" already used by other items in the ID cache.\nFiles containing conflicting items:");
         this.bvP.forEach(var1x -> var2.append("\n  - ").append(var1x));
         var2.append("\nChange or remove the custom_model_data value from each conflicting item.")
            .append("\nIf the previous items were removed, run '/iacleancache items' to remove their unused IDs, then generate the pack again.")
            .append("\nFull details: ItemsAdder/output/resource-pack-diagnostics.txt");
         Log.error(var2.toString());
      }
   }

   public void saveIdCache() {
      if (this.bvQ) {
         this.bvy.save();
      }
   }

   public int countRealIds() {
      return ((YamlConfig)this.bvE.get(Material.BROWN_MUSHROOM_BLOCK)).abO().size()
         + ((YamlConfig)this.bvE.get(Material.RED_MUSHROOM_BLOCK)).abO().size()
         + ((YamlConfig)this.bvE.get(Material.MUSHROOM_STEM)).abO().size();
   }

   public int countNoteIds() {
      return ((YamlConfig)this.bvE.get(Material.NOTE_BLOCK)).abO().size();
   }

   public int countChorusIds() {
      return ((YamlConfig)this.bvE.get(Material.CHORUS_PLANT)).abO().size();
   }

   public int countFireIds() {
      return ((YamlConfig)this.bvE.get(Material.FIRE)).abO().size();
   }

   public int countWireIds() {
      return ((YamlConfig)this.bvE.get(Material.TRIPWIRE)).abO().size();
   }

   public int getRealCount() {
      return this.bvH;
   }

   public int getNoteCount() {
      return this.bvI;
   }

   public int getChorusCount() {
      return this.bvJ;
   }

   public int getWireCount() {
      return this.bvK;
   }

   public int getFireCount() {
      return this.bvL;
   }

   public int cleanItemIds(CommandSender var1) {
      if (!this.bvQ) {
         Log.warn("Cannot clean item IDs because the latest content load did not complete successfully.");
         return 0;
      }

      int var2 = 0;

      for (String var4 : this.bvy.abO()) {
         for (String var6 : this.bvy.getStringList(var4)) {
            String var7 = var4 + this.bvy.abP() + var6;
            int var8 = this.bvy.getInt(var7);
            if (!this.matchesPreviousId(this.bvy, var7, var8)) {
               Log.log(ChatColor.YELLOW + "Removed unused or outdated item ID " + var6 + " (" + var4 + ") with id " + var8 + " from " + this.bvy.DJ());
               this.bvy.method7048(var7, null);
               var2++;
            }
         }
      }

      if (var2 > 0) {
         this.bvy.save();
      }

      Log.sendPrefixed(var1, "Removed " + var2 + " unused or outdated item IDs from cache. Check console for more info.");
      return var2;
   }

   public int cleanBlockIds() {
      if (!this.bvQ) {
         Log.warn("Cannot clean block IDs because the latest content load did not complete successfully.");
         return 0;
      }

      int var1 = 0;

      for (YamlConfig var3 : this.bvE.values()) {
         var1 += this.cleanFile(var3);
      }

      return var1;
   }

   private int cleanFile(YamlConfig var1) {
      int var2 = 0;

      for (String var4 : var1.abO()) {
         MaterialTableEntry var5 = MaterialTable.findByState(this.materialForFile(var1), var1.getString(var4));
         int var6 = var5 == null ? -1 : var5.id();
         if (!this.matchesPreviousId(var1, var4, var6)) {
            Log.log(ChatColor.YELLOW + "Removed unused or outdated block ID " + var4 + " with id " + var6 + " from " + var1.DJ());
            var1.method7048(var4, null);
            if (var5 != null) {
               ((BitSet)this.bvF.get(var5.material())).clear(var5.id());
            }

            var2++;
         }
      }

      if (var2 > 0) {
         var1.save();
      }

      return var2;
   }

   private Material materialForFile(YamlConfig var1) {
      for (Entry var3 : this.bvE.entrySet()) {
         if (var3.getValue() == var1) {
            return (Material)var3.getKey();
         }
      }

      throw new IllegalArgumentException("Unknown block-state file " + var1.getFile());
   }

   public void registerCustomBlockOwner(Integer var1, String var2) {
      this.bvG.put(var1, var2);
   }

   public void clearBlockOwners() {
      this.bvG.clear();
   }
}
