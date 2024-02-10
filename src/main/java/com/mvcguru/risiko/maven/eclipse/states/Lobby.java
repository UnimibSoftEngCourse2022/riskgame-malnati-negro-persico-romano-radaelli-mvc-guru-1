package com.mvcguru.risiko.maven.eclipse.states;


import com.mvcguru.risiko.maven.eclipse.exception.PartitaPienaException;
import com.mvcguru.risiko.maven.eclipse.states.Lobby;

public class Lobby extends State {

    @Override
    public void acceptRiprendi(State statoPartita) {
        statoPartita.riprendi(this);
    }

    @Override
    public void onAzioneGiocatore(EntraInPartita entraInPartita) {
        partita.fermaAttesa();
        // Aggiorna i client e poi si rimette in attesa se non è stato raggiunto il numero
        try {
            partita.aggiungiGiocatore(entraInPartita.getGiocatore());
        } catch (GiocatoreEsistenteException e) {
            // Lancia un messaggio di errore
        }
        if (partita.getGiocatori().size() == partita.getConfig().getNumeroGiocatori()) {
            partita.inizioPartita();
        }
    }

	@Override
	public void startGame() throws PartitaPienaException {
		// TODO Auto-generated method stub
		
	}
}
