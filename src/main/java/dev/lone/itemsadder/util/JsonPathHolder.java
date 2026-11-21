package dev.lone.itemsadder.util;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;
import dev.lone.itemsadder.utils.json.Fastjson2JsonProvider;

final class JsonPathHolder {
   private static final ParseContext PARSE_CONTEXT = JsonPath.using(
      Configuration.builder().jsonProvider(new Fastjson2JsonProvider()).options(new Option[]{Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS}).build()
   );

   private JsonPathHolder() {
   }
}
