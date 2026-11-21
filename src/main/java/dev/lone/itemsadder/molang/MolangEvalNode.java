package dev.lone.itemsadder.molang;

@FunctionalInterface
interface MolangEvalNode {
   double eval(MolangContext var1, MolangEvalState var2);
}
