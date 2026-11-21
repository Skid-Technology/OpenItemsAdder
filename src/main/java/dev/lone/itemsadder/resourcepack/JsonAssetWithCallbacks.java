package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONReader;
import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.resourcepack.generator.file.data.JsonAsset;
import dev.lone.itemsadder.resourcepack.generator.file.data.JsonAsset.MissingAssetException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class JsonAssetWithCallbacks extends JsonAsset {
   public JsonAssetWithCallbacks(String var1) {
      InputStream var2 = Main.getInstance().getResource(var1);
      if (var2 == null) {
         this.bIH = new MissingAssetException("OriginalAsset: No vanilla model file: '" + var1 + "'");
      } else {
         try {
            this.asL = var1;
            JSONReader var3 = JSONReader.of(new InputStreamReader(var2, StandardCharsets.UTF_8));
            this.bIG = var3.readAny();
            var3.close();
         } catch (Exception var4) {
            this.bIH = var4;
         }
      }
   }

   public static void loadWithCallbacks(String var0, Consumer var1, Consumer var2) {
      JsonAssetWithCallbacks var3 = new JsonAssetWithCallbacks(var0);
      if (var3.hasError()) {
         var2.accept(var3);
      } else {
         var1.accept(var3);
      }
   }
}
