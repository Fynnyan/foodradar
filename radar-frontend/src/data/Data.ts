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
    id: string,
    name: string,
    web: string,
    location: string,
    days: string[]
}