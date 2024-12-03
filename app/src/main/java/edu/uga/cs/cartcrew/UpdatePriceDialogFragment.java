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

// A DialogFragment class to handle changing the price of a purchase
public class UpdatePriceDialogFragment extends DialogFragment {

    private EditText itemNameEditText, itemQuantityEditText;

    private int position;
    private String key;
    private double originalPrice;

    // Interface for communicating with the activity
    public interface UpdatePriceDialogFragmentListener {
        void updatePrice(String key, int position, double newPrice);
    }

    private UpdatePriceDialogFragmentListener listener;

    // Static method to create a new instance of this dialog
    public static UpdatePriceDialogFragment newInstance(int position, String key, double originalPrice) {
        UpdatePriceDialogFragment dialog = new UpdatePriceDialogFragment();

        // Pass item details as arguments
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("key", key);
        args.putDouble("price", originalPrice);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Ensure the parent activity implements the interface
        if (context instanceof UpdatePriceDialogFragmentListener) {
            listener = (UpdatePriceDialogFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement AddShoppingItemDialogFragment.AddShoppingItemDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Retrieve arguments
        position = getArguments().getInt("position");
        key = getArguments().getString("key");
        originalPrice = getArguments().getDouble("originalPrice");

        // Inflate the custom dialog layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_item, null);
        itemNameEditText = view.findViewById(R.id.editTextItemName);
        itemQuantityEditText = view.findViewById(R.id.editTextQuantity);
        ((ViewGroup) itemQuantityEditText.getParent()).removeView(itemQuantityEditText);
        itemNameEditText.setHint("New price");

        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Update Price")
                .setView(view)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updatePrice();
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

    private void updatePrice() {
        String price = itemNameEditText.getText().toString();

        if (!price.isEmpty()) {
            try {
                double parsedPrice = Double.parseDouble(price);
                listener.updatePrice(key, position, parsedPrice);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Price could not be parsed", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Show a Toast if the item name is empty
            Toast.makeText(getContext(), "Price is required", Toast.LENGTH_SHORT).show();
        }
    }
}
