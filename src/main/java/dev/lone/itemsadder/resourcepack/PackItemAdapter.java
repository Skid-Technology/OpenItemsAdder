package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.item.TexturedItemBase;
import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.Utils;
import java.io.File;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public class PackItemAdapter {
   @Nullable
   TexturedItemBase bIg;
   private final boolean bIh;

   public PackItemAdapter(TexturedItemBase var1) {
      this.bIh = var1.getModelPath().startsWith("minecraft:");
      if (!this.bIh) {
         this.bIg = var1;
      }
   }

   @Nullable
   public PackManifest buildManifest() {
      if (!this.bIh && this.bIg != null) {
         String var2 = this.bIg.getModelPath();
         String var1;
         if (var2.contains(":")) {
            String[] var3 = var2.split(":");
            var1 = var3[0] + "/models/" + var3[1] + ".json";
         } else {
            var1 = this.bIg.getNamespace() + "/models/" + var2 + ".json";
         }

         if (var1.contains("ia_auto")) {
            return null;
         }

         File var17 = ContentManager.method8198(var1);
         if (var17 == null) {
            Log.error(
               "Model file '"
                  + var1
                  + "' not found in any pack for item '"
                  + this.bIg.getNamespacedID()
                  + "'. File: "
                  + (this.bIg.ape != null ? this.bIg.ape.DJ() : "unknown")
            );
            return null;
         }

         try {
            JSONObject var4 = JsonHelper.readJsonFile(var17);
            PackManifest var5 = new PackConfigProcessor("assets/" + var1).process(var4);
            if ((this.bIg.apf == Material.POTION || this.bIg.apf == Material.SPLASH_POTION || this.bIg.apf == Material.LINGERING_POTION)
               && var4.containsKey("elements")) {
               for (Object var8 : var4.getJSONArray("elements")) {
                  JSONObject var9 = (JSONObject)var8;
                  if (var9.containsKey("faces")) {
                     JSONObject var10 = var9.getJSONObject("faces");
                     String[] var11 = new String[]{"down", "east", "north", "south", "up", "west"};

                     for (String var15 : var11) {
                        if (var10.containsKey(var15)) {
                           var10.getJSONObject(var15).put("tintindex", 0);
                        }
                     }
                  }
               }
            }

            return var5;
         } catch (Throwable var16) {
            Log.error("Failed to validate model: " + Utils.shortPath(var17));
            Log.error(var16.getMessage());
            return null;
         }
      } else {
         return null;
      }
   }
}
