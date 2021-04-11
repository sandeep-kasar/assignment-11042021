package com.sample.assignment.ui.result;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sample.assignment.R;
import com.sample.assignment.data.model.PostOffice;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {

    private List<PostOffice> postOfficeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, branchType, address, delStatus;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tvName);
            description = (TextView) view.findViewById(R.id.tvDesc);
            branchType = (TextView) view.findViewById(R.id.tvBranchType);
            address = (TextView) view.findViewById(R.id.tvAddress);
            delStatus = (TextView) view.findViewById(R.id.tvDelStatus);
        }
    }


    public ResultAdapter(List<PostOffice> dataList) {
        this.postOfficeList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PostOffice postOffice = postOfficeList.get(position);
        holder.name.setText(postOffice.getName());
        if (postOffice.getDescription() != "null"){
            holder.description.setText(postOffice.getDescription());
        }else {
            holder.description.setVisibility(View.GONE);
        }

        holder.branchType.setText(postOffice.getBranchType());
        holder.address.setText(
                postOffice.getCircle() + " " +
                postOffice.getDistrict() + " " +
                postOffice.getDivision() + " " +
                postOffice.getRegion() + " " +
                postOffice.getBlock() + " " +
                postOffice.getState() + " " +
                postOffice.getCountry() + " " +
                postOffice.getPincode()
                );
        holder.delStatus.setText(postOffice.getDeliveryStatus());

    }
    @Override
    public int getItemCount() {
        return postOfficeList.size();
    }
}
