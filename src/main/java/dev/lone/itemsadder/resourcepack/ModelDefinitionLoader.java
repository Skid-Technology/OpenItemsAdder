package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.item.ItemSpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ModelDefinitionLoader {
   private final ItemSpec bHy;

   public ModelDefinitionLoader(ItemSpec var1) {
      this.bHy = Objects.requireNonNull(var1, "CustomItem cannot be null");
   }

   public ModelDefinitionBundle loadBundle() {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.IllegalStateException: Invalid switch case set: [[const(0)], [const(1)], [const(2)], [const(3)], [const(4)], [const(5)], [const(null), null]] for selector of type Lorg/bukkit/Material;
      //   at org.jetbrains.java.decompiler.modules.decompiler.exps.SwitchHeadExprent.checkExprTypeBounds(SwitchHeadExprent.java:63)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.checkTypeExpr(VarTypeProcessor.java:156)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.checkTypeExprent(VarTypeProcessor.java:137)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.lambda$processVarTypes$2(VarTypeProcessor.java:125)
      //   at org.jetbrains.java.decompiler.modules.decompiler.flow.DirectGraph.iterateExprents(DirectGraph.java:114)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.processVarTypes(VarTypeProcessor.java:125)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarTypeProcessor.calculateVarTypes(VarTypeProcessor.java:44)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarVersionsProcessor.setVarVersions(VarVersionsProcessor.java:68)
      //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarProcessor.setVarVersions(VarProcessor.java:47)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:320)
      //
      // Bytecode:
      // 00: aload 0
      // 01: getfield dev/lone/itemsadder/resourcepack/ModelDefinitionLoader.bHy Ldev/lone/itemsadder/item/ItemSpec;
      // 04: ldc "Item graphics cannot be null"
      // 06: invokestatic java/util/Objects.requireNonNull (Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
      // 09: checkcast dev/lone/itemsadder/item/ItemSpec
      // 0c: astore 1
      // 0d: aload 1
      // 0e: invokevirtual dev/lone/itemsadder/item/ItemSpec.Ep ()Lorg/bukkit/Material;
      // 11: invokestatic dev/lone/itemsadder/item/ItemStackUtils.ac (Lorg/bukkit/Material;)Z
      // 14: ifeq 24
      // 17: aload 0
      // 18: new dev/lone/itemsadder/resourcepack/ModelConverterSimple
      // 1b: dup
      // 1c: aload 1
      // 1d: invokespecial dev/lone/itemsadder/resourcepack/ModelConverterSimple.<init> (Ldev/lone/itemsadder/item/ItemSpec;)V
      // 20: invokevirtual dev/lone/itemsadder/resourcepack/ModelDefinitionLoader.buildBundle (Ldev/lone/itemsadder/resourcepack/ModelConverterBase;)Ldev/lone/itemsadder/resourcepack/ModelDefinitionBundle;
      // 23: areturn
      // 24: aload 1
      // 25: invokevirtual dev/lone/itemsadder/item/ItemSpec.Ep ()Lorg/bukkit/Material;
      // 28: ifnull 47
      // 2b: aload 1
      // 2c: invokevirtual dev/lone/itemsadder/item/ItemSpec.Ep ()Lorg/bukkit/Material;
      // 2f: invokevirtual org/bukkit/Material.name ()Ljava/lang/String;
      // 32: ldc "_SPEAR"
      // 34: invokevirtual java/lang/String.endsWith (Ljava/lang/String;)Z
      // 37: ifeq 47
      // 3a: aload 0
      // 3b: new dev/lone/itemsadder/resourcepack/ModelConverterBlockStates
      // 3e: dup
      // 3f: aload 1
      // 40: invokespecial dev/lone/itemsadder/resourcepack/ModelConverterBlockStates.<init> (Ldev/lone/itemsadder/item/ItemSpec;)V
      // 43: invokevirtual dev/lone/itemsadder/resourcepack/ModelDefinitionLoader.buildBundle (Ldev/lone/itemsadder/resourcepack/ModelConverterBase;)Ldev/lone/itemsadder/resourcepack/ModelDefinitionBundle;
      // 46: areturn
      // 47: aload 1
      // 48: invokevirtual dev/lone/itemsadder/item/ItemSpec.Ep ()Lorg/bukkit/Material;
      // 4b: astore 2
      // 4c: bipush 0
      // 4d: istore 3
      // 4e: aload 2
      // 4f: iload 3
      // 50: invokedynamic typeSwitch (Ljava/lang/Object;I)I bsm=java/lang/runtime/SwitchBootstraps.typeSwitch (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite; args=[ null.invoke Ljava/lang/Enum$EnumDesc;, null.invoke Ljava/lang/Enum$EnumDesc;, null.invoke Ljava/lang/Enum$EnumDesc;, null.invoke Ljava/lang/Enum$EnumDesc;, null.invoke Ljava/lang/Enum$EnumDesc;, null.invoke Ljava/lang/Enum$EnumDesc; ]
      // 55: tableswitch 133 -1 5 133 43 58 73 88 103 118
      // 80: aload 0
      // 81: new dev/lone/itemsadder/resourcepack/ModelConverterSorted
      // 84: dup
      // 85: aload 1
      // 86: invokespecial dev/lone/itemsadder/resourcepack/ModelConverterSorted.<init> (Ldev/lone/itemsadder/item/ItemSpec;)V
      // 89: invokevirtual dev/lone/itemsadder/resourcepack/ModelDefinitionLoader.buildBundle (Ldev/lone/itemsadder/resourcepack/ModelConverterBase;)Ldev/lone/itemsadder/resourcepack/ModelDefinitionBundle;
      // 8c: goto e6
      // 8f: aload 0
      // 90: new dev/lone/itemsadder/resourcepack/ModelConverterJsonBuilder
      // 93: dup
      // 94: aload 1
      // 95: invokespecial dev/lone/itemsadder/resourcepack/ModelConverterJsonBuilder.<init> (Ldev/lone/itemsadder/item/ItemSpec;)V
      // 98: invokevirtual dev/lone/itemsadder/resourcepack/ModelDefinitionLoader.buildBundle (Ldev/lone/itemsadder/resourcepack/ModelConverterBase;)Ldev/lone/itemsadder/resourcepack/ModelDefinitionBundle;
      // 9b: goto e6
      // 9e: aload 0
      // 9f: new dev/lone/itemsadder/resourcepack/ModelConverterElements
      // a2: dup
      // a3: aload 1
      // a4: invokespecial dev/lone/itemsadder/resourcepack/ModelConverterElements.<init> (Ldev/lone/itemsadder/item/ItemSpec;)V
      // a7: invokevirtual dev/lone/itemsadder/resourcepack/ModelDefinitionLoader.buildBundle (Ldev/lone/itemsadder/resourcepack/ModelConverterBase;)Ldev/lone/itemsadder/resourcepack/ModelDefinitionBundle;
      // aa: goto e6
      // ad: aload 0
      // ae: new dev/lone/itemsadder/resourcepack/ModelConverterOrdered
      // b1: dup
      // b2: aload 1
      // b3: invokespecial dev/lone/itemsadder/resourcepack/ModelConverterOrdered.<init> (Ldev/lone/itemsadder/item/ItemSpec;)V
      // b6: invokevirtual dev/lone/itemsadder/resourcepack/ModelDefinitionLoader.buildBundle (Ldev/lone/itemsadder/resourcepack/ModelConverterBase;)Ldev/lone/itemsadder/resourcepack/ModelDefinitionBundle;
      // b9: goto e6
      // bc: aload 0
      // bd: new dev/lone/itemsadder/resourcepack/ModelConverterBlockFaces
      // c0: dup
      // c1: aload 1
      // c2: invokespecial dev/lone/itemsadder/resourcepack/ModelConverterBlockFaces.<init> (Ldev/lone/itemsadder/item/ItemSpec;)V
      // c5: invokevirtual dev/lone/itemsadder/resourcepack/ModelDefinitionLoader.buildBundle (Ldev/lone/itemsadder/resourcepack/ModelConverterBase;)Ldev/lone/itemsadder/resourcepack/ModelDefinitionBundle;
      // c8: goto e6
      // cb: aload 0
      // cc: new dev/lone/itemsadder/resourcepack/ModelConverterMerged
      // cf: dup
      // d0: aload 1
      // d1: invokespecial dev/lone/itemsadder/resourcepack/ModelConverterMerged.<init> (Ldev/lone/itemsadder/item/ItemSpec;)V
      // d4: invokevirtual dev/lone/itemsadder/resourcepack/ModelDefinitionLoader.buildBundle (Ldev/lone/itemsadder/resourcepack/ModelConverterBase;)Ldev/lone/itemsadder/resourcepack/ModelDefinitionBundle;
      // d7: goto e6
      // da: aload 0
      // db: new dev/lone/itemsadder/resourcepack/ModelConverterMapped
      // de: dup
      // df: aload 1
      // e0: invokespecial dev/lone/itemsadder/resourcepack/ModelConverterMapped.<init> (Ldev/lone/itemsadder/item/ItemSpec;)V
      // e3: invokevirtual dev/lone/itemsadder/resourcepack/ModelDefinitionLoader.buildBundle (Ldev/lone/itemsadder/resourcepack/ModelConverterBase;)Ldev/lone/itemsadder/resourcepack/ModelDefinitionBundle;
      // e6: areturn
   }

   private ModelDefinitionBundle buildBundle(ModelConverterBase var1) {
      ModelConversionResult var2 = var1.convert();
      List var3 = Collections.emptyList();
      if (var2.bHr != null) {
         var3 = processModelConfig(var2.bHr.bHp, var2.bHr.bHq);
      }

      ArrayList var4 = new ArrayList();
      var2.bHs.forEach((var1x, var2x) -> {
         if (!var2x.isVanillaReference()) {
            var4.addAll(processModelConfig(var2x.bHp, var2x.bHq));
         }
      });
      List var5 = Collections.emptyList();
      if (var2.bHt != null) {
         var5 = processModelConfig(this.bHy.Em(), var2.bHt);
      }

      ArrayList var6 = new ArrayList(var4);
      var6.addAll(var3);
      var6.addAll(var5);
      return new ModelDefinitionBundle(var2.bHr, var2.bHs, var2.bHt, var6, var2.bHu, var2.bHv);
   }

   private static List processModelConfig(String var0, JSONObject var1) {
      return new PackConfigProcessor(var0).process(var1).bIM;
   }
}
