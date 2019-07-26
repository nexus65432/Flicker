package co.affrim.flicker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotosResponse {

    @SerializedName("page")
    long page;

    @SerializedName("pages")
    long pages;

    @SerializedName("perpage")
    long perpage;

    @SerializedName("photo")
    List<Photo> photo;

    public long getPage() {
        return page;
    }

    public long getPages() {
        return pages;
    }

    public long getPerpage() {
        return perpage;
    }

    public List<Photo> getPhoto() {
        return photo;
    }
}
