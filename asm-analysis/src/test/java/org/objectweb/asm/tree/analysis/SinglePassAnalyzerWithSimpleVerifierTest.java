package org.objectweb.asm.tree.analysis;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.test.AsmTest;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

class SinglePassAnalyzerWithSimpleVerifierTest extends AsmTest {

  @ParameterizedTest
  @MethodSource(ALL_CLASSES_AND_LATEST_API)
  void testAnalyze_simpleVerifier(
      final AsmTest.PrecompiledClass classParameter, final AsmTest.Api apiParameter)
      throws AnalyzerException {
    assumeFalse(hasJsrOrRetInstructions(classParameter));
    ClassNode classNode = computeFrames(classParameter);
    assumeFalse(classNode.methods.isEmpty());
    Analyzer<BasicValue> baseAnalyzer =
        new Analyzer<BasicValue>(
            new SimpleVerifier(
                Type.getObjectType(classNode.name),
                Type.getObjectType(classNode.superName),
                (classNode.access & Opcodes.ACC_INTERFACE) != 0));

    SinglePassAnalyzer frameAnalyzer =
        new SinglePassAnalyzer(
            new SimpleVerifier(
                Type.getObjectType(classNode.name),
                Type.getObjectType(classNode.superName),
                (classNode.access & Opcodes.ACC_INTERFACE) != 0));

    for (MethodNode methodNode : classNode.methods) {
      Frame<BasicValue>[] framesA = baseAnalyzer.analyze(classNode.name, methodNode);
      Frame<BasicValue>[] framesB = frameAnalyzer.analyze(classNode.name, methodNode);
      if (framesA.length != framesB.length) {
        throw new AssertionError("Analyzer returned different number of frames");
      }
      for (int i = 0; i < framesA.length; i++) {
        try {
          compareFrames(framesA[i], framesB[i], i);
        } catch (AssertionError e) {
          //          for (int j = Math.max(0, i - 10); j < Math.min(framesA.length, i + 10); j++) {
          //            System.out.println("Frame " + j + ":");
          //            System.out.println("Instruction: " + methodNode.instructions.get(j));
          //            System.out.println("A: " + framesA[j]);
          //            System.out.println("B: " + framesB[j]);
          //          }
          fail(
              "Error in "
                  + classNode.name
                  + "."
                  + methodNode.name
                  + methodNode.desc
                  + " at instruction "
                  + i,
              e);
        }
      }
    }
  }

  private void compareFrames(
      final Frame<BasicValue> frameA, final Frame<BasicValue> frameB, final int idx) {
    if (frameA == frameB) {
      return;
    }
    if (frameA == null || frameB == null) {
      throw new AssertionError("Frame is null at " + idx + " \n" + frameA + "\n" + frameB);
    }
    // check contents
    int localsA = frameA.getLocals();
    int stackA = frameA.getStackSize();
    int localsB = frameB.getLocals();
    int stackB = frameB.getStackSize();
    if (localsA != localsB) {
      throw new AssertionError(
          "Different number of locals at " + idx + " \n" + frameA + "\n" + frameB);
    }
    if (stackA != stackB) {
      throw new AssertionError(
          "Different number of stack elements at " + idx + " \n" + frameA + "\n" + frameB);
    }
    for (int i = 0; i < localsA; i++) {
      if (!frameA.getLocal(i).equals(frameB.getLocal(i))) {
        throw new AssertionError(
            "Different local at " + idx + "  at index " + i + "\n" + frameA + "\n" + frameB);
      }
    }
    for (int i = 0; i < stackA; i++) {
      if (!frameA.getStack(i).equals(frameB.getStack(i))) {
        throw new AssertionError(
            "Different stack element at "
                + idx
                + "  at index "
                + i
                + "\n"
                + frameA
                + "\n"
                + frameB);
      }
    }
  }

  private ClassNode computeFrames(final PrecompiledClass classParameter) {
    byte[] classFile = classParameter.getBytes();
    ClassReader classReader = new ClassReader(classFile);
    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    classReader.accept(classWriter, 0);
    classFile = classWriter.toByteArray();
    ClassNode classNode = new ClassNode();
    new ClassReader(classFile).accept(classNode, 0);
    return classNode;
  }

  private boolean hasJsrOrRetInstructions(final PrecompiledClass classParameter) {
    return classParameter == PrecompiledClass.JDK3_ALL_INSTRUCTIONS
        || classParameter == PrecompiledClass.JDK3_LARGE_METHOD;
  }
}
