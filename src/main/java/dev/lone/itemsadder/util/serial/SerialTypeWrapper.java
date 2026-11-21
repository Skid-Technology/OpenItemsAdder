package dev.lone.itemsadder.util.serial;

public abstract class SerialTypeWrapper implements SerialType {
   public final SerialType delegate;

   public SerialTypeWrapper(SerialType var1) {
      this.delegate = var1;
   }

   @Override
   public String nameOf(int var1) {
      return this.delegate.nameOf(var1);
   }

   @Override
   public int idOf(String var1) {
      return this.delegate.idOf(var1);
   }

   public boolean hasHandler(SerialTypeHandler var1) {
      return true;
   }

   @Override
   public String getDisplayName() {
      return this.delegate.getDisplayName();
   }

   @Override
   public String getName() {
      return this.delegate.getName();
   }

   public SerialTypeHandler getHandler() {
      throw new UnsupportedOperationException();
   }

   @Override
   public Class getJavaClass() {
      throw new UnsupportedOperationException();
   }

   public final void writeWith(SerialWriter var1, SerialTypeHandler var2) {
      if (var2.writer != null) {
         var2.reader.readMessage(var2, this);
      } else {
         var2.writer = var1;
         SerialReader var3 = var2.getReader(this);
         if (var3 == null) {
            var2.writer = null;
            var2.verifyReader(this, var3, true);
         } else {
            var2.reader = var3;
            boolean var4 = false;

            try {
               this.initHandler(var2, var3, var1);
               var4 = true;
            } finally {
               var2.verifyReader(this, var3, !var4);
            }
         }
      }
   }

   public final void readWith(SerialReader var1, SerialTypeHandler var2) {
      this.initHandler(var2, var1, var2.writer);
   }

   protected abstract void initHandler(SerialTypeHandler var1, SerialReader var2, SerialWriter var3);
}
