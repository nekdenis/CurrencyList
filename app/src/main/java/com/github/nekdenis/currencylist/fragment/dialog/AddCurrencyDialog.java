package com.github.nekdenis.currencylist.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.nekdenis.currencylist.R;

public class AddCurrencyDialog extends DialogFragment {

    private Button addButton;
    private Spinner currencySpinner;
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
        currencySpinner = (Spinner) rootView.findViewById(R.id.add_currency_spinner);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.currencies, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(spinnerAdapter);
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
        String selectedCurrency = spinnerAdapter.getItem(currencySpinner.getSelectedItemPosition()).toString();
        if (onDialogClickListener != null) {
            onDialogClickListener.onAddClick(selectedCurrency, getDialog());
        }
    }

    public interface OnDialogClickListener {
        void onAddClick(String name, Dialog dialog);
    }

}
