package dev.lone.itemsadder.config;

import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigSectionWrapper {
   public final ConfigurationSection bLU;

   public ConfigSectionWrapper(ConfigurationSection var1) {
      this.bLU = var1;
   }

   public ConfigSectionWrapper(YamlConfig var1, String var2) {
      this.bLU = var1.kb(var2);
   }

   @NotNull
   public Set bJ(boolean var1) {
      return this.bLU.getKeys(var1);
   }

   @NotNull
   public Map bK(boolean var1) {
      return this.bLU.getValues(var1);
   }

   public boolean hA(@NotNull String var1) {
      return this.bLU.contains(var1);
   }

   public boolean method7072(@NotNull String var1, boolean var2) {
      return this.bLU.contains(var1, var2);
   }

   public boolean ke(@NotNull String var1) {
      return this.bLU.isSet(var1);
   }

   @Nullable
   public String abQ() {
      return this.bLU.getCurrentPath();
   }

   @NotNull
   public String getName() {
      return this.bLU.getName();
   }

   @Nullable
   public Configuration abR() {
      return this.bLU.getRoot();
   }

   @Nullable
   public ConfigurationSection abS() {
      return this.bLU.getParent();
   }

   @Nullable
   public Object kf(@NotNull String var1) {
      return this.bLU.get(var1);
   }

   @Nullable
   public Object method7073(@NotNull String var1, @Nullable Object var2) {
      return this.bLU.get(var1, var2);
   }

   public void method7074(@NotNull String var1, @Nullable Object var2) {
      this.bLU.set(var1, var2);
   }

   @NotNull
   public ConfigurationSection kg(@NotNull String var1) {
      return this.bLU.createSection(var1);
   }

   @NotNull
   public ConfigurationSection method7075(@NotNull String var1, @NotNull Map var2) {
      return this.bLU.createSection(var1, var2);
   }

   @Nullable
   public String jO(@NotNull String var1) {
      return this.bLU.getString(var1);
   }

   @Nullable
   public String bc(@NotNull String var1, @Nullable String var2) {
      return this.bLU.getString(var1, var2);
   }

   public String bf(@NotNull String var1, @NotNull String var2) {
      return this.bLU.getString(var1, var2);
   }

   public boolean kh(@NotNull String var1) {
      return this.bLU.isString(var1);
   }

   public int jP(@NotNull String var1) {
      return this.bLU.getInt(var1);
   }

   public int method7076(@NotNull String var1, int var2) {
      return this.bLU.getInt(var1, var2);
   }

   public Integer method7077(@NotNull String var1, @Nullable Integer var2) {
      return this.bLU.contains(var1) ? this.bLU.getInt(var1) : var2;
   }

   public boolean ki(@NotNull String var1) {
      return this.bLU.isInt(var1);
   }

   public boolean kj(@NotNull String var1) {
      return this.bLU.getBoolean(var1);
   }

   public boolean method7078(@NotNull String var1, boolean var2) {
      return this.bLU.getBoolean(var1, var2);
   }

   public boolean method7079(@NotNull String var1) {
      return this.bLU.isBoolean(var1);
   }

   public double kl(@NotNull String var1) {
      return this.bLU.getDouble(var1);
   }

   public double method7080(@NotNull String var1, double var2) {
      return this.bLU.getDouble(var1, var2);
   }

   public boolean km(@NotNull String var1) {
      return this.bLU.isDouble(var1);
   }

   public long kn(@NotNull String var1) {
      return this.bLU.getLong(var1);
   }

   public long method7081(@NotNull String var1, long var2) {
      return this.bLU.getLong(var1, var2);
   }

   public boolean ko(@NotNull String var1) {
      return this.bLU.isLong(var1);
   }

   @Nullable
   public List kp(@NotNull String var1) {
      return this.bLU.getList(var1);
   }

   @Nullable
   public List method7082(@NotNull String var1, @Nullable List var2) {
      return this.bLU.getList(var1, var2);
   }

   public boolean kq(@NotNull String var1) {
      return this.bLU.isList(var1);
   }

   @NotNull
   public List jZ(@NotNull String var1) {
      return this.bLU.getStringList(var1);
   }

   @NotNull
   public List kr(@NotNull String var1) {
      return this.bLU.getIntegerList(var1);
   }

   @NotNull
   public List ks(@NotNull String var1) {
      return this.bLU.getBooleanList(var1);
   }

   @NotNull
   public List kt(@NotNull String var1) {
      return this.bLU.getDoubleList(var1);
   }

   @NotNull
   public List ku(@NotNull String var1) {
      return this.bLU.getFloatList(var1);
   }

   @NotNull
   public List kv(@NotNull String var1) {
      return this.bLU.getLongList(var1);
   }

   @NotNull
   public List kw(@NotNull String var1) {
      return this.bLU.getByteList(var1);
   }

   @NotNull
   public List kx(@NotNull String var1) {
      return this.bLU.getCharacterList(var1);
   }

   @NotNull
   public List ky(@NotNull String var1) {
      return this.bLU.getShortList(var1);
   }

   public static List method7083(Object var0, String var1) {
      if (var0 instanceof ConfigurationSection var2) {
         var0 = var2.getValues(false);
      }

      if (!(var0 instanceof Map var3)) {
         throw new IllegalArgumentException(var1 + " must be named sections");
      } else {
         Iterator var5 = var3.keySet().iterator();

         while (var5.hasNext()) {
            if (!(var5.next() instanceof String var4) || !var4.matches("[A-Za-z0-9_-]{1,64}")) {
               throw new IllegalArgumentException(var1 + " entry names must use 1-64 letters, digits, underscores or hyphens");
            }
         }

         Collection var13 = var3.values();
         ArrayList var14 = new ArrayList();

         for (Object var16 : var13) {
            if (var16 instanceof ConfigurationSection var7) {
               var16 = var7.getValues(false);
            }

            if (!(var16 instanceof Map var17)) {
               throw new IllegalArgumentException(var1 + " entries must be mappings");
            }

            LinkedHashMap var8 = new LinkedHashMap();

            for (Entry var10 : var17.entrySet()) {
               if (!(var10.getKey() instanceof String var11)) {
                  throw new IllegalArgumentException(var1 + " entry fields must be strings");
               }

               var8.put(var11, var10.getValue());
            }

            var14.add(var8);
         }

         return List.copyOf(var14);
      }
   }

   @NotNull
   public List kz(@NotNull String var1) {
      return this.bLU.getMapList(var1);
   }

   @Nullable
   public Object method7084(@NotNull String var1, @NotNull Class var2) {
      return this.bLU.getObject(var1, var2);
   }

   @Nullable
   public Object method7085(@NotNull String var1, @NotNull Class var2, @Nullable Object var3) {
      return this.bLU.getObject(var1, var2, var3);
   }

   @Nullable
   public ConfigurationSerializable method7086(@NotNull String var1, @NotNull Class var2) {
      return this.bLU.getSerializable(var1, var2);
   }

   @Nullable
   public ConfigurationSerializable method7087(@NotNull String var1, @NotNull Class var2, @Nullable ConfigurationSerializable var3) {
      return this.bLU.getSerializable(var1, var2, var3);
   }

   @Nullable
   public Vector kA(@NotNull String var1) {
      return this.bLU.getVector(var1);
   }

   @Nullable
   public Vector method7088(@NotNull String var1, @Nullable Vector var2) {
      return this.bLU.getVector(var1, var2);
   }

   public boolean kB(@NotNull String var1) {
      return this.bLU.isVector(var1);
   }

   @Nullable
   public OfflinePlayer kC(@NotNull String var1) {
      return this.bLU.getOfflinePlayer(var1);
   }

   @Nullable
   public OfflinePlayer method7089(@NotNull String var1, @Nullable OfflinePlayer var2) {
      return this.bLU.getOfflinePlayer(var1, var2);
   }

   public boolean kD(@NotNull String var1) {
      return this.bLU.isOfflinePlayer(var1);
   }

   @Nullable
   public ItemStack kE(@NotNull String var1) {
      return this.bLU.getItemStack(var1);
   }

   @Nullable
   public ItemStack method7090(@NotNull String var1, @Nullable ItemStack var2) {
      return this.bLU.getItemStack(var1, var2);
   }

   public boolean kF(@NotNull String var1) {
      return this.bLU.isItemStack(var1);
   }

   @Nullable
   public Color kG(@NotNull String var1) {
      return this.bLU.getColor(var1);
   }

   @Nullable
   public Color method7091(@NotNull String var1, @Nullable Color var2) {
      return this.bLU.getColor(var1, var2);
   }

   public boolean kH(@NotNull String var1) {
      return this.bLU.isColor(var1);
   }

   @Nullable
   public Location kI(@NotNull String var1) {
      return this.bLU.getLocation(var1);
   }

   @Nullable
   public Location method7092(@NotNull String var1, @Nullable Location var2) {
      return this.bLU.getLocation(var1, var2);
   }

   public boolean kJ(@NotNull String var1) {
      return this.bLU.isLocation(var1);
   }

   @Nullable
   public ConfigurationSection kb(@NotNull String var1) {
      return this.bLU.getConfigurationSection(var1);
   }

   @Nullable
   public ConfigSectionWrapper kc(@NotNull String var1) {
      ConfigurationSection var2 = this.kb(var1);
      return var2 == null ? null : new ConfigSectionWrapper(var2);
   }

   public boolean kK(@NotNull String var1) {
      return this.bLU.isConfigurationSection(var1);
   }

   @Nullable
   public ConfigurationSection abT() {
      return this.bLU.getDefaultSection();
   }

   public void method7093(@NotNull String var1, @Nullable Object var2) {
      this.bLU.addDefault(var1, var2);
   }

   @NotNull
   public List kL(@NotNull String var1) {
      return this.bLU.getComments(var1);
   }

   @NotNull
   public List kM(@NotNull String var1) {
      return this.bLU.getInlineComments(var1);
   }

   public void method7094(@NotNull String var1, @Nullable List var2) {
      this.bLU.setComments(var1, var2);
   }

   public void method7095(@NotNull String var1, @Nullable List var2) {
      this.bLU.setInlineComments(var1, var2);
   }

   public Enum method7096(String var1, Class var2) {
      return YamlConfig.method7064(this.bLU, var1, var2, null);
   }

   public Enum method7097(String var1, Class var2, Enum var3) {
      return YamlConfig.method7064(this.bLU, var1, var2, var3);
   }

   public Enum method7098(String var1, Class var2, Enum var3, Consumer var4) {
      String var5 = this.bLU.getString(var1);
      Enum var6 = var5 != null ? BukkitRegistryLookup.enumOrDefault(var5.toUpperCase(), var2, var3) : var3;
      if (var6 == null) {
         var4.accept(var5);
      }

      return var6;
   }

   public List kN(String var1) {
      return this.method7099(var1, false);
   }

   @NotNull
   public List method7099(String var1, boolean var2) {
      ConfigurationSection var3 = this.bLU.getConfigurationSection(var1);
      return var3 == null ? new ArrayList() : new ArrayList(var3.getKeys(var2));
   }

   @NotNull
   public List bL(boolean var1) {
      return new ArrayList(this.bLU.getKeys(var1));
   }

   public boolean jI(String var1) {
      return this.bLU.contains(var1);
   }

   @Nullable
   public Long method7100(@NotNull String var1, @Nullable Long var2) {
      return this.jI(var1) ? this.bLU.getLong(var1) : var2;
   }

   @Nullable
   public Float method7101(String var1, @Nullable Float var2) {
      return this.jI(var1) ? (float)this.bLU.getDouble(var1) : var2;
   }

   public float method7102(String var1, float var2) {
      return this.jI(var1) ? (float)this.bLU.getDouble(var1) : var2;
   }

   public float jQ(String var1) {
      return this.method7102(var1, 0.0F);
   }

   @Nullable
   public Double method7103(String var1, @Nullable Double var2) {
      return this.jI(var1) ? this.bLU.getDouble(var1) : var2;
   }

   @Nullable
   public Boolean method7104(String var1, @Nullable Boolean var2) {
      return this.jI(var1) ? this.bLU.getBoolean(var1) : var2;
   }

   public Material method7105(String var1, Material var2) {
      return this.jI(var1) ? (Material)BukkitRegistryLookup.enumOrDefault(this.bLU.getString(var1).toUpperCase(), Material.class, var2) : var2;
   }

   public static Material method7106(String var0, Material var1) {
      return YamlConfig.method7060(var0, var1);
   }

   public Material jS(String var1) {
      return this.method7105(var1, null);
   }

   @Nullable
   public Material method7107(String var1, Material var2, @Nullable Consumer var3) {
      String var4 = this.jO(var1);
      if (var4 != null) {
         var4 = var4.toUpperCase();
         Material var5 = (Material)BukkitRegistryLookup.enumOrDefault(var4, Material.class, null);
         if (var5 == null) {
            if (var3 != null) {
               var3.accept(var4);
            }

            return var2;
         } else {
            return var5;
         }
      } else {
         return var2;
      }
   }

   public List abO() {
      return this.method7108("", false);
   }

   public List jK(String var1) {
      return this.method7108(var1, false);
   }

   public List method7108(String var1, boolean var2) {
      return !this.jI(var1) ? new ArrayList() : new ArrayList(this.bLU.getConfigurationSection(var1).getKeys(var2));
   }

   public List kO(String var1) {
      ArrayList var2 = new ArrayList();

      try {
         for (String var5 : (ArrayList)this.bLU.getStringList(var1)) {
            var2.add(Utils.toSectionCodes(var5));
         }
      } catch (NullPointerException var6) {
      }

      return var2;
   }

   @Nullable
   public EntityType method7109(String var1, EntityType var2, @Nullable Consumer var3) {
      if (this.jI(var1)) {
         String var4 = this.jO(var1).toUpperCase();

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

   public List method7110(Class var1, String var2) {
      return this.method7111(var1, var2, null);
   }

   public List method7111(Class var1, String var2, @Nullable List var3) {
      ArrayList var4 = new ArrayList();
      if (!this.jI(var2)) {
         return var4;
      }

      List var5 = this.jZ(var2);

      for (int var6 = 0; var6 < var5.size(); var6++) {
         String var7 = ((String)var5.get(var6)).toUpperCase();

         try {
            var4.add(BukkitRegistryLookup.enumOrNull(var7, var1));
         } catch (Exception var9) {
            if (var3 != null) {
               var3.add(var7);
            }

            var5.remove(var6);
            var6--;
         }
      }

      return var4;
   }

   @NotNull
   public List kP(String var1) {
      ArrayList var2 = new ArrayList();
      if (!this.jI(var1)) {
         return var2;
      }

      for (String var5 : this.jZ(var1)) {
         EntityType var6 = (EntityType)BukkitRegistryLookup.enumOrNull(var5.toUpperCase(), EntityType.class);
         if (var6 != null) {
            var2.add(var6);
         } else {
            var5 = var5.toLowerCase();
            if (var5.startsWith("minecraft:")) {
               var5 = var5.replaceAll("minecraft:", "");
            }

            EntityType var7 = EntityType.fromName(var5);
            if (var7 != null) {
               var2.add(var7);
            } else {
               Log.error("[Items] Invalid vanilla entity type '" + var5 + "' in " + var1 + " in yml file: " + this.abQ());
            }
         }
      }

      return var2;
   }

   @Nullable
   public EntityType[] kQ(String var1) {
      List var2 = this.kP(var1);
      return var2.isEmpty() ? null : var2.toArray(new EntityType[0]);
   }
}
