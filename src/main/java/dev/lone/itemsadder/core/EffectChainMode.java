package dev.lone.itemsadder.core;

public enum EffectChainMode {
   STOP_IF_LAST_SUCCESS("flow.stop_if_last_success"),
   STOP_IF_LAST_FAIL("flow.stop_if_last_fail"),
   SKIP_IF_LAST_SUCCESS("flow.skip_if_last_success"),
   SKIP_IF_LAST_FAIL("flow.skip_if_last_fail"),
   STOP_IF_ANY_SUCCESS("flow.stop_if_any_success"),
   STOP_IF_ANY_FAIL("flow.stop_if_any_fail"),
   SKIP_IF_ANY_SUCCESS("flow.skip_if_any_success"),
   SKIP_IF_ANY_FAIL("flow.skip_if_any_fail"),
   STOP_IF_SUCCESS("flow.stop_if_success"),
   STOP_IF_FAIL("flow.stop_if_fail"),
   SKIP_IF_SUCCESS("flow.skip_if_success"),
   SKIP_IF_FAIL("flow.skip_if_fail");

   public final String key;

   EffectChainMode(String var3) {
      this.key = var3;
   }
}
