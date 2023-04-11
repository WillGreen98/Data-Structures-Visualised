module com.dsav.datastructuresvisualised {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.dsav.datastructuresvisualised to javafx.fxml;
    exports com.dsav.datastructuresvisualised;
}