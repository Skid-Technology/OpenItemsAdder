package dev.lone.itemsadder.config;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

final class YamlNodeDocument {
   private final Yaml bNW;
   private final MappingNode bNX;
   boolean bLS;

   YamlNodeDocument(String var1) {
      LoaderOptions var2 = new LoaderOptions();
      var2.setProcessComments(true);
      var2.setAllowDuplicateKeys(false);
      DumperOptions var3 = new DumperOptions();
      var3.setProcessComments(true);
      var3.setDefaultFlowStyle(FlowStyle.BLOCK);
      var3.setIndent(2);
      var3.setWidth(120);
      this.bNW = new Yaml(new SafeConstructor(var2), new Representer(var3), var3, var2);
      Node var4 = this.bNW.compose(new StringReader(var1));
      if (var4 == null) {
         var4 = this.bNW.represent(new LinkedHashMap());
      }

      if (var4 instanceof MappingNode var5) {
         this.bNX = var5;
         this.method7500(this.bNX, "", Collections.newSetFromMap(new IdentityHashMap()));
      } else {
         throw new IllegalArgumentException("config.yml must contain a YAML mapping");
      }
   }

   private void method7500(Node var1, String var2, Set var3) {
      if (!var3.add(var1)) {
         throw new IllegalArgumentException("Recursive YAML alias at " + var2);
      }

      if (var1 instanceof MappingNode var4) {
         HashSet var6 = new HashSet();

         for (NodeTuple var8 : var4.getValue()) {
            if (!(var8.getKeyNode() instanceof ScalarNode var9)) {
               throw new IllegalArgumentException("Expected a string key at " + var2);
            }

            if (var2.equals("pack_delivery.per_client_version.") && var9.getTag().equals(Tag.INT)) {
               var9.setTag(Tag.STR);
               this.bLS = true;
            }

            if (!var9.getTag().equals(Tag.STR)) {
               throw new IllegalArgumentException("Expected a string key at " + var2);
            }

            if (!var6.add(var9.getValue())) {
               throw new IllegalArgumentException("Duplicate config key: " + var2 + var9.getValue());
            }

            if ("<<".equals(var9.getValue())) {
               throw new IllegalArgumentException("YAML merge keys are not supported in config.yml");
            }

            this.method7500(var8.getValueNode(), var2 + var9.getValue() + ".", var3);
         }
      } else if (var1 instanceof SequenceNode var5) {
         for (Node var12 : var5.getValue()) {
            this.method7500(var12, var2, var3);
         }
      } else if (!(var1 instanceof ScalarNode) || !Set.of(Tag.STR, Tag.INT, Tag.FLOAT, Tag.BOOL, Tag.NULL).contains(var1.getTag())) {
         throw new IllegalArgumentException("Unsupported YAML value at " + var2);
      }

      var3.remove(var1);
   }

   Node lX(String var1) {
      Object var2 = this.bNX;

      for (String var6 : var1.split("\\.")) {
         if (!(var2 instanceof MappingNode var7)) {
            return null;
         }

         NodeTuple var8 = method7514(var7, var6);
         if (var8 == null) {
            return null;
         }

         var2 = var8.getValueNode();
      }

      return (Node)var2;
   }

   boolean jI(String var1) {
      return this.lX(var1) != null;
   }

   YamlNodeDocument acY() {
      return this;
   }

   Object kf(String var1) {
      return this.method7501(var1, null);
   }

   Object method7501(String var1, Object var2) {
      Node var3 = this.lX(var1);
      if (var3 == null) {
         return var2;
      } else {
         return var3 instanceof MappingNode ? var3 : this.method7502(var3);
      }
   }

   private Object method7502(Node var1) {
      if (var1 instanceof ScalarNode var3) {
         if (var1.getTag().equals(Tag.STR)) {
            return var3.getValue();
         } else {
            return var1.getTag().equals(Tag.NULL) ? null : this.bNW.load(var3.getValue());
         }
      } else {
         return var1 instanceof SequenceNode var2 ? var2.getValue().stream().map(this::method7502).toList() : var1;
      }
   }

   boolean kj(String var1) {
      return this.method7503(var1, false);
   }

   boolean method7503(String var1, boolean var2) {
      if (this.method7501(var1, var2) instanceof Boolean var4) {
         return var4;
      } else {
         throw new IllegalArgumentException("Expected boolean: " + var1);
      }
   }

   int jP(String var1) {
      return this.method7504(var1, 0);
   }

   int method7504(String var1, int var2) {
      if (this.method7501(var1, var2) instanceof Number var4) {
         return method7520(var4, var1);
      } else {
         throw new IllegalArgumentException("Expected integer: " + var1);
      }
   }

   void method7505(String var1, Object var2) {
      if (var2 == null) {
         this.jJ(var1);
      } else {
         Node var3 = var2 instanceof Node var4 ? var4 : this.bNW.represent(var2);
         MappingNode var7 = this.method7513(var1, true);
         String var5 = lZ(var1);
         NodeTuple var6 = method7514(var7, var5);
         if (var6 != null) {
            method7518(var6.getValueNode(), var3);
            var7.getValue().set(var7.getValue().indexOf(var6), new NodeTuple(var6.getKeyNode(), var3));
         } else {
            var7.getValue().add(new NodeTuple(this.bNW.represent(var5), var3));
         }

         this.bLS = true;
      }
   }

   void method7506(String var1, Object var2) {
      if (!this.jI(var1)) {
         this.method7505(var1, var2);
      }
   }

   void lY(String var1) {
      if (this.lX(var1) instanceof MappingNode var2 && var2.getValue().isEmpty()) {
         this.jJ(var1);
      }
   }

   void method7507(String var1, String var2, boolean var3) {
      if (this.jI(var1)) {
         boolean var4 = this.jI(var2);
         boolean var5 = this.kj(var1);
         this.bo(var1, var2);
         if (!var4 && var3) {
            this.method7505(var2, !var5);
         }
      }
   }

   void jJ(String var1) {
      MappingNode var2 = this.method7513(var1, false);
      if (var2 != null) {
         NodeTuple var3 = method7514(var2, lZ(var1));
         if (var3 != null) {
            ArrayList var4 = new ArrayList();
            method7521(var3.getKeyNode(), var4);
            method7521(var3.getValueNode(), var4);
            this.bNX
               .setEndComments(
                  method7517(
                     this.bNX.getEndComments(),
                     var4.stream()
                        .map(var0 -> var0.getCommentType() == CommentType.IN_LINE ? new CommentLine(null, null, var0.getValue(), CommentType.BLOCK) : var0)
                        .toList()
                  )
               );
            var2.getValue().remove(var3);
            this.bLS = true;
         }
      }
   }

   void bo(String var1, String var2) {
      if (!var1.equals(var2)) {
         MappingNode var3 = this.method7513(var1, false);
         if (var3 != null) {
            NodeTuple var4 = method7514(var3, lZ(var1));
            if (var4 != null) {
               boolean var5 = var2.startsWith(var1 + ".");
               if (var5) {
                  var3.getValue().remove(var4);
               }

               if (var1.startsWith(var2 + ".")) {
                  var3.getValue().remove(var4);
                  var5 = true;
                  if (this.lX(var2) instanceof MappingNode var6 && var6.getValue().isEmpty()) {
                     method7518(var6, var4.getValueNode());
                     MappingNode var11 = this.method7513(var2, false);
                     NodeTuple var8 = method7514(var11, lZ(var2));
                     method7518(var8.getKeyNode(), var4.getKeyNode());
                     var11.getValue().remove(var8);
                  }
               }

               MappingNode var10 = this.method7513(var2, true);
               NodeTuple var12 = method7514(var10, lZ(var2));
               if (var12 == null) {
                  ScalarNode var13 = (ScalarNode)this.bNW.represent(lZ(var2));
                  method7518(var4.getKeyNode(), var13);
                  var10.getValue().add(new NodeTuple(var13, var4.getValueNode()));
               } else {
                  method7518(var4.getKeyNode(), var12.getKeyNode());
                  this.method7508(var4.getValueNode(), var12.getValueNode(), var2);
               }

               var3.getValue().remove(var4);
               this.bLS = true;
            }
         }
      }
   }

   private void method7508(Node var1, Node var2, String var3) {
      if (var1 instanceof MappingNode var4 && var2 instanceof MappingNode var5) {
         for (NodeTuple var7 : var4.getValue()) {
            String var8 = ((ScalarNode)var7.getKeyNode()).getValue();
            NodeTuple var9 = method7514(var5, var8);
            if (var9 == null) {
               var5.getValue().add(var7);
            } else {
               method7518(var7.getKeyNode(), var9.getKeyNode());
               this.method7508(var7.getValueNode(), var9.getValueNode(), var3 + "." + var8);
            }
         }
      } else if (var1 instanceof MappingNode != (var2 instanceof MappingNode)) {
         if (var1 instanceof MappingNode var6 && var6.getValue().isEmpty()) {
            method7518(var1, var2);
            return;
         }

         throw new IllegalArgumentException("Conflicting section/scalar during migration: " + var3);
      }

      method7518(var1, var2);
   }

   void method7509(YamlNodeDocument var1) {
      this.method7510(this.bNX, var1.bNX, "");
      this.method7519("resource_pack.custom_model_data_starting_value", Tag.INT, "integer");
      this.method7519("recipes.remove_vanilla_recipes.list", Tag.BOOL, "boolean");
   }

   private void method7510(MappingNode var1, MappingNode var2, String var3) {
      for (NodeTuple var5 : var2.getValue()) {
         String var6 = ((ScalarNode)var5.getKeyNode()).getValue();
         String var7 = var3.isEmpty() ? var6 : var3 + "." + var6;
         NodeTuple var8 = method7514(var1, var6);
         if (var8 != null) {
            if (method7515(var5.getKeyNode(), var8.getKeyNode())) {
               this.bLS = true;
            }

            Node var9 = var8.getValueNode();
            Node var10 = var5.getValueNode();
            if (!(var10 instanceof MappingNode var11)) {
               boolean var17 = var7.equals("registry_changes.rejoin.port") && var10.getTag().equals(Tag.STR) && var9.getTag().equals(Tag.INT);
               if (!var10.getTag().equals(var9.getTag()) && !var17 && (!var10.getTag().equals(Tag.FLOAT) || !var9.getTag().equals(Tag.INT))) {
                  throw new IllegalArgumentException("Invalid type for config key: " + var7);
               }

               if (var9.getTag().equals(Tag.INT)) {
                  method7520((Number)this.method7502(var9), var7);
               }

               if (var9 instanceof SequenceNode var13 && var10 instanceof SequenceNode) {
                  if (var7.equals("resource_pack.font.custom.shift")) {
                     if (var13.getValue().size() != 2
                        || var13.getValue()
                           .stream()
                           .anyMatch(var1x -> !(this.method7502(var1x) instanceof Number var2x && Double.isFinite(var2x.doubleValue())))) {
                        throw new IllegalArgumentException("Expected two finite numbers: " + var7);
                     }
                  } else {
                     for (Node var16 : var13.getValue()) {
                        if (!(var16 instanceof ScalarNode var14) || !var14.getTag().equals(Tag.STR)) {
                           throw new IllegalArgumentException("Expected a list of strings: " + var7);
                        }
                     }
                  }
               }

               if (var9.getTag().equals(Tag.FLOAT) && !Double.isFinite(((Number)this.kf(var7)).doubleValue())) {
                  throw new IllegalArgumentException("Expected a finite number: " + var7);
               }
            } else {
               if (!(var9 instanceof MappingNode var12)) {
                  throw new IllegalArgumentException("Expected a section: " + var7);
               }

               this.method7510(var12, var11, var7);
            }
         } else {
            var1.getValue().add(var5);
            this.bLS = true;
         }
      }
   }

   void method7511(YamlNodeDocument var1) {
      this.method7512(this.bNX, var1.bNX);
   }

   private void method7512(MappingNode var1, MappingNode var2) {
      ArrayList var3 = new ArrayList();

      for (NodeTuple var5 : var2.getValue()) {
         NodeTuple var6 = method7514(var1, ((ScalarNode)var5.getKeyNode()).getValue());
         if (var6 != null) {
            var3.add(var6);
            if (var6.getValueNode() instanceof MappingNode var7 && var5.getValueNode() instanceof MappingNode var8) {
               this.method7512(var7, var8);
            }
         }
      }

      for (NodeTuple var11 : var1.getValue()) {
         if (!var3.contains(var11)) {
            var3.add(var11);
         }
      }

      var1.setValue(var3);
   }

   String acZ() {
      StringWriter var1 = new StringWriter();
      this.bNW.serialize(this.bNX, var1);
      return var1.toString();
   }

   private MappingNode method7513(String var1, boolean var2) {
      String[] var3 = var1.split("\\.");
      MappingNode var4 = this.bNX;

      for (int var5 = 0; var5 < var3.length - 1; var5++) {
         NodeTuple var6 = method7514(var4, var3[var5]);
         if (var6 == null) {
            if (!var2) {
               return null;
            }

            MappingNode var8 = (MappingNode)this.bNW.represent(new LinkedHashMap());
            var4.getValue().add(new NodeTuple(this.bNW.represent(var3[var5]), var8));
            var4 = var8;
         } else {
            if (!(var6.getValueNode() instanceof MappingNode var7)) {
               if (!var2) {
                  return null;
               }

               throw new IllegalArgumentException("Expected a section: " + String.join(".", Arrays.copyOf(var3, var5 + 1)));
            }

            var4 = var7;
         }
      }

      return var4;
   }

   private static NodeTuple method7514(MappingNode var0, String var1) {
      for (NodeTuple var3 : var0.getValue()) {
         if (((ScalarNode)var3.getKeyNode()).getValue().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   private static String lZ(String var0) {
      return var0.substring(var0.lastIndexOf(46) + 1);
   }

   private static boolean method7515(Node var0, Node var1) {
      List var2 = var1.getBlockComments();
      List var3 = method7516(var2, var0.getBlockComments());
      if (var3.size() == (var2 == null ? 0 : var2.size())) {
         return false;
      }

      var1.setBlockComments(var3);
      return true;
   }

   private static List method7516(List var0, List var1) {
      ArrayList var2 = new ArrayList(var0 == null ? List.of() : var0);
      boolean[] var3 = new boolean[var2.size()];
      if (var1 != null) {
         for (CommentLine var5 : var1) {
            int var6 = -1;

            for (int var7 = 0; var7 < var3.length; var7++) {
               CommentLine var8 = (CommentLine)var2.get(var7);
               if (!var3[var7] && var8.getCommentType() == var5.getCommentType() && var8.getValue().equals(var5.getValue())) {
                  var6 = var7;
                  break;
               }
            }

            if (var6 < 0) {
               var2.add(var5);
            } else {
               var3[var6] = true;
            }
         }
      }

      return var2;
   }

   private static List method7517(List var0, List var1) {
      ArrayList var2 = new ArrayList(var0 == null ? List.of() : var0);
      if (var1 != null) {
         var2.addAll(var1);
      }

      return var2;
   }

   private static void method7518(Node var0, Node var1) {
      if (var0 instanceof ScalarNode && var1 instanceof SequenceNode var2) {
         var2.setFlowStyle(FlowStyle.FLOW);
      }

      var1.setBlockComments(method7517(var1.getBlockComments(), var0.getBlockComments()));
      var1.setInLineComments(method7517(var1.getInLineComments(), var0.getInLineComments()));
      var1.setEndComments(method7517(var1.getEndComments(), var0.getEndComments()));
   }

   private void method7519(String var1, Tag var2, String var3) {
      if (this.lX(var1) instanceof MappingNode var4) {
         for (NodeTuple var6 : var4.getValue()) {
            String var7 = ((ScalarNode)var6.getKeyNode()).getValue();
            Node var8 = var6.getValueNode();
            if (!(var8 instanceof ScalarNode) || !var8.getTag().equals(var2)) {
               throw new IllegalArgumentException("Expected " + var3 + ": " + var1 + "." + var7);
            }

            if (var2.equals(Tag.INT)) {
               method7520((Number)this.method7502(var8), var1 + "." + var7);
            }
         }
      }
   }

   private static int method7520(Number var0, String var1) {
      try {
         return new BigDecimal(var0.toString()).intValueExact();
      } catch (ArithmeticException | NumberFormatException var3) {
         throw new IllegalArgumentException("Integer out of range: " + var1, var3);
      }
   }

   private static void method7521(Node var0, List var1) {
      if (var0.getBlockComments() != null) {
         var1.addAll(var0.getBlockComments());
      }

      if (var0.getInLineComments() != null) {
         var1.addAll(var0.getInLineComments());
      }

      if (var0.getEndComments() != null) {
         var1.addAll(var0.getEndComments());
      }

      if (var0 instanceof MappingNode var2) {
         for (NodeTuple var4 : var2.getValue()) {
            method7521(var4.getKeyNode(), var1);
            method7521(var4.getValueNode(), var1);
         }
      }

      if (var0 instanceof SequenceNode var5) {
         for (Node var7 : var5.getValue()) {
            method7521(var7, var1);
         }
      }
   }
}
