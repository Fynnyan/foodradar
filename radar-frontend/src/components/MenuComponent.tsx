import {Typography} from "@mui/material";
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import {Course, Menu} from "../data/Data";
import React from "react";
import CardContent from '@mui/material/CardContent';
import {FoodRadarIconButton} from "./common/FoodRadarIconButton";

interface MenuComponentProps {
    menu: Menu
}

export const MenuComponent = (props: MenuComponentProps) => {
    return (
        <CardContent>
            <Typography variant={"body1"}>{props.menu.date}</Typography>
            <ul style={{
                listStyleType: '"➢ "',
                listStylePosition: "outside",
                margin: 0,
                paddingLeft: "20px"
            }}>
                {props.menu.courses.map((value, index) => <CourseComponent key={index} course={value}/>)}
            </ul>
        </CardContent>
    )
}

interface CourseComponentProps {
    course: Course
}

const CourseComponent = (props: CourseComponentProps) =>
    <Typography component={"li"} variant={"body1"}>{props.course.name}</Typography>

interface CopyMenuButtonProps {
    text: string
}

export const CopyMenuButton = (props: CopyMenuButtonProps) => {
    return <FoodRadarIconButton
        aria-label="Copy the menu to the clipboard for sharing."
        onClick={() => navigator.clipboard.writeText(props.text)}>
        <ContentCopyIcon/>
    </FoodRadarIconButton>
}
