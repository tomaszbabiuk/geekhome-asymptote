package eu.geekhome.asymptote.services;


import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.RelayValue;

public interface AutomationAddedListener {
    void onAutomationAdded(Automation trigger);
    void onAutomationEdit(Automation automation);
}
