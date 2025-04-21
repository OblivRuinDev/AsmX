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
 * An abstract class to simplify the implementation of Parent Delegation Visitors.
 *
 * @param <T> The type of the delegate visitor
 *
 * @author OblivRuinDev
 */
public abstract class DelegateVisitor<T extends IVisitor> extends VerObj {
    /**
     * The parent visitor for delegation.
     */
    public final T parent;

    /**
     * Construct this without any ClassFile Version checking on later visits
     *
     * @see #DelegateVisitor(int, IVisitor)
     * @see VerObj#VerObj()
     */
    protected DelegateVisitor(T parent) {
        super();
        this.parent = parent;
    }

    /**
     * Construct this without any ClassFile Version checking on later visits
     *
     * @see #DelegateVisitor(int, IVisitor)
     * @see VerObj#VerObj()
     */
    protected DelegateVisitor() {
        super();
        this.parent = null;
    }
    /**
     * Construct this.<br>
     * Platform ClassFile Version checking if {@code ver != 0}
     * @param parent might be {@code null}
     * @param ver a ClassFile Version or {@link Opcodes}'s ClassFile Version field which name started with {@code V},<br>
     *            0 or {@link Opcodes#V_BYPASS} means don't check
     * @see VerObj#VerObj(int)
     */
    protected DelegateVisitor(int ver, T parent) {
        super(ver);
        this.parent = parent;
    }

    /**
     * @see #DelegateVisitor(int, IVisitor)
     */
    protected DelegateVisitor(int ver) {
        super(ver);
        this.parent = null;
    }

    /**
     * @return The visitor to which this visitor must delegate method calls, or {@literal null}.
     */
    public final T getDelegate() {
        return parent;
    }
}
