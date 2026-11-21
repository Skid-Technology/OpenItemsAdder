package dev.lone.itemsadder.config;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import net.kyori.adventure.text.Component;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.parser.ParserException;

public class YamlConfig {
   final Plugin bLL;
   private boolean bFi;
   private FileConfiguration bLM;
   private final String bLN;
   private final File bLO;
   private boolean bLP = true;
   private boolean bLQ = false;
   private String aJv;
   private boolean bLR;
   private boolean bLS;
   @Nullable
   private Long bLT = null;

   public static YamlConfig method7035(Plugin var0, String var1, YamlConfiguration var2) {
      return new YamlConfig(var0, var1, var2);
   }

   private YamlConfig(Plugin var1, String var2, YamlConfiguration var3) {
      this.bLL = var1;
      this.aJv = var2;
      this.bLO = new File(var1.getDataFolder(), var2);
      this.bLN = this.bLO.getPath();
      this.bLM = var3;
      this.bFi = true;
   }

   public YamlConfig(Plugin var1, boolean var2, String var3, boolean var4, boolean var5, @Nullable Character var6) {
      this(var1, var2, var3, var4, var5, true, var6);
   }

   public YamlConfig(Plugin var1, boolean var2, String var3, boolean var4, boolean var5) {
      this(var1, var2, var3, var4, var5, true);
   }

   public YamlConfig(Plugin var1, boolean var2, String var3, boolean var4, boolean var5, boolean var6) {
      this(var1, var2, var3, var4, var5, var6, false, null);
   }

   public YamlConfig(Plugin var1, boolean var2, String var3, boolean var4, boolean var5, boolean var6, @Nullable Character var7) {
      this(var1, var2, var3, var4, var5, var6, false, var7);
   }

   public YamlConfig(Plugin var1, boolean var2, String var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      this(var1, var2, var3, var4, var5, var6, var7, null);
   }

   public YamlConfig(Plugin var1, boolean var2, String var3, boolean var4, boolean var5, boolean var6, boolean var7, @Nullable Character var8) {
      this.bLL = var1;
      this.bLR = var6;
      if (var2) {
         this.aJv = var3;
         this.bLN = var1.getDataFolder() + File.separator + var3;
      } else {
         this.aJv = var3.replace(var1.getDataFolder().getAbsolutePath(), "");
         this.bLN = var3;
      }

      this.aJv = this.aJv.replace("\\", "/");
      if (!new File(this.bLN).exists()) {
         new File(this.bLN).getParentFile().mkdirs();
      }

      this.bLP = var4;
      this.bLQ = var5;
      this.bLO = new File(this.bLN);

      try {
         this.bLM = method7045(this.bLO, var8);
         this.abL();
         this.bFi = true;
      } catch (InvalidConfigurationException | ParserException var10) {
         Bukkit.getLogger().log(Level.WARNING, "Cannot load file: " + this.aJv);
         if (var7) {
            var10.printStackTrace();
         } else {
            Log.warn("Error: " + var10.getMessage());
         }

         this.bFi = false;
      } catch (Throwable var11) {
         Bukkit.getLogger().log(Level.SEVERE, "Cannot load file: " + this.aJv);
         var11.printStackTrace();
         this.bFi = false;
      }
   }

   public YamlConfig(Plugin var1, boolean var2, String var3, boolean var4) {
      this(var1, var2, var3, var4, false, null);
   }

   public YamlConfig(Plugin var1, boolean var2, String var3, boolean var4, @Nullable Character var5) {
      this(var1, var2, var3, var4, false, var5);
   }

   public static PotionType jE(String var0) {
      if (var0 == null) {
         return null;
      }

      PotionType var1 = (PotionType)BukkitRegistryLookup.enumOrNull(var0.replace("minecraft:", "").toUpperCase(), PotionType.class);
      if (var1 == null) {
         for (PotionType var5 : PotionType.values()) {
            if (var5.getKey() != null && var5.getKey().toString().equalsIgnoreCase(var0)) {
               return var5;
            }
         }
      }

      return var1;
   }

   @Nullable
   public static NamespacedKey method7036(String var0, String var1, boolean var2, Consumer var3) {
      if (var0 == null) {
         return null;
      }

      if (!var0.contains(":")) {
         var0 = var1 + ":" + var0;
      }

      try {
         return NamespacedKey.fromString(var0);
      } catch (Exception var5) {
         if (var2) {
            return null;
         }

         var3.accept(var0);
         return null;
      }
   }

   @Nullable
   public static EquipmentSlot method7037(String var0, @Nullable Consumer var1) {
      Object var2 = null;

      try {
         return jF(var0);
      } finally {
         if (var1 != null && var2 == null) {
            var1.accept(var0);
         }
      }
   }

   @Nullable
   public static EquipmentSlot jF(String var0) {
      return BukkitRegistryLookup.equipmentSlot(var0);
   }

   @Nullable
   public static String method7038(String var0, boolean var1, Runnable var2) {
      if (var0 == null) {
         if (!var1) {
            var2.run();
         }

         return null;
      } else {
         return var0;
      }
   }

   @Nullable
   public static List method7039(List var0, Class var1, boolean var2, boolean var3, Consumer var4) {
      ArrayList var5 = new ArrayList();

      for (String var7 : var0) {
         Enum var8 = BukkitRegistryLookup.enumOrNull(var7, var1);
         if (var8 == null) {
            if (var2) {
               continue;
            }

            var4.accept(var7);
            if (!var3) {
               return null;
            }
         }

         var5.add(var8);
      }

      return var5;
   }

   public List jG(String var1) {
      return this.method7040(var1, false);
   }

   public List method7040(String var1, boolean var2) {
      return !this.contains(var1) ? new ArrayList() : new ArrayList(this.bLM.getConfigurationSection(var1).getKeys(var2));
   }

   public static List method7041(ConfigurationSection var0) {
      return method7042(var0, "");
   }

   public static List method7042(ConfigurationSection var0, String var1) {
      return !var0.contains(var1) ? new ArrayList() : new ArrayList(var0.getKeys(false));
   }

   public static List method7043(ConfigSectionWrapper var0, String var1) {
      return !var0.hA(var1) ? new ArrayList() : new ArrayList(var0.bJ(false));
   }

   public static List method7044(ConfigSectionWrapper var0) {
      return method7043(var0, "");
   }

   public boolean isLoaded() {
      return this.bFi;
   }

   public boolean abI() {
      return this.bLS;
   }

   public Long lastModified() {
      if (this.bLT != null) {
         return this.bLT;
      }

      this.bLT = this.getFile().lastModified();
      return this.bLT;
   }

   @NotNull
   public static YamlConfiguration method7045(@NotNull File var0, @Nullable Character var1) {
      Validate.notNull(var0, "File cannot be null");
      YamlConfiguration var2 = new YamlConfiguration();
      if (var1 != null) {
         var2.options().pathSeparator(var1);
      }

      var2.options().width(99999);

      try {
         YamlDuplicateKeyChecker.method7470(var2, var0);
      } catch (FileNotFoundException var4) {
      }

      return var2;
   }

   private YamlConfig(Plugin var1, String var2) {
      this.bLL = var1;
      this.bLN = var2;
      this.bLO = new File(this.bLN);
      this.bLM = YamlConfiguration.loadConfiguration(this.bLO);
   }

   private YamlConfig(Plugin var1, String var2, char var3) {
      this.bLL = var1;
      this.bLN = var2;
      this.bLO = new File(this.bLN);
      this.bLM = method7045(this.bLO, var3);
   }

   public YamlConfig abK() {
      YamlConfig var1 = new YamlConfig(this.bLL, this.bLN);
      var1.aJv = this.aJv;
      var1.bLP = this.bLP;
      var1.bLQ = this.bLQ;
      return var1;
   }

   public YamlConfig method7046(char var1) {
      YamlConfig var2 = new YamlConfig(this.bLL, this.bLN, var1);
      var2.abM().options().pathSeparator(var1);
      return var2;
   }

   public File getFile() {
      return this.bLO;
   }

   public void reload() {
      this.bLM = YamlConfiguration.loadConfiguration(this.bLO);
   }

   public void abL() {
      if (this.bLP) {
         Path var1 = this.bLL.getDataFolder().toPath().toAbsolutePath().normalize();
         Path var2 = this.bLO.toPath().toAbsolutePath().normalize();
         String var3 = (var2.startsWith(var1) ? var1.relativize(var2).toString() : this.aJv).replace('\\', '/');

         try {
            if (!this.bLO.exists()) {
               FileUtils.copyInputStreamToFile(this.bLL.getResource(var3), this.bLO);
            } else if (this.bLQ) {
               YamlConfiguration var4 = YamlConfiguration.loadConfiguration(new InputStreamReader(this.bLL.getResource(var3), StandardCharsets.UTF_8));

               for (String var6 : var4.getKeys(true)) {
                  if (!this.bLM.contains(var6)) {
                     this.bLM.set(var6, var4.get(var6));
                  }
               }

               this.bLM.save(this.bLO);
            }

            this.bLM.load(this.bLO);
         } catch (IOException | InvalidConfigurationException | NullPointerException var7) {
            if (this.bLR) {
               Log.log(ChatColor.YELLOW + "Using custom language file '" + this.bLN + "'");
            }
         }
      }
   }

   public FileConfiguration abM() {
      return this.bLM;
   }

   public String GQ() {
      return this.bLN;
   }

   public String DJ() {
      return this.aJv;
   }

   public static String method7047(File var0) {
      return var0.getAbsolutePath().replace(Main.getPlugin().getDataFolder().getAbsolutePath(), "");
   }

   public static String jA(String var0) {
      return Utils.toSectionCodes(var0).replace("\\n", "\n");
   }

   protected void jH(String var1) {
      Log.error("MISSING FILE PROPERTY! '" + var1 + "' in file '" + this.bLN + "'");
   }

   public void method7048(String var1, Object var2) {
      this.bLM.set(var1, var2);
      this.bLS = true;
   }

   public void abN() {
      if (this.bLS) {
         try {
            this.bLM.save(this.bLO);
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }
   }

   public void save() {
      try {
         this.bLM.save(this.bLO);
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }

   public void method7049(File var1) {
      try {
         this.bLM.save(var1);
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   public boolean contains(String var1) {
      return this.bLM.contains(var1);
   }

   public void jJ(String var1) {
      this.bLM.set(var1, null);
      this.bLS = true;
   }

   public List abO() {
      return this.method7050("", false);
   }

   public List getStringList(String var1) {
      return this.method7050(var1, false);
   }

   public List method7050(String var1, boolean var2) {
      return !this.contains(var1) ? new ArrayList() : new ArrayList(this.bLM.getConfigurationSection(var1).getKeys(var2));
   }

   public static List method7051(ConfigurationSection var0) {
      return method7052(var0, "");
   }

   public static List method7052(ConfigurationSection var0, String var1) {
      return !var0.contains(var1) ? new ArrayList() : new ArrayList(var0.getKeys(false));
   }

   public Map jL(String var1) {
      return !this.contains(var1) ? null : this.bLM.getConfigurationSection(var1).getValues(false);
   }

   @Nullable
   public Map jM(String var1) {
      if (!this.contains(var1)) {
         return null;
      }

      HashMap var2 = new HashMap();
      ConfigurationSection var3 = this.bLM.getConfigurationSection(var1);
      if (var3 == null) {
         return null;
      }

      String var4 = String.valueOf(this.bLM.options().pathSeparator());

      for (String var7 : var3.getKeys(true)) {
         Object var8 = var3.get(var7);
         if (!(var8 instanceof MemorySection)) {
            String[] var9 = var7.split(var4);
            var2.put(var9[var9.length - 1], var8);
         }
      }

      return var2;
   }

   @Nullable
   public List jN(String var1) {
      if (!this.contains(var1)) {
         return null;
      }

      ArrayList var2 = new ArrayList();
      ConfigurationSection var3 = this.bLM.getConfigurationSection(var1);
      if (var3 == null) {
         return null;
      }

      for (String var6 : var3.getKeys(true)) {
         Object var7 = var3.get(var6);
         if (!(var7 instanceof MemorySection)) {
            var2.add((Integer)var7);
         }
      }

      return var2;
   }

   public Object method7053(String var1, Object var2) {
      return this.bLM.get(var1, var2);
   }

   public String getString(String var1) {
      return this.bLM.getString(var1);
   }

   public String getString(String var1, String var2) {
      return this.bLM.getString(var1, var2);
   }

   public int getInt(String var1) {
      return this.bLM.getInt(var1);
   }

   public int getInt(String var1, int var2) {
      return this.bLM.getInt(var1, var2);
   }

   public double getDouble(String var1, double var2) {
      return this.bLM.getDouble(var1, var2);
   }

   public float getFloat(String var1, float var2) {
      return (float)this.bLM.getDouble(var1, var2);
   }

   public float getFloat(String var1) {
      return this.getFloat(var1, 0.0F);
   }

   public boolean getBoolean(String var1) {
      return this.bLM.getBoolean(var1);
   }

   public boolean getBoolean(String var1, boolean var2) {
      return this.bLM.getBoolean(var1, var2);
   }

   @Nullable
   public Boolean method7058(String var1, @Nullable Boolean var2) {
      return !this.contains(var1) ? var2 : this.bLM.getBoolean(var1, var2 != null && var2);
   }

   public Material method7059(String var1, Material var2) {
      String var3 = (String)this.bLM.get(var1, null);
      if (var3 instanceof String) {
         var3 = var3.toUpperCase();
         return (Material)BukkitRegistryLookup.enumOrDefault(var3, Material.class, var2);
      } else {
         return var2;
      }
   }

   public static Material method7060(String var0, Material var1) {
      return var0 == null ? null : (Material)BukkitRegistryLookup.enumOrDefault(var0.toUpperCase(), Material.class, var1);
   }

   public Material getMaterial(String var1) {
      return this.method7059(var1, null);
   }

   public PotionEffectType jT(String var1) {
      Object var2 = this.bLM.get(var1, null);
      return var2 instanceof String ? jU((String)var2) : null;
   }

   @Nullable
   public static PotionEffectType jU(String var0) {
      return BukkitRegistryLookup.potionEffectType(var0);
   }

   public Enum method7061(String var1, Class var2, Enum var3) {
      String var4 = this.getString(var1);
      return var4 != null ? BukkitRegistryLookup.enumOrDefault(var4.toUpperCase(), var2, var3) : var3;
   }

   public Enum method7062(String var1, Class var2, Enum var3, Consumer var4) {
      String var5 = this.getString(var1);
      Enum var6 = var5 != null ? BukkitRegistryLookup.enumOrDefault(var5.toUpperCase(), var2, var3) : var3;
      if (var6 == null) {
         var4.accept(var5);
      }

      return var6;
   }

   public static Enum method7063(ConfigurationSection var0, String var1, Class var2) {
      return method7064(var0, var1, var2, null);
   }

   public static Enum method7064(ConfigurationSection var0, String var1, Class var2, Enum var3) {
      String var4 = var0.getString(var1);
      return var4 != null ? BukkitRegistryLookup.enumOrDefault(var4.toUpperCase(), var2, var3) : var3;
   }

   public String jV(String var1) {
      return this.contains(var1) ? ChatColor.translateAlternateColorCodes('&', this.getString(var1)) : var1;
   }

   public Component jW(String var1) {
      return this.contains(var1) ? AdventureText.fromLegacy(this.jV(var1)) : AdventureText.fromLegacy(var1);
   }

   public String bd(String var1, String var2) {
      return this.contains(var1) ? AdventureText.plainTextOf(this.getString(var1)) : var2;
   }

   public String jX(String var1) {
      return this.bd(var1, null);
   }

   public String jY(String var1) {
      return ChatColor.stripColor(this.jX(var1));
   }

   public String be(String var1, String var2) {
      return ChatColor.stripColor(this.bd(var1, var2));
   }

   public List method7065(String var1, boolean var2) {
      if (var2) {
         ArrayList var3 = new ArrayList();

         try {
            for (String var6 : (ArrayList)this.bLM.getStringList(var1)) {
               var3.add(AdventureText.plainTextOf(var6));
            }
         } catch (NullPointerException var7) {
            var7.printStackTrace();
            this.jH(var1);
         }

         return var3;
      } else {
         return this.bLM.getStringList(var1);
      }
   }

   public List jZ(String var1) {
      return this.method7065(var1, false);
   }

   public Set ka(String var1) {
      return new HashSet(this.method7065(var1, false));
   }

   @Nullable
   public ConfigurationSection kb(@NotNull String var1) {
      return this.bLM.getConfigurationSection(var1);
   }

   @Nullable
   public ConfigurationSection method7066(@NotNull String... var1) {
      for (String var5 : var1) {
         ConfigurationSection var6 = this.kb(var5);
         if (var6 != null) {
            return var6;
         }
      }

      return null;
   }

   @Nullable
   public ConfigSectionWrapper kc(@NotNull String var1) {
      return !this.contains(var1) ? null : new ConfigSectionWrapper(this.bLM.getConfigurationSection(var1));
   }

   public ConfigSectionWrapper kd(@NotNull String var1) {
      if (!this.contains(var1)) {
         this.bLM.createSection(var1);
         this.bLS = true;
      } else if (!(this.bLM.get(var1) instanceof ConfigurationSection)) {
         this.bLM.set(var1, null);
         this.bLM.createSection(var1);
         this.bLS = true;
      }

      return new ConfigSectionWrapper(this.bLM.getConfigurationSection(var1));
   }

   @Nullable
   public EntityType method7067(String var1, EntityType var2, @Nullable Consumer var3) {
      if (this.contains(var1)) {
         String var4 = this.getString(var1).toUpperCase();

         try {
            return EntityType.valueOf(var4);
         } catch (Exception var6) {
            if (var3 != null) {
               var3.accept(var4);
            }

            return null;
         }
      } else {
         return var2;
      }
   }

   public char abP() {
      return this.abM().options().pathSeparator();
   }

   @Nullable
   public Integer method7068(String var1, @Nullable Integer var2) {
      return this.method7053(var1, var2) instanceof Integer var3 ? var3 : var2;
   }
}
