import com.leff.midi.MidiFile;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        MidiFile mf = null;
        File input = new File("example.mid");

        try {
            mf = new MidiFile(input);
            mf.dumpToConsole();

            System.out.println("Success!");
        } catch (IOException e) {
            System.err.println("Error parsing MIDI file:");
            e.printStackTrace();
            return;
        }
    }
}
