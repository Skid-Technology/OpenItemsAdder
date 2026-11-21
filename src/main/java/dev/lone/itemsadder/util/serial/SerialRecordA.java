package dev.lone.itemsadder.util.serial;

import java.util.ArrayList;
import java.util.Objects;

public final class SerialRecordA implements SerialRecord {
   private SerialRecordF inventory;
   private int cropSeedId;
   private boolean isLiquid;
   private int realBiomeId;
   private String song;
   private int instrument;
   private int fireAge;
   private boolean isOtherPluginCustomBlock;
   private int presence;
   public static final SerialType TYPE = SerialTypeA.INSTANCE;

   private SerialRecordA self() {
      return this;
   }

   public static SerialRecordA create() {
      return createWith(true);
   }

   public static SerialRecordA createWith(boolean var0) {
      return var0 ? new SerialRecordA().self() : new SerialRecordA();
   }

   public SerialRecordF getInventory() {
      return (this.presence & 1) == 0 ? null : this.inventory;
   }

   public SerialRecordA withInventory(SerialRecordF var1) {
      this.inventory = var1;
      this.presence |= 1;
      return this;
   }

   public SerialRecordA clearInventory() {
      this.inventory = null;
      this.presence &= -2;
      return this;
   }

   public boolean hasInventory() {
      return (this.presence & 1) == 1;
   }

   public int getCropSeedId() {
      return (this.presence & 2) == 0 ? 0 : this.cropSeedId;
   }

   public SerialRecordA withCropSeedId(int var1) {
      this.cropSeedId = var1;
      this.presence |= 2;
      return this;
   }

   public SerialRecordA clearCropSeedId() {
      this.cropSeedId = 0;
      this.presence &= -3;
      return this;
   }

   public boolean hasCropSeedId() {
      return (this.presence & 2) == 2;
   }

   public boolean isLiquid() {
      return (this.presence & 4) == 0 ? false : this.isLiquid;
   }

   public SerialRecordA withIsLiquid(boolean var1) {
      this.isLiquid = var1;
      this.presence |= 4;
      return this;
   }

   public SerialRecordA clearIsLiquid() {
      this.isLiquid = false;
      this.presence &= -5;
      return this;
   }

   public boolean hasIsLiquid() {
      return (this.presence & 4) == 4;
   }

   public int getRealBiomeId() {
      return (this.presence & 8) == 0 ? 0 : this.realBiomeId;
   }

   public SerialRecordA withRealBiomeId(int var1) {
      this.realBiomeId = var1;
      this.presence |= 8;
      return this;
   }

   public SerialRecordA clearRealBiomeId() {
      this.realBiomeId = 0;
      this.presence &= -9;
      return this;
   }

   public boolean hasRealBiomeId() {
      return (this.presence & 8) == 8;
   }

   public String getSong() {
      return (this.presence & 16) == 0 ? null : this.song;
   }

   public SerialRecordA withSong(String var1) {
      this.song = var1;
      this.presence |= 16;
      return this;
   }

   public SerialRecordA clearSong() {
      this.song = null;
      this.presence &= -17;
      return this;
   }

   public boolean hasSong() {
      return (this.presence & 16) == 16;
   }

   public int getInstrument() {
      return (this.presence & 32) == 0 ? 0 : this.instrument;
   }

   public SerialRecordA withInstrument(int var1) {
      this.instrument = var1;
      this.presence |= 32;
      return this;
   }

   public SerialRecordA clearInstrument() {
      this.instrument = 0;
      this.presence &= -33;
      return this;
   }

   public boolean hasInstrument() {
      return (this.presence & 32) == 32;
   }

   public int getFireAge() {
      return (this.presence & 64) == 0 ? 0 : this.fireAge;
   }

   public SerialRecordA withFireAge(int var1) {
      this.fireAge = var1;
      this.presence |= 64;
      return this;
   }

   public SerialRecordA clearFireAge() {
      this.fireAge = 0;
      this.presence &= -65;
      return this;
   }

   public boolean hasFireAge() {
      return (this.presence & 64) == 64;
   }

   public boolean isOtherPluginCustomBlock() {
      return (this.presence & 128) == 0 ? false : this.isOtherPluginCustomBlock;
   }

   public SerialRecordA withIsOtherPluginCustomBlock(boolean var1) {
      this.isOtherPluginCustomBlock = var1;
      this.presence |= 128;
      return this;
   }

   public SerialRecordA clearIsOtherPluginCustomBlock() {
      this.isOtherPluginCustomBlock = false;
      this.presence &= -129;
      return this;
   }

   public boolean hasIsOtherPluginCustomBlock() {
      return (this.presence & 128) == 128;
   }

   @Override
   public SerialType getType() {
      return SerialTypeA.INSTANCE;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }

      if (obj != null && this.getClass() == obj.getClass()) {
         SerialRecordA var2 = (SerialRecordA)obj;
         if (!Objects.equals(this.inventory, var2.inventory)) {
            return false;
         } else if (!Objects.equals(this.cropSeedId, var2.cropSeedId)) {
            return false;
         } else if (!Objects.equals(this.isLiquid, var2.isLiquid)) {
            return false;
         } else if (!Objects.equals(this.realBiomeId, var2.realBiomeId)) {
            return false;
         } else if (!Objects.equals(this.song, var2.song)) {
            return false;
         } else if (!Objects.equals(this.instrument, var2.instrument)) {
            return false;
         } else {
            return !Objects.equals(this.fireAge, var2.fireAge) ? false : Objects.equals(this.isOtherPluginCustomBlock, var2.isOtherPluginCustomBlock);
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int var1 = 1;
      var1 = 31 * var1 + (this.inventory == null ? 0 : this.inventory.hashCode());
      var1 = 31 * var1 + Integer.hashCode(this.cropSeedId);
      var1 = 31 * var1 + Boolean.hashCode(this.isLiquid);
      var1 = 31 * var1 + Integer.hashCode(this.realBiomeId);
      var1 = 31 * var1 + (this.song == null ? 0 : this.song.hashCode());
      var1 = 31 * var1 + Integer.hashCode(this.instrument);
      var1 = 31 * var1 + Integer.hashCode(this.fireAge);
      return 31 * var1 + Boolean.hashCode(this.isOtherPluginCustomBlock);
   }

   @Override
   public String toString() {
      ArrayList var1 = new ArrayList();
      if (this.hasInventory()) {
         var1.add("inventory=" + this.getInventory());
      }

      if (this.hasCropSeedId()) {
         var1.add("cropSeedId=" + this.getCropSeedId());
      }

      if (this.hasIsLiquid()) {
         var1.add("isLiquid=" + this.isLiquid());
      }

      if (this.hasRealBiomeId()) {
         var1.add("realBiomeId=" + this.getRealBiomeId());
      }

      if (this.hasSong()) {
         var1.add("song=" + this.getSong());
      }

      if (this.hasInstrument()) {
         var1.add("instrument=" + this.getInstrument());
      }

      if (this.hasFireAge()) {
         var1.add("fireAge=" + this.getFireAge());
      }

      if (this.hasIsOtherPluginCustomBlock()) {
         var1.add("isOtherPluginCustomBlock=" + this.isOtherPluginCustomBlock());
      }

      return "PAdditionalAttributes{" + String.join(", ", var1) + "}";
   }
}
