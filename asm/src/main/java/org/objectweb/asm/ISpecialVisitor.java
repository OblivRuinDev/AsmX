// ---------------------------------------------------------------------
// ORIGINAL WORK:
// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom (https://asm.ow2.io/)
// All rights reserved.
//
// Distributed under the BSD-3-Clause License
// ---------------------------------------------------------------------

// ---------------------------------------------------------------------
// MODIFIED WORK and structural adaptations:
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
 * A special interface for asm tree API.
 *
 * @see org.objectweb.asm.tree.ATypeAnnotatedNode
 * @see org.objectweb.asm.tree.SpecialNode
 *
 * @author OblivRuinDev
 */
public interface ISpecialVisitor {
    /**
     * Visits an annotation.
     *
     * @param descriptor the class descriptor of the annotation class.
     * @param visible    {@literal true} if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or {@literal null} if this visitor is not
     * interested in visiting this annotation.
     */
    IAnnotationVisitor visitAnnotation(String descriptor, boolean visible);

    /**
     * Visits an annotation on the type.
     *
     * @param typeRef    See {@link TypeReference}.
     * @param typePath   the path to the annotated type argument, wildcard bound, array element type, or
     *                   static inner type within 'typeRef'. May be {@literal null} if the annotation targets
     *                   'typeRef' as a whole.
     * @param descriptor the class descriptor of the annotation class.
     * @param visible    {@literal true} if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or {@literal null} if this visitor is not
     * interested in visiting this annotation.
     */
    IAnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible);

    /**
     * Visits a non standard attribute of this.
     *
     * @param attribute an attribute.
     */
    void visitAttribute(Attribute attribute);

    /**
     * Visits the end of this.
     */
    void visitEnd();
}
