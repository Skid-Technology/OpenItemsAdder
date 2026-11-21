package dev.lone.itemsadder.util.serial;

final class ByteArrayTypeHandlerDup2 extends SerialTypeHandler {
   ByteArrayTypeHandlerDup2(ByteArraySerialReader var1) {
      this.bRH = var1;
   }

   @Override
   protected SerialReader getReader(SerialTypeWrapper var1) {
      return this.bRH;
   }

   @Override
   protected void verifyReader(SerialTypeWrapper var1, SerialReader var2, boolean var3) {
      if (!var3) {
         assert var2 == this.bRH;
      }
   }
}
