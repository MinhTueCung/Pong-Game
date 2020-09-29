module org.openjfx.ea_aufgabe_2_pong {
    requires javafx.controls;
    exports org.openjfx.ea_aufgabe_2_pong;
    opens Model to javafx.base;
}