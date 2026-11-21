package dev.lone.itemsadder.resourcepack;

enum PackOrigin {
   ITEMSADDER_GENERATED("ItemsAdder generated"),
   ITEMSADDER_BUNDLED("ItemsAdder bundled"),
   USER_CONTENT_PACK("user content pack"),
   EXTERNAL_RESOURCE_PACK("external or third-party resource pack"),
   EVENT_STREAM_OR_WRITER("added by pack event, stream, or writer"),
   VANILLA_CLIENT_ASSET("Vanilla client asset"),
   UNKNOWN("source could not be determined");

   private final String label;

   PackOrigin(String var3) {
      this.label = var3;
   }

   String getLabel() {
      return this.label;
   }

   boolean isItemsAdderOwned() {
      return this == ITEMSADDER_GENERATED || this == ITEMSADDER_BUNDLED;
   }

   boolean isExternal() {
      return !this.isItemsAdderOwned() && this != VANILLA_CLIENT_ASSET;
   }
}
