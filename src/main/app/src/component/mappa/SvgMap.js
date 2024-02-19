import React, { useEffect, useState } from "react";
import { Container, Alert, Tooltip, OverlayTrigger } from "react-bootstrap";

function SvgMap({ paths, gioco, onTerritoryClick, truppeAssegnate }) {
  const handleTerritoryClick = (e, territory, action) => {
    e.preventDefault(); // Questo impedisce la comparsa del menu contestuale del browser

    // Chiama direttamente onTerritoryClick con l'azione specifica
    onTerritoryClick(territory, action);
  };
  const [myTerritories, setMyTerritories] = useState([]);
  const [myColor, setMyColor] = useState("");
  const [objective, setObjective] = useState("");
  const [territoryColorMap, setTerritoryColorMap] = useState({});
  const url = window.location.href;
  const nickname = url.split("/").pop();

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
    // Ora hai una mappa nomeTerritorio -> coloreGiocatore
    setTerritoryColorMap(territoryColorMap);

    setObjective(player.objective.objective);
  }, [gioco, nickname, myColor]);

  const renderTooltip = (props, name) => (
    <Tooltip id="button-tooltip" {...props}>
      {`${name}: ${truppeAssegnate[name] || 0} truppe`}
    </Tooltip>
  );
  // cordinate carro armato
  const coordinate_x = 530;
  const coordinate_y = 400;

  return (
    <Container className="w-75">
      <Alert variant="info">Obiettivo del Gioco: {objective}</Alert>
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
            const fillColor = territoryColorMap[name] || "grey";
            return (
              <OverlayTrigger
                key={index}
                placement="top"
                overlay={(props) => renderTooltip(props, name)}
              >
                <path
                  id={name}
                  d={d}
                  stroke="black"
                  fill={fillColor} // Usa il colore specifico del giocatore
                  opacity="0.5"
                  onClick={(e) =>
                    isTerritoryOwned && handleTerritoryClick(e, name, "add")
                  }
                  onContextMenu={(e) =>
                    isTerritoryOwned && handleTerritoryClick(e, name, "remove")
                  }
                >
                  <svg
                    x={coordinate_x}
                    y={coordinate_y}
                    width="140"
                    height="140"
                    viewBox="0 0 200 200"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <g id="g65">
                      <path
                        fill="#00ff00"
                        d="m 40.991335,100.43546 c -17.81739,-3.126508 -27.89577,-27.716701 -17.80486,-43.44202 3.12224,-4.865593 10.41979,-10.909668 13.17221,-10.909668 2.74977,0 3.44007,-1.985269 2.7053,-7.780342 -1.96629,-15.50803 -1.90748,-18.194919 0.40893,-18.684997 1.55554,-0.329101 33.09021,-2.576971 38.65559,-2.755467 2.4943,-0.08 2.86271,1.030225 3.51436,10.590718 0.72052,10.571022 1.7837,14.622886 3.00313,11.445119 0.34379,-0.895906 0.22211,-5.09149 -0.27039,-9.323521 -1.10031,-9.454826 -2.10743,-8.87573 18.252895,-10.495481 8.25001,-0.656323 19.63138,-1.750423 25.29194,-2.431334 11.65469,-1.401946 11.20877,-1.648506 11.95779,6.611748 l 0.54774,6.04063 7.60127,-0.621159 c 4.1807,-0.341638 10.52627,-1.056432 14.10127,-1.58843 11.56428,-1.72089 15.25448,-1.40369 14.45499,1.242516 -1.05921,3.505868 -1.20495,3.554311 -14.70499,4.887684 -13.10592,1.294448 -15.19028,2.293922 -9.77903,4.689171 19.37237,8.575044 19.21751,40.383914 -0.24494,50.312917 -3.79473,1.935923 -28.61788,5.123886 -68.726035,8.826278 -6.875,0.63463 -15.2,1.55908 -18.5,2.05433 -8.04514,1.207378 -20.38321,1.902298 -23.63717,1.331308 z m 19.66703,-3.816508 c 6.58358,-0.80565 23.67014,-2.619006 37.97014,-4.029683 51.307055,-5.061377 53.658585,-5.621511 59.949415,-14.279898 12.47396,-17.168568 1.218,-40.543575 -19.4191,-40.327234 -5.36013,0.05619 -48.749155,4.138605 -64.110185,6.032041 -4.16893,0.513872 -13.16893,1.439795 -20,2.057607 -23.79354,2.151923 -32.92013,9.396434 -32.92013,26.131367 0,19.775631 12.37362,27.61661 38.52986,24.4158 z m -17.59624,-7.282444 c -12.60932,-6.05948 -14.74198,-25.251256 -3.6161,-32.541209 3.71581,-2.434692 51.94555,-8.367928 92.720685,-11.406526 14.8126,-1.103847 24.5431,9.395083 20.97803,22.634714 -2.86864,10.653312 -7.48664,12.823654 -32.24624,15.154885 -10.94642,1.030656 -27.860245,2.794983 -37.586285,3.920725 -20.30175,2.349831 -37.97215,3.332088 -40.25009,2.237411 z m 14.58526,-9.673145 c 4.74348,-3.108048 4.47709,-12.969332 -0.42864,-15.867215 -7.33743,-4.334328 -16.09024,0.04891 -16.09024,8.057691 0,8.456924 9.02153,12.721974 16.51888,7.809524 z m 85.021305,-9.621531 c 4.2442,-4.429989 4.11182,-10.092804 -0.32868,-14.060396 -6.02666,-5.384813 -14.89321,-2.998336 -16.77924,4.516214 -2.52178,10.047585 9.9563,17.008868 17.10792,9.544182 z m -0.87296,-46.621324 c -0.77908,-4.800885 -2.70656,-4.168795 19.47309,-6.385926 10.64783,-1.064382 20.48469,-2.165106 21.85969,-2.446052 3.23058,-0.660087 11.89819,-0.952078 12.38014,-0.417058 3.99941,4.439772 0.51115,7.469312 -9.55121,8.295182 -4.58091,0.37598 -13.50393,1.312237 -19.82893,2.080573 -6.325,0.768335 -14.25112,1.668778 -17.6136,2.000984 l -6.11361,0.604011 z"
                        id="path177"
                        strokeWidth="2"
                        stroke="black"
                      />
                    </g>
                  </svg>
                  <title>{`${name}: ${
                    truppeAssegnate[name] || 0
                  } truppe`}</title>
                </path>
              </OverlayTrigger>
            );
          })}
        </svg>
      </Container>
    </Container>
  );
}

export default SvgMap;
