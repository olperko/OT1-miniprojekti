package com.mokkikodit.cottagereservation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

    public class ReviewResultsController {

        @FXML
        private TextArea resultsArea;

        public void setResultsText(String results) {
            resultsArea.setText(results);
        }
    }


