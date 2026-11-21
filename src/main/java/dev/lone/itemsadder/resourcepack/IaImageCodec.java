package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.util.json.JsonNode;
import dev.lone.itemsadder.util.json.SafeJsonValidator;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

public final class IaImageCodec {
   private static final byte[] auO = new byte[]{73, 65, 80, 49};
   private static final int auP = 12;
   private static final int auQ = 16777216;
   private static final int auR = 16777228;
   private static final int auS = 4194304;
   private static final int auT = 64;
   private static final int auU = 64;

   private IaImageCodec() {
   }

   public static byte[] encodeBundle(JSONObject var0) {
      validateBundle(var0);
      byte[] var1 = canonicalJson(var0).getBytes(StandardCharsets.UTF_8);
      if (var1.length > 16777216) {
         throw invalidBundle("JSON too large");
      }

      ByteBuffer var2 = ByteBuffer.allocate(12 + var1.length).order(ByteOrder.LITTLE_ENDIAN);
      var2.put(auO).putShort((short)1).putShort((short)0).putInt(var1.length).put(var1);
      return var2.array();
   }

   public static JSONObject decodeBundle(byte[] var0) {
      if (var0 != null && var0.length >= 12 && var0.length <= 16777228) {
         ByteBuffer var1 = ByteBuffer.wrap(var0).order(ByteOrder.LITTLE_ENDIAN);

         for (byte var5 : auO) {
            if (var1.get() != var5) {
               throw invalidBundle("bad magic");
            }
         }

         if (Short.toUnsignedInt(var1.getShort()) == 1 && Short.toUnsignedInt(var1.getShort()) == 0) {
            int var8 = var1.getInt();
            if (var8 >= 0 && var8 <= 16777216 && var8 == var1.remaining()) {
               byte[] var9 = new byte[var8];
               var1.get(var9);

               try {
                  String var10 = StandardCharsets.UTF_8
                     .newDecoder()
                     .onMalformedInput(CodingErrorAction.REPORT)
                     .onUnmappableCharacter(CodingErrorAction.REPORT)
                     .decode(ByteBuffer.wrap(var9))
                     .toString();
                  runJsonValidator(var10);
                  JSONObject var11 = JSON.parseObject(var10);
                  if (var11 == null) {
                     throw invalidBundle("document must be an object");
                  }

                  validateBundle(var11);
                  return var11;
               } catch (CharacterCodingException var6) {
                  throw invalidBundle("invalid UTF-8");
               } catch (RuntimeException var7) {
                  if (var7 instanceof IllegalArgumentException) {
                     throw var7;
                  } else {
                     throw invalidBundle("invalid JSON");
                  }
               }
            } else {
               throw invalidBundle("invalid JSON length or trailing data");
            }
         } else {
            throw invalidBundle("unsupported version or flags");
         }
      } else {
         throw invalidBundle("invalid file length");
      }
   }

   public static void validateBundle(JSONObject var0) {
      if (var0 == null) {
         throw invalidBundle("document must be an object");
      }

      checkJsonLimits(var0);
      requireKeys(var0, Set.of("profile", "root", "effects", "textures"), "document");
      if (!"java-display-1".equals(var0.getString("profile"))) {
         throw invalidBundle("unsupported profile");
      }

      String var1 = requireNamespacedId(var0.get("root"));
      JSONObject var2 = requireObject(var0.get("effects"), "effects");
      JSONObject var3 = requireObject(var0.get("textures"), "textures");
      if (!var2.isEmpty() && var2.size() <= 64) {
         if (var3.size() > 64) {
            throw invalidBundle("invalid texture count");
         }

         HashMap var4 = new HashMap();
         HashSet var5 = new HashSet();

         for (String var7 : var2.keySet()) {
            requireNamespacedId(var7);
            JSONObject var8 = requireObject(var2.get(var7), "effect " + var7);
            if (var8.keySet().contains("source")
               && var8.keySet().contains("texture")
               && !var8.keySet().stream().anyMatch(var0x -> !Set.of("source", "texture", "display", "variants").contains(var0x))) {
               JSONObject var9 = requireObject(var8.get("source"), "source " + var7);
               if (var9.get("format_version") instanceof String var10 && !var10.isEmpty()) {
                  requireObject(var9.get("particle_effect"), "particle_effect " + var7);
                  JSONObject var26 = var9.getJSONObject("particle_effect");
                  JSONObject var12 = requireObject(var26.get("description"), "description " + var7);
                  requireObject(var26.get("components"), "components " + var7);
                  if (var26.containsKey("events")) {
                     requireObject(var26.get("events"), "events " + var7);
                  }

                  if (var8.containsKey("display")) {
                     requireObject(var8.get("display"), "display " + var7);
                  }

                  if (!var7.equals(var12.getString("identifier"))) {
                     throw invalidBundle("source identifier mismatch");
                  }

                  String var13 = requireSha256(var8.get("texture"));
                  var5.add(var13);
                  if (var8.containsKey("variants")) {
                     if (!(var8.get("variants") instanceof JSONArray var14) || var14.isEmpty() || var14.size() > 64) {
                        throw invalidBundle("texture variants must contain 1..64 entries");
                     }

                     int var32 = 0;

                     while (var32 < var14.size()) {
                        JSONObject var16 = requireObject(var14.get(var32), "texture variant");
                        if (!var16.keySet().containsAll(Set.of("texture", "weight"))
                           || var16.keySet().stream().anyMatch(var0x -> !Set.of("texture", "weight", "name").contains(var0x))) {
                           throw invalidBundle("invalid texture variant keys");
                        }

                        String var17 = requireSha256(var16.get("texture"));
                        var5.add(var17);
                        if (var32 == 0 && !var17.equals(var13)) {
                           throw invalidBundle("first variant must match the primary texture");
                        }

                        if (var16.get("weight") instanceof Number var18
                           && Double.isFinite(var18.doubleValue())
                           && !(var18.doubleValue() <= 0.0)
                           && !(var18.doubleValue() > 1000000.0)) {
                           if (!var16.containsKey("name") || var16.get("name") instanceof String var33 && var33.length() <= 128) {
                              var32++;
                              continue;
                           }

                           throw invalidBundle("invalid texture name");
                        }

                        throw invalidBundle("texture weight must be positive and at most 1000000");
                     }
                  }

                  var4.put(var7, collectParticleRefs(var26.get("events")));
                  continue;
               }

               throw invalidBundle("missing Bedrock format_version");
            }

            throw invalidBundle("invalid effect keys");
         }

         long var22 = 0L;
         long var23 = 0L;

         for (String var27 : var3.keySet()) {
            requireSha256(var27);
            if (!(var3.get(var27) instanceof String var30)) {
               throw invalidBundle("texture must be base64");
            }

            var22 += var30.length();
            if (var22 > 12582912L) {
               throw invalidBundle("textures too large");
            }

            byte[] var31;
            try {
               var31 = Base64.getDecoder().decode(var30);
            } catch (IllegalArgumentException var21) {
               throw invalidBundle("invalid base64");
            }

            if (var31.length > 4194304 || !Base64.getEncoder().encodeToString(var31).equals(var30)) {
               throw invalidBundle("noncanonical or oversized base64");
            }

            if (!sha256HexOf(var31).equals(var27)) {
               throw invalidBundle("texture digest mismatch");
            }

            var23 = countDecodedPixels(var31, var23);
         }

         if (!var5.equals(var3.keySet())) {
            throw invalidBundle("missing or unused texture");
         }

         if (!var2.containsKey(var1)) {
            throw invalidBundle("missing root effect");
         }

         HashSet var25 = new HashSet();
         HashSet var28 = new HashSet();
         computeEffectDepth(var1, var4, var2.keySet(), var25, var28, new HashMap());
         if (!var28.equals(var2.keySet())) {
            throw invalidBundle("unreachable effect");
         }
      } else {
         throw invalidBundle("invalid effect count");
      }
   }

   private static int computeEffectDepth(String var0, Map var1, Set var2, Set var3, Set var4, Map var5) {
      if (var3.contains(var0)) {
         throw invalidBundle("effect cycle");
      }

      if (var4.contains(var0)) {
         return (Integer)var5.get(var0);
      }

      var3.add(var0);
      int var6 = 1;

      for (String var8 : (Set)var1.get(var0)) {
         if (!var2.contains(var8)) {
            throw invalidBundle("missing referenced effect " + var8);
         }

         var6 = Math.max(var6, 1 + computeEffectDepth(var8, var1, var2, var3, var4, var5));
      }

      var3.remove(var0);
      var4.add(var0);
      if (var6 > 16) {
         throw invalidBundle("effect depth exceeds 16");
      }

      var5.put(var0, var6);
      return var6;
   }

   private static Set collectParticleRefs(Object var0) {
      HashSet var1 = new HashSet();
      if (var0 == null) {
         return var1;
      }

      ArrayDeque var2 = new ArrayDeque();
      var2.push(var0);

      while (!var2.isEmpty()) {
         Object var3 = var2.pop();
         if (var3 instanceof Map var4) {
            if (var4.containsKey("particle_effect")) {
               if (!(var4.get("particle_effect") instanceof Map var12) || !(var12.get("effect") instanceof String var8)) {
                  throw invalidBundle("malformed particle_effect event reference");
               }

               var1.add(requireNamespacedId(var8));
            }

            for (Object var13 : var4.values()) {
               if (var13 != null) {
                  var2.push(var13);
               }
            }
         } else if (var3 instanceof Iterable) {
            for (Object var7 : (Iterable)var3) {
               if (var7 != null) {
                  var2.push(var7);
               }
            }
         }
      }

      return var1;
   }

   private static long countDecodedPixels(byte[] var0, long var1) {
      try (MemoryCacheImageInputStream var3 = new MemoryCacheImageInputStream(new ByteArrayInputStream(var0))) {
         Iterator var21 = ImageIO.getImageReaders(var3);
         if (!var21.hasNext()) {
            throw invalidBundle("invalid PNG");
         }

         ImageReader var5 = (ImageReader)var21.next();

         try {
            var5.setInput(var3, true, true);
            if (!"png".equalsIgnoreCase(var5.getFormatName())) {
               throw invalidBundle("texture is not PNG");
            } else {
               int var6 = var5.getWidth(0);
               int var7 = var5.getHeight(0);
               if (var6 < 1 || var7 < 1 || var6 > 4096 || var7 > 4096 || (long)var6 * var7 > 16777216L) {
                  throw invalidBundle("PNG dimensions out of range");
               } else {
                  long var8 = var1 + (long)var6 * var7;
                  if (var8 > 16777216L) {
                     throw invalidBundle("decoded pixels too large");
                  } else if (var5.read(0) == null) {
                     throw invalidBundle("invalid PNG");
                  } else {
                     return var8;
                  }
               }
            }
         } finally {
            var5.dispose();
         }
      } catch (Exception var20) {
         if (var20 instanceof IllegalArgumentException var4) {
            throw var4;
         } else {
            throw invalidBundle("invalid PNG");
         }
      }
   }

   private static String requireNamespacedId(Object var0) {
      if (var0 instanceof String var1 && var1.matches("[a-z0-9_-]{1,64}:[a-z0-9_/-]{1,128}")) {
         String var2 = var1.substring(var1.indexOf(58) + 1);

         for (String var6 : var2.split("/", -1)) {
            if (var6.isEmpty() || var6.equals(".") || var6.equals("..")) {
               throw invalidBundle("invalid ID path");
            }
         }

         return var1;
      } else {
         throw invalidBundle("invalid namespaced ID");
      }
   }

   private static String requireSha256(Object var0) {
      if (var0 instanceof String var1 && var1.matches("[0-9a-f]{64}")) {
         return var1;
      } else {
         throw invalidBundle("invalid SHA-256");
      }
   }

   private static JSONObject requireObject(Object var0, String var1) {
      if (var0 instanceof JSONObject var2) {
         return var2;
      } else {
         throw invalidBundle(var1 + " must be an object");
      }
   }

   private static void requireKeys(JSONObject var0, Set var1, String var2) {
      if (!var0.keySet().equals(var1)) {
         throw invalidBundle("invalid " + var2 + " keys");
      }
   }

   private static String sha256HexOf(byte[] var0) {
      try {
         return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var0));
      } catch (Exception var2) {
         throw new AssertionError(var2);
      }
   }

   private static String canonicalJson(Object var0) {
      if (var0 != null && !(var0 instanceof Boolean) && !(var0 instanceof String) && !(var0 instanceof Number)) {
         if (var0 instanceof Map var6) {
            TreeMap var7 = new TreeMap();
            var6.forEach((var1x, var2x) -> {
               if (!(var1x instanceof String)) {
                  throw invalidBundle("invalid JSON key");
               }

               var7.put(var1x, var2x);
            });
            ArrayList var8 = new ArrayList();
            var7.forEach((var1x, var2x) -> var8.add(JSON.toJSONString(var1x) + ":" + canonicalJson(var2x)));
            return "{" + String.join(",", var8) + "}";
         } else if (!(var0 instanceof Iterable var5)) {
            throw invalidBundle("unsupported JSON value");
         } else {
            ArrayList var2 = new ArrayList();

            for (Object var4 : var5) {
               var2.add(canonicalJson(var4));
            }

            return "[" + String.join(",", var2) + "]";
         }
      } else if (var0 instanceof Number var1 && !Double.isFinite(var1.doubleValue())) {
         throw invalidBundle("non-finite number");
      } else {
         return JSON.toJSONString(var0);
      }
   }

   private static void checkJsonLimits(Object var0) {
      Object var1 = ((Map)var0).get("effects");
      ArrayDeque var2 = new ArrayDeque();
      var2.push(new JsonNode(var1, 0));
      int var3 = 0;

      while (!var2.isEmpty()) {
         JsonNode var4 = (JsonNode)var2.pop();
         if (++var3 > 100000 || var4.depth > 64) {
            throw invalidBundle("JSON structure exceeds limits");
         }

         Object var5 = var4.value;
         if (var5 instanceof Map var6) {
            for (Entry var11 : var6.entrySet()) {
               if (!(var11.getKey() instanceof String var12)) {
                  throw invalidBundle("JSON key is not a string");
               }

               if (var12.equals("__proto__") || var12.equals("prototype") || var12.equals("constructor")) {
                  throw invalidBundle("unsafe object key");
               }

               if (var12.length() > 16384) {
                  throw invalidBundle("string too long");
               }

               var2.push(new JsonNode(var11.getValue(), var4.depth + 1));
            }
         } else if (var5 instanceof Iterable) {
            for (Object var15 : (Iterable)var5) {
               var2.push(new JsonNode(var15, var4.depth + 1));
            }
         } else {
            if (var5 instanceof String var8 && var8.length() > 16384) {
               throw invalidBundle("string too long");
            }

            if (var5 instanceof Number var9 && !Double.isFinite(var9.doubleValue())) {
               throw invalidBundle("non-finite number");
            }
         }
      }
   }

   private static void runJsonValidator(String var0) {
      new SafeJsonValidator(var0).run();
   }

   private static IllegalArgumentException invalidBundle(String var0) {
      return new IllegalArgumentException("Invalid IAP1 bundle: " + var0);
   }
}
