package com.example.loginapp.adapter.cart_adapter;

import android.annotation.SuppressLint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.databinding.LayoutItemCartBinding;
import com.example.loginapp.model.entity.FirebaseProduct;

public class CartAdapter extends ListAdapter<FirebaseProduct, CartAdapter.ItemCartViewHolder> {
    private final String TAG = this.toString();

    private final CartItemClickListener listener;

    public CartAdapter(CartItemClickListener listener) {
        super(DiffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartViewHolder holder = new ItemCartViewHolder(LayoutItemCartBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
        ), listener);
        return holder;
    }

    public void removeItem(int position) {
        listener.onDeleteProduct(getItem(position).getId());
    }


    @Override
    public void onBindViewHolder(ItemCartViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class ItemCartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final LayoutItemCartBinding binding;

        private final CartItemClickListener listener;

        public ItemCartViewHolder(LayoutItemCartBinding binding, CartItemClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
            binding.getRoot().setOnClickListener(this);
            binding.add.setOnClickListener(this);
            binding.minus.setOnClickListener(this);
            binding.checkbox.setOnClickListener(this);
            binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) ->
                listener.onItemChecked(getItem(getAdapterPosition()), isChecked)
            );
        }

        public void bind(FirebaseProduct product) {
            binding.setProduct(product);
        }

        @Override
        public void onClick(View v) {
            FirebaseProduct product = getItem(getAdapterPosition());
            int quantity = Integer.parseInt(product.getQuantity());
            if (v.getId() == binding.minus.getId()) {
                if (quantity > 1) listener.updateQuantity(product.getId(), quantity - 1);
                else listener.onDeleteProduct(product.getId());
            } else if (v.getId() == binding.add.getId()) {
                listener.updateQuantity(product.getId(), quantity + 1);
            } else if (v.getId() == binding.getRoot().getId()) {
                listener.onItemClick(product.getId());
            }
        }
    }

    public static final DiffUtil.ItemCallback<FirebaseProduct> DiffCallback =
        new DiffUtil.ItemCallback<FirebaseProduct>() {

            @Override
            public boolean areItemsTheSame(@NonNull FirebaseProduct oldItem, @NonNull FirebaseProduct newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull FirebaseProduct oldItem, @NonNull FirebaseProduct newItem) {
                return oldItem.equals(newItem);
            }
        };
}
