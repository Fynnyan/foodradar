import {Link, useTheme} from "@mui/material";
import {alpha} from "@mui/system";
import React from "react";
import {LinkProps} from "@mui/material/Link/Link";

export const IconLink = (props: LinkProps) => {
    const theme = useTheme()
    return <Link
        rel="noreferrer"
        target="_blank"
        {...props}
        sx={{
            // coped the styling props form the MUI Icon Button
            width: "41.5px",
            height: "41.5px",
            textAlign: 'center',
            flex: '0 0 auto',
            padding: "8px",
            borderRadius: '50%',
            backgroundColor: alpha(theme.palette.action.active, 0.15),
            overflow: 'visible',
            color: theme.palette.action.active,
            transition: theme.transitions.create('background-color', {
                duration: theme.transitions.duration.shortest
            }),
            '&:hover': {
                backgroundColor: alpha(theme.palette.action.active, theme.palette.action.hoverOpacity),
                // Reset on touch devices, it doesn't add specificity
                '@media (hover: none)': {
                    backgroundColor: 'transparent'
                }
            },
            ...props.sx
        }}
        >{props.children}</Link>
}