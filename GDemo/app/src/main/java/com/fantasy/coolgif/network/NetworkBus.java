package com.fantasy.coolgif.network;


import android.util.Log;

import com.fantasy.coolgif.response.GifResponse;
import com.fantasy.coolgif.response.LikeResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by fanlitao on 17/3/9.
 */

public class NetworkBus {


    public static String HOST_NAME;

    static {

//        if (NetWorkUtil.isOffice()) {
//            HOST_NAME = "http://cp01-rdqa-dev366.cp01.baidu.com";
//        } else {
//            HOST_NAME = "http://192.168.2.105";
//        }

        HOST_NAME = "http://www.xiaotk1.com/app/coolgif/";

    }

    private static final NetworkBus sInstance = new NetworkBus();

    public static NetworkBus getDefault() {
        return sInstance;
    }


    private NetworkBus() {
        initNetworkBus();
    }

    private Retrofit mRetrofit;

    public void initNetworkBus() {
        //String baseUrl = HOST_NAME + ":8000/gif_api/";
        String baseUrl = HOST_NAME;
        //获取实例
        mRetrofit = new Retrofit.Builder()
                //设置OKHttpClient,如果不设置会提供一个默认的
                .client(new OkHttpClient())
                //设置baseUrl
                .baseUrl(baseUrl)
                //添加FastJson转换器
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();

    }

//    public void requestImageList(String categoryId,int page, final INetworkCallback callback) {
//        final TuBaApi repo = mRetrofit.create(TuBaApi.class);
//        String categoryJson = "category_" + TUBA_ID;
//        if (page >  0) {
//            categoryJson = categoryJson + "_" + page;
//        }
//
//        final Call<ResponseBody> call = repo.getImageList(categoryId, categoryJson);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                //LogUtil.v("fan","response:" + response.code() + ":" + response.isSuccessful() + ":" + response);
//                if (response.isSuccessful()) {
//                    try {
//                        final String jsonString = new String(response.body().bytes(), "utf-8");
//                        JSONObject object = new JSONObject(jsonString);
//                        if (callback != null) {
//                            callback.onResponse(object);
//                        }
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//    }

    public void getTopGifList(final INetworkCallback callback) {
        final CoolGifAPI repo = mRetrofit.create(CoolGifAPI.class);

        final Call<GifResponse> call = repo.getTopGifList();
        call.enqueue(new Callback<GifResponse>() {
            @Override
            public void onResponse(Call<GifResponse> call, Response<GifResponse> response) {
                Log.v("fan", "onResponse:" + response);
                if (response.isSuccessful()) {
                    callback.onResponse(response.body());
                } else {
                    callback.onFailed();
                }
            }

            @Override
            public void onFailure(Call<GifResponse> call, Throwable t) {
                Log.v("fan", "onFailure" + call.toString());
                t.printStackTrace();
            }
        });
    }

    public void getTopGifList(int pos, final INetworkCallback callback) {

        
        final CoolGifAPI repo = mRetrofit.create(CoolGifAPI.class);

        final Call<GifResponse> call = repo.getTopGifList(pos);
        call.enqueue(new Callback<GifResponse>() {
            @Override
            public void onResponse(Call<GifResponse> call, Response<GifResponse> response) {
                Log.v("fan", "onResponse:" + response);
                if (response.isSuccessful()) {
                    callback.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<GifResponse> call, Throwable t) {
                Log.v("fan", "onFailure" + call.toString());
                t.printStackTrace();
                if (callback != null) {
                    callback.onFailed();
                }
            }
        });
    }

    public void deleteGifById(final int gifId, final INetworkCallback callback) {
        final CoolGifAPI repo = mRetrofit.create(CoolGifAPI.class);
        final Call<GifResponse> call = repo.deleteGifUrl(gifId);
        call.enqueue(new Callback<GifResponse>() {
            @Override
            public void onResponse(Call<GifResponse> call, Response<GifResponse> response) {
                Log.v("fan", "onResponse:" + response);
                if (response.isSuccessful()) {
                    if (callback != null) {
                        callback.onDeleteCompeletd(gifId);
                    }
                }
            }

            @Override
            public void onFailure(Call<GifResponse> call, Throwable t) {
                Log.v("fan", "onFailure" + call.request().url());
                t.printStackTrace();
            }
        });
    }

    public void likeGifById(final int gifId, final ILikeGifCallback callback) {
        final CoolGifAPI repo = mRetrofit.create(CoolGifAPI.class);
        final Call<LikeResponse> call = repo.likeGifById(gifId);
        call.enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                Log.v("fan", "onResponse:" + response);
                if (response.isSuccessful()) {
                    if (callback != null) {
                        callback.onSucessful(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<LikeResponse> call, Throwable t) {
                Log.v("fan", "onFailure" + call.request().url());
                t.printStackTrace();
            }
        });
    }

    public interface ILikeGifCallback {
        public void onSucessful(LikeResponse response);
    }


    public interface CoolGifAPI {
        // @GET("{category_id}/category/{json_id}.json")
        //Call<ResponseBody> getImageList(@Path("category_id") String categoryId, @Path("json_id") String jsonId);
        @GET("gif_main.json")
        Call<GifResponse> getTopGifList();

        @GET("get_gif")
        Call<GifResponse> getTopGifList(@Query("pos") int pos);

        @GET("delete")
        Call<GifResponse> deleteGifUrl(@Query("id") int gifId);

        @GET("like")
        Call<LikeResponse> likeGifById(@Query("id") int gifId);


    }

}
