package dev.lone.itemsadder.anim;

import dev.lone.itemsadder.Main;
import java.util.HashMap;
import java.util.Map.Entry;
import org.bukkit.Bukkit;

public class AnimationTicker {
   public static HashMap blO = new HashMap();
   public int blP = 1;
   float blQ = 1.0F;

   public static void method5252(String var0) {
      AnimationTicker var1 = blO.computeIfAbsent(var0, var0x -> new AnimationTicker());
      var1.blP++;
   }

   static void tick() {
      System.out.println("--------");
      System.out.println("Avg packets sent last 1 second.");
      float var0 = 0.0F;

      for (Entry var2 : blO.entrySet()) {
         System.out.println((String)var2.getKey() + " | " + ((AnimationTicker)var2.getValue()).PF());
         var0 += ((AnimationTicker)var2.getValue()).PF();
         ((AnimationTicker)var2.getValue()).blP = 0;
      }

      System.out.println("# Total AVG: " + var0);
      System.out.println("--------");
   }

   static float method5253(float var0, float var1) {
      return var0 + 0.75F * (var1 - var0);
   }

   public float PF() {
      this.blQ = method5253(this.blP, this.blQ);
      return this.blQ;
   }

   static {
      Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), AnimationTicker::tick, 20L, 20L);
   }
}
