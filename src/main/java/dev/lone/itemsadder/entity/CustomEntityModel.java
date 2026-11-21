package dev.lone.itemsadder.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader.Feature;
import dev.lone.itemsadder.anim.AnimationBinaryCodec;
import dev.lone.itemsadder.anim.AnimationBinaryPayload;
import dev.lone.itemsadder.anim.AnimationClip;
import dev.lone.itemsadder.anim.AnimationFileIO;
import dev.lone.itemsadder.anim.AnimationPresetLibrary;
import dev.lone.itemsadder.anim.AnimationWindow;
import dev.lone.itemsadder.block.CustomBlockStateSettings;
import dev.lone.itemsadder.core.ContentManager;
import dev.lone.itemsadder.core.NamespacedId;
import dev.lone.itemsadder.internal.SwitchMap89;
import dev.lone.itemsadder.network.NativeValueCache;
import dev.lone.itemsadder.particle.EntityParticleEmitter;
import dev.lone.itemsadder.particle.ParticleEmitterBase;
import dev.lone.itemsadder.particle.ParticleEmitterManager;
import dev.lone.itemsadder.particle.ParticleOffsetEmitter;
import dev.lone.itemsadder.util.FloatVector3;
import dev.lone.itemsadder.util.JsonHelper;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.NullableBox;
import dev.lone.itemsadder.util.StringOrderComparator;
import dev.lone.itemsadder.util.Utils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class CustomEntityModel extends NamespacedId {
   private static final AtomicLong AI = new AtomicLong();
   private volatile long bG = AI.incrementAndGet();
   LinkedHashMap AJ = new LinkedHashMap();
   public LinkedHashMap AK = new LinkedHashMap();
   public LinkedHashMap AL = new LinkedHashMap();
   public boolean AM;
   public boolean AN;
   public boolean AO;
   private boolean AP;
   private Matrix4f[] AQ;
   private TransformDecomposition[] AR;
   private List AS = List.of();
   private List AT = List.of("default");
   private Map AU = Map.of("default", 0);
   private Map AV = Map.of();
   private List AW = List.of();
   private Map AX = Map.of();
   private Map AY = Map.of();
   private Map AZ = Map.of();
   private String Ba = "colored";
   private boolean Bb = true;
   private int Bc;
   @Nullable
   private AnimationBlend Bd;
   @Nullable
   private AnimationSetup Be;
   @Nullable
   private AnimationWindow Bf;
   @Nullable
   private HumanoidEquipmentSet Bg;
   @Nullable
   private BoneStateList vo;
   @Nullable
   private AnimationClip Bh;
   private final NativeValueCache Bi = new NativeValueCache();
   @Nullable
   public FloatVector3 Bj;
   @Nullable
   public FloatVector3 Bk;
   public float Bl = 70.0F;
   public float Bm = 45.0F;
   public InterpolationMode Bn;
   private boolean Bo;
   private String Bp;
   private boolean Bq;
   private boolean Br;
   private Set Bs = Set.of();

   public boolean nS() {
      return this.Bb;
   }

   public String nT() {
      return this.Ba;
   }

   public int method441(int var1, boolean var2) {
      return var2 && this.Bc > 0 ? Math.max(0, Math.min(var1, this.Bc - 1)) + this.Bc : var1;
   }

   @Nullable
   public BoneStateList nU() {
      return this.vo;
   }

   @Nullable
   public BoneStateList nV() {
      if (this.vo == null) {
         this.vo = BoneStateList.of(this.AL.values());
      }

      return this.vo;
   }

   @Nullable
   public AnimationWindow nW() {
      return this.Bf;
   }

   @Nullable
   public HumanoidEquipmentSet nX() {
      return this.Bg;
   }

   public CustomEntityModel(String var1, String var2) {
      super(var1, var2);
   }

   public long ai() {
      return this.bG;
   }

   @Nullable
   public AnimationClip nY() {
      return this.Bh;
   }

   public void method442(String var1, String var2, boolean var3) {
      this.bG = AI.incrementAndGet();
      this.Bi.NT();
      this.AN = false;
      this.AO = false;
      this.Bq = false;
      this.Br = false;
      this.Bs = Set.of();
      this.AP = false;
      this.AQ = null;
      this.AR = null;
      this.AS = List.of();
      this.AT = List.of("default");
      this.AU = Map.of("default", 0);
      this.AV = Map.of();
      this.AW = List.of();
      this.AX = Map.of();
      this.AY = Map.of();
      this.AZ = Map.of();
      this.Bd = null;
      this.Be = null;
      this.Bh = null;
      this.Bf = null;
      this.Bg = null;
      this.method443(var1, JSON.parseObject(var2), var3);
   }

   public boolean ba(String var1) {
      return this.AM && this.AN && !this.AO && AnimationPresetLibrary.bw(var1) != null;
   }

   private void method443(String var1, JSONObject var2, boolean var3) {
      this.AM = var3;
      if (var2.containsKey("hitbox")) {
         JSONObject var4 = var2.getJSONObject("hitbox");
         this.Bj = JsonHelper.parseScaledVector(var4.getJSONArray("pos"));
         this.Bk = JsonHelper.parseScaledVector(var4.getJSONArray("size"));
      }

      this.Bn = InterpolationMode.aY(JsonHelper.getStringOr(var2, "rotationMode", "precise"));
      JSONArray var23 = var2.getJSONArray("bones");

      for (int var5 = 0; var5 < var23.size(); var5++) {
         JSONObject var6 = var23.getJSONObject(var5);
         String var7 = var6.getString("name");
         boolean var8 = CustomEntityDefinition.aU(var7) || this.ba(var7);
         BonePurpose var9 = BonePurpose.fromString(JsonHelper.getStringOr(var6, "boneType", BonePurpose.NORMAL.toString()));
         String[] var10 = JsonHelper.toStringArray(var6.getJSONArray("parents"));
         HashMap var11 = new HashMap();
         if (var6.containsKey("scales")) {
            String[] var12 = JsonHelper.toStringArray(var6.getJSONArray("scales"));

            for (String var16 : var12) {
               if (!var16.equals("1-1-1")) {
                  int var17 = CustomBlockStateSettings.getInstance()
                     .allocateStateId(
                        var8 ? Material.PLAYER_HEAD : Material.POTION,
                        "__manually_handled",
                        this.dk + "__" + this.dl + "_" + var7 + "_scale_" + var16.replace(".", ",")
                     );
                  var11.put(var16, var17);
               }
            }
         }

         if (!this.Bo && var9 == BonePurpose.HEAD) {
            this.Bl = (float)JsonHelper.getDoubleOr(var6, "maxHeadRotX", 70.0);
            this.Bm = (float)JsonHelper.getDoubleOr(var6, "maxHeadRotY", 45.0);
            this.Bo = true;
         }

         int var34 = -1;
         if (var9.nO()) {
            var34 = CustomBlockStateSettings.getInstance()
               .allocateStateId(var8 ? Material.PLAYER_HEAD : Material.POTION, "__manually_handled", this.dk + "__" + this.dl + "_" + var7);
         }

         String var35 = this.getNamespace();
         if (CustomEntityDefinition.aW(var7) || this.ba(var7)) {
            var35 = "_iainternal";
            var34 = CustomBlockStateSettings.getPlayerHeadModelId(this.ba(var7) ? AnimationPresetLibrary.bw(var7) : var7);
            if (var34 == -1) {
               Log.error(
                  "Unknown player model bone '"
                     + var7
                     + "' for model '"
                     + this.getNamespacedID()
                     + "'. This bone will be ignored. Please report this to the developer."
               );
               continue;
            }
         }

         CustomEntityDefinition var36 = new CustomEntityDefinition(
            this,
            var35,
            var7,
            var1,
            JsonHelper.parseVector(var6.getJSONArray("pos")),
            JsonHelper.parseVector(var6.getJSONArray("rot")),
            var34,
            var9,
            var10,
            var11
         );
         var36.Ao = var6.getBooleanValue("locomotionPriority", false);
         if (this.AN && !this.AO) {
            int var37 = var6.getIntValue("staticMatrix");
            Matrix4f var38 = this.AQ[var37];
            if (this.AP) {
               var36.method8738(var38, this.AR[var37], (float)JsonHelper.getDoubleOr(var6, "geometryScale", 1.0));
            } else {
               var36.zZ = var38;
            }

            EnumMap var39 = (EnumMap)this.AZ.get(var7);
            if (var39 != null) {
               var36.method8737(var39);
            }

            boolean[] var18 = (boolean[])this.AY.get(var7);
            int[] var19 = new int[this.AT.size()];
            String[] var20 = new String[this.AT.size()];
            Arrays.fill(var19, var34);
            if (var18 != null) {
               for (int var21 = 1; var21 < var18.length; var21++) {
                  if (var18[var21]) {
                     String var22 = (String)this.AT.get(var21);
                     var19[var21] = CustomBlockStateSettings.getInstance()
                        .allocateStateId(Material.POTION, "__manually_handled", method465(var35, this.dl, var22, var7));
                     var20[var21] = this.method464(var1, var22, var7);
                  }
               }
            }

            var36.method8736(var19, var20);
         }

         this.AL.put(var36.getId(), var36);
      }

      if (!this.AP) {
         StringOrderComparator.sortNumeric(new ArrayList(this.AL.keySet())).forEach(var1x -> {
            CustomEntityDefinition var2x = (CustomEntityDefinition)this.AL.get(var1x);
            this.AL.remove(var1x);
            this.AL.put(var1x, var2x);
         });
      }

      if (var3 && !this.AN) {
         for (int var24 = 0; var24 < 6; var24++) {
            String var27 = "dummy_" + var24;
            CustomEntityDefinition var30 = new CustomEntityDefinition(
               this,
               var27,
               var1,
               new FloatVector3(0.0F, 0.0F, 0.0F),
               new FloatVector3(0.0F, 0.0F, 0.0F),
               CustomBlockStateSettings.getInstance().allocateStateId(Material.PLAYER_HEAD, "__manually_handled", this.dk + "__" + this.dl + "_" + var27),
               BonePurpose.NORMAL,
               new String[0],
               new HashMap()
            );
            this.AL.putFirst(var27, var30);
         }

         LinkedHashMap var25 = new LinkedHashMap();
         Iterator var28 = this.AL.entrySet().iterator();

         while (var28.hasNext()) {
            Entry var31 = (Entry)var28.next();
            String var32 = (String)var31.getKey();
            CustomEntityDefinition var33 = (CustomEntityDefinition)var31.getValue();
            if (var33.An.nP()) {
               var25.put(var32, var33);
               var28.remove();
            }
         }

         LinkedHashMap var29 = new LinkedHashMap();
         var29.putAll(var25);
         var29.putAll(this.AL);
         this.AL = var29;
      }

      if (this.AP) {
         this.oa();
      } else if (this.AN && !this.AO) {
         this.AS = List.copyOf(this.AL.values());

         for (int var26 = 0; var26 < this.AS.size(); var26++) {
            ((CustomEntityDefinition)this.AS.get(var26)).method8740(var26);
         }
      }

      this.AL.forEach((var1x, var2x) -> {
         for (String var6x : var2x.Am) {
            CustomEntityDefinition var7x = (CustomEntityDefinition)this.AL.get(var6x);
            if (var7x != null && var7x.An == BonePurpose.HEAD) {
               var2x.setParent(var7x);
               break;
            }
         }
      });
      this.method447(null, var2);
   }

   public void method444(File var1, String var2, String var3) {
      JSONObject var4 = JSONObject.parseObject(var3);
      JSONArray var5 = var4.getJSONArray("bones");
      var5.forEach(
         var3x -> {
            JSONObject var4x = (JSONObject)var3x;
            String var5x = var4x.getString("name");
            String var6 = var2 + "_" + var5x;
            String[] var7 = JsonHelper.toStringArray(var4x.getJSONArray("parents"));
            BonePurpose var8 = BonePurpose.fromString(JsonHelper.getStringOr(var4x, "boneType", BonePurpose.NORMAL.toString()));
            if (var8 == BonePurpose.LOCATOR_PIVOT) {
               if (!this.AL.containsKey(var6)) {
                  CustomEntityDefinition var9 = new CustomEntityDefinition(
                     this,
                     var6,
                     "player",
                     JsonHelper.parseVector(var4x.getJSONArray("pos")),
                     JsonHelper.parseVector(var4x.getJSONArray("rot")),
                     CustomBlockStateSettings.getInstance().allocateStateId(Material.PLAYER_HEAD, "__manually_handled", this.dk + "__" + this.dl + "_" + var6),
                     var8,
                     var7,
                     new HashMap()
                  );
                  this.AL.put(var9.getId(), var9);
               } else {
                  Log.warn("Duplicate locator '" + var5x + "' for emotes file: " + Utils.relativePath(var1));
               }
            }
         }
      );
   }

   public void method445(File var1, String var2, String var3) {
      this.method444(var1, var2, var3);
      this.method446(var2, var3);
   }

   public void method446(@Nullable String var1, String var2) {
      this.method447(var1, JSON.parseObject(var2));
   }

   private void method447(@Nullable String var1, JSONObject var2) {
      JSONArray var3 = var2.getJSONArray("animations");
      var3.forEach(
         var2x -> {
            JSONObject var3x = (JSONObject)var2x;
            ModelDefinitionData var4x = new ModelDefinitionData(
               var3x.getString("name"),
               var3x.getString("loopMode"),
               JsonHelper.getBoolOr(var3x, "canPlayerMove", false),
               JsonHelper.getStringOr(var3x, "animType", null),
               var3x.getDouble("length"),
               JsonHelper.getDoubleOr(var3x, "loopStartTime", -1.0),
               JsonHelper.getDoubleOr(var3x, "loopEndTime", -1.0),
               JsonHelper.getDoubleOr(var3x, "strideDistance", -1.0),
               !this.AN || this.AO
            );
            JSONObject var5 = var3x.getJSONObject("clientInterpolation");
            if (var5 != null) {
               var4x.setInterpolation(var5.getString("mode"), var5.getIntValue("intervalTicks"));
            }

            var4x.setPlaybackFlags("elapsed".equals(var3x.getString("locomotionMode")), JsonHelper.getBoolOr(var3x, "restOnStop", false));
            JSONObject var6x = var3x.getJSONObject("effects");
            JSONObject var7x = this.AN && !this.AO && var6x != null ? var6x.getJSONObject("appearance") : null;
            if (var7x != null) {
               short[][] var8 = new short[this.AW.size()][];
               JSONObject var9x = var7x.getJSONObject("tracks");
               if (var9x != null) {
                  for (String var11 : var9x.keySet()) {
                     int var12 = (Integer)this.AX.get(var11);
                     JSONArray var13 = var9x.getJSONArray(var11);
                     var8[var12] = new short[var13.size()];

                     for (int var14 = 0; var14 < var13.size(); var14++) {
                        Object var15 = var13.get(var14);
                        var8[var12][var14] = var15 == null ? -1 : (short)this.method457(var11, (String)var15);
                     }
                  }
               }

               var4x.setShortTable(var8);
            }

            JSONObject var25 = this.AN && !this.AO && var6x != null ? var6x.getJSONObject("texture_variant") : null;
            if (var25 != null) {
               JSONArray var26 = var25.getJSONArray("keyframes");
               char[] var28 = new char[var26.size()];

               for (int var30 = 0; var30 < var28.length; var30++) {
                  var28[var30] = (char)((Integer)this.AU.get(var26.getString(var30))).intValue();
               }

               var4x.setCharTable(var28);
            }

            LinkedList var27 = new LinkedList();
            LinkedList var29 = new LinkedList();
            if (var3x.containsKey("effects")) {
               JSONObject var31 = var3x.getJSONObject("effects");
               if (var31.containsKey("sounds")) {
                  JSONObject var34 = var31.getJSONObject("sounds");
                  if (var34.containsKey("keyframes")) {
                     for (Object var47 : var34.getJSONArray("keyframes")) {
                        JSONArray var16 = (JSONArray)var47;
                        ArrayList var17 = new ArrayList();
                        var27.add(var17);

                        for (Object var19 : var16) {
                           JSONObject var20 = (JSONObject)var19;
                           var17.add(new ParticleOffsetEmitter(var20.getString("name"), var20.getFloat("volume"), var20.getFloat("pitch")));
                           if (this.AN && !this.AO) {
                              var4x.addEmitterTrigger(
                                 JsonHelper.getDoubleOr(var20, "time", Math.min(var4x.yQ, (double)(var27.size() - 1) / var3x.getIntValue("sampleRate", 20))),
                                 (ParticleEmitterBase)var17.getLast()
                              );
                           }
                        }
                     }
                  }
               }

               if (var31.containsKey("particles")) {
                  JSONObject var35 = var31.getJSONObject("particles");
                  if (var35.containsKey("keyframes")) {
                     for (Object var48 : var35.getJSONArray("keyframes")) {
                        JSONArray var51 = (JSONArray)var48;
                        ArrayList var54 = new ArrayList();
                        var29.add(var54);

                        for (Object var58 : var51) {
                           JSONObject var59 = (JSONObject)var58;
                           String var21 = var59.getString("locator");
                           String var22 = var1;
                           if (CustomEntityDefinition.aV(var21)) {
                              var22 = null;
                           }

                           CustomEntityDefinition var23 = (CustomEntityDefinition)this.AL.get(var22 == null ? var21 : var22 + "_" + var21);
                           if (var23 != null) {
                              if (var59.getBooleanValue("follow_locator", false) && (!this.AN || this.AO)) {
                                 throw new IllegalArgumentException("follow_locator requires a native V2 model");
                              }

                              var54.add(
                                 new EntityParticleEmitter(
                                    var23,
                                    var59.getString("name").toLowerCase(),
                                    var59.containsKey("speed") ? var59.getFloatValue("speed") : 1.0F,
                                    var59.getIntValue("count", 1),
                                    var59.containsKey("x_delta") ? var59.getFloatValue("x_delta") : 0.0F,
                                    var59.containsKey("y_delta") ? var59.getFloatValue("y_delta") : 0.0F,
                                    var59.containsKey("z_delta") ? var59.getFloatValue("z_delta") : 0.0F,
                                    ParticleEmitterManager.method1713(var59.containsKey("duration_ticks") ? var59.getDoubleValue("duration_ticks") : 60.0),
                                    var59.getBooleanValue("follow_locator", false)
                                 )
                              );
                              if (this.AN && !this.AO) {
                                 var4x.addEmitterTrigger(
                                    JsonHelper.getDoubleOr(var59, "time", Math.min(var4x.yQ, (double)(var29.size() - 1) / var3x.getIntValue("sampleRate", 20))),
                                    (ParticleEmitterBase)var54.getLast()
                                 );
                              }
                           } else {
                              String var24 = "Can't find 'locator' '" + var21 + "' for particle effect. Animation: '" + var4x.yK + "'.";
                              if (var22 != null) {
                                 Log.error("[Entities] '" + var22 + "'. " + var24);
                              } else {
                                 Log.error("[Entities] '" + this.getNamespacedID() + "'. " + var24);
                              }
                           }
                        }
                     }
                  }
               }
            }

            if (this.AN && !this.AO && var6x != null && var6x.containsKey("signals")) {
               for (Object var41 : var6x.getJSONObject("signals").getJSONArray("keyframes")) {
                  for (Object var49 : (JSONArray)var41) {
                     JSONObject var52 = (JSONObject)var49;
                     var4x.addTagTrigger(var52.getDoubleValue("time"), var52.getString("id"));
                  }
               }
            }

            ArrayList var33 = new ArrayList();
            if (this.AN && !this.AO) {
               HashMap var38 = new HashMap();
               JSONObject var42 = var3x.getJSONObject("tracks");

               for (CustomEntityDefinition var50 : this.AL.values()) {
                  JSONArray var53 = var42.getJSONArray(var50.getId());
                  ArrayList var55 = new ArrayList();
                  if (var53 == null) {
                     var55.add(method450(var4x, this.AP ? var50.getTransform() : var50.zZ, this.AP ? var50.getDecomposition() : null));
                  } else {
                     for (int var57 = 0; var57 < var53.size(); var57++) {
                        var55.add(var38.computeIfAbsent(var53.getIntValue(var57), var2xx -> method450(var4x, this.AQ[var2xx], this.AR[var2xx])));
                     }
                  }

                  var33.add(ModelBinding.create(var4x, var50, var55));
               }
            } else {
               JSONObject var37 = var3x.getJSONObject("bones");
               var37.entrySet()
                  .forEach(
                     var4xx -> {
                        String var5x = (String)var4xx.getKey();
                        CustomEntityDefinition var6xx = (CustomEntityDefinition)this.AL.get(var5x);
                        if (var6xx == null && var1 != null) {
                           var5x = var1 + "_" + var5x;
                           var6xx = (CustomEntityDefinition)this.AL.get(var5x);
                        }

                        if (var6xx != null) {
                           JSONObject var7xx = (JSONObject)var4xx.getValue();
                           ArrayList var8x = new ArrayList();
                           CustomEntityDefinition var9xx = var6xx;
                           var7xx.getJSONArray("keyframes")
                              .forEach(
                                 var4xxx -> {
                                    JSONObject var5xx = (JSONObject)var4xxx;
                                    String var6xxx;
                                    if (var5xx.containsKey("scale_str")) {
                                       var6xxx = var5xx.getString("scale_str");
                                       if (var6xxx.startsWith("-")) {
                                          var6xxx = var6xxx.substring(1);
                                       }
                                    } else {
                                       var6xxx = "1-1-1";
                                    }

                                    String[] var7xxx = var6xxx.split("-");
                                    if (var7xxx.length != 3) {
                                       Log.error(
                                          "Invalid scale_str '"
                                             + var6xxx
                                             + "' for bone '"
                                             + var9xx.getNamespacedID()
                                             + "' animation '"
                                             + var4x.yK
                                             + "' of model '"
                                             + this.getNamespacedID()
                                             + "'. Using 1-1-1."
                                       );
                                       var7xxx = new String[]{"1", "1", "1"};
                                       var6xxx = "1-1-1";
                                    }

                                    ModelPartTransform var8xx = new ModelPartTransform(
                                       var4x,
                                       JsonHelper.parseVector(var5xx.getJSONArray("pos")),
                                       JsonHelper.parseVector(var5xx.getJSONArray("rot")),
                                       new FloatVector3(Float.parseFloat(var7xxx[0]), Float.parseFloat(var7xxx[1]), Float.parseFloat(var7xxx[2])),
                                       var6xxx
                                    );
                                    var8x.add(var8xx);
                                 }
                              );
                           ModelBinding var10 = new ModelBinding(var4x, var6xx, var8x);
                           var33.add(var10);
                        }
                     }
                  );
            }

            var4x.setBindings(var33);
            if (this.AN && !this.AO) {
               this.method452(var4x, var3x, var33);
            }

            var4x.setEmitters(var27, var29);
            this.AK.put(var4x.yK, var4x);
            this.AJ.put(var4x.yM, var4x);
         }
      );
      if (this.AM) {
         this.AK
            .forEach(
               (var1x, var2x) -> this.AL
                  .forEach(
                     (var1xx, var2xx) -> {
                        if (var2xx.getId().startsWith("dummy_")) {
                           ArrayList var3x = new ArrayList();

                           for (int var4x = 0; var4x < 2; var4x++) {
                              var3x.add(
                                 new ModelPartTransform(
                                    var2x, new FloatVector3(0.0F, 0.0F, 0.0F), new FloatVector3(0.0F, 0.0F, 0.0F), new FloatVector3(1.0F, 1.0F, 1.0F), "1-1-1"
                                 )
                              );
                           }

                           var2x.getBindings().add(new ModelBinding(var2x, var2xx, var3x));
                        }
                     }
                  )
            );
      }

      if (this.AP && this.Be == null && this.Bd == null && this.AK.values().stream().noneMatch(var0 -> var0.getVariable() != AnimationVariable.NONE)) {
         int var4 = 65536;

         for (ModelDefinitionData var6 : this.AK.values()) {
            long var7 = (long)var6.getMaxPartCount() * this.AS.size();
            if (var7 != 0L && var7 <= var4 && "base".equals(var6.getLayer()) && !var6.isLocomotionDriven()) {
               var6.buildBoneTable(this.AS);
               var4 -= (int)var7;
            }
         }
      }

      NullableBox var9 = NullableBox.of((ModelDefinitionData)this.AK.get("idle"));
      if (!var9.isPresent()) {
         this.AK.entrySet().stream().findFirst().ifPresent(var1x -> var9.set((ModelDefinitionData)var1x.getValue()));
      }

      if (var9.value != null) {
         this.AL.forEach((var1x, var2x) -> {
            ModelBinding var3x = ((ModelDefinitionData)var9.value).getBindingById(var1x);
            if (var3x != null && var3x.getPartCount() >= 2) {
               ModelPartTransform var4x = var3x.getPart(1);
               var2x.setTransform(var4x.Dd, var4x.De, var4x.Df);
            }
         });
      }
   }

   public boolean nZ() {
      return this.AP && this.method473(AnimationState.IDLE) == null && this.AK.values().stream().anyMatch(ModelDefinitionData::isStoppable);
   }

   public void method448(String var1) {
      ModelDefinitionData var2 = (ModelDefinitionData)this.AK.remove(var1);
      if (var2 != null) {
         this.AJ.remove(var2.yM);
      }
   }

   public boolean bc(String var1) {
      this.Bi.NT();
      File var2 = ContentManager.method8198(var1);
      if (var2 != null && var2.exists()) {
         try {
            this.Bp = var2.getAbsolutePath();
            String var3 = this.bg(var1);
            if (Files.size(var2.toPath()) > 67108864L) {
               throw new IOException("Entity metadata exceeds 64 MiB");
            }

            byte[] var4 = Files.readAllBytes(var2.toPath());
            this.method442(var3, new String(var4, StandardCharsets.UTF_8), this.AM);
            return true;
         } catch (IOException | IllegalArgumentException var5) {
            Log.error("Error loading model animation for model: " + var1);
            var5.printStackTrace();
            return false;
         }
      } else {
         Log.error("Custom entity model doesn't exists! namespace: " + this.dk + " | id: " + this.dl + " | File: " + var1);
         return false;
      }
   }

   public boolean method449(File var1, AnimationBinaryPayload var2) {
      this.Bi.NT();

      try {
         AnimationBinaryCodec.method9164(var2.vH(), var2.vI());
         JSONObject var3 = var2.vH().getJSONObject("entity");
         if (var3 != null && this.dk.equals(var3.getString("namespace")) && this.dl.equals(var3.getString("id"))) {
            this.Bp = var1.getAbsolutePath();
            this.method454(this.dl, var2);
            return true;
         } else {
            throw new IllegalArgumentException("Entity bundle identity does not match " + this.getNamespacedID());
         }
      } catch (IllegalArgumentException var4) {
         Log.error("Error loading entity bundle: " + var1.getAbsolutePath());
         var4.printStackTrace();
         return false;
      }
   }

   private static ModelPartTransform method450(ModelDefinitionData var0, Matrix4fc var1, @Nullable TransformDecomposition var2) {
      return new ModelPartTransform(
         var0,
         new FloatVector3(var1.m30(), var1.m31(), var1.m32()),
         new FloatVector3(0.0F, 0.0F, 0.0F),
         new FloatVector3(1.0F, 1.0F, 1.0F),
         null,
         var1,
         var2 == null ? TransformDecomposition.decompose(var1) : var2
      );
   }

   private void oa() {
      ArrayList var1 = new ArrayList(this.AL.size());
      IdentityHashMap var2 = new IdentityHashMap();
      Set var3 = Collections.newSetFromMap(new IdentityHashMap());

      for (CustomEntityDefinition var5 : this.AL.values()) {
         this.method451(var5, var1, var2, var3);
      }

      this.AS = List.copyOf(var1);
   }

   private Matrix4f method451(CustomEntityDefinition var1, List var2, IdentityHashMap var3, Set var4) {
      Matrix4f var5 = (Matrix4f)var3.get(var1);
      if (var5 != null) {
         return var5;
      }

      if (!var4.add(var1)) {
         throw new IllegalArgumentException("Local display bone hierarchy cycle at '" + var1.getId() + "'");
      }

      CustomEntityDefinition var6 = var1.Am.length == 0 ? null : (CustomEntityDefinition)this.AL.get(var1.Am[0]);
      if (var1.getTransform() == null) {
         throw new IllegalArgumentException("Missing local display transform for bone '" + var1.getId() + "'");
      }

      Matrix4f var7 = var6 == null ? new Matrix4f(var1.getTransform()) : new Matrix4f(this.method451(var6, var2, var3, var4)).mul(var1.getTransform());
      var4.remove(var1);
      var3.put(var1, var7);
      var1.method8739(var6, var2.size(), var7);
      var2.add(var1);
      return var7;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void method452(ModelDefinitionData var1, JSONObject var2, List var3) {
      int var4 = this.AS.size();
      ModelBinding[] var5 = new ModelBinding[var4];

      for (ModelBinding var7 : var3) {
         var5[var7.AC.nH()] = var7;
      }

      JSONObject var25 = var2.getJSONObject("interpolation");
      if (var25 != null) {
         MatrixTable[] var26 = new MatrixTable[var4];

         for (Entry var9 : var25.entrySet()) {
            CustomEntityDefinition var10 = (CustomEntityDefinition)this.AL.get(var9.getKey());
            JSONArray var11 = (JSONArray)var9.getValue();
            int[] var12 = new int[var11.size()];
            Matrix4fc[] var13 = new Matrix4fc[var11.size()];

            for (int var14 = 0; var14 < var11.size(); var14++) {
               JSONArray var15 = var11.getJSONArray(var14);
               var12[var14] = var15.getIntValue(0);
               var13[var14] = this.AQ[var15.getIntValue(1)];
            }

            var26[var10.nH()] = new MatrixTable(var12, var13);
         }

         var1.setMatrixTables(var26);
      }

      byte[] var27 = new byte[var4];
      JSONArray var28 = var2.getJSONArray("affectedBones");
      JSONArray var29 = var2.getJSONArray("channelMasks");
      if (var28 == null && var29 == null) {
         Arrays.fill(var27, (byte)7);
      } else if (var28 != null) {
         for (Object var33 : var28) {
            CustomEntityDefinition var35 = (CustomEntityDefinition)this.AL.get((String)var33);
            var27[var35.nH()] = 7;
         }
      } else {
         ArrayList var31 = new ArrayList(this.AL.values());

         for (Object var36 : var29) {
            JSONArray var38 = (JSONArray)var36;
            CustomEntityDefinition var39 = (CustomEntityDefinition)var31.get(var38.getIntValue(0));
            var27[var39.nH()] = var38.getByteValue(1);
         }
      }

      double var10000;
      if (this.AP) {
         switch (SwitchMap89.Bt[var1.yM.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
               var10000 = 0.1;
               break;
            case 7:
               var10000 = 0.05;
               break;
            default:
               var10000 = 0.0;
         }
      } else {
         var10000 = 0.0;
      }

      double var32 = var10000;
      if (this.AP) {
         switch (SwitchMap89.Bt[var1.yM.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
               var10000 = 0.05;
               break;
            case 7:
               var10000 = 0.1;
               break;
            default:
               var10000 = 0.0;
         }
      } else {
         var10000 = 0.0;
      }

      double var37 = var10000;
      var1.configure(
         JsonHelper.getStringOr(var2, "layer", var1.yM != AnimationState.ATTACK && !"hurt".equals(var1.yK) ? "base" : "action"),
         var2.getIntValue("priority", 0),
         JsonHelper.getDoubleOr(var2, "blendIn", var32),
         JsonHelper.getDoubleOr(var2, "blendOut", var37),
         var27,
         var5
      );
      var1.setVariableWithRange(JsonHelper.getStringOr(var2, "semanticDriver", null), JsonHelper.getDoubleOr(var2, "semanticPeriod", -1.0));
      var1.setName(JsonHelper.getStringOr(var2, "semanticGroup", ""));
      var1.setSelectionWeight(var2.getIntValue("selectionWeight", 1));
      var1.setFrameRate(var2.getIntValue("sampleRate", 20));
      byte[][] var40 = new byte[var4][];
      int[][] var41 = new int[var4][];
      JSONObject var16 = var2.getJSONObject("effects");
      if (var16 != null) {
         JSONObject var17 = method453(var16, "visibility");
         if (var17 != null) {
            for (Entry var19 : var17.entrySet()) {
               CustomEntityDefinition var20 = (CustomEntityDefinition)this.AL.get(var19.getKey());
               JSONArray var21 = (JSONArray)var19.getValue();
               byte[] var22 = new byte[var21.size()];

               for (int var23 = 0; var23 < var22.length; var23++) {
                  Object var24 = var21.get(var23);
                  var22[var23] = var24 == null ? -1 : (byte)((Boolean)var24 ? 1 : 0);
               }

               var40[var20.nH()] = var22;
            }
         }

         JSONObject var42 = method453(var16, "tint");
         if (var42 != null) {
            for (Entry var44 : var42.entrySet()) {
               CustomEntityDefinition var45 = (CustomEntityDefinition)this.AL.get(var44.getKey());
               JSONArray var46 = (JSONArray)var44.getValue();
               int[] var47 = new int[var46.size()];

               for (int var48 = 0; var48 < var47.length; var48++) {
                  var47[var48] = var46.getIntValue(var48);
               }

               var41[var45.nH()] = var47;
            }
         }
      }

      var1.setBoneStates(var40, var41);
   }

   @Nullable
   private static JSONObject method453(JSONObject var0, String var1) {
      JSONObject var2 = var0.getJSONObject(var1);
      return var2 == null ? null : var2.getJSONObject("tracks");
   }

   private void method454(String var1, AnimationBinaryPayload var2) {
      this.AV = Map.of();
      this.AW = List.of();
      this.AX = Map.of();
      boolean var3 = var2.vH().getJSONObject("entity").getBooleanValue("playerEmote");
      this.AM = var3;
      this.AZ = Map.of();
      JSONObject var4 = var2.vH();
      this.Ba = var4.getJSONObject("entity").getString("hurtMode");
      this.Bb = !"parts_only".equals(var4.getJSONObject("entity").getString("interactionMode"));
      if (this.Ba == null) {
         this.Ba = "colored";
      }

      this.Bc = 0;
      JSONObject var5 = var4.getJSONObject("extensions");
      this.Bg = var5 != null && var5.containsKey("itemsadder:humanoid_equipment")
         ? HumanoidEquipmentSet.method56(
            var5.getJSONObject("itemsadder:humanoid_equipment"),
            var4.getJSONArray("bones").stream().map(var0 -> ((JSONObject)var0).getString("name")).collect(Collectors.toSet())
         )
         : null;
      this.Bh = var5 != null && var5.containsKey("itemsadder:ragdoll_physics")
         ? AnimationClip.method81(var5.getJSONObject("itemsadder:ragdoll_physics"))
         : null;
      this.Bd = null;
      this.Be = null;
      if (var4.containsKey("legacy")) {
         this.Bf = null;
         JSONObject var12 = AnimationFileIO.method9202(var2);
         this.AN = this.AO = true;
         this.AQ = null;
         this.method443(var1, var12, false);
      } else {
         this.AO = false;
         JSONObject var6 = var4.getJSONObject("textures");
         this.Bq = var6 != null && !var6.isEmpty();
         this.Br = var4.getJSONObject("geometryModels") != null;
         if (this.Bq) {
            HashSet var7 = new HashSet();
            var6.keySet().forEach(var2x -> var7.add(this.getNamespace() + ":ia_v2/" + var2x));
            this.Bs = Set.copyOf(var7);
         }

         this.AP = "local".equals(var4.getString("transformSpace"));
         float[] var13 = var2.vI();
         this.AQ = new Matrix4f[var13.length / 12];
         this.AR = new TransformDecomposition[this.AQ.length];

         for (int var8 = 0; var8 < this.AQ.length; var8++) {
            int var9 = var8 * 12;
            this.AQ[var8] = new Matrix4f(
               var13[var9],
               var13[var9 + 1],
               var13[var9 + 2],
               0.0F,
               var13[var9 + 3],
               var13[var9 + 4],
               var13[var9 + 5],
               0.0F,
               var13[var9 + 6],
               var13[var9 + 7],
               var13[var9 + 8],
               0.0F,
               var13[var9 + 9],
               var13[var9 + 10],
               var13[var9 + 11],
               1.0F
            );
            this.AR[var8] = TransformDecomposition.decompose(this.AQ[var8]);
         }

         this.method456(var4);
         this.method458(var1, var4);
         this.method459(var1, var4);

         for (Object var15 : var4.getJSONArray("bones")) {
            JSONObject var10 = (JSONObject)var15;
            Matrix4f var11 = this.AQ[var10.getIntValue("staticMatrix")];
            var10.put("pos", new JSONArray(new Object[]{var11.m30() * 16.0F, var11.m31() * 16.0F, var11.m32() * 16.0F}));
            var10.put("rot", new JSONArray(new Object[]{0, 0, 0}));
         }

         this.AN = true;
         this.method443(var1, var4, var3);
         this.method466(var4.getJSONObject("extensions"));
         this.method467(var4.getJSONObject("extensions"));
         this.Bf = var5 != null && var5.containsKey("itemsadder:terrain_feet") ? AnimationWindow.method78(var5.getJSONObject("itemsadder:terrain_feet")) : null;
         this.method455(var4, var3);
         this.AT = List.of();
         this.AU = Map.of();
         this.AY = Map.of();
      }
   }

   private void method455(JSONObject var1, boolean var2) {
      JSONObject var3 = var1.getJSONObject("extensions");
      String var4 = var1.getJSONObject("entity").getString("interactionMode");
      this.vo = var3 != null && var3.containsKey("itemsadder:damageable_parts")
         ? BoneStateList.parseListStatic(var3.getJSONObject("itemsadder:damageable_parts"))
         : null;
      this.Bb = !"parts_only".equals(var4);
   }

   private void method456(JSONObject var1) {
      JSONObject var2 = var1.getJSONObject("appearanceGroups");
      if (var2 == null) {
         this.AV = Map.of();
         this.AW = List.of();
         this.AX = Map.of();
      } else {
         TreeMap var3 = new TreeMap();

         for (String var5 : var2.keySet()) {
            List var6 = var2.getJSONArray(var5).toJavaList(String.class, new Feature[0]);
            var3.put(var5, List.copyOf(var6));
         }

         this.AV = Collections.unmodifiableMap(var3);
         this.AW = List.copyOf(var3.keySet());
         HashMap var7 = new HashMap();

         for (int var8 = 0; var8 < this.AW.size(); var8++) {
            var7.put((String)this.AW.get(var8), var8);
         }

         this.AX = Map.copyOf(var7);
      }
   }

   public Map ob() {
      return this.AV;
   }

   public List oc() {
      return this.AW;
   }

   public int od() {
      return this.AW.size();
   }

   public int bd(String var1) {
      return this.AX.getOrDefault(var1, -1);
   }

   public int method457(String var1, String var2) {
      List var3 = (List)this.AV.get(var1);
      return var3 == null ? -1 : var3.indexOf(var2);
   }

   private void method458(String var1, JSONObject var2) {
      ArrayList var3 = new ArrayList();
      var3.add("default");
      JSONArray var4 = var2.getJSONArray("variants");
      if (var4 != null) {
         for (Object var6 : var4) {
            var3.add((String)var6);
         }
      }

      this.AT = List.copyOf(var3);
      this.Bc = "texture_swap".equals(this.Ba) ? var3.size() : 0;
      HashMap var14 = new HashMap();

      for (int var15 = 0; var15 < var3.size(); var15++) {
         var14.put((String)var3.get(var15), var15);
      }

      this.AU = Map.copyOf(var14);
      if (var2.containsKey("geometryProfiles")) {
         this.AY = Map.of();
      } else if (var3.size() == 1) {
         this.AY = Map.of();
      } else {
         if (!be(var1)) {
            throw new IllegalArgumentException("Unsafe texture variant model folder '" + var1 + "'");
         }

         boolean[] var16 = new boolean[var3.size()];
         HashMap var7 = new HashMap();

         for (Object var9 : var2.getJSONArray("bones")) {
            JSONObject var10 = (JSONObject)var9;
            if (BonePurpose.fromString(var10.getString("boneType")).nO()) {
               String var11 = var10.getString("name");
               if (!be(var11)) {
                  throw new IllegalArgumentException("Unsafe texture variant bone name '" + var11 + "'");
               }

               boolean[] var12 = new boolean[var3.size()];

               for (int var13 = 1; var13 < var3.size(); var13++) {
                  var12[var13] = ContentManager.method8200(this.method464(var1, (String)var3.get(var13), var11)) != null;
                  var16[var13] |= var12[var13];
               }

               var7.put(var11, var12);
            }
         }

         for (int var17 = 1; var17 < var3.size(); var17++) {
            if (!var16[var17]) {
               throw new IllegalArgumentException("Texture variant '" + (String)var3.get(var17) + "' has no model files");
            }
         }

         this.AY = Map.copyOf(var7);
      }
   }

   private void method459(String var1, JSONObject var2) {
      JSONObject var3 = var2.getJSONObject("geometryProfiles");
      if (var3 == null) {
         this.AZ = Map.of();
      } else {
         if (!be(var1)) {
            throw new IllegalArgumentException("Unsafe geometry profile model folder '" + var1 + "'");
         }

         HashMap var4 = new HashMap();
         JSONObject var5 = var2.getJSONObject("geometryModels");

         for (Object var7 : var2.getJSONArray("bones")) {
            JSONObject var8 = (JSONObject)var7;
            if (BonePurpose.fromString(var8.getString("boneType")).nO()) {
               String var9 = var8.getString("name");
               if (!this.AM || !CustomEntityDefinition.aW(var9) && AnimationPresetLibrary.bw(var9) == null) {
                  if (!be(var9)) {
                     throw new IllegalArgumentException("Unsafe geometry profile bone name '" + var9 + "'");
                  }

                  EnumMap var10 = new EnumMap<>(RotationPolicy.class);

                  for (RotationPolicy var14 : RotationPolicy.values()) {
                     JSONArray var15 = var3.getJSONObject(var14.getKey()).getJSONArray(var9);
                     BoneHierarchy[] var16 = new BoneHierarchy[var15.size()];

                     for (int var17 = 0; var17 < var16.length; var17++) {
                        Matrix4f var18 = this.AQ[var15.getIntValue(var17)];
                        String var19 = this.method460(var1, var14, var9, var17);
                        JSONObject var20 = var5 == null ? null : var5.getJSONObject(var14.getKey()).getJSONArray(var9).getJSONObject(var17);
                        method463(var19, var20 != null || ContentManager.method8200(var19) != null);
                        JSONArray var21 = var20 == null ? new JSONArray() : var20.getJSONArray("appearanceGroups");
                        int[] var22 = new int[var21 == null ? 0 : var21.size()];
                        int[] var23 = new int[var22.length];
                        int var24 = 1;

                        for (int var25 = 0; var25 < var22.length; var25++) {
                           String var26 = var21.getString(var25);
                           var22[var25] = this.bd(var26);
                           var23[var25] = ((List)this.AV.get(var26)).size();
                           var24 *= var23[var25];
                        }

                        int var32 = "texture_swap".equals(this.Ba) ? var24 : 0;
                        int[] var33 = new int[var24 + var32];
                        String[] var27 = new String[var33.length];
                        var27[0] = var19;
                        var33[0] = CustomBlockStateSettings.getInstance()
                           .allocateStateId(Material.POTION, "__manually_handled", method462(this.dk, this.dl, var14, "default", var9, var17));

                        for (int var28 = 1; var28 < var24; var28++) {
                           String var29 = Integer.toString(var28);
                           String var30 = this.method461(var1, var14, var29, var9, var17);
                           boolean var31 = var20 != null && var20.getJSONObject("variants").containsKey(var29);
                           if (!var31 && ContentManager.method8200(var30) == null) {
                              var33[var28] = var33[0];
                           } else {
                              var27[var28] = var30;
                              var33[var28] = CustomBlockStateSettings.getInstance()
                                 .allocateStateId(Material.POTION, "__manually_handled", method462(this.dk, this.dl, var14, var29, var9, var17));
                           }
                        }

                        if (var32 > 0) {
                           for (int var34 = 0; var34 < var24; var34++) {
                              if (var34 > 0 && var27[var34] == null) {
                                 var33[var34 + var32] = var33[var32];
                              } else {
                                 String var35 = "hurt/" + (var34 == 0 ? "default" : var34);
                                 var27[var34 + var32] = this.method461(var1, var14, var35, var9, var17);
                                 var33[var34 + var32] = CustomBlockStateSettings.getInstance()
                                    .allocateStateId(Material.POTION, "__manually_handled", method462(this.dk, this.dl, var14, var35, var9, var17));
                              }
                           }
                        }

                        var16[var17] = new BoneHierarchy(var18, var33, var27, var22, var23, var32, var20 == null ? null : var20.getJSONObject("model"));
                     }

                     var10.put(var14, var16);
                  }

                  var4.put(var9, var10);
               }
            }
         }

         this.AZ = Map.copyOf(var4);
      }
   }

   private String method460(String var1, RotationPolicy var2, String var3, int var4) {
      return this.dk + ":entity/" + var1 + "/profiles/" + var2.getKey() + "/" + var3 + "/" + var4;
   }

   private String method461(String var1, RotationPolicy var2, String var3, String var4, int var5) {
      return this.dk + ":entity/" + var1 + "/profiles/" + var2.getKey() + "/" + var3 + "/" + var4 + "/" + var5;
   }

   static String method462(String var0, String var1, RotationPolicy var2, String var3, String var4, int var5) {
      return "entity_geometry_fragment_" + bf(var0) + bf(var1) + bf(var2.getKey()) + bf(var3) + bf(var4) + var5;
   }

   static boolean be(String var0) {
      return var0 != null && !var0.equals(".") && !var0.equals("..") && var0.matches("[a-z0-9._-]+");
   }

   static void method463(String var0, boolean var1) {
      if (!var1) {
         throw new IllegalArgumentException("Missing declared geometry fragment model '" + var0 + "'");
      }
   }

   private String method464(String var1, String var2, String var3) {
      return this.dk + ":entity/" + var1 + "/" + var2 + "/" + var3;
   }

   static String method465(String var0, String var1, String var2, String var3) {
      return "entity_texture_variant_" + bf(var0) + bf(var1) + bf(var2) + bf(var3);
   }

   private static String bf(String var0) {
      String var1 = Base64.getUrlEncoder().withoutPadding().encodeToString(var0.getBytes(StandardCharsets.UTF_8));
      return var1.length() + "_" + var1;
   }

   public NativeValueCache oe() {
      return this.Bi;
   }

   public boolean of() {
      return this.AP;
   }

   public List og() {
      return this.AS;
   }

   private void method466(@Nullable JSONObject var1) {
      JSONObject var2 = var1 == null ? null : var1.getJSONObject("itemsadder:flight_blend");
      if (var2 != null) {
         String var3 = var2.getString("straight");
         String var4 = var2.getString("left");
         String var5 = var2.getString("right");
         if (var3 != null && var4 != null && var5 != null) {
            ModelDefinitionData var6 = (ModelDefinitionData)this.AK.get(var3);
            ModelDefinitionData var7 = (ModelDefinitionData)this.AK.get(var4);
            ModelDefinitionData var8 = (ModelDefinitionData)this.AK.get(var5);
            if (var6 != null && var7 != null && var8 != null) {
               this.Bd = new AnimationBlend(
                  var6,
                  var7,
                  var8,
                  var2.containsKey("deadzone") ? var2.getFloatValue("deadzone") : 0.5F,
                  var2.containsKey("maxTurnRate") ? var2.getFloatValue("maxTurnRate") : 8.0F,
                  var2.containsKey("smoothing") ? var2.getFloatValue("smoothing") : 0.35F
               );
               this.AJ.put(AnimationState.FLY, var6);
            } else {
               throw new IllegalArgumentException("Flight Blend references an unknown animation");
            }
         }
      }
   }

   @Nullable
   public AnimationBlend oh() {
      return this.Bd;
   }

   private void method467(@Nullable JSONObject var1) {
      JSONObject var2 = var1 == null ? null : var1.getJSONObject("itemsadder:procedural_motion");
      if (var2 != null) {
         this.Be = new AnimationSetup(
            var2.getString("preset"),
            var2.getIntValue("historyTicks"),
            var2.getString("phaseSource") == null ? "elapsed" : var2.getString("phaseSource"),
            this.method469(var2.getJSONObject("root")),
            this.method469(var2.getJSONObject("body")),
            this.method469(var2.getJSONObject("head")),
            this.method470(var2.getJSONObject("neck")),
            this.method470(var2.getJSONObject("tail")),
            this.method468(var2.getJSONObject("bodyFrame"))
         );
      }
   }

   @Nullable
   private PoseFrame method468(@Nullable JSONObject var1) {
      if (var1 == null) {
         return null;
      }

      JSONArray var2 = var1.getJSONArray("pivot");
      ArrayList var3 = new ArrayList();

      for (Object var5 : var1.getJSONArray("targets")) {
         var3.add((CustomEntityDefinition)this.AL.get((String)var5));
      }

      return new PoseFrame(
         (CustomEntityDefinition)this.AL.get(var1.getString("anchor")), var3, var2.getFloatValue(0), var2.getFloatValue(1), var2.getFloatValue(2)
      );
   }

   @Nullable
   private PoseSample method469(@Nullable JSONObject var1) {
      return var1 == null
         ? null
         : new PoseSample(
            (CustomEntityDefinition)this.AL.get(var1.getString("bone")),
            var1.getFloatValue("sampleDelay"),
            var1.getFloatValue("referenceDelay"),
            var1.getFloatValue("yawInfluence"),
            var1.getFloatValue("verticalInfluence"),
            var1.getFloatValue("rollInfluence"),
            var1.getFloatValue("follow"),
            var1.getFloatValue("response"),
            var1.containsKey("verticalSampleDelay") ? var1.getFloatValue("verticalSampleDelay") : var1.getFloatValue("sampleDelay"),
            var1.containsKey("verticalReferenceDelay") ? var1.getFloatValue("verticalReferenceDelay") : var1.getFloatValue("referenceDelay"),
            var1.containsKey("rollReferenceStartDelay") ? var1.getFloatValue("rollReferenceStartDelay") : var1.getFloatValue("referenceDelay"),
            var1.containsKey("rollReferenceEndDelay") ? var1.getFloatValue("rollReferenceEndDelay") : var1.getFloatValue("referenceDelay"),
            var1.getFloatValue("rollReferenceBlend")
         );
   }

   @Nullable
   private PoseSampleList method470(@Nullable JSONObject var1) {
      if (var1 == null) {
         return null;
      }

      ArrayList var2 = new ArrayList();

      for (Object var4 : var1.getJSONArray("bones")) {
         var2.add((CustomEntityDefinition)this.AL.get((String)var4));
      }

      return new PoseSampleList(
         var2,
         var1.getFloatValue("startDelay"),
         var1.getFloatValue("endDelay"),
         var1.getFloatValue("referenceDelay"),
         var1.getFloatValue("yawInfluence"),
         var1.getFloatValue("verticalInfluence"),
         var1.getFloatValue("rollInfluence"),
         var1.getFloatValue("follow"),
         var1.getFloatValue("response"),
         var1.getFloatValue("waveAmplitude"),
         var1.getFloatValue("waveFrequency"),
         var1.containsKey("rollReferenceStartDelay") ? var1.getFloatValue("rollReferenceStartDelay") : var1.getFloatValue("referenceDelay"),
         var1.containsKey("rollReferenceEndDelay") ? var1.getFloatValue("rollReferenceEndDelay") : var1.getFloatValue("referenceDelay"),
         var1.getFloatValue("rollReferenceBlend")
      );
   }

   @Nullable
   public AnimationSetup oi() {
      return this.Be;
   }

   public boolean nJ() {
      return !this.AZ.isEmpty();
   }

   public String bg(String var1) {
      String var2 = Utils.normalizeSlashes(var1);
      var2 = Utils.removeSuffix(var2, "/.metadata");
      var2 = Utils.removePrefix(var2, "/");
      return Utils.lastSegment(var2);
   }

   public boolean oj() {
      return this.Bk != null && this.Bj != null;
   }

   public List getAnimationsNames() {
      return new ArrayList(this.AK.keySet());
   }

   public boolean method471(File var1) {
      return this.Bp == null ? false : var1.getParentFile().equals(new File(this.Bp).getParentFile());
   }

   @Nullable
   public Path ok() {
      return this.AN && !this.AO && this.Bq && this.Bp != null ? Path.of(this.Bp) : null;
   }

   @Nullable
   public Path method472() {
      return this.AN && !this.AO && (this.Bq || this.Br) && this.Bp != null ? Path.of(this.Bp) : null;
   }

   public Set om() {
      return this.Bs;
   }

   public boolean on() {
      return this.Br;
   }

   @Nullable
   public ModelDefinitionData method473(AnimationState var1) {
      ModelDefinitionData var2 = (ModelDefinitionData)this.AJ.get(var1);
      if (var2 != null) {
         return var2;
      }

      for (Entry var4 : this.AK.entrySet()) {
         if (((ModelDefinitionData)var4.getValue()).yK.equalsIgnoreCase(var1.toString())) {
            return (ModelDefinitionData)var4.getValue();
         }
      }

      return null;
   }

   public List method474(ModelDefinitionData var1) {
      if (this.AP && var1.yM != AnimationState.OTHER && var1.getVariable() == AnimationVariable.NONE && this.AK.get(var1.yK) == var1) {
         ArrayList var2 = new ArrayList();

         for (ModelDefinitionData var4 : this.AK.values()) {
            if (var4.yM == var1.yM && var4.getLayer().equals(var1.getLayer()) && var4.getVariable() == AnimationVariable.NONE) {
               var2.add(var4);
            }
         }

         return var2;
      } else {
         return List.of(var1);
      }
   }

   public ModelDefinitionData method475(ModelDefinitionData var1) {
      return method476(this.method474(var1));
   }

   public static ModelDefinitionData method476(List var0) {
      long var1 = 0L;

      for (ModelDefinitionData var4 : var0) {
         var1 += var4.getSelectionWeight();
      }

      return method477(var0, ThreadLocalRandom.current().nextLong(var1));
   }

   public static ModelDefinitionData method477(List var0, long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("Negative animation selection ticket");
      }

      for (ModelDefinitionData var4 : var0) {
         var1 -= var4.getSelectionWeight();
         if (var1 < 0L) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Animation selection ticket exceeds total weight");
   }
}
