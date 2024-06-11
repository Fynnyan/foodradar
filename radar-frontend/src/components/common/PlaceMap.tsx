import {FoodRadarIconButton} from "./FoodRadarIconButton";
import {Dialog, DialogContent, useMediaQuery, useTheme} from "@mui/material";
import React, {useState} from "react";
import {Map} from "@mui/icons-material";

import {MapContainer, Marker, Polyline, Popup, TileLayer} from "react-leaflet";
import {Position, Route} from "../../data/Data";

interface PlaceMapProps {
    placePosition?: Position
    placeName?: string
    routeFromOffice?: Route
}

export const PlaceMap = (props: PlaceMapProps) => {

    const theme = useTheme()
    const [open, setOpen] = useState(false)

    const onClose = () => {
        setOpen(false)
    }
    const onOpen = () => {
        setOpen(true)
    }
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const isMdScreen = () => useMediaQuery(theme.breakpoints.up('md'));

    return <>
        <FoodRadarIconButton
            aria-label={"Show site QR Code"}
            onClick={onOpen}
        >
            <Map/>
        </FoodRadarIconButton>
        <Dialog
            open={open}
            onClose={onClose}
            fullWidth={true}
            maxWidth={"sm"}
            PaperProps={{
                sx: {
                    margin: 0,
                    width: "100%",
                }
            }}
        >
            <DialogContent>
                <MapContainer center={[46.934240, 7.418366]}
                              zoom={20} // max
                              scrollWheelZoom={true}
                              style={{
                                  "minWidth": isMdScreen() ? "400px" : "300px",
                                  "minHeight": isMdScreen() ? "400px" : "300px",
                              }}
                >
                    <TileLayer
                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    {props.placePosition && <Marker position={props.placePosition.toLatLng()}>
                        <Popup>{props.placeName}</Popup>
                    </Marker>}
                    {props.routeFromOffice && <Polyline
                        pathOptions={{color: 'black'}}
                        positions={props.routeFromOffice.toLatLangArray()}/>
                    }
                </MapContainer>
            </DialogContent>
        </Dialog>
    </>
}

