package dev.lone.itemsadder.util.json;

public class JsonNode {
   private final Object value;
   private final int depth;

   JsonNode(Object var1, int var2) {
      this.value = var1;
      this.depth = var2;
   }
}
