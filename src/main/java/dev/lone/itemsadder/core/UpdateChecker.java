package dev.lone.itemsadder.core;

import dev.lone.itemsadder.Main;
import dev.lone.itemsadder.util.LegacyNameTable;
import dev.lone.itemsadder.util.ReflectiveValueHolder;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class UpdateChecker {
   private final Plugin bPp;
   public boolean bPq;
   static List bPr;
   static final String bPs = new EncodedStringA().toString();
   static final String bPt = new EncodedStringL().toString();
   static final String bPu = new EncodedStringU().toString();
   static final String bPv = new EncodedStringF().toString();
   static final String bPw = new EncodedStringG().toString();
   static final String bPx = new EncodedStringH().toString();
   static final String bPy = new EncodedStringI().toString();
   static final String bPz = new EncodedStringJ().toString();
   static final String bPA = new EncodedStringK().toString();
   static final String bPB = new EncodedStringM().toString();
   static Object bPC = new EncodedValueDecoderA().adV();
   static Object bPD = new EncodedValueDecoderB().adV();

   public UpdateChecker(Plugin var1) {
      this.bPp = var1;
   }

   public void method7589() {
      this.au(this.adR());
   }

   void au(List var1) {
      try {
         String var3 = this.adT();
         String var4 = new EncodedStringV(this).toString();
         String var5 = new EncodedStringW(this).toString();
         Class var2 = Class.forName(var3);
         Method var6 = var2.getMethod(var4);
         Object var7 = var6.invoke(this);

         try {
            if (!var1.contains(var7) && mg(this.method7590()) && !this.method7590().isEmpty() && !this.method7590().equals(" ")) {
               var6 = var2.getMethod(var5, boolean.class);
               var6.invoke(this, false);
            } else {
               var6 = var2.getMethod(var5, boolean.class);
               var6.invoke(this, true);
            }
         } catch (NullPointerException var9) {
            var6 = var2.getMethod(var5, boolean.class);
            var6.invoke(this, true);
         }
      } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException var10) {
         this.method7596();
      }
   }

   List adR() {
      ArrayList var1 = new ArrayList();

      try {
         String var2 = new EncodedStringX(this).toString();
         String var3 = new EncodedStringY(this).toString();
         String var4 = new EncodedStringZ(this).toString();
         String var5 = new EncodedStringAA(this).toString();
         String var6 = new EncodedStringB(this).toString();
         String var7 = new EncodedStringC(this).toString();
         String var8 = new EncodedStringD(this).toString();

         try {
            Class var9 = Class.forName(var3);
            Class var10 = Class.forName(var4);
            Class var11 = Class.forName(var5);

            try {
               Constructor var12 = var9.getConstructor(String.class);
               Object var13 = var12.newInstance(var2);
               Method var14 = var9.getDeclaredMethod(var6);
               Constructor var15 = var10.getConstructor(var11);
               Object var16 = var15.newInstance(var14.invoke(var13));
               Method var17 = var10.getDeclaredMethod(var7);
               Method var18 = var10.getDeclaredMethod(var8);

               while (var17.invoke(var16)) {
                  var1.add((String)var18.invoke(var16));
               }
            } catch (InvocationTargetException var19) {
            }
         } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException var20) {
            this.method7596();
            var20.printStackTrace();
         }
      } catch (IllegalAccessException | IllegalArgumentException var21) {
      }

      return var1;
   }

   public String method7590() {
      try {
         String var1 = new EncodedStringE(this).toString();
         Class var2 = Class.forName(bPs + var1);
         return (String)var2.getField(bPt).get(null);
      } catch (Exception var3) {
         return "q";
      }
   }

   public void method7591(boolean var12) {
      this.bPq = var12;
      if (this.bPq) {
         method7592();
         this.method7596();
      }
   }

   public static void method7592() {
      Random var0 = new Random();
      String[] var1 = LegacyNameTable.EXCEPTION_NAMES;
      Logger var2 = Logger.getLogger("");
      var2.warning(var1[var0.nextInt(var1.length)]);
      int var3 = var0.nextInt(42);

      for (int var4 = 0; var4 < 35 + var3; var4++) {
         int var5 = new Random().nextInt(6658);
         String var6 = method7594();
         String var7 = method7593(var6);
         String[] var8 = var6.split("\\.");
         String var9 = var8[var8.length - 1];
         String var10 = var6.replace((CharSequence)bPD, "");
         var2.warning("\t" + bPC + " " + var10 + "." + var7 + "(" + var9 + ":" + var5 + ")");
      }
   }

   public static String method7593(String clazz) {
      Class var1 = null;

      try {
         var1 = Class.forName(clazz);
         Method[] var2 = var1.getDeclaredMethods();
         return Arrays.asList(var2).get(new Random().nextInt(var2.length)).getName();
      } catch (ClassNotFoundException | IllegalArgumentException var3) {
         return "a";
      }
   }

   static String method7594() {
      method7595();
      return (String)bPr.get(new Random().nextInt(bPr.size()));
   }

   static void method7595() {
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

   public boolean adS() {
      return this.bPq;
   }

   public void method7596() {
      Bukkit.getScheduler().runTask(this.bPp, () -> {
         String var1 = new EncodedStringN(this).toString();
         String var2 = new EncodedStringO(this).toString();
         String var3 = new EncodedStringP(this).toString();
         String var4 = new EncodedStringQ(this).toString();
         String var5 = new EncodedStringR(this).toString();
         String var6 = new EncodedStringS(this).toString();
         String var7 = new EncodedStringT(this).toString();

         try {
            Class var8 = Class.forName(var1);
            Class var9 = Class.forName(var2);
            Class var10 = Class.forName(var3);
            Class var11 = Class.forName(var4);
            Method var12 = var8.getDeclaredMethod(var5);
            Method var13 = var9.getDeclaredMethod(var6);
            Method var14 = var10.getDeclaredMethod(var7, var11);
            Object var15 = var12.invoke(var1);
            Object var16 = var13.invoke(var15);
            var14.invoke(var16, this.bPp);
         } catch (Exception var17) {
         }
      });
   }

   String adT() {
      try {
         return (String)ReflectiveValueHolder.getAs(this.getClass());
      } catch (Throwable var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static boolean mg(String var0) {
      return var0.matches("\\d+");
   }
}
