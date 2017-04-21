package cz.jan.maly.model.tracking;

import com.google.common.io.Files;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

/**
 * Replay class to mark status of particular replay
 * Created by Jan on 17-Nov-16.
 */
public class Replay implements Serializable {

    @Getter
    private final String file;

    @Getter
    private final Optional<File> replayFile;

    public static class ReplaySerializer implements Serializer<Replay>, Serializable {

        @Override
        public void serialize(@NotNull DataOutput2 out, @NotNull Replay replay) throws IOException {
            out.writeUTF(replay.file);
        }

        @Override
        public Replay deserialize(@NotNull DataInput2 in, int i) throws IOException {
            return new Replay(in.readUTF());
        }
    }

    Replay(String file) {
        this.file = file;
        this.replayFile = getFile();
    }

    public Replay(File replayFile) {
        this.file = replayFile.getPath();
        this.replayFile = getFile();
    }

    /**
     * Return replay file if it exists and is replay
     *
     * @return
     */
    public Optional<File> getFile() {
        File replayFile = new File(file);
        if (replayFile.exists() && replayFile.isFile() && Files.getFileExtension(replayFile.getPath()).equals("rep")) {
            return Optional.of(replayFile);
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Replay replay = (Replay) o;

        return file.equals(replay.file);
    }

    @Override
    public int hashCode() {
        return file.hashCode();
    }
}
