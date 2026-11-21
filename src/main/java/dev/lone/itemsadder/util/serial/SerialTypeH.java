package dev.lone.itemsadder.util.serial;

import java.util.HashMap;
import java.util.Map;

public final class SerialTypeH implements SerialType {
   private static final SerialTypeH INSTANCE = new SerialTypeH();
   private static final Map NAMES = new HashMap();

   public SerialRecordH createRecord() {
      return SerialRecordH.create();
   }

   @Override
   public Class getJavaClass() {
      return SerialRecordH.class;
   }

   @Override
   public String getName() {
      return SerialRecordH.class.getSimpleName();
   }

   @Override
   public String getDisplayName() {
      return SerialRecordH.class.getName();
   }

   @Deprecated
   public boolean matchesRecord(SerialRecordH var1) {
      return true;
   }

   public void readRecord(SerialReader var1, SerialRecordH var2) {
      while (true) {
         int var3 = var1.readTag(this);
         if (var3 == 0) {
            var2.self();
            return;
         }

         switch (var3) {
            case 1:
               var2.key = var1.readInt32();
               var2.presence |= 1;
               break;
            case 2:
               var2.value = var1.readString();
               var2.presence |= 2;
               break;
            default:
               var1.skipField(var3, this);
         }
      }
   }

   public void writeRecord(SerialWriter var1, SerialRecordH var2) {
      if ((var2.presence & 1) == 1) {
         var1.writeInt(1, var2.key, false);
      }

      if ((var2.presence & 2) == 2) {
         var1.writeString(2, var2.value, false);
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
