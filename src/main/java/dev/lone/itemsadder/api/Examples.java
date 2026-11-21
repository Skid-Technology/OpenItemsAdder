package dev.lone.itemsadder.api;

import dev.lone.itemsadder.api.Events.CustomEntityDeathEvent;
import dev.lone.itemsadder.event.IaListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;

public class Examples implements IaListener {
   @EventHandler
   void test(CustomEntityDeathEvent e) {
      System.out.println(e.getKiller().getName() + " killed " + e.getNamespacedID());
      Player var2 = e.getKiller();
      CustomStack var3 = CustomStack.byItemStack(var2.getItemInHand());
      if (var3 == null) {
         System.out.println("killed using: " + var2.getItemInHand().getType() + " (non custom item)");
      } else {
         System.out.println("killed using: " + var3.getNamespacedID());
         System.out.println("       model_path:    " + var3.getModelPath());
         System.out.println("       config_path:   " + var3.getConfigPath());
         System.out.println("       attack_damage: " + var3.getDamageMainhand());
         ItemMeta var4 = var3.getItemStack().getItemMeta();
         var4.setDisplayName("TEST");
         var3.getItemStack().setItemMeta(var4);
      }
   }

   @EventHandler
   void test(PlayerInteractEvent e) {
      if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getHand() != EquipmentSlot.OFF_HAND) {
         CustomBlock var2 = CustomBlock.getInstance("ruby_ore");
         var2.place(e.getClickedBlock().getLocation().add(1.0, 0.0, 1.0));
         if (var2 != null) {
            var2.setCurrentLightLevel(15);
            System.out.println(var2.getLoot());
         }

         CustomCrop var3 = CustomCrop.place("tomato_seeds", e.getClickedBlock().getLocation().add(0.0, 1.0, 0.0));
         var3.setFullyGrown();
         if (var3 != null) {
            System.out.println(var3.getSeed().getItemStack());
         }

         ItemsAdder.playTotemAnimation(e.getPlayer(), "bruh");
         CustomMob var4 = CustomMob.spawn("soul", e.getClickedBlock().getLocation().add(0.0, 1.0, 0.0));
         if (var4 != null) {
            System.out.println("Spawned mob: " + var4.getName());
         }
      }
   }

   @EventHandler
   void interact(PlayerInteractEvent e) {
      if (e.getHand() != EquipmentSlot.OFF_HAND) {
         CustomCrop var2 = CustomCrop.byAlreadyPlaced(e.getClickedBlock());
         if (var2 == null) {
            System.out.println("Not a crop");
         } else {
            System.out.println(var2.getSeed().getNamespacedID());
         }

         CustomBlock var3 = CustomBlock.byAlreadyPlaced(e.getClickedBlock());
         if (var3 == null) {
            System.out.println("Not a custom block");
         } else {
            System.out.println(var3.getNamespacedID());
            System.out.println(var3.generateBlockData());
         }
      }
   }

   @EventHandler
   void interact(PlayerInteractAtEntityEvent e) {
      if (e.getHand() != EquipmentSlot.OFF_HAND) {
         CustomMob var2 = CustomMob.byAlreadySpawned(e.getRightClicked());
         if (var2 == null) {
            System.out.println("Not a custom mob");
         } else {
            System.out.println(var2.getNamespacedID());
         }
      }
   }
}
