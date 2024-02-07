import {Box, keyframes} from "@mui/material";
import React from "react";
import {RadarSpinnerIcon} from "../../icons/Icons";
import {Spacer} from "./Spacer";


interface RadarSpinnerProps {
    label?: React.ReactNode
}

export const RadarSpinner = (props: RadarSpinnerProps) =>
    <Box sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        padding: "1rem",
        justifyContent: "center"
    }}>
        {props.label && <>{props.label}<Spacer/></>}
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
