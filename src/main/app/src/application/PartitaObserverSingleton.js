class PartitaObserverSingleton {
  constructor() {
    // Assicurati che l'array dei listeners sia inizializzato nel costruttore
    this.listeners = [];
    this.listenersEsiti = [];
  }

  // Metodo per aggiungere un listener
  	addListener(listener) {
    	this.listeners.push(listener);
  	}
  
	addListenerEsiti(listener) {
		this.listenersEsiti.push(listener);
	}
  
  // Metodo per rimuovere un listener
	removeListener(listener) {
		 this.listListener = this.listListener.filter(el => el !== listener);
	}
	
	removeListenerEsiti(listener) {
		this.listenersEsiti = this.listenersEsiti.filter(el => el !== listener);
		}

  // Metodo per notificare tutti i listener
  notifyListeners(partita) {
    console.log("partita in observer", partita);
    this.listeners.forEach((listener) => listener.updatePartita(partita));
  }
  
	notifyListenersEsiti(esiti) {
		console.log("esiti in observer", esiti);
    	this.listenersEsiti.forEach((listener) => listener(esiti));
	}

  // Implementazione del pattern Singleton
  static getInstance() {
    if (!PartitaObserverSingleton.instance) {
      PartitaObserverSingleton.instance = new PartitaObserverSingleton();
    }
    return PartitaObserverSingleton.instance;
  }
}

// Esporta un'istanza del singleton per l'uso nell'applicazione
export default PartitaObserverSingleton.getInstance();
