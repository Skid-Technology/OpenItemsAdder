package dev.lone.itemsadder.resourcepack;

enum ShaderFileKind {
   VERTEX("vertex"),
   FRAGMENT("fragment"),
   INCLUDE("include"),
   PROGRAM("program"),
   NONE("none");

   private final String text;

   ShaderFileKind(String var3) {
      this.text = var3;
   }

   @Override
   public String toString() {
      return this.text;
   }
}
