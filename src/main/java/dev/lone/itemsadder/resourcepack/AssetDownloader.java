package dev.lone.itemsadder.resourcepack;

import java.net.URI;

@FunctionalInterface
interface AssetDownloader {
   byte[] download(URI var1, String var2, long var3, long var5);
}
