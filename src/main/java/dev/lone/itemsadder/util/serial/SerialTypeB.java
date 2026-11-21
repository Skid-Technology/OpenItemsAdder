package dev.lone.itemsadder.util.serial;

import java.util.HashMap;
import java.util.Map;

public final class SerialTypeB implements SerialType {
   private static final SerialTypeB INSTANCE = new SerialTypeB();
   private static final Map NAMES = new HashMap();

   public SerialRecordB createRecord() {
      return SerialRecordB.create();
   }

   @Override
   public Class getJavaClass() {
      return SerialRecordB.class;
   }

   @Override
   public String getName() {
      return SerialRecordB.class.getSimpleName();
   }

   @Override
   public String getDisplayName() {
      return SerialRecordB.class.getName();
   }

   @Deprecated
   public boolean matchesRecord(SerialRecordB var1) {
      return true;
   }

   public void readRecord(SerialReader var1, SerialRecordB var2) {
      while (true) {
         int var3 = var1.readTag(this);
         if (var3 == 0) {
            var2.self();
            return;
         }

         switch (var3) {
            case 1:
               var2.intValue = var1.readInt32();
               var2.presence |= 1;
               break;
            case 2:
               var2.attributes = (SerialRecordA)var1.readMessage(var2.attributes, SerialRecordA.TYPE);
               var2.presence |= 2;
               break;
            default:
               var1.skipField(var3, this);
         }
      }
   }

   public void writeRecord(SerialWriter var1, SerialRecordB var2) {
      if ((var2.presence & 1) == 1) {
         var1.writeInt(1, var2.intValue, false);
      }

      if ((var2.presence & 2) == 2) {
         var1.writeObject(2, var2.attributes, SerialRecordA.TYPE, false);
      }
   }

   @Override
   public String nameOf(int number) {
      switch (number) {
         case 1:
            return "blockId";
         case 2:
            return "additionalProperties";
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
      NAMES.put("blockId", 1);
      NAMES.put("additionalProperties", 2);
   }
}
