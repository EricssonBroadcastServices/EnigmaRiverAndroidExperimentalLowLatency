package com.redbeemedia.enigma.experimentallowlatency;

import android.os.Looper;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.redbeemedia.enigma.core.util.OpenContainer;
import com.redbeemedia.enigma.core.util.OpenContainerUtil;

/*package-protected*/ class EnigmaDrmSessionManager<T extends ExoMediaCrypto> implements DrmSessionManager<T> {
    private final IDrmSessionManagerFactory<T> sessionManagerFactory;
    private final DefaultDrmSessionManager<T> normalSessionManager;
    private final OpenContainer<DrmSessionManager<T>> activeSessionManager;

    public EnigmaDrmSessionManager(ExoMediaDrm<T> mediaDrm, MediaDrmCallback mediaDrmCallback) {
        sessionManagerFactory = () -> {
            DefaultDrmSessionManager<ExoMediaCrypto> sessionManager = new DefaultDrmSessionManager.Builder().setUuidAndExoMediaDrmProvider(C.WIDEVINE_UUID, new ExoMediaDrm.AppManagedProvider<T>(mediaDrm)).build(mediaDrmCallback);
            sessionManager.prepare();
            return (DefaultDrmSessionManager<T>) sessionManager;
        };
        normalSessionManager = sessionManagerFactory.newDrmSessionManager();
        activeSessionManager = new OpenContainer<>(normalSessionManager);
    }

    @Nullable
    @Override
    public Class<? extends ExoMediaCrypto> getExoMediaCryptoType(DrmInitData drmInitData) {
        return OpenContainerUtil.getValueSynchronized(activeSessionManager).getExoMediaCryptoType(drmInitData);
    }

    @Override
    public boolean canAcquireSession(DrmInitData drmInitData) {
        return OpenContainerUtil.getValueSynchronized(activeSessionManager).canAcquireSession(drmInitData);
    }

    @Override
    public DrmSession<T> acquireSession(Looper playbackLooper, DrmInitData drmInitData) {
        DrmSessionManager<T> sessionManager = OpenContainerUtil.getValueSynchronized(activeSessionManager);
        DrmSession<T> drmSession = sessionManager.acquireSession(playbackLooper, drmInitData);
        return drmSession;
    }

    public void reset() {
        OpenContainerUtil.setValueSynchronized(activeSessionManager, normalSessionManager, null);
    }

    public void useOfflineManager(byte[] drmKeys) {
        DefaultDrmSessionManager<T> drmSessionManager = sessionManagerFactory.newDrmSessionManager();
        drmSessionManager.setMode(DefaultDrmSessionManager.MODE_QUERY, drmKeys);
        OpenContainerUtil.setValueSynchronized(activeSessionManager, drmSessionManager, null);
    }

    private interface IDrmSessionManagerFactory<T extends ExoMediaCrypto> {
        DefaultDrmSessionManager<T> newDrmSessionManager();
    }
}
