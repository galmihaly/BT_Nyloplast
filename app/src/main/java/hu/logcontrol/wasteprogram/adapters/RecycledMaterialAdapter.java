package hu.logcontrol.wasteprogram.adapters;

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
import hu.logcontrol.wasteprogram.interfaces.IModesThreeView;
import hu.logcontrol.wasteprogram.interfaces.IModesTwoView;
import hu.logcontrol.wasteprogram.models.RawMaterialTypeMass;
import hu.logcontrol.wasteprogram.models.RecycledMaterial;

public class RecycledMaterialAdapter extends RecyclerView.Adapter<RecycledMaterialAdapter.RecycledMaterialViewHolder> {

    private final List<RecycledMaterial> recycledMaterialList;
    private final WeakReference<IModesThreeView> modesThreeWeakReference;

    public RecycledMaterialAdapter(List<RecycledMaterial> recycledMaterialList, IModesThreeView modesThreeWeakReference) {
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
        if(recycledMaterialList != null){
            if(recycledMaterialList.size() != 0){

                holder.getNumberOfItem_3().setText(position + 1 + ".");
                holder.getRawMaterialTypeInput_3().setText(recycledMaterialList.get(position).getMaterialType());
                holder.getStorageBoxIdentifierInput_3().setText(recycledMaterialList.get(position).getStorageBoxIdentifier());
                holder.getMassDataInput_3().setText(recycledMaterialList.get(position).getMassData());
            }

            holder.getDeleteItemButton_3().setOnClickListener(view -> {
                recycledMaterialList.remove(position);
                notifyDataSetChanged();

                if(recycledMaterialList.size() == 0){
                    modesThreeWeakReference.get().settingSaveButton(EditButtonEnums.SAVE_BUTTON_DISABLED);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return recycledMaterialList.size();
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
