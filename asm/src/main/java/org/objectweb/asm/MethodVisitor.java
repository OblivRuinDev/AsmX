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

import dev.oblivruin.asm.VersionChecker;

import static org.objectweb.asm.Opcodes.*;

/**
 * A visitor to visit a Java method. The methods of this class must be called in the following
 * order: ( {@code visitParameter} )* [ {@code visitAnnotationDefault} ] ( {@code visitAnnotation} |
 * {@code visitAnnotableParameterCount} | {@code visitParameterAnnotation} | {@code
 * visitTypeAnnotation} | {@code visitAttribute} )* [ {@code visitCode} ( {@code visitFrame} |
 * {@code visit<i>X</i>Insn} | {@code visitLabel} | {@code visitInsnAnnotation} | {@code
 * visitTryCatchBlock} | {@code visitTryCatchAnnotation} | {@code visitLocalVariable} | {@code
 * visitLocalVariableAnnotation} | {@code visitLineNumber} | {@code visitAttribute} )* {@code
 * visitMaxs} ] {@code visitEnd}. In addition, the {@code visit<i>X</i>Insn} and {@code visitLabel}
 * methods must be called in the sequential order of the bytecode instructions of the visited code,
 * {@code visitInsnAnnotation} must be called <i>after</i> the annotated instruction, {@code
 * visitTryCatchBlock} must be called <i>before</i> the labels passed as arguments have been
 * visited, {@code visitTryCatchBlockAnnotation} must be called <i>after</i> the corresponding try
 * catch block has been visited, and the {@code visitLocalVariable}, {@code
 * visitLocalVariableAnnotation} and {@code visitLineNumber} methods must be called <i>after</i> the
 * labels passed as arguments have been visited. Finally, the {@code visitAttribute} method must be
 * called before {@code visitCode} for non-code attributes, and after it for code attributes.
 *
 * @author Eric Bruneton
 * @author OblivRuinDev
 */
public abstract class MethodVisitor extends DelegateVisitor<IMethodVisitor> implements IMethodVisitor {
  /** @see DelegateVisitor#DelegateVisitor() */
  protected MethodVisitor() {
    super();
  }

  /** @see DelegateVisitor#DelegateVisitor(int) */
  protected MethodVisitor(final int ver) {
    super(ver);
  }

  /** @see DelegateVisitor#DelegateVisitor(int, IVisitor) */
  protected MethodVisitor(final int ver, final IMethodVisitor methodVisitor) {
    super(ver, methodVisitor);
  }

  /** @see DelegateVisitor#DelegateVisitor(IVisitor) */
  protected MethodVisitor(IMethodVisitor visitor) {
    super(visitor);
  }

  @Override
  public void visitParameter(final String name, final int access) {
    VersionChecker.methodPara(ver);
    if (parent != null) {
      parent.visitParameter(name, access);
    }
  }

  @Override
  public IAnnotationVisitor visitAnnotationDefault() {
    if (parent != null) {
      return parent.visitAnnotationDefault();
    }
    return null;
  }

  @Override
  public IAnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
    if (!visible) {
      VersionChecker.invisAnn(ver);
    }
    if (parent != null) {
      return parent.visitAnnotation(descriptor, visible);
    }
    return null;
  }

  @Override
  public IAnnotationVisitor visitTypeAnnotation(
          final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
    VersionChecker.typeAnn(ver);
    if (parent != null) {
      return parent.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }
    return null;
  }

  @Override
  public void visitAnnotableParameterCount(final int parameterCount, final boolean visible) {
    if (parent != null) {
      parent.visitAnnotableParameterCount(parameterCount, visible);
    }
  }

  @Override
  public IAnnotationVisitor visitParameterAnnotation(
          final int parameter, final String descriptor, final boolean visible) {
    if (parent != null) {
      return parent.visitParameterAnnotation(parameter, descriptor, visible);
    }
    return null;
  }

  @Override
  public void visitAttribute(final Attribute attribute) {
    if (parent != null) {
      parent.visitAttribute(attribute);
    }
  }

  @Override
  public void visitCode() {
    if (parent != null) {
      parent.visitCode();
    }
  }

  @Override
  public void visitFrame(
          final int type,
          final int numLocal,
          final Object[] local,
          final int numStack,
          final Object[] stack) {
    if (parent != null) {
      parent.visitFrame(type, numLocal, local, numStack, stack);
    }
  }

  @Override
  public void visitInsn(final int opcode) {
    if (parent != null) {
      parent.visitInsn(opcode);
    }
  }

  @Override
  public void visitIntInsn(final int opcode, final int operand) {
    if (parent != null) {
      parent.visitIntInsn(opcode, operand);
    }
  }

  @Override
  public void visitVarInsn(final int opcode, final int varIndex) {
    if (parent != null) {
      parent.visitVarInsn(opcode, varIndex);
    }
  }

  @Override
  public void visitTypeInsn(final int opcode, final String type) {
    if (parent != null) {
      parent.visitTypeInsn(opcode, type);
    }
  }

  @Override
  public void visitFieldInsn(
          final int opcode, final String owner, final String name, final String descriptor) {
    if (parent != null) {
      parent.visitFieldInsn(opcode, owner, name, descriptor);
    }
  }

  @Override
  public void visitMethodInsn(
          final int opcode,
          final String owner,
          final String name,
          final String descriptor,
          final boolean isInterface) {
    if (isInterface && (opcode != INVOKEINTERFACE)) {
      VersionChecker.invokeInterface(ver);
    }
    if (parent != null) {
      parent.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }
  }

  @Override
  public void visitInvokeDynamicInsn(
          final String name,
          final String descriptor,
          final Handle bootstrapMethodHandle,
          final Object... bootstrapMethodArguments) {
    VersionChecker.invokeDyna(ver);
    if (parent != null) {
      parent.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }
  }

  @Override
  public void visitJumpInsn(final int opcode, final Label label) {
    if (parent != null) {
      parent.visitJumpInsn(opcode, label);
    }
  }

  @Override
  public void visitLabel(final Label label) {
    if (parent != null) {
      parent.visitLabel(label);
    }
  }

  @Override
  public void visitLdcInsn(final Object value) {
    if (value instanceof Handle
            || (value instanceof Type && ((Type) value).getSort() == Type.METHOD)) {
      VersionChecker.LDC(ver);
    } else if (value instanceof ConstantDynamic) {
      VersionChecker.constDyna(ver);
    }
    if (parent != null) {
      parent.visitLdcInsn(value);
    }
  }

  @Override
  public void visitIincInsn(final int varIndex, final int increment) {
    if (parent != null) {
      parent.visitIincInsn(varIndex, increment);
    }
  }

  @Override
  public void visitTableSwitchInsn(
          final int min, final int max, final Label dflt, final Label... labels) {
    if (parent != null) {
      parent.visitTableSwitchInsn(min, max, dflt, labels);
    }
  }

  @Override
  public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
    if (parent != null) {
      parent.visitLookupSwitchInsn(dflt, keys, labels);
    }
  }

  @Override
  public void visitMultiANewArrayInsn(final String descriptor, final int numDimensions) {
    if (parent != null) {
      parent.visitMultiANewArrayInsn(descriptor, numDimensions);
    }
  }

  @Override
  public IAnnotationVisitor visitInsnAnnotation(
          final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
    VersionChecker.insnTypeAnn(ver);
    if (parent != null) {
      return parent.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
    }
    return null;
  }

  @Override
  public void visitTryCatchBlock(
          final Label start, final Label end, final Label handler, final String type) {
    if (parent != null) {
      parent.visitTryCatchBlock(start, end, handler, type);
    }
  }

  @Override
  public IAnnotationVisitor visitTryCatchAnnotation(
          final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
    VersionChecker.tryTypeAnn(ver);
    if (parent != null) {
      return parent.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
    }
    return null;
  }

  @Override
  public void visitLocalVariable(
          final String name,
          final String descriptor,
          final String signature,
          final Label start,
          final Label end,
          final int index) {
    if (parent != null) {
      parent.visitLocalVariable(name, descriptor, signature, start, end, index);
    }
  }

  @Override
  public IAnnotationVisitor visitLocalVariableAnnotation(
          final int typeRef,
          final TypePath typePath,
          final Label[] start,
          final Label[] end,
          final int[] index,
          final String descriptor,
          final boolean visible) {
    VersionChecker.localVarAnn(ver);
    if (parent != null) {
      return parent.visitLocalVariableAnnotation(
          typeRef, typePath, start, end, index, descriptor, visible);
    }
    return null;
  }

  @Override
  public void visitLineNumber(final int line, final Label start) {
    if (parent != null) {
      parent.visitLineNumber(line, start);
    }
  }

  @Override
  public void visitMaxs(final int maxStack, final int maxLocals) {
    if (parent != null) {
      parent.visitMaxs(maxStack, maxLocals);
    }
  }

  @Override
  public void visitEnd() {
    if (parent != null) {
      parent.visitEnd();
    }
  }
}