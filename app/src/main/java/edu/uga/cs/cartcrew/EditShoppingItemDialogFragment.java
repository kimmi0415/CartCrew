package edu.uga.cs.cartcrew;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


// A DialogFragment to handle edits to a ShoppingItem
public class EditShoppingItemDialogFragment extends DialogFragment {

    public static final int SAVE = 1;   // Save (update) the shopping item
    public static final int DELETE = 2; // Delete the shopping item

    private EditText itemNameEditText;
    private EditText itemQuantityEditText;

    private int position;
    private String key;
    private String name;
    private String quantity;

    // Interface for communication with parent activity
    public interface EditShoppingItemDialogListener {
        void updateShoppingItem(int position, ShoppingItem shoppingItem, int action, boolean showToast);
    }

    private EditShoppingItemDialogListener listener;

    // Static method to create a new instance of this dialog
    public static EditShoppingItemDialogFragment newInstance(int position, String key, String name, String quantity) {
        EditShoppingItemDialogFragment dialog = new EditShoppingItemDialogFragment();

        // Pass item details as arguments
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("key", key);
        args.putString("name", name);
        args.putString("quantity", quantity);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof EditShoppingItemDialogListener) {
            listener = (EditShoppingItemDialogListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement EditShoppingItemDialogFragment.EditShoppingItemDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Retrieve arguments
        position = getArguments().getInt("position");
        key = getArguments().getString("key");
        name = getArguments().getString("name");
        quantity = getArguments().getString("quantity");

        // Inflate the layout
        //LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //final View layout = inflater.inflate( R.layout.dialog_add_item, getActivity().findViewById( R.id.root ) );
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_item, null);
        itemNameEditText = view.findViewById(R.id.editTextItemName);
        itemQuantityEditText = view.findViewById(R.id.editTextQuantity);

        // Pre-fill the fields with the current values
        itemNameEditText.setText(name);
        itemQuantityEditText.setText(quantity);

        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Edit Shopping Item")
                .setView(view)
                .setPositiveButton("SAVE", new SaveButtonClickListener())
                .setNeutralButton("DELETE", new DeleteButtonClickListener())
                .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }

    private class SaveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String updatedName = itemNameEditText.getText().toString();
            String updatedQuantity = itemQuantityEditText.getText().toString();

            if (!updatedName.isEmpty()) {
                ShoppingItem updatedItem = new ShoppingItem(updatedName, updatedQuantity);
                updatedItem.setKey(key);
                listener.updateShoppingItem(position, updatedItem, SAVE, true);
            }
        }
    }

    private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            ShoppingItem itemToDelete = new ShoppingItem(name, quantity);
            itemToDelete.setKey(key);
            listener.updateShoppingItem(position, itemToDelete, DELETE, true);
        }
    }
}
