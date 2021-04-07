package kazakovya.ru.geekbrains.notes;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import ru.geekbrains.notes.R;

public class NotesSource implements Parcelable {
    private ArrayList<Note> notes;
    private Resources resources;

    public static final Creator<NotesSource> CREATOR = new Creator<NotesSource>() {

        @Override
        public NotesSource createFromParcel(Parcel in) {
            return new NotesSource(in);
        }

        @Override
        public NotesSource[] newArray(int size) {
            return new NotesSource[size];
        }
    };

    public NotesSource(Resources resources) {
        this.resources = resources;
        notes = new ArrayList<>();
    }

    protected NotesSource(Parcel in) {
        notes = in.createTypedArrayList(Note.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(notes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public NotesSource init() {
        Note[] notesArray = new Note[]{
                new Note(resources.getString(R.string.first_note_title), resources.getString(R.string.first_note_content), getDateOfCreation()),
                new Note(resources.getString(R.string.second_note_title), resources.getString(R.string.second_note_content), getDateOfCreation()),
                new Note(resources.getString(R.string.third_note_title), resources.getString(R.string.third_note_content), getDateOfCreation()),
                new Note(resources.getString(R.string.fourth_note_title), resources.getString(R.string.fourth_note_content), getDateOfCreation()),
                new Note(resources.getString(R.string.fifth_note_title), resources.getString(R.string.fifth_note_content), getDateOfCreation()),
                new Note(resources.getString(R.string.sixth_note_title), resources.getString(R.string.sixth_note_content), getDateOfCreation()),
                new Note(resources.getString(R.string.seventh_note_title), resources.getString(R.string.seventh_note_content), getDateOfCreation()),
                new Note(resources.getString(R.string.eighth_note_title), resources.getString(R.string.eighth_note_content), getDateOfCreation()),
                new Note(resources.getString(R.string.ninth_note_title), resources.getString(R.string.ninth_note_content), getDateOfCreation())
        };
        Collections.addAll(notes, notesArray);
        return this;
    }

    public Note getNote(int position) {
        return notes.get(position);
    }

    public int size() {
        return notes.size();
    }

    public void deleteNote(int position) {
        notes.remove(position);
    }

    public void changeNote(int position, Note note) {
        notes.set(position, note);
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public String getDateOfCreation() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",
                Locale.getDefault());
        return String.format("%s: %s", "Дата создания",
                formatter.format(Calendar.getInstance().getTime()));
    }
}
