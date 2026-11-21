package dev.lone.itemsadder.particle;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import dev.lone.itemsadder.anim.AnimationCurve;
import dev.lone.itemsadder.anim.DistanceEventTrigger;
import dev.lone.itemsadder.anim.SpeedEventTrigger;
import dev.lone.itemsadder.molang.MolangExpression;
import dev.lone.itemsadder.molang.MolangParser;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ParticleEffectDefinition {
   final String avh;
   final String avi;
   final String avj;
   final String avk;
   final String avl;
   final String avm;
   final String avn;
   final String avo;
   final MolangExpression avp;
   final MolangExpression avq;
   final MolangExpression avr;
   final MolangExpression avs;
   final MolangExpression avt;
   final MolangExpression avu;
   final MolangExpression avv;
   final MolangExpression avw;
   final MolangExpression avx;
   final MolangExpression avy;
   final MolangExpression avz;
   final MolangExpression avA;
   final MolangExpression avB;
   final MolangExpression avC;
   final MolangExpression avD;
   final MolangExpression avE;
   final MolangExpression avF;
   final MolangExpression avG;
   final MolangExpression avH;
   final MolangExpression avI;
   final MolangExpression avJ;
   final MolangExpression avK;
   final MolangExpression avL;
   final MolangExpression[] avM;
   final MolangExpression[] avN;
   final MolangExpression[] avO;
   final MolangExpression[] avP;
   final MolangExpression[] avQ;
   final MolangExpression[] avR;
   final MolangExpression[] avS;
   final MolangExpression[] avT;
   final MolangExpression[] avU;
   final boolean avV;
   final double avW;
   private final double[] avX;
   final boolean avY;
   final boolean avZ;
   final boolean awa;
   final boolean awb;
   final boolean awc;
   final boolean awd;
   final Map awe;
   final JSONObject awf;
   final JSONObject awg;
   final Map awh;
   final ParticleColor awi;
   final MolangExpression[] awj;
   final boolean awk;
   final List awl;
   final Set awm;
   final Set awn;
   final boolean awo;
   final boolean awp;
   final MolangExpression awq;
   final double awr;
   final double aws;
   final double awt;
   final List awu;
   private final JSONObject awv = new JSONObject();

   public ParticleEffectDefinition(JSONObject var1) {
      this(var1, null);
   }

   public ParticleEffectDefinition(JSONObject var1, JSONArray var2) {
      this.avX = var2 == null ? new double[]{1.0} : var2.stream().mapToDouble(var0 -> ((JSONObject)var0).getDoubleValue("weight")).toArray();
      this.awf = var1;
      JSONObject var3 = var1.getJSONObject("particle_effect");
      this.avh = var3.getJSONObject("description").getString("identifier");
      this.awg = var3.getJSONObject("components");
      this.awm = method2072(this.awg.get("minecraft:particle_expire_if_in_blocks"));
      this.awn = method2072(this.awg.get("minecraft:particle_expire_if_not_in_blocks"));
      JSONObject var4 = this.dG("particle_motion_collision");
      this.awo = this.au("particle_motion_collision");
      this.awp = var4.getBooleanValue("expire_on_contact");
      Object var5 = var4.getOrDefault("enabled", 1);
      this.awq = method2074(var5 instanceof Boolean var6 ? var6 ? 1 : 0 : var5);
      this.awr = Math.max(1.0E-4, var4.getDoubleValue("collision_radius"));
      this.aws = var4.getDoubleValue("collision_drag");
      this.awt = var4.getDoubleValue("coefficient_of_restitution");
      ArrayList var29 = new ArrayList();
      Object var7 = var4.get("events");
      if (var7 != null) {
         for (Object var10 : var7 instanceof JSONArray var8 ? var8 : List.of(var7)) {
            JSONObject var11 = (JSONObject)var10;
            var29.add(new SpeedEventTrigger(var11.getString("event"), var11.getDoubleValue("min_speed")));
         }
      }

      this.awu = List.copyOf(var29);
      this.awi = new ParticleColor(this.dG("particle_appearance_tinting").get("color"));
      this.awj = method2076(this.awg.get("minecraft:particle_kill_plane"), 0.0, 0.0, 0.0, 0.0);
      this.awk = this.awg.containsKey("minecraft:particle_kill_plane");
      this.avi = this.au("emitter_rate_instant") ? "instant" : (this.au("emitter_rate_manual") ? "manual" : "steady");
      this.avp = this.method2073("emitter_rate_steady", "spawn_rate", 4);
      this.avq = this.method2073("emitter_rate_instant", "num_particles", 1);
      this.avr = this.method2073("emitter_rate_" + this.avi, "max_particles", 100);
      this.avj = this.au("emitter_lifetime_expression") ? "expression" : (this.au("emitter_lifetime_once") ? "once" : "looping");
      this.avs = this.method2073("emitter_lifetime_" + this.avj, "active_time", 1);
      this.avt = this.method2073("emitter_lifetime_looping", "sleep_time", 0);
      this.avu = this.method2073("emitter_lifetime_expression", "activation_expression", 0);
      this.avv = this.method2073("emitter_lifetime_expression", "expiration_expression", 0);
      String var30 = "point";

      for (String var12 : new String[]{"custom", "sphere", "box", "disc", "entity_aabb"}) {
         if (this.au("emitter_shape_" + var12)) {
            var30 = var12;
         }
      }

      this.avk = var30;
      JSONObject var32 = this.dG("emitter_shape_" + this.avk);
      this.avM = method2076(var32.get("offset"), 0.0, 0.0, 0.0);
      this.avN = method2076(var32.get("half_dimensions"), 0.0, 0.0, 0.0);
      this.avz = method2074(var32.getOrDefault("radius", 0));
      this.avY = var32.getBooleanValue("surface_only");
      Object var34 = var32.get("plane_normal");
      Object var46;
      if (var34 instanceof String var36) {
         switch (var36) {
            case "x":
               var46 = JSONArray.of(1, 0, 0);
               break;
            case "y":
               var46 = JSONArray.of(0, 1, 0);
               break;
            case "z":
               var46 = JSONArray.of(0, 0, 1);
               break;
            default:
               throw bj("disc normal");
         }
      } else {
         var46 = var34;
      }

      this.avO = method2076(var46, 0.0, 0.0, 0.0);
      Object var37 = var32.getOrDefault("direction", "outwards");
      Object var39 = this.awg.get("minecraft:particle_initial_speed");
      if (var39 instanceof JSONArray) {
         var37 = var39;
      }

      this.avl = var37 instanceof String var40 ? var40 : "direction";
      this.avP = method2076(var37 instanceof JSONArray ? var37 : null, 0.0, 0.0, 0.0);
      this.avy = method2074(var39 instanceof JSONArray ? 1 : (var39 == null ? this.au("particle_motion_dynamic") ? 1 : 0 : var39));
      this.avw = this.method2073("particle_lifetime_expression", "max_lifetime", 1);
      this.avx = this.method2073("particle_lifetime_expression", "expiration_expression", 0);
      JSONObject var41 = this.dG("particle_motion_dynamic");
      JSONObject var14 = this.dG("particle_motion_parametric");
      this.avm = !var14.containsKey("relative_position") && !var14.containsKey("direction")
         ? (this.au("particle_motion_dynamic") ? "dynamic" : "static")
         : "parametric";
      this.avn = var14.containsKey("rotation") ? "parametric" : "dynamic";
      this.avQ = method2076(var41.get("linear_acceleration"), 0.0, 0.0, 0.0);
      this.avA = method2074(var41.getOrDefault("linear_drag_coefficient", 0));
      this.avB = this.method2073("particle_initial_spin", "rotation", 0);
      this.avC = this.method2073("particle_initial_spin", "rotation_rate", 0);
      this.avD = method2074(var41.getOrDefault("rotation_acceleration", 0));
      this.avE = method2074(var41.getOrDefault("rotation_drag_coefficient", 0));
      this.avF = method2074(var14.getOrDefault("rotation", 0));
      this.avR = method2076(var14.get("relative_position"), 0.0, 0.0, 0.0);
      this.avS = method2076(var14.get("direction"), 0.0, 0.0, 0.0);
      JSONObject var15 = this.dG("particle_appearance_billboard");
      this.avT = method2076(var15.get("size"), 0.2, 0.2);
      this.avo = var15.getString("facing_camera_mode") == null ? "rotate_xyz" : var15.getString("facing_camera_mode");
      JSONObject var16 = var15.getJSONObject("direction");
      this.avV = var16 != null && "custom".equals(var16.getString("mode"));
      this.avU = method2076(var16 == null ? null : var16.get("custom_direction"), 0.0, 0.0, 0.0);
      this.avW = var16 != null && var16.containsKey("min_speed_threshold") ? var16.getDoubleValue("min_speed_threshold") : 0.01;
      JSONObject var17 = var15.getJSONObject("uv");
      JSONObject var18 = var17 == null ? null : var17.getJSONObject("flipbook");
      this.avK = method2074(var18 == null ? 0 : var18.getOrDefault("max_frame", 0));
      this.avL = method2074(var18 == null ? 0 : var18.getOrDefault("frames_per_second", 0));
      this.avZ = var18 != null && var18.getBooleanValue("loop");
      this.awa = var18 != null && var18.getBooleanValue("stretch_to_lifetime");
      this.awd = this.dG("emitter_local_space").getBooleanValue("velocity");
      this.awb = this.dG("emitter_local_space").getBooleanValue("position");
      this.awc = this.dG("emitter_local_space").getBooleanValue("rotation");
      this.avG = this.method2073("emitter_initialization", "creation_expression", "");
      this.avH = this.method2073("emitter_initialization", "per_update_expression", "");
      this.avI = this.method2073("particle_initialization", "per_update_expression", "");
      this.avJ = this.method2073("particle_initialization", "per_render_expression", "");
      LinkedHashMap var19 = new LinkedHashMap();
      JSONObject var20 = var3.getJSONObject("curves");
      if (var20 != null) {
         for (String var22 : var20.keySet()) {
            var19.put(var22, new AnimationCurve(var20.getJSONObject(var22)));
         }
      }

      this.awe = Map.copyOf(var19);
      LinkedHashMap var42 = new LinkedHashMap();
      method2075(var3.get("events"), var42);
      this.awh = Map.copyOf(var42);
      ArrayList var43 = new ArrayList();
      JSONObject var23 = this.dG("emitter_lifetime_events");
      JSONObject var24 = var23.getJSONObject("travel_distance_events");
      if (var24 != null) {
         for (String var26 : var24.keySet()) {
            var43.add(new DistanceEventTrigger(Double.parseDouble(var26), var24.get(var26), false));
         }
      }

      var43.sort(Comparator.comparingDouble(DistanceEventTrigger::fm));
      JSONArray var44 = var23.getJSONArray("looping_travel_distance_events");
      if (var44 != null) {
         for (Object var27 : var44) {
            JSONObject var28 = (JSONObject)var27;
            var43.add(new DistanceEventTrigger(var28.getDoubleValue("distance"), var28.get("effects"), true));
         }
      }

      this.awl = List.copyOf(var43);
   }

   int method2071(double var1) {
      if (this.avX.length == 1) {
         return 0;
      }

      double var3 = 0.0;

      for (double var8 : this.avX) {
         var3 += var8;
      }

      double var10 = var1 * var3;
      double var11 = 0.0;

      for (int var9 = 0; var9 < this.avX.length; var9++) {
         var11 += this.avX[var9];
         if (var10 < var11) {
            return var9;
         }
      }

      return this.avX.length - 1;
   }

   boolean au(String var1) {
      return this.awg.containsKey("minecraft:" + var1);
   }

   static Set method2072(Object var0) {
      if (var0 == null) {
         return Set.of();
      }

      if (var0 instanceof JSONArray var1 && var1.size() <= 256) {
         HashSet var2 = new HashSet();
         Iterator var3 = var1.iterator();

         while (var3.hasNext()) {
            if (!(var3.next() instanceof String var5) || !var5.matches("(?:[a-z0-9_-]+:)?[a-z0-9_/.-]+") || var5.length() > 256) {
               throw bj("invalid expiry block ID");
            }

            var2.add(var5.contains(":") ? var5 : "minecraft:" + var5);
         }

         return Set.copyOf(var2);
      } else {
         throw bj("block expiry requires at most 256 block IDs");
      }
   }

   JSONObject dG(String var1) {
      JSONObject var2 = this.awg.getJSONObject("minecraft:" + var1);
      return var2 == null ? this.awv : var2;
   }

   MolangExpression method2073(String var1, String var2, Object var3) {
      return method2074(this.dG(var1).getOrDefault(var2, var3));
   }

   static MolangExpression method2074(Object var0) {
      MolangExpression var1 = MolangParser.method2088(var0 == null ? 0 : var0);

      for (String var3 : var1.Ih()) {
         if (var3.startsWith("query.") || var3.startsWith("context.")) {
            throw bj("world/viewer query requires an explicit input binding: " + var3);
         }
      }

      return var1;
   }

   private static void method2075(Object var0, Map var1) {
      if (var0 instanceof JSONObject var2) {
         for (String var5 : var2.keySet()) {
            Object var6 = var2.get(var5);
            if (!var5.equals("expression") && !var5.equals("pre_effect_expression")) {
               method2075(var6, var1);
            } else {
               if (!(var6 instanceof String var7)) {
                  throw bj("event expression must be a string");
               }

               var1.computeIfAbsent(var7, ParticleEffectDefinition::method2074);
            }
         }
      } else if (var0 instanceof JSONArray) {
         for (Object var9 : (JSONArray)var0) {
            method2075(var9, var1);
         }
      }
   }

   static MolangExpression[] method2076(Object var0, double... var1) {
      if (var0 == null || var0 instanceof JSONArray var2 && var2.size() == var1.length) {
         MolangExpression[] var4 = new MolangExpression[var1.length];

         for (int var3 = 0; var3 < var4.length; var3++) {
            var4[var3] = method2074(var0 == null ? var1[var3] : ((JSONArray)var0).get(var3));
         }

         return var4;
      } else {
         throw bj("vector dimensions");
      }
   }

   static IllegalArgumentException bj(String var0) {
      return new IllegalArgumentException("Particle definition: " + var0);
   }
}
