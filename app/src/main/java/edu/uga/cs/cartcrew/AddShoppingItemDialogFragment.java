package edu.uga.cs.cartcrew;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                    Toast.makeText(getActivity(), "Item added!", Toast.LENGTH_SHORT).show()
            ).addOnFailureListener(e ->
                    Toast.makeText(getActivity(), "Failed to add item", Toast.LENGTH_SHORT).show()
            );
        } else {
            Toast.makeText(getActivity(), "Item name is required", Toast.LENGTH_SHORT).show();
        }
    }
}