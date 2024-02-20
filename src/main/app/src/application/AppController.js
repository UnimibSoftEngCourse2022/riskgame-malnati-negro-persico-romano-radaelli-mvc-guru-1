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
    if (!this.client || !this.client.connected) {
      console.error("Client STOMP non connesso");
    }

    this.client.subscribe(`/topic/partite/${idPartita}`, (message) => {
      PartitaObserverSingleton.notifyListeners(JSON.parse(message.body));
      console.log("Aggiornamento topic ricevuto:", JSON.parse(message.body));
    });
    const payload = { username: nickname };
    this.client.publish({
      destination: `/app/partite/${idPartita}/entra`,
      body: JSON.stringify(payload),
    });
  }

  esciDallaPartita(idPartita, nickname) {
    if (this.client && this.client.connected) {
      this.client.unsubscribe(`/topic/partite/${idPartita}`);
      // Notifica al server l'uscita dalla partita
      const payload = { username: nickname };
      this.client.publish({
        destination: `/app/partite/${idPartita}/esci`,
        body: JSON.stringify(payload),
      });
      console.log(
        `Notifica di uscita dalla partita ${idPartita} inviata, ${nickname} si è disconnesso`
      );
    } else {
      console.error("Client STOMP non connesso");
    }
  }
  
	subscribeToEsitiPartita(idPartita, nickname) {
		if (!this.client || !this.client.connected) {
			console.error("Client STOMP non connesso");
			return;
		}
		this.client.subscribe(`/topic/partite/${idPartita}/${nickname}`, (message) => {
		  PartitaObserverSingleton.notifyListenersEsiti(JSON.parse(message.body));
		  console.log("Esito attacco ricevuto:", JSON.parse(message.body));
	  });
	}
	
	unsubscribeToEsitiPartita(idPartita, nickname) {
		if (this.client && this.client.connected) {
			this.client.unsubscribe(`/topic/partite/${idPartita}/${nickname}`);
		} else {
			console.error("Client STOMP non connesso");
		}
	}
  
  	setUpPartita(idPartita, territoriAssegnati) {
  		if (!this.client || !this.client.connected) {
    	console.error("Client STOMP non connesso");
    	return;
  		}	 
  console.log("territoriAssegnatinellotomp", territoriAssegnati);
  // Assicurati che territoriAssegnati sia un array di oggetti che corrispondono
  // alla struttura di TerritoryBody, ovvero con i campi `name` e `troops`.
  
  console.log("setUpBodystomp", territoriAssegnati);

  // Pubblicazione del messaggio al topic di setup della partita
  this.client.publish({
    destination: `/app/partite/${idPartita}/confermaSetup`,
    body: JSON.stringify(territoriAssegnati),
  });

  console.log(`Setup per la partita ${idPartita} inviato:`, territoriAssegnati);
}

	setUpTurno(idPartita, territoriAssegnati) {
		if (!this.client || !this.client.connected) {
			console.error("Client STOMP non connesso");
			return;
		}
		console.log("territoriAssegnati", territoriAssegnati);
		this.client.publish({
			destination: `/app/partite/${idPartita}/turnAssignation`,
			body: JSON.stringify(territoriAssegnati),
		});
		console.log(`Setup per il turno della partita: ${idPartita} inviato:`, territoriAssegnati);
	}
}

	

// Esporta l'istanza per utilizzarla nell'app
const appController = new AppController();
export default appController;
