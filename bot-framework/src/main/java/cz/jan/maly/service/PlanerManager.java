package cz.jan.maly.service;

import cz.jan.maly.model.data.planing.Plan;

import java.util.List;

/**
 * Template for PlanerManager. PlanerManager uses planers to decide sequences of plans to execute.
 * Plans sequences from different Planers are then merged to single sequence.
 * Created by Jan on 09-Feb-17.
 *
 * @param <V> subtype of plan
 */
public abstract class PlanerManager<V extends Plan> implements Planer<V> {
    final List<Planer<V>> planers;

    public PlanerManager(List<Planer<V>> planers) {
        this.planers = planers;
    }
}
