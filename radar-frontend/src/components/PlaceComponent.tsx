import {Card, CardActions, CardContent, CardHeader, Link, Typography} from "@mui/material";
import {getMenuText, Menu, Place, ProcessingStatus} from "../data/Data";
import {CopyMenuButton, MenuComponent} from "./MenuComponent";
import React from "react";
import LocalDiningIcon from '@mui/icons-material/LocalDining';
import {RadarSpinner} from "./CommonComponents";
import {IconLink} from "./common/IconLink";

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
        {menus.map((value, index) => <MenuComponent key={index} menu={value}/>)}
        {menus.length === 0 && <PlaceStatusInfoBox status={props.place.processingStatus}/>}
    </Card>
}

interface DailyMenuComponentProps {
    place: Place
}

export const DailyMenuComponent = (props: DailyMenuComponentProps) => {

    const now = new Date()
    const dateString = now.toISOString().split("T")[0]

    const menu: Menu | null =
        props.place.menus.filter((it) => it.date === dateString)[0] || null

    return <Card>
        <CardHeader
            title={props.place.name}
            titleTypographyProps={{variant: "h6"}}
        />
        {
            menu != null
                ? <MenuComponent menu={menu}/>
                : <PlaceStatusInfoBox status={props.place.processingStatus}/>
        }
        <CardActions sx={{gap: "0.5rem"}}>
            <IconLink
                aria-label={`See the ${props.place.name} webseite for the daily menu.`}
                href={props.place.web}
            >
                <LocalDiningIcon/>
            </IconLink>
            {
                menu != null && <>
                    <CopyMenuButton text={getMenuText(props.place.name, menu)}/>
                </>
            }
        </CardActions>
    </Card>
}

interface LoadingPlaceProps {
    title: string
}

export const LoadingPlace = (props: LoadingPlaceProps) =>
    <Card>
        <RadarSpinner label={<Typography variant={"h6"}>{props.title}</Typography>}/>
    </Card>


interface InfoBoxProps {
    status: ProcessingStatus
}

export const PlaceStatusInfoBox = (props: InfoBoxProps) => {
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

