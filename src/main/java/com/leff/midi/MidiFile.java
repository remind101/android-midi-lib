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

import com.leff.midi.util.MidiUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MidiFile {
    public static final int HEADER_SIZE = 14;
    public static final byte[] IDENTIFIER = {'M', 'T', 'h', 'd'};

    public static final int DEFAULT_RESOLUTION = 480;

    private int mType;
    private int mTrackCount;
    private int mResolution;
    private List<MidiTrack> mTracks = new ArrayList<>();

    public MidiFile(byte[] buffer) throws IOException {
        this(new ByteArrayInputStream(buffer));
    }

    public MidiFile(InputStream in) throws IOException {
        readHeader(in);
        for (int i = 0; i < mTrackCount; i++) {
            mTracks.add(new MidiTrack(in));
        }
    }

    private void readHeader(InputStream in) throws IOException {
        byte[] buffer = new byte[HEADER_SIZE];
        if (HEADER_SIZE != in.read(buffer)) {
            throw new IllegalStateException("Unable to read the file header");
        }
        initFromBuffer(buffer);
    }

    public int getType() {
        return mType;
    }

    public int getTrackCount() {
        return mTrackCount;
    }

    public int getResolution() {
        return mResolution;
    }

    public long getLengthInTicks() {
        long length = 0;
        for (MidiTrack T : mTracks) {
            long l = T.getLengthInTicks();
            if (l > length) {
                length = l;
            }
        }
        return length;
    }

    public List<MidiTrack> getTracks() {
        return Collections.unmodifiableList(mTracks);
    }

    private void initFromBuffer(byte[] buffer) {
        if (!MidiUtil.bytesEqual(buffer, IDENTIFIER, 0, 4)) {
            throw new IllegalStateException("File identifier did not match MThd!");
        }

        // Skip bytes 4-7 because they will contain the size of the header, which is
        // always 6 (the combined size of the next three fields)

        mType = MidiUtil.bytesToInt(buffer, 8, 2);
        mTrackCount = MidiUtil.bytesToInt(buffer, 10, 2);
        mResolution = MidiUtil.bytesToInt(buffer, 12, 2);
    }

    public void dumpToConsole() {
        System.out.println("MIDI File Type: " + mType);
        System.out.println("Resolution: " + mResolution);
        System.out.println("Track Count: " + mTrackCount);
        System.out.println("Tracks: ");

        for (int i = 0; i < mTrackCount; i++) {
            System.out.println("Track " + i + ": ---");
            mTracks.get(i).dumpEvents();
        }
    }
}
