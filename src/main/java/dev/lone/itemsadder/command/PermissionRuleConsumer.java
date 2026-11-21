package dev.lone.itemsadder.command;

@FunctionalInterface
public interface PermissionRuleConsumer {
   PermissionRuleOutcome accept(AbstractCommandPermissionRule var1);
}
