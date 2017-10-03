package eu.geekhome.asymptote.services.impl;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HttpListAutomationResponse {
    @SerializedName("dt")
    private ArrayList<HttpDateTimeAutomationResponse> _dateTimeAutomations;

    @SerializedName("sr")
    private ArrayList<HttpSchedulerAutomationResponse> _schedulerAutomations;

    public ArrayList<HttpDateTimeAutomationResponse> getDateTimeAutomations() {
        return _dateTimeAutomations;
    }

    public ArrayList<HttpSchedulerAutomationResponse> getSchedulerAutomations() {
        return _schedulerAutomations;
    }
}
