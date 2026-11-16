package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.architecture.Domain;
import ru.mipt.bit.platformer.architecture.Port;

@Domain
@Port
public interface LevelListener {

    void onObjectAdded(GameObject object);

    void onObjectRemoved(GameObject object);
}
