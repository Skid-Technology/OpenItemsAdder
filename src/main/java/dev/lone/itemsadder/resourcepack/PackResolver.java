package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

final class PackResolver {
   private final Map bBo;
   private final Set bBp;
   private final Set bBq;
   private final Predicate bBr;
   private final Function bBs;
   private final Map bBt;
   private final Map bBu;
   private final Map bBv;

   private PackResolver(ResourcePackGenerator var1, Predicate var2, Function var3) {
      this.bBw = var1;
      this.bBt = new HashMap();
      this.bBu = new PackResolverCache(this, 64, 0.75F, true);
      this.bBv = new HashMap();
      this.bBr = var2;
      this.bBs = var3;
      this.bBp = var1.modelJsonCache
         .keySet()
         .stream()
         .map(PackManifest::normalizeModelId)
         .map(ItemContextBuilder::normalizeId)
         .collect(Collectors.toUnmodifiableSet());
      this.bBq = Set.copyOf(var1.itemContextIndex.resolvedNames().values());
      this.bBo = new HashMap();
      var1.modernModelCache.keySet().forEach(var2x -> {
         var1.buildMetrics.bAT++;
         String var3x = ItemContextBuilder.normalizeId(PackManifest.normalizeModelId(var2x));
         String var4 = ItemContextBuilder.modelJsonPath(var3x);
         int var5 = var2x.equals("ia_overlay_modern_atlas/" + var4) ? 2 : (var2x.equals(var4) ? 1 : 0);
         this.bBo.merge(var3x, new PathWithIndex(var2x, var5), (var0, var1xx) -> (PathWithIndex)(var1xx.priority() > var0.priority() ? var1xx : var0));
      });
   }

   private PackResolutionEntry resolveEntry(String var1) {
      this.bBw.buildMetrics.bAY++;
      String var2 = ItemContextBuilder.normalizeId(var1);
      PackResolutionEntry var3 = (PackResolutionEntry)this.bBu.get(var2);
      if (var3 != null) {
         return var3;
      }

      this.bBw.buildMetrics.bAZ++;
      PathWithIndex var4 = (PathWithIndex)this.bBo.get(var2);
      PackMetadata var5 = var4 == null ? null : ((PackFileCache)this.bBw.modernModelCache).getMetadata(var4.path());
      PackResolutionEntry var6;
      if (var5 != null) {
         var6 = new PackResolutionEntry(var5, this.resolveKind(var2));
      } else {
         JSONObject var7 = (JSONObject)this.bBs.apply(var2);
         var5 = var7 == null ? null : PackMetadata.fromModelJson(var7);
         var6 = new PackResolutionEntry(var5, var5 == null ? PackResolutionKind.ORIGINAL_UNRESOLVED : PackResolutionKind.CACHED_VANILLA_ASSET);
      }

      this.bBu.put(var2, var6);
      this.bBw.buildMetrics.bBa = Math.max(this.bBw.buildMetrics.bBa, this.bBu.size());
      return var6;
   }

   private JSONObject resolveJson(String var1) {
      String var2 = ItemContextBuilder.normalizeId(var1);
      PathWithIndex var3 = (PathWithIndex)this.bBo.get(var2);
      return var3 == null ? (JSONObject)this.bBs.apply(var2) : (JSONObject)this.bBw.modernModelCache.get(var3.path());
   }

   private boolean matchesFilter(String var1) {
      String var2 = ItemContextBuilder.normalizeId(var1);
      return this.bBv.computeIfAbsent(var2, this.bBr::test);
   }

   private PackResolutionKind resolveKind(String var1) {
      this.bBw.buildMetrics.bAW++;
      String var2 = ItemContextBuilder.normalizeId(var1);
      PackResolutionKind var3 = (PackResolutionKind)this.bBt.get(var2);
      if (var3 != null) {
         return var3;
      }

      this.bBw.buildMetrics.bAX++;
      PackResolutionKind var4 = this.bBq.contains(var2)
         ? PackResolutionKind.PLANNER_ITEM_VARIANT
         : (this.bBw.bAk.contains(var2) ? PackResolutionKind.GENERATED_ITEMSADDER_ENTRY : PackResolutionKind.SELECTED_PACK_ENTRY);
      this.bBt.put(var2, var4);
      return var4;
   }

   private boolean isPlannedModel(String var1) {
      return this.bBp.contains(var1);
   }
}
