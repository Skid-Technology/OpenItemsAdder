package dev.lone.itemsadder.item;

import beer.devs.fastnbt.nms.nbt.NItem;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.util.RateLimiter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class ThrowTask {
   public ItemThrowAction agb;
   public final RateLimiter agc = new RateLimiter();
   public Entity agd;
   public ArmorStand age;
   public int sK = Integer.MIN_VALUE;
   public NItem agf;
   public Long agg;
   public double agh;
   public Vector aeS;
   public Location agi;
   double agj;
   BukkitTask mS;

   public ThrowTask(ItemThrowAction var1, ArmorStand var2, Entity var3, NItem var4, Long var5) {
      this.agb = var1;
      this.age = var2;
      this.agd = var3;
      this.agf = var4;
      this.agg = var5;
      this.aeS = var1.AR().clone();
      this.agh = var2.getLocation().getYaw();
      this.agi = var2.getLocation().clone();
   }

   public void method9650(Location var1) {
      if (!(Math.abs(this.age.getVelocity().getX()) > 0.35F)
         && !(Math.abs(this.age.getVelocity().getY()) > 0.35F)
         && !(Math.abs(this.age.getVelocity().getZ()) > 0.35F)) {
         this.agj = Math.toRadians(var1.getYaw());
         this.agi.setX(var1.getX() + this.aeS.getX() * Math.cos(this.agj) + this.aeS.getZ() * -Math.sin(this.agj));
         this.agi.setY(var1.getY() + this.aeS.getY());
         this.agi.setZ(var1.getZ() + this.aeS.getX() * Math.sin(this.agj) + this.aeS.getZ() * Math.cos(this.agj));
         var1.getWorld().spawnParticle(Particle.SMOKE, this.agi.getX(), this.agi.getY(), this.agi.getZ(), this.agb.AS(), 0.0, 0.0, 0.0, 0.0);
      }
   }

   public void Br() {
      if (this.agb.AS() > 0) {
         this.mS = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            if (this.age.getPassenger() == null) {
               this.Bs();
            } else {
               this.method9650(this.age.getLocation());
            }
         }, 5L, 10L);
      }
   }

   public void Bs() {
      if (this.agb.AS() > 0) {
         if (this.mS != null) {
            this.mS.cancel();
         }
      }
   }
}
