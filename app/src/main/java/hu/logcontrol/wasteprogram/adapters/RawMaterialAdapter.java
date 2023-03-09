package hu.logcontrol.wasteprogram.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import hu.logcontrol.wasteprogram.ModesOne;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.models.LocalRawMaterialsStorage;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.R;

public class RawMaterialAdapter extends RecyclerView.Adapter<RawMaterialAdapter.RawMaterialItemViewHolder> {

    private Context context;
    private List<RawMaterial> rawMaterialList;

    public RawMaterialAdapter(Context context, List<RawMaterial> rawMaterialList) {
        this.context = context.getApplicationContext();
        this.rawMaterialList = rawMaterialList;
    }

    @NonNull
    @Override
    public RawMaterialItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rawmaterial_item_layout,
                parent, false);

        return new RawMaterialItemViewHolder(v);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull RawMaterialItemViewHolder holder, int position) {
        if(rawMaterialList != null){

            holder.getRawMaterialTimeStampInput().setText(rawMaterialList.get(position).getDate());
            holder.getRawMaterialTypeInput().setText(rawMaterialList.get(position).getMaterialType());
            holder.getRawMaterialCountInput().setText(rawMaterialList.get(position).getDoseNumber());
            holder.getNumberOfItem().setText(position + 1 + ".");

            holder.getDeleteItemButton().setOnClickListener(view -> {
                rawMaterialList.remove(position);
                notifyDataSetChanged();

                if(rawMaterialList.size() == 0){
                    Intent intent  = new Intent(context, ModesOne.class);
                    intent.putExtra("rawMaterialAdapter", "2");
                    context.getApplicationContext().startActivity(intent);
                }

            });
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

        private ImageButton deleteItemButton;

        public RawMaterialItemViewHolder(@NonNull View itemView) {
            super(itemView);

            rawMaterialTimeStampInput = itemView.findViewById(R.id.rawMaterialTimeStampInput);
            rawMaterialTypeInput = itemView.findViewById(R.id.rawMaterialTypeInput);
            rawMaterialCountInput = itemView.findViewById(R.id.rawMaterialCountInput);
            numberOfItem = itemView.findViewById(R.id.numberOfItem);

            deleteItemButton = itemView.findViewById(R.id.deleteItemButton);
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

        public ImageButton getDeleteItemButton() {
            return deleteItemButton;
        }
    }
}
