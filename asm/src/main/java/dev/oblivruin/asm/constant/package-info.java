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
/**
 * Contains classes that declare compile-time constants for javac inlining optimization.
 * And classes which provide
 *
 * <p>Under normal conditions, classes in this package <b>will not be loaded at runtime</b>,
 * because their constant values are inlined directly into bytecode during compilation.
 * This design prevents unnecessary class loading and reduces runtime memory footprint.
 *
 * <h3>Technical Rationale</h3>
 * When javac processes {@code static final} primitive/String constants, it performs:
 * <ol>
 *   <li><b>Constant folding</b> - Computations resolved at compile time</li>
 *   <li><b>Bytecode inlining</b> - Direct embedding of constant values</li>
 *   <li><b>Class elimination</b> - Removal of constant-holder classes from runtime loading</li>
 * </ol>
 *
 * <h3>Exception Case</h3>
 * The {@link dev.oblivruin.asm.constant.RuntimeConstant} class and <i>Xxx</i>Helper
 * <b>deviate from this pattern</b>.
 */
package dev.oblivruin.asm.constant;