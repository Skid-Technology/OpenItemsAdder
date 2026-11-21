package dev.lone.itemsadder.item.effect;

import dev.lone.itemsadder.entity.ProjectileTypeHelper;
import dev.lone.itemsadder.item.CustomItemDefinition;
import dev.lone.itemsadder.util.SchedulerUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class AreaEffect extends ItemEffect {
   public final Vector ajq;
   public final BlockMatcherSpec ajr;
   public final int ajs;

   public AreaEffect(BlockMatcherSpec var1, int var2, int var3, int var4, int var5) {
      this.ajr = var1;
      this.ajs = var5;
      this.ajq = new Vector(var2, var3, var4);
   }

   @Override
   public EffectOutcome applyEffect(Event var1, CustomItemDefinition var2, ItemStack var3, Player var4) {
      Location var5 = var4.getLocation();
      int var6 = var5.getBlockX();
      int var7 = var5.getBlockY();
      int var8 = var5.getBlockZ();
      int var9 = (int)this.ajq.getX();
      int var10 = (int)this.ajq.getY();
      int var11 = (int)this.ajq.getZ();
      long var12 = (long)var9 * var9;
      World var14 = var5.getWorld();
      ArrayList var15 = new ArrayList(101);

      label41:
      for (int var16 = var6 - var9; var16 <= var6 + var9; var16++) {
         long var17 = (long)(var6 - var16) * (var6 - var16);

         for (int var19 = var8 - var11; var19 <= var8 + var11; var19++) {
            long var20 = var17 + (long)(var8 - var19) * (var8 - var19);

            for (int var22 = var7 - var10; var22 < var7 + var10; var22++) {
               long var23 = var20 + (long)(var7 - var22) * (var7 - var22);
               if (var23 < var12) {
                  Block var25 = var14.getBlockAt(var16, var22 + 2, var19);
                  if (this.ajr.matches(var25)) {
                     if (var15.size() > 100) {
                        break label41;
                     }

                     var15.add(ProjectileTypeHelper.getAdapter().method3264(new Location(var14, var16 + 0.5, var22 + 2, var19 + 0.5)));
                  }
               }
            }
         }
      }

      if (var15.size() > 0) {
         double var26 = 128.0 + new Vector(this.ajq.getX(), this.ajq.getY() + 2.0, this.ajq.getZ()).length();
         double var18 = var26 * var26;
         List var27 = var14.getPlayers().stream().filter(var3x -> var3x.getLocation().distanceSquared(var5) < var18).toList();
         int[] var21 = var15.stream().mapToInt(Integer::intValue).toArray();
         SchedulerUtils.runLaterSync(
            () -> var27.stream().filter(OfflinePlayer::isOnline).forEach(var1xx -> ProjectileTypeHelper.getAdapter().method3261(var1xx, var21)), this.ajs
         );
         return EffectOutcome.failure();
      } else {
         return EffectOutcome.success();
      }
   }
}
