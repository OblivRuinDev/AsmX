// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom
// All rights reserved.
//
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

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.objectweb.asm.IMethodVisitor;

/**
 * A doubly linked list of {@link AbstractInsnNode} objects. <i>This implementation is not thread
 * safe</i>.
 *
 * @author OblivRuinDev
 * @see AbstractInsnNode
 */
public class InsnList implements Iterable<AbstractInsnNode>, List<AbstractInsnNode> {
  int size;

  /** The first instruction in this list. May be {@literal null}. */
  AbstractInsnNode firstInsn;

  /** The last instruction in this list. May be {@literal null}. */
  AbstractInsnNode lastInsn;

  AbstractInsnNode[] cache;

  /**
   * Points to the next unused position in array.
   */
  int pointer = 0;

  boolean array = true;

  public InsnList() {
    this(100);
  }

  public InsnList(int size) {
    cache = new AbstractInsnNode[size];
  }

  public final int size() {
    return size;
  }

  public final boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns the first instruction in this list.
   *
   * @return the first instruction in this list, or {@literal null} if the list is empty.
   */
  public AbstractInsnNode getFirst() {
    AbstractInsnNode node = cache[0];
    AbstractInsnNode node1;
    return node != null && (node1 = node.link) != null ? node1 : node;
  }

  /**
   * Returns the last instruction in this list.
   *
   * @return the last instruction in this list, or {@literal null} if the list is empty.
   */
  public AbstractInsnNode getLast() {
    return cache[pointer - 1];
  }

  /**
   * Returns the instruction whose index is given. This method builds a cache of the instructions in
   * this list to avoid scanning the whole list each time it is called. Once the cache is built,
   * this method runs in constant time. This cache is invalidated by all the methods that modify the
   * list.
   *
   * @param index the index of the instruction that must be returned.
   * @return the instruction whose index is given.
   * @throws IndexOutOfBoundsException if (index &lt; 0 || index &gt;= size()).
   *
   * @deprecated This incurs a significant performance overhead.
   *
   * @see #get(int, int)
   */
  @Deprecated
  public AbstractInsnNode get(final int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException();
    }
    reArray(0);
    return cache[index];
  }

  /**
   * Unsafe element getter. May throw Exception if arguments are bad.
   * @param index the element at the specified position in this list
   * @return Most of the time it's not null
   * @throws NullPointerException if index out of bound or offset is bad
   * @throws IndexOutOfBoundsException if index out of bound
   */
  public AbstractInsnNode get(int index, int offset) {
    return cache[index].getOffset0(offset);
  }

  /**
   * Safe element getter. Don't throw any Exception.
   * @param index the element at the specified position in this list
   * @return Maybe null if it doesn't exist
   */
  public AbstractInsnNode get1(int index, int offset) {
    if (index < 0 || index >= pointer) {
      return null;
    }
    return cache[index].getOffset(offset);
  }

  /**
   * If there are links in the array's elements,the array is rearranged to remove all links
   * @param extraExpand must {@code >= 0}, will use to add additional size to excepted array size
   */
  public void reArray(int extraExpand) {
    if (extraExpand < 0) {
      throw new IllegalArgumentException();
    }
    int size = this.size;
    int pointer0 = this.pointer;
    if (pointer0 != size) {
      this.pointer = size;
      int pointer = 0;
      AbstractInsnNode[] temp = new AbstractInsnNode[size + extraExpand];
      AbstractInsnNode node;
      for (int index = 0; index < pointer0; index++) {
        node = this.cache[index];
        if (node.offset == 0) {
          //This means element doesn't hold link
          node.index = pointer;
          temp[pointer++] = node;
        } else {
          //Element (in array) 's offset should <=0
          //This means a link is holden by this element
          //Sets the element using a positive value of the offset
          int index1 = pointer - node.offset;
          node.index = index1;
          node.offset = 0;
          temp[index1] = node;
          //Now start store links to array
          while ((node = node.link) != null) {
            node.index = pointer;
            node.offset = 0;
            temp[pointer++] = node;
          }
          //Increment to prevent element being overwritten
          pointer++;
        }
      }
      this.cache = temp;
    }
  }

  /**
   * Returns {@literal true} if the given instruction belongs to this list. This method always scans
   * the instructions of this list until it finds the given instruction or reaches the end of the
   * list.
   *
   * @param insnNode an instruction.
   * @return {@literal true} if the given instruction belongs to this list.
   */
  public boolean contains(final AbstractInsnNode insnNode) {
    if (insnNode == null)
      throw new NullPointerException();
    int index = insnNode.index;
    if (index == -1 || index >= pointer) {
      return false;
    }
    return cache[index].getOffset(insnNode.offset) == insnNode;
  }

  /**
   * Returns the index of the given instruction in this list. This method builds a cache of the
   * instruction indexes to avoid scanning the whole list each time it is called. Once the cache is
   * built, this method run in constant time. The cache is invalidated by all the methods that
   * modify the list.
   *
   * @param insnNode an instruction <i>of this list</i>.
   * @return the index of the given instruction in this list. <i>The result of this method is
   *     undefined if the given instruction does not belong to this list</i>. Use {@link #contains }
   *     to test if an instruction belongs to an instruction list or not.
   */
  public int indexOf(final AbstractInsnNode insnNode) {
    return contains(insnNode) ? insnNode.index : -1;
  }

  /**
   * Makes the given visitor visit all the instructions in this list.
   *
   * @param methodVisitor the method visitor that must visit the instructions.
   */
  public void accept(final IMethodVisitor methodVisitor) {
    for (AbstractInsnNode node : cache) {
      node.accept0(methodVisitor);
    }
  }

  @Override
  public ListIterator<AbstractInsnNode> iterator() {
    return new Iterator(0, 0);
  }

  /**
   * Returns an iterator over the instructions in this list.
   *
   * @param index index of instruction for the iterator to start at.
   * @return an iterator over the instructions in this list.
   */
  public final Iterator iterator(int index) {
    return new Iterator(index, 0);
  }

  /**
   * Returns an array containing all the instructions in this list.
   *
   * @return an array containing all the instructions in this list.
   */
  public AbstractInsnNode[] toArray() {
    reArray(0);
    return cache.clone();
  }

  /**
   * Replaces an instruction of this list with another instruction.
   *
   * @param oldInsnNode an instruction <i>of this list</i>.
   * @param newInsnNode another instruction, <i>which must not belong to any {@link InsnList}</i>.
   */
  public void set(final AbstractInsnNode oldInsnNode, final AbstractInsnNode newInsnNode) {
    if (newInsnNode.index != -1) {
      throw new IllegalArgumentException("The new value is belonging to other InsnList.");
    }
    int index = oldInsnNode.index;
    if (index == -1 || index >= pointer) {
      throw new IllegalArgumentException("The old value isn't belonging to this.");
    }
    int offset = oldInsnNode.offset;
    if (offset <= 0) {
      if (cache[index] != oldInsnNode) {
        throw new IllegalArgumentException("The old value isn't belonging to this.");
      }
      cache[index] = newInsnNode;
    } else {
      //Get previous element
      AbstractInsnNode node = cache[index].getOffset0(offset - 1);
      if (node.link != oldInsnNode) {
        throw new IllegalArgumentException("The old value isn't belonging to this.");
      }
      node.link = newInsnNode;
    }
    newInsnNode.offset = offset;
    newInsnNode.index = index;
    newInsnNode.link = oldInsnNode.link;
    oldInsnNode.index = -1;
  }

  /**
   * Adds the given instruction to the end of this list.
   *
   * @param insnNode an instruction, <i>which must not belong to any {@link InsnList}</i>.
   */
  public void add(final AbstractInsnNode insnNode) {
    ++size;
    ensure();
    insnNode.index = pointer;
    cache[pointer++] = insnNode;
  }

  /**
   * @param size the desired minimum capacity
   *
   * @see InsnList#ensure(int, int)
   */
  public final void ensure(int size) {
    ensure(size, size);
  }

  /**
   * Increases the capacity of this {@code InsnList} instance, if necessary,
   * to ensure that it can hold at least the number of elements specified by the minimum capacity argument.
   * @param size the desired minimum capacity
   * @param except the length used if the array growth
   */
  public final void ensure(int size, int except) {
    if (size >= this.cache.length) {
      AbstractInsnNode[] temp = new AbstractInsnNode[except == 0 ? size : except];
      System.arraycopy(cache, 0, temp, 0, this.cache.length);
      this.cache = temp;
    }
  }

  /**
   * Ensure array adapt current {@link #pointer}.
   */
  public final void ensure() {
    int length = this.cache.length;
    if (pointer >= length) {
      AbstractInsnNode[] temp = new AbstractInsnNode[length > 5000 ? length + 1500 : length * 2];//todo: Capacity expansion to be discussed.
      System.arraycopy(cache, 0, temp, 0, length);
      this.cache = temp;
    }
  }

  /**
   * Adds the given instructions to the end of this list.
   *
   * @param insnList an instruction list, which is cleared during the process. This list must be
   *     different from 'this'.
   */
  public void add(final InsnList insnList) {
    if (insnList.size == 0) {
      return;
    }
    this.size += insnList.size;
    ensure(pointer + insnList.pointer);
    for (AbstractInsnNode node : insnList.cache) {
      node.index = this.pointer;
      cache[this.pointer++] = node;
    }
    insnList.cache = new AbstractInsnNode[0];
  }

  public void addAll(Collection<? extends AbstractInsnNode> collection) {
    int size = collection.size();
    if (size == 0) {
      return;
    }
    ensure(pointer + size);
    this.size += size;
    for (AbstractInsnNode node : collection) {
      cache[pointer++] = node;
    }
  }

  public void addAll(AbstractInsnNode[] array) {
    if (array.length == 0) {
      return;
    }
    ensure(pointer + array.length);
    this.size += array.length;
    for (AbstractInsnNode node : array) {
      cache[pointer++] = node;
    }
  }

  /**
   * Inserts the given instruction at the beginning of this list.
   *
   * @param insnNode an instruction, <i>which must not belong to any {@link InsnList}</i>.
   */
  public void insert(final AbstractInsnNode insnNode) {
    if (pointer == 0) {
      add(insnNode);
      return;
    }
    ++size;
    AbstractInsnNode node = cache[0];
    node.offset--;
    insnNode.index = 0;
    insnNode.offset = 1;
    if (node.link != null) {
      (insnNode.link = node.link).updateOffset(1);
    }
    node.link = insnNode;
  }

  /**
   * Inserts the given instructions at the beginning of this list.
   *
   * @param insnList an instruction list, which is cleared during the process. This list must be
   *     different from 'this'.
   */
  public void insert(final InsnList insnList) {
    if (insnList.size == 0) {
      return;
    }
    this.size += insnList.size;
    int amount = insnList.pointer;
    for (AbstractInsnNode node : cache) {
      node.addIndex(amount);//update index
    }
    if (insnList.cache.length - amount > pointer) {
      System.arraycopy(cache, 0, (cache = insnList.cache), amount, pointer);
    } else {
      AbstractInsnNode[] temp = new AbstractInsnNode[pointer + amount];
      System.arraycopy(insnList.cache, 0, temp, 0, amount);
      System.arraycopy(cache, 0, (cache = temp), amount, pointer);
    }
    this.pointer += amount;
    insnList.cache = null;
  }

  /**
   * Inserts the given instruction after the specified instruction.
   *
   * @param previousInsn an instruction <i>of this list</i> after which insnNode must be inserted.
   * @param insnNode the instruction to be inserted, <i>which must not belong to any {@link
   *     InsnList}</i>.
   */
  public void insert(final AbstractInsnNode previousInsn, final AbstractInsnNode insnNode) {
    if (insnNode.index != -1) {
      throw new IllegalArgumentException("The value is belonging to other InsnList.");
    }
    if (previousInsn.index == pointer-1) {

    }
    int amount = insnNode.offset == 0 ? 1 : -insnNode.offset;
    size+=amount;
    if (previousInsn.offset <= 0) {
      previousInsn.offset-=amount;
      AbstractInsnNode node = previousInsn.link;
      if (node != null) {
        insnNode.link = node;
        node.updateOffset(amount);
      }
      previousInsn.link = insnNode;
    }
  }

  private void insertBeforeInArray(AbstractInsnNode node, AbstractInsnNode target, int number) {
    size+=number;
    node.offset-=number;
    node.link.updateOffset(number);
    node.link = target;
  }

  private void insertAfterInLink(AbstractInsnNode node, AbstractInsnNode target, int number) {}

  /**
   * Inserts the given instructions after the specified instruction.
   *
   * @param previousInsn an instruction <i>of this list</i> after which the instructions must be
   *     inserted.
   * @param insnList the instruction list to be inserted, which is cleared during the process. This
   *     list must be different from 'this'.
   */
  public void insert(final AbstractInsnNode previousInsn, final InsnList insnList) {
    if (insnList.size == 0) {
      return;
    }
    size += insnList.size;
    AbstractInsnNode firstInsnListElement = insnList.firstInsn;
    AbstractInsnNode lastInsnListElement = insnList.lastInsn;
    AbstractInsnNode nextInsn = previousInsn.nextInsn;
    if (nextInsn == null) {
      lastInsn = lastInsnListElement;
    } else {
      nextInsn.previousInsn = lastInsnListElement;
    }
    previousInsn.nextInsn = firstInsnListElement;
    lastInsnListElement.nextInsn = nextInsn;
    firstInsnListElement.previousInsn = previousInsn;
    cache = null;
    insnList.removeAll(false);
  }

  /**
   * Inserts the given instruction before the specified instruction.
   *
   * @param nextInsn an instruction <i>of this list</i> before which insnNode must be inserted.
   * @param insnNode the instruction to be inserted, <i>which must not belong to any {@link
   *     InsnList}</i>.
   */
  public void insertBefore(final AbstractInsnNode nextInsn, final AbstractInsnNode insnNode) {
    ++size;
    AbstractInsnNode previousInsn = nextInsn.previousInsn;
    if (previousInsn == null) {
      firstInsn = insnNode;
    } else {
      previousInsn.nextInsn = insnNode;
    }
    nextInsn.previousInsn = insnNode;
    insnNode.nextInsn = nextInsn;
    insnNode.previousInsn = previousInsn;
    cache = null;
    insnNode.index = 0; // insnNode now belongs to an InsnList.
  }

  /**
   * Inserts the given instructions before the specified instruction.
   *
   * @param nextInsn an instruction <i>of this list</i> before which the instructions must be
   *     inserted.
   * @param insnList the instruction list to be inserted, which is cleared during the process. This
   *     list must be different from 'this'.
   */
  public void insertBefore(final AbstractInsnNode nextInsn, final InsnList insnList) {
    if (insnList.size == 0) {
      return;
    }
    size += insnList.size;
    AbstractInsnNode firstInsnListElement = insnList.firstInsn;
    AbstractInsnNode lastInsnListElement = insnList.lastInsn;
    AbstractInsnNode previousInsn = nextInsn.previousInsn;
    if (previousInsn == null) {
      firstInsn = firstInsnListElement;
    } else {
      previousInsn.nextInsn = firstInsnListElement;
    }
    nextInsn.previousInsn = lastInsnListElement;
    lastInsnListElement.nextInsn = nextInsn;
    firstInsnListElement.previousInsn = previousInsn;
    cache = null;
    insnList.removeAll(false);
  }

  /**
   * Removes the given instruction from this list.
   *
   * @param insnNode the instruction <i>of this list</i> that must be removed.
   */
  public void remove(final AbstractInsnNode insnNode) {
    --size;
    AbstractInsnNode nextInsn = insnNode.nextInsn;
    AbstractInsnNode previousInsn = insnNode.previousInsn;
    if (nextInsn == null) {
      if (previousInsn == null) {
        firstInsn = null;
        lastInsn = null;
      } else {
        previousInsn.nextInsn = null;
        lastInsn = previousInsn;
      }
    } else {
      if (previousInsn == null) {
        firstInsn = nextInsn;
        nextInsn.previousInsn = null;
      } else {
        previousInsn.nextInsn = nextInsn;
        nextInsn.previousInsn = previousInsn;
      }
    }
    cache = null;
    insnNode.index = -1; // insnNode no longer belongs to an InsnList.
    insnNode.previousInsn = null;
    insnNode.nextInsn = null;
  }

  /**
   * Removes all the instructions of this list.
   *
   * @param mark if the instructions must be marked as no longer belonging to any {@link InsnList}.
   */
  void removeAll(final boolean mark) {
    if (mark) {
      AbstractInsnNode currentInsn = firstInsn;
      while (currentInsn != null) {
        AbstractInsnNode next = currentInsn.nextInsn;
        currentInsn.index = -1; // currentInsn no longer belongs to an InsnList.
        currentInsn.previousInsn = null;
        currentInsn.nextInsn = null;
        currentInsn = next;
      }
    }
    size = 0;
    firstInsn = null;
    lastInsn = null;
    cache = null;
  }

  /** Removes all the instructions of this list. */
  @Override
  public void clear() {
    removeAll(false);
  }

  /**
   * Resets all the labels in the instruction list. This method should be called before reusing an
   * instruction list between several <code>ClassWriter</code>s.
   */
  public void resetLabels() {
    AbstractInsnNode currentInsn = firstInsn;
    while (currentInsn != null) {
      if (currentInsn instanceof LabelNode) {
        ((LabelNode) currentInsn).resetLabel();
      }
      currentInsn = currentInsn.nextInsn;
    }
  }

  public final class Iterator implements ListIterator<AbstractInsnNode> {
    int index;
    int offset;
    AbstractInsnNode obj;

    public Iterator(int index, int offset) {
      this.index = index;
      this.offset = offset;
    }

    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public AbstractInsnNode next() {
      return null;
    }

    @Override
    public boolean hasPrevious() {
      return false;
    }

    @Override
    public AbstractInsnNode previous() {
      return null;
    }

    @Override
    public int nextIndex() {
      return 0;
    }

    @Override
    public int previousIndex() {
      return 0;
    }

    @Override
    public void remove() {

    }

    @Override
    public void set(AbstractInsnNode abstractInsnNode) {

    }

    @Override
    public void add(AbstractInsnNode abstractInsnNode) {

    }
  }

  // Note: this class is not generified because it would create bridges.
  @SuppressWarnings("rawtypes")
  private final class InsnListIterator implements ListIterator {

    AbstractInsnNode nextInsn;

    AbstractInsnNode previousInsn;

    AbstractInsnNode remove;

    InsnListIterator(final int index) {
      if (index < 0 || index > size()) {
        throw new IndexOutOfBoundsException();
      } else if (index == size()) {
        nextInsn = null;
        previousInsn = getLast();
      } else {
        AbstractInsnNode currentInsn = getFirst();
        for (int i = 0; i < index; i++) {
          currentInsn = currentInsn.nextInsn;
        }

        nextInsn = currentInsn;
        previousInsn = currentInsn.previousInsn;
      }
    }

    @Override
    public boolean hasNext() {
      return nextInsn != null;
    }

    @Override
    public Object next() {
      if (nextInsn == null) {
        throw new NoSuchElementException();
      }
      AbstractInsnNode result = nextInsn;
      previousInsn = result;
      nextInsn = result.nextInsn;
      remove = result;
      return result;
    }

    @Override
    public void remove() {
      if (remove != null) {
        if (remove == nextInsn) {
          nextInsn = nextInsn.nextInsn;
        } else {
          previousInsn = previousInsn.previousInsn;
        }
        InsnList.this.remove(remove);
        remove = null;
      } else {
        throw new IllegalStateException();
      }
    }

    @Override
    public boolean hasPrevious() {
      return previousInsn != null;
    }

    @Override
    public Object previous() {
      if (previousInsn == null) {
        throw new NoSuchElementException();
      }
      AbstractInsnNode result = previousInsn;
      nextInsn = result;
      previousInsn = result.previousInsn;
      remove = result;
      return result;
    }

    @Override
    public int nextIndex() {
      if (nextInsn == null) {
        return size();
      }
      if (cache == null) {
        cache = toArray();
      }
      return nextInsn.index;
    }

    @Override
    public int previousIndex() {
      if (previousInsn == null) {
        return -1;
      }
      if (cache == null) {
        cache = toArray();
      }
      return previousInsn.index;
    }

    @Override
    public void add(final Object o) {
      if (nextInsn != null) {
        InsnList.this.insertBefore(nextInsn, (AbstractInsnNode) o);
      } else if (previousInsn != null) {
        InsnList.this.insert(previousInsn, (AbstractInsnNode) o);
      } else {
        InsnList.this.add((AbstractInsnNode) o);
      }
      previousInsn = (AbstractInsnNode) o;
      remove = null;
    }

    @Override
    public void set(final Object o) {
      if (remove != null) {
        InsnList.this.set(remove, (AbstractInsnNode) o);
        if (remove == previousInsn) {
          previousInsn = (AbstractInsnNode) o;
        } else {
          nextInsn = (AbstractInsnNode) o;
        }
      } else {
        throw new IllegalStateException();
      }
    }
  }
}
