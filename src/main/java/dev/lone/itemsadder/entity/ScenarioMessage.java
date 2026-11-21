package dev.lone.itemsadder.entity;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class ScenarioMessage {
   private final boolean Tk;
   @Nullable
   private final UUID Tl;
   private final String message;

   public ScenarioMessage(boolean var1, @Nullable UUID var2, String var3) {
      this.Tk = var1;
      this.Tl = var2;
      this.message = var3;
   }

   static ScenarioMessage of(String var0) {
      return new ScenarioMessage(false, null, var0);
   }

   public boolean isPublic() {
      return this.Tk;
   }

   @Nullable
   public UUID playerId() {
      return this.Tl;
   }
}
