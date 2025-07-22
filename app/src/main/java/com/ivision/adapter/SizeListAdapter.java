package com.ivision.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.isapanah.awesomespinner.AwesomeSpinner;
import com.ivision.R;
import com.ivision.databinding.ProgressbarBinding;
import com.ivision.databinding.RecyclerviewSizeListItemBinding;
import com.ivision.model.Size;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Session;

import java.util.ArrayList;
import java.util.List;

public class SizeListAdapter extends RecyclerView.Adapter implements Filterable {

    final int VIEW_TYPE_ITEM = 0;
    final int VIEW_TYPE_LOADING = 1;
    private ClickListener listener;
    private CartClickListener cartClickListener;
    private Context context;
    private Session session;
    private List<Size> list;
    private List<Size> filterList;

    public SizeListAdapter(Context context, List<Size> list, ClickListener listener, CartClickListener cartClickListener) {
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
            listHolder = new MyViewHolder(RecyclerviewSizeListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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

                final float[] piece = {0};
                final MyViewHolder mainHolder = (MyViewHolder) holder;

                mainHolder.binding.cbTitle.setText(model.getName());
                mainHolder.binding.tvProductName.setText(model.getProductName());

                if (model.getProductType().equals("1")) {
                    mainHolder.binding.llPrice.setVisibility(View.GONE);
                    mainHolder.binding.llEnterPrice.setVisibility(View.VISIBLE);
                } else {
                    mainHolder.binding.tvBasicPrice.setText(context.getResources().getString(R.string.rupee) + " " + model.getBasicPrice());
                    mainHolder.binding.tvPrice.setText(context.getResources().getString(R.string.rupee) + " " + model.getDiffPrice() + " (SD)");
                    float total = Float.parseFloat(model.getBasicPrice()) + Float.parseFloat(model.getDiffPrice());
                    mainHolder.binding.tvTotal.setText(context.getResources().getString(R.string.rupee) + " " + total);
                    mainHolder.binding.llPrice.setVisibility(View.VISIBLE);
                    mainHolder.binding.llEnterPrice.setVisibility(View.GONE);
                }

                mainHolder.binding.cbTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            mainHolder.binding.llFields.setVisibility(View.VISIBLE);
                            if (model.getCartId() == null || model.getCartId().isEmpty()) {
                                mainHolder.binding.llUpdate.setVisibility(View.GONE);
                                mainHolder.binding.llAdd.setVisibility(View.VISIBLE);
                            } else {
                                mainHolder.binding.llUpdate.setVisibility(View.VISIBLE);
                                mainHolder.binding.llAdd.setVisibility(View.GONE);
                            }
                        } else {
                            mainHolder.binding.llFields.setVisibility(View.GONE);
                            if (model.getCartId() == null || model.getCartId().isEmpty()) {
                                mainHolder.binding.etQuantity.getText().clear();
                                mainHolder.binding.etPrice.getText().clear();
                                mainHolder.binding.tvPiece.setText("");
                            }
                        }
                    }
                });

                List<String> categories = new ArrayList<String>();
                categories.add("Kg");
                categories.add("Pcs");

                final int[] type = new int[1];
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_list_item, categories);
                mainHolder.binding.spinner.setAdapter(adapter);

                mainHolder.binding.spinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                        if (position == 0) {
                            type[0] = 0;
                        } else {
                            type[0] = 1;
                        }
                        if (!mainHolder.binding.etQuantity.getText().toString().isEmpty()) {
                            if (type[0] == 0) {
                                piece[0] = Float.parseFloat(mainHolder.binding.etQuantity.getText().toString()) / Float.parseFloat(model.getNosKg());
                                mainHolder.binding.tvPiece.setText(String.format("%.2f", piece[0]) + " Pcs");
                            } else {
                                piece[0] = Float.parseFloat(mainHolder.binding.etQuantity.getText().toString()) * Float.parseFloat(model.getNosKg());
                                mainHolder.binding.tvPiece.setText(String.format("%.2f", piece[0]) + " Kg");
                            }
                        }
                    }
                });

                mainHolder.binding.spinner.setSelection(0);

                if (Float.parseFloat(model.getNosKg()) > 0) {
                    mainHolder.binding.etQuantity.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (!charSequence.toString().isEmpty()) {
                                if (!charSequence.toString().equals(".")){
                                    if (type[0] == 0) {
                                        piece[0] = Float.parseFloat(charSequence.toString()) / Float.parseFloat(model.getNosKg());
                                        mainHolder.binding.tvPiece.setText(String.format("%.2f", piece[0]) + " Pcs");
                                    } else {
                                        piece[0] = Float.parseFloat(charSequence.toString()) * Float.parseFloat(model.getNosKg());
                                        mainHolder.binding.tvPiece.setText(String.format("%.2f", piece[0]) + " Kg");
                                    }
                                } else {
                                    piece[0] = 0;
                                    mainHolder.binding.tvPiece.setText("");
                                }
                            } else {
                                piece[0] = 0;
                                mainHolder.binding.tvPiece.setText("");
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }

                if (model.getCartId() != null) {
                    if (!model.getCartId().isEmpty()) {
                        mainHolder.binding.etQuantity.setText(model.getQuantity());
                        if (model.getCartMType().equals("0")) {
                            mainHolder.binding.tvPiece.setText(model.getPiece() + " Pcs");
                            mainHolder.binding.spinner.setSelection(0);
                        } else {
                            mainHolder.binding.tvPiece.setText(model.getPiece() + " Kg");
                            mainHolder.binding.spinner.setSelection(1);
                        }
                        if (model.getProductType().equals("1")) {
                            mainHolder.binding.etPrice.setText(model.getSystemPrice());
                            mainHolder.binding.llEnterPrice.setVisibility(View.VISIBLE);
                        } else {
                            mainHolder.binding.llEnterPrice.setVisibility(View.GONE);
                        }
                        mainHolder.binding.cbTitle.setChecked(true);
                        mainHolder.binding.llFields.setVisibility(View.VISIBLE);
                        mainHolder.binding.ivDelete.setVisibility(View.VISIBLE);
                        mainHolder.binding.llUpdate.setVisibility(View.VISIBLE);
                        mainHolder.binding.llAdd.setVisibility(View.GONE);
                    } else {
                        mainHolder.binding.etQuantity.getText().clear();
                        mainHolder.binding.etPrice.getText().clear();
                        mainHolder.binding.tvPiece.setText("");
                        mainHolder.binding.cbTitle.setChecked(false);
                        mainHolder.binding.llFields.setVisibility(View.GONE);
                        mainHolder.binding.ivDelete.setVisibility(View.GONE);
                    }
                } else {
                    mainHolder.binding.etQuantity.getText().clear();
                    mainHolder.binding.tvPiece.setText("");
                    mainHolder.binding.cbTitle.setChecked(false);
                    mainHolder.binding.llFields.setVisibility(View.GONE);
                    mainHolder.binding.ivDelete.setVisibility(View.GONE);
                }

                mainHolder.binding.llAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String quantity = mainHolder.binding.etQuantity.getText().toString();
                        if (quantity.isEmpty()) {
                            mainHolder.binding.etQuantity.setError("Enter quantity");
                        } else if (Float.parseFloat(model.getNosKg()) > 0 && piece[0] <= 0) {
                            mainHolder.binding.etQuantity.setError("Enter quantity");
                        } else {
                            if (model.getProductType().equals("0")) {
                                cartClickListener.onItemSelected(position, model, quantity, String.valueOf(piece[0]), String.valueOf(type[0]), 1);
                            } else {
                                String price = mainHolder.binding.etPrice.getText().toString();
                                if (price.isEmpty()) {
                                    mainHolder.binding.etPrice.setError("Enter price");
                                } else if (Integer.parseInt(price) <= 0) {
                                    mainHolder.binding.etPrice.setError("Enter valid price");
                                } else {
                                    cartClickListener.onItemSelected(position, model, quantity, String.valueOf(piece[0]), String.valueOf(type[0]), price, 1);
                                }
                            }
                        }
                    }
                });
                mainHolder.binding.llUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String quantity = mainHolder.binding.etQuantity.getText().toString();
                        if (quantity.isEmpty()) {
                            mainHolder.binding.etQuantity.setError("Enter quantity");
                        } else if (Float.parseFloat(model.getNosKg()) > 0 && piece[0] <= 0) {
                            mainHolder.binding.etQuantity.setError("Enter quantity");
                        } else {
                            if (model.getProductType().equals("0")) {
                                cartClickListener.onItemSelected(position, model, quantity, String.valueOf(piece[0]), String.valueOf(type[0]), 2);
                            } else {
                                String price = mainHolder.binding.etPrice.getText().toString();
                                if (price.isEmpty()) {
                                    mainHolder.binding.etPrice.setError("Enter price");
                                } else if (Float.parseFloat(price) <= 0) {
                                    mainHolder.binding.etPrice.setError("Enter valid price");
                                } else {
                                    cartClickListener.onItemSelected(position, model, quantity, String.valueOf(piece[0]), String.valueOf(type[0]), price, 2);
                                }
                            }
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

        private RecyclerviewSizeListItemBinding binding;
        private ProgressbarBinding progressbarBinding;

        MyViewHolder(RecyclerviewSizeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        MyViewHolder(ProgressbarBinding progressbarBinding) {
            super(progressbarBinding.getRoot());
            this.progressbarBinding = progressbarBinding;
        }
    }

    public interface CartClickListener {
        default void onItemSelected(int position, Size model, String quantity, String piece, String type, int status) {

        }

        default void onItemSelected(int position, Size model, String quantity, String piece, String type, String price, int status) {

        }
    }
}