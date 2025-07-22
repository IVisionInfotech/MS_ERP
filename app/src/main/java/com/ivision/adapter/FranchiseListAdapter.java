package com.ivision.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.ivision.databinding.ProgressbarBinding;
import com.ivision.databinding.RecyclerviewCustomerListItemBinding;
import com.ivision.model.Franchise;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.Session;

import java.util.ArrayList;
import java.util.List;

public class FranchiseListAdapter extends RecyclerView.Adapter implements Filterable {

    final int VIEW_TYPE_ITEM = 0;
    final int VIEW_TYPE_LOADING = 1;
    private ClickListener listener, listener2, listener3;
    private Context context;
    private Session session;
    private List<Franchise> list;
    private List<Franchise> filterList;
    private int type = 0;

    public FranchiseListAdapter(Context context, List<Franchise> list, ClickListener listener, ClickListener listener2) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.listener = listener;
        this.listener2 = listener2;
    }

    public FranchiseListAdapter(Context context, List<Franchise> list, ClickListener listener, ClickListener listener2, ClickListener listener3) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.listener = listener;
        this.listener2 = listener2;
        this.listener3 = listener3;
        type = 1;
    }

    public void addLoadingView() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                filterList.add(null);
                notifyItemInserted(filterList.size() - 1);
            }
        });
    }

    public void removeLoadingView() {
        if (filterList.size() > 0) {
            filterList.remove(filterList.size() - 1);
            notifyItemRemoved(filterList.size());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder listHolder = null;
        if (viewType == VIEW_TYPE_ITEM) {
            listHolder = new MyViewHolder(RecyclerviewCustomerListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == VIEW_TYPE_LOADING) {
            listHolder = new MyViewHolder(ProgressbarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            return listHolder;
        }
        MyViewHolder finalListHolder = listHolder;
        listHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSelected(list.indexOf(filterList.get(finalListHolder.getAbsoluteAdapterPosition())));
            }
        });
        return listHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Franchise model = filterList.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            if (holder instanceof MyViewHolder) {

                final MyViewHolder mainHolder = (MyViewHolder) holder;

                String title = model.getName();
                Common.bindShortName(context, mainHolder.binding.includedLayout.tvShort, position, title);
                mainHolder.binding.tvTitle.setText(title);
                mainHolder.binding.tvContact.setText(model.getContact());
                mainHolder.binding.tvCityName.setText(model.getCity());

                mainHolder.binding.ivContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener2.onItemSelected(list.indexOf(filterList.get(position)));
                    }
                });

                if (type == 1) mainHolder.binding.llAddQuotation.setVisibility(View.VISIBLE);
                mainHolder.binding.llAddQuotation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener3.onItemSelected(list.indexOf(filterList.get(position)));
                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return filterList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : 0);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterList = list;
                } else {
                    List<Franchise> newFilteredList = new ArrayList<>();
                    for (Franchise row : list) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getContact().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getCity().toLowerCase().contains(charString.toLowerCase())) {
                            newFilteredList.add(row);
                        }
                    }
                    filterList = newFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterList = (ArrayList<Franchise>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RecyclerviewCustomerListItemBinding binding;
        private ProgressbarBinding progressbarBinding;

        MyViewHolder(RecyclerviewCustomerListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        MyViewHolder(ProgressbarBinding progressbarBinding) {
            super(progressbarBinding.getRoot());
            this.progressbarBinding = progressbarBinding;
        }
    }
}