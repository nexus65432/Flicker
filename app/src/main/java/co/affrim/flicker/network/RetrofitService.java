package co.affrim.flicker.network;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import co.affrim.flicker.model.PhotosBaseResponse;
import co.affrim.flicker.util.Constant;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rest interface to fetch data from server
 */
public class RetrofitService {

    private static int pageCounter = 1;
    private static int IMAGES_PER_PAGE = 75;

    private Retrofit mRetroFit;
    private FlickerApi mFlickerApi;

    private HashSet<String> userQueryStack = new HashSet<>();

    private static class SingletonRetroFitServiceHelper {
        private static final RetrofitService INSTANCE = new RetrofitService();
    }

    public static RetrofitService getInstance() {
        return SingletonRetroFitServiceHelper.INSTANCE;
    }

    private RetrofitService() {
        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setLenient()
                .create();
        mRetroFit = new Retrofit.Builder().baseUrl(Constant.FLICKER_END_POINT)
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    public Single<PhotosBaseResponse> getSearchResults(@NonNull String userQuery) {
        mFlickerApi = mRetroFit.create(FlickerApi.class);
        checkQuery(userQuery);
        return mFlickerApi.searchFlicker(queryBuilder(userQuery));
    }

    /**
     * Build the quiry with input params
     * @param query
     * @return
     */
    private Map<String, String> queryBuilder(@NonNull String query) {

        Map<String, String> queryMap = new HashMap<>();

        queryMap.put("method", "flickr.photos.search");
        queryMap.put("api_key", "675894853ae8ec6c242fa4c077bcf4a0");
        queryMap.put("extras", "url_s");
        queryMap.put("format", "json");
        queryMap.put("nojsoncallback", "1");
        if (!TextUtils.isEmpty(query)) {
            userQueryStack.add(query);
            queryMap.put("text", query);
        }
        queryMap.put("per_page", String.valueOf(IMAGES_PER_PAGE));
        queryMap.put("page", String.valueOf(pageCounter));

        return queryMap;
    }

    private void checkQuery(@NonNull String query) {
        if (!TextUtils.isEmpty(query)) {
            userQueryStack.add(query);
            if (userQueryStack.contains(query)) {
                ++pageCounter;
            }
        }
    }
}
