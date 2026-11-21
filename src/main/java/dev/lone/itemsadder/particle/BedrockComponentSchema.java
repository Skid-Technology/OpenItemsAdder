package dev.lone.itemsadder.particle;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BedrockComponentSchema {
   private static final Map axF = Map.ofEntries(
      Map.entry("emitter_rate_instant", "num_particles"),
      Map.entry("emitter_rate_steady", "spawn_rate max_particles"),
      Map.entry("emitter_rate_manual", ""),
      Map.entry("emitter_lifetime_once", "active_time"),
      Map.entry("emitter_lifetime_looping", "active_time sleep_time"),
      Map.entry("emitter_lifetime_expression", "activation_expression expiration_expression"),
      Map.entry("emitter_initialization", "creation_expression per_update_expression"),
      Map.entry("particle_initialization", "per_update_expression per_render_expression"),
      Map.entry("emitter_local_space", "position rotation velocity"),
      Map.entry("emitter_shape_point", "offset direction"),
      Map.entry("emitter_shape_custom", "offset direction"),
      Map.entry("emitter_shape_box", "offset direction half_dimensions surface_only"),
      Map.entry("emitter_shape_entity_aabb", "direction surface_only"),
      Map.entry("emitter_shape_sphere", "offset direction radius surface_only"),
      Map.entry("emitter_shape_disc", "offset direction radius surface_only plane_normal"),
      Map.entry("particle_lifetime_expression", "max_lifetime expiration_expression"),
      Map.entry("particle_initial_spin", "rotation rotation_rate"),
      Map.entry("particle_motion_dynamic", "linear_acceleration linear_drag_coefficient rotation_acceleration rotation_drag_coefficient"),
      Map.entry("particle_motion_collision", "enabled collision_drag coefficient_of_restitution collision_radius expire_on_contact events"),
      Map.entry("particle_motion_parametric", "relative_position direction rotation"),
      Map.entry("particle_appearance_billboard", "size facing_camera_mode direction uv"),
      Map.entry("particle_appearance_tinting", "color"),
      Map.entry("particle_appearance_lighting", ""),
      Map.entry("emitter_lifetime_events", "creation_event expiration_event timeline travel_distance_events looping_travel_distance_events"),
      Map.entry("particle_lifetime_events", "creation_event expiration_event timeline")
   );

   static void method2141(JSONObject var0) {
      JSONObject var1 = var0.getJSONObject("particle_effect");
      JSONObject var2 = var1.getJSONObject("components");

      for (String var4 : var2.keySet()) {
         if (!var4.equals("minecraft:particle_initial_speed") && !var4.equals("minecraft:particle_kill_plane")) {
            if (!var4.equals("minecraft:particle_expire_if_in_blocks") && !var4.equals("minecraft:particle_expire_if_not_in_blocks")) {
               String var5 = var4.startsWith("minecraft:") ? var4.substring(10) : var4;
               String var6 = (String)axF.get(var5);
               if (var6 == null) {
                  throw bj(var4 + " is not implemented by the current runtime");
               }

               if (!(var2.get(var4) instanceof JSONObject var7)) {
                  throw bj(var4 + " must be an object");
               }

               method2144(var7, var6, var4);
               if (var5.startsWith("emitter_shape_") && var7.get("direction") instanceof String var9 && !Set.of("outwards", "inwards").contains(var9)) {
                  throw bj(var4 + ".direction: " + var9);
               }
            } else {
               ParticleEffectDefinition.method2072(var2.get(var4));
            }
         }
      }

      for (String var29 : new String[]{"emitter_rate_", "emitter_lifetime_", "emitter_shape_"}) {
         long var32 = var2.keySet().stream().filter(var1x -> var1x.startsWith("minecraft:" + var29) && !var1x.endsWith("_events")).count();
         if (var32 > 1L) {
            throw bj("conflicting " + var29 + " components");
         }
      }

      JSONObject var24 = var2.getJSONObject("minecraft:emitter_local_space");
      JSONObject var26 = var2.getJSONObject("minecraft:particle_motion_parametric");
      if (var26 == null || !var2.containsKey("minecraft:particle_motion_dynamic") || !var26.containsKey("relative_position") && !var26.containsKey("direction")
         )
       {
         JSONObject var28 = var2.getJSONObject("minecraft:particle_appearance_billboard");
         if (var28 != null) {
            String var30 = var28.getString("facing_camera_mode");
            if (var30 != null
               && !Set.of(
                     "rotate_xyz",
                     "rotate_y",
                     "lookat_xyz",
                     "lookat_y",
                     "lookat_direction",
                     "direction_x",
                     "direction_y",
                     "direction_z",
                     "emitter_transform_xy",
                     "emitter_transform_xz",
                     "emitter_transform_yz"
                  )
                  .contains(var30)) {
               throw bj("unknown facing mode " + var30);
            }

            JSONObject var33 = var28.getJSONObject("direction");
            if (var33 != null) {
               method2144(var33, "mode min_speed_threshold custom_direction", "billboard.direction");
               String var36 = var33.getString("mode");
               if (var36 != null && !Set.of("custom", "derive_from_velocity").contains(var36)) {
                  throw bj("unknown billboard direction mode " + var36);
               }

               if (var33.containsKey("min_speed_threshold")
                  && (!Double.isFinite(var33.getDoubleValue("min_speed_threshold")) || var33.getDoubleValue("min_speed_threshold") < 0.0)) {
                  throw bj("invalid facing speed threshold");
               }
            }

            JSONObject var37 = var28.getJSONObject("uv");
            if (var37 != null) {
               method2144(var37, "texture_width texture_height uv uv_size flipbook", "billboard.uv");
               JSONObject var40 = var37.getJSONObject("flipbook");
               if (var40 != null) {
                  method2144(var40, "base_UV size_UV step_UV frames_per_second max_frame stretch_to_lifetime loop", "billboard.uv.flipbook");
               }
            }
         }

         JSONObject var31 = var1.getJSONObject("description").getJSONObject("basic_render_parameters");
         if (var31 != null && var31.containsKey("material") && !"particles_alpha".equals(var31.getString("material"))) {
            throw bj("material has no verified native representation: " + var31.getString("material"));
         }

         JSONObject var34 = var1.getJSONObject("events");
         if (var34 == null) {
            var34 = new JSONObject();
         }

         for (String var41 : var34.keySet()) {
            method2143(var41, var34, new HashSet(), 0);
         }

         JSONObject var39 = var2.getJSONObject("minecraft:particle_motion_collision");
         if (var39 != null) {
            for (String var12 : new String[]{"collision_drag", "coefficient_of_restitution", "collision_radius"}) {
               if (var39.containsKey(var12)
                  && (!(var39.get(var12) instanceof Number) || !Double.isFinite(var39.getDoubleValue(var12)) || var39.getDoubleValue(var12) < 0.0)) {
                  throw bj("invalid collision " + var12);
               }
            }

            if (var39.getDoubleValue("collision_radius") > 0.5) {
               throw bj("collision radius exceeds half a block");
            }

            Object var43 = var39.get("events");
            if (var43 != null) {
               Object var45 = var43 instanceof JSONArray var47 ? var47 : List.of(var43);
               if (var45.size() > 256) {
                  throw bj("too many collision events");
               }

               for (Object var50 : var45) {
                  if (!(var50 instanceof JSONObject var13)) {
                     throw bj("collision event must be an object");
                  }

                  method2144(var13, "event min_speed", "collision event");
                  if (!(var13.get("event") instanceof String)) {
                     throw bj("collision event requires an event name");
                  }

                  method2142(var13.get("event"), var34);
                  if (var13.containsKey("min_speed")
                     && (
                        !(var13.get("min_speed") instanceof Number)
                           || !Double.isFinite(var13.getDoubleValue("min_speed"))
                           || var13.getDoubleValue("min_speed") < 0.0
                     )) {
                     throw bj("invalid collision event speed");
                  }
               }
            }
         }

         for (String var51 : new String[]{"emitter_lifetime_events", "particle_lifetime_events"}) {
            JSONObject var52 = var2.getJSONObject("minecraft:" + var51);
            if (var52 != null) {
               method2142(var52.get("creation_event"), var34);
               method2142(var52.get("expiration_event"), var34);
               JSONObject var14 = var52.getJSONObject("timeline");
               if (var14 != null) {
                  for (String var16 : var14.keySet()) {
                     double var17;
                     try {
                        var17 = Double.parseDouble(var16);
                     } catch (NumberFormatException var22) {
                        throw bj("invalid timeline time " + var16);
                     }

                     if (!Double.isFinite(var17) || var17 < 0.0) {
                        throw bj("invalid timeline time " + var16);
                     }

                     method2142(var14.get(var16), var34);
                  }
               }

               JSONObject var53 = var52.getJSONObject("travel_distance_events");
               if (var53 != null) {
                  for (String var56 : var53.keySet()) {
                     double var18;
                     try {
                        var18 = Double.parseDouble(var56);
                     } catch (NumberFormatException var21) {
                        throw bj("invalid travel distance " + var56);
                     }

                     if (!Double.isFinite(var18) || var18 < 0.0) {
                        throw bj("invalid travel distance " + var56);
                     }

                     method2142(var53.get(var56), var34);
                  }
               }

               JSONArray var55 = var52.getJSONArray("looping_travel_distance_events");
               if ((var53 == null ? 0 : var53.size()) + (var55 == null ? 0 : var55.size()) > 256) {
                  throw bj("at most 256 travel distance entries are allowed");
               }

               if (var55 != null) {
                  for (Object var58 : var55) {
                     if (!(var58 instanceof JSONObject var19)) {
                        throw bj("looping distance event must be an object");
                     }

                     method2144(var19, "distance effects", "looping distance event");
                     if (!(var19.get("distance") instanceof Number)
                        || !Double.isFinite(var19.getDoubleValue("distance"))
                        || var19.getDoubleValue("distance") <= 0.0) {
                        throw bj("looping distance must be finite and positive");
                     }

                     if (!var19.containsKey("effects")) {
                        throw bj("looping distance event requires effects");
                     }

                     method2142(var19.get("effects"), var34);
                  }
               }
            }
         }
      } else {
         throw bj("conflicting dynamic and parametric movement");
      }
   }

   private static void method2142(Object var0, JSONObject var1) {
      if (var0 != null) {
         if (var0 instanceof JSONArray) {
            for (Object var4 : (JSONArray)var0) {
               if (!(var4 instanceof String)) {
                  throw bj("component events require event names");
               }

               method2143(var4, var1, new HashSet(), 0);
            }
         } else {
            if (!(var0 instanceof String)) {
               throw bj("component events require an event name or array of names");
            }

            method2143(var0, var1, new HashSet(), 0);
         }
      }
   }

   private static void method2143(Object var0, JSONObject var1, Set var2, int var3) {
      if (var0 != null) {
         if (var3 > 16) {
            throw bj("event nesting exceeds 16");
         }

         if (var0 instanceof String var5) {
            if (!var1.containsKey(var5)) {
               throw bj("missing event " + var5);
            }

            if (!var2.add(var5)) {
               throw bj("event cycle at " + var5);
            }

            method2143(var1.get(var5), var1, var2, var3 + 1);
            var2.remove(var5);
         } else if (var0 instanceof JSONArray) {
            for (Object var8 : (JSONArray)var0) {
               method2143(var8, var1, var2, var3 + 1);
            }
         } else {
            if (!(var0 instanceof JSONObject var4)) {
               throw bj("invalid event value");
            }

            method2144(var4, "sequence randomize expression particle_effect sound_effect weight", "event");
            if (var4.containsKey("sound_effect")) {
               if (!(var4.get("sound_effect") instanceof JSONObject var10)) {
                  throw bj("event.sound_effect must be an object");
               }

               method2144(var10, "event_name", "event.sound_effect");
               if (!(var10.get("event_name") instanceof String var9) || var9.length() > 256 || !var9.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")) {
                  throw bj("sound event_name requires a namespaced Java sound key");
               }
            }

            method2143(var4.get("sequence"), var1, var2, var3 + 1);
            method2143(var4.get("randomize"), var1, var2, var3 + 1);
            JSONObject var11 = var4.getJSONObject("particle_effect");
            if (var11 != null) {
               method2144(var11, "effect type pre_effect_expression", "event.particle_effect");
               String var14 = var11.getString("type");
               if (var14 != null && !Set.of("emitter", "emitter_bound", "particle", "particle_with_velocity").contains(var14)) {
                  throw bj("unknown child type: " + var14);
               }
            }
         }
      }
   }

   private static void method2144(JSONObject var0, String var1, String var2) {
      Set var3 = var1.isEmpty() ? Set.of() : Set.of(var1.split(" "));

      for (String var5 : var0.keySet()) {
         if (!var3.contains(var5)) {
            throw bj(var2 + "." + var5 + " is not implemented");
         }
      }
   }

   private static IllegalArgumentException bj(String var0) {
      return new IllegalArgumentException("Particle executable profile: " + var0);
   }
}
