package com.example.vladislav.ccash.DebtCardView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.vladislav.ccash.Frontend.InvestItem;
import com.example.vladislav.ccash.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InvestCardAdapter extends RecyclerView.Adapter<InvestCardAdapter.InvestViewHolder>
{
    private ArrayList<InvestItem> mInvestList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onShowInfo(int position);
        void onShareQR(int position);
    }

    public static class InvestViewHolder extends RecyclerView.ViewHolder {
        TextView InvestName;
        TextView InvestSum;
        TextView InvestMyDebt;
        TextView InvestDesc;
        TextView Debtors;

        Button CheckMore, ShareQR;

        InvestViewHolder(View itemView, final OnItemClickListener listener)
        {
            super(itemView);

            InvestName = (TextView) itemView.findViewById(R.id.InvestNameVIew);
            InvestDesc = (TextView) itemView.findViewById(R.id.InvestDescView);
            InvestSum  = (TextView) itemView.findViewById(R.id.TotalSumView);
            InvestMyDebt = (TextView) itemView.findViewById(R.id.MyDebtView);
            Debtors    = (TextView) itemView.findViewById(R.id.InvestDebtorsView);

            CheckMore  = (Button) itemView.findViewById(R.id.btnCheckMore);
            ShareQR    = (Button) itemView.findViewById(R.id.btnShreQR);

            CheckMore.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onShowInfo(position);
                        }
                    }
                }
            });

            ShareQR.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onShareQR(position);
                        }
                    }
                }
            });


        }

    }

    public InvestCardAdapter(ArrayList<InvestItem> items)
    {
        this.mInvestList = items;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public InvestViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.invest_cardview, parent, false);
        return new InvestViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull InvestViewHolder holder, int position)
    {
        InvestItem currentItem = mInvestList.get(position);

        holder.InvestDesc.setText(currentItem.getInvestDescription());
        holder.InvestName.setText(currentItem.getInvestName());
        holder.InvestSum.setText(String.valueOf(currentItem.getInvestSum()));
        holder.InvestMyDebt.setText(String.valueOf(currentItem.getInvestMyDebts()));

        StringBuilder finalDebtors = new StringBuilder();
        finalDebtors.append(currentItem.getDebts().get(0).getDebtorName());
        for(int i = 1; i < currentItem.getDebts().size(); i++)
        {
            finalDebtors.append(", ");
            finalDebtors.append(currentItem.getDebts().get(i).getDebtorName());
        }

        holder.Debtors.setText(finalDebtors.toString());
    }

    @Override
    public int getItemCount()
    {
        return mInvestList.size();
    }
}
