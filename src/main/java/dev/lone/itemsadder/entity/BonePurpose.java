package dev.lone.itemsadder.entity;

public enum BonePurpose {
   NORMAL("normal"),
   HEAD("head"),
   HAT_PIVOT("hatPivot"),
   LEFT_HAND_PIVOT("leftHandPivot"),
   RIGHT_HAND_PIVOT("rightHandPivot"),
   MOUNT_PIVOT("mount"),
   LOCATOR_PIVOT("locator");

   private final String str;

   BonePurpose(String var3) {
      this.str = var3;
   }

   public static BonePurpose fromString(String var0) {
      for (BonePurpose var4 : values()) {
         if (var4.str.equals(var0)) {
            return var4;
         }
      }

      return NORMAL;
   }

   @Override
   public String toString() {
      return this.str;
   }

   public boolean nN() {
      return this == HEAD || this == HAT_PIVOT;
   }

   public boolean nO() {
      return this != MOUNT_PIVOT && this != LEFT_HAND_PIVOT && this != RIGHT_HAND_PIVOT && this != HAT_PIVOT && this != LOCATOR_PIVOT;
   }

   public boolean nP() {
      return this == LEFT_HAND_PIVOT || this == RIGHT_HAND_PIVOT || this == HAT_PIVOT;
   }
}
