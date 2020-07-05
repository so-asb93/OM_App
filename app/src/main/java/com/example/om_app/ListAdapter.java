package com.example.om_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<Joueurs> values;
    public Context c;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView photo;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            photo = (ImageView) v.findViewById(R.id.icon) ;
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);

        }
    }

    public void add(int position, Joueurs item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListAdapter(List<Joueurs> myDataset,Context context) {
        values = myDataset;
        c = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Joueurs currentJoueur = values.get(position);
        holder.txtHeader.setText(currentJoueur.getName());
        holder.txtFooter.setText(currentJoueur.getPosition());
        Glide.with(holder.itemView.getContext()).load(currentJoueur.getImage()).into(holder.photo);
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openNewActivity();
                Intent intent = new Intent(c, JoueursDetails.class);
                intent.putExtra("name", currentJoueur.getName());
                intent.putExtra("position", currentJoueur.getPosition());
                intent.putExtra("number", currentJoueur.getNumber());
                intent.putExtra("age", currentJoueur.getAge());
                intent.putExtra("height", currentJoueur.getHeight());
                intent.putExtra("nationality", currentJoueur.getNationality());
                intent.putExtra("transfer", currentJoueur.getTransfer_value());
                intent.putExtra("end", currentJoueur.getEnd_contract());

                c.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }
}
