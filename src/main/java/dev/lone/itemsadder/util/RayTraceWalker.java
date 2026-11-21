package dev.lone.itemsadder.util;

import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class RayTraceWalker {
   private ArrayList passableMaterials;
   private ArrayList traversedLocations;
   private Player player;
   private Location origin;
   private double maxDistance;
   private RayTraceTarget hitTarget;
   private Entity hitEntity;
   private Block hitBlock;
   private Location hitLocation;
   private boolean forceStepping;
   private boolean showParticles;
   private Entity ignoredEntity;
   private Location currentPoint;
   private boolean nativeTraceEnabled;
   private FluidCollisionMode fluidMode = FluidCollisionMode.NEVER;
   private double entityRaySize = 0.0;

   public RayTraceWalker(Location var1, double var2) {
      this.passableMaterials = new ArrayList();
      this.traversedLocations = new ArrayList();
      this.showParticles = false;
      this.addPassableMaterial(Material.AIR);
      this.addPassableMaterial(Material.CAVE_AIR);
      this.addPassableMaterial(Material.VOID_AIR);
      this.nativeTraceEnabled = true;
      this.origin = var1;
      this.maxDistance = var2;
   }

   public RayTraceWalker(Player var1, double var2) {
      this(var1, var2, 0.0);
   }

   public RayTraceWalker(Player var1, double var2, double var4) {
      this(var1.getEyeLocation(), var2);
      this.player = var1;
      this.ignoredEntity = var1;
      this.entityRaySize = var4;
   }

   public static RayTraceWalker forPlayer(Player var0, double var1, double var3) {
      return new RayTraceWalker(var0, var1, var3);
   }

   public void setFluidMode(FluidCollisionMode var1) {
      this.fluidMode = var1;
   }

   private RayTraceResult traceBlocks() {
      return this.origin.getWorld().rayTraceBlocks(this.origin, this.origin.getDirection(), this.maxDistance, this.fluidMode, true);
   }

   private RayTraceResult traceEntities() {
      return this.player == null
         ? this.origin.getWorld().rayTraceEntities(this.origin, this.origin.getDirection(), this.maxDistance)
         : this.origin
            .getWorld()
            .rayTraceEntities(
               this.player.getEyeLocation(),
               this.player.getEyeLocation().getDirection(),
               this.maxDistance,
               this.entityRaySize,
               var1 -> this.player != null && var1.getEntityId() != this.player.getEntityId()
            );
   }

   private RayTraceResult traceAll() {
      return this.player == null
         ? this.origin
            .getWorld()
            .rayTrace(
               this.origin,
               this.origin.getDirection(),
               this.maxDistance,
               this.fluidMode,
               true,
               this.entityRaySize,
               var1 -> this.player != null && var1.getEntityId() != this.player.getEntityId()
            )
         : this.origin
            .getWorld()
            .rayTrace(
               this.player.getEyeLocation(),
               this.player.getEyeLocation().getDirection(),
               this.maxDistance,
               this.fluidMode,
               true,
               this.entityRaySize,
               var1 -> this.player != null && var1.getEntityId() != this.player.getEntityId()
            );
   }

   public boolean matchesTarget(RayTraceTarget var1) {
      this.traversedLocations.clear();
      if (!this.forceStepping && this.nativeTraceEnabled) {
         RayTraceResult var6 = null;
         switch (var1) {
            case ENTITY_AND_BLOCK:
               var6 = this.traceAll();
               break;
            case ENTITY:
               var6 = this.traceEntities();
               break;
            case BLOCK:
               var6 = this.traceBlocks();
         }

         if (var6 != null) {
            this.hitBlock = null;
            this.hitEntity = var6.getHitEntity();
            this.hitBlock = var6.getHitBlock();
            this.hitLocation = new Location(this.origin.getWorld(), var6.getHitPosition().getX(), var6.getHitPosition().getY(), var6.getHitPosition().getZ());
            return true;
         }
      } else {
         int var2 = 0;
         this.advancePoint(new Vector(0.0, 0.0, var2 + 50.0));
         if (var1 == RayTraceTarget.BLOCK) {
            this.hitTarget = RayTraceTarget.BLOCK;

            while (this.passableMaterials.contains(this.currentPoint.getBlock().getType()) && var2 <= this.maxDistance * 100.0) {
               this.traversedLocations.add(this.currentPoint);
               this.advancePoint(new Vector(0.0, 0.0, ++var2 + 50.0));
               if (this.showParticles) {
                  this.origin
                     .getWorld()
                     .spawnParticle(Particle.CLOUD, this.currentPoint.getX(), this.currentPoint.getY(), this.currentPoint.getZ(), 0, 0.0, 0.0, 0.0);
               }
            }

            if (!this.passableMaterials.contains(this.currentPoint.getBlock().getType())) {
               this.hitBlock = this.currentPoint.getBlock();
               this.hitLocation = this.currentPoint;
               return true;
            }
         } else if (var1 == RayTraceTarget.ENTITY) {
            this.hitTarget = RayTraceTarget.ENTITY;
            Collection var3 = this.origin.getWorld().getNearbyEntities(this.currentPoint, 0.01, 0.01, 0.01);

            while ((var3.size() <= 0 || var3.contains(this.ignoredEntity)) && var2 <= this.maxDistance * 100.0) {
               this.traversedLocations.add(this.currentPoint);
               this.advancePoint(new Vector(0.0, 0.0, ++var2 + 50.0));
               var3 = this.origin.getWorld().getNearbyEntities(this.currentPoint, 0.01, 0.01, 0.01);
               if (this.showParticles) {
                  this.origin
                     .getWorld()
                     .spawnParticle(Particle.CLOUD, this.currentPoint.getX(), this.currentPoint.getY(), this.currentPoint.getZ(), 0, 0.0, 0.0, 0.0);
               }
            }

            if (var3.size() > 0) {
               for (Entity var5 : var3) {
                  this.hitEntity = var5;
                  this.hitLocation = this.currentPoint;
               }

               return true;
            }
         } else if (var1 == RayTraceTarget.ENTITY_AND_BLOCK) {
            Collection var7 = this.origin.getWorld().getNearbyEntities(this.currentPoint, 0.01, 0.01, 0.01);

            while (
               this.passableMaterials.contains(this.currentPoint.getBlock().getType())
                  && (var7.size() <= 0 || var7.contains(this.ignoredEntity))
                  && var2 <= this.maxDistance * 100.0
            ) {
               this.traversedLocations.add(this.currentPoint);
               this.advancePoint(new Vector(0.0, 0.0, ++var2 + 50.0));
               var7 = this.origin.getWorld().getNearbyEntities(this.currentPoint, 0.01, 0.01, 0.01);
               if (this.showParticles) {
                  this.origin
                     .getWorld()
                     .spawnParticle(Particle.CLOUD, this.currentPoint.getX(), this.currentPoint.getY(), this.currentPoint.getZ(), 0, 0.0, 0.0, 0.0);
               }
            }

            if (!this.passableMaterials.contains(this.currentPoint.getBlock().getType())) {
               this.hitTarget = RayTraceTarget.BLOCK;
               this.hitBlock = this.currentPoint.getBlock();
               this.hitLocation = this.currentPoint;
               return true;
            }

            if (var7.size() > 0) {
               this.hitTarget = RayTraceTarget.ENTITY;

               for (Entity var9 : var7) {
                  this.hitEntity = var9;
                  this.hitLocation = this.currentPoint;
               }

               return true;
            }
         }
      }

      return false;
   }

   private void advancePoint(Vector var1) {
      var1 = VectorMath.rotateByYawPitch(var1, this.origin.getYaw(), this.origin.getPitch());
      this.currentPoint = new Location(this.origin.getWorld(), this.origin.getX(), this.origin.getY(), this.origin.getZ())
         .clone()
         .add(var1.getX() / 100.0, var1.getY() / 100.0, var1.getZ() / 100.0);
   }

   public ArrayList getPassableMaterials() {
      return this.passableMaterials;
   }

   public void setPassableMaterials(ArrayList var1) {
      this.passableMaterials = var1;
   }

   public void addPassableMaterial(Material var1) {
      if (!this.passableMaterials.contains(var1)) {
         this.nativeTraceEnabled = false;
         this.passableMaterials.add(var1);
      }
   }

   public ArrayList getTraversedLocations() {
      return this.traversedLocations;
   }

   public World getWorld() {
      return this.origin.getWorld();
   }

   public double getX() {
      return this.origin.getX();
   }

   public double getY() {
      return this.origin.getY();
   }

   public double getZ() {
      return this.origin.getZ();
   }

   public double getYaw() {
      return this.origin.getYaw();
   }

   public double getPitch() {
      return this.origin.getPitch();
   }

   public double getMaxDistance() {
      return this.maxDistance;
   }

   public void setMaxDistance(double var1) {
      this.maxDistance = var1;
   }

   public RayTraceTarget getHitTarget() {
      return this.hitTarget;
   }

   public void setHitTarget(RayTraceTarget var1) {
      this.hitTarget = var1;
   }

   public Entity getHitEntity() {
      return this.hitEntity;
   }

   public void setHitEntity(Entity var1) {
      this.hitEntity = var1;
   }

   public boolean hasHitEntity() {
      return this.hitEntity != null;
   }

   public Block getHitBlock() {
      return this.hitBlock;
   }

   public boolean hasHitBlock() {
      return this.hitBlock != null;
   }

   public void setHitBlock(Block var1) {
      this.hitBlock = var1;
   }

   public Location getHitLocation() {
      return this.hitLocation;
   }

   public void setHitLocation(Location var1) {
      this.hitLocation = var1;
   }

   public boolean hasHitLocation() {
      return this.hitLocation != null;
   }

   public boolean isShowingParticles() {
      return this.showParticles;
   }

   @Deprecated
   public void setShowParticles(boolean var1) {
      this.showParticles = var1;
   }

   public void setForceStepping(boolean var1) {
      this.forceStepping = var1;
   }

   public Entity getIgnoredEntity() {
      return this.ignoredEntity;
   }

   public void setIgnoredEntity(Entity var1) {
      this.ignoredEntity = var1;
   }
}
