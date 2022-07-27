package com.example.vicharan.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vicharan.Activity.ApartmentDetails;
import com.example.vicharan.Models.WishlistModel;
import com.example.vicharan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {
    Context context;
    List<WishlistModel> wishlist;
    FirebaseFirestore db;
    FirebaseUser curUser;
    FirebaseAuth auth;
    String userid = null;


    public WishlistAdapter(Context context, List<WishlistModel> wishlist) {
        this.context = context;
        this.wishlist = wishlist;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        curUser = auth.getCurrentUser();
        if (curUser != null) {
            userid = curUser.getUid();
        }

    }

    @NonNull
    @Override
    public WishlistAdapter.WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_wishlistactivity, parent, false);
        return new WishlistAdapter.WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WishlistAdapter.WishlistViewHolder holder, final int position) {
        Picasso.get().load(wishlist.get(position).getImage()).fit().into(holder.aptimage);

        holder.price.setText(wishlist.get(position).getPrice() + "$");
        holder.type.setText(wishlist.get(position).getType());
        holder.bedroom.setText(wishlist.get(position).getBedroom());
        holder.bathroom.setText(wishlist.get(position).getBathroom());
        holder.location.setText(wishlist.get(position).getLocation());

        holder.wishlistIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Query productIdRef = db.collection("Wishlist")
                        .whereEqualTo("Filter", userid + "_" + wishlist.get(position).getApartmentId());

                productIdRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                            wishlist.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(view.getContext(), "Removed from Wishlist", Toast.LENGTH_SHORT).show();
                            notifyItemRangeChanged(position, wishlist.size());
                        }
                    }


//

                });
            }
        });
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ApartmentDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("AptId", wishlist.get(position).getApartmentId());
                i.putExtras(bundle);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }

    public static final class WishlistViewHolder extends RecyclerView.ViewHolder {

        ImageView aptimage, wishlistIcon;
        TextView price, bedroom, bathroom, location, type;
        View item;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            aptimage = itemView.findViewById(R.id.wishlistimage);
            price = itemView.findViewById(R.id.wishlistPrice);
            bedroom = itemView.findViewById(R.id.bednumberwishlist);
            bathroom = itemView.findViewById(R.id.bathnumberwishlist);
            location = itemView.findViewById(R.id.addresswishlist);
            type = itemView.findViewById(R.id.apartmentwishlist);
            wishlistIcon = itemView.findViewById(R.id.wishlisticon);
            item = itemView;
        }
    }
}
