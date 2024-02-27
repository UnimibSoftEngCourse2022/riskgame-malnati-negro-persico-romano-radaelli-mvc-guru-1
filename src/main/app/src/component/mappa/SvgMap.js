import React, { useEffect, useState } from "react";
import { Container, Alert, Tooltip, OverlayTrigger } from "react-bootstrap";
import SvgTanker from "./SvgTanker";
import coordinateTruppe from "../../resources/tankersCoordinates.json";
import Console from "./Console";

function SvgMap({
  paths,
  gioco,
  onTerritoryClick,
  truppeAssegnate,
  territoryAttack,
  territoryDefense,
  territoryNeighbors,
  sxSelected,
}) {
  const [myTerritories, setMyTerritories] = useState([]);
  const [myColor, setMyColor] = useState("");
  const [objective, setObjective] = useState("");
  const [territoryColorMap, setTerritoryColorMap] = useState({});
  const [showAlert, setShowAlert] = useState(true);
  const url = window.location.href;
  const nickname = url.split("/").pop();
  const [gameState, setGameState] = useState("");
  
  const handleTerritoryClick = (e, territory, action) => {
    e.preventDefault();
    console.log("territorio cliccato", territory, action);
    onTerritoryClick(territory, action);
  };

  const findCoordinates = (nomeTerritorio) => {
    const territorio = coordinateTruppe.territori.find(
      (t) => t.nome === nomeTerritorio
    );
    return territorio ? { x: territorio.x, y: territorio.y } : null;
  };

  const handleCloseAlert = () => setShowAlert(false);

  useEffect(() => {
    const player = gioco.players.find((p) => p.userName === nickname);
    if (player) {
      const territoriGiocatore = player.territories.map((t) => t.name);
      setMyTerritories(territoriGiocatore);
      setMyColor(player.color);
    }

    const territoryColorMap = {};
    gioco.players.forEach((player) => {
      player.territories.forEach((territory) => {
        territoryColorMap[territory.name] = player.color;
      });
    });

    setTerritoryColorMap(territoryColorMap);

    setObjective(player.objective.objective);

    const statoGioco = gioco.state.type;
    setGameState(statoGioco);

    console.log("stato del gioco nella mappa", statoGioco);
  }, [gioco, nickname, myColor, truppeAssegnate]);

  const calculateOpacity = (name, territoryAttack, territoryNeighbors, territoryDefense, sxSelected) => {
  if (name === territoryAttack || name === territoryDefense) {
    return "1";
  }
  if (territoryNeighbors?.includes(name) && sxSelected) {
    return "1";
  }
  return "0.5";
};

  const calculateStrokeWidth = (name, territoryAttack, territoryNeighbors) => {
    if (name === territoryAttack || territoryNeighbors?.includes(name))
      return "2.5";
    return "1.5";
  };

  const renderTooltip = (props, name) => (
    <Tooltip id="button-tooltip" {...props}>
      {`${name}: ${truppeAssegnate[name] || 0} truppe`}
    </Tooltip>
  );

  return (
    <div className="">
      {showAlert && (
        <Alert variant="info" onClose={handleCloseAlert} dismissible>
          Obiettivo del Gioco: {objective}
        </Alert>
      )}
      <Container className="m-2">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 1024 650"
          id="mapSvg"
          aria-labelledby="title"
        >
          <path
            className="sea"
            fill="lightblue"
            d="M0 792c0,-264 0,-528 0,-792 341,0 682,0 1023,0 0,264 0,528 0,792 -341,0 -682,0 -1023,0z"
          />

          {paths.map(({ d, name }, index) => {
            const isTerritoryOwned = myTerritories.includes(name);
            const coordinates = findCoordinates(name);
            const fillColor = territoryColorMap[name] || "grey";

            const setOpacity = calculateOpacity(
              name,
              territoryAttack,
              territoryNeighbors,
              territoryDefense,
              sxSelected
            );

            const spessoreStroke = calculateStrokeWidth(
              name,
              territoryAttack,
              territoryNeighbors
            );

            return (
              <g key={index}>
                <OverlayTrigger
                  placement="top"
                  overlay={(props) => renderTooltip(props, name)}
                >
                  <path
                    id={name}
                    d={d}
                    stroke="black"
                    strokeWidth={spessoreStroke}
                    fill={fillColor}
                    opacity={setOpacity}
                    onClick={(e) =>
                      isTerritoryOwned && handleTerritoryClick(e, name, "add")
                    }
                    onContextMenu={(e) => {
                      if (isTerritoryOwned && gameState === "setupState") {
                        handleTerritoryClick(e, name, "remove");
                      } else if (
                        !isTerritoryOwned &&
                        gameState !== "setupState"
                      ) {
                        handleTerritoryClick(e, name, "addNonOwned");
                      }
                      e.preventDefault(); // Previene l'apertura del menu contestuale del browser
                    }}
                  />
                </OverlayTrigger>

                {isTerritoryOwned && coordinates && (
                  <SvgTanker
                    x={coordinates.x}
                    y={coordinates.y}
                    color={fillColor}
                    armate={truppeAssegnate[name] || 0}
                  />
                )}
                {isTerritoryOwned && coordinates && (
                  <text
                    x={coordinates.x}
                    y={coordinates.y - 15}
                    fill="black"
                    fontSize="15"
                    fontFamily="Arial"
                    fontWeight="bold"
                  >
                    <tspan>{name}</tspan>
                    <tspan x={coordinates.x} dy="20">
                      {truppeAssegnate[name] || 0}
                    </tspan>
                  </text>
                )}
              </g>
            );
          })}
          <Console />
        </svg>
      </Container>
    </div>
  );
}

export default SvgMap;
