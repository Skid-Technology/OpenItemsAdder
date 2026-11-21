package dev.lone.itemsadder.particle;

import dev.lone.itemsadder.entity.CustomEntityDefinition;
import dev.lone.itemsadder.entity.TrackedDisplayEntity;
import dev.lone.itemsadder.util.Log;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class EntityParticleEmitter extends ParticleEmitterBase {
   public CustomEntityDefinition Dt;
   public Particle Du;
   public final String Dv;
   public final int Dw;
   public final boolean Dx;
   public float Dy;
   public int Dz;
   float DA;
   float DB;
   float DC;

   public EntityParticleEmitter(CustomEntityDefinition var1, String var2, float var3, int var4, float var5, float var6, float var7) {
      this(var1, var2, var3, var4, var5, var6, var7, 60);
   }

   public EntityParticleEmitter(CustomEntityDefinition var1, String var2, float var3, int var4, float var5, float var6, float var7, int var8) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, false);
   }

   public EntityParticleEmitter(CustomEntityDefinition var1, String var2, float var3, int var4, float var5, float var6, float var7, int var8, boolean var9) {
      this.Dt = var1;
      this.Dv = ParticleEmitterManager.dP(var2) ? var2 : null;
      this.Dw = this.Dv == null ? 60 : ParticleEmitterManager.method1713(var8);
      if (var9 && this.Dv == null) {
         throw new IllegalArgumentException("follow_locator requires a custom particle effect");
      }

      this.Dx = var9;
      this.Du = this.Dv == null ? ParticleResolverHolder.PL().fh(var2) : null;
      if (this.Du == null && this.Dv == null) {
         Log.error("Unknown particle '" + var2 + "' for locator " + var1.getNamespacedID());
      }

      this.Dy = var3;
      this.Dz = var4;
      this.DA = var5;
      this.DB = var6;
      this.DC = var7;
   }

   @Override
   public void method8878(Location var1) {
      if (this.Dv != null) {
         new ParticleEmitterManager().method1714(this.Dv, var1, null, this.Dw);
      } else if (this.Du != null) {
         for (Player var3 : var1.getWorld().getPlayers()) {
            if (var1.distance(var3.getLocation()) < 32.0) {
               this.method8884(var3, var1.getX(), var1.getY(), var1.getZ());
            }
         }
      }
   }

   public void method8879(Location var1, double var2, double var4, double var6) {
      if (this.Dv != null) {
         new ParticleEmitterManager().method1714(this.Dv, new Location(var1.getWorld(), var2, var4, var6), null, this.Dw);
      } else if (this.Du != null) {
         for (Player var9 : var1.getWorld().getPlayers()) {
            if (var1.distance(var9.getLocation()) < 32.0) {
               this.method8884(var9, var2, var4, var6);
            }
         }
      }
   }

   public void method8880(Player var1, double var2, double var4, double var6) {
      if (this.Dv != null) {
         new ParticleEmitterManager().method1714(this.Dv, new Location(var1.getWorld(), var2, var4, var6), List.of(var1.getUniqueId()), this.Dw);
      } else if (this.Du != null) {
         this.method8884(var1, var2, var4, var6);
      }
   }

   public void method8881(ParticleEmitterManager var1, Location var2, List var3, List var4) {
      this.method8882(var1, var2, var3, var4, null);
   }

   public void method8882(ParticleEmitterManager var1, Location var2, List var3, List var4, TrackedDisplayEntity var5) {
      this.method8883(var1, var2, null, var3, var4, var5);
   }

   public void method8883(ParticleEmitterManager var1, Location var2, UUID var3, List var4, List var5, TrackedDisplayEntity var6) {
      if (this.Dv != null) {
         List var7 = var4.stream().map(OfflinePlayer::getUniqueId).toList();
         if (this.Dx) {
            if (var6 == null) {
               throw new IllegalArgumentException("Missing following particle anchor");
            }

            var5.add(() -> var1.method1715(this.Dv, var6, var7, this.Dw));
         } else {
            Location var8 = var2.clone();
            var5.add(() -> {
               if (var3 != null) {
                  var8.setWorld(Bukkit.getWorld(var3));
               }

               if (var8.getWorld() != null) {
                  var1.method1714(this.Dv, var8, var7, this.Dw);
               }
            });
         }
      } else {
         for (Player var10 : var4) {
            var5.add(() -> this.method8880(var10, var2.getX(), var2.getY(), var2.getZ()));
         }
      }
   }

   private void method8884(Player var1, double var2, double var4, double var6) {
      Object var8 = method8885(this.Du.getDataType());
      if (var8 != null) {
         var1.spawnParticle(this.Du, var2, var4, var6, this.Dz, this.DA, this.DB, this.DC, this.Dy, var8);
      } else {
         var1.spawnParticle(this.Du, var2, var4, var6, this.Dz, this.DA, this.DB, this.DC, this.Dy);
      }
   }

   private static Object method8885(Class var0) {
      return var0 == Color.class ? Color.WHITE : null;
   }

   static {
      assert method8885(Color.class) == Color.WHITE && method8885(Void.class) == null;
   }
}
