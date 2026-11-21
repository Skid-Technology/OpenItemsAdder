package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.EffectChainMode;
import dev.lone.itemsadder.core.EffectChainRule;
import dev.lone.itemsadder.core.NamespaceRegistrar;
import dev.lone.itemsadder.core.NamespacedId;
import dev.lone.itemsadder.item.effect.ActionEffect;
import dev.lone.itemsadder.item.effect.AreaEffect;
import dev.lone.itemsadder.item.effect.ArrowDisplayEffect;
import dev.lone.itemsadder.item.effect.BlockAreaEffect;
import dev.lone.itemsadder.item.effect.BlockBreakEffect;
import dev.lone.itemsadder.item.effect.BlockClickToggleEffect;
import dev.lone.itemsadder.item.effect.BlockFillEffect;
import dev.lone.itemsadder.item.effect.BlockMatcherSpec;
import dev.lone.itemsadder.item.effect.ClickMode;
import dev.lone.itemsadder.item.effect.CommandEffect;
import dev.lone.itemsadder.item.effect.ConsumeEffect;
import dev.lone.itemsadder.item.effect.CountEffect;
import dev.lone.itemsadder.item.effect.CountEffectDup2;
import dev.lone.itemsadder.item.effect.CountEffectDup3;
import dev.lone.itemsadder.item.effect.DamageMultiplierEffect;
import dev.lone.itemsadder.item.effect.DropEffect;
import dev.lone.itemsadder.item.effect.EffectOutcome;
import dev.lone.itemsadder.item.effect.EffectResult;
import dev.lone.itemsadder.item.effect.FurnitureSwapEffect;
import dev.lone.itemsadder.item.effect.FurnitureToggleEffect;
import dev.lone.itemsadder.item.effect.IntScaledEffect;
import dev.lone.itemsadder.item.effect.InteractionEffect;
import dev.lone.itemsadder.item.effect.ItemEffect;
import dev.lone.itemsadder.item.effect.ItemGrantEffect;
import dev.lone.itemsadder.item.effect.ItemRefreshEffect;
import dev.lone.itemsadder.item.effect.ItemSwapEffect;
import dev.lone.itemsadder.item.effect.ParticleEffect;
import dev.lone.itemsadder.item.effect.ParticleEffectDup2;
import dev.lone.itemsadder.item.effect.PlacementEffect;
import dev.lone.itemsadder.item.effect.PotionGrantEffect;
import dev.lone.itemsadder.item.effect.PotionGrantEffectDup2;
import dev.lone.itemsadder.item.effect.PotionRemoveEffect;
import dev.lone.itemsadder.item.effect.PotionRemoveEffectDup2;
import dev.lone.itemsadder.item.effect.ProjectileLaunchEffect;
import dev.lone.itemsadder.item.effect.ProjectileSpreadEffect;
import dev.lone.itemsadder.item.effect.ScaledCountEffect;
import dev.lone.itemsadder.item.effect.ScriptEffect;
import dev.lone.itemsadder.item.effect.ScriptRunEffect;
import dev.lone.itemsadder.item.effect.SoundCategoryEffect;
import dev.lone.itemsadder.item.effect.SoundEffect;
import dev.lone.itemsadder.item.effect.SoundEffectDup2;
import dev.lone.itemsadder.item.effect.TargetCategory;
import dev.lone.itemsadder.item.effect.TextEffect;
import dev.lone.itemsadder.item.effect.VisualEffect;
import dev.lone.itemsadder.particle.ParticleEmitterManager;
import dev.lone.itemsadder.script.CompiledScript;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.CollectionUtils;
import dev.lone.itemsadder.util.IntTableConstantsL;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.ResultConsumer;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class ItemEventDispatcher {
   public static boolean aiA;
   final CustomItemDefinition aiB;
   public final HashMap aiC = new HashMap();
   public final HashMap aiD = new HashMap();

   public ItemEventDispatcher(CustomItemDefinition var1) {
      this.aiB = var1;
   }

   public void loadEvents(ContentManager var1) {
      ConfigSectionWrapper var2 = this.aiB.DS();
      ConfigurationSection var3 = var2.kb("events");
      if (var3 != null) {
         for (ItemEventType var7 : ItemEventType.values()) {
            if (var3.contains(var7.toString())) {
               if (var3.contains(var7 + ".cooldown")) {
                  this.aiD.put(var7, var3.getInt(var7 + ".cooldown", 0) / 20 * 1000L);
               }

               ConfigurationSection var8 = var3.getConfigurationSection(var7.toString());
               if (var8 != null) {
                  for (String var10 : YamlConfig.method7042(var8, "")) {
                     ConfigurationSection var11 = var8.getConfigurationSection(var10);
                     if (var11 != null) {
                        if (var10.startsWith("play_sound")) {
                           this.registerEffect(
                                 var7,
                                 var10,
                                 new SoundCategoryEffect(
                                    var11.getString("name"),
                                    (float)var11.getDouble("volume", 1.0),
                                    (float)var11.getDouble("pitch", 1.0),
                                    var11.getString("category", null),
                                    var11.getBoolean("stop_previous", true)
                                 )
                              )
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("stop_sound")) {
                           this.registerEffect(var7, var10, new TextEffect(var11.getString("name")))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("execute_commands")) {
                           for (String var65 : YamlConfig.method7041(var11)) {
                              if (!EffectChainRule.bY(var65)) {
                                 CommandEffect var79 = new CommandEffect(
                                    var11.getString(var65 + ".command"),
                                    var11.getBoolean(var65 + ".as_console"),
                                    var11.getBoolean(var65 + ".silent", true),
                                    var11.getString(var65 + ".flow_success_if_message_contains", null)
                                 );
                                 this.registerEffect(var7, var10, var79)
                                    .cu(var11.getString("permission", null))
                                    .bu(var11.getInt(var65 + ".delay", -1))
                                    .method9775(this.parseEffectChainRule(var11));
                              }
                           }
                        } else if (var10.startsWith("play_particle")) {
                           String var49 = var11.getString("name");
                           if (var49 != null && var49.contains(":") && !var49.regionMatches(true, 0, "minecraft:", 0, 10)) {
                              try {
                                 this.registerEffect(
                                       var7, var10, new ParticleEffectDup2(var49, ParticleEmitterManager.method1713(var11.getDouble("duration_ticks", 60.0)))
                                    )
                                    .cu(var11.getString("permission", null))
                                    .bu(var11.getInt("delay", -1))
                                    .method9775(this.parseEffectChainRule(var11));
                              } catch (IllegalArgumentException var21) {
                                 Log.error("Invalid custom particle for action " + var10 + " of item " + this.aiB.getNamespacedID(), var21);
                              }
                              continue;
                           }

                           Particle var64 = BukkitRegistryLookup.particle(var49);
                           if (var64 == null) {
                              Log.error(
                                 "&cUnknown particle &e"
                                    + var49
                                    + "&c for action &e"
                                    + var10
                                    + "&c of item &e"
                                    + this.aiB.getNamespacedID()
                                    + ". Available particles: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html"
                              );
                              continue;
                           }

                           Object var78 = BukkitRegistryLookup.particleDataFor(var64);
                           if (var64.getDataType() != Void.class && var78 == null) {
                              Log.error(
                                 "&cParticle &e"
                                    + var49
                                    + "&c for action &e"
                                    + var10
                                    + "&c of item &e"
                                    + this.aiB.getNamespacedID()
                                    + " is currently not supported by the plugin."
                              );
                              continue;
                           }

                           this.registerEffect(var7, var10, new ParticleEffectDup2(var64, var78))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("shoot_particle")) {
                           String var48 = var11.getString("name");
                           if (var48 != null && var48.contains(":") && !var48.regionMatches(true, 0, "minecraft:", 0, 10)) {
                              try {
                                 this.registerEffect(
                                       var7,
                                       var10,
                                       new ParticleEffect(
                                          var48, var11.getInt("distance", 7), ParticleEmitterManager.method1713(var11.getDouble("duration_ticks", 60.0))
                                       )
                                    )
                                    .cu(var11.getString("permission", null))
                                    .bu(var11.getInt("delay", -1))
                                    .method9775(this.parseEffectChainRule(var11));
                              } catch (IllegalArgumentException var20) {
                                 Log.error("Invalid custom particle for action " + var10 + " of item " + this.aiB.getNamespacedID(), var20);
                              }
                              continue;
                           }

                           Particle var63 = BukkitRegistryLookup.particle(var48);
                           if (var63 == null) {
                              Log.error(
                                 "&cUnknown particle &e"
                                    + var48
                                    + "&c for action &e"
                                    + var10
                                    + "&c of item &e"
                                    + this.aiB.getNamespacedID()
                                    + ". Available particles: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html"
                              );
                              continue;
                           }

                           Object var77 = BukkitRegistryLookup.particleDataFor(var63);
                           if (var63.getDataType() != Void.class && var77 == null) {
                              Log.error(
                                 "&cParticle &e"
                                    + var48
                                    + "&c for action &e"
                                    + var10
                                    + "&c of item &e"
                                    + this.aiB.getNamespacedID()
                                    + " is currently not supported by the plugin."
                              );
                              continue;
                           }

                           this.registerEffect(var7, var10, new ParticleEffect(var63, var77, var11.getInt("distance", 7)))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("shoot_item")) {
                           int var47 = var11.getInt("distance", 7);
                           double var62 = var11.getDouble("speed", var47 / 10.0);
                           double var86 = var11.getDouble("damage", 0.0);
                           if (var47 <= 0
                              || !Double.isFinite(var62)
                              || var62 <= 0.0
                              || !Double.isFinite(var86)
                              || var86 < 0.0
                              || var11.contains("distance") && !var11.isInt("distance")
                              || var11.contains("speed") && !(var11.get("speed") instanceof Number)
                              || var11.contains("damage") && !(var11.get("damage") instanceof Number)
                              || var11.contains("gravity") && !var11.isBoolean("gravity")) {
                              Log.error(
                                 "Invalid distance, speed, damage or gravity for action '"
                                    + var10
                                    + "' of item '"
                                    + this.aiB.getNamespacedID()
                                    + "'. File: "
                                    + this.aiB.lH().DJ()
                              );
                              continue;
                           }

                           String var17 = var11.getString("item");
                           ItemStack var18 = var1.method8179(var17);
                           if (ItemStackUtils.aE(var18)) {
                              Log.error(
                                 "Invalid item '"
                                    + var17
                                    + "' for action '"
                                    + var10
                                    + "' of item '"
                                    + this.aiB.getNamespacedID()
                                    + "'. File: "
                                    + this.aiB.lH().DJ()
                              );
                              continue;
                           }

                           if (var11.contains("custom_model_data")) {
                              ItemStackUtils.method7259(var18, var11.getInt("custom_model_data"));
                           }

                           this.registerEffect(var7, var10, new ArrowDisplayEffect(var18, var47, var62, var86, var11.getBoolean("gravity", true)))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("play_effect")) {
                           String var46 = var11.getString("name");
                           Effect var61 = BukkitRegistryLookup.effect(var46);
                           if (var61 == null) {
                              Log.error(
                                 "&cUnknown effect &e"
                                    + var46
                                    + "&c for action &e"
                                    + var10
                                    + "&c of item &e"
                                    + this.aiB.getNamespacedID()
                                    + ". Available effects: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Effect.html"
                              );
                              continue;
                           }

                           this.registerEffect(var7, var10, new VisualEffect(var61))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("increment_durability")) {
                           this.registerEffect(var7, var10, new CountEffectDup3(var11.getInt("amount")))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("decrement_durability")) {
                           this.registerEffect(var7, var10, new CountEffectDup2(var11.getInt("amount")))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("decrement_usages")) {
                           this.registerEffect(var7, var10, new ConsumeEffect(var11.getInt("amount")))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("increment_amount")) {
                           this.registerEffect(var7, var10, new ProjectileLaunchEffect(var11.getInt("amount")))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("decrement_amount")) {
                           this.registerEffect(var7, var10, new CountEffect(var11.getInt("amount")))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("drop_exp")) {
                           this.registerEffect(
                                 var7, var10, new BlockBreakEffect(var11.getDouble("chance", 0.0), var11.getInt("min_amount"), var11.getInt("max_amount"))
                              )
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("drop_item")) {
                           ItemStack var45 = ContentManager.method8084().method8179(var11.getString("item"));
                           if (var45 == null) {
                              Log.error("Invalid item for 'drop_item' action of item '" + this.aiB.getNamespacedID() + "'. File: " + this.aiB.lH().DJ());
                              break;
                           }

                           this.registerEffect(
                                 var7,
                                 var10,
                                 new DropEffect(var45, var11.getDouble("chance", 100.0), var11.getInt("min_amount", 1), var11.getInt("max_amount", 1))
                              )
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("feed")) {
                           this.registerEffect(var7, var10, new IntScaledEffect(var11.getInt("amount"), (float)var11.getDouble("saturation", 0.0)))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("replace_properties")) {
                           String var44 = var11.getString("custom_model_data.copy_from_item");
                           String var60 = var11.getString("item_model.copy_from_item");
                           if (var11.contains("custom_model_data") && var44 == null
                              || var11.contains("item_model") && var60 == null
                              || var44 == null && var60 == null) {
                              Log.error(
                                 "Missing 'copy_from_item' for action '"
                                    + var10
                                    + "' of item '"
                                    + this.aiB.getNamespacedID()
                                    + "'. File: "
                                    + this.aiB.lH().DJ()
                              );
                              continue;
                           }

                           if (var60 != null && !Version.isAtLeast(Version.v1_21_4)) {
                              Log.error(
                                 "'item_model' requires Minecraft 1.21.4+ for action '"
                                    + var10
                                    + "' of item '"
                                    + this.aiB.getNamespacedID()
                                    + "'. File: "
                                    + this.aiB.lH().DJ()
                              );
                              continue;
                           }

                           CustomItemDefinition var76 = var44 == null ? null : var1.method8154(var44);
                           CustomItemDefinition var85 = var60 == null ? null : var1.method8154(var60);
                           if (var44 != null && var76 == null || var60 != null && var85 == null) {
                              Log.error(
                                 "&cUnknown item &e'"
                                    + (var76 == null && var44 != null ? var44 : var60)
                                    + "'&c for action &e'"
                                    + var10
                                    + "'&c of item &e'"
                                    + this.aiB.getNamespacedID()
                                    + "'&c. File: "
                                    + this.aiB.lH().DJ()
                              );
                              continue;
                           }

                           this.registerEffect(
                                 var7,
                                 var10,
                                 new ItemSwapEffect(
                                    var76, var11.getBoolean("custom_model_data.restorable", false), var85, var11.getBoolean("item_model.restorable", false)
                                 )
                              )
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("give_item")) {
                           String var27 = var11.getString("item");
                           ItemStack var51 = var1.method8177(var27, this.aiB.lH().DJ());
                           if (var51 == null) {
                              Log.error(
                                 "&cUnknown item &e'"
                                    + var27
                                    + "'&c for action &e'"
                                    + var10
                                    + "'&c of item &e'"
                                    + this.aiB.getNamespacedID()
                                    + "&c'. File: "
                                    + this.aiB.lH().DJ()
                              );
                              continue;
                           }

                           this.registerEffect(var7, var10, new ItemGrantEffect(var51, var11.getInt("amount", 1)))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("replace_near_blocks")) {
                           if (var11.getString("from") == null && var11.getStringList("from_multiple").isEmpty()) {
                              Log.error(
                                 "Missing 'from' for 'replace_near_blocks' action of item '" + this.aiB.getNamespacedID() + "'. File: " + this.aiB.lH().DJ()
                              );
                              break;
                           }

                           HashSet var28 = new HashSet(var11.getStringList("from_multiple"));
                           var28.add(var11.getString("from"));
                           ArrayList var52 = new ArrayList();

                           for (String var15 : var28) {
                              if (var15 != null) {
                                 try {
                                    BlockMatcherSpec var16 = BlockMatcherSpec.cw(var15);
                                    var52.add(var16);
                                 } catch (IllegalArgumentException var26) {
                                    Log.error(
                                       "Invalid 'from' for 'replace_near_blocks' action of item '"
                                          + this.aiB.getNamespacedID()
                                          + "'. File: "
                                          + this.aiB.lH().DJ()
                                    );
                                    var52.clear();
                                    break;
                                 }
                              }
                           }

                           if (var52.isEmpty()) {
                              break;
                           }

                           String var68 = var11.getString("to");
                           if (var68 == null) {
                              Log.error(
                                 "Missing 'to' for 'replace_near_blocks' action of item '" + this.aiB.getNamespacedID() + "'. File: " + this.aiB.lH().DJ()
                              );
                              break;
                           }

                           BlockMatcherSpec var80;
                           try {
                              var80 = BlockMatcherSpec.cw(var68);
                           } catch (IllegalArgumentException var25) {
                              Log.error(
                                 "Invalid 'to' for 'replace_near_blocks' action of item '" + this.aiB.getNamespacedID() + "'. File: " + this.aiB.lH().DJ()
                              );
                              break;
                           }

                           int var87 = var11.getInt("radius");
                           this.registerEffect(
                                 var7,
                                 var10,
                                 new BlockFillEffect(
                                    var52,
                                    var80,
                                    var11.getInt("radius.x", var87),
                                    var11.getInt("radius.y", var87),
                                    var11.getInt("radius.z", var87),
                                    var11.getInt("decrement_durability"),
                                    var11.getInt("decrement_amount"),
                                    var11.getBoolean("no_physics", false)
                                 )
                              )
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("glow_near_blocks")) {
                           String var29 = var11.getString("material");
                           if (var29 == null) {
                              Log.error(
                                 "Missing 'material' for 'glow_near_blocks' action of item '" + this.aiB.getNamespacedID() + "'. File: " + this.aiB.lH().DJ()
                              );
                              break;
                           }

                           BlockMatcherSpec var53;
                           try {
                              var53 = BlockMatcherSpec.cw(var29);
                           } catch (IllegalArgumentException var24) {
                              Log.error(
                                 "Invalid 'material' for 'glow_near_blocks' action of item '" + this.aiB.getNamespacedID() + "'. File: " + this.aiB.lH().DJ()
                              );
                              break;
                           }

                           this.registerEffect(
                                 var7,
                                 var10,
                                 new AreaEffect(var53, var11.getInt("radius.x"), var11.getInt("radius.y"), var11.getInt("radius.z"), var11.getInt("ticks", 40))
                              )
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("replace_block")) {
                           String var30 = var11.getString("from");
                           if (var30 == null) {
                              Log.error("Missing 'from' for 'replace_block' action of item '" + this.aiB.getNamespacedID() + "'. File: " + this.aiB.lH().DJ());
                              break;
                           }

                           BlockMatcherSpec var54;
                           try {
                              var54 = BlockMatcherSpec.cw(var30);
                           } catch (IllegalArgumentException var22) {
                              Log.error("Invalid 'from' for 'replace_block' action of item '" + this.aiB.getNamespacedID() + "'. File: " + this.aiB.lH().DJ());
                              break;
                           }

                           String var69 = var11.getString("to");
                           if (var69 == null) {
                              Log.error("Missing 'to' for 'replace_block' action of item '" + this.aiB.getNamespacedID() + "'. File: " + this.aiB.lH().DJ());
                              break;
                           }

                           BlockMatcherSpec var81;
                           try {
                              var81 = BlockMatcherSpec.cw(var69);
                           } catch (IllegalArgumentException var23) {
                              Log.error("Invalid 'to' for 'replace_block' action of item '" + this.aiB.getNamespacedID() + "'. File: " + this.aiB.lH().DJ());
                              break;
                           }

                           this.registerEffect(
                                 var7,
                                 var10,
                                 new BlockAreaEffect(
                                    var54, var81, var11.getInt("decrement_durability"), var11.getInt("decrement_amount"), var11.getBoolean("no_physics", false)
                                 )
                              )
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("multiple_break")) {
                           this.registerEffect(
                                 var7,
                                 var10,
                                 new PlacementEffect(
                                    var11.getInt("size"),
                                    var11.getInt("depth", 1) - 1,
                                    var11.getBoolean("vertical", false),
                                    var11.getBoolean("keep_ores"),
                                    var11.getBoolean("drop_all_blocks.enabled"),
                                    var11.getBoolean("drop_all_blocks.need_silk_touch")
                                 )
                              )
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("set_block")) {
                           ClickMode var31 = ClickMode.parse(var11.getString("target", ClickMode.RELATIVE.toString()));
                           String var55 = var11.getString("block");
                           BlockClickToggleEffect var70;
                           if (BukkitRegistryLookup.hasEnum(var55, Material.class)) {
                              var70 = new BlockClickToggleEffect(Material.valueOf(var55), var31, var11.getBoolean("decrement_amount", false));
                           } else {
                              CustomBlockDefinition var82 = ContentManager.method8084().method8160(var55);
                              if (var82 == null) {
                                 Log.error("Invalid block for 'set_block' action of item '" + this.aiB.getNamespacedID() + "'. File: " + this.aiB.lH().DJ());
                                 break;
                              }

                              var70 = new BlockClickToggleEffect(var82, var31, var11.getBoolean("decrement_amount", false));
                           }

                           this.registerEffect(var7, var10, var70)
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("place_furniture")) {
                           FurnitureItemEntity var32 = (FurnitureItemEntity)ContentManager.method8084().method8147("furniture", var11.getString("furniture"));
                           if (var32 == null) {
                              Log.error(
                                 "Invalid furniture '"
                                    + var11.getString("furniture")
                                    + "' for 'place_furniture' action of item '"
                                    + this.aiB.getNamespacedID()
                                    + "'. File: "
                                    + this.aiB.lH().DJ()
                              );
                              break;
                           }

                           this.registerEffect(
                              var7,
                              var10,
                              new FurnitureToggleEffect(var32, var11.getBoolean("decrement_amount", false))
                                 .cu(var11.getString("permission", null))
                                 .bu(var11.getInt("delay", -1))
                                 .method9775(this.parseEffectChainRule(var11))
                           );
                        } else if (var10.startsWith("replace_furniture")) {
                           String var33 = var11.getString("from");
                           var33 = var33 == null ? null : NamespacedId.method385(var33, this.aiB.getNamespace());
                           FurnitureItemEntity var56 = var33 == null ? null : (FurnitureItemEntity)ContentManager.method8084().method8147("furniture", var33);
                           if (var11.contains("from") && var56 == null) {
                              Log.error(
                                 "Invalid 'from' for 'replace_furniture' action of item '" + this.aiB.getNamespacedID() + "'. File: " + this.aiB.lH().DJ()
                              );
                              break;
                           }

                           String var71 = var11.getString("to", var11.getString("furniture"));
                           var71 = var71 == null ? null : NamespacedId.method385(var71, this.aiB.getNamespace());
                           FurnitureItemEntity var83 = var71 == null ? null : (FurnitureItemEntity)ContentManager.method8084().method8147("furniture", var71);
                           if (var83 == null) {
                              Log.error(
                                 "Invalid furniture '"
                                    + var71
                                    + "' for 'replace_furniture' action of item '"
                                    + this.aiB.getNamespacedID()
                                    + "'. File: "
                                    + this.aiB.lH().DJ()
                              );
                              break;
                           }

                           this.registerEffect(
                              var7,
                              var10,
                              new FurnitureSwapEffect(
                                    var56,
                                    var83,
                                    var11.getInt("decrement_durability", 0),
                                    var11.getInt("decrement_amount", 0),
                                    var11.getBoolean("copy_direction", true)
                                 )
                                 .cu(var11.getString("permission", null))
                                 .bu(var11.getInt("delay", -1))
                                 .method9775(this.parseEffectChainRule(var11))
                           );
                        } else if (var10.startsWith("remove_furniture")) {
                           this.registerEffect(var7, var10, new ActionEffect())
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("potion_effect")) {
                           PotionEffectType var35 = YamlConfig.jU(var11.getString("type"));
                           if (var35 == null) {
                              Log.error(
                                 "Invalid potion effect 'type' for action '"
                                    + var10
                                    + "' of item '"
                                    + this.aiB.getNamespacedID()
                                    + "'. File: "
                                    + this.aiB.lH().DJ()
                              );
                              continue;
                           }

                           this.registerEffect(
                                 var7,
                                 var10,
                                 new PotionGrantEffect(
                                    var35,
                                    var11.getInt("amplifier", 1),
                                    var11.getInt("duration", 40),
                                    var11.getBoolean("ambient"),
                                    var11.getBoolean("icon", true),
                                    var11.getBoolean("particles", true)
                                 )
                              )
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("remove_potion_effect")) {
                           PotionEffectType var36 = YamlConfig.jU(var11.getString("type"));
                           if (var36 == null) {
                              Log.error(
                                 "Invalid potion effect 'type' for action '"
                                    + var10
                                    + "' of item '"
                                    + this.aiB.getNamespacedID()
                                    + "'. File: "
                                    + this.aiB.lH().DJ()
                              );
                              continue;
                           }

                           this.registerEffect(var7, var10, new PotionRemoveEffect(var36))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("explosion")) {
                           this.registerEffect(
                                 var7,
                                 var10,
                                 new InteractionEffect(var11.getInt("power", 1), var11.getBoolean("fire", false), var11.getBoolean("break_blocks", true))
                              )
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("damage_near_entities")) {
                           List var37 = var11.getStringList("entity_groups");
                           ArrayList var57 = new ArrayList();

                           for (String var84 : var37) {
                              TargetCategory var88 = TargetCategory.parse(var84);
                              if (var88 == null) {
                                 Log.error(
                                    "&cUnknown entity group &e"
                                       + var84
                                       + "&c for action &e"
                                       + var10
                                       + "&c of item &e"
                                       + this.aiB.getNamespacedID()
                                       + ". Available entity groups: HOSTILE, PLAYERS, PASSIVE"
                                 );
                              } else {
                                 var57.add(var88);
                              }
                           }

                           int var74;
                           if (var2.jI(var7 + "." + var10 + ".range")) {
                              var74 = var11.getInt("range", 3);
                           } else {
                              var74 = var11.getInt("radius", 3);
                           }

                           this.registerEffect(var7, var10, new ProjectileSpreadEffect((float)var11.getDouble("damage", 1.0), var74, var57))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("damage_entity_in_sight")) {
                           this.registerEffect(var7, var10, new ScaledCountEffect((float)var11.getDouble("damage", 1.0), var11.getInt("distance", 3)))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("damage_entity")) {
                           this.registerEffect(var7, var10, new DamageMultiplierEffect((float)var11.getDouble("damage", 1.0)))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("target_potion_effect")) {
                           PotionEffectType var38 = YamlConfig.jU(var11.getString("type"));
                           if (var38 == null) {
                              Log.error(
                                 "Invalid potion effect 'type' for action '"
                                    + var10
                                    + "' of item '"
                                    + this.aiB.getNamespacedID()
                                    + "'. File: "
                                    + this.aiB.lH().DJ()
                              );
                              continue;
                           }

                           this.registerEffect(
                                 var7,
                                 var10,
                                 new PotionGrantEffectDup2(
                                    var38,
                                    var11.getInt("amplifier", 1),
                                    var11.getInt("duration", 40),
                                    var11.getBoolean("ambient"),
                                    var11.getBoolean("particles", true),
                                    var11.getBoolean("icon", true)
                                 )
                              )
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("target_remove_potion_effect")) {
                           this.registerEffect(var7, var10, new PotionRemoveEffectDup2(YamlConfig.jU(var11.getString("type"))))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("play_totem_animation")) {
                           String var39 = var8.getString(var10);
                           CustomItemDefinition var58 = var1.method8151(var39, false);
                           if (var58 == null) {
                              Log.error("&cUnknown totem &e" + var39 + "&c for action &e" + var10 + "&c of item &e" + this.aiB.getNamespacedID());
                              continue;
                           }

                           this.registerEffect(var7, var10, new ItemRefreshEffect(var58))
                              .cu(var11.getString("permission", null))
                              .bu(var11.getInt("delay", -1))
                              .method9775(this.parseEffectChainRule(var11));
                        } else if (var10.startsWith("script")) {
                           String var40 = var11.getString("name");
                           if (var40 == null) {
                              Log.error("&cMissing script name for action &e" + var10 + "&c of item &e" + this.aiB.getNamespacedID());
                              continue;
                           }

                           var40 = var40.replace(".jspp", "");
                           var40 = var40.replace(".js", "");
                           var40 = var40.replace(".java", "");
                           String var59 = var40;
                           if (!var59.contains(":")) {
                              var59 = this.aiB.getNamespace() + ":" + var40;
                           }

                           CompiledScript var75 = NamespaceRegistrar.do_(var59);
                           if (var75 == null) {
                              Log.error("&cScript &e" + var59 + "&c not found for action &e" + var10 + "&c of item &e" + this.aiB.getNamespacedID());
                              continue;
                           }

                           try {
                              this.registerEffect(var7, var10, new ScriptRunEffect(var59, var75))
                                 .cu(var11.getString("permission", null))
                                 .bu(var11.getInt("delay", -1))
                                 .method9775(this.parseEffectChainRule(var11));
                           } catch (Exception var19) {
                              Log.error("&cError loading script for action &e" + var10 + "&c of item &e" + this.aiB.getNamespacedID(), var19);
                           }
                        }

                        if (ContentManager.method8084().aF != null && ContentManager.method8084().aF.isEnabled()) {
                           if (var10.startsWith("increment_player_stat")) {
                              this.registerEffect(var7, var10, new SoundEffectDup2(var11.getString("name"), (float)var11.getDouble("amount")))
                                 .cu(var11.getString("permission", null))
                                 .bu(var11.getInt("delay", -1))
                                 .method9775(this.parseEffectChainRule(var11));
                           } else if (var10.startsWith("decrement_player_stat")) {
                              this.registerEffect(var7, var10, new SoundEffect(var11.getString("name"), (float)var11.getDouble("amount")))
                                 .cu(var11.getString("permission", null))
                                 .bu(var11.getInt("delay", -1))
                                 .method9775(this.parseEffectChainRule(var11));
                           }
                        }
                     } else if (var10.equals("cancel") && var8.get(var10) instanceof Boolean var12 && var12) {
                        this.registerEffect(var7, var10, new ScriptEffect());
                     } else if (var10.startsWith("remove_furniture") && var8.get(var10) instanceof Boolean var13 && var13) {
                        this.registerEffect(var7, var10, new ActionEffect());
                     }
                  }
               }
            }
         }
      }
   }

   public EffectChainRule parseEffectChainRule(ConfigurationSection var1) {
      for (EffectChainMode var5 : EffectChainMode.values()) {
         if (var1.contains(var5.key)) {
            String var6 = null;
            if (!(var1.get(var5.key) instanceof Boolean)) {
               var6 = var1.getString(var5.key);
            }

            return new EffectChainRule(var5, var6);
         }
      }

      return null;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public ItemEffect registerEffect(ItemEventType var1, String var2, ItemEffect var3) {
      List var4;
      if (this.aiC.containsKey(var1)) {
         var4 = (List)this.aiC.get(var1);
      } else {
         var4 = new ArrayList();
      }

      var4.add(var3);
      this.aiC.put(var1, var4);
      var3.cv(var2);
      if (!aiA) {
         switch (IntTableConstantsL.SWITCH_MAP_A[var1.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
               aiA = true;
         }
      }

      return var3;
   }

   public boolean hasEffects(ItemEventType var1) {
      return this.aiC.containsKey(var1);
   }

   public List getEffects(ItemEventType var1) {
      return this.hasEffects(var1) ? (List)this.aiC.get(var1) : new ArrayList();
   }

   @Deprecated
   public List getEffectsForEntity(LivingEntity var1, ItemEventType var2) {
      if (this.hasEffects(var2)) {
         if (this.aiB.Dz() && !var1.hasPermission(this.aiB.aou)) {
            Log.sendPrefixed(var1, Main.aFi.lj("no-permission-detail").replace("{permission}", this.aiB.aou));
            return new ArrayList();
         }

         if ((!(var1 instanceof Player) || ItemEventListener.Cp().method935(var1, this.aiB))
            && (
               !(var1 instanceof Player)
                  || this.aiB.bz((Player)var1)
                     && (
                        !(this.aiB instanceof CustomBlockDefinition) || ((CustomBlockDefinition)this.aiB).matchesRestrictedItem(((Player)var1).getItemInHand())
                     )
            )) {
            return (List)this.aiC.get(var2);
         }
      }

      return new ArrayList();
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public void dispatchEvent(Entity var1, ItemEventType var2, ResultConsumer var3) {
      if (var1 instanceof LivingEntity) {
         if (var1 instanceof Player var4) {
            if (var2 == ItemEventType.HELD || var2 == ItemEventType.HELD_OFFHAND) {
               ContentManager.method8084().aK.aiN.showBar(this.aiB, var4, true);
            } else if (var2 == ItemEventType.UNHELD || var2 == ItemEventType.UNHELD_OFFHAND) {
               ContentManager.method8084().aK.aiN.showBar(this.aiB, var4, false);
            }

            if (var2 == ItemEventType.PLACED_BLOCK_INTERACT
               || var2 == ItemEventType.PLACED_BLOCK_BREAK
               || var2 == ItemEventType.PLACED_FURNITURE_BREAK
               || var2 == ItemEventType.PLACED_FURNITURE_INTERACT
               || var2 == ItemEventType.PLACED_ARMORSTAND_INTERACT) {
               ContentManager.method8084().aK.aiN.showBarIndexed(this.aiB, var4, false, 25);
            }
         }

         if (this.hasEffects(var2)) {
            if (var1 instanceof Player var16 && aiA) {
               EquipmentSlot var5 = ItemEventType.method178(this.aiB, var2);
               if (var5 != null) {
                  if (var2 != ItemEventType.WEAR && var2 != ItemEventType.HELD && var2 != ItemEventType.HELD_OFFHAND) {
                     ContentManager.method8084().aR.alM.removeEquipmentEffect(var16, var5);
                  } else {
                     ContentManager.method8084().aR.alM.applyEquipmentEffect(var16, this.aiB, var5);
                  }
               }
            }

            if (this.aiB.Dz() && !var1.hasPermission(this.aiB.aou)) {
               Log.sendPrefixed(var1, Main.aFi.lj("no-permission-detail").replace("{permission}", this.aiB.aou));
               return;
            }

            boolean var17 = var1 instanceof Player;
            if ((!var17 || ItemEventListener.Cp().method935((LivingEntity)var1, this.aiB))
               && (
                  !var17
                     || this.aiB.bz((Player)var1)
                        && (
                           !(this.aiB instanceof CustomBlockDefinition)
                              || ((CustomBlockDefinition)this.aiB).matchesRestrictedItem(((Player)var1).getItemInHand())
                        )
               )) {
               boolean var18 = false;
               EffectResult var6 = null;
               boolean var7 = false;
               boolean var8 = false;
               List var9 = null;
               List var10 = null;
               List var11 = (List)this.aiC.get(var2);

               for (ItemEffect var13 : var11) {
                  if (var13.cq() && !var13.method9776(var1)) {
                     if (!var18) {
                        Log.sendPrefixed(var1, Main.aFi.lj("no-permission-detail").replace("{permission}", var13.iL));
                        var18 = true;
                     }
                  } else if (var13.cp()) {
                     SchedulerUtils.runLaterSync(() -> var3.accept(var13), var13.Cv());
                  } else if (var11.size() <= 1) {
                     var3.accept(var13);
                  } else {
                     EffectChainRule var14 = var13.Cu();
                     if (var14 != null && var6 != null) {
                        switch (IntTableConstantsL.SWITCH_MAP_B[var14.acR.ordinal()]) {
                           case 1:
                              if (var6 == EffectResult.SUCCESS) {
                                 return;
                              }
                              break;
                           case 2:
                              if (var6 == EffectResult.FAILED) {
                                 return;
                              }
                              break;
                           case 3:
                              if (var6 == EffectResult.SUCCESS) {
                                 continue;
                              }
                              break;
                           case 4:
                              if (var6 == EffectResult.FAILED) {
                                 continue;
                              }
                              break;
                           case 5:
                              if (var8) {
                                 return;
                              }
                              break;
                           case 6:
                              if (var7) {
                                 return;
                              }
                              break;
                           case 7:
                              if (var8) {
                                 continue;
                              }
                              break;
                           case 8:
                              if (var7) {
                                 continue;
                              }
                              break;
                           case 9:
                              if (var8 && CollectionUtils.listContains(var10, var14.acS)) {
                                 return;
                              }
                              break;
                           case 10:
                              if (var7 && CollectionUtils.listContains(var9, var14.acS)) {
                                 return;
                              }
                              break;
                           case 11:
                              if (var8 && CollectionUtils.listContains(var10, var14.acS)) {
                                 continue;
                              }
                              break;
                           case 12:
                              if (var7 && CollectionUtils.listContains(var9, var14.acS)) {
                                 continue;
                              }
                        }
                     }

                     EffectOutcome var15 = var3.accept(var13);
                     var6 = var15.ala;
                     if (var15.ala == EffectResult.SUCCESS) {
                        var8 = true;
                        var10 = CollectionUtils.addOrCreate(var10, var13.cr());
                     } else if (var15.ala == EffectResult.FAILED) {
                        var7 = true;
                        var9 = CollectionUtils.addOrCreate(var9, var13.cr());
                     }
                  }
               }
            }
         }
      }
   }

   public boolean isOnCooldown(ItemEventType var1) {
      return this.aiD.containsKey(var1);
   }

   public Long getCooldownMillis(ItemEventType var1) {
      return this.isOnCooldown(var1) ? (Long)this.aiD.get(var1) : 0L;
   }

   public void method9769(CustomItemDefinition var1) {
      this.method9770(var1, false);
   }

   public void method9770(CustomItemDefinition var1, boolean var2) {
      for (Entry var4 : this.aiC.entrySet()) {
         if (!var2 || !var1.aog.aiC.containsKey(var4.getKey())) {
            var1.aog.aiC.put((ItemEventType)var4.getKey(), (List)var4.getValue());
         }
      }

      var1.method8833(this.aiB.aot);
      var1.aou = this.aiB.aou;
   }
}
