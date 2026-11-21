package dev.lone.itemsadder.loot;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.NamedFlags;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.entity.CustomEntityImpl;
import dev.lone.itemsadder.entity.CustomEntityManager;
import dev.lone.itemsadder.entity.CustomMobSettings;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.EffectPlaybackSet;
import dev.lone.itemsadder.item.ItemVariantHolder;
import dev.lone.itemsadder.item.TimedStep;
import dev.lone.itemsadder.util.BukkitRegistryLookup;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.world.ChunkBlockHookHolder;
import dev.lone.itemsadder.world.InstrumentRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public class LootManager {
   static final NamespacedKey atl = new NamespacedKey(Main.getPlugin(), "spawned_from_spawner");
   final Plugin atm;
   final ContentManager atn;
   public HashMap ato = new HashMap();
   public HashMap atp = new HashMap();
   public List atq = new ArrayList();
   public List atr = new ArrayList();
   public HashMap ats = new HashMap();
   public HashMap att = new HashMap();
   public LootDropListener atu;

   public LootManager(Plugin var1, ContentManager var2) {
      this.atn = var2;
      this.atm = var1;
      this.atu = new LootDropListener();
      this.atu.GZ();
   }

   public void method1980(Collection var1) {
      for (YamlConfig var3 : var1) {
         this.method1981(var3);
      }
   }

   public void method1981(YamlConfig var1) {
      if (!var1.contains("loots")) {
         if (Settings.get().isLoggingEnabled("logging.loots.loading_empty")) {
            Log.log(ChatColor.YELLOW + "    No loots found in file " + var1.GQ());
         }
      } else {
         for (String var3 : var1.getStringList("loots")) {
            String var4 = "loots." + var3;
            switch (var3) {
               case "mobs":
                  for (String var21 : var1.getStringList(var4)) {
                     String var23 = var4 + "." + var21;
                     if (var1.getBoolean(var23 + ".enabled", true)) {
                        String var25 = var1.getString(var23 + ".type");
                        if (var25 == null) {
                           Log.error("Missing 'type' in loot '" + var21 + "'. File " + var1.DJ());
                        } else {
                           EntityType var26 = BukkitRegistryLookup.entityType(var25);
                           LootTable var28;
                           if (var26 != null) {
                              var28 = new EntityLootTable(var26, var1.getBoolean(var23 + ".ignore_spawner", true));
                              EntityLootTable var30 = (EntityLootTable)var28;
                              this.ats.computeIfAbsent(var30.getType(), var0 -> new ArrayList<>()).add(var30);
                           } else {
                              CustomMobSettings var31 = (CustomMobSettings)CustomEntityManager.getInstance().oN.get(var25);
                              if (var31 == null) {
                                 Log.error("Unknown entity '" + var25 + "' not found for loot '" + var21 + "'. File: " + var1.DJ());
                                 continue;
                              }

                              var28 = new NamespacedLootEntry(var31.vn.getNamespacedID(), var1.getBoolean(var23 + ".ignore_spawner", true));
                              NamespacedLootEntry var34 = (NamespacedLootEntry)var28;
                              this.att.computeIfAbsent(var34.Eo(), var0 -> new ArrayList<>()).add(var34);
                           }

                           if (var1.contains(var23 + ".nbt")) {
                              for (String var35 : var1.getStringList(var23 + ".nbt")) {
                                 String var37 = var23 + ".nbt." + var35;
                                 NbtDropTrigger var39 = new NbtDropTrigger(
                                    var1.getString(var37 + ".path"), var1.getString(var37 + ".value"), var1.getString(var37 + ".type")
                                 );
                                 var28.method1997(var39);
                              }
                           }

                           if (var1.contains(var23 + ".metadata")) {
                              for (String var36 : var1.getStringList(var23 + ".metadata")) {
                                 String var38 = var23 + ".metadata." + var36;
                                 String var40 = var1.getString(var38 + ".name");
                                 if (var40.equals("ItemsAdderMob")) {
                                    var40 = "ItemsAdderEntity";
                                 }

                                 MetadataDropTrigger var17 = new MetadataDropTrigger(
                                    var40, var1.getString(var38 + ".value"), var1.getString(var38 + ".type", "string")
                                 );
                                 var28.method1998(var17);
                              }
                           }

                           this.method1982(var28, var1, var23);
                           this.atr.add(var28);
                        }
                     } else if (Settings.get().isLoggingEnabled("logging.loots.loading_disabled")) {
                        Log.log("    Ignored disabled loot: " + var21);
                     }
                  }
                  break;
               case "blocks":
                  for (String var20 : var1.getStringList(var4)) {
                     String var22 = var4 + "." + var20;
                     if (var1.getBoolean(var22 + ".enabled", true)) {
                        String var24 = var1.getString(var22 + ".type");
                        if (var24 == null) {
                           Log.error("Missing 'type' in loot '" + var20 + "'. File " + var1.DJ());
                        } else {
                           Material var11 = BukkitRegistryLookup.material(var24);
                           if (var11 == null) {
                              if (!this.atn.method8155(var24)) {
                                 Log.error("Block '" + var24 + "' not found for loot '" + var20 + "'. File: " + var1.DJ());
                              } else {
                                 CustomItemDefinition var27 = this.atn.method8154(var24);
                                 if (var27 instanceof CustomBlockDefinition) {
                                    BlockToolDropRule var29 = new BlockToolDropRule((CustomBlockDefinition)var27);
                                    this.method1982(var29, var1, var22);
                                    this.atp.computeIfAbsent((CustomBlockDefinition)var27, var0 -> new ArrayList<>()).add(var29);
                                    if (Settings.get().isLoggingEnabled("logging.loots.loading")) {
                                       Log.log(ChatColor.AQUA + "Registered block loot: " + var29.Bu().getNamespacedID());
                                    }
                                 } else {
                                    Log.error(
                                       "Error: You have to set a valid BLOCK as the 'type' of block loot '"
                                          + var20
                                          + "'. You set "
                                          + var24
                                          + "' which is NOT a block. File: "
                                          + var1.GQ()
                                    );
                                 }
                              }
                           } else {
                              MaterialToolDropRule var12 = new MaterialToolDropRule(var11);
                              if (var1.contains(var22 + ".nbt")) {
                                 if (InstrumentRegistry.method2792(var12.Ep())) {
                                    for (String var14 : var1.getStringList(var22 + ".nbt")) {
                                       String var15 = var22 + ".nbt." + var14;
                                       NbtDropTrigger var16 = new NbtDropTrigger(
                                          var1.getString(var15 + ".path"), var1.getString(var15 + ".value"), var1.getString(var15 + ".type")
                                       );
                                       var12.method1997(var16);
                                    }
                                 } else {
                                    Log.error(
                                       "Ignored NBT rule of loot for block: "
                                          + ChatColor.AQUA
                                          + var20
                                          + ChatColor.RED
                                          + ". File "
                                          + ChatColor.AQUA
                                          + var1.DJ()
                                          + ChatColor.RED
                                          + " ("
                                          + ChatColor.AQUA
                                          + var12.Ep()
                                          + ChatColor.RED
                                          + " is not a tile entity!)"
                                    );
                                 }
                              }

                              this.method1982(var12, var1, var22);
                              this.ato.computeIfAbsent(Material.getMaterial(var24), var0 -> new ArrayList<>()).add(var12);
                              if (Settings.get().isLoggingEnabled("logging.loots.loading")) {
                                 Log.log(ChatColor.AQUA + "Registered block loot: " + var12.Ep());
                              }
                           }
                        }
                     } else if (Settings.get().isLoggingEnabled("logging.loots.loading_disabled")) {
                        Log.log("    Ignored disabled loot: " + var20);
                     }
                  }
                  break;
               case "fishing":
                  for (String var8 : var1.getStringList(var4)) {
                     String var9 = var4 + "." + var8;
                     if (!var1.getBoolean(var9 + ".enabled", true)) {
                        if (Settings.get().isLoggingEnabled("logging.loots.loading_disabled")) {
                           Log.log("    Ignored disabled loot: " + var8);
                        }
                     } else {
                        ItemDropApplier var10 = new ItemDropApplier();
                        this.method1982(var10, var1, var9);
                        this.atq.add(var10);
                     }
                  }
            }
         }
      }
   }

   private void method1982(LootDropRules var1, YamlConfig var2, String var3) {
      for (String var5 : var2.getStringList(var3 + ".items")) {
         String var6 = var3 + ".items." + var5;
         String var7 = var2.getString(var6 + ".item");
         ItemStack var8 = this.atn.method8178(var7);
         if (var8 == null) {
            Log.error("Item '" + var7 + "' not found for loot '" + var3 + "'. File: " + var2.DJ());
         } else {
            ItemVariantHolder var9 = new ItemVariantHolder(
               var8,
               var2.getInt(var6 + ".min_amount", 1),
               var2.getInt(var6 + ".max_amount", 1),
               var2.getFloat(var6 + ".chance", 100.0F),
               var2.getBoolean(var6 + ".ignore_fortune", false)
            );
            if (var9.Fr() > var9.Fs()) {
               Log.error(
                  "Invalid loot. 'min_amount' "
                     + var9.Fr()
                     + " greater than 'max_amount' "
                     + var9.Fs()
                     + "! Must be lower. Loot '"
                     + var3
                     + "'. File: "
                     + var2.DJ()
               );
            } else {
               var1.method1999(var9);
            }
         }
      }

      if (var2.contains(var3 + ".exp")) {
         for (String var16 : var2.getStringList(var3 + ".exp")) {
            String var20 = var3 + ".exp." + var16;
            TimedStep var23 = new TimedStep(var2.getInt(var20 + ".min_amount"), var2.getInt(var20 + ".max_amount"), var2.getFloat(var20 + ".chance"));
            var1.method2000(var23);
         }
      }

      if (var2.contains(var3 + ".commands")) {
         ConfigSectionWrapper var13 = var2.kc(var3 + ".commands");
         assert var13 != null;

         for (String var21 : var13.abO()) {
            ConfigSectionWrapper var24 = var13.kc(var21);
            assert var24 != null;
            ArrayList var25 = new ArrayList();

            for (String var10 : var24.abO()) {
               if (!var10.equals("chance")) {
                  ConfigSectionWrapper var11 = var24.kc(var10);
                  assert var11 != null;
                  var25.add(new NamedFlags(var11.jO("command"), var11.method7078("as_console", true), var11.method7078("silent", false)));
               }
            }

            var1.method2001(new EffectPlaybackSet(var25, var24.jP("chance")));
         }
      }

      if (var2.contains(var3 + ".biomes")) {
         for (String var18 : var2.jZ(var3 + ".biomes")) {
            NamespacedKey var22 = ChunkBlockHookHolder.getInstance().hook.parseBiomeKey(var18.toUpperCase());
            if (var22 != null) {
               var1.method2008(var22);
            }
         }
      }

      if (var2.contains(var3 + ".worlds")) {
         for (String var19 : var2.jZ(var3 + ".worlds")) {
            var1.dz(var19);
         }
      }

      var1.method2011(var2.kc(var3));
      var1.bg(var2.getBoolean(var3 + ".stop_on_first_success", var2.getBoolean(var3 + ".drop_only_first", false)));
   }

   @Nullable
   public List method1983(Block var1, Material var2) {
      return this.method1984(var1, var2, null);
   }

   @Nullable
   public List method1984(Block var1, Material var2, @Nullable Player var3) {
      ArrayList var4 = new ArrayList();
      List var5 = (List)this.ato.get(var2);
      if (var5 == null) {
         return null;
      }

      Biome var6 = var1.getBiome();
      World var7 = var1.getWorld();

      for (MaterialToolDropRule var9 : var5) {
         if (var9.method2009(var6) && var9.method2010(var7) && var9.method2012(var1.getLocation(), var3)) {
            var4.add(var9);
         }
      }

      return var4;
   }

   @Nullable
   public List method1985(Block var1, CustomBlockDefinition var2) {
      return this.method1986(var1, var2, null);
   }

   @Nullable
   public List method1986(Block var1, CustomBlockDefinition var2, @Nullable Player var3) {
      ArrayList var4 = new ArrayList();
      List var5 = (List)this.atp.get(var2);
      if (var5 == null) {
         return null;
      }

      Biome var6 = var1.getBiome();
      World var7 = var1.getWorld();

      for (BlockToolDropRule var9 : var5) {
         if (var9.method2009(var6) && var9.method2010(var7) && var9.method2012(var1.getLocation(), var3)) {
            var4.add(var9);
         }
      }

      return var4;
   }

   @Nullable
   public List aI(Block var1) {
      return this.method1987(var1, null);
   }

   @Nullable
   public List method1987(Block var1, @Nullable Player var2) {
      ArrayList var3 = new ArrayList();
      Biome var4 = var1.getBiome();
      World var5 = var1.getWorld();

      for (ItemDropApplier var7 : this.atq) {
         if (var7.method2009(var4) && var7.method2010(var5) && var7.method2012(var1.getLocation(), var2)) {
            var3.add(var7);
         }
      }

      return var3;
   }

   @Nullable
   public List method1988(LivingEntity var1) {
      ArrayList var2 = new ArrayList();
      List var3 = (List)this.ats.get(var1.getType());
      if (var3 == null) {
         return null;
      }

      Block var4 = var1.getLocation().getBlock();
      Biome var5 = var4.getBiome();
      World var6 = var4.getWorld();

      for (EntityLootTable var8 : var3) {
         if (method1991(var1, var5, var6, var8, var1.getKiller())) {
            var2.add(var8);
         }
      }

      return var2;
   }

   @Nullable
   public List method1989(CustomEntityImpl var1) {
      return this.method1990(var1, null);
   }

   @Nullable
   public List method1990(CustomEntityImpl var1, @Nullable Player var2) {
      LivingEntity var3 = var1.getLivingEntity();
      if (var3 == null) {
         return null;
      }

      ArrayList var4 = new ArrayList();
      List var5 = (List)this.att.get(var1.qK.vn.getNamespacedID());
      if (var5 == null) {
         return null;
      }

      Block var6 = var3.getLocation().getBlock();
      Biome var7 = var6.getBiome();
      World var8 = var6.getWorld();

      for (NamespacedLootEntry var10 : var5) {
         if (method1991(var3, var7, var8, var10, var2)) {
            var4.add(var10);
         }
      }

      return var4;
   }

   private static boolean method1991(LivingEntity var0, Biome var1, World var2, LootTable var3, @Nullable Player var4) {
      if (var3.method2009(var1) && var3.method2010(var2) && var3.method2012(var0.getLocation(), var4)) {
         if (var3.Hd() && (Boolean)var0.getPersistentDataContainer().getOrDefault(atl, PersistentDataType.BOOLEAN, false)) {
            return false;
         }

         if (var3.Ha()) {
            for (DropTrigger var6 : var3.Hc()) {
               if (!var6.method2018(var0)) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }
}
