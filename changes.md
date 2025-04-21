AsmX API Breaking Note
=========================
## 1. Visitor Changes

### 1.1 Method Extracting Convention
- Extract `visit<i>XXX</i>` methods
  - see [Hierarchy I<i>XXX</i>Visitor](#15-class-hierarchy-restructuring) for details.

### 1.2 Delegate Model Rewrite
- Revised delegate model implementation
  - see [Hierarchy DelegateVisitor](#15-class-hierarchy-restructuring) for details.

### 1.3 Constructor Changes
#### Parameters:
- Changed constructor argument from `int api` to `int ver`
  - Now requires **AsmX Java ClassFile Versions** instead of ASMx values from `Opcodes.java`
  - Details in [Hierarchy VerObj](#15-class-hierarchy-restructuring) section

#### Exception Throwing:
- Removed `IllegalStateException` throw in subclass constructor calls to super constructors

### 1.4 Field Change
- `api` - Plan to be removed in the future, use `ver` instead.
- `ver` - Own by VerObj, see [Hierarchy VerObj](#15-class-hierarchy-restructuring) for details

### 1.5 Class Hierarchy Restructuring
- Added visitor interfaces (`I<i>xxx</i>Visitor`)
  - Extracted `visit<i>XXX</i>` methods to dedicated interfaces
- Introduced generic components:
  - [DelegateVisitor.java](asm/src/main/java/org/objectweb/asm/DelegateVisitor.java)
    - Field consolidation: `av`, `cv`, `fv`, `mv`, `delegate` renamed to `parent`
    - Access modifier changed to `public final`
  - [VerObj.java](asm/src/main/java/org/objectweb/asm/VerObj.java)
    - Declare field `public final int ver`
---

## 2. [Opcodes.java](asm/src/main/java/org/objectweb/asm/Opcodes.java) Change

### 2.1 Removed Fields
- `ASM<i>x</i>` constants
  - **Reason:** Caused version confusion
- `SOURCE_DEPRECATED` and `SOURCE_MASK`
  - **Reason:** Deprecated functionality

### 2.2 New Fields
- `V_DYNA`:
    - Dynamically sets value from `java.class.version` system property
- `V_BYPASS`:
    - Disables ClassFile Version validation while calling `visit<i>xxx</i>`
    - **Warning:** Not a **valid** ClassFile Version in Java(just a special flag), don't
      use it by calling `ClassWriter.visit(V_BYPASS, int access, String name,String
      signature, String superName, String[] interfaces` or Java will throw
      `VerifyError` when handling your generated class bytes
> Note: `V_DYNA` and `V_BYPASS` are AsmX Java ClassFile Versions

---

## 3. [ClassReader.java](asm/src/main/java/org/objectweb/asm/ClassReader.java) Change

### 3.1 New Method
- `public int readVer()`
    - **Functionality:** Reads class bytes and returns its ClassFile Version

---

## 4. Tree API Modifications

### 4.1 Hierarchy Changes
- New base classes added:
  - [ATypeAnnotatedNode](asm-tree/src/main/java/org/objectweb/asm/tree/ATypeAnnotatedNode.java)
    - Base class for TypeAnnotated nodes
  - [SpecialNode](asm-tree/src/main/java/org/objectweb/asm/tree/SpecialNode.java)
    - Simplifies implementation of specialized node classes
- Extend relation changes:
  - Now all node class implement `I<i>XXX</i>Visitor` extend `<i>XXX</i>Visitor`