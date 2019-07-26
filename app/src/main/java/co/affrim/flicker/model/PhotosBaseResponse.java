package co.affrim.flicker.model;

import com.google.gson.annotations.SerializedName;


public class PhotosBaseResponse {

    @SerializedName("photos")
    PhotosResponse page;

    @SerializedName("stat")
    String stat;

    public PhotosResponse getPage() {
        return page;
    }

    public String getStat() {
        return stat;
    }
}
