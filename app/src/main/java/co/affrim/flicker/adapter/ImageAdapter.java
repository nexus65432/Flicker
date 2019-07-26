package co.affrim.flicker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import co.affrim.flicker.R;
import co.affrim.flicker.listener.CleanUp;
import co.affrim.flicker.model.LoadingViewHolder;
import co.affrim.flicker.model.Photo;
import co.affrim.flicker.model.PhotoViewHolder;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements CleanUp {

    // This type has valid data
    private final int VIEW_TYPE_ITEM = 0;
    // This is to show loading indicator
    private final int VIEW_TYPE_LOADING = 1;

    private Context mContext;
    private List<Photo> mPhotosList = new ArrayList<>();
    private HashMap<String, String> aa;
    private TreeMap<String, String> a;
    private HashSet<String> hashSet;
    private Object o;

    private SparseArray<Integer> sparseArray;
    private ArrayMap<String, String> arrayMap;


    public ImageAdapter(@NonNull Context context, @NonNull List<Photo> photos) {
        mContext = context;
        mPhotosList = photos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View itemView = LayoutInflater.from(mContext)
                    .inflate(R.layout.flicker_photos_layout, parent, false);
            return new PhotoViewHolder(itemView);
        } else {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Photo p = mPhotosList.get(position);

        if (holder instanceof PhotoViewHolder) {

            populateItemRows((PhotoViewHolder) holder, position);

        } else if (holder instanceof LoadingViewHolder) {

            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mPhotosList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        int retVal = 0;
        if (mPhotosList != null) {
            retVal = mPhotosList.size();
        }
        return retVal;
    }

    private void populateItemRows(PhotoViewHolder viewHolder, int position) {

        final Photo photo = mPhotosList.get(position);
        if (photo != null) {
            String imgUrl = photo.getImageUrl();
            if (imgUrl != null) {
                Glide.with(mContext)
                        .load(imgUrl)
                        .centerCrop()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_menu_camera)
                        .error(R.drawable.ic_menu_camera)
                        //ToDo placeholder and error can be improved
                        .into(viewHolder.mFlickerImage);
            }

            final String imgTitle = photo.getTitle();
            if (!TextUtils.isEmpty(imgTitle)) {
                viewHolder.mFlickerTitle.setText(imgTitle);
            } else {
                viewHolder.mFlickerTitle.setText("title");
            }
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed
    }

    public void addList(List<Photo> photos) {
        mPhotosList = photos;
    }

    public void replaceWithNewList(List<Photo> photos) {
        mPhotosList = photos;
    }

    @Override
    public void onDestroy() {
        mContext = null;
    }

}
