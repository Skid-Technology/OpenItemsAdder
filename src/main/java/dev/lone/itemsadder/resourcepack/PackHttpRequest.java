package dev.lone.itemsadder.resourcepack;

import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.http.FullHttpRequest;
import java.io.InputStream;
import java.net.URI;

public class PackHttpRequest implements PackRequestView {
   private final FullHttpRequest bKy;
   private final URI bKz;

   public PackHttpRequest(FullHttpRequest var1) {
      this(var1, URI.create(var1.uri()));
   }

   public PackHttpRequest(FullHttpRequest var1, URI var2) {
      this.bKy = var1;
      this.bKz = var2;
   }

   @Override
   public String method() {
      return this.bKy.method().name();
   }

   @Override
   public String path() {
      return this.bKz.getPath();
   }

   @Override
   public InputStream bodyStream() {
      return new ByteBufInputStream(this.bKy.content());
   }

   public FullHttpRequest request() {
      return this.bKy;
   }

   public URI uri() {
      return this.bKz;
   }
}
