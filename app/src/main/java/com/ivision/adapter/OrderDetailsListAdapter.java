package com.ivision.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.ivision.R;
import com.ivision.databinding.ProgressbarBinding;
import com.ivision.databinding.RecyclerviewCheckoutListItemBinding;
import com.ivision.model.OrderDetails;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Session;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsListAdapter extends RecyclerView.Adapter implements Filterable {

    final int VIEW_TYPE_ITEM = 0;
    final int VIEW_TYPE_LOADING = 1;
    private ClickListener listener;
    private Context context;
    private Session session;
    private List<OrderDetails> list;
    private List<OrderDetails> filterList;

    public OrderDetailsListAdapter(Context context, List<OrderDetails> list, ClickListener listener) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.listener = listener;
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
            listHolder = new MyViewHolder(RecyclerviewCheckoutListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == VIEW_TYPE_LOADING) {
            listHolder = new MyViewHolder(ProgressbarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            return listHolder;
        }
        MyViewHolder finalListHolder = listHolder;
        listHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSelected(finalListHolder.getAdapterPosition());
            }
        });
        return listHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final OrderDetails model = filterList.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            if (holder instanceof MyViewHolder) {

                final MyViewHolder mainHolder = (MyViewHolder) holder;

                mainHolder.binding.tvTitle.setText(model.getSizeName());
                mainHolder.binding.tvProductName.setText(model.getProductName());

                float total = Float.parseFloat(model.getDiffPrice()) + Float.parseFloat(model.getBasicPrice());

                if (model.getProductType().equals("1")) {
                    mainHolder.binding.llPrice.setVisibility(View.GONE);
                    mainHolder.binding.llPrice2.setVisibility(View.GONE);
                } else {
                    mainHolder.binding.tvBasicPrice.setText(context.getResources().getString(R.string.rupee) + " " + model.getBasicPrice());
                    mainHolder.binding.tvPrice.setText(context.getResources().getString(R.string.rupee) + " " + model.getDiffPrice());
                    mainHolder.binding.tvTotal.setText(context.getResources().getString(R.string.rupee) + " " + total);
                    mainHolder.binding.llPrice.setVisibility(View.VISIBLE);
                    mainHolder.binding.llPrice2.setVisibility(View.VISIBLE);
                }

                float oneKgPrice = total / 1000;
                float totalKgPrice = oneKgPrice * Float.parseFloat(model.getQuantity());
                mainHolder.binding.tvTotalKgPrice.setText(context.getResources().getString(R.string.rupee) + " " + String.format("%.2f", totalKgPrice));
                if (model.getOneKgPrice() != null) {
                    if (!model.getOneKgPrice().isEmpty()) {
                        if (Float.parseFloat(model.getOneKgPrice()) > 0) {
                            oneKgPrice = Float.parseFloat(model.getOneKgPrice());
                        }
                    }
                }
                totalKgPrice = oneKgPrice * Float.parseFloat(model.getQuantity());
                if (model.getSystemPrice() != null) {
                    if (!model.getSystemPrice().isEmpty()) {
                        if (Float.parseFloat(model.getSystemPrice()) > 0) {
                            totalKgPrice = Float.parseFloat(model.getSystemPrice());
                            oneKgPrice = totalKgPrice / Float.parseFloat(model.getQuantity());
                        }
                    }
                }

                mainHolder.binding.tvWeight.setText(model.getQuantity() + " " + (model.getmType().equals("0") ? "Kg" : "Pcs"));
                mainHolder.binding.tvOneKgPrice.setText(context.getResources().getString(R.string.rupee) + " " + String.format("%.2f", oneKgPrice));
                mainHolder.binding.tvSystemPrice.setText(context.getResources().getString(R.string.rupee) + " " + String.format("%.2f", totalKgPrice));
                mainHolder.binding.tvPiece.setText(model.getPiece() + " " + (model.getmType().equals("0") ? "Piece" : "Kg"));
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
                    List<OrderDetails> newFilteredList = new ArrayList<>();
                    for (OrderDetails row : list) {
                        if (row.getSizeName().toLowerCase().contains(charString.toLowerCase())) {
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
                filterList = (ArrayList<OrderDetails>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RecyclerviewCheckoutListItemBinding binding;
        private ProgressbarBinding progressbarBinding;

        MyViewHolder(RecyclerviewCheckoutListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        MyViewHolder(ProgressbarBinding progressbarBinding) {
            super(progressbarBinding.getRoot());
            this.progressbarBinding = progressbarBinding;
        }
    }
}