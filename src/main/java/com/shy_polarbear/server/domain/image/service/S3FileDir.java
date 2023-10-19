package com.shy_polarbear.server.domain.image.service;


public enum S3FileDir {
    PROFILE("profile"),
    FEED("feed");
    final String path;
    S3FileDir(String path) {
        this.path = path;
    }
}
