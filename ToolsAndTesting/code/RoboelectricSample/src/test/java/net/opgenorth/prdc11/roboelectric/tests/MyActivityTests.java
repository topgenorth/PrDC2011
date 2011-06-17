package net.opgenorth.prdc11.roboelectric.tests;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import junit.framework.Assert;
import net.opgenorth.prdc11.roboelectric.MyActivity;
import net.opgenorth.prdc11.testing.roboelectric.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class MyActivityTests {

    private MyActivity _activity;
    private Button _convertVolumeButton;
    private TextView _convertedResults;
    private EditText _inputVolumeEdit;
    private RadioButton _toMillilitresRadioButton;
    private RadioButton _toDramsRadioButton;

    @Before
    public void setup() throws Exception {
        _activity = new MyActivity();
        _activity.onCreate(null);

        _convertedResults = (TextView) _activity.findViewById(R.id.convertedVolumeResult);
        _convertVolumeButton = (Button) _activity.findViewById(R.id.convertVolumeButton);
        _inputVolumeEdit = (EditText) _activity.findViewById(R.id.inputVolumeEdit);
        _toMillilitresRadioButton = (RadioButton) _activity.findViewById(R.id.radio_millilitres);
        _toDramsRadioButton = (RadioButton) _activity.findViewById(R.id.radio_drams);
    }

    @Test
    public void shouldHaveToDramsRadioButtonCheckedOnStartup() {
        assertThat(_toMillilitresRadioButton.isChecked(), equalTo(false));
        assertThat(_toDramsRadioButton.isChecked(), equalTo(true));
    }

    @Test
    public void shouldConvertMillilitresToDrams() throws Exception {
        _toDramsRadioButton.performClick();
        _inputVolumeEdit.setText("30");
        _convertVolumeButton.performClick();

        assertThat(_convertedResults.getText().toString(), equalTo("8.1 drams."));
    }
    @Test
    public void shouldConvertDramsToMillilitres() throws Exception {
        _toMillilitresRadioButton.performClick()  ;
        _inputVolumeEdit.setText("8");
        _convertVolumeButton.performClick();

        assertThat(_convertedResults.getText().toString(), equalTo("29.6 millilitres."));
    }
}
