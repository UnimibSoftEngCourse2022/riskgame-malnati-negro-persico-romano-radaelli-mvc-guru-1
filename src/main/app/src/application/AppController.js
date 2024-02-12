export default class AppController {
  constructor(setIsLobbyCreated, setData, difficolta, players) {
    this.setIsLobbyCreated = setIsLobbyCreated;
    this.setData = setData;
    this.difficolta = difficolta;
    this.players = players;
  }
  async creaPartita(difficolta, players, nomeMappa) {
    try {
      const response = await fetch("/partita", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          difficolta,
          players,
          nomeMappa,
        }),
      });
      if (!response.ok) {
        throw new Error(
          `Creazione lobby fallita con stato: ${response.status}`
        );
      }
    } catch (error) {
      console.error("Error:", error);
    }
  }

  getPartite = async () => {
    try {
      const response = await fetch("/partita", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error(
          `Creazione lobby fallita con stato: ${response.status}`
        );
      }

      const data = await response.json();
      console.log("Success:", data);
    } catch (error) {
      console.error("Error:", error);
    }
  };
}
