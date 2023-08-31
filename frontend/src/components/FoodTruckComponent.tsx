import {FoodTruck} from "./Data";
import {Card, CardContent, CardHeader, Link, Typography} from "@mui/material";
import React from "react";


interface FoodTruckComponentProps {
    foodTrucks: FoodTruck[]
}

export const FoodTruckComponent = (props: FoodTruckComponentProps) => {
    return <Card>
        <CardHeader title={"Food Trucks Today"}/>
        <CardContent>
            {props.foodTrucks.map((truck) =>
                <>
                    <Typography variant={"body1"}>{truck.name}</Typography>
                    <Link href={truck.web}>{truck.location}</Link>
                </>
            )}
        </CardContent>
    </Card>
}