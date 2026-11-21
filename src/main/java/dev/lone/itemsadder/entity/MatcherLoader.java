package dev.lone.itemsadder.entity;

import dev.lone.itemsadder.config.ConfigSectionWrapper;
import dev.lone.itemsadder.config.YamlConfig;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface MatcherLoader {
   @Nullable
   EntityMatcherBase load(String var1, CustomMobSettings var2, ConfigSectionWrapper var3, String var4, YamlConfig var5);
}
