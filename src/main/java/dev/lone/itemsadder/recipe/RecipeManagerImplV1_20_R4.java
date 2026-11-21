package dev.lone.itemsadder.recipe;

import beer.devs.nativepackets.api.ListenerFailurePolicy;
import beer.devs.nativepackets.api.ListenerPriority;
import beer.devs.nativepackets.api.NativeValue;
import beer.devs.nativepackets.api.PacketEvent;
import beer.devs.nativepackets.api.PacketSelector;
import beer.devs.nativepackets.api.PacketTypes;
import beer.devs.nativepackets.api.RecipeBookPacketView;
import beer.devs.nativepackets.api.UpdateRecipesPacketView;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.event.IaListener;
import dev.lone.itemsadder.internal.SwitchMap57;
import dev.lone.itemsadder.item.ItemRegistryBridgeV1_20_R4;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.PersistentCounter;
import dev.lone.itemsadder.util.ServerCompat;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.PacketPlayOutRecipes;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.IInventory;
import net.minecraft.world.inventory.InventoryCrafting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.IRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeItemStack;
import net.minecraft.world.item.crafting.Recipes;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapedRecipes;
import net.minecraft.world.item.crafting.ShapelessRecipes;
import net.minecraft.world.item.crafting.CraftingManager.a;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.entity.TileEntityFurnace;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_20_R4.block.CraftFurnace;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.v1_20_R4.util.CraftNamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ShapedRecipe;

public class RecipeManagerImplV1_20_R4 extends AbstractRecipeManager implements IaListener {
   static Field boq = ServerCompat.findFieldByTypeIndex(net.minecraft.world.item.crafting.CraftingManager.class, Multimap.class, 0);
   static Field bor;
   static Field bos;
   Map bot = new Object2ObjectOpenHashMap();

   private static boolean method614(RecipeHolder var0) {
      return var0.a().b().startsWith(AbstractRecipeManager.boe);
   }

   @Override
   public void register() {
      this.boh = NativePacketsHolder.PE()
         .method1888(
            this,
            PacketSelector.exact(PacketTypes.UPDATE_RECIPES),
            ListenerPriority.NORMAL,
            false,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var1 -> this.method615(var1, (UpdateRecipesPacketView)var1.view(UpdateRecipesPacketView.class))
         );
      this.boi = NativePacketsHolder.PE()
         .method1888(
            this,
            PacketSelector.exact(PacketTypes.RECIPE_BOOK),
            ListenerPriority.LOWEST,
            false,
            ListenerFailurePolicy.PASS_ORIGINAL,
            var1 -> this.method616(var1, (RecipeBookPacketView)var1.view(RecipeBookPacketView.class))
         );
      this.PU();
   }

   private void method615(PacketEvent var1, UpdateRecipesPacketView var2) {
      if (var2.hasLegacyRecipes()) {
         long var3 = PersistentCounter.now();
         AtomicInteger var5 = new AtomicInteger();
         if (!Settings.get().fixPacketTooLargeKick) {
            for (int var11 = var2.recipeCount() - 1; var11 >= 0; var11--) {
               RecipeHolder var12 = (RecipeHolder)var2.recipe(var11).nativeHandle();
               if (method614(var12)) {
                  var2.removeRecipe(var11);
               }
            }

            method620(var2.recipeCount(), var5, var3);
         } else {
            ArrayList var6 = new ArrayList(var2.recipeCount());
            boolean var7 = false;

            for (int var8 = 0; var8 < var2.recipeCount(); var8++) {
               RecipeHolder var9 = (RecipeHolder)var2.recipe(var8).nativeHandle();
               if (method614(var9)) {
                  var7 = true;
               } else {
                  RecipeHolder var10 = var9;
                  if (var9.b() instanceof ShapelessRecipes || var9.b() instanceof ShapedRecipes) {
                     var10 = this.bom.computeIfAbsent(var9.a(), var3x -> this.method619(var9, var5));
                  }

                  var6.add(var10);
                  var7 |= var10 != var9;
               }
            }

            if (var7) {
               NativeValue var13 = () -> var6;
               var1.replaceNativePacket(NativePacketsHolder.PE().Pv().updateRecipes(var13, null));
            }

            method620(var6.size(), var5, var3);
         }
      }
   }

   private void method616(PacketEvent var1, RecipeBookPacketView var2) {
      PacketPlayOutRecipes var3 = (PacketPlayOutRecipes)var2.nativeHandle();
      List var4 = method617(var3.e());
      if (var4 == var3.e()) {
         for (int var5 = var2.recipeCount() - 1; var5 >= 0; var5--) {
            MinecraftKey var6 = (MinecraftKey)var2.recipe(var5).nativeHandle();
            if (method618(var6)) {
               var2.removeRecipe(var5);
            }
         }
      } else {
         var1.replaceNativePacket(new PacketPlayOutRecipes(var3.g(), method617(var3.b()), var4, var3.f()));
      }
   }

   private static List method617(List var0) {
      ArrayList var1 = null;

      for (int var2 = 0; var2 < var0.size(); var2++) {
         MinecraftKey var3 = (MinecraftKey)var0.get(var2);
         if (method618(var3)) {
            if (var1 == null) {
               var1 = new ArrayList(var0.subList(0, var2));
            }
         } else if (var1 != null) {
            var1.add(var3);
         }
      }

      return var1 == null ? var0 : var1;
   }

   private static boolean method618(MinecraftKey var0) {
      return var0.b().startsWith(AbstractRecipeManager.boe);
   }

   private RecipeHolder method619(RecipeHolder var1, AtomicInteger var2) {
      if (!(var1.b() instanceof ShapelessRecipes) && !(var1.b() instanceof ShapedRecipes)) {
         return var1;
      }

      RegistryFriendlyByteBuf var3 = new RegistryFriendlyByteBuf(new PacketDataSerializer(Unpooled.buffer()), IRegistryCustom.b);
      IRecipe var4;
      if (var1.b() instanceof ShapelessRecipes) {
         ShapelessRecipes.i.encode(var3, var1.b());
         var4 = (IRecipe)ShapelessRecipes.i.decode(var3);
      } else {
         ShapedRecipes.i.encode(var3, var1.b());
         var4 = (IRecipe)ShapedRecipes.i.decode(var3);
      }

      for (Object var6 : var4.a()) {
         RecipeItemStack var7 = (RecipeItemStack)var6;

         for (ItemStack var11 : var7.a()) {
            var11.c(DataComponents.i);
            var11.c(DataComponents.n);
            var11.c(DataComponents.b);
            var2.incrementAndGet();
         }
      }

      return new RecipeHolder(var1.a(), var4);
   }

   private static void method620(int var0, AtomicInteger var1, long var2) {
      if (Settings.get().logFixPacketTooLarge) {
         Log.warn("Cleaned " + var0 + " recipes.");
         Log.warn("Cleaned " + var1.get() + " NBT.");
         Log.warn("Time " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - var2) + " ms.");
      }
   }

   @Override
   public void unregister() {
      IaListener.super.unregister();
      this.PV();
      this.bot.clear();
   }

   @Override
   public Object method595(NamespacedKey key) {
      net.minecraft.world.item.crafting.CraftingManager var2 = MinecraftServer.getServer().aJ();
      Optional var3 = var2.a(CraftNamespacedKey.toMinecraft(key));
      return var3.orElse(null);
   }

   @Override
   public Object method596(NamespacedKey namespacedKey) {
      return new MinecraftKey(namespacedKey.toString());
   }

   @Override
   public void method597(Player player, List nmsRecipes) {
      ((CraftPlayer)player).getHandle().a(nmsRecipes);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void method598(Set namespaces) {
      net.minecraft.world.item.crafting.CraftingManager var2 = MinecraftServer.getServer().aJ();
      LinkedHashMultimap var3 = LinkedHashMultimap.create(var2.d);
      boolean var4 = false;

      for (RecipeType var8 : RecipeType.values()) {
         Recipes var9 = switch (SwitchMap57.bov[var8.ordinal()]) {
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
               if (namespaces.contains(var14.a().a())) {
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
            boq.set(var2, var3);
         } catch (IllegalAccessException var15) {
            throw new RuntimeException(var15);
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void PW() {
      for (RecipeType var4 : RecipeType.values()) {
         Recipes var5 = switch (SwitchMap57.bov[var4.ordinal()]) {
            case 1 -> Recipes.a;
            case 2 -> Recipes.b;
            case 3 -> Recipes.c;
            case 4 -> Recipes.d;
            case 5 -> Recipes.e;
            case 6 -> Recipes.f;
            case 7 -> Recipes.g;
            default -> throw new MatchException(null, null);
         };
         net.minecraft.world.item.crafting.CraftingManager var6 = MinecraftServer.getServer().aJ();
         Collection var7 = var6.d.get(var5);
         List var8 = var7.stream().filter(RecipeManagerImplV1_20_R4::method614).toList();
         if (!var8.isEmpty()) {
            LinkedHashMultimap var9 = LinkedHashMultimap.create(var6.d);

            for (RecipeHolder var11 : var8) {
               var9.put(var5, var11);
               LinkedList var12 = new LinkedList(var9.get(var5));
               var12.addFirst(var11);
               var9.replaceValues(var5, var12);
               var6.d = var9;
            }
         }
      }
   }

   private static TileEntityFurnace method621(Furnace var0) {
      CraftFurnace var1 = (CraftFurnace)var0;
      return (TileEntityFurnace)var1.getWorldHandle().c_(var1.getPosition());
   }

   @Override
   public NamespacedKey method600(Furnace furnace) {
      TileEntityFurnace var2 = method621(furnace);
      a var3 = (a)ServerCompat.readField(bor, var2);
      if (bos == null) {
         bos = ServerCompat.findFieldByType(var3.getClass(), MinecraftKey.class);
      }

      assert bos != null;
      if (ServerCompat.readField(bos, var3) instanceof MinecraftKey var4) {
         Optional var6 = MinecraftServer.getServer().aJ().a(var4);
         if (var6.isPresent()) {
            return new NamespacedKey(var4.b(), var4.a());
         }
      }

      return null;
   }

   @Override
   public void method601(Furnace furnace, NamespacedKey bukkitKey) {
      TileEntityFurnace var3 = method621(furnace);
      a var4 = (a)ServerCompat.readField(bor, var3);
      if (bos == null) {
         bos = ServerCompat.findFieldByType(var4.getClass(), MinecraftKey.class);
      }

      assert bos != null;
      RecipeHolder var5 = (RecipeHolder)this.method595(bukkitKey);
      ServerCompat.writeField(bos, var4, var5 == null ? null : var5.a());
   }

   @Override
   public Set method602(Set validNamespaces) {
      net.minecraft.world.item.crafting.CraftingManager var2 = MinecraftServer.getServer().aJ();
      HashSet var3 = new HashSet();

      for (RecipeHolder var5 : var2.d.get(Recipes.a)) {
         if (validNamespaces.contains(var5.a().b())) {
            var3.add(var5.a().toString());
         }
      }

      return var3;
   }

   @Override
   public String method603(CraftingInventory bukkitCraftingInventory) {
      CraftInventoryCrafting var2 = (CraftInventoryCrafting)bukkitCraftingInventory;
      if (var2.getInventory().getOwner() instanceof CraftPlayer var3) {
         net.minecraft.world.item.crafting.CraftingManager var10 = MinecraftServer.getServer().aJ();
         IInventory var5 = var2.getMatrixInventory();
         World var6 = var3.getHandle().dP();

         for (RecipeHolder var8 : var10.d.get(Recipes.a)) {
            IRecipe var9 = var8.b();
            if (!var8.a().b().startsWith(AbstractRecipeManager.boe) && var9.a(var5, var6)) {
               return var8.a().toString();
            }
         }
      }

      return null;
   }

   @Override
   public boolean method604(ShapedRecipe bukkitRecipe, CraftingInventory bukkitCraftingInventory) {
      InventoryCrafting var3 = (InventoryCrafting)((CraftInventoryCrafting)bukkitCraftingInventory).getMatrixInventory();
      ShapedRecipePattern var4 = this.bot.computeIfAbsent(bukkitRecipe.getKey(), var1x -> {
         CraftShapedRecipe var2x = (CraftShapedRecipe)Bukkit.getRecipe(bukkitRecipe.getKey());
         Map var3x = var2x.getChoiceMap();
         String[] var4x = RecipeManagerHolder.method5358(var2x.getShape(), var3x);
         var3x.values().removeIf(Objects::isNull);
         Map var5x = Maps.transformValues(var3x, var1xx -> var2x.toNMS(var1xx, false));
         return ShapedRecipePattern.a(var5x, var4x);
      });

      for (int var5 = 0; var5 <= var3.f() - var4.a(); var5++) {
         for (int var6 = 0; var6 <= var3.g() - var4.b(); var6++) {
            if (this.method622(var3, var5, var6, var4.c(), var4.a(), var4.b(), true)) {
               return true;
            }

            if (this.method622(var3, var5, var6, var4.c(), var4.a(), var4.b(), false)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean method622(InventoryCrafting var1, int var2, int var3, NonNullList var4, int var5, int var6, boolean var7) {
      for (int var8 = 0; var8 < var1.f(); var8++) {
         for (int var9 = 0; var9 < var1.g(); var9++) {
            int var10 = var8 - var2;
            int var11 = var9 - var3;
            RecipeItemStack var12 = RecipeItemStack.a;
            if (var10 >= 0 && var11 >= 0 && var10 < var5 && var11 < var6) {
               if (var7) {
                  var12 = (RecipeItemStack)var4.get(var5 - var10 - 1 + var11 * var5);
               } else {
                  var12 = (RecipeItemStack)var4.get(var10 + var11 * var5);
               }
            }

            ItemStack var13 = var1.a(var8 + var9 * var1.f());
            if (!method623(var12, var13)) {
               return false;
            }
         }
      }

      return true;
   }

   private static boolean method623(RecipeItemStack var0, ItemStack var1) {
      if (var1 == null) {
         return false;
      }

      if (var0.c()) {
         return var1.e();
      }

      ItemStack[] var2 = var0.a();
      ItemStack[] var3 = var2;
      int var4 = var3.length;
      byte var5 = 0;
      if (var5 < var4) {
         ItemStack var6 = var3[var5];
         if (var6.g() != var1.g()) {
            return false;
         } else if (var6.d().d() != var1.d().d()) {
            return false;
         } else {
            return ItemRegistryBridgeV1_20_R4.method3444(var6) && ItemRegistryBridgeV1_20_R4.method3444(var1)
               ? ItemRegistryBridgeV1_20_R4.method3446(var6, var1)
               : ItemStack.c(var6, var1);
         }
      } else {
         return false;
      }
   }

   static {
      if (boq == null) {
         throw new RuntimeException("RecipeManager#byType not found! Try to update the server or downgrade it to a previous build.");
      }

      bor = ServerCompat.findFieldByType(TileEntityFurnace.class, a.class);
   }
}
