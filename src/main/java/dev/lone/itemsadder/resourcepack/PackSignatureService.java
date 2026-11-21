package dev.lone.itemsadder.resourcepack;

import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;
import java.util.Base64.Encoder;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PSource.PSpecified;

public final class PackSignatureService {
   private static final String INSPECTION_PUBLIC_KEY = "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAsYekL2YGoSdp3FJGJ2eZf/kT+a+4rxmejJj7XfSCmNYcZlVQ52mz/B31VBxePGKHrTDs9N7iHDukrwbpdPYMKD2sKsz94acyqJYMYMkivce8Lx/GYySVXn18uKvjRI1q5NsZFTyhMtsR+UXEjLNsImMWccCNBi3Pi9bjKGBaxtX5jvxkdElqJmKo403sox5vvC84AjhdamYeLqIGO90xKgAqnII4yq41CafQhxgKmJsd0ieTqJpOf1Xyb2AHty58xAPeo2RH0GxJySLj6rXMkkbPrVGeLPUkFEiyWHMBsUwnVhuTrDPCXa3uTK/Na4ZV0sH8vIkE7TrxpLRy1BPceLDhfc9sP1OuT9orb2u3FLJFV1J3aNQsvoC7KFZlnPtpz3CzVeaoVcmm+67l06yZ49iPk1SAMswuDWqHCfnPrJXrmu4qifLK6xMJRCJdj/pMMh9GzDtok4ytv/JONygjhDWsp+DkWwT4kNU/1nvUcMBjDI9LrnAthopWUEjPgQVLAgMBAAE=";

   private PackSignatureService() {
   }

   public static String signPayload(PackIdentityFile var0, UUID var1, PackSignaturePayload var2) {
      return buildEncryptedPayload(
         var0,
         var1,
         var2,
         parsePublicKey(
            Base64.getDecoder()
               .decode(
                  "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAsYekL2YGoSdp3FJGJ2eZf/kT+a+4rxmejJj7XfSCmNYcZlVQ52mz/B31VBxePGKHrTDs9N7iHDukrwbpdPYMKD2sKsz94acyqJYMYMkivce8Lx/GYySVXn18uKvjRI1q5NsZFTyhMtsR+UXEjLNsImMWccCNBi3Pi9bjKGBaxtX5jvxkdElqJmKo403sox5vvC84AjhdamYeLqIGO90xKgAqnII4yq41CafQhxgKmJsd0ieTqJpOf1Xyb2AHty58xAPeo2RH0GxJySLj6rXMkkbPrVGeLPUkFEiyWHMBsUwnVhuTrDPCXa3uTK/Na4ZV0sH8vIkE7TrxpLRy1BPceLDhfc9sP1OuT9orb2u3FLJFV1J3aNQsvoC7KFZlnPtpz3CzVeaoVcmm+67l06yZ49iPk1SAMswuDWqHCfnPrJXrmu4qifLK6xMJRCJdj/pMMh9GzDtok4ytv/JONygjhDWsp+DkWwT4kNU/1nvUcMBjDI9LrnAthopWUEjPgQVLAgMBAAE="
               )
         ),
         null
      );
   }

   static String buildEncryptedPayload(PackIdentityFile var0, UUID var1, PackSignaturePayload var2, PublicKey var3, String var4) {
      if (var3 instanceof RSAPublicKey var5 && var5.getModulus().bitLength() >= 3072 && var5.getModulus().bitLength() <= 4096) {
         JsonObject var6 = new JsonObject();
         var6.addProperty("v", 2);
         var6.addProperty("fingerprint", var0.getIdentityHash());
         var6.addProperty("installationId", var0.id().toString());
         var6.addProperty("worldId", var1.toString());
         var6.addProperty("store", var2.store());
         var6.addProperty("buyerId", var2.buyerId());
         var6.addProperty("identitySource", var2.identitySource());
         var6.addProperty("identityVerified", false);
         var6.addProperty("configuredIp", var2.configuredIp());
         var6.addProperty("publicIp", var4);
         var6.addProperty("publicIpSource", var4 == null ? null : "beta.itemsadder.com (outbound connection)");
         var6.addProperty("publicIpStatus", var4 == null ? "not_collected" : "observed");
         var6.addProperty("observedAt", Instant.now().toString());
         var6.addProperty("pluginVersion", var2.pluginVersion());
         var6.addProperty("serverVersion", var2.serverVersion());
         String var7 = HexFormat.of().formatHex(PackIdentityFile.sha256Digest().digest(var3.getEncoded())).substring(0, 16);
         byte[] var8 = new byte[32];
         byte[] var9 = new byte[12];
         SecureRandom var10 = new SecureRandom();
         var10.nextBytes(var8);
         var10.nextBytes(var9);

         try {
            Cipher var11 = Cipher.getInstance("AES/GCM/NoPadding");
            var11.init(1, new SecretKeySpec(var8, "AES"), new GCMParameterSpec(128, var9));
            var11.updateAAD(("ia-fingerprint-v2\n" + var0.getIdentityHash() + "\n" + var7).getBytes(StandardCharsets.UTF_8));
            byte[] var12 = var11.doFinal(var6.toString().getBytes(StandardCharsets.UTF_8));
            Cipher var13 = Cipher.getInstance("RSA/ECB/OAEPPadding");
            var13.init(1, var3, new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSpecified.DEFAULT));
            Encoder var14 = Base64.getUrlEncoder().withoutPadding();
            JsonObject var15 = new JsonObject();
            var15.addProperty("v", 2);
            var15.addProperty("fingerprint", var0.getIdentityHash());
            var15.addProperty("keyId", var7);
            var15.addProperty("wrappedKey", var14.encodeToString(var13.doFinal(var8)));
            var15.addProperty("iv", var14.encodeToString(var9));
            var15.addProperty("ciphertext", var14.encodeToString(var12));
            JsonObject var16 = new JsonObject();
            var16.add("itemsadder_installation", var15);
            return var16.toString();
         } finally {
            Arrays.fill(var8, (byte)0);
         }
      } else {
         throw new GeneralSecurityException("Unsupported inspection key");
      }
   }

   private static PublicKey parsePublicKey(byte[] var0) {
      if (var0.length > 1024) {
         throw new GeneralSecurityException("Invalid inspection key");
      } else {
         PublicKey var1 = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(var0));
         if (var1 instanceof RSAPublicKey var2 && var2.getModulus().bitLength() >= 3072 && var2.getModulus().bitLength() <= 4096) {
            return var1;
         } else {
            throw new GeneralSecurityException("Invalid inspection key");
         }
      }
   }
}
