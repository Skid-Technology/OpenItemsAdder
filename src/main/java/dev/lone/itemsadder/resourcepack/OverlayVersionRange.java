package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;

public enum OverlayVersionRange {
   v_1_21_4_to_5("ia_overlay_1_21_4_to_5", Version.v1_21_4),
   v_1_21_6_plus("ia_overlay_1_21_6_plus", Version.v1_21_6),
   v_26_1_plus("ia_overlay_26_1_plus", Version.v26_1_1);

   private final String overlayKey;
   private final Version baseVersion;

   OverlayVersionRange(String var3, Version var4) {
      this.overlayKey = var3;
      this.baseVersion = var4;
   }

   String getOverlayKey() {
      return this.overlayKey;
   }
}
