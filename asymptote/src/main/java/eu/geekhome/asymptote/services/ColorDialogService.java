package eu.geekhome.asymptote.services;

public interface ColorDialogService {
    void pick(int currentColor, final ColorPickedListener colorPickedListener);
}
