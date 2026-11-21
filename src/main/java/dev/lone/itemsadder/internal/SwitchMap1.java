package dev.lone.itemsadder.internal;

import dev.lone.itemsadder.item.HudDisplayMode;
import org.bukkit.entity.EntityType;

// $VF: synthetic class
public class SwitchMap1 {
   static {
      try {
         asj[HudDisplayMode.HUD.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         asj[HudDisplayMode.BOSSBAR.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      asi = new int[EntityType.values().length];

      try {
         asi[EntityType.ARMOR_STAND.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         asi[EntityType.ITEM_FRAME.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         asi[EntityType.GLOW_ITEM_FRAME.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         asi[EntityType.ITEM_DISPLAY.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
