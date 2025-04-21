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
// Distributed under the BSD-3-Clause License (inherits original terms)
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
package org.objectweb.asm.benchmarks;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;

/**
 * An {@link Adapter} implemented with the ASM library.
 *
 * @author Eric Bruneton
 * @author OblivRuinDev
 */
public class AsmAdapter extends Adapter {

    @Override
  public String getVersion() {
    for (int i = 9; i >= 4; --i) {
      try {
        String version = "ASM" + i;
        int asmApi = Opcodes.class.getField(version).getInt(null);
        return version;
      } catch (NoSuchFieldException ignored) {
      } catch (IllegalAccessException e) {
        throw new AssertionError(e);
      }
    }
    return "";
  }

  @Override
  public ClassInfo getClassInfo(final byte[] classFile) {
    ClassReader classReader = new ClassReader(classFile);
    return new ClassInfo(
        classReader.getAccess(),
        classReader.getClassName(),
        classReader.getSuperName(),
        classReader.getInterfaces());
  }

  @Override
  public Object getClassObjectModel(final byte[] classFile) {
    ClassNode classNode = new ClassNode();
    new ClassReader(classFile).accept(classNode, 0);
    return classNode;
  }

  @Override
  public int read(final byte[] classFile) {
    CountingVisitor countingVisitor = new CountingVisitor();
    new ClassReader(classFile).accept(countingVisitor, 0);
    return countingVisitor.count;
  }

  @Override
  public byte[] readAndWrite(final byte[] classFile, final boolean computeMaxs) {
    ClassReader classReader = new ClassReader(classFile);
    ClassWriter classWriter = new ClassWriter(computeMaxs ? ClassWriter.COMPUTE_MAXS : 0);
    classReader.accept(classWriter, 0);
    return classWriter.toByteArray();
  }

  @Override
  public byte[] readAndWriteWithComputeFrames(final byte[] classFile) {
    ClassReader classReader = new ClassReader(classFile);
    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    classReader.accept(classWriter, 0);
    return classWriter.toByteArray();
  }

  @Override
  public byte[] readAndWriteWithCopyPool(final byte[] classFile) {
    ClassReader classReader = new ClassReader(classFile);
    ClassWriter classWriter = new ClassWriter(classReader, 0);
    classReader.accept(classWriter, 0);
    return classWriter.toByteArray();
  }

  @Override
  public byte[] readAndWriteWithObjectModel(final byte[] classFile) {
    ClassWriter classWriter = new ClassWriter(0);
    ClassNode classNode = new ClassNode();
    new ClassReader(classFile).accept(classNode, 0);
    classNode.accept(classWriter);
    return classWriter.toByteArray();
  }

  private static class CountingVisitor implements IClassVisitor {

    int count;

    IAnnotationVisitor annotationVisitor = new IAnnotationVisitor() {

          @Override
          public void visit(final String name, final Object value) {
            ++count;
          }

          @Override
          public void visitEnum(final String name, final String descriptor, final String value) {
            ++count;
          }

          @Override
          public IAnnotationVisitor visitAnnotation(final String name, final String descriptor) {
            ++count;
            return this;
          }

          @Override
          public IAnnotationVisitor visitArray(final String name) {
            ++count;
            return this;
          }

          @Override
          public void visitEnd() {
          }
    };

    public CountingVisitor() {}

    @Override
    public void visit(
        final int version,
        final int access,
        final String name,
        final String signature,
        final String superName,
        final String[] interfaces) {
      ++count;
    }

    @Override
    public void visitSource(final String source, final String debug) {
      ++count;
    }

    @Override
    public IModuleVisitor visitModule(final String name, final int access, final String version) {
      ++count;
      return null;
    }

    @Override
    public void visitNestHost(String nestHost) {

    }

    @Override
    public void visitOuterClass(final String owner, final String name, final String descriptor) {
      ++count;
    }

    @Override
    public IAnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
      ++count;
      return annotationVisitor;
    }

    @Override
    public IAnnotationVisitor visitTypeAnnotation(
        final int typeRef,
        final TypePath typePath,
        final String descriptor,
        final boolean visible) {
      ++count;
      return annotationVisitor;
    }

    @Override
    public void visitAttribute(Attribute attribute) {}

    @Override
    public void visitNestMember(String nestMember) {}

    @Override
    public void visitPermittedSubclass(String permittedSubclass) {}

    @Override
    public void visitInnerClass(
        final String name, final String outerName, final String innerName, final int access) {
      ++count;
    }

    @Override
    public IRecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
      return null;
    }

    @Override
    public IFieldVisitor visitField(
        final int access,
        final String name,
        final String descriptor,
        final String signature,
        final Object value) {
      ++count;
      return new IFieldVisitor() {

        @Override
        public IAnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
          ++count;
          return annotationVisitor;
        }

        @Override
        public IAnnotationVisitor visitTypeAnnotation(
            final int typeRef,
            final TypePath typePath,
            final String descriptor,
            final boolean visible) {
          ++count;
          return annotationVisitor;
        }

        @Override
        public void visitAttribute(Attribute attribute) {}

        @Override
        public void visitEnd() {}
      };
    }

    @Override
    public IMethodVisitor visitMethod(
        final int access,
        final String name,
        final String descriptor,
        final String signature,
        final String[] exceptions) {
      return new IMethodVisitor() {

        @Override
        public void visitParameter(final String name, final int access) {
          ++count;
        }

        @Override
        public IAnnotationVisitor visitAnnotationDefault() {
          ++count;
          return annotationVisitor;
        }

        @Override
        public IAnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
          ++count;
          return annotationVisitor;
        }

        @Override
        public IAnnotationVisitor visitTypeAnnotation(
            final int typeRef,
            final TypePath typePath,
            final String descriptor,
            final boolean visible) {
          ++count;
          return annotationVisitor;
        }

        @Override
        public void visitAnnotableParameterCount(final int parameterCount, final boolean visible) {
          ++count;
        }

        @Override
        public IAnnotationVisitor visitParameterAnnotation(
            final int parameter, final String descriptor, final boolean visible) {
          ++count;
          return annotationVisitor;
        }

        @Override
        public void visitAttribute(Attribute attribute) {}

        @Override
        public void visitCode() {}

        @Override
        public void visitFrame(
            final int type,
            final int numLocal,
            final Object[] local,
            final int numStack,
            final Object[] stack) {
          ++count;
        }

        @Override
        public void visitInsn(final int opcode) {
          ++count;
        }

        @Override
        public void visitIntInsn(final int opcode, final int operand) {
          ++count;
        }

        @Override
        public void visitVarInsn(final int opcode, final int varIndex) {
          ++count;
        }

        @Override
        public void visitTypeInsn(final int opcode, final String type) {
          ++count;
        }

        @Override
        public void visitFieldInsn(
            final int opcode, final String owner, final String name, final String descriptor) {
          ++count;
        }

        @Override
        public void visitMethodInsn(
            final int opcode,
            final String owner,
            final String name,
            final String descriptor,
            final boolean isInterface) {
          ++count;
        }

        @Override
        public void visitInvokeDynamicInsn(
            final String name,
            final String descriptor,
            final Handle bootstrapMethodHandle,
            final Object... bootstrapMethodArguments) {
          ++count;
        }

        @Override
        public void visitJumpInsn(final int opcode, final Label label) {
          ++count;
        }

        @Override
        public void visitLabel(final Label label) {
          ++count;
        }

        @Override
        public void visitLdcInsn(final Object value) {
          ++count;
        }

        @Override
        public void visitIincInsn(final int varIndex, final int increment) {
          ++count;
        }

        @Override
        public void visitTableSwitchInsn(
            final int min, final int max, final Label dflt, final Label... labels) {
          ++count;
        }

        @Override
        public void visitLookupSwitchInsn(
            final Label dflt, final int[] keys, final Label[] labels) {
          ++count;
        }

        @Override
        public void visitMultiANewArrayInsn(final String descriptor, final int numDimensions) {
          ++count;
        }

        @Override
        public IAnnotationVisitor visitInsnAnnotation(
            final int typeRef,
            final TypePath typePath,
            final String descriptor,
            final boolean visible) {
          ++count;
          return annotationVisitor;
        }

        @Override
        public void visitTryCatchBlock(
            final Label start, final Label end, final Label handler, final String type) {
          ++count;
        }

        @Override
        public IAnnotationVisitor visitTryCatchAnnotation(
            final int typeRef,
            final TypePath typePath,
            final String descriptor,
            final boolean visible) {
          ++count;
          return annotationVisitor;
        }

        @Override
        public void visitLocalVariable(
            final String name,
            final String descriptor,
            final String signature,
            final Label start,
            final Label end,
            final int index) {
          ++count;
        }

        @Override
        public IAnnotationVisitor visitLocalVariableAnnotation(
            final int typeRef,
            final TypePath typePath,
            final Label[] start,
            final Label[] end,
            final int[] index,
            final String descriptor,
            final boolean visible) {
          ++count;
          return annotationVisitor;
        }

        @Override
        public void visitLineNumber(final int line, final Label start) {
          ++count;
        }

        @Override
        public void visitMaxs(final int maxStack, final int maxLocals) {
          ++count;
        }

        @Override
        public void visitEnd() {}
      };
    }

    @Override
    public void visitEnd() {}
  }
}
