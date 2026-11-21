package dev.lone.itemsadder.api;

import com.google.gson.JsonObject;
import java.util.Objects;

public class VanillaModelResource {
   private final String path;
   private final JsonObject json;

   public VanillaModelResource(String path, JsonObject json) {
      Objects.requireNonNull(path, "path");
      Objects.requireNonNull(json, "json");
      this.path = path;
      this.json = json;
   }
}
