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

import org.objectweb.asm.VerObj;

import java.util.Collection;
import java.util.List;

public final class UtilX {
    private UtilX() {}
    public static int getClassVer() {
        String str = System.getProperty("asmx.class.version");
        if (str == null) {
            str = System.getProperty("java.class.version");
            if (str.endsWith(".0")) {//system property is end up with ".0"
                try {
                    return VerObj.checkReal(Integer.parseInt(str.substring(0, str.length() - 2)));
                } catch (NumberFormatException | ClassVersionException e) {
                    throw new IllegalArgumentException("Property {\"java.class.version\"=" + str +
                            "} is unrecognizable!\nYou can set property \"asmx.class.version\" to replace it", e);
                }
            } else {
                throw new IllegalArgumentException("Property {\"java.class.version\"=" + str +
                        "} is unrecognizable!\nYou can set property \"asmx.class.version\" to replace it");
            }
        } else {
            try {
                return VerObj.checkReal(Integer.parseInt(str));
            } catch (NumberFormatException | ClassVersionException e) {
                throw new IllegalArgumentException("Property {\"asmx.class.version\"=" + str + "} is unrecognizable!\nCheck your settings!", e);
            }
        }
    }

    /**
     * Compares two lists for content equality regardless of element order.
     * @param list1 first list
     * @param list2 second list
     * @return true if both lists contain the same elements (order-agnostic)
     * @param <T> the class of the objects in the list
     */
    public static <T> boolean equalE(List<T> list1, List<T> list2) {
        if (nullOrEmpty(list1, list2)) {
            return true;
        }
        if (list1 == null || list2 == null) {//only one list is null and another list is not empty
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        // Convert first list to array for destructive matching
        final Object[] array = list1.toArray();
        lab:for (T another : list2) {
            for (int index = 0; index < array.length; index++) {
                if (another.equals(array[index])) {
                    // Mark element as matched to prevent reuse
                    array[index] = null;
                    // Continue with next element in list2
                    continue lab;
                }
            }
            // Current element(in list2) has no match in list1
            return false;
        }
        // All elements matched successfully
        return true;
    }

    /**
     * Checks if both collections are either null or empty.<br>
     * Null collection means empty.
     * @param c1 first collection
     * @param c2 second collection
     * @return true if both collections are null/empty
     * @param <T> the class of the objects in the collection
     */
    public static <T> boolean nullOrEmpty(Collection<T> c1, Collection<T> c2) {
        return c1 == c2 ||
                ((c1 == null || c1.isEmpty()) &&
                        (c2 == null || c2.isEmpty()));
    }

}
