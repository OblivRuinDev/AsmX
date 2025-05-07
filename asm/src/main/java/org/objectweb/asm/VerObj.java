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

import dev.oblivruin.asm.ClassVersionException;

import static org.objectweb.asm.Opcodes.V_BYPASS;

/**
 * An abstract class to simplify Visitor Framework implementations.
 *
 * @author OblivRuinDev
 */
public abstract class VerObj {
    /**
     * Java ClassFile Version
     */
    public final int ver;

    /**
     * Construct this with a Java ClassFile Version.<br>
     * Platform ClassFile Version checking if {@code ver != 0}
     * @param ver a ClassFile Version or {@link Opcodes}'s ClassFile Version field
     *            which name started with {@code V},<br>
     *            0 or {@link Opcodes#V_BYPASS} means don't check
     * @throws IllegalArgumentException if {@code ver} is an invalid ClassFile Version
     *                                  and not equal to 0
     */
    protected VerObj(int ver) {
        checkClassVer(ver);
        this.ver = ver;
    }

    /**
     * Construct this without any ClassFile Version checking on later visits<br>
     * Calling this is equivalent to calling {@link #VerObj(int 0)}
     */
    protected VerObj() {
        this.ver = V_BYPASS;
    }

    public static void checkClassVer(int version) {
        if (version != 0) {
            if (minor(version) != 0) {
                System.err.println("[Warning]: AsmX detect a preview version passed as an argument!\nThis may cause problem!");
                checkReal(major(version));
            } else {
                checkReal(version);
            }
        }
    }

    public static int checkReal(int version) {
        if (version < 46 || version > 69) {
            throw new ClassVersionException(version);
        }
        return version;
    }

    public final int major() {
        return ver & 0xFFFF;
    }

    public final int minor() {
        return ver >>> 16;
    }

    public static int major(int version) {
        return version & 0xFFFF;
    }

    public static int minor(int version) {
        return version >>> 16;
    }
}
