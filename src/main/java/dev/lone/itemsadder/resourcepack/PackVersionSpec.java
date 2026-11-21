package dev.lone.itemsadder.resourcepack;

public class PackVersionSpec {
   private final String version;
   private final int protocol;
   private final int major;
   private final int minor;

   public PackVersionSpec(String var1, int var2, int var3, int var4) {
      this.version = var1;
      this.protocol = var2;
      this.major = var3;
      this.minor = var4;
   }

   public String directoryName() {
      return "ia_humanoid_equipment_" + this.protocol;
   }

   PackVersion toPackVersion() {
      return new PackVersion(this.major, this.minor);
   }
}
