package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.config.Settings;
import java.util.Arrays;
import java.util.List;

public enum PackFormat {
   V1_20_5_TO_1_21_1("1_20_5_to_1_21_1", new int[]{32, 34}, 766, 767),
   V1_21_2_TO_1_21_3("1_21_2_to_1_21_3", new int[]{42}, 768),
   field23("1_21_4", new int[]{46}, 769),
   field24("1_21_5", new int[]{55}, 770),
   field25("1_21_6_to_1_21_10", new int[]{63, 64, 69}, 771, 772, 773),
   field26("1_21_11", new int[]{75}, 774),
   V26_1("26_1", new int[]{84}, 775),
   V26_2("26_2", new int[]{88}, 776),
   V26_3("26_3", new PackVersion[]{new PackVersion(97, 1)}, 777);

   private final String key;
   private final List formats;
   private final int[] protocols;

   PackFormat(String var3, int[] var4, int... var5) {
      this.key = var3;
      this.formats = Arrays.stream(var4).mapToObj(var0 -> new PackVersion(var0, 0)).toList();
      this.protocols = var5;
   }

   PackFormat(String var3, PackVersion[] var4, int... var5) {
      this.key = var3;
      this.formats = List.of(var4);
      this.protocols = var5;
   }

   public String getKey() {
      return this.key;
   }

   List getFormats() {
      return this.formats;
   }

   boolean isEnabledFor(Settings var1) {
      return var1.perClientVersionDelivery && var1.enabledClientVersions.contains(this.key);
   }

   static PackFormat fromProtocol(int var0) {
      for (PackFormat var4 : values()) {
         for (int var8 : var4.protocols) {
            if (var8 == var0) {
               return var4;
            }
         }
      }

      return null;
   }
}
