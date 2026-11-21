package dev.lone.itemsadder.util.serial;

public abstract class SerialTypeHandler {
   protected SerialReader reader;
   protected SerialWriter writer;

   protected SerialTypeHandler afh() {
      this.writer = null;
      this.reader = null;
      return this;
   }

   protected abstract SerialReader getReader(SerialTypeWrapper var1);

   protected abstract void verifyReader(SerialTypeWrapper var1, SerialReader var2, boolean var3);

   public static void bind(SerialTypeWrapper var0, SerialTypeHandler var1, SerialReader var2, SerialWriter var3) {
      var0.initHandler(var1, var2, var3);
   }
}
