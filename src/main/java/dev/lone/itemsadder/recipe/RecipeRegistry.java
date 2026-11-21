package dev.lone.itemsadder.recipe;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.Log;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecipeRegistry {
   public static HashMap aBs = new LinkedHashMap();
   private final LinkedHashMap aBt;
   private final HashMap aBu;
   private final HashMap aBv;
   private final HashMap aBw;
   private final HashMap aBx;
   private final HashMap aBy;
   private final HashMap aBz;
   private final HashMap aBA;
   private final LinkedList aBB;
   private final LinkedList aBC;
   private final LinkedHashMap aBD = new LinkedHashMap();

   public RecipeRegistry() {
      this.aBt = new LinkedHashMap();
      this.aBu = new HashMap();
      this.aBv = new HashMap();
      this.aBw = new HashMap();
      this.aBx = new HashMap();
      this.aBA = new HashMap();
      this.aBB = new LinkedList();
      this.aBC = new LinkedList();
      this.aBy = new HashMap();
      this.aBz = new HashMap();
   }

   public void JX() {
      boolean var1 = false;
      Iterator var2 = aBs.entrySet().iterator();

      while (var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         NamespacedKey var4 = (NamespacedKey)var3.getKey();
         if (var4.getNamespace().startsWith(AbstractRecipeManager.boe)) {
            NamespacedKey var5 = new NamespacedKey(var4.getNamespace().substring(AbstractRecipeManager.boe.length()), var4.getKey());
            if (ContentManager.method8084().bp.dV(var5.toString()) == null) {
               RecipeManagerHolder.Qb().method651(var5);
               var2.remove();
               Log.warn("---- Removed fake unused/disabled/failed to load recipe: " + var4);
               var1 = true;
            }
         } else if (ContentManager.method8084().bp.dV(var4.toString()) == null) {
            RecipeManagerHolder.Qb().method651(var4);
            var2.remove();
            Log.warn("---- Removed unused/disabled/failed to load recipe: " + var4);
            var1 = true;
         }
      }

      if (var1) {
         RecipeManagerHolder.Qb().PY();
      }
   }

   public void method2221(boolean var1) {
      if (!var1 && !aBs.isEmpty()) {
         aBs.forEach((var0, var1x) -> RecipeManagerHolder.Qb().method651(var0));
         aBs.clear();
         RecipeManagerHolder.Qb().PY();
      }

      this.aBt.clear();
      this.aBu.clear();
      this.aBv.clear();
      this.aBw.clear();
      this.aBA.clear();
      this.aBB.clear();
      this.aBC.clear();
      this.aBy.clear();
      this.aBz.clear();
   }

   public void method2222(AbstractCustomRecipe var1) {
      this.aBt.put(var1.aBn.getNamespace() + ":" + var1.aBn.getKey(), var1);
      this.aBB.add(var1.api);
      this.aBC.add(RecipeManagerHolder.Qb().method596(var1.aBn));
      CustomItemDefinition var2 = var1.JS();
      if (var2 != null) {
         this.aBA.computeIfAbsent(var1.getClass(), var0 -> new HashMap<>()).computeIfAbsent(var2.aol, var0 -> new LinkedList<>()).add(var1);
         this.aBw.computeIfAbsent(var1.getClass(), var0 -> new HashMap<>()).computeIfAbsent(var2.aol, var0 -> new LinkedList<>()).add(var1);
      }
   }

   public void method2223(CookingRecipe var1) {
      CustomItemDefinition var2 = ContentManager.method8084().method8149(var1.Kg().getItemStack());
      if (var2 != null) {
         this.aBz.computeIfAbsent(var2.aol, var0 -> new LinkedList<>()).add(var1);
         this.aBv.computeIfAbsent(var2.aol, var0 -> new LinkedList<>()).add(var1.aBn);
         this.aBx.computeIfAbsent(var1.getClass(), var0 -> new HashMap<>()).computeIfAbsent(var2.aol, var0 -> new LinkedList<>()).add(var1);
         this.aBw.computeIfAbsent(var1.getClass(), var0 -> new HashMap<>()).computeIfAbsent(var2.aol, var0 -> new LinkedList<>()).add(var1);
         this.aBu.computeIfAbsent(var2.aol, var0 -> new LinkedList<>()).add(var1.aBn);
      }

      CustomItemDefinition var3 = var1.JS();
      if (var3 != null) {
         this.aBu.computeIfAbsent(var3.aol, var0 -> new LinkedList<>()).add(var1.aBn);
         this.aBw.computeIfAbsent(var1.getClass(), var0 -> new HashMap<>()).computeIfAbsent(var3.aol, var0 -> new LinkedList<>()).add(var1);
      }
   }

   public void method2224(CraftingRecipe var1, ShapedIngredient var2) {
      if (var2.Eb()) {
         Integer var3 = var2.Kl();
         this.aBu.computeIfAbsent(var3, var0 -> new LinkedList<>()).add(var1.aBn);
         this.aBy.computeIfAbsent(var3, var0 -> new LinkedList<>()).add(var1);
         this.aBv.computeIfAbsent(var3, var0 -> new LinkedList<>()).add(var1.aBn);
         this.aBz.computeIfAbsent(var3, var0 -> new LinkedList<>()).add(var1);
         this.aBx.computeIfAbsent(var1.getClass(), var0 -> new HashMap<>()).computeIfAbsent(var3, var0 -> new LinkedList<>()).add(var1);
         this.aBw.computeIfAbsent(var1.getClass(), var0 -> new HashMap<>()).computeIfAbsent(var3, var0 -> new LinkedList<>()).add(var1);
      }
   }

   public void method2225(CraftingRecipe var1) {
      if (var1.JS() != null) {
         this.aBu.computeIfAbsent(var1.JS().aol, var0 -> new LinkedList<>()).add(var1.aBn);
      }
   }

   public void method2226(SmithingRecipe var1) {
      this.method2227(var1, var1.aCx);
      this.method2227(var1, var1.aCy);
      CustomItemDefinition var2 = var1.JS();
      if (var2 != null) {
         this.aBu.computeIfAbsent(var2.aol, var0 -> new LinkedList<>()).add(var1.aBn);
         this.aBw.computeIfAbsent(var1.getClass(), var0 -> new HashMap<>()).computeIfAbsent(var2.aol, var0 -> new LinkedList<>()).add(var1);
      }
   }

   public void method2227(SmithingRecipe var1, SmithingIngredient var2) {
      CustomItemDefinition var3 = ContentManager.method8084().method8149(var2.getItem());
      if (var3 != null) {
         this.aBz.computeIfAbsent(var3.aol, var0 -> new LinkedList<>()).add(var1);
         this.aBv.computeIfAbsent(var3.aol, var0 -> new LinkedList<>()).add(var1.aBn);
         this.aBx.computeIfAbsent(var1.getClass(), var0 -> new HashMap<>()).computeIfAbsent(var3.aol, var0 -> new LinkedList<>()).add(var1);
         this.aBw.computeIfAbsent(var1.getClass(), var0 -> new HashMap<>()).computeIfAbsent(var3.aol, var0 -> new LinkedList<>()).add(var1);
         this.aBu.computeIfAbsent(var3.aol, var0 -> new LinkedList<>()).add(var1.aBn);
      }
   }

   public ItemStackCraftingRecipe method2228(CustomItemDefinition var1) {
      ItemStackCraftingRecipe var2 = (ItemStackCraftingRecipe)this.aBD.get(var1);
      if (var2 != null) {
         return var2;
      }

      var2 = new ItemStackCraftingRecipe(var1);
      this.aBD.put(var1, var2);
      return var2;
   }

   public boolean dU(String var1) {
      return this.aBt.containsKey(var1);
   }

   public LinkedList method2229(Class var1, CustomItemDefinition var2) {
      return this.method2232(var1, var2, (HashMap)this.aBA.get(var1));
   }

   public LinkedList method2230(Class var1, CustomItemDefinition var2) {
      return this.method2232(var1, var2, (HashMap)this.aBw.get(var1));
   }

   public LinkedList method2231(Class var1, CustomItemDefinition var2) {
      return this.method2232(var1, var2, (HashMap)this.aBx.get(var1));
   }

   @NotNull
   private LinkedList method2232(Class var1, CustomItemDefinition var2, HashMap var3) {
      if (var3 == null) {
         return new LinkedList();
      }

      LinkedList var4 = (LinkedList)var3.get(var2.aol);
      return var4 == null ? new LinkedList() : var4;
   }

   public LinkedList method2233(CustomItemDefinition var1) {
      return (LinkedList)this.aBu.get(var1.aol);
   }

   @Nullable
   public AbstractCustomRecipe dV(String var1) {
      return (AbstractCustomRecipe)this.aBt.get(var1);
   }

   public LinkedHashMap JY() {
      return this.aBt;
   }

   @Nullable
   public LinkedList JZ() {
      return this.aBC;
   }

   @Nullable
   public LinkedList Ka() {
      return this.aBB;
   }

   @Nullable
   public LinkedList method2234(CustomItemDefinition var1) {
      return (LinkedList)this.aBy.get(var1.aol);
   }

   @Nullable
   public LinkedList method2235(CustomItemDefinition var1) {
      return (LinkedList)this.aBz.get(var1.aol);
   }

   public static void method2236(Recipe var0) {
      NamespacedKey var1 = ((Keyed)var0).getKey();
      if (!aBs.isEmpty()) {
         Keyed var2 = (Keyed)aBs.get(var1);
         if (var2 != null) {
            if (var0 instanceof org.bukkit.inventory.CraftingRecipe) {
               if (var2 instanceof org.bukkit.inventory.CraftingRecipe var4
                  && var4.getResult().isSimilar(var0.getResult())
                  && var4.getCategory().equals(((org.bukkit.inventory.CraftingRecipe)var0).getCategory())) {
                  if (var2 instanceof ShapedRecipe var6) {
                     if (Arrays.equals(var6.getShape(), ((ShapedRecipe)var0).getShape()) && var6.getChoiceMap().equals(((ShapedRecipe)var0).getChoiceMap())) {
                        return;
                     }
                  } else {
                     if (!(var2 instanceof ShapelessRecipe var5)) {
                        throw new IllegalStateException("Recipe type not supported: " + var2.getClass());
                     }

                     if (var5.getChoiceList().equals(((ShapelessRecipe)var0).getChoiceList())) {
                        return;
                     }
                  }
               }
            } else if (var0 instanceof org.bukkit.inventory.CookingRecipe var3
               && var2 instanceof org.bukkit.inventory.CookingRecipe var12
               && var12.getResult().isSimilar(var3.getResult())
               && var12.getCookingTime() == var3.getCookingTime()
               && var12.getExperience() == var3.getExperience()
               && var12.getInputChoice().getItemStack().isSimilar(var3.getInputChoice().getItemStack())) {
               return;
            }
         }
      }

      try {
         RecipeManagerHolder.Qb().method648(var0);
      } catch (IllegalStateException var10) {
         RecipeManagerHolder.Qb().method651(var1);
         RecipeManagerHolder.Qb().method648(var0);
      } finally {
         aBs.put(var1, (Keyed)var0);
      }
   }

   public Set Kb() {
      return this.aBt.values().stream().map(var0 -> var0.aBn.getNamespace()).collect(Collectors.toSet());
   }
}
