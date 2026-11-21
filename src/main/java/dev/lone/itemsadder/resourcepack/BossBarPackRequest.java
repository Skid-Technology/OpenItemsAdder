package dev.lone.itemsadder.resourcepack;

import com.google.gson.JsonObject;
import dev.lone.itemsadder.util.ConstantsHolder;
import dev.lone.itemsadder.util.GsonHelper;
import dev.lone.itemsadder.util.Log;
import dev.lone.itemsadder.util.ProgressHttpEntity;
import dev.lone.itemsadder.util.Utils;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.AbstractExecutionAwareRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class BossBarPackRequest extends PackRequestHandler {
   private static final Set bJh = ConcurrentHashMap.newKeySet();

   public static void abortAll() {
      bJh.forEach(AbstractExecutionAwareRequest::abort);
   }

   public BossBarPackRequest() {
      super("https://lobfile.com/api/v3/upload");
   }

   @Override
   public String uploadPack(ResourcePackGenerator var1, @Nullable Player var2) {
      String var3 = ConstantsHolder.apiKey;
      if (var3 != null && var3.length() >= 10) {
         this.bJg = true;
         Log.log(ChatColor.AQUA + "[Pack] Uploading resourcepack...");
         BossBar var4;
         if (var2 == null) {
            var4 = null;
         } else {
            var4 = Bukkit.createBossBar("Uploading...", BarColor.GREEN, BarStyle.SEGMENTED_6, new BarFlag[0]);
            var4.setProgress(0.0);
            var4.addPlayer(var2);
         }

         try {
            String var5 = uploadFile(var4, var3, var1.getPackFile());
            if (var5 == null) {
               Log.log("Pack URL is null.");
               return null;
            }

            if (var2 != null) {
               Log.sendLocalizedWith(var2, "resourcepack-uploaded-successfully", var1x -> var1x.replace("{url}", var5));
            }

            Log.log(ChatColor.GREEN + "[Pack] Finished uploading resourcepack: " + var5);
            return var5;
         } catch (Throwable var10) {
            Log.logToSender(var2, ChatColor.RED + "[Pack] Can't upload the resourcepack automatically to " + this.bJf + ". Error: " + var10);
            return null;
         } finally {
            this.bJg = false;
            if (var4 != null) {
               var4.removeAll();
            }
         }
      } else {
         Log.error(" ");
         Log.error(ChatColor.RED + "[Pack] Can't upload the resourcepack.");
         Log.error(ChatColor.RED + "[Pack] Please specify a api_key for LobFile in secret.yml file.");
         Log.error(ChatColor.RED + "[Pack] Read more here: https://a.devs.beer/lobfile-userhash");
         Log.error(" ");
         return null;
      }
   }

   private static String uploadFile(BossBar var0, String var1, File var2) {
      return doUpload(var0, var1, var2, false);
   }

   public static String publishFile(File var0) {
      String var1 = ConstantsHolder.apiKey;
      if (var1 != null && var1.length() >= 10) {
         return doUpload(null, var1, var0, true);
      } else {
         throw new IOException("Missing LobFile api_key");
      }
   }

   private static String doUpload(BossBar var0, String var1, File var2, boolean var3) {
      HttpPost var4 = new HttpPost("https://lobfile.com/api/v3/upload");

      try {
         CloseableHttpClient var5 = HttpClients.createDefault();

         Object var13;
         label332: {
            String var14;
            label333: {
               Object var38;
               try {
                  if (var3) {
                     bJh.add(var4);
                     if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedIOException("Client pack upload stopped");
                     }

                     var4.setConfig(RequestConfig.custom().setConnectTimeout(15000).setConnectionRequestTimeout(15000).setSocketTimeout(30000).build());
                  }

                  HttpEntity var6 = MultipartEntityBuilder.create().addTextBody("api_key", var1).addPart("file", new FileBody(var2)).build();
                  long var7 = var6.getContentLength();
                  ProgressHttpEntity var9 = new ProgressHttpEntity(var6, var3x -> {
                     if (var0 != null) {
                        var0.setTitle(Utils.humanReadableBytes(var3x) + " / " + Utils.humanReadableBytes(var7));
                        float var5x = Math.max((float)var3x / (float)var7, 1.0F);
                        var0.setProgress(var5x);
                     }
                  });
                  var4.setEntity(var9);
                  HttpEntity var10 = null;

                  try {
                     label317: {
                        CloseableHttpResponse var11 = var5.execute(var4);

                        label295: {
                           label318: {
                              try {
                                 var10 = var11.getEntity();
                                 if (var10 != null) {
                                    try {
                                       String var12 = EntityUtils.toString(var10, "UTF-8");
                                       JsonObject var39 = GsonHelper.parseObject(var12);
                                       var14 = var39.get("url").getAsString();
                                       break label318;
                                    } catch (Throwable var33) {
                                       Log.error("Failed to upload resourcepack, invalid server response.", var33);
                                       var13 = null;
                                       break label295;
                                    }
                                 }
                              } catch (Throwable var34) {
                                 if (var11 != null) {
                                    try {
                                       var11.close();
                                    } catch (Throwable var32) {
                                       var34.addSuppressed(var32);
                                    }
                                 }

                                 throw var34;
                              }

                              if (var11 != null) {
                                 var11.close();
                              }
                              break label317;
                           }

                           if (var11 != null) {
                              var11.close();
                           }
                           break label333;
                        }

                        if (var11 != null) {
                           var11.close();
                        }
                        break label332;
                     }
                  } finally {
                     EntityUtils.consume(var10);
                  }

                  var38 = null;
               } catch (Throwable var36) {
                  if (var5 != null) {
                     try {
                        var5.close();
                     } catch (Throwable var31) {
                        var36.addSuppressed(var31);
                     }
                  }

                  throw var36;
               }

               if (var5 != null) {
                  var5.close();
               }

               return (String)var38;
            }

            if (var5 != null) {
               var5.close();
            }

            return var14;
         }

         if (var5 != null) {
            var5.close();
         }

         return (String)var13;
      } finally {
         if (var3) {
            bJh.remove(var4);
         }
      }
   }
}
