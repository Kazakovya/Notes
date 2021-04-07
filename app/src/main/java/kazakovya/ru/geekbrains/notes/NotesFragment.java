package kazakovya.ru.geekbrains.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;

import ru.geekbrains.notes.R;

public class NotesFragment extends Fragment {

    private static final int MY_DEFAULT_DURATION = 1000;
    private NotesSourceInterface data;
    private Adapter adapter;
    private RecyclerView recyclerView;
    private Navigation navigation;
    private Publisher publisher;
    private boolean moveToFirstPosition;

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_notes, container, false);
        recyclerView = view.findViewById(R.id.notes_recycler_view);
        data = new NotesSourceFirebase().init(notesData -> adapter.notifyDataSetChanged());
        initRecyclerView(recyclerView, data);
        setHasOptionsMenu(true);
        adapter.setDataSource(data);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    private void initRecyclerView(RecyclerView recyclerView, NotesSourceInterface data) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Adapter(this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration
                (requireContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull
                (ContextCompat.getDrawable(requireContext(), R.drawable.decorator)));
        recyclerView.addItemDecoration(itemDecoration);

        if (moveToFirstPosition && data.size() > 0) {
            recyclerView.scrollToPosition(0);
            moveToFirstPosition = false;
        }
        adapter.setOnItemClickListener((position, note) -> {
            navigation.addFragment(NoteFragment.newInstance(data.getNote(position)),
                    true);
        });


        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getMenuPosition();
        if (item.getItemId() == R.id.menu_delete_note) {
            data.deleteNote(position);
            adapter.notifyItemRemoved(position);
            return true;

        } else if (item.getItemId() == R.id.menu_edit_note) {
            navigation.addFragment(NoteFragment.newInstance(data.getNote(position)), true);
            publisher.subscribe(note1 -> {
                data.changeNote(position, note1);
                adapter.notifyItemChanged(position);
            });
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem search = menu.findItem(R.id.menu_search);
        MenuItem sort = menu.findItem(R.id.menu_sort);
        MenuItem addNote = menu.findItem(R.id.menu_add_note);
        MenuItem send = menu.findItem(R.id.menu_send);
        MenuItem addPhoto = menu.findItem(R.id.menu_add_photo);
        search.setVisible(true);
        sort.setVisible(true);
        send.setVisible(false);
        addPhoto.setVisible(false);
        addNote.setOnMenuItemClickListener(item -> {
            navigation.addFragment(NoteFragment.newInstance(), true);
            publisher.subscribe(note -> {
                data.addNote(note);
                adapter.notifyItemInserted(data.size() - 1);
                moveToFirstPosition = true;
            });
            return true;
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}