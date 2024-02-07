import {IconButton, useTheme} from "@mui/material";
import React from "react";
import {IconButtonProps} from "@mui/material/IconButton/IconButton";
import {alpha} from "@mui/system";

export const FoodRadarIconButton = (props: IconButtonProps) => {
    const theme = useTheme()
    return <IconButton
        sx={{backgroundColor: alpha(theme.palette.action.active, 0.15)}}
        {...props}
    >
        {props.children}
    </IconButton>
}