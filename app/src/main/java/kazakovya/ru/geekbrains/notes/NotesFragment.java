package kazakovya.ru.geekbrains.notes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Objects;

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
        initList();
        RecyclerView recyclerView = view.findViewById(R.id.notes_recycler_view);
        initRecyclerView(recyclerView, notes);
    }

    private void initList() {
        notes = new Note[]{
                new Note(getString(R.string.first_note_title), getString(R.string.first_note_content), Calendar.getInstance()),
                new Note(getString(R.string.second_note_title), getString(R.string.second_note_content), Calendar.getInstance()),
                new Note(getString(R.string.third_note_title), getString(R.string.third_note_content), Calendar.getInstance()),
                new Note(getString(R.string.fourth_note_title), getString(R.string.fourth_note_content), Calendar.getInstance()),
                new Note(getString(R.string.fifth_note_title), getString(R.string.fifth_note_content), Calendar.getInstance()),
                new Note(getString(R.string.sixth_note_title), getString(R.string.sixth_note_content), Calendar.getInstance()),
                new Note(getString(R.string.seventh_note_title), getString(R.string.seventh_note_content), Calendar.getInstance()),
                new Note(getString(R.string.eighth_note_title), getString(R.string.eighth_note_content), Calendar.getInstance()),
                new Note(getString(R.string.ninth_note_title), getString(R.string.ninth_note_content), Calendar.getInstance())
        };
    }

    private void initRecyclerView(RecyclerView recyclerView, Note[] notes) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        Adapter adapter = new Adapter(notes);
        adapter.setOnItemClickListener((position, note) -> {
            currentNote = note;
            showNote(currentNote);
        });
        recyclerView.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(Objects.requireNonNull(getContext()), //декоратор
                LinearLayoutManager.VERTICAL);
        decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.decorator)));
        recyclerView.addItemDecoration(decoration);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ContentFragment.ARG_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLandscape = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(ContentFragment.ARG_NOTE);
        } else {
            currentNote = notes[0];
        }
        if (isLandscape) {
            showLandForNotes(currentNote);
        }
    }

    private void showNote(Note currentNote) {
        if (isLandscape) {
            showLandForNotes(currentNote);
        } else {
            showPortForNotes(currentNote);
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
