package dev.lone.itemsadder.anim;

import com.alibaba.fastjson2.JSONObject;
import java.util.Objects;

public class AnimationBinaryPayload {
   private final JSONObject TS;
   private final float[] field20;

   public AnimationBinaryPayload(JSONObject var1, float[] var2) {
      Objects.requireNonNull(var1, "metadata");
      Objects.requireNonNull(var2, "matrices");
      this.TS = var1;
      this.field20 = var2;
   }

   public JSONObject vH() {
      return this.TS;
   }

   public float[] vI() {
      return this.field20;
   }
}
