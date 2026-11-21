package dev.lone.itemsadder.util.serial;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerialByteBuffer {
   public static final int DEFAULT_BLOCK_SIZE = 256;
   public final int blockSize;
   ByteBuffer current;
   List completed = new ArrayList();

   public SerialByteBuffer() {
      this(256);
   }

   public SerialByteBuffer(int var1) {
      assert var1 >= 8;
      this.blockSize = var1;
      this.current = ByteBuffer.allocate(var1);
   }

   public long size() {
      long var1 = 0L;

      for (ByteBuffer var4 : this.completed) {
         var1 += var4.remaining();
      }

      if (this.current != null) {
         var1 += this.current.position();
      }

      return var1;
   }

   public List getBuffers() {
      int var1 = this.completed.size() + (this.current != null ? 1 : 0);
      ArrayList var2 = new ArrayList(var1);

      for (ByteBuffer var4 : this.completed) {
         var2.add(var4.duplicate());
      }

      if (this.current != null) {
         ByteBuffer var5 = this.current.duplicate();
         ((Buffer)var5).flip();
         var2.add(var5);
      }

      return Collections.unmodifiableList(var2);
   }

   private void rollOver() {
      ((Buffer)this.current).flip();
      this.completed.add(this.current);
      this.current = ByteBuffer.allocate(this.blockSize);
   }

   private void appendBuffer(ByteBuffer var1) {
      if (this.current.position() == 0) {
         this.completed.add(var1);
      } else {
         ((Buffer)this.current).flip();
         this.completed.add(this.current);
         this.completed.add(var1);
         this.current = ByteBuffer.allocate(this.blockSize);
      }
   }

   private void ensureRemaining(int var1) {
      if (this.current.remaining() < var1) {
         this.rollOver();
      }
   }

   public List finish() {
      ((Buffer)this.current).flip();
      this.completed.add(this.current);
      this.current = null;
      this.completed = Collections.unmodifiableList(this.completed);
      return this.getBuffers();
   }

   public SerialByteBuffer writeByte(byte var1) {
      this.ensureRemaining(1);
      this.current.put(var1);
      return this;
   }

   public SerialByteBuffer writeShort(int var1) {
      this.ensureRemaining(2);
      this.current.putShort((short)var1);
      return this;
   }

   public SerialByteBuffer writeShortLE(int var1) {
      this.ensureRemaining(2);
      FixedWidthCodec.writeShortLEBuf(var1, this.current);
      return this;
   }

   public SerialByteBuffer writeIntBE(int var1) {
      this.ensureRemaining(4);
      this.current.putInt(var1);
      return this;
   }

   public SerialByteBuffer writeIntLE(int var1) {
      this.ensureRemaining(4);
      FixedWidthCodec.writeIntLEBuf(var1, this.current);
      return this;
   }

   public SerialByteBuffer writeLongBE(long var1) {
      this.ensureRemaining(8);
      this.current.putLong(var1);
      return this;
   }

   public SerialByteBuffer writeLongLE(long var1) {
      this.ensureRemaining(8);
      FixedWidthCodec.writeLongLEBuf(var1, this.current);
      return this;
   }

   public SerialByteBuffer writeVarInt(int var1) {
      byte[] var2 = new byte[5];
      int var3 = 0;

      while ((var1 & -128) != 0) {
         var2[var3++] = (byte)(var1 & 127 | 128);
         var1 >>>= 7;
      }

      var2[var3++] = (byte)var1;
      this.ensureRemaining(var3);
      this.current.put(var2, 0, var3);
      return this;
   }

   public SerialByteBuffer writeVarLong(long var1) {
      byte[] var3 = new byte[10];
      int var4 = 0;

      while ((var1 & -128L) != 0L) {
         var3[var4++] = (byte)((int)var1 & 127 | 128);
         var1 >>>= 7;
      }

      var3[var4++] = (byte)var1;
      this.ensureRemaining(var4);
      this.current.put(var3, 0, var4);
      return this;
   }

   public SerialByteBuffer writeDouble(double var1) {
      return this.writeLongBE(Double.doubleToRawLongBits(var1));
   }

   public SerialByteBuffer writeFloat(float var1) {
      return this.writeIntBE(Float.floatToRawIntBits(var1));
   }

   public SerialByteBuffer writeBytes(byte[] var1, int var2, int var3) {
      if (this.current.remaining() >= var3) {
         this.current.put(var1, var2, var3);
      } else {
         ByteBuffer var4 = ByteBuffer.wrap(var1, var2, var3);
         this.appendBuffer(var4);
      }

      return this;
   }

   public SerialByteBuffer writeBytes(byte[] var1) {
      return this.writeBytes(var1, 0, var1.length);
   }

   public SerialByteBuffer writeBuffer(ByteBuffer var1) {
      ByteBuffer var2 = var1.slice();
      if (this.current.remaining() >= var2.remaining()) {
         this.current.put(var2);
      } else {
         this.appendBuffer(var2);
      }

      return this;
   }
}
