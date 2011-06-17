package net.opgenorth.prdc11.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.jayway.android.robotium.solo.Solo;
import net.opgenorth.prdc11.MyActivity;
import net.opgenorth.prdc11.R;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MyActivityRobotiumTests extends ActivityInstrumentationTestCase2<MyActivity> {
    private Button _convertVolumeButton;
    private TextView _convertedResults;
    private EditText _inputVolumeEdit;
    private RadioButton _toMillilitresRadioButton;
    private RadioButton _toDramsRadioButton;
    private MyActivity _activity;
    private Solo _solo;

    public MyActivityRobotiumTests() {
        super("net.opgenorth.prdc11", MyActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _activity = this.getActivity();
        _solo = new Solo(getInstrumentation(), _activity);



        _convertedResults = (TextView) _activity.findViewById(R.id.convertedVolumeResult);
        _convertVolumeButton = (Button) _activity.findViewById(R.id.convertVolumeButton);
        _inputVolumeEdit = (EditText) _activity.findViewById(R.id.inputVolumeEdit);
        _toMillilitresRadioButton = (RadioButton) _activity.findViewById(R.id.radio_millilitres);
        _toDramsRadioButton = (RadioButton) _activity.findViewById(R.id.radio_drams);
    }

    @Test
    public void testShouldHaveToDramsRadioButtonCheckedOnStartup() {
        assertThat(_toMillilitresRadioButton.isChecked(), equalTo(false));
        assertThat(_toDramsRadioButton.isChecked(), equalTo(true));
        assertThat(_inputVolumeEdit.getHint().toString(), equalTo("Enter millilitres"));
    }

    @Test
    public void testShouldConvertMillilitresToDrams() throws Exception {
        _solo.clickOnRadioButton(0);
        _solo.enterText(_inputVolumeEdit, "30");
        _solo.clickOnButton("To drams");
        assertThat(_convertedResults.getText().toString(), equalTo("8.1 drams."));
    }

    @Test
    public void testShouldConvertDramsToMillilitres() throws Exception {
        _solo.clickOnRadioButton(1);
        _solo.enterText(_inputVolumeEdit, "8");
        _solo.clickOnButton("To millilitres");
        assertThat(_convertedResults.getText().toString(), equalTo("29.6 millilitres."));
    }

    @Override
    public void tearDown() throws Exception {

        try {
            _solo.finalize();
        } catch (Throwable e) {

            e.printStackTrace();
        }
        getActivity().finish();
        super.tearDown();

    }
}
