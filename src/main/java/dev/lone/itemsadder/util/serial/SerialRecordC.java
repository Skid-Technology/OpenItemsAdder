package dev.lone.itemsadder.util.serial;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class SerialRecordC implements SerialRecord {
   private Map block;
   private SerialRecordG blocksPalette;
   private SerialRecordG biomesPalette;
   private SerialRecordG cropsSeedsPalette;
   private int presence;
   public static final SerialType TYPE = SerialTypeC.INSTANCE;

   private SerialRecordC self() {
      if (this.block == null) {
         this.block = new ConcurrentHashMap();
      }

      return this;
   }

   public static SerialRecordC create() {
      return createWith(true);
   }

   public static SerialRecordC createWith(boolean var0) {
      return var0 ? new SerialRecordC().self() : new SerialRecordC();
   }

   public Map getBlock() {
      return this.block;
   }

   public SerialRecordC withBlock(Map var1) {
      this.block = var1;
      return this;
   }

   public SerialRecordC putBlock(String var1, SerialRecordB var2) {
      this.block.put(var1, var2);
      return this;
   }

   public int blockCount() {
      return this.block.size();
   }

   public boolean hasBlockKey(String var1) {
      return this.block.containsKey(var1);
   }

   public boolean containsBlockValue(SerialRecordB var1) {
      return this.block.containsValue(var1);
   }

   public SerialRecordB getBlockEntry(String var1) {
      return (SerialRecordB)this.block.get(var1);
   }

   public SerialRecordB removeBlockEntry(String var1) {
      return (SerialRecordB)this.block.remove(var1);
   }

   public boolean removeBlockEntryIf(String var1, SerialRecordB var2) {
      return this.block.remove(var1, var2);
   }

   public SerialRecordG getBlocksPalette() {
      return (this.presence & 2) == 0 ? null : this.blocksPalette;
   }

   public SerialRecordC withBlocksPalette(SerialRecordG var1) {
      this.blocksPalette = var1;
      this.presence |= 2;
      return this;
   }

   public SerialRecordC clearBlocksPalette() {
      this.blocksPalette = null;
      this.presence &= -3;
      return this;
   }

   public boolean hasBlocksPalette() {
      return (this.presence & 2) == 2;
   }

   public SerialRecordG getBiomesPalette() {
      return (this.presence & 4) == 0 ? null : this.biomesPalette;
   }

   public SerialRecordC withBiomesPalette(SerialRecordG var1) {
      this.biomesPalette = var1;
      this.presence |= 4;
      return this;
   }

   public SerialRecordC clearBiomesPalette() {
      this.biomesPalette = null;
      this.presence &= -5;
      return this;
   }

   public boolean hasBiomesPalette() {
      return (this.presence & 4) == 4;
   }

   public SerialRecordG getCropsSeedsPalette() {
      return (this.presence & 8) == 0 ? null : this.cropsSeedsPalette;
   }

   public SerialRecordC withCropsSeedsPalette(SerialRecordG var1) {
      this.cropsSeedsPalette = var1;
      this.presence |= 8;
      return this;
   }

   public SerialRecordC clearCropsSeedsPalette() {
      this.cropsSeedsPalette = null;
      this.presence &= -9;
      return this;
   }

   public boolean hasCropsSeedsPalette() {
      return (this.presence & 8) == 8;
   }

   @Override
   public SerialType getType() {
      return SerialTypeC.INSTANCE;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }

      if (obj != null && this.getClass() == obj.getClass()) {
         SerialRecordC var2 = (SerialRecordC)obj;
         if (!Objects.equals(this.block, var2.block)) {
            return false;
         } else if (!Objects.equals(this.blocksPalette, var2.blocksPalette)) {
            return false;
         } else {
            return !Objects.equals(this.biomesPalette, var2.biomesPalette) ? false : Objects.equals(this.cropsSeedsPalette, var2.cropsSeedsPalette);
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int var1 = 1;
      var1 = 31 * var1 + (this.block == null ? 0 : this.block.hashCode());
      var1 = 31 * var1 + (this.blocksPalette == null ? 0 : this.blocksPalette.hashCode());
      var1 = 31 * var1 + (this.biomesPalette == null ? 0 : this.biomesPalette.hashCode());
      return 31 * var1 + (this.cropsSeedsPalette == null ? 0 : this.cropsSeedsPalette.hashCode());
   }

   @Override
   public String toString() {
      ArrayList var1 = new ArrayList();
      if (!this.block.isEmpty()) {
         var1.add("block=" + this.getBlock());
      }

      if (this.hasBlocksPalette()) {
         var1.add("blocksPalette=" + this.getBlocksPalette());
      }

      if (this.hasBiomesPalette()) {
         var1.add("biomesPalette=" + this.getBiomesPalette());
      }

      if (this.hasCropsSeedsPalette()) {
         var1.add("cropsSeedsPalette=" + this.getCropsSeedsPalette());
      }

      return "PChunk{" + String.join(", ", var1) + "}";
   }
}
