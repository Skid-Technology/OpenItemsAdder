package dev.lone.itemsadder.item;

import dev.lone.itemsadder.block.CustomBlockStateSettings;
import dev.lone.itemsadder.block.ExplosiveBlockSpec;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.nms.NmsWorldService;
import dev.lone.itemsadder.world.BiomeWaterColor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ItemLoadSession {
   private final List ask;
   private final Map asl;
   private int aaR;
   private boolean aap;

   private ItemLoadSession(ItemDefinitionLoader var1, List var2, Map var3) {
      this.asm = var1;
      this.ask = var2;
      this.asl = var3;
   }

   public boolean hasRemainingFiles() {
      return this.aaR < this.ask.size();
   }

   public void loadNextFile() {
      if (this.aap) {
         throw new IllegalStateException("Item load session is already finished");
      }

      if (!this.hasRemainingFiles()) {
         throw new IllegalStateException("Item load session has no remaining files");
      }

      this.asm.method10113((YamlConfig)this.ask.get(this.aaR), this.asl);
      this.aaR++;
   }

   public void finish() {
      if (this.aap) {
         throw new IllegalStateException("Item load session is already finished");
      }

      if (this.hasRemainingFiles()) {
         throw new IllegalStateException("Item load session has remaining files");
      }

      this.aap = true;
      CustomBlockStateSettings.getInstance().saveIdCache();
      ExplosiveBlockSpec.recomputeTriggerState();
      if (this.asm.asd.method8098()) {
         this.asm.asd.field92.forEach((var1x, var2) -> this.asm.asd.method8131(null, var2));
      }

      ArrayList var1 = new ArrayList<>(
         this.asm.asd.ay.values().stream().map(var0 -> new BiomeWaterColor(var0.getNamespace(), var0.getId(), var0.getColor())).toList()
      );
      this.asm.asg = NmsWorldService.Om().method3198(var1);
   }
}
