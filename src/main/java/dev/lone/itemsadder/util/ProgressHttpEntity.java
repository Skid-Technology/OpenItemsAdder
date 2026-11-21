package dev.lone.itemsadder.util;

import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

public class ProgressHttpEntity extends HttpEntityWrapper {
   private final ProgressListener listener;

   public ProgressHttpEntity(HttpEntity var1, ProgressListener var2) {
      super(var1);
      this.listener = var2;
   }

   public void writeTo(OutputStream out) {
      this.wrappedEntity.writeTo(out instanceof ProgressOutputStream ? out : new ProgressOutputStream(out, this.listener));
   }
}
