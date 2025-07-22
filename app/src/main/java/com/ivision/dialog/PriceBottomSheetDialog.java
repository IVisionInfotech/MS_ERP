package com.ivision.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ivision.R;

public class PriceBottomSheetDialog extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private Context context;
    private EditText etPrice;
    private Button btnSubmit, btnCancel;
    private ItemClickListener itemClickListener;
    private int position;
    private String price = "";

    public static PriceBottomSheetDialog newInstance() {
        return new PriceBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_price, container, false);
        this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        context = getActivity();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey("position")) {
                position = getArguments().getInt("position");
            }
            if (getArguments().containsKey("price")) {
                price = getArguments().getString("price");
            }
        }

        init(view);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        return new BottomSheetDialog(requireContext(), R.style.BottomSheetDialog); // To have transparent dialog window background.
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            itemClickListener = (ItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
//            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        itemClickListener = null;
    }

    private void init(View view) {

        etPrice = view.findViewById(R.id.etPrice);
        etPrice.setText(price);

        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnCancel = view.findViewById(R.id.btnCancel);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPrice.getText().toString().isEmpty()) {
                    etPrice.setError("Enter price");
                } else {
                    itemClickListener.onItemClick(position, etPrice.getText().toString());
                    dismiss();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface ItemClickListener {
        void onItemClick(int position, String model);
    }
}
