import {Box, Typography} from "@mui/material";
import React from "react";
import "./radar-spin.css"
import {RadarSpinnerIcon} from "../icons/Icons";

interface SpacerProps {}

export const Spacer = (props: SpacerProps) =>
    <Box sx={{height: "1rem"}} />

interface RadarSpinnerProps {
    // ToDo; Make it a component
    label?: string
}

export const RadarSpinner = (props: RadarSpinnerProps) =>
    <Box sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        padding: "1rem",
        justifyContent: "center"
    }}>
        {props.label && <Typography variant={"h5"}>{props.label}</Typography>}
        <Spacer/>
        <RadarSpinnerIcon sx={{
            animationName: "radar-spin",
            animationDuration: "0.8s",
            animationTimingFunction: "linear",
            animationDelay: "1s",
            animationIterationCount: "infinite",
        }}
        />
    </Box>