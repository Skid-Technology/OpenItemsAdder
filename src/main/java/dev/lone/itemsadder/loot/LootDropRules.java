package dev.lone.itemsadder.loot;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.item.EffectPlaybackSet;
import dev.lone.itemsadder.item.ItemVariantHolder;
import dev.lone.itemsadder.item.TimedStep;
import dev.lone.itemsadder.util.LocationScanner;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class LootDropRules {
   private static final String[] atB = new String[]{
      "FULL_MOON", "WANING_GIBBOUS", "LAST_QUARTER", "WANING_CRESCENT", "NEW_MOON", "WAXING_CRESCENT", "FIRST_QUARTER", "WAXING_GIBBOUS"
   };
   protected final List atC;
   protected final List atD;
   protected final List atE;
   protected final HashSet atF;
   protected final HashSet atG;
   protected boolean atH;
   private boolean atI;
   private Integer atJ;
   private Integer atK;
   private Double atL;
   private Double atM;
   private boolean atN;
   private boolean atO;
   private boolean atP;
   private Set atQ;
   private Set atR;
   private Set atS;

   public LootDropRules() {
      this.atC = new ArrayList();
      this.atD = new ArrayList();
      this.atE = new ArrayList();
      this.atF = new HashSet();
      this.atG = new HashSet();
      this.atQ = new HashSet();
      this.atR = new HashSet();
      this.atS = new HashSet();
   }

   public void method1999(ItemVariantHolder var1) {
      this.atC.add(var1);
   }

   public void method2000(TimedStep var1) {
      this.atD.add(var1);
   }

   public void method2001(EffectPlaybackSet var1) {
      this.atE.add(var1);
   }

   public void method2002(Location var1) {
      this.method2003(null, var1);
   }

   public boolean method2003(@Nullable Player var1, Location var2) {
      boolean var3 = false;
      ItemStack var4 = null;
      if (var1 != null) {
         var4 = var1.getInventory().getItemInMainHand();
      }

      var3 = this.method2004(var1, var4, var2);
      this.method2006(var4, var2);
      this.method2007(var1, var2);
      return var3;
   }

   public boolean method2004(@Nullable Player var1, @Nullable ItemStack var2, Location var3) {
      boolean var4 = false;

      for (ItemVariantHolder var6 : this.atC) {
         if (var6.gC()) {
            ItemStack var7 = var6.method10017(var2, true);
            if (var7.getAmount() > 0) {
               var4 = true;
               LocationScanner.dropItemNear(var3, var7, var1);
               if (this.atH) {
                  return var4;
               }
            }
         }
      }

      return var4;
   }

   public List method2005(@Nullable ItemStack var1) {
      ArrayList var2 = new ArrayList();

      for (ItemVariantHolder var4 : this.atC) {
         if (var4.gC()) {
            ItemStack var5 = var4.method10017(var1, true);
            if (var5.getAmount() > 0) {
               var2.add(var5);
               if (this.atH) {
                  break;
               }
            }
         }
      }

      return var2;
   }

   public void method2006(@Nullable ItemStack var1, Location var2) {
      for (TimedStep var4 : this.atD) {
         double var5 = Math.random();
         if (!(var5 * 100.0 > var4.gF())) {
            ((ExperienceOrb)var2.getWorld().spawn(var2, ExperienceOrb.class)).setExperience(Utils.randomInt(var4.Fn(), var4.Fo()));
         }
      }
   }

   public void method2007(@Nullable Player var1, Location var2) {
      for (EffectPlaybackSet var4 : this.atE) {
         var4.method72(var1, var2);
      }
   }

   public void method2008(NamespacedKey var1) {
      this.atF.add(var1);
   }

   public boolean method2009(Biome var1) {
      if (var1 == null) {
         return false;
      }

      if (this.atF.isEmpty()) {
         return true;
      }

      try {
         return this.atF.contains(var1.getKey());
      } catch (NullPointerException var3) {
         return false;
      }
   }

   public boolean method2010(World var1) {
      if (this.atG.isEmpty()) {
         return true;
      }

      String var2 = var1.getName();
      if (this.atG.contains(var2)) {
         return true;
      }

      if (this.atI) {
         for (String var4 : this.atG) {
            if (var4.endsWith("*")) {
               var4 = var4.substring(0, var4.length() - 1);
               if (var4.startsWith("!")) {
                  var4 = var4.substring(1);
                  if (var2.startsWith(var4)) {
                     return false;
                  }
               } else if (var2.startsWith(var4)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public void dz(String var1) {
      if (var1.endsWith("*")) {
         this.atI = true;
      }

      this.atG.add(var1);
   }

   public void method2011(ConfigSectionWrapper var1) {
      this.atJ = var1.method7077("height_below", null);
      this.atK = var1.method7077("height_above", null);
      this.atL = var1.method7103("temperature_below", null);
      this.atM = var1.method7103("temperature_above", null);
      this.atN = var1.jI("weather");
      this.atO = var1.jI("time");
      this.atP = var1.jI("moon_phase");
      this.atQ.addAll(var1.jZ("weather").stream().map(var0 -> var0.toUpperCase(Locale.ROOT)).toList());
      this.atR.addAll(var1.jZ("time").stream().map(var0 -> var0.toUpperCase(Locale.ROOT)).toList());
      this.atS.addAll(var1.jZ("moon_phase").stream().map(var0 -> var0.toUpperCase(Locale.ROOT)).toList());

      for (String var3 : this.atQ) {
         if (!Set.of("CLEAR", "RAIN", "THUNDER").contains(var3)) {
            Log.warn("Unknown loot weather '" + var3 + "' in " + var1.abQ());
         }
      }

      for (String var6 : this.atR) {
         if (!Set.of("DAY", "NIGHT").contains(var6)) {
            Log.warn("Unknown loot time '" + var6 + "' in " + var1.abQ());
         }
      }

      for (String var7 : this.atS) {
         if (!List.of(atB).contains(var7)) {
            Log.warn("Unknown loot moon phase '" + var7 + "' in " + var1.abQ());
         }
      }
   }

   public boolean method2012(Location var1, @Nullable Player var2) {
      if (this.atJ == null && this.atK == null && this.atL == null && this.atM == null && !this.atN && !this.atO && !this.atP) {
         return true;
      }

      World var3 = var1.getWorld();
      Integer var4 = var2 == null ? null : var2.getLocation().getBlockY();
      return this.method2013(var4, var1.getBlock().getTemperature(), var3.hasStorm(), var3.isThundering(), var3.getTime(), var3.getFullTime());
   }

   boolean method2013(@Nullable Integer var1, double var2, boolean var4, boolean var5, long var6, long var8) {
      if ((this.atJ != null || this.atK != null) && var1 == null) {
         return false;
      }

      if ((this.atJ == null || var1 < this.atJ) && (this.atK == null || var1 > this.atK)) {
         if ((this.atL == null || !(var2 >= this.atL)) && (this.atM == null || !(var2 <= this.atM))) {
            if (this.atN && !this.atQ.contains(var5 ? "THUNDER" : (var4 ? "RAIN" : "CLEAR"))) {
               return false;
            } else {
               return this.atO && !this.atR.contains(var6 < 12000L ? "DAY" : "NIGHT")
                  ? false
                  : !this.atP || this.atS.contains(atB[Math.floorMod(var8 / 24000L, 8)]);
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Generated
   private static List He() {
      return new ArrayList();
   }

   @Generated
   private static List Hf() {
      return new ArrayList();
   }

   @Generated
   private static List Hg() {
      return new ArrayList();
   }

   @Generated
   private static HashSet Hh() {
      return new HashSet();
   }

   @Generated
   private static HashSet Hi() {
      return new HashSet();
   }

   @Generated
   private static Set Hj() {
      return new HashSet();
   }

   @Generated
   private static Set Hk() {
      return new HashSet();
   }

   @Generated
   private static Set Hl() {
      return new HashSet();
   }

   @Generated
   protected LootDropRules(AbstractLootConditions var1) {
      if (var1.atT) {
         this.atC = var1.atU;
      } else {
         this.atC = He();
      }

      if (var1.atV) {
         this.atD = var1.atW;
      } else {
         this.atD = Hf();
      }

      if (var1.atX) {
         this.atE = var1.atY;
      } else {
         this.atE = Hg();
      }

      if (var1.atZ) {
         this.atF = var1.aua;
      } else {
         this.atF = Hh();
      }

      if (var1.aub) {
         this.atG = var1.auc;
      } else {
         this.atG = Hi();
      }

      this.atH = var1.atH;
      this.atI = var1.atI;
      this.atJ = var1.atJ;
      this.atK = var1.atK;
      this.atL = var1.atL;
      this.atM = var1.atM;
      this.atN = var1.atN;
      this.atO = var1.atO;
      this.atP = var1.atP;
      if (var1.aud) {
         this.atQ = var1.aue;
      } else {
         this.atQ = Hj();
      }

      if (var1.auf) {
         this.atR = var1.aug;
      } else {
         this.atR = Hk();
      }

      if (var1.auh) {
         this.atS = var1.aui;
      } else {
         this.atS = Hl();
      }
   }

   @Generated
   public static AbstractLootConditions Hm() {
      return new LootConditionsBuilder();
   }

   @Generated
   public List Hn() {
      return this.atC;
   }

   @Generated
   public List Ho() {
      return this.atD;
   }

   @Generated
   public List Hp() {
      return this.atE;
   }

   @Generated
   public HashSet Hq() {
      return this.atF;
   }

   @Generated
   public void bg(boolean var1) {
      this.atH = var1;
   }

   @Generated
   public boolean Hr() {
      return this.atH;
   }
}
