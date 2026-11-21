package dev.lone.itemsadder.item.conversion;

import com.google.common.base.Splitter;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.util.Mth;
import net.minecraft.util.datafix.ExtraDataFixUtils;
import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class ItemDataFixK extends DataFix {
   private static final Set bbv = Set.of("minecraft:potion", "minecraft:splash_potion", "minecraft:lingering_potion", "minecraft:tipped_arrow");
   private static final Set bbw = Set.of(
      "minecraft:pufferfish_bucket",
      "minecraft:salmon_bucket",
      "minecraft:cod_bucket",
      "minecraft:tropical_fish_bucket",
      "minecraft:axolotl_bucket",
      "minecraft:tadpole_bucket"
   );
   private static final List bbx = List.of(
      "NoAI", "Silent", "NoGravity", "Glowing", "Invulnerable", "Health", "Age", "Variant", "HuntingCooldown", "BucketVariantTag"
   );
   private static final Set bby = Set.of(
      "attached",
      "bottom",
      "conditional",
      "disarmed",
      "drag",
      "enabled",
      "extended",
      "eye",
      "falling",
      "hanging",
      "has_bottle_0",
      "has_bottle_1",
      "has_bottle_2",
      "has_record",
      "has_book",
      "inverted",
      "in_wall",
      "lit",
      "locked",
      "occupied",
      "open",
      "persistent",
      "powered",
      "short",
      "signal_fire",
      "snowy",
      "triggered",
      "unstable",
      "waterlogged",
      "berries",
      "bloom",
      "shrieking",
      "can_summon",
      "up",
      "down",
      "north",
      "east",
      "south",
      "west",
      "slot_0_occupied",
      "slot_1_occupied",
      "slot_2_occupied",
      "slot_3_occupied",
      "slot_4_occupied",
      "slot_5_occupied",
      "cracked",
      "crafting"
   );
   private static final Splitter bbz = Splitter.on(',');

   public ItemDataFixK(Schema var1) {
      super(var1, true);
   }

   public static void method4222(ItemConversionNodeK var0, Dynamic var1) {
      int var2 = var0.fa("HideFlags").asInt(0);
      var0.method4276("Damage", "minecraft:damage", var1.createInt(0));
      var0.method4276("RepairCost", "minecraft:repair_cost", var1.createInt(0));
      var0.method4277("CustomModelData", "minecraft:custom_model_data");
      var0.fa("BlockStateTag").result().ifPresent(var1x -> var0.method4273("minecraft:block_state", method4223(var1x)));
      var0.method4277("EntityTag", "minecraft:entity_data");
      var0.method4278("BlockEntityTag", false, var1x -> {
         String var2x = NamespacedSchema.ensureNamespaced(var1x.get("id").asString(""));
         var1x = method4225(var0, var1x, var2x);
         Dynamic var3x = var1x.remove("id");
         return var3x.equals(var1x.emptyMap()) ? var3x : var1x;
      });
      var0.method4277("BlockEntityTag", "minecraft:block_entity_data");
      if (var0.fa("Unbreakable").asBoolean(false)) {
         Dynamic var3 = var1.emptyMap();
         if ((var2 & 4) != 0) {
            var3 = var3.set("show_in_tooltip", var1.createBoolean(false));
         }

         var0.method4273("minecraft:unbreakable", var3);
      }

      method4226(var0, var1, "Enchantments", "minecraft:enchantments", (var2 & 1) != 0);
      if (var0.fb("minecraft:enchanted_book")) {
         method4226(var0, var1, "StoredEnchantments", "minecraft:stored_enchantments", (var2 & 32) != 0);
      }

      var0.method4278("display", false, var2x -> method4224(var0, var2x, var2));
      method4228(var0, var1, var2);
      method4231(var0, var1, var2);
      Optional var5 = var0.fa("Trim").result();
      if (var5.isPresent()) {
         Dynamic var4 = (Dynamic)var5.get();
         if ((var2 & 128) != 0) {
            var4 = var4.set("show_in_tooltip", var4.createBoolean(false));
         }

         var0.method4273("minecraft:trim", var4);
      }

      if ((var2 & 32) != 0) {
         var0.method4273("minecraft:hide_additional_tooltip", var1.emptyMap());
      }

      if (var0.fb("minecraft:crossbow")) {
         var0.fa("Charged");
         var0.method4276("ChargedProjectiles", "minecraft:charged_projectiles", var1.createList(Stream.empty()));
      }

      if (var0.fb("minecraft:bundle")) {
         var0.method4276("Items", "minecraft:bundle_contents", var1.createList(Stream.empty()));
      }

      if (var0.fb("minecraft:filled_map")) {
         var0.method4277("map", "minecraft:map_id");
         Map var6 = var0.fa("Decorations")
            .asStream()
            .map(ItemDataFixK::method4233)
            .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond, (var0x, var1x) -> var0x));
         if (!var6.isEmpty()) {
            var0.method4273("minecraft:map_decorations", var1.createMap(var6));
         }
      }

      if (var0.method4280(bbv)) {
         method4234(var0, var1);
      }

      if (var0.fb("minecraft:writable_book")) {
         method4235(var0, var1);
      }

      if (var0.fb("minecraft:written_book")) {
         method4236(var0, var1);
      }

      if (var0.fb("minecraft:suspicious_stew")) {
         var0.method4277("effects", "minecraft:suspicious_stew_effects");
      }

      if (var0.fb("minecraft:debug_stick")) {
         var0.method4277("DebugProperty", "minecraft:debug_stick_state");
      }

      if (var0.method4280(bbw)) {
         method4239(var0, var1);
      }

      if (var0.fb("minecraft:goat_horn")) {
         var0.method4277("instrument", "minecraft:instrument");
      }

      if (var0.fb("minecraft:knowledge_book")) {
         var0.method4277("Recipes", "minecraft:recipes");
      }

      if (var0.fb("minecraft:compass")) {
         method4240(var0, var1);
      }

      if (var0.fb("minecraft:firework_rocket")) {
         method4242(var0);
      }

      if (var0.fb("minecraft:firework_star")) {
         method4241(var0);
      }

      if (var0.fb("minecraft:player_head")) {
         var0.fa("SkullOwner").result().ifPresent(var1x -> var0.method4273("minecraft:profile", method4244(var1x)));
      }
   }

   private static Dynamic method4223(Dynamic var0) {
      Optional var1 = var0.asMapOpt().result().map(var0x -> var0x.collect(Collectors.toMap(Pair::getFirst, var0xx -> {
         String var1x = ((Dynamic)var0xx.getFirst()).asString("");
         Dynamic var2 = (Dynamic)var0xx.getSecond();
         if (bby.contains(var1x)) {
            Optional var3 = var2.asBoolean().result();
            if (var3.isPresent()) {
               return var2.createString(String.valueOf(var3.get()));
            }
         }

         Optional var4 = var2.asNumber().result();
         return var4.isPresent() ? var2.createString(((Number)var4.get()).toString()) : var2;
      })));
      return (Dynamic)DataFixUtils.orElse(var1.map(var1x -> var0.createMap((Map)var1x)), var0);
   }

   private static Dynamic method4224(ItemConversionNodeK var0, Dynamic var1, int var2) {
      var0.method4274("minecraft:custom_name", var1.get("Name"));
      var0.method4274("minecraft:lore", var1.get("Lore"));
      Optional var3 = var1.get("color").asNumber().result().map(Number::intValue);
      boolean var4 = (var2 & 64) != 0;
      if (var3.isPresent() || var4) {
         Dynamic var5 = var1.emptyMap().set("rgb", var1.createInt(var3.orElse(10511680)));
         if (var4) {
            var5 = var5.set("show_in_tooltip", var1.createBoolean(false));
         }

         var0.method4273("minecraft:dyed_color", var5);
      }

      Optional var6 = var1.get("LocName").asString().result();
      if (var6.isPresent()) {
         var0.method4273("minecraft:item_name", LegacyComponentDataFixUtils.createTranslatableComponent(var1.getOps(), (String)var6.get()));
      }

      if (var0.fb("minecraft:filled_map")) {
         var0.method4274("minecraft:map_color", var1.get("MapColor"));
         var1 = var1.remove("MapColor");
      }

      return var1.remove("Name").remove("Lore").remove("color").remove("LocName");
   }

   private static Dynamic method4225(ItemConversionNodeK var0, Dynamic var1, String var2) {
      var0.method4274("minecraft:lock", var1.get("Lock"));
      var1 = var1.remove("Lock");
      Optional var3 = var1.get("LootTable").result();
      if (var3.isPresent()) {
         Dynamic var4 = var1.emptyMap().set("loot_table", (Dynamic)var3.get());
         long var5 = var1.get("LootTableSeed").asLong(0L);
         if (var5 != 0L) {
            var4 = var4.set("seed", var1.createLong(var5));
         }

         var0.method4273("minecraft:container_loot", var4);
         var1 = var1.remove("LootTable").remove("LootTableSeed");
      }
      return switch (var2) {
         case "minecraft:skull" -> {
            var0.method4274("minecraft:note_block_sound", var1.get("note_block_sound"));
            yield var1.remove("note_block_sound");
         }
         case "minecraft:decorated_pot" -> {
            var0.method4274("minecraft:pot_decorations", var1.get("sherds"));
            Optional var12 = var1.get("item").result();
            if (var12.isPresent()) {
               var0.method4273(
                  "minecraft:container", var1.createList(Stream.of(var1.emptyMap().set("slot", var1.createInt(0)).set("item", (Dynamic)var12.get())))
               );
            }

            yield var1.remove("sherds").remove("item");
         }
         case "minecraft:banner" -> {
            var0.method4274("minecraft:banner_patterns", var1.get("patterns"));
            Optional var11 = var1.get("Base").asNumber().result();
            if (var11.isPresent()) {
               var0.method4273("minecraft:base_color", var1.createString(ExtraDataFixUtils.dyeColorIdToName(((Number)var11.get()).intValue())));
            }

            yield var1.remove("patterns").remove("Base");
         }
         case "minecraft:shulker_box", "minecraft:chest", "minecraft:trapped_chest", "minecraft:furnace", "minecraft:ender_chest", "minecraft:dispenser", "minecraft:dropper", "minecraft:brewing_stand", "minecraft:hopper", "minecraft:barrel", "minecraft:smoker", "minecraft:blast_furnace", "minecraft:campfire", "minecraft:chiseled_bookshelf", "minecraft:crafter" -> {
            List var8 = var1.get("Items")
               .asList(var0x -> var0x.emptyMap().set("slot", var0x.createInt(var0x.get("Slot").asByte((byte)0) & 255)).set("item", var0x.remove("Slot")));
            if (!var8.isEmpty()) {
               var0.method4273("minecraft:container", var1.createList(var8.stream()));
            }

            yield var1.remove("Items");
         }
         case "minecraft:beehive" -> {
            var0.method4274("minecraft:bees", var1.get("bees"));
            yield var1.remove("bees");
         }
         default -> var1;
      };
   }

   private static void method4226(ItemConversionNodeK var0, Dynamic var1, String var2, String var3, boolean var4) {
      OptionalDynamic var5 = var0.fa(var2);
      List var6 = var5.asList(Function.identity()).stream().flatMap(var0x -> method4227(var0x).stream()).toList();
      if (!var6.isEmpty() || var4) {
         Dynamic var7 = var1.emptyMap();
         Dynamic var8 = var1.emptyMap();

         for (Pair var9 : var6) {
            var8 = var8.set((String)var9.getFirst(), var1.createInt((Integer)var9.getSecond()));
         }

         var7 = var7.set("levels", var8);
         if (var4) {
            var7 = var7.set("show_in_tooltip", var1.createBoolean(false));
         }

         var0.method4273(var3, var7);
      }

      if (var5.result().isPresent() && var6.isEmpty()) {
         var0.method4273("minecraft:enchantment_glint_override", var1.createBoolean(true));
      }
   }

   private static Optional method4227(Dynamic var0) {
      return var0.get("id").asString().apply2stable((var0x, var1) -> Pair.of(var0x, Mth.clamp(var1.intValue(), 0, 255)), var0.get("lvl").asNumber()).result();
   }

   private static void method4228(ItemConversionNodeK var0, Dynamic var1, int var2) {
      method4229(var0, var1, "CanDestroy", "minecraft:can_break", (var2 & 8) != 0);
      method4229(var0, var1, "CanPlaceOn", "minecraft:can_place_on", (var2 & 16) != 0);
   }

   private static void method4229(ItemConversionNodeK var0, Dynamic var1, String var2, String var3, boolean var4) {
      Optional var5 = var0.fa(var2).result();
      if (!var5.isEmpty()) {
         Dynamic var6 = var1.emptyMap().set("predicates", var1.createList(((Dynamic)var5.get()).asStream().map(var0x -> {
            Dynamic var1x = (Dynamic)var0x;
            return (Dynamic)DataFixUtils.orElse(var1x.asString().map(var1xx -> method4230(var1x, (String)var1xx)).result(), var0x);
         })));
         if (var4) {
            var6 = var6.set("show_in_tooltip", var1.createBoolean(false));
         }

         var0.method4273(var3, var6);
      }
   }

   private static Dynamic method4230(Dynamic var0, String var1) {
      int var2 = var1.indexOf(91);
      int var3 = var1.indexOf(123);
      int var4 = var1.length();
      if (var2 != -1) {
         var4 = var2;
      }

      if (var3 != -1) {
         var4 = Math.min(var4, var3);
      }

      String var5 = var1.substring(0, var4);
      Dynamic var6 = var0.emptyMap().set("blocks", var0.createString(var5.trim()));
      int var7 = var1.indexOf(93);
      if (var2 != -1 && var7 != -1) {
         Dynamic var8 = var0.emptyMap();

         for (String var11 : bbz.split(var1.substring(var2 + 1, var7))) {
            int var12 = var11.indexOf(61);
            if (var12 != -1) {
               String var13 = var11.substring(0, var12).trim();
               String var14 = var11.substring(var12 + 1).trim();
               var8 = var8.set(var13, var0.createString(var14));
            }
         }

         var6 = var6.set("state", var8);
      }

      int var15 = var1.indexOf(125);
      if (var3 != -1 && var15 != -1) {
         var6 = var6.set("nbt", var0.createString(var1.substring(var3, var15 + 1)));
      }

      return var6;
   }

   private static void method4231(ItemConversionNodeK var0, Dynamic var1, int var2) {
      List var3 = var0.fa("AttributeModifiers").asList(ItemDataFixK::method4232);
      boolean var4 = (var2 & 2) != 0;
      if (!var3.isEmpty() || var4) {
         Dynamic var5 = var1.emptyMap().set("modifiers", var1.createList(var3.stream()));
         if (var4) {
            var5 = var5.set("show_in_tooltip", var1.createBoolean(false));
         }

         var0.method4273("minecraft:attribute_modifiers", var5);
      }
   }

   private static Dynamic method4232(Dynamic var0) {
      Dynamic var1 = var0.emptyMap().set("name", var0.createString("")).set("amount", var0.createDouble(0.0)).set("operation", var0.createString("add_value"));
      var1 = Dynamic.copyField(var0, "AttributeName", var1, "type");
      var1 = Dynamic.copyField(var0, "Slot", var1, "slot");
      var1 = Dynamic.copyField(var0, "UUID", var1, "uuid");
      var1 = Dynamic.copyField(var0, "Name", var1, "name");
      var1 = Dynamic.copyField(var0, "Amount", var1, "amount");
      return Dynamic.copyAndFixField(var0, "Operation", var1, "operation", var0x -> {
         return var0x.createString(switch (var0x.asInt(0)) {
            case 1 -> "add_multiplied_base";
            case 2 -> "add_multiplied_total";
            default -> "add_value";
         });
      });
   }

   private static Pair method4233(Dynamic var0) {
      Dynamic var1 = (Dynamic)DataFixUtils.orElseGet(var0.get("id").result(), () -> var0.createString(""));
      Dynamic var2 = var0.emptyMap()
         .set("type", var0.createString(cB(var0.get("type").asInt(0))))
         .set("x", var0.createDouble(var0.get("x").asDouble(0.0)))
         .set("z", var0.createDouble(var0.get("z").asDouble(0.0)))
         .set("rotation", var0.createFloat((float)var0.get("rot").asDouble(0.0)));
      return Pair.of(var1, var2);
   }

   private static String cB(int var0) {
      return switch (var0) {
         case 1 -> "frame";
         case 2 -> "red_marker";
         case 3 -> "blue_marker";
         case 4 -> "target_x";
         case 5 -> "target_point";
         case 6 -> "player_off_map";
         case 7 -> "player_off_limits";
         case 8 -> "mansion";
         case 9 -> "monument";
         case 10 -> "banner_white";
         case 11 -> "banner_orange";
         case 12 -> "banner_magenta";
         case 13 -> "banner_light_blue";
         case 14 -> "banner_yellow";
         case 15 -> "banner_lime";
         case 16 -> "banner_pink";
         case 17 -> "banner_gray";
         case 18 -> "banner_light_gray";
         case 19 -> "banner_cyan";
         case 20 -> "banner_purple";
         case 21 -> "banner_blue";
         case 22 -> "banner_brown";
         case 23 -> "banner_green";
         case 24 -> "banner_red";
         case 25 -> "banner_black";
         case 26 -> "red_x";
         case 27 -> "village_desert";
         case 28 -> "village_plains";
         case 29 -> "village_savanna";
         case 30 -> "village_snowy";
         case 31 -> "village_taiga";
         case 32 -> "jungle_temple";
         case 33 -> "swamp_hut";
         default -> "player";
      };
   }

   private static void method4234(ItemConversionNodeK var0, Dynamic var1) {
      Dynamic var2 = var1.emptyMap();
      Optional var3 = var0.fa("Potion").asString().result().filter(var0x -> !var0x.equals("minecraft:empty"));
      if (var3.isPresent()) {
         var2 = var2.set("potion", var1.createString((String)var3.get()));
      }

      var2 = var0.method4275("CustomPotionColor", var2, "custom_color");
      var2 = var0.method4275("custom_potion_effects", var2, "custom_effects");
      if (!var2.equals(var1.emptyMap())) {
         var0.method4273("minecraft:potion_contents", var2);
      }
   }

   private static void method4235(ItemConversionNodeK var0, Dynamic var1) {
      Dynamic var2 = method4237(var0, var1);
      if (var2 != null) {
         var0.method4273("minecraft:writable_book_content", var1.emptyMap().set("pages", var2));
      }
   }

   private static void method4236(ItemConversionNodeK var0, Dynamic var1) {
      Dynamic var2 = method4237(var0, var1);
      String var3 = var0.fa("title").asString("");
      Optional var4 = var0.fa("filtered_title").asString().result();
      Dynamic var5 = var1.emptyMap();
      var5 = var5.set("title", method4238(var1, var3, var4));
      var5 = var0.method4275("author", var5, "author");
      var5 = var0.method4275("resolved", var5, "resolved");
      var5 = var0.method4275("generation", var5, "generation");
      if (var2 != null) {
         var5 = var5.set("pages", var2);
      }

      var0.method4273("minecraft:written_book_content", var5);
   }

   @Nullable
   private static Dynamic method4237(ItemConversionNodeK var0, Dynamic var1) {
      List var2 = var0.fa("pages").asList(var0x -> var0x.asString(""));
      Map var3 = var0.fa("filtered_pages").asMap(var0x -> var0x.asString("0"), var0x -> var0x.asString(""));
      if (var2.isEmpty()) {
         return null;
      }

      ArrayList var4 = new ArrayList(var2.size());

      for (int var5 = 0; var5 < var2.size(); var5++) {
         String var6 = (String)var2.get(var5);
         String var7 = (String)var3.get(String.valueOf(var5));
         var4.add(method4238(var1, var6, Optional.ofNullable(var7)));
      }

      return var1.createList(var4.stream());
   }

   private static Dynamic method4238(Dynamic var0, String var1, Optional var2) {
      Dynamic var3 = var0.emptyMap().set("raw", var0.createString(var1));
      if (var2.isPresent()) {
         var3 = var3.set("filtered", var0.createString((String)var2.get()));
      }

      return var3;
   }

   private static void method4239(ItemConversionNodeK var0, Dynamic var1) {
      Dynamic var2 = var1.emptyMap();

      for (String var3 : bbx) {
         var2 = var0.method4275(var3, var2, var3);
      }

      if (!var2.equals(var1.emptyMap())) {
         var0.method4273("minecraft:bucket_entity_data", var2);
      }
   }

   private static void method4240(ItemConversionNodeK var0, Dynamic var1) {
      Optional var2 = var0.fa("LodestonePos").result();
      Optional var3 = var0.fa("LodestoneDimension").result();
      if (!var2.isEmpty() || !var3.isEmpty()) {
         boolean var4 = var0.fa("LodestoneTracked").asBoolean(true);
         Dynamic var5 = var1.emptyMap();
         if (var2.isPresent() && var3.isPresent()) {
            var5 = var5.set("target", var1.emptyMap().set("pos", (Dynamic)var2.get()).set("dimension", (Dynamic)var3.get()));
         }

         if (!var4) {
            var5 = var5.set("tracked", var1.createBoolean(false));
         }

         var0.method4273("minecraft:lodestone_tracker", var5);
      }
   }

   private static void method4241(ItemConversionNodeK var0) {
      var0.method4278("Explosion", true, var1 -> {
         var0.method4273("minecraft:firework_explosion", method4243(var1));
         return var1.remove("Type").remove("Colors").remove("FadeColors").remove("Trail").remove("Flicker");
      });
   }

   private static void method4242(ItemConversionNodeK var0) {
      var0.method4278("Fireworks", true, var1 -> {
         Stream var2 = var1.get("Explosions").asStream().map(ItemDataFixK::method4243);
         int var3 = var1.get("Flight").asInt(0);
         var0.method4273("minecraft:fireworks", var1.emptyMap().set("explosions", var1.createList(var2)).set("flight_duration", var1.createByte((byte)var3)));
         return var1.remove("Explosions").remove("Flight");
      });
   }

   private static Dynamic method4243(Dynamic var0) {
      var0 = var0.set("shape", var0.createString(switch (var0.get("Type").asInt(0)) {
         case 1 -> "large_ball";
         case 2 -> "star";
         case 3 -> "creeper";
         case 4 -> "burst";
         default -> "small_ball";
      })).remove("Type");
      var0 = var0.renameField("Colors", "colors");
      var0 = var0.renameField("FadeColors", "fade_colors");
      var0 = var0.renameField("Trail", "has_trail");
      return var0.renameField("Flicker", "has_twinkle");
   }

   public static Dynamic method4244(Dynamic var0) {
      Optional var1 = var0.asString().result();
      if (var1.isPresent()) {
         return eY((String)var1.get()) ? var0.emptyMap().set("name", var0.createString((String)var1.get())) : var0.emptyMap();
      }

      String var2 = var0.get("Name").asString("");
      Optional var3 = var0.get("Id").result();
      Dynamic var4 = method4245(var0.get("Properties"));
      Dynamic var5 = var0.emptyMap();
      if (eY(var2)) {
         var5 = var5.set("name", var0.createString(var2));
      }

      if (var3.isPresent()) {
         var5 = var5.set("id", (Dynamic)var3.get());
      }

      if (var4 != null) {
         var5 = var5.set("properties", var4);
      }

      return var5;
   }

   private static boolean eY(String var0) {
      return var0.length() > 16 ? false : var0.chars().filter(var0x -> var0x <= 32 || var0x >= 127).findAny().isEmpty();
   }

   @Nullable
   private static Dynamic method4245(OptionalDynamic var0) {
      Map var1 = var0.asMap(var0x -> var0x.asString(""), var0x -> var0x.asList(var0xx -> {
         String var1x = var0xx.get("Value").asString("");
         Optional var2 = var0xx.get("Signature").asString().result();
         return Pair.of(var1x, var2);
      }));
      return var1.isEmpty() ? null : var0.createList(var1.entrySet().stream().flatMap(var1x -> ((List)var1x.getValue()).stream().map(var2 -> {
         Pair var3 = (Pair)var2;
         Dynamic var4 = var0.emptyMap().set("name", var0.createString((String)var1x.getKey())).set("value", var0.createString((String)var3.getFirst()));
         Optional var5 = (Optional)var3.getSecond();
         return var5.isPresent() ? var4.set("signature", var0.createString((String)var5.get())) : var4;
      })));
   }

   protected TypeRewriteRule makeRule() {
      return this.writeFixAndRead(
         "ItemStack componentization", this.getInputSchema().getType(References.ITEM_STACK), this.getOutputSchema().getType(References.ITEM_STACK), var0 -> {
            Optional var1 = ItemConversionNodeK.method4272(var0).map(var0x -> {
               method4222(var0x, var0x.baa);
               return var0x.OR();
            });
            return (Dynamic)DataFixUtils.orElse(var1, var0);
         }
      );
   }
}
