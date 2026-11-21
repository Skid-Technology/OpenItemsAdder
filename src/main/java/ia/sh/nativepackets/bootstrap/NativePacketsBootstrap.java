package ia.sh.nativepackets.bootstrap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient$Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse$BodySubscriber;
import java.net.http.HttpResponse.BodySubscribers;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.Flow.Subscription;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.bukkit.Server;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class NativePacketsBootstrap {
   private static final String MAIN = "beer.devs.nativepackets.bukkit.NativePacketsPlugin";
   private static final String METADATA = "META-INF/nativepackets/build.properties";
   private static final int METADATA_LIMIT = 262144;
   private static final int JAR_LIMIT = 134217728;
   static final List<String> GITHUB_RELEASE_PROXIES = List.of(
      "https://gh-proxy.com/",
      "https://ghproxy.net/",
      "https://ghfast.top/",
      "https://gh-proxy.org/",
      "https://gh.ddlc.top/",
      "https://gh.xmly.dev/",
      "https://mirror.houlang.cloud/",
      "https://proxy.vvvv.ee/"
   );
   private static final PublicKey RELEASE_KEY = releaseKey();

   private static PublicKey releaseKey() {
      try {
         return KeyFactory.getInstance("Ed25519")
            .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode("MCowBQYDK2VwAyEAGUO3HNSLhCRLG4dPA3RjzDimS7t09ezsL1LuHP+aUy4=")));
      } catch (GeneralSecurityException failure) {
         throw new ExceptionInInitializerError(failure);
      }
   }

   private NativePacketsBootstrap() {
   }

   public static NativePacketsBootstrap.Environment capture(JavaPlugin consumer) {
      Server server = consumer.getServer();
      Plugin installed = server.getPluginManager().getPlugin("NativePackets");
      String version = installed == null ? null : installed.getDescription().getVersion();
      int major = 0;
      if (installed != null) {
         try (InputStream input = installed.getResource("META-INF/nativepackets/build.properties")) {
            if (input != null) {
               major = apiMajor(properties(input), version);
            }
         } catch (IOException | IllegalArgumentException var11) {
         }
      }

      String platform = "SPIGOT";

      try {
         Class.forName("io.papermc.paper.configuration.GlobalConfiguration", false, server.getClass().getClassLoader());
         platform = "PAPER";
      } catch (ClassNotFoundException var8) {
      }

      Path updates = server.getUpdateFolder().isBlank() ? null : server.getUpdateFolderFile().toPath();
      return new NativePacketsBootstrap.Environment(
         consumer.getDataFolder().toPath().toAbsolutePath().getParent(),
         updates,
         minecraftVersion(server.getBukkitVersion()),
         platform,
         Runtime.version().feature(),
         version,
         major,
         installed != null && installed.isEnabled()
      );
   }

   static String minecraftVersion(String bukkitVersion) {
      return bukkitVersion.split("(?:\\.build\\.|-)", 2)[0];
   }

   public static boolean available(NativePacketsBootstrap.Environment environment, NativePacketsBootstrap.Requirement requirement) {
      return environment.enabled() && compatible(environment, requirement);
   }

   public static boolean compatible(NativePacketsBootstrap.Environment environment, NativePacketsBootstrap.Requirement requirement) {
      return environment.loadedVersion() != null
         && environment.loadedApiMajor() == requirement.apiMajor()
         && NativePacketsBootstrap.Version.parse(environment.loadedVersion()).compareTo(NativePacketsBootstrap.Version.parse(requirement.minimumVersion()))
            >= 0;
   }

   public static URI manifestEndpoint() {
      String value = System.getProperty(
            "nativepackets.manifestUrl", "https://github.com/LoneDev6/NativePackets-Releases/releases/latest/download/nativepackets-manifest.properties"
         )
         .trim();
      if (value.isEmpty()) {
         throw new IllegalArgumentException("Set -Dnativepackets.manifestUrl=https://<release-host>/manifest.properties or install NativePackets manually");
      } else {
         return URI.create(value);
      }
   }

   public static NativePacketsBootstrap.Result run(
      NativePacketsBootstrap.Environment environment, NativePacketsBootstrap.Requirement requirement, URI endpoint, NativePacketsBootstrap.Mode mode
   ) {
      try {
         if (mode == NativePacketsBootstrap.Mode.REQUIRE && available(environment, requirement)) {
            return result(NativePacketsBootstrap.Status.READY, environment.loadedVersion(), null, "NativePackets is compatible");
         }

         Path plugins = environment.plugins().toRealPath();
         Path lockPath = plugins.resolve(".nativepackets-bootstrap.lock");

         try (FileChannel channel = FileChannel.open(lockPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, LinkOption.NOFOLLOW_LINKS)) {
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(120L);

            while (!Thread.currentThread().isInterrupted()) {
               FileLock lock = null;

               try {
                  lock = channel.tryLock();
               } catch (OverlappingFileLockException var20) {
               }

               if (lock != null) {
                  try {
                     return locked(environment, requirement, endpoint, mode, plugins);
                  } finally {
                     lock.release();
                  }
               }

               if (System.nanoTime() > deadline) {
                  throw new IOException("Another NativePackets installer still holds the lock; retry after it finishes");
               }

               Thread.sleep(100L);
            }

            throw new InterruptedException();
         }
      } catch (InterruptedException failure) {
         Thread.currentThread().interrupt();
         return result(NativePacketsBootstrap.Status.FAILED, null, null, "NativePackets bootstrap interrupted");
      } catch (IOException | RuntimeException failure) {
         return result(NativePacketsBootstrap.Status.FAILED, null, null, "NativePackets bootstrap failed: " + failure.getMessage());
      }
   }

   private static NativePacketsBootstrap.Result locked(
      NativePacketsBootstrap.Environment environment,
      NativePacketsBootstrap.Requirement requirement,
      URI endpoint,
      NativePacketsBootstrap.Mode mode,
      Path plugins
   ) throws IOException, InterruptedException {
      NativePacketsBootstrap.Artifact installed = find(plugins, environment);
      if (environment.loadedVersion() != null && installed == null) {
         throw new IOException("Loaded NativePackets JAR is not in the plugins directory; update manually");
      }

      if (installed != null && installed.major() != requirement.apiMajor()) {
         throw new IOException("Installed NativePackets API major " + installed.major() + " does not match required API major " + requirement.apiMajor());
      }

      if (environment.loadedVersion() != null && environment.loadedApiMajor() != requirement.apiMajor()) {
         throw new IOException("Loaded NativePackets API major is incompatible or unknown; update manually");
      }

      if (mode != NativePacketsBootstrap.Mode.REQUIRE
         || installed == null
         || !satisfies(installed, requirement)
         || environment.loadedVersion() != null && installed.version().equals(environment.loadedVersion())) {
         if (mode == NativePacketsBootstrap.Mode.REQUIRE && available(environment, requirement)) {
            return result(NativePacketsBootstrap.Status.READY, environment.loadedVersion(), null, "NativePackets is compatible");
         }

         if (mode == NativePacketsBootstrap.Mode.REQUIRE && environment.loadedVersion() != null && satisfies(installed, requirement) && !environment.enabled()) {
            throw new IOException("Compatible NativePackets is installed but failed to enable; inspect its startup error");
         }

         Path updateDirectory = environment.updates();
         NativePacketsBootstrap.Artifact staged = null;
         if (updateDirectory != null && Files.exists(updateDirectory)) {
            if (Files.isSameFile(plugins, updateDirectory)) {
               throw new IOException("Bukkit update folder must differ from the plugins directory");
            }

            staged = find(updateDirectory, environment);
         }

         if (staged == null
            || staged.major() == requirement.apiMajor()
               && (
                  installed == null
                     || NativePacketsBootstrap.Version.parse(staged.version()).compareTo(NativePacketsBootstrap.Version.parse(installed.version())) >= 0
               )) {
            if (installed != null && staged != null && !staged.path().getFileName().equals(installed.path().getFileName())) {
               throw new IOException("Staged NativePackets filename differs from installed JAR; resolve it manually");
            }

            if (mode == NativePacketsBootstrap.Mode.REQUIRE && staged != null && satisfies(staged, requirement)) {
               return restart(staged);
            }

            String floor = installed == null ? null : installed.version();
            if (environment.loadedVersion() != null
               && (
                  floor == null || NativePacketsBootstrap.Version.parse(environment.loadedVersion()).compareTo(NativePacketsBootstrap.Version.parse(floor)) > 0
               )) {
               floor = environment.loadedVersion();
            }

            if (staged != null
               && (floor == null || NativePacketsBootstrap.Version.parse(staged.version()).compareTo(NativePacketsBootstrap.Version.parse(floor)) > 0)) {
               floor = staged.version();
            }

            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10L)).followRedirects(HttpClient$Redirect.NEVER).build();
            Properties manifest = properties(
               new ByteArrayInputStream(fetchVerified(endpoint, 262144, null, RELEASE_KEY, (url, limit, deadline) -> fetch(client, url, limit, deadline)))
            );
            NativePacketsBootstrap.Release release = select(manifest, environment, requirement, floor);
            if (release == null) {
               if (staged != null && satisfies(staged, requirement)) {
                  return restart(staged);
               } else if (mode != NativePacketsBootstrap.Mode.REQUIRE) {
                  return result(NativePacketsBootstrap.Status.CURRENT, floor, null, "No newer compatible NativePackets release");
               } else {
                  throw new IOException("Manifest contains no compatible NativePackets release meeting " + requirement);
               }
            } else {
               if (mode == NativePacketsBootstrap.Mode.NOTIFY) {
                  return result(
                     NativePacketsBootstrap.Status.UPDATE_AVAILABLE,
                     release.version(),
                     null,
                     "NativePackets " + release.version() + " is available; install it and restart"
                  );
               }

               Path directory;
               Path target;
               if (installed == null) {
                  directory = plugins;
                  target = plugins.resolve("NativePackets.jar");
                  if (Files.exists(target, LinkOption.NOFOLLOW_LINKS)) {
                     throw new IOException("NativePackets.jar already exists but is not a valid NativePackets plugin");
                  }
               } else {
                  if (updateDirectory == null) {
                     throw new IOException("Bukkit update folder is disabled; install the update manually while stopped");
                  }

                  Files.createDirectories(updateDirectory);
                  directory = updateDirectory.toRealPath();
                  if (Files.isSameFile(plugins, directory)) {
                     throw new IOException("Bukkit update folder must differ from the plugins directory");
                  }

                  target = directory.resolve(installed.path().getFileName());
                  if (Files.exists(target, LinkOption.NOFOLLOW_LINKS) && (staged == null || Files.isSymbolicLink(target))) {
                     throw new IOException("Update destination is occupied by an unrecognized file");
                  }
               }

               Path temporary = directory.resolve(".nativepackets-download.tmp");

               try {
                  byte[] bytes = fetchVerified(
                     release.url(), 134217728, release.method129(), RELEASE_KEY, (url, limit, deadline) -> fetch(client, url, limit, deadline)
                  );

                  try (FileChannel output = FileChannel.open(
                        temporary, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE, LinkOption.NOFOLLOW_LINKS
                     )) {
                     ByteBuffer buffer = ByteBuffer.wrap(bytes);

                     while (buffer.hasRemaining()) {
                        output.write(buffer);
                     }

                     output.force(true);
                  }

                  NativePacketsBootstrap.Artifact verified = inspect(temporary, environment, true);
                  if (verified == null || !verified.version().equals(release.version()) || verified.major() != release.major()) {
                     throw new IOException("Downloaded JAR identity/version/API does not match NativePackets manifest");
                  }

                  if (Thread.currentThread().isInterrupted()) {
                     throw new InterruptedException();
                  }

                  Files.move(temporary, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                  return restart(new NativePacketsBootstrap.Artifact(target, verified.version(), verified.major()));
               } finally {
                  Files.deleteIfExists(temporary);
               }
            }
         } else {
            throw new IOException("Conflicting NativePackets JAR in update folder; resolve it manually");
         }
      } else {
         return restart(installed);
      }
   }

   private static NativePacketsBootstrap.Result result(NativePacketsBootstrap.Status status, String version, Path path, String message) {
      return new NativePacketsBootstrap.Result(status, version, path, message);
   }

   private static NativePacketsBootstrap.Result restart(NativePacketsBootstrap.Artifact artifact) {
      return result(
         NativePacketsBootstrap.Status.RESTART_REQUIRED,
         artifact.version(),
         artifact.path(),
         "NativePackets " + artifact.version() + " prepared at " + artifact.path() + "; restart required"
      );
   }

   private static boolean satisfies(NativePacketsBootstrap.Artifact artifact, NativePacketsBootstrap.Requirement requirement) {
      return artifact != null
         && artifact.major() == requirement.apiMajor()
         && NativePacketsBootstrap.Version.parse(artifact.version()).compareTo(NativePacketsBootstrap.Version.parse(requirement.minimumVersion())) >= 0;
   }

   static NativePacketsBootstrap.Release select(
      Properties manifest, NativePacketsBootstrap.Environment environment, NativePacketsBootstrap.Requirement requirement, String floor
   ) throws IOException {
      if (!"1".equals(manifest.getProperty("schema"))) {
         throw new IOException("Unsupported NativePackets manifest schema");
      }

      NativePacketsBootstrap.Release selected = null;
      String[] ids = required(manifest, "releases").split(",");
      if (ids.length > 256) {
         throw new IOException("Too many manifest releases");
      }

      for (String id : ids) {
         if (!id.matches("[a-zA-Z0-9_-]+")) {
            throw new IOException("Invalid release identifier");
         }

         String prefix = id + ".";
         String version = required(manifest, prefix + "version");
         NativePacketsBootstrap.Version parsed = NativePacketsBootstrap.Version.parse(version);
         int major = Integer.parseInt(required(manifest, prefix + "apiMajor"));
         int java = Integer.parseInt(required(manifest, prefix + "minJava"));
         List<String> minecraft = List.of(required(manifest, prefix + "minecraft").split(","));
         String sha = required(manifest, prefix + "sha256");
         if (!sha.matches("[a-fA-F0-9]{64}")) {
            throw new IOException("Invalid SHA-256 in manifest");
         }

         URI url = URI.create(required(manifest, prefix + "url"));
         validateUrl(url);
         if (major == requirement.apiMajor()
            && java <= environment.javaVersion()
            && minecraft.contains(environment.minecraftVersion())
            && parsed.compareTo(NativePacketsBootstrap.Version.parse(requirement.minimumVersion())) >= 0
            && (floor == null || parsed.compareTo(NativePacketsBootstrap.Version.parse(floor)) > 0)
            && (selected == null || parsed.compareTo(NativePacketsBootstrap.Version.parse(selected.version())) > 0)) {
            selected = new NativePacketsBootstrap.Release(version, major, sha, url);
         }
      }

      return selected;
   }

   private static NativePacketsBootstrap.Artifact find(Path directory, NativePacketsBootstrap.Environment environment) throws IOException {
      NativePacketsBootstrap.Artifact found = null;

      try (DirectoryStream<Path> entries = Files.newDirectoryStream(directory, "*.jar")) {
         for (Path path : entries) {
            NativePacketsBootstrap.Artifact artifact = inspect(path, environment, false);
            if (artifact != null) {
               if (found != null) {
                  throw new IOException("Multiple NativePackets JARs in " + directory + "; resolve manually");
               }

               found = artifact;
            }
         }
      }

      return found;
   }

   private static NativePacketsBootstrap.Artifact inspect(Path path, NativePacketsBootstrap.Environment environment, boolean downloaded) throws IOException {
      if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {
         throw new IOException("Plugin JAR is not a regular file: " + path);
      }

      try (JarFile jar = new JarFile(path.toFile())) {
         JarEntry descriptorEntry = jar.getJarEntry("plugin.yml");
         if (descriptorEntry == null) {
            return null;
         }

         PluginDescriptionFile descriptor;
         try (InputStream input = jar.getInputStream(descriptorEntry)) {
            descriptor = new PluginDescriptionFile(new ByteArrayInputStream(bounded(input, 262144)));
         }

         if (!"NativePackets".equals(descriptor.getName())) {
            return null;
         }

         if (!"beer.devs.nativepackets.bukkit.NativePacketsPlugin".equals(descriptor.getMain())
            || jar.getJarEntry("beer.devs.nativepackets.bukkit.NativePacketsPlugin".replace('.', '/') + ".class") == null) {
            throw new IOException("NativePackets JAR has an invalid entrypoint");
         }

         Properties metadata = jarProperties(jar, "META-INF/nativepackets/build.properties");
         String version = descriptor.getVersion();
         NativePacketsBootstrap.Version.parse(version);
         if (!"NativePackets".equals(metadata.getProperty("name")) || !version.equals(metadata.getProperty("version"))) {
            throw new IOException("NativePackets JAR metadata does not match plugin.yml");
         }

         int major = apiMajor(metadata, version);
         if (major < 1 || downloaded && !metadata.containsKey("apiMajor")) {
            throw new IOException("NativePackets JAR API major is missing");
         }

         if (Integer.parseInt(required(metadata, "javaTarget")) > environment.javaVersion()) {
            throw new IOException("NativePackets JAR needs a newer Java runtime");
         }

         String adapterPlatform = environment.platform();
         if ("PAPER".equals(adapterPlatform) && ("1.19.4".equals(environment.minecraftVersion()) || "1.20.4".equals(environment.minecraftVersion()))) {
            adapterPlatform = "SPIGOT";
         }

         String index = "META-INF/nativepackets/adapters/";
         Properties adapter = jarProperties(jar, index + adapterPlatform.toLowerCase(Locale.ROOT) + "-" + environment.minecraftVersion() + ".properties");
         if (!environment.minecraftVersion().equals(adapter.getProperty("minecraftVersion"))
            || !adapterPlatform.equals(adapter.getProperty("platform"))
            || Integer.parseInt(required(adapter, "javaVersion")) > environment.javaVersion()
            || jar.getJarEntry(required(adapter, "resource")) == null) {
            throw new IOException("NativePackets JAR does not support this exact server runtime");
         } else {
            return new NativePacketsBootstrap.Artifact(path, version, major);
         }
      } catch (InvalidDescriptionException failure) {
         throw new IOException("Invalid plugin descriptor in " + path.getFileName(), failure);
      }
   }

   private static int apiMajor(Properties metadata, String version) {
      String value = metadata.getProperty("apiMajor");
      return value == null ? ("0.1.0-SNAPSHOT".equals(version) ? 1 : 0) : Integer.parseInt(value);
   }

   private static Properties jarProperties(JarFile jar, String name) throws IOException {
      JarEntry entry = jar.getJarEntry(name);
      if (entry == null) {
         throw new IOException("NativePackets JAR is missing " + name);
      }

      try (InputStream input = jar.getInputStream(entry)) {
         return properties(input);
      }
   }

   private static Properties properties(InputStream input) throws IOException {
      Properties properties = new Properties();
      properties.load(new ByteArrayInputStream(bounded(input, 262144)));
      return properties;
   }

   private static byte[] bounded(InputStream input, int limit) throws IOException {
      byte[] bytes = input.readNBytes(limit + 1);
      if (bytes.length > limit) {
         throw new IOException("NativePackets response/metadata exceeds size limit");
      } else {
         return bytes;
      }
   }

   private static String required(Properties properties, String key) throws IOException {
      String value = properties.getProperty(key);
      if (value != null && !value.isBlank()) {
         return value.trim();
      } else {
         throw new IOException("Missing manifest/metadata field: " + key);
      }
   }

   private static void validateUrl(URI url) throws IOException {
      boolean testLoopback = Boolean.getBoolean("nativepackets.allowLoopbackHttp")
         && "http".equals(url.getScheme())
         && ("127.0.0.1".equals(url.getHost()) || "[::1]".equals(url.getHost()));
      if (!"https".equals(url.getScheme()) && !testLoopback || url.getHost() == null || url.getUserInfo() != null || url.getFragment() != null) {
         throw new IOException("NativePackets downloads require HTTPS (HTTP allowed only for explicit loopback tests)");
      }
   }

   static byte[] fetchVerified(URI source, int limit, String expectedHash, PublicKey key, NativePacketsBootstrap.DownloadAttempt download) throws IOException, InterruptedException {
      validateUrl(source);
      boolean github = "github.com".equalsIgnoreCase(source.getHost()) && githubDownloadHost(source);
      int attempts = github ? 1 + GITHUB_RELEASE_PROXIES.size() : 1;
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(90L);
      IOException last = null;

      for (int attempt = 0; attempt < attempts; attempt++) {
         if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
         }

         if (System.nanoTime() >= deadline) {
            break;
         }

         URI url = attempt == 0 ? source : URI.create(GITHUB_RELEASE_PROXIES.get(attempt - 1) + source);

         try {
            byte[] bytes = download.fetch(url, limit, Math.min(deadline, System.nanoTime() + TimeUnit.SECONDS.toNanos(10L)));
            if (bytes.length > limit) {
               throw new IOException("NativePackets response exceeds size limit");
            }

            if (expectedHash != null) {
               if (!HexFormat.of().formatHex(method1900(bytes)).equalsIgnoreCase(expectedHash)) {
                  throw new IOException("NativePackets SHA-256 mismatch");
               }

               return bytes;
            }

            if ("http".equals(source.getScheme())) {
               return bytes;
            }

            return verifyManifest(bytes, key);
         } catch (IOException failure) {
            last = failure;
         }
      }

      throw new IOException("NativePackets download failed on all available routes: " + (last == null ? "deadline exceeded" : last.getMessage()), last);
   }

   static byte[] verifyManifest(byte[] signed, PublicKey key) throws IOException {
      int newline = -1;

      for (int i = 0; i < Math.min(signed.length, 128); i++) {
         if (signed[i] == 10) {
            newline = i;
            break;
         }
      }

      if (newline < 0) {
         throw new IOException("NativePackets manifest signature is missing");
      }

      String header = new String(signed, 0, newline, StandardCharsets.US_ASCII);
      if (!header.startsWith("signature=")) {
         throw new IOException("NativePackets manifest signature is missing");
      }

      try {
         byte[] signature = Base64.getDecoder().decode(header.substring("signature=".length()));
         Signature verifier = Signature.getInstance("Ed25519");
         verifier.initVerify(key);
         verifier.update(signed, newline + 1, signed.length - newline - 1);
         if (signature.length == 64 && verifier.verify(signature)) {
            return Arrays.copyOfRange(signed, newline + 1, signed.length);
         } else {
            throw new IOException("NativePackets manifest signature is invalid");
         }
      } catch (GeneralSecurityException | IllegalArgumentException failure) {
         throw new IOException("NativePackets manifest signature is invalid", failure);
      }
   }

   private static byte[] fetch(HttpClient client, URI url, int limit, long deadline) throws IOException, InterruptedException {
      validateUrl(url);
      int redirects = 0;

      while (true) {
         long remaining = deadline - System.nanoTime();
         if (remaining <= 0L) {
            throw new IOException("NativePackets download timed out");
         }

         HttpRequest request = HttpRequest.newBuilder(url).timeout(Duration.ofNanos(remaining)).header("User-Agent", "NativePackets-Bootstrap/1").GET().build();
         CompletableFuture<HttpResponse<byte[]>> pending = client.sendAsync(request, info -> new NativePacketsBootstrap.LimitedBody(limit));

         HttpResponse<byte[]> response;
         try {
            response = pending.get(remaining, TimeUnit.NANOSECONDS);
         } catch (TimeoutException failure) {
            pending.cancel(true);
            throw new IOException("NativePackets download timed out", failure);
         } catch (ExecutionException failure) {
            throw new IOException("NativePackets request failed: " + failure.getCause().getMessage(), failure.getCause());
         } catch (InterruptedException failure) {
            pending.cancel(true);
            throw failure;
         }

         int status = response.statusCode();
         if (status == 200) {
            return response.body();
         }

         if (status != 301 && status != 302 && status != 303 && status != 307 && status != 308) {
            throw new IOException("NativePackets endpoint returned HTTP " + status);
         }

         if (redirects == 3) {
            throw new IOException("Too many NativePackets download redirects");
         }

         String location = response.headers().firstValue("Location").orElseThrow(() -> new IOException("NativePackets redirect has no Location"));
         url = githubRedirect(url, location);
         redirects++;
      }
   }

   static URI githubRedirect(URI source, String location) throws IOException {
      URI target;
      try {
         target = source.resolve(location);
      } catch (IllegalArgumentException failure) {
         throw new IOException("Invalid NativePackets redirect", failure);
      }

      validateUrl(target);
      if (githubDownloadHost(source) && githubDownloadHost(target) || proxyHost(source) && (sameOrigin(source, target) || githubDownloadHost(target))) {
         return target;
      } else {
         throw new IOException("NativePackets redirects are restricted to GitHub release downloads or the selected proxy");
      }
   }

   private static boolean sameOrigin(URI first, URI second) {
      return first.getScheme().equals(second.getScheme()) && first.getHost().equalsIgnoreCase(second.getHost()) && first.getPort() == second.getPort();
   }

   private static boolean proxyHost(URI url) {
      if ("https".equals(url.getScheme()) && url.getUserInfo() == null && url.getFragment() == null && (url.getPort() == -1 || url.getPort() == 443)) {
         for (String proxy : GITHUB_RELEASE_PROXIES) {
            if (URI.create(proxy).getHost().equalsIgnoreCase(url.getHost())) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static boolean githubDownloadHost(URI url) {
      if ("https".equals(url.getScheme()) && url.getUserInfo() == null && url.getFragment() == null && (url.getPort() == -1 || url.getPort() == 443)) {
         String host = url.getHost();
         return "github.com".equalsIgnoreCase(host) && url.getPath().startsWith("/LoneDev6/NativePackets-Releases/releases/")
            || "release-assets.githubusercontent.com".equalsIgnoreCase(host)
            || "github-releases.githubusercontent.com".equalsIgnoreCase(host)
            || "objects.githubusercontent.com".equalsIgnoreCase(host);
      } else {
         return false;
      }
   }

   private static byte[] method1900(byte[] bytes) {
      try {
         return MessageDigest.getInstance("SHA-256").digest(bytes);
      } catch (NoSuchAlgorithmException impossible) {
         throw new AssertionError(impossible);
      }
   }

   private record Artifact(Path path, String version, int major) {
   }

   @FunctionalInterface
   interface DownloadAttempt {
      byte[] fetch(URI var1, int var2, long var3) throws IOException, InterruptedException;
   }

   public record Environment(
      Path plugins, Path updates, String minecraftVersion, String platform, int javaVersion, String loadedVersion, int loadedApiMajor, boolean enabled
   ) {
   }

   private static final class LimitedBody implements HttpResponse$BodySubscriber<byte[]> {
      private final HttpResponse$BodySubscriber<byte[]> delegate = BodySubscribers.ofByteArray();
      private final int limit;
      private int received;
      private Subscription subscription;

      private LimitedBody(int limit) {
         this.limit = limit;
      }

      @Override
      public CompletionStage<byte[]> getBody() {
         return this.delegate.getBody();
      }

      @Override
      public void onSubscribe(Subscription value) {
         this.subscription = value;
         this.delegate.onSubscribe(value);
      }

      public void onNext(List<ByteBuffer> buffers) {
         for (ByteBuffer buffer : buffers) {
            if (buffer.remaining() > this.limit - this.received) {
               this.subscription.cancel();
               this.delegate.onError(new IOException("NativePackets download exceeds size limit"));
               return;
            }

            this.received = this.received + buffer.remaining();
         }

         this.delegate.onNext(buffers);
      }

      @Override
      public void onError(Throwable error) {
         this.delegate.onError(error);
      }

      @Override
      public void onComplete() {
         this.delegate.onComplete();
      }
   }

   public enum Mode {
      REQUIRE,
      NOTIFY,
      DOWNLOAD;
   }

   record Release(String version, int major, String sha256, URI url) {
      public String method129() {
         return this.sha256;
      }
   }

   public record Requirement(int apiMajor, String minimumVersion) {
      public Requirement {
         if (apiMajor < 1) {
            throw new IllegalArgumentException("API major must be positive");
         }

         NativePacketsBootstrap.Version.parse(minimumVersion);
      }
   }

   public record Result(NativePacketsBootstrap.Status status, String version, Path file, String message) {
   }

   public enum Status {
      READY,
      RESTART_REQUIRED,
      UPDATE_AVAILABLE,
      CURRENT,
      FAILED;
   }

   record Version(List<BigInteger> numbers, String prerelease) implements Comparable<NativePacketsBootstrap.Version> {
      static NativePacketsBootstrap.Version parse(String value) {
         if (value != null
            && value.matches("(0|[1-9][0-9]*)\\.(0|[1-9][0-9]*)\\.(0|[1-9][0-9]*)(-[0-9A-Za-z-]+(\\.[0-9A-Za-z-]+)*)?(\\+[0-9A-Za-z-]+(\\.[0-9A-Za-z-]+)*)?")) {
            String[] parts = value.split("\\+", 2)[0].split("-", 2);
            List<BigInteger> numbers = new ArrayList<>(3);

            for (String number : parts[0].split("\\.")) {
               numbers.add(new BigInteger(number));
            }

            String prerelease = parts.length == 1 ? "" : parts[1];

            for (String part : prerelease.split("\\.")) {
               if (part.matches("0[0-9]+")) {
                  throw new IllegalArgumentException("Invalid numeric prerelease: " + value);
               }
            }

            return new NativePacketsBootstrap.Version(numbers, prerelease);
         } else {
            throw new IllegalArgumentException("Invalid NativePackets semantic version: " + value);
         }
      }

      public int compareTo(NativePacketsBootstrap.Version other) {
         for (int i = 0; i < 3; i++) {
            int order = this.numbers.get(i).compareTo(other.numbers.get(i));
            if (order != 0) {
               return order;
            }
         }

         if (this.prerelease.equals(other.prerelease)) {
            return 0;
         }

         if (this.prerelease.isEmpty()) {
            return 1;
         }

         if (other.prerelease.isEmpty()) {
            return -1;
         }

         String[] left = this.prerelease.split("\\.");
         String[] right = other.prerelease.split("\\.");

         for (int i = 0; i < Math.min(left.length, right.length); i++) {
            boolean ln = left[i].matches("[0-9]+");
            boolean rn = right[i].matches("[0-9]+");
            int order = ln && rn ? new BigInteger(left[i]).compareTo(new BigInteger(right[i])) : (ln != rn ? (ln ? -1 : 1) : left[i].compareTo(right[i]));
            if (order != 0) {
               return order;
            }
         }

         return Integer.compare(left.length, right.length);
      }
   }
}
