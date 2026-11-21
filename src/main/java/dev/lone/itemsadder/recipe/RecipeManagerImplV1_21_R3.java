package dev.lone.itemsadder.recipe;

import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.NativeValue;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketListenerHandle;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.PlaceRecipePacketView;
import beer.devs.nativepackets.api.RecipeBookAction;
import beer.devs.nativepackets.api.RecipeBookPacketView;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.internal.SwitchMap62;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemRegistryBridgeV1_21_R3;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.EnvironmentDetector;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.ItemSlotHandle;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.PlayerInventoryProxy;
import dev.lone.itemsadder.util.PlayerTimingSession;
import dev.lone.itemsadder.util.ServerCompat;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.PacketPlayOutAutoRecipe;
import net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket.a;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.inventory.InventoryCrafting;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.IRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeItemStack;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.Recipes;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.CraftingManager.d;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.entity.TileEntityFurnace;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_21_R3.block.CraftFurnace;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R3.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.v1_21_R3.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_21_R3.inventory.CraftShapedRecipe;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecipeManagerImplV1_21_R3 extends AbstractRecipeManager implements IaListener {
   protected PacketListenerHandle boz;
   private volatile boolean boA;
   private volatile long boB;
   private final Map boQ = new ConcurrentHashMap();
   Map boD = new ConcurrentHashMap();
   Map boE = new ConcurrentHashMap();
   Map boF = new ConcurrentHashMap();
   static Field boq = ServerCompat.findFieldByTypeIndex(RecipeMap.class, Multimap.class, 0);
   static Field bor;
   static Field bos;
   Map bot = new Object2ObjectOpenHashMap();
   static Field boG;

   private static boolean method834(RecipeHolder var0) {
      return var0.a().a().b().startsWith(AbstractRecipeManager.boe);
   }

   @Override
   public void register() {
      this.boi = NativePacketsHolder.PE()
         .method1888(this, PacketSelector.exact(PacketTypes.RECIPE_BOOK), ListenerPriority.LOWEST, false, ListenerFailurePolicy.PASS_ORIGINAL, var0 -> {
            RecipeBookPacketView var1x = (RecipeBookPacketView)var0.view(RecipeBookPacketView.class);
            if (var1x.action() == RecipeBookAction.ADD) {
               for (int var2 = var1x.recipeCount() - 1; var2 >= 0; var2--) {
                  NativeValue var3 = var1x.recipe(var2);
                  a var4 = (a)var3.nativeHandle();
                  d var5 = MinecraftServer.getServer().aI().a(var4.c().a());
                  if (method834(var5.b())) {
                     var1x.removeRecipe(var2);
                  }
               }
            }
         });
      if (!EnvironmentDetector.paperAdventure) {
         this.boA = true;
         long var1 = ++this.boB;
         this.boz = NativePacketsHolder.PE()
            .method1888(
               this.bof,
               PacketSelector.exact(PacketTypes.PLACE_RECIPE),
               ListenerPriority.NORMAL,
               true,
               ListenerFailurePolicy.CANCEL_PACKET,
               var3 -> this.method835(var3, (PlaceRecipePacketView)var3.view(PlaceRecipePacketView.class), var1)
            );
      }

      this.PU();
      EventRegistrar.registerListener(this, this.bof);
   }

   private void method835(PacketEvent var1, PlaceRecipePacketView var2, long var3) {
      if (this.boA && this.boB == var3) {
         long var5 = var1.connection().id();
         int var7 = var2.containerId();
         int var8 = ((RecipeDisplayId)var2.recipe().nativeHandle()).a();
         boolean var9 = var2.useMaxItems();
         var1.sync(
            (var8x, var9x) -> {
               if (this.boA
                  && this.boB == var3
                  && var9x.player() instanceof Player var10
                  && var10.isOnline()
                  && NativePacketsHolder.PE().method1885(var10.getUniqueId(), var5) == var10) {
                  this.method836(var8x, var10, var7, var8, var9);
               }
            }
         );
      }
   }

   private void method836(PacketEvent var1, Player var2, int var3, int var4, boolean var5) {
      d var6 = MinecraftServer.getServer().aI().a(new RecipeDisplayId(var4));
      if (var6 == null) {
         var1.cancel();
         Log.error("'" + var2 + "' clicked on a recipe that doesn't exist '" + var4 + "'. Hacking?");
      } else {
         UUID var7 = var2.getUniqueId();
         Long var8 = (Long)this.boF.get(var7);
         if (var8 != null) {
            if (System.currentTimeMillis() - var8 < 5000L) {
               var1.cancel();
               var2.closeInventory();
               var2.sendMessage(ChatColor.RED + "You abused the recipebook. Wait before using the recipe book again.");
               return;
            }

            this.boF.remove(var7);
         }

         RecipeHolder var9 = var6.b();
         AbstractCustomRecipe var10 = ContentManager.method8084().bp.dV(var9.a().a().toString());
         if (var10 != null) {
            var1.cancel();
            if (this.boQ.putIfAbsent(var7, true) == null) {
               try {
                  this.method837(var3, var5, var2, var6.a(), var9, var10);
               } finally {
                  this.boQ.remove(var7);
               }
            }
         }
      }
   }

   void method644(Player var1, CraftingInventory var2) {
      ItemStack[] var3 = (ItemStack[])var2.getMatrix().clone();

      for (int var4 = 0; var4 <= 9; var4++) {
         ItemStack var5 = var2.getItem(var4);
         if (var5 != null && !var5.getType().isAir()) {
            var2.setItem(var4, null);
         }
      }

      ItemStackUtils.method7244(var1, var3);
   }

   private void method837(int var1, boolean var2, Player var3, RecipeDisplayEntry var4, RecipeHolder var5, AbstractCustomRecipe var6) {
      if (var3.getOpenInventory().getTopInventory() instanceof CraftingInventory var7) {
         if (!this.boD.containsKey(var3.getUniqueId())) {
            this.method644(var3, var7);
         }

         if (this.boD.getOrDefault(var3.getUniqueId(), var5.a().a()) != var5.a().a()) {
            this.method644(var3, var7);
            PlayerTimingSession var27 = this.boE.computeIfAbsent(var3.getUniqueId(), var2x -> new PlayerTimingSession(var3, var2 ? 800 : 200, 750, 10));

            try {
               if (var27.isCompleted()) {
                  Log.error("Player '" + var3.getName() + "' is abusing the recipe book changing recipe too fast. Hacking?");
                  var3.sendMessage(ChatColor.RED + "You are abusing the recipe book changing recipe too fast!");
                  var3.closeInventory();
                  this.boF.put(var3.getUniqueId(), System.currentTimeMillis());
                  return;
               }

               if (var27.isTimedOut()) {
                  return;
               }
            } finally {
               var27.stop();
            }
         }

         this.boD.put(var3.getUniqueId(), var5.a().a());
         if (var6 instanceof CraftingRecipe var28) {
            Map var9 = var28.Kt();
            Object2ObjectOpenHashMap var10 = new Object2ObjectOpenHashMap();
            var9.forEach((var1x, var2x) -> {
               if (var2x != null && var2x.getItem() != null && var2x.getItem().getType() != Material.AIR) {
                  var10.put(var1x, false);
               } else {
                  var10.put(var1x, true);
               }
            });
            Object2ObjectOpenHashMap var11 = new Object2ObjectOpenHashMap();
            PlayerInventoryProxy var12 = new PlayerInventoryProxy(var3);
            if (var2) {
               for (int var13 = 0; var13 < var12.getSize(); var13++) {
                  ItemSlotHandle var14 = var12.getSlotHandle(var13);
                  if (var14 != null && !var14.empty) {
                     while (true) {
                        boolean var15 = false;
                        if (var14.getAmount() - 1 <= 0) {
                           break;
                        }

                        for (Entry var17 : var9.entrySet()) {
                           ShapedIngredient var18 = (ShapedIngredient)var17.getValue();
                           if (var18 != null) {
                              int var19 = (Integer)var17.getKey();
                              int var20 = var19 + 1;
                              ItemStack var21 = (ItemStack)var11.get(var20);
                              if (var14.getAmount() <= 1) {
                                 break;
                              }

                              if (var21 == null || var21.isSimilar(var14.item)) {
                                 ItemStack var22 = var7.getItem(var20);
                                 int var23 = var22 != null && !var22.getType().isAir() ? var22.getAmount() : 0;
                                 if (var23 + (var21 == null ? 0 : var21.getAmount()) < ItemStackUtils.ax(var14.item)) {
                                    if (var18.Eb()) {
                                       CustomItemDefinition var24 = ContentManager.method8084().method8149(var14.item);
                                       if (var24 != null && var24.aol == var18.aBW) {
                                          var15 = true;
                                          var14.shrinkBy(1);
                                          if (var21 == null) {
                                             var21 = var14.item.clone();
                                             var21.setAmount(1);
                                             var11.put(var20, var21);
                                             var10.put(var19, true);
                                          } else if (var21.getAmount() + 1 <= ItemStackUtils.ax(var21)) {
                                             var21.setAmount(var21.getAmount() + 1);
                                          }
                                       }
                                    } else if (var18.Kf().test(var14.item)) {
                                       var15 = true;
                                       var14.shrinkBy(1);
                                       if (var21 == null) {
                                          var21 = var14.item.clone();
                                          var21.setAmount(1);
                                          var11.put(var20, var21);
                                          var10.put(var19, true);
                                       } else if (var21.getAmount() + 1 > ItemStackUtils.ax(var21)) {
                                          var14.setAmount(0);
                                       } else {
                                          var21.setAmount(var21.getAmount() + 1);
                                       }
                                    }
                                 }
                              }
                           }
                        }

                        if (!var15) {
                           break;
                        }
                     }
                  }
               }

               this.method838(var3, var12, var7, var10, var11, var1, var4);
            } else {
               for (Entry var30 : var9.entrySet()) {
                  ShapedIngredient var31 = (ShapedIngredient)var30.getValue();
                  if (var31 != null) {
                     int var32 = (Integer)var30.getKey();
                     int var33 = var32 + 1;
                     if (var31.Eb()) {
                        for (int var37 = 0; var37 < var12.getSize(); var37++) {
                           ItemSlotHandle var39 = var12.getSlotHandle(var37);
                           if (var39 != null && !var39.empty) {
                              CustomItemDefinition var42 = ContentManager.method8084().method8149(var39.item);
                              if (var42 != null && var42.aol == var31.aBW) {
                                 ItemStack var35 = var39.takeAmount(1);
                                 var11.put(var33, var35);
                                 var10.put(var32, true);
                                 break;
                              }
                           }
                        }
                     } else {
                        for (int var36 = 0; var36 < var12.getSize(); var36++) {
                           ItemSlotHandle var38 = var12.getSlotHandle(var36);
                           if (var38 != null && !var38.empty && var31.Kf().test(var38.item)) {
                              ItemStack var34 = var38.takeAmount(1);
                              var11.put(var33, var34);
                              var10.put(var32, true);
                              break;
                           }
                        }
                     }
                  }
               }

               this.method838(var3, var12, var7, var10, var11, var1, var4);
            }
         } else {
            Log.error("Custom recipe type not supported, probably a bug: " + var6.getClass().getSimpleName());
         }
      }
   }

   private void method838(Player var1, PlayerInventoryProxy var2, CraftingInventory var3, Map var4, Map var5, int var6, RecipeDisplayEntry var7) {
      if (var4.values().stream().allMatch(var0 -> var0)) {
         ItemStack[] var8 = var3.getMatrix();

         for (Entry var10 : var5.entrySet()) {
            ItemStack var11 = (ItemStack)var10.getValue();
            if (var11 != null) {
               int var12 = (Integer)var10.getKey() - 1;
               ItemStack var13 = var8[var12];
               if (var13 != null && !var13.getType().isAir()) {
                  if (!var13.isSimilar(var11) || var13.getAmount() + var11.getAmount() > ItemStackUtils.ax(var11)) {
                     return;
                  }

                  var11 = var11.clone();
                  var11.setAmount(var11.getAmount() + var13.getAmount());
               }

               var8[var12] = var11;
            }
         }

         var2.setCraftingMatrix(var3, var8);
      } else {
         PacketPlayOutAutoRecipe var14 = new PacketPlayOutAutoRecipe(var6, var7.b());
         NativePacketsHolder.PE().method1868(var1, var14);
      }
   }

   @Override
   public void unregister() {
      this.boA = false;
      this.boB++;
      IaListener.super.unregister();
      this.PV();
      if (this.boz != null) {
         NativePacketsHolder.PE().method1889(this.bof, this.boz);
      }

      this.boQ.clear();
      this.boD.clear();
      this.boE.clear();
      this.boF.clear();
      this.bot.clear();
   }

   @Override
   public Object method595(NamespacedKey key) {
      return MinecraftServer.getServer().aI().b(CraftRecipe.toMinecraft(key)).orElse(null);
   }

   @Override
   public Object method596(NamespacedKey namespacedKey) {
      return MinecraftKey.a(namespacedKey.toString());
   }

   @Override
   public void method597(Player player, List nmsRecipes) {
      ((CraftPlayer)player).getHandle().a(nmsRecipes);
   }

   @Override
   public void method839(Player player, @NotNull NamespacedKey recipeKey) {
      try {
         EntityPlayer var3 = ((CraftPlayer)player).getHandle();
         int var4 = var3.cd.l;
         Optional var5 = MinecraftServer.getServer().aI().b(CraftRecipe.toMinecraft(recipeKey));
         if (var5.isEmpty()) {
            return;
         }

         MinecraftServer.getServer().aI().a(((RecipeHolder)var5.get()).a(), var2x -> {
            PacketPlayOutAutoRecipe var3x = new PacketPlayOutAutoRecipe(var4, var2x.b());
            NativePacketsHolder.PE().method1868(player, var3x);
         });
      } catch (Exception var6) {
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void method598(Set namespaces) {
      net.minecraft.world.item.crafting.CraftingManager var2 = MinecraftServer.getServer().aI();
      LinkedHashMultimap var3 = LinkedHashMultimap.create(var2.e.b);
      boolean var4 = false;

      for (RecipeType var8 : RecipeType.values()) {
         Recipes var9 = switch (SwitchMap62.boS[var8.ordinal()]) {
            case 1 -> Recipes.a;
            case 2 -> Recipes.b;
            case 3 -> Recipes.c;
            case 4 -> Recipes.d;
            case 5 -> Recipes.e;
            case 6 -> Recipes.f;
            case 7 -> Recipes.g;
            default -> throw new MatchException(null, null);
         };
         Set var10 = var3.get(var9);
         if (!var10.isEmpty()) {
            ArrayList var11 = new ArrayList();
            ArrayList var12 = new ArrayList();

            for (RecipeHolder var14 : var10) {
               if (namespaces.contains(var14.a().a().b())) {
                  var11.add(var14);
               } else {
                  var12.add(var14);
               }
            }

            if (!var11.isEmpty()) {
               ArrayList var16 = new ArrayList(var10.size());
               var16.addAll(var11);
               var16.addAll(var12);
               var3.replaceValues(var9, var16);
               var4 = true;
            }
         }
      }

      if (var4) {
         try {
            boq.set(var2.e, var3);
         } catch (IllegalAccessException var15) {
            throw new RuntimeException(var15);
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void PW() {
      net.minecraft.world.item.crafting.CraftingManager var1 = MinecraftServer.getServer().aI();
      LinkedHashMultimap var2 = LinkedHashMultimap.create(var1.e.b);
      boolean var3 = false;

      for (RecipeType var7 : RecipeType.values()) {
         Recipes var8 = switch (SwitchMap62.boS[var7.ordinal()]) {
            case 1 -> Recipes.a;
            case 2 -> Recipes.b;
            case 3 -> Recipes.c;
            case 4 -> Recipes.d;
            case 5 -> Recipes.e;
            case 6 -> Recipes.f;
            case 7 -> Recipes.g;
            default -> throw new MatchException(null, null);
         };
         Set var9 = var2.get(var8);
         if (!var9.isEmpty()) {
            ArrayList var10 = new ArrayList();
            ArrayList var11 = new ArrayList();

            for (RecipeHolder var13 : var9) {
               if (method834(var13)) {
                  var10.add(var13);
               } else {
                  var11.add(var13);
               }
            }

            if (!var10.isEmpty()) {
               ArrayList var15 = new ArrayList(var9.size());
               var15.addAll(var10);
               var15.addAll(var11);
               var2.replaceValues(var8, var15);
               var3 = true;
            }
         }
      }

      if (var3) {
         try {
            boq.set(var1.e, var2);
         } catch (IllegalAccessException var14) {
            throw new RuntimeException(var14);
         }
      }
   }

   private static TileEntityFurnace method840(Furnace var0) {
      CraftFurnace var1 = (CraftFurnace)var0;
      return (TileEntityFurnace)var1.getWorldHandle().c_(var1.getPosition());
   }

   @Override
   public NamespacedKey method600(Furnace furnace) {
      TileEntityFurnace var2 = method840(furnace);
      net.minecraft.world.item.crafting.CraftingManager.a var3 = (net.minecraft.world.item.crafting.CraftingManager.a)ServerCompat.readField(bor, var2);
      if (bos == null) {
         bos = ServerCompat.findFieldByType(var3.getClass(), ResourceKey.class);
      }

      assert bos != null;
      if (ServerCompat.readField(bos, var3) instanceof ResourceKey var4) {
         Optional var6 = MinecraftServer.getServer().aI().b(var4);
         if (var6.isPresent()) {
            return new NamespacedKey(var4.a().b(), var4.a().a());
         }
      }

      return null;
   }

   @Override
   public void method601(Furnace furnace, NamespacedKey bukkitKey) {
      TileEntityFurnace var3 = method840(furnace);
      net.minecraft.world.item.crafting.CraftingManager.a var4 = (net.minecraft.world.item.crafting.CraftingManager.a)ServerCompat.readField(bor, var3);
      if (bos == null) {
         bos = ServerCompat.findFieldByType(var4.getClass(), ResourceKey.class);
      }

      assert bos != null;
      RecipeHolder var5 = (RecipeHolder)MinecraftServer.getServer().aI().b(CraftRecipe.toMinecraft(bukkitKey)).orElse(null);
      ServerCompat.writeField(bos, var4, var5 == null ? null : var5.a());
   }

   @Override
   public Set method602(Set validNamespaces) {
      net.minecraft.world.item.crafting.CraftingManager var2 = MinecraftServer.getServer().aI();
      ObjectOpenHashSet var3 = new ObjectOpenHashSet();

      for (RecipeHolder var5 : var2.e.b.get(Recipes.a)) {
         if (validNamespaces.contains(var5.a().a().b())) {
            var3.add(var5.a().a().toString());
         }
      }

      return var3;
   }

   @Override
   public String method603(CraftingInventory bukkitCraftingInventory) {
      CraftInventoryCrafting var2 = (CraftInventoryCrafting)bukkitCraftingInventory;
      if (var2.getInventory().getOwner() instanceof CraftPlayer var3) {
         net.minecraft.world.item.crafting.CraftingManager var10 = MinecraftServer.getServer().aI();
         CraftingInput var5 = var2.getMatrixInventory().aB_();
         World var6 = var3.getHandle().dV();

         for (RecipeHolder var8 : var10.e.b.get(Recipes.a)) {
            IRecipe var9 = var8.b();
            if (!method834(var8) && var9.a(var5, var6)) {
               return var8.a().a().toString();
            }
         }
      }

      return null;
   }

   @Override
   public void PX() {
      CrafterCraftListenerE var1 = new CrafterCraftListenerE();
      EventRegistrar.registerIfAbsent(var1, this.bof);
   }

   @Override
   public boolean method604(ShapedRecipe bukkitRecipe, CraftingInventory bukkitCraftingInventory) {
      InventoryCrafting var3 = ((CraftInventoryCrafting)bukkitCraftingInventory).getMatrixInventory();
      ShapedRecipePattern var4 = this.bot.computeIfAbsent(bukkitRecipe.getKey(), var1x -> {
         CraftShapedRecipe var2x = (CraftShapedRecipe)Bukkit.getRecipe(bukkitRecipe.getKey());
         assert var2x != null;
         Map var3x = var2x.getChoiceMap();
         String[] var4x = RecipeManagerHolder.method5358(var2x.getShape(), var3x);
         var3x.values().removeIf(Objects::isNull);
         Map var5x = Maps.transformValues(var3x, var1xx -> var2x.toNMS(var1xx, false));
         return ShapedRecipePattern.a(var5x, var4x);
      });
      List var5 = var4.c();

      for (int var6 = 0; var6 <= var3.ay_() - var4.a(); var6++) {
         for (int var7 = 0; var7 <= var3.g() - var4.b(); var7++) {
            if (this.method841(var3, var6, var7, var5, var4.a(), var4.b(), true)) {
               return true;
            }

            if (this.method841(var3, var6, var7, var5, var4.a(), var4.b(), false)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean method841(InventoryCrafting var1, int var2, int var3, List var4, int var5, int var6, boolean var7) {
      for (int var8 = 0; var8 < var1.ay_(); var8++) {
         for (int var9 = 0; var9 < var1.g(); var9++) {
            int var10 = var8 - var2;
            int var11 = var9 - var3;
            Optional var12 = Optional.empty();
            int var13 = var11 * var5;
            if (var10 >= 0 && var11 >= 0 && var10 < var5 && var11 < var6) {
               if (var7) {
                  var12 = (Optional)var4.get(var5 - var10 - 1 + var13);
               } else {
                  var12 = (Optional)var4.get(var10 + var13);
               }
            }

            net.minecraft.world.item.ItemStack var14 = var1.a(var8 + var9 * var1.ay_());
            if (!method842((RecipeItemStack)var12.orElse(null), var14)) {
               return false;
            }
         }
      }

      return true;
   }

   private static boolean method842(@Nullable RecipeItemStack var0, net.minecraft.world.item.ItemStack var1) {
      if (var0 == null && var1 == null) {
         return true;
      }

      if (var0 == null) {
         return var1.f();
      }

      if (var1 == null) {
         return false;
      }

      if (var0.isExact()) {
         for (net.minecraft.world.item.ItemStack var3 : var0.itemStacks()) {
            if (var3.h() == var1.h() && var3.d().d() == var1.d().d()) {
               if (ItemRegistryBridgeV1_21_R3.method3444(var3) && ItemRegistryBridgeV1_21_R3.method3444(var1)) {
                  if (ItemRegistryBridgeV1_21_R3.method3446(var3, var1)) {
                     return true;
                  }
               } else if (net.minecraft.world.item.ItemStack.c(var1, var3)) {
                  return true;
               }
            }
         }
      } else {
         for (Holder var5 : var0.a().toList()) {
            if (var1.a(var5) && !ItemRegistryBridgeV1_21_R3.method3444(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   @EventHandler
   private void method516(PlayerQuitEvent var1) {
      this.boD.remove(var1.getPlayer().getUniqueId());
      this.boE.remove(var1.getPlayer().getUniqueId());
      this.boF.remove(var1.getPlayer().getUniqueId());
      this.boQ.remove(var1.getPlayer().getUniqueId());
   }

   @EventHandler
   private void method655(InventoryCloseEvent var1) {
      this.boD.remove(var1.getPlayer().getUniqueId());
      this.boQ.remove(var1.getPlayer().getUniqueId());
   }

   static {
      if (boq == null) {
         throw new RuntimeException("RecipeMap#byType not found! Try to update the server or downgrade it to a previous build.");
      }

      bor = ServerCompat.findFieldByType(TileEntityFurnace.class, net.minecraft.world.item.crafting.CraftingManager.a.class);
      boG = ServerCompat.findFieldByTypeIndex(TileEntityFurnace.class, net.minecraft.world.item.crafting.CraftingManager.a.class, 0);
      if (boG == null) {
         new RuntimeException("Error getting fieldQuickCheck from AbstractFurnaceBlockEntity.").printStackTrace();
      }
   }
}
