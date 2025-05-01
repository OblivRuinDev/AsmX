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

/**
 * A visitor to visit a Java field. The methods of this class must be called in the following order:
 * ( {@code visitAnnotation} | {@code visitTypeAnnotation} | {@code visitAttribute} )* {@code
 * visitEnd}.
 *
 * @author Eric Bruneton
 * @author OblivRuinDev
 */
public abstract class FieldVisitor extends DelegateVisitor<IFieldVisitor> implements IFieldVisitor {
  /** @see DelegateVisitor#DelegateVisitor() */
  protected FieldVisitor() {
    super();
  }

  /** @see DelegateVisitor#DelegateVisitor(int) */
  protected FieldVisitor(final int ver) {
    super(ver);
  }

  /** @see DelegateVisitor#DelegateVisitor(int, IVisitor) */
  protected FieldVisitor(final int ver, final IFieldVisitor fieldVisitor) {
    super(ver, fieldVisitor);
  }

  /** @see DelegateVisitor#DelegateVisitor(IVisitor) */
  protected FieldVisitor(IFieldVisitor visitor) {
    super(visitor);
  }

  /** @see DelegateVisitor#DelegateVisitor() */
  @Override
  public IAnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
    if (parent != null) {
      return parent.visitAnnotation(descriptor, visible);
    }
    return null;
  }

  /** @see DelegateVisitor#DelegateVisitor() */
  @Override
  public IAnnotationVisitor visitTypeAnnotation(
          final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
    VersionChecker.typeAnn(ver);
    if (parent != null) {
      return parent.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }
    return null;
  }

  /** @see DelegateVisitor#DelegateVisitor() */
  @Override
  public void visitAttribute(final Attribute attribute) {
    if (parent != null) {
      parent.visitAttribute(attribute);
    }
  }

  /** @see DelegateVisitor#DelegateVisitor() */
  @Override
  public void visitEnd() {
    if (parent != null) {
      parent.visitEnd();
    }
  }
}
