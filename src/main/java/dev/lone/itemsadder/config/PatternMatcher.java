package dev.lone.itemsadder.config;

public class PatternMatcher {
   private final PatternMode de;
   private final String pattern;

   public PatternMatcher(PatternMode var1, String var2) {
      this.de = var1;
      this.pattern = var2;
   }

   public static PatternMatcher method17(String var0) {
      if (!var0.startsWith("*") && !var0.startsWith("?") && !var0.endsWith("*") && !var0.endsWith("?")) {
         return var0.matches("[a-zA-Z0-9_:-]+") && var0.chars().filter(var0x -> var0x == 58).count() <= 1L
            ? new PatternMatcher(PatternMode.EXACT, var0)
            : new PatternMatcher(PatternMode.REGEX, var0);
      } else {
         return new PatternMatcher(PatternMode.WILDCARD, var0);
      }
   }

   public PatternMode bn() {
      return this.de;
   }

   public String bo() {
      return this.pattern;
   }
}
