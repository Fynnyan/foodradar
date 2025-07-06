import {app, HttpRequest, HttpResponseInit, InvocationContext} from "@azure/functions";
import {FoodTruck} from "../components/Types";
import {getDay} from "date-fns";
import {getDayOfWeek} from "../components/Helpers";

app.http("food-trucks-today", {
    methods: ["GET"],
    authLevel: "anonymous",
    route: "food-trucks/today",
    handler: fetchFoodTrucksToday
})

export async function fetchFoodTrucksToday(request: HttpRequest, context: InvocationContext): Promise<HttpResponseInit> {

    const foodTrucks: FoodTruck[] = [
        {
            "name": "FireChefs",
            "web": "https://www.firechefs.ch/food-truck",
            "locations": []
        },
        {
            "name": "Gabriele",
            "web": "https://www.gabriele-streetfood.ch/",
            "locations": [
                {
                    "location": "Liebefeld Bahnhof",
                    "day": "MONDAY"
                },
                {
                    "location": "Liebefeld Bahnhof",
                    "day": "WEDNESDAY"
                }
            ]
        },
        {
            "name": "Mê - vietnamese cuisine",
            "web": "https://mevietnam.ch/",
            "locations": [
                {
                    "location": "Liebefeld Bahnhof",
                    "day": "TUESDAY"
                },
                {
                    "location": "Liebefeld Bahnhof",
                    "day": "THURSDAY"
                },
                {
                    "location": "Innenhof VIDMARplus, Liebefeld",
                    "day": "FRIDAY"
                },
            ]
        }
        ]

    const currentDay = getDayOfWeek(getDay(new Date()))
    const foodTruckDays = (truck: FoodTruck): string[] => truck.locations.map(value => value.day)

    return {
        status: 200,
        jsonBody: foodTrucks.filter(truck => foodTruckDays(truck).find(day => day === currentDay))
    }
}