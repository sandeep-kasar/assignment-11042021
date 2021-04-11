package com.sample.assignment.ui.adhaar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sample.assignment.R;
import com.sample.assignment.utils.RawData;
import com.sample.assignment.utils.VerhoeffAlgorithm;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdhaarFragment extends Fragment {

    EditText editTextInput;

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ){
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ){
        super.onViewCreated(view, savedInstanceState);

        TextView sampleOne = (TextView)view.findViewById(R.id.tvOne);
        TextView sampleTwo = (TextView)view.findViewById(R.id.tvTwo);
        TextView sampleThree = (TextView)view.findViewById(R.id.tvThree);
        TextView sampleFour = (TextView)view.findViewById(R.id.tvFour);
        editTextInput = (EditText) view.findViewById(R.id.edtSample);
        Button processData = (Button) view.findViewById(R.id.btnProcess);

        sampleOne.setOnClickListener(v ->{
            editTextInput.setText(RawData.INSTANCE.getSample_1());
        });

        sampleTwo.setOnClickListener(v ->{
            editTextInput.setText(RawData.INSTANCE.getSammple_2());
        });

        sampleThree.setOnClickListener(v ->{
            editTextInput.setText(RawData.INSTANCE.getSample_3());
        });

        sampleFour.setOnClickListener(v ->{
            editTextInput.setText(RawData.INSTANCE.getSample_4());
        });

        processData.setOnClickListener(v->{
            findAdhharNumber(view);
        });

    }

    private void findAdhharNumber(View view)
    {

        TextView tvAdhaarNum = (TextView)view.findViewById(R.id.tvAdhaarNumber);
        ArrayList<String> adhaarNumbers = new ArrayList<>();
        String sampleData = editTextInput.getText().toString().trim();

        if (sampleData.equals("")){
            Toast.makeText(getContext(),"Please select sample first", Toast.LENGTH_SHORT).show();
        }else {

            // process text data
            String sample = sampleData.replaceAll("I","1");
            String samplenw = sample.replaceAll("B","3");
            String samplenew = samplenw.replaceAll("Z","2");

            String newOne = "";
            String regex ="(\\s\\d{6}\\s)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(samplenew);
            while (matcher.find()){
                newOne = samplenew.replaceAll(matcher.group(),"");
            }

            String regex1 ="(\\d{4}\\s\\d{4}\\s\\d{4})";
            Pattern pattern1 = Pattern.compile(regex1);
            Matcher matcher1 = pattern1.matcher(newOne);
            while (matcher1.find()){
                Log.e("TAgX",matcher1.group());
                if (!adhaarNumbers.contains(matcher1.group())){
                    adhaarNumbers.add(matcher1.group());
                }
            }
        }

        // display adhaar number
        tvAdhaarNum.setText("Adhaar Number List : \n");
        for (int j = 0; j < adhaarNumbers.size(); j++){
            if (isValidAadharNumber(adhaarNumbers.get(j))){
                tvAdhaarNum.append(adhaarNumbers.get(j) + ",\n");
            }
        }
    }

    public static boolean isValidAadharNumber(String str)
    {
        String regex = "^[2-9]{1}[0-9]{3}\\s[0-9]{4}\\s[0-9]{4}$";
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean validateAadharWithVerhoeffAlgorithm(String aadharNumber)
    {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if(isValidAadhar){
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }
}