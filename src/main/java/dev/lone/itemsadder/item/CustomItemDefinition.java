package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.Version;
import beer.devs.fastnbt.nms.nbt.NBT;
import beer.devs.fastnbt.nms.nbt.NBTType;
import beer.devs.fastnbt.nms.nbt.NCompound;
import beer.devs.fastnbt.nms.nbt.NItem;
import beer.devs.fastnbt.nms.nbt.NList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespacedIdentity;
import dev.lone.itemsadder.fontimage.FontImageLoader;
import dev.lone.itemsadder.text.NmsChatService;
import dev.lone.itemsadder.util.AdventureText;
import dev.lone.itemsadder.util.BossBarSpec;
import dev.lone.itemsadder.util.ComponentLabel;
import dev.lone.itemsadder.util.ComponentMapper;
import dev.lone.itemsadder.util.ComponentResult;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.ResultConsumer;
import dev.lone.itemsadder.util.SchedulerUtils;
import dev.lone.itemsadder.util.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import lombok.Generated;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomItemDefinition extends ItemStack implements NamespacedIdentity {
   private static final double anR = -1.0;
   private static final double anS = -4.0;
   protected NItem anT;
   @Deprecated
   private String As;
   @Deprecated
   private String anU;
   @Deprecated
   private List anV;
   @Deprecated
   @Nullable
   private String anW;
   @Deprecated
   @Nullable
   private String anX;
   @Deprecated
   private boolean anY;
   @Nullable
   public ItemSpec anZ;
   @Nullable
   public String aoa;
   @Nullable
   private ItemRuleSpec aob;
   final YamlConfig aoc;
   @Nullable
   Long aod;
   @Nullable
   private NamespacedKey aoe;
   ConfigSectionWrapper aof;
   public final ItemEventDispatcher aog;
   public final ItemActionRegistry aoh;
   public final String aoi;
   public final String aoj;
   public final String aok;
   public final int aol;
   @Nullable
   Integer aom;
   @Nullable
   Integer aon;
   boolean aoo = true;
   @Nullable
   ItemUpdatePolicy aop;
   public String YK;
   @Nullable
   public ArrayList aoq;
   public boolean aor;
   ImmutableMap aos = ImmutableMap.of();
   public long aot = -1L;
   public String aou;
   private HashMap aov;
   private boolean aow;
   private String aox;
   public boolean aoy;
   private final boolean aoz;
   @Nullable
   private BossBarSpec aoA;
   @Nullable
   private ItemFeatureSpec aoB;
   private Set aoC = new HashSet();
   private boolean aoD = true;
   static final int aoE = 43520;
   static final ComponentLabel aoF = new ComponentLabel("item.modifiers.head");
   static final ComponentLabel aoG = new ComponentLabel("item.modifiers.chest");
   static final ComponentLabel aoH = new ComponentLabel("item.modifiers.legs");
   static final ComponentLabel aoI = new ComponentLabel("item.modifiers.feet");
   static final ComponentLabel aoJ = new ComponentLabel("item.modifiers.mainhand");
   static final ComponentLabel aoK = new ComponentLabel("item.modifiers.offhand");
   static final ComponentLabel aoL = new ComponentLabel("attribute.name.generic.attack_damage", 43520);
   static final ComponentLabel aoM = new ComponentLabel("attribute.name.attack_damage", 43520);
   static final ComponentLabel aoN = new ComponentLabel("attribute.name.generic.attack_speed", 43520);
   static final ComponentLabel aoO = new ComponentLabel("attribute.name.attack_speed", 43520);
   static final ComponentLabel aoP = new ComponentLabel("attribute.name.generic.attack_knockback", 43520);
   static final ComponentLabel aoQ = new ComponentLabel("attribute.name.attack_knockback", 43520);
   static final ComponentLabel aoR = new ComponentLabel("attribute.name.player.block_interaction_range", 43520);
   static final ComponentLabel aoS = new ComponentLabel("attribute.name.block_interaction_range", 43520);
   static final ComponentLabel aoT = new ComponentLabel("attribute.name.player.entity_interaction_range", 43520);
   static final ComponentLabel aoU = new ComponentLabel("attribute.name.entity_interaction_range", 43520);

   public CustomItemDefinition(YamlConfig var1, String var2, String var3, Material var4) {
      super(var4);
      this.aoc = var1;
      this.aoj = var3;
      this.aoi = var2;
      this.aok = var2 + ":" + var3;
      this.aol = this.aok.hashCode();
      this.anT = new NItem(this);
      this.Dp();
      this.aog = new ItemEventDispatcher(this);
      this.aoh = new ItemActionRegistry(this);
      this.aoz = ItemStackUtils.ac(var4);
   }

   CustomItemDefinition(YamlConfig var1, String var2, String var3, int var4, Material var5) {
      super(var5);
      this.aol = var4;
      this.aoc = var1;
      this.aoj = var3;
      this.aoi = var2;
      this.aok = var2 + ":" + var3;
      this.anT = new NItem(this);
      this.Dp();
      this.aog = new ItemEventDispatcher(this);
      this.aoh = new ItemActionRegistry(this);
      this.aoz = ItemStackUtils.ac(var5);
   }

   public static void method8745(ItemStack var0, String var1, String var2) {
      NItem var3 = new NItem(var0);
      var3.getOrAddCompound("itemsadder").setString("namespace", var1);
      var3.getOrAddCompound("itemsadder").setString("id", var2);
      var3.save();
   }

   private void Dp() {
      this.anT.getOrAddCompound("itemsadder").setString("namespace", this.aoi);
      this.anT.getOrAddCompound("itemsadder").setString("id", this.aoj);
      this.anT.save();
   }

   public void method8746(@Nullable ItemSpec var1) {
      this.anZ = var1;
      assert var1 != null;
      if (Version.isAtLeast(Version.v1_21_3)) {
         ItemOperationsService.getRegistryBridge().method3437(this, var1.Ei());
      }
   }

   public void cC(String var1) {
      this.aoa = var1;
      ItemOperationsService.getRegistryBridge().method3437(this, var1);
      this.DC();
   }

   public boolean setItemMeta(@Nullable ItemMeta itemMeta) {
      boolean var2 = super.setItemMeta(itemMeta);
      if (!var2) {
         return false;
      }

      this.DC();
      return var2;
   }

   public void method8747(ItemStack var1) {
      NItem var2 = new NItem(var1);
      this.anT.merge(var2);
   }

   public void cD(String var1) {
      try {
         ItemOperationsService.getRegistryBridge().method3429(this.anT, var1);
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }

   public CustomItemDefinition Dq() {
      CustomItemDefinition var1 = new CustomItemDefinition(this.aoc, this.aoi, this.aoj, this.aol, this.getType());
      var1.setItemMeta(this.getItemMeta());
      var1.cJ(this.getPermissionSuffix());
      return var1;
   }

   public CustomItemDefinition Dr() {
      CustomItemDefinition var1 = new CustomItemDefinition(this.aoc, this.aoi, this.aoj, this.aol, this.getType());
      var1.setItemMeta(this.getItemMeta());
      var1.cJ(this.getPermissionSuffix());
      var1.aoh.copyActionsTo(var1);
      var1.aog.method9769(var1);
      return var1;
   }

   @NotNull
   public ItemStack Ds() {
      ItemStack var1 = new ItemStack(this.getType());
      var1.setAmount(this.getAmount());
      var1.setItemMeta(this.getItemMeta());
      return var1;
   }

   public static boolean isCustomItem(@Nullable ItemStack itemStack) {
      if (itemStack == null || itemStack.getType() == Material.AIR) {
         return false;
      } else {
         return !NItem.hasNBT(itemStack) ? false : new NItem(itemStack).hasKey("itemsadder");
      }
   }

   public static boolean method8748(ItemStack var0) {
      if (var0 == null) {
         return false;
      } else {
         return !NItem.hasNBT(var0) ? false : new NItem(var0).hasKey("itemsadder");
      }
   }

   @Nullable
   public static String method8749(ItemStack var0) {
      if (var0 == null) {
         return null;
      }

      if (!NItem.hasNBT(var0)) {
         return null;
      }

      NCompound var1 = new NItem(var0).getCompound("itemsadder");
      return var1 != null ? var1.getString("namespace") + ":" + var1.getString("id") : null;
   }

   public static String method8750(ItemStack var0) {
      NItem var1 = new NItem(var0);
      return method8770(var1, "namespace") + ":" + method8770(var1, "id");
   }

   public static Integer method8751(ItemStack var0) {
      CustomItemDefinition var1 = ContentManager.method8084().method8149(var0);
      return var1 != null ? var1.aol : null;
   }

   @Deprecated
   public static String method8752(ItemStack var0) {
      NItem var1 = new NItem(var0);
      return method8770(var1, "namespace");
   }

   @Deprecated
   public static String method8753(ItemStack var0) {
      NItem var1 = new NItem(var0);
      return method8770(var1, "id");
   }

   public NItem Dt() {
      return this.anT;
   }

   public void method8754(Set var1) {
      ItemMeta var2 = this.getItemMeta();
      var2.addItemFlags(var1.toArray(new ItemFlag[0]));
      this.setItemMeta(var2);
      this.aoC = var1;
   }

   public void method8755(ItemFlag var1) {
      ItemMeta var2 = this.getItemMeta();
      var2.addItemFlags(new ItemFlag[]{var1});
      this.setItemMeta(var2);
      this.aoC.add(var1);
   }

   public boolean hasItemFlag(@NotNull ItemFlag hideFlag) {
      return this.aoC.contains(hideFlag);
   }

   private void method8756(ItemMeta var1) {
      if (!this.aoC.isEmpty()) {
         var1.addItemFlags(this.aoC.toArray(new ItemFlag[0]));
      }
   }

   @Override
   public boolean ny() {
      if (this.anZ instanceof ItemSpecWithAttributes var1) {
         return true;
      } else {
         return this.anZ instanceof ItemSpecWithTexture ? false : this.As != null && !this.As.isEmpty() && !this.As.equals(" ");
      }
   }

   @Override
   public String nC() {
      return this.method8734(false, false);
   }

   @Override
   public String method8734(boolean var1, boolean var2) {
      if (this.anZ != null) {
         return this.anZ.Ek();
      } else if (this.ny()) {
         return this.As.contains(":") ? this.As : (var2 ? this.getNamespace() + ":" : "") + this.As;
      } else {
         return (var2 ? this.getNamespace() + ":" : "") + "item/ia_auto/" + this.getId() + (var1 ? ".json" : "");
      }
   }

   @Deprecated
   public void cE(String var1) {
      if (var1 == null) {
         this.As = null;
      } else {
         if (var1.endsWith(".json")) {
            var1 = var1.replace(".json", "");
         }

         this.As = var1;
      }
   }

   public List getTextures() {
      if (this.anZ instanceof ItemSpecWithAttributes var3) {
         throw new IllegalStateException("This item has a custom model path, it doesn't use textures. Use getModelPath() instead.");
      } else {
         return this.anZ instanceof ItemSpecWithTexture var1
            ? var1.Fd().values().stream().map(var1x -> (String)(var1x.contains(":") ? var1x : this.aoi + ":" + var1x)).toList()
            : this.anV;
      }
   }

   @Nullable
   public String Du() {
      return this.anZ != null ? this.anZ.Du() : this.anW;
   }

   @Deprecated
   public void cF(String var1) {
      if (var1 == null || var1.isEmpty()) {
         this.anW = null;
      } else if (var1.contains(":")) {
         this.anW = var1;
      } else {
         this.anW = this.aoi + ":" + var1;
      }
   }

   @Nullable
   public String Dv() {
      return this.anX;
   }

   public void method8757(List var1) {
      for (int var2 = 0; var2 < var1.size(); var2++) {
         if (!((String)var1.get(var2)).contains(":")) {
            var1.set(var2, this.aoi + ":" + (String)var1.get(var2));
         }
      }

      this.anV = var1;
   }

   @Override
   public boolean nB() {
      return this.anY;
   }

   @Nullable
   public ArrayList Dw() {
      return this.aoq;
   }

   public static void method8758(CustomItemDefinition var0, ItemStack var1) {
      var1.addUnsafeEnchantments(var0.DU());
   }

   public long Dx() {
      return (long)((float)this.aot / 20.0F * 1000.0F);
   }

   public boolean Dy() {
      return this.aot != -1L;
   }

   public void cG(String var1) {
      this.aou = var1;
   }

   public boolean Dz() {
      return this.aou != null;
   }

   public void method8759(String var1, String var2) {
      if (this.aov == null) {
         this.aov = new HashMap();
      }

      this.aov.put(var1, var2);
   }

   @Nullable
   public String DA() {
      return this.aox;
   }

   public boolean bz(Player var1) {
      if (this.aov == null) {
         return true;
      }

      for (Entry var3 : this.aov.entrySet()) {
         float var4 = ContentManager.method8084().aG.method1429(var1, (String)var3.getKey(), 0.0F);
         char var5 = ((String)var3.getValue()).charAt(0);
         float var6 = Float.parseFloat(((String)var3.getValue()).substring(1));
         if (var5 == '>' ? !(var4 > var6) : (var5 == '<' ? !(var4 < var6) : var5 != '=' || var4 != var6)) {
            var1.sendTitle(
               " ",
               Main.aFi.lj("player-stat-value-insufficient").replace("{stat}", (CharSequence)var3.getKey()).replace("{rule}", (CharSequence)var3.getValue()),
               3,
               40,
               5
            );
            return false;
         }
      }

      return true;
   }

   public List method8760(LivingEntity var1, ItemEventType var2) {
      return this.aog.getEffectsForEntity(var1, var2);
   }

   public void method8761(LivingEntity var1, ItemEventType var2, ResultConsumer var3) {
      SchedulerUtils.runOnMainThread(() -> this.aog.dispatchEvent(var1, var2, var3));
   }

   public void method8762(ItemEventType var1, PlayerInteractEvent var2, ItemStack var3) {
      this.method8761(var2.getPlayer(), var1, var3x -> var3x.applyOnInteract(var2, this, var3));
   }

   public static void method8763(ItemStack var0) {
      NItem var1 = new NItem(var0);
      NCompound var2 = var1.getCompound("itemsadder");
      if (var2 != null && var2.hasKey("max_usages")) {
         var2.setInt("usages", var2.getInt("max_usages"));
         var1.save();
      }
   }

   public static void method8764(ItemStack var0, int var1) {
      NItem var2 = new NItem(var0);
      var2.getOrAddCompound("itemsadder").setInt("usages", var2.getOrAddCompound("itemsadder").getInt("usages") - var1);
      var2.save();
   }

   @Nullable
   public static Integer method8765(ItemStack var0) {
      NItem var1 = new NItem(var0);
      NCompound var2 = var1.getCompound("itemsadder");
      return var2 == null ? null : var2.getInt("usages");
   }

   public static void method8766(ItemStack var0) {
      NItem var1 = new NItem(var0);
      int var2 = method8771(var1, "usages");
      ItemMeta var3 = var0.getItemMeta();
      List var4 = new ArrayList();
      if (var3.hasLore()) {
         var4 = var3.lore();
      }

      Component var5 = AdventureText.fromLegacy(StringUtils.replace(Main.aFi.lj("lore-usages"), "{value}", var2 + ""));
      boolean var6 = false;

      for (int var7 = 0; var7 < var4.size(); var7++) {
         if (ChatColor.stripColor(AdventureText.toPlainText((Component)var4.get(var7)))
            .startsWith(StringUtils.replace(Main.aFi.jY("lore-usages"), "{value}", ""))) {
            var4.set(var7, var5);
            var6 = true;
         }
      }

      if (!var6) {
         var4.add(var5);
      }

      var3.lore(var4);
      var0.setItemMeta(var3);
   }

   public void DB() {
      method8766(this);
      this.DC();
   }

   public static void method8767(ItemStack var0, String var1) {
      NItem var2 = new NItem(var0);
      var2.getOrAddCompound("itemsadder").setString("owner", var1);
      var2.save();
   }

   public static String method8768(ItemStack var0) {
      NCompound var1 = new NItem(var0).getCompound("itemsadder");
      if (var1 == null) {
         return null;
      } else {
         return var1.hasKey("owner") ? var1.getString("owner") : null;
      }
   }

   public void DC() {
      this.anT.refreshCopy();
   }

   public void method8769() {
      this.anT.save();
   }

   @Nullable
   public static String method8770(NItem var0, String var1) {
      NCompound var2 = var0.getCompound("itemsadder");
      return var2 == null ? null : var2.getString(var1);
   }

   public static int method8771(NItem var0, String var1) {
      NCompound var2 = var0.getCompound("itemsadder");
      return var2 == null ? 0 : var2.getInt(var1);
   }

   public int cH(String var1) {
      return this.anT.getCompound("itemsadder").getInt(var1);
   }

   public CustomItemDefinition method8772(String var1, int var2) {
      this.anT.getOrAddCompound("itemsadder").setInt(var1, var2);
      this.anT.save();
      return this;
   }

   public CustomItemDefinition cI(String var1) {
      return this.method8773(AdventureText.fromLegacy(var1));
   }

   public CustomItemDefinition method8773(Component var1) {
      this.anT.setItemName(AdventureText.serializePlain(var1));
      return this;
   }

   public CustomItemDefinition method8774(Component var1) {
      this.anT.setCustomName(AdventureText.serializePlain(var1));
      return this;
   }

   @Deprecated
   public String DE() {
      return this.getItemMeta().getDisplayName();
   }

   public Component itemName() {
      if (ItemStackUtils.am(this.getType())) {
         return EnvironmentDetector.paperAdventure ? this.getItemMeta().displayName() : AdventureText.fromNative(this.anT.getCustomName());
      }

      if (EnvironmentDetector.paperAdventure) {
         return this.getItemMeta().itemName();
      }

      Object var1 = this.anT.getItemName();
      return var1 == null ? null : AdventureText.fromNative(var1);
   }

   public void bv(int var1) {
      ItemMeta var2 = this.getItemMeta();
      var2.setCustomModelData(var1);
      this.setItemMeta(var2);
      this.anT.refreshCopy();
   }

   public static void method8775(ItemStack var0, int var1) {
      ItemMeta var2 = var0.getItemMeta();
      var2.setCustomModelData(var1);
      var0.setItemMeta(var2);
   }

   @Override
   public int nz() {
      ItemMeta var1 = this.getItemMeta();
      return var1.hasCustomModelData() ? var1.getCustomModelData() : 0;
   }

   public boolean DF() {
      ItemMeta var1 = this.getItemMeta();
      return var1.hasCustomModelData();
   }

   public static int method8776(ItemStack var0) {
      ItemMeta var1 = var0.getItemMeta();
      return var1.hasCustomModelData() ? var1.getCustomModelData() : 0;
   }

   @Override
   public String getId() {
      return this.aoj;
   }

   @Override
   public String getNamespace() {
      return this.aoi;
   }

   @Override
   public String getNamespacedID() {
      return this.aok;
   }

   public CustomItemDefinition bw(int var1) {
      this.method8772("max_usages", var1);
      return this;
   }

   public int DG() {
      return this.cH("max_usages");
   }

   public CustomItemDefinition bx(int var1) {
      this.method8772("usages", var1);
      return this;
   }

   public CustomItemDefinition method8777(int var1, int var2) {
      NCompound var3 = this.anT.getOrAddCompound("itemsadder");
      var3.setInt("usages", var1);
      var3.setInt("max_usages", var2);
      this.anT.save();
      return this;
   }

   public int getUsages() {
      return this.cH("usages");
   }

   public String DH() {
      return "items." + this.getId();
   }

   public void cJ(String var1) {
      if (var1 == null || !var1.isEmpty() && !var1.equals(" ")) {
         this.YK = var1;
      } else {
         this.YK = null;
      }
   }

   @Nullable
   public String getPermissionSuffix() {
      return this.YK;
   }

   public boolean hasPermission() {
      return this.YK != null;
   }

   public boolean method8778(CustomItemDefinition var1) {
      return var1 == null ? false : this.aol == var1.aol;
   }

   public boolean method8779(ItemStack var1) {
      if (!isCustomItem(var1)) {
         return false;
      }

      CustomItemDefinition var2 = ContentManager.method8084().method8149(var1);
      return var2 == null ? false : this.aol == var2.aol;
   }

   @Deprecated
   public static boolean method8780(ItemStack var0, ItemStack var1) {
      if (!isCustomItem(var0) || !isCustomItem(var1)) {
         return false;
      } else {
         return var0 == var1 ? true : method8750(var0).equals(method8750(var1));
      }
   }

   @Deprecated
   public static boolean method8781(CustomItemDefinition var0, ItemStack var1) {
      if (var0 == var1) {
         return true;
      }

      Integer var2 = method8751(var1);
      return var2 == null ? false : var0.aol == var2;
   }

   public static boolean method8782(@Nullable CustomItemDefinition var0, @Nullable CustomItemDefinition var1) {
      if (var0 == var1) {
         return true;
      } else {
         return var0 != null && var1 != null ? var0.aol == var1.aol : false;
      }
   }

   @Deprecated
   public static boolean method8783(ItemStack var0, String var1) {
      return !isCustomItem(var0) ? false : method8750(var0).equals(var1);
   }

   public static boolean method8784(Integer var0, ItemStack var1) {
      CustomItemDefinition var2 = ContentManager.method8084().method8149(var1);
      return var2 == null ? false : var0 == var2.aol;
   }

   public static boolean method8785(ItemStack var0, ItemStack var1) {
      if (isCustomItem(var0) && isCustomItem(var1)) {
         if (var0 == var1) {
            return true;
         }

         boolean var2 = method8750(var0).equals(method8750(var1));
         if (!var2) {
            return false;
         }

         List var3 = var0.getItemMeta().getLore();
         List var4 = var1.getItemMeta().getLore();
         if (var3.size() != var4.size()) {
            return false;
         }

         for (int var5 = 0; var5 < var3.size(); var5++) {
            if (!((String)var3.get(var5)).equals(var4.get(var5))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void method8786(String var1, String var2, double var3, String var5) {
      method8787(this, this.getNamespacedID(), var1, var2, var3, var5);
      this.DC();
   }

   public static void method8787(CustomItemDefinition var0, String var1, String var2, String var3, double var4, String var6) {
      var3 = var3.toLowerCase();
      if (EquipmentSlotType.cP(var3) == null) {
         Log.error("Unknown slot: '" + var3 + "' for item " + var1);
      } else {
         var2 = var2.replace("minecraft:", "");
         var2 = var2.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
         if (Version.isAtLeast(Version.v1_21_3)) {
            if (var2.startsWith("generic")) {
               var2 = var2.replace("generic.", "");
            }
         } else if (!var2.contains(".") && !var2.startsWith("generic")) {
            var2 = "generic." + var2;
         }

         if (var6.equals("add") && ItemStackUtils.ab(var0.getType())) {
            if (var2.equals("generic.attackDamage") || var2.equals("generic.attack_damage") || var2.equals("attack_damage")) {
               var4 += -1.0;
            } else if (var2.equals("generic.attackSpeed") || var2.equals("generic.attack_speed") || var2.equals("attack_speed")) {
               var4 += -4.0;
            }
         }

         Operation var7 = Operation.ADD_NUMBER;
         if (var6.equals("multiply_base")) {
            var7 = Operation.ADD_SCALAR;
         } else if (var6.equals("multiply")) {
            var7 = Operation.MULTIPLY_SCALAR_1;
         }

         Attribute var8 = cK(var2);
         if (var8 == null) {
            Log.error("&cUnknown attribute modifier '&e" + var2 + "&c' for item &e'" + var1 + "'&c. File " + var0.DJ());
         } else {
            UUID var9 = UUID.nameUUIDFromBytes((var8 + var7.name() + var4 + var3).getBytes());
            ItemMeta var10 = var0.getItemMeta();
            var10.addAttributeModifier(var8, new AttributeModifier(var9, "itemsadder", var4, var7, EquipmentSlotType.cQ(var3)));
            var0.method8756(var10);
            var0.setItemMeta(var10);
         }
      }
   }

   public static void method8788(CustomItemDefinition var0, ItemStack var1, NItem var2, String var3, String var4, String var5, double var6, String var8) {
      var5 = var5.toLowerCase();
      if (EquipmentSlotType.cP(var5) == null) {
         Log.error("Unknown slot: '" + var5 + "' for item " + var3);
      } else {
         var4 = var4.replace("minecraft:", "");
         var4 = var4.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
         if (Version.isAtLeast(Version.v1_21_3)) {
            if (var4.startsWith("generic")) {
               var4 = var4.replace("generic.", "");
            }
         } else if (!var4.contains(".") && !var4.startsWith("generic")) {
            var4 = "generic." + var4;
         }

         if (var8.equals("add") && ItemStackUtils.ab(var1.getType())) {
            if (var4.equals("generic.attackDamage") || var4.equals("generic.attack_damage") || var4.equals("attack_damage")) {
               var6 += -1.0;
            } else if (var4.equals("generic.attackSpeed") || var4.equals("generic.attack_speed") || var4.equals("attack_speed")) {
               var6 += -4.0;
            }
         }

         Operation var9 = Operation.ADD_NUMBER;
         if (var8.equals("multiply_base")) {
            var9 = Operation.ADD_SCALAR;
         } else if (var8.equals("multiply")) {
            var9 = Operation.MULTIPLY_SCALAR_1;
         }

         Attribute var10 = cK(var4);
         if (var10 == null) {
            Log.error("&cUnknown attribute modifier '&e" + var4 + "&c' for item &e'" + var3 + "'&c. File " + var0.DJ());
         } else {
            UUID var11 = UUID.nameUUIDFromBytes((var10 + var9.name() + var6 + var5).getBytes());
            ItemMeta var12 = var1.getItemMeta();
            var12.addAttributeModifier(var10, new AttributeModifier(var11, "itemsadder", var6, var9, EquipmentSlotType.cQ(var5)));
            var1.setItemMeta(var12);
            var2.refreshCopy();
         }
      }
   }

   @Nullable
   private static Attribute cK(String var0) {
      var0 = var0.replace("minecraft:", "");
      if (var0.equals("attack_reach") || var0.equals("generic.attack_reach")) {
         var0 = "entity_interaction_range";
      }

      for (Attribute var4 : Attribute.values()) {
         if (var4.getKey().getKey().equalsIgnoreCase(var0)) {
            return var4;
         }
      }

      String[] var9 = var0.split("\\.");
      if (var9.length != 2) {
         return null;
      }

      String var10 = var9[1];

      for (Attribute var6 : Attribute.values()) {
         String var7 = var6.getKey().getKey();
         if (var7.equalsIgnoreCase(var10) || var7.endsWith("." + var10)) {
            return var6;
         }
      }

      return null;
   }

   @Nullable
   public static Multimap method8789(NItem var0, EquipmentSlotType var1) {
      ItemMeta var2 = var0.getItem().getItemMeta();
      if (var2 == null) {
         return null;
      }

      Multimap var3 = var2.getAttributeModifiers();
      if (var3 == null) {
         return null;
      }

      for (Entry var5 : var3.entries()) {
         if (((AttributeModifier)var5.getValue()).getSlot() == var1.bukkitSlot
            && (var5.getKey() == Attribute.GENERIC_ATTACK_DAMAGE || var5.getKey() == Attribute.GENERIC_ATTACK_SPEED)) {
            return var3;
         }
      }

      return null;
   }

   public static double method8790(ItemStack var0, double var1, EquipmentSlotType var3) {
      NItem var4 = new NItem(var0);
      NList var5 = var4.getList("AttributeModifiers", NBTType.Compound);
      if (var5 == null) {
         return 1.0;
      }

      double var6 = ItemStackUtils.ah(var0.getType());

      for (int var8 = 0; var8 < var5.size(); var8++) {
         NCompound var9 = var5.getOrAddCompound(var8);
         String var10 = var9.getString("Slot");
         if (var10 != null && var10.equals(var3.name)) {
            String var11 = var9.getString("AttributeName");
            if (var11.equals("generic.attackDamage") || var11.equals("minecraft:generic.attack_damage") || var11.equals("attack_damage")) {
               var6 = var9.getDouble("Amount") - var1;
               break;
            }
         }
      }

      if (var0.containsEnchantment(Enchantment.SHARPNESS)) {
         float var12 = 1.0F;
         if (var0.getEnchantmentLevel(Enchantment.SHARPNESS) > 1) {
            var12 += (var0.getEnchantmentLevel(Enchantment.SHARPNESS) - 1) * 0.5F;
         }

         var6 += var12;
      }

      return var6;
   }

   public static double method8791(ItemStack var0, Multimap var1, double var2, EquipmentSlotType var4) {
      double var5 = ItemStackUtils.ah(var0.getType());

      for (Entry var8 : var1.entries()) {
         if (((AttributeModifier)var8.getValue()).getSlot() == var4.bukkitSlot && var8.getKey() == Attribute.GENERIC_ATTACK_DAMAGE) {
            var5 = ((AttributeModifier)var8.getValue()).getAmount() - var2;
         }
      }

      Map var11 = var0.getItemMeta().getEnchants();

      for (Entry var9 : var11.entrySet()) {
         if (var9.getKey() == Enchantment.SHARPNESS) {
            float var10 = 1.0F;
            if (var0.getEnchantmentLevel(Enchantment.SHARPNESS) > 1) {
               var10 += (var0.getEnchantmentLevel(Enchantment.SHARPNESS) - 1) * 0.5F;
            }

            var5 += var10;
            break;
         }
      }

      return var5;
   }

   public static double method8792(ItemStack var0, EquipmentSlotType var1) {
      return method8790(var0, -1.0, var1);
   }

   public void method8793(ConfigSectionWrapper var1) {
      this.aof = var1;
      if (var1 != null) {
         YamlConfiguration var2 = new YamlConfiguration();
         var2.createSection(this.DH(), var1.bLU.getValues(true));
         String var3 = var2.saveToString();
         this.aod = ItemMetaTracker.xxHashString(var3);
      }
   }

   public void method8794(@Nullable Integer var1) {
      if (var1 != null) {
         if (var1 < 1 || var1 > 99) {
            Log.error("[Items] '" + this.getNamespacedID() + "'. Invalid max_stack_size '" + var1 + "'. It must be between 1 and 99. File: " + this.DJ());
         } else if (var1 > 1 && ItemStackUtils.method7233(this) > 0) {
            Log.error("[Items] '" + this.getNamespacedID() + "'. Damageable items require max_stack_size 1. File: " + this.DJ());
         } else if (this.getMaxStackSize() != var1) {
            ItemMeta var2 = this.getItemMeta();
            var2.setMaxStackSize(var1);
            this.setItemMeta(var2);
         }
      }
   }

   public void aR(boolean var1) {
      ItemMeta var2 = this.getItemMeta();
      if (var2.isFireResistant() != var1) {
         var2.setFireResistant(var1);
         this.setItemMeta(var2);
      }
   }

   public void aS(boolean var1) {
      ItemMeta var2 = this.getItemMeta();
      if (var2.isHideTooltip() != var1) {
         var2.setHideTooltip(var1);
         this.setItemMeta(var2);
      }
   }

   public void by(int var1) {
      ItemMeta var2 = this.getItemMeta();
      if (var2 instanceof Repairable var3) {
         if (!var3.hasRepairCost() && var1 == 0) {
            return;
         }

         if (var3.hasRepairCost() && var3.getRepairCost() == var1) {
            return;
         }

         var3.setRepairCost(var1);
         this.setItemMeta(var2);
      }
   }

   public void aT(boolean var1) {
      ItemMeta var2 = this.getItemMeta();
      if (!var2.hasEnchantmentGlintOverride() || var2.getEnchantmentGlintOverride() != var1) {
         var2.setEnchantmentGlintOverride(var1);
         this.setItemMeta(var2);
      }
   }

   public void method8795(@Nullable Integer var1, @Nullable Integer var2, @Nullable Boolean var3, @Nullable Boolean var4) {
      this.aoo = var3 != null && var3;
      this.aom = var2;
      this.aon = var1;
      if (var2 != null) {
         ItemStackUtils.method7240(this, var2);
      }

      if (var1 != null) {
         ItemStackUtils.method7235(this, var1);
      }

      if (var4 != null) {
         ItemStackUtils.method7241(this, var4);
      }

      this.DC();
   }

   public void method8796(
      NamespacedKey var1,
      EquipmentSlot var2,
      @Nullable String var3,
      @Nullable NamespacedKey var4,
      @Nullable List var5,
      @Nullable Boolean var6,
      @Nullable Boolean var7,
      @Nullable Boolean var8,
      @Nullable Boolean var9
   ) {
      ItemOperationsService.getRegistryBridge().method3433(this, var2, var1, var3, var4, var5, var6, var7, var8, var9);
      this.DC();
   }

   public void method8797(@Nullable NamespacedKey var1) {
      ItemOperationsService.getRegistryBridge().method3434(this, var1);
   }

   public void method8798(@Nullable NamespacedKey var1) {
      this.aoe = var1;
      ItemOperationsService.getRegistryBridge().method3435(this, FontImageLoader.method1930(var1));
      this.DC();
   }

   public boolean method8799(ItemStack var1) {
      if (this.aoe == null) {
         return false;
      }

      ItemOperationsService.getRegistryBridge().method3435(var1, FontImageLoader.method1930(this.aoe));
      return true;
   }

   public void method8800(
      CustomItemDefinition var1,
      int var2,
      float var3,
      boolean var4,
      int var5,
      @Nullable String var6,
      @Nullable String var7,
      @Nullable Boolean var8,
      @Nullable List var9
   ) {
      ItemOperationsService.getRegistryBridge().method3432(var1, var2, var3, var4, var5, var6, var7, var8, var9);
      this.aoB = ItemFeatureSpec.Ev().bz(var2).method10004(var3).aZ(var4).bA(var5).cY(var6).cZ(var7).method10005(var8 != null && var8).method10006(var9).EB();
   }

   public boolean DI() {
      return this.anZ != null || this.aoa != null;
   }

   public String DJ() {
      return this.lH().DJ();
   }

   public boolean DK() {
      return this.aov != null && !this.aov.isEmpty();
   }

   public ConfigSectionWrapper aU(boolean var1) {
      return !var1
         ? this.lH().kc(this.DH())
         : ItemDefinitionLoader.method10116(this.lH(), this.aU(false), ContentManager.method8084().method8209(), this.aoi, this.aoj);
   }

   static ItemAttributeValues method8801(ItemStack var0, @Nullable Multimap var1, EquipmentSlotType var2) {
      ItemAttributeValues var3 = new ItemAttributeValues();

      for (Entry var5 : var1.entries()) {
         if (((AttributeModifier)var5.getValue()).getSlot() == var2.bukkitSlot) {
            if (var3.aoX == null && var5.getKey() == Attribute.GENERIC_ATTACK_DAMAGE) {
               var3.aoX = ((AttributeModifier)var5.getValue()).getAmount() - -1.0;
            }

            if (var3.aoY == null && var5.getKey() == Attribute.GENERIC_ATTACK_SPEED) {
               var3.aoY = ((AttributeModifier)var5.getValue()).getAmount() - -4.0;
            }

            if (var3.aoZ == null && var5.getKey() == Attribute.GENERIC_ATTACK_KNOCKBACK) {
               var3.aoZ = ((AttributeModifier)var5.getValue()).getAmount();
            }

            String var6 = ((Attribute)var5.getKey()).getKey().getKey();
            if (var3.apa == null && var6.endsWith("block_interaction_range")) {
               var3.apa = ((AttributeModifier)var5.getValue()).getAmount();
            }

            if (var3.apb == null && var6.endsWith("entity_interaction_range")) {
               var3.apb = ((AttributeModifier)var5.getValue()).getAmount();
            }
         }
      }

      if (var3.aoY == null) {
         var3.aoY = ItemOperationsService.getRegistryBridge().ah(var0);
      }

      return var3;
   }

   public static void method8802(ItemStack var0, EquipmentSlotType var1) {
      if (var0 != null && var0.hasItemMeta()) {
         NItem var2 = new NItem(var0);
         Multimap var3 = method8789(var2, var1);
         if (var3 != null) {
            double var4 = method8791(var0, var3, -1.0, var1);
            List var6 = Objects.requireNonNullElseGet(var2.getLoreCopy(), ArrayList::new);
            if (Version.isAtLeast(Version.v1_21_3)) {
               method8804(var2, var6, var1, var4, aoM);
            } else {
               method8804(var2, var6, var1, var4, aoL);
            }
         }
      }
   }

   public static void method8803(CustomItemDefinition var0, EquipmentSlotType var1) {
      NItem var2 = var0.anT;
      Multimap var3 = method8789(var2, var1);
      if (var3 != null) {
         ItemStack var4 = var0.Ds();
         List var5 = Objects.requireNonNullElseGet(var2.getLoreCopy(), ArrayList::new);
         double var6 = method8791(var4, var3, -1.0, var1);
         if (Version.isAtLeast(Version.v1_21_3)) {
            method8804(var2, var5, var1, var6, aoM);
         } else {
            method8804(var2, var5, var1, var6, aoL);
         }

         ItemAttributeValues var8 = method8801(var4, var3, var1);
         if (Version.isAtLeast(Version.v1_21_3)) {
            method8804(var2, var5, var1, var8.aoY, aoO);
            method8804(var2, var5, var1, var8.aoZ, aoQ);
            method8804(var2, var5, var1, var8.apa, aoS);
            method8804(var2, var5, var1, var8.apb, aoU);
         } else {
            method8804(var2, var5, var1, var8.aoY, aoN);
            method8804(var2, var5, var1, var8.aoZ, aoP);
            method8804(var2, var5, var1, var8.apa, aoR);
            method8804(var2, var5, var1, var8.apb, aoT);
         }
      }
   }

   public static void method8804(NItem var0, List var1, EquipmentSlotType var2, @Nullable Double var3, ComponentLabel var4) {
      if (var3 != null) {
         ComponentLabel var5 = switch (var2) {
            case HEAD -> aoF;
            case CHEST -> aoG;
            case LEGS -> aoH;
            case FEET -> aoI;
            case MAINHAND -> aoJ;
            case OFFHAND -> aoK;
         };
         boolean var6 = false;
         int var7 = 0;

         for (int var8 = 0; var8 < var1.size(); var8++) {
            Component var9 = AdventureText.fromNative(var1.get(var8));
            if (AdventureText.serializePlain(var9).contains(var5.key)) {
               var6 = true;
               var7 = var8;
               break;
            }
         }

         Object var13 = AdventureText.toNative(
            ComponentLabel.withLabelStyle(Component.text(" " + Utils.formatNumber(var3, false) + " ")).append(var4.component)
         );
         if (!var6) {
            var1.add(NmsChatService.aPl);
            var1.add(var5.nativeComponent);
            var1.add(var13);
         } else {
            boolean var14 = false;

            for (int var10 = var7; var10 < var1.size(); var10++) {
               Component var11 = AdventureText.fromNative(var1.get(var10));
               if (AdventureText.serializePlain(var11).contains(var4.key)) {
                  var1.set(var10, var13);
                  var14 = true;
                  break;
               }
            }

            if (!var14) {
               for (int var15 = var7 + 1; var15 < var1.size(); var15++) {
                  Component var16 = AdventureText.fromNative(var1.get(var15));
                  String var12 = AdventureText.serializePlain(var16);
                  if (!var12.contains("translate.") && !var12.contains("attribute.name.")) {
                     var1.add(var7, var13);
                     var14 = true;
                     break;
                  }
               }

               if (!var14) {
                  var1.add(var13);
               }
            }
         }

         NBT.dataComponents.setLore(var0.getItem(), var1);
         var0.save();
      }
   }

   public static void method8805(ItemStack var0, EquipmentSlotType var1) {
      NItem var2 = new NItem(var0);
      Multimap var3 = method8789(var2, var1);
      if (var3 != null) {
         List var4 = Objects.requireNonNullElseGet(var2.getLoreCopy(), ArrayList::new);
         ItemAttributeValues var5 = method8801(var0, var3, var1);
         method8804(var2, var4, var1, var5.aoY, aoO);
         method8804(var2, var4, var1, var5.aoZ, aoQ);
      }
   }

   public void method8806(BossBarSpec var1) {
      this.aoA = var1;
      if (var1 != null && var1.displayMode == HudDisplayMode.VANILLA) {
         ItemOperationsService.getRegistryBridge().method3431(this, (float)this.aot / 20.0F, this.aok);
         this.DC();
      }
   }

   @Nullable
   public BossBarSpec DL() {
      return this.aoA;
   }

   @Nullable
   public HudDisplayMode DM() {
      return this.aoA == null ? null : this.aoA.displayMode;
   }

   public static void method8807(CustomItemDefinition var0, ComponentMapper var1) {
      method8808(var0, var1, var1);
   }

   public static void method8808(CustomItemDefinition var0, ComponentMapper var1, ComponentMapper var2) {
      var0.DC();
      if (method8810(var1, var2, var0.anT)) {
         var0.Dt().save();
      }
   }

   public static void method8809(ItemStack var0, ComponentMapper var1, ComponentMapper var2) {
      NItem var3 = new NItem(var0);
      if (method8810(var1, var2, var3)) {
         var3.save();
      }
   }

   private static boolean method8810(ComponentMapper var0, ComponentMapper var1, NItem var2) {
      boolean var3 = false;
      Object var4 = var2.getCustomName();
      if (var4 != null) {
         Component var5 = AdventureText.fromNative(var4);
         ComponentResult var6 = var0.map(var5);
         if (var6.changed) {
            var5 = var6.component;
            var2.setCustomName(AdventureText.serializePlain(var5));
            var3 = true;
         }
      }

      Object var12 = var2.getItemName();
      if (var12 != null) {
         Component var13 = AdventureText.fromNative(var12);
         ComponentResult var7 = var0.map(var13);
         if (var7.changed) {
            var13 = var7.component;
            var2.setItemName(AdventureText.serializePlain(var13));
            var3 = true;
         }
      }

      List var15 = var2.getLoreCopy();
      if (var15 != null) {
         boolean var16 = false;

         for (int var8 = 0; var8 < var15.size(); var8++) {
            Component var9 = AdventureText.fromNative(var15.get(var8));
            ComponentResult var10 = var1.map(var9);
            if (var10.changed) {
               var9 = var10.component;
               var15.set(var8, AdventureText.toNative(var9));
               var3 = true;
               var16 = true;
            }
         }

         if (var16) {
            var2.setLore(var15);
         }
      }

      return var3;
   }

   public static void method8811(CustomItemDefinition var0) {
      method8807(var0, var0x -> new ComponentResult(AdventureText.disableCommonDecorations(var0x)));
   }

   public static void method8812(ItemStack var0) {
      method8809(var0, CustomItemDefinition::method8813, CustomItemDefinition::method8813);
   }

   private static ComponentResult method8813(Component var0) {
      return new ComponentResult(AdventureText.disableCommonDecorations(var0));
   }

   public boolean hasCustomDurability() {
      return ItemStackUtils.aA(this);
   }

   public boolean DN() {
      return this.aoo;
   }

   public static int method8814(ItemStack var0) {
      return ItemStackUtils.method7233(var0);
   }

   public static void method8815(ItemStack var0, int var1) {
      ItemStackUtils.method7240(var0, var1);
   }

   public static void method8816(ItemStack var0, int var1) {
      ItemStackUtils.method7235(var0, var1);
   }

   public static void method8817(ItemStack var0, int var1) {
      ItemStackUtils.method7236(var0, var1);
   }

   public static void method8818(ItemStack var0, int var1) {
      ItemStackUtils.method7237(var0, var1);
   }

   @Deprecated
   public static boolean method8819(ItemStack var0) {
      return ItemStackUtils.aA(var0);
   }

   public static boolean method8820(ItemStack var0) {
      return !method8819(var0) ? false : ItemStackUtils.aw(var0) <= 0;
   }

   public static boolean method8821(ItemStack var0) {
      return !method8819(var0) ? ItemStackUtils.method7242(var0) : ItemStackUtils.aw(var0) == 1;
   }

   public static void method8822(ItemStack var0) {
      ItemStackUtils.ay(var0);
   }

   @Deprecated
   public static void method8823(ItemStack var0, int var1) {
      ItemStackUtils.method7236(var0, var1);
   }

   public static ItemStack method8824(ItemStack var0, int var1, boolean var2) {
      return method8827(var0, var1, null, var2, true);
   }

   public static void method8825(int var0, Player var1) {
      var1.getInventory().setItemInMainHand(method8826(var1.getInventory().getItemInMainHand(), var0, var1));
   }

   public static ItemStack method8826(ItemStack var0, int var1, LivingEntity var2) {
      return method8827(var0, var1, var2, false, true);
   }

   public static ItemStack method8827(ItemStack var0, int var1, LivingEntity var2, boolean var3, boolean var4) {
      if (var1 == 0) {
         return var0;
      }

      if (var0.getType() == Material.AIR) {
         return var0;
      }

      if (!ItemStackUtils.aA(var0)) {
         return var0;
      }

      ItemMeta var5 = var0.getItemMeta();
      if (var5.isUnbreakable()) {
         return var0;
      }

      if (var5.hasEnchant(Enchantment.UNBREAKING)) {
         for (int var6 = var1; var6 > 0; var6--) {
            if (ItemStackUtils.dM(var5.getEnchantLevel(Enchantment.UNBREAKING))) {
               var1--;
            }
         }
      }

      if (var1 == 0) {
         return var0;
      }

      CustomItemDefinition var7 = ContentManager.method8084().method8149(var0);
      boolean var10;
      if (var7 == null) {
         var10 = true;
      } else {
         var10 = var7.DN();
      }

      int var8 = ItemStackUtils.aw(var0);
      if (var8 == 0 && var3) {
         return var0;
      }

      if (!var10 && var8 - 1 <= 0) {
         ItemStackUtils.method7235(var0, 0);
         return var0;
      }

      if (var2 instanceof Player var9) {
         if (var4 && method8828(var9, var0, var1)) {
            return var0;
         }

         method8818(var0, var1);
         if (var8 - var1 <= 0 && !var3 && var10) {
            EventRegistrar.callEvent(new PlayerItemBreakEvent(var9, var0));
            ItemStackUtils.method7254(var9, var0);
            ItemStackUtils.au(var0);
            if (var0.getType() != Material.AIR && var0.getAmount() > 0) {
               method8822(var0);
            }

            return var0;
         }
      } else {
         method8818(var0, var1);
         if (var8 - var1 <= 0 && !var3 && var10) {
            ItemStackUtils.method7255(var2.getEyeLocation(), var0);
            ItemStackUtils.au(var0);
            if (var0.getType() != Material.AIR && var0.getAmount() > 0) {
               method8822(var0);
            }
         }
      }

      return var0;
   }

   public static boolean method8828(@Nullable Player var0, ItemStack var1, int var2) {
      return var0 == null ? false : !EventRegistrar.callEvent(new PlayerItemDamageEvent(var0, var1, var2));
   }

   @Override
   public int hashCode() {
      return this.aol;
   }

   @Deprecated
   @Generated
   public String DO() {
      return this.anU;
   }

   @Deprecated
   @Generated
   public void cL(String var1) {
      this.anU = var1;
   }

   @Deprecated
   @Generated
   public void cM(@Nullable String var1) {
      this.anX = var1;
   }

   @Deprecated
   @Generated
   public void aV(boolean var1) {
      this.anY = var1;
   }

   @Nullable
   @Generated
   public ItemRuleSpec DP() {
      return this.aob;
   }

   @Generated
   public void method8829(@Nullable ItemRuleSpec var1) {
      this.aob = var1;
   }

   @Generated
   public YamlConfig lH() {
      return this.aoc;
   }

   @Nullable
   @Generated
   public Long DQ() {
      return this.aod;
   }

   @Nullable
   @Generated
   public NamespacedKey DR() {
      return this.aoe;
   }

   @Generated
   public ConfigSectionWrapper DS() {
      return this.aof;
   }

   @Nullable
   @Generated
   public ItemUpdatePolicy DT() {
      return this.aop;
   }

   @Generated
   public void method8830(@Nullable ItemUpdatePolicy var1) {
      this.aop = var1;
   }

   @Generated
   public void method8831(@Nullable ArrayList var1) {
      this.aoq = var1;
   }

   @Generated
   public void aW(boolean var1) {
      this.aor = var1;
   }

   @Generated
   public boolean isBlockAllEnchants() {
      return this.aor;
   }

   @Generated
   public ImmutableMap DU() {
      return this.aos;
   }

   @Generated
   public void method8832(ImmutableMap var1) {
      this.aos = var1;
   }

   @Generated
   public void method8833(long var1) {
      this.aot = var1;
   }

   @Generated
   public HashMap DV() {
      return this.aov;
   }

   @Generated
   public void aX(boolean var1) {
      this.aow = var1;
   }

   @Generated
   public boolean DW() {
      return this.aow;
   }

   @Generated
   public void cN(String var1) {
      this.aox = var1;
   }

   @Generated
   public boolean DX() {
      return this.aoz;
   }

   @Nullable
   @Generated
   public ItemFeatureSpec DY() {
      return this.aoB;
   }

   @Generated
   public boolean DZ() {
      return this.aoD;
   }

   @Generated
   public void aY(boolean var1) {
      this.aoD = var1;
   }
}
