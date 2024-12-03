package edu.uga.cs.cartcrew;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


// A DialogFragment class to handle adding shopping items
public class FinalizePurchaseDialogFragment extends DialogFragment {

    private EditText itemNameEditText, itemQuantityEditText;

    private AddShoppingItemDialogFragment.AddShoppingItemDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Ensure the parent activity implements the interface
        if (context instanceof AddShoppingItemDialogFragment.AddShoppingItemDialogListener) {
            listener = (AddShoppingItemDialogFragment.AddShoppingItemDialogListener) context;
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
        ((ViewGroup) itemQuantityEditText.getParent()).removeView(itemQuantityEditText);
        itemNameEditText.setHint("Total price of the purchase");

        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Finalize Purchase")
                .setView(view)
                .setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
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
        String price = itemNameEditText.getText().toString();

        if (!price.isEmpty()) {
            try {
                double parsedPrice = Double.parseDouble(price);
                listener.finalizePurchase(parsedPrice);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Price could not be parsed", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Show a Toast if the item name is empty
            Toast.makeText(getContext(), "Price is required", Toast.LENGTH_SHORT).show();
        }
    }
}