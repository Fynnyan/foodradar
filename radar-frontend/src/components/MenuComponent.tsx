import {Box, Button, Card, CardContent, IconButton, Typography} from "@mui/material";
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import {Course, Menu} from "./Data";
import React from "react";

interface MenuComponentProps {
    placeName: string
    menu: Menu
}

function getMenuText(place: String, menu: Menu) {
    const courses = menu.courses.map((value) => `- ${value.name}`).join("\n")
    return `${place} - ${menu.date}\n${courses}`
}


export const MenuComponent = (props: MenuComponentProps) => {
    return (
        <CardContent sx={{display: "flex", flexDirection: "column", gap: "1rem"}}>
            <Box sx={{display: "fley", flexDirection: "row"}}>
                <Typography variant={"body1"}>{props.menu.date}</Typography>
                <CopyMenuButton text={getMenuText(props.placeName, props.menu)}/>
            </Box>
            {props.menu.courses.map((value, index) => <CourseComponent key={index} course={value}/>)}
        </CardContent>
    )
}

interface CourseComponentProps {
    course: Course
}

const CourseComponent = (props: CourseComponentProps) =>
    <>
        <Typography sx={{'&::before': {content: '"➢ "'}}} variant={"body1"}>{props.course.name}</Typography>
    </>
interface CopyMenuButtonProps {
    text: string
}

const CopyMenuButton = (props: CopyMenuButtonProps) =>
    <IconButton
        aria-label="copy menu to clipboard"
        size={"small"}
        onClick={ () =>  navigator.clipboard.writeText(props.text) }>
        <ContentCopyIcon fontSize="inherit" />
    </IconButton>