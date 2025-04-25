// ASMX: Extended bytecode manipulation toolkit based on ASM
// Copyright (c) 2025 OblivRuinDev
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holders nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
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
package dev.oblivruin.asm;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public final class UtilX {
    private UtilX() {}
    public static int getClassVer() {
        String str = System.getProperty("java.class.version");
        if (str.endsWith(".0")) {
            return Integer.parseInt(str.substring(0, str.length() - 2));
        } else {
            throw new ClassVersionException("Cannot parse Java ClassFile Version: \"java.class.version\"=" + str);
        }
    }
    public static <T> boolean equalE(List<T> list1, List<T> list2) {
        if (nullOrEmpty(list1, list2)) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        final Object[] array = list1.toArray();
        lab:for (T another : list2) {
            for (int index = 0; index < array.length; index++) {
                if (another.equals(array[index])) {
                    array[index] = null;
                    continue lab;
                }
            }
            return false;
        }
        return true;
    }
    public static <T> boolean nullOrEmpty(Collection<T> c1, Collection<T> c2) {
        return c1 == c2 ||
                ((c1 == null || c1.isEmpty()) &&
                        (c2 == null || c2.isEmpty()));
    }
    static final class EqualHelper<T> implements Consumer<T> {
        boolean done = false;

        @Override
        public void accept(T t) {

        }
    }
    static final class Holder<T> {
        int count0 = 0;
        int count = 0;

    }
}
