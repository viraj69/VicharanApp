package com.example.vicharan.Activity.prasangList.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vicharan.Activity.prasangList.ui.PrasangListItemUi;
import com.example.vicharan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;

public class PrasangListAdapter extends RecyclerView.Adapter<PrasangListAdapter.PostListViewHolder> {
    Context context;
    List<PrasangListItemUi> postlist = new LinkedList<>();
    FirebaseFirestore db;
    FirebaseUser curUser;
    FirebaseAuth auth;
    String userid = null;


    public PrasangListAdapter(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        curUser = auth.getCurrentUser();
        if (curUser != null) {
            userid = curUser.getUid();
        }
    }

    public void clear() {
        this.postlist.clear();
        notifyDataSetChanged();
    }

    public void setData(List<PrasangListItemUi> list) {
        if (list != null) {
            this.postlist.addAll(list);
            notifyDataSetChanged();
        } else {
            clear();
        }
    }

    @NonNull
    @Override
    public PrasangListAdapter.PostListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.postlist_item, parent, false);
        return new PrasangListAdapter.PostListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PrasangListAdapter.PostListViewHolder holder, final int pos) {
        int position = holder.getAdapterPosition();
        PrasangListItemUi prasangListItemUi = postlist.get(position);
        prasangListItemUi.onViewCreated(holder.getView());
        prasangListItemUi.init();
    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }


    public static final class PostListViewHolder extends RecyclerView.ViewHolder {
        public PostListViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public View getView() {
            return super.itemView;
        }
    }
}
