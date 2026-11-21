package dev.lone.itemsadder.internal;

import net.md_5.bungee.api.chat.HoverEvent.Action;

// $VF: synthetic class
public class SwitchMap83 {
   static {
      try {
         bLu[Action.SHOW_TEXT.ordinal()] = 1;
      } catch (NoSuchFieldError var10) {
      }

      try {
         bLu[Action.SHOW_ITEM.ordinal()] = 2;
      } catch (NoSuchFieldError var9) {
      }

      try {
         bLu[Action.SHOW_ENTITY.ordinal()] = 3;
      } catch (NoSuchFieldError var8) {
      }

      try {
         bLu[Action.SHOW_ACHIEVEMENT.ordinal()] = 4;
      } catch (NoSuchFieldError var7) {
      }

      bLt = new int[net.md_5.bungee.api.chat.ClickEvent.Action.values().length];

      try {
         bLt[net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         bLt[net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_FILE.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      try {
         bLt[net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND.ordinal()] = 3;
      } catch (NoSuchFieldError var4) {
      }

      try {
         bLt[net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND.ordinal()] = 4;
      } catch (NoSuchFieldError var3) {
      }

      try {
         bLt[net.md_5.bungee.api.chat.ClickEvent.Action.CHANGE_PAGE.ordinal()] = 5;
      } catch (NoSuchFieldError var2) {
      }

      try {
         bLt[net.md_5.bungee.api.chat.ClickEvent.Action.COPY_TO_CLIPBOARD.ordinal()] = 6;
      } catch (NoSuchFieldError var1) {
      }
   }
}
