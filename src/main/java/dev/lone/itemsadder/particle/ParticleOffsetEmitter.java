package dev.lone.itemsadder.particle;

import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class ParticleOffsetEmitter extends ParticleEmitterBase {
   public String yK;
   public float zR;
   public float zS;

   public ParticleOffsetEmitter(String var1, float var2, float var3) {
      this.yK = var1;
      this.zR = var2;
      this.zS = var3;
   }

   @Override
   public void method8878(Location var1) {
      for (Player var3 : var1.getWorld().getPlayers()) {
         if (var1.distance(var3.getLocation()) < 64.0) {
            var3.playSound(var1, this.yK, SoundCategory.MASTER, this.zR, this.zS);
         }
      }
   }

   public void method8889(Location var1) {
      for (Player var3 : var1.getWorld().getPlayers()) {
         if (var1.distance(var3.getLocation()) < 64.0) {
            var3.stopSound(this.yK, SoundCategory.MASTER);
         }
      }
   }
}
