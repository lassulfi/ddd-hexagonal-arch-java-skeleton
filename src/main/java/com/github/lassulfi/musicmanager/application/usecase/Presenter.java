package com.github.lassulfi.musicmanager.application.usecase;

public interface Presenter<IN, OUT> {
    OUT present(IN input);

    OUT present(Throwable error);
}
