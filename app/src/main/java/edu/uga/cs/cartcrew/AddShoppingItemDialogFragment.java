package edu.uga.cs.cartcrew;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
public class AddShoppingItemDialogFragment extends DialogFragment {
    private EditText itemNameEditText, itemQuantityEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_item, null);
        itemNameEditText = view.findViewById(R.id.editTextItemName);
        itemQuantityEditText = view.findViewById(R.id.editTextQuantity);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Add Shopping Item")
                .setView(view)
                .setPositiveButton("Add", (dialog, which) -> addItem())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }

    private void addItem() {
        String name = itemNameEditText.getText().toString();
        String quantity = itemQuantityEditText.getText().toString();

        if (!name.isEmpty()) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("shoppingList");
            ref.push().setValue(new ShoppingItem(name, quantity)).addOnSuccessListener(aVoid ->
                    // this line crashes right now - i think it is because the dialog is being
                    // dismissed before the toast shows since the error says the dialog is
                    // no longer attached to an activity
                    Toast.makeText(requireActivity(), "Item added!", Toast.LENGTH_SHORT).show()
            ).addOnFailureListener(e ->
                    Toast.makeText(requireActivity(), "Failed to add item", Toast.LENGTH_SHORT).show()
            );
        } else {
            Toast.makeText(getActivity(), "Item name is required", Toast.LENGTH_SHORT).show();
        }
    }
}
 */

// A DialogFragment class to handle adding shopping items
public class AddShoppingItemDialogFragment extends DialogFragment {

    private EditText itemNameEditText, itemQuantityEditText;

    // Interface for communicating with the activity
    public interface AddShoppingItemDialogListener {
        void addShoppingItem(ShoppingItem shoppingItem);
    }

    private AddShoppingItemDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Ensure the parent activity implements the interface
        if (context instanceof AddShoppingItemDialogListener) {
            listener = (AddShoppingItemDialogListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement AddShoppingItemDialogFragment.AddShoppingItemDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the custom dialog layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_item, null);
        itemNameEditText = view.findViewById(R.id.editTextItemName);
        itemQuantityEditText = view.findViewById(R.id.editTextQuantity);

        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Add Shopping Item")
                .setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addItem();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    private void addItem() {
        String name = itemNameEditText.getText().toString();
        String quantity = itemQuantityEditText.getText().toString();

        if (!name.isEmpty()) {
            // Pass the new shopping item to the parent activity
            ShoppingItem newItem = new ShoppingItem(name, quantity);
            listener.addShoppingItem(newItem);
        } else {
            // Show a Toast if the item name is empty
            Toast.makeText(getContext(), "Item name is required", Toast.LENGTH_SHORT).show();
        }
    }
}
