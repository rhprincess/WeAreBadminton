package data.payload

data class YearMatchCalendarPayload(
    val smallBackground: Boolean = true,
    val yearKey: String = "2024"
) : Payload