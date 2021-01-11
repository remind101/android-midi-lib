import com.leff.midi.MidiFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            byte[] midiData = Files.readAllBytes(Paths.get("example.mid"));
            MidiFile mf = new MidiFile(midiData);
            mf.dumpToConsole();
            System.out.println("Success!");
        } catch (IOException e) {
            System.err.println("Error parsing MIDI file:");
            e.printStackTrace();
        }
    }
}
