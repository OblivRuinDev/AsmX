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
package org.objectweb.asm.tree;

import dev.oblivruin.asm.VersionChecker;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ISpecialVisitor;
import org.objectweb.asm.TypePath;

import java.util.List;
import java.util.function.Consumer;

/**
 * A class used to simplify some nodes' implementations.
 *
 * @author OblivRuinDev
 */
public abstract class SpecialNode extends ATypeAnnotatedNode implements ISpecialVisitor {
    /** The runtime visible annotations of this. May be {@literal null}. */
    public List<AnnotationNode> visibleAnnotations;
    /** The runtime invisible annotations of this. May be {@literal null}. */
    public List<AnnotationNode> invisibleAnnotations;
    /** The non standard attributes of this. * May be {@literal null}. */
    public List<Attribute> attrs;
    /** The  signature. May be {@literal null}. */
    public String signature;
    public int access;
    public String name;

    public SpecialNode(int access, String name, String signature) {
        this.access = access;
        this.name = name;
        this.signature = signature;
    }

    public SpecialNode(final String name, final String signature) {
        this.name = name;
        this.signature = signature;
    }

    public SpecialNode() {}

    /**
     * Visit the annotations and non standard attributes.
     */
    public final void acceptSpecial(ISpecialVisitor visitor) {
        if (visibleAnnotations != null) {
            visibleAnnotations.forEach(node -> node.accept(visitor.visitAnnotation(node.desc, true)));
        }
        if (invisibleAnnotations != null) {
            invisibleAnnotations.forEach(node -> node.accept(visitor.visitAnnotation(node.desc, false)));
        }
        super.acceptTypeAnn(visitor);
        if (attrs != null) {
            attrs.forEach(visitor::visitAttribute);
        }
    }

    public final void checkSpecial(int version) {
        if (visibleTypeAnnotations != null && !visibleTypeAnnotations.isEmpty() ||
                (invisibleTypeAnnotations != null && !invisibleTypeAnnotations.isEmpty())) {
            VersionChecker.typeAnn(version);
        }
        checkTypeAnn(version);
    }

    @Override
    public AnnotationNode visitAnnotation(final String descriptor, final boolean visible) {
      AnnotationNode annotation = new AnnotationNode(descriptor);
      if (visible) {
        visibleAnnotations = Util.add(visibleAnnotations, annotation);
      } else {
        invisibleAnnotations = Util.add(invisibleAnnotations, annotation);
      }
      return annotation;
    }

    @Override
    public TypeAnnotationNode visitTypeAnnotation(
            final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
      TypeAnnotationNode typeAnnotation = new TypeAnnotationNode(typeRef, typePath, descriptor);
      if (visible) {
        visibleTypeAnnotations = Util.add(visibleTypeAnnotations, typeAnnotation);
      } else {
        invisibleTypeAnnotations = Util.add(invisibleTypeAnnotations, typeAnnotation);
      }
      return typeAnnotation;
    }

    @Override
    public void visitAttribute(final Attribute attribute) {
      attrs = Util.add(attrs, attribute);
    }
}
