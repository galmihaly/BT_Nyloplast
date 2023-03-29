package hu.logcontrol.wasteprogram.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import hu.logcontrol.wasteprogram.interfaces.IModesTwoView;
import hu.logcontrol.wasteprogram.models.LocalEncryptedPreferences;
import hu.logcontrol.wasteprogram.models.RawMaterialTypeMass;

public class RawMaterialTypeMassAdapter extends RecyclerView.Adapter<RawMaterialTypeMassAdapter.RawMaterialTypeMassViewHolder> {

    private Context context;
    private final List<RawMaterialTypeMass> rawMaterialTypeMassList;
    private final WeakReference<IModesTwoView> modesTwoWeakReference;
    private LocalEncryptedPreferences preferences;

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

        preferences = LocalEncryptedPreferences.getInstance(
                "values",
                MasterKeys.AES256_GCM_SPEC,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        if(rawMaterialTypeMassList != null){
            if(rawMaterialTypeMassList.size() != 0){
                if(holder.getAdapterPosition() != RecyclerView.NO_POSITION){
                    holder.getNumberOfItem_2().setText(position + 1 + ".");
                    holder.getRawMaterialWasteCodeInput().setText(rawMaterialTypeMassList.get(position).getWasteCode());
                    holder.getRawMaterialTypeInput_2().setText(rawMaterialTypeMassList.get(position).getMaterialType());
                    holder.getStorageBoxIdentifierInput().setText(rawMaterialTypeMassList.get(position).getStorageBoxIdentifier());
                    holder.getMassDataInput().setText(rawMaterialTypeMassList.get(position).getMassData());
                    holder.getCommentDataInput().setText(rawMaterialTypeMassList.get(position).getComment());

                    if(rawMaterialTypeMassList.size() == preferences.getIntValueByKey("listvalue")){
                        modesTwoWeakReference.get().settingButton(EditButtonEnums.ADD_BUTTON_DISABLED);
                    }
                    else {
                        modesTwoWeakReference.get().settingButton(EditButtonEnums.ADD_BUTTON_ENABLED);
                    }
                }
            }

            holder.getDeleteItemButton_2().setOnClickListener(view -> {
                if(rawMaterialTypeMassList.size() > 0){
                    rawMaterialTypeMassList.remove(position);
                    notifyDataSetChanged();

                    if(rawMaterialTypeMassList.size() == 0){
                        modesTwoWeakReference.get().settingButton(EditButtonEnums.SAVE_BUTTON_DISABLED);
                    }
                }
            });
        }

        if(position % 2 == 0){ holder.itemView.setBackgroundColor(Color.parseColor("#6BCEFF")); }
        else{ holder.itemView.setBackgroundColor(Color.parseColor("#506BCEFF")); }
    }

    @Override
    public int getItemCount() {
        return rawMaterialTypeMassList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearRawMaterialList(){
        if(rawMaterialTypeMassList == null) return;
        rawMaterialTypeMassList.clear();
        notifyDataSetChanged();
    }

    public static class RawMaterialTypeMassViewHolder extends RecyclerView.ViewHolder {

        private TextView numberOfItem_2;
        private TextView rawMaterialWasteCodeInput;
        private TextView rawMaterialTypeInput_2;
        private TextView storageBoxIdentifierInput;
        private TextView massDataInput;
        private TextView commentDataInput;

        private ImageButton deleteItemButton_2;

        public RawMaterialTypeMassViewHolder(@NonNull View itemView) {
            super(itemView);

            numberOfItem_2 = itemView.findViewById(R.id.numberOfItem_2);
            rawMaterialWasteCodeInput = itemView.findViewById(R.id.rawMaterialWasteCodeInput);
            rawMaterialTypeInput_2 = itemView.findViewById(R.id.rawMaterialTypeInput_2);
            storageBoxIdentifierInput = itemView.findViewById(R.id.storageBoxIdentifierInput);
            massDataInput = itemView.findViewById(R.id.massDataInput);
            commentDataInput = itemView.findViewById(R.id.commentDataInput);

            deleteItemButton_2 = itemView.findViewById(R.id.deleteItemButton_2);
        }

        public TextView getNumberOfItem_2() {
            return numberOfItem_2;
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

        public TextView getCommentDataInput() {
            return commentDataInput;
        }

        public ImageButton getDeleteItemButton_2() {
            return deleteItemButton_2;
        }
    }
}
