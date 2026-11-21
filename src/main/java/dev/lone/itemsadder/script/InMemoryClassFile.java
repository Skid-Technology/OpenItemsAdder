package dev.lone.itemsadder.script;

import dev.lone.itemsadder.util.MarkerAnnotation;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;
import javax.tools.JavaFileObject.Kind;

public class InMemoryClassFile extends SimpleJavaFileObject {
   private final ByteArrayOutputStream aCM = new ByteArrayOutputStream();
   private final String aCN;

   public InMemoryClassFile(String var1, Kind var2) {
      super(URI.create("string:///" + var1.replace('.', '/') + var2.extension), var2);
      this.aCN = var1;
   }

   @MarkerAnnotation
   @Override
   public OutputStream openOutputStream() {
      return this.aCM;
   }

   public byte[] getBytes() {
      return this.aCM.toByteArray();
   }

   public String Ky() {
      return this.aCN;
   }
}
