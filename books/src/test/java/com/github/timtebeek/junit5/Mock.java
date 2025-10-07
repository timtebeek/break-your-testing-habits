package com.github.timtebeek.junit5;

import org.opentest4j.AssertionFailedError;

public class Mock {
    private final boolean verifyMode;
    private boolean proceeded;
    public static Mock verify(Mock mock) {
        return new Mock(true, mock.proceeded);
    }

    public Mock(){
        this(false, false);
    }

    private Mock(boolean isVerifyMode, boolean proceeded) {
        this.verifyMode = isVerifyMode;
        this.proceeded = proceeded;
    }
    public void method() {
        if (this.verifyMode) {
            if (!this.proceeded) {
                throw new AssertionFailedError("method() isn't proceeded at least one time");
            }
        } else {
            this.proceeded = true;
        }
    }
}
