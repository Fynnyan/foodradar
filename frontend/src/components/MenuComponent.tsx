import {Card, CardContent, Typography} from "@mui/material";
import {Course, Menu} from "./Data";

interface MenuComponentProps {
    menu: Menu
}

export const MenuComponent = (props: MenuComponentProps) => {
    return <Card>
        <CardContent>
            <Typography variant={"body1"}>{props.menu.date}</Typography>
            {props.menu.courses.map((value, index) => <CourseComponent key={index} course={value}/>)}
        </CardContent>
    </Card>
}

interface CourseComponentProps {
    course: Course
}

const CourseComponent = (props: CourseComponentProps) =>
    <>
        <Typography variant={"body1"}>{props.course.name}</Typography>
    </>