import React, {useState, MouseEvent, useEffect} from 'react';
import defaultImg from '../img/ukraine_admin_map.jpg';
import '../styles/TaskConfiguration.scss';

interface Props {
    imgSource?: string;
}

export type Point = {
    x: number;
    y: number;
}

function PathView({imgSource = defaultImg}: Props) {
    const [coordinates, setCoordinates] = useState<Point[]>([]);
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
        let points = [...coordinates, position];
        setCoordinates(points);
    };

    useEffect(() => {
        setDrawPoints(getDrawPoints);
    }, [coordinates]);

    function getDrawPoints() {
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
    const [drawPoints, setDrawPoints] = useState(getDrawPoints());
    return (
        <div className="path-view">
            <p>Point number: {coordinates.length}</p>
            <p>X: {position.x} Y: {position.y}</p>
            <div className="img_wrapper">
                <img
                    className="path-image"
                    src={imgSource}
                    alt="map for path"
                    onMouseMove={handleMouseMove}
                    onClick={handleImageClick}
                    style={{cursor: 'crosshair'}}
                />
                {drawPoints}
            </div>
        </div>
    );
}

export default PathView;
