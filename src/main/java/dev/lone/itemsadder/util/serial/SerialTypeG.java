package dev.lone.itemsadder.util.serial;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public final class SerialTypeG implements SerialType {
   private static final SerialTypeG INSTANCE = new SerialTypeG();
   private static final Map NAMES = new HashMap();

   public SerialRecordG createRecord() {
      return SerialRecordG.create();
   }

   @Override
   public Class getJavaClass() {
      return SerialRecordG.class;
   }

   @Override
   public String getName() {
      return SerialRecordG.class.getSimpleName();
   }

   @Override
   public String getDisplayName() {
      return SerialRecordG.class.getName();
   }

   @Deprecated
   public boolean matchesRecord(SerialRecordG var1) {
      return true;
   }

   public void readRecord(SerialReader var1, SerialRecordG var2) {
      while (true) {
         int var3 = var1.readTag(this);
         if (var3 == 0) {
            var2.self();
            return;
         }

         switch (var3) {
            case 1:
               if (var2.data == null) {
                  var2.data = new ConcurrentHashMap();
               }

               SerialRecordH var4 = (SerialRecordH)var1.readMessage(null, SerialRecordH.TYPE);
               var2.data.put(var4.getKey(), var4.getValue());
               break;
            default:
               var1.skipField(var3, this);
         }
      }
   }

   public void writeRecord(SerialWriter var1, SerialRecordG var2) {
      synchronized (var2.data) {
         for (Entry var5 : var2.data.entrySet()) {
            SerialRecordH var6 = SerialRecordH.create().withKey((Integer)var5.getKey()).withValue((String)var5.getValue());
            var1.writeObject(1, var6, SerialRecordH.TYPE, true);
         }
      }
   }

   @Override
   public String nameOf(int number) {
      switch (number) {
         case 1:
            return "data";
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
      NAMES.put("data", 1);
   }
}
