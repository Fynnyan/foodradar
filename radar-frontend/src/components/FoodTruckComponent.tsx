import {FoodTruck} from "../data/Data";
import {Box, Card, CardContent, CardHeader, Stack, SvgIcon, Typography} from "@mui/material";
import {ReactComponent as TruckGoneIcon} from '../icons/truck_gone.svg';
import React from "react";
import {IconLink} from "./common/IconLink";
import LocalDiningIcon from "@mui/icons-material/LocalDining";
import {Spacer} from "./common/Spacer";

interface FoodTruckComponentProps {
    currentDay: string
    foodTrucks: FoodTruck[]
}

export const FoodTruckComponent = (props: FoodTruckComponentProps) => {
    return <Card>
        <CardHeader
            title={"Today's Food Trucks"}
            titleTypographyProps={{variant: "h6"}}
            subheader={"Be aware, currently the seasons and holidays are not tracked. Check the website if the truck is present."}
        />
        <CardContent>
            {props.foodTrucks.map((truck) =>
                <FoodTruckCard currentDay={props.currentDay} truck={truck}/>
            )}
            {props.foodTrucks.length === 0 ? <NoFoodTrucks/> : ""}
        </CardContent>
    </Card>
}

const FoodTruckCard = (props: { currentDay: string, truck: FoodTruck }) => {
    const todayLocation =
        props.truck.locations.find(value => value.day == props.currentDay)
    return <>
        <Stack direction={"row"} spacing={2}>
            <Box sx={{flexGrow: 2}}>
                <Typography variant={"h6"}>{props.truck.name}</Typography>
                <Typography variant={"body1"}>{todayLocation?.location}</Typography>
            </Box>

                <IconLink
                    aria-label={`The ${props.truck.name} food truck's webseite.`}
                    href={props.truck.web}
                    sx={{
                        alignSelf: "center",
                        flexGrow: 1,
                        // need to define a max else the stiling gets broken
                        maxWidth: "41.5px",
                        mayHeight: "41.5px",
                    }}
                >
                    <LocalDiningIcon/>
                </IconLink>
        </Stack>
        <Spacer/>
    </>
}

const NoFoodTrucks = () =>
    <Box sx={{display: "flex", flexDirection: "column", alignItems: "center"}}>
        <SvgIcon viewBox={"0 0 36 36"} sx={{fontSize: "5rem"}}>
            <TruckGoneIcon/>
        </SvgIcon>
        <Typography variant={"body1"}>There are no food trucks today</Typography>
    </Box>
