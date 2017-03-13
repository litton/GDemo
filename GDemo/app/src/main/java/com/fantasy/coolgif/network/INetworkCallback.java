package com.fantasy.coolgif.network;

import com.fantasy.coolgif.response.GifResponse;

/**
 * Created by fanlitao on 17/3/9.
 */

public interface INetworkCallback {


    public void onResponse(GifResponse response);
    public void onDeleteCompeletd(String gifUrl);
}
