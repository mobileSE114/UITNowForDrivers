package com.uit.uitnowfordrivers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder>{
    private ArrayList<Request> mRequests;
    private RequestListener mListener;
    public interface RequestListener {
        void onAcceptRequest(Request request);
    }
    public RequestAdapter(ArrayList<Request> list,RequestListener listener)
    {
        this.mRequests=list;
        this.mListener=listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_request, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Request r=mRequests.get(position);
        holder.tvId.setText(r.getId());
        holder.tvStoreAddress.setText(r.getStoreAddress());
        holder.tvStoreName.setText(r.getStoreName());
        holder.tvCusAddress.setText(r.getUserAddress());
        holder.tvCusName.setText(r.getUserName());
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAcceptRequest(r);
            }
        });
        holder.tvTotalPrices.setText(r.getTotal());
    }

    @Override
    public int getItemCount() {
        return mRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvStoreAddress,tvStoreName,tvTotalPrices,tvCusAddress,tvCusName;
        Button btnAccept;
        public ViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvOrderId) ;
            tvStoreAddress = itemView.findViewById(R.id.tvStoreAddress);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            tvTotalPrices = itemView.findViewById(R.id.tvTotalPrices);
            tvCusAddress=itemView.findViewById(R.id.tvCusAddress);
            tvCusName = itemView.findViewById(R.id.tvCusName);
            btnAccept=itemView.findViewById(R.id.btnAccept);
        }
    }
}
