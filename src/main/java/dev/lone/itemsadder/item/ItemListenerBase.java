package dev.lone.itemsadder.item;

import dev.lone.itemsadder.event.IaListener;
import java.util.WeakHashMap;
import org.bukkit.entity.Player;

public class ItemListenerBase implements IaListener {
   private WeakHashMap agI = new WeakHashMap();
   int YV = 200;

   public ItemListenerBase() {
   }

   public ItemListenerBase(int var1) {
      this.YV = var1;
   }

   public boolean aY(Player var1) {
      int var2 = Math.max(this.YV, var1.getPing() + this.YV);
      return System.currentTimeMillis() - this.agI.getOrDefault(var1, 0L) < var2;
   }

   public boolean method1262(Player var1, int var2) {
      int var3 = Math.max(var2, var1.getPing() + var2);
      return System.currentTimeMillis() - this.agI.getOrDefault(var1, 0L) < var3;
   }

   public void aZ(Player var1) {
      this.agI.put(var1, System.currentTimeMillis());
   }
}
