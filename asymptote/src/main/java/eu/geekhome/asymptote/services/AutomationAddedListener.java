package eu.geekhome.asymptote.services;


import eu.geekhome.asymptote.model.Automation;

public interface AutomationAddedListener {
    void onAutomationAdded(Automation trigger);
}
