package com.redbeemedia.enigma.experimentallowlatency;

import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.redbeemedia.enigma.core.player.IPlaybackTechnologyIdentifier;

/*package-protected*/ class ExperimentalLowLatencyTechnologyIdentifier implements IPlaybackTechnologyIdentifier {
    private static final ExperimentalLowLatencyTechnologyIdentifier instance = new ExperimentalLowLatencyTechnologyIdentifier();

    @Override
    public String getTechnologyName() {
        return "ExperimentalLowLatency_"+ExoPlayerLibraryInfo.TAG;
    }

    @Override
    public String getTechnologyVersion() {
        return ExoPlayerLibraryInfo.VERSION;
    }


    public static ExperimentalLowLatencyTechnologyIdentifier get() {
        return instance;
    }
}
