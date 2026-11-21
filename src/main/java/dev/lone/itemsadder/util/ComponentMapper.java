package dev.lone.itemsadder.util;

import net.kyori.adventure.text.Component;

@FunctionalInterface
public interface ComponentMapper {
   ComponentResult map(Component var1);
}
