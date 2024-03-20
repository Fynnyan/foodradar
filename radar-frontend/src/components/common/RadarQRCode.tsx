import {Box, Dialog, DialogContent} from "@mui/material";
import {QrCode} from "@mui/icons-material";
import React, {useState} from "react";
import {QRCodeSVG} from "qrcode.react";
import {FoodRadarIconButton} from "./FoodRadarIconButton";
import {FoodRadarIcon} from "../../icons/Icons";

export const RadarQRCode = () => {

    const [open, setOpen] = useState(false)

    const onClose = () => {
        setOpen(false)
    }

    return <>
        <FoodRadarIconButton
            aria-label={"Show site QR Code"}
            onClick={() => setOpen(true)}
        >
            <QrCode/>
        </FoodRadarIconButton>
        <Dialog
            open={open}
            onClose={onClose}
        >
            <DialogContent>
                <Box
                    sx={{display: "grid"}}
                >
                    <QRCodeSVG
                        value={window.location.href}
                        level={"H"}
                        includeMargin={true}
                        size={256}
                        style={{gridArea: "1/1"}}
                    />
                    <FoodRadarIcon
                        sx={{
                            width: "48px",
                            height: "48px",
                            color: "#000000",
                            backgroundColor: "#ffffff",
                            gridArea: "1/1",
                            position: "relative",
                            top: "104px",
                            left: "104px",
                            borderRadius: "15%",
                            borderColor: "#fbf2f2",
                            borderStyle: "solid",
                            borderWidth: "0.1px"

                        }}
                    />
                </Box>
            </DialogContent>
        </Dialog>
    </>
}