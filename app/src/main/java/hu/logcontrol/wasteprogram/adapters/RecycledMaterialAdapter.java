package hu.logcontrol.wasteprogram.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.lang.ref.WeakReference;
import java.util.List;

import hu.logcontrol.wasteprogram.R;
import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.interfaces.IModesThreeView;
import hu.logcontrol.wasteprogram.models.LocalEncryptedPreferences;
import hu.logcontrol.wasteprogram.models.RecycledMaterial;

public class RecycledMaterialAdapter extends RecyclerView.Adapter<RecycledMaterialAdapter.RecycledMaterialViewHolder> {

    private Context context;
    private final List<RecycledMaterial> recycledMaterialList;
    private final WeakReference<IModesThreeView> modesThreeWeakReference;
    private LocalEncryptedPreferences preferences;

    public RecycledMaterialAdapter(Context context, List<RecycledMaterial> recycledMaterialList, IModesThreeView modesThreeWeakReference) {
        this.context = context;
        this.recycledMaterialList = recycledMaterialList;
        this.modesThreeWeakReference = new WeakReference<>(modesThreeWeakReference);
    }

    @NonNull
    @Override
    public RecycledMaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycledmaterial_item_layout, parent, false);
        return new RecycledMaterialAdapter.RecycledMaterialViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycledMaterialViewHolder holder, int position) {

        preferences = LocalEncryptedPreferences.getInstance(
                "values",
                MasterKeys.AES256_GCM_SPEC,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        if(recycledMaterialList != null){
            if(recycledMaterialList.size() != 0){

                if(holder.getAdapterPosition() != RecyclerView.NO_POSITION){
                    holder.getNumberOfItem_3().setText(position + 1 + ".");
                    holder.getRawMaterialTypeInput_3().setText(recycledMaterialList.get(position).getMaterialType());
                    holder.getStorageBoxIdentifierInput_3().setText(recycledMaterialList.get(position).getStorageBoxIdentifier());
                    holder.getMassDataInput_3().setText(recycledMaterialList.get(position).getMassData());

                    if(recycledMaterialList.size() == preferences.getIntValueByKey("listvalue")){
                        modesThreeWeakReference.get().settingButton(EditButtonEnums.ADD_BUTTON_DISABLED);
                    }
                    else {
                        modesThreeWeakReference.get().settingButton(EditButtonEnums.ADD_BUTTON_ENABLED);
                    }
                }
            }

            holder.getDeleteItemButton_3().setOnClickListener(view -> {
                if(recycledMaterialList.size() > 0){
                    recycledMaterialList.remove(position);
                    notifyDataSetChanged();

                    if(recycledMaterialList.size() == 0){
                        modesThreeWeakReference.get().settingButton(EditButtonEnums.SAVE_BUTTON_DISABLED);
                    }
                }
            });
        }

        if(position % 2 == 0){ holder.itemView.setBackgroundColor(Color.parseColor("#6BCEFF")); }
        else{ holder.itemView.setBackgroundColor(Color.parseColor("#506BCEFF")); }
    }

    @Override
    public int getItemCount() {
        return recycledMaterialList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearRawMaterialList(){
        if(recycledMaterialList == null) return;
        recycledMaterialList.clear();
        notifyDataSetChanged();
    }

    public static class RecycledMaterialViewHolder extends RecyclerView.ViewHolder {

        private TextView numberOfItem_3;
        private TextView rawMaterialTypeInput_3;
        private TextView storageBoxIdentifierInput_3;
        private TextView massDataInput_3;

        private ImageButton deleteItemButton_3;

        public RecycledMaterialViewHolder(@NonNull View itemView) {
            super(itemView);

            numberOfItem_3 = itemView.findViewById(R.id.numberOfItem_3);
            rawMaterialTypeInput_3 = itemView.findViewById(R.id.rawMaterialTypeInput_3);
            storageBoxIdentifierInput_3 = itemView.findViewById(R.id.storageBoxIdentifierInput_3);
            massDataInput_3 = itemView.findViewById(R.id.massDataInput_3);

            deleteItemButton_3 = itemView.findViewById(R.id.deleteItemButton_3);
        }

        public TextView getNumberOfItem_3() {
            return numberOfItem_3;
        }

        public TextView getRawMaterialTypeInput_3() {
            return rawMaterialTypeInput_3;
        }

        public TextView getStorageBoxIdentifierInput_3() {
            return storageBoxIdentifierInput_3;
        }

        public TextView getMassDataInput_3() {
            return massDataInput_3;
        }

        public ImageButton getDeleteItemButton_3() {
            return deleteItemButton_3;
        }
    }
}
