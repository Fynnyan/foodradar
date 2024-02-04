import {Box, keyframes, Typography} from "@mui/material";
import React from "react";
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
            animationName: `${radarSpin}`,
            animationDuration: "0.8s",
            animationTimingFunction: "linear",
            animationIterationCount: "infinite",
        }}
        />
    </Box>

const radarSpin = keyframes`
  0% {
    rotate: 0deg;
  }
  100% {
    rotate: 360deg;
  }
`