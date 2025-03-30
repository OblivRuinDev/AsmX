// ASMX: A modification of ASM
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
package dev.oblivruin.asmx;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author OblivRuinDev
 */
public final class VersionChecker {
    /**
     * Method Parameters Exception
     */
    public static void methodPara(int version) {
        if (version < V1_8)
            methodPara();
    }

    public static void methodPara() {
        throw new ClassVersionException("Method parameters require Java 8+ (class version 52+)");
    }

    public static void typeAnn() {
        throw new ClassVersionException("Type annotations require Java 8+ (class version 52+)");
    }

    public static void typeAnn(int version) {
        if (version < V1_8)
            typeAnn();
    }

    public static void tryTypeAnn(int version) {
        if (version < V1_8)
            tryTypeAnn();
    }

    public static void tryTypeAnn() {
        throw new ClassVersionException("Try-catch type annotations require Java 8+");
    }

    public static void insnTypeAnn(int version) {
        if (version < V1_8)
            insnTypeAnn();
    }

    public static void insnTypeAnn() {
        throw new ClassVersionException("Instruction type annotations require Java 8+");
    }
//todo: ?
    public static void invokeInterface() {
        throw new ClassVersionException("INVOKESPECIAL/STATIC/VIRTUAL on interfaces requires Java 8+");
    }

    public static void invokeInterface(int version) {
        if (version < V1_7)
            invokeInterface();
    }

//(value instanceof Handle
// || (value instanceof Type && ((Type) value).getSort() == Type.METHOD))
    public static void LDC(int version) {
        if (version < V1_7)
            LDC();
    }

    public static void LDC() {
        throw new ClassVersionException("LDC of MethodType/Handle requires Java 7+");
    }

    public static void localVarAnn() {
        throw new ClassVersionException("Local variable annotations require Java 8+");
    }

    public static void localVarAnn(int version) {
        if (version < V1_8)
            localVarAnn();
    }

    public static void constDyna(int version) {
        if (version < V11)
            constDyna();
    }

    public static void constDyna() {
        throw new ClassVersionException("ConstantDynamic requires Java 11+ (class version 55+)");
    }

    public static void module_(int version) {
        if (version < V9)
            throw new ClassVersionException("Modules require Java 9+ (class version 53+)");
    }

    public static void nest(int version) {
        if (version < V11)
            throw new ClassVersionException("Nest-based access requires Java 11+ (class version 55+)");
    }

    public static void record_(int version) {
        if (version < V16)
            throw new ClassVersionException("Records require Java 16+ (class version 60+)");
    }

    public static void permit(int version) {
        if (version < V17)
            throw new ClassVersionException("Permitted subclasses require Java 17+ (class version 61+)");
    }

    public static void invokeDyna(int version) {
        if (version < V1_7)
            throw new ClassVersionException("invokedynamic requires Java 7+ (class version 51+)");
    }
}
