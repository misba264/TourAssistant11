package com.tourassistant.coderoids.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourassistant.coderoids.R;
import com.tourassistant.coderoids.helpers.AppHelper;
import com.tourassistant.coderoids.interfaces.onClickListner;
import com.tourassistant.coderoids.models.Profile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripRequestAdapter extends RecyclerView.Adapter<TripRequestAdapter.ViewHolder> {
    Context context;
    List<Profile> tripRequesArray;
    onClickListner onClickListner;
    JSONArray tripRequest;
    public TripRequestAdapter(Context applicationContext, List<Profile> tripRequesArray, onClickListner onClickListner, JSONArray tripRequest) {
        this.context = applicationContext;
        this.tripRequesArray = tripRequesArray;
        this.onClickListner = onClickListner;
        this.tripRequest = tripRequest;

    }

    @NonNull
    @Override
    public TripRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_joiners, viewGroup, false);
        return new TripRequestAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final TripRequestAdapter.ViewHolder viewHolder, int position) {
        position = viewHolder.getAdapterPosition();
        try {
            if(tripRequest != null) {
                JSONObject jsonObject = tripRequest.getJSONObject(position);
                if (jsonObject.has("status") && jsonObject.getString("status").matches("1")) {
                    viewHolder.btnFollow.setText("Added");
                    viewHolder.btnFollow.setBackgroundColor(context.getResources().getColor(R.color.green));

                } else {
                    viewHolder.btnFollow.setText("Accept Request");
                    viewHolder.btnFollow.setBackgroundColor(context.getResources().getColor(R.color.purple));
                }
            } else {
                if(AppHelper.currentProfileInstance.getUserId().matches(tripRequesArray.get(position).getUserId())){
                    viewHolder.btnFollow.setText("Admin");
                } else
                    viewHolder.btnFollow.setText("Member");
            }
            viewHolder.mtUserName.setText(tripRequesArray.get(position).getDisplayName());

            byte[] bytes = tripRequesArray.get(position).getProfileImage().toBytes();
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            viewHolder.userImage.setImageBitmap(bmp);
            int finalPosition = position;
            viewHolder.btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(viewHolder.btnFollow.getText().toString().matches("Accept Request") && onClickListner != null)
                        onClickListner.onClick(finalPosition,null);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return tripRequesArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView mtUserName;
        MaterialButton btnFollow;
        CircleImageView userImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtUserName = itemView.findViewById(R.id.tv_name);
            btnFollow = itemView.findViewById(R.id.btn_follow);
            userImage = itemView.findViewById(R.id.image);
        }
    }
}
