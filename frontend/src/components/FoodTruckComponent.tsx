import {FoodTruck} from "./Data";
import {Box, Card, CardContent, CardHeader, Link, SvgIcon, Typography} from "@mui/material";
import {ReactComponent as TruckGoneIcon} from '../icons/truck_gone.svg';
import React from "react";

interface FoodTruckComponentProps {
    foodTrucks: FoodTruck[]
}

export const FoodTruckComponent = (props: FoodTruckComponentProps) => {
    return <Card>
        <CardHeader title={"Today's Food Trucks"}/>
        <CardContent>
            {props.foodTrucks.map((truck) =>
                <>
                    <Typography variant={"body1"}>{truck.name}</Typography>
                    <Link href={truck.web}>{truck.location}</Link>
                </>
            )}
            {props.foodTrucks.length === 0 ? <NoFoodTrucks/> : ""}
        </CardContent>
    </Card>
}

const NoFoodTrucks = () =>
    <Box sx={{display: "flex", flexDirection: "column", alignItems: "center"}}>
        <SvgIcon viewBox={"0 0 36 36"} sx={{fontSize: "10rem"}}>
            <TruckGoneIcon/>
        </SvgIcon>
        <Typography variant={"body1"}>There are no food trucks today</Typography>
    </Box>
