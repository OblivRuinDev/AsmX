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

import static dev.oblivruin.asm.constant.AttributeNames.*;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.Arrays;
import java.util.Set;

import dev.oblivruin.asm.constant.AttributeNames;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link MethodWriter}.
 *
 * @author Eric Bruneton
 */
class MethodWriterTest {

  /**
   * Tests that the attribute name fields of Constants are the expected ones. This test is designed
   * to fail each time new attributes are added to Constants, and serves as a reminder to update the
   * {@link MethodWriter#canCopyMethodAttributes} method, if needed.
   */
  @Test
  void testCanCopyMethodAttributesUpdated() {
    Set<Object> actualAttributes =
        Arrays.stream(Constants.class.getDeclaredFields())
            .filter(field -> field.getType() == String.class)
            .map(
                field -> {
                  try {
                    return field.get(null);
                  } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException("Can't get field value", e);
                  }
                })
            .collect(toSet());

    Set<String> expectedAttributes =
        Set.of(
                AttributeNames.ConstantValue,
                AttributeNames.Code,
                StackMapTable,
                Exceptions,
                InnerClasses,
                EnclosingMethod,
                Synthetic,
                Signature,
                SourceFile,
                SourceDebugExtension,
                LineNumberTable,
                LocalVariableTable,
                LocalVariableTypeTable,
                Deprecated,
                RuntimeVisibleAnnotations,
                RuntimeInvisibleAnnotations,
                RuntimeVisibleParameterAnnotations,
                RuntimeInvisibleParameterAnnotations,
                RuntimeVisibleTypeAnnotations,
                RuntimeInvisibleTypeAnnotations,
                AnnotationDefault,
                BootstrapMethods,
                MethodParameters,
                Module,
                ModulePackages,
                ModuleMainClass,
                NestHost,
                NestMembers,
                PermittedSubclasses,
                Record);
    // IMPORTANT: if this fails, update the list AND update MethodWriter.canCopyMethodAttributes(),
    // if needed.
    assertEquals(expectedAttributes, actualAttributes);
  }

  @Test
  void testRecursiveCondyFastEnough() {
    Handle bsm =
        new Handle(
                Tag.REF_invokeStatic,
            "RT",
            "bsm",
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/Class;I)Ljava/lang/invoke/CallSite;",
            false);
    ConstantDynamic chain = new ConstantDynamic("condy", "I", bsm, 0);
    for (int i = 0; i < 32; i++) {
      chain = new ConstantDynamic("condy" + i, "I", bsm, chain);
    }
    ConstantDynamic condy = chain;

    assertTimeoutPreemptively(
        Duration.ofMillis(1_000),
        () -> {
          ClassWriter classWriter = new ClassWriter(0);
          classWriter.visit(Opcodes.V11, Opcodes.ACC_SUPER, "Test", null, "java/lang/Object", null);
          IMethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_STATIC, "m", "()V", null, null);
          mv.visitCode();
          mv.visitLdcInsn(condy);
          mv.visitMaxs(0, 0);
          mv.visitEnd();
          classWriter.visitEnd();
          classWriter.toByteArray();
        });
  }
}
