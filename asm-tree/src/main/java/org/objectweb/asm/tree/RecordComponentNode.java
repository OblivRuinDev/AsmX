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

import org.objectweb.asm.*;

/**
 * A node that represents a record component.
 *
 * @author Remi Forax
 * @author OblivRuinDev
 */
public class RecordComponentNode extends SpecialNode implements IRecordComponentVisitor {
  /** The record component descriptor (see {@link org.objectweb.asm.Type}). */
  public String descriptor;

  /**
   * Constructs a new {@link RecordComponentNode}.
   *
   * @param name the record component name.
   * @param descriptor the record component descriptor (see {@link org.objectweb.asm.Type}).
   * @param signature the record component signature.
   */
  public RecordComponentNode(final String name, final String descriptor, final String signature) {
    super(name, signature);
    this.descriptor = descriptor;
  }

  /**
   * Constructs a new {@link RecordComponentNode}.
   *
   * @param api the ASM API version implemented by this visitor. Must be one of {@link Opcodes#ASM8}
   *     or {@link Opcodes#ASM9}.
   * @param name the record component name.
   * @param descriptor the record component descriptor (see {@link org.objectweb.asm.Type}).
   * @param signature the record component signature.
   */
  @Deprecated
  public RecordComponentNode(
      final int api, final String name, final String descriptor, final String signature) {
    this(name, descriptor, signature);
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
   * Makes the given class visitor visit this record component.
   *
   * @param classVisitor a class visitor.
   */
  public void accept(final IClassVisitor classVisitor) {
    IRecordComponentVisitor recordComponentVisitor =
        classVisitor.visitRecordComponent(name, descriptor, signature);
    if (recordComponentVisitor == null) {
      return;
    }
    // Visit the annotations.
    // Visit the non standard attributes.
    super.acceptSpecial(recordComponentVisitor);
    recordComponentVisitor.visitEnd();
  }
}
