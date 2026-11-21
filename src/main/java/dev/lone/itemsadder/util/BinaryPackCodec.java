package dev.lone.itemsadder.util;

import dev.lone.itemsadder.resourcepack.GuiRect;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public final class BinaryPackCodec {
   static final int MAX_PNG_SIZE = 134217728;
   static final int MAX_CHUNK_SIZE = 67108864;
   static final int MAX_INFLATED_SIZE = 268435456;
   private static final byte[] PNG_SIGNATURE = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};
   private static final int IHDR_TYPE = chunkType("IHDR");
   private static final int PLTE_TYPE = chunkType("PLTE");
   private static final int IDAT_TYPE = chunkType("IDAT");
   private static final int IEND_TYPE = chunkType("IEND");
   private static final int MARKER_TYPE = chunkType("iaPR");
   private static final byte[] MARKER_PAYLOAD = new byte[]{73, 65, 80, 1};
   private static final int CRC_XOR_KEY = -1513885317;
   private static final byte[] PROTECTION_KEY = new byte[]{90, 107, 124, -115};

   private BinaryPackCodec() {
   }

   static String validatePngHeader(byte[] var0) {
      if (var0.length < 24) {
         return "truncated PNG header";
      } else if (!Arrays.equals(PNG_SIGNATURE, Arrays.copyOf(var0, PNG_SIGNATURE.length))) {
         return "invalid PNG signature";
      } else if (readInt32(var0, 8) != 13 || readInt32(var0, 12) != IHDR_TYPE) {
         return "IHDR must be the first PNG chunk";
      } else {
         return readUInt32(var0, 16) != 0L && readUInt32(var0, 20) != 0L ? null : "PNG width and height must be positive";
      }
   }

   static long getWidth(byte[] var0) {
      return readUInt32(var0, 16);
   }

   static long getHeight(byte[] var0) {
      return readUInt32(var0, 20);
   }

   static BinaryPackResult protect(byte[] var0) {
      return protectIfEnabled(var0, true);
   }

   static BinaryPackResult protectIfEnabled(byte[] var0, boolean var1) {
      Objects.requireNonNull(var0, "input");
      if (!var1) {
         return new BinaryPackResult(ImageOptimizeStatus.NOT_NEEDED, var0, "disabled");
      }

      if (var0.length > 134217728) {
         return new BinaryPackResult(ImageOptimizeStatus.UNSUPPORTED_FORMAT, var0, "PNG exceeds size limit");
      }

      try {
         BinaryPackPart var2 = scanChunks(var0);
         byte[] var3 = assembleImage(var0, var2);
         if (var2.XN()) {
            removeProtection(var3);
         }

         applyKey(var3, var2.XM());
         if (var2.XN()) {
            return new BinaryPackResult(ImageOptimizeStatus.ALREADY_PROCESSED, var0, "already processed");
         }

         byte[] var4 = (byte[])var0.clone();
         injectMarkerChunk(var4, var2);
         return new BinaryPackResult(ImageOptimizeStatus.APPLIED, finalizeOutput(var4, var2), "protected");
      } catch (BinaryPackException var5) {
         return new BinaryPackResult(var5.status, var0, var5.getMessage());
      }
   }

   private static BinaryPackPart scanChunks(byte[] var0) {
      if (var0.length >= PNG_SIGNATURE.length && Arrays.equals(PNG_SIGNATURE, Arrays.copyOf(var0, PNG_SIGNATURE.length))) {
         ArrayList var1 = new ArrayList();
         int var2 = PNG_SIGNATURE.length;
         boolean var3 = false;
         boolean var4 = false;
         boolean var5 = false;
         boolean var6 = false;
         boolean var7 = false;
         boolean var8 = false;
         long var9 = 0L;
         long var11 = -1L;
         int var13 = -1;
         int var14 = -1;

         while (var2 < var0.length) {
            if (var0.length - var2 < 12) {
               throw error("truncated PNG chunk");
            }

            long var15 = readUInt32(var0, var2);
            if (var15 > 67108864L) {
               throw unsupported("PNG chunk exceeds size limit");
            }

            int var17 = (int)var15;
            long var18 = var2 + 12L + var17;
            if (var18 > var0.length) {
               throw error("truncated PNG chunk");
            }

            int var20 = readInt32(var0, var2 + 4);
            validateChunkTypeBytes(var20);
            int var21 = var2 + 8;
            int var22 = var21 + var17;
            GuiRect var23 = new GuiRect(var20, var21, var17, var22);
            if (!var3 && var20 != IHDR_TYPE) {
               throw error("IHDR must be the first chunk");
            }

            if (var20 == IHDR_TYPE) {
               if (var3 || var17 != 13) {
                  throw error("invalid IHDR chunk");
               }

               var3 = true;
               long var24 = readUInt32(var0, var21);
               long var26 = readUInt32(var0, var21 + 4);
               var14 = var0[var21 + 8] & 255;
               var13 = var0[var21 + 9] & 255;
               var11 = computeInflatedSize(var24, var26, var14, var13, var0[var21 + 10] & 255, var0[var21 + 11] & 255, var0[var21 + 12] & 255);
            } else if (var20 == PLTE_TYPE) {
               if (var4 || var5 || var17 == 0 || var17 % 3 != 0 || var17 > 768) {
                  throw error("invalid PLTE chunk");
               }

               var4 = true;
               if (var13 == 3 && var17 / 3 > 1 << var14) {
                  throw error("PLTE has too many entries");
               }

               if (var13 == 0 || var13 == 4) {
                  throw error("PLTE is not allowed for this color type");
               }
            } else if (var20 == IDAT_TYPE) {
               if (var6) {
                  throw unsupported("non-consecutive IDAT chunks");
               }

               var5 = true;
               var9 += var17;
               if (var9 > 134217728L) {
                  throw unsupported("IDAT payload exceeds size limit");
               }
            } else if (var20 == MARKER_TYPE) {
               if (var8 || !var5 || var17 != MARKER_PAYLOAD.length || !matchesAt(var0, var21, MARKER_PAYLOAD)) {
                  throw unsupported("unsupported ItemsAdder PNG marker");
               }

               var8 = true;
               var6 = true;
            } else if (var20 == IEND_TYPE) {
               if (var7 || !var5 || var17 != 0) {
                  throw error("invalid IEND chunk");
               }

               var7 = true;
               var6 = true;
            } else {
               if (isCriticalChunk(var20)) {
                  throw unsupported("unknown critical PNG chunk " + chunkName(var20));
               }

               if (var5) {
                  var6 = true;
               }
            }

            var1.add(var23);
            var2 = (int)var18;
            if (var7 && var2 != var0.length) {
               throw error("data found after IEND");
            }
         }

         if (!var3 || !var5 || !var7 || var9 < 6L) {
            throw error("missing required PNG chunks");
         }

         if (var13 == 3 && !var4) {
            throw error("indexed PNG is missing PLTE");
         }

         for (GuiRect var16 : var1) {
            int var29 = readUnsignedBytes(var0, var16.dataOffset() - 4, var16.length() + 4);
            int var30 = readInt32(var0, var16.crcOffset());
            int var19 = var8 && var16.type() != MARKER_TYPE ? var29 ^ -1513885317 : var29;
            if (var30 != var19) {
               throw error("invalid CRC for " + chunkName(var16.type()));
            }
         }

         return new BinaryPackPart(List.copyOf(var1), (int)var9, (int)var11, var8);
      } else {
         throw error("invalid PNG signature");
      }
   }

   private static long computeInflatedSize(long var0, long var2, int var4, int var5, int var6, int var7, int var8) {
      if (var0 != 0L && var2 != 0L && var6 == 0 && var7 == 0 && (var8 == 0 || var8 == 1)) {
         byte var9 = switch (var5) {
            case 0, 3 -> 1;
            default -> throw unsupported("unsupported PNG color type");
            case 2 -> 3;
            case 4 -> 2;
            case 6 -> 4;
         };

         boolean var10 = switch (var5) {
            case 0 -> var4 == 1 || var4 == 2 || var4 == 4 || var4 == 8 || var4 == 16;
            case 3 -> var4 == 1 || var4 == 2 || var4 == 4 || var4 == 8;
            default -> var4 == 8 || var4 == 16;
         };
         if (!var10) {
            throw unsupported("unsupported PNG bit depth");
         }

         long var11 = (long)var9 * var4;
         long var13;
         if (var8 == 0) {
            var13 = inflatedSizeForPass(var0, var2, var11);
         } else {
            int[] var15 = new int[]{0, 4, 0, 2, 0, 1, 0};
            int[] var16 = new int[]{0, 0, 4, 0, 2, 0, 1};
            int[] var17 = new int[]{8, 8, 4, 4, 2, 2, 1};
            int[] var18 = new int[]{8, 8, 8, 4, 4, 2, 2};
            var13 = 0L;

            try {
               for (int var19 = 0; var19 < 7; var19++) {
                  long var20 = passDimension(var0, var15[var19], var17[var19]);
                  long var22 = passDimension(var2, var16[var19], var18[var19]);
                  if (var20 != 0L && var22 != 0L) {
                     var13 = Math.addExact(var13, inflatedSizeForPass(var20, var22, var11));
                  }
               }
            } catch (ArithmeticException var24) {
               throw unsupported("PNG dimensions exceed size limit");
            }
         }

         if (var13 > 268435456L) {
            throw unsupported("inflated PNG exceeds size limit");
         } else {
            return var13;
         }
      } else {
         throw unsupported("unsupported IHDR layout");
      }
   }

   private static long inflatedSizeForPass(long var0, long var2, long var4) {
      try {
         long var6 = Math.addExact(Math.multiplyExact(var0, var4), 7L) / 8L;
         return Math.multiplyExact(Math.addExact(var6, 1L), var2);
      } catch (ArithmeticException var8) {
         throw unsupported("PNG dimensions exceed size limit");
      }
   }

   private static long passDimension(long var0, int var2, int var3) {
      return var0 <= var2 ? 0L : (var0 - var2 + var3 - 1L) / var3;
   }

   private static byte[] assembleImage(byte[] var0, BinaryPackPart var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(var1.XL());

      for (GuiRect var4 : var1.XK()) {
         if (var4.type() == IDAT_TYPE) {
            var2.write(var0, var4.dataOffset(), var4.length());
         }
      }

      return var2.toByteArray();
   }

   private static void applyKey(byte[] var0, int var1) {
      Inflater var2 = new Inflater();
      byte[] var3 = new byte[8192];
      int var4 = 0;

      try {
         var2.setInput(var0);

         while (!var2.finished()) {
            int var5 = var2.inflate(var3);
            var4 = Math.addExact(var4, var5);
            if (var4 > var1) {
               throw error("inflated PNG data is larger than expected");
            }

            if (var5 == 0) {
               throw error(var2.needsDictionary() ? "PNG requires a zlib dictionary" : "incomplete PNG zlib stream");
            }
         }

         if (var2.getRemaining() != 0 || var4 != var1) {
            throw error("PNG zlib stream length does not match IHDR");
         }
      } catch (DataFormatException | ArithmeticException var9) {
         throw error("invalid PNG zlib stream");
      } finally {
         var2.end();
      }
   }

   private static void injectMarkerChunk(byte[] var0, BinaryPackPart var1) {
      int var2 = 0;
      int var3 = var1.XL() - PROTECTION_KEY.length;

      for (GuiRect var5 : var1.XK()) {
         if (var5.type() == IDAT_TYPE) {
            for (int var6 = 0; var6 < var5.length(); var2++) {
               if (var2 >= var3) {
                  var0[var5.dataOffset() + var6] ^= PROTECTION_KEY[var2 - var3];
               }

               var6++;
            }
         }
      }
   }

   private static void removeProtection(byte[] var0) {
      for (int var1 = 0; var1 < PROTECTION_KEY.length; var1++) {
         var0[var0.length - PROTECTION_KEY.length + var1] = (byte)(var0[var0.length - PROTECTION_KEY.length + var1] ^ PROTECTION_KEY[var1]);
      }
   }

   private static byte[] finalizeOutput(byte[] var0, BinaryPackPart var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(var0.length + 16);
      var2.write(var0, 0, PNG_SIGNATURE.length);

      for (GuiRect var4 : var1.XK()) {
         if (var4.type() == IEND_TYPE) {
            writeChunk(var2, MARKER_TYPE, MARKER_PAYLOAD);
         }

         writeInt32(var2, var4.length());
         var2.write(var0, var4.dataOffset() - 4, var4.length() + 4);
         writeInt32(var2, readUnsignedBytes(var0, var4.dataOffset() - 4, var4.length() + 4) ^ -1513885317);
      }

      return var2.toByteArray();
   }

   private static void writeChunk(ByteArrayOutputStream var0, int var1, byte[] var2) {
      writeInt32(var0, var2.length);
      byte[] var3 = new byte[]{(byte)(var1 >>> 24), (byte)(var1 >>> 16), (byte)(var1 >>> 8), (byte)var1};
      var0.writeBytes(var3);
      var0.writeBytes(var2);
      CRC32 var4 = new CRC32();
      var4.update(var3);
      var4.update(var2);
      writeInt32(var0, (int)var4.getValue());
   }

   private static void validateChunkTypeBytes(int var0) {
      for (byte var1 = 24; var1 >= 0; var1 -= 8) {
         int var2 = var0 >>> var1 & 0xFF;
         if (!isAsciiLetter(var2)) {
            throw error("invalid PNG chunk type");
         }
      }

      if ((var0 >>> 8 & 32) != 0) {
         throw error("invalid PNG reserved chunk bit");
      }
   }

   private static boolean isCriticalChunk(int var0) {
      return (var0 >>> 24 & 32) == 0;
   }

   private static boolean isAsciiLetter(int var0) {
      return var0 >= 65 && var0 <= 90 || var0 >= 97 && var0 <= 122;
   }

   private static boolean matchesAt(byte[] var0, int var1, byte[] var2) {
      if (var1 >= 0 && var1 + var2.length <= var0.length) {
         for (int var3 = 0; var3 < var2.length; var3++) {
            if (var0[var1 + var3] != var2[var3]) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static int readUnsignedBytes(byte[] var0, int var1, int var2) {
      CRC32 var3 = new CRC32();
      var3.update(var0, var1, var2);
      return (int)var3.getValue();
   }

   private static long readUInt32(byte[] var0, int var1) {
      return Integer.toUnsignedLong(readInt32(var0, var1));
   }

   private static int readInt32(byte[] var0, int var1) {
      return (var0[var1] & 0xFF) << 24 | (var0[var1 + 1] & 0xFF) << 16 | (var0[var1 + 2] & 0xFF) << 8 | var0[var1 + 3] & 0xFF;
   }

   private static void writeInt32(ByteArrayOutputStream var0, int var1) {
      var0.write(var1 >>> 24);
      var0.write(var1 >>> 16);
      var0.write(var1 >>> 8);
      var0.write(var1);
   }

   private static int chunkType(String var0) {
      return var0.charAt(0) << 24 | var0.charAt(1) << 16 | var0.charAt(2) << 8 | var0.charAt(3);
   }

   private static String chunkName(int var0) {
      return new String(new byte[]{(byte)(var0 >>> 24), (byte)(var0 >>> 16), (byte)(var0 >>> 8), (byte)var0});
   }

   private static BinaryPackException error(String var0) {
      return new BinaryPackException(ImageOptimizeStatus.INVALID_PNG, var0);
   }

   private static BinaryPackException unsupported(String var0) {
      return new BinaryPackException(ImageOptimizeStatus.UNSUPPORTED_FORMAT, var0);
   }
}
