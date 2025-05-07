// ASMX: Extended bytecode manipulation toolkit based on ASM
// Copyright (c) 2025 OblivRuinDev
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
package dev.oblivruin.asm;

import static org.objectweb.asm.Opcodes.*;

/**
 * Define class file version check behavior.<br>
 * Warning: Checking Java Preview is not supported!
 *
 * @author OblivRuinDev
 */
public final class VersionChecker {
    private VersionChecker() {}

    public static void invisAnn() {
        throw new ClassVersionException(5, "Invisible Annotation");
    }

    public static void _synthetic() {
        throw new ClassVersionException(5, "ACC_SYNTHETIC");
    }

    public static void _enum() {
        throw new ClassVersionException(5, "Enum");
    }

    public static void LDC() {
        throw new ClassVersionException(7, "LDC of MethodType/Handle");
    }

    public static void invokeDyna() {
        throw new ClassVersionException(7, "INVOKEDYNAMIC");
    }

    public static void localVarAnn() {
        throw new ClassVersionException(8, "Local Variable Annotations");
    }

    public static void methodPara() {
        throw new ClassVersionException(8, "Method Parameters");
    }

    public static void typeAnn() {
        throw new ClassVersionException(8, "Type Annotations");
    }

    public static void tryTypeAnn() {
        throw new ClassVersionException(8, "Try-catch Type Annotations");
    }

    public static void insnTypeAnn() {
        throw new ClassVersionException(8, "Instruction Type Annotations");
    }

    public static void invokeInterface() {
        throw new ClassVersionException(8, "INVOKE SPECIAL/STATIC on interfaces");
    }

    public static void _module() {
        throw new ClassVersionException(9, "Module");
    }

    public static void constDyna() {
        throw new ClassVersionException(11, "ConstantDynamic");
    }

    public static void nest() {
        throw new ClassVersionException(11, "Nest-based access");
    }

    public static void record_() {
        throw new ClassVersionException(16, "Records");
    }

    public static void permit() {
        throw new ClassVersionException(17, "Permitted Subclasses");
    }

    public static void invisAnn(int version) {
        if (version < V1_5 && version != 0) {
            invisAnn();
        }
    }

    public static void _synthetic(int version) {
        if (version < V1_5 && version != 0) {
            _synthetic();
        }
    }

    public static void _enum(int version) {
        if (version < V1_5 && version != 0) {
            _enum();
        }
    }

    public static void LDC(int version) {
        if (version < V1_7 && version != 0) {
            LDC();
        }
    }

    public static void invokeDyna(int version) {
        if (version < V1_7 && version != 0) {
            invokeDyna();
        }
    }

    public static void methodPara(int version) {
        if (version < V1_8 && version != 0) {
            methodPara();
        }
    }

    public static void typeAnn(int version) {
        if (version < V1_8 && version != 0) {
            typeAnn();
        }
    }

    public static void tryTypeAnn(int version) {
        if (version < V1_8 && version != 0) {
            tryTypeAnn();
        }
    }

    public static void insnTypeAnn(int version) {
        if (version < V1_8 && version != 0) {
            insnTypeAnn();
        }
    }

    public static void invokeInterface(int version) {
        if (version < V1_8 && version != 0) {
            invokeInterface();
        }
    }

    public static void localVarAnn(int version) {
        if (version < V1_8 && version != 0) {
            localVarAnn();
        }
    }

    public static void _module(int version) {
        if (version < V9 && version != 0) {
            _module();
        }
    }

    public static void constDyna(int version) {
        if (version < V11 && version != 0) {
            constDyna();
        }
    }

    public static void nest(int version) {
        if (version < V11 && version != 0) {
            nest();
        }
    }

    public static void record_(int version) {
        if (version < V16 && version != 0) {
            record_();
        }
    }

    public static void permit(int version) {
        if (version < V17 && version != 0) {
            permit();
        }
    }
}
