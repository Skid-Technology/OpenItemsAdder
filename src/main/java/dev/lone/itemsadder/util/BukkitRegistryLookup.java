package dev.lone.itemsadder.util;

import java.util.Locale;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

public class BukkitRegistryLookup {
   public static boolean hasEnum(String var0, Class var1) {
      try {
         Enum.valueOf(var1, var0);
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   @Nullable
   public static Enum enumOrNull(String var0, Class var1) {
      return enumOrDefault(var0, var1, null);
   }

   public static Enum enumOrDefault(String var0, Class var1, Enum var2) {
      if (StringUtils.isBlank(var0)) {
         return var2;
      }

      try {
         return Enum.valueOf(var1, var0);
      } catch (Exception var4) {
         return var2;
      }
   }

   public static Enum enumOrDefaultWarn(String var0, Class var1, Enum var2, Runnable var3) {
      if (StringUtils.isBlank(var0)) {
         return var2;
      }

      try {
         return Enum.valueOf(var1, var0);
      } catch (Exception var5) {
         var3.run();
         return var2;
      }
   }

   public static SoundCategory soundCategoryOr(String var0, SoundCategory var1) {
      return var0 == null ? var1 : (SoundCategory)enumOrDefault(var0.toUpperCase(), SoundCategory.class, SoundCategory.MASTER);
   }

   public static SoundCategory soundCategory(String var0) {
      return soundCategoryOr(var0, SoundCategory.MASTER);
   }

   @Nullable
   public static ItemFlag itemFlag(String var0) {
      if (var0 == null) {
         return null;
      } else {
         return var0.equals("HIDE_POTION_EFFECTS") ? ItemFlag.HIDE_ADDITIONAL_TOOLTIP : (ItemFlag)enumOrNull(var0, ItemFlag.class);
      }
   }

   @Nullable
   public static Particle particle(@Nullable String var0) {
      if (var0 == null) {
         return null;
      }

      Particle var1 = switch (var0) {
         case "EXPLOSION_NORMAL" -> Particle.POOF;
         case "EXPLOSION_LARGE" -> Particle.EXPLOSION;
         case "EXPLOSION_HUGE" -> Particle.EXPLOSION_EMITTER;
         case "FIREWORKS_SPARK" -> Particle.FIREWORK;
         case "WATER_BUBBLE" -> Particle.BUBBLE;
         case "WATER_SPLASH" -> Particle.SPLASH;
         case "WATER_WAKE" -> Particle.FISHING;
         case "SUSPENDED" -> Particle.UNDERWATER;
         case "SUSPENDED_DEPTH" -> Particle.UNDERWATER;
         case "CRIT_MAGIC" -> Particle.ENCHANTED_HIT;
         case "SMOKE_NORMAL" -> Particle.SMOKE;
         case "SMOKE_LARGE" -> Particle.LARGE_SMOKE;
         case "SPELL" -> Particle.EFFECT;
         case "SPELL_INSTANT" -> Particle.INSTANT_EFFECT;
         case "SPELL_MOB" -> Particle.ENTITY_EFFECT;
         case "SPELL_WITCH" -> Particle.WITCH;
         case "DRIP_WATER" -> Particle.DRIPPING_WATER;
         case "DRIP_LAVA" -> Particle.DRIPPING_LAVA;
         case "VILLAGER_ANGRY" -> Particle.ANGRY_VILLAGER;
         case "VILLAGER_HAPPY" -> Particle.HAPPY_VILLAGER;
         case "TOWN_AURA" -> Particle.MYCELIUM;
         case "ENCHANTMENT_TABLE" -> Particle.ENCHANT;
         case "REDSTONE" -> Particle.DUST;
         case "SNOWBALL" -> Particle.ITEM_SNOWBALL;
         case "SNOW_SHOVEL" -> Particle.ITEM_SNOWBALL;
         case "SLIME" -> Particle.ITEM_SLIME;
         case "ITEM_CRACK" -> Particle.ITEM;
         case "BLOCK_CRACK" -> Particle.BLOCK;
         case "BLOCK_DUST" -> Particle.BLOCK;
         case "WATER_DROP" -> Particle.RAIN;
         case "MOB_APPEARANCE" -> Particle.ELDER_GUARDIAN;
         case "TOTEM" -> Particle.TOTEM_OF_UNDYING;
         case "GUST_EMITTER" -> Particle.GUST_EMITTER_LARGE;
         default -> (Particle)enumOrNull(var0, Particle.class);
      };
      if (var1 == null) {
         var0 = var0.toLowerCase();
         NamespacedKey var10 = NamespacedKey.fromString(var0);

         for (Particle var6 : Particle.values()) {
            try {
               if (var6.getKey().equals(var10)) {
                  return var6;
               }
            } catch (UnsupportedOperationException var8) {
            }
         }
      }

      return var1;
   }

   @Nullable
   public static Object particleDataFor(Particle var0) {
      Class var1 = var0.getDataType();
      if (var1 == Void.class) {
         return null;
      }

      return switch (var0.name()) {
         case "DRAGON_BREATH" -> var1 == Float.class ? 1.0F : null;
         case "FLASH" -> var1 == Color.class ? Color.WHITE : null;
         case "EFFECT", "INSTANT_EFFECT" -> createSpellData(var1);
         default -> null;
      };
   }

   @Nullable
   private static Object createSpellData(Class var0) {
      if (!var0.getName().equals("org.bukkit.Particle$Spell")) {
         return null;
      }

      try {
         return var0.getConstructor(Color.class, float.class).newInstance(Color.WHITE, 1.0F);
      } catch (ReflectiveOperationException var2) {
         return null;
      }
   }

   @Nullable
   public static Effect effect(@Nullable String var0) {
      Effect var1 = (Effect)enumOrNull(var0, Effect.class);
      return var1 == null && var0 != null ? (Effect)enumOrNull(mapLegacyEffectName(var0), Effect.class) : var1;
   }

   private static String mapLegacyEffectName(String var0) {
      return switch (var0) {
         case "CLICK2" -> "DISPENSER_DISPENSE";
         case "CLICK1" -> "DISPENSER_FAIL";
         case "BOW_FIRE" -> "DISPENSER_PROJECTILE_LAUNCH";
         case "SOUND_STOP_JUKEBOX_SONG" -> "RECORD_STOP";
         case "ENDERDRAGON_SHOOT" -> "ENDER_DRAGON_SHOOT";
         case "ZOMBIE_CONVERTED_VILLAGER" -> "ZOMBIE_CONVERTED_TO_VILLAGER";
         case "SOUND_WITH_CHARGE_SHOT" -> "WIND_CHARGE_SHOOT";
         case "DRAGON_BREATH" -> "ENDER_DRAGON_BREATH";
         case "VILLAGER_PLANT_GROW" -> "BEE_GROWTH";
         case "SMOKE" -> "SMOKE_SHOOT";
         case "SHOOT_WHITE_SMOKE" -> "WHITE_SMOKE_SHOOT";
         case "ENDERDRAGON_GROWL" -> "ENDER_DRAGON_GROWL";
         case "PARTICLES_SCULK_CHARGE" -> "SCULK_CHARGE";
         case "PARTICLES_SCULK_SHRIEK" -> "SCULK_SHRIEK";
         case "PARTICLES_AND_SOUND_BRUSH_BLOCK_COMPLETE" -> "BRUSH_BLOCK_COMPLETE";
         case "PARTICLES_EGG_CRACK" -> "EGG_CRACK";
         default -> var0;
      };
   }

   public static Material material(String var0) {
      var0 = var0.toUpperCase(Locale.ROOT);
      return var0.equals("GRASS") ? Material.SHORT_GRASS : Material.matchMaterial(var0);
   }

   @Nullable
   public static EntityType entityType(String var0) {
      var0 = var0.toUpperCase(Locale.ROOT);
      EntityType var1 = (EntityType)enumOrNull(var0, EntityType.class);
      if (var1 != null) {
         return var1;
      }

      var0 = var0.toLowerCase(Locale.ROOT);
      if (var0.startsWith("minecraft:")) {
         var0 = var0.substring("minecraft:".length());
      }

      return EntityType.fromName(var0);
   }

   public static PotionEffectType potionEffectType(String var0) {
      PotionEffectType var1 = PotionEffectType.getByName(var0);
      if (var1 == null) {
         return switch (var0) {
            case "SLOW" -> PotionEffectType.SLOWNESS;
            case "FAST_DIGGING" -> PotionEffectType.HASTE;
            case "SLOW_DIGGING" -> PotionEffectType.MINING_FATIGUE;
            case "INCREASE_DAMAGE" -> PotionEffectType.STRENGTH;
            case "HEAL" -> PotionEffectType.INSTANT_HEALTH;
            case "HARM" -> PotionEffectType.INSTANT_DAMAGE;
            case "JUMP" -> PotionEffectType.JUMP_BOOST;
            case "CONFUSION" -> PotionEffectType.NAUSEA;
            case "DAMAGE_RESISTANCE" -> PotionEffectType.RESISTANCE;
            default -> null;
         };
      } else {
         return var1;
      }
   }

   public static Enchantment enchantment(String var0) {
      Enchantment var1 = Enchantment.getByName(var0);
      if (var1 == null && var0.equals("ARRROW_DAMAGE")) {
         return Enchantment.FLAME;
      }

      if (var1 == null && var0.toLowerCase().equals(var0)) {
         var1 = Enchantment.getByKey(NamespacedKey.minecraft(var0));
      }

      return var1;
   }

   @Nullable
   public static EquipmentSlot equipmentSlot(@Nullable String var0) {
      if (var0 == null) {
         return null;
      } else {
         EquipmentSlot var1 = (EquipmentSlot)enumOrDefault(var0, EquipmentSlot.class, null);
         if (var1 != null) {
            return var1;
         } else if (var0.equalsIgnoreCase("main_hand")) {
            return EquipmentSlot.HAND;
         } else if (var0.equalsIgnoreCase("mainhand")) {
            return EquipmentSlot.HAND;
         } else if (var0.equalsIgnoreCase("offhand")) {
            return EquipmentSlot.OFF_HAND;
         } else if (var0.equalsIgnoreCase("helmet")) {
            return EquipmentSlot.HEAD;
         } else if (var0.equalsIgnoreCase("chestplate")) {
            return EquipmentSlot.CHEST;
         } else if (var0.equalsIgnoreCase("leggings")) {
            return EquipmentSlot.LEGS;
         } else {
            return var0.equalsIgnoreCase("boots") ? EquipmentSlot.FEET : null;
         }
      }
   }

   @Nullable
   public static BarStyle barStyle(String var0) {
      return (BarStyle)enumOrDefault(var0.toUpperCase(), BarStyle.class, null);
   }

   public static BarColor barColor(String var0) {
      return (BarColor)enumOrDefault(var0.toUpperCase(), BarColor.class, null);
   }

   static {
      assert mapLegacyEffectName("SMOKE").equals("SMOKE_SHOOT");
      assert mapLegacyEffectName("DRAGON_BREATH").equals("ENDER_DRAGON_BREATH");
   }
}
