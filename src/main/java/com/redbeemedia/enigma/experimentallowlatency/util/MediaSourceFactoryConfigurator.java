package com.redbeemedia.enigma.experimentallowlatency.util;

import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.redbeemedia.enigma.core.player.IPlayerImplementationControls;
import com.redbeemedia.enigma.core.time.Duration;

/**
 * Configures MediaSourceFactories based on a {@link com.redbeemedia.enigma.core.player.IPlayerImplementationControls.ILoadRequest}.
 */
public class MediaSourceFactoryConfigurator {
    private final IPlayerImplementationControls.ILoadRequest loadRequest;
    private final DrmSessionManager<ExoMediaCrypto> drmSessionManager;

    public MediaSourceFactoryConfigurator(IPlayerImplementationControls.ILoadRequest loadRequest, DrmSessionManager<ExoMediaCrypto> drmSessionManager) {
        this.loadRequest = loadRequest;
        this.drmSessionManager = drmSessionManager;
    }

    public DashMediaSource.Factory configure(DashMediaSource.Factory factory) {
        configureInternal(new IMediaSourceFactoryAdapter() {
            @Override
            public void setLiveDelay(Duration liveDelay) {
                factory.setLivePresentationDelayMs(liveDelay.inWholeUnits(Duration.Unit.MILLISECONDS), true);
            }

            @Override
            public void setDrmSessionManager(DrmSessionManager<ExoMediaCrypto> drmSessionManager) {
                factory.setDrmSessionManager(drmSessionManager);
            }
        });
        return factory;
    }

    public SsMediaSource.Factory configure(SsMediaSource.Factory factory) {
        configureInternal(new IMediaSourceFactoryAdapter() {
            @Override
            public void setLiveDelay(Duration liveDelay) {
                factory.setLivePresentationDelayMs(liveDelay.inWholeUnits(Duration.Unit.MILLISECONDS));
            }

            @Override
            public void setDrmSessionManager(DrmSessionManager<ExoMediaCrypto> drmSessionManager) {
                factory.setDrmSessionManager(drmSessionManager);
            }
        });
        return factory;
    }


    public HlsMediaSource.Factory configure(HlsMediaSource.Factory factory) {
        configureInternal(new IMediaSourceFactoryAdapter() {
            @Override
            public void setLiveDelay(Duration liveDelay) {
                // Not supported
            }

            @Override
            public void setDrmSessionManager(DrmSessionManager<ExoMediaCrypto> drmSessionManager) {
                factory.setDrmSessionManager(drmSessionManager);
            }
        });
        return factory;
    }

    public ExtractorMediaSource.Factory configure(ExtractorMediaSource.Factory factory) {
        configureInternal(new IMediaSourceFactoryAdapter() {
            @Override
            public void setLiveDelay(Duration liveDelay) {
                // Not supported
            }

            @Override
            public void setDrmSessionManager(DrmSessionManager<ExoMediaCrypto> drmSessionManager) {
                // Not supported
            }
        });
        return factory;
    }

    private void configureInternal(IMediaSourceFactoryAdapter factoryAdapter) {
        Duration duration = loadRequest.getLiveDelay();
        if(duration != null) {
            factoryAdapter.setLiveDelay(duration);
        }
        if(drmSessionManager != null) {
            factoryAdapter.setDrmSessionManager(drmSessionManager);
        }
    }

    private interface IMediaSourceFactoryAdapter {
        void setLiveDelay(Duration liveDelay);
        void setDrmSessionManager(DrmSessionManager<ExoMediaCrypto> drmSessionManager);
    }
}
