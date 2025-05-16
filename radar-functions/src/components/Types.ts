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

export interface Course {
    name: string;
    price?: string;
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