package dev.lone.itemsadder.resourcepack;

import java.io.InputStream;

public interface PackRequestView {
   String method();

   String path();

   InputStream bodyStream();
}
