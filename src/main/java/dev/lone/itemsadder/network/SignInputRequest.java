package dev.lone.itemsadder.network;

import dev.lone.itemsadder.text.LoreLines;
import dev.lone.itemsadder.util.BlockPos;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SignInputRequest {
   private final SignInputListener aMg;
   private final Player aMh;
   private final List aMi;
   private BiPredicate aMj;
   private boolean aMk;
   private Consumer aMl;

   SignInputRequest(SignInputListener var1, Player var2, List var3) {
      this.aMg = var1;
      this.aMh = var2;
      this.aMi = var3;
   }

   void method2726(Consumer var1) {
      this.aMl = var1;
   }

   public SignInputRequest MN() {
      this.aMk = true;
      return this;
   }

   public SignInputRequest method2727(BiPredicate var1) {
      this.aMj = var1;
      return this;
   }

   public void Mn() {
      if (this.aMg.cV) {
         Location var1 = this.aMh.getLocation();
         var1.setY(var1.getBlockY() - 4);
         this.aMh.sendBlockChange(var1, Material.OAK_SIGN.createBlockData());
         String[] var2 = LoreLines.method2723(this.aMi);

         for (int var3 = 0; var3 < 4; var3++) {
            var2[var3] = this.eJ(var2[var3]);
         }

         this.aMh.sendSignChange(var1, var2);
         BlockPos var6 = new BlockPos(var1.getBlockX(), var1.getBlockY(), var1.getBlockZ());

         try {
            Object var4 = NativePacketsHolder.PE().Pv().openSignEditor(var6.x(), var6.y(), var6.z(), true);
            NativePacketsHolder.PE().method1867(this.aMh, var4);
         } catch (RuntimeException | LinkageError var5) {
            var5.printStackTrace();
         }

         this.aMl.accept(var6);
      }
   }

   private String eJ(String var1) {
      return ChatColor.translateAlternateColorCodes('&', var1);
   }
}
