import {Card, CardContent, CardHeader, Link, Typography} from "@mui/material";
import {Place, ProcessingStatus} from "./Data";
import {MenuComponent} from "./MenuComponent";
import React from "react";
import LocalDiningIcon from '@mui/icons-material/LocalDining';
import {RadarSpinner} from "./CommonComponents";

interface PlaceComponentProps {
    place: Place
    today: Boolean
}

export const PlaceComponent = (props: PlaceComponentProps) => {
    const now = new Date()
    const dateString = now.toISOString().split("T")[0]

    const menus = props.place.menus
        .filter((it) => props.today ? it.date === dateString : true)

    return <Card>
        <CardHeader
            title={props.place.name}
            titleTypographyProps={{variant: "h5"}}
            avatar={<Link target="_blank" href={props.place.web} sx={{height: "32px"}}><LocalDiningIcon
                sx={{fontSize: "32px"}}/></Link>}
        />
        {menus.map((value, index) => <MenuComponent key={index} menu={value} placeName={props.place.name}/>)}
        {menus.length === 0 && <InfoBox status={props.place.processingStatus}/>}
    </Card>
}

interface LoadingPlaceProps {
    title: string
}

export const LoadingPlace = (props: LoadingPlaceProps) =>
    <Card>
        <RadarSpinner label={props.title}/>
    </Card>


interface InfoBoxProps {
    status: ProcessingStatus
}

const InfoBox = (props: InfoBoxProps) => {
    let text = "Unexpected Status"
    switch (props.status) {
        case ProcessingStatus.PROCESSED:
            text = "There are no menus today or the place is closed."
            break;
        case ProcessingStatus.PROCESS_ERROR:
            text = "Could not parse the site, check the web page for the menu."
            break;
        case ProcessingStatus.SITE_NOT_ACCESSIBLE:
            text = "Could not load the site, check the web page for the menu."
            break;
    }

    return (
        <CardContent>
            <Typography variant={"body1"}>{text}</Typography>
        </CardContent>
    )
}

