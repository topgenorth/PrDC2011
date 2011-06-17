package net.opgenorth.prdc11.roboelectric;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import net.opgenorth.prdc11.testing.roboelectric.R;

import java.text.DecimalFormat;

public class MyActivity extends Activity {
    /**
     * Conversion factor for drams -> millilitres
     */
    private static final double DRAM_TO_MILLILITER = 3.6967162;
    /**
     * Conversion factor for millilitres -> drams
     */
    private static final double MILLILITER_TO_DRAM = 0.270510351863;

    private static final DecimalFormat FORMATTER = new DecimalFormat("###.#");
    private TextView _convertedResultsText;
    private EditText _inputVolumeEdit;
    private Button _convertVolumeButton;
    private boolean _convertToDramsFlag = true;


    private View.OnClickListener _changeUnitsListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            RadioButton radiobutton = (RadioButton) view;
            _inputVolumeEdit.setText("");
            _convertedResultsText.setText("");
            if (radiobutton.getText().equals("To Drams")) {
                _convertVolumeButton.setText("To drams");
                _inputVolumeEdit.setHint("Enter millilitres");
                _convertToDramsFlag = true;
            } else {
                _inputVolumeEdit.setHint("Enter drams");
                _convertVolumeButton.setText("To millilitres");
                _convertToDramsFlag = false;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        _convertVolumeButton = (Button) findViewById(R.id.convertVolumeButton);
        _convertedResultsText = (TextView) findViewById(R.id.convertedVolumeResult);
        _inputVolumeEdit = (EditText) findViewById(R.id.inputVolumeEdit);

        final RadioButton dramRadioButton = (RadioButton) findViewById(R.id.radio_drams);
        final RadioButton millilitersRadioButton = (RadioButton) findViewById(R.id.radio_millilitres);
        dramRadioButton.setOnClickListener(_changeUnitsListener);
        millilitersRadioButton.setOnClickListener(_changeUnitsListener);

        _convertVolumeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                double oldVolume = Double.valueOf(_inputVolumeEdit.getText().toString());
                String convertedResult;
                double newVolume;
                if (_convertToDramsFlag) {
                    newVolume = oldVolume * MILLILITER_TO_DRAM;
                    convertedResult = FORMATTER.format(newVolume) + " drams.";
                } else {
                    newVolume = oldVolume * DRAM_TO_MILLILITER;
                    convertedResult = FORMATTER.format(newVolume) + " millilitres.";
                }
                _convertedResultsText.setText(convertedResult);
            }
        });
    }
}
