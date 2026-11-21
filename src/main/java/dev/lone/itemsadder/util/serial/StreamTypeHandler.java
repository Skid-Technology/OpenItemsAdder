package dev.lone.itemsadder.util.serial;

final class StreamTypeHandler extends SerialTypeHandler {
   StreamTypeHandler(StreamSerialReader var1) {
      this.reader = var1;
   }

   @Override
   protected SerialReader getReader(SerialTypeWrapper var1) {
      return this.reader;
   }

   @Override
   protected void verifyReader(SerialTypeWrapper var1, SerialReader var2, boolean var3) {
      if (!var3) {
         assert var2 == this.reader;
      }
   }
}
