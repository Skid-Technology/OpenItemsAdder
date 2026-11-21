package dev.lone.itemsadder.Core.OtherPlugins.Denizen;

import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.MaterialTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.objects.properties.PropertyParser.ClassPropertiesInfo;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.ScriptEntry.ArgumentIterator;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand.TabCompletionsBuilder;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import dev.lone.itemsadder.block.CustomBlockDefinition;
import dev.lone.itemsadder.core.ContentManager;
import java.util.List;
import org.bukkit.block.Block;

public class SetCustomBlock extends AbstractCommand {
   public SetCustomBlock() {
      this.setName("set_custom_block");
      this.setSyntax("set_custom_block [<location>|...] [<namespaced_id>]");
      this.setRequiredArguments(1, 2);
      this.isProcedural = false;
      this.allowedDynamicPrefixes = true;
   }

   public void addCustomTabCompletions(TabCompletionsBuilder tab) {
      tab.add(((ClassPropertiesInfo)PropertyParser.propertiesByClass.get(MaterialTag.class)).propertiesByMechanism.keySet());
   }

   public void parseArgs(ScriptEntry scriptEntry) {
      ArgumentIterator var2 = scriptEntry.iterator();

      while (var2.hasNext()) {
         Argument var3 = (Argument)var2.next();
         if (!scriptEntry.hasObject("locations") && var3.matchesArgumentList(LocationTag.class)) {
            scriptEntry.addObject("locations", ((ListTag)var3.asType(ListTag.class)).filter(LocationTag.class, scriptEntry));
         } else if (!scriptEntry.hasObject("namespaced_id")) {
            scriptEntry.addObject("namespaced_id", new ElementTag(var3.getRawValue()));
         } else {
            var3.reportUnhandled();
         }
      }

      if (!scriptEntry.hasObject("locations")) {
         throw new InvalidArgumentsException("You must specify a location!");
      }

      if (!scriptEntry.hasObject("namespaced_id")) {
         throw new InvalidArgumentsException("You must specify a namespaced_id!");
      }
   }

   public void execute(ScriptEntry scriptEntry) {
      ElementTag var2 = scriptEntry.getElement("namespaced_id");
      List var3 = (List)scriptEntry.getObject("locations");
      if (scriptEntry.dbCallShouldDebug()) {
         Debug.report(scriptEntry, this.getName(), new Object[]{db("locations", var3), var2});
      }

      CustomBlockDefinition var4 = ContentManager.method8084().method8160(var2.asString());
      if (var4 == null) {
         throw new InvalidArgumentsRuntimeException("Not a custom ItemAdder block: " + var2.asString());
      }

      for (LocationTag var6 : var3) {
         Block var7 = var6.getBlock();
         var4.placeBlockNoPhysics(var7);
      }
   }
}
