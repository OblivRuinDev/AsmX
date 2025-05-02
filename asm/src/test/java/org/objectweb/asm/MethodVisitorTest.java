// ---------------------------------------------------------------------
// ORIGINAL WORK:
// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom (https://asm.ow2.io/)
// All rights reserved.
//
// Distributed under the BSD-3-Clause License
// ---------------------------------------------------------------------

// ---------------------------------------------------------------------
// MODIFIED WORK:
// ASMX: Extended bytecode manipulation toolkit based on ASM
// Copyright (c) 2025 OblivRuinDev
// Modifications: See git commits for details
//
// Distributed under the BSD-3-Clause License, preserving original terms
// for ASM code.
// ---------------------------------------------------------------------

// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holders nor the names of its
//    contributors may be used to endorse or promote products derived from
//    this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
// THE POSSIBILITY OF SUCH DAMAGE.
package org.objectweb.asm;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringWriter;

import dev.oblivruin.asm.ClassVersionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.objectweb.asm.test.AsmTest;

/**
 * Unit tests for {@link MethodVisitor}.
 *
 * @author Eric Bruneton
 * @author OblivRuinDev
 */
class MethodVisitorTest extends AsmTest {

  @Test
  void testConstructor_validApi() {
    Executable constructor = () -> new MethodVisitor(Opcodes.V11) {};

    assertDoesNotThrow(constructor);
  }

  @Test
  void testConstructor_invalidApi() {
    Executable constructor = () -> new MethodVisitor(-1) {};

    Exception exception = assertThrows(ClassVersionException.class, constructor);
  }

  @Test
  void testGetDelegate() {
    IMethodVisitor delegate = new MethodVisitor() {};
    DelegateVisitorTest.testGetDelegate(delegate, new MethodVisitor(delegate) {});
  }

  @Test
  void testVisitParameter_V1_7() {
    IMethodVisitor methodVisitor = new MethodVisitor(Opcodes.V1_7) {};

    Executable visitParameter = () -> methodVisitor.visitParameter(null, 0);

      assertThrows(ClassVersionException.class, visitParameter);
  }

  @Test
  void testVisitTypeAnnotation_V1_7() {
    IMethodVisitor methodVisitor = new MethodVisitor(Opcodes.V1_7, null) {};

    Executable visitTypeAnnotation = () -> methodVisitor.visitTypeAnnotation(0, null, null, false);

      assertThrows(ClassVersionException.class, visitTypeAnnotation);
  }

  @Test
  void testVisitInvokeDynamicInsn_V1_6() {
    IMethodVisitor methodVisitor = new MethodVisitor(Opcodes.V1_6, null) {};

    Executable visitInvokeDynamicInsn =
        () -> methodVisitor.visitInvokeDynamicInsn(null, null, null);

      assertThrows(ClassVersionException.class, visitInvokeDynamicInsn);
      //assertTrue(exception.getMessage().matches(UNSUPPORTED_OPERATION_MESSAGE_PATTERN));
  }

  @Test
  void testVisitInsnAnnotation_V1_7() {
    IMethodVisitor methodVisitor = new MethodVisitor(Opcodes.V1_7, null) {};

    Executable visitInsnAnnotation = () -> methodVisitor.visitInsnAnnotation(0, null, null, false);

      assertThrows(ClassVersionException.class, visitInsnAnnotation);
      //assertTrue(exception.getMessage().matches(UNSUPPORTED_OPERATION_MESSAGE_PATTERN));
  }

  @Test
  void testVisitTryCatchAnnotation_V1_7() {
    IMethodVisitor methodVisitor = new MethodVisitor(Opcodes.V1_7, null) {};

    Executable visitTryCatchAnnotation =
        () -> methodVisitor.visitTryCatchAnnotation(0, null, null, false);

      assertThrows(ClassVersionException.class, visitTryCatchAnnotation);
      //assertTrue(exception.getMessage().matches(UNSUPPORTED_OPERATION_MESSAGE_PATTERN));
  }

  @Test
  void testVisitLocalVariableAnnotation_V1_7() {
      IMethodVisitor methodVisitor = new MethodVisitor(Opcodes.V1_7, null) {};

    Executable visitLocalVariableAnnotation =
        () -> methodVisitor.visitLocalVariableAnnotation(0, null, null, null, null, null, false);

      assertThrows(ClassVersionException.class, visitLocalVariableAnnotation);
      //assertTrue(exception.getMessage().matches(UNSUPPORTED_OPERATION_MESSAGE_PATTERN));
  }

  @Test
  void testVisitFrame_consecutiveFrames_sameFrame() {
    ClassWriter classWriter = new ClassWriter(0);
    classWriter.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC, "C", null, "D", null);
    IMethodVisitor methodVisitor =
        classWriter.visitMethod(Opcodes.ACC_STATIC, "m", "()V", null, null);
    methodVisitor.visitCode();
    methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

    Executable visitFrame = () -> methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

    assertDoesNotThrow(visitFrame);
  }

  @Test
  void testVisitFrame_consecutiveFrames() {
    ClassWriter classWriter = new ClassWriter(0);
    classWriter.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC, "C", null, "D", null);
    IMethodVisitor methodVisitor =
        classWriter.visitMethod(Opcodes.ACC_STATIC, "m", "()V", null, null);
    methodVisitor.visitCode();
    methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

    Executable visitFrame =
        () ->
            methodVisitor.visitFrame(Opcodes.F_APPEND, 1, new Object[] {Opcodes.INTEGER}, 0, null);

    assertThrows(IllegalStateException.class, visitFrame);
  }

  @Test
  void testVisitFrame_compressedFrame_V1_5() {
    ClassWriter classWriter = new ClassWriter(0);
    classWriter.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC, "C", null, "D", null);
    IMethodVisitor methodVisitor =
        new ClassWriter(0).visitMethod(Opcodes.ACC_STATIC, "m", "()V", null, null);
    methodVisitor.visitCode();

    Executable visitFrame = () -> methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

    Exception exception = assertThrows(IllegalArgumentException.class, visitFrame);
    assertTrue(exception.getMessage().contains("versions V1_5 or less must use F_NEW frames."));
  }

  /** Tests the ASM5 visitMethodInsn on an ASM4 visitor, with isInterface set to false. */
  @Test
  void testVisitMethodInsn_notInterface_V1_7() {
    StringWriter log = new StringWriter();
    LogMethodVisitor logMethodVisitor = new LogMethodVisitor(log);
    MethodVisitor methodVisitor = new MethodVisitor(Opcodes.V1_7, logMethodVisitor) {};

    methodVisitor.visitMethodInsn(0, "C", "m", "()V", false);

    assertEquals("LogMethodVisitor:m()V;", log.toString());
  }

  /** Tests the ASM5 visitMethodInsn on an ASM4 visitor, with isInterface set to true. */
  @Test
  void testVisitMethodInsn_isInterface_V1_7() {
    StringWriter log = new StringWriter();
    LogMethodVisitor logMethodVisitor = new LogMethodVisitor(log);
    MethodVisitor methodVisitor = new MethodVisitor(Opcodes.V1_7, logMethodVisitor) {};

    Executable visitMethodInsn = () -> methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "C", "m", "()V", true);

      assertThrows(ClassVersionException.class, visitMethodInsn);
  }

  /**
   * Tests the ASM5 visitMethodInsn on an ASM4 visitor which overrides the ASM4 method, with
   * isInterface set to false.
   */
  @Test
  void testVisitMethodInsn_overridenAsm4Visitor_isNotInterface() {
    StringWriter log = new StringWriter();
    LogMethodVisitor logMethodVisitor = new LogMethodVisitor(log);
    MethodVisitor methodVisitor = new MethodVisitor4Override(logMethodVisitor, log);

    methodVisitor.visitMethodInsn(0, "C", "m", "()V", false);

    assertEquals("LogMethodVisitor:m4()V;MethodVisitor4:m()V;", log.toString());
  }

  /**
   * Tests the ASM5 visitMethodInsn on an ASM4 visitor which overrides the ASM4 method, with
   * isInterface set to true.
   */
  @Test
  void testVisitMethodInsn_overridenAsm4Visitor_isInterface() {
    StringWriter log = new StringWriter();
    LogMethodVisitor logMethodVisitor = new LogMethodVisitor(log);
    MethodVisitor methodVisitor = new MethodVisitor(Opcodes.V1_7, logMethodVisitor) {};

    Executable visitMethodInsn = () -> methodVisitor.visitMethodInsn(0, "C", "m", "()V", true);

    Exception exception = assertThrows(ClassVersionException.class, visitMethodInsn);
  }

  /** Tests the ASM5 visitMethodInsn on an ASM5 visitor. */
  @Test
  void testVisitMethodInsn_asm5Visitor() {
    StringWriter log = new StringWriter();
    LogMethodVisitor logMethodVisitor = new LogMethodVisitor(log);
    MethodVisitor methodVisitor = new MethodVisitor5(logMethodVisitor);

    methodVisitor.visitMethodInsn(0, "C", "m", "()V", false);

    assertEquals("LogMethodVisitor:m()V;", log.toString());
  }

  /** Tests the ASM5 visitMethodInsn on an ASM5 visitor which overrides this method. */
  @Test
  void testVisitMethodInsn_overridenAsm5Visitor() {
    StringWriter log = new StringWriter();
    LogMethodVisitor logMethodVisitor = new LogMethodVisitor(log);
    MethodVisitor methodVisitor = new MethodVisitor5Override(logMethodVisitor, log);

    methodVisitor.visitMethodInsn(0, "C", "m", "()V", false);

    assertEquals("LogMethodVisitor:m5()V;MethodVisitor5:m()V;", log.toString());
  }

  /**
   * Tests the ASM5 visitMethodInsn on an ASM4 visitor which overrides this method, and is a
   * subclass of an ASM subclass of MethodVisitor.
   */
  @Test
  void testVisitMethodInsn_userTrace1() {
    StringWriter log = new StringWriter();
    LogMethodVisitor logMethodVisitor = new LogMethodVisitor(log);
    MethodVisitor methodVisitor = new TraceMethodVisitor(Opcodes.V1_7, logMethodVisitor, log) {
      @Override
      public void visitMethodInsn(int opcodeAndSource, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
        log.append("Trace1:").append(name).append(descriptor).append(';');
      }
    };

    methodVisitor.visitMethodInsn(0, "C", "m", "()V", false);

    assertEquals(
        "LogMethodVisitor:m()V;TraceMethodVisitor:m()V;Trace1:m()V;",
        log.toString());
  }

  /**
   * Tests the ASM5 visitMethodInsn on an ASM5 visitor which overrides this method, and is a
   * subclass of an ASM subclass of MethodVisitor.
   */
  @Test
  void testVisitMethodInsn_userTraceMethodVisitor5() {
    StringWriter log = new StringWriter();
    LogMethodVisitor logMethodVisitor = new LogMethodVisitor(log);
    MethodVisitor methodVisitor = new UserTraceMethodVisitor5(logMethodVisitor, log);

    methodVisitor.visitMethodInsn(0, "C", "m", "()V", false);

    assertEquals(
        "LogMethodVisitor:m()V;TraceMethodVisitor:m()V;UserTraceMethodVisitor5:m()V;",
        log.toString());
  }

  /**
   * Tests that method visitors with different API versions can be chained together and produce the
   * expected result, when calling the ASM5 visitMethodInsn on the first visitor.
   */
  @Test
  void testVisitMethodInsn_mixedVisitorChain() {
    StringWriter log = new StringWriter();
    LogMethodVisitor logMethodVisitor = new LogMethodVisitor(log);
    MethodVisitor methodVisitor =
        new MethodVisitor5Override(
            new MethodVisitor5(
                new MethodVisitor4Override(new MethodVisitor4(logMethodVisitor), log)),
            log);

    methodVisitor.visitMethodInsn(0, "C", "m", "()V", false);

    assertEquals(
        "LogMethodVisitor:m54()V;MethodVisitor4:m5()V;MethodVisitor5:m()V;", log.toString());
  }

  /** An ASM4 {@link MethodVisitor} which does not override the ASM4 visitMethodInsn method. */
  private static class MethodVisitor4 extends MethodVisitor {
    MethodVisitor4(final IMethodVisitor methodVisitor) {
      super(Opcodes.V1_8, methodVisitor);//todo
    }
  }

  /**
   * An ASM4 {@link MethodVisitor} which overrides the ASM4 visitMethodInsn method, by adding "v4"
   * at the end of method names and by duplicating the instruction.
   */
  private static class MethodVisitor4Override extends MethodVisitor {

    private final StringWriter log;

    MethodVisitor4Override(final IMethodVisitor methodVisitor, final StringWriter log) {
      super(Opcodes.V1_8, methodVisitor);
      this.log = log;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
      super.visitMethodInsn(opcode, owner, name + "4", descriptor, isInterface);
      log.append("MethodVisitor4:").append(name).append(descriptor).append(';');
    }
  }

  /** An ASM5 {@link MethodVisitor} which does not override the ASM5 visitMethodInsn method. */
  private static class MethodVisitor5 extends MethodVisitor {
    MethodVisitor5(final IMethodVisitor methodVisitor) {
      super(Opcodes.V11, methodVisitor);//todo
    }
  }

  /** An ASM5 {@link MethodVisitor} which overrides the ASM5 visitMethodInsn method. */
  private static class MethodVisitor5Override extends MethodVisitor {

    private final StringWriter log;

    MethodVisitor5Override(final IMethodVisitor methodVisitor, final StringWriter log) {
      super(Opcodes.V11, methodVisitor);//TODO
      this.log = log;
    }

    @Override
    public void visitMethodInsn(
        final int opcode,
        final String owner,
        final String name,
        final String descriptor,
        final boolean isInterface) {
      super.visitMethodInsn(opcode, owner, name + "5", descriptor, isInterface);
      log.append("MethodVisitor5:").append(name).append(descriptor).append(";");
    }
  }

  /**
   * An ASM-like {@link MethodVisitor} subclass, which overrides the ASM5 visitMethodInsn method,
   * but can be used with any API version.
   */
  private static class TraceMethodVisitor extends MethodVisitor {

    protected final StringWriter log;

    TraceMethodVisitor(final int api, final IMethodVisitor methodVisitor, final StringWriter log) {
      super(api, methodVisitor);
      this.log = log;
    }

    @Override
    public void visitMethodInsn(
        final int opcodeAndSource,
        final String owner,
        final String name,
        final String descriptor,
        final boolean isInterface) {
      super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);

      log.append("TraceMethodVisitor:").append(name).append(descriptor).append(";");
    }
  }

  /** A user subclass of {@link TraceMethodVisitor}, implemented for ASM5. */
  private static class UserTraceMethodVisitor5 extends TraceMethodVisitor {

    UserTraceMethodVisitor5(final IMethodVisitor methodVisitor, final StringWriter log) {
      super(Opcodes.V11, methodVisitor, log);//TODO
    }

    @Override
    public void visitMethodInsn(
        final int opcode,
        final String owner,
        final String name,
        final String descriptor,
        final boolean isInterface) {
      super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
      log.append("UserTraceMethodVisitor5:").append(name).append(descriptor).append(";");
    }
  }

  /** A {@link MethodVisitor} that logs the calls to its visitMethodInsn method. */
  private static class LogMethodVisitor extends MethodVisitor {

    private final StringWriter log;

    LogMethodVisitor(final StringWriter log) {
      super();
      this.log = log;
    }

    @Override
    public void visitMethodInsn(
        final int opcode,
        final String owner,
        final String name,
        final String descriptor,
        final boolean isInterface) {
      log.append("LogMethodVisitor:").append(name).append(descriptor).append(";");
    }
  }
}
