package dev.lone.itemsadder.item;

import beer.devs.nativepackets.api.PlayerAction;
import java.util.UUID;

class BlockDigAction {
   private final UUID ahn;
   private final long aho;
   private final PlayerAction ahp;
   private final int ahq;
   private final int ahr;
   private final int ahs;
   private final int aht;
   private final long ahu;

   private BlockDigAction(UUID var1, long var2, PlayerAction var4, int var5, int var6, int var7, int var8, long var9) {
      this.ahn = var1;
      this.aho = var2;
      this.ahp = var4;
      this.ahq = var5;
      this.ahr = var6;
      this.ahs = var7;
      this.aht = var8;
      this.ahu = var9;
   }

   public UUID bk() {
      return this.ahn;
   }

   public long bl() {
      return this.aho;
   }

   public PlayerAction BS() {
      return this.ahp;
   }

   public int method1() {
      return this.ahq;
   }

   public int method2() {
      return this.ahr;
   }

   public int method3() {
      return this.ahs;
   }

   public int BT() {
      return this.aht;
   }

   public long ai() {
      return this.ahu;
   }
}
