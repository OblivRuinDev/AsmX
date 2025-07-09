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
package dev.oblivruin.asm.constant;

/**
 * Note: The javadoc for Constant Members may have referred to the Java Virtual Machine Specifications and made some adjustments.
 *
 * <table class="striped" style="text-align:left">
 * <caption>Constant Member Usage</caption>
 * <thead><tr>
 * <th scope="col">Name</th>
 * <th scope="col">Class</th>
 * <th scope="col">Field</th>
 * <th scope="col">Method</th>
 * <th scope="col">Inner Class</th>
 * <th scope="col">Method Parameter</th>
 * <th scope="col">Module</th>
 * <th scope="col">Module Requires</th>
 * <th scope="col">Module Exports</th>
 * <th scope="col">Module Opens</th>
 * </tr></thead>
 * <tbody>
 *     <tr><th scope="row">{@link #ACC_PUBLIC}</th>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td></tr>
 *     <tr><th scope="row">{@link #ACC_PRIVATE}, {@link #ACC_PROTECTED}, {@link #ACC_STATIC}</th>
 *         <td>×</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td></tr>
 *     <tr><th scope="row">{@link #ACC_FINAL}</th>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td></tr>
 *     <tr><th scope="row">{@link #ACC_SUPER}, {@link #ACC_MODULE}</th>
 *         <td>√</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td></tr>
 *     <tr><th scope="row">{@link #ACC_OPEN}</th>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>√</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td></tr>
 *     <tr><th scope="row">{@link #ACC_SYNCHRONIZED}, {@link #ACC_BRIDGE}, {@link #ACC_VARARGS}, {@link #ACC_NATIVE}, {@link #ACC_STRICT}</th>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>√</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td></tr>
 *     <tr><th scope="row">{@link #ACC_VOLATILE}, {@link #ACC_TRANSIENT}</th>
 *         <td>×</td>
 *         <td>√</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td></tr>
 *     <tr><th scope="row">{@link #ACC_INTERFACE}, {@link #ACC_ANNOTATION}</th>
 *         <td>√</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>√</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td></tr>
 *     <tr><th scope="row">{@link #ACC_ABSTRACT}</th>
 *         <td>√</td>
 *         <td>×</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td></tr>
 *     <tr><th scope="row">{@link #ACC_ENUM}</th>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>×</td>
 *         <td>√</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td></tr>
 *     <tr><th scope="row">{@link #ACC_TRANSITIVE}, {@link #ACC_STATIC_PHASE}</th>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>√</td>
 *         <td>×</td>
 *         <td>×</td></tr>
 *     <tr><th scope="row">{@link #ACC_SYNTHETIC}</th>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td></tr>
 *     <tr><th scope="row">{@link #ACC_MANDATED}</th>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>×</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td>
 *         <td>√</td></tr>
 * </tbody>
 *
 * @author OblivRuinDev
 */
public final class Flags {
    public static final int ACC_PUBLIC = 0x0001;
    public static final int ACC_PRIVATE = 0x0002;
    public static final int ACC_PROTECTED = 0x0004;
    public static final int ACC_STATIC = 0x0008;
    public static final int ACC_FINAL = 0x0010;
    /**
     * Treat superclass methods specially when invoked by the <em>invokespecial</em> instruction.
     * <p>
     * The {@code ACC_SUPER} flag indicates which of two alternative semantics is to be expressed by the
     * <em>invokespecial</em> instruction if it appears in this class or interface.
     * Compilers to the instruction set of JVM should set the {@code ACC_SUPER} flag.
     * In Java SE 8 and above, JVM considers the {@code ACC_SUPER} flag to be set in every classfile,
     * regardless of the actual value of the flag in the classfile and the version of the classfile.
     * </p>
     * <p class="note">
     * The {@code ACC_SUPER} flag exists for backward compatibility with code compiled
     * by older compilers for the Java programming language. Prior to JDK 1.0.2,
     * the compiler generated {@code access_flags} in which the flag now representing {@code ACC_SUPER}
     * had no assigned meaning, and Oracle's Java Virtual Machine implementation ignored the flag if it was set.
     * </p>
     */
    public static final int ACC_SUPER = 0x0020;
    /**
     * Indicates that this module is open.
     */
    public static final int ACC_OPEN = 0x0020;
    /**
     * Indicates that any module which depends on the current module,
     * implicitly declares a dependence on the module indicated by this entry.
     */
    public static final int ACC_TRANSITIVE = 0x0020;
    /**
     * Declared {@code synchronized}; invocation is wrapped by a monitor use.
     */
    public static final int ACC_SYNCHRONIZED = 0x0020;
    /**
     * Indicates that this dependence is mandatory in the static phase,
     * i.e., at compile time, but is optional in the dynamic phase, i.e., at run time.
     */
    public static final int ACC_STATIC_PHASE = 0x0040;
    /**
     * Declared {@code volatile}; cannot be cached.
     */
    public static final int ACC_VOLATILE = 0x0040;
    /**
     * A bridge method, generated by the compiler.
     */
    public static final int ACC_BRIDGE = 0x0040;
    /**
     * Declared {@code transient}; not written or read by a persistent object manager.
     */
    public static final int ACC_TRANSIENT = 0x0080;
    /**
     * Declared with variable number of arguments.
     */
    public static final int ACC_VARARGS = 0x0080;
    /**
     * Declared {@code native}; implemented in a language other than the Java programming language.
     */
    public static final int ACC_NATIVE = 0x0100;
    public static final int ACC_INTERFACE = 0x0200;
    public static final int ACC_ABSTRACT = 0x0400;
    /**
     * In a class file whose major version number is at least 46 and at most 60: Declared {@code strictfp}.
     */
    public static final int ACC_STRICT = 0x0800;
    /**
     * Indicates that thing was not explicitly or implicitly declared in the source declaration.
     */
    public static final int ACC_SYNTHETIC = 0x1000;
    /**
     * Declared as an annotation interface.
     */
    public static final int ACC_ANNOTATION = 0x2000;
    /**
     * For class, it declared as an {@code enum} class.<br>
     * For field, it declared as an element of an {@code enum} class.
     */
    public static final int ACC_ENUM = 0x4000;
    /**
     * Indicates that thing was implicitly declared.
     */
    public static final int ACC_MANDATED = 0x8000;
    /**
     * Declared as a module, not a class or interface.
     */
    public static final int ACC_MODULE = 0x8000;

    private Flags() {}
}
