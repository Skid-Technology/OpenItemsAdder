package ia.sh.libby;

import ia.sh.libby.classloader.IsolatedClassLoader;
import ia.sh.libby.configuration.Configuration;
import ia.sh.libby.configuration.ConfigurationFetcher;
import ia.sh.libby.logging.LogLevel;
import ia.sh.libby.logging.Logger;
import ia.sh.libby.logging.adapters.LogAdapter;
import ia.sh.libby.relocation.Relocation;
import ia.sh.libby.relocation.RelocationHelper;
import ia.sh.libby.transitive.TransitiveDependencyHelper;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class LibraryManager {
   protected final Logger logger;
   protected final Path saveDirectory;
   protected final Set<String> repositories = new LinkedHashSet<>();
   protected RelocationHelper relocator;
   protected TransitiveDependencyHelper transitiveDependencyHelper;
   protected ConfigurationFetcher configurationFetcher;
   protected final IsolatedClassLoader globalIsolatedClassLoader = new IsolatedClassLoader();
   protected final Map<String, IsolatedClassLoader> isolatedLibraries = new HashMap<>();
   protected RepositoryResolutionMode repositoryResolutionMode = RepositoryResolutionMode.DEFAULT;

   @Deprecated
   protected LibraryManager(@NotNull LogAdapter logAdapter, @NotNull Path dataDirectory) {
      this.logger = new Logger(Objects.requireNonNull(logAdapter, "logAdapter"));
      this.saveDirectory = Objects.requireNonNull(dataDirectory, "dataDirectory").toAbsolutePath().resolve("lib");
   }

   protected LibraryManager(@NotNull LogAdapter logAdapter, @NotNull Path dataDirectory, @NotNull String directoryName) {
      this.logger = new Logger(Objects.requireNonNull(logAdapter, "logAdapter"));
      this.saveDirectory = Objects.requireNonNull(dataDirectory, "dataDirectory")
         .toAbsolutePath()
         .resolve(Objects.requireNonNull(directoryName, "directoryName"));
   }

   protected abstract void addToClasspath(@NotNull Path var1);

   protected void addToIsolatedClasspath(@NotNull Library library, @NotNull Path file) {
      String loaderId = library.getLoaderId();
      IsolatedClassLoader classLoader;
      if (loaderId != null) {
         classLoader = this.isolatedLibraries.computeIfAbsent(loaderId, s -> new IsolatedClassLoader());
      } else {
         classLoader = this.globalIsolatedClassLoader;
      }

      classLoader.addPath(file);
   }

   @NotNull
   public IsolatedClassLoader getGlobalIsolatedClassLoader() {
      return this.globalIsolatedClassLoader;
   }

   @Nullable
   public IsolatedClassLoader getIsolatedClassLoaderById(@NotNull String loaderId) {
      return this.isolatedLibraries.get(loaderId);
   }

   @NotNull
   public LogLevel getLogLevel() {
      return this.logger.getLevel();
   }

   public void setLogLevel(@NotNull LogLevel level) {
      this.logger.setLevel(level);
   }

   @NotNull
   public Logger getLogger() {
      return this.logger;
   }

   @NotNull
   public Collection<String> getRepositories() {
      List<String> urls;
      synchronized (this.repositories) {
         urls = new LinkedList<>(this.repositories);
      }

      return Collections.unmodifiableList(urls);
   }

   public void addRepository(@NotNull String url) {
      String repo = Objects.requireNonNull(url, "url").endsWith("/") ? url : url + '/';
      synchronized (this.repositories) {
         this.repositories.add(repo);
      }
   }

   public void addMavenLocal() {
      this.addRepository(Paths.get(System.getProperty("user.home")).resolve(".m2/repository").toUri().toString());
   }

   public void addMavenCentral() {
      this.addRepository("https://repo1.maven.org/maven2/");
   }

   public void addSonatype() {
      this.addRepository("https://oss.sonatype.org/content/groups/public/");
   }

   public void addJCenter() {
      this.addRepository("https://jcenter.bintray.com/");
   }

   public void addJitPack() {
      this.addRepository("https://jitpack.io/");
   }

   public RepositoryResolutionMode getRepositoryResolutionMode() {
      return this.repositoryResolutionMode;
   }

   public void setRepositoryResolutionMode(RepositoryResolutionMode repositoryResolutionMode) {
      this.repositoryResolutionMode = repositoryResolutionMode;
   }

   @NotNull
   public Collection<String> resolveLibrary(@NotNull Library library) {
      Set<String> urls = new LinkedHashSet<>(Objects.requireNonNull(library, "library").getUrls());
      boolean snapshot = library.isSnapshot();

      for (String repository : this.resolveRepositories(library)) {
         if (snapshot) {
            String url = this.resolveSnapshot(repository, library);
            if (url != null) {
               urls.add(repository + url);
            }
         } else {
            urls.add(repository + library.getPath());
         }
      }

      return Collections.unmodifiableSet(urls);
   }

   public Collection<String> resolveRepositories(@NotNull Library library) {
      switch (this.getRepositoryResolutionMode()) {
         case GLOBAL_FIRST:
            return Stream.of(this.getRepositories(), library.getRepositories(), library.getFallbackRepositories())
               .flatMap(Collection::stream)
               .collect(Collectors.toCollection(LinkedHashSet::new));
         case LIBRARY_FIRST:
            return Stream.of(library.getRepositories(), library.getFallbackRepositories(), this.getRepositories())
               .flatMap(Collection::stream)
               .collect(Collectors.toCollection(LinkedHashSet::new));
         case DEFAULT:
         default:
            return Stream.of(library.getRepositories(), this.getRepositories(), library.getFallbackRepositories())
               .flatMap(Collection::stream)
               .collect(Collectors.toCollection(LinkedHashSet::new));
      }
   }

   @Nullable
   protected String resolveSnapshot(@NotNull String repository, @NotNull Library library) {
      String mavenMetadata = repository.startsWith("file") ? "maven-metadata-local.xml" : "maven-metadata.xml";
      String url = Objects.requireNonNull(repository, "repository") + Objects.requireNonNull(library, "library").getPartialPath() + mavenMetadata;

      try {
         URLConnection connection = new URL(Objects.requireNonNull(url, "url")).openConnection();
         connection.setConnectTimeout(5000);
         connection.setReadTimeout(5000);
         connection.setRequestProperty("User-Agent", "libby/2.0.0-SNAPSHOT");
         InputStream in = connection.getInputStream();

         String var7;
         try {
            var7 = this.getURLFromMetadata(in, library);
         } catch (Throwable var10) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }
            }

            throw var10;
         }

         if (in != null) {
            in.close();
         }

         return var7;
      } catch (MalformedURLException e) {
         throw new IllegalArgumentException(e);
      } catch (IOException e) {
         if (e instanceof FileNotFoundException) {
            this.logger.debug("File not found: " + url);
         } else if (e instanceof SocketTimeoutException) {
            this.logger.debug("Connect timed out: " + url);
         } else if (e instanceof UnknownHostException) {
            this.logger.debug("Unknown host: " + url);
         } else {
            this.logger.debug("Unexpected IOException", e);
         }

         return null;
      }
   }

   @Nullable
   protected String getURLFromMetadata(@NotNull InputStream inputStream, @NotNull Library library) throws IOException {
      Objects.requireNonNull(inputStream, "inputStream");
      Objects.requireNonNull(library, "library");
      String version = library.getVersion();

      try {
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputStream);
         doc.getDocumentElement().normalize();
         NodeList nodes = doc.getElementsByTagName("snapshot");
         if (nodes.getLength() == 0) {
            return null;
         }

         Node snapshot = nodes.item(0);
         if (snapshot.getNodeType() != 1) {
            return null;
         }

         Node localCopyNode = ((Element)snapshot).getElementsByTagName("localCopy").item(0);
         if (localCopyNode == null || localCopyNode.getNodeType() != 1) {
            Node timestampNode = ((Element)snapshot).getElementsByTagName("timestamp").item(0);
            if (timestampNode != null && timestampNode.getNodeType() == 1) {
               Node buildNumberNode = ((Element)snapshot).getElementsByTagName("buildNumber").item(0);
               if (buildNumberNode != null && buildNumberNode.getNodeType() == 1) {
                  Node timestampChild = timestampNode.getFirstChild();
                  if (timestampChild != null && timestampChild.getNodeType() == 3) {
                     Node buildNumberChild = buildNumberNode.getFirstChild();
                     if (buildNumberChild != null && buildNumberChild.getNodeType() == 3) {
                        String timestamp = timestampChild.getNodeValue();
                        String buildNumber = buildNumberChild.getNodeValue();
                        version = library.getVersion();
                        if (version.endsWith("-SNAPSHOT")) {
                           version = version.substring(0, version.length() - "-SNAPSHOT".length());
                        }

                        version = version + '-' + timestamp + '-' + buildNumber;
                        return Util.craftPath(library.getPartialPath(), library.getArtifactId(), version, library.getClassifier());
                     }

                     return null;
                  }

                  return null;
               }

               return null;
            }

            return null;
         }
      } catch (ParserConfigurationException | SAXException e) {
         this.logger.debug("Invalid maven-metadata.xml", e);
         return null;
      }

      return Util.craftPath(library.getPartialPath(), library.getArtifactId(), version, library.getClassifier());
   }

   protected byte[] downloadLibrary(@NotNull String url) {
      try {
         URLConnection connection = new URL(Objects.requireNonNull(url, "url")).openConnection();
         connection.setConnectTimeout(5000);
         connection.setReadTimeout(5000);
         connection.setRequestProperty("User-Agent", "libby/2.0.0-SNAPSHOT");
         InputStream in = connection.getInputStream();

         byte[] e;
         label72: {
            Object var8;
            try {
               label73: {
                  byte[] buf = new byte[8192];
                  ByteArrayOutputStream out = new ByteArrayOutputStream();

                  int len;
                  try {
                     while ((len = in.read(buf)) != -1) {
                        out.write(buf, 0, len);
                     }
                  } catch (SocketTimeoutException ex) {
                     this.logger.warn("Download timed out: " + connection.getURL());
                     var8 = null;
                     break label73;
                  }

                  this.logger.info("Downloaded library " + connection.getURL());
                  e = out.toByteArray();
                  break label72;
               }
            } catch (Throwable var11) {
               if (in != null) {
                  try {
                     in.close();
                  } catch (Throwable var9) {
                     var11.addSuppressed(var9);
                  }
               }

               throw var11;
            }

            if (in != null) {
               in.close();
            }

            return (byte[])var8;
         }

         if (in != null) {
            in.close();
         }

         return e;
      } catch (MalformedURLException e) {
         throw new IllegalArgumentException(e);
      } catch (IOException e) {
         if (e instanceof FileNotFoundException) {
            this.logger.debug("File not found: " + url);
         } else if (e instanceof SocketTimeoutException) {
            this.logger.debug("Connect timed out: " + url);
         } else if (e instanceof UnknownHostException) {
            this.logger.debug("Unknown host: " + url);
         } else {
            this.logger.debug("Unexpected IOException", e);
         }

         return null;
      }
   }

   @NotNull
   public Path downloadLibrary(@NotNull Library library) {
      Path file = this.saveDirectory.resolve(Objects.requireNonNull(library, "library").getPath());
      label127:
      if (!Files.exists(file)) {
         Collection<String> urls = this.resolveLibrary(library);
         if (urls.isEmpty()) {
            throw new RuntimeException("Library '" + library + "' couldn't be resolved, add a repository");
         }

         MessageDigest md = null;
         if (library.hasChecksum()) {
            try {
               md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
               throw new RuntimeException(e);
            }
         }

         Path out = file.resolveSibling(file.getFileName() + ".tmp");
         out.toFile().deleteOnExit();

         try {
            Files.createDirectories(file.getParent());

            for (String url : urls) {
               byte[] bytes = this.downloadLibrary(url);
               if (bytes != null) {
                  if (md != null) {
                     byte[] checksum = md.digest(bytes);
                     if (!Arrays.equals(checksum, library.getChecksum())) {
                        this.logger.warn("*** INVALID CHECKSUM ***");
                        this.logger.warn(" Library :  " + library);
                        this.logger.warn(" URL :  " + url);
                        this.logger.warn(" Expected :  " + Base64.getEncoder().encodeToString(library.getChecksum()));
                        this.logger.warn(" Actual :  " + Base64.getEncoder().encodeToString(checksum));
                        continue;
                     }
                  }

                  Files.write(out, bytes);
                  Files.move(out, file);
                  if (library.hasRelocations()) {
                     file = this.relocate(file, library.getRelocatedPath(), library.getRelocations());
                  }

                  return file;
               }
            }
         } catch (IOException e) {
            throw new UncheckedIOException(e);
         } finally {
            try {
               Files.deleteIfExists(out);
            } catch (IOException var20) {
            }
         }

         throw new RuntimeException("Failed to download library '" + library + "'");
      } else if (!library.isSnapshot()) {
         if (library.hasRelocations()) {
            file = this.relocate(file, library.getRelocatedPath(), library.getRelocations());
         }

         return file;
      } else {
         try {
            Files.delete(file);
            break label127;
         } catch (IOException e) {
            throw new UncheckedIOException(e);
         }
      }
   }

   @NotNull
   public Path relocate(@NotNull Path in, @NotNull String out, @NotNull Collection<Relocation> relocations) {
      Objects.requireNonNull(in, "in");
      Objects.requireNonNull(out, "out");
      Objects.requireNonNull(relocations, "relocations");
      Path file = this.saveDirectory.resolve(out);
      if (Files.exists(file)) {
         return file;
      }

      Path tmpOut = file.resolveSibling(file.getFileName() + ".tmp");
      tmpOut.toFile().deleteOnExit();
      synchronized (this) {
         if (this.relocator == null) {
            this.relocator = new RelocationHelper(this);
         }
      }

      try {
         this.relocator.relocate(in, tmpOut, relocations);
         Files.move(tmpOut, file);
         this.logger.info("Relocations applied to " + in.getFileName());
         return file;
      } catch (IOException e) {
         throw new UncheckedIOException(e);
      } finally {
         try {
            Files.deleteIfExists(tmpOut);
         } catch (IOException var15) {
         }
      }
   }

   protected void resolveTransitiveLibraries(@NotNull Library library) {
      Objects.requireNonNull(library, "library");
      synchronized (this) {
         if (this.transitiveDependencyHelper == null) {
            this.transitiveDependencyHelper = new TransitiveDependencyHelper(this, this.saveDirectory);
         }
      }

      for (Library transitiveLibrary : this.transitiveDependencyHelper.findTransitiveLibraries(library)) {
         this.loadLibrary(transitiveLibrary);
      }
   }

   public void loadLibrary(@NotNull Library library) {
      this.logger.info("Loading library " + library);
      Path file = this.downloadLibrary(Objects.requireNonNull(library, "library"));
      if (library.resolveTransitiveDependencies()) {
         this.resolveTransitiveLibraries(library);
      }

      if (library.isIsolatedLoad()) {
         this.addToIsolatedClasspath(library, file);
      } else {
         this.addToClasspath(file);
      }
   }

   public void loadLibraries(@NotNull Library... libraries) {
      for (Library library : libraries) {
         this.loadLibrary(library);
      }
   }

   public void configureFromJSON() {
      this.configureFromJSON("libby.json");
   }

   public void configureFromJSON(@NotNull String fileName) {
      this.configureFromJSON(Objects.requireNonNull(this.getResourceAsStream(fileName), "resourceAsStream"));
   }

   public void configureFromJSON(@NotNull InputStream data) {
      synchronized (this) {
         if (this.configurationFetcher == null) {
            this.configurationFetcher = new ConfigurationFetcher(this);
         }
      }

      Configuration config = this.configurationFetcher.readJsonFile(data);

      for (String repo : config.getRepositories()) {
         this.addRepository(repo);
      }

      for (Library library : config.getLibraries()) {
         this.loadLibrary(library);
      }
   }

   @Nullable
   protected InputStream getResourceAsStream(@NotNull String path) {
      return this.getClass().getClassLoader().getResourceAsStream(path);
   }
}
