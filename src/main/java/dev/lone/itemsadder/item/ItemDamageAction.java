package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ItemDamageAction extends ItemActionBase {
   public static final String aed = "furniture_consumable";
   public final int aee;
   public final float aef;
   public final List aeg;

   public ItemDamageAction(CustomItemDefinition var1, int var2, float var3, List var4, boolean var5) {
      super(var1);
      this.aee = var2;
      this.aef = var3;
      this.aeg = var4;
      this.aeg.addFirst(var1);
      if (var5) {
         for (CustomItemDefinition var7 : var4) {
            var7.aoh.addAction("furniture_consumable", this);
         }
      }
   }

   public int method337(CustomItemDefinition var1) {
      for (int var2 = 0; var2 < this.aeg.size(); var2++) {
         if (((CustomItemDefinition)this.aeg.get(var2)).getNamespacedID().equals(var1.getNamespacedID())) {
            return var2;
         }
      }

      return -1;
   }

   public boolean method338(CustomItemDefinition var1) {
      int var2 = this.method337(var1);
      return var2 < 0 || var2 >= this.aeg.size() - 1;
   }

   public void method339(Player var1, Entity var2, ItemDamageAction var3, CustomItemDefinition var4) {
      if (this.method338(var4)) {
         this.method340(var2, var1);
      } else {
         int var5 = this.method337(var4);
         CustomItemDefinition var6 = (CustomItemDefinition)this.aeg.get(var5 + 1);
         ItemDamageAction var7 = (ItemDamageAction)var6.aoh.getAction("furniture_consumable");
         if (var7 == null) {
            Log.error(
               "Item '"
                  + var6.getNamespacedID()
                  + "' has no 'furniture_consumable' behaviour, but is referenced in the item '"
                  + var4.getNamespacedID()
                  + "' consumable stages list."
            );
            this.method340(var2, var1);
         } else {
            boolean var8 = FurnitureItemEntity.method401(var6, var3.aeT, var2);
            if (!var8) {
               this.method340(var2, var1);
            } else {
               this.aH(var1);
            }
         }
      }
   }

   private void aH(Player var1) {
      var1.getFoodLevel();
      var1.setFoodLevel(Math.min(20, var1.getFoodLevel() + this.aee));
      var1.setSaturation(Math.min(20.0F, var1.getSaturation() + this.aef));
   }

   private void method340(Entity var1, Player var2) {
      ContentManager.method8084().aV.method564(var1, var2, false, false, false);
      this.aH(var2);
   }
}
