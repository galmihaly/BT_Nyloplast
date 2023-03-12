package hu.logcontrol.wasteprogram.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

import hu.logcontrol.wasteprogram.R;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.interfaces.IModesOneView;
import hu.logcontrol.wasteprogram.interfaces.IModesTwoView;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.models.RawMaterialTypeMass;

public class RawMaterialTypeMassAdapter extends RecyclerView.Adapter<RawMaterialTypeMassAdapter.RawMaterialTypeMassViewHolder> {

    private Context context;
    private final List<RawMaterialTypeMass> rawMaterialTypeMassList;
    private final WeakReference<IModesTwoView> modesTwoWeakReference;

    public RawMaterialTypeMassAdapter(Context context, List<RawMaterialTypeMass> rawMaterialTypeMassList, IModesTwoView modesTwoWeakReference) {
        this.context = context.getApplicationContext();
        this.rawMaterialTypeMassList = rawMaterialTypeMassList;
        this.modesTwoWeakReference = new WeakReference<>(modesTwoWeakReference);
    }

    @NonNull
    @Override
    public RawMaterialTypeMassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rawmaterial_typemass_item_layout, parent, false);
        return new RawMaterialTypeMassAdapter.RawMaterialTypeMassViewHolder(v);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull RawMaterialTypeMassViewHolder holder, int position) {
        if(rawMaterialTypeMassList != null){
            if(rawMaterialTypeMassList.size() != 0){

                holder.getNumberOfItem_2().setText(position + 1 + ".");
                holder.getRawMaterialTimeStampInput_2().setText(rawMaterialTypeMassList.get(position).getTimeStamp());
                holder.getRawMaterialWasteCodeInput().setText(rawMaterialTypeMassList.get(position).getWasteCode());
                holder.getRawMaterialTypeInput_2().setText(rawMaterialTypeMassList.get(position).getMaterialType());
                holder.getStorageBoxIdentifierInput().setText(rawMaterialTypeMassList.get(position).getStorageBoxIdentifier());
                holder.getMassDataInput().setText(rawMaterialTypeMassList.get(position).getMassData());
            }

            holder.getDeleteItemButton_2().setOnClickListener(view -> {
                rawMaterialTypeMassList.remove(position);
                notifyDataSetChanged();

                if(rawMaterialTypeMassList.size() == 0){
                    modesTwoWeakReference.get().settingSaveButton(EditButtonEnums.SAVE_BUTTON_DISABLED);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return rawMaterialTypeMassList.size();
    }

    public static class RawMaterialTypeMassViewHolder extends RecyclerView.ViewHolder {

        private TextView numberOfItem_2;
        private TextView rawMaterialTimeStampInput_2;
        private TextView rawMaterialWasteCodeInput;
        private TextView rawMaterialTypeInput_2;
        private TextView storageBoxIdentifierInput;
        private TextView massDataInput;

        private ImageButton deleteItemButton_2;

        public RawMaterialTypeMassViewHolder(@NonNull View itemView) {
            super(itemView);

            numberOfItem_2 = itemView.findViewById(R.id.numberOfItem_2);
            rawMaterialTimeStampInput_2 = itemView.findViewById(R.id.rawMaterialTimeStampInput_2);
            rawMaterialWasteCodeInput = itemView.findViewById(R.id.rawMaterialWasteCodeInput);
            rawMaterialTypeInput_2 = itemView.findViewById(R.id.rawMaterialTypeInput_2);
            storageBoxIdentifierInput = itemView.findViewById(R.id.storageBoxIdentifierInput);
            massDataInput = itemView.findViewById(R.id.massDataInput);

            deleteItemButton_2 = itemView.findViewById(R.id.deleteItemButton_2);
        }

        public TextView getNumberOfItem_2() {
            return numberOfItem_2;
        }

        public TextView getRawMaterialTimeStampInput_2() {
            return rawMaterialTimeStampInput_2;
        }

        public TextView getRawMaterialWasteCodeInput() {
            return rawMaterialWasteCodeInput;
        }

        public TextView getRawMaterialTypeInput_2() {
            return rawMaterialTypeInput_2;
        }

        public TextView getStorageBoxIdentifierInput() {
            return storageBoxIdentifierInput;
        }

        public TextView getMassDataInput() {
            return massDataInput;
        }

        public ImageButton getDeleteItemButton_2() {
            return deleteItemButton_2;
        }
    }
}
