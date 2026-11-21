package dev.lone.itemsadder.util;

import java.io.OutputStream;

@FunctionalInterface
public interface StreamWriter {
   void writeTo(OutputStream var1);
}
