/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	boxRegista.getItems().clear();
    	txtAttoriCondivisi.clear();
    	if (boxAnno.getValue()==null) {
    		txtResult.setText("Scegliere un anno di riferimento.");
    		return;
    	}
    	int anno = boxAnno.getValue();
    	
    	this.model.creaGrafo(anno);
    	txtResult.appendText("Grafo creato!\n");
    	txtResult.appendText("# Vertici: "+this.model.getNumVertici()+"\n");
    	txtResult.appendText("# Archi: "+this.model.getNumArchi()+"\n");
    	btnAdiacenti.setDisable(false);
    	btnCercaAffini.setDisable(false);
    	boxRegista.getItems().addAll(this.model.getRegisti());
    	
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	
    	txtResult.clear();
    	if (boxRegista.getValue()==null) {
    		txtResult.setText("Selezionare un regista");
    		return;
    	}
    	List<Director> risultato = this.model.getAdiacenti(boxRegista.getValue());
    	txtResult.appendText("Registi adiacenti a: "+boxRegista.getValue()+"\n");
    	for (Director d : risultato) {
    		txtResult.appendText(d.toString()+" - # attori condivisi: "+ (int) d.getPesoAdiacente()+"\n");
    	}
    	
    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	
    	txtResult.clear();
    	if (boxRegista.getValue()==null) {
    		txtResult.setText("Selezionare un regista");
    		return;
    	}
    	try {
    		int c = Integer.parseInt(txtAttoriCondivisi.getText());
    		
    		if (c<=0) {
    			txtResult.setText("Inserire un numero intero positivo!");
    			return;
    		}
    		
    		this.model.camminoMassimo(boxRegista.getValue(), c);
    		txtResult.appendText("Percorso migliore partendo da : "+boxRegista.getValue().toString()+"\n");
    		for (Director d : this.model.getPercorsoMigliore()) {
    			txtResult.appendText(d.toString()+"\n");
    		}
    		txtResult.appendText("Attori condivisi: "+this.model.getAttoriMassimi());
    		
    	} catch (NumberFormatException e) {
    		txtResult.setText("Errore: inserire un numero di attori condivisi intero.");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	this.boxAnno.getItems().add(2004);
    	this.boxAnno.getItems().add(2005);
    	this.boxAnno.getItems().add(2006);
    	
    }
    
}
