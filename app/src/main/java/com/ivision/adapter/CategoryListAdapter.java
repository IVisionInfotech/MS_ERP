package com.ivision.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ivision.R;
import com.ivision.databinding.ProgressbarBinding;
import com.ivision.databinding.RecyclerviewCategoryListItemBinding;
import com.ivision.model.CommonModel;
import com.ivision.model.Customer;
import com.ivision.model.Mill;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Session;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter {

    private ClickListener listener;
    private Context context;
    private Session session;
    private List<CommonModel> list;
    private List<Customer> customerList;
    private String catId = "", customerId = "";
    private int type = 1; // 1 category, 2 customer, 3 product,

    public CategoryListAdapter(Context context, List<CommonModel> list, ClickListener listener) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.listener = listener;
    }

    public CategoryListAdapter(Context context, List<CommonModel> list, String catId, ClickListener listener) {
        this.context = context;
        this.session = new Session(context);
        this.catId = catId;
        this.list = list;
        this.listener = listener;
    }

    public CategoryListAdapter(Context context, List<Customer> customerList, String customerId, int type, ClickListener listener) {
        this.context = context;
        this.session = new Session(context);
        this.customerList = customerList;
        this.customerId = customerId;
        this.type = type;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder listHolder = null;
        listHolder = new MyViewHolder(RecyclerviewCategoryListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        listHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return listHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (type == 1) {
            final CommonModel model = list.get(position);
            if (holder instanceof MyViewHolder) {

                final MyViewHolder mainHolder = (MyViewHolder) holder;

                if (model.getName() != null) {
                    if (!model.getName().isEmpty()) {
                        mainHolder.binding.tvTitle.setText(model.getName());
                    }
                }

                mainHolder.binding.cvMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        catId = model.getCatId();
                        listener.onItemSelected(position);
                        notifyDataSetChanged();
                    }
                });

                if (catId.equals(model.getCatId())) {
                    mainHolder.binding.cvMain.setCardBackgroundColor(context.getResources().getColor(R.color.viewBackground));
                } else {
                    mainHolder.binding.cvMain.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                }
            }
        } else if (type == 2) {
            final Customer model = customerList.get(position);
            if (holder instanceof MyViewHolder) {

                final MyViewHolder mainHolder = (MyViewHolder) holder;

                if (model.getName() != null) {
                    if (!model.getName().isEmpty()) {
                        mainHolder.binding.tvTitle.setText(model.getName());
                    }
                }

                mainHolder.binding.cvMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customerId = model.getCustomerId();
                        listener.onItemSelected(position);
                        notifyDataSetChanged();
                    }
                });

                if (customerId.equals(model.getCustomerId())) {
                    mainHolder.binding.cvMain.setCardBackgroundColor(context.getResources().getColor(R.color.viewBackground));
                } else {
                    mainHolder.binding.cvMain.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (type == 2) {
            return (null != customerList ? customerList.size() : 0);
        } else {
            return (null != list ? list.size() : 0);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RecyclerviewCategoryListItemBinding binding;
        private ProgressbarBinding progressbarBinding;

        MyViewHolder(RecyclerviewCategoryListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        MyViewHolder(ProgressbarBinding progressbarBinding) {
            super(progressbarBinding.getRoot());
            this.progressbarBinding = progressbarBinding;
        }
    }
}