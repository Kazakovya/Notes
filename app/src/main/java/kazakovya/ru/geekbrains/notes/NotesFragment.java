package kazakovya.ru.geekbrains.notes;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.geekbrains.notes.R;

public class NotesFragment extends Fragment {

    private Note currentNote;
    private boolean isLandscape;
    private Note[] notes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    private void initList(View view) {
        notes = new Note[]{
                new Note(getString(R.string.first_note_title), getString(R.string.first_note_content), Calendar.getInstance()),
                new Note(getString(R.string.second_note_title), getString(R.string.second_note_content), Calendar.getInstance()),
                new Note(getString(R.string.third_note_title), getString(R.string.third_note_content), Calendar.getInstance()),
        };

        for (Note note : notes) {
            Context context = getContext();
            if (context != null) {
                LinearLayout linearView = (LinearLayout) view;
                TextView firstTextView = new TextView(context);
                TextView secondTextView = new TextView(context);
                firstTextView.setText(note.getTitle());
                firstTextView.setTextSize(25);
                firstTextView.setTextColor(Color.BLACK);
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
                secondTextView.setText(formatter.format(note.getCreationDate().getTime()));
                secondTextView.setTextColor(Color.BLACK);
                secondTextView.setTextSize(25);
                linearView.addView(firstTextView);
                linearView.addView(secondTextView);
                firstTextView.setPadding(0, 50, 0, 0);
                firstTextView.setOnClickListener(v -> initCurrentNote(note));
                secondTextView.setOnClickListener(v -> initCurrentNote(note));
            }
        }
    }

    private void initCurrentNote(Note note) {
        currentNote = note;
        showNote(note);
    }

    private void showNote(Note currentNote) {
        if (isLandscape) {
            showLandForNotes(currentNote);
        } else {
            showPortForNotes(currentNote);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ContentFragment.ARG_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(ContentFragment.ARG_NOTE);
        } else {
            currentNote = notes[0];
        }
        if (isLandscape) {
            showLandForNotes(currentNote);
        }
    }

    private void showLandForNotes(Note currentNote) {
        ContentFragment detail = ContentFragment.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.note_layout, detail);  // замена фрагмента
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void showPortForNotes(Note currentNote) {
        ContentFragment fragment = ContentFragment.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("list_fragment");
        fragmentTransaction.replace(R.id.list_of_notes_fragment_container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}