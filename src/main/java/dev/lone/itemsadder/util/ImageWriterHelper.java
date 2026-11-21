package dev.lone.itemsadder.util;

import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Iterator;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageWriterHelper {
   public static boolean writeImage(RenderedImage var0, String var1, File var2) {
      if (var2 == null) {
         throw new IllegalArgumentException("output == null!");
      }

      ImageWriter var3 = findWriter(var0, var1);
      if (var3 == null) {
         return false;
      }

      ImageOutputStream var4 = ImageIO.createImageOutputStream(var2);
      if (var4 == null) {
         throw new IIOException("Can't create an ImageOutputStream!");
      }

      try {
         return writeWith(var0, var3, var4);
      } finally {
         var4.close();
      }
   }

   private static ImageWriter findWriter(RenderedImage var0, String var1) {
      ImageTypeSpecifier var2 = ImageTypeSpecifier.createFromRenderedImage(var0);
      Iterator var3 = ImageIO.getImageWriters(var2, var1);
      return var3.hasNext() ? (ImageWriter)var3.next() : null;
   }

   private static boolean writeWith(RenderedImage var0, ImageWriter var1, ImageOutputStream var2) {
      if (var1 == null) {
         return false;
      }

      var1.setOutput(var2);

      try {
         var1.write(var0);
      } finally {
         var1.dispose();
         var2.flush();
      }

      return true;
   }
}
