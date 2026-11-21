package dev.lone.itemsadder.util.serial;

import java.util.ArrayList;

public final class SerialListType extends SerialReaderDelegate implements SerialCollectionReader, SerialType {
   private final ArrayList items;
   private int tag = -1;
   private SerialType elementType;
   private boolean packed = false;

   public SerialListType(ByteArraySerialReader var1) {
      super(var1);
      assert var1.lenient;
      this.items = new ArrayList();
   }

   public SerialListType(ByteArraySerialReader var1, int var2) {
      super(var1);
      assert var1.lenient;
      this.items = new ArrayList(var2);
   }

   @Override
   public void addItem(Object var1, Object var2) {
      int var3 = this.items.size() - 1;
      if (var2 != null && var2 == this.items.get(var3)) {
         this.items.set(var3, var1);
      }
   }

   @Override
   public boolean isPackedEncoding() {
      return this.packed;
   }

   @Override
   public int readTag(SerialType var1) {
      int var2 = ((ByteArraySerialReader)this.bQT).readTag(var1);
      if (WireTypes.getWireType(((ByteArraySerialReader)this.bQT).getLastTag()) == 6) {
         this.tag = ((ByteArraySerialReader)this.bQT).readUInt32();
         this.packed = true;
      } else {
         this.packed = false;
      }

      return var2;
   }

   @Override
   public Object readMessage(Object var1, SerialType var2) {
      if (this.packed) {
         return this.items.get(this.tag);
      }

      this.elementType = var2;
      if (var1 == null) {
         var1 = var2.defaultValue();
      }

      this.items.add(var1);
      ((ByteArraySerialReader)this.bQT).readMessage(var1, this);
      return var1;
   }

   @Override
   public void skipField(int var1, SerialType var2) {
      if (!this.packed) {
         ((ByteArraySerialReader)this.bQT).skipUnknownField(((ByteArraySerialReader)this.bQT).getLastTag());
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
      SerialType var3 = this.elementType;
      var3.read(this, var2);
      if (!var3.accepts(var2)) {
         throw new SerialTypeMismatchException(var2, var3);
      }

      this.elementType = var3;
   }

   @Override
   public void write(SerialWriter var1, Object var2) {
      throw new UnsupportedOperationException();
   }
}
