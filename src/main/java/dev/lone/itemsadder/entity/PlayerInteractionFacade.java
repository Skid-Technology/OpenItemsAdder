package dev.lone.itemsadder.entity;

import beer.devs.nativepackets.api.NativeEquipment;
import beer.devs.nativepackets.api.NativeEquipmentSlot;
import beer.devs.nativepackets.api.NativeItem;
import dev.lone.itemsadder.internal.SwitchMap55;
import dev.lone.itemsadder.item.ItemStackUtils;
import dev.lone.itemsadder.network.NativePacketsHolder;
import dev.lone.itemsadder.nms.NmsRegistry;
import dev.lone.itemsadder.util.BlockFaceUtils;
import java.util.List;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public final class PlayerInteractionFacade {
   public static final int bni = 36;
   private final PlayerInteractionHandler bnj = (PlayerInteractionHandler)NmsRegistry.method2777(PlayerInteractionHandler.class);
   private static PlayerInteractionFacade bnk;

   PlayerInteractionFacade() {
   }

   private static PlayerInteractionFacade getInstance() {
      if (bnk == null) {
         bnk = new PlayerInteractionFacade();
      }

      return bnk;
   }

   public static PlayerInteractionHandler getHandler() {
      return getInstance().bnj;
   }

   public static void method5310(Player player, EquipmentSlot hand) {
      getHandler().swingHand(player, hand);
   }

   public static void cI(Player var0) {
      giveItem(var0, var0.getInventory().getItemInOffHand(), 45, 0);
   }

   public static void method5311(Player var0, ItemStack var1) {
      PlayerInventory var2 = var0.getInventory();
      giveItem(var0, var1, 36 + var2.getHeldItemSlot(), 0);
   }

   public static void method5312(Player var0, ItemStack var1) {
      PlayerInventory var2 = var0.getInventory();
      giveItem(var0, var1, 45, 0);
   }

   public static void method5313(Player var0, boolean var1) {
      int var2 = var0.getInventory().getHeldItemSlot();
      PlayerInventory var3 = var0.getInventory();
      int var4 = 36;

      for (int var5 = 0; var5 <= 8; var5++) {
         if (var1 || var2 != var5) {
            giveItem(var0, var3.getItem(var5), var4, 0);
         }

         var4++;
      }
   }

   public static void method5314(Player var0, ItemStack var1, int var2) {
      giveItem(var0, var1, var2, getInstance().bnj.cx(var0));
   }

   public static void giveItem(Player var0, ItemStack var1, int var2, int var3) {
      if (var1 == null) {
         var1 = new ItemStack(Material.AIR);
      }

      NativeItem var4 = NativePacketsHolder.PE().Py().item(var1);
      Object var5 = NativePacketsHolder.PE().Pv().setSlot(var3, 0, var2, var4);
      NativePacketsHolder.PE().method1867(var0, var5);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static String slotName(EquipmentSlot var0) {
      switch (SwitchMap55.bnl[var0.ordinal()]) {
         case 1:
            return "mainhand";
         case 2:
            return "offhand";
         case 3:
            return "feet";
         case 4:
            return "legs";
         case 5:
            return "chest";
         case 6:
            return "head";
         default:
            return null;
      }
   }

   public static boolean method5317(PlayerTeleportEvent var0) {
      return var0.getCause() == TeleportCause.PLUGIN
         && var0.getFrom().getX() == var0.getTo().getX()
         && var0.getFrom().getZ() == var0.getTo().getZ()
         && var0.getTo().getY() - var0.getFrom().getY() >= 0.0049F
         && var0.getTo().getY() - var0.getFrom().getY() <= 0.00509F;
   }

   public static void cJ(Player var0) {
      SpectatorModeService.getAdapter().cw(var0);
   }

   public static void cK(Player var0) {
      Location var1 = var0.getLocation();
      var1.setY(var1.getY() + 0.005);
      var0.teleport(var1);
      SpectatorModeService.getAdapter().method3425(var0, true, true, 1.0E-10F);
      var0.setVelocity(new Vector(0, 0, 0));
   }

   public static void sendBreakPacket(Player var0, Vector var1, int var2, int var3) {
      Object var4 = NativePacketsHolder.PE().Pv().blockBreakAnimation(var3, var1.getBlockX(), var1.getBlockY(), var1.getBlockZ(), var2);

      for (Player var6 : var0.getLocation().getWorld().getPlayers()) {
         if (var6.getLocation().distance(var0.getLocation()) <= 6.0) {
            NativePacketsHolder.PE().method1868(var6, var4);
         }
      }
   }

   public static Block cL(Player var0) {
      return var0.getTargetBlock(BlockFaceUtils.PASSABLE_MATERIALS, 8);
   }

   public static Block cM(Player var0) {
      return var0.getTargetBlock(BlockFaceUtils.REPLACEABLE_MATERIALS, 8);
   }

   public static void cN(Player var0) {
      Object var1 = NativePacketsHolder.PE().Pv().entityEvent(var0.getEntityId(), EntityEffect.TOTEM_RESURRECT.getData());
      NativePacketsHolder.PE().method1868(var0, var1);
   }

   public static void method5319(Player var0, ItemStack var1) {
      ItemStack var2 = var0.getInventory().getItemInMainHand();
      ItemStack var3 = var0.getInventory().getItemInOffHand();
      boolean var4 = var2.getType() == Material.TOTEM_OF_UNDYING;
      if (var4) {
         method5311(var0, ItemStackUtils.bMp);
      }

      method5312(var0, var1);
      cN(var0);
      method5312(var0, var3);
      if (var4) {
         method5311(var0, var2);
      }
   }

   public static void method5320(Player var0, NativeEquipmentSlot var1, ItemStack var2) {
      ItemStack var3 = var2 == null ? ItemStackUtils.bMp : var2;
      NativeItem var4 = NativePacketsHolder.PE().Py().item(var3);
      Object var5 = NativePacketsHolder.PE().Pv().equipment(var0.getEntityId(), List.of(new NativeEquipment(var1, var4)));
      NativePacketsHolder.PE().method1867(var0, var5);
   }
}
