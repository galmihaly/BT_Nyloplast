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

import hu.logcontrol.wasteprogram.enums.EditButtonEnums;
import hu.logcontrol.wasteprogram.interfaces.IModesOneView;
import hu.logcontrol.wasteprogram.models.LocalEncryptedPreferences;
import hu.logcontrol.wasteprogram.models.RawMaterial;
import hu.logcontrol.wasteprogram.R;

public class RawMaterialAdapter extends RecyclerView.Adapter<RawMaterialAdapter.RawMaterialItemViewHolder> {

    private Context context;
    private List<RawMaterial> rawMaterialList;
    private WeakReference<IModesOneView> modesOneWeakReference;
    private LocalEncryptedPreferences preferences;

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

        preferences = LocalEncryptedPreferences.getInstance(
                "values",
                MasterKeys.AES256_GCM_SPEC,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        if(rawMaterialList != null){
            if(rawMaterialList.size() != 0){
                if(holder.getAdapterPosition() != RecyclerView.NO_POSITION){
                    holder.getNumberOfItem().setText(position + 1 + ".");
                    holder.getRawMaterialTypeInput().setText(rawMaterialList.get(position).getMaterialType());
                    holder.getRawMaterialCountInput().setText(rawMaterialList.get(position).getDoseNumber());
                    holder.getComment().setText(rawMaterialList.get(position).getComment());

                    holder.getDeleteItemButton().setFocusable(false);

                    modesOneWeakReference.get().settingButton(EditButtonEnums.ADD_BUTTON_ENABLED);

//                    if(rawMaterialList.size() == preferences.getIntValueByKey("listvalue")){
//                        modesOneWeakReference.get().settingButton(EditButtonEnums.ADD_BUTTON_DISABLED);
//                    }
//                    else {
//                        modesOneWeakReference.get().settingButton(EditButtonEnums.ADD_BUTTON_ENABLED);
//                    }
                }
            }

            holder.getDeleteItemButton().setOnClickListener(view -> {
                if(rawMaterialList.size() > 0){
                    rawMaterialList.remove(position);
                    notifyDataSetChanged();

                    if(rawMaterialList.size() == 0){
                        modesOneWeakReference.get().settingButton(EditButtonEnums.SAVE_BUTTON_DISABLED);
                    }
                }
            });
        }

        if(position % 2 == 0){ holder.itemView.setBackgroundColor(Color.parseColor("#6BCEFF")); }
        else{ holder.itemView.setBackgroundColor(Color.parseColor("#506BCEFF")); }
    }

    @Override
    public int getItemCount() {
        if(rawMaterialList == null) return -1;
        return rawMaterialList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearRawMaterialList(){
        if(rawMaterialList == null) return;
        rawMaterialList.clear();
        notifyDataSetChanged();
    }

    public static class RawMaterialItemViewHolder extends RecyclerView.ViewHolder {

        private TextView rawMaterialTypeInput;
        private TextView rawMaterialCountInput;
        private TextView numberOfItem;
        private TextView comment;

        private ImageButton deleteItemButton;

        public RawMaterialItemViewHolder(@NonNull View itemView) {
            super(itemView);

            rawMaterialTypeInput = itemView.findViewById(R.id.rawMaterialTypeInput_1);
            rawMaterialCountInput = itemView.findViewById(R.id.rawMaterialCountInput_1);
            numberOfItem = itemView.findViewById(R.id.numberOfItem_1);
            comment = itemView.findViewById(R.id.rawMaterialCommentInput_1);

            deleteItemButton = itemView.findViewById(R.id.deleteItemButton_1);
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

        public TextView getComment() {
            return comment;
        }

        public ImageButton getDeleteItemButton() {
            return deleteItemButton;
        }
    }
}
