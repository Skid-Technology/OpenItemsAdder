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
import dev.lone.itemsadder.internal.SwitchMap69;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemRegistryBridgeGenericDup3;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
import net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket.Entry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.AbstractCookingRecipe.CookingBookInfo;
import net.minecraft.world.item.crafting.CraftingRecipe.CraftingBookInfo;
import net.minecraft.world.item.crafting.Recipe.CommonInfo;
import net.minecraft.world.item.crafting.RecipeManager.CachedCheck;
import net.minecraft.world.item.crafting.RecipeManager.ServerDisplayInfo;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_21_R7.block.CraftFurnace;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftBlastingRecipe;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftFurnaceRecipe;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftRecipe;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftShapelessRecipe;
import org.bukkit.craftbukkit.v1_21_R7.inventory.CraftSmokingRecipe;
import org.bukkit.craftbukkit.v1_21_R7.util.CraftNamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spigotmc.AsyncCatcher;

public class RecipeManagerImplGenericDup3 extends AbstractRecipeManager implements IaListener {
   private static Method boy;
   protected PacketListenerHandle boz;
   private volatile boolean boA;
   private volatile long boB;
   private final Map bpl = new ConcurrentHashMap();
   Map boD = new ConcurrentHashMap();
   Map boE = new ConcurrentHashMap();
   Map boF = new ConcurrentHashMap();
   static Field boq;
   static Field bor;
   static Field bos;
   Map bot = new Object2ObjectOpenHashMap();
   static Field boG;
   static Method boH;

   private static boolean method805(RecipeHolder var0) {
      return var0.id().identifier().getNamespace().startsWith(AbstractRecipeManager.boe);
   }

   @Override
   public void register() {
      this.boi = NativePacketsHolder.PE()
         .method1888(this, PacketSelector.exact(PacketTypes.RECIPE_BOOK), ListenerPriority.LOWEST, false, ListenerFailurePolicy.PASS_ORIGINAL, var0 -> {
            RecipeBookPacketView var1x = (RecipeBookPacketView)var0.view(RecipeBookPacketView.class);
            if (var1x.action() == RecipeBookAction.ADD) {
               for (int var2 = var1x.recipeCount() - 1; var2 >= 0; var2--) {
                  NativeValue var3 = var1x.recipe(var2);
                  Entry var4 = (Entry)var3.nativeHandle();
                  ServerDisplayInfo var5 = MinecraftServer.getServer().getRecipeManager().getRecipeFromDisplay(var4.contents().id());
                  if (method805(var5.parent())) {
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
               var3 -> this.method806(var3, (PlaceRecipePacketView)var3.view(PlaceRecipePacketView.class), var1)
            );
      }

      this.PU();
      EventRegistrar.registerListener(this, this.bof);
   }

   private void method806(PacketEvent var1, PlaceRecipePacketView var2, long var3) {
      if (this.boA && this.boB == var3) {
         long var5 = var1.connection().id();
         int var7 = var2.containerId();
         int var8 = ((RecipeDisplayId)var2.recipe().nativeHandle()).index();
         boolean var9 = var2.useMaxItems();
         var1.sync(
            (var8x, var9x) -> {
               if (this.boA
                  && this.boB == var3
                  && var9x.player() instanceof Player var10
                  && var10.isOnline()
                  && NativePacketsHolder.PE().method1885(var10.getUniqueId(), var5) == var10) {
                  this.method807(var8x, var10, var7, var8, var9);
               }
            }
         );
      }
   }

   private void method807(PacketEvent var1, Player var2, int var3, int var4, boolean var5) {
      ServerDisplayInfo var6 = MinecraftServer.getServer().getRecipeManager().getRecipeFromDisplay(new RecipeDisplayId(var4));
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

         RecipeHolder var9 = var6.parent();
         AbstractCustomRecipe var10 = ContentManager.method8084().bp.dV(var9.id().identifier().toString());
         if (var10 != null) {
            var1.cancel();
            if (this.bpl.putIfAbsent(var7, true) == null) {
               try {
                  this.method808(var3, var5, var2, var6.display(), var9, var10);
               } finally {
                  this.bpl.remove(var7);
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

   private void method808(int var1, boolean var2, Player var3, RecipeDisplayEntry var4, RecipeHolder var5, AbstractCustomRecipe var6) {
      if (var3.getOpenInventory().getTopInventory() instanceof CraftingInventory var7) {
         if (!this.boD.containsKey(var3.getUniqueId())) {
            this.method644(var3, var7);
         }

         if (this.boD.getOrDefault(var3.getUniqueId(), var5.id().identifier()) != var5.id().identifier()) {
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

         this.boD.put(var3.getUniqueId(), var5.id().identifier());
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

                        for (java.util.Map.Entry var17 : var9.entrySet()) {
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

               this.method809(var3, var12, var7, var10, var11, var1, var4);
            } else {
               for (java.util.Map.Entry var30 : var9.entrySet()) {
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

               this.method809(var3, var12, var7, var10, var11, var1, var4);
            }
         } else {
            Log.error("Custom recipe type not supported, probably a bug: " + var6.getClass().getSimpleName());
         }
      }
   }

   private void method809(Player var1, PlayerInventoryProxy var2, CraftingInventory var3, Map var4, Map var5, int var6, RecipeDisplayEntry var7) {
      if (var4.values().stream().allMatch(var0 -> var0)) {
         ItemStack[] var8 = var3.getMatrix();

         for (java.util.Map.Entry var10 : var5.entrySet()) {
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
         ClientboundPlaceGhostRecipePacket var14 = new ClientboundPlaceGhostRecipePacket(var6, var7.display());
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

      this.bpl.clear();
      this.boD.clear();
      this.boE.clear();
      this.boF.clear();
      this.bot.clear();
   }

   @Override
   public Object method595(NamespacedKey key) {
      return MinecraftServer.getServer().getRecipeManager().byKey(method767(key)).orElse(null);
   }

   static ResourceKey/* @NotNull */ method767(NamespacedKey var0) {
      return ResourceKey.create(Registries.RECIPE, CraftNamespacedKey.toMinecraft(var0));
   }

   @Override
   public Object method596(NamespacedKey namespacedKey) {
      return Identifier.parse(namespacedKey.toString());
   }

   @Override
   public void method597(Player player, List nmsRecipes) {
      ((CraftPlayer)player).getHandle().awardRecipes(nmsRecipes);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void method598(Set namespaces) {
      net.minecraft.world.item.crafting.RecipeManager var2 = MinecraftServer.getServer().getRecipeManager();
      LinkedHashMultimap var3 = LinkedHashMultimap.create(var2.recipes.byType);
      boolean var4 = false;

      for (RecipeType var8 : RecipeType.values()) {
         net.minecraft.world.item.crafting.RecipeType var9 = switch (SwitchMap69.bpn[var8.ordinal()]) {
            case 1 -> net.minecraft.world.item.crafting.RecipeType.CRAFTING;
            case 2 -> net.minecraft.world.item.crafting.RecipeType.SMELTING;
            case 3 -> net.minecraft.world.item.crafting.RecipeType.BLASTING;
            case 4 -> net.minecraft.world.item.crafting.RecipeType.SMOKING;
            case 5 -> net.minecraft.world.item.crafting.RecipeType.CAMPFIRE_COOKING;
            case 6 -> net.minecraft.world.item.crafting.RecipeType.STONECUTTING;
            case 7 -> net.minecraft.world.item.crafting.RecipeType.SMITHING;
            default -> throw new MatchException(null, null);
         };
         Set var10 = var3.get(var9);
         if (!var10.isEmpty()) {
            ArrayList var11 = new ArrayList();
            ArrayList var12 = new ArrayList();

            for (RecipeHolder var14 : var10) {
               if (namespaces.contains(var14.id().identifier().getNamespace())) {
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
            boq.set(var2.recipes, var3);
         } catch (IllegalAccessException var15) {
            throw new RuntimeException(var15);
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void PW() {
      net.minecraft.world.item.crafting.RecipeManager var1 = MinecraftServer.getServer().getRecipeManager();
      LinkedHashMultimap var2 = LinkedHashMultimap.create(var1.recipes.byType);
      boolean var3 = false;

      for (RecipeType var7 : RecipeType.values()) {
         net.minecraft.world.item.crafting.RecipeType var8 = switch (SwitchMap69.bpn[var7.ordinal()]) {
            case 1 -> net.minecraft.world.item.crafting.RecipeType.CRAFTING;
            case 2 -> net.minecraft.world.item.crafting.RecipeType.SMELTING;
            case 3 -> net.minecraft.world.item.crafting.RecipeType.BLASTING;
            case 4 -> net.minecraft.world.item.crafting.RecipeType.SMOKING;
            case 5 -> net.minecraft.world.item.crafting.RecipeType.CAMPFIRE_COOKING;
            case 6 -> net.minecraft.world.item.crafting.RecipeType.STONECUTTING;
            case 7 -> net.minecraft.world.item.crafting.RecipeType.SMITHING;
            default -> throw new MatchException(null, null);
         };
         Set var9 = var2.get(var8);
         if (!var9.isEmpty()) {
            ArrayList var10 = new ArrayList();
            ArrayList var11 = new ArrayList();

            for (RecipeHolder var13 : var9) {
               if (method805(var13)) {
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
            boq.set(var1.recipes, var2);
         } catch (IllegalAccessException var14) {
            throw new RuntimeException(var14);
         }
      }
   }

   private static AbstractFurnaceBlockEntity method810(Furnace var0) {
      CraftFurnace var1 = (CraftFurnace)var0;
      return (AbstractFurnaceBlockEntity)var1.getWorldHandle().getBlockEntity(var1.getPosition());
   }

   @Override
   public NamespacedKey method600(Furnace furnace) {
      AbstractFurnaceBlockEntity var2 = method810(furnace);
      CachedCheck var3 = (CachedCheck)ServerCompat.readField(bor, var2);
      if (bos == null) {
         bos = ServerCompat.findFieldByType(var3.getClass(), ResourceKey.class);
      }

      assert bos != null;
      if (ServerCompat.readField(bos, var3) instanceof ResourceKey var4) {
         Optional var6 = MinecraftServer.getServer().getRecipeManager().byKey(var4);
         if (var6.isPresent()) {
            return new NamespacedKey(var4.identifier().getNamespace(), var4.identifier().getPath());
         }
      }

      return null;
   }

   @Override
   public void method601(Furnace furnace, NamespacedKey bukkitKey) {
      AbstractFurnaceBlockEntity var3 = method810(furnace);
      CachedCheck var4 = (CachedCheck)ServerCompat.readField(bor, var3);
      if (bos == null) {
         bos = ServerCompat.findFieldByType(var4.getClass(), ResourceKey.class);
      }

      assert bos != null;
      RecipeHolder var5 = (RecipeHolder)MinecraftServer.getServer().getRecipeManager().byKey(method767(bukkitKey)).orElse(null);
      ServerCompat.writeField(bos, var4, var5 == null ? null : var5.id());
   }

   @Override
   public Set method602(Set validNamespaces) {
      net.minecraft.world.item.crafting.RecipeManager var2 = MinecraftServer.getServer().getRecipeManager();
      ObjectOpenHashSet var3 = new ObjectOpenHashSet();

      for (RecipeHolder var5 : var2.recipes.byType.get(net.minecraft.world.item.crafting.RecipeType.CRAFTING)) {
         if (validNamespaces.contains(var5.id().identifier().getNamespace())) {
            var3.add(var5.id().identifier().toString());
         }
      }

      return var3;
   }

   @Override
   public String method603(CraftingInventory bukkitCraftingInventory) {
      CraftInventoryCrafting var2 = (CraftInventoryCrafting)bukkitCraftingInventory;
      if (var2.getInventory().getOwner() instanceof CraftPlayer var3) {
         net.minecraft.world.item.crafting.RecipeManager var10 = MinecraftServer.getServer().getRecipeManager();
         CraftingInput var5 = var2.getMatrixInventory().asCraftInput();
         ServerLevel var6 = var3.getHandle().level();

         for (RecipeHolder var8 : var10.recipes.byType.get(net.minecraft.world.item.crafting.RecipeType.CRAFTING)) {
            Recipe var9 = var8.value();
            if (!method805(var8) && var9.matches(var5, var6)) {
               return var8.id().identifier().toString();
            }
         }
      }

      return null;
   }

   @Override
   public void PX() {
      CrafterCraftListenerL var1 = new CrafterCraftListenerL();
      EventRegistrar.registerIfAbsent(var1, this.bof);
   }

   @Override
   public boolean method648(org.bukkit.inventory.Recipe bukkitRecipe) {
      AsyncCatcher.catchOp("Recipe Add");
      if (bukkitRecipe instanceof org.bukkit.inventory.SmithingRecipe) {
         Bukkit.addRecipe(bukkitRecipe);
         return true;
      }

      Object var2;
      if (bukkitRecipe instanceof CraftRecipe) {
         var2 = (CraftRecipe)bukkitRecipe;
      } else {
         switch (bukkitRecipe) {
            case ShapedRecipe var5:
               var2 = CraftShapedRecipe.fromBukkitRecipe(var5);
               break;
            case ShapelessRecipe var6:
               var2 = CraftShapelessRecipe.fromBukkitRecipe(var6);
               break;
            case FurnaceRecipe var7:
               var2 = CraftFurnaceRecipe.fromBukkitRecipe(var7);
               break;
            case BlastingRecipe var8:
               var2 = CraftBlastingRecipe.fromBukkitRecipe(var8);
               break;
            case SmokingRecipe var9:
               var2 = CraftSmokingRecipe.fromBukkitRecipe(var9);
               break;
            case null:
            default:
               return false;
         }
      }

      if (boy == null) {
         var2.addToRecipeManager();
         return true;
      }

      switch (bukkitRecipe) {
         case ShapedRecipe var13:
            Map var15 = var13.getChoiceMap();
            String[] var18 = method811(var13.getShape(), var15);
            var15.values().removeIf(Objects::isNull);
            Map var22 = Maps.transformValues(var15, var1x -> method772(var2, var1x, false));
            MinecraftServer.getServer()
               .getRecipeManager()
               .recipes
               .addRecipe(
                  new RecipeHolder(
                     method767(var13.getKey()),
                     new net.minecraft.world.item.crafting.ShapedRecipe(
                        new CommonInfo(true),
                        new CraftingBookInfo(CraftRecipe.getCategory(var13.getCategory()), var13.getGroup()),
                        ShapedRecipePattern.of(var22, var18),
                        ItemStackTemplate.fromNonEmptyStack(CraftItemStack.asNMSCopy(var13.getResult()))
                     )
                  )
               );
            break;
         case ShapelessRecipe var14:
            List var17 = var14.getChoiceList();
            ArrayList var21 = new ArrayList(var17.size());

            for (RecipeChoice var26 : var17) {
               var21.add(method772((CraftRecipe)var2, var26, true));
            }

            MinecraftServer.getServer()
               .getRecipeManager()
               .recipes
               .addRecipe(
                  new RecipeHolder(
                     method767(var14.getKey()),
                     new net.minecraft.world.item.crafting.ShapelessRecipe(
                        new CommonInfo(true),
                        new CraftingBookInfo(CraftRecipe.getCategory(var14.getCategory()), var14.getGroup()),
                        ItemStackTemplate.fromNonEmptyStack(CraftItemStack.asNMSCopy(var14.getResult())),
                        var21
                     )
                  )
               );
            break;
         case FurnaceRecipe var16:
            ItemStack var20 = var16.getResult();
            MinecraftServer.getServer()
               .getRecipeManager()
               .recipes
               .addRecipe(
                  new RecipeHolder(
                     method767(var16.getKey()),
                     new SmeltingRecipe(
                        new CommonInfo(true),
                        new CookingBookInfo(CraftRecipe.getCategory(var16.getCategory()), var16.getGroup()),
                        method772((CraftRecipe)var2, var16.getInputChoice(), true),
                        ItemStackTemplate.fromNonEmptyStack(CraftItemStack.asNMSCopy(var20)),
                        var16.getExperience(),
                        var16.getCookingTime()
                     )
                  )
               );
            break;
         case BlastingRecipe var19:
            ItemStack var24 = var19.getResult();
            MinecraftServer.getServer()
               .getRecipeManager()
               .recipes
               .addRecipe(
                  new RecipeHolder(
                     method767(var19.getKey()),
                     new net.minecraft.world.item.crafting.BlastingRecipe(
                        new CommonInfo(true),
                        new CookingBookInfo(CraftRecipe.getCategory(var19.getCategory()), var19.getGroup()),
                        method772((CraftRecipe)var2, var19.getInputChoice(), true),
                        ItemStackTemplate.fromNonEmptyStack(CraftItemStack.asNMSCopy(var24)),
                        var19.getExperience(),
                        var19.getCookingTime()
                     )
                  )
               );
            break;
         case SmokingRecipe var23:
            ItemStack var10 = var23.getResult();
            MinecraftServer.getServer()
               .getRecipeManager()
               .recipes
               .addRecipe(
                  new RecipeHolder(
                     method767(var23.getKey()),
                     new net.minecraft.world.item.crafting.SmokingRecipe(
                        new CommonInfo(true),
                        new CookingBookInfo(CraftRecipe.getCategory(var23.getCategory()), var23.getGroup()),
                        method772((CraftRecipe)var2, var23.getInputChoice(), true),
                        ItemStackTemplate.fromNonEmptyStack(CraftItemStack.asNMSCopy(var10)),
                        var23.getExperience(),
                        var23.getCookingTime()
                     )
                  )
               );
            break;
         default:
            throw new IllegalStateException("Unexpected value: " + bukkitRecipe);
      }

      return true;
   }

   static Ingredient method772(CraftRecipe var0, RecipeChoice var1, boolean var2) {
      try {
         return (Ingredient)boy.invoke(var0, var1, var2);
      } catch (IllegalAccessException | InvocationTargetException var4) {
         throw new RuntimeException(var4);
      }
   }

   private static String[] method811(String[] var0, Map var1) {
      for (int var2 = 0; var2 < var0.length; var2++) {
         String var3 = var0[var2];
         StringBuilder var4 = new StringBuilder(var3.length());

         for (char var8 : var3.toCharArray()) {
            var4.append(var1.get(var8) == null ? ' ' : var8);
         }

         var0[var2] = var4.toString();
      }

      return var0;
   }

   @Override
   public boolean method651(NamespacedKey recipeKey) {
      ResourceKey var2 = method767(recipeKey);
      return MinecraftServer.getServer().getRecipeManager().recipes.removeRecipe(var2);
   }

   @Override
   public void PY() {
      MinecraftServer.getServer().getRecipeManager().finalizeRecipeLoading();
   }

   @Override
   public boolean method604(ShapedRecipe bukkitRecipe, CraftingInventory bukkitCraftingInventory) {
      CraftingContainer var3 = ((CraftInventoryCrafting)bukkitCraftingInventory).getMatrixInventory();
      ShapedRecipePattern var4 = this.bot.computeIfAbsent(bukkitRecipe.getKey(), var1x -> {
         CraftShapedRecipe var2x = (CraftShapedRecipe)Bukkit.getRecipe(bukkitRecipe.getKey());
         assert var2x != null;
         Map var3x = var2x.getChoiceMap();
         String[] var4x = RecipeManagerHolder.method5358(var2x.getShape(), var3x);
         var3x.values().removeIf(Objects::isNull);
         Map var5x = Maps.transformValues(var3x, var1xx -> method772(var2x, var1xx, false));
         return ShapedRecipePattern.of(var5x, var4x);
      });
      List var5 = var4.ingredients();

      for (int var6 = 0; var6 <= var3.getWidth() - var4.width(); var6++) {
         for (int var7 = 0; var7 <= var3.getHeight() - var4.height(); var7++) {
            if (this.method812(var3, var6, var7, var5, var4.width(), var4.height(), true)) {
               return true;
            }

            if (this.method812(var3, var6, var7, var5, var4.width(), var4.height(), false)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean method812(CraftingContainer var1, int var2, int var3, List var4, int var5, int var6, boolean var7) {
      for (int var8 = 0; var8 < var1.getWidth(); var8++) {
         for (int var9 = 0; var9 < var1.getHeight(); var9++) {
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

            net.minecraft.world.item.ItemStack var14 = var1.getItem(var8 + var9 * var1.getWidth());
            if (!method813((Ingredient)var12.orElse(null), var14)) {
               return false;
            }
         }
      }

      return true;
   }

   static Collection method775(Ingredient var0) {
      try {
         if (boH == null) {
            boH = var0.getClass().getMethod("itemStacks");
         }

         Object var1 = boH.invoke(var0);
         if (var1 instanceof Collection) {
            return (Collection)var1;
         }
      } catch (Exception var2) {
      }

      return Collections.emptyList();
   }

   private static boolean method813(@Nullable Ingredient var0, net.minecraft.world.item.ItemStack var1) {
      if (var0 == null && var1 == null) {
         return true;
      }

      if (var0 == null) {
         return var1.isEmpty();
      }

      if (var1 == null) {
         return false;
      }

      if (var0.isExact()) {
         for (net.minecraft.world.item.ItemStack var3 : method775(var0)) {
            if (var3.getItem() == var1.getItem() && var3.getComponentsPatch().isEmpty() == var1.getComponentsPatch().isEmpty()) {
               if (ItemRegistryBridgeGenericDup3.method3444(var3) && ItemRegistryBridgeGenericDup3.method3444(var1)) {
                  if (ItemRegistryBridgeGenericDup3.method3446(var3, var1)) {
                     return true;
                  }
               } else if (net.minecraft.world.item.ItemStack.isSameItemSameComponents(var1, var3)) {
                  return true;
               }
            }
         }
      } else {
         for (Holder var5 : var0.items().toList()) {
            if (var1.is(var5) && !ItemRegistryBridgeGenericDup3.method3444(var1)) {
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
      this.bpl.remove(var1.getPlayer().getUniqueId());
   }

   @EventHandler
   private void method655(InventoryCloseEvent var1) {
      this.boD.remove(var1.getPlayer().getUniqueId());
      this.bpl.remove(var1.getPlayer().getUniqueId());
   }

   static {
      try {
         for (Method var3 : CraftRecipe.class.getMethods()) {
            if (var3.getReturnType() == Ingredient.class) {
               Class[] var4 = var3.getParameterTypes();
               if (var4.length == 2 && RecipeChoice.class.isAssignableFrom(var4[0]) && var4[1] == boolean.class) {
                  var3.setAccessible(true);
                  boy = var3;
               }
            }
         }
      } catch (Exception var5) {
         Log.error("Failed to find CraftRecipe#toIngredient method, this is a bug, please report it.", var5);
         boy = null;
      }

      boq = ServerCompat.findFieldByTypeIndex(RecipeMap.class, Multimap.class, 0);
      if (boq == null) {
         throw new RuntimeException("RecipeMap#byType not found! Try to update the server or downgrade it to a previous build.");
      }

      bor = ServerCompat.findFieldByType(AbstractFurnaceBlockEntity.class, CachedCheck.class);
      boG = ServerCompat.findFieldByTypeIndex(AbstractFurnaceBlockEntity.class, CachedCheck.class, 0);
      if (boG == null) {
         Log.error("Failed to find quickCheck field from AbstractFurnaceBlockEntity.");
      }
   }
}
