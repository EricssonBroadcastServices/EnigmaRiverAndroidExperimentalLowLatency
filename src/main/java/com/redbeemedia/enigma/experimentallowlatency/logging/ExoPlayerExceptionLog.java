package com.redbeemedia.enigma.experimentallowlatency.logging;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.redbeemedia.playersapplog.log.ILog;

public class ExoPlayerExceptionLog implements ILog {
    private final ExoPlaybackException error;

    public ExoPlayerExceptionLog(ExoPlaybackException error) {
        this.error = error;
    }

    @Override
    public String getLogType() {
        return "playback_error";
    }

    @Override
    public void appendTo(StringBuilder builder) {
        builder.append("Playback error : "+error.getClass().getSimpleName()+" "+error.getMessage()+"\n");
        for(StackTraceElement ste : error.getStackTrace()) {
            builder.append(ste + "\n");
        }
        Throwable cause = error.getCause();
        if(cause != null) {
            builder.append("Caused by: "+cause.getClass().getSimpleName()+" "+cause.getMessage()+"\n");
            for(StackTraceElement ste : cause.getStackTrace()) {
                builder.append(ste + "\n");
            }
        }
    }
}
