package dev.lone.itemsadder.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class RefreshingValue {
   private final Object key;
   private final Function compute;
   private final BiConsumer applier;
   private Map values = Map.of();
   private Object pendingValue;
   private Runnable onApplied;
   private int pendingGeneration = -1;
   private int currentGeneration = -1;
   private long version;
   private boolean refreshing;
   private boolean forced;
   volatile boolean present;
   volatile Object appliedValue;

   RefreshingValue(Object var1, Function var2, BiConsumer var3) {
      this.key = var1;
      this.compute = var2;
      this.applier = var3;
   }

   void mergeValues(Map var1) {
      HashMap var2 = new HashMap(this.values);
      var1.forEach(var2::put);
      this.values = Map.copyOf(var2);
      this.present = this.values.containsKey(this.key);
      if (var1.containsKey(this.key)) {
         this.version++;
         this.refreshing = this.forced = false;
         this.appliedValue = null;
         this.refresh();
      }
   }

   void update(Object var1, int var2, Runnable var3) {
      this.pendingValue = var1;
      this.pendingGeneration = var2;
      this.onApplied = var3;
      if (this.currentGeneration == var2) {
         this.refresh();
      }
   }

   void enterGeneration(int var1) {
      this.currentGeneration = var1;
      if (this.pendingGeneration != var1) {
         this.pendingValue = null;
         this.onApplied = null;
      }

      this.refresh();
   }

   void leaveGeneration(int var1) {
      if (this.currentGeneration == var1) {
         this.currentGeneration = -1;
         if (this.pendingGeneration == var1) {
            this.pendingValue = null;
            this.onApplied = null;
         }

         this.refresh();
      }
   }

   void reset() {
      this.version++;
      this.present = false;
      this.values = Map.of();
      this.appliedValue = this.pendingValue = null;
      this.onApplied = null;
      this.refreshing = this.forced = false;
      this.currentGeneration = this.pendingGeneration = -1;
   }

   private void refresh() {
      Object var1 = this.pendingValue != null && this.pendingGeneration == this.currentGeneration && this.present ? this.pendingValue : null;
      if (var1 == null || this.appliedValue != var1) {
         this.appliedValue = null;
         if (var1 == null) {
            if (this.refreshing) {
               this.version++;
               this.refreshing = this.forced = false;
               this.applier.accept(this.values, () -> {});
            }
         } else {
            Object var2 = this.compute.apply(this.values);
            if (!this.refreshing || this.forced) {
               if (!this.forced && var2 != this.values) {
                  this.refreshing = true;
                  long var3 = ++this.version;
                  this.applier.accept(var2, () -> {
                     if (this.version == var3 && this.refreshing) {
                        this.forced = true;
                        this.refresh();
                     }
                  });
               } else {
                  this.appliedValue = var1;
                  if (this.onApplied != null) {
                     this.onApplied.run();
                  }
               }
            }
         }
      }
   }
}
