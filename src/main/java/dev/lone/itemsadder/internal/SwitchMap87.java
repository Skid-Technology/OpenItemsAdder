package dev.lone.itemsadder.internal;

import beer.devs.nativepackets.api.PacketSendOutcome;
import org.bukkit.block.BlockFace;

// $VF: synthetic class
public class SwitchMap87 {
   static {
      try {
         nq[BlockFace.UP.ordinal()] = 1;
      } catch (NoSuchFieldError var10) {
      }

      try {
         nq[BlockFace.DOWN.ordinal()] = 2;
      } catch (NoSuchFieldError var9) {
      }

      try {
         nq[BlockFace.NORTH.ordinal()] = 3;
      } catch (NoSuchFieldError var8) {
      }

      try {
         nq[BlockFace.SOUTH.ordinal()] = 4;
      } catch (NoSuchFieldError var7) {
      }

      try {
         nq[BlockFace.EAST.ordinal()] = 5;
      } catch (NoSuchFieldError var6) {
      }

      try {
         nq[BlockFace.WEST.ordinal()] = 6;
      } catch (NoSuchFieldError var5) {
      }

      np = new int[PacketSendOutcome.values().length];

      try {
         np[PacketSendOutcome.SUCCESS.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         np[PacketSendOutcome.CANCELLED.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         np[PacketSendOutcome.FAILED.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         np[PacketSendOutcome.ABORTED.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
