package dev.lone.itemsadder.util.json;

import dev.lone.itemsadder.resourcepack.IaImageCodec;
import java.util.HashSet;

public final class SafeJsonValidator {
   private final String input;
   private int pos;
   private int depth;
   private int nodeCount;

   SafeJsonValidator(String var1) {
      this.input = var1;
   }

   void run() {
      this.skipWhitespace();
      this.validateValue(false, 0, false);
      this.skipWhitespace();
      if (this.pos != this.input.length()) {
         throw IaImageCodec.invalidBundle("trailing JSON data");
      }
   }

   private void validateValue(boolean var1, int var2, boolean var3) {
      if (!var1 || ++this.nodeCount <= 100000 && var2 <= 64) {
         this.skipWhitespace();
         if (this.pos >= this.input.length()) {
            throw IaImageCodec.invalidBundle("truncated JSON");
         }

         char var4 = this.input.charAt(this.pos);
         if (var4 == '{') {
            if (++this.depth > 68) {
               throw IaImageCodec.invalidBundle("JSON depth exceeded");
            }

            this.validateObject(var1, var2, var3);
            this.depth--;
         } else if (var4 == '[') {
            if (++this.depth > 68) {
               throw IaImageCodec.invalidBundle("JSON depth exceeded");
            }

            this.validateArray(var1, var2);
            this.depth--;
         } else if (var4 == '"') {
            int var5 = this.readString(null);
            if (var1 && var5 > 16384) {
               throw IaImageCodec.invalidBundle("string too long");
            }

            if (!var1 && !var3 && var5 > 16384 && this.depth > 0) {
               throw IaImageCodec.invalidBundle("string too long");
            }
         } else {
            this.readLiteral();
         }
      } else {
         throw IaImageCodec.invalidBundle("JSON structure exceeds limits");
      }
   }

   private void validateObject(boolean var1, int var2, boolean var3) {
      this.pos++;
      this.skipWhitespace();
      if (!this.consume('}')) {
         HashSet var4 = new HashSet();

         while (true) {
            this.skipWhitespace();
            if (this.pos < this.input.length() && this.input.charAt(this.pos) == '"') {
               StringBuilder var5 = new StringBuilder();
               int var6 = this.readString(var5);
               if (!var4.add(var5.toString())) {
                  throw IaImageCodec.invalidBundle("duplicate JSON object key");
               }

               if (!var1 || var6 <= 16384 && !var5.equals("__proto__") && !var5.equals("prototype") && !var5.equals("constructor")) {
                  this.skipWhitespace();
                  this.expect(':');
                  boolean var7 = !var1 && this.depth == 1;
                  boolean var8 = var7 && var5.toString().equals("effects");
                  boolean var9 = var7 && var5.toString().equals("textures");
                  boolean var10 = var3 && this.pos < this.input.length() && this.input.charAt(this.pos) == '"';
                  this.validateValue(var1 || var8, var8 ? 0 : var2 + (var1 ? 1 : 0), var10 || var9);
                  this.skipWhitespace();
                  if (this.consume('}')) {
                     return;
                  }

                  this.expect(',');
                  continue;
               }

               throw IaImageCodec.invalidBundle("unsafe or oversized object key");
            }

            throw IaImageCodec.invalidBundle("invalid JSON object key");
         }
      }
   }

   private void validateArray(boolean var1, int var2) {
      this.pos++;
      this.skipWhitespace();
      if (!this.consume(']')) {
         while (true) {
            this.validateValue(var1, var2 + (var1 ? 1 : 0), false);
            this.skipWhitespace();
            if (this.consume(']')) {
               return;
            }

            this.expect(',');
         }
      }
   }

   private int readString(StringBuilder var1) {
      this.expect('"');
      int var2 = 0;

      while (this.pos < this.input.length()) {
         char var3 = this.input.charAt(this.pos++);
         if (var3 == '"') {
            return var2;
         }

         if (var3 < ' ') {
            throw IaImageCodec.invalidBundle("invalid JSON string");
         }

         if (var3 == '\\') {
            if (this.pos >= this.input.length()) {
               throw IaImageCodec.invalidBundle("invalid JSON escape");
            }

            char var4 = this.input.charAt(this.pos++);
            switch (var4) {
               case '"':
               case '/':
               case '\\':
                  var3 = var4;
                  break;
               case 'b':
                  var3 = '\b';
                  break;
               case 'f':
                  var3 = '\f';
                  break;
               case 'n':
                  var3 = '\n';
                  break;
               case 'r':
                  var3 = '\r';
                  break;
               case 't':
                  var3 = '\t';
                  break;
               case 'u':
                  if (this.pos + 4 > this.input.length()) {
                     throw IaImageCodec.invalidBundle("invalid JSON unicode escape");
                  }

                  try {
                     var3 = (char)Integer.parseInt(this.input.substring(this.pos, this.pos + 4), 16);
                  } catch (NumberFormatException var6) {
                     throw IaImageCodec.invalidBundle("invalid JSON unicode escape");
                  }

                  this.pos += 4;
                  break;
               default:
                  throw IaImageCodec.invalidBundle("invalid JSON escape");
            }
         }

         var2++;
         if (var1 != null) {
            var1.append(var3);
         }
      }

      throw IaImageCodec.invalidBundle("unterminated JSON string");
   }

   private void readLiteral() {
      int var1 = this.pos;

      while (this.pos < this.input.length() && ",]} \t\r\n".indexOf(this.input.charAt(this.pos)) < 0) {
         this.pos++;
      }

      if (var1 == this.pos) {
         throw IaImageCodec.invalidBundle("invalid JSON value");
      }

      String var2 = this.input.substring(var1, this.pos);
      if (!var2.equals("true") && !var2.equals("false") && !var2.equals("null") && !var2.matches("-?(?:0|[1-9][0-9]*)(?:\\.[0-9]+)?(?:[eE][+-]?[0-9]+)?")) {
         throw IaImageCodec.invalidBundle("invalid JSON value");
      }
   }

   private void skipWhitespace() {
      while (this.pos < this.input.length() && " \t\r\n".indexOf(this.input.charAt(this.pos)) >= 0) {
         this.pos++;
      }
   }

   private boolean consume(char var1) {
      if (this.pos < this.input.length() && this.input.charAt(this.pos) == var1) {
         this.pos++;
         return true;
      } else {
         return false;
      }
   }

   private void expect(char var1) {
      if (!this.consume(var1)) {
         throw IaImageCodec.invalidBundle("malformed JSON");
      }
   }
}
