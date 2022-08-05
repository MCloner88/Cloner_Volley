package com.example.clonervolley.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clonervolley.R;
import com.example.clonervolley.app.app;
import com.example.clonervolley.interfaces.MultiAction_interface;
import com.example.clonervolley.models.images_model;
import com.example.clonervolley.views.ShowImage_activity;

import java.util.List;

public class images_adapter extends RecyclerView.Adapter<images_adapter.MyViewHolder>
{
    List<images_model> list;
    Context context ;
    Activity activity;

    MultiAction_interface multiAction_interface ;

    public static boolean multiAction = false;
    public static int count     = 0;


    public images_adapter (List<images_model> list , Context context , Activity activity , MultiAction_interface multiAction_interface){
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.multiAction_interface = multiAction_interface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_images, parent , false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(app.BASE_URL+ list.get(position) .getImage()).into(holder.image_view);

        holder.checkbox.setVisibility(View.GONE);
        holder.checkbox.setChecked(false);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout parent;
        ImageView image_view;
        CheckBox  checkbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            image_view = itemView.findViewById(R.id.image_view);
            checkbox = itemView.findViewById(R.id.checkbox);

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(multiAction) {
                        if(count>0){
                            if(!list.get(getAdapterPosition()).getMultiAction()){
                                list.get(getAdapterPosition()).setMultiAction(true) ;
                                multiAction_interface.selected(++count , getAdapterPosition());
                                checkbox.setVisibility(View.VISIBLE);
                                checkbox.setChecked(true);
                            }else {
                                list.get(getAdapterPosition()).setMultiAction(false);
                                multiAction_interface.deSelected(--count,getAdapterPosition());
                                checkbox.setVisibility(View.GONE);
                                checkbox.setChecked(false);
                            }
                        }else {
                            multiAction = false;
                        }
                    }
                    if(!multiAction){
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, image_view , "image");
                        Intent intent = new Intent(context , ShowImage_activity.class);
                        intent.putExtra("link", list.get(getAdapterPosition()).getImage());
                        context.startActivity(intent,activityOptionsCompat.toBundle());
                    }

                }
            });

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(count == 0 || !multiAction){
                        multiAction_interface.start();
                        multiAction_interface.selected(++count,getAdapterPosition());
                        
                        multiAction = true ;
                        list.get(getAdapterPosition()).setMultiAction(true);

                        checkbox.setVisibility(View.VISIBLE);
                        checkbox.setChecked(true);

                    }
                    return true;
                }
            });
        }
    }
}
