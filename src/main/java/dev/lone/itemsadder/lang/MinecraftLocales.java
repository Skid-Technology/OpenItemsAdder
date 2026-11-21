package dev.lone.itemsadder.lang;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.config.Settings;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class MinecraftLocales {
   public static final String aus = "itemsadder.container.inventory";
   public static final List aut = Arrays.asList(
      "af_za",
      "ar_sa",
      "ast_es",
      "az_az",
      "bar",
      "be_by",
      "bg_bg",
      "brb",
      "br_fr",
      "bs_ba",
      "ca_es",
      "cs_cz",
      "cy_gb",
      "da_dk",
      "de_at",
      "de_ch",
      "de_de",
      "el_gr",
      "enp",
      "enws",
      "en_au",
      "en_ca",
      "en_gb",
      "en_nz",
      "en_pt",
      "en_ud",
      "en_us",
      "eo_uy",
      "es_ar",
      "es_cl",
      "es_es",
      "es_mx",
      "es_uy",
      "es_ve",
      "et_ee",
      "eu_es",
      "fa_ir",
      "fil_ph",
      "fi_fi",
      "fo_fo",
      "fra_de",
      "fr_ca",
      "fr_fr",
      "fy_nl",
      "ga_ie",
      "gd_gb",
      "gl_es",
      "gv_im",
      "haw_us",
      "he_il",
      "hi_in",
      "hr_hr",
      "hu_hu",
      "hy_am",
      "id_id",
      "ig_ng",
      "io_en",
      "is_is",
      "it_it",
      "ja_jp",
      "jbo_en",
      "kab_kab",
      "ka_ge",
      "kk_kz",
      "kn_in",
      "ko_kr",
      "ksh",
      "kw_gb",
      "la_la",
      "lb_lu",
      "li_li",
      "lol_us",
      "lt_lt",
      "lv_lv",
      "mi_nz",
      "mk_mk",
      "mn_mn",
      "moh_ca",
      "ms_my",
      "mt_mt",
      "nds_de",
      "nl_be",
      "nl_nl",
      "nn_no",
      "no_no",
      "nuk",
      "oc_fr",
      "oj_ca",
      "ovd",
      "pl_pl",
      "pt_br",
      "pt_pt",
      "qya_aa",
      "ro_ro",
      "ru_ru",
      "se_no",
      "sk_sk",
      "sl_si",
      "so_so",
      "sq_al",
      "sr_sp",
      "sv_se",
      "swg",
      "sxu",
      "szl",
      "ta_in",
      "th_th",
      "tlh_aa",
      "tr_tr",
      "tt_ru",
      "tzl_tzl",
      "uk_ua",
      "val_es",
      "vec_it",
      "vi_vn",
      "yo_ng",
      "zh_cn",
      "zh_tw",
      "ba_ru",
      "be_latn",
      "cv_cu",
      "es_ec",
      "esan",
      "fr_ch",
      "fur_it",
      "go_fr",
      "got_de",
      "hal_ua",
      "hn_no",
      "isv",
      "ky_kg",
      "lmo",
      "lo_la",
      "lzh",
      "nah",
      "pls",
      "qcb_es",
      "qid",
      "rpr",
      "ry_ua",
      "sah_sah",
      "sr_cs",
      "tl_ph",
      "tok",
      "tzo_mx",
      "uz_uz",
      "vp_vl",
      "vro",
      "yi_de",
      "zh_hk",
      "zlm_arab"
   );
   private HashMap auu = new HashMap();

   public void method2037(String var1, String var2, String var3) {
      VanillaLangMerger var4 = this.auu.computeIfAbsent(var1, var1x -> new VanillaLangMerger(var1));
      var4.method2036(var2, var3);
   }

   public HashMap HC() {
      HashMap var1 = new HashMap();
      this.auu.forEach((var1x, var2) -> var1.put(var1x, var2.HB()));
      method2038(var1, Settings.get().inventoryTitle);
      return var1;
   }

   static void method2038(Map var0, String var1) {
      if (var1 != null) {
         JSONObject var2;
         try (InputStream var3 = MinecraftLocales.class.getResourceAsStream("/data/vanilla_inventory_titles.json")) {
            if (var3 == null) {
               throw new IllegalStateException("Missing vanilla inventory translations");
            }

            var2 = JSON.parseObject(new String(var3.readAllBytes(), StandardCharsets.UTF_8));
         } catch (IOException var11) {
            throw new IllegalStateException("Cannot read vanilla inventory translations", var11);
         }

         TreeSet var12 = new TreeSet(aut);
         var12.addAll(var2.keySet());
         var12.addAll(var0.keySet());

         for (String var5 : var12) {
            String var6 = (String)var0.get(var5);
            JSONObject var7 = var6 == null ? new JSONObject() : JSON.parseObject(var6);
            String var8 = var7.getString("container.inventory");
            if (var8 == null || var8.isEmpty()) {
               var8 = var2.getString(var5);
            }

            if (var8 == null) {
               var8 = var2.getString("en_us");
            }

            var7.putIfAbsent("itemsadder.container.inventory", var8);
            var7.put("container.inventory", var1);
            var0.put(var5, JSON.toJSONString(var7));
         }
      }
   }

   public void clear() {
      this.auu.clear();
      this.auu = new HashMap();
   }
}
