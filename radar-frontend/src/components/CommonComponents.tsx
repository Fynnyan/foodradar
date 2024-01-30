import {Box, SvgIcon, Typography} from "@mui/material";
import React from "react";
import "./radar-spin.css"
import {ReactComponent as RadarSpinnerIcon} from '../icons/radar-spinner.svg';

interface SpacerProps {}

export const Spacer = (props: SpacerProps) =>
    <Box sx={{height: "1rem"}} />

interface RadarSpinnerProps {
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
        {props.label && <Typography variant={"h5"}>{props.label}</Typography> }
        <Spacer/>
        <SvgIcon viewBox={"0 0 36 36"} sx={
            {
                animationName: "radar-spin",
                animationDuration: "0.8s",
                animationTimingFunction: "linear",
                animationDelay: "1s",
                animationIterationCount: "infinite",
            }}>
            <RadarSpinnerIcon/>
        </SvgIcon>
    </Box>
