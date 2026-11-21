package dev.lone.itemsadder.resourcepack;

import beer.devs.fastnbt.nms.Version;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.resourcepack.generator.file.data.JsonAsset;
import dev.lone.itemsadder.resourcepack.generator.file.data.JsonAsset.MissingAssetException;
import dev.lone.itemsadder.util.IaRuntimeException;

public class VersionedJsonAsset extends JsonAsset {
   public VersionedJsonAsset(String var1) {
      this(var1, Version.get());
   }

   public VersionedJsonAsset(String var1, Version var2) {
      try {
         this.asL = var1;
         this.bIG = ContentManager.method8084().field91.readJson(var1, var2);
         if (this.bIG == null) {
            this.bIH = new MissingAssetException("OriginalAsset: No vanilla resource: '" + var1 + "'");
         }
      } catch (IaRuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         this.bIH = var5;
      }
   }
}
