package com.example.aksharSparsh.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.aksharSparsh.Models.GridModel;
import com.example.aksharSparsh.R;

import java.util.ArrayList;

public class GridListAdapters extends ArrayAdapter<GridModel> {

    public GridListAdapters(@NonNull Context context, ArrayList<GridModel> gridModels) {
        super(context, 0, gridModels);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        HolderView holderview;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_view_item_list, parent, false);
            holderview = new HolderView(convertView);
            convertView.setTag(holderview);
        } else {
            holderview = (HolderView) convertView.getTag();

        }

        GridModel model = getItem(position);
        holderview.icons.setImageResource(model.getImage());
//        holderview.tv.setText(model.getTitle());
        return super.getView(position, convertView, parent);
    }

    private static class HolderView {

        private final ImageView icons;
//        private final TextView tv;

        private HolderView(ImageView icons, TextView tv) {
            this.icons = icons;
//            this.tv = tv;
        }

        public HolderView(View view) {
            icons = view.findViewById(R.id.image);
//            tv = view.findViewById(R.id.textViewImage);
        }
    }

}
