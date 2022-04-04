package com.redbeemedia.enigma.experimentallowlatency.tracks;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.redbeemedia.enigma.core.audio.IAudioTrack;
import com.redbeemedia.enigma.experimentallowlatency.ExoUtil;

public final class ExoAudioTrack extends AbstractExoTrack implements IAudioTrack {
    public ExoAudioTrack(String language) {
        super(language);
    }

    @Override
    public void applyTo(DefaultTrackSelector trackSelector) {
        DefaultTrackSelector.ParametersBuilder parametersBuilder = trackSelector.buildUponParameters();
        parametersBuilder.setPreferredAudioLanguage(getLanguageCode());
        parametersBuilder.setRendererDisabled(ExoUtil.DEFAULT_AUDIO_RENDERER_INDEX, false);
        trackSelector.setParameters(parametersBuilder.build());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof ExoAudioTrack && super.equals(obj);
    }

    @Override
    public String getLabel() {
        return super.getLanguageCode();
    }

    @Override
    public String getCode() {
        return super.getLanguageCode();
    }

    @Override
    public String getTrackId() {
        return null;
    }
}
