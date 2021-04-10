package kazakovya.ru.geekbrains.notes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.button.MaterialButton;

public class DeleteDialogFragment extends DialogFragment {

    private OnClickDeleteListener deleteDlgListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View contentView = requireActivity().getLayoutInflater().
                inflate(R.layout.fragment_delete_dialog, null);
        MaterialButton confirmDelete = contentView.findViewById(R.id.delete_button);
        confirmDelete.setOnClickListener(v -> {
            if (deleteDlgListener != null) {
                deleteDlgListener.onDelete();
                dismiss();
            }
        });
        MaterialButton deleteCancel = contentView.findViewById(R.id.no_delete_button);
        deleteCancel.setOnClickListener(v -> {
            if (deleteDlgListener != null) {
                deleteDlgListener.onCancelDelete();
                dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                .setView(contentView);
        return builder.create();
    }

    public void setOnDialogListener(OnClickDeleteListener deleteDlgListener) {
        this.deleteDlgListener = deleteDlgListener;
    }
}