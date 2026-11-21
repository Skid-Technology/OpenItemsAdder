package dev.lone.itemsadder.config;

import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;

public class YamlConfigurationBuilder {
   public static YamlConfiguration method7477(ConfigSectionWrapper var0) {
      YamlConfiguration var1 = new YamlConfiguration();
      Map var2 = var0.bK(true);
      var2.forEach((var1x, var2x) -> var1.set(var1x, var2x));
      String var3 = var1.saveToString();
      YamlConfiguration var4 = new YamlConfiguration();
      var4.loadFromString(var3);
      return var4;
   }
}
