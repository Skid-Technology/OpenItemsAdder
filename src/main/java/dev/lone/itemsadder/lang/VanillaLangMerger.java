package dev.lone.itemsadder.lang;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter.Feature;
import dev.lone.itemsadder.config.Settings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.util.Log;
import java.io.File;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;

public class VanillaLangMerger {
   JSONObject Cy = new JSONObject();

   public VanillaLangMerger(String var1) {
      for (File var4 : ContentManager.method8201("minecraft/lang/" + var1 + ".json")) {
         try {
            JSONObject var5 = JSON.parseObject(FileUtils.readFileToString(var4, StandardCharsets.UTF_8.toString()));
            this.Cy.putAll(var5);
         } catch (Throwable var6) {
            Log.error("Error opening minecraft lang file. Please make sure it's valid. File: " + var4.getAbsolutePath(), var6);
         }
      }
   }

   public void method2036(String var1, String var2) {
      this.Cy.put(var1, var2);
   }

   public String HB() {
      return Settings.get().compressJson ? JSON.toJSONString(this.Cy, new Feature[]{Feature.MapSortField, Feature.PrettyFormat}) : JSON.toJSONString(this.Cy);
   }
}
