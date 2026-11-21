package ia.sh.libby.transitive;

import ia.sh.libby.Library;
import ia.sh.libby.Library$Builder;
import ia.sh.libby.LibraryManager;
import ia.sh.libby.Util;
import ia.sh.libby.classloader.IsolatedClassLoader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map$Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public class TransitiveDependencyHelper {
   private static final String TRANSITIVE_DEPENDENCY_COLLECTOR_CLASS = Util.replaceWithDots(
      "com{}alessiodp{}libby{}maven{}resolver{}TransitiveDependencyCollector"
   );
   private static final String ARTIFACT_CLASS = Util.replaceWithDots("org{}eclipse{}aether{}artifact{}Artifact");
   private final Object transitiveDependencyCollectorObject;
   private final Method resolveTransitiveDependenciesMethod;
   private final Method artifactGetGroupIdMethod;
   private final Method artifactGetArtifactIdMethod;
   private final Method artifactGetVersionMethod;
   private final Method artifactGetBaseVersionMethod;
   private final Method artifactGetClassifierMethod;
   private final LibraryManager libraryManager;

   public TransitiveDependencyHelper(@NotNull LibraryManager libraryManager, @NotNull Path saveDirectory) {
      Objects.requireNonNull(libraryManager, "libraryManager");
      this.libraryManager = libraryManager;
      IsolatedClassLoader classLoader = new IsolatedClassLoader();
      classLoader.addPath(
         libraryManager.downloadLibrary(
            Library.builder()
               .groupId("com{}alessiodp{}libby{}maven{}resolver")
               .artifactId("libby-maven-resolver")
               .version("1.0.1")
               .checksumFromBase64("EmsSUwjtqSeYTt8WEw7LPI/5Yz8bWSxf23XcdLEM7dk=")
               .fallbackRepository("https://repo1.maven.org/maven2/")
               .fallbackRepository("https://repo.alessiodp.com/releases")
               .build()
         )
      );

      try {
         Class<?> transitiveDependencyCollectorClass = classLoader.loadClass(TRANSITIVE_DEPENDENCY_COLLECTOR_CLASS);
         Class<?> artifactClass = classLoader.loadClass(ARTIFACT_CLASS);
         Constructor<?> constructor = transitiveDependencyCollectorClass.getConstructor(Path.class);
         constructor.setAccessible(true);
         this.transitiveDependencyCollectorObject = constructor.newInstance(saveDirectory);
         this.resolveTransitiveDependenciesMethod = transitiveDependencyCollectorClass.getMethod(
            "findTransitiveDependencies", String.class, String.class, String.class, String.class, Stream.class
         );
         this.resolveTransitiveDependenciesMethod.setAccessible(true);
         this.artifactGetGroupIdMethod = artifactClass.getMethod("getGroupId");
         this.artifactGetArtifactIdMethod = artifactClass.getMethod("getArtifactId");
         this.artifactGetVersionMethod = artifactClass.getMethod("getVersion");
         this.artifactGetBaseVersionMethod = artifactClass.getMethod("getBaseVersion");
         this.artifactGetClassifierMethod = artifactClass.getMethod("getClassifier");
      } catch (ReflectiveOperationException e) {
         throw new RuntimeException(e);
      }
   }

   @NotNull
   public Collection<Library> findTransitiveLibraries(@NotNull Library library) {
      List<Library> transitiveLibraries = new ArrayList<>();
      Set<ExcludedDependency> excludedDependencies = new HashSet<>(library.getExcludedTransitiveDependencies());
      Collection<String> globalRepositories = this.libraryManager.getRepositories();
      Collection<String> libraryRepositories = library.getRepositories();
      Collection<String> libraryFallbackRepositories = library.getFallbackRepositories();
      if (globalRepositories.isEmpty() && libraryRepositories.isEmpty() && libraryFallbackRepositories.isEmpty()) {
         throw new IllegalArgumentException("No repositories have been added before resolving transitive dependencies");
      }

      Stream<String> repositories = this.libraryManager.resolveRepositories(library).stream();

      try {
         for (Object resolved : (Collection)this.resolveTransitiveDependenciesMethod
            .invoke(
               this.transitiveDependencyCollectorObject,
               library.getGroupId(),
               library.getArtifactId(),
               library.getVersion(),
               library.getClassifier(),
               repositories
            )) {
            Map$Entry<?, ?> resolvedEntry = (Map$Entry<?, ?>)resolved;
            Object artifact = resolvedEntry.getKey();
            String repository = (String)resolvedEntry.getValue();
            String groupId = (String)this.artifactGetGroupIdMethod.invoke(artifact);
            String artifactId = (String)this.artifactGetArtifactIdMethod.invoke(artifact);
            String baseVersion = (String)this.artifactGetBaseVersionMethod.invoke(artifact);
            String classifier = (String)this.artifactGetClassifierMethod.invoke(artifact);
            if ((!library.getGroupId().equals(groupId) || !library.getArtifactId().equals(artifactId))
               && !excludedDependencies.contains(new ExcludedDependency(groupId, artifactId))) {
               Library$Builder libraryBuilder = Library.builder()
                  .groupId(groupId)
                  .artifactId(artifactId)
                  .version(baseVersion)
                  .isolatedLoad(library.isIsolatedLoad())
                  .loaderId(library.getLoaderId());
               if (classifier != null && !classifier.isEmpty()) {
                  libraryBuilder.classifier(classifier);
               }

               library.getRelocations().forEach(libraryBuilder::relocate);
               if (repository != null) {
                  if (!repository.endsWith("/")) {
                     repository = repository + '/';
                  }

                  String version = (String)this.artifactGetVersionMethod.invoke(artifact);
                  String partialPath = Util.craftPartialPath(artifactId, groupId, baseVersion);
                  String path = Util.craftPath(partialPath, artifactId, version, classifier);
                  libraryBuilder.url(repository + path);
               } else {
                  library.getRepositories().forEach(libraryBuilder::repository);
                  library.getFallbackRepositories().forEach(libraryBuilder::fallbackRepository);
               }

               transitiveLibraries.add(libraryBuilder.build());
            }
         }
      } catch (ReflectiveOperationException e) {
         throw new RuntimeException(e);
      }

      return Collections.unmodifiableCollection(transitiveLibraries);
   }
}
