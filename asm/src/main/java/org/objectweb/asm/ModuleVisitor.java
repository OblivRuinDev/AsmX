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
 * A visitor to visit a Java module. The methods of this class must be called in the following
 * order: ( {@code visitMainClass} | ( {@code visitPackage} | {@code visitRequire} | {@code
 * visitExport} | {@code visitOpen} | {@code visitUse} | {@code visitProvide} )* ) {@code visitEnd}.
 *
 * @author Remi Forax
 * @author Eric Bruneton
 * @author OblivRuinDev
 */
public abstract class ModuleVisitor extends VerObj implements IModuleVisitor {
  /**
   * The parent IModuleVisitor for delegation
   */
  public final IModuleVisitor parent;

  protected ModuleVisitor() {
    super();

    this.parent = null;
  }

  /**
   * Constructs a new {@link ModuleVisitor}.
   *
   * @param ver the ASM API version implemented by this visitor. Must be one of {@link Opcodes#ASM6}
   *     or {@link Opcodes#ASM7}.
   */
  protected ModuleVisitor(final int ver) {
    super(ver);

    this.parent = null;
  }

  /**
   * Constructs a new {@link ModuleVisitor}.
   *
   * @param ver the ASM API version implemented by this visitor. Must be one of {@link Opcodes#ASM6}
   *     or {@link Opcodes#ASM7}.
   * @param moduleVisitor the module visitor to which this visitor must delegate method calls. May
   *     be null.
   */
  protected ModuleVisitor(final int ver, final IModuleVisitor moduleVisitor) {
    super(ver);

    this.parent = moduleVisitor;
  }

  /**
   * The module visitor to which this visitor must delegate method calls. May be {@literal null}.
   *
   * @return the module visitor to which this visitor must delegate method calls, or {@literal
   *     null}.
   */
  @Deprecated
  public IModuleVisitor getDelegate() {
    return parent;
  }

  @Override
  public void visitMainClass(final String mainClass) {
    if (parent != null) {
      parent.visitMainClass(mainClass);
    }
  }

  @Override
  public void visitPackage(final String packaze) {
    if (parent != null) {
      parent.visitPackage(packaze);
    }
  }

  @Override
  public void visitRequire(final String module, final int access, final String version) {
    if (parent != null) {
      parent.visitRequire(module, access, version);
    }
  }

  @Override
  public void visitExport(final String packaze, final int access, final String... modules) {
    if (parent != null) {
      parent.visitExport(packaze, access, modules);
    }
  }

  @Override
  public void visitOpen(final String packaze, final int access, final String... modules) {
    if (parent != null) {
      parent.visitOpen(packaze, access, modules);
    }
  }

  @Override
  public void visitUse(final String service) {
    if (parent != null) {
      parent.visitUse(service);
    }
  }

  @Override
  public void visitProvide(final String service, final String... providers) {
    if (parent != null) {
      parent.visitProvide(service, providers);
    }
  }

  @Override
  public void visitEnd() {
    if (parent != null) {
      parent.visitEnd();
    }
  }
}
