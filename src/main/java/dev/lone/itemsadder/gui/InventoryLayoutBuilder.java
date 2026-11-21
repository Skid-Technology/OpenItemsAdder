package dev.lone.itemsadder.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import org.bukkit.inventory.ItemStack;

public class InventoryLayoutBuilder {
   private final List bQh = new ArrayList();
   private final Map bQi = new HashMap();
   private final Map bQj = new HashMap();
   private char bQk;

   public InventoryLayoutBuilder mn(String var1) {
      Objects.requireNonNull(var1);
      this.bQh.add(var1.length() > 9 ? var1.substring(0, 10) : var1);
      return this;
   }

   public InventoryLayoutBuilder method7645(String... var1) {
      for (String var5 : Objects.requireNonNull(var1)) {
         this.mn(var5);
      }

      return this;
   }

   public InventoryLayoutBuilder method7646(char var1, ItemStack var2, Consumer var3) {
      this.bQi.put(var1, Objects.requireNonNull(var2));
      if (var3 != null) {
         this.bQj.put(var1, var3);
      }

      return this;
   }

   public InventoryLayoutBuilder method7647(char var1, ItemStack var2) {
      return this.method7646(var1, var2, null);
   }

   public InventoryLayoutBuilder method7648(char var1) {
      this.bQk = var1;
      return this;
   }

   public InventoryLayoutBuilder method7649(char var1) {
      this.bQi.remove(var1);
      this.bQj.remove(var1);
      return this;
   }

   public void method7650(CustomInventoryHolder var1) {
      ArrayList var2 = new ArrayList();

      for (int var3 = 0; var3 < this.bQh.size(); var3++) {
         String var4 = (String)this.bQh.get(var3);

         for (int var5 = 0; var5 < var4.length(); var5++) {
            char var6 = var4.charAt(var5);
            if (var6 == this.bQk) {
               var2.add(9 * var3 + var5);
            } else {
               ItemStack var7 = (ItemStack)this.bQi.get(var6);
               Consumer var8 = (Consumer)this.bQj.get(var6);
               if (var7 != null) {
                  var1.method2666(9 * var3 + var5, var7, var8);
               }
            }
         }
      }

      if (var1 instanceof PaginatedInventoryHolder && !var2.isEmpty()) {
         ((PaginatedInventoryHolder)var1).ay(var2);
      }
   }
}
