package com.pkothari.calc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class CalcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText in_fv = (EditText) findViewById(R.id.in_fv);
        final EditText in_pv = (EditText) findViewById(R.id.in_pv);
        final EditText in_period = (EditText) findViewById(R.id.in_period);
        final EditText in_rate = (EditText) findViewById(R.id.in_rate);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calc.FIELD missing = calcMissing(in_fv.getText(), in_pv.getText(), in_period.getText(), in_rate.getText());
                String out = missing == Calc.FIELD.ERROR ? "Enter exactly 3 fields" : "Computed answer for " + missing.toString();
                Snackbar.make(view, out, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton clear = (FloatingActionButton) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                in_fv.getText().clear();
                in_pv.getText().clear();
                in_period.getText().clear();
                in_rate.getText().clear();
            }
        });
    }

    private Calc.FIELD calcMissing(Editable fv, Editable pv, Editable period, Editable rate) {
        Calc c = new Calc();
        if (fv.length() > 0) c.futureValue(Integer.parseInt(fv.toString()));
        if (pv.length() > 0) c.presentValue(Integer.parseInt(pv.toString()));
        if (period.length() > 0) c.period(Integer.parseInt(period.toString()));
        if (rate.length() > 0) c.rate(Double.parseDouble(rate.toString())/100);

        Calc.FIELD computed = c.go();
        switch (computed) {
            case FUTURE_VALUE:
                fv.append(c.futureValue.toString());
                break;
            case PRESENT_VALUE:
                pv.append(c.presentValue.toString());
                break;
            case PERIODS:
                period.append(c.periods.toString());
                break;
            case RATE:
                rate.append(c.getRateForDisplay().toString());
                break;
            case ERROR:
                break;
        }
        return computed;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
