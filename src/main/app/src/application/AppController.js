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

  // async creaPartita(configuration) {
  //   console.log("fetch crea partita ", JSON.stringify(configuration));
  //   try {
  //     const response = await fetch("/partita", {
  //       method: "POST",
  //       headers: {
  //         "Content-Type": "application/json",
  //       },
  //       body: JSON.stringify(configuration),
  //     });

  //     if (!response.ok) {
  //       throw new Error(
  //         `Errore nella creazione della partita: ${response.statusText}`
  //       );
  //     }

  //     return await response.text(); // Potrebbe essere necessario adattare in base al formato della risposta
  //   } catch (error) {
  //     console.error("Errore:", error);
  //     throw error;
  //   }
  // }

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

  // entraInPartita(idPartita, nickname) {
  //   // Controlla se il client STOMP è connesso prima di procedere
  //   if (this.client && this.client.connected) {
  //     // Iscriviti al topic della partita per ricevere gli aggiornamenti
  //     this.client.subscribe(`/topic/partite/${idPartita}`, (message) => {
  //       const partita = JSON.parse(message.body);
  //       console.log("Aggiornamento partita ricevuto:", partita);
  //       // Aggiorna lo stato della partita nell'UI o gestisci l'aggiornamento come necessario
  //     });

  //     // Invia un messaggio al server per unirsi alla partita
  //     // Assicurati che il payload corrisponda a quello atteso dal server
  //     const payload = { username: nickname };
  //     this.client.publish({
  //       destination: `/app/partite/${idPartita}/entra`,
  //       body: JSON.stringify(payload),
  //     });
  //   } else {
  //     console.error("Client STOMP non connesso.");
  //     // Gestisci il caso in cui il client STOMP non sia connesso
  //     // Potresti voler mostrare un messaggio di errore all'utente o tentare di riconnetterti
  //   }
  // }

  // entraInPartita con observer
  // entraInPartita(idPartita, nickname) {
  //   return new Promise((resolve, reject) => {
  //     if (this.client && this.client.connected) {
  //       const subscription = this.client.subscribe(
  //         `/topic/partite/${idPartita}`,
  //         (message) => {
  //           const partita = JSON.parse(message.body);
  //           console.log("Aggiornamento partita ricevuto:", partita);
  //           // Notifica tutti i listener con l'aggiornamento della partita
  //           PartitaObserverSingleton.getInstance().notifyListeners(partita);
  //           resolve(partita);
  //         }
  //       );

  //       const payload = { username: nickname };
  //       this.client.publish({
  //         destination: `/app/partite/${idPartita}/entra`,
  //         body: JSON.stringify(payload),
  //         onComplete: (receipt) => {
  //           console.log("Messaggio inviato con successo");
  //           // Potresti voler aggiungere il listener qui, se appropriato
  //           resolve("Partecipazione confermata");
  //         },
  //         onError: (error) => {
  //           console.error("Errore nell'invio del messaggio", error);
  //           reject("Errore nell'invio del messaggio");
  //         },
  //       });

  //       // Considera di aggiungere qui la logica per rimuovere il listener se necessario
  //     } else {
  //       console.error("Client STOMP non connesso.");
  //       reject("Client STOMP non connesso.");
  //     }
  //   });
  // }

  // entraInPartita(idPartita, nickname) {
  //   return new Promise((resolve, reject) => {
  //     console.log(
  //       `Tentativo di entrare in partita con id: ${idPartita} e nickname: ${nickname}`
  //     );

  //     if (!this.client) {
  //       console.error("Client non inizializzato.");
  //       reject("Client non inizializzato.");
  //       return;
  //     }

  //     if (!this.client.connected) {
  //       console.error("Client non connesso.");
  //       reject("Client non connesso.");
  //       return;
  //     }

  //     console.log("Inizio sottoscrizione alla partita");
  //     this.client.subscribe(`/topic/partite/${idPartita}`, (message) => {
  //       console.log("Messaggio ricevuto:", message.body);
  //       try {
  //         const partita = JSON.parse(message.body);
  //         console.log("Aggiornamento partita ricevuto:", partita);
  //         resolve(partita);
  //       } catch (error) {
  //         console.error("Errore nella deserializzazione del messaggio:", error);
  //         reject(error);
  //       }
  //     });
  //   });
  // }

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
