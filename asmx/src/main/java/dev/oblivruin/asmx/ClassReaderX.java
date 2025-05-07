package dev.oblivruin.asmx;

import dev.oblivruin.asm.ClassVersionException;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Symbol;
import org.objectweb.asm.VerObj;

public class ClassReaderX extends VerObj {
    //If it works, I believe performance will be increased
    //However, @Stable only work in classes loaded by the boot loader
    private final byte[] bytes;
    private final int[] cpInfoOffsets;
    private final String[] constUtf8;
    private final ConstantDynamic[] constantDynamics;
    public final int header;
    public final int accessFlags;
    {
        if (ver == 0) {
            throw new ClassVersionException(ver);
        }
    }

    public ClassReaderX(final byte[] classByte) throws ClassFormatError, ClassVersionException {
        // Check the class' major_version. This field is after the magic and minor_version fields, which
        // use 4 and 2 bytes respectively.
        super((short) (((classByte[6] & 0xFF) << 8) | (classByte[7] & 0xFF)));
        this.bytes = classByte;

        // Create the constant pool arrays. The constant_pool_count field is after the magic,
        // minor_version and major_version fields, which use 4, 2 and 2 bytes respectively.
        int constCount = readUnsignedShort(8);
        cpInfoOffsets = new int[constCount];
        // Compute the offset of each constant pool entry, as well as a conservative estimate of the
        // maximum length of the constant pool strings. The first constant pool entry is after the
        // magic, minor_version, major_version and constant_pool_count fields, which use 4, 2, 2 and 2
        // bytes respectively.
        int offset = 10;
        int currentMaxStringLength = 0;
        boolean hasBootstrapMethods = false;
        int constantDynaCount = 0;
        int utf8Count = 0;
        // The offset of the other entries depend on the total size of all the previous entries.
        for (int i = 1; i < constCount; i++) {
            cpInfoOffsets[i] = offset + 1;
            switch (classByte[offset]) {
                case Symbol.CONSTANT_UTF8_TAG:
                    utf8Count++;
                    int cpInfoSize = 3 + readUnsignedShort(offset + 1);
                    offset += cpInfoSize;
                    if (cpInfoSize > currentMaxStringLength) {
                        // The size in bytes of this CONSTANT_Utf8 structure provides a conservative estimate
                        // of the length in characters of the corresponding string, and is much cheaper to
                        // compute than this exact length.
                        currentMaxStringLength = cpInfoSize;
                    }
                    break;
                case Symbol.CONSTANT_DYNAMIC_TAG:
                    constantDynaCount++;
                    //offset += 5;
                    //hasBootstrapMethods = true;
                    //break;
                case Symbol.CONSTANT_INVOKE_DYNAMIC_TAG:
                    //offset += 5;
                    hasBootstrapMethods = true;
                    //break;
                case Symbol.CONSTANT_FIELDREF_TAG:
                case Symbol.CONSTANT_METHODREF_TAG:
                case Symbol.CONSTANT_INTERFACE_METHODREF_TAG:
                case Symbol.CONSTANT_INTEGER_TAG:
                case Symbol.CONSTANT_FLOAT_TAG:
                case Symbol.CONSTANT_NAME_AND_TYPE_TAG:
                    offset += 5;
                    break;
                case Symbol.CONSTANT_LONG_TAG:
                case Symbol.CONSTANT_DOUBLE_TAG:
                    offset += 9;
                    cpInfoOffsets[++i] = -1;//unavailable
                    break;
                case Symbol.CONSTANT_METHOD_HANDLE_TAG:
                    offset += 4;
                    break;
                case Symbol.CONSTANT_CLASS_TAG:
                case Symbol.CONSTANT_STRING_TAG:
                case Symbol.CONSTANT_METHOD_TYPE_TAG:
                case Symbol.CONSTANT_PACKAGE_TAG:
                case Symbol.CONSTANT_MODULE_TAG:
                    offset += 3;
                    break;
                default:
                    throw new ClassFormatError("Undefined constant type, index= " + i
                            + " type: " + classByte[offset]);
            }
        }
        // The Classfile's access_flags field is just after the last constant pool entry.
        header = offset;
        accessFlags = readUnsignedShort(offset);
        constantDynamics = constantDynaCount == 0 ? null : new ConstantDynamic[constantDynaCount];
        constUtf8 = new String[utf8Count];
    }

    private String readUTF8(int offset, final char[] buffer) {
        int length = readUnsignedShort(offset);
        offset += 2;
        char[] buffer0 = new char[length];
        for (int i = 0; i < length; i++) {
            int b = bytes[offset + i] & 0xFF;
            if (b > 127) {
                buffer0 = null;
                break;
            }
            buffer0[i] = (char) b;
        }
        if (buffer0 != null) {
            return new String(buffer0);
        }
        return null;//todo
    }

    public int readByte(final int offset) {
        return bytes[offset] & 0xFF;
    }

    public int readUnsignedShort(final int offset) {
        return ((bytes[offset] & 0xFF) << 8) | (bytes[offset + 1] & 0xFF);
    }

    public short readShort(final int offset) {
        return (short) (((bytes[offset] & 0xFF) << 8) | (bytes[offset + 1] & 0xFF));
    }

    public int readInt(final int offset) {
        return ((bytes[offset] & 0xFF) << 24)
                | ((bytes[offset + 1] & 0xFF) << 16)
                | ((bytes[offset + 2] & 0xFF) << 8)
                | (bytes[offset + 3] & 0xFF);
    }

    public long readLong(final int offset) {
        return (((long) readInt(offset)) << 32) |
                (((long) readInt(offset + 4)) & 0xFFFFFFFFL);
    }

    public byte[] getBytes() {
        return bytes.clone();
    }
//    private static final class UnsafeClassReader extends ClassReaderX {
//        private static final Unsafe unsafe;
//
//        static {
//            Unsafe unsafe1;
//            try {
//                unsafe1 = Unsafe.getUnsafe();
//            } catch (SecurityException e) {
//                try {
//                    Field unsafeF = Unsafe.class.getDeclaredField("theUnsafe");
//                    unsafeF.setAccessible(true);
//                    unsafe1 = (Unsafe) unsafeF.get(null);
//                } catch (ReflectiveOperationException ex) {
//                    ex.addSuppressed(e);
//                    throw new IllegalStateException(ex);
//                }
//            }
//            unsafe = unsafe1;
//        }
//
//        public UnsafeClassReader(byte[] classByte) throws ClassFormatError, ClassVersionException {
//            super(classByte);
//        }
//    }
//    public static final boolean INIT_UNSAFE;
//    static {
//        boolean b = true;
//        try {
//            UnsafeClassReader.class.getName();
//        } catch (ExceptionInInitializerError error) {
//            b = false;
//        }
//        INIT_UNSAFE = b;
//    }
//    public static ClassReaderX of(byte[] classByte) {
//        if (INIT_UNSAFE) {
//            return new UnsafeClassReader(classByte);
//        }
//        return new ClassReaderX(classByte);
//    }
//    public static ClassReaderX ofUnsafe(byte[] classByte) {
//        if (INIT_UNSAFE) {
//            return new UnsafeClassReader(classByte);
//        }
//        throw new IllegalStateException("Class UnsafeClassReader cannot initialize!");
//    }
}
