package dev.oblivruin.asm.delegate;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.IAnnotationVisitor;
import org.objectweb.asm.IRecordComponentVisitor;
import org.objectweb.asm.TypePath;

public class RecordVisitor extends Delegate<IRecordComponentVisitor> implements IRecordComponentVisitor {
    public RecordVisitor(IRecordComponentVisitor parent) {
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
