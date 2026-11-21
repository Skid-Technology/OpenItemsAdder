package dev.lone.itemsadder.util.serial;

public interface SerialType {
   String nameOf(int var1);

   int idOf(String var1);

   boolean accepts(Object var1);

   Object defaultValue();

   String getName();

   String getDisplayName();

   Class getJavaClass();

   void read(SerialReader var1, Object var2);

   void write(SerialWriter var1, Object var2);
}
