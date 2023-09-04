import React, {useEffect, useState} from 'react';
import {PlaceComponent} from "./PlaceComponent";
import {FoodTruck, Place} from "./Data";
import {AppBar, Box, Container, Link, Toolbar, Typography} from "@mui/material";
import {FoodTruckComponent} from "./FoodTruckComponent";
import {GitHub} from "@mui/icons-material";
import {Spacer} from "./CommonComponents";


function App() {

    const [drei, setDrei] = useState<Place>()
    const [schicht, setSchicht] = useState<Place>()
    const [beiz, setBeiz] = useState<Place>()
    const [foodTrucks, setFoodTrucks] = useState<FoodTruck[]>()

    useEffect(() => {
        fetch('/api/place/dreiganger')
            .then((response) => response.json() as Promise<Place>)
            .then((place) => {
                setDrei(place)
            })
    }, [])

    useEffect(() => {
        fetch('/api/place/schichtwechsel')
            .then((response) => response.json() as Promise<Place>)
            .then((place) => {
                setSchicht(place)
            })
    }, [])

    useEffect(() => {
        fetch('/api/place/le-beizli')
            .then((response) => response.json() as Promise<Place>)
            .then((place) => {
                setBeiz(place)
            })
    }, [])

    useEffect(() => {
        fetch('/api/food-trucks/today')
            .then((response) => response.json() as Promise<FoodTruck[]>)
            .then((foodTrucks) => {
                setFoodTrucks(foodTrucks)
            })
    }, [])

    return (
        <Container maxWidth="md">
            <Box>
                <AppBar position="static">
                    <Toolbar sx={{ justifyContent: "space-between"}}>
                        <Typography variant={"h6"}>Food Radar Bern</Typography>
                        <Link href={"https://github.com/Fynnyan/foodradar"}><GitHub/></Link>
                    </Toolbar>
                </AppBar>
            </Box>
            <Spacer/>
            {drei && <PlaceComponent place={drei} today={true}/>}
            <Spacer/>
            {schicht && <PlaceComponent place={schicht} today={true}/>}
            <Spacer/>
            {beiz && <PlaceComponent place={beiz} today={true}/>}
            <Spacer/>
            {foodTrucks && <FoodTruckComponent foodTrucks={foodTrucks}/>}
        </Container>
    );
}

export default App;
