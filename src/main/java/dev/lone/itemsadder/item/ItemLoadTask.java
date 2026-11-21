package dev.lone.itemsadder.item;

import dev.lone.itemsadder.resourcepack.ResourcePackLoadState;
import dev.lone.itemsadder.util.CounterRegistry;
import dev.lone.itemsadder.util.PersistentCounter;
import java.util.concurrent.CompletableFuture;

public class ItemLoadTask {
   private final long bG;
   private final boolean bH;
   private final boolean isReload;
   private final PersistentCounter bI;
   private final CounterRegistry bJ;
   private final ItemDefinitionLoader bK;
   private final ItemLoadSession bL;
   private final CompletableFuture bM;
   private final ResourcePackLoadState bN;

   private ItemLoadTask(
      long var1,
      boolean var3,
      boolean var4,
      PersistentCounter var5,
      CounterRegistry var6,
      ItemDefinitionLoader var7,
      ItemLoadSession var8,
      CompletableFuture var9,
      ResourcePackLoadState var10
   ) {
      this.bG = var1;
      this.bH = var3;
      this.isReload = var4;
      this.bI = var5;
      this.bJ = var6;
      this.bK = var7;
      this.bL = var8;
      this.bM = var9;
      this.bN = var10;
   }

   public long ai() {
      return this.bG;
   }

   public boolean aj() {
      return this.bH;
   }

   public boolean ak() {
      return this.isReload;
   }

   public PersistentCounter al() {
      return this.bI;
   }

   public CounterRegistry am() {
      return this.bJ;
   }

   public ItemDefinitionLoader an() {
      return this.bK;
   }

   public ItemLoadSession ao() {
      return this.bL;
   }

   public CompletableFuture ap() {
      return this.bM;
   }

   public ResourcePackLoadState aq() {
      return this.bN;
   }
}
