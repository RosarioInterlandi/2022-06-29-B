/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.VerticeBilanciato;
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

    @FXML // fx:id="btnAdiacenze"
    private Button btnAdiacenze; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA2"
    private ComboBox<Album> cmbA2; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doCalcolaAdiacenze(ActionEvent event) {
    	Album root = this.cmbA1.getValue();
    	if (root == null) {
    		txtResult.appendText("Scegli un Album");
    	}
    	txtResult.clear();
    	List<VerticeBilanciato> successori = this.model.stampaAdiacenze(root);
    	if (successori.size()!= 0) {
    		this.btnPercorso.setDisable(false);
    		for( VerticeBilanciato v: successori) {
    			txtResult.appendText(v.getVertice().getTitle()+",bilancio="+v.getPeso()+"\n");
    		}
    		return;
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	int pesoMinimo = 0;
    	Album target =cmbA2.getValue();
    	//Per evitare che si possa scegliere un root diverso da quello usato
    	// per creare il grafo non lo prendo dall'interfaccia ma userò direttamente
    	// quello del modello
    	try {
    		pesoMinimo = Integer.parseInt(txtX.getText());
    		 	
    	}catch(Exception e1){
    		txtResult.setText("Inserisci correttamente i dati (il root deve essere lo stesso del punto 1)");
    		return;
    	}
    	if (target== null) {
    		txtResult.setText("Scegli un album a2");
    		return;
    	}
    	List<Album> path = this.model.getPath(target, pesoMinimo);
    	if (path.size()==0) {
    		txtResult.setText("Non esiste un cammino che colleghi i due album");
    	}else {
    		txtResult.appendText("Cammino trova:\n");
    		for (Album a: path) {
    			txtResult.appendText(a.getTitle()+"\n");
    		}
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Integer n;
    	try {
    		n = Integer.parseInt(this.txtN.getText());
    	}catch(Exception e) {
    		this.txtResult.setText("Inserisci un numero");
    		return;
    	}
    	this.model.BuildGraph(n);
    	if (this.model.getVertici().size()!= 0) {
    		txtResult.setText("Grafo creato con successo, \n "+this.model.getVertici().size()+"---"+this.model.getArchi().size());
    	}
    	this.btnAdiacenze.setDisable(false);
    	this.cmbA1.getItems().clear();
    	this.cmbA1.getItems().addAll(this.model.getVertici());
    	this.cmbA2.getItems().clear();
    	this.cmbA2.getItems().addAll(this.model.getVertici());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenze != null : "fx:id=\"btnAdiacenze\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA2 != null : "fx:id=\"cmbA2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    
    public void setModel(Model model) {
    	this.model = model;
    	this.btnPercorso.setDisable(true);
    	this.btnAdiacenze.setDisable(true);
    }
}
