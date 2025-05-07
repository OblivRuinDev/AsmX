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

import java.util.Arrays;

/**
 * A constant whose value is computed at runtime, with a bootstrap method.
 *
 * @author Remi Forax
 * @author OblivRuinDev
 */
public final class ConstantDynamic {

    /** The constant name (can be arbitrary). */
    public final String name;

    /** The constant type (must be a field descriptor). */
    public final String descriptor;

    /** The bootstrap method to use to compute the constant value at runtime. */
    public final Handle bsm;

    /**
     * The arguments to pass to the bootstrap method, in order to compute the constant value at
     * runtime.
     */
    final Object[] bsmArgs;

    /**
     * Constructs a new {@link ConstantDynamic}.
     *
     * @param name the constant name (can be arbitrary).
     * @param descriptor the constant type (must be a field descriptor).
     * @param bootstrapMethod the bootstrap method to use to compute the constant value at runtime.
     * @param bootstrapMethodArguments the arguments to pass to the bootstrap method, in order to
     *     compute the constant value at runtime.
     */
    public ConstantDynamic(
            final String name,
            final String descriptor,
            final Handle bootstrapMethod,
            final Object... bootstrapMethodArguments) {
        this.name = name;
        this.descriptor = descriptor;
        this.bsm = bootstrapMethod;
        this.bsmArgs = bootstrapMethodArguments;
    }

    /**
     * Returns the name of this constant.
     *
     * @return the name of this constant.
     * @deprecated use {@link #name} instead
     */
    @Deprecated
    public String getName() {
        return name;
    }

    /**
     * Returns the type of this constant.
     *
     * @return the type of this constant, as a field descriptor.
     * @deprecated use {@link #descriptor} instead
     */
    @Deprecated
    public String getDescriptor() {
        return descriptor;
    }

    /**
     * Returns the bootstrap method used to compute the value of this constant.
     *
     * @return the bootstrap method used to compute the value of this constant.
     * @deprecated use {@link #bsm} instead
     */
    @Deprecated
    public Handle getBootstrapMethod() {
        return bsm;
    }

    /**
     * Returns the number of arguments passed to the bootstrap method, in order to compute the value
     * of this constant.
     *
     * @return the number of arguments passed to the bootstrap method, in order to compute the value
     *     of this constant.
     */
    public int getBootstrapMethodArgumentCount() {
        return bsmArgs.length;
    }

    /**
     * Returns an argument passed to the bootstrap method, in order to compute the value of this
     * constant.
     *
     * @param index an argument index, between 0 and {@link #getBootstrapMethodArgumentCount()}
     *     (exclusive).
     * @return the argument passed to the bootstrap method, with the given index.
     */
    public Object getBootstrapMethodArgument(final int index) {
        return bsmArgs[index];
    }

    /**
     * Returns the copy of bootstrap method arguments.
     * @return  the copy of bootstrap method arguments.
     */
    public Object[] getBootstrapMethodArguments() {
        return bsmArgs.clone();
    }

    /**
     * Returns the size of this constant.
     *
     * @return the size of this constant, i.e., 2 for {@code long} and {@code double}, 1 otherwise.
     */
    public int getSize() {
        char firstCharOfDescriptor = descriptor.charAt(0);
        return (firstCharOfDescriptor == 'J' || firstCharOfDescriptor == 'D') ? 2 : 1;
    }

    @Override
    public boolean equals(final Object object) {
        return object == this ||
                (object instanceof ConstantDynamic && equals((ConstantDynamic) object));
    }

    public boolean equals(final ConstantDynamic obj) {
        return obj != null
                && name.equals(obj.name)
                && descriptor.equals(obj.descriptor)
                && bsm.equals(obj.bsm)
                && Arrays.equals(bsmArgs, obj.bsmArgs);
    }

    @Override
    public int hashCode() {
        return name.hashCode()
                ^ Integer.rotateLeft(descriptor.hashCode(), 8)
                ^ Integer.rotateLeft(bsm.hashCode(), 16)
                ^ Integer.rotateLeft(Arrays.hashCode(bsmArgs), 24);
    }

    @Override
    public String toString() {
        return name
                + " : "
                + descriptor
                + ' '
                + bsm
                + ' '
                + Arrays.toString(bsmArgs);
    }
}
