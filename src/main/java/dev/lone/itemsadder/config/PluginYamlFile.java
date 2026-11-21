package dev.lone.itemsadder.config;

import dev.lone.itemsadder.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.parser.ParserException;

public class PluginYamlFile {
   final Plugin bMK;
   private FileConfiguration bLM;
   private final String bML;
   private final File bMM;
   private String aJv;
   private boolean bFi;
   private boolean bLS;

   public static PluginYamlFile method7292(Plugin var0, String var1) {
      try {
         PluginYamlFile var2 = new PluginYamlFile(var0, true, var1, true, null);
         if (var2.bFi) {
            return var2;
         }
      } catch (FileNotFoundException var3) {
      }

      return null;
   }

   public PluginYamlFile(Plugin var1, boolean var2, String var3, boolean var4, @Nullable Character var5) {
      this.bMK = var1;
      if (var2) {
         this.aJv = var3;
         this.bML = var1.getDataFolder() + File.separator + var3;
      } else {
         this.aJv = var3.replace(var1.getDataFolder().getAbsolutePath(), "");
         this.bML = var3;
      }

      this.aJv = this.aJv.replace("\\", "/");
      if (!new File(this.bML).exists()) {
         throw new FileNotFoundException();
      }

      this.bMM = new File(this.bML);
      if (!this.bMM.exists()) {
         throw new FileNotFoundException();
      }

      try {
         this.bLM = method7293(this.bMM, var5);
         this.bFi = true;
      } catch (InvalidConfigurationException | ParserException var7) {
         Bukkit.getLogger().log(Level.WARNING, "Cannot load file: " + this.aJv);
         if (var4) {
            var7.printStackTrace();
         } else {
            Log.warn("Error: " + var7.getMessage());
         }

         this.bFi = false;
      } catch (Throwable var8) {
         Bukkit.getLogger().log(Level.SEVERE, "Cannot load file: " + this.aJv);
         var8.printStackTrace();
         this.bFi = false;
      }
   }

   public boolean abH() {
      return this.bFi;
   }

   public boolean abI() {
      return this.bLS;
   }

   @NotNull
   public static YamlConfiguration method7293(@NotNull File var0, @Nullable Character var1) {
      Validate.notNull(var0, "File cannot be null");
      YamlConfiguration var2 = new YamlConfiguration();
      if (var1 != null) {
         var2.options().pathSeparator(var1);
      }

      try {
         YamlDuplicateKeyChecker.method7470(var2, var0);
      } catch (FileNotFoundException var4) {
      }

      return var2;
   }

   public File getFile() {
      return this.bMM;
   }

   public void reload() {
      this.bLM = YamlConfiguration.loadConfiguration(this.bMM);
   }

   public FileConfiguration abM() {
      return this.bLM;
   }

   public String GQ() {
      return this.bML;
   }

   public String DJ() {
      return this.aJv;
   }

   public void method7294(String var1, Object var2) {
      this.bLM.set(var1, var2);
      this.bLS = true;
   }

   public void abN() {
      if (this.bLS) {
         this.save();
      }
   }

   public void save() {
      try {
         this.bLM.save(this.bMM);
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }

   public void method7295(File var1) {
      try {
         this.bLM.save(var1);
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   public boolean jI(String var1) {
      return this.bLM.contains(var1);
   }

   public void jJ(String var1) {
      this.bLM.set(var1, null);
      this.bLS = true;
   }

   public Object method7296(String var1, Object var2) {
      return this.jI(var1) ? this.bLM.get(var1) : var2;
   }

   public String jO(String var1) {
      return this.bLM.getString(var1);
   }

   public String bc(String var1, String var2) {
      return !this.jI(var1) ? var2 : this.bLM.getString(var1);
   }

   public int jP(String var1) {
      return this.bLM.getInt(var1);
   }

   public int method7297(String var1, int var2) {
      return this.jI(var1) ? this.bLM.getInt(var1) : var2;
   }

   public double method7298(String var1, double var2) {
      return this.jI(var1) ? this.bLM.getDouble(var1) : var2;
   }

   public float method7299(String var1, float var2) {
      return this.jI(var1) ? (float)this.bLM.getDouble(var1) : var2;
   }

   public float jQ(String var1) {
      return this.method7299(var1, 0.0F);
   }

   public boolean kj(String var1) {
      return this.bLM.getBoolean(var1);
   }

   public Boolean method7300(String var1, Boolean var2) {
      return this.jI(var1) ? this.bLM.getBoolean(var1) : var2;
   }

   public char abP() {
      return this.abM().options().pathSeparator();
   }
}
