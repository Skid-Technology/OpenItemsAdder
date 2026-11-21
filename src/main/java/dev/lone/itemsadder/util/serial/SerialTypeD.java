package dev.lone.itemsadder.util.serial;

import java.util.HashMap;
import java.util.Map;

public final class SerialTypeD implements SerialType {
   private static final SerialTypeD INSTANCE = new SerialTypeD();
   private static final Map NAMES = new HashMap();

   public SerialRecordD createRecord() {
      return SerialRecordD.create();
   }

   @Override
   public Class getJavaClass() {
      return SerialRecordD.class;
   }

   @Override
   public String getName() {
      return SerialRecordD.class.getSimpleName();
   }

   @Override
   public String getDisplayName() {
      return SerialRecordD.class.getName();
   }

   @Deprecated
   public boolean matchesRecord(SerialRecordD var1) {
      return true;
   }

   public void readRecord(SerialReader var1, SerialRecordD var2) {
      while (true) {
         int var3 = var1.readTag(this);
         if (var3 == 0) {
            var2.self();
            return;
         }

         switch (var3) {
            case 1:
               var2.key = var1.readString();
               var2.presence |= 1;
               break;
            case 2:
               var2.value = (SerialRecordB)var1.readMessage(var2.value, SerialRecordB.TYPE);
               var2.presence |= 2;
               break;
            default:
               var1.skipField(var3, this);
         }
      }
   }

   public void writeRecord(SerialWriter var1, SerialRecordD var2) {
      if ((var2.presence & 1) == 1) {
         var1.writeString(1, var2.key, false);
      }

      if ((var2.presence & 2) == 2) {
         var1.writeObject(2, var2.value, SerialRecordB.TYPE, false);
      }
   }

   @Override
   public String nameOf(int number) {
      switch (number) {
         case 1:
            return "key";
         case 2:
            return "value";
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
      NAMES.put("key", 1);
      NAMES.put("value", 2);
   }
}
