package dev.lone.itemsadder.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityEquipmentChangeEvent extends EntityEvent implements Cancellable {
   private static final HandlerList aHO = new HandlerList();
   private final ItemStack aHP;
   private final ItemStack aHQ;
   private Entity aHR;
   private final EquipmentSlot aHS;
   private final float aHT;
   private boolean aHU;
   private boolean cancelled;

   public EntityEquipmentChangeEvent(
      @NotNull LivingEntity var1,
      @Nullable ItemStack var2,
      @Nullable ItemStack var3,
      @NotNull Entity var4,
      @NotNull EquipmentSlot var5,
      float var6,
      boolean var7
   ) {
      super(var1);
      this.aHP = var2;
      this.aHQ = var3;
      this.aHR = var4;
      this.aHS = var5;
      this.aHT = var6;
      this.aHU = var7;
   }

   @NotNull
   public LivingEntity getEntity() {
      return (LivingEntity)this.entity;
   }

   @Nullable
   public ItemStack Lx() {
      return this.aHP;
   }

   @Nullable
   public ItemStack Ly() {
      return this.aHQ;
   }

   @NotNull
   public Entity Lz() {
      return this.aHR;
   }

   public void method2403(@NotNull Entity var1) {
      this.aHR = var1;
   }

   @NotNull
   public EquipmentSlot getHand() {
      return this.aHS;
   }

   public float LA() {
      return this.aHT;
   }

   public void br(boolean var1) {
      this.aHU = var1;
   }

   public boolean LB() {
      return this.aHU;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean cancel) {
      this.cancelled = cancel;
   }

   @NotNull
   public HandlerList getHandlers() {
      return aHO;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return aHO;
   }
}
