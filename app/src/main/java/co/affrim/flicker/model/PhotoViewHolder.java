package co.affrim.flicker.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import co.affrim.flicker.R;

/**
 *  A ViewHolder object stores each of the component views inside the tag field of the Layout,
 *  so you can immediately access them without the need to look them up repeatedly
 */
public class PhotoViewHolder extends RecyclerView.ViewHolder {

    public ImageView mFlickerImage;
    public TextView mFlickerTitle;


    public PhotoViewHolder(View itemView) {
        super(itemView);

        mFlickerImage = itemView.findViewById(R.id.image_view);
        mFlickerTitle = itemView.findViewById(R.id.image_text);
    }
}
