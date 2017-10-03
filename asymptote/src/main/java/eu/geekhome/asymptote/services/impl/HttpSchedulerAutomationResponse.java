package eu.geekhome.asymptote.services.impl;

import com.google.gson.annotations.SerializedName;

public class HttpSchedulerAutomationResponse extends HttpDateTimeAutomationResponse {
    @SerializedName("dys")
    private int _days;

    public int getDays() {
        return _days;
    }
}
