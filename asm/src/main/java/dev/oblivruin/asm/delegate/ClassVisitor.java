package dev.oblivruin.asm.delegate;
import org.objectweb.asm.*;

public class ClassVisitor extends Delegate<IClassVisitor> implements IClassVisitor {
    public ClassVisitor(IClassVisitor visitor) {
        super(visitor);
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        parent.visit(version, access, name, signature, superName, interfaces);
    }

    public void visitSource(String source, String debug) {
        parent.visitSource(source, debug);
    }

    public IModuleVisitor visitModule(String name, int access, String version) {
        return parent.visitModule(name, access, version);
    }

    public void visitNestHost(String nestHost) {
        parent.visitNestHost(nestHost);
    }

    public void visitOuterClass(String owner, String name, String descriptor) {
        parent.visitOuterClass(owner, name, descriptor);
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

    public void visitNestMember(String nestMember) {
        parent.visitNestMember(nestMember);
    }

    public void visitPermittedSubclass(String permittedSubclass) {
        parent.visitPermittedSubclass(permittedSubclass);
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        parent.visitInnerClass(name, outerName, innerName, access);
    }

    public IRecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        return parent.visitRecordComponent(name, descriptor, signature);
    }

    public IFieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return parent.visitField(access, name, descriptor, signature, value);
    }

    public IMethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return parent.visitMethod(access, name, descriptor, signature, exceptions);
    }

    public void visitEnd() {
        parent.visitEnd();
    }
}
