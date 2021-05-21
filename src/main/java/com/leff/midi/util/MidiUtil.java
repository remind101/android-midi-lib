//////////////////////////////////////////////////////////////////////////////
//	Copyright 2011 Alex Leffelman
//
//	Licensed under the Apache License, Version 2.0 (the "License");
//	you may not use this file except in compliance with the License.
//	You may obtain a copy of the License at
//
//	http://www.apache.org/licenses/LICENSE-2.0
//
//	Unless required by applicable law or agreed to in writing, software
//	distributed under the License is distributed on an "AS IS" BASIS,
//	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//	See the License for the specific language governing permissions and
//	limitations under the License.
//////////////////////////////////////////////////////////////////////////////

package com.leff.midi.util;

import java.util.Arrays;

public class MidiUtil {
    private MidiUtil() {}

    /**
     * MIDI Unit Conversions
     */
    public static long ticksToMs(long ticks, int mpqn, int resolution) {
        return ((ticks * mpqn) / resolution) / 1000;
    }

    public static long ticksToMs(long ticks, float bpm, int resolution) {
        return ticksToMs(ticks, bpmToMpqn(bpm), resolution);
    }

    public static double msToTicks(long ms, int mpqn, int ppq) {
        return ((ms * 1000.0) * ppq) / mpqn;
    }

    public static double msToTicks(long ms, float bpm, int ppq) {
        return msToTicks(ms, bpmToMpqn(bpm), ppq);
    }

    public static int bpmToMpqn(float bpm) {
        return (int) (bpm * 60000000);
    }

    public static float mpqnToBpm(int mpqn) {
        return mpqn / 60000000.0f;
    }

    /**
     * Utility methods for working with bytes and byte buffers
     */
    public static int bytesToInt(byte[] buff, int off, int len) {
        int num = 0;

        int shift = 1;
        for (int i = off + len - 1; i >= off; i--) {
            num += (buff[i] & 0xFF) << shift;
            shift += 8;
        }

        return num;
    }

    public static byte[] intToBytes(int val, int byteCount) {
        byte[] buffer = new byte[byteCount];
        int[] ints = new int[byteCount];
        for (int i = 0; i < byteCount; i++) {
            ints[i] = val & 0xFF;
            buffer[byteCount - i - 1] = (byte) ints[i];

            val = val >> 8;

            if (val == 0) {
                break;
            }
        }

        return buffer;
    }

    public static boolean bytesEqual(byte[] buf1, byte[] buf2, int off, int len) {
        int endIndex = off + len;
        return 0 == Arrays.compare(buf1, off, endIndex, buf2, off, endIndex);
    }

    public static byte[] extractBytes(byte[] buffer, int off, int len) {
        return Arrays.copyOfRange(buffer, off, off + len);
    }

    public static String byteToHex(byte b) {
        return String.format("%02x", b);
    }

    public static String bytesToHex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            sb.append(byteToHex(b[i])).append(" ");
        }
        return sb.toString();
    }
}
