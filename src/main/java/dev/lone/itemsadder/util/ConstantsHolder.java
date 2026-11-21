package dev.lone.itemsadder.util;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.YamlConfig;

public class ConstantsHolder {
   public static String apiKey;

   public static void init() {
      YamlConfig var0 = new YamlConfig(Main.getInstance(), true, "secret.yml", true, true);
      apiKey = var0.getString("lobfile.api_key");
   }
}
