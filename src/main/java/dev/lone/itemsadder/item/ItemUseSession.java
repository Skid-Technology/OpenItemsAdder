package dev.lone.itemsadder.item;

import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespacedElementWithEffects;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class ItemUseSession {
   public final NamespacedElementWithEffects aqi;
   public long aqj;
   public final CustomItemDefinition aqk;
   long aql;
   private boolean aqm;
   private long aqn;
   private int lg;
   public int aqo;

   public ItemUseSession(CustomItemDefinition var1, NamespacedElementWithEffects var2) {
      this.aqi = var2;
      this.aqk = var1;
      this.aql = var1.Dx();
      this.aqm = true;
      this.EJ();
   }

   public long method10007() {
      long var1 = this.aql - this.EF();
      return var1 < 0L ? 0L : var1;
   }

   public long EF() {
      return System.currentTimeMillis() - this.aqj;
   }

   public boolean method10008() {
      return this.EF() >= this.aql;
   }

   public String EG() {
      long var1 = this.method10007();
      this.aqn = TimeUnit.MILLISECONDS.toSeconds(var1);
      return formatDuration(var1);
   }

   static String formatDuration(long var0) {
      long var2 = TimeUnit.MILLISECONDS.toSeconds(Math.max(0L, var0));
      long var4 = TimeUnit.SECONDS.toHours(var2);
      long var6 = TimeUnit.SECONDS.toMinutes(var2) % 60L;
      return var4 > 0L ? String.format(Locale.ROOT, "%d:%02d:%02d", var4, var6, var2 % 60L) : String.format(Locale.ROOT, "%02d:%02d", var6, var2 % 60L);
   }

   public boolean EH() {
      return TimeUnit.MILLISECONDS.toSeconds(this.aql - this.EF()) != this.aqn;
   }

   public boolean EI() {
      return this.lg++ == 0;
   }

   public void method10010(float var1, Player var2) {
      this.aqi.method499(var1);
      ContentManager.method8084().aH.az(var2);
   }

   public void EJ() {
      this.aqj = System.currentTimeMillis();
   }

   public void method10011(boolean var1, Player var2) {
      this.aqm = var1;
      if (!var1 || !this.method10008()) {
         if (this.aqk.DM() == HudDisplayMode.HUD) {
            NamespacedElementWithEffects var3 = ContentManager.method8084().aH.method878(var2, this.aqk.DL().title);
            if (var3 == null) {
               return;
            }

            if (var1 && var3.zP() == 0.0F) {
               return;
            }

            var3.setVisible(var1);
            ContentManager.method8084().aH.az(var2);
         } else if (this.aqk.DM() == HudDisplayMode.TITLE) {
            if (!var1) {
               var2.sendTitle(" ", " ", 0, 0, 0);
            }
         } else {
            BossBar var4 = ContentManager.method8084().aK.aiN.getBar(var2);
            if (var4 != null) {
               if (var1 && var4.getProgress() == 0.0) {
                  return;
               }

               if (var1) {
                  var4.show();
               } else {
                  ContentManager.method8084().aK.aiN.removeBar(var2);
                  var4.hide();
               }
            }
         }
      }
   }

   public boolean EK() {
      return this.aqm;
   }

   public void EL() {
      this.aqo++;
   }

   public void EM() {
      this.aqo = 0;
   }
}
