package dev.oblivruin.asm.delegate;

import org.objectweb.asm.*;

public class MethodVisitor extends Delegate<IMethodVisitor> implements IMethodVisitor {
    public MethodVisitor(IMethodVisitor parent) {
        super(parent);
    }

    public void visitParameter(String name, int access) {
        parent.visitParameter(name, access);
    }

    public IAnnotationVisitor visitAnnotationDefault() {
        return parent.visitAnnotationDefault();
    }

    public IAnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return parent.visitAnnotation(descriptor, visible);
    }

    public IAnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return parent.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    public void visitAttribute(Attribute attribute) {
        parent.visitAttribute(attribute);
    }

    public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
        parent.visitAnnotableParameterCount(parameterCount, visible);
    }

    public IAnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        return parent.visitParameterAnnotation(parameter, descriptor, visible);
    }

    public void visitCode() {
        parent.visitCode();
    }

    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        parent.visitFrame(type, numLocal, local, numStack, stack);
    }

    public void visitInsn(int opcode) {
        parent.visitInsn(opcode);
    }

    public void visitIntInsn(int opcode, int operand) {
        parent.visitIntInsn(opcode, operand);
    }

    public void visitVarInsn(int opcode, int varIndex) {
        parent.visitVarInsn(opcode, varIndex);
    }

    public void visitTypeInsn(int opcode, String type) {
        parent.visitTypeInsn(opcode, type);
    }

    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        parent.visitFieldInsn(opcode, owner, name, descriptor);
    }

    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        parent.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        parent.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    public void visitJumpInsn(int opcode, Label label) {
        parent.visitJumpInsn(opcode, label);
    }

    public void visitLabel(Label label) {
        parent.visitLabel(label);
    }

    public void visitLdcInsn(Object value) {
        parent.visitLdcInsn(value);
    }

    public void visitIincInsn(int varIndex, int increment) {
        parent.visitIincInsn(varIndex, increment);
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        parent.visitTableSwitchInsn(min, max, dflt, labels);
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        parent.visitLookupSwitchInsn(dflt, keys, labels);
    }

    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        parent.visitMultiANewArrayInsn(descriptor, numDimensions);
    }

    public IAnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return parent.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        parent.visitTryCatchBlock(start, end, handler, type);
    }

    public IAnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return parent.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
    }

    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        parent.visitLocalVariable(name, descriptor, signature, start, end, index);
    }

    public IAnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        return parent.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
    }

    public void visitLineNumber(int line, Label start) {
        parent.visitLineNumber(line, start);
    }

    public void visitMaxs(int maxStack, int maxLocals) {
        parent.visitMaxs(maxStack, maxLocals);
    }

    public void visitEnd() {
        parent.visitEnd();
    }
}
