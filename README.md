# Starship Fleet Management (Spring Boot practice project)
Run by executing the maven goal `mvn spring-boot:run`

Accepts requests from localhost only - configured via the `@CrossOrigin` annotation in the ShipsController class.

## API
| Methods | URLs                       | Actions                                                |
|---------|----------------------------|--------------------------------------------------------|
| POST    | /api/ships                 | Create new ship                                        |
| GET     | /api/ships                 | Retrieve all ships                                     |
| GET     | /api/ships/:id             | Retrieve a ship by `:id`                               |
| PUT     | /api/ships/:id             | Update a ship by `:id`                                 |
| DELETE  | /api/ships/:id             | Delete a ship by `:id`                                 |
| DELETE  | /api/ships/                | Delete all ships                                       |
| GET     | /api/ships/flown           | Retrieve all ships that have flown                     |
| GET     | /api/ships?model=[keyword] | Retrieve all ships with a model containing `[keyword]` |

### Example calls
* GET `http://localhost:8080/api/ships?model=V1`
* GET `http://localhost:8080/api/ships/flown`

## Dummy data
Future easy reference data, as persistence isn't currently configured in this microservice.

`"SN8, launched 09/12/2021. Launch, ascent, reorientation, and controlled descent were successful, but low pressure in the methane header tank kept the engines from producing enough thrust for the landing burn, destroying SN8 on impact."`

`"SN9, launched 02/02/2022. Ascent, engine cutoffs, reorientation and controlled descent were stable, but one engine's oxygen pre-burner failed, sending SN9 crashing into the landing pad."`

`"SN10, launched 03/03/2022. Landed without exploding. The test ended with a hard landing-at 10 m/s – most likely due to partial helium ingestion from the fuel header tank. Three landing legs were not locked in place, producing a slight lean after landing. Although the vehicle initially remained intact, the impact crushed the legs and part of the leg skirt. Eight minutes later the prototype exploded"`

`"SN12, scrapped before completion"`

`"SN13, scrapped before completion"`

`"SN14, scrapped before completion"`

Referenced tutorial: https://www.bezkoder.com/spring-boot-3-rest-api/
