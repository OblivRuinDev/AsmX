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
// Distributed under the BSD-3-Clause License, preserving original terms
// for ASM code.
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
package org.objectweb.asm.tree;

import java.util.*;

import org.objectweb.asm.IMethodVisitor;

/**
 * A node that represents a bytecode instruction. <i>An instruction can appear at most once in at
 * most one {@link InsnList} at a time</i>.
 *
 * @author Eric Bruneton
 * @author OblivRuinDev
 * @see InsnList
 */
public abstract class AbstractInsnNode extends ATypeAnnotatedNode {

  /** The type of {@link InsnNode} instructions. */
  public static final int INSN = 0;

  /** The type of {@link IntInsnNode} instructions. */
  public static final int INT_INSN = 1;

  /** The type of {@link VarInsnNode} instructions. */
  public static final int VAR_INSN = 2;

  /** The type of {@link TypeInsnNode} instructions. */
  public static final int TYPE_INSN = 3;

  /** The type of {@link FieldInsnNode} instructions. */
  public static final int FIELD_INSN = 4;

  /** The type of {@link MethodInsnNode} instructions. */
  public static final int METHOD_INSN = 5;

  /** The type of {@link InvokeDynamicInsnNode} instructions. */
  public static final int INVOKE_DYNAMIC_INSN = 6;

  /** The type of {@link JumpInsnNode} instructions. */
  public static final int JUMP_INSN = 7;

  /** The type of {@link LabelNode} "instructions". */
  public static final int LABEL = 8;

  /** The type of {@link LdcInsnNode} instructions. */
  public static final int LDC_INSN = 9;

  /** The type of {@link IincInsnNode} instructions. */
  public static final int IINC_INSN = 10;

  /** The type of {@link TableSwitchInsnNode} instructions. */
  public static final int TABLESWITCH_INSN = 11;

  /** The type of {@link LookupSwitchInsnNode} instructions. */
  public static final int LOOKUPSWITCH_INSN = 12;

  /** The type of {@link MultiANewArrayInsnNode} instructions. */
  public static final int MULTIANEWARRAY_INSN = 13;

  /** The type of {@link FrameNode} "instructions". */
  public static final int FRAME = 14;

  /** The type of {@link LineNumberNode} "instructions". */
  public static final int LINE = 15;

  /**
   * The opcode of this instruction, or -1 if this is not a JVM instruction (e.g. a label or a line
   * number).
   */
  protected int opcode;

  /*
    The runtime visible type annotations of this instruction. This field is only used for real
    instructions (i.e. not for labels, frames, or line number nodes). This list is a list of {@link
   * TypeAnnotationNode} objects. May be {@literal null}.
   */

  /** The previous instruction in the list to which this instruction belongs. */
  AbstractInsnNode previousInsn = null;

  /** The next instruction in the list to which this instruction belongs. */
  AbstractInsnNode nextInsn = null;

  // -----------------------------------------------------------------------------------------------
  // InsnList Position Info
  // -----------------------------------------------------------------------------------------------

  AbstractInsnNode link = null;

  int index = -1;

  int offset = 0;

  /**
   * Constructs a new {@link AbstractInsnNode}.
   *
   * @param opcode the opcode of the instruction to be constructed.
   */
  protected AbstractInsnNode(final int opcode) {
    this.opcode = opcode;
  }

  /**
   * Returns the opcode of this instruction.
   *
   * @return the opcode of this instruction, or -1 if this is not a JVM instruction (e.g. a label or
   *     a line number).
   */
  public int getOpcode() {
    return opcode;
  }

  /**
   * Returns the type of this instruction.
   *
   * @return the type of this instruction, i.e. one the constants defined in this class.
   */
  public abstract int getType();

  /**
   * Returns the previous instruction in the list to which this instruction belongs, if any.
   *
   * @return the previous instruction in the list to which this instruction belongs, if any. May be
   *     {@literal null}.
   */
  public AbstractInsnNode getPrevious() {
    return previousInsn;
  }

  /**
   * Returns the next instruction in the list to which this instruction belongs, if any.
   *
   * @return the next instruction in the list to which this instruction belongs, if any. May be
   *     {@literal null}.
   */
  public AbstractInsnNode getNext() {
    return nextInsn;
  }

  /**
   * Makes the given method visitor visit this instruction.
   *
   * @param methodVisitor a method visitor.
   */
  public abstract void accept(IMethodVisitor methodVisitor);

  final void accept0(IMethodVisitor visitor) {
    if (link == null) {
      accept(visitor);
    } else if (offset < 0) {//This is an array element
      link.accept0(visitor);
      this.accept(visitor);
    } else {//This is a linked element and the link isn't null
      this.accept(visitor);
      link.accept0(visitor);
    }
  }

  /**
   * A unsafe offset getter.
   *
   * @param offset Suggest {@code >= 0}
   * @return Most of the time it's not null.<p>
   *         Return null if just reach offset but {@code node} is null.
   * @throws NullPointerException if {@code node.link == null}
   * @throws IllegalArgumentException if {@code offset < 0}
   */
  public final AbstractInsnNode getOffset0(int offset) {
    if (offset < 0 && offset != this.offset) {
      throw new IllegalArgumentException();
    }
    AbstractInsnNode node = this;
    while (offset > 0) {
      offset--;
      node = node.link;
    }
    return node;
  }

  /**
   * A safe offset getter. Don't throw any Exception.
   * @return the node in target offset, maybe null
   */
  public final AbstractInsnNode getOffset(int offset) {
    if (offset < 0 && offset != this.offset) {
      return null;
    }
    AbstractInsnNode node = this;
    while (offset > 0) {
      offset--;
      if (node == null) {
        return null;
      }
      node = node.link;
    }
    return node;
  }

  public final void updateOffset(int number) {
    this.offset += number;
    AbstractInsnNode node = link;
    while (node != null) {
      node.offset += number;
      node = node.link;
    }
  }

  final void addIndex(int extra) {
    this.index += extra;
    if (this.link != null) {
      this.link.addIndex(extra);
    }
  }

  public final void link(AbstractInsnNode target) {
    if (link == null) {
      link = target;
    } else {
      link.link(target);
    }
  }

  public final void makeOffset() {
    int depth = 0;
    AbstractInsnNode node = link;
    while (node != null) {
      node.offset = ++depth;
      node = node.link;
    }
    this.offset = -depth;
  }

  public static AbstractInsnNode makeLink(AbstractInsnNode[] array) {
    int length = array.length;
    AbstractInsnNode node = array[0];
    node.offset = -length;
    for (int index = 1; index < length; index++) {
      (node = (node.link = array[index])).offset = index;
    }
    return array[0];
  }

  public static AbstractInsnNode makeLink(Collection<AbstractInsnNode> collection) {
    Iterator<AbstractInsnNode> iterator = collection.iterator();
    if (iterator.hasNext()) {
      AbstractInsnNode node = iterator.next();
      AbstractInsnNode ret = node;
      int depth = 0;
      while (iterator.hasNext()) {
        (node = (node.link = iterator.next())).offset = ++depth;
      }
      ret.offset = -depth;
      return ret;
    }
    return null;
  }

  /**
   * Makes the given visitor visit the annotations of this instruction.
   *
   * @param methodVisitor a method visitor.
   */
  @Deprecated
  protected final void acceptAnnotations(final IMethodVisitor methodVisitor) {
    super.acceptTypeAnn(methodVisitor);
  }

  /**
   * Returns a copy of this instruction.
   *
   * @param clonedLabels a map from LabelNodes to cloned LabelNodes.
   * @return a copy of this instruction. The returned instruction does not belong to any {@link
   *     InsnList}.
   */
  public abstract AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels);

  /**
   * Returns the clones of the given labels.
   *
   * @param labels a list of labels.
   * @param clonedLabels a map from LabelNodes to cloned LabelNodes.
   * @return the clones of the given labels.
   */
  static LabelNode[] clone(
      final List<LabelNode> labels, final Map<LabelNode, LabelNode> clonedLabels) {
    LabelNode[] clones = new LabelNode[labels.size()];
    for (int i = 0, n = clones.length; i < n; ++i) {
      clones[i] = clonedLabels.get(labels.get(i));
    }
    return clones;
  }

  /**
   * Clones the annotations of the given instruction into this instruction.
   *
   * @param insnNode the source instruction.
   * @return this instruction.
   */
  protected final AbstractInsnNode cloneAnnotations(final AbstractInsnNode insnNode) {
    if (insnNode.visibleTypeAnnotations != null) {
      this.visibleTypeAnnotations = new ArrayList<>();
      for (int i = 0, n = insnNode.visibleTypeAnnotations.size(); i < n; ++i) {
        this.visibleTypeAnnotations.add(insnNode.visibleTypeAnnotations.get(i).clone());
      }
    }
    if (insnNode.invisibleTypeAnnotations != null) {
      this.invisibleTypeAnnotations = new ArrayList<>();
      for (int i = 0, n = insnNode.invisibleTypeAnnotations.size(); i < n; ++i) {
        this.invisibleTypeAnnotations.add(insnNode.invisibleTypeAnnotations.get(i).clone());
      }
    }
    return this;
  }
}
