package org.objectweb.asm.tree.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;

/**
 * An {@link Analyzer} that trusts the {@link org.objectweb.asm.tree.FrameNode} values to be correct
 * in order to improve throughput. @Author Wagyourtail
 */
public class SinglePassAnalyzer extends Analyzer<BasicValue> {

  /**
   * The number of locals in the last stack map frame processed by {@link #expandFrame}. Long and
   * double values are represented with two elements.
   */
  private int currentLocals;

  /**
   * Constructs a new {@link Analyzer}.
   *
   * @param interpreter the interpreter to use to symbolically interpret the bytecode instructions.
   */
  public SinglePassAnalyzer(Interpreter<BasicValue> interpreter) {
    super(interpreter);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Frame<BasicValue>[] analyze(String owner, MethodNode method) throws AnalyzerException {
    if ((method.access & (ACC_ABSTRACT | ACC_NATIVE)) != 0) {
      frames = (Frame<BasicValue>[]) new Frame<?>[0];
      return frames;
    }
    insnList = method.instructions;
    insnListSize = insnList.size();
    currentLocals = Type.getArgumentsAndReturnSizes(method.desc) >> 2;
    if ((method.access & Opcodes.ACC_STATIC) != 0) {
      currentLocals -= 1;
    }
    handlers = (List<TryCatchBlockNode>[]) new List<?>[insnListSize];
    frames = (Frame<BasicValue>[]) new Frame<?>[insnListSize];

    // For each exception handler, and each instruction within its range, record in 'handlers' the
    // fact that execution can flow from this instruction to the exception handler.
    for (int i = 0; i < method.tryCatchBlocks.size(); ++i) {
      TryCatchBlockNode tryCatchBlock = method.tryCatchBlocks.get(i);
      int startIndex = insnList.indexOf(tryCatchBlock.start);
      int endIndex = insnList.indexOf(tryCatchBlock.end);
      for (int j = startIndex; j < endIndex; ++j) {
        List<TryCatchBlockNode> insnHandlers = handlers[j];
        if (insnHandlers == null) {
          insnHandlers = new ArrayList<>();
          handlers[j] = insnHandlers;
        }
        insnHandlers.add(tryCatchBlock);
      }
    }

    Frame<BasicValue> currentFrame;
    Frame<BasicValue> lastFrameNodeFrame;
    try {
      currentFrame = computeInitialFrame(owner, method);
      frames[0] = newFrame(currentFrame);
      lastFrameNodeFrame = currentFrame;
    } catch (RuntimeException e) {
      throw new AnalyzerException(insnList.get(0), "Error at instruction 0: " + e.getMessage(), e);
    }

    // analyze linearly using the FrameNode data
    for (int i = 0; i < insnListSize - 1; ++i) {
      AbstractInsnNode insn = insnList.get(i);
      int insnType = insn.getType();
      if (insnType == AbstractInsnNode.FRAME) {
        currentFrame = expandFrame(owner, lastFrameNodeFrame, (FrameNode) insn);
        frames[i + 1] = newFrame(currentFrame);
        lastFrameNodeFrame = frames[i + 1];
      } else if (insnType == AbstractInsnNode.LABEL || insnType == AbstractInsnNode.LINE) {
        frames[i + 1] = newFrame(currentFrame);
        continue;
      } else {
        currentFrame.execute(insn, interpreter);
        frames[i + 1] = newFrame(currentFrame);
      }
      // backfill adjacent label/line/frame so results are the same as the normal Analyzer
      for (int j = i; j >= 0; --j) {
        int type = insnList.get(j).getType();
        if (type == AbstractInsnNode.LABEL
            || type == AbstractInsnNode.LINE
            || type == AbstractInsnNode.FRAME) {
          frames[j] = newFrame(currentFrame);
        } else {
          break;
        }
      }
    }
    // remove frames on trailing label/line so results are the same as the normal Analyzer
    for (int j = insnListSize - 1; j >= 0; --j) {
      int type = insnList.get(j).getType();
      if (type == AbstractInsnNode.LABEL || type == AbstractInsnNode.LINE) {
        frames[j] = null;
      } else {
        break;
      }
    }
    return frames;
  }

  /** copied from CheckFrameAnalyzer */
  private Frame<BasicValue> expandFrame(
      final String owner, final Frame<BasicValue> previousFrame, final FrameNode frameNode)
      throws AnalyzerException {
    Frame<BasicValue> frame = newFrame(previousFrame);
    List<Object> locals = frameNode.local == null ? Collections.emptyList() : frameNode.local;
    int currentLocal = currentLocals;
    switch (frameNode.type) {
      case Opcodes.F_NEW:
      case Opcodes.F_FULL:
        currentLocal = 0;
        // fall through
      case Opcodes.F_APPEND:
        for (Object type : locals) {
          BasicValue value = newFrameValue(owner, frameNode, type);
          if (currentLocal + value.getSize() > frame.getLocals()) {
            throw new AnalyzerException(frameNode, "Cannot append more locals than maxLocals");
          }
          frame.setLocal(currentLocal++, value);
          if (value.getSize() == 2) {
            frame.setLocal(currentLocal++, interpreter.newValue(null));
          }
        }
        break;
      case Opcodes.F_CHOP:
        for (Object unusedType : locals) {
          if (currentLocal <= 0) {
            throw new AnalyzerException(frameNode, "Cannot chop more locals than defined");
          }
          if (currentLocal > 1 && frame.getLocal(currentLocal - 2).getSize() == 2) {
            currentLocal -= 2;
          } else {
            currentLocal -= 1;
          }
        }
        break;
      case Opcodes.F_SAME:
      case Opcodes.F_SAME1:
        break;
      default:
        throw new AnalyzerException(frameNode, "Illegal frame type " + frameNode.type);
    }
    currentLocals = currentLocal;
    while (currentLocal < frame.getLocals()) {
      frame.setLocal(currentLocal++, interpreter.newValue(null));
    }

    List<Object> stack = frameNode.stack == null ? Collections.emptyList() : frameNode.stack;
    frame.clearStack();
    for (Object type : stack) {
      frame.push(newFrameValue(owner, frameNode, type));
    }
    return frame;
  }

  private BasicValue newFrameValue(final String owner, final FrameNode frameNode, final Object type)
      throws AnalyzerException {
    if (type == Opcodes.TOP) {
      return interpreter.newValue(null);
    } else if (type == Opcodes.INTEGER) {
      return interpreter.newValue(Type.INT_TYPE);
    } else if (type == Opcodes.FLOAT) {
      return interpreter.newValue(Type.FLOAT_TYPE);
    } else if (type == Opcodes.LONG) {
      return interpreter.newValue(Type.LONG_TYPE);
    } else if (type == Opcodes.DOUBLE) {
      return interpreter.newValue(Type.DOUBLE_TYPE);
    } else if (type == Opcodes.NULL) {
      return interpreter.newOperation(new InsnNode(Opcodes.ACONST_NULL));
    } else if (type == Opcodes.UNINITIALIZED_THIS) {
      return interpreter.newValue(Type.getObjectType(owner));
    } else if (type instanceof String) {
      return interpreter.newValue(Type.getObjectType((String) type));
    } else if (type instanceof LabelNode) {
      AbstractInsnNode referencedNode = (LabelNode) type;
      while (referencedNode != null && !isJvmInsnNode(referencedNode)) {
        referencedNode = referencedNode.getNext();
      }
      if (referencedNode == null || referencedNode.getOpcode() != Opcodes.NEW) {
        throw new AnalyzerException(frameNode, "LabelNode does not designate a NEW instruction");
      }
      return interpreter.newValue(Type.getObjectType(((TypeInsnNode) referencedNode).desc));
    }
    throw new AnalyzerException(frameNode, "Illegal stack map frame value " + type);
  }

  private static boolean isJvmInsnNode(final AbstractInsnNode insnNode) {
    return insnNode.getOpcode() >= 0;
  }
}
