package dev.lone.itemsadder.entity;

import java.util.Arrays;
import lombok.Generated;
import org.bukkit.entity.EntityType;

public class EntityMatcherRootO extends EntityMatcherBase {
   public final double ZV;
   public final int ZW;
   public final float ZX;

   public static boolean matchesType(EntityType var0) {
      return Arrays.asList(getTypes()).contains(var0);
   }

   public static EntityType[] getTypes() {
      return new EntityType[]{
         EntityType.BOGGED,
         EntityType.DROWNED,
         EntityType.ILLUSIONER,
         EntityType.LLAMA,
         EntityType.PIGLIN,
         EntityType.PILLAGER,
         EntityType.SKELETON,
         EntityType.SNOW_GOLEM,
         EntityType.STRAY,
         EntityType.TRADER_LLAMA,
         EntityType.WITCH,
         EntityType.WITHER,
         EntityType.WITHER_SKELETON
      };
   }

   @Generated
   protected EntityMatcherRootO(MatcherNodeO var1) {
      super(var1);
      this.ZV = var1.Ze;
      this.ZW = var1.ZW;
      this.ZX = var1.ZX;
   }

   @Generated
   public static MatcherNodeO getNode() {
      return new MatcherNodeOImpl();
   }
}
