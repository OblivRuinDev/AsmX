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
package dev.oblivruin.asm.collection;

import java.util.Arrays;

public final class ByteArray extends Array {
    /**
     * Core data.
     * Shouldn't be {@literal null}
     */
    public byte[] data;
    public ByteArray() {
        this(200);
    }
    public ByteArray(byte[] bytes) {
        this(bytes, bytes.length);
    }
    public ByteArray(byte[] bytes, int freeOff) {
        this.data = bytes;
        this.length = freeOff;
    }
    public ByteArray(int size) {
        this.data = new byte[size];
    }

    public void add(byte b) {
        ensureFree(1);
        data[length++] = b;
    }

    public void put1_(byte b) {
        data[length++] = b;
    }

    /**
     * Copy the source ByteArray.
     * @param byteArray source ByteArray
     */
    public void add(ByteArray byteArray) {
        add(byteArray.data, 0, byteArray.length);
    }

    /**
     * Copy the source array.
     * @param bytes source array, might be null
     */
    public void add(byte[] bytes) {
        add(bytes, 0, bytes.length);
    }

    /**
     * Copy the source array within given range.
     *
     * @param bytes    source array, might be null
     * @param startPos the start of the array (inclusive)
     * @param endPos   the end of the array (exclusive)
     */
    public void addRange(byte[] bytes, int startPos, int endPos) {
        add(bytes, startPos, endPos - startPos);
    }

    /**
     * Copy the source array within given range.
     * @param bytes source array, might be null
     * @param startPos starting position
     * @param len the number of array elements to be copied
     */
    public void add(byte[] bytes, int startPos, int len) {
        if (bytes == null) {
            return;
        }
        this.ensureFree(len);
        System.arraycopy(bytes, startPos, data, length, len);
        length+=len;
    }

    /** {@inheritDoc} */
    @Override
    public void ensureFree(int size) {
        int i = length + size;
        if (i >= data.length) {
            System.arraycopy(data, 0,
                    (data = new byte[Math.max(i, data.length * 2)]), 0,
                    length);
        }
    }

    @Override
    public boolean tryExpand(int size) {
        int i = length + size;
        if (i >= data.length) {
            System.arraycopy(data, 0,
                    (data = new byte[Math.max(i, data.length * 2)]), 0,
                    length);
            return true;
        }
        return false;
    }

    public void put2(short value) {
        ensureFree(2);
        data[length++] = (byte) (value >>> 8);
        data[length++] = (byte) value;
    }

    public void put2(int value) {
        ensureFree(2);
        put2_(value);
    }

    public void put2_(int value) {
        data[length++] = (byte) (value >>> 8);
        data[length++] = (byte) value;
    }

    public void put2On(int index, int value) {
        data[index] = (byte) (value >>> 8);
        data[index + 1] = (byte) value;
    }

    public void put4(int value) {
        ensureFree(4);
        int pointer = this.length;
        byte[] array = this.data;
        array[pointer++] = (byte) (value >>> 24);
        array[pointer++] = (byte) (value >>> 16);
        array[pointer++] = (byte) (value >>> 8 );
        array[pointer  ] = (byte)  value        ;
        this.length = ++pointer;
    }

    public void put8(long value) {
        ensureFree(8);
        int pointer = this.length;
        byte[] array = this.data;
        array[pointer++] = (byte) (value >>> 56);
        array[pointer++] = (byte) (value >>> 48);
        array[pointer++] = (byte) (value >>> 40);
        array[pointer++] = (byte) (value >>> 32);
        array[pointer++] = (byte) (value >>> 24);
        array[pointer++] = (byte) (value >>> 16);
        array[pointer++] = (byte) (value >>> 8 );
        array[pointer  ] = (byte)  value        ;
        this.length = ++pointer;
    }

    public void put4(float value) {
        put4(Float.floatToIntBits(value));
    }

    public void put8(double value) {
        put8(Double.doubleToLongBits(value));
    }

    public void put222(int shortV1, int shortV2, int shortV3) {
        ensureFree(6);
        int pointer = this.length;
        byte[] array = this.data;
        array[pointer++] = (byte) (shortV1 >>> 8);
        array[pointer++] = (byte) shortV1;
        array[pointer++] = (byte) (shortV2 >>> 8);
        array[pointer++] = (byte) shortV2;
        array[pointer++] = (byte) (shortV3 >>> 8);
        array[pointer  ] = (byte) shortV3;
        this.length = ++pointer;
    }

    public void put122_(int byteV, int shortV1, int shortV2) {
        int pointer = this.length;
        byte[] array = this.data;
        array[pointer++] = (byte) byteV;
        array[pointer++] = (byte) (shortV1 >>> 8);
        array[pointer++] = (byte) shortV1;
        array[pointer++] = (byte) (shortV2 >>> 8);
        array[pointer  ] = (byte) shortV2;
        this.length = ++pointer;//faster than x++
    }

    public void put12_(int byteV, int shortV) {
        int pointer = this.length;
        byte[] array = this.data;
        array[pointer++] = (byte) byteV;
        array[pointer++] = (byte) (shortV >>> 8);
        array[pointer  ] = (byte) shortV;
        this.length = ++pointer;
    }

    public byte[] toArray() {
        return data.length == length ? data : Arrays.copyOf(data, length);
    }

    public static ByteArray of(byte... bytes) {
        return new ByteArray(bytes);
    }
}
