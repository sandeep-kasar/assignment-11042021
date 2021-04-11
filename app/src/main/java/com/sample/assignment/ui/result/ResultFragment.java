package com.sample.assignment.ui.result;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.assignment.R;
import com.sample.assignment.data.model.PostOffice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultFragment extends Fragment {

    private ResultViewModel resultViewModel;
    private RecyclerView recycler_view;
    private Button search;
    private EditText pincode;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ){
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ){
        // init recyclerview
        recycler_view = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);

        // init viewModelProvider
        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);

        // init views
        search = (Button)view.findViewById(R.id.btnSearch);
        pincode = (EditText)view.findViewById(R.id.edtPincode);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

    }

    @Override
    public void onResume() {
        super.onResume();
        search.setOnClickListener(v -> {
            String pin = pincode.getText().toString();
            if (pin.isEmpty()){
                Toast.makeText(getContext(), R.string.war_enter_pin,Toast.LENGTH_LONG).show();
            }else {
                hideKeyboard(v);
                getData(pin);
            }
        });
    }

    private void getData(String pincode){
        progressBar.setVisibility(View.VISIBLE);
        resultViewModel.getPostOfficeResponseLiveData(pincode).observe(getViewLifecycleOwner(), response -> {
            try {
                JSONObject jsonObject = new JSONObject(response.get(0).toString());

                if (jsonObject.getString("Status").equals("Error")){
                    Toast.makeText(getContext(), jsonObject.getString("Message"),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    recycler_view.setVisibility(View.GONE);
                }else {
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("PostOffice"));
                    ArrayList<PostOffice> postOfficeArrayList = new ArrayList<PostOffice>();
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            // Log.e("xxx",jsonArray.get(i).toString());
                            JSONObject ele = jsonArray.getJSONObject(i);
                            PostOffice postOffice = new PostOffice();
                            postOffice.setName(ele.get("Name").toString());
                            postOffice.setDescription(ele.get("Description").toString());
                            postOffice.setBranchType(ele.get("BranchType").toString());
                            postOffice.setDeliveryStatus(ele.get("DeliveryStatus").toString());
                            postOffice.setCircle(ele.get("Circle").toString());
                            postOffice.setDistrict(ele.get("District").toString());
                            postOffice.setDivision(ele.get("Division").toString());
                            postOffice.setRegion(ele.get("Region").toString());
                            postOffice.setBlock(ele.get("Block").toString());
                            postOffice.setState(ele.get("State").toString());
                            postOffice.setCountry(ele.get("Country").toString());
                            postOffice.setPincode(ele.get("Pincode").toString());
                            postOfficeArrayList.add(postOffice);
                        }
                    }

                    ResultAdapter resultAdapter = new ResultAdapter(postOfficeArrayList);
                    recycler_view.setAdapter(resultAdapter);
                    progressBar.setVisibility(View.GONE);
                    recycler_view.setVisibility(View.VISIBLE);
                }
            }catch (JSONException err){
                Log.e("Error", err.toString());
            }

        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}