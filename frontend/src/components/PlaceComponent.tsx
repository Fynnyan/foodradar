import {Card, CardHeader, Link} from "@mui/material";
import {Place} from "./Data";
import {MenuComponent} from "./MenuComponent";
import React from "react";
import LocalDiningIcon from '@mui/icons-material/LocalDining';

interface PlaceComponentProps {
    place: Place
    today: Boolean
}

export const PlaceComponent = (props: PlaceComponentProps) => {
    const now = new Date()
    const dateString = now.toISOString().split("T")[0]
    return <Card>
        <CardHeader
            title={props.place.name}
            titleTypographyProps={{variant: "h5"}}
            avatar={<Link target="_blank" href={props.place.web} sx={{height: "32px"}}><LocalDiningIcon sx={{fontSize: "32px"}}/></Link>}
        />
        {props.place.menus
            .filter( (it) => it.date === dateString )
            .map((value, index) => <MenuComponent key={index} menu={value}/>)
        }
    </Card>
}