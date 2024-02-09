package com.mvcguru.risiko.maven.eclipse.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameRepository {
    private static GameRepository instance;
    
    private final HashMap<String, IPartita> partite;
    private final HashMap<String, Giocatore> giocatori;

    private GameRepository() {
        partite = new HashMap<>();
        giocatori = new HashMap<>();
    }

    public static synchronized GameRepository getInstance() {
        if (instance == null) {
            instance = new GameRepository();
        }

        return instance;
    }

    public synchronized void rimuoviGiocatoreById(String idSessione) {
        giocatori.remove(idSessione);
    }

    public synchronized Giocatore getGiocatoreByIdSessione(String idSessione) {
        return giocatori.get(idSessione);
    }

    public synchronized void registraGiocatore(String idSessione, Giocatore giocatore) {
        giocatori.put(idSessione, giocatore);
    }

    public synchronized IPartita getPartitaById(String id) {
        return partite.get(id);
    }

    public synchronized void addPartita(IPartita partita) {
        partite.put(partita.getId(), partita);
    }

    public synchronized List<IPartita> getPartiteAperte() {
        return new ArrayList<>(partite.values());
    }

    public synchronized void rimuoviPartitaById(String id) {
        partite.remove(id);
    }
}
