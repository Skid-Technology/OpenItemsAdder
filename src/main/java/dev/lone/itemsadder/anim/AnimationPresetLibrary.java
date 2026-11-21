package dev.lone.itemsadder.anim;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

public final class AnimationPresetLibrary {
   private static final String[] Ui = new String[]{"body", "head", "left_arm", "right_arm", "left_leg", "right_leg"};
   private static final float[][] Uj = new float[][]{
      {0.0F, 0.75F, 0.0F}, {0.0F, 0.75F, 0.0F}, {-0.321875F, 0.625F, 0.0F}, {0.328125F, 0.625F, 0.0F}, {-0.1125F, 0.75F, 0.0F}, {0.11875F, 0.75F, 0.0F}
   };

   private AnimationPresetLibrary() {
   }

   public static String bw(String var0) {
      return switch (var0) {
         case "body" -> "pbody_2";
         case "head" -> "phead_0";
         case "left_arm" -> "parm_left_3";
         case "right_arm" -> "parm_right_4";
         case "left_leg" -> "pleg_left_1";
         case "right_leg" -> "pleg_right_5";
         default -> null;
      };
   }

   public static AnimationBinaryPayload method9226(String var0, String var1) {
      float[] var2 = new float[Ui.length * 12];
      JSONArray var3 = new JSONArray();
      JSONArray var4 = new JSONArray();
      JSONArray var5 = new JSONArray();
      JSONObject var6 = new JSONObject();

      for (int var7 = 0; var7 < Ui.length; var7++) {
         int var8 = var7 * 12;
         var2[var8] = var2[var8 + 4] = var2[var8 + 8] = 1.0F;
         var2[var8 + 9] = Uj[var7][0];
         var2[var8 + 10] = Uj[var7][1];
         var2[var8 + 11] = Uj[var7][2];
         JSONObject var9 = new JSONObject();
         var9.put("name", Ui[var7]);
         var9.put("boneType", var7 == 1 ? "head" : "normal");
         var9.put("parents", var7 >= 1 && var7 <= 3 ? new JSONArray(new Object[]{Ui[0]}) : new JSONArray());
         var9.put("staticMatrix", var7);
         var9.put("geometryScale", 1);
         var3.add(var9);
         var6.put(Ui[var7], new JSONArray(new Object[]{var7, var7}));
      }

      for (int var16 = 0; var16 < 2; var16++) {
         var4.add(new JSONArray());
         var5.add(new JSONArray());
      }

      JSONObject var17 = new JSONObject();
      var17.put("name", "death_ragdoll_pose");
      var17.put("length", 0.05);
      var17.put("loopMode", "hold");
      var17.put("animType", "other");
      var17.put("canPlayerMove", false);
      var17.put("tracks", var6);
      var17.put("channelMasks", new JSONArray());
      JSONObject var18 = new JSONObject();
      JSONObject var19 = new JSONObject();
      var19.put("keyframes", var4);
      JSONObject var10 = new JSONObject();
      var10.put("keyframes", var5);
      var18.put("sounds", var19);
      var18.put("particles", var10);
      var17.put("effects", var18);
      JSONObject var11 = new JSONObject();
      var11.putAll(var17);
      var11.put("name", "idle");
      var11.put("loopMode", "loop");
      var11.put("animType", "idle");
      var11.put("canPlayerMove", true);
      JSONObject var12 = new JSONObject();
      var12.put("namespace", var0);
      var12.put("id", var1);
      var12.put("type", "ZOMBIE");
      var12.put("canSunBurn", false);
      var12.put("playerEmote", true);
      JSONObject var13 = new JSONObject();
      var13.put("transformSpace", "local");
      var13.put("bones", var3);
      var13.put("animations", new JSONArray(new Object[]{var11, var17}));
      var13.put("entity", var12);
      JSONObject var14 = new JSONObject();
      var14.put("itemsadder:ragdoll_physics", vL());
      var13.put("extensions", var14);
      AnimationBinaryPayload var15 = new AnimationBinaryPayload(var13, var2);
      AnimationBinaryCodec.method9164(var13, var2);
      return var15;
   }

   private static JSONObject vL() {
      JSONObject var0 = new JSONObject();
      var0.put("version", 1);
      var0.put("preset", "humanoid");
      var0.put("root", Ui[0]);
      var0.put(
         "bodies",
         new JSONArray(
            new Object[]{
               method9227(Ui[0], 10.0, 0.0, 0.375, 0.0, 0.5, 0.75, 0.25),
               method9227(Ui[1], 3.0, 0.0, 0.25, 0.0, 0.5, 0.5, 0.5),
               method9227(Ui[2], 2.0, 0.0, -0.25, 0.0, 0.25, 0.75, 0.25),
               method9227(Ui[3], 2.0, 0.0, -0.25, 0.0, 0.25, 0.75, 0.25),
               method9227(Ui[4], 4.0, 0.0, -0.375, 0.0, 0.25, 0.75, 0.25),
               method9227(Ui[5], 4.0, 0.0, -0.375, 0.0, 0.25, 0.75, 0.25)
            }
         )
      );
      var0.put(
         "joints",
         new JSONArray(
            new Object[]{
               method9228(Ui[0], Ui[1], 0.0, 0.75, 0.0, 0.0, 0.0, 0.0, 0.8),
               method9228(Ui[0], Ui[2], -0.321875, 0.625, 0.0, 0.0, 0.0, 0.0, 1.5),
               method9228(Ui[0], Ui[3], 0.328125, 0.625, 0.0, 0.0, 0.0, 0.0, 1.5),
               method9228(Ui[0], Ui[4], -0.1125, 0.0, 0.0, 0.0, 0.0, 0.0, 1.4),
               method9228(Ui[0], Ui[5], 0.11875, 0.0, 0.0, 0.0, 0.0, 0.0, 1.4)
            }
         )
      );
      var0.put("followers", new JSONArray());
      var0.put("ignored", new JSONArray());
      return var0;
   }

   private static JSONObject method9227(String var0, double var1, double var3, double var5, double var7, double var9, double var11, double var13) {
      JSONObject var15 = new JSONObject();
      var15.put("center", new JSONArray(new Object[]{var3, var5, var7}));
      if (var11 > var9 * 2.0 && var9 == var13) {
         var15.put("type", "capsule");
         var15.put("radius", var9 / 2.0);
         var15.put("length", var11 - var9);
      } else {
         var15.put("type", "box");
         var15.put("size", new JSONArray(new Object[]{var9, var11, var13}));
      }

      JSONObject var16 = new JSONObject();
      var16.put("id", var0);
      var16.put("bone", var0);
      var16.put("mass", var1);
      var16.put("collider", var15);
      return var16;
   }

   private static JSONObject method9228(String var0, String var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14) {
      JSONObject var16 = new JSONObject();
      var16.put("parent", var0);
      var16.put("child", var1);
      var16.put("type", "ball");
      var16.put("anchorParent", new JSONArray(new Object[]{var2, var4, var6}));
      var16.put("anchorChild", new JSONArray(new Object[]{var8, var10, var12}));
      var16.put("axisParent", new JSONArray(new Object[]{0, 1, 0}));
      var16.put("axisChild", new JSONArray(new Object[]{0, 1, 0}));
      var16.put("swing", var14);
      var16.put("twist", 1.5);
      return var16;
   }
}
