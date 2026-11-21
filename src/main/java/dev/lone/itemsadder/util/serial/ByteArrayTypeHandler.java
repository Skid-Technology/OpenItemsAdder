package dev.lone.itemsadder.util.serial;

final class ByteArrayTypeHandler extends SerialTypeHandler {
   ByteArrayTypeHandler(ByteArraySerialReader var1) {
      this.bRA = var1;
   }

   @Override
   protected SerialReader getReader(SerialTypeWrapper var1) {
      return this.bRA;
   }

   @Override
   protected void verifyReader(SerialTypeWrapper var1, SerialReader var2, boolean var3) {
      if (!var3) {
         assert var2 == this.bRA;
      }
   }
}
