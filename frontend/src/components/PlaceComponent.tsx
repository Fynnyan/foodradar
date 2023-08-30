import {Card, CardHeader, Link} from "@mui/material";
import {Place} from "./Data";
import {MenuComponent} from "./MenuComponent";

interface PlaceComponentProps {
    place: Place
}

export const PlaceComponent = (props: PlaceComponentProps) => {
    return <Card>
        <CardHeader
            title={props.place.name}
            action={<Link href={props.place.web}>To Site</Link>}
        />
        {props.place.menus.map((value, index) => <MenuComponent key={index} menu={value}/>)}
    </Card>
}