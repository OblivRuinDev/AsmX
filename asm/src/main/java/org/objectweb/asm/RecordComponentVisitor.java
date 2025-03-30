// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom
// All rights reserved.
//
// Modifications (c) 2025 OblivRuinDev
//
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

/**
 * A visitor to visit a record component. The methods of this class must be called in the following
 * order: ( {@code visitAnnotation} | {@code visitTypeAnnotation} | {@code visitAttribute} )* {@code
 * visitEnd}.
 *
 * @author Remi Forax
 * @author Eric Bruneton
 * @author OblivRuinDev
 */
public abstract class RecordComponentVisitor extends DelegateVisitor<IRecordComponentVisitor> implements IRecordComponentVisitor {

  protected RecordComponentVisitor() {
    super();
  }

  /**
   * Constructs a new {@link RecordComponentVisitor}.
   *
   * @param ver the ASM API version implemented by this visitor. Must be one of {@link Opcodes#ASM8}
   *     or {@link Opcodes#ASM9}.
   */
  protected RecordComponentVisitor(final int ver) {
    super(ver);
  }

  /**
   * Constructs a new {@link RecordComponentVisitor}.
   *
   * @param ver the ASM API version implemented by this visitor. Must be {@link Opcodes#ASM8}.
   * @param recordComponentVisitor the record component visitor to which this visitor must delegate
   *     method calls. May be null.
   */
  protected RecordComponentVisitor(
          final int ver, final IRecordComponentVisitor recordComponentVisitor) {
    super(ver, recordComponentVisitor);
  }

  protected RecordComponentVisitor(IRecordComponentVisitor visitor) {
    super(visitor);
  }

  @Override
  public IAnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
    if (parent != null) {
      return parent.visitAnnotation(descriptor, visible);
    }
    return null;
  }

  @Override
  public IAnnotationVisitor visitTypeAnnotation(
          final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
    if (parent != null) {
      return parent.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
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
  public void visitEnd() {
    if (parent != null) {
      parent.visitEnd();
    }
  }
}
