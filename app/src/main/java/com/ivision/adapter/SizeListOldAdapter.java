package com.ivision.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ivision.R;
import com.ivision.databinding.ProgressbarBinding;
import com.ivision.databinding.RecyclerviewSizeListOldItemBinding;
import com.ivision.model.Size;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.Session;

import java.util.ArrayList;
import java.util.List;

public class SizeListOldAdapter extends RecyclerView.Adapter implements Filterable {

    final int VIEW_TYPE_ITEM = 0;
    final int VIEW_TYPE_LOADING = 1;
    private ClickListener listener;
    private CartClickListener cartClickListener;
    private Context context;
    private Session session;
    private List<Size> list;
    private List<Size> filterList;

    public SizeListOldAdapter(Context context, List<Size> list, ClickListener listener, CartClickListener cartClickListener) {
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
            listHolder = new MyViewHolder(RecyclerviewSizeListOldItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
                final String[] mType = {""};

                mainHolder.binding.cbTitle.setText(model.getName());
                mainHolder.binding.tvPrice.setText(context.getResources().getString(R.string.rupee) + " " + model.getDiffPrice());

                if (model.getCartId() != null) {
                    if (!model.getCartId().isEmpty()) {
                        mainHolder.binding.etQuantity.setText(model.getQuantity());
                        mainHolder.binding.etWeight.setText(model.getWeight());
                        if (model.getCartMType().equals("0")) {
                            mainHolder.binding.rdoKg.setChecked(true);
                            mType[0] = "0";
                        } else if (model.getCartMType().equals("1")) {
                            mainHolder.binding.rdoPcs.setChecked(true);
                            mType[0] = "1";
                        }
                        mainHolder.binding.cbTitle.setChecked(true);
                        mainHolder.binding.llFields.setVisibility(View.VISIBLE);
                        mainHolder.binding.ivDelete.setVisibility(View.VISIBLE);
                        mainHolder.binding.llUpdate.setVisibility(View.VISIBLE);
                        mainHolder.binding.llAdd.setVisibility(View.GONE);
                    } else {
                        mainHolder.binding.rdgMeasure.clearCheck();
                        mainHolder.binding.etQuantity.getText().clear();
                        mainHolder.binding.etWeight.getText().clear();
                        mainHolder.binding.cbTitle.setChecked(false);
                        mainHolder.binding.llFields.setVisibility(View.GONE);
                        mainHolder.binding.ivDelete.setVisibility(View.GONE);
                    }
                } else {
                    mainHolder.binding.rdgMeasure.clearCheck();
                    mainHolder.binding.etQuantity.getText().clear();
                    mainHolder.binding.etWeight.getText().clear();
                    mainHolder.binding.cbTitle.setChecked(false);
                    mainHolder.binding.llFields.setVisibility(View.GONE);
                    mainHolder.binding.ivDelete.setVisibility(View.GONE);
                }

                mainHolder.binding.rdgMeasure.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (mainHolder.binding.rdoKg.isChecked()) {
                            mType[0] = "0";
                        } else if (mainHolder.binding.rdoPcs.isChecked()) {
                            mType[0] = "1";
                        }
                    }
                });

                mainHolder.binding.cbTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            mainHolder.binding.llFields.setVisibility(View.VISIBLE);
                            mainHolder.binding.llUpdate.setVisibility(View.GONE);
                            mainHolder.binding.llAdd.setVisibility(View.VISIBLE);
                        } else {
                            mainHolder.binding.llFields.setVisibility(View.GONE);
                            if (model.getCartId() == null || model.getCartId().isEmpty()) {
                                mainHolder.binding.rdgMeasure.clearCheck();
                                mainHolder.binding.etQuantity.getText().clear();
                                mainHolder.binding.etWeight.getText().clear();
                            }
                        }
                    }
                });

                mainHolder.binding.llAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String quantity = mainHolder.binding.etQuantity.getText().toString();
                        String weight = mainHolder.binding.etWeight.getText().toString();
                        if (quantity.isEmpty()) {
                            mainHolder.binding.etQuantity.setError("Enter quantity");
                        } else if (weight.isEmpty()) {
                            mainHolder.binding.etWeight.setError("Enter weight");
                        } else if (mType[0].isEmpty()) {
                            Common.showToast("Select measurement type");
                        } else {
                            cartClickListener.onItemSelected(position, model, quantity, weight, mType[0], 1);
                        }
                    }
                });
                mainHolder.binding.llUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String quantity = mainHolder.binding.etQuantity.getText().toString();
                        String weight = mainHolder.binding.etWeight.getText().toString();
                        if (quantity.isEmpty()) {
                            mainHolder.binding.etQuantity.setError("Enter quantity");
                        } else if (weight.isEmpty()) {
                            mainHolder.binding.etWeight.setError("Enter weight");
                        } else if (mType[0].isEmpty()) {
                            Common.showToast("Select measurement type");
                        } else {
                            cartClickListener.onItemSelected(position, model, quantity, weight, mType[0], 2);
                        }
                    }
                });
                mainHolder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemSelected(position, model);
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

        private RecyclerviewSizeListOldItemBinding binding;
        private ProgressbarBinding progressbarBinding;

        MyViewHolder(RecyclerviewSizeListOldItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        MyViewHolder(ProgressbarBinding progressbarBinding) {
            super(progressbarBinding.getRoot());
            this.progressbarBinding = progressbarBinding;
        }
    }

    public interface CartClickListener {
        void onItemSelected(int position, Size model, String quantity, String weight, String mType, int status);
    }
}