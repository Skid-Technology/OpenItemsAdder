package dev.lone.itemsadder.resourcepack;

import dev.lone.itemsadder.util.Log;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class VanillaAssetTask extends FutureTask {
   VanillaAssetTask(VanillaAssetManager var1, Callable var2, boolean var3) {
      super(var2);
      this.iF = var3;
   }

   @Override
   protected void done() {
      try {
         this.get();
      } catch (CancellationException var2) {
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
      } catch (ExecutionException var4) {
         if (!this.iF) {
            Log.error("Failed to prepare vanilla assets", var4.getCause());
         }
      }
   }
}
