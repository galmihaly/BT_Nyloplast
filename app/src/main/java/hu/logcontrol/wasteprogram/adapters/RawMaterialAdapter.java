package hu.logcontrol.wasteprogram.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.interfaces.IModesOneView;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.R;

public class RawMaterialAdapter extends RecyclerView.Adapter<RawMaterialAdapter.RawMaterialItemViewHolder> {

    private Context context;
    private List<RawMaterial> rawMaterialList;
    private WeakReference<IModesOneView> modesOneWeakReference;

    public RawMaterialAdapter(Context context, List<RawMaterial> rawMaterialList, IModesOneView modesOneWeakReference) {
        this.context = context.getApplicationContext();
        this.rawMaterialList = rawMaterialList;
        this.modesOneWeakReference = new WeakReference<>(modesOneWeakReference);
    }

    @NonNull
    @Override
    public RawMaterialItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rawmaterial_item_layout, parent, false);
        return new RawMaterialItemViewHolder(v);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull RawMaterialItemViewHolder holder, int position) {
        if(rawMaterialList != null){
            if(rawMaterialList.size() != 0){
                holder.getNumberOfItem().setText(position + 1 + ".");
                holder.getRawMaterialTimeStampInput().setText(rawMaterialList.get(position).getTimeStamp());
                holder.getRawMaterialTypeInput().setText(rawMaterialList.get(position).getMaterialType());
                holder.getRawMaterialCountInput().setText(rawMaterialList.get(position).getDoseNumber());
            }

            holder.getDeleteItemButton().setOnClickListener(view -> {
                rawMaterialList.remove(position);
                notifyDataSetChanged();

                if(rawMaterialList.size() == 0){
                    modesOneWeakReference.get().settingSaveButton(EditButtonEnums.SAVE_BUTTON_DISABLED);
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


            // TODO: meg kell nézni az elemek sorrendjét, nem jól íródik ki

            rawMaterialTimeStampInput = itemView.findViewById(R.id.rawMaterialTimeStampInput_1);
            rawMaterialTypeInput = itemView.findViewById(R.id.rawMaterialTypeInput_1);
            rawMaterialCountInput = itemView.findViewById(R.id.rawMaterialCountInput_1);
            numberOfItem = itemView.findViewById(R.id.numberOfItem_1);

            deleteItemButton = itemView.findViewById(R.id.deleteItemButton_1);
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
