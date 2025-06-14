package dev.oblivruin.asm.delegate;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.IAnnotationVisitor;
import org.objectweb.asm.IFieldVisitor;
import org.objectweb.asm.TypePath;

public class FieldVisitor extends Delegate<IFieldVisitor> implements IFieldVisitor {
    public FieldVisitor(IFieldVisitor parent) {
        super(parent);
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

    public void visitEnd() {
        parent.visitEnd();
    }
}
