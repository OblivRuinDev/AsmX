package dev.oblivruin.asm.delegate;

import org.objectweb.asm.IAnnotationVisitor;

public class AnnotationVisitor extends Delegate<IAnnotationVisitor> implements IAnnotationVisitor{
    public AnnotationVisitor(IAnnotationVisitor visitor) {
        super(visitor);
    }

    public void visit(String name, Object value) {
        parent.visit(name, value);
    }

    public void visitEnum(String name, String descriptor, String value) {
        parent.visitEnum(name, descriptor, value);
    }

    public IAnnotationVisitor visitAnnotation(String name, String descriptor) {
        return parent.visitAnnotation(name, descriptor);
    }

    public IAnnotationVisitor visitArray(String name) {
        return parent.visitArray(name);
    }

    public void visitEnd() {
        parent.visitEnd();
    }
}
