package dev.lone.itemsadder.shaderbridge;

import dev.lone.itemsadder.util.MarkerAnnotation;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.shaderc.GLSLang;
import org.lwjgl.util.shaderc.GLSLangInput;
import org.lwjgl.util.shaderc.GLSLangResource;

@MarkerAnnotation
public final class NativeGlslangBridge {
   private static final AtomicInteger ACTIVE_HANDLES = new AtomicInteger();

   private NativeGlslangBridge() {
   }

   public static String[] validate(String[] stageNames, String[] sources) {
      if (stageNames.length == sources.length && stageNames.length != 0 && stageNames.length <= 2) {
         ArrayList var2 = new ArrayList();
         boolean var3 = false;
         long var4 = 0L;
         long[] var6 = new long[sources.length];

         String[] var35;
         try {
            MemoryStack var7 = MemoryStack.stackPush();

            try {
               var3 = GLSLang.glslang_initialize_process();
               if (!var3) {
                  throw new IllegalStateException("glslang initialization failed");
               }

               GLSLangResource var8 = resourceLimits(var7);
               boolean var9 = true;

               for (int var10 = 0; var10 < sources.length; var10++) {
                  int var11 = nativeStage(stageNames[var10]);
                  ByteBuffer var12 = var7.UTF8(sources[var10]);
                  GLSLangInput var13 = GLSLangInput.calloc(var7)
                     .language(1)
                     .stage(var11)
                     .client(0)
                     .client_version(0)
                     .target_language(0)
                     .target_language_version(0)
                     .code(var12)
                     .default_version(150)
                     .default_profile(4)
                     .force_default_version_and_profile(false)
                     .forward_compatible(false)
                     .messages(131073)
                     .resource(var8);
                  long var14 = GLSLang.glslang_shader_create(var13);
                  if (var14 == 0L) {
                     throw new IllegalStateException("glslang shader allocation failed");
                  }

                  var6[var10] = var14;
                  ACTIVE_HANDLES.incrementAndGet();
                  GLSLang.glslang_shader_set_options(var14, 0);
                  boolean var16 = GLSLang.glslang_shader_preprocess(var14, var13);
                  String var17 = log(GLSLang.glslang_shader_get_info_log(var14));
                  if (!var16) {
                     message(var2, "preprocess", var10, var16, var17);
                  }

                  if (!var16) {
                     var9 = false;
                  } else {
                     String var18 = GLSLang.glslang_shader_get_preprocessed_code(var14);
                     if (var18 != null) {
                        GLSLang.glslang_shader_set_preprocessed_code(var14, var18);
                     }

                     boolean var19 = GLSLang.glslang_shader_parse(var14, var13);
                     String var20 = log(GLSLang.glslang_shader_get_info_log(var14));
                     if (!var19) {
                        message(var2, "parse", var10, var19, var20);
                     }

                     var9 &= var19;
                  }
               }

               if (sources.length == 2 && var9) {
                  var4 = GLSLang.glslang_program_create();
                  if (var4 == 0L) {
                     throw new IllegalStateException("glslang program allocation failed");
                  }

                  ACTIVE_HANDLES.incrementAndGet();

                  for (long var39 : var6) {
                     GLSLang.glslang_program_add_shader(var4, var39);
                  }

                  boolean var34 = GLSLang.glslang_program_link(var4, 655361);
                  String var37 = log(GLSLang.glslang_program_get_info_log(var4));
                  if (!var34) {
                     message(var2, "link", -1, var34, var37);
                  }
               }

               var35 = var2.toArray(String[]::new);
            } catch (Throwable var31) {
               if (var7 != null) {
                  try {
                     var7.close();
                  } catch (Throwable var30) {
                     var31.addSuppressed(var30);
                  }
               }

               throw var31;
            }

            if (var7 != null) {
               var7.close();
            }
         } finally {
            if (var4 != 0L) {
               GLSLang.glslang_program_delete(var4);
               ACTIVE_HANDLES.decrementAndGet();
            }

            for (long var25 : var6) {
               if (var25 != 0L) {
                  GLSLang.glslang_shader_delete(var25);
                  ACTIVE_HANDLES.decrementAndGet();
               }
            }

            if (var3) {
               GLSLang.glslang_finalize_process();
            }
         }

         return var35;
      } else {
         throw new IllegalArgumentException("expected one shader stage or one vertex/fragment pair");
      }
   }

   public static int activeHandles() {
      return ACTIVE_HANDLES.get();
   }

   private static GLSLangResource resourceLimits(MemoryStack stack) {
      GLSLangResource var1 = GLSLangResource.calloc(stack);

      for (byte var2 = 0; var2 < GLSLangResource.LIMITS; var2 += 4) {
         MemoryUtil.memPutInt(var1.address() + var2, 4096);
      }

      var1.limits().set(true, true, true, true, true, true, true, true, true);
      var1.min_program_texel_offset(-8);
      return var1;
   }

   private static int nativeStage(String stage) {
      return switch (stage) {
         case "vertex" -> 0;
         case "fragment" -> 4;
         default -> throw new IllegalArgumentException("unsupported GLSL stage: " + stage);
      };
   }

   private static void message(List result, String kind, int stage, boolean success, String log) {
      result.add(kind);
      result.add(Integer.toString(stage));
      result.add(Boolean.toString(success));
      result.add(log);
   }

   private static String log(String value) {
      return value == null ? "" : value.trim();
   }
}
