package dev.lone.itemsadder.util.serial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class SerialTypeF implements SerialType {
   private static final SerialTypeF INSTANCE = new SerialTypeF();
   private static final Map NAMES = new HashMap();

   public SerialRecordF createRecord() {
      return SerialRecordF.create();
   }

   @Override
   public Class getJavaClass() {
      return SerialRecordF.class;
   }

   @Override
   public String getName() {
      return SerialRecordF.class.getSimpleName();
   }

   @Override
   public String getDisplayName() {
      return SerialRecordF.class.getName();
   }

   @Deprecated
   public boolean matchesRecord(SerialRecordF var1) {
      return true;
   }

   public void readRecord(SerialReader var1, SerialRecordF var2) {
      while (true) {
         int var3 = var1.readTag(this);
         if (var3 == 0) {
            var2.self();
            return;
         }

         switch (var3) {
            case 1:
               if (var2.items == null) {
                  var2.items = new ArrayList();
               }

               var2.items.add(var1.readString());
               break;
            default:
               var1.skipField(var3, this);
         }
      }
   }

   public void writeRecord(SerialWriter var1, SerialRecordF var2) {
      synchronized (var2.items) {
         for (String var5 : var2.items) {
            var1.writeString(1, var5, true);
         }
      }
   }

   @Override
   public String nameOf(int number) {
      switch (number) {
         case 1:
            return "item";
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
      NAMES.put("item", 1);
   }
}
