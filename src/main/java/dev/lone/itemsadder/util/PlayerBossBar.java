package dev.lone.itemsadder.util;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class PlayerBossBar {
   final Player player;
   final BossBar bossBar;
   long shownAtMillis;

   public PlayerBossBar(Player var1, String var2, BarColor var3, BarStyle var4) {
      this.player = var1;
      this.bossBar = Bukkit.createBossBar(var2, var3, var4, new BarFlag[0]);
      this.bossBar.addPlayer(var1);
      this.bossBar.setVisible(false);
   }

   public void show() {
      this.bossBar.setVisible(true);
      this.shownAtMillis = System.currentTimeMillis();
   }

   public void hide() {
      this.bossBar.setVisible(false);
   }

   public void scheduleAutoHide() {
      SchedulerUtils.runLaterAsync(() -> {
         if (System.currentTimeMillis() - this.shownAtMillis > 2500L) {
            this.hide();
         }
      }, 60L);
   }

   public void setTitle(String var1) {
      this.bossBar.setTitle(var1);
   }

   public void removeViewer() {
      this.bossBar.removePlayer(this.player);
   }
}
