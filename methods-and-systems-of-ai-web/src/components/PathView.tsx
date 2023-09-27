import React, {MouseEvent, useEffect, useState} from 'react';
import defaultImg from '../img/ukraine_admin_map.jpg';
import '../styles/TaskConfiguration.scss';
import {Point} from "../data/TaskData";
import DatasetManager from "./DatasetManager";

interface Props {
    imgSource?: string;
    coordinates: Point[];
    calculatedPath: Point[];
    setCoordinates: (value: (((prevState: Point[]) => Point[]) | Point[])) => void
}

function PathView({coordinates, calculatedPath, setCoordinates, imgSource = defaultImg}: Props) {
    const [position, setPosition] = useState<Point>({x: 0, y: 0});
    const [imgPosition, setImgPosition] = useState<Point>({x: 0, y: 0});

    const handleMouseMove = (event: MouseEvent<HTMLImageElement>) => {
        const imgElement = event.target as HTMLElement;
        const imgRect = imgElement.getBoundingClientRect();

        const xS = (event.clientX - imgRect.left).toFixed(2);
        const yS = (event.clientY - imgRect.top).toFixed(2);
        const x = Number.parseInt(xS)
        const y = Number.parseInt(yS);
        setPosition({x: x, y: y})
    };

    const handleImageClick = () => {
        if (coordinates.findIndex(x => x.y == position.y && x.x == position.x) >= 0) {
            return;
        }
        let points = [...coordinates, position];
        setCoordinates(points);
    };

    useEffect(() => {
        setDrawPoints(getPointsPositions);
    }, [coordinates]);

    function getPointsPositions() {
        let i = 0;
        return coordinates.map(clickedPoint => {
            let x = clickedPoint.x;
            let y = clickedPoint.y;
            i++;
            return (<div className="point-marker"
                         id={"point-marker: " + clickedPoint.x + "" + clickedPoint.y}
                         style={{
                             left: x + 'px',
                             top: y + 'px',
                             background: i == 1 ? 'red' : 'blue',
                         }}></div>)
        });
    }

    const handleResize = () => {
        // Ensure that the clicked point remains in place when resizing the window
        const imgElement = document.querySelector('.path-image') as HTMLElement;
        const imgRect = imgElement.getBoundingClientRect();
        const newX = (imgRect.left + imgPosition.x).toFixed(2);
        const newY = (imgRect.top + imgPosition.y).toFixed(2);
        const x = Number.parseInt(newX)
        const y = Number.parseInt(newY)
        let newVar: Point = {x: x, y: y};
        setImgPosition(newVar);
    };
    useEffect(() => {
        handleResize();
    }, [])
    useEffect(() => {
        window.addEventListener('resize', handleResize);
        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, [imgPosition]);
    const [drawPoints, setDrawPoints] = useState(getPointsPositions());

// Function to create SVG path from coordinates
    function createPath() {
        const isPresentRes = calculatedPath.length > 2;
        const points = isPresentRes ? calculatedPath : coordinates;
        const color = isPresentRes ? "red" : "black";
        const pathData = points
            .map((point) => `${point.x},${point.y}`)
            .join(' ');
        return (
            <path
                d={`M ${pathData} L ${points[0].x},${points[0].y}`}
                fill="none"
                stroke={color}
            />
        );
    }

    function getSvg() {
        const imgElement = document.querySelector('.path-image'); // Replace with your image selector
        const width = imgElement?.clientWidth || 0;
        const height = imgElement?.clientHeight || 0;
        return <svg className={"path"}
                    xmlns="http://www.w3.org/2000/svg"
                    style={{cursor: 'crosshair', width: `${width}`, height: `${height}`}}>{createPath()}
        </svg>;
    }

    const svg = coordinates.length < 2 ? null : getSvg();



    return (
        <div className="path-view">
            <DatasetManager setPoints={setCoordinates} points={coordinates}/>
            <p>Point number: {coordinates.length}</p>
            <p>X: {position.x} Y: {position.y}</p>
            <div className="img_wrapper"
                 onMouseMove={handleMouseMove}
                 onClick={handleImageClick}>
                <img
                    className="path-image"
                    src={imgSource}
                    alt="map for path"
                    style={{cursor: 'crosshair'}}
                />
                {svg}
                {drawPoints}
            </div>
        </div>
    );
}

export default PathView;
