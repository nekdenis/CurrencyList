package com.github.nekdenis.currencylist.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.nekdenis.currencylist.R;

public class AddCurrencyDialog extends DialogFragment {

    private Button addButton;
    private Spinner currencyFromSpinner;
    private Spinner currencyToSpinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    private OnDialogClickListener onDialogClickListener;

    public static AddCurrencyDialog newInstance(OnDialogClickListener onDialogClickListener) {
        AddCurrencyDialog fragment = new AddCurrencyDialog();
        fragment.setOnDialogClickListener(onDialogClickListener);
        return fragment;
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_add_currency, container, true);
        addButton = (Button) rootView.findViewById(R.id.add_currency_button);
        currencyFromSpinner = (Spinner) rootView.findViewById(R.id.add_currency_from_spinner);
        currencyToSpinner = (Spinner) rootView.findViewById(R.id.add_currency_to_spinner);
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.add_currency_title);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.currencies, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyFromSpinner.setAdapter(spinnerAdapter);
        currencyToSpinner.setAdapter(spinnerAdapter);
        initListeners();
    }

    private void initListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmClick();
            }
        });
    }


    private void onConfirmClick() {
        String selectedCurrencyFrom = spinnerAdapter.getItem(currencyFromSpinner.getSelectedItemPosition()).toString();
        String selectedCurrencyTo = spinnerAdapter.getItem(currencyToSpinner.getSelectedItemPosition()).toString();
        if (onDialogClickListener != null) {
            onDialogClickListener.onAddClick(selectedCurrencyFrom, selectedCurrencyTo, getDialog());
        }
    }

    public interface OnDialogClickListener {
        void onAddClick(String from, String to, Dialog dialog);
    }

}
