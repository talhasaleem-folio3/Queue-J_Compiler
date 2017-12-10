package controller;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class LexicalController implements Initializable {

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	@FXML
	private TextArea editorTextArea;

	@FXML
	private Button compileButton;

	@FXML
	private Label outputLabel;

	@FXML
	private void compileButtonClicked(ActionEvent event) {
		String input = editorTextArea.getText();
		writeIntoFile(input);
		try {
			LexicalAnalyzer la = new LexicalAnalyzer();
			outputLabel.setText(la.getCompileMessage());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeIntoFile(String text) {
		try {
			BufferedWriter input = new BufferedWriter(new FileWriter("file.txt"));
			input.write(text);
			input.close();
		} catch (IOException e) {
			System.out.println("File Not Found");
		}
	}

}
