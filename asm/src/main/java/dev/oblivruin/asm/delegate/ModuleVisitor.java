package dev.oblivruin.asm.delegate;

import org.objectweb.asm.IModuleVisitor;

public class ModuleVisitor extends Delegate<IModuleVisitor> implements IModuleVisitor {
    public ModuleVisitor(IModuleVisitor parent) {
        super(parent);
    }

    public void visitMainClass(String mainClass) {
        parent.visitMainClass(mainClass);
    }

    public void visitPackage(String packaze) {
        parent.visitPackage(packaze);
    }

    public void visitRequire(String module, int access, String version) {
        parent.visitRequire(module, access, version);
    }

    public void visitExport(String packaze, int access, String... modules) {
        parent.visitExport(packaze, access, modules);
    }

    public void visitOpen(String packaze, int access, String... modules) {
        parent.visitOpen(packaze, access, modules);
    }

    public void visitUse(String service) {
        parent.visitUse(service);
    }

    public void visitProvide(String service, String... providers) {
        parent.visitProvide(service, providers);
    }

    public void visitEnd() {
        parent.visitEnd();
    }
}
