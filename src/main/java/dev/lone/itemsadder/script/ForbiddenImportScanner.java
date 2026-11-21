package dev.lone.itemsadder.script;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.util.TreeScanner;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.MarkerAnnotation;
import java.util.List;

class ForbiddenImportScanner extends TreeScanner {
   ForbiddenImportScanner(JavaSourceCompiler var1, List var2) {
      this.aDp = var2;
      this.aDq = var1;
   }

   @MarkerAnnotation
   public Void visitImport(ImportTree tree, Void unused) {
      String var3 = tree.getQualifiedIdentifier().toString();
      Log.devLog("Checking import for threats: " + var3);

      for (String var5 : this.aDq.aDo) {
         if (var3.startsWith(var5)) {
            this.aDp.add("Forbidden import: " + var3);
         }
      }

      return (Void)super.visitImport(tree, null);
   }

   @MarkerAnnotation
   public Void visitIdentifier(IdentifierTree tree, Void unused) {
      this.eh(tree.getName().toString());
      return (Void)super.visitIdentifier(tree, null);
   }

   @MarkerAnnotation
   public Void visitMemberSelect(MemberSelectTree tree, Void unused) {
      this.eh(tree.toString());
      return (Void)super.visitMemberSelect(tree, null);
   }

   @MarkerAnnotation
   public Void visitNewClass(NewClassTree tree, Void unused) {
      if (tree.getIdentifier() != null) {
         this.eh(tree.getIdentifier().toString());
      }

      return (Void)super.visitNewClass(tree, null);
   }

   private void eh(String var1) {
      for (String var3 : this.aDq.aDo) {
         if (var1.startsWith(var3)) {
            this.aDp.add("Forbidden usage: " + var1);
         }
      }
   }
}
