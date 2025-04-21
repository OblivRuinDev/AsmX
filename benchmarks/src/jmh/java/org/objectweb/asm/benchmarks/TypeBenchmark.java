package org.objectweb.asm.benchmarks;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.objectweb.asm.*;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@Fork(1)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class TypeBenchmark extends AbstractBenchmark {

  private ArrayList<String> descriptors;
  private ArrayList<String> methodDescriptors;

  public TypeBenchmark() {
    super("org.objectweb.asm.benchmarks.Type");
  }

  @Setup
  public void prepare() throws Exception {
    prepareClasses();
    descriptors = new ArrayList<>();
    methodDescriptors = new ArrayList<>();
    for (byte[] classFile : classFiles) {
      new ClassReader(classFile).accept(new CollectTypesVisitor(), 0);
    }
  }

  @Benchmark
  public void getTypeFromDescriptor(final Blackhole blackhole) {
    for (String descriptor : descriptors) {
      blackhole.consume(Type.getType(descriptor));
    }
    for (String methodDescriptor : methodDescriptors) {
      blackhole.consume(Type.getType(methodDescriptor));
    }
  }

  @Benchmark
  public void getArgumentsAndReturnTypesFromDescriptor(final Blackhole blackhole) {
    for (String methodDescriptor : methodDescriptors) {
      Type[] argumentTypes = Type.getArgumentTypes(methodDescriptor);
      Type returnType = Type.getReturnType(methodDescriptor);
      blackhole.consume(Type.getMethodType(returnType, argumentTypes));
    }
  }

  @Benchmark
  public void getArgumentsAndReturnSizeFromDescriptor(final Blackhole blackhole) {
    for (String methodDescriptor : methodDescriptors) {
      blackhole.consume(Type.getArgumentsAndReturnSizes(methodDescriptor));
    }
  }

  class CollectTypesVisitor extends ClassVisitor {

    IAnnotationVisitor annotationVisitor =
        new AnnotationVisitor(ver) {

          @Override
          public void visitEnum(final String name, final String descriptor, final String value) {
            descriptors.add(descriptor);
          }

          @Override
          public IAnnotationVisitor visitAnnotation(final String name, final String descriptor) {
            descriptors.add(descriptor);
            return this;
          }
        };

    CollectTypesVisitor() {
      super();
    }

    @Override
    public void visitOuterClass(final String owner, final String name, final String descriptor) {
      if (descriptor != null) {
        methodDescriptors.add(descriptor);
      }
    }

    @Override
    public IAnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
      descriptors.add(descriptor);
      return annotationVisitor;
    }

    @Override
    public IAnnotationVisitor visitTypeAnnotation(
        final int typeRef,
        final TypePath typePath,
        final String descriptor,
        final boolean visible) {
      descriptors.add(descriptor);
      return annotationVisitor;
    }

    @Override
    public IFieldVisitor visitField(
        final int access,
        final String name,
        final String descriptor,
        final String signature,
        final Object value) {
      descriptors.add(descriptor);
      return new FieldVisitor(ver) {

        @Override
        public IAnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
          descriptors.add(descriptor);
          return annotationVisitor;
        }

        @Override
        public IAnnotationVisitor visitTypeAnnotation(
            final int typeRef,
            final TypePath typePath,
            final String descriptor,
            final boolean visible) {
          descriptors.add(descriptor);
          return annotationVisitor;
        }
      };
    }

    @Override
    public IMethodVisitor visitMethod(
        final int access,
        final String name,
        final String descriptor,
        final String signature,
        final String[] exceptions) {
      methodDescriptors.add(descriptor);
      return new MethodVisitor(ver) {

        @Override
        public IAnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
          descriptors.add(descriptor);
          return annotationVisitor;
        }

        @Override
        public IAnnotationVisitor visitTypeAnnotation(
            final int typeRef,
            final TypePath typePath,
            final String descriptor,
            final boolean visible) {
          descriptors.add(descriptor);
          return annotationVisitor;
        }

        @Override
        public IAnnotationVisitor visitParameterAnnotation(
            final int parameter, final String descriptor, final boolean visible) {
          descriptors.add(descriptor);
          return annotationVisitor;
        }

        @Override
        public void visitFieldInsn(
            final int opcode, final String owner, final String name, final String descriptor) {
          descriptors.add(descriptor);
        }

        @Override
        public void visitMethodInsn(
            final int opcode,
            final String owner,
            final String name,
            final String descriptor,
            final boolean isInterface) {
          methodDescriptors.add(descriptor);
        }

        @Override
        public void visitInvokeDynamicInsn(
            final String name,
            final String descriptor,
            final Handle bootstrapMethodHandle,
            final Object... bootstrapMethodArguments) {
          methodDescriptors.add(descriptor);
        }

        @Override
        public void visitMultiANewArrayInsn(final String descriptor, final int numDimensions) {
          descriptors.add(descriptor);
        }

        @Override
        public IAnnotationVisitor visitInsnAnnotation(
            final int typeRef,
            final TypePath typePath,
            final String descriptor,
            final boolean visible) {
          descriptors.add(descriptor);
          return annotationVisitor;
        }

        @Override
        public IAnnotationVisitor visitTryCatchAnnotation(
            final int typeRef,
            final TypePath typePath,
            final String descriptor,
            final boolean visible) {
          descriptors.add(descriptor);
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
          descriptors.add(descriptor);
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
          descriptors.add(descriptor);
          return annotationVisitor;
        }
      };
    }
  }
}
