package dev.lone.itemsadder.resourcepack;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackNamePrefixer {
   private static final String FUNCTION_PREFIX = "f_";
   private final Map functionRenames = new HashMap();
   private String source;

   public PackNamePrefixer(String var1) {
      this.source = var1;
   }

   public String minify() {
      this.source = this.prefixFunctionNames();
      this.source = this.stripComments();
      this.source = this.inlineDefines();
      this.source = this.source.replaceAll("\\.0( |\\D|)", ".$1");
      this.source = this.source.replaceAll("0\\.(\\d+)", ".$1");
      this.source = this.source.replaceAll("} }", "}}");
      this.source = this.source.replaceAll("if \\(", "if(");
      this.source = this.source.replaceAll("for \\(", "for(");
      this.source = this.source.replaceAll("while \\(", "while(");
      this.source = this.source.replaceAll("} else \\{", "}else{");
      this.source = this.source.replace(" = ", "=");
      this.source = this.source.replace(" == ", "==");
      this.source = this.source.replace(" + ", "+");
      this.source = this.source.replace(" += ", "+=");
      this.source = this.source.replace(" - ", "-");
      this.source = this.source.replace(" -= ", "-=");
      this.source = this.source.replace(" * ", "*");
      this.source = this.source.replace(" *= ", "*=");
      return this.source;
   }

   private String inlineDefines() {
      Matcher var1 = Pattern.compile("#define\\s+(\\w+)\\s+(.+)").matcher(this.source);

      while (var1.find()) {
         String var2 = var1.group(1).trim();
         String var3 = var1.group(2).trim();
         this.source = this.source.replace(var1.group(), "");
         this.source = this.source.replaceAll("\\b" + var2 + "\\b", var3);
      }

      return this.source;
   }

   public String stripComments() {
      String var1 = "/\\*.*?\\*/";
      String var2 = "//.*";
      this.source = this.source.replaceAll(var1, "");
      this.source = this.source.replaceAll(var2, "");
      return this.source;
   }

   public String prefixFunctionNames() {
      Matcher var1 = Pattern.compile("\\bvoid\\s+(\\w+)\\s*\\(").matcher(this.source);

      while (var1.find()) {
         String var2 = var1.group(1).trim();
         if (!var2.equals("main") && !this.functionRenames.containsKey(var2)) {
            this.functionRenames.put(var2, "f_" + this.nextUniqueSuffix());
         }
      }

      for (Entry var3 : this.functionRenames.entrySet()) {
         String var4 = (String)var3.getKey();
         String var5 = (String)var3.getValue();
         this.source = this.source.replaceAll("\\bvoid\\s+" + var4 + "\\s*\\(", "void " + var5 + "(");
         this.source = this.source.replaceAll("\\b" + var4 + "\\s*\\(", var5 + "(");
      }

      return this.source;
   }

   private String nextUniqueSuffix() {
      int var1 = this.functionRenames.size();

      String var2;
      do {
         var2 = Integer.toHexString(var1++);
      } while (this.functionRenames.containsValue("f_" + var2) || Pattern.compile("\\bf_" + var2 + "\\b").matcher(this.source).find());

      return var2;
   }
}
