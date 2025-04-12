// Original code derived from ASM framework (https://asm.ow2.io/)
// Original copyright notice:
//      ASM: a very small and fast Java bytecode manipulation framework
//      Copyright (c) 2000-2011 INRIA, France Telecom
//      All rights reserved.
//
// Modifications and structural adaptations copyright:
//      ASMX: A modifications of ASM(ASMX is just a modified branch of ASM and has nothing else to do with it)
//      Copyright (c) 2025 OblivRuinDev
//      All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holders nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
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
 * An interface visitor to visit a Java module. The methods of this class must be called in the following
 * order: ( {@code visitMainClass} | ( {@code visitPackage} | {@code visitRequire} | {@code
 * visitExport} | {@code visitOpen} | {@code visitUse} | {@code visitProvide} )* ) {@code visitEnd}.
 *
 * @author OblivRuinDev
 */
public interface IModuleVisitor extends IVisitor {
    /**
     * Visit the main class of the current module.
     *
     * @param mainClass the internal name of the main class of the current module (see {@link
     *                  Type#getInternalName()}).
     */
    void visitMainClass(String mainClass);

    /**
     * Visit a package of the current module.
     *
     * @param packaze the internal name of a package (see {@link Type#getInternalName()}).
     */
    void visitPackage(String packaze);

    /**
     * Visits a dependence of the current module.
     *
     * @param module  the fully qualified name (using dots) of the dependence.
     * @param access  the access flag of the dependence among {@code ACC_TRANSITIVE}, {@code
     *                ACC_STATIC_PHASE}, {@code ACC_SYNTHETIC} and {@code ACC_MANDATED}.
     * @param version the module version at compile time, or {@literal null}.
     */
    void visitRequire(String module, int access, String version);

    /**
     * Visit an exported package of the current module.
     *
     * @param packaze the internal name of the exported package (see {@link Type#getInternalName()}).
     * @param access  the access flag of the exported package, valid values are among {@code
     *                ACC_SYNTHETIC} and {@code ACC_MANDATED}.
     * @param modules the fully qualified names (using dots) of the modules that can access the public
     *                classes of the exported package, or {@literal null}.
     */
    void visitExport(String packaze, int access, String... modules);

    /**
     * Visit an open package of the current module.
     *
     * @param packaze the internal name of the opened package (see {@link Type#getInternalName()}).
     * @param access  the access flag of the opened package, valid values are among {@code
     *                ACC_SYNTHETIC} and {@code ACC_MANDATED}.
     * @param modules the fully qualified names (using dots) of the modules that can use deep
     *                reflection to the classes of the open package, or {@literal null}.
     */
    void visitOpen(String packaze, int access, String... modules);

    /**
     * Visit a service used by the current module. The name must be the internal name of an interface
     * or a class.
     *
     * @param service the internal name of the service (see {@link Type#getInternalName()}).
     */
    void visitUse(String service);

    /**
     * Visit an implementation of a service.
     *
     * @param service   the internal name of the service (see {@link Type#getInternalName()}).
     * @param providers the internal names (see {@link Type#getInternalName()}) of the implementations
     *                  of the service (there is at least one provider).
     */
    void visitProvide(String service, String... providers);

    /**
     * Visits the end of the module. This method, which is the last one to be called, is used to
     * inform the visitor that everything have been visited.
     */
    void visitEnd();
}
