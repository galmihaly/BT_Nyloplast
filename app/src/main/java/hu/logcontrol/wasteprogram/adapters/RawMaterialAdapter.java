package hu.logcontrol.wasteprogram.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import hu.logcontrol.wasteprogram.models.LocalRawMaterialsStorage;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.R;

public class RawMaterialAdapter extends RecyclerView.Adapter<RawMaterialAdapter.RawMaterialItemViewHolder> {

    private List<RawMaterial> rawMaterialList;

    public RawMaterialAdapter(List<RawMaterial> rawMaterialList) {
        this.rawMaterialList = rawMaterialList;
    }

    @NonNull
    @Override
    public RawMaterialItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rawmaterial_item_layout,
                parent, false);

        return new RawMaterialItemViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RawMaterialItemViewHolder holder, int position) {

        for (int i = 0; i < rawMaterialList.size(); i++) {
            Log.d("adapter", rawMaterialList.get(i).getMaterialType());
        }

        if(rawMaterialList != null){

            for (int i = 0; i < rawMaterialList.size(); i++) {
                Log.d("adapter", rawMaterialList.get(i).getMaterialType());
            }

            holder.getRawMaterialTimeStampInput().setText(rawMaterialList.get(position).getDate());
            holder.getRawMaterialTypeInput().setText(rawMaterialList.get(position).getMaterialType());
            holder.getRawMaterialCountInput().setText(rawMaterialList.get(position).getDoseNumber());
            holder.getNumberOfItem().setText(position + 1 + ".");
        }
    }

    @Override
    public int getItemCount() {
        if(rawMaterialList == null) return -1;
        return rawMaterialList.size();
    }

    public static class RawMaterialItemViewHolder extends RecyclerView.ViewHolder {

        private TextView rawMaterialTimeStampInput;
        private TextView rawMaterialTypeInput;
        private TextView rawMaterialCountInput;
        private TextView numberOfItem;

        public RawMaterialItemViewHolder(@NonNull View itemView) {
            super(itemView);

            rawMaterialTimeStampInput = itemView.findViewById(R.id.rawMaterialTimeStampInput);
            rawMaterialTypeInput = itemView.findViewById(R.id.rawMaterialTypeInput);
            rawMaterialCountInput = itemView.findViewById(R.id.rawMaterialCountInput);
            numberOfItem = itemView.findViewById(R.id.numberOfItem);
        }

        public TextView getRawMaterialTimeStampInput() {
            return rawMaterialTimeStampInput;
        }

        public TextView getRawMaterialTypeInput() {
            return rawMaterialTypeInput;
        }

        public TextView getRawMaterialCountInput() {
            return rawMaterialCountInput;
        }

        public TextView getNumberOfItem() {
            return numberOfItem;
        }
    }
}
