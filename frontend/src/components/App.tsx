import React, {useEffect, useState} from 'react';
import {PlaceComponent} from "./PlaceComponent";
import {Place} from "./Data";
import {Container} from "@mui/material";

function App() {

    const [drei, setDrei] = useState<Place>()
    const [schicht, setSchicht] = useState<Place>()

    useEffect(() => {
        fetch('/api/food-radar/dreiganger')
            .then((response) => response.json() as Promise<Place>)
            .then((place) => { setDrei(place) })
    })

    useEffect(() => {
        fetch('/api/food-radar/schichtwechsel')
            .then((response) => response.json() as Promise<Place>)
            .then((place) => { setSchicht(place) })
    })

    return (
        <Container maxWidth="md">
            {drei && <PlaceComponent place={drei}/>}
            {schicht && <PlaceComponent place={schicht}/>}
        </Container>
    );
}

export default App;
