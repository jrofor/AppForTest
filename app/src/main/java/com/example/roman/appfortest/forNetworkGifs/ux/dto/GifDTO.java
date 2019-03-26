package com.example.roman.appfortest.forNetworkGifs.ux.dto;

public class GifDTO {

    private ImagesDTO images;

    public String getUrl() {
        return images.getOriginal().getUrl();
    }
}
