package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.Log;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

final class ArmorEffectManager {
   private static final String alg = "itemsadder_armor_set";
   private static final int alh = 40;
   private final ContentManager ali;
   private final List alj = new ArrayList();
   private final Set alk = new HashSet();
   private final Map alm = new HashMap();
   private final Map aln = new HashMap();
   private final Set alo = new HashSet();
   private final Map alp = new HashMap();
   private BukkitTask mS;
   private boolean cz;
   private boolean alq;
   private int ticks;

   ArmorEffectManager(Plugin var1, ContentManager var2, Iterable var3) {
      this.ali = var2;

      for (YamlConfig var5 : var3) {
         this.method9862(var5);
      }

      if (!this.alj.isEmpty()) {
         this.mS = Bukkit.getScheduler().runTaskTimer(var1, this::tick, 1L, 1L);

         for (Player var7 : Bukkit.getOnlinePlayers()) {
            this.bq(var7);
         }
      }
   }

   boolean isEnabled() {
      return !this.alj.isEmpty();
   }

   private void method9862(YamlConfig var1) {
      ConfigurationSection var2 = var1.kb("armor_sets");
      if (var2 != null) {
         String var3 = var1.getString("info.namespace");

         for (String var5 : var2.getKeys(false)) {
            String var6 = var3 + ":" + var5;
            ConfigurationSection var7 = var2.getConfigurationSection(var5);

            try {
               if (!this.alk.add(var6)) {
                  throw new IllegalArgumentException("duplicate set id");
               }

               if (var7 == null) {
                  throw new IllegalArgumentException("expected a section");
               }

               ConfigurationSection var8 = getOrCreateSection(var7, "pieces");
               String[] var9 = new String[4];
               String[] var10 = new String[]{"head", "chest", "legs", "feet"};
               if (var8.getKeys(false).size() != 4) {
                  throw new IllegalArgumentException("pieces must contain head, chest, legs and feet");
               }

               for (int var11 = 0; var11 < 4; var11++) {
                  String var12 = var8.getString(var10[var11]);
                  if (var12 == null || var12.isBlank()) {
                     throw new IllegalArgumentException("missing pieces." + var10[var11]);
                  }

                  var9[var11] = var12.contains(":") ? var12 : var3 + ":" + var12;
                  if (this.ali.method8154(var9[var11]) == null) {
                     throw new IllegalArgumentException("unknown pieces." + var10[var11] + " item '" + var9[var11] + "'");
                  }
               }

               ConfigurationSection var28 = getOrCreateSection(var7, "bonuses");
               AttributeSetGroup[] var29 = new AttributeSetGroup[5];
               Iterator var13 = var28.getKeys(false).iterator();

               while (true) {
                  if (var13.hasNext()) {
                     String var14 = (String)var13.next();

                     int var15;
                     try {
                        var15 = Integer.parseInt(var14);
                     } catch (NumberFormatException var26) {
                        throw new IllegalArgumentException("bonus threshold must be 1, 2, 3 or 4");
                     }

                     if (var15 >= 1 && var15 <= 4) {
                        ConfigurationSection var16 = var28.getConfigurationSection(var14);
                        if (var16 == null) {
                           throw new IllegalArgumentException("bonuses." + var15 + " must be a section");
                        }

                        HashMap var17 = new HashMap();
                        ConfigurationSection var18 = var16.getConfigurationSection("attributes");
                        if (var16.contains("attributes") && var18 == null) {
                           throw new IllegalArgumentException("bonuses." + var15 + ".attributes must be a section");
                        }

                        if (var18 != null) {
                           for (String var20 : var18.getKeys(false)) {
                              Attribute var21 = parseAttribute(var20);
                              if (!(var21 != null && var18.get(var20) instanceof Number var23) || !Double.isFinite(var23.doubleValue())) {
                                 throw new IllegalArgumentException("invalid attribute '" + var20 + "' in bonuses." + var15);
                              }

                              UUID var24 = UUID.nameUUIDFromBytes((var6 + ":" + var20).getBytes(StandardCharsets.UTF_8));
                              var17.put(var21, new AttributeModifier(var24, "itemsadder_armor_set", var23.doubleValue(), Operation.ADD_NUMBER));
                           }
                        }

                        HashMap var30 = new HashMap();
                        ConfigurationSection var31 = var16.getConfigurationSection("effects");
                        if (var16.contains("effects") && var31 == null) {
                           throw new IllegalArgumentException("bonuses." + var15 + ".effects must be a section");
                        }

                        if (var31 != null) {
                           for (String var33 : var31.getKeys(false)) {
                              PotionEffectType var34 = YamlConfig.jU(var33);
                              if (!(var34 != null && var31.get(var33) instanceof Integer var25) || var25 < 1 || var25 > 256) {
                                 throw new IllegalArgumentException("invalid effect level for '" + var33 + "' in bonuses." + var15);
                              }

                              var30.put(var34, var25 - 1);
                           }
                        }

                        if (var17.isEmpty() && var30.isEmpty()) {
                           throw new IllegalArgumentException("bonuses." + var15 + " has no attributes or effects");
                        }

                        var29[var15] = new AttributeSetGroup(var17, var30);
                        continue;
                     }

                     throw new IllegalArgumentException("bonus threshold must be 1, 2, 3 or 4");
                  }

                  if (var28.getKeys(false).isEmpty()) {
                     throw new IllegalArgumentException("bonuses is empty");
                  }

                  this.alj.add(new AttributeSetRule(var9, var29));
                  break;
               }
            } catch (IllegalArgumentException var27) {
               Log.error("[Armor sets] '" + var6 + "': " + var27.getMessage() + ". File: " + var1.DJ());
            }
         }
      }
   }

   private static ConfigurationSection getOrCreateSection(ConfigurationSection var0, String var1) {
      ConfigurationSection var2 = var0.getConfigurationSection(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("missing " + var1 + " section");
      } else {
         return var2;
      }
   }

   private static Attribute parseAttribute(String var0) {
      for (Attribute var4 : Attribute.values()) {
         String var5 = var4.getKey().getKey();
         if (var5.equalsIgnoreCase(var0) || var5.endsWith("." + var0.toLowerCase())) {
            return var4;
         }
      }

      return null;
   }

   private void tick() {
      boolean var1 = ++this.ticks % 20 == 0;

      for (Player var3 : Bukkit.getOnlinePlayers()) {
         if (!var3.isDead()) {
            String[] var4 = getAttributeNames(var3);
            String[] var5 = (String[])this.alp.get(var3.getUniqueId());
            boolean var6 = var5 == null ? var4[0] != null || var4[1] != null || var4[2] != null || var4[3] != null : !Arrays.equals(var5, var4);
            if (var1 || var6 || this.alo.contains(var3.getUniqueId())) {
               this.applyAttributes(var3, var4);
            }
         }
      }
   }

   void bq(Player var1) {
      if (!this.cz && var1.isOnline() && !var1.isDead()) {
         this.applyAttributes(var1, getAttributeNames(var1));
      }
   }

   private static String[] getAttributeNames(Player var0) {
      ItemStack[] var1 = var0.getInventory().getArmorContents();
      return new String[]{
         CustomItemDefinition.method8749(var1[3]),
         CustomItemDefinition.method8749(var1[2]),
         CustomItemDefinition.method8749(var1[1]),
         CustomItemDefinition.method8749(var1[0])
      };
   }

   private void applyAttributes(Player var1, String[] var2) {
      UUID var3 = var1.getUniqueId();
      boolean var4 = var2[0] == null && var2[1] == null && var2[2] == null && var2[3] == null;
      if (var4) {
         this.alp.remove(var3);
      } else {
         this.alp.put(var3, var2);
      }

      if (var4 && !this.alm.containsKey(var3) && !this.aln.containsKey(var3)) {
         this.alo.remove(var3);
      } else {
         HashMap var5 = new HashMap();
         HashMap var6 = new HashMap();

         for (AttributeSetRule var8 : this.alj) {
            AttributeSetGroup var9 = var8.parseGroup(var2);
            if (var9 != null) {
               for (Entry var11 : var9.alv.entrySet()) {
                  var5.put(
                     ((AttributeModifier)var11.getValue()).getUniqueId(),
                     new AttributeModifierEntry((Attribute)var11.getKey(), (AttributeModifier)var11.getValue())
                  );
               }

               var9.alw.forEach((var1x, var2x) -> var6.merge(var1x, var2x, Math::max));
            }
         }

         Map var17 = this.alm.getOrDefault(var3, Map.of());

         for (Entry var21 : var17.entrySet()) {
            AttributeModifierEntry var25 = (AttributeModifierEntry)var5.get(var21.getKey());
            if (var25 == null || !var25.equals(var21.getValue())) {
               removeModifierEntry(var1, (AttributeModifierEntry)var21.getValue());
            }
         }

         for (Entry var22 : var5.entrySet()) {
            AttributeModifierEntry var26 = (AttributeModifierEntry)var17.get(var22.getKey());
            if (!((AttributeModifierEntry)var22.getValue()).equals(var26) || !hasModifier(var1, (AttributeModifierEntry)var22.getValue())) {
               addModifier(var1, (AttributeModifierEntry)var22.getValue());
            }
         }

         if (var5.isEmpty()) {
            this.alm.remove(var3);
         } else {
            this.alm.put(var3, var5);
         }

         Map var20 = this.aln.getOrDefault(var3, Map.of());

         for (Entry var27 : var20.entrySet()) {
            if (!((Integer)var27.getValue()).equals(var6.get(var27.getKey()))) {
               this.applyPotionEffect(var1, (PotionEffectType)var27.getKey(), (Integer)var27.getValue());
            }
         }

         HashMap var24 = new HashMap();

         for (Entry var30 : var6.entrySet()) {
            PotionEffect var12 = var1.getPotionEffect((PotionEffectType)var30.getKey());
            boolean var13 = ((Integer)var30.getValue()).equals(var20.get(var30.getKey())) && isPotionEffectMatching(var12, (Integer)var30.getValue());
            if (var12 == null || var12.getAmplifier() < (Integer)var30.getValue() || var13 && var12.getDuration() < 30) {
               this.alq = true;

               try {
                  if (var1.addPotionEffect(new PotionEffect((PotionEffectType)var30.getKey(), 40, (Integer)var30.getValue(), true, false, true))) {
                     var24.put((PotionEffectType)var30.getKey(), (Integer)var30.getValue());
                  }
               } finally {
                  this.alq = false;
               }
            } else if (var13) {
               var24.put((PotionEffectType)var30.getKey(), (Integer)var30.getValue());
            }
         }

         for (Entry var31 : var20.entrySet()) {
            if (!((Integer)var31.getValue()).equals(var6.get(var31.getKey()))
               && isPotionEffectMatching(var1.getPotionEffect((PotionEffectType)var31.getKey()), (Integer)var31.getValue())) {
               var24.putIfAbsent((PotionEffectType)var31.getKey(), (Integer)var31.getValue());
            }
         }

         if (var24.entrySet().stream().anyMatch(var1x -> !((Integer)var1x.getValue()).equals(var6.get(var1x.getKey())))) {
            this.alo.add(var3);
         } else {
            this.alo.remove(var3);
         }

         if (var24.isEmpty()) {
            this.aln.remove(var3);
         } else {
            this.aln.put(var3, var24);
         }
      }
   }

   private static void addModifier(Player var0, AttributeModifierEntry var1) {
      AttributeInstance var2 = var0.getAttribute(var1.alr);
      if (var2 != null) {
         for (AttributeModifier var4 : var2.getModifiers()) {
            if (var4.getUniqueId().equals(var1.als.getUniqueId())) {
               var2.removeModifier(var4);
            }
         }

         var2.addTransientModifier(var1.als);
      }
   }

   private static boolean hasModifier(Player var0, AttributeModifierEntry var1) {
      AttributeInstance var2 = var0.getAttribute(var1.alr);
      if (var2 == null) {
         return false;
      }

      for (AttributeModifier var4 : var2.getModifiers()) {
         if (var4.getUniqueId().equals(var1.als.getUniqueId())) {
            return var4.equals(var1.als);
         }
      }

      return false;
   }

   private static void removeModifierEntry(Player var0, AttributeModifierEntry var1) {
      AttributeInstance var2 = var0.getAttribute(var1.alr);
      if (var2 != null) {
         for (AttributeModifier var4 : var2.getModifiers()) {
            if (var4.getUniqueId().equals(var1.als.getUniqueId())) {
               var2.removeModifier(var4);
            }
         }
      }
   }

   private static boolean isPotionEffectMatching(PotionEffect var0, int var1) {
      return var0 != null && var0.getAmplifier() == var1 && var0.getDuration() <= 40 && var0.isAmbient() && !var0.hasParticles() && var0.hasIcon();
   }

   private void applyPotionEffect(Player var1, PotionEffectType var2, int var3) {
      if (isPotionEffectMatching(var1.getPotionEffect(var2), var3)) {
         this.alq = true;

         try {
            var1.removePotionEffect(var2);
         } finally {
            this.alq = false;
         }
      }
   }

   void onPotionEffect(EntityPotionEffectEvent var1) {
      if (!this.alq && var1.getEntity() instanceof Player var2) {
         Map var4 = (Map)this.aln.get(var2.getUniqueId());
         if (var4 != null) {
            var4.remove(var1.getModifiedType());
            if (var4.isEmpty()) {
               this.aln.remove(var2.getUniqueId());
            }
         }
      }
   }

   void bs(Player var1) {
      this.alp.remove(var1.getUniqueId());
      this.alo.remove(var1.getUniqueId());
      Map var2 = (Map)this.alm.remove(var1.getUniqueId());
      if (var2 != null) {
         for (AttributeModifierEntry var4 : var2.values()) {
            removeModifierEntry(var1, var4);
         }
      }

      Map var6 = (Map)this.aln.remove(var1.getUniqueId());
      if (var6 != null) {
         for (Entry var5 : var6.entrySet()) {
            this.applyPotionEffect(var1, (PotionEffectType)var5.getKey(), (Integer)var5.getValue());
         }
      }
   }

   void close() {
      this.cz = true;
      if (this.mS != null) {
         this.mS.cancel();
      }

      for (Player var2 : Bukkit.getOnlinePlayers()) {
         this.bs(var2);
      }

      this.alm.clear();
      this.aln.clear();
      this.alo.clear();
      this.alp.clear();
   }
}
