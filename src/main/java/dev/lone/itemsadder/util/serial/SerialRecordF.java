package dev.lone.itemsadder.util.serial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class SerialRecordF implements SerialRecord {
   private List items;
   private int presence;
   public static final SerialType TYPE = SerialTypeF.INSTANCE;

   private SerialRecordF self() {
      if (this.items == null) {
         this.items = Collections.synchronizedList(new ArrayList());
      }

      return this;
   }

   public static SerialRecordF create() {
      return createWith(true);
   }

   public static SerialRecordF createWith(boolean var0) {
      return var0 ? new SerialRecordF().self() : new SerialRecordF();
   }

   public List getItems() {
      return this.items;
   }

   public SerialRecordF withItems(List var1) {
      this.items = var1;
      return this;
   }

   public SerialRecordF addItem(String var1) {
      this.items.add(var1);
      return this;
   }

   public SerialRecordF addAllItems(List var1) {
      this.items.addAll(var1);
      return this;
   }

   public int getItemCount() {
      return this.items.size();
   }

   public String getItem(int var1) {
      return (String)this.items.get(var1);
   }

   @Override
   public SerialType getType() {
      return SerialTypeF.INSTANCE;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         SerialRecordF var2 = (SerialRecordF)obj;
         return Objects.equals(this.items, var2.items);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      byte var1 = 1;
      return 31 * var1 + (this.items == null ? 0 : this.items.hashCode());
   }

   @Override
   public String toString() {
      ArrayList var1 = new ArrayList();
      if (!this.items.isEmpty()) {
         var1.add("item=" + this.getItems());
      }

      return "PInventory{" + String.join(", ", var1) + "}";
   }
}
