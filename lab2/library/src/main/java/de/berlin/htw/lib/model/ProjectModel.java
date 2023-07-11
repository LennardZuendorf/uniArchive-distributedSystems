package de.berlin.htw.lib.model;

import java.util.List;

/**
 * @author Lennard Zündorf [s0568383@htw-berlin.de]
 * mostly copied from UserModel.java by Alexander Stanik
 */
public interface ProjectModel {

    String getId();

    String getName();

    List<? extends UserModel> getUsers();
}