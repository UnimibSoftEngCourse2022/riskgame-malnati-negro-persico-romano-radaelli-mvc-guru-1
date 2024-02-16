import React, { useState, useRef, useEffect } from "react";
import "../WorldMap.css";
// import CarroArmato from "./carro_armato.svg";
// import ParallelBackground from "./parallel.svg";

// Valori di default per lo style del territorio
const defaultStylesCountry = {
  East_Africa: {
    fill: "orange",
    stroke: "#000000",
    strokeWidth: "1",
    opacity: "0.5",
  },
  Congo: {
    fill: "orange",
    stroke: "#000000",
    strokeWidth: "1",
    opacity: "0.5",
  },
  Egypt: {
    fill: "orange",
    stroke: "#000000",
    strokeWidth: "1",
    opacity: "0.5",
  },
  Madagascar: {
    fill: "orange",
    stroke: "#000000",
    strokeWidth: "1",
    opacity: "0.5",
  },
  Sud_Africa: {
    fill: "orange",
    stroke: "#000000",
    strokeWidth: "1",
    opacity: "0.5",
  },
  Nord_Africa: {
    fill: "orange",
    stroke: "#000000",
    strokeWidth: "1",
    opacity: "0.5",
  },
};

// Valori per lo style del territorio on click
const onClickStylesCountry = {
  East_Africa: {
    fill: "orange",
    stroke: "#00ff00",
    strokeWidth: "5",
    opacity: "1",
  },
};

// Collasso dei territori
const collapsedCountryData = {
  East_Africa: {
    fill: "orange",
    stroke: "#000000",
    strokeWidth: "0",
    opacity: "0.5",
  },
  Congo: {
    fill: "orange",
    stroke: "#000000",
    strokeWidth: "0",
    opacity: "0.5",
  },
};

const SvgMap = (props, svgRef) => {
  // set style country default
  const [countryStrokes, setCountryStrokes] = useState(defaultStylesCountry);

  // mostrare o nascondere country
  const [showCountries, setShowCountries] = useState(true);
  const toggleCountriesVisibility = () => {
    setShowCountries(!showCountries);
  };

  function mergeSvgPaths(d1, d2) {
    console.log("d1", d1);
    console.log("d2", d2);
    // Function to parse the "d" attribute of a path and extract points
    function parsePathD(d) {
      const points = [];
      const commands = d.match(/[a-zA-Z]+[^a-zA-Z]*/g);
      if (commands) {
        for (const command of commands) {
          const type = command[0];
          const args = command
            .slice(1)
            .trim()
            .split(/[\s,]+/)
            .map(Number);
          if (type === "M" || type === "L") {
            // MoveTo or LineTo commands
            for (let i = 0; i < args.length; i += 2) {
              points.push({ x: args[i], y: args[i + 1] });
            }
          }
          // Add support for other command types (C, Q, etc.) if needed
        }
      }
      return points;
    }

    // Parse the "d" attributes to get points
    const path1 = parsePathD(d1);
    const path2 = parsePathD(d2);

    // Merge the two arrays of points
    const points = path1.concat(path2);

    // Function to find the convex hull using the Graham Scan algorithm
    function convexHull(points) {
      // Sort points lexicographically
      points.sort((a, b) => (a.x === b.x ? a.y - b.y : a.x - b.x));

      // Function to check the orientation of the ordered triplet (p, q, r)
      // Returns positive if counterclockwise, negative if clockwise, and zero if collinear
      function cross(p, q, r) {
        return (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
      }

      // Build the lower hull
      const lower = [];
      for (let point of points) {
        while (
          lower.length >= 2 &&
          cross(lower[lower.length - 2], lower[lower.length - 1], point) <= 0
        ) {
          lower.pop();
        }
        lower.push(point);
      }

      // Build the upper hull
      const upper = [];
      for (let i = points.length - 1; i >= 0; i--) {
        const point = points[i];
        while (
          upper.length >= 2 &&
          cross(upper[upper.length - 2], upper[upper.length - 1], point) <= 0
        ) {
          upper.pop();
        }
        upper.push(point);
      }

      // Concatenate the lower and upper hulls
      // The last point of each list is omitted because it is repeated at the beginning of the other list
      upper.pop();
      lower.pop();
      return lower.concat(upper);
    }

    // Find the convex hull of the merged points
    return convexHull(points);
  }

  //collassare territori
  const collapsedCountry = (idCountry) => {
    console.log("idCountry", idCountry);

    const currentCountry = countryStrokes[idCountry];
    console.log("currentCountry", currentCountry);

    const getCollapsedCountry = collapsedCountryData[idCountry];
    console.log("getCollapsedCountry", getCollapsedCountry);

    if (currentCountry && getCollapsedCountry) {
      // Update the countryStrokes object for the given idCountry
      setCountryStrokes({
        ...countryStrokes,
        [idCountry]: {
          // Apply the fill and strokeWidth from getCollapsedCountry
          fill: getCollapsedCountry.fill,
          strokeWidth: getCollapsedCountry.strokeWidth,
          opacity: getCollapsedCountry.opacity,
        },
      });

      console.log("countryStrokes", countryStrokes);
    }
  };

  // set style country onClick
  const [checkOnClick, setcheckOnClick] = useState(false);
  useEffect(() => {}, [checkOnClick]);
  const handleCountry = (countryId) => {
    console.log("hai selezionato: ", countryId);
    checkOnClick === false ? setcheckOnClick(true) : setcheckOnClick(false);
    console.log("checkOnClick: ", checkOnClick);

    // Ottieni gli stili correnti e gli stili al click
    const currentStyles = countryStrokes[countryId];
    const clickStyles = onClickStylesCountry[countryId];
    // Controlla se lo stile corrente è uguale a quello al click
    const isClicked =
      currentStyles.stroke === clickStyles.stroke &&
      currentStyles.strokeWidth === clickStyles.strokeWidth;

    console.log("isClicked", isClicked);

    setCountryStrokes({
      ...countryStrokes,
      [countryId]: isClicked
        ? { ...defaultStylesCountry[countryId] }
        : { ...clickStyles },
    });
  };

  // set style country mouse sopra
  const [countryHovered, setCountryHovered] = useState("");
  useEffect(() => {
    // Applica il nuovo stile alla country attualmente hoverata
    if (countryHovered && !checkOnClick) {
      const newFillColor =
        countryStrokes[countryHovered].fill === "orange" ? "#ff0000" : "orange";
      setCountryStrokes((prevStrokes) => ({
        ...prevStrokes,
        [countryHovered]: {
          ...prevStrokes[countryHovered],
          fill: newFillColor,
        },
      }));
    }

    // Funzione di pulizia: resetta lo stile delle country quando countryHovered cambia
    return () => {
      if (countryHovered && !checkOnClick) {
        setCountryStrokes((prevStrokes) => ({
          ...prevStrokes,
          [countryHovered]: defaultStylesCountry[countryHovered],
        }));
      }
    };
  }, [countryHovered]);

  const handleCountryMouseOver = (countryId) => {
    !checkOnClick
      ? setCountryHovered(countryId)
      : console.log("checkOnClick attivo");
  };

  // set style country mouse no sopra
  const handleCountryMouseOut = () => {
    !checkOnClick
      ? // Resetta il paese hoverato quando il mouse esce
        setCountryHovered("")
      : console.log("checkOnClick attivo");
  };

  // carro armato
  const coordinate_x = 530;
  const coordinate_y = 400;

  return (
    <div>
      <button onClick={toggleCountriesVisibility}>
        {showCountries ? "Nascondi Paesi" : "Mostra Paesi"}
      </button>
      <button onClick={() => collapsedCountry("East_Africa")}>
        Collasso East_Africa
      </button>
      <button onClick={() => collapsedCountry("Congo")}>Collasso Congo</button>
      <button
        onClick={() =>
          mergeSvgPaths(
            document.getElementById("Congo").getAttribute("d"),
            document.getElementById("East_Africa").getAttribute("d")
          )
        }
      >
        Unisci
      </button>
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="100%"
        height="100%"
        viewBox="0 0 1024 650"
        id="mapSvg"
        ref={svgRef}
        aria-labelledby="title"
        {...props}
      >
        {/* <image href={ParallelBackground} width="100%" height="100%" /> */}
        <title id="title">Mappa Interattiva</title>
        <g id="map" fill="none" strokeWidth="1.5">
          <path
            className="sea"
            fill="lightblue"
            d="M0 792c0,-264 0,-528 0,-792 341,0 682,0 1023,0 0,264 0,528 0,792 -341,0 -682,0 -1023,0z"
          />
          <g id="Africa" data-continent="Africa" fill="none" strokeWidth="1.5">
            {/* East Africa */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="East_Africa"
              d="M532 514c-1,-6 -1,-12 -2,-17 5,-1 6,-8 7,-12 1,0 2,0 4,0 1,-8 11,-8 10,-19 -5,-1 -10,-2 -16,-2 0,-2 -1,-4 -2,-6 0,1 -1,2 -2,3 -7,-10 -12,-18 -16,-29 -1,-14 2,-24 4,-37 0,-1 0,-3 0,-4 1,0 2,0 4,0 -1,-3 -1,-3 1,-9 11,0 22,0 33,0 2,-3 2,-3 5,-4 7,11 9,26 14,38 1,1 2,1 3,1 2,5 3,9 5,14 -1,0 -2,0 -3,0 9,6 21,1 30,-1 0,0 0,1 1,2 -4,12 -9,23 -16,34 -15,12 -22,25 -33,39 0,7 0,14 1,21 -6,1 -11,2 -16,4 -2,6 -2,16 -2,18 -2,-1 -3,-1 -5,-1 0,-7 0,-15 0,-23 -2,-3 -5,-7 -7,-10 -1,0 -2,0 -2,0z"
              fill={countryStrokes["East_Africa"].fill}
              stroke={countryStrokes["East_Africa"].stroke}
              strokeWidth={countryStrokes["East_Africa"].strokeWidth}
              opacity={countryStrokes["East_Africa"].opacity}
              onClick={(e) => handleCountry(e.target.id)}
              onMouseOver={(e) => handleCountryMouseOver(e.target.id)}
              onMouseOut={(e) => handleCountryMouseOut(e.target.id)}
            >
              <title id="title">East Africa</title>
            </path>
            <text
              x="530"
              y="450"
              fill="black"
              fontSize="12"
              fontFamily="Arial"
              fontWeight="bold"
            >
              East Africa
            </text>
            {/* // carro armato */}
            <svg
              x={coordinate_x}
              y={coordinate_y}
              width="40"
              height="40"
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
            {/* Egypt */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Egypt"
              d="M484 328c8,2 15,10 23,9 0,-4 0,-4 1,-7 12,-3 24,4 35,8 5,0 9,0 14,0 1,5 3,10 4,15 -2,0 -4,-1 -6,-1 2,9 5,17 7,26 -3,1 -3,1 -5,4 -11,0 -22,0 -33,0 -2,6 -2,6 -1,9 -2,0 -3,0 -4,0 0,1 0,3 0,4 -7,-2 -8,-7 -15,-9 0,-2 -1,-4 -2,-6 -12,-2 -19,-3 -28,-11 -1,-11 0,-19 -1,-29 1,-1 2,-1 2,-2 1,-2 1,-4 1,-7 4,-3 4,-3 8,-3z"
              fill={countryStrokes["Egypt"].fill}
              stroke={countryStrokes["Egypt"].stroke}
              strokeWidth={countryStrokes["Egypt"].strokeWidth}
              opacity={countryStrokes["Egypt"].opacity}
              onClick={(e) => handleCountry(e.target.id)}
              onMouseOver={(e) => handleCountryMouseOver(e.target.id)}
              onMouseOut={(e) => handleCountryMouseOut(e.target.id)}
            >
              <title id="title">Egypt</title>
            </path>
            <text
              x="500"
              y="360"
              fill="black"
              fontSize="12"
              fontFamily="Arial"
              fontWeight="bold"
            >
              Egypt
            </text>
            {/* Congo */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Congo"
              d="M473 502c-5,-6 -11,-12 -11,-20 4,-3 4,-4 6,-9 2,0 4,0 6,0 0,-1 0,-2 0,-3 9,2 9,2 13,2 1,-8 2,-13 0,-20 4,-2 5,-5 10,-7 1,-1 1,-2 1,-4 7,-1 11,-5 17,-9 4,11 9,19 16,29 1,-1 2,-2 2,-3 1,2 2,4 2,6 6,0 11,1 16,2 1,11 -9,11 -10,19 -2,0 -3,0 -4,0 -1,4 -2,11 -7,12 1,5 1,11 2,17 -1,2 -2,3 -3,5 -1,0 -2,0 -3,0 1,5 1,11 2,16 -2,0 -3,0 -4,0 0,-1 0,-3 0,-4 -23,-5 -15,-2 -21,-20 -2,0 -4,-1 -6,-1 0,1 -1,2 -1,3 -1,0 -3,0 -4,1 -1,-4 -1,-4 -4,-9 -8,-1 -9,-2 -15,-3z"
              fill={countryStrokes["Congo"].fill}
              stroke={countryStrokes["Congo"].stroke}
              strokeWidth={countryStrokes["Congo"].strokeWidth}
              opacity={countryStrokes["Congo"].opacity}
              onClick={(e) => handleCountry(e.target.id)}
              onMouseOver={(e) => handleCountryMouseOver(e.target.id)}
              onMouseOut={(e) => handleCountryMouseOut(e.target.id)}
            >
              <title id="title">Congo</title>
            </path>{" "}
            <text
              x="480"
              y="490"
              fill="black"
              fontSize="12"
              fontFamily="Arial"
              fontWeight="bold"
            >
              Congo
            </text>
            {/* Madagascar */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Madagascar"
              d="M579 589c-5,-6 -4,-15 -1,-22 0,0 1,0 2,0 2,-15 1,-16 14,-21 6,-5 6,-5 7,-8 1,0 2,0 3,0 0,5 0,11 0,17 0,0 -1,0 -2,0 0,1 0,2 0,4 -1,0 -2,0 -3,0 -1,11 -9,26 -20,30z"
              fill={countryStrokes["Madagascar"].fill}
              stroke={countryStrokes["Madagascar"].stroke}
              strokeWidth={countryStrokes["Madagascar"].strokeWidth}
              opacity={countryStrokes["Madagascar"].opacity}
              onClick={(e) => handleCountry(e.target.id)}
              onMouseOver={(e) => handleCountryMouseOver(e.target.id)}
              onMouseOut={(e) => handleCountryMouseOut(e.target.id)}
            >
              <title id="title">Madagascar</title>
            </path>{" "}
            <text
              x="550"
              y="600"
              fill="black"
              fontSize="12"
              fontFamily="Arial"
              fontWeight="bold"
            >
              Madagascar
            </text>
            {/* Sud Africa */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Sud_Africa"
              d="M473 502c6,1 7,2 15,3 3,5 3,5 4,9 1,-1 3,-1 4,-1 0,-1 1,-2 1,-3 2,0 4,1 6,1 6,18 -2,15 21,20 0,1 0,3 0,4 1,0 2,0 4,0 -1,-5 -1,-11 -2,-16 1,0 2,0 3,0 1,-2 2,-3 3,-5 0,0 1,0 2,0 2,3 5,7 7,10 0,8 0,16 0,23 2,0 3,0 5,1 0,-2 0,-12 2,-18 5,-2 10,-3 16,-4 0,13 2,29 -13,33 -1,2 -1,2 -6,7 -1,9 0,19 -10,22 -2,13 -2,13 -3,18 -1,0 -2,0 -3,0 0,1 0,2 0,3 -1,0 -2,0 -3,0 -7,18 -20,18 -39,22 -2,-1 -4,-2 -5,-2 0,-2 0,-3 0,-4 0,0 1,0 2,0 0,-10 -1,-15 -6,-24 -1,-9 -2,-12 -5,-30 -4,-11 -8,-21 -2,-31 1,0 3,-1 4,-1 0,-8 -1,-16 -1,-24 1,0 2,-1 3,-1 0,-3 0,-7 -1,-11 -1,0 -2,0 -3,0 0,0 0,-1 0,-1z"
              fill={countryStrokes["Sud_Africa"].fill}
              stroke={countryStrokes["Sud_Africa"].stroke}
              strokeWidth={countryStrokes["Sud_Africa"].strokeWidth}
              opacity={countryStrokes["Sud_Africa"].opacity}
              onClick={(e) => handleCountry(e.target.id)}
              onMouseOver={(e) => handleCountryMouseOver(e.target.id)}
              onMouseOut={(e) => handleCountryMouseOut(e.target.id)}
            >
              <title id="title">Sud Africa</title>
            </path>{" "}
            <text
              x="480"
              y="580"
              fill="black"
              fontSize="12"
              fontFamily="Arial"
              fontWeight="bold"
            >
              Sud Africa
            </text>
            {/* Nord Africa */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Nord_Africa"
              d="M468 473c-1,-4 -1,-6 0,-10 -7,-3 -7,-3 -13,-3 -2,-6 -2,-6 -4,-7 -5,0 -9,1 -14,1 0,1 0,2 0,3 -16,-1 -25,7 -36,-8 -6,-2 -10,-16 -12,-22 0,0 -1,0 -2,0 0,-1 0,-2 0,-3 -1,0 -2,0 -3,0 -2,-9 -2,-16 1,-25 0,-9 -1,-19 2,-28 5,-1 8,-15 10,-20 13,-6 13,-10 17,-25 7,-4 9,-7 14,-14 0,0 1,0 1,0 7,5 16,-1 23,-4 7,0 10,-1 28,-2 0,2 0,3 0,4 -1,1 -1,1 -2,1 2,3 1,6 1,10 -1,0 -3,0 -4,0 0,1 0,1 3,1 0,1 0,2 0,3 2,1 4,2 6,3 -4,0 -4,0 -8,3 0,3 0,5 -1,7 0,1 -1,1 -2,2 1,10 0,18 1,29 9,8 16,9 28,11 1,2 2,4 2,6 7,2 8,7 15,9 -2,13 -5,23 -4,37 -6,4 -10,8 -17,9 0,2 0,3 -1,4 -5,2 -6,5 -10,7 2,7 1,12 0,20 -4,0 -4,0 -13,-2 0,1 0,2 0,3 -2,0 -4,0 -6,0z"
              fill={countryStrokes["Nord_Africa"].fill}
              stroke={countryStrokes["Nord_Africa"].stroke}
              strokeWidth={countryStrokes["Nord_Africa"].strokeWidth}
              opacity={countryStrokes["Nord_Africa"].opacity}
              onClick={(e) => handleCountry(e.target.id)}
              onMouseOver={(e) => handleCountryMouseOver(e.target.id)}
              onMouseOut={(e) => handleCountryMouseOut(e.target.id)}
            >
              <title id="title">Nord Africa</title>
            </path>
            <text
              x="420"
              y="410"
              fill="black"
              fontSize="12"
              fontFamily="Arial"
              fontWeight="bold"
            >
              Nord Africa
            </text>
          </g>
          <g id="Asia" data-continent="Asia" fill="none" strokeWidth="1.5">
            {/* Afghanistan */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Afghanistan"
              d="M651 310c-3,-4 -4,-5 -8,-7 -3,-8 -10,-4 -17,-3 0,-3 0,-5 -1,-7 -1,0 -2,-1 -3,-1 0,-3 0,-5 0,-8 1,0 2,-1 3,-1 0,-3 0,-6 0,-10 -1,0 -2,0 -3,0 1,4 1,4 0,7 -2,-1 -2,-1 -2,-9 -2,-1 -4,-2 -7,-3 1,-6 2,-7 5,-12 0,1 0,2 1,3 0,0 1,0 2,0 0,-3 0,-6 0,-9 -7,0 -11,1 -18,5 4,-14 -7,-21 0,-33 8,-5 22,-6 31,-7 0,1 1,2 1,3 11,-1 14,1 25,-6 0,-2 -1,-15 0,-15 10,0 21,-4 27,7 4,1 15,15 18,19 12,-1 10,9 15,16 -4,4 -5,5 -7,11 -11,3 -5,14 -16,18 0,1 0,2 0,3 -4,1 -7,1 -11,2 0,7 5,19 7,26 -3,1 -5,3 -7,5 -2,-3 -3,-5 -6,-5 0,-1 0,-2 0,-3 -5,-1 -7,-1 -9,4 -6,-1 -8,-1 -11,5 -3,1 -3,1 -9,5z"
              fill="yellow"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* India */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="India"
              d="M713 452c-1,-1 -2,-3 -4,-4 0,-3 1,-7 1,-10 1,0 2,0 4,0 3,5 4,6 4,11 -2,1 -3,2 -5,3z M656 363c0,-6 3,-10 4,-16 -1,0 -2,0 -3,0 -1,-6 -3,-13 -4,-19 -1,-1 -2,-1 -3,-1 0,-6 0,-11 1,-17 6,-4 6,-4 9,-5 3,-6 5,-6 11,-5 2,-5 4,-5 9,-4 0,1 0,2 0,3 3,0 4,2 6,5 2,-2 4,-4 7,-5 2,0 5,0 7,0 1,6 4,6 10,7 0,5 0,11 0,16 -2,0 -3,0 -4,0 0,2 0,3 0,4 7,1 21,10 25,14 4,0 7,1 10,1 0,2 0,3 -1,5 3,0 5,-1 8,-1 0,-1 0,-2 0,-3 2,0 5,-1 8,-1 -1,-2 -1,-3 -1,-5 6,-2 8,-7 13,-8 1,2 2,5 3,8 -3,5 -3,7 -4,13 -1,0 -2,0 -2,0 0,3 0,3 -1,7 -1,0 -2,0 -3,1 -2,4 -3,8 -4,12 -7,4 -12,6 -20,7 -3,7 -13,25 -19,27 -1,11 -5,21 -6,32 -2,0 -3,0 -5,0 0,1 0,2 1,3 -1,1 -2,2 -3,3 -1,0 -3,0 -4,0 -5,-13 -10,-26 -15,-39 0,-15 0,-15 -1,-17 2,-5 2,-5 1,-7 0,2 -1,3 -2,4 -4,0 -8,-1 -9,-5 1,-1 2,-2 4,-2 -2,-1 -3,-2 -4,-2 -1,-1 -1,-2 -1,-3 -4,-1 -4,-3 -5,-7 -5,0 -9,0 -13,0z "
              fill="yellow"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Irkutsk */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Irkutsk"
              d="M786 217c-11,-10 -5,-34 3,-45 5,-3 5,-3 8,-6 3,0 7,-1 11,-2 2,-2 3,-5 5,-7 8,0 10,-2 29,-1 0,0 0,1 1,2 2,1 5,1 8,1 2,6 3,6 8,9 -1,10 -5,20 -6,30 -4,0 -9,2 -12,-1 -2,1 -4,2 -5,3 0,4 1,8 -4,9 0,5 -1,8 -3,13 -1,0 -2,0 -3,0 0,-2 -1,-4 -1,-7 -4,1 -4,1 -11,5 -9,-1 -19,0 -28,-3z"
              fill="yellow"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Kamchatka */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Kamchatka"
              d="M853 198c1,-10 5,-20 6,-30 3,-11 12,-13 14,-24 3,0 5,-1 7,-1 0,-1 1,-2 1,-3 3,-1 5,-2 8,-2 1,1 1,3 2,5 2,1 4,1 6,2 0,1 0,2 1,3 2,-1 4,-2 6,-2 1,-3 2,-5 3,-7 1,0 2,0 4,0 0,-3 0,-6 0,-8 -3,-1 -6,-1 -8,-2 0,-3 1,-7 1,-10 7,-3 12,-7 19,-8 1,-2 2,-4 3,-5 1,-1 2,-1 3,-1 0,-1 0,-2 1,-3 4,0 24,8 28,4 -1,0 -3,0 -4,0 0,-1 0,-2 0,-3 -1,0 -2,0 -3,0 0,-1 0,-2 0,-3 6,0 12,0 18,0 1,2 1,3 1,4 2,-1 2,-1 6,-2 -1,1 -1,1 -2,2 2,1 2,1 7,1 8,5 8,5 11,6 2,4 4,5 7,8 0,-1 1,-2 1,-3 4,2 4,2 9,8 1,-1 2,-1 4,-1 -2,4 -2,4 -3,9 -1,0 -3,-1 -5,-1 0,1 0,2 0,2 1,1 3,1 4,1 -1,1 -1,1 -9,1 -2,-1 -2,-1 -2,-5 -1,0 -2,0 -3,-1 0,0 0,-1 0,-2 -3,0 -5,1 -8,1 0,-2 -1,-4 -2,-6 0,3 0,6 0,9 -3,0 -5,0 -8,0 0,1 0,2 0,2 2,1 4,1 6,1 0,2 -1,4 -1,6 1,0 2,0 3,0 0,2 0,4 0,5 -2,0 -4,0 -5,0 -1,-1 -1,-1 -1,-2 -5,3 -11,10 -14,15 -7,-1 -8,-2 -14,1 -1,-1 -2,-3 -3,-4 -1,1 -3,2 -4,4 1,0 2,0 2,1 0,3 -4,3 -4,5 1,0 2,0 3,0 -1,3 -2,5 -3,8 1,0 2,0 3,0 0,6 0,10 -5,12 0,7 -1,9 -4,16 -1,0 -2,1 -2,1 -4,-5 -7,-11 -10,-16 -2,-22 13,-23 19,-40 1,0 2,-1 3,-1 -3,0 -4,0 -7,2 0,1 0,1 1,2 -2,2 -4,4 -5,7 -2,0 -4,0 -5,0 -1,-2 -1,-4 -1,-6 -3,1 -6,1 -9,2 -1,3 -1,3 -2,8 1,0 2,1 3,1 0,1 0,2 0,3 -12,0 -40,-6 -44,6 -5,2 -8,8 -11,13 3,1 7,2 10,3 0,1 0,2 1,3 0,-1 1,-3 1,-4 2,0 4,0 6,1 6,13 7,28 5,43 -3,-2 -5,-3 -8,-4 0,-2 -1,-3 -1,-5 -1,0 -3,0 -4,-1 0,-1 0,-1 0,-2 -3,0 -6,0 -9,0 -3,-9 -10,-8 -12,-18 0,0 -1,-1 -2,-1z M962 99c-1,0 -2,-1 -3,-1 0,-1 1,-2 1,-3 3,-2 3,-2 7,-2 -2,2 -2,2 -3,6 -1,0 -2,0 -2,0z"
              fill="yellow"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Middle East */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Middle East"
              d="M548 321c0,-1 -1,-3 -1,-4 4,-2 6,-2 10,-2 -3,4 -4,5 -9,6z M563 353c-1,-5 -3,-10 -4,-15 7,-8 4,-17 6,-28 -1,0 -1,-1 -2,-1 -3,2 -3,2 -11,3 0,0 -1,-1 -2,-2 -3,1 -6,2 -9,3 0,-1 -1,-2 -1,-3 -1,1 -3,1 -5,1 -2,-10 -5,-15 -5,-21 8,-15 0,0 8,-15 2,2 3,4 5,6 3,0 6,0 8,0 0,-1 0,-2 0,-3 9,0 15,-3 20,4 2,-1 6,0 14,-2 1,1 1,1 5,3 1,2 1,4 1,6 2,0 3,0 4,0 1,2 3,5 4,7 4,0 4,0 9,-2 1,8 1,8 2,11 7,1 11,2 17,0 0,-1 0,-3 0,-4 7,-1 13,-5 16,3 4,2 5,3 8,7 -1,6 -1,11 -1,16 1,0 2,0 3,1 1,6 3,13 4,19 1,0 2,0 3,0 -1,6 -4,10 -4,16 -5,1 -11,1 -16,2 0,-2 0,-4 0,-6 -2,0 -6,1 -15,0 -1,-1 -1,-2 -1,-3 -2,0 -3,0 -4,-1 -4,-12 -3,-15 -15,-12 0,4 2,6 4,10 1,0 2,0 3,0 2,4 4,8 6,12 1,-2 2,-3 3,-4 3,2 0,6 0,9 1,1 2,1 3,2 4,-3 8,-6 13,-10 3,8 7,9 10,15 -1,4 -3,7 -5,10 0,0 -1,-1 -1,-1 -1,3 -1,6 -2,9 -1,1 -3,1 -4,1 -4,14 -34,22 -45,26 0,-16 -6,-29 -16,-42 0,-9 -7,-18 -11,-27l0 0z"
              fill="yellow"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Mongolia */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Mongolia"
              d="M836 271c-7,-10 -24,-9 -34,-11 0,-3 0,-6 0,-9 -3,0 -5,-1 -8,-1 -3,-7 -3,-12 -8,-18 0,-2 0,-3 1,-4 -2,-4 -1,-7 -1,-11 9,3 19,2 28,3 7,-4 7,-4 11,-5 0,3 1,5 1,7 1,0 2,0 3,0 2,-5 3,-8 3,-13 5,-1 4,-5 4,-9 1,-1 3,-2 5,-3 3,3 8,1 12,1 1,0 2,1 2,1 2,10 9,9 12,18 3,0 6,0 9,0 0,1 0,1 0,2 1,1 3,1 4,1 0,2 1,3 1,5 3,1 5,2 8,4 -4,5 -8,11 -11,17 -2,0 -3,0 -5,0 0,0 -1,-1 -1,-1 -4,6 -4,10 -9,15 0,1 0,2 0,3 6,3 11,10 11,17 -4,3 -6,5 -11,5 1,-5 1,-6 -2,-10 0,1 -1,1 -1,2 -2,-3 -3,-3 -2,-7 -5,0 -8,0 -12,2 0,-5 1,-5 -1,-9 -3,2 -5,5 -9,8z"
              fill="yellow"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Siam */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Siam"
              d="M757 369c1,-4 2,-8 4,-12 1,-1 2,-1 3,-1 1,-4 1,-4 1,-7 0,0 1,0 2,0 1,-6 1,-8 4,-13 2,-1 3,-2 5,-2 3,8 3,9 0,16 1,0 3,0 4,0 0,1 0,2 0,3 1,-1 2,-1 2,-1 6,-10 14,-14 20,-3 -3,1 -5,3 -7,5 0,2 -1,5 -1,8 2,0 3,1 5,1 0,2 1,3 1,4 1,1 2,1 3,2 0,0 0,1 0,2 1,0 2,0 3,0 0,9 0,14 -3,21 -2,0 -4,0 -5,0 -2,4 -2,6 -6,8 -1,-2 -1,-2 -2,-6 -1,0 -2,0 -2,0 0,-2 0,-3 0,-4 -3,-1 -5,-1 -7,-2 0,-1 0,-1 0,-2 -1,0 -2,0 -4,0 0,-2 0,-3 0,-4 0,0 -1,0 -1,0 -1,11 -6,13 2,24 3,1 5,2 7,3 1,6 0,11 1,17 -1,0 -2,0 -4,0 -7,-6 -7,-8 -9,-18 0,0 -1,0 -2,0 -7,-12 2,-31 -14,-35 0,-2 0,-3 0,-4z"
              fill="yellow"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* China */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="China"
              d="M803 361c1,-2 1,-3 2,-5 2,0 5,0 7,-1 0,2 0,3 0,4 -3,1 -6,1 -9,2z M802 349c-6,-11 -14,-7 -20,3 0,0 -1,0 -2,1 0,-1 0,-2 0,-3 -1,0 -3,0 -4,0 3,-7 3,-8 0,-16 -2,0 -3,1 -5,2 -1,-3 -2,-6 -3,-8 -5,1 -7,6 -13,8 0,2 0,3 1,5 -3,0 -6,1 -8,1 0,1 0,2 0,3 -3,0 -5,1 -8,1 1,-2 1,-3 1,-5 -3,0 -6,-1 -10,-1 -4,-4 -18,-13 -25,-14 0,-1 0,-2 0,-4 1,0 2,0 4,0 0,-5 0,-11 0,-16 -6,-1 -9,-1 -10,-7 -2,0 -5,0 -7,0 -2,-7 -7,-19 -7,-26 4,-1 7,-1 11,-2 0,-1 0,-2 0,-3 11,-4 5,-15 16,-18 2,-6 3,-7 7,-11 10,3 27,19 38,11 12,7 30,13 44,10 10,2 27,1 34,11 -1,1 -1,1 -4,0 1,1 2,3 3,4 1,0 2,1 4,1 -1,1 -1,2 -1,3 3,0 5,-1 14,0 0,1 0,1 0,2 -6,2 -9,3 -13,6 3,3 3,3 4,6 3,1 3,1 5,2 -1,1 -2,1 -3,2 2,0 3,1 4,1 1,2 1,3 1,5 -2,0 -2,0 -2,1 1,0 1,0 2,0 0,1 0,2 0,4 -1,0 -1,0 -2,0 7,14 -23,43 -36,41 0,1 0,3 0,5 -5,-1 -5,-4 -10,-5z M841 345c1,-8 1,-8 3,-12 1,0 2,0 2,0 0,-1 0,-2 0,-3 2,0 3,0 4,0 -2,8 -2,11 -9,15z"
              fill="yellow"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Japan */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Japan"
              d="M887 288c0,2 0,3 0,5 0,0 -1,0 -2,0 -1,2 -2,5 -2,7 -2,0 -3,1 -5,1 0,-5 2,-6 2,-10 -1,1 -3,1 -4,2 0,-2 0,-5 0,-8 8,1 8,1 11,3z M887 288c0,-1 -1,-2 -1,-3 4,-6 8,-7 14,-8 0,1 0,2 0,3 -3,5 -6,5 -11,9 0,-1 -1,-1 -2,-1z M900 280c0,-1 0,-2 0,-3 -1,0 -1,-1 -2,-2 -7,2 -13,8 -20,9 0,-1 0,-2 0,-3 5,-2 13,-10 16,-14 2,0 4,1 6,1 -2,-4 -1,-5 0,-9 1,0 2,0 3,0 -1,-7 -2,-9 -1,-15 3,0 3,0 5,1 0,-1 0,-3 0,-5 0,0 1,0 2,0 0,0 -1,-1 -1,-2 -1,0 -2,1 -3,1 0,-1 0,-2 0,-3 -2,2 -2,2 -1,6 -1,0 -3,0 -4,0 0,-2 0,-3 0,-5 2,-4 2,-4 5,-5 0,-1 -1,-3 -1,-5 -1,0 -2,-1 -3,-1 -3,-17 -12,-23 -8,-42 6,3 8,13 8,20 4,3 6,5 7,10 -1,0 -2,-1 -3,-1 0,7 1,13 7,17 0,1 0,2 0,2 1,0 3,0 5,0 0,3 1,5 1,7 -12,2 -3,13 -7,22 1,5 3,8 1,13 -2,0 -4,0 -5,1 0,2 0,3 0,5 -2,1 -5,2 -7,3 0,-1 0,-2 0,-3z"
              fill="yellow"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Siberia */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Siberia"
              d="M758 250c-6,-12 1,-24 -14,-27 1,-5 2,-6 2,-9 -6,-4 -11,-8 -13,-14 -12,-5 -16,-18 -18,-31 -3,-4 -4,-9 -8,-10 -1,-7 -1,-9 -5,-15 0,0 -1,1 -1,1 -2,-6 -2,-6 -3,-13 -2,0 -4,-1 -6,-1 1,-2 1,-2 0,-4 0,0 1,0 2,1 1,-3 1,-3 -1,-10 -3,0 -6,0 -9,1 -1,-6 -1,-12 -2,-17 1,-1 2,-2 3,-3 2,6 3,6 9,8 -2,-4 -2,-4 -2,-7 1,0 1,-1 1,-1 8,5 6,11 14,13 0,-1 0,-2 0,-2 -1,0 -2,0 -3,0 0,-1 0,-2 0,-3 -1,0 -1,0 -2,0 0,-3 0,-5 0,-8 0,0 -1,1 -1,1 -3,-3 -4,-4 -4,-9 3,-1 6,-3 9,-4 0,1 0,3 0,4 3,-1 5,-3 7,-5 0,0 -1,-1 -2,-2 8,-3 20,-11 29,-11 3,2 3,2 9,1 1,-2 1,-4 1,-5 9,-4 10,-2 17,3 1,3 1,3 3,3 0,-1 0,-1 0,-2 4,0 9,0 14,1 2,3 1,5 1,9 -5,3 -10,6 -15,9 5,0 8,-4 13,-4 0,1 0,2 1,3 1,0 1,0 2,0 0,-1 0,-2 0,-3 5,1 5,1 7,3 2,-6 9,-4 14,-4 0,1 0,2 1,3 2,1 4,2 6,2 1,6 2,7 2,12 -5,6 -6,12 -8,19 -8,0 -15,0 -23,2 0,1 1,2 1,3 1,1 2,1 3,1 0,1 0,2 0,3 2,1 5,2 8,2 0,1 0,2 0,3 -1,1 -3,2 -4,3 3,8 3,18 4,26 -3,3 -3,3 -8,6 -8,11 -14,35 -3,45 0,4 -1,7 1,11 -1,1 -1,2 -1,4 5,6 5,11 8,18 3,0 5,1 8,1 0,3 0,6 0,9 -14,3 -32,-3 -44,-10z M747 54c1,0 2,0 3,0 1,4 1,4 2,5 1,0 1,0 2,-1 0,2 1,5 2,7 -2,0 -4,0 -6,0 -1,-1 -1,-1 -3,-11z M747 54c-2,3 -2,3 -3,8 -10,-1 -10,-1 -15,-3 1,-12 8,-8 18,-8 0,1 0,2 0,3z M725 53c0,1 1,2 1,4 -4,0 -5,2 -8,2 -1,-2 -1,-2 0,-6 3,0 5,0 7,0z M725 53c-4,-2 -4,-2 -5,-4 1,-1 1,-3 2,-4 6,-1 7,-1 12,3 -3,2 -6,3 -9,5z"
              fill="yellow"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Ural */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Ural"
              d="M720 239c-5,-7 -3,-17 -15,-16 -3,-4 -14,-18 -18,-19 -6,-11 -17,-7 -27,-7 -1,-2 -1,-4 -2,-5 -1,-1 -3,-1 -4,-1 -1,-16 0,-33 -2,-50 -6,-10 0,-14 7,-22 0,-5 0,-9 1,-13 3,-4 3,-10 7,-14 4,3 8,5 12,7 -4,8 -7,12 1,17 0,5 0,10 -1,15 -3,2 -6,4 -9,6 6,0 6,0 13,-2 2,-4 2,-4 2,-11 1,0 3,0 4,0 1,1 2,2 3,3 1,2 1,2 0,4 2,0 4,1 6,1 1,7 1,7 3,13 0,0 1,-1 1,-1 4,6 4,8 5,15 4,1 5,6 8,10 2,13 6,26 18,31 2,6 7,10 13,14 0,3 -1,4 -2,9 15,3 8,15 14,27 -11,8 -28,-8 -38,-11z"
              fill="yellow"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Yakutsk */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Yakutsk"
              d="M797 166c-1,-8 -1,-18 -4,-26 1,-1 3,-2 4,-3 0,-1 0,-2 0,-3 -3,0 -6,-1 -8,-2 0,-1 0,-2 0,-3 -1,0 -2,0 -3,-1 0,-1 -1,-2 -1,-3 8,-2 15,-2 23,-2 2,-7 3,-13 8,-19 0,-5 -1,-6 -2,-12 1,0 2,-1 3,-2 -1,0 -3,0 -4,0 0,-1 0,-2 0,-3 7,0 19,-4 22,4 -1,1 -2,1 -3,1 1,2 1,3 1,4 5,0 8,4 13,4 1,-1 2,-3 3,-5 4,3 8,-1 10,2 3,0 5,-1 8,-1 -2,-1 -3,-2 -5,-2 0,-2 0,-4 -1,-5 3,0 6,0 8,-1 0,1 0,2 0,3 4,-2 4,-2 12,-3 0,1 0,2 0,3 -3,0 -3,0 -3,1 15,2 30,4 44,8 0,0 -1,1 -1,1 1,1 3,1 5,2 0,1 0,2 0,3 -1,1 -2,3 -3,5 -7,1 -12,5 -19,8 0,3 -1,7 -1,10 2,1 5,1 8,2 0,2 0,5 0,8 -2,0 -3,0 -4,0 -1,2 -2,4 -3,7 -2,0 -4,1 -6,2 -1,-1 -1,-2 -1,-3 -2,-1 -4,-1 -6,-2 -1,-2 -1,-4 -2,-5 -3,0 -5,1 -8,2 0,1 -1,2 -1,3 -2,0 -4,1 -7,1 -2,11 -11,13 -14,24 -5,-3 -6,-3 -8,-9 -3,0 -6,0 -8,-1 -1,-1 -1,-2 -1,-2 -19,-1 -21,1 -29,1 -2,2 -3,5 -5,7 -4,1 -8,2 -11,2z M857 87c0,-1 1,-3 2,-4 8,0 8,0 9,1 -5,1 -6,2 -11,3z M857 77c-2,7 -11,2 -15,0 0,-1 0,-2 0,-3 2,0 3,0 5,0 0,-1 0,-2 1,-3 1,1 2,1 3,1 0,1 0,2 1,3 1,1 3,1 5,2z M862 77c0,-2 0,-3 0,-4 4,0 4,0 10,4 -3,0 -7,0 -10,0z"
              fill="yellow"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
          </g>
          <g id="Australia" data-continent="Australia" strokeWidth="1.5">
            {/* Eastern Australia */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Eastern Australia"
              d="M875 600c0,-18 0,-35 0,-52 -14,0 -28,0 -41,0 -1,-13 -2,-25 -3,-38 1,0 2,-1 2,-1 0,-8 6,-9 12,-10 0,-1 0,-2 0,-4 1,0 2,0 3,0 0,2 1,3 2,4 1,-1 1,-1 12,0 -1,4 -3,4 -5,6 0,1 0,3 0,5 7,3 11,6 18,5 2,-4 2,-13 4,-21 1,-1 2,-1 3,-1 1,6 2,10 6,14 0,0 1,-1 1,-1 5,8 5,18 15,21 0,1 0,3 0,4 1,0 2,1 3,1 0,1 1,2 1,3 32,8 6,44 3,62 -8,2 -14,5 -22,6 -1,-1 -2,-2 -3,-3 -2,3 -2,5 0,8 3,0 7,1 10,1 -2,8 -3,10 -11,12 -4,-9 -1,-16 -10,-21z M948 594c-8,-10 -19,-5 -11,-21 7,0 12,-6 13,-13 2,0 4,-1 6,-1 1,-4 3,-7 4,-11 1,0 2,0 3,0 0,2 1,4 1,5 4,1 4,1 5,3 0,1 -1,2 -1,3 1,0 2,0 3,0 -2,2 -2,2 -9,20 -1,0 -2,0 -3,0 -3,6 -5,12 -11,15z M972 556c-1,-8 -2,-9 -7,-14 4,-7 -1,-13 -1,-20 3,3 3,7 6,10 0,0 1,-1 1,-1 1,1 2,3 3,4 2,0 5,1 7,1 0,-1 1,-1 1,-2 1,2 2,4 3,6 -2,7 -2,7 -7,15 -2,0 -4,1 -6,1z"
              fill="purple"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* New Guinea */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="New Guinea"
              d="M936 485c-2,-1 -5,-2 -8,-3 -2,-3 -2,-5 -6,-6 0,-1 0,-2 0,-3 -1,0 -2,0 -3,0 -1,8 -12,2 -18,1 1,-2 2,-4 3,-6 -1,-1 -2,-1 -3,-1 0,-1 0,-2 -1,-3 -2,-1 -4,-2 -6,-2 0,-1 1,-1 1,-2 -1,0 -2,0 -4,0 0,1 0,1 0,2 -1,0 -2,0 -3,-1 0,-1 0,-3 -1,-4 3,0 3,0 3,-1 -1,0 -3,0 -4,0 0,-2 0,-3 0,-4 -1,0 -2,-1 -2,-1 4,-4 9,-4 14,-2 -1,2 -2,4 -3,6 1,0 2,0 3,1 0,1 0,1 0,2 2,-1 4,-3 6,-4 0,-1 -1,-1 -1,-2 2,-2 21,8 24,10 1,2 2,4 3,6 1,0 2,0 3,1 0,1 0,2 1,3 -2,0 -3,0 -4,0 1,2 1,3 2,5 1,0 2,0 3,0 0,1 0,3 0,4 1,1 3,1 5,2 -1,0 -3,1 -4,2z M955 461c0,1 0,2 -1,3 -8,3 -8,3 -19,5 -1,-1 -1,-2 -1,-3 7,-2 14,-3 21,-5z M955 461c-4,-3 -4,-3 -7,-4 0,-1 0,-2 0,-3 3,1 6,2 10,3 0,1 0,2 1,3 -1,0 -3,1 -4,1z "
              fill="purple"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Western Australia */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Western Australia"
              d="M831 510c1,13 2,25 3,38 13,0 27,0 41,0 0,17 0,34 0,52 -2,-1 -3,-1 -5,-1 0,-3 -1,-6 -2,-9 -4,0 -4,0 -5,-2 1,0 2,-1 3,-1 0,-1 0,-2 0,-3 -1,2 -1,2 -5,2 1,-1 2,-3 3,-4 -2,0 -5,1 -8,2 -2,-4 -4,-7 -5,-10 -12,-2 -25,-1 -32,9 -5,0 -8,-1 -13,-1 -11,6 -19,10 -30,5 2,-7 2,-11 1,-18 -5,-14 -5,-14 -5,-17 1,0 1,0 2,0 -2,-5 -1,-10 0,-15 1,0 3,0 4,0 0,-1 0,-1 0,-2 7,-4 14,-6 21,-9 0,-1 0,-2 0,-3 9,-3 12,-9 21,-12 0,-1 1,-2 1,-4 1,1 1,1 2,2 0,-1 0,-2 0,-3 1,0 3,-1 4,-1 1,1 2,2 3,3 -1,0 -1,1 -1,1 0,0 1,1 2,1z"
              fill="purple"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Indonesia */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Indonesia"
              d="M838 474c0,0 1,-1 2,-1 3,-1 3,-1 6,1 -3,2 -7,4 -11,6 0,-2 0,-3 0,-4 1,-1 2,-1 3,-2z M822 476c0,0 0,0 0,1 -2,-1 -4,-1 -6,-1 0,0 0,1 0,2 -10,-2 -10,-2 -30,-2 0,-1 0,-2 0,-3 -2,-1 -5,-2 -7,-3 0,-2 0,-4 0,-6 -2,0 -3,0 -4,0 -3,-8 -6,-11 -8,-18 -1,0 -2,-1 -2,-1 -1,-4 -1,-4 -4,-10 -1,0 -2,0 -3,0 0,-2 -1,-4 -1,-6 5,1 5,1 13,4 2,6 5,8 7,13 1,0 2,-1 3,-1 1,6 2,7 8,8 0,1 0,2 0,3 -1,0 -2,1 -3,1 1,1 1,3 2,4 0,0 1,-1 1,-1 2,2 0,4 4,6 0,1 0,2 0,3 0,1 1,1 2,1 0,-1 0,-2 0,-2 9,-1 16,3 26,5 0,1 0,2 0,3 0,0 1,0 2,0z M822 476c3,-6 13,-3 18,-3 -1,0 -2,1 -2,1 -2,0 -3,0 -4,0 0,1 0,2 0,3 -4,-1 -8,-1 -12,-1z M821 465c1,-11 4,-16 10,-24 5,0 10,0 15,-2 0,0 0,1 0,2 -4,2 -8,4 -13,3 0,1 0,2 -1,3 3,0 6,0 8,0 -1,2 -1,2 -6,2 0,4 0,8 0,12 -2,0 -4,0 -5,0 0,-2 0,-4 0,-7 -1,0 -1,0 -2,0 -1,4 -1,7 -2,11 -2,0 -3,0 -4,0z M802 460c-2,-4 -4,-5 -3,-9 -1,0 -2,0 -2,0 -1,-5 0,-7 2,-12 2,0 4,-1 7,-2 0,-1 0,-2 0,-3 3,-1 6,-2 8,-3 1,-3 2,-5 3,-7 1,0 2,0 3,0 0,-2 0,-3 1,-4 4,0 5,0 9,1 0,1 0,2 0,3 -1,0 -2,0 -3,1 1,0 1,1 2,2 -3,3 -6,8 -2,13 -7,5 -8,10 -11,17 -3,2 -3,2 -14,3z M841 458c0,-1 0,-1 0,-2 7,-1 11,-2 17,2 -6,0 -11,0 -17,0z M849 445c0,-2 0,-5 0,-8 1,1 2,1 3,1 0,3 1,5 1,7 -1,0 -3,0 -4,0z M875 394c0,-1 0,-1 1,-2 0,0 1,0 2,-1 0,4 0,4 -2,7 -1,0 -3,1 -4,1 1,-2 2,-4 3,-5z M853 396c1,-5 8,-10 12,-14 0,8 -4,13 -12,14z M876 392c-1,1 -1,1 -1,2 -1,0 -2,0 -3,0 0,-6 -3,-8 -4,-12 4,0 8,4 12,6 -2,0 -3,1 -4,2 0,1 0,1 0,2z M881 388c0,-2 -1,-5 -1,-8 -3,0 -5,0 -8,0 -1,-1 -1,-2 -2,-3 0,0 -1,0 -2,0 -2,-1 -3,-1 -4,-4 2,0 3,0 4,0 0,-2 0,-2 -2,-2 -1,-3 -1,-6 -1,-9 1,1 2,3 3,4 -2,-5 -2,-5 -1,-11 3,0 6,0 9,0 0,2 -1,3 -1,5 1,0 1,0 2,1 0,2 -1,4 -2,6 0,0 -1,-1 -1,-1 0,2 0,4 1,7 2,0 4,1 6,2 2,5 3,7 3,12 -1,1 -2,1 -3,1z"
              fill="purple"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
          </g>
          <g id="Europe" data-continent="Europe" fill="none" strokeWidth="1.5">
            {/* Great Britain */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Great Britain"
              d="M455 226c-7,2 -23,9 -30,8 0,-1 -1,-2 -1,-3 4,-2 8,-5 12,-7 -2,0 -4,-1 -6,-1 2,-6 2,-6 3,-12 1,0 3,0 5,0 0,-3 -1,-5 1,-8 -1,0 -2,-1 -3,-1 0,-1 0,-3 0,-4 -1,0 -2,0 -3,0 0,-9 2,-13 3,-21 5,-2 5,-2 8,-2 1,2 1,2 0,6 2,1 4,2 6,3 -8,11 0,20 2,33 1,0 2,0 3,1 0,2 0,5 0,8z M415 226c-1,-5 1,-9 0,-20 1,-1 2,-1 4,-2 -1,-2 -1,-2 0,-5 5,0 8,0 13,2 0,3 0,6 0,9 -1,0 -2,0 -3,0 -1,10 -3,14 -14,16z"
              fill="green"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Iceland */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Iceland"
              d="M425 156c-2,-1 -5,-2 -8,-2 0,-2 0,-3 -1,-5 3,0 3,0 3,-1 -1,0 -3,0 -4,0 0,-1 0,-2 0,-3 1,0 3,0 4,0 0,0 0,-1 0,-1 -1,0 -3,0 -4,0 0,-1 1,-2 1,-3 4,-2 4,-2 8,-1 0,0 0,1 0,2 7,0 15,-1 23,-1 1,1 2,1 3,2 -1,1 -1,1 -2,2 1,0 2,0 3,0 -2,12 -17,10 -26,11z"
              fill="green"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Northern Europe */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Northern Europe"
              d="M470 242c-2,-3 -3,-4 -6,-4 -2,-5 -6,-7 -9,-11 0,0 0,-1 0,-1 2,-3 4,-6 7,-7 2,-8 2,-8 4,-9 0,0 0,1 1,1 3,-4 6,-6 1,-10 0,-4 1,-8 2,-12 4,-2 5,-3 7,-7 1,6 3,15 -2,19 5,3 9,4 14,5 0,-1 1,-2 1,-3 1,1 2,2 2,3 4,-4 9,-4 14,-5 0,1 -1,2 -1,3 1,0 1,0 2,1 0,-1 0,-2 0,-3 1,0 3,-1 4,-1 3,2 6,4 9,6 2,8 3,14 3,23 -1,0 -2,0 -3,0 0,2 -1,3 -1,5 -2,5 -2,5 -4,16 -6,4 -8,3 -15,2 -1,-15 2,-14 -12,-15 0,2 -1,3 -1,5 -8,2 -10,2 -17,-1z"
              fill="green"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Scandinavia */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Scandinavia"
              d="M541 95c-9,16 1,39 1,57 -3,4 -6,8 -7,13 0,0 -1,0 -2,0 -8,1 -10,2 -19,2 -5,-7 -3,-12 -3,-20 1,0 2,0 3,0 4,-8 7,-11 7,-20 -2,1 -4,1 -6,2 -4,9 -8,15 -14,23 0,7 3,15 2,21 -1,0 -1,0 -2,0 -1,1 -1,3 -1,4 -1,1 -2,1 -3,1 -1,10 -1,17 -11,18 -2,-8 -7,-13 -6,-22 -1,0 -1,0 -2,0 -1,-1 -1,-1 -1,-2 -1,2 -3,4 -4,7 -5,0 -7,-1 -12,-3 -2,-11 -3,-12 -4,-21 1,-1 2,-1 2,-1 0,-2 0,-2 0,-3 0,-1 1,-1 2,-1 0,-1 -1,-1 -1,-1 3,-4 6,-3 8,-8 0,0 1,0 2,0 1,-1 2,-3 2,-5 5,1 5,-3 6,-7 1,0 2,-1 4,-2 -1,0 -1,-1 -1,-1 3,-4 3,-4 6,-15 1,0 1,1 2,1 0,-1 0,-2 0,-2 1,0 2,0 3,0 0,-4 0,-7 0,-11 3,-1 5,1 7,0 0,-1 -1,-3 -1,-4 4,-1 4,1 8,1 1,-4 1,-5 5,-5 0,-1 -1,-2 -1,-4 3,-3 8,-2 11,-2 0,2 0,2 2,2 0,-2 0,-3 0,-5 1,-1 3,-1 5,-1 -1,5 -1,5 0,6 1,-2 2,-3 3,-5 4,1 7,3 11,4 0,1 0,2 0,3 0,1 -1,2 -2,3 0,1 0,2 1,3z M557 48c1,0 2,0 3,1 0,1 0,2 0,2 -1,0 -2,0 -3,0 -1,5 -1,5 -5,5 -2,2 -4,5 -6,7 -2,-1 -3,-1 -4,-2 0,0 1,-1 1,-1 -1,-1 -2,-1 -3,-2 0,-3 0,-3 1,-4 -2,-1 -3,1 -6,0 0,-4 -2,-4 -3,-8 5,1 10,1 15,-1 1,1 1,3 2,4 1,-2 2,-5 4,-7 0,2 1,4 2,6 0,0 1,0 2,0z M560 61c-1,-1 -2,-2 -3,-3 1,-1 1,-2 1,-4 3,0 5,0 7,-1 -1,1 -1,1 -1,2 0,0 1,1 2,1 -2,3 -2,3 -6,5z M557 48c0,-3 0,-5 0,-7 2,0 4,0 6,0 0,1 0,2 1,3 2,0 4,0 6,0 0,1 -1,3 -1,5 -3,0 -5,0 -8,0 0,0 1,0 1,-1 -1,0 -3,0 -5,0z"
              fill="green"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Southern Europe */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Southern Europe"
              d="M521 316c-1,-2 -1,-2 -4,-3 0,-1 0,-2 0,-3 10,1 10,1 15,3 0,1 0,2 1,3 -4,0 -8,0 -12,0z M474 288c-1,-3 -1,-6 -2,-10 1,0 2,0 3,0 0,3 0,6 1,9 -1,0 -2,1 -2,1z M527 289c-1,0 -2,0 -3,0 0,0 1,1 1,1 -1,0 -2,0 -2,1 0,-3 0,-3 -2,-3 1,7 4,16 -2,21 0,0 -1,-1 -1,-1 0,0 -1,1 -1,1 -3,-6 -3,-6 -5,-18 -1,0 -1,0 -2,0 -2,-6 -5,-11 -7,-15 -6,-2 -12,-13 -16,-19 -4,6 7,20 12,22 3,4 6,9 8,13 -2,-1 -3,-2 -5,-4 -3,4 -2,6 0,9 -1,0 -2,0 -3,0 -2,3 -4,6 -6,8 -1,1 -2,1 -2,1 -2,-2 -3,-3 -4,-4 1,-1 1,-1 6,-1 1,-3 2,-5 3,-7 -3,-6 -9,-14 -13,-17 0,0 0,0 0,-1 -1,-1 -2,-1 -3,-1 -1,-3 -2,-5 -2,-8 -2,0 -5,0 -7,0 0,1 -1,2 -1,2 -8,-4 -5,-21 1,-26 7,3 9,3 16,1 0,-2 1,-3 1,-5 14,1 11,0 12,15 7,1 9,2 15,-2 2,-11 2,-11 4,-16 4,-2 4,-2 8,-2 0,1 0,2 1,3 1,1 2,1 3,1 2,10 2,17 10,22 -3,4 -4,9 -6,14 -8,15 0,0 -8,15z"
              fill="green"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Ukraine */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Ukraine"
              d="M583 279c1,-9 -5,-13 -12,-15 0,-2 -1,-4 -1,-6 -1,0 -2,0 -3,0 1,-5 3,-7 6,-10 -5,1 -10,1 -13,5 2,1 2,1 5,1 0,1 0,1 0,2 -1,1 -2,1 -3,1 0,1 -1,2 -1,3 -5,0 -5,0 -10,-3 1,-2 1,-2 1,-4 -1,0 -3,1 -4,1 -1,0 -2,-1 -2,-1 3,-2 3,-2 3,-3 -1,0 -3,0 -5,0 0,2 0,5 0,8 -2,0 -2,0 -2,1 -9,-5 -9,-12 -11,-22 -1,0 -2,0 -3,-1 -1,-1 -1,-2 -1,-3 -4,0 -4,0 -8,2 0,-2 1,-3 1,-5 1,0 2,0 3,0 0,-9 -1,-15 -3,-23 -3,-2 -6,-4 -9,-6 0,-3 -1,-6 -1,-8 1,0 1,0 2,0 1,-4 3,-7 4,-11 1,0 2,-1 3,-1 0,1 -1,3 -1,4 2,0 3,1 5,1 0,-2 0,-4 0,-6 -1,0 -2,0 -3,0 0,-1 1,-2 1,-3 -1,-1 -2,-1 -2,-1 0,-1 0,-2 1,-3 3,-1 3,-1 6,1 6,-3 6,-3 12,-4 -1,-3 -2,-3 -5,-5 1,0 2,0 2,0 1,-5 4,-9 7,-13 0,-18 -10,-41 -1,-57 4,-3 5,-3 11,-3 0,2 0,3 0,4 7,1 32,14 23,23 -5,2 -5,2 -19,3 0,-1 -1,-3 -1,-3 -2,-1 -3,-1 -4,-1 1,6 6,9 11,11 0,3 -1,5 -1,7 6,0 11,-1 17,1 -1,-2 -1,-4 -2,-6 4,-2 5,-2 9,-2 1,-2 3,-4 4,-7 -1,0 -2,-1 -3,-1 0,-4 -2,-9 0,-13 5,1 8,2 10,7 -1,0 -2,0 -3,0 0,2 0,4 1,6 1,0 3,0 4,0 1,-2 1,-3 1,-5 5,0 6,-3 11,-3 0,-2 1,-3 1,-5 5,-2 8,-4 14,-4 -1,0 -3,1 -4,2 0,1 0,1 0,2 5,-1 7,-2 12,-1 0,-1 1,-3 2,-4 0,1 0,2 0,3 2,0 3,0 4,0 -1,-4 -1,-4 -2,-7 0,0 0,-1 1,-1 4,0 6,2 19,4 1,2 1,2 3,4 -1,4 -1,8 -1,13 -7,8 -13,12 -7,22 2,17 1,34 2,50 1,0 3,0 4,1 1,1 1,3 2,5 -1,0 0,13 0,15 -11,7 -14,5 -25,6 0,-1 -1,-2 -1,-3 -9,1 -23,2 -31,7 -7,12 4,19 0,33 -2,5 -6,11 1,13 0,4 0,7 0,11 2,1 5,2 7,2 -1,4 -3,8 -4,12 -5,2 -5,2 -9,2 -1,-2 -3,-5 -4,-7 -1,0 -2,0 -4,0 0,-2 0,-4 -1,-6 -5,-2 -5,-2 -6,-3z M638 98c-1,0 -1,1 -1,1 -2,-1 -3,-1 -5,-2 0,-5 0,-5 1,-6 2,2 3,5 5,7z M615 86c-4,-2 -8,-4 -12,-6 2,-4 3,-7 7,-9 -1,-1 -1,-1 -2,-2 5,-5 5,-5 12,-8 0,-1 0,-2 0,-3 10,-2 10,-2 13,-4 0,2 0,3 0,4 -3,3 -9,6 -16,11 0,1 0,1 0,2 -1,0 -1,0 -2,0 0,7 2,8 4,14 -2,0 -3,0 -4,1z"
              fill="green"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Western Europe */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Western Europe"
              d="M428 312c-1,-2 -3,-3 -4,-5 -3,0 -6,0 -9,0 -3,-11 -2,-19 -1,-31 0,0 0,-1 -1,-1 3,-12 15,-7 26,-7 5,-6 0,-16 -1,-22 -4,-1 -4,-1 -8,-4 3,-1 3,-1 4,-4 1,0 2,0 3,0 0,1 0,1 0,2 6,-2 6,-2 7,-3 0,0 -1,0 -2,-1 0,-1 0,-1 0,-2 2,0 5,0 8,0 1,-1 1,-1 2,-5 3,0 3,0 3,-2 3,4 7,6 9,11 3,0 4,1 6,4 -6,5 -9,22 -1,26 -1,1 -1,2 -1,3 -4,0 -8,0 -12,1 -6,11 -7,12 -7,25 -1,3 -4,5 -6,8 -6,1 -10,2 -14,7 0,0 -1,0 -1,0z"
              fill="green"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
          </g>
          <g
            id="North America"
            data-continent="North America"
            fill="none"
            strokeWidth="1.5"
          >
            {/* Alaska */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Alaska"
              d="M109 174c1,1 2,2 3,3 0,2 0,4 0,7 -1,-3 -3,-5 -5,-8 1,0 1,-1 2,-2z M105 164c0,-1 0,-2 0,-3 3,0 3,0 3,-1 -3,-1 -6,-3 -8,-4 -1,1 -1,2 -1,2 -2,0 -4,-1 -6,-2 0,-1 0,-2 0,-3 0,-1 -1,-1 -2,-1 0,1 -1,1 -1,2 -1,0 -2,-1 -3,-1 1,-1 1,-1 2,-2 -1,0 -2,-1 -2,-1 0,0 -1,1 -1,2 -3,-1 -3,-2 -5,-5 -3,0 -3,0 -6,-2 -1,0 -1,0 -2,1 1,1 2,3 3,5 -8,2 -13,4 -17,5 0,-1 0,-3 0,-4 3,-2 5,-4 8,-6 -6,1 -10,7 -15,6 0,2 0,4 0,5 -2,2 -5,4 -7,5 0,1 0,2 0,3 -6,3 -24,13 -29,9 5,-4 10,-6 16,-8 0,0 0,-1 0,-2 4,-1 4,-3 5,-7 -2,1 -5,1 -7,1 0,-2 0,-2 -2,-2 0,1 0,2 0,2 -1,1 -3,1 -5,1 0,-3 0,-3 2,-7 -1,0 -2,0 -3,0 -1,-5 -3,-4 -6,-3 -1,-2 -1,-4 -2,-6 2,-1 3,-2 4,-2 -1,-1 -2,-1 -2,-1 0,-2 0,-4 1,-5 5,-3 9,-1 15,0 2,-3 5,-6 4,-9 -7,4 -8,3 -14,-2 1,-1 2,-3 3,-5 1,0 1,1 1,1 0,-2 0,-3 0,-5 3,0 6,0 9,0 -2,-4 -5,-8 -4,-12 1,-1 2,-1 4,-1 0,-1 0,-2 0,-3 9,-2 19,-9 29,-8 0,1 -1,2 -1,3 13,0 26,5 40,6 0,17 0,33 0,49 2,0 5,0 7,0 8,10 12,20 12,32 -4,-2 -11,-12 -13,-17 -1,0 -3,0 -4,0z M44 172c0,-3 2,-8 6,-7 1,1 1,2 1,3 -2,1 -5,2 -7,4z"
              fill="red"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Alberta */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Alberta"
              d="M136 205c-1,-1 -2,-3 -4,-4 0,-2 1,-4 1,-6 -3,-3 -6,-6 -8,-8 -1,0 -3,0 -4,0 0,-2 0,-3 0,-5 1,1 1,1 1,2 0,-1 0,-2 0,-3 0,-12 -4,-22 -12,-32 34,2 69,4 103,6 -1,17 -3,35 -4,52 -24,-1 -49,-1 -73,-2z"
              fill="red"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Central America */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Central America"
              d="M212 363c-3,-4 -6,-9 -9,-14 0,-7 0,-7 -2,-16 -10,-6 -18,-11 -28,-16 0,-1 0,-2 0,-3 1,0 3,0 4,0 0,-1 -1,-3 -1,-4 -9,-7 -11,-18 -16,-27 -1,0 -1,1 -2,1 4,11 8,20 11,32 -6,-2 -9,-10 -10,-16 -4,-1 -5,-2 -7,-6 0,-1 1,-1 1,-2 -2,-6 -3,-10 -8,-13 -1,-1 -1,-3 -1,-4 4,0 9,0 14,0 8,9 18,0 27,10 2,0 4,1 6,2 2,2 4,4 6,6 -2,8 -2,22 5,26 4,0 8,0 12,0 4,-4 11,-4 14,2 -2,5 -9,8 -13,11 2,1 4,2 6,3 0,2 0,4 0,6 -6,4 -8,5 -11,11 0,1 1,2 1,2 6,1 8,0 13,4 -5,3 -8,3 -12,5z"
              fill="red"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Eastern United States */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Eastern United States"
              d="M197 293c-2,-2 -4,-4 -6,-6 0,-4 0,-9 0,-13 1,0 3,0 5,-1 1,-4 2,-8 3,-13 2,0 5,-1 8,-1 0,-3 1,-6 2,-9 6,0 12,0 18,0 1,-15 1,-29 2,-43 3,1 6,1 9,1 1,1 2,2 3,3 0,1 0,2 0,3 -1,0 -2,0 -3,0 0,1 0,2 0,3 5,-1 9,-1 13,-2 1,1 2,2 2,3 2,-1 2,-1 6,0 0,1 0,2 0,3 -2,0 -4,0 -5,0 -4,6 -6,9 -7,17 2,-1 4,-1 5,-2 1,-3 1,-7 2,-10 1,0 1,0 2,0 1,-3 2,-4 4,-5 3,3 2,5 0,9 1,0 3,0 4,0 0,0 0,1 0,1 0,2 -1,4 -1,6 7,1 13,-5 20,-7 0,-1 0,-1 0,-2 3,-1 6,-2 9,-3 7,0 7,0 17,-2 0,-2 0,-4 1,-5 5,-2 5,-2 9,-2 0,4 0,8 0,13 -15,1 -18,18 -33,21 -1,1 -2,3 -3,5 -1,0 -1,-1 -2,-1 0,2 0,4 0,6 -4,5 -11,9 -17,11 -1,4 -1,4 -3,8 0,8 0,15 -3,22 -2,1 -4,1 -6,2 -2,-14 -2,-14 -1,-20 -8,0 -10,-1 -20,-2 0,2 1,4 1,6 -10,-5 -19,-4 -30,0 -3,5 -3,5 -5,6z"
              fill="red"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Greenland */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Greenland"
              d="M373 174c-4,-2 -6,-3 -8,-8 -2,0 -4,0 -7,-1 0,-1 0,-2 -1,-3 -1,0 -2,0 -2,-1 -1,-1 -1,-3 -1,-5 -2,-1 -3,-1 -4,-2 0,-1 1,-1 2,-2 -1,-4 -2,-7 -3,-11 0,0 -1,-1 -2,-1 0,-2 0,-4 0,-5 1,-1 2,-1 3,-1 -4,-3 -2,-5 0,-9 1,1 1,1 2,2 0,-2 1,-5 2,-7 2,0 2,0 2,-1 -2,0 -4,0 -6,0 0,1 1,2 1,4 -1,0 -3,0 -4,0 0,-1 0,-3 0,-4 0,-1 1,-1 1,-2 -1,0 -1,0 -2,-1 1,-4 1,-4 2,-6 6,3 6,3 8,3 -1,-2 -2,-4 -3,-6 -2,1 -3,1 -5,1 0,-10 -2,-22 -12,-25 0,-1 0,-2 0,-4 -1,0 -3,1 -4,1 -2,-2 -2,-2 -3,-5 -7,1 -9,5 -17,3 -1,-3 -1,-3 -2,-3 0,1 0,2 0,3 -5,-1 -5,-1 -8,-2 -1,-4 -1,-7 -2,-10 3,-1 3,-1 10,1 0,-1 0,-2 0,-3 -2,0 -5,0 -8,-1 2,-7 10,-3 16,-7 -2,-1 -3,-2 -4,-3 6,-3 10,-5 17,-7 0,-1 0,-2 0,-2 2,0 4,0 6,0 1,-4 1,-4 4,-5 1,0 2,1 3,2 0,-2 1,-4 2,-5 3,0 5,4 6,4 0,-1 0,-2 0,-3 2,0 3,1 4,1 1,-1 1,-2 1,-2 2,-1 4,-1 6,-1 0,1 1,2 1,3 2,-1 2,-1 10,-3 0,0 -1,-1 -1,-1 1,-2 1,-2 13,-2 -1,-3 -1,-6 -1,-9 3,0 6,0 10,-1 0,1 0,2 0,3 0,0 1,0 2,0 3,-9 19,-7 26,-7 0,1 0,2 0,3 13,3 22,7 36,8 0,1 0,2 0,3 -2,0 -4,1 -6,2 5,-1 10,-3 13,0 -4,4 -6,4 -13,4 0,1 0,2 0,3 4,0 9,-1 12,2 0,-1 1,-1 1,-2 7,-1 14,-1 21,-2 1,1 1,2 2,3 -1,1 -1,1 -10,3 1,0 1,2 1,3 -1,0 -2,0 -4,0 -2,4 -3,9 -7,10 -1,3 -1,6 -1,9 1,0 2,0 3,0 0,2 0,3 0,4 0,1 -1,1 -2,1 0,2 0,4 0,6 -1,0 -2,1 -3,1 0,1 0,2 0,3 2,0 4,0 6,1 0,1 0,3 0,4 -8,1 -7,5 -8,11 -2,0 -2,-3 -3,-3 0,1 0,2 0,3 -3,-1 -6,-4 -9,-1 2,1 4,2 7,3 0,3 1,6 2,9 -2,2 -3,4 -4,6 -2,-1 -2,-1 -5,-1 -3,-4 -4,-8 -5,-8 0,1 0,3 0,5 -2,1 -3,1 -5,2 3,1 6,1 9,2 -1,1 -2,3 -2,4 -10,3 -18,8 -28,10 0,0 0,-1 0,-2 -5,4 -7,6 -12,6 0,1 0,2 0,3 -7,2 -14,2 -20,4 0,7 -4,10 -8,16 -2,8 -4,12 -8,18z"
              fill="red"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Northwest Territory */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Northwest Territory"
              d="M213 155c-34,-2 -69,-4 -103,-6 -2,0 -5,0 -7,0 0,-16 0,-32 0,-49 2,2 5,3 7,4 1,-1 1,-2 1,-3 4,-1 4,-1 10,-1 0,0 0,1 1,1 -1,0 -2,1 -3,1 7,2 7,2 9,3 5,-3 13,-3 18,-3 0,0 -1,-1 -1,-1 2,0 4,0 6,0 3,-3 4,-3 8,-3 0,1 -1,2 -1,3 2,0 4,-1 10,-2 0,1 0,2 0,3 -1,0 -2,0 -3,0 0,1 0,1 0,2 9,-4 18,2 27,4 0,1 0,2 0,3 -1,0 -2,0 -2,0 0,1 0,2 0,3 6,0 9,-1 15,-2 0,1 0,2 0,3 1,0 2,0 3,0 0,-1 0,-2 0,-3 1,0 2,0 4,0 0,-1 -3,-1 -3,-2 9,-1 13,0 22,2 -1,15 -2,29 -3,44 -5,-1 -10,-1 -15,-1z M228 156c1,-15 2,-29 3,-44 2,0 4,-1 6,-1 -1,-4 -1,-4 -2,-6 3,-1 5,-1 8,-1 0,2 0,3 0,4 2,0 4,0 6,0 0,-1 0,-1 0,-2 -1,0 -2,0 -3,0 3,-17 9,-26 26,-28 -4,6 -5,10 -12,10 -2,10 0,14 3,23 1,-1 2,-3 2,-4 2,1 3,1 4,2 1,2 1,4 1,6 2,0 3,0 4,0 0,-1 0,-2 0,-3 1,0 2,-1 3,-1 0,-1 1,-3 1,-4 3,0 5,0 8,0 0,0 0,-1 0,-1 -3,0 -5,0 -8,0 0,-1 0,-2 -1,-3 0,0 -1,1 -1,1 -4,-6 -3,-7 -2,-15 5,-2 8,-4 14,-4 -1,2 -3,4 -4,6 0,2 1,4 1,5 2,-3 3,-5 6,-7 0,-2 1,-4 1,-6 4,0 8,0 12,0 -2,4 -3,7 -4,11 2,0 4,0 6,0 0,-6 0,-6 1,-7 9,5 9,5 10,6 0,1 0,2 -1,3 8,1 8,1 9,2 0,1 -1,1 -1,2 1,0 3,0 4,0 0,1 -1,2 -1,3 2,1 5,3 7,4 0,1 -1,2 -1,3 -1,0 -3,0 -4,0 0,0 0,1 0,2 1,0 3,0 4,0 0,1 0,2 0,3 2,0 3,0 5,0 1,2 2,5 3,8 1,0 1,0 2,0 -1,2 -2,4 -3,5 -2,1 -3,1 -4,1 -2,-2 -2,-2 -4,-7 -1,1 -1,1 -1,7 1,1 2,2 3,3 -1,3 -1,6 -1,9 -4,0 -5,0 -8,-3 0,2 0,2 2,6 -8,-1 -14,-2 -16,-10 -5,0 -10,0 -15,-1 0,-1 1,-2 1,-3 9,-3 13,-5 18,-6 0,-1 -1,-2 -1,-3 -1,0 -3,0 -4,1 0,-2 -1,-3 -1,-5 -2,0 -4,0 -7,1 1,-1 1,-3 1,-3 1,-1 2,-1 3,-1 0,-1 0,-2 0,-3 -1,0 -2,0 -2,0 -1,-3 -1,-5 -2,-8 -1,0 -1,0 -1,3 -3,0 -5,1 -8,2 0,-1 -1,-2 -1,-2 -1,0 -3,0 -4,1 0,12 -4,20 -18,19 -2,3 -2,4 -5,5 -3,-2 -4,-4 -7,-4 1,2 3,4 5,6 -5,5 -13,8 -20,12 -1,4 -3,7 -5,11 -3,0 -6,0 -10,0z M264 145c1,-5 3,-10 6,-15 2,1 4,1 5,1 0,1 0,2 0,2 1,0 2,0 3,0 1,4 2,4 5,6 -1,2 -1,2 -2,5 -1,0 -2,0 -3,0 0,-1 0,-3 0,-4 -6,1 -8,4 -14,5z M296 124c-1,-6 -1,-6 5,-8 0,2 0,4 0,6 -2,0 -3,1 -5,2z M195 110c-1,-3 -3,-6 -4,-8 -4,-1 -9,-1 -13,-2 -1,-2 -2,-5 -2,-8 2,0 4,-1 6,-2 -1,0 -3,0 -4,-1 1,-1 1,-1 9,-3 -2,0 -4,-1 -5,-1 -1,-1 -1,-2 -1,-3 6,-1 7,-2 14,-2 3,4 5,3 9,5 -1,1 -1,1 -2,1 3,1 6,2 8,2 1,-2 1,-2 3,-3 2,3 2,4 5,5 1,-4 3,-8 4,-12 3,6 3,10 2,17 3,1 5,2 8,3 0,1 0,1 0,2 -1,1 -2,1 -3,1 1,0 1,1 2,1 -2,1 -3,1 -4,2 0,1 1,2 1,3 -4,0 -5,0 -9,-2 -8,2 -16,3 -24,5z M235 95c-1,-3 -2,-6 -2,-9 0,-1 1,-2 2,-3 1,1 1,2 1,3 1,-2 2,-3 3,-4 0,0 1,1 1,1 0,-1 1,-2 1,-2 1,0 2,0 4,0 -1,1 -1,3 -2,5 3,-1 6,-2 8,-2 -3,5 -3,5 -6,6 0,1 0,2 0,2 -4,1 -7,2 -10,3z M160 88c0,-2 -1,-4 -1,-6 2,-1 3,-1 5,-1 0,-2 0,-3 0,-4 6,-1 6,-1 10,-3 0,-1 0,-2 0,-3 2,0 4,0 5,0 1,2 1,3 1,5 3,-1 5,-1 7,-1 0,1 -1,2 -1,3 -4,2 -8,3 -12,5 0,1 -1,2 -1,3 -9,0 -9,0 -13,2z M252 79c0,-2 0,-5 0,-8 1,1 2,1 3,1 1,3 1,5 1,7 -1,0 -2,0 -4,0z M199 78c-2,-1 -3,-1 -4,-2 0,-1 1,-1 1,-1 -2,-1 -3,-1 -5,-1 0,-1 0,-2 0,-3 1,0 3,-1 4,-2 -1,0 -1,-1 -1,-1 -3,1 -6,2 -8,3 -1,-1 -1,-2 -1,-2 -2,0 -3,0 -4,0 2,-8 15,-7 21,-7 0,1 0,2 0,3 -3,0 -3,0 -5,2 3,0 6,0 8,0 1,1 2,2 2,3 2,-1 4,-1 6,-1 -1,-1 -1,-2 -1,-3 4,-3 8,-1 14,-1 0,1 0,2 0,4 -3,0 -3,0 -3,1 1,0 3,0 4,0 -1,1 -1,3 -1,4 -11,1 -16,1 -27,4z M240 75c-3,-2 -6,-2 -9,-2 0,-3 1,-7 1,-10 1,0 3,0 4,0 0,2 1,3 2,4 0,-1 0,-1 0,-2 3,-1 6,-2 8,-3 1,2 2,4 2,6 -5,3 -5,3 -8,7z M268 74c-5,-2 -8,-3 -11,-8 -2,0 -3,0 -5,0 0,-2 0,-5 0,-8 -5,-1 -5,-7 -6,-12 1,0 2,0 3,0 0,1 0,2 0,3 4,2 5,2 7,6 1,0 2,0 3,0 1,4 3,7 5,10 8,0 16,1 24,1 0,2 0,5 -1,7 -6,0 -13,1 -19,1z M290 65c-2,-1 -4,-1 -6,-2 0,-1 0,-1 0,-2 -1,-1 -2,-1 -2,-1 -1,1 -1,1 -1,2 -3,0 -5,-1 -7,-1 0,-1 -1,-2 -1,-3 -1,0 -3,0 -4,0 -1,-1 -1,-2 -1,-3 -4,1 -5,1 -8,-1 -1,-1 -1,-2 -1,-3 3,0 6,0 9,0 1,-1 1,-2 2,-3 1,0 2,0 3,0 0,1 1,2 1,3 1,0 2,0 3,0 0,-1 0,-2 0,-3 -4,-1 -7,-2 -10,-2 1,-2 1,-2 9,-3 0,-1 0,-1 1,-2 -3,0 -7,-1 -10,-1 0,-1 0,-2 0,-3 11,-7 25,-5 38,-6 -13,-5 -27,1 -40,1 -1,-1 -2,-1 -2,-2 1,-1 3,-2 5,-2 0,-2 1,-5 1,-7 3,0 5,1 8,1 1,-1 1,-2 2,-3 10,0 20,-1 30,-2 0,1 0,2 0,3 18,-1 18,-1 27,-3 0,2 0,4 1,5 4,1 18,-1 13,7 -1,1 -4,1 -10,4 0,1 0,2 0,3 -4,0 -6,-1 -8,0 0,1 1,1 1,2 -1,1 -19,6 -21,8 0,1 0,2 1,3 -7,2 -7,2 -10,5 0,1 0,2 0,3 -5,0 -6,-1 -10,-4 0,5 1,6 3,10 -2,1 -4,1 -6,2z M224 57c0,-2 0,-3 0,-5 1,0 3,0 4,0 0,1 0,3 0,4 -1,0 -2,0 -4,1z M241 57c-4,-6 -9,-5 -6,-13 5,3 5,3 7,4 1,4 1,5 -1,9z"
              fill="red"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Ontario */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Ontario"
              d="M264 231c2,-1 2,-1 3,-6 1,1 3,1 4,2 0,-2 -1,-4 -1,-6 -16,-5 -8,-18 -29,-10 -1,-1 -2,-2 -3,-3 -3,0 -6,0 -9,-1 -6,0 -13,0 -20,0 1,-17 3,-35 4,-52 5,0 10,0 15,1 4,0 7,0 10,0 0,2 0,4 0,5 1,1 2,1 3,1 0,2 0,4 1,6 2,2 5,3 8,5 0,1 0,2 0,3 7,2 15,6 23,5 0,8 1,11 5,17 0,8 -1,17 -1,25 1,0 2,0 4,0 0,-1 0,-2 0,-2 5,0 7,0 11,4 -3,1 -6,2 -9,3 0,1 0,1 0,2 -7,2 -13,8 -20,7 0,-2 1,-4 1,-6z"
              fill="red"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Western United States */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Western United States"
              d="M144 275c-15,-24 -15,-33 -9,-61 1,0 2,0 4,0 -1,-5 -1,-5 -3,-9 24,1 49,1 73,2 7,0 14,0 20,0 -1,14 -1,28 -2,43 -6,0 -12,0 -18,0 -1,3 -2,6 -2,9 -3,0 -6,1 -8,1 -1,5 -2,9 -3,13 -2,1 -4,1 -5,1 0,4 0,9 0,13 -2,-1 -4,-2 -6,-2 -9,-10 -19,-1 -27,-10 -5,0 -10,0 -14,0z"
              fill="red"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Quebec */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Quebec"
              d="M319 229c0,-5 0,-9 0,-13 -4,0 -4,0 -9,2 -1,1 -1,3 -1,5 -10,2 -10,2 -17,2 -4,-4 -6,-4 -11,-4 0,0 0,1 0,2 -2,0 -3,0 -4,0 0,-8 1,-17 1,-25 1,0 2,0 3,0 2,-5 2,-11 2,-17 5,-1 7,-3 11,-6 0,-7 -1,-9 -3,-15 8,-2 7,-8 8,-16 5,3 10,2 15,2 0,1 1,2 1,3 1,0 3,0 4,0 3,6 3,6 4,9 -1,0 -2,0 -3,0 1,3 3,5 4,7 2,1 3,1 5,1 0,0 0,-1 0,-2 2,-1 4,-2 6,-3 0,-1 0,-3 1,-5 1,-1 3,-2 5,-2 3,7 4,17 6,24 2,2 5,4 8,6 -4,2 -4,2 -4,4 3,0 6,0 9,0 0,3 1,6 1,9 -9,2 -9,2 -15,4 -7,7 -18,1 -22,6 2,1 5,2 7,3 0,1 0,2 1,4 -2,0 -3,0 -4,1 2,1 2,1 2,7 3,0 6,0 10,0 0,1 0,2 0,3 -9,3 -13,9 -21,10 0,-1 0,-2 0,-3 2,-1 3,-2 5,-3 -1,-1 -1,-1 -5,0z M357 219c0,-1 -1,-2 -2,-4 -1,1 -2,2 -4,3 0,-1 0,-2 0,-3 -2,0 -5,0 -7,-1 0,-2 1,-3 2,-5 1,0 2,0 3,0 1,-6 9,-10 15,-11 -1,2 -1,4 -1,5 -2,0 -3,0 -4,0 0,1 0,2 1,3 1,-1 2,-1 3,-1 0,1 1,3 2,4 -1,0 -2,1 -2,1 0,1 0,2 0,3 1,0 2,1 3,1 0,1 0,2 -1,3 -2,1 -5,1 -8,2z"
              fill="red"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            <text x="coordinate_x" y="coordinate_y" fill="black">
              East Africa
            </text>
          </g>
          <g
            id="South America"
            data-continent="South America"
            fill="none"
            strokeWidth="1.5"
          >
            {/* Argentina */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Argentina"
              d="M227 487c1,0 3,-1 5,-2 0,2 1,5 1,8 6,2 5,8 10,10 1,-2 2,-4 3,-5 1,0 2,-1 3,-1 3,4 4,3 9,1 0,1 0,2 0,3 1,0 3,0 5,0 4,7 9,7 16,10 -1,4 -2,7 -4,11 4,-1 8,-1 12,-2 2,-4 2,-4 5,-6 1,1 1,2 2,3 -4,3 -10,8 -11,13 2,0 4,0 7,0 0,2 0,2 0,6 5,1 6,4 10,8 0,2 0,2 -3,2 0,1 0,2 -1,4 -8,3 -12,-1 -19,-1 5,5 5,5 7,8 0,0 1,0 1,-1 0,3 0,6 0,8 -3,2 -13,3 -16,5 -1,6 -1,6 -2,9 -2,0 -4,0 -6,0 0,-1 -1,-1 -1,-2 0,1 0,3 0,4 0,1 1,2 2,3 -3,1 -3,1 -3,2 1,0 2,0 2,0 0,1 0,2 0,3 0,0 -1,0 -2,0 0,1 0,2 1,3 -4,4 -4,4 -4,9 1,0 2,1 3,1 -1,3 -1,5 -1,8 -1,0 -2,1 -3,1 1,3 -2,8 -2,11 1,1 3,3 4,4 0,1 -1,1 -2,2 1,1 1,2 1,3 1,0 2,0 3,0 0,1 0,2 0,3 6,3 12,6 18,10 -14,4 -13,3 -26,0 1,-1 1,-1 1,-2 -1,0 -2,0 -3,0 0,-2 0,-3 0,-4 -2,0 -4,0 -5,0 0,-1 0,-2 0,-3 0,0 -1,1 -2,1 -1,-1 -3,-1 -5,-2 0,-2 0,-3 0,-4 -9,-5 -9,-14 -10,-23 -3,0 -3,0 -4,-2 1,-1 1,-2 1,-3 -1,0 -1,0 -2,0 0,-1 0,-3 0,-4 2,0 2,0 2,-1 -1,0 -1,0 -2,0 0,-2 0,-4 0,-6 1,0 2,0 4,0 0,-4 0,-9 0,-13 -1,0 -2,-1 -3,-1 5,-28 6,-61 4,-89 M309 631c-2,0 -3,-1 -4,-2 -1,-3 -1,-6 -1,-9 7,-7 9,-5 18,-4 0,1 0,3 1,5 -6,2 -7,3 -10,9 -2,1 -3,1 -4,1z M295 630c-1,-2 -1,-2 -4,-3 0,0 0,-1 0,-2 1,0 2,-1 4,-1 -1,-1 -2,-3 -3,-4 3,-2 6,-3 9,-4 0,3 0,7 1,10 -3,3 -3,3 -7,4z"
              fill="blue"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Brazil */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Brazil"
              d="M292 514c-2,-10 -4,-13 -13,-18 0,-2 -1,-4 -1,-6 1,-1 2,-1 3,-1 -5,-12 -5,-12 -7,-14 -5,-1 -5,-1 -6,-3 1,-1 1,-2 2,-2 -1,-1 -2,-2 -3,-2 0,-2 0,-3 0,-4 -8,-1 -14,-5 -20,-9 -2,-4 -2,-4 -3,-6 -5,3 -11,8 -16,7 -2,-6 -2,-6 -1,-9 -4,0 -5,2 -9,2 -5,-8 -14,-6 -9,-17 1,0 2,0 3,0 0,-7 1,-7 7,-9 0,0 1,1 1,1 3,0 5,0 8,0 4,-7 -1,-11 0,-18 2,-1 5,-2 7,-2 4,3 6,2 11,2 3,-3 3,-4 3,-9 6,-1 7,-3 15,-4 3,7 3,7 4,13 7,-1 14,1 20,-4 7,5 13,-5 16,-11 8,3 7,7 9,16 2,0 5,0 7,0 -1,2 -2,4 -3,5 5,1 10,1 16,3 0,1 0,2 0,2 14,-1 25,9 38,12 -1,14 -3,19 -14,27 0,3 -1,7 -1,10 -1,1 -2,1 -3,1 0,5 0,11 0,16 -12,18 -11,19 -31,23 0,1 0,2 0,3 -2,0 -3,0 -4,0 -5,8 -5,11 -5,20 -6,3 -9,9 -11,15 -1,0 -1,0 -2,0 -4,-4 -5,-7 -10,-8 0,-4 0,-4 0,-6 -3,0 -5,0 -7,0 1,-5 7,-10 11,-13 -1,-1 -1,-2 -2,-3z"
              fill="blue"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Peru */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Peru"
              d="M227 487c-4,-1 -7,-3 -10,-5 0,-1 0,-2 0,-2 -4,-3 -8,-5 -12,-7 -5,-7 -17,-29 -25,-34 0,-2 0,-5 0,-7 2,-2 4,-3 4,-7 -1,1 -2,2 -3,3 -4,-7 4,-16 7,-22 6,4 14,4 16,10 14,5 14,5 16,6 0,0 -1,1 -1,1 -6,2 -7,2 -7,9 -1,0 -2,0 -3,0 -5,11 4,9 9,17 4,0 5,-2 9,-2 -1,3 -1,3 1,9 5,1 11,-4 16,-7 1,2 1,2 3,6 6,4 12,8 20,9 0,1 0,2 0,4 1,0 2,1 3,2 -1,0 -1,1 -2,2 1,2 1,2 6,3 2,2 2,2 7,14 -1,0 -2,0 -3,1 0,2 1,4 1,6 9,5 11,8 13,18 -3,2 -3,2 -5,6 -4,1 -8,1 -12,2 2,-4 3,-7 4,-11 -7,-3 -12,-3 -16,-10 -2,0 -4,0 -5,0 0,-1 0,-2 0,-3 -5,2 -6,3 -9,-1 -1,0 -2,1 -3,1 -1,1 -2,3 -3,5 -5,-2 -4,-8 -10,-10 0,-3 -1,-6 -1,-8 -2,1 -4,2 -5,2z"
              fill="blue"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
            {/* Venezuela */}
            <path
              className={`country ${!showCountries ? "hidden" : ""}`}
              id="Venezuela"
              d="M219 423c0,0 1,-1 1,-1 -2,-1 -2,-1 -16,-6 -2,-6 -10,-6 -16,-10 1,-7 4,-13 5,-21 -1,0 -2,0 -3,-1 0,-3 1,-7 2,-10 1,0 2,0 3,0 0,-2 0,-4 0,-5 2,2 3,3 3,6 1,1 2,1 3,2 0,-2 -1,-4 -1,-6 4,-2 8,-5 12,-8 4,-2 7,-2 12,-5 2,0 3,0 5,-1 1,1 2,2 3,3 -1,1 -2,3 -4,4 1,2 2,3 3,5 0,-2 0,-2 0,-3 1,-1 1,-1 2,-1 0,-1 0,-2 0,-3 5,-1 8,0 13,2 10,0 18,-2 21,9 0,0 1,0 2,0 1,1 1,2 1,4 6,1 9,3 13,7 1,-1 2,-1 2,-2 5,3 16,7 19,9 -3,6 -9,16 -16,11 -6,5 -13,3 -20,4 -1,-6 -1,-6 -4,-13 -8,1 -9,3 -15,4 0,5 0,6 -3,9 -5,0 -7,1 -11,-2 -2,0 -5,1 -7,2 -1,7 4,11 0,18 -3,0 -5,0 -8,0 0,0 -1,-1 -1,-1z"
              fill="blue"
              strokeWidth="1"
              stroke="black"
              opacity="0.5"
              onClick={(e) => handleCountry(e.target.id)}
            />
          </g>
        </g>
      </svg>
    </div>
  );
};

const ForwardRef = React.forwardRef(SvgMap);
export default ForwardRef;
