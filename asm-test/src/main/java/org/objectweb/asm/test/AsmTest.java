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
package org.objectweb.asm.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Base class for the ASM tests. ASM can be used to read, write or transform any Java class, ranging
 * from very old (e.g. JDK 1.3) to very recent classes, containing all possible class file
 * structures. ASM can also be used with different variants of its API (ASM4, ASM5, ASM6, etc). In
 * order to test it thoroughly, it is therefore necessary to run read, write and transform tests,
 * for each API version, and for each class in a set of classes containing all possible class file
 * structures. The purpose of this class is to automate this process. For this it relies on:
 *
 * <ul>
 *   <li>a small set of hand-crafted classes designed to contain as much class file structures as
 *       possible (it is impossible to represent all possible bytecode sequences). These classes are
 *       called "precompiled classes" below, because they are not compiled as part of the build.
 *       Instead, they have been compiled beforehand with the appropriate JDKs (e.g. with the JDK
 *       1.3, 1.5, etc).
 *   <li>the JUnit framework for parameterized tests. Using the {@link #allClassesAndAllApis()}
 *       method, selected test methods can be instantiated for each possible (precompiled class, ASM
 *       API) tuple.
 * </ul>
 *
 * <p>For instance, to run a test on all the precompiled classes, with all the APIs, use a subclass
 * such as the following:
 *
 * <pre>
 * public class MyParameterizedTest extends AsmTest {
 *
 *   &#64;ParameterizedTest
 *   &#64;MethodSource(ALL_CLASSES_AND_ALL_APIS)
 *   public void testSomeFeature(PrecompiledClass classParameter, JavaVer apiParameter) {
 *     byte[] b = classParameter.getBytes();
 *     ClassWriter classWriter = new ClassWriter(apiParameter.value(), 0);
 *     ...
 *   }
 * }
 * </pre>
 *
 * @author Eric Bruneton
 * @author OblivRuinDev
 */
public abstract class AsmTest {

  /** The size of the temporary byte array used to read class input streams chunk by chunk. */
  private static final int INPUT_STREAM_DATA_CHUNK_SIZE = 4096;

  /**
   * MethodSource name to be used in parameterized tests that must be instantiated for all possible
   * (precompiled class, api) pairs.
   */
  public static final String ALL_CLASSES_AND_ALL_APIS = "allClassesAndAllApis";

  /**
   * MethodSource name to be used in parameterized tests that must be instantiated for all
   * precompiled classes, with the latest api.
   */
  public static final String ALL_CLASSES_AND_LATEST_API = "allClassesAndLatestApi";

  /**
   * A precompiled class, hand-crafted to contain some set of class file structures. These classes
   * are not compiled as part of the build. Instead, they have been compiled beforehand, with the
   * appropriate JDKs (including some now very hard to download and install).
   */
  public enum PrecompiledClass {
    //DEFAULT_PACKAGE("DefaultPackage"), This means to exist when Java1.1
    JDK3_ALL_INSTRUCTIONS("jdk3.AllInstructions", JavaVer.V1_3),
    JDK3_ALL_STRUCTURES("jdk3.AllStructures", JavaVer.V1_3),
    JDK3_ANONYMOUS_INNER_CLASS("jdk3.AllStructures$1", JavaVer.V1_3),
    JDK3_ARTIFICIAL_STRUCTURES("jdk3.ArtificialStructures", JavaVer.V1_3),//Bad test
    JDK3_INNER_CLASS("jdk3.AllStructures$InnerClass", JavaVer.V1_3),
    JDK3_LARGE_METHOD("jdk3.LargeMethod", JavaVer.V1_3),
    JDK3_SUB_OPTIMAL_MAX_STACK_AND_LOCALS("jdk3.SubOptimalMaxStackAndLocals", JavaVer.V1_3),
    JDK5_ALL_INSTRUCTIONS("jdk5.AllInstructions", JavaVer.V1_5),
    JDK5_ALL_STRUCTURES("jdk5.AllStructures", JavaVer.V1_5),//todo:
    JDK5_ANNOTATION("jdk5.AllStructures$InvisibleAnnotation", JavaVer.V1_5),
    JDK5_ENUM("jdk5.AllStructures$EnumClass", JavaVer.V1_5),
    JDK5_LOCAL_CLASS("jdk5.AllStructures$1LocalClass", JavaVer.V1_5),
    JDK8_ALL_FRAMES("jdk8.AllFrames", JavaVer.V1_8),
    JDK8_ALL_INSTRUCTIONS("jdk8.AllInstructions", JavaVer.V1_8),
    JDK8_ALL_STRUCTURES("jdk8.AllStructures", JavaVer.V1_8),
    JDK8_ANONYMOUS_INNER_CLASS("jdk8.AllStructures$1", JavaVer.V1_8),
    JDK8_ARTIFICIAL_STRUCTURES("jdk8.Artificial$()$Structures", JavaVer.V1_8),
    JDK8_INNER_CLASS("jdk8.AllStructures$InnerClass", JavaVer.V1_8),
    JDK8_LARGE_METHOD("jdk8.LargeMethod", JavaVer.V1_8),
    JDK9_MODULE("jdk9.module-info", JavaVer.V9),
    JDK11_ALL_INSTRUCTIONS("jdk11.AllInstructions", JavaVer.V11),
    JDK11_ALL_STRUCTURES("jdk11.AllStructures", JavaVer.V11),
    JDK11_ALL_STRUCTURES_NESTED("jdk11.AllStructures$Nested", JavaVer.V11),
    JDK14_ALL_STRUCTURES_RECORD("jdk14.AllStructures$RecordSubType", JavaVer.V14, JavaVer.V16),
    JDK14_ALL_STRUCTURES_EMPTY_RECORD("jdk14.AllStructures$EmptyRecord", JavaVer.V14, JavaVer.V16),
    JDK15_ALL_STRUCTURES("jdk15.AllStructures", JavaVer.V15, JavaVer.V17);

    public final String name;
    public final JavaVer java;
    private byte[] bytes;
    public final int ver;
      private final JavaVer publish;

      PrecompiledClass(final String name, final JavaVer java, JavaVer publish) {
      this.name = name;
      this.java = java;
      this.ver = java.value;
          this.publish = publish;
      }

    PrecompiledClass(final String name, final JavaVer jdkVersion) {
      this(name, jdkVersion, null);
    }

    PrecompiledClass(final String name) {
      this(name, JavaVer.V1_7, null);
    }

    /**
     * Returns the fully qualified name of this class.
     *
     * @return the fully qualified name of this class.
     */
    public String getName() {
      return name;
    }

    /**
     * Returns the internal name of this class.
     *
     * @return the internal name of this class.
     */
    public String getInternalName() {
      return name.endsWith(ClassFile.MODULE_INFO) ? ClassFile.MODULE_INFO : name.replace('.', '/');
    }

    /**
     * Returns true if this class was compiled with a JDK which is more recent than the given ASM
     * API. For instance, returns true for a class compiled with the JDK 1.8 if the ASM API version
     * is ASM4.
     *
     * @param api an ASM API version.
     * @return whether this class was compiled with a JDK which is more recent than api.
     */
    public boolean notSuit(final JavaVer api) {
      return (publish != null ? publish.value : java.value) > api.value;
    }

    public boolean isPreview(final JavaVer javaVer) {
      return publish != null && javaVer.value < publish.value && javaVer.value >= java.value;
    }

    public int computeCFV(final JavaVer require) {
      return (publish != null && require.value < publish.value && require.value >= java.value) ? 0 : require.value;
    }

    /**
     * Returns true if this class was compiled with a JDK which is not compatible with the JDK used
     * to run the tests.
     *
     * @return true if this class was compiled with a JDK which is not compatible with the JDK used
     *     to run the tests.
     */
    public boolean isNotCompatibleWithCurrentJdk() {
      if (publish != null) {
        if (!Util.previewFeatureEnabled()) {
          return true;
        }
        return CLASSFILE_VER != java.value;
      }
      return CLASSFILE_VER < java.value;
    }

    /**
     * Returns the content of this class.
     *
     * @return the content of this class.
     */
    public byte[] getBytes() {
      if (bytes == null) {
        bytes = AsmTest.getBytes(name, ".class");
      }
      return bytes.clone();
    }

    @Override
    public String toString() {
      return name;
    }
  }

  public static final int CLASSFILE_VER;
  static {
    String str = System.getProperty("java.class.version");
    CLASSFILE_VER = Integer.parseInt(str.substring(0, str.length() - 2));
    if (CLASSFILE_VER == 0) {
      throw new IllegalStateException("Illegal Java ClassFile Version!");
    }
  }

  /**
   * An invalid class, hand-crafted to contain some set of invalid class file structures. These
   * classes are not compiled as part of the build. Instead, they have been compiled beforehand, and
   * then manually edited to introduce errors.
   */
  public enum InvalidClass {
    INVALID_BYTECODE_OFFSET("invalid.InvalidBytecodeOffset"),
    INVALID_CLASS_VERSION("invalid.InvalidClassVersion"),
    INVALID_CODE_LENGTH("invalid.InvalidCodeLength"),
    INVALID_CONSTANT_POOL_INDEX("invalid.InvalidConstantPoolIndex"),
    INVALID_CONSTANT_POOL_REFERENCE("invalid.InvalidConstantPoolReference"),
    INVALID_CP_INFO_TAG("invalid.InvalidCpInfoTag"),
    INVALID_ELEMENT_VALUE("invalid.InvalidElementValue"),
    INVALID_INSN_TYPE_ANNOTATION_TARGET_TYPE("invalid.InvalidInsnTypeAnnotationTargetType"),
    INVALID_OPCODE("invalid.InvalidOpcode"),
    INVALID_SOURCE_DEBUG_EXTENSION("invalid.InvalidSourceDebugExtension"),
    INVALID_STACK_MAP_FRAME_TYPE("invalid.InvalidStackMapFrameType"),
    INVALID_TYPE_ANNOTATION_TARGET_TYPE("invalid.InvalidTypeAnnotationTargetType"),
    INVALID_VERIFICATION_TYPE_INFO("invalid.InvalidVerificationTypeInfo"),
    INVALID_WIDE_OPCODE("invalid.InvalidWideOpcode");

    public final String name;

    InvalidClass(final String name) {
      this.name = name;
    }

    /**
     * Returns the fully qualified name of this class.
     *
     * @return the fully qualified name of this class.
     */
    public String getName() {
      return name;
    }

    /**
     * Returns the content of this class.
     *
     * @return the content of this class.
     */
    public byte[] getBytes() {
      return AsmTest.getBytes(name, ".clazz");
    }

    @Override
    public String toString() {
      return name;
    }
  }

  /** An Java version. */
  public enum JavaVer {
    V1_2(46),
    V1_3(47),
    V1_4(48),
    V1_5(49),
    V1_6(50),
    V1_7(51),
    V1_8(52),
    V9(53),
    V10(54),
    V11(55),
    V12(56),
    V13(57),
    V14(58),
    V15(59),
    V16(60),
    V17(61),
    V18(62),
    V19(63),
    V20(64),
    V21(65),
    V22(66),
    V23(67),
    V24(68),
    V25(69)
    ;

    public final int value;

    JavaVer(int value) {
      this.value = value;
    }
  }

  /**
   * Builds a list of test arguments for a parameterized test. Parameterized test cases annotated
   * with {@code @MethodSource("allClassesAndAllApis")} will be executed on all the possible
   * (precompiledClass, api) pairs.
   *
   * @return all the possible (precompiledClass, api) pairs, for all the precompiled classes and all
   *     the given ASM API versions.
   */
  public static Stream<Arguments> allClassesAndAllApis() {
    return classesAndApis(JavaVer.values());
  }

  /**
   * Builds a list of test arguments for a parameterized test. Parameterized test cases annotated
   * with {@code @MethodSource("allClassesAndLatestApi")} will be executed on all the precompiled
   * classes, with the latest api.
   *
   * @return all the possible (precompiledClass, ASM9) pairs, for all the precompiled classes.
   */
  public static Stream<Arguments> allClassesAndLatestApi() {
    return classesAndApis(JavaVer.V25);
  }

  private static Stream<Arguments> classesAndApis(final JavaVer... apis) {
    return Arrays.stream(PrecompiledClass.values())
        .flatMap(
            precompiledClass ->
                Arrays.stream(apis).map(api -> Arguments.of(precompiledClass, api)));
  }

  private static byte[] getBytes(final String name, final String extension) {
    String resourceName = name.replace('.', '/') + extension;
    try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(resourceName)) {
      if (inputStream == null) {
        throw new IllegalArgumentException("Class not found " + name);
      }
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      byte[] data = new byte[INPUT_STREAM_DATA_CHUNK_SIZE];
      int bytesRead;
      while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
        outputStream.write(data, 0, bytesRead);
      }
      outputStream.flush();
      return outputStream.toByteArray();
    } catch (IOException e) {
      throw new ClassFormatException("Can't read " + name, e);
    }
  }
}
