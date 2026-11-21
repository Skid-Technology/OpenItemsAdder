package dev.lone.itemsadder.item;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.worldgen.TreeDecorator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ItemActionRegistry {
   public final Map aiy = new ConcurrentHashMap();
   final CustomItemDefinition aiz;

   public ItemActionRegistry(CustomItemDefinition var1) {
      this.aiz = var1;
   }

   public void loadBehaviours(ContentManager var1) {
      String var2 = this.aiz.DJ();
      ConfigSectionWrapper var3 = this.aiz.DS();
      List var4 = var3.jK("behaviours");
      if (var4 != null) {
         for (String var6 : var4) {
            String var7 = "behaviours." + var6;
            if (var3.method7078(var7 + ".enabled", true)) {
               switch (var6) {
                  case "gun":
                     String var42 = var3.jO(var7 + ".projectile");
                     ItemStack var51 = var1.method8179(var42);
                     if (var51 == null) {
                        if (var42 == null) {
                           Log.error("Projectile item not found for gun '" + this.aiz.getNamespacedID() + "'. File " + var2);
                        } else {
                           Log.error("Projectile item '" + var42 + "' not found for gun '" + this.aiz.getNamespacedID() + "'. File " + var2);
                        }
                        continue;
                     }

                     ItemGiveAction var59 = new ItemGiveAction(this.aiz, var51);
                     this.aiy.put("gun", var59);
                     break;
                  case "hat":
                     if (var3.kj(var7)) {
                        ItemActionLabelled var41 = new ItemActionLabelled(this.aiz);
                        this.aiy.put("hat", var41);
                     }
                     break;
                  case "keep_on_death":
                     if (var3.kj(var7)) {
                        ItemActionBare var40 = new ItemActionBare(this.aiz);
                        this.aiy.put("keep_on_death", var40);
                     }
                     break;
                  case "fuel":
                     int var39 = var3.method7076(var7 + ".burn_ticks", 1600);
                     if (var39 > 32767) {
                        var39 = 32767;
                        Log.warn(
                           "'burn_ticks' for fuel '" + this.aiz.getNamespacedID() + "' is greater than the max value '32767'. Capped to it. File: " + var2
                        );
                     }

                     ItemBlockAction var50 = new ItemBlockAction(this.aiz, (short)var39);
                     if (var3.jI(var7 + ".machines")) {
                        for (String var68 : var3.jZ(var7 + ".machines")) {
                           if (BukkitRegistryLookup.hasEnum(var68, Material.class)) {
                              var50.adF.add(Material.valueOf(var68.toUpperCase()));
                           } else {
                              Log.error(
                                 "machine '"
                                    + var68
                                    + "' set for fuel '"
                                    + this.aiz.getNamespacedID()
                                    + "' is not a furnace machine. Please fix it. File: "
                                    + var2
                              );
                           }
                        }
                     }

                     this.aiy.put("fuel", var50);
                     break;
                  case "music_disc":
                     ItemCommandAction var38 = new ItemCommandAction(this.aiz, var3.jO(var7 + ".song.description"), var3.jO(var7 + ".song.name"));
                     this.aiy.put("music_disc", var38);
                     break;
                  case "vehicle":
                     Object var37 = var3.kf(var7 + ".waterline_offset");
                     double var49 = var37 == null ? 0.0 : (var37 instanceof Number var62 ? var62.doubleValue() : Double.NaN);
                     if (!Double.isFinite(var49)) {
                        Log.error("Invalid vehicle waterline_offset for '" + this.aiz.getNamespacedID() + "'. File " + var2);
                        continue;
                     }

                     ItemThrowAction var63 = new ItemThrowAction(
                        this.aiz,
                        var3.method7078(var7 + ".fixed_rotation", false),
                        var3.method7078(var7 + ".small", var3.method7078(var7 + ".small_hitbox", true)),
                        var3.method7102(var7 + ".step_height", 0.3F),
                        var3.method7102(var7 + ".sit_height", 1.0F),
                        var3.method7102(var7 + ".speed.drive", 0.3F),
                        var3.method7102(var7 + ".speed.jump", 0.3F),
                        var3.method7102(var7 + ".speed.fly", 0.0F),
                        var3.method7076(var7 + ".fuel.start", -1),
                        var3.method7076(var7 + ".fuel.max", -1),
                        var3.method7078(var7 + ".float_on_water", false),
                        var49
                     );
                     float var67 = var3.method7102(var7 + ".hitbox.length", var3.method7102(var7 + ".size.x", 1.0F));
                     float var70 = var3.method7102(var7 + ".hitbox.width", var3.method7102(var7 + ".size.y", 1.0F));
                     float var72 = var3.method7102(var7 + ".hitbox.height", var3.method7102(var7 + ".size.z", 1.0F));
                     if (!(var67 > 6.0F) && !(var70 > 6.0F) && !(var72 > 6.0F)) {
                        var63.method358(var67, var70, var72);
                     } else {
                        Log.error("Hitbox too big (max 6) for item " + this.aiz.getNamespacedID() + "'. File " + var2);
                     }

                     var63.setSmokeEffect(
                        var3.method7076(var7 + ".smoke.amount", 0),
                        var3.method7080(var7 + ".smoke.offset.x", 0.0),
                        var3.method7080(var7 + ".smoke.offset.y", 0.0),
                        var3.method7080(var7 + ".smoke.offset.z", -0.8)
                     );
                     String var73 = var7 + ".fuel.items.";

                     for (String var75 : var3.jK(var7 + ".fuel.items")) {
                        ItemStack var76 = var1.method8179(var75);
                        if (var76 == null) {
                           Log.error("Fuel item '" + var75 + "' not found for vehicle '" + this.aiz.getNamespacedID() + "'. File " + var2);
                        } else if (var3.ki(var73 + var75)) {
                           var63.addFuelOption(new ItemStackPair(var76, null, var3.jP(var73 + "." + var75)));
                        } else {
                           String var79 = var3.bc(var73 + "." + var75 + ".return_item", null);
                           ItemStack var82 = var79 != null ? var1.method8179(var79) : null;
                           var63.addFuelOption(new ItemStackPair(var76, var82, var3.method7076(var73 + "." + var75 + ".fuel_amount", 1)));
                        }
                     }

                     this.aiy.put("vehicle", var63);
                     break;
                  case "furniture":
                     ConfigSectionWrapper var36 = var3.kc(var7);
                     boolean var48 = var3.method7078(var7 + ".small_hitbox", var3.method7078(var7 + ".small", false));
                     EntityType var57 = var3.method7109(
                        var7 + ".entity",
                        EntityType.ARMOR_STAND,
                        var2x -> Log.error("Entity type '" + var2x + "' not valid for item '" + this.aiz.getNamespacedID() + "'. File " + var2)
                     );
                     if (var57 != null) {
                        if (var57 != EntityType.ARMOR_STAND && !ProjectileTypeHelper.isAllowedEntityType(var57) && var57 != EntityType.ITEM_DISPLAY) {
                           Log.error(
                              "Entity type '"
                                 + var57
                                 + "' not allowed (only ARMOR_STAND, ITEM_FRAME or GLOW_ITEM_FRAME, ITEM_DISPLAY) for item '"
                                 + this.aiz.getNamespacedID()
                                 + "'. File "
                                 + var2
                           );
                        } else {
                           FurnitureItemEntity var61 = new FurnitureItemEntity(
                              this.aiz,
                              var57,
                              var3.method7078(var7 + ".gravity", false),
                              var3.method7078(var7 + ".fixed_rotation", false),
                              var48,
                              var3.method7076(var7 + ".light_level", -1),
                              var3.method7078(var7 + ".solid", false),
                              var3.method7078(var7 + ".opposite_direction", false),
                              var3.method7078(var7 + ".drop_when_mined", !var3.method7078(var7 + ".cancel_drop", false)),
                              var3.method7078(var7 + ".shift_interact_rotate", true)
                           );
                           float var66 = var3.method7102(var7 + ".hitbox.length", 1.0F);
                           float var69 = var3.method7102(var7 + ".hitbox.width", 1.0F);
                           float var71 = var3.method7102(var7 + ".hitbox.height", 1.0F);
                           if (!var61.method406() || !(var66 > 4.0F) && !(var69 > 4.0F) && !(var71 > 4.0F)) {
                              var61.method358(Math.min(var66, 4.0F), Math.min(var69, 4.0F), Math.min(var71, 4.0F));
                           } else {
                              Log.error("Solid hitbox too big (max 4) for furniture '" + this.aiz.getNamespacedID() + "'. File " + var2);
                           }

                           float var17 = var3.method7102(var7 + ".hitbox.length_offset", 0.0F);
                           float var18 = var3.method7102(var7 + ".hitbox.width_offset", 0.0F);
                           float var19 = var3.method7102(var7 + ".hitbox.height_offset", 0.0F);
                           if (!(var17 > 5.0F) && !(var18 > 5.0F) && !(var19 > 5.0F)) {
                              var61.method359(var17, var18, var19);
                           } else {
                              Log.error("Hitbox offsets too big (max 5) for furniture '" + this.aiz.getNamespacedID() + "'. File " + var2);
                           }

                           if (var36.jI("display_transformation") && var57 == EntityType.ITEM_DISPLAY) {
                              ConfigSectionWrapper var20 = var36.kc("display_transformation");
                              if (var20 != null) {
                                 if (!Settings.get().autoUpdateTransformations) {
                                    var61.aH(false);
                                 } else {
                                    var61.aH(var20.method7078("auto_update_in_world", true));
                                 }

                                 if (var20.jI("source_entity") && !"item_display".equalsIgnoreCase(var20.jO("source_entity"))) {
                                    var61.aG(true);
                                    switch (var20.bc("source_entity", "").toLowerCase(Locale.ROOT)) {
                                       case "armor_stand":
                                          var61.aF(false);
                                          break;
                                       case "small_armor_stand":
                                          var61.aF(true);
                                          break;
                                       case "item_frame":
                                          var61.AF();
                                    }
                                 } else if (var20.jI("adjust_legacy_model")) {
                                    switch (var20.bc("adjust_legacy_model", "").toLowerCase(Locale.ROOT)) {
                                       case "armor_stand":
                                          var61.method407(ItemDisplayTransform.HEAD);
                                          var61.setTransformation(
                                             new Transformation(
                                                new Vector3f(var18 == 0.5F ? -1.0F : 0.0F, 1.7F, 0.0F),
                                                new Quaternionf(),
                                                new Vector3f(0.65F, 0.65F, 0.65F),
                                                new Quaternionf()
                                             )
                                          );
                                          var61.method408(FurnitureHostType.ARMOR_STAND);
                                          break;
                                       case "small_armor_stand":
                                          var61.method407(ItemDisplayTransform.HEAD);
                                          var61.setTransformation(
                                             new Transformation(
                                                new Vector3f(var18 == 0.5F ? -1.0F : 0.0F, 0.92F, 0.0F),
                                                new Quaternionf(),
                                                new Vector3f(0.45F, 0.45F, 0.45F),
                                                new Quaternionf()
                                             )
                                          );
                                          var61.method408(FurnitureHostType.SMALL_ARMOR_STAND);
                                          break;
                                       case "item_frame":
                                          var61.method407(ItemDisplayTransform.FIXED);
                                          var61.setTransformation(
                                             new Transformation(
                                                new Vector3f(0.0F, 0.0F, 0.0F),
                                                new Quaternionf(),
                                                new Vector3f(0.5F, 0.5F, 0.5F),
                                                new Quaternionf(new AxisAngle4f((float)Math.toRadians(270.0), 1.0F, 0.0F, 0.0F))
                                             )
                                          );
                                          var61.method408(FurnitureHostType.ITEM_FRAME);
                                    }
                                 } else {
                                    var61.method407(
                                       (ItemDisplayTransform)BukkitRegistryLookup.enumOrDefault(
                                          var20.jO("transform"), ItemDisplayTransform.class, ItemDisplayTransform.FIXED
                                       )
                                    );
                                    Quaternionf var77 = parseRotation(var20, "right_rotation");
                                    Quaternionf var80 = parseRotation(var20, "left_rotation");
                                    Vector3f var23 = new Vector3f(1.0F, 1.0F, 1.0F);
                                    Vector3f var24 = new Vector3f(0.0F, 0.0F, 0.0F);
                                    if (var20.jI("scale")) {
                                       var23 = new Vector3f(var20.jQ("scale.x"), var20.jQ("scale.y"), var20.jQ("scale.z"));
                                    }

                                    if (var20.jI("translation")) {
                                       var24 = new Vector3f(var20.jQ("translation.x"), var20.jQ("translation.y"), var20.jQ("translation.z"));
                                    }

                                    var61.method427(var20.method7102("render_size.width", var69), var20.method7102("render_size.height", var71));
                                    var61.setTransformation(new Transformation(var24, var80, var23, var77));
                                 }
                              }
                           }

                           var61.method360(
                              var3.method7078(var7 + ".placeable_on.walls", true),
                              var3.method7078(var7 + ".placeable_on.ceiling", true),
                              var3.method7078(var7 + ".placeable_on.floor", true)
                           );
                           if (var3.jI(var7 + ".permission_suffix.break")) {
                              var61.bZ(var3.bc(var7 + ".permission_suffix.break", null));
                           }

                           if (var3.jI(var7 + ".permission_suffix.place")) {
                              var61.ca(var3.bc(var7 + ".permission_suffix.place", null));
                           }

                           this.aiy.put("furniture", var61);
                        }
                     }
                     break;
                  case "furniture_sit":
                     ItemFlagAction var35 = new ItemFlagAction(
                        this.aiz,
                        var3.method7080(var7 + ".sit_height", 0.0),
                        var3.method7078(var7 + ".opposite_direction", true),
                        var3.method7078(var7 + ".sit_all_solid_blocks", true),
                        var3.method7078(var7 + ".passenger_front_view_on_sit", true),
                        var3.method7078(var7 + ".passenger_rotate_body", false)
                     );
                     this.aiy.put("furniture_sit", var35);
                     break;
                  case "furniture_consumable":
                     List var34 = var3.jZ(var7 + ".furniture_list");
                     ArrayList var47 = new ArrayList();

                     for (String var60 : var34) {
                        CustomItemDefinition var65 = ContentManager.method8084().method8154(var60);
                        if (var65 == null) {
                           Log.error(
                              "Unknown item '" + var60 + "' in furniture_list of furniture_consumable '" + this.aiz.getNamespacedID() + "'. File " + var2
                           );
                        } else {
                           var47.add(var65);
                        }
                     }

                     ItemDamageAction var56 = new ItemDamageAction(
                        this.aiz, var3.method7076(var7 + ".hunger", 0), var3.method7102(var7 + ".saturation", 0.0F), var47, true
                     );
                     this.aiy.put("furniture_consumable", var56);
                     break;
                  case "furniture_trade_machine":
                  case "block_trade_machine":
                     ItemMerchantAction var32 = null;
                     if (var6.equals("furniture_trade_machine")) {
                        var32 = new ItemActionKeyedDup2(this.aiz, var3.bc(var7 + ".title", "Trades"));
                        this.aiy.put("furniture_trade_machine", var32);
                     } else {
                        var32 = new ItemActionKeyed(this.aiz, var3.bc(var7 + ".title", "Trades"));
                        this.aiy.put("block_trade_machine", var32);
                     }

                     if (var3.jI(var7 + ".permission")) {
                        var32.cb(var3.jO(var7 + ".permission"));
                     }

                     if (var3.jI(var7 + ".gui_texture")) {
                        if (var3.jI(var7 + ".gui_texture.left") && var3.jI(var7 + ".gui_texture.right")) {
                           var32.method376(ContentManager.method8084().aE.bB(var3.jO(var7 + ".gui_texture.left")));
                           var32.method377(ContentManager.method8084().aE.bB(var3.jO(var7 + ".gui_texture.right")));
                           var32.bq(var3.method7076(var7 + ".gui_texture.title_offset", -97));
                           var32.AQ();
                        } else {
                           Log.error(
                              "Missing texture for furniture_trade_machine "
                                 + this.aiz.getNamespacedID()
                                 + ". Check if left and right textures exist. File "
                                 + var2
                           );
                        }
                     }

                     String var46 = var7 + ".trades_list";
                     if (!var3.jI(var7 + ".trades_list")) {
                        Log.error("No trades_list found for item '" + this.aiz.getNamespacedID() + "'. File " + var2);
                        continue;
                     }

                     for (String var13 : var3.jK(var7 + ".trades_list")) {
                        if (!var3.jI(var46 + "." + var13 + ".result.item")) {
                           Log.error("No result for trade '" + var13 + "' of " + this.aiz.getNamespacedID() + ". File " + var2);
                        } else {
                           ItemStack var14 = var1.method8177(var3.jO(var46 + "." + var13 + ".result.item"), var2);
                           if (var14 != null) {
                              var14.setAmount(var3.method7076(var46 + "." + var13 + ".result.amount", 1));
                              ItemStack var15 = var1.method8177(var3.jO(var46 + "." + var13 + ".ingredients.slot1.item"), var2);
                              if (var15 != null) {
                                 var15.setAmount(var3.method7076(var46 + "." + var13 + ".ingredients.slot1.amount", 1));
                                 ItemStack var16 = null;
                                 if (var3.jI(var46 + "." + var13 + ".ingredients.slot2.item")) {
                                    var16 = var1.method8177(var3.jO(var46 + "." + var13 + ".ingredients.slot2.item"), var2);
                                    if (var16 == null) {
                                       continue;
                                    }

                                    var16.setAmount(var3.method7076(var46 + "." + var13 + ".ingredients.slot2.amount", 1));
                                 }

                                 var32.method378(var14, var15, var16);
                              }
                           }
                        }
                     }
                     break;
                  case "entity_summoner":
                     String var31 = var3.bc(var7 + ".entity", null);

                     try {
                        ItemSpawnAction var45 = new ItemSpawnAction(
                           this.aiz,
                           var31,
                           var3.method7077(var7 + ".max_per_chunk", null),
                           var3.method7077(var7 + ".decrement_durability", null),
                           var3.method7076(var7 + ".decrement_amount", 1)
                        );
                        this.aiy.put("entity_summoner", var45);
                     } catch (IllegalArgumentException var25) {
                        Log.error("Invalid 'entity' on 'entity_summoner' behaviour of '" + this.aiz.getNamespacedID() + "'. File " + var2);
                     }
                     break;
                  case "complex_furniture":
                     if (!Settings.get().customEntitiesEnabled) {
                        Log.error(
                           "Can't load complex_furniture for item '"
                              + this.aiz.getNamespacedID()
                              + "'. Custom entities are disabled in config.yml. File "
                              + var2
                        );
                     } else {
                        String var30 = var3.bc(var7 + ".entity", null);

                        try {
                           ItemSoundEffects var44 = ItemSoundEffects.apP;
                           if (var3.jI(var7 + ".sound.place") || var3.jI(var7 + ".sound.break")) {
                              var44 = new ItemSoundEffects();
                              var44.method9994(
                                 var3.bc(var7 + ".sound.place.name", null),
                                 var3.method7102(var7 + ".sound.place.volume", 1.0F),
                                 var3.method7102(var7 + ".sound.place.pitch", 1.0F)
                              );
                              var44.method9993(
                                 var3.bc(var7 + ".sound.break.name", null),
                                 var3.method7102(var7 + ".sound.break.volume", 1.0F),
                                 var3.method7102(var7 + ".sound.break.pitch", 1.0F)
                              );
                           }

                           PlaceableItemSpec var53 = new PlaceableItemSpec(
                              this.aiz,
                              var30,
                              var3.method7104(var7 + ".static", null),
                              var3.method7077(var7 + ".max_per_chunk", null),
                              var3.method7077(var7 + ".light_level", null),
                              var3.method7104(var7 + ".solid", null),
                              var3.method7104(var7 + ".drop_when_mined", null),
                              var3.method7078(var7 + ".fixed_rotation", false),
                              var3.method7078(var7 + ".fixed_position", true),
                              var3.method7078(var7 + ".opposite_direction", false),
                              var3.bc(var7 + ".permission_suffix", null),
                              var44,
                              var3.method7080(var7 + ".placement_offset.ceiling", 0.0),
                              var3.method7080(var7 + ".placement_offset.floor", 0.0),
                              var3.method7080(var7 + ".placement_offset.wall", 0.0)
                           );
                           this.aiy.put("complex_furniture", var53);
                        } catch (IllegalArgumentException var26) {
                           Log.error("Invalid 'entity' on 'complex_furniture' behaviour of '" + this.aiz.getNamespacedID() + "'. File " + var2);
                        }
                     }
                     break;
                  case "liquid_bucket":
                     if (Settings.get().liquidsEnabled) {
                        String var29 = var3.bc(var7 + ".name", null);
                        ColoredNamespacedId var43 = (ColoredNamespacedId)ContentManager.method8084().ay.get(var29);
                        if (var43 == null) {
                           Log.error("Liquid with name '" + var29 + "' not found for liquid bucket '" + this.aiz.getNamespacedID() + "'. File " + var2);
                           continue;
                        }

                        ItemMaterialAction var52 = new ItemMaterialAction(this.aiz, var43);
                        this.aiy.put("liquid_bucket", var52);
                        ContentManager.method8084().by.put(var52.AO().getNamespacedID(), this.aiz.getNamespacedID());
                     }
                     break;
                  case "liquid_analyzer":
                     if (var3.kj(var7) && Settings.get().liquidsEnabled) {
                        ItemActionBareDup2 var28 = new ItemActionBareDup2(this.aiz);
                        this.aiy.put("liquid_analyzer", var28);
                     }
                     break;
                  case "sapling":
                     String var10 = var3.bc(var7 + ".tree_populator", null);
                     if (var10 == null) {
                        Log.error("Cannot create sapling '" + this.aiz.getNamespacedID() + ". You didn't set tree_populator in sapling behaviour. File " + var2);
                     } else {
                        TreeDecorator var11 = (TreeDecorator)TreeDecorator.aDN.get(var10);
                        if (var11 == null) {
                           Log.error("Cannot create sapling '" + this.aiz.getNamespacedID() + "'. tree_populator '" + var10 + "' doesn't exist. File " + var2);
                        } else {
                           ItemTreeAction var12 = new ItemTreeAction(this.aiz, var11);
                           this.aiy.put("sapling", var12);
                        }
                     }
               }

               ItemEntityAction var27 = this.getEntityAction();
               if (var27 != null) {
                  var27.acY
                     .method9993(
                        var3.bc(var7 + ".sound.break.name", "minecraft:entity.armor_stand.break"),
                        var3.method7102(var7 + ".sound.break.volume", 1.0F),
                        var3.method7102(var7 + ".sound.break.pitch", 0.8F)
                     );
                  var27.acY
                     .method9994(
                        var3.bc(var7 + ".sound.place.name", "minecraft:entity.armor_stand.place"),
                        var3.method7102(var7 + ".sound.place.volume", 1.0F),
                        var3.method7102(var7 + ".sound.place.pitch", 0.8F)
                     );
               }
            }
         }

         this.registerActionIndex();
      }
   }

   private void registerActionIndex() {
      for (Entry var2 : this.aiy.entrySet()) {
         ContentManager.method8084().ai.computeIfAbsent((String)var2.getKey(), var0 -> new HashMap<>()).put(this.aiz.getNamespacedID(), this.aiz);
         ContentManager.method8084().aj.computeIfAbsent((String)var2.getKey(), var0 -> new ArrayList<>()).add(this.aiz.getNamespacedID());
         ContentManager.method8084().ak.computeIfAbsent((String)var2.getKey(), var0 -> new ArrayList<>()).add(this.aiz.getId());
      }
   }

   public void addAction(String var1, ItemActionBase var2) {
      this.aiy.put(var1, var2);
      ContentManager.method8084().ai.computeIfAbsent(var1, var0 -> new HashMap<>()).put(this.aiz.getNamespacedID(), this.aiz);
      ContentManager.method8084().aj.computeIfAbsent(var1, var0 -> new ArrayList<>()).add(this.aiz.getNamespacedID());
      ContentManager.method8084().ak.computeIfAbsent(var1, var0 -> new ArrayList<>()).add(this.aiz.getId());
   }

   public boolean hasAction(String var1) {
      return this.aiy.containsKey(var1);
   }

   @Nullable
   public ItemActionBase getAction(String var1) {
      return (ItemActionBase)this.aiy.get(var1);
   }

   @Nullable
   public ItemEntityAction getEntityAction() {
      ItemEntityAction var1 = (ItemEntityAction)this.aiz.aoh.getAction("furniture");
      if (var1 == null) {
         var1 = (ItemEntityAction)this.aiz.aoh.getAction("vehicle");
      }

      return var1;
   }

   public void copyActionsTo(CustomItemDefinition var1) {
      for (Entry var3 : this.aiy.entrySet()) {
         if (!var1.aoh.aiy.containsKey(var3.getKey())) {
            ItemActionBase var4 = ((ItemActionBase)var3.getValue()).copyForItem(var1);
            var1.aoh.aiy.put((String)var3.getKey(), var4);
            var1.aoh.registerActionIndex();
         }
      }
   }

   private static Quaternionf parseRotation(ConfigSectionWrapper var0, String var1) {
      if (var0.jI(var1)) {
         if (var0.jI(var1 + ".quaternion")) {
            return new Quaternionf(
               var0.jQ(var1 + ".quaternion.x"), var0.jQ(var1 + ".quaternion.y"), var0.jQ(var1 + ".quaternion.z"), var0.jQ(var1 + ".quaternion.w")
            );
         }

         if (var0.jI(var1 + ".axis_angle")) {
            return new Quaternionf(
               new AxisAngle4f(
                  (float)Math.toRadians(var0.jQ(var1 + ".axis_angle.angle")),
                  var0.jQ(var1 + ".axis_angle.axis.x"),
                  var0.jQ(var1 + ".axis_angle.axis.y"),
                  var0.jQ(var1 + ".axis_angle.axis.z")
               )
            );
         }
      }

      return new Quaternionf();
   }
}
