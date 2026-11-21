package dev.lone.itemsadder.item.effect;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

class AttributeModifierEntry {
   private final Attribute alr;
   private final AttributeModifier als;

   private AttributeModifierEntry(Attribute var1, AttributeModifier var2) {
      this.alr = var1;
      this.als = var2;
   }

   public Attribute attribute() {
      return this.alr;
   }

   public AttributeModifier modifier() {
      return this.als;
   }
}
