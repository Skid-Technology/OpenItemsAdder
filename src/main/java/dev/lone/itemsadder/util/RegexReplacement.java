package dev.lone.itemsadder.util;

import java.util.function.Function;
import java.util.regex.Pattern;

public class RegexReplacement {
   private final Pattern pattern;
   private final Function replacer;

   private RegexReplacement(Pattern var1, Function var2) {
      this.pattern = var1;
      this.replacer = var2;
   }
}
