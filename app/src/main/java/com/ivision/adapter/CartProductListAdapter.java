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
import com.ivision.databinding.RecyclerviewCartProductListItemBinding;
import com.ivision.model.Product;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.Session;

import java.util.ArrayList;
import java.util.List;

public class CartProductListAdapter extends RecyclerView.Adapter implements Filterable {

    final int VIEW_TYPE_ITEM = 0;
    final int VIEW_TYPE_LOADING = 1;
    private ClickListener listener;
    private CartClickListener cartClickListener;
    private Context context;
    private Session session;
    private List<Product> list;
    private List<Product> filterList;

    public CartProductListAdapter(Context context, List<Product> list, ClickListener listener, CartClickListener cartClickListener) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.listener = listener;
        this.cartClickListener = cartClickListener;
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
            listHolder = new MyViewHolder(RecyclerviewCartProductListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == VIEW_TYPE_LOADING) {
            listHolder = new MyViewHolder(ProgressbarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            return listHolder;
        }
        MyViewHolder finalListHolder = listHolder;
        listHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return listHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Product model = filterList.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            if (holder instanceof MyViewHolder) {

                final MyViewHolder mainHolder = (MyViewHolder) holder;

                Common.loadImage(context, mainHolder.binding.ivImage, model.getImage());
                mainHolder.binding.tvTitle.setText(model.getName());
                /*mainHolder.binding.tvMRP.setText(context.getResources().getString(R.string.rupee) + " " + model.getMrp());
                mainHolder.binding.tvMRP.setPaintFlags(mainHolder.binding.tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mainHolder.binding.tvPrice.setText(context.getResources().getString(R.string.rupee) + " " + model.getPrice());
                mainHolder.binding.tvDiscount.setText(model.getDiscount() + "% OFF");

                final int[] quantity = {Common.getCartQuantity(context, model)};

                if (quantity[0] > 0) {
                    mainHolder.binding.ivRemove.setVisibility(View.VISIBLE);
                    mainHolder.binding.tvQuantity.setVisibility(View.VISIBLE);
                } else {
                    mainHolder.binding.ivRemove.setVisibility(View.GONE);
                    mainHolder.binding.tvQuantity.setVisibility(View.GONE);
                }
                mainHolder.binding.tvQuantity.setText(String.valueOf(quantity[0]));

                mainHolder.binding.ivAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quantity[0]++;
                        mainHolder.binding.tvQuantity.setText(String.valueOf(quantity[0]));
                        mainHolder.binding.tvQuantity.setVisibility(View.VISIBLE);
                        mainHolder.binding.ivRemove.setVisibility(View.VISIBLE);
                        cartClickListener.onItemSelected(position, quantity[0], 1);
                    }
                });
                mainHolder.binding.ivRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quantity[0]--;
                        if (quantity[0] <= 0) {
                            quantity[0] = 0;
                            mainHolder.binding.ivRemove.setVisibility(View.GONE);
                            mainHolder.binding.tvQuantity.setVisibility(View.GONE);
                        }
                        mainHolder.binding.tvQuantity.setText(String.valueOf(quantity[0]));
                        cartClickListener.onItemSelected(position, quantity[0], 0);
                    }
                });*/

                mainHolder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemSelected(position);
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
                    List<Product> newFilteredList = new ArrayList<>();
                    for (Product row : list) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
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
                filterList = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RecyclerviewCartProductListItemBinding binding;
        private ProgressbarBinding progressbarBinding;

        MyViewHolder(RecyclerviewCartProductListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        MyViewHolder(ProgressbarBinding progressbarBinding) {
            super(progressbarBinding.getRoot());
            this.progressbarBinding = progressbarBinding;
        }
    }

    public interface CartClickListener {
        void onItemSelected(int position, int quantity, int status);
    }
}