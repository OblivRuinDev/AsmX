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
package org.objectweb.asm.tree;

import java.util.function.Consumer;

import dev.oblivruin.asm.VersionChecker;
import org.objectweb.asm.*;

/**
 * A node that represents a field.
 *
 * @author Eric Bruneton
 * @author OblivRuinDev
 */
public class FieldNode extends SpecialNode implements IFieldVisitor, Consumer<IClassVisitor> {
  /**
   * The field's initial value. This field, which may be {@literal null} if the field does not have
   * an initial value, must be an {@link Integer}, a {@link Float}, a {@link Long}, a {@link Double}
   * or a {@link String}.
   */
  public Object value;

  /** The field's descriptor (see {@link org.objectweb.asm.Type}). */
  public String desc;

  /**
   * Constructs a new {@link FieldNode}.
   *
   * @param access the field's access flags (see {@link org.objectweb.asm.Opcodes}). This parameter
   *     also indicates if the field is synthetic and/or deprecated.
   * @param name the field's name.
   * @param descriptor the field's descriptor (see {@link org.objectweb.asm.Type}).
   * @param signature the field's signature.
   * @param value the field's initial value. This parameter, which may be {@literal null} if the
   *     field does not have an initial value, must be an {@link Integer}, a {@link Float}, a {@link
   *     Long}, a {@link Double} or a {@link String}.
   */
  public FieldNode(
      final int access,
      final String name,
      final String descriptor,
      final String signature,
      final Object value) {
    super(access, name, signature);
    this.desc = descriptor;
    this.value = value;
  }

  /**
   * Constructs a new {@link FieldNode}.
   *
   * @param api the ASM API version implemented by this visitor. Must be one of the {@code
   *     ASM}<i>x</i> values in {@link Opcodes}.
   * @param access the field's access flags (see {@link org.objectweb.asm.Opcodes}). This parameter
   *     also indicates if the field is synthetic and/or deprecated.
   * @param name the field's name.
   * @param descriptor the field's descriptor (see {@link org.objectweb.asm.Type}).
   * @param signature the field's signature.
   * @param value the field's initial value. This parameter, which may be {@literal null} if the
   *     field does not have an initial value, must be an {@link Integer}, a {@link Float}, a {@link
   *     Long}, a {@link Double} or a {@link String}.
   */
  @Deprecated(forRemoval = true)
  public FieldNode(
      final int api,
      final int access,
      final String name,
      final String descriptor,
      final String signature,
      final Object value) {
    this(access, name, descriptor, signature, value);
  }

  // -----------------------------------------------------------------------------------------------
  // Implementation of the FieldVisitor abstract class
  // -----------------------------------------------------------------------------------------------

  @Override
  public void visitEnd() {
    // Nothing to do.
  }

  // -----------------------------------------------------------------------------------------------
  // Accept methods
  // -----------------------------------------------------------------------------------------------

  /**
   * Checks that this field node is compatible with the given ASM API version. This method checks
   * that this node, and all its children recursively, do not contain elements that were introduced
   * in more recent versions of the ASM API than the given version.
   *
   * @param version a Java ClassFile version. Must be one of the {@code V}<i>x</i> values in {@link
   *     Opcodes}.
   */
  public void check(final int version) {
    super.checkSpecial(version);
  }

  /**
   * Makes the given class visitor visit this field.
   *
   * @param classVisitor a class visitor.
   */
  @Override
  public void accept(final IClassVisitor classVisitor) {
    IFieldVisitor fieldVisitor = classVisitor.visitField(access, name, desc, signature, value);
    if (fieldVisitor == null) {
      return;
    }
    // Visit the annotations.
    // Visit the non standard attributes.
    acceptSpecial(fieldVisitor);
    fieldVisitor.visitEnd();
  }
}
