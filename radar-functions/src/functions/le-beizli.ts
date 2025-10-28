import {app, HttpRequest, HttpResponseInit, InvocationContext} from "@azure/functions";
import {PDFParse} from 'pdf-parse';
import {formatISO, getDay, parse} from "date-fns";
import {load} from "cheerio";
import {getDayOfWeek} from "../components/Helpers";
import {Course, Menu, Place, ProcessingStatus} from "../components/Types";


app.http("le-beizli-menu-v2", {
    methods: ["GET"],
    authLevel: "anonymous",
    route: "place/le-beizli",
    handler: fetchBeizlV2Menu
})

export async function fetchBeizlV2Menu(request: HttpRequest, context: InvocationContext): Promise<HttpResponseInit> {

    const baseUrl = "http://www.lebeizli.ch"
    const serviceName = "Le-Beizli"
    const serviceUrl = baseUrl + "/flavours"
    let body: Place

    const currentDay = getDayOfWeek(getDay(new Date()))
    const openingDays = ["TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"]

    try {
        const menus: Menu[] = !openingDays.find((d) => d === currentDay)
            ? []
            : await fetch(serviceUrl)
                .then(response => response.text())
                .then(async html => {
                    const $ = load(html);
                    const menus = []
                    const linkToPdf = baseUrl + $('a:icontains("mittagsmenu")').attr('href')
                    const parser = new PDFParse({url: linkToPdf});
                    // Parse PDF
                    return parser.getText();
                })
                .then(textResult => {

                    const text = textResult.text.replaceAll("\n", " ")
                    const courses: Course[] = [];
                    itemsToMarch.forEach(itemRegex => {
                        const match = text.match(itemRegex);
                        if (match) {
                            courses.push({name: cleanMenuText(match[0])});
                        }
                    });

                    // the current card doesn't contain the date but that might change, keep the old lookup for it.
                    const textDate = text.match(dateRegex)

                    return [{
                        date: textDate
                            ? formatIsoDate(parse(textDate[0], 'dd.MM.yyyy', new Date()))
                            : formatIsoDate(new Date()),
                        courses: courses
                    }]
                })
        body = {
            name: serviceName,
            web: serviceUrl,
            menus: menus,
            processingStatus: ProcessingStatus.PROCESSED
        }
    } catch (error) {
        context.error("Could not process / parse the schichtwechsel menu, error:", error.message)
        body = {
            processingStatus: ProcessingStatus.PROCESS_ERROR,
            name: serviceName,
            web: serviceUrl,
            menus: []
        }
    }
    return {
        status: 200,
        jsonBody: body
    }
}

function formatIsoDate(date) {
    return formatISO(date, {representation: 'date'})
}

function buildMenuRegex(id, startToken, endTokens) {
    return new RegExp(`(?<${id}>${startToken}(?:(?!(${endTokens.join("|")}))(.|\\s))*)`, "ig")
}

function cleanMenuText(text) {
    return text.replace(priceRegex, "").trim()
}

// Tokens, beginning of the titles of the different daily menu items
const saladeToken = "salade champ"
const quicheToken = "quiche"
const pastaToken = "pasta"
const vegiToken = "garten"
const meatToken = "fleischer"
const dopaminToken = "dopaminration"
const suessesSection = "SÜSSES"
const secondPageTitle = "mittagstisch"
// special summer menu tokens
const tellerSection = "teller"
const sauvageToken = "la sauvage"
const jardiniereToken = "la jardinière"
const fraichetteToken = "la fraîchette"

const itemsToMarch = [
    // items form the summer card
    buildMenuRegex("sauvage", sauvageToken, [jardiniereToken, fraichetteToken, suessesSection, tellerSection, secondPageTitle]),
    buildMenuRegex("jardiniere", jardiniereToken, [sauvageToken, fraichetteToken, suessesSection, tellerSection, secondPageTitle]),
    buildMenuRegex("fraichette", fraichetteToken, [sauvageToken, jardiniereToken, suessesSection, tellerSection, secondPageTitle]),
    buildMenuRegex("salade", saladeToken, [quicheToken, meatToken, pastaToken, vegiToken, dopaminToken, suessesSection, secondPageTitle]),
    buildMenuRegex("quiche", quicheToken, [meatToken, pastaToken, vegiToken, dopaminToken, suessesSection, secondPageTitle]),
    buildMenuRegex("pasta", pastaToken, [meatToken, quicheToken, vegiToken, dopaminToken, suessesSection, secondPageTitle]),
    buildMenuRegex("dopamin", dopaminToken, [meatToken, pastaToken, vegiToken, quicheToken, suessesSection, secondPageTitle]),
    buildMenuRegex("fleisch", meatToken, [dopaminToken, pastaToken, vegiToken, quicheToken, suessesSection, secondPageTitle]),
    buildMenuRegex("garten", vegiToken, [meatToken, quicheToken, pastaToken, dopaminToken, suessesSection, secondPageTitle]),
]

const priceRegex = new RegExp("(\\+[\\d.]|\\d+[\\s.|\\d]*)")
const dateRegex = new RegExp("\\d{1,2}\\.\\d{1,2}\\.\\d{2,4}", "ig")