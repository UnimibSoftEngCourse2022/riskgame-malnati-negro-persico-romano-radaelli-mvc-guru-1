import React, { useEffect, useState } from "react";
import { Container, Alert, Tooltip, OverlayTrigger } from "react-bootstrap";
import SvgTanker from "./SvgTanker";
import coordinateTruppe from "../../resources/tankersCoordinates.json";

function SvgMap({ paths, gioco, onTerritoryClick, truppeAssegnate }) {
  const handleTerritoryClick = (e, territory, action) => {
    e.preventDefault();

    onTerritoryClick(territory, action);
  };
  const [myTerritories, setMyTerritories] = useState([]);
  const [myColor, setMyColor] = useState("");
  const [objective, setObjective] = useState("");
  const [territoryColorMap, setTerritoryColorMap] = useState({});
  const [showAlert, setShowAlert] = useState(true);
  const url = window.location.href;
  const nickname = url.split("/").pop();

  // qua set cordinate carri armati
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
  }, [gioco, nickname, myColor]);

  const renderTooltip = (props, name) => (
    <Tooltip id="button-tooltip" {...props}>
      {`${name}: ${truppeAssegnate[name] || 0} truppe`}
    </Tooltip>
  );

  return (
    <Container className="w-100">
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
                    strokeWidth="1.5"
                    fill={fillColor}
                    opacity="0.5"
                    onClick={(e) =>
                      isTerritoryOwned && handleTerritoryClick(e, name, "add")
                    }
                    onContextMenu={(e) =>
                      isTerritoryOwned &&
                      handleTerritoryClick(e, name, "remove")
                    }
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
        </svg>
      </Container>
    </Container>
  );
}

export default SvgMap;
