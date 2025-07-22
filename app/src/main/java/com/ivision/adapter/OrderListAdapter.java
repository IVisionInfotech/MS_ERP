package com.ivision.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.ivision.R;
import com.ivision.databinding.ProgressbarBinding;
import com.ivision.databinding.RecyclerviewOrderListItemBinding;
import com.ivision.model.Order;
import com.ivision.model.OrderDetails;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.Session;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter implements Filterable {

    final int VIEW_TYPE_ITEM = 0;
    final int VIEW_TYPE_LOADING = 1;
    private ClickListener listener, listener2, listener3;
    private Context context;
    private Session session;
    private ArrayList<Order> list;
    private ArrayList<Order> filterList;

    public OrderListAdapter(Context context, ArrayList<Order> list, ClickListener listener, ClickListener listener2, ClickListener listener3) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.listener = listener;
        this.listener2 = listener2;
        this.listener3 = listener3;
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
            listHolder = new MyViewHolder(RecyclerviewOrderListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final Order model = filterList.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            if (holder instanceof MyViewHolder) {

                final MyViewHolder mainHolder = (MyViewHolder) holder;

                mainHolder.binding.tvTitle.setText(model.getOrderPrefix() + " - " + model.getOrderNo());

                mainHolder.binding.btnSubmit.setVisibility(View.GONE);

                switch (model.getStatus()) {
                    case "1":
                        mainHolder.binding.btnSubmit.setText("Dispatch");
                        mainHolder.binding.btnSubmit.setVisibility(View.VISIBLE);
                        mainHolder.binding.tvStatus.setText("Confirm");
                        mainHolder.binding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_completed_background));
                        break;
                    case "2":
                        mainHolder.binding.btnSubmit.setText("Deliver");
                        mainHolder.binding.btnSubmit.setVisibility(View.VISIBLE);
                        mainHolder.binding.tvStatus.setText("Dispatch");
                        mainHolder.binding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_dispatch_background));
                        break;
                    case "3":
                        mainHolder.binding.tvStatus.setText("Delivered");
                        mainHolder.binding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_delivered_background));
                        break;
                    default:
                        mainHolder.binding.btnSubmit.setText("Confirm");
                        mainHolder.binding.btnSubmit.setVisibility(View.VISIBLE);
                        mainHolder.binding.tvStatus.setText("Pending");
                        mainHolder.binding.tvStatus.setTextColor(context.getResources().getColor(R.color.black));
                        mainHolder.binding.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_pending_background));
                        break;
                }

                mainHolder.binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener2.onItemSelected(list.indexOf(filterList.get(position)));
                    }
                });

                mainHolder.binding.ivShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener3.onItemSelected(list.indexOf(filterList.get(position)));
                    }
                });

                mainHolder.binding.tvDate.setText(Common.changeDateFormat(model.getDateTimeAdded(), "yyyy-MM-dd", "dd-MM-yyyy"));
                mainHolder.binding.tvCustomer.setText(model.getName());
                float price = 0;
                for (int i = 0; i < model.getDetailsList().size(); i++) {
                    OrderDetails details = model.getDetailsList().get(i);
                    if (Float.parseFloat(details.getOneKgPrice()) > 0) {
                        price = price + (Float.parseFloat(details.getOneKgPrice()) * Float.parseFloat(details.getQuantity()));
                    } else if (Float.parseFloat(details.getSystemPrice()) > 0) {
                        price = price + Float.parseFloat(details.getSystemPrice());
                    } else {
                        price = price + ((Float.parseFloat(details.getBasicPrice()) + Float.parseFloat(details.getDiffPrice())) / 1000) * Float.parseFloat(details.getQuantity());
                    }
                }
                mainHolder.binding.tvPrice.setText(context.getResources().getString(R.string.rupee) + " " + price);

                float gst = price * Float.parseFloat(model.getGst()) / 100;
                mainHolder.binding.tvGST.setText(context.getResources().getString(R.string.rupee) + " " + gst);

                float total = price + gst;
                mainHolder.binding.tvTotal.setText(context.getResources().getString(R.string.rupee) + " " + total);

                OrderDetailsListAdapter adapter = new OrderDetailsListAdapter(context, model.getDetailsList(), new ClickListener() {
                    @Override
                    public void onItemSelected(int position) {
                    }
                });

                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                mainHolder.binding.recyclerView.setLayoutManager(layoutManager);
                mainHolder.binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
                mainHolder.binding.recyclerView.setDrawingCacheEnabled(true);
                mainHolder.binding.recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                mainHolder.binding.recyclerView.setItemViewCacheSize(20);
                mainHolder.binding.recyclerView.setHasFixedSize(false);
                mainHolder.binding.recyclerView.setAdapter(adapter);
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
                    ArrayList<Order> newFilteredList = new ArrayList<>();
                    for (Order row : list) {
                        if (row.getOrderNo().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getContact().toLowerCase().contains(charString.toLowerCase())) {
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
                filterList = (ArrayList<Order>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RecyclerviewOrderListItemBinding binding;
        private ProgressbarBinding progressbarBinding;

        MyViewHolder(RecyclerviewOrderListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        MyViewHolder(ProgressbarBinding progressbarBinding) {
            super(progressbarBinding.getRoot());
            this.progressbarBinding = progressbarBinding;
        }
    }
}