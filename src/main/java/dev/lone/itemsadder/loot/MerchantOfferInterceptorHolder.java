package dev.lone.itemsadder.loot;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.lone.itemsadder.nms.NmsRegistry;
import java.util.concurrent.TimeUnit;

public final class MerchantOfferInterceptorHolder {
   public MerchantOfferInterceptor bpT = (MerchantOfferInterceptor)NmsRegistry.method2777(MerchantOfferInterceptor.class);
   static MerchantOfferInterceptorHolder bpU;
   public static Cache bpV = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.MINUTES).build();

   MerchantOfferInterceptorHolder() {
   }

   public static MerchantOfferInterceptor Qh() {
      if (bpU == null || bpU.bpT == null) {
         bpU = new MerchantOfferInterceptorHolder();
      }

      return bpU.bpT;
   }
}
