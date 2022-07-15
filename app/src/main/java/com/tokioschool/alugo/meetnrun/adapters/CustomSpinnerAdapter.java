package com.tokioschool.alugo.meetnrun.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tokioschool.alugo.meetnrun.R;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    public CustomSpinnerAdapter(Context context, List<String> items) {
        super(context, R.layout.simple_spinner_item, items);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(true);
        }
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(false);
        }
        return getCustomView(position, convertView, parent);
    }


    @Override
    public int getCount() {
        return super.getCount() + 1; // Adjust for initial selection item
    }

    private View initialSelection(boolean dropdown) {
        // Just an example using a simple TextView. Create whatever default view
        // to suit your needs, inflating a separate layout if it's cleaner.
        TextView view = new TextView(getContext());
        view.setText("select one");
        //int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.);
        //view.setPadding(0, spacing, 0, spacing);

        if (dropdown) { // Hidden when the dropdown is opened
            view.setHeight(0);
        }

        return view;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        // Distinguish "real" spinner items (that can be reused) from initial selection item
        View row = convertView != null && !(convertView instanceof TextView)
                ? convertView :
                LayoutInflater.from(getContext()).inflate(R.layout.simple_spinner_item, parent, false);

        position = position - 1; // Adjust for initial selection item
        String item = getItem(position);

        TextView valueView = (TextView) row.findViewById(R.id.spinner_item_value);
        valueView.setText(item);
        // ... Resolve views & populate with data ...
        return row;
    }

}