package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import java.util.Set;

class PackCacheKey {
   private final JSONObject bun;
   private final JSONObject buo;
   private final Set bup;

   private PackCacheKey(JSONObject var1, JSONObject var2, Set var3) {
      this.bun = var1;
      this.buo = var2;
      this.bup = var3;
   }

   @Override
   public boolean equals(Object other) {
      return other instanceof PackCacheKey var2 && this.bun == var2.bun && this.buo == var2.buo && this.bup.equals(var2.bup);
   }

   @Override
   public int hashCode() {
      return (31 * System.identityHashCode(this.bun) + System.identityHashCode(this.buo)) * 31 + this.bup.hashCode();
   }

   public JSONObject lower() {
      return this.bun;
   }

   public JSONObject higher() {
      return this.buo;
   }

   public Set ids() {
      return this.bup;
   }
}
