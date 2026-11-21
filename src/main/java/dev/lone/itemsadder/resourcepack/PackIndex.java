package dev.lone.itemsadder.resourcepack;

import com.alibaba.fastjson2.JSONObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

final class PackIndex {
   final Map buh = new LinkedHashMap();
   final Map bui = new HashMap();
   final JSONObject buj;

   PackIndex(JSONObject var1) {
      this.buj = var1;
   }
}
