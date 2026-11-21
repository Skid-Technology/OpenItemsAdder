package dev.lone.itemsadder.util.serial;

public final class SerialTypeMismatchException extends RuntimeException {
   private static final long serialVersionUID = -7466929953374883507L;
   public final Object value;
   public final SerialType type;

   public SerialTypeMismatchException(SerialRecord var1) {
      this(var1, var1.getType());
   }

   public SerialTypeMismatchException(Object var1, SerialType var2) {
      this.value = var1;
      this.type = var2;
   }

   public SerialTypeMismatchException(String var1, SerialRecord var2) {
      this(var1, var2, var2.getType());
   }

   public SerialTypeMismatchException(String var1, Object var2, SerialType var3) {
      super(var1);
      this.value = var2;
      this.type = var3;
   }

   public Object getValue() {
      return this.value;
   }

   public SerialType getType() {
      return this.type;
   }
}
