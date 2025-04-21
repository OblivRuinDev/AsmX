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

public final class Util {
    private Util() {}
    public static int getClassVer() {
        String str = System.getProperty("java.class.version");
        if (str.endsWith(".0")) {
            return Integer.parseInt(str.substring(0, str.length() - 2));
        } else {
            throw new ClassVersionException("Cannot parse Java ClassFile Version: \"java.class.version\"=" + str);
        }
    }
    public static <T> boolean equal(List<T> list1, List<T> list2) {
        if (list1 == list2) {
            return true;
        }
        if ((list1 == null || list1.isEmpty()) && (list2 == null || list2.isEmpty())) {
            return true;
        }
        return list1 == null ? (list2 == null || list2.isEmpty()) : list1.equals(list2);
    }
    public static <T> boolean equalEle(Collection<T> collection1, Collection<T> collection2) {
        if ((collection1 == null || collection1.isEmpty()) && (collection2 == null || collection2.isEmpty())) {
            return true;
        }
        if (collection1 == collection2) {
            return true;
        }
        if (collection1 == null || collection2 == null) {
            return false;
        }
        if (collection1.size() != collection2.size()) {
            return false;
        }
        EqualHelper<T>  helper = new EqualHelper<>();
        collection1.forEach(helper);
        helper.done = true;
        return false;//todo
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
