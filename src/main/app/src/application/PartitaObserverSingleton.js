// Controllo del Tipo: Nel metodo notifyListeners, ho aggiunto un controllo per assicurarmi che listener.updatePartita
//sia effettivamente una funzione prima di chiamarla.
//Questo passo è importante in JavaScript per evitare errori a runtime
//dato che non abbiamo il controllo dei tipi a compile-time come in TypeScript.

// Esportazione dell'Istanza: Infine,
// ho esportato direttamente l'istanza singleton con export default instance;
// e ho utilizzato Object.freeze(instance) per prevenire ulteriori modifiche all'istanza,
// rendendo il pattern singleton più robusto in JavaScript.

class PartitaObserverSingleton {
  constructor() {
    if (!PartitaObserverSingleton.instance) {
      this.listListeners = [];
      PartitaObserverSingleton.instance = this;
    }
    return PartitaObserverSingleton.instance;
  }

  addListener(listener) {
    this.listListeners.push(listener);
  }

  removeListener(listener) {
    this.listListeners = this.listListeners.filter((el) => el !== listener);
  }

  notifyListeners(partita) {
    this.listListeners.forEach((listener) => {
      if (typeof listener.updatePartita === "function") {
        listener.updatePartita(partita);
      }
    });
  }
}

// Assicurati che l'istanza sia unica
const instance = new PartitaObserverSingleton();
Object.freeze(instance);

export default instance;
