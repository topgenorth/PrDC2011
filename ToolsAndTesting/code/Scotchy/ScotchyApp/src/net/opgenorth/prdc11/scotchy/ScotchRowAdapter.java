package net.opgenorth.prdc11.scotchy;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import net.opgenorth.prdc11.intro.R;

public class ScotchRowAdapter {
    private View _base = null;
    private TextView _brandLabel;

    public ScotchRowAdapter(View base) {
        _base = base;
    }

    public TextView getBrandLabel() {
        if (_brandLabel == null) {
            _brandLabel = (TextView) _base.findViewById(R.id.scotch_brand);
        }
        return _brandLabel;
    }


    public void display(ScotchBrand scotch) {
        TextView brandLabel = getBrandLabel();
        brandLabel.setText(scotch.getBrand());
        final Uri uriToViewScotch = Uri.parse(scotch.getUrl());

        brandLabel.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {
                Intent intendToViewScotch = new Intent("android.intent.action.VIEW", uriToViewScotch);
            }
        });
    }
}

