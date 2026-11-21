package dev.lone.itemsadder.molang;

@FunctionalInterface
interface MolangNode {
   double eval(MolangContext var1, MolangEvalState var2);
}
