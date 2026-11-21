package dev.lone.itemsadder.util;

import beer.devs.nativepackets.api.PacketSendOutcome;

// $VF: synthetic class
public class PackedIntTable {
   static {
      try {
         SWITCH_PACKET_OUTCOME[PacketSendOutcome.SUCCESS.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         SWITCH_PACKET_OUTCOME[PacketSendOutcome.CANCELLED.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         SWITCH_PACKET_OUTCOME[PacketSendOutcome.FAILED.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         SWITCH_PACKET_OUTCOME[PacketSendOutcome.ABORTED.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
