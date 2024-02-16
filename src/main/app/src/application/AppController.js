import { Client } from "@stomp/stompjs";
import PartitaObserverSingleton from "./PartitaObserverSingleton";

class AppController {
  constructor() {
    this.client = new Client({
      brokerURL: "ws://localhost:8080/stomp",
      onConnect: () => {
        console.log("Connected to STOMP");
        // Aggiungi le sottoscrizioni ai topic qui, se necessario
      },
      onDisconnect: () => {
        console.log("Disconnected from STOMP");
      },
      // Altri eventi e configurazioni...
    });

    this.client.activate();
  }

  async creaPartita(configuration) {
    //qui arriva la configurazione della partita
    console.log("fetch crea partita ", JSON.stringify(configuration));
    try {
      const response = await fetch("/partita", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(configuration),
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      return await response.json();
    } catch (error) {
      console.error("There was a problem with the fetch operation:", error);
    }
  }

  async creaPartitaComponent(configuration) {
    console.log("fetch crea partita ", JSON.stringify(configuration));
    try {
      const response = await fetch("/partita", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(configuration),
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const idNuovaPartita = await response.json(); // Supponendo che ritorni solo l'ID
      console.log("nuovaPartita controller", idNuovaPartita);

      // Combina l'ID della partita con le informazioni di configurazione
      const nuovaPartita = {
        id: idNuovaPartita,
        ...configuration, // Espande tutte le proprietà di configuration nell'oggetto
      };

      PartitaObserverSingleton.notifyListeners(nuovaPartita);

      return nuovaPartita;
    } catch (error) {
      console.error("There was a problem with the fetch operation:", error);
    }
  }

  async getPartite() {
    try {
      const response = await fetch("/partita", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error(
          `Errore nell'ottenimento delle partite: ${response.statusText}`
        );
      }

      return await response.json();
    } catch (error) {
      console.error("Errore:", error);
      throw error;
    }
  }

  entraInPartita(idPartita, nickname) {
    return new Promise((resolve, reject) => {
      console.log(
        `Tentativo di entrare in partita con id: ${idPartita} e nickname: ${nickname}`
      );

      if (!this.client) {
        console.error("Client non inizializzato.");
        reject("Client non inizializzato.");
        return;
      }

      if (!this.client.connected) {
        console.error("Client non connesso.");
        reject("Client non connesso.");
        return;
      }

      console.log("Inizio sottoscrizione alla partita");
      this.client.subscribe(`/topic/partite/${idPartita}`, (message) => {
        if (message.body === "Partita piena") {
          console.error("La partita è piena.");
          reject("La partita è piena");
          return;
        }

        PartitaObserverSingleton.notifyListeners(JSON.parse(message.body));
        console.log("Messaggio ricevuto:", message.body);

        try {
          const partita = JSON.parse(message.body);
          console.log("Aggiornamento partita ricevuto:", partita);
          resolve(partita);
        } catch (error) {
          console.error("Errore nella deserializzazione del messaggio:", error);
          reject(error);
        }
      });

      const payload = { username: nickname };
      console.log("Payload inviato:", payload);
      this.client.publish({
        destination: `/app/partite/${idPartita}/entra`,
        body: JSON.stringify(payload),
        onComplete: (receipt) => {
          console.log("Messaggio inviato con successo", receipt);
          resolve("Partecipazione confermata");
        },
        onError: (error) => {
          console.error("Errore nell'invio del messaggio", error);
          reject("Errore nell'invio del messaggio");
        },
      });
    });
  }
}

// Esporta l'istanza per utilizzarla nell'app
const appController = new AppController();
export default appController;
