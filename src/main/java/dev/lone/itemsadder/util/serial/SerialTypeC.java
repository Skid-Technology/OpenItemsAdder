package dev.lone.itemsadder.util.serial;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public final class SerialTypeC implements SerialType {
   private static final SerialTypeC INSTANCE = new SerialTypeC();
   private static final Map NAMES = new HashMap();

   public SerialRecordC createRecord() {
      return SerialRecordC.create();
   }

   @Override
   public Class getJavaClass() {
      return SerialRecordC.class;
   }

   @Override
   public String getName() {
      return SerialRecordC.class.getSimpleName();
   }

   @Override
   public String getDisplayName() {
      return SerialRecordC.class.getName();
   }

   @Deprecated
   public boolean matchesRecord(SerialRecordC var1) {
      return true;
   }

   public void readRecord(SerialReader var1, SerialRecordC var2) {
      while (true) {
         int var3 = var1.readTag(this);
         if (var3 == 0) {
            var2.self();
            return;
         }

         switch (var3) {
            case 1:
               if (var2.block == null) {
                  var2.block = new ConcurrentHashMap();
               }

               SerialRecordD var4 = (SerialRecordD)var1.readMessage(null, SerialRecordD.TYPE);
               var2.block.put(var4.getKey(), var4.getValue());
               break;
            case 2:
               var2.blocksPalette = (SerialRecordG)var1.readMessage(var2.blocksPalette, SerialRecordG.TYPE);
               var2.presence |= 2;
               break;
            case 3:
               var2.biomesPalette = (SerialRecordG)var1.readMessage(var2.biomesPalette, SerialRecordG.TYPE);
               var2.presence |= 4;
               break;
            case 4:
               var2.cropsSeedsPalette = (SerialRecordG)var1.readMessage(var2.cropsSeedsPalette, SerialRecordG.TYPE);
               var2.presence |= 8;
               break;
            default:
               var1.skipField(var3, this);
         }
      }
   }

   public void writeRecord(SerialWriter var1, SerialRecordC var2) {
      synchronized (var2.block) {
         for (Entry var5 : var2.block.entrySet()) {
            SerialRecordD var6 = SerialRecordD.create().withKey((String)var5.getKey()).withValue((SerialRecordB)var5.getValue());
            var1.writeObject(1, var6, SerialRecordD.TYPE, true);
         }
      }

      if ((var2.presence & 2) == 2) {
         var1.writeObject(2, var2.blocksPalette, SerialRecordG.TYPE, false);
      }

      if ((var2.presence & 4) == 4) {
         var1.writeObject(3, var2.biomesPalette, SerialRecordG.TYPE, false);
      }

      if ((var2.presence & 8) == 8) {
         var1.writeObject(4, var2.cropsSeedsPalette, SerialRecordG.TYPE, false);
      }
   }

   @Override
   public String nameOf(int number) {
      switch (number) {
         case 1:
            return "block";
         case 2:
            return "blocksPalette";
         case 3:
            return "biomesPalette";
         case 4:
            return "cropsSeedsPalette";
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
      NAMES.put("block", 1);
      NAMES.put("blocksPalette", 2);
      NAMES.put("biomesPalette", 3);
      NAMES.put("cropsSeedsPalette", 4);
   }
}
