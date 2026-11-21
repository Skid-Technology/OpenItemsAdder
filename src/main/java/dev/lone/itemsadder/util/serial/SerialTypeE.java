package dev.lone.itemsadder.util.serial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class SerialTypeE implements SerialType {
   private static final SerialTypeE INSTANCE = new SerialTypeE();
   private static final Map NAMES = new HashMap();

   public SerialRecordE createRecord() {
      return SerialRecordE.create();
   }

   @Override
   public Class getJavaClass() {
      return SerialRecordE.class;
   }

   @Override
   public String getName() {
      return SerialRecordE.class.getSimpleName();
   }

   @Override
   public String getDisplayName() {
      return SerialRecordE.class.getName();
   }

   @Deprecated
   public boolean matchesRecord(SerialRecordE var1) {
      return true;
   }

   public void readRecord(SerialReader var1, SerialRecordE var2) {
      while (true) {
         int var3 = var1.readTag(this);
         if (var3 == 0) {
            var2.self();
            return;
         }

         switch (var3) {
            case 1:
               if (var2.chunks == null) {
                  var2.chunks = new ArrayList();
               }

               var2.chunks.add(var1.readString());
               break;
            default:
               var1.skipField(var3, this);
         }
      }
   }

   public void writeRecord(SerialWriter var1, SerialRecordE var2) {
      synchronized (var2.chunks) {
         for (String var5 : var2.chunks) {
            var1.writeString(1, var5, true);
         }
      }
   }

   @Override
   public String nameOf(int number) {
      switch (number) {
         case 1:
            return "chunk";
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
      NAMES.put("chunk", 1);
   }
}
