package com.ivision.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.ivision.R;
import com.ivision.databinding.ProgressbarBinding;
import com.ivision.databinding.RecyclerviewCartListItemBinding;
import com.ivision.model.Size;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Session;

import java.util.ArrayList;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter implements Filterable {

    final int VIEW_TYPE_ITEM = 0;
    final int VIEW_TYPE_LOADING = 1;
    private ClickListener listener, listener3, listener2;
    private Context context;
    private Session session;
    private List<Size> list;
    private List<Size> filterList;

    public CartListAdapter(Context context, List<Size> list, ClickListener listener, ClickListener listener3, ClickListener listener2) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.listener = listener;
        this.listener3 = listener3;
        this.listener2 = listener2;
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
            listHolder = new MyViewHolder(RecyclerviewCartListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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

        final Size model = filterList.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            if (holder instanceof MyViewHolder) {

                final MyViewHolder mainHolder = (MyViewHolder) holder;

                mainHolder.binding.tvTitle.setText(model.getName());
                mainHolder.binding.tvProductName.setText(model.getProductName());

                float total = Float.parseFloat(model.getDiffPrice()) + Float.parseFloat(model.getBasicPrice());

                if (model.getProductType().equals("1")) {
                    mainHolder.binding.llPrice.setVisibility(View.GONE);
                } else {
                    mainHolder.binding.tvBasicPrice.setText(context.getResources().getString(R.string.rupee) + " " + model.getBasicPrice());
                    mainHolder.binding.tvPrice.setText(context.getResources().getString(R.string.rupee) + " " + model.getDiffPrice());
                    mainHolder.binding.tvTotal.setText(context.getResources().getString(R.string.rupee) + " " + total);
                    mainHolder.binding.llPrice.setVisibility(View.VISIBLE);
                }

                float oneKgPrice = total / 1000;
                if (model.getOneKgPrice() != null) {
                    if (!model.getOneKgPrice().isEmpty()) {
                        if (Float.parseFloat(model.getOneKgPrice()) > 0) {
                            oneKgPrice = Float.parseFloat(model.getOneKgPrice());
                        }
                    }
                }
                float totalKgPrice = oneKgPrice * Float.parseFloat(model.getQuantity());
                if (model.getSystemPrice() != null) {
                    if (!model.getSystemPrice().isEmpty()) {
                        if (Float.parseFloat(model.getSystemPrice()) > 0) {
                            totalKgPrice = Float.parseFloat(model.getSystemPrice());
                            oneKgPrice = totalKgPrice / Float.parseFloat(model.getQuantity());
                        }
                    }
                }
                mainHolder.binding.tvQuantity.setText(model.getQuantity());
                mainHolder.binding.tvOneKgPrice.setText(context.getResources().getString(R.string.rupee) + " " + oneKgPrice);
                mainHolder.binding.tvSystemPrice.setText(String.valueOf(totalKgPrice));

                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (!charSequence.toString().isEmpty()) {
                            if (!charSequence.toString().equals(".")) {
                                float totalPrice = Float.parseFloat(charSequence.toString()) * Float.parseFloat(model.getQuantity());
                                mainHolder.binding.etPrice.setText(String.valueOf(totalPrice));
                            } else {
                                mainHolder.binding.etPrice.getText().clear();
                            }
                        } else {
                            mainHolder.binding.etPrice.getText().clear();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                };

                mainHolder.binding.etOneKgPrice.addTextChangedListener(textWatcher);

                mainHolder.binding.llPriceUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String price = mainHolder.binding.etPrice.getText().toString();
                        if (price.isEmpty()) {
                            mainHolder.binding.etPrice.setError("Enter price");
                        } else if (Float.parseFloat(price) <= 0) {
                            mainHolder.binding.etPrice.setError("Enter valid price");
                        } else {
                            listener.onItemSelected(position, price);
                        }
                    }
                });

                mainHolder.binding.llPriceUpdate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String price = mainHolder.binding.etOneKgPrice.getText().toString();
                        if (price.isEmpty()) {
                            mainHolder.binding.etOneKgPrice.setError("Enter price");
                        } else if (Float.parseFloat(price) <= 0) {
                            mainHolder.binding.etOneKgPrice.setError("Enter valid price");
                        } else {
                            listener3.onItemSelected(position, price);
                        }
                    }
                });

                if (model.getCartId() != null) {
                    if (!model.getCartId().isEmpty()) {
                        mainHolder.binding.tvQuantity.setText(model.getQuantity());
                        mainHolder.binding.llFields.setVisibility(View.VISIBLE);
                        mainHolder.binding.ivDelete.setVisibility(View.VISIBLE);
                    } else {
                        mainHolder.binding.llFields.setVisibility(View.GONE);
                        mainHolder.binding.ivDelete.setVisibility(View.GONE);
                    }
                } else {
                    mainHolder.binding.llFields.setVisibility(View.GONE);
                    mainHolder.binding.ivDelete.setVisibility(View.GONE);
                }

                mainHolder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener2.onItemSelected(position, model);
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
                    List<Size> newFilteredList = new ArrayList<>();
                    for (Size row : list) {
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
                filterList = (ArrayList<Size>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RecyclerviewCartListItemBinding binding;
        private ProgressbarBinding progressbarBinding;

        MyViewHolder(RecyclerviewCartListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        MyViewHolder(ProgressbarBinding progressbarBinding) {
            super(progressbarBinding.getRoot());
            this.progressbarBinding = progressbarBinding;
        }
    }
}