package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.api.PlayerActionEvent;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemEventType;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.util.IntTableConstantsO;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

class EquipmentSwapTask {
   public final Player alQ;
   public final ItemEventType alR;
   public final ItemEventType alS;
   @Nullable
   public final CustomItemDefinition alT;
   @Nullable
   public final ItemStack alU;
   @Nullable
   public final CustomItemDefinition alV;
   @Nullable
   public final ItemStack alW;

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public EquipmentSwapTask(EquipmentSlot var1, Player var2, @Nullable CustomItemDefinition var3, @Nullable CustomItemDefinition var4) {
      if (ItemStackUtils.method7264(var1)) {
         this.alR = ItemEventType.WEAR;
         this.alS = ItemEventType.UNWEAR;
      } else {
         switch (IntTableConstantsO.SWITCH_MAP[var1.ordinal()]) {
            case 1:
               this.alR = ItemEventType.HELD_OFFHAND;
               this.alS = ItemEventType.UNHELD_OFFHAND;
               break;
            default:
               this.alR = ItemEventType.HELD;
               this.alS = ItemEventType.UNHELD;
         }
      }

      this.alQ = var2;
      this.alT = var3;
      this.alU = var3 != null ? var3.Ds() : null;
      this.alV = var4;
      this.alW = var4 != null ? var4.Ds() : null;
   }

   public void run() {
      this.CL();
      this.CM();
   }

   void CL() {
      if (this.alT != null) {
         this.alT.method8761(this.alQ, this.alS, var1 -> var1.applyOnAction(new PlayerActionEvent(this.alQ), this.alT, this.alU));
      }
   }

   void CM() {
      if (this.alV != null) {
         this.alV.method8761(this.alQ, this.alR, var1 -> var1.applyOnAction(new PlayerActionEvent(this.alQ), this.alV, this.alW));
      }
   }
}
