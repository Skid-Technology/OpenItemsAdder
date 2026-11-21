package ia.sh.libby.transitive;

import ia.sh.libby.Util;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class ExcludedDependency {
   @NotNull
   private final String groupId;
   @NotNull
   private final String artifactId;

   public ExcludedDependency(@NotNull String groupId, @NotNull String artifactId) {
      this.groupId = Util.replaceWithDots(Objects.requireNonNull(groupId, "groupId"));
      this.artifactId = Util.replaceWithDots(Objects.requireNonNull(artifactId, "artifactId"));
   }

   @NotNull
   public String getGroupId() {
      return this.groupId;
   }

   @NotNull
   public String getArtifactId() {
      return this.artifactId;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ExcludedDependency that = (ExcludedDependency)o;
         return !this.groupId.equals(that.groupId) ? false : this.artifactId.equals(that.artifactId);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.groupId.hashCode();
      return 31 * result + this.artifactId.hashCode();
   }
}
