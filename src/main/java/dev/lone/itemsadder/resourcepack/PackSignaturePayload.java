package dev.lone.itemsadder.resourcepack;

public class PackSignaturePayload {
   private final String store;
   private final String buyerId;
   private final String identitySource;
   private final String configuredIp;
   private final String pluginVersion;
   private final String serverVersion;

   public PackSignaturePayload(String var1, String var2, String var3, String var4, String var5, String var6) {
      this.store = var1;
      this.buyerId = var2;
      this.identitySource = var3;
      this.configuredIp = var4;
      this.pluginVersion = var5;
      this.serverVersion = var6;
   }
}
