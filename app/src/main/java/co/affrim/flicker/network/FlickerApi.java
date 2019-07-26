package co.affrim.flicker.network;

import java.util.Map;

import co.affrim.flicker.model.PhotosBaseResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Class to build Flicker API request
 */
public interface FlickerApi {

    @GET("services/rest/")
    Single<PhotosBaseResponse> searchFlicker(@QueryMap Map<String, String> options);
}
