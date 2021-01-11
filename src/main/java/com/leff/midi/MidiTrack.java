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

package com.leff.midi;

import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.meta.EndOfTrack;
import com.leff.midi.util.MidiUtil;
import com.leff.midi.util.VariableLengthInt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


public class MidiTrack {
    private static final boolean VERBOSE = true;

    public static final byte[] IDENTIFIER = {'M', 'T', 'r', 'k'};

    private int mSize;
    private long mEndOfTrackDelta;
    private final SortedSet<MidiEvent> mEvents = new TreeSet<>();

    public MidiTrack() {
        mSize = 0;
        mEndOfTrackDelta = 0;
    }

    public MidiTrack(InputStream in) throws IOException {
        this();

        if (VERBOSE) {
            System.out.println("Reading MidiTrack...");
        }

        byte[] buffer = new byte[4];
        in.read(buffer);

        if (!MidiUtil.bytesEqual(buffer, IDENTIFIER, 0, 4)) {
            throw new IllegalStateException("Track identifier did not match MTrk!");
        }

        in.read(buffer);
        mSize = MidiUtil.bytesToInt(buffer, 0, 4);

        buffer = new byte[mSize];
        if (mSize != in.read(buffer)) {
            throw new IOException(MessageFormat.format("Could not read {0} bytes from input buffer", mSize));
        }

        this.readTrackData(buffer);
    }

    private void readTrackData(byte[] data) throws IOException {
        InputStream in = new ByteArrayInputStream(data);

        long totalTicks = 0;

        while (in.available() > 0) {
            VariableLengthInt delta = new VariableLengthInt(in);
            totalTicks += delta.getValue();

            MidiEvent E = MidiEvent.parseEvent(totalTicks, delta.getValue(), in);
            if (E == null) {
                System.out.println("Event skipped!");
                continue;
            }

            if (VERBOSE) {
                System.out.println(E);
            }

            // Not adding the EndOfTrack event here allows the track to be
            // edited
            // after being read in from file.
            if (E.getClass().equals(EndOfTrack.class)) {
                mEndOfTrackDelta = E.getDelta();
                break;
            }
            mEvents.add(E);
        }

        mSize = 0;
        Iterator<MidiEvent> it = mEvents.iterator();
        MidiEvent last = null;
        while (it.hasNext()) {
            MidiEvent E = it.next();
            mSize += E.getSize();

            // If an event is of the same type as the previous event,
            // no status byte is written.
            if (last != null && !E.requiresStatusByte(last)) {
                mSize--;
            }
            last = E;
        }
    }

    public SortedSet<MidiEvent> getEvents() {
        return Collections.unmodifiableSortedSet(mEvents);
    }

    public int getEventCount() {
        return mEvents.size();
    }

    public int getSize() {
        return mSize;
    }

    public long getLengthInTicks() {
        if (mEvents.isEmpty()) {
            return 0;
        }

        return mEvents.last().getTick();
    }

    public long getEndOfTrackDelta() {
        return mEndOfTrackDelta;
    }

    public void dumpEvents() {
        Iterator<MidiEvent> it = mEvents.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}
