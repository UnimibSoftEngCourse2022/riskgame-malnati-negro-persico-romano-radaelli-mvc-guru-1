class PartitaObserverSingleton {
  constructor() {
    // Assicurati che l'array dei listeners sia inizializzato nel costruttore
    this.listeners = [];
  }

  // Metodo per aggiungere un listener
  addListener(listener) {
    this.listeners.push(listener);
  }

  // Metodo per notificare tutti i listener
  notifyListeners(partita) {
    console.log("partita in observer", partita);
    this.listeners.forEach((listener) => listener.updatePartita(partita));
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
