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
package org.objectweb.asm.tree;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

import java.util.Objects;

/**
 * A node that represents a type annotation.
 *
 * @author Eric Bruneton
 * @author OblivRuinDev
 */
public class TypeAnnotationNode extends AnnotationNode implements Cloneable {

  /** A reference to the annotated type. See {@link org.objectweb.asm.TypeReference}. */
  public int typeRef;

  /**
   * The path to the annotated type argument, wildcard bound, array element type, or static outer
   * type within the referenced type. May be {@literal null} if the annotation targets 'typeRef' as
   * a whole.
   */
  public TypePath typePath;

  /**
   * Constructs a new {@link AnnotationNode}.
   *
   * @param typeRef a reference to the annotated type. See {@link org.objectweb.asm.TypeReference}.
   * @param typePath the path to the annotated type argument, wildcard bound, array element type, or
   *     static inner type within 'typeRef'. May be {@literal null} if the annotation targets
   *     'typeRef' as a whole.
   * @param descriptor the class descriptor of the annotation class.
   * @throws IllegalStateException If a subclass calls this constructor.
   */
  public TypeAnnotationNode(final int typeRef, final TypePath typePath, final String descriptor) {
    super(descriptor);
    this.typeRef = typeRef;
    this.typePath = typePath;
  }

  /**
   * Constructs a new {@link AnnotationNode}.
   *
   * @param api the ASM API version implemented by this visitor. Must be one of the {@code
   *     ASM}<i>x</i> values in {@link Opcodes}.
   * @param typeRef a reference to the annotated type. See {@link org.objectweb.asm.TypeReference}.
   * @param typePath the path to the annotated type argument, wildcard bound, array element type, or
   *     static inner type within 'typeRef'. May be {@literal null} if the annotation targets
   *     'typeRef' as a whole.
   * @param descriptor the class descriptor of the annotation class.
   */
  @Deprecated
  public TypeAnnotationNode(
      final int api, final int typeRef, final TypePath typePath, final String descriptor) {
    this(typeRef, typePath, descriptor);
  }

  @SuppressWarnings("MethodDoesntCallSuperMethod")
  @Override
  public TypeAnnotationNode clone() {
    TypeAnnotationNode clone = new TypeAnnotationNode(typeRef, typePath, desc);
    accept(clone);
    return clone;
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this || (obj instanceof TypeAnnotationNode && equals((TypeAnnotationNode) obj));
  }

  @Override
  public boolean equals(AnnotationNode node) {
    return false;
  }

  public boolean equals(TypeAnnotationNode node) {
    return super.equals(node) && typeRef == node.typeRef && Objects.equals(typePath, node.typePath);
  }
}
