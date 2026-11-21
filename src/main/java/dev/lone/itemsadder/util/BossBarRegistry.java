package dev.lone.itemsadder.util;

import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.item.ItemUseSession;
import java.util.HashMap;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class BossBarRegistry {
   public final HashMap sessionsByPlayer = new HashMap();
   public final HashMap barsByPlayer = new HashMap();

   public void clearAll() {
      this.barsByPlayer.clear();
      this.sessionsByPlayer.clear();
   }

   @Nullable
   public BossBar getBar(Player var1) {
      return (BossBar)this.barsByPlayer.get(var1);
   }

   public void removeBar(Player var1) {
      this.barsByPlayer.remove(var1);
   }

   public void showBar(@Nullable CustomItemDefinition var1, Player var2, boolean var3) {
      if (var1 != null) {
         HashMap var4 = (HashMap)this.sessionsByPlayer.get(var2.getUniqueId());
         if (var4 != null) {
            ItemUseSession var5 = (ItemUseSession)var4.get(var1.aol);
            if (var5 != null) {
               var5.method10011(var3, var2);
            }
         }
      }
   }

   public void showBarIndexed(@Nullable CustomItemDefinition var1, Player var2, boolean var3, int var4) {
      if (var1 != null) {
         HashMap var5 = (HashMap)this.sessionsByPlayer.get(var2.getUniqueId());
         if (var5 != null) {
            ItemUseSession var6 = (ItemUseSession)var5.get(var1.aol);
            if (var6 != null) {
               SchedulerUtils.runLaterSync(() -> var6.method10011(var3, var2), var4);
            }
         }
      }
   }
}
