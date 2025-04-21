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
// Distributed under the BSD-3-Clause License (inherits original terms)
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

/**
 * A visitor to visit a Java annotation. The methods of this class must be called in the following
 * order: ( {@code visit} | {@code visitEnum} | {@code visitAnnotation} | {@code visitArray} )*
 * {@code visitEnd}.
 *
 * @author Eric Bruneton
 * @author Eugene Kuleshov
 * @author OblivRuinDev
 */
public abstract class AnnotationVisitor extends DelegateVisitor<IAnnotationVisitor> implements IAnnotationVisitor {

  /** @see DelegateVisitor#DelegateVisitor() */
  protected AnnotationVisitor() {
    super();
  }

  /** @see DelegateVisitor#DelegateVisitor(int) */
  protected AnnotationVisitor(final int ver) {
    super(ver);
  }

  /** @see DelegateVisitor#DelegateVisitor(int, IVisitor) */
  protected AnnotationVisitor(final int ver, final IAnnotationVisitor annotationVisitor) {
    super(ver, annotationVisitor);
  }

  /** @see DelegateVisitor#DelegateVisitor(IVisitor) */
  protected AnnotationVisitor(IAnnotationVisitor visitor) {
    super(visitor);
  }

  /** {@inheritDoc} */
  @Override
  public void visit(final String name, final Object value) {
    if (parent != null) {
      parent.visit(name, value);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void visitEnum(final String name, final String descriptor, final String value) {
    if (parent != null) {
      parent.visitEnum(name, descriptor, value);
    }
  }

  /** {@inheritDoc} */
  @Override
  public IAnnotationVisitor visitAnnotation(final String name, final String descriptor) {
    if (parent != null) {
      return parent.visitAnnotation(name, descriptor);
    }
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public IAnnotationVisitor visitArray(final String name) {
    if (parent != null) {
      return parent.visitArray(name);
    }
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public void visitEnd() {
    if (parent != null) {
      parent.visitEnd();
    }
  }
}
