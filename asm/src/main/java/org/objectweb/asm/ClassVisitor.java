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

import dev.oblivruin.asmx.ClassVersionException;
import dev.oblivruin.asmx.VersionChecker;

/**
 * A visitor to visit a Java class. The methods of this class must be called in the following order:
 * {@code visit} [ {@code visitSource} ] [ {@code visitModule} ][ {@code visitNestHost} ][ {@code
 * visitOuterClass} ] ( {@code visitAnnotation} | {@code visitTypeAnnotation} | {@code
 * visitAttribute} )* ( {@code visitNestMember} | [ {@code * visitPermittedSubclass} ] | {@code
 * visitInnerClass} | {@code visitRecordComponent} | {@code visitField} | {@code visitMethod} )*
 * {@code visitEnd}.
 *
 * @author Eric Bruneton
 * @author OblivRuinDev
 */
public abstract class ClassVisitor extends DelegateVisitor<IClassVisitor> implements IClassVisitor {
  /** @see DelegateVisitor#DelegateVisitor() */
  protected ClassVisitor() {
      super();
  }

  /** @see DelegateVisitor#DelegateVisitor(int) */
  protected ClassVisitor(final int ver) {
    super(ver);
  }

  /** @see DelegateVisitor#DelegateVisitor(int, IVisitor) */
  protected ClassVisitor(final int ver, final IClassVisitor classVisitor) {
    super(ver, classVisitor);
  }

  /** @see DelegateVisitor#DelegateVisitor(IVisitor) */
  protected ClassVisitor(IClassVisitor visitor) {
    super(visitor);
  }

  /** {@inheritDoc} */
  @Override
  public void visit(
          final int version,
          final int access,
          final String name,
          final String signature,
          final String superName,
          final String[] interfaces) {
    if (version > ver && ver != 0) {
      throw new ClassVersionException();
    }
    if ((access & Opcodes.ACC_RECORD) != 0) {
      VersionChecker.record_(version);
    }
    if (parent != null) {
      parent.visit(version, access, name, signature, superName, interfaces);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void visitSource(final String source, final String debug) {
    if (parent != null) {
      parent.visitSource(source, debug);
    }
  }

  /** {@inheritDoc} */
  @Override
  public IModuleVisitor visitModule(final String name, final int access, final String version) {
    VersionChecker.module_(ver);
    if (parent != null) {
      return parent.visitModule(name, access, version);
    }
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public void visitNestHost(final String nestHost) {
    VersionChecker.nest(ver);
    if (parent != null) {
      parent.visitNestHost(nestHost);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void visitOuterClass(final String owner, final String name, final String descriptor) {
    if (parent != null) {
      parent.visitOuterClass(owner, name, descriptor);
    }
  }

  /** {@inheritDoc} */
  @Override
  public IAnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
    if (parent != null) {
      return parent.visitAnnotation(descriptor, visible);
    }
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public IAnnotationVisitor visitTypeAnnotation(
          final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
    VersionChecker.typeAnn(ver);
    if (parent != null) {
      return parent.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public void visitAttribute(final Attribute attribute) {
    if (parent != null) {
      parent.visitAttribute(attribute);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void visitNestMember(final String nestMember) {
    VersionChecker.nest(ver);
    if (parent != null) {
      parent.visitNestMember(nestMember);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void visitPermittedSubclass(final String permittedSubclass) {
    VersionChecker.permit(ver);
    if (parent != null) {
      parent.visitPermittedSubclass(permittedSubclass);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void visitInnerClass(
          final String name, final String outerName, final String innerName, final int access) {
    if (parent != null) {
      parent.visitInnerClass(name, outerName, innerName, access);
    }
  }

  /** {@inheritDoc} */
  @Override
  public IRecordComponentVisitor visitRecordComponent(
          final String name, final String descriptor, final String signature) {
    VersionChecker.record_(ver);
    if (parent != null) {
      return parent.visitRecordComponent(name, descriptor, signature);
    }
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public IFieldVisitor visitField(
          final int access,
          final String name,
          final String descriptor,
          final String signature,
          final Object value) {
    if (parent != null) {
      return parent.visitField(access, name, descriptor, signature, value);
    }
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public IMethodVisitor visitMethod(
          final int access,
          final String name,
          final String descriptor,
          final String signature,
          final String[] exceptions) {
    if (parent != null) {
      return parent.visitMethod(access, name, descriptor, signature, exceptions);
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
