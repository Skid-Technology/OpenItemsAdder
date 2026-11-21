package dev.lone.itemsadder.util.serial;

import java.util.ArrayList;

public final class SerialListTypeDup2 extends SerialReaderDelegate implements SerialCollectionReader, SerialType {
   private final ArrayList bRa;
   private int bQW = -1;
   private SerialType bQX;
   private boolean bQY = false;

   public SerialListTypeDup2(StreamSerialReader var1) {
      super(var1);
      assert var1.lenient;
      this.bRa = new ArrayList();
   }

   public SerialListTypeDup2(StreamSerialReader var1, int var2) {
      super(var1);
      assert var1.lenient;
      this.bRa = new ArrayList(var2);
   }

   @Override
   public void addItem(Object var1, Object var2) {
      int var3 = this.bRa.size() - 1;
      if (var2 != null && var2 == this.bRa.get(var3)) {
         this.bRa.set(var3, var1);
      }
   }

   @Override
   public boolean isPackedEncoding() {
      return this.bQY;
   }

   @Override
   public int readTag(SerialType var1) {
      int var2 = ((StreamSerialReader)this.bQT).readTag(var1);
      if (WireTypes.getWireType(((StreamSerialReader)this.bQT).getLastTag()) == 6) {
         this.bQW = ((StreamSerialReader)this.bQT).readUInt32();
         this.bQY = true;
      } else {
         this.bQY = false;
      }

      return var2;
   }

   @Override
   public Object readMessage(Object var1, SerialType var2) {
      if (this.bQY) {
         return this.bRa.get(this.bQW);
      }

      this.bQX = var2;
      if (var1 == null) {
         var1 = var2.defaultValue();
      }

      this.bRa.add(var1);
      ((StreamSerialReader)this.bQT).readMessage(var1, this);
      return var1;
   }

   @Override
   public void skipField(int var1, SerialType var2) {
      if (!this.bQY) {
         ((StreamSerialReader)this.bQT).skipUnknownField(((StreamSerialReader)this.bQT).getLastTag());
      }
   }

   @Override
   public String nameOf(int var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public int idOf(String var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean accepts(Object var1) {
      return true;
   }

   @Override
   public String getDisplayName() {
      throw new UnsupportedOperationException();
   }

   @Override
   public String getName() {
      throw new UnsupportedOperationException();
   }

   @Override
   public Object defaultValue() {
      throw new UnsupportedOperationException();
   }

   @Override
   public Class getJavaClass() {
      throw new UnsupportedOperationException();
   }

   @Override
   public void read(SerialReader var1, Object var2) {
      SerialType var3 = this.bQX;
      var3.read(this, var2);
      if (!var3.accepts(var2)) {
         throw new SerialTypeMismatchException(var2, var3);
      }

      this.bQX = var3;
   }

   @Override
   public void write(SerialWriter var1, Object var2) {
      throw new UnsupportedOperationException();
   }
}
