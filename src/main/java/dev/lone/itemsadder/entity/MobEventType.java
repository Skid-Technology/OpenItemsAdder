package dev.lone.itemsadder.entity;

public enum MobEventType {
   INTERACT("interact"),
   ATTACK("attack"),
   ATTACKED("attacked"),
   DAMAGED("damaged"),
   DEATH("death");

   private final String text;

   MobEventType(String var3) {
      this.text = var3;
   }

   @Override
   public String toString() {
      return this.text;
   }
}
