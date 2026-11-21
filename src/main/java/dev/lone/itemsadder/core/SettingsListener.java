package dev.lone.itemsadder.core;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.config.YamlConfig;
import dev.lone.itemsadder.util.CancellableEvent;
import dev.lone.itemsadder.util.EventRegistrar;
import dev.lone.itemsadder.util.LegacyNameTable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SettingsListener implements Listener {
   YamlConfig bPH;
   static List bPI = new ArrayList();
   static List bPJ = new ArrayList();
   static List bPr;
   Object bPK = new EncodedValueDecoderM(this).adV();
   Object bPL = new EncodedValueDecoderN(this).adV();
   Object bPM = new EncodedValueDecoderO(this).adV();
   static Object bPC = new EncodedValueDecoderP().adV();
   static Object bPD = new EncodedValueDecoderQ().adV();
   static final Object bPN = new EncodedStringAJ().toString();
   static final Object bPO = new EncodedStringAB().toString();
   static final Object bPP = new EncodedStringAE().toString();
   static final Object bPQ = new EncodedStringAF().toString();
   static final Object bPR = new EncodedStringAG().toString();
   static final Object bPS = new EncodedStringAH().toString();
   static final Object bPT = new EncodedStringAI().toString();
   Object bPU = new EncodedValueDecoderD(this).adV();
   Object bPV = new EncodedValueDecoderE(this).adV();
   Object bMX;
   Object bPW;

   public SettingsListener() {
      bPI.add((String)new EncodedValueDecoderF(this).adV());
      bPJ.add((String)new EncodedValueDecoderG(this).adV());
      bPJ.add((String)new EncodedValueDecoderI(this).adV());
      this.bMX = new EncodedValueDecoderJ(this).adV();
      this.bPW = new EncodedValueDecoderK(this).adV();
   }

   public void register() {
      this.bPH = new YamlConfig(Main.getInstance(), true, (String)this.bPK, true, true, false);
      EventRegistrar.registerIfAbsent(this, Main.getInstance());
   }

   @EventHandler
   void method7608(CancellableEvent var1) {
      boolean var2 = Main.getInstance().getFile().getName().toLowerCase(Locale.ENGLISH).matches((String)this.bPV);
      if (!var2) {
         CodeSource var3 = Main.class.getProtectionDomain().getCodeSource();
         if (var3 == null) {
            return;
         }

         URL var4 = var3.getLocation();
         ZipInputStream var5 = null;

         try {
            var5 = new ZipInputStream(var4.openStream());

            label100:
            while (!var2) {
               ZipEntry var6 = var5.getNextEntry();
               if (var6 == null) {
                  break;
               }

               String var7 = var6.getName();
               if (!var7.endsWith(".json") && !var7.endsWith(".yml")) {
                  for (String var9 : bPI) {
                     if (var7.toLowerCase().endsWith(var9 + ".class")) {
                        var2 = true;
                        break label100;
                     }
                  }

                  if (var7.endsWith((String)this.bPM)) {
                     Scanner var20 = new Scanner(var5);
                     StringBuilder var22 = new StringBuilder();

                     while (var20.hasNextLine()) {
                        var22.append(var20.nextLine());
                     }

                     String var10 = var22.toString().toLowerCase();
                     if (var10.contains((CharSequence)this.bPL) || var10.contains(((String)this.bPU).toLowerCase())) {
                        var2 = true;
                        break;
                     }

                     String var11 = var22.toString();

                     for (String var13 : bPJ) {
                        if (!var11.contains(var13)) {
                           var2 = true;
                           break label100;
                        }
                     }
                  }
               }
            }
         } catch (IOException var16) {
         }

         try {
            Object var18 = new EncodedValueDecoderC(this).adV();
            Object var19 = new EncodedValueDecoderH(this).adV();
            Class.forName((String)var18 + (String)var19);
            Object var21 = new EncodedValueDecoderL(this).adV();
            Main.class.getField((String)var21);
         } catch (NoSuchFieldException | ClassNotFoundException var14) {
            var2 = true;
         } catch (Exception var15) {
         }
      }

      if (var2) {
         this.bPH.method7048(String.valueOf(this.bMX), this.bPH.getInt((String)this.bMX, 0) + 1);
         this.bPH.save();
         if (this.bPH.getInt((String)this.bMX) > Integer.parseInt((String)this.bPW)) {
            var1.setCancelled(true);
            if (Math.random() * 100.0 > 80.0) {
               method7609();
            }
         }
      } else {
         this.bPH.jJ("a");
         this.bPH.save();
      }

      if (this.bPH.contains((String)this.bMX) && this.bPH.getInt((String)this.bMX, 0) > 0) {
         this.bPH.save();
      }

      this.bPH = null;
      bPI = null;
      bPJ = null;
   }

   public static void method7609() {
      Random var0 = new Random();
      String[] var1 = LegacyNameTable.EXCEPTION_NAMES;
      Logger var2 = Logger.getLogger("");
      var2.warning(var1[var0.nextInt(var1.length)]);
      int var3 = var0.nextInt(30);

      for (int var4 = 0; var4 < 10 + var3; var4++) {
         String var5 = method7611();
         String var6 = method7610(var5);
         String[] var7 = var5.split("\\.");
         String var8 = var7[var7.length - 1];
         String var9 = var5.replace((CharSequence)bPD, "");
         int var10 = new Random().nextInt(9564);
         var2.warning("\t" + bPC + " " + var9 + "." + var6 + "(" + var8 + ":" + var10 + ")");
      }
   }

   public static String method7610(String clazz) {
      Class var1 = null;

      try {
         var1 = Class.forName(clazz);
         Method[] var2 = var1.getDeclaredMethods();
         return Arrays.asList(var2).get(new Random().nextInt(var2.length)).getName();
      } catch (ClassNotFoundException | IllegalArgumentException var3) {
         return "a";
      }
   }

   static String method7611() {
      method7612();
      return (String)bPr.get(new Random().nextInt(bPr.size()));
   }

   static void method7612() {
      if (bPr == null) {
         bPr = new ArrayList();
         CodeSource var0 = Main.class.getProtectionDomain().getCodeSource();
         if (var0 != null) {
            URL var1 = var0.getLocation();
            ZipInputStream var2 = null;

            try {
               var2 = new ZipInputStream(var1.openStream());

               while (true) {
                  ZipEntry var3 = var2.getNextEntry();
                  if (var3 == null) {
                     break;
                  }

                  String var4 = var3.getName();
                  if (var4.endsWith(".class")) {
                     bPr.add(var4.replace("\\", ".").replace("/", "."));
                  }
               }
            } catch (IOException var5) {
               var5.printStackTrace();
            }
         }
      }
   }
}
