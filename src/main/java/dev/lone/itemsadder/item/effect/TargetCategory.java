package dev.lone.itemsadder.item.effect;

import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Monster;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.WaterMob;
import org.jetbrains.annotations.Nullable;

public enum TargetCategory {
   HOSTILE("HOSTILE"),
   PLAYERS("PLAYERS"),
   PASSIVE("PASSIVE");

   private final String name;

   TargetCategory(String var3) {
      this.name = var3;
   }

   @Nullable
   public static TargetCategory fromEntity(Entity var0) {
      if (var0 instanceof Monster || var0 instanceof ComplexLivingEntity || var0 instanceof Flying || var0 instanceof Slime) {
         return HOSTILE;
      } else if (var0 instanceof Player) {
         return PLAYERS;
      } else {
         return !(var0 instanceof Animals) && !(var0 instanceof NPC) && !(var0 instanceof Snowman) && !(var0 instanceof WaterMob) && !(var0 instanceof Ambient)
            ? null
            : PASSIVE;
      }
   }

   @Nullable
   public static TargetCategory parse(String var0) {
      for (TargetCategory var4 : values()) {
         if (var4.name.equals(var0)) {
            return var4;
         }
      }

      return null;
   }
}
