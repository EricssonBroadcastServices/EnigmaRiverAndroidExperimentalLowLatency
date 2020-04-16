package com.redbeemedia.enigma.experimentallowlatency.ui;

import com.google.android.exoplayer2.ui.TimeBar;
import com.redbeemedia.enigma.core.player.IEnigmaPlayer;
import com.redbeemedia.enigma.core.player.timeline.BaseTimelineListener;
import com.redbeemedia.enigma.core.player.timeline.ITimeline;
import com.redbeemedia.enigma.core.player.timeline.ITimelinePosition;
import com.redbeemedia.enigma.core.time.Duration;
import com.redbeemedia.enigma.core.util.AndroidThreadUtil;

public class TimeBarUtil {

    public static void connect(TimeBar timeBar, IEnigmaPlayer enigmaPlayer) {
        new TimeBarHelper(timeBar, enigmaPlayer);
    }

    private static class TimeBarHelper {
        private final TimeBar timeBar;
        private ITimelinePosition currentStartBound;
        private ITimelinePosition currentEndBound;
        private ITimelinePosition currentPosition;

        public TimeBarHelper(TimeBar timeBar, IEnigmaPlayer enigmaPlayer) {
            this.timeBar = timeBar;
            ITimeline timeline = enigmaPlayer.getTimeline();
            currentStartBound = timeline.getCurrentStartBound();
            currentEndBound = timeline.getCurrentEndBound();
            currentPosition = timeline.getCurrentPosition();
            updateTimeBar();
            timeline.addListener(new BaseTimelineListener() {
                @Override
                public void onCurrentPositionChanged(ITimelinePosition timelinePosition) {
                    currentPosition = timelinePosition;
                    updateTimeBar();
                }

                @Override
                public void onBoundsChanged(ITimelinePosition start, ITimelinePosition end) {
                    currentStartBound = start;
                    currentEndBound = end;
                    updateTimeBar();
                }

                @Override
                public void onVisibilityChanged(boolean visible) {
                    AndroidThreadUtil.runOnUiThread(() -> {
                        timeBar.setEnabled(visible);
                    });
                }
            });
            timeBar.addListener(new TimeBar.OnScrubListener() {
                @Override
                public void onScrubStart(TimeBar timeBar, long position) {
                }

                @Override
                public void onScrubMove(TimeBar timeBar, long position) {
                }

                @Override
                public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                    if(!canceled) {
                        AndroidThreadUtil.runOnUiThread(() -> {
                            if(currentStartBound != null) {
                                enigmaPlayer.getControls().seekTo(currentStartBound.add(Duration.millis(position)));
                            } else {
                                enigmaPlayer.getControls().seekTo(position);
                            }
                        });
                    }
                }
            });
        }

        private void updateTimeBar() {
            AndroidThreadUtil.runOnUiThread(() -> {
                timeBar.setDuration(getDurationInMillis());
                timeBar.setPosition(getPositionInMillis());
            });
        }

        private long getDurationInMillis() {
            if(currentEndBound != null && currentStartBound != null) {
                return currentEndBound.subtract(currentStartBound).inWholeUnits(Duration.Unit.MILLISECONDS);
            } else {
                return 0L;
            }
        }

        private long getPositionInMillis() {
            if(currentPosition != null && currentStartBound != null) {
                return currentPosition.subtract(currentStartBound).inWholeUnits(Duration.Unit.MILLISECONDS);
            } else {
                return 0L;
            }
        }
    }
}
