package com.redbeemedia.enigma.experimentallowlatency;

import com.google.android.exoplayer2.source.MediaSource;
import com.redbeemedia.enigma.experimentallowlatency.util.MediaSourceFactoryConfigurator;

public interface IMediaSourceFactory {
    MediaSource createMediaSource(MediaSourceFactoryConfigurator configurator);
}
