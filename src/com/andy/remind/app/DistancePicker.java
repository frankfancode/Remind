package com.andy.remind.app;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

public class DistancePicker extends DialogPreference {

	
	int NUMBER_OF_VALUES = 20; // num of values in the picker
	int PICKER_RANGE = 100;
	String[] displayedValues = new String[NUMBER_OF_VALUES];
	String choosenValue = "";
	private NumberPicker numPicker;
	
	public DistancePicker(Context context, AttributeSet attrs) {
		super(context, attrs);

		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
	}

	@Override
	protected View onCreateDialogView() {
		numPicker = new NumberPicker(getContext());

		// numPicker.setMaxValue(10);
		// numPicker.setMinValue(0);
		// numPicker.setValue(5);

		for (int i = 0; i < NUMBER_OF_VALUES; i++)
			displayedValues[i] = String.valueOf(PICKER_RANGE * (i + 1));

		numPicker.setMinValue(0);
		numPicker.setMaxValue(displayedValues.length - 1);
		numPicker.setDisplayedValues(displayedValues);
		return (numPicker);
	}

	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);

		// numPicker.setValue(11);

		// To set a new value (let's say 150)
		for (int i = 0; i < displayedValues.length; i++)
			if (displayedValues[i].equals("500"))
				numPicker.setValue(i);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {

			// To get the current value in the picker
			choosenValue = displayedValues[numPicker.getValue()];
			Toast.makeText(DistancePicker.this.getContext(), choosenValue,
					Toast.LENGTH_SHORT).show();

		}
	}

}
