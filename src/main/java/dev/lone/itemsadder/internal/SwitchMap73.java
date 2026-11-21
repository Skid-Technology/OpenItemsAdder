package dev.lone.itemsadder.internal;

import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

// $VF: synthetic class
public class SwitchMap73 {
   static {
      try {
         bzo[Status.DECLINED.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         bzo[Status.FAILED_DOWNLOAD.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         bzo[Status.DISCARDED.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }

      try {
         bzo[Status.INVALID_URL.ordinal()] = 4;
      } catch (NoSuchFieldError var2) {
      }

      try {
         bzo[Status.FAILED_RELOAD.ordinal()] = 5;
      } catch (NoSuchFieldError var1) {
      }
   }
}
