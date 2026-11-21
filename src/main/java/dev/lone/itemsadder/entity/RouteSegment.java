package dev.lone.itemsadder.entity;

import org.bukkit.Location;
import org.bukkit.util.Vector;

class RouteSegment {
   private final Location Sd;
   private final Vector Se;
   private final Vector Sf;
   private final Location Sg;
   private final Location Sh;

   private RouteSegment(Location var1, Vector var2, Vector var3, Location var4, Location var5) {
      this.Sd = var1;
      this.Se = var2;
      this.Sf = var3;
      this.Sg = var4;
      this.Sh = var5;
   }

   public Location start() {
      return this.Sd;
   }

   public Vector uX() {
      return this.Se;
   }

   public Vector uY() {
      return this.Sf;
   }

   public Location end() {
      return this.Sg;
   }

   public Location va() {
      return this.Sh;
   }
}
