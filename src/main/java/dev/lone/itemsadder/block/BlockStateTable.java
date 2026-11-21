package dev.lone.itemsadder.block;

import dev.lone.itemsadder.resourcepack.ByteNamedKey;
import it.unimi.dsi.fastutil.ints.Int2ByteMap;
import it.unimi.dsi.fastutil.ints.Int2ByteOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

public final class BlockStateTable {
   private final List jc = new ObjectArrayList(16);
   private final Int2IntMap jd = new Int2IntOpenHashMap(64);
   private final Int2ByteMap je = new Int2ByteOpenHashMap(64);
   private volatile BlockStateSnapshot jf;

   public static int packLocalKey(int var0, int var1, int var2) {
      if ((var0 & -16) == 0 && (var2 & -16) == 0) {
         int var3 = var1 & 65535;
         return var0 & 15 | (var2 & 15) << 4 | var3 << 8;
      } else {
         throw new IllegalArgumentException("x,z must be 0..15");
      }
   }

   public static int unpackX(int var0) {
      return var0 & 15;
   }

   public static int unpackZ(int var0) {
      return var0 >>> 4 & 15;
   }

   public static int unpackY(int var0) {
      return (short)(var0 >>> 8);
   }

   BlockStateTable() {
      this.jd.defaultReturnValue(-1);
      this.je.defaultReturnValue((byte)0);
   }

   private int internStateName(String var1) {
      int var2 = this.jc.indexOf(var1);
      if (var2 >= 0) {
         return var2;
      }

      this.jc.add(var1);
      return this.jc.size() - 1;
   }

   public void setState(int var1, int var2, int var3, String var4, byte var5) {
      int var6 = packLocalKey(var1, var2, var3);
      int var7 = this.internStateName(var4);
      this.jd.put(var6, var7);
      this.je.put(var6, (byte)(var5 & 0xFF));
      this.jf = null;
   }

   public void setStateAt(Block var1, String var2, byte var3) {
      int var4 = var1.getX() & 15;
      int var5 = var1.getY();
      int var6 = var1.getZ() & 15;
      this.setState(var4, var5, var6, var2, var3);
   }

   public boolean removeState(int var1, int var2, int var3) {
      int var4 = packLocalKey(var1, var2, var3);
      boolean var5 = this.jd.remove(var4) != -1;
      this.je.remove(var4);
      if (var5) {
         this.jf = null;
      }

      return var5;
   }

   @Nullable
   public ByteNamedKey getStateKey(int var1, int var2, int var3) {
      int var4 = packLocalKey(var1, var2, var3);
      int var5 = this.jd.get(var4);
      if (var5 == -1) {
         return null;
      }

      String var6 = (String)this.jc.get(var5);
      byte var7 = this.je.getOrDefault(var4, (byte)0);
      return new ByteNamedKey(var6, var7);
   }

   public ByteNamedKey getStateKeyAt(Block var1) {
      int var2 = var1.getX() & 15;
      int var3 = var1.getY();
      int var4 = var1.getZ() & 15;
      return this.getStateKey(var2, var3, var4);
   }

   public int size() {
      return this.jd.size();
   }

   public List getStateNames() {
      return Collections.unmodifiableList(this.jc);
   }

   public void compact() {
      BitSet var1 = new BitSet();
      IntIterator var2 = this.jd.values().iterator();

      while (var2.hasNext()) {
         int var3 = (Integer)var2.next();
         var1.set(var3);
      }

      if (var1.length() != this.jc.size()) {
         int[] var7 = new int[this.jc.size()];
         ArrayList var8 = new ArrayList();

         for (int var4 = 0; var4 < this.jc.size(); var4++) {
            if (var1.get(var4)) {
               var7[var4] = var8.size();
               var8.add((String)this.jc.get(var4));
            } else {
               var7[var4] = -1;
            }
         }

         ObjectIterator var9 = this.jd.int2IntEntrySet().iterator();

         while (var9.hasNext()) {
            Entry var5 = (Entry)var9.next();
            int var6 = var7[var5.getValue()];
            if (var6 < 0) {
               throw new IllegalStateException("Remap produced gap");
            }

            var5.setValue(var6);
         }

         this.jc.clear();
         this.jc.addAll(var8);
         this.jf = null;
      }
   }

   public byte[] serialize() {
      try (
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         DataOutputStream var2 = new DataOutputStream(var1);
      ) {
         var2.writeByte(1);
         var2.writeInt(this.jc.size());

         for (String var4 : this.jc) {
            var2.writeUTF(var4);
         }

         var2.writeInt(this.jd.size());
         IntIterator var12 = this.jd.keySet().iterator();

         while (var12.hasNext()) {
            int var14 = (Integer)var12.next();
            int var5 = this.jd.get(var14);
            byte var6 = this.je.getOrDefault(var14, (byte)0);
            var2.writeInt(var14);
            var2.writeShort(var5 & 65535);
            var2.writeByte(var6 & 255);
         }

         return var1.toByteArray();
      } catch (IOException var11) {
         throw new IllegalStateException("Serialize chunk crops failed", var11);
      }
   }

   public static BlockStateTable deserialize(byte[] var0) {
      try (DataInputStream var1 = new DataInputStream(new ByteArrayInputStream(var0))) {
         int var2 = var1.readUnsignedByte();
         if (var2 != 1) {
            throw new IllegalStateException("Unsupported version " + var2);
         }

         BlockStateTable var3 = new BlockStateTable();
         int var4 = var1.readInt();

         for (int var5 = 0; var5 < var4; var5++) {
            var3.jc.add(var1.readUTF());
         }

         int var13 = var1.readInt();

         for (int var6 = 0; var6 < var13; var6++) {
            int var7 = var1.readInt();
            int var8 = var1.readUnsignedShort();
            byte var9 = (byte)var1.readUnsignedByte();
            var3.jd.put(var7, var8);
            var3.je.put(var7, var9);
         }

         return var3;
      } catch (IOException var12) {
         throw new IllegalStateException("Deserialize chunk crops failed", var12);
      }
   }

   public BlockStateSnapshot snapshot() {
      BlockStateSnapshot var1 = this.jf;
      if (var1 != null) {
         return var1;
      }

      ArrayList var2 = new ArrayList(this.jd.size());
      IntIterator var3 = this.jd.keySet().iterator();

      while (var3.hasNext()) {
         int var4 = (Integer)var3.next();
         int var5 = this.jd.get(var4);
         String var6 = (String)this.jc.get(var5);
         byte var7 = this.je.getOrDefault(var4, (byte)0);
         var2.add(new ChunkLocalBlock(unpackX(var4), unpackY(var4), unpackZ(var4), var6, var7));
      }

      this.jf = var1 = new BlockStateSnapshot(Collections.unmodifiableList(var2));
      return var1;
   }
}
