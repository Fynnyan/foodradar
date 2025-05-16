import {LatLng, LatLngExpression} from "leaflet";

export interface Place {
    name: string;
    web: string;
    menus: Menu[]
    processingStatus: ProcessingStatus
}

export enum ProcessingStatus {
    SITE_NOT_ACCESSIBLE = "SITE_NOT_ACCESSIBLE",
    PROCESS_ERROR = "PROCESS_ERROR",
    PROCESSED = "PROCESSED"

}

export interface Menu {
    date: string;
    courses: Course[],
}

export function getMenuText(place: String, menu: Menu) {
    const courses = menu.courses.map((value) => `- ${value.name}`).join("\n")
    return `${place} - ${menu.date}\n${courses}`
}

export interface Course {
    name: string;
    price: string;
}

export interface FoodTruck {
    name: string,
    web: string,
    locations: FoodTruckLocation[]
}

export interface FoodTruckLocation {
    location: string,
    day: string
}

export class Position {
    constructor(position: { latitude: number, longitude: number }) {
        this.latitude = position.latitude
        this.longitude = position.longitude
    }

    // north south
    readonly latitude: number
    // west east
    readonly longitude: number

    toLatLng(): LatLng {
        return new LatLng(this.latitude, this.longitude, undefined)
    }
}

export class Route {
    constructor(route: number[][]) {
        this.route = route.map(it => new Position({latitude: it[0], longitude: it[1]}))
    }

    readonly route: Array<Position>

    toLatLangArray(): LatLngExpression[] {
        return this.route.map(it => it.toLatLng())
    }
}