package dev.lone.itemsadder.network;

import beer.devs.nativepackets.api.EntityInteractionAction;
import beer.devs.nativepackets.api.InteractEntityPacketView;
import beer.devs.nativepackets.api.InteractionHand;
import java.util.UUID;
import org.bukkit.inventory.EquipmentSlot;

class EntityInteractionEvent {
   private final UUID UN;
   private final long UO;
   private final UUID UP;
   private final int UQ;
   private final EntityInteractionAction UR;
   private final EquipmentSlot US;
   private final boolean UT;
   private final double field19;
   private final double UV;
   private final double UW;

   EntityInteractionEvent(
      UUID var1, long var2, UUID var4, int var5, EntityInteractionAction var6, EquipmentSlot var7, boolean var8, double var9, double var11, double var13
   ) {
      this.UN = var1;
      this.UO = var2;
      this.UP = var4;
      this.UQ = var5;
      this.UR = var6;
      this.US = var7;
      this.UT = var8;
      this.field19 = var9;
      this.UV = var11;
      this.UW = var13;
   }

   private static EntityInteractionEvent method114(UUID var0, long var1, UUID var3, InteractEntityPacketView var4) {
      boolean var5 = var4.hasInteractionPoint();
      return new EntityInteractionEvent(
         var0,
         var1,
         var3,
         var4.entityId(),
         var4.action(),
         var4.hand() == InteractionHand.OFF_HAND ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND,
         var5,
         var5 ? var4.interactionX() : 0.0,
         var5 ? var4.interactionY() : 0.0,
         var5 ? var4.interactionZ() : 0.0
      );
   }

   public UUID bk() {
      return this.UN;
   }

   public long bl() {
      return this.UO;
   }

   public UUID gp() {
      return this.UP;
   }

   public int jI() {
      return this.UQ;
   }

   public EntityInteractionAction vN() {
      return this.UR;
   }

   public EquipmentSlot vO() {
      return this.US;
   }

   public boolean vP() {
      return this.UT;
   }

   public double ka() {
      return this.field19;
   }

   public double kb() {
      return this.UV;
   }

   public double kc() {
      return this.UW;
   }
}
