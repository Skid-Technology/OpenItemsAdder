package dev.lone.itemsadder.item.conversion;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.OptionalDynamic;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import net.minecraft.util.datafix.schemas.DataConverterSchemaNamed;

public class ItemConversionNodeH {
   private final String bbc;
   private final int bbd;
   private Dynamic aZY;
   private final Dynamic bbe;
   Dynamic baa;

   public ItemConversionNodeH(String var1, int var2, Dynamic var3) {
      this.bbc = DataConverterSchemaNamed.a(var1);
      this.bbd = var2;
      this.aZY = var3.emptyMap();
      this.baa = var3.get("tag").orElseEmptyMap();
      this.bbe = var3.remove("tag");
   }

   public static Optional method4080(Dynamic var0) {
      return var0.get("id")
         .asString()
         .apply2stable((var1, var2) -> new ItemConversionNodeH(var1, var2.intValue(), var0.remove("id").remove("Count")), var0.get("Count").asNumber())
         .result();
   }

   public OptionalDynamic fa(String var1) {
      OptionalDynamic var2 = this.baa.get(var1);
      this.baa = this.baa.remove(var1);
      return var2;
   }

   public void method4081(String var1, Dynamic var2) {
      this.aZY = this.aZY.set(var1, var2);
   }

   public void method4082(String var1, OptionalDynamic var2) {
      var2.result().ifPresent(var2x -> this.aZY = this.aZY.set(var1, var2x));
   }

   public Dynamic method4083(String var1, Dynamic var2, String var3) {
      Optional var4 = this.fa(var1).result();
      return var4.isPresent() ? var2.set(var3, (Dynamic)var4.get()) : var2;
   }

   public void method4084(String var1, String var2, Dynamic var3) {
      Optional var4 = this.fa(var1).result();
      if (var4.isPresent() && !((Dynamic)var4.get()).equals(var3)) {
         this.method4081(var2, (Dynamic)var4.get());
      }
   }

   public void method4085(String var1, String var2) {
      this.fa(var1).result().ifPresent(var2x -> this.method4081(var2, var2x));
   }

   public void method4086(String var1, boolean var2, UnaryOperator var3) {
      OptionalDynamic var4 = this.baa.get(var1);
      if (!var2 || !var4.result().isEmpty()) {
         Dynamic var5 = var4.orElseEmptyMap();
         var5 = var3.apply(var5);
         if (var5.equals(var5.emptyMap())) {
            this.baa = this.baa.remove(var1);
         } else {
            this.baa = this.baa.set(var1, var5);
         }
      }
   }

   public Dynamic OR() {
      Dynamic var1 = this.baa.emptyMap().set("id", this.baa.createString(this.bbc)).set("count", this.baa.createInt(this.bbd));
      if (!this.baa.equals(this.baa.emptyMap())) {
         this.aZY = this.aZY.set("minecraft:custom_data", this.baa);
      }

      if (!this.aZY.equals(this.baa.emptyMap())) {
         var1 = var1.set("components", this.aZY);
      }

      return method4087(var1, this.bbe);
   }

   private static Dynamic method4087(Dynamic var0, Dynamic var1) {
      DynamicOps var2 = var0.getOps();
      return var2.getMap(var0.getValue())
         .flatMap(var2x -> var2.mergeToMap(var1.convert(var2).getValue(), var2x))
         .map(var1x -> new Dynamic(var2, var1x))
         .result()
         .orElse(var0);
   }

   public boolean fb(String var1) {
      return this.bbc.equals(var1);
   }

   public boolean method4088(Set var1) {
      return var1.contains(this.bbc);
   }
}
