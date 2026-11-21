package dev.lone.itemsadder.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.OutputStream;

public class PackedByteStream extends ByteArrayOutputStream {
   public PackedByteStream() {
   }

   public PackedByteStream(int var1) {
      super(var1);
   }

   public byte[] getBuffer() {
      return this.buf;
   }

   public void writeAt(byte[] var1, int var2) {
      System.arraycopy(var1, 0, this.buf, var2, var1.length);
   }

   public void skipBytes(int var1) {
      this.count += var1;
   }

   public void writeTo(DataOutput var1) {
      var1.write(this.getBuffer(), 0, this.size());
   }

   @Override
   public void writeTo(OutputStream var1) {
      var1.write(this.getBuffer(), 0, this.size());
   }

   public ByteArrayInputStream toInputStream() {
      return new ByteArrayInputStream(this.getBuffer(), 0, this.size());
   }

   public PackedByteStream replaceContents(byte[] var1) {
      System.arraycopy(var1, 0, this.buf, 0, var1.length);
      return this;
   }
}
