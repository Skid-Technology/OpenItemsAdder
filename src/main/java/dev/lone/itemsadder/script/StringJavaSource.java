package dev.lone.itemsadder.script;

import dev.lone.itemsadder.util.MarkerAnnotation;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;
import javax.tools.JavaFileObject.Kind;

public class StringJavaSource extends SimpleJavaFileObject {
   private final String aDt;

   public StringJavaSource(String var1, String var2) {
      super(URI.create("string:///" + var1.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
      this.aDt = var2;
   }

   @MarkerAnnotation
   @Override
   public CharSequence getCharContent(boolean ignoreEncodingErrors) {
      return this.aDt;
   }
}
