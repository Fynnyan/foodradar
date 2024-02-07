import React, {useEffect, useState} from 'react';
import {DailyMenuComponent, LoadingPlace} from "./PlaceComponent";
import {FoodTruck, Place} from "../data/Data";
import {AppBar, Box, Container, Toolbar, Typography} from "@mui/material";
import {FoodTruckComponent} from "./FoodTruckComponent";
import {GitHub} from "@mui/icons-material";
import {Spacer} from "./common/Spacer";
import {AnimatedFoodRadarIcon} from "../icons/AnimatedFoodRadarIcon";
import {IconLink} from "./common/IconLink";

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
                    <IconLink
                        aria-label={"See the project on Github."}
                        href={"https://github.com/Fynnyan/foodradar"}
                    >
                        <GitHub/>
                    </IconLink>
                </Toolbar>
            </AppBar>
            <Spacer/>
            {drei ? <DailyMenuComponent place={drei} /> : <LoadingPlace title={"Dreigänger"}/>}
            <Spacer/>
            {schicht ? <DailyMenuComponent place={schicht} /> : <LoadingPlace title={"Schichtwechsel"}/>}
            <Spacer/>
            {beiz ? <DailyMenuComponent place={beiz} /> : <LoadingPlace title={"Le-Beizli"}/>}
            <Spacer/>
            {foodTrucks && <FoodTruckComponent foodTrucks={foodTrucks}/>}
        </Container>
    );
}

export default App;
