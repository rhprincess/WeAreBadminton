package data

enum class Round(val round: String) {
    // Men's single
    MSR32("MS · R32"),
    MSR16("MS · R16"),
    MSQF("MS · QF"),
    MSSF("MS · SF"),
    MSF("MS · FINAL"),

    // Women's single
    WSR32("WS · R32"),
    WSR16("WS · R16"),
    WSQF("WS · QF"),
    WSSF("WS · SF"),
    WSF("WS · FINAL"),

    // Man's Doubles
    MDR32("MD · R32"),
    MDR16("MD · R16"),
    MDQF("MD · QF"),
    MDSF("MD · SF"),
    MDF("MD · FINAL"),

    // Women's Doubles
    WDR32("WD · R32"),
    WDR16("WD · R16"),
    WDQF("WD · QF"),
    WDSF("WD · SF"),
    WDF("WD · FINAL"),

    // Mixed Doubles
    XDR32("XD · R32"),
    XDR16("XD · R16"),
    XDQF("XD · QF"),
    XDSF("XD · SF"),
    XDF("XD · FINAL")
}