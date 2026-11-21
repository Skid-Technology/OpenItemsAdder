package dev.lone.itemsadder.util.serial;

public abstract class SerialTypeAdapter implements SerialType {
   protected final SerialType delegate;

   public SerialTypeAdapter(SerialType var1) {
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

   @Override
   public boolean accepts(Object var1) {
      return this.delegate.accepts(var1);
   }

   @Override
   public void read(SerialReader var1, Object var2) {
      this.delegate.read(var1, var2);
   }

   @Override
   public String getDisplayName() {
      return this.delegate.getDisplayName();
   }

   @Override
   public String getName() {
      return this.delegate.getName();
   }

   @Override
   public Object defaultValue() {
      return this.delegate.defaultValue();
   }

   @Override
   public Class getJavaClass() {
      return this.delegate.getJavaClass();
   }

   @Override
   public void write(SerialWriter var1, Object var2) {
      this.delegate.write(var1, var2);
   }
}
