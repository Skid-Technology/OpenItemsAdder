package dev.lone.itemsadder.core;

import dev.lone.itemsadder.command.PermissionNodes;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.lang.TextFormatCache;
import dev.lone.itemsadder.text.NmsChatService;
import dev.lone.itemsadder.util.AdventureText;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PlayerEffectState {
   public final Player acI;
   public final List acJ = new CopyOnWriteArrayList();
   public final List acK = new CopyOnWriteArrayList();
   public final HashMap acL = new HashMap();
   public final HashMap acM = new HashMap();
   public final HashMap acN = new HashMap();
   private String acO;
   private Object acP;
   private long acQ;

   public PlayerEffectState(Player var1) {
      this.acI = var1;
   }

   public NamespacedElement bW(String var1) {
      return (NamespacedElement)this.acL.get(var1);
   }

   void method9644(NamespacedElement var1) {
      this.acJ.add(var1);
      this.acL.put(var1.getNamespacedID(), var1);
      if (var1 instanceof NamespacedElementWithEffects var2) {
         this.acK.add(var2);
         this.acM.put(var1.getNamespacedID(), var2);
         this.acN.put(var2.abF, var2);
      }
   }

   public void recalculateOffsets() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.acJ.size(); var2++) {
         NamespacedElement var3 = (NamespacedElement)this.acJ.get(var2);
         var3.bo(var1);
         if (var3.zE()) {
            var1 -= var3.zC();
         }
      }
   }

   @Nullable
   public String Al() {
      StringBuilder var1 = new StringBuilder();
      int var2 = 0;

      for (NamespacedElement var4 : this.acJ) {
         if (var4.zE()) {
            var1.append(var4.zD());
            var2++;
         }
      }

      if (var2 == 0) {
         return null;
      }

      var1 = new StringBuilder(TextFormatCache.method9634(var1.toString(), this.Am()));
      return var1.toString();
   }

   public int Am() {
      float var1 = 0.0F;

      for (NamespacedElement var3 : this.acJ) {
         var1 += var3.zC();
      }

      return (int)var1 / 2;
   }

   public void An() {
      String var1 = this.Al();
      if (var1 != null) {
         if (var1.equals(this.acO)) {
            if (System.currentTimeMillis() - this.acQ >= 1200L) {
               NmsChatService.method2952(this.acI, this.acP);
               this.acQ = System.currentTimeMillis();
            }
         } else {
            this.acO = var1;
            Integer var2 = Settings.get().hudForceColorRgb;
            Object var3 = ContentManager.method8084().aE.bD(var1);
            if (var2 != null) {
               var3 = var3.color(TextColor.color(var2));
            }

            this.acP = AdventureText.toNative((Component)var3);
            NmsChatService.method2952(this.acI, this.acP);
            this.acQ = System.currentTimeMillis();
         }
      }
   }

   public void Ao() {
      NmsChatService.cl(this.acI);
   }

   public void Ap() {
      for (NamespacedElement var2 : this.acJ) {
         if (var2.abs || var2.isVisible()) {
            var2.setVisible(var2.method492(this.acI.getWorld()));
         }
      }
   }

   public void Aq() {
      for (NamespacedElement var2 : this.acJ) {
         var2.aE(PermissionNodes.method1616(this.acI, var2));
      }
   }

   public NamespacedElementWithEffects bX(String var1) {
      for (NamespacedElementWithEffects var3 : this.acK) {
         if (var3.zQ().equals(var1)) {
            return var3;
         }
      }

      return null;
   }
}
