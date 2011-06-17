package net.opgenorth.prdc11.scotchy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import net.opgenorth.prdc11.intro.R;

import java.util.ArrayList;
import java.util.List;

public class ScotchListAdapter extends ArrayAdapter {
    private Activity _context;
    private ArrayList<ScotchBrand> _scotches;

    public ScotchListAdapter(Activity context, List<ScotchBrand> scotchList) {
        super(context, R.layout.row, scotchList);
        _context = context;
        _scotches = new ArrayList<ScotchBrand>(scotchList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ScotchRowAdapter rowWrapper;

        if (row == null) {
            LayoutInflater inflater = _context.getLayoutInflater();
            row = inflater.inflate(R.layout.row, null);
            rowWrapper = new ScotchRowAdapter(row);
            row.setTag(rowWrapper);
        } else {
            rowWrapper = (ScotchRowAdapter) row.getTag();
        }

        if (!_scotches.isEmpty()) {
            ScotchBrand scotch = _scotches.get(position);
            rowWrapper.display(scotch);
        }

        return (row);
    }
}
