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

package org.objectweb.asm.commons;

import org.objectweb.asm.*;

/**
 * A {@link MethodVisitor} that remaps types with a {@link Remapper}.
 *
 * @author Eugene Kuleshov
 * @author OblivRuinDev
 */
public class MethodRemapper extends MethodVisitor {

  /** The remapper used to remap the types in the visited field. */
  protected final Remapper remapper;

  /**
   * Constructs a new {@link MethodRemapper}. <i>Subclasses must not use this constructor</i>.
   * Instead, they must use the {@link #MethodRemapper(int, IMethodVisitor,Remapper)} version.
   *
   * @param methodVisitor the method visitor this remapper must delegate to.
   * @param remapper the remapper to use to remap the types in the visited method.
   */
  public MethodRemapper(final IMethodVisitor methodVisitor, final Remapper remapper) {
    super(methodVisitor);
    this.remapper = remapper;
  }

  /**
   * Constructs a new {@link MethodRemapper}.
   *
   * @param api the ASM API version supported by this remapper. Must be one of the {@code
   *     ASM}<i>x</i> values in {@link Opcodes}.
   * @param methodVisitor the method visitor this remapper must delegate to.
   * @param remapper the remapper to use to remap the types in the visited method.
   */
  protected MethodRemapper(
          final int api, final IMethodVisitor methodVisitor, final Remapper remapper) {
    super(api, methodVisitor);
    this.remapper = remapper;
  }

  @Override
  public IAnnotationVisitor visitAnnotationDefault() {
    IAnnotationVisitor annotationVisitor = super.visitAnnotationDefault();
    return annotationVisitor == null
        ? null
        : createAnnotationRemapper(/* descriptor= */ null, annotationVisitor);
  }

  @Override
  public IAnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
    IAnnotationVisitor annotationVisitor =
        super.visitAnnotation(remapper.mapDesc(descriptor), visible);
    return annotationVisitor == null
        ? null
        : createAnnotationRemapper(descriptor, annotationVisitor);
  }

  @Override
  public IAnnotationVisitor visitTypeAnnotation(
      final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
    IAnnotationVisitor annotationVisitor =
        super.visitTypeAnnotation(typeRef, typePath, remapper.mapDesc(descriptor), visible);
    return annotationVisitor == null
        ? null
        : createAnnotationRemapper(descriptor, annotationVisitor);
  }

  @Override
  public IAnnotationVisitor visitParameterAnnotation(
      final int parameter, final String descriptor, final boolean visible) {
    IAnnotationVisitor annotationVisitor =
        super.visitParameterAnnotation(parameter, remapper.mapDesc(descriptor), visible);
    return annotationVisitor == null
        ? null
        : createAnnotationRemapper(descriptor, annotationVisitor);
  }

  @Override
  public void visitFrame(
      final int type,
      final int numLocal,
      final Object[] local,
      final int numStack,
      final Object[] stack) {
    super.visitFrame(
        type,
        numLocal,
        remapFrameTypes(numLocal, local),
        numStack,
        remapFrameTypes(numStack, stack));
  }

  private Object[] remapFrameTypes(final int numTypes, final Object[] frameTypes) {
    if (frameTypes == null) {
      return null;
    }
    Object[] remappedFrameTypes = null;
    for (int i = 0; i < numTypes; ++i) {
      if (frameTypes[i] instanceof String) {
        if (remappedFrameTypes == null) {
          remappedFrameTypes = new Object[numTypes];
          System.arraycopy(frameTypes, 0, remappedFrameTypes, 0, numTypes);
        }
        remappedFrameTypes[i] = remapper.mapType((String) frameTypes[i]);
      }
    }
    return remappedFrameTypes == null ? frameTypes : remappedFrameTypes;
  }

  @Override
  public void visitFieldInsn(
      final int opcode, final String owner, final String name, final String descriptor) {
    super.visitFieldInsn(
        opcode,
        remapper.mapType(owner),
        remapper.mapFieldName(owner, name, descriptor),
        remapper.mapDesc(descriptor));
  }

  @Override
  public void visitMethodInsn(
      final int opcodeAndSource,
      final String owner,
      final String name,
      final String descriptor,
      final boolean isInterface) {
    super.visitMethodInsn(
        opcodeAndSource,
        remapper.mapType(owner),
        remapper.mapMethodName(owner, name, descriptor),
        remapper.mapMethodDesc(descriptor),
        isInterface);
  }

  @Override
  public void visitInvokeDynamicInsn(
      final String name,
      final String descriptor,
      final Handle bootstrapMethodHandle,
      final Object... bootstrapMethodArguments) {
    Object[] remappedBootstrapMethodArguments = new Object[bootstrapMethodArguments.length];
    for (int i = 0; i < bootstrapMethodArguments.length; ++i) {
      remappedBootstrapMethodArguments[i] = remapper.mapValue(bootstrapMethodArguments[i]);
    }
    super.visitInvokeDynamicInsn(
        remapper.mapInvokeDynamicMethodName(name, descriptor),
        remapper.mapMethodDesc(descriptor),
        (Handle) remapper.mapValue(bootstrapMethodHandle),
        remappedBootstrapMethodArguments);
  }

  @Override
  public void visitTypeInsn(final int opcode, final String type) {
    super.visitTypeInsn(opcode, remapper.mapType(type));
  }

  @Override
  public void visitLdcInsn(final Object value) {
    super.visitLdcInsn(remapper.mapValue(value));
  }

  @Override
  public void visitMultiANewArrayInsn(final String descriptor, final int numDimensions) {
    super.visitMultiANewArrayInsn(remapper.mapDesc(descriptor), numDimensions);
  }

  @Override
  public IAnnotationVisitor visitInsnAnnotation(
      final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
    IAnnotationVisitor annotationVisitor =
        super.visitInsnAnnotation(typeRef, typePath, remapper.mapDesc(descriptor), visible);
    return annotationVisitor == null
        ? null
        : createAnnotationRemapper(descriptor, annotationVisitor);
  }

  @Override
  public void visitTryCatchBlock(
      final Label start, final Label end, final Label handler, final String type) {
    super.visitTryCatchBlock(start, end, handler, type == null ? null : remapper.mapType(type));
  }

  @Override
  public IAnnotationVisitor visitTryCatchAnnotation(
      final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
    IAnnotationVisitor annotationVisitor =
        super.visitTryCatchAnnotation(typeRef, typePath, remapper.mapDesc(descriptor), visible);
    return annotationVisitor == null
        ? annotationVisitor
        : createAnnotationRemapper(descriptor, annotationVisitor);
  }

  @Override
  public void visitLocalVariable(
      final String name,
      final String descriptor,
      final String signature,
      final Label start,
      final Label end,
      final int index) {
    super.visitLocalVariable(
        name,
        remapper.mapDesc(descriptor),
        remapper.mapSignature(signature, true),
        start,
        end,
        index);
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
    IAnnotationVisitor annotationVisitor =
        super.visitLocalVariableAnnotation(
            typeRef, typePath, start, end, index, remapper.mapDesc(descriptor), visible);
    return annotationVisitor == null
        ? null
        : createAnnotationRemapper(descriptor, annotationVisitor);
  }

  /**
   * Constructs a new remapper for annotations. The default implementation of this method returns a
   * new {@link AnnotationRemapper}.
   *
   * @param annotationVisitor the AnnotationVisitor the remapper must delegate to.
   * @return the newly created remapper.
   * @deprecated use {@link #createAnnotationRemapper(String, IAnnotationVisitor)} instead.
   */
  @Deprecated
  protected IAnnotationVisitor createAnnotationRemapper(final IAnnotationVisitor annotationVisitor) {
    return new AnnotationRemapper(ver, /* descriptor= */ null, annotationVisitor, remapper);
  }

  /**
   * Constructs a new remapper for annotations. The default implementation of this method returns a
   * new {@link AnnotationRemapper}.
   *
   * @param descriptor the descriptor of the visited annotation.
   * @param annotationVisitor the AnnotationVisitor the remapper must delegate to.
   * @return the newly created remapper.
   */
  protected IAnnotationVisitor createAnnotationRemapper(
      final String descriptor, final IAnnotationVisitor annotationVisitor) {
    return new AnnotationRemapper(ver, descriptor, annotationVisitor, remapper)
        .orDeprecatedValue(createAnnotationRemapper(annotationVisitor));
  }
}
