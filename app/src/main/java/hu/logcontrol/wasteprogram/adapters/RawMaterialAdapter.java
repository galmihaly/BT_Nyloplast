package hu.logcontrol.wasteprogram.adapters;

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

    @Override
    public void onBindViewHolder(@NonNull RawMaterialItemViewHolder holder, int position) {
        if(rawMaterialList != null){
            holder.getRawMaterialTimeStampInput().setText(rawMaterialList.get(position).getDate());
            holder.getRawMaterialTypeInput().setText(rawMaterialList.get(position).getMaterialType());
            holder.getRawMaterialCountInput().setText(rawMaterialList.get(position).getDoseNumber());
        }
    }

    @Override
    public int getItemCount() {
        return rawMaterialList.size();
    }

    public static class RawMaterialItemViewHolder extends RecyclerView.ViewHolder {

        private TextView rawMaterialTimeStampInput;
        private TextView rawMaterialTypeInput;
        private TextView rawMaterialCountInput;

        public RawMaterialItemViewHolder(@NonNull View itemView) {
            super(itemView);

            rawMaterialTimeStampInput = itemView.findViewById(R.id.rawMaterialTimeStampInput);
            rawMaterialTypeInput = itemView.findViewById(R.id.rawMaterialTypeInput);
            rawMaterialCountInput = itemView.findViewById(R.id.rawMaterialCountInput);
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
    }
}
