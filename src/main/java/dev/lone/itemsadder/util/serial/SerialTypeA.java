package dev.lone.itemsadder.util.serial;

import java.util.HashMap;
import java.util.Map;

public final class SerialTypeA implements SerialType {
   private static final SerialTypeA INSTANCE = new SerialTypeA();
   private static final Map NAMES = new HashMap();

   public SerialRecordA createRecord() {
      return SerialRecordA.create();
   }

   @Override
   public Class getJavaClass() {
      return SerialRecordA.class;
   }

   @Override
   public String getName() {
      return SerialRecordA.class.getSimpleName();
   }

   @Override
   public String getDisplayName() {
      return SerialRecordA.class.getName();
   }

   @Deprecated
   public boolean matchesRecord(SerialRecordA var1) {
      return true;
   }

   public void readRecord(SerialReader var1, SerialRecordA var2) {
      while (true) {
         int var3 = var1.readTag(this);
         if (var3 == 0) {
            var2.self();
            return;
         }

         switch (var3) {
            case 1:
               var2.inventory = (SerialRecordF)var1.readMessage(var2.inventory, SerialRecordF.TYPE);
               var2.presence |= 1;
               break;
            case 2:
               var2.cropSeedId = var1.readInt32();
               var2.presence |= 2;
               break;
            case 3:
               var2.isLiquid = var1.readBool();
               var2.presence |= 4;
               break;
            case 4:
               var2.realBiomeId = var1.readInt32();
               var2.presence |= 8;
               break;
            case 5:
               var2.song = var1.readString();
               var2.presence |= 16;
               break;
            case 6:
               var2.instrument = var1.readInt32();
               var2.presence |= 32;
               break;
            case 7:
               var2.fireAge = var1.readInt32();
               var2.presence |= 64;
               break;
            case 8:
               var2.isOtherPluginCustomBlock = var1.readBool();
               var2.presence |= 128;
               break;
            default:
               var1.skipField(var3, this);
         }
      }
   }

   public void writeRecord(SerialWriter var1, SerialRecordA var2) {
      if ((var2.presence & 1) == 1) {
         var1.writeObject(1, var2.inventory, SerialRecordF.TYPE, false);
      }

      if ((var2.presence & 2) == 2) {
         var1.writeInt(2, var2.cropSeedId, false);
      }

      if ((var2.presence & 4) == 4) {
         var1.writeBool(3, var2.isLiquid, false);
      }

      if ((var2.presence & 8) == 8) {
         var1.writeInt(4, var2.realBiomeId, false);
      }

      if ((var2.presence & 16) == 16) {
         var1.writeString(5, var2.song, false);
      }

      if ((var2.presence & 32) == 32) {
         var1.writeInt(6, var2.instrument, false);
      }

      if ((var2.presence & 64) == 64) {
         var1.writeInt(7, var2.fireAge, false);
      }

      if ((var2.presence & 128) == 128) {
         var1.writeBool(8, var2.isOtherPluginCustomBlock, false);
      }
   }

   @Override
   public String nameOf(int number) {
      switch (number) {
         case 1:
            return "inventory";
         case 2:
            return "cropSeedId";
         case 3:
            return "isLiquid";
         case 4:
            return "realBiomeId";
         case 5:
            return "song";
         case 6:
            return "instrument";
         case 7:
            return "fireAge";
         case 8:
            return "isOtherPluginCustomBlock";
         default:
            return null;
      }
   }

   @Override
   public int idOf(String name) {
      Integer var2 = (Integer)NAMES.get(name);
      return var2 == null ? 0 : var2;
   }

   static {
      NAMES.put("inventory", 1);
      NAMES.put("cropSeedId", 2);
      NAMES.put("isLiquid", 3);
      NAMES.put("realBiomeId", 4);
      NAMES.put("song", 5);
      NAMES.put("instrument", 6);
      NAMES.put("fireAge", 7);
      NAMES.put("isOtherPluginCustomBlock", 8);
   }
}
