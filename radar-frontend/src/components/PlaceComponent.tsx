import {Box, Card, CardActions, CardContent, CardHeader, Link, Typography} from "@mui/material";
import {getMenuText, Menu, Place, Position, ProcessingStatus, Route} from "../data/Data";
import {CopyMenuButton, MenuComponent} from "./MenuComponent";
import React from "react";
import LocalDiningIcon from '@mui/icons-material/LocalDining';
import {RadarSpinner} from "./common/RadarSpinner";
import {IconLink} from "./common/IconLink";
import {PlaceMap} from "./common/PlaceMap";
import {Spacer} from "./common/Spacer";
import BeachAccessIcon from '@mui/icons-material/BeachAccess';

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
    position?: Position
    routeFromOffice?: Route
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
        <CardActions sx={{gap: "0.5rem"}} disableSpacing={true}>
            <IconLink
                aria-label={`See the ${props.place.name} webseite for the daily menu.`}
                href={props.place.web}
            >
                <LocalDiningIcon/>
            </IconLink>
            <PlaceMap placePosition={props.position}
                      placeName={props.place.name}
                      routeFromOffice={props.routeFromOffice}/>
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

interface ClosedPlaceProps {
    title: string
    subTitle: string
    text: string
}

export const ClosedPlace = (props: ClosedPlaceProps) =>
    <Card>
        <Box sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            padding: "1rem",
            justifyContent: "center"
        }}>
            <Typography variant={"h6"}>{props.title}</Typography>
            <Spacer/>
            <Box sx={{display: "flex", flexDirection: "row" }}>
                <BeachAccessIcon/>
                <Typography variant={"subtitle1"}>{props.subTitle}</Typography>
                <BeachAccessIcon/>
            </Box>
            <Typography>{props.text}</Typography>
        </Box>
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

