package dev.lone.itemsadder.script;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;

public class JavaSourceCompiler {
   private final JavaCompiler aDj;
   private final MemoryJavaFileManager aDk;
   private final List aDl;
   private final JavaFileObject aDm;
   String aDn;
   Set aDo;

   public JavaSourceCompiler(JavaCompiler var1, MemoryJavaFileManager var2, List var3, JavaFileObject var4, String var5, Set var6) {
      this.aDj = var1;
      this.aDk = var2;
      this.aDl = var3;
      this.aDm = var4;
      this.aDn = var5;
      this.aDo = var6;
   }

   public List KM() {
      DiagnosticCollector var1 = new DiagnosticCollector();
      JavacTask var2 = (JavacTask)this.aDj.getTask(null, this.aDk, var1, this.aDl, null, List.of(this.aDm));
      Iterable var3 = var2.parse();
      ArrayList var4 = new ArrayList();

      for (CompilationUnitTree var6 : var3) {
         var6.accept(new ForbiddenImportScanner(this, var4), null);
      }

      return var4.stream().distinct().collect(Collectors.toList());
   }
}
