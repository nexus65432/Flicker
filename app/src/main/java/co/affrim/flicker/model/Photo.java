package co.affrim.flicker.model;

import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("id")
    String photoId;

    @SerializedName("title")
    String title;

    @SerializedName("url_s")
    String imageUrl;

    @SerializedName("ispublic")
    long isPublic;

    @SerializedName("isfriend")
    long isFriend;

    @SerializedName("isfamily")
    long isFamily;

    @SerializedName("height_s")
    long imgHeight;

    @SerializedName("width_s")
    long imgWidth;

    public String getPhotoId() {
        return photoId;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean getIsPublic() {
        return isPublic == 0 ? false : true;
    }

    public boolean getIsFriend() {
        return isFriend == 0 ? false : true;
    }

    public boolean getIsFamily() {
        return isFamily == 0 ? false : true;
    }

    public long getImgHeight() {
        return imgHeight;
    }

    public long getImgWidth() {
        return imgWidth;
    }

}
