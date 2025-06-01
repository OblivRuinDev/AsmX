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
package org.objectweb.asm;

/**
 * Enumerate tags of each entry in Constant Pool.<br>
 * Or the possible values that may occur in Constant Pool.(like kinds for MethodHandle)
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.4">JVMS
 *     4.4</a></p>
 *
 * @author OblivRuinDev
 */
public final class Tag {
    /**
     * The tag value of CONSTANT_Utf8 JVMS structures.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.7">
     *     JVMS-4.4.7</a>
     */
    public static final int Utf8 = 1;
    /**
     * The tag value of CONSTANT_Integer JVMS structures.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.4">
     *     JVMS-4.4.4</a>
     */
    public static final int Integer = 3;
    /**
     * The tag value of CONSTANT_Float JVMS structures.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.4">
     *     JVMS-4.4.4</a>
     */
    public static final int Float = 4;
    /**
     * The tag value of CONSTANT_Long JVMS structures
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.5">
     *     JVMS-4.4.5</a>
     */
    public static final int Long = 5;
    /**
     * The tag value of CONSTANT_Double JVMS structures.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.5">
     *     JVMS-4.4.5</a>
     */
    public static final int Double = 6;
    /**
     * The tag value of CONSTANT_Class JVMS structures.
     * @see Class
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.1">
     *     JVMS-4.4.1</a>
     */
    public static final int Class = 7;
    /**
     * The tag value of CONSTANT_String JVMS structures.
     * @see String
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.3">
     *     JVMS-4.4.3</a>
     */
    public static final int String = 8;
    /**
     * The tag value of CONSTANT_Fieldref JVMS structures.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.2">
     *     JVMS-4.4.2</a>
     */
    public static final int Fieldref = 9;
    /**
     * The tag value of CONSTANT_Methodref JVMS structures.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.2">
     *     JVMS-4.4.2</a>
     */
    public static final int Methodref = 10;
    /**
     * The tag value of CONSTANT_InterfaceMethodref JVMS structures.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.2">
     *     JVMS-4.4.2</a>
     */
    public static final int InterfaceMethodRef = 11;
    /**
     * The tag value of CONSTANT_NameAndType JVMS structures.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.6">
     *     JVMS-4.4.6</a>
     */
    public static final int NameAndType = 12;
    /**
     * The tag value of CONSTANT_MethodHandle JVMS structures.
     * @see java.lang.invoke.MethodHandle
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.8">
     *     JVMS-4.4.8</a>
     * @since 1.7
     */
    public static final int MethodHandle = 15;
    /**
     * The tag value of CONSTANT_MethodType JVMS structures.
     * @see java.lang.invoke.MethodType
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.9">
     *     JVMS-4.4.9</a>
     * @since 1.7
     */
    public static final int MethodType = 16;
    /**
     * The tag value of CONSTANT_Dynamic JVMS structures.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.10">
     *     JVMS-4.4.10</a>
     */
    public static final int Dynamic = 17;
    /**
     * The tag value of CONSTANT_InvokeDynamic JVMS structures.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.10">
     *     JVMS-4.4.10</a>
     */
    public static final int InvokeDynamic = 18;
    /**
     * The tag value of CONSTANT_Module JVMS structures.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.11">
     *     JVMS-4.4.11</a>
     * @since 9
     */
    public static final int Module = 19;
    /**
     * The tag value of CONSTANT_Package JVMS structures.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se24/html/jvms-4.html#jvms-4.4.12">
     *     JVMS-4.4.12</a>
     * @since 9
     */
    public static final int Package = 20;

    // =========== Kinds for MethodHandle ===========
    /** Kind for {@link #MethodHandle}. */
    public static final int REF_getField         = 1;
    /** Kind for {@link #MethodHandle}. */
    public static final int REF_getStatic        = 2;
    /** Kind for {@link #MethodHandle}. */
    public static final int REF_putField         = 3;
    /** Kind for {@link #MethodHandle}. */
    public static final int REF_putStatic        = 4;
    /** Kind for {@link #MethodHandle}. */
    public static final int REF_invokeVirtual    = 5;
    /** Kind for {@link #MethodHandle}. */
    public static final int REF_invokeStatic     = 6;
    /** Kind for {@link #MethodHandle}. */
    public static final int REF_invokeSpecial    = 7;
    /** Kind for {@link #MethodHandle}. */
    public static final int REF_newInvokeSpecial = 8;
    /** Kind for {@link #MethodHandle}. */
    public static final int REF_invokeInterface  = 9;
    /** Kind for {@link #MethodHandle}. */

    private Tag() {}
}
