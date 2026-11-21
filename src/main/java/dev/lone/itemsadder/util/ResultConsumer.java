package dev.lone.itemsadder.util;

import dev.lone.itemsadder.item.effect.EffectOutcome;

@FunctionalInterface
public interface ResultConsumer {
   EffectOutcome accept(Object var1);
}
