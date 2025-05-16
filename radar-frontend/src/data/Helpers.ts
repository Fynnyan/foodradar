
export function getDayOfWeek(day: number): string {
    // follow the date-fs week days, a week starts with sunday, number 0
    switch (day) {
        case 0:
            return "SUNDAY"
        case 1:
            return "MONDAY"
        case 2:
            return "TUESDAY"
        case 3:
            return "WEDNESDAY"
        case 4:
            return "THURSDAY"
        case 5:
            return "FRIDAY"
        default:
            return "SATURDAY"
    }
}