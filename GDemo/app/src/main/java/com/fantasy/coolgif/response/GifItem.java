package com.fantasy.coolgif.response;

/**
 * Created by fanlitao on 17/3/12.
 */

public class GifItem {

    public int id;
    public String gif_title;
    public String gif_url;
    public int like_info;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GifItem gifItem = (GifItem) o;

        return id == gifItem.id;

    }

    @Override
    public int hashCode() {
        int result = gif_title != null ? gif_title.hashCode() : 0;
        result = 31 * result + (gif_url != null ? gif_url.hashCode() : 0);
        return result;
    }
}
