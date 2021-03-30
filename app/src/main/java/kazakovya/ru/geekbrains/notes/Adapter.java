package kazakovya.ru.geekbrains.notes;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Locale;



public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private Note[] notes;
    private MyClickListener myClickListener;

    public Adapter(Note[] notes) {
        this.notes = notes;
    }

    public void setOnItemClickListener(MyClickListener itemClickListener) {
        myClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        holder.getTitleTextView().setText(notes[position].getTitle());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",
                Locale.getDefault());
        holder.getDateTextView().setText(formatter.format(notes[position].getCreationDate().getTime()));
    }

    @Override
    public int getItemCount() {
        return notes.length;
    }

    public interface MyClickListener {
        void onItemClick(int position, Note note);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final LinearLayout itemLayout;
        private final TextView titleTextView;
        private final TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            itemLayout = itemView.findViewById(R.id.element_of_recycler_view);
            titleTextView = itemView.findViewById(R.id.first_textView);
            titleTextView.setTextColor(Color.BLACK);
            dateTextView = itemView.findViewById(R.id.second_textView);
            dateTextView.setTextColor(Color.BLACK);
            itemLayout.setOnClickListener(v -> {
                int position = getAdapterPosition();
                myClickListener.onItemClick(position, notes[position]);
            });
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }
    }
}