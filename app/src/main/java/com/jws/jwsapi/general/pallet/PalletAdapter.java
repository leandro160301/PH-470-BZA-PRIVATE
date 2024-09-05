package com.jws.jwsapi.general.pallet;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jws.jwsapi.databinding.ItemPalletBinding;

import java.util.List;

public class PalletAdapter extends RecyclerView.Adapter<PalletAdapter.PalletViewHolder> {

    private List<Pallet> palletList;

    public PalletAdapter(List<Pallet> palletList) {
        this.palletList = palletList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Pallet> newPalletList) {
        this.palletList = newPalletList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPalletBinding binding = ItemPalletBinding.inflate(inflater, parent, false);
        return new PalletViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PalletViewHolder holder, int position) {
        Pallet pallet = palletList.get(position);
        holder.bind(pallet);
    }

    @Override
    public int getItemCount() {
        return palletList.size();
    }

    static class PalletViewHolder extends RecyclerView.ViewHolder {
        private final ItemPalletBinding binding;

        public PalletViewHolder(ItemPalletBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Pallet pallet) {
            binding.tvPalletName.setText(pallet.getName());
            binding.tvPalletCode.setText(pallet.getCode());
        }
    }
}