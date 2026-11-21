package dev.lone.itemsadder.entity;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.MaterialItemMatcher;
import dev.lone.itemsadder.item.NamespacedItemMatcher;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MobGoalSettings {
   private final String aat;
   private final Map aau;
   private static final Set aav = Set.of("enabled", "priority", "replace_vanilla", "conditions");
   private static final Set aaw = Set.of("prepare", "execute", "recover", "active");
   private static final Map aax = zp();

   public MobGoalSettings(String var1, Map var2) {
      if (var1 != null && aax.containsKey(var1)) {
         var2 = method154(Objects.requireNonNull(var2, "options"));
         this.aat = var1;
         this.aau = var2;
      } else {
         throw new IllegalArgumentException("unknown behaviour kind '" + var1 + "'");
      }
   }

   public Map zk() {
      return method154(this.aau);
   }

   public static Set zl() {
      return aax.keySet();
   }

   public static MobGoalSettings parse(String var0, ConfigurationSection var1, String var2) {
      Map var3 = var0 == null ? null : (Map)aax.get(var0);
      if (var3 == null) {
         throw new IllegalArgumentException("unknown behaviour kind '" + var0 + "'");
      }

      if (var1 == null) {
         throw new IllegalArgumentException("behaviour '" + var0 + "' must be a mapping");
      }

      Set var4 = var1.getKeys(false);

      for (String var6 : var4) {
         if (!aav.contains(var6) && !var3.containsKey(var6)) {
            throw new IllegalArgumentException("unknown " + var0 + " parameter '" + var6 + "'");
         }
      }

      LinkedHashMap var11 = new LinkedHashMap();

      for (Entry var7 : var3.entrySet()) {
         String var8 = (String)var7.getKey();
         MobConfigEntry var9 = (MobConfigEntry)var7.getValue();
         if (!var4.contains(var8) && var9.required()) {
            throw new IllegalArgumentException(var0 + "." + var8 + " is required");
         }

         Object var10 = var4.contains(var8) ? var1.get(var8) : var9.value();
         var11.put(var8, method136(var0, var8, var9, var10, var2));
      }

      if (var0.equals("work_block") && ((List)var11.get("produce_items")).isEmpty() && var11.get("replace_with").equals("unchanged")) {
         throw new IllegalArgumentException("work_block must produce items or replace_with a block");
      } else if (var0.equals("periodic_effect")
         && (Double)var11.get("damage") == 0.0
         && (Double)var11.get("heal_amount") == 0.0
         && ((List)var11.get("effects")).isEmpty()) {
         throw new IllegalArgumentException("periodic_effect must damage, heal, or apply effects");
      } else {
         return new MobGoalSettings(var0, var11);
      }
   }

   public boolean has(String var1) {
      return this.aau.containsKey(var1);
   }

   public Map zm() {
      return (Map)this.getValue("animations", Map.class);
   }

   public double getDouble(String var1) {
      return (Double)this.getValue(var1, Double.class);
   }

   public int getInt(String var1) {
      return (Integer)this.getValue(var1, Integer.class);
   }

   public boolean getBoolean(String var1) {
      return (Boolean)this.getValue(var1, Boolean.class);
   }

   public String getString(String var1) {
      return (String)this.getValue(var1, String.class);
   }

   public List getList(String var1) {
      return (List)this.getValue(var1, List.class);
   }

   public Set getSet(String var1) {
      return (Set)this.getValue(var1, Set.class);
   }

   public List getStringList(String var1) {
      ArrayList var2 = new ArrayList();

      for (ItemStack var4 : (List)this.getValue(var1, List.class)) {
         var2.add(var4.clone());
      }

      return List.copyOf(var2);
   }

   public List zn() {
      return (List)this.getValue("effects", List.class);
   }

   public Predicate zo() {
      List var1 = (List)this.getValue("items", List.class);
      return var1x -> var1.stream().anyMatch(var1xx -> var1xx.matches(var1x));
   }

   public static Predicate method134(Object var0) {
      List var1 = method138("conditions", "held_items", var0);
      return var1x -> var1.stream().anyMatch(var1xx -> var1xx.matches(var1x));
   }

   private Object getValue(String var1, Class var2) {
      Object var3 = this.aau.get(var1);
      if (!var2.isInstance(var3)) {
         throw new IllegalArgumentException(this.aat + " has no " + var2.getSimpleName() + " parameter '" + var1 + "'");
      } else {
         return var2.cast(var3);
      }
   }

   private static Object method136(String var0, String var1, MobConfigEntry var2, Object var3, String var4) {
      return switch (var2.type()) {
         case NUMBER -> {
            if (!(var3 instanceof Number var9) || !Double.isFinite(var9.doubleValue()) || var9.doubleValue() < var2.min() || var9.doubleValue() > var2.max()) {
               throw method153(var0, var1, "a finite number in [" + var2.min() + ", " + var2.max() + "]");
            }

            yield var9.doubleValue();
         }
         case INTEGER -> {
            if (!(var3 instanceof Number var8) || !Double.isFinite(var8.doubleValue()) || var8.doubleValue() != Math.rint(var8.doubleValue())) {
               throw method153(var0, var1, "an integer");
            }

            long var6 = var8.longValue();
            if (var6 < var2.min() || var6 > var2.max()) {
               throw method153(var0, var1, "an integer in [" + (long)var2.min() + ", " + (long)var2.max() + "]");
            }

            yield (int)var6;
         }
         case BOOLEAN -> {
            if (!(var3 instanceof Boolean)) {
               throw method153(var0, var1, "true or false");
            }

            yield var3;
         }
         case ENUM -> {
            String var5 = method150(var0, var1, var3);
            if (!var2.options().contains(var5)) {
               throw method153(var0, var1, "one of " + var2.options());
            }

            yield var5;
         }
         case ANIMATIONS -> method137(var0, var3);
         case ITEM_MATCHERS -> method138(var0, var1, var3);
         case MATERIALS -> method139(var0, var1, var3);
         case STRINGS -> method140(var0, var1, var3);
         case EFFECTS -> method141(var0, var1, var3);
         case ENTITY_TYPES -> method142(var0, var1, var3);
         case PROJECTILE -> method143(var0, var1, var3);
         case SUMMON_ENTITY -> method144(var0, var1, var3);
         case GROUP -> method145(var0, var1, var3, var4);
         case STACKS -> method146(var0, var1, var3);
         case BLOCK -> method147(var0, var1, var3);
      };
   }

   private static Map method137(String var0, Object var1) {
      Map var2 = method148(var0, "animations", var1);
      LinkedHashMap var3 = new LinkedHashMap();

      for (Entry var5 : var2.entrySet()) {
         String var6 = (String)var5.getKey();
         if (!aaw.contains(var6)) {
            throw method153(var0, "animations." + var6, "a supported animation key");
         }

         String var7 = method150(var0, "animations." + var6, var5.getValue());
         if (var7.isEmpty()) {
            throw method153(var0, "animations." + var6, "a nonempty string");
         }

         var3.put(var6, var7);
      }

      return Map.copyOf(var3);
   }

   private static List method138(String var0, String var1, Object var2) {
      List var3 = method149(var0, var1, var2);
      if (var3.size() > 64) {
         throw method153(var0, var1, "at most 64 entries");
      }

      if (var3.isEmpty()) {
         throw method153(var0, var1, "a nonempty list of item names");
      }

      ArrayList var4 = new ArrayList();

      for (Object var6 : var3) {
         String var7 = method150(var0, var1, var6);
         Material var8 = getMaterial(var7);
         if (var8 == null || var8.isAir()) {
            if (ContentManager.method8084() == null || !ContentManager.method8084().method8155(var7)) {
               throw method153(var0, var1, "a material or registered custom item");
            }

            var4.add(new NamespacedItemMatcher(var7));
         } else {
            var4.add(new MaterialItemMatcher(var8));
         }
      }

      return List.copyOf(var4);
   }

   private static Set method139(String var0, String var1, Object var2) {
      List var3 = method149(var0, var1, var2);
      if (var3.size() > 64) {
         throw method153(var0, var1, "at most 64 entries");
      }

      if (var3.isEmpty()) {
         throw method153(var0, var1, "a nonempty list of block materials");
      }

      LinkedHashSet var4 = new LinkedHashSet();

      for (Object var6 : var3) {
         String var7 = method150(var0, var1, var6);
         Material var8 = getMaterial(var7);
         if (var8 == null || !var8.isBlock()) {
            throw method153(var0, var1, "block materials");
         }

         var4.add(var8);
      }

      return Set.copyOf(var4);
   }

   private static List method140(String var0, String var1, Object var2) {
      List var3 = method149(var0, var1, var2);
      if (var3.size() > 64) {
         throw method153(var0, var1, "at most 64 strings");
      }

      ArrayList var4 = new ArrayList();

      for (Object var6 : var3) {
         String var7 = method150(var0, var1, var6);
         if (var7.length() > 128) {
            throw method153(var0, var1, "strings of at most 128 characters");
         }

         if (var7.isEmpty()) {
            throw method153(var0, var1, "nonempty strings");
         }

         var4.add(var7);
      }

      return List.copyOf(var4);
   }

   private static List method141(String var0, String var1, Object var2) {
      List var3 = ConfigSectionWrapper.method7083(var2, var0 + "." + var1);
      if (var3.size() > 128) {
         throw method153(var0, var1, "at most 128 effects");
      }

      ArrayList var4 = new ArrayList();
      Set var5 = Set.of("type", "duration", "amplifier");

      for (Object var7 : var3) {
         Map var8 = method148(var0, var1, var7);
         method152(var0, var1, var8, var5);
         String var9 = method150(var0, var1 + ".type", var8.get("type"));
         PotionEffectType var10 = PotionEffectType.getByName(var9.toUpperCase(Locale.ROOT));
         if (var10 == null) {
            throw method153(var0, var1 + ".type", "a known potion effect type");
         }

         int var11 = method151(var0, var1 + ".duration", var8.get("duration"), 1, 72000);
         int var12 = method151(var0, var1 + ".amplifier", var8.get("amplifier"), 0, 4);
         var4.add(new PotionEffect(var10, var11, var12, false, true, true));
      }

      return List.copyOf(var4);
   }

   private static List method142(String var0, String var1, Object var2) {
      List var3 = method149(var0, var1, var2);
      if (var3.size() > 64) {
         throw method153(var0, var1, "at most 64 entries");
      }

      ArrayList var4 = new ArrayList();

      for (Object var6 : var3) {
         String var7 = method150(var0, var1, var6);
         EntityType var8 = BukkitRegistryLookup.entityType(var7);
         if (var8 == null || !var8.isAlive()) {
            throw method153(var0, var1, "living entity types");
         }

         var4.add(var8.name());
      }

      return List.copyOf(var4);
   }

   private static String method143(String var0, String var1, Object var2) {
      String var3 = method150(var0, var1, var2);
      EntityType var4 = BukkitRegistryLookup.entityType(var3);
      if (var4 != null
         && var4.getEntityClass() != null
         && Set.of(
               "ARROW",
               "SPECTRAL_ARROW",
               "TRIDENT",
               "FIREBALL",
               "SMALL_FIREBALL",
               "WITHER_SKULL",
               "SHULKER_BULLET",
               "LLAMA_SPIT",
               "SNOWBALL",
               "EGG",
               "POTION",
               "SPLASH_POTION",
               "LINGERING_POTION"
            )
            .contains(var4.name())
         && Projectile.class.isAssignableFrom(var4.getEntityClass())) {
         return var4.name();
      } else {
         throw method153(var0, var1, "a projectile entity type");
      }
   }

   private static String method144(String var0, String var1, Object var2) {
      String var3 = method150(var0, var1, var2);
      EntityType var4 = BukkitRegistryLookup.entityType(var3);
      if (var4 != null && var4.isAlive() && var4 != EntityType.PLAYER && var4.getEntityClass() != null && Mob.class.isAssignableFrom(var4.getEntityClass())) {
         return var4.name();
      }

      if (CustomEntityManager.isInitialized()) {
         CustomMobSettings var5 = (CustomMobSettings)CustomEntityManager.getInstance().oN.get(var3);
         if (var5 != null && var5.vw != null && var5.vw.getEntityClass() != null && Mob.class.isAssignableFrom(var5.vw.getEntityClass())) {
            return var3;
         }
      }

      throw method153(var0, var1, "a living vanilla entity (except PLAYER) or registered custom entity");
   }

   private static String method145(String var0, String var1, Object var2, String var3) {
      String var4 = method150(var0, var1, var2);
      if (var4.equals("$group")) {
         var4 = var3;
      }

      if (var4 != null && !var4.isEmpty() && !var4.equals("$group")) {
         return var4;
      } else {
         throw method153(var0, var1, "a nonempty group name");
      }
   }

   private static List method146(String var0, String var1, Object var2) {
      List var3 = ConfigSectionWrapper.method7083(var2, var0 + "." + var1);
      if (var3.size() > 128) {
         throw method153(var0, var1, "at most 128 stacks");
      }

      ArrayList var4 = new ArrayList();
      Set var5 = Set.of("item", "amount");

      for (Object var7 : var3) {
         Map var8 = method148(var0, var1, var7);
         method152(var0, var1, var8, var5);
         String var9 = method150(var0, var1 + ".item", var8.get("item"));
         int var10 = method151(var0, var1 + ".amount", var8.get("amount"), 1, 64);
         Material var11 = getMaterial(var9);
         ItemStack var12;
         if (var11 != null && !var11.isAir()) {
            var12 = new ItemStack(var11);
         } else {
            ContentManager var13 = ContentManager.method8084();
            if (var13 == null || !var13.method8155(var9)) {
               throw method153(var0, var1 + ".item", "a material or registered custom item");
            }

            CustomItemDefinition var14 = var13.method8154(var9);
            if (var14 == null || var14.getType().isAir()) {
               throw method153(var0, var1 + ".item", "a registered non-air custom item");
            }

            var12 = var14.clone();
         }

         var12.setAmount(var10);
         var4.add(var12);
      }

      return List.copyOf(var4);
   }

   private static String method147(String var0, String var1, Object var2) {
      String var3 = method150(var0, var1, var2);
      if (var3.equals("unchanged")) {
         return var3;
      } else {
         Material var4 = getMaterial(var3);
         if (var4 != null && var4.isBlock()) {
            return var4.name();
         } else {
            throw method153(var0, var1, "'unchanged' or a block material");
         }
      }
   }

   private static Map method148(String var0, String var1, Object var2) {
      if (var2 instanceof ConfigurationSection var9) {
         return var9.getValues(false);
      } else if (var2 instanceof Map var3) {
         LinkedHashMap var4 = new LinkedHashMap();

         for (Entry var6 : var3.entrySet()) {
            if (!(var6.getKey() instanceof String var7)) {
               throw method153(var0, var1, "a mapping with string keys");
            }

            var4.put(var7, var6.getValue());
         }

         return var4;
      } else {
         throw method153(var0, var1, "a mapping");
      }
   }

   private static List method149(String var0, String var1, Object var2) {
      if (var2 instanceof List var3) {
         return var3;
      } else {
         throw method153(var0, var1, "a list");
      }
   }

   private static String method150(String var0, String var1, Object var2) {
      if (var2 instanceof String var3 && !var3.isEmpty()) {
         return var3;
      } else {
         throw method153(var0, var1, "a nonempty string");
      }
   }

   private static int method151(String var0, String var1, Object var2, int var3, int var4) {
      if (var2 instanceof Number var5 && Double.isFinite(var5.doubleValue()) && var5.doubleValue() == Math.rint(var5.doubleValue())) {
         long var6 = var5.longValue();
         if (var6 >= var3 && var6 <= var4) {
            return (int)var6;
         } else {
            throw method153(var0, var1, "an integer in [" + var3 + ", " + var4 + "]");
         }
      } else {
         throw method153(var0, var1, "an integer");
      }
   }

   private static void method152(String var0, String var1, Map var2, Set var3) {
      for (String var5 : var2.keySet()) {
         if (!var3.contains(var5)) {
            throw method153(var0, var1 + "." + var5, "a supported field");
         }
      }

      for (String var7 : var3) {
         if (!var2.containsKey(var7)) {
            throw method153(var0, var1 + "." + var7, "a required field");
         }
      }
   }

   private static Material getMaterial(String var0) {
      String var1 = var0;
      int var2 = var1.indexOf(58);
      if (var2 >= 0) {
         if (!var1.substring(0, var2).equalsIgnoreCase("minecraft")) {
            return null;
         }

         var1 = var1.substring(var2 + 1);
      }

      return Material.matchMaterial(var1.toUpperCase(Locale.ROOT));
   }

   private static IllegalArgumentException method153(String var0, String var1, String var2) {
      return new IllegalArgumentException(var0 + "." + var1 + " must be " + var2);
   }

   private static Map method154(Map var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      var0.forEach((var1x, var2) -> var1.put(var1x, method155(var2)));
      return Collections.unmodifiableMap(var1);
   }

   private static Object method155(Object var0) {
      if (var0 instanceof ItemStack var5) {
         return var5.clone();
      } else if (var0 instanceof Map var4) {
         LinkedHashMap var2 = new LinkedHashMap();
         var4.forEach((var1x, var2x) -> var2.put(var1x, method155(var2x)));
         return Collections.unmodifiableMap(var2);
      } else if (var0 instanceof List var3) {
         return var3.stream().map(MobGoalSettings::method155).toList();
      } else {
         return var0 instanceof Set var1 ? Set.copyOf(var1) : var0;
      }
   }

   private static Map zp() {
      try (InputStream var0 = MobGoalSettings.class.getResourceAsStream("/entity-behaviour-schema.json")) {
         if (var0 == null) {
            throw new IllegalStateException("missing /entity-behaviour-schema.json");
         } else if (!(new Gson().fromJson(JsonParser.parseString(new String(var0.readAllBytes(), StandardCharsets.UTF_8)), Object.class) instanceof Map var2)) {
            throw new IllegalStateException("behaviour schema root must be a mapping");
         } else {
            LinkedHashMap var3 = new LinkedHashMap();

            for (Entry var5 : var2.entrySet()) {
               if (!(var5.getKey() instanceof String var6) || !(var5.getValue() instanceof Map var7)) {
                  throw new IllegalStateException("invalid behaviour schema kind");
               }

               LinkedHashMap var33 = new LinkedHashMap();

               for (Entry var10 : var7.entrySet()) {
                  if (!(var10.getKey() instanceof String var11) || !(var10.getValue() instanceof Map var12)) {
                     throw new IllegalStateException("invalid behaviour schema parameter");
                  }

                  if (!(var12.get("type") instanceof String var14)) {
                     throw new IllegalStateException("missing schema type for " + var6 + "." + var11);
                  }

                  MobConfigValueType var15 = MobConfigValueType.valueOf(var14.toUpperCase(Locale.ROOT));
                  boolean var16 = Boolean.TRUE.equals(var12.get("required"));
                  Object var17 = var12.get("default");
                  if (var16 && var12.containsKey("default")) {
                     throw new IllegalStateException("required schema parameter has default: " + var6 + "." + var11);
                  }

                  double var18 = var12.get("min") instanceof Number var20 ? var20.doubleValue() : 0.0;
                  double var36 = var12.get("max") instanceof Number var22 ? var22.doubleValue() : 0.0;
                  LinkedHashSet var37 = new LinkedHashSet();
                  Object var24 = var12.get("values");
                  if (var24 instanceof List) {
                     for (Object var25 : (List)var24) {
                        if (!(var25 instanceof String var26)) {
                           throw new IllegalStateException("invalid schema enum value");
                        }

                        var37.add(var26);
                     }
                  }

                  var33.put(var11, new MobConfigEntry(var15, var17, var16, var18, var36, Set.copyOf(var37)));
               }

               var3.put(var6, Map.copyOf(var33));
            }

            return Map.copyOf(var3);
         }
      } catch (IOException | RuntimeException var29) {
         if (var29 instanceof IllegalStateException var1) {
            throw var1;
         } else {
            throw new IllegalStateException("could not load /entity-behaviour-schema.json", var29);
         }
      }
   }

   public String zq() {
      return this.aat;
   }
}
