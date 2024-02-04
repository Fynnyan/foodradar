import React, {useEffect, useState} from 'react';
import {LoadingPlace, PlaceComponent} from "./PlaceComponent";
import {FoodTruck, Place} from "./Data";
import {AppBar, Box, Container, Link, Toolbar, Typography} from "@mui/material";
import {FoodTruckComponent} from "./FoodTruckComponent";
import {GitHub} from "@mui/icons-material";
import {Spacer} from "./CommonComponents";
import {AnimatedFoodRadarIcon} from "../icons/AnimatedFoodRadarIcon";

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
            .catch(reason => console.log(reason))
    }, [])

    useEffect(() => {
        fetch('/api/place/schichtwechsel')
            .then((response) => response.json() as Promise<Place>)
            .then((place) => {
                setSchicht(place)
            })
            .catch(reason => console.log(reason))
    }, [])

    useEffect(() => {
        fetch('/api/place/le-beizli')
            .then((response) => response.json() as Promise<Place>)
            .then((place) => {
                setBeiz(place)
            })
            .catch(reason => console.log(reason))
    }, [])

    useEffect(() => {
        fetch('/api/food-trucks/today')
            .then((response) => response.json() as Promise<FoodTruck[]>)
            .then((foodTrucks) => {
                setFoodTrucks(foodTrucks)
            })
            .catch(reason => console.log(reason))
    }, [])

    return (
        <Container maxWidth="md">
            <AppBar position="static">
                <Toolbar sx={{justifyContent: "space-between"}}>
                    <Box sx={{display: "flex", flexDirection: "row", alignItems: "center"}}>
                        <AnimatedFoodRadarIcon/>
                        <Typography variant={"h6"}> Food Radar Bern</Typography>
                    </Box>
                    <Link href={"https://github.com/Fynnyan/foodradar"}><GitHub/></Link>
                </Toolbar>
            </AppBar>
            <Spacer/>
            {drei ? <PlaceComponent place={drei} today={true}/> : <LoadingPlace title={"Dreigänger"}/>}
            <Spacer/>
            {schicht ? <PlaceComponent place={schicht} today={true}/> : <LoadingPlace title={"Schichtwechsel"}/>}
            <Spacer/>
            {beiz ? <PlaceComponent place={beiz} today={true}/> : <LoadingPlace title={"Le-Beizli"}/>}
            <Spacer/>
            {foodTrucks && <FoodTruckComponent foodTrucks={foodTrucks}/>}
        </Container>
    );
}

export default App;
