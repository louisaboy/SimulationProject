package com.example.simulationproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.*;

public class MainController {


    private Model model = new Model();

    @FXML
    private TextField tfM;
    @FXML
    private TextField tfQ;
    @FXML
    private TextField tfOutputA;
    @FXML
    private TextField tfOutputQ;
    @FXML
    private Button btnCalculate;
    @FXML
    private Label txError;  // to display for any type error
    @FXML
    private Label txSteps;  // to display the steps
    @FXML
    private TextField tfSteps;
    private String bin_m, bin_q;
    @FXML
    private VBox myView = new VBox();
    @FXML
    private BorderPane bp;
    private ArrayList<Integer> Q = new ArrayList<>();
    private ArrayList<Integer> A = new ArrayList<>();
    private ArrayList<Integer> M = new ArrayList<>();
    private ArrayList<Integer> nM = new ArrayList<>();
    private ArrayList<Integer> q_ans;
    private ArrayList<Integer> a_ans;

    // onclick listener for restoring Division
    public void submit(ActionEvent actionEvent) {
        boolean isBinary = true;
        this.Q.removeAll(Q);
        this.A.removeAll(A);
        this.M.removeAll(M);
        this.nM.removeAll(nM);
        try {
            bin_m = tfM.getText();
            bin_q = tfQ.getText();

        // For cases of wrong data type input
        } catch (NumberFormatException e){
            txError.setText("Enter a binary number");
        } catch (Exception e) {
            txError.setText("Error");
        }
        for(int i=0; i< bin_m.length(); i++){

            if(bin_m.charAt(i) > 'a' && bin_m.charAt(i) < 'z' || bin_m.charAt(i) > 'A'&& bin_m.charAt(i) > 'Z' || bin_m.charAt(i) < '0' || bin_m.charAt(i) > '1')
                isBinary=false;
            else
            {
                A.add(Integer.parseInt(String.valueOf(bin_m.charAt(i))));
            }

        }
        for(int i=0; i< bin_q.length(); i++){

            if(bin_q.charAt(i) > 'a' && bin_q.charAt(i) < 'z' || bin_q.charAt(i) > 'A'&& bin_q.charAt(i) > 'Z' || bin_q.charAt(i) < '0' || bin_q.charAt(i) > '1')
                isBinary=false;
            else {
                Q.add(Integer.parseInt(String.valueOf(bin_q.charAt(i))));
            }
        }

        if (isBinary) {
            Simulation simulation = new Simulation (Q, A);
            txSteps.setText(simulation.getAllPasses().toString());
            tfOutputA.setText(String.valueOf(simulation.A));
            tfOutputQ.setText(String.valueOf(simulation.Q));
            txError.setText("");
        }
        else
        {
            txError.setText("Enter a binary number");
        }

    }
//        tfOutput.setText(String.valueOf(binary));
        public static class Simulation {
            private ArrayList<Integer> Q = new ArrayList<>();
            private ArrayList<Integer> A = new ArrayList<>();
            private ArrayList<Integer> M = new ArrayList<>();
            private ArrayList<Integer> nM = new ArrayList<>();
            private StringBuilder sb = new StringBuilder();
    private Object q_ans;

    public Simulation(ArrayList<Integer> Q, ArrayList<Integer> M){
                this.Q.removeAll(Q);
                this.A.removeAll(A);
                this.M.removeAll(M);
                this.nM.removeAll(nM);
                this.Q = Q;
                // add extra 0 bit
                this.M.add(0);
                this.sb.setLength(0);
                for(int i=0; i<Q.size(); i++){
                    this.M.add(M.get(i));
                }

                for(int i=0; i<this.M.size(); i++){
                    this.A.add(0);
                }

                this.nM = twosCompliment(this.M);
            }

            public StringBuilder getAllPasses() {
                int n = this.Q.size();
                // iteration for each pass
                for(int i=0; i<n; i++) {
                    sb.append("Pass " +(i+1)+":"+"\n");
                    this.leftShift(this.A, this.Q, sb);
                    sb.append("A: "+this.A+", Q: "+this.Q +"\n\n");
                }

                return sb;
            }


            public ArrayList<Integer> twosCompliment(ArrayList<Integer> M){

                boolean first1=false;

                for (int i=M.size()-1; i>=0; i--) {

                    if (M.get(i) == 1){
                        // check if it's the 1st 1 bit is already found
                        if (!first1){
                            first1=true;
                        } else {
                            M.set(i, 0);
                        }
                    }

                    else{
                        // check if it's the 1st 1 bit is already found
                        if (!first1){
                            continue;
                        } else {
                            M.set(i, 1);
                        }
                    }
                }
                return M;
            }


            public void leftShift(ArrayList<Integer> A, ArrayList<Integer> Q, StringBuilder sb){
                ArrayList<Integer> QCopy = new ArrayList<>(Q);
                ArrayList<Integer> ACopy = new ArrayList<>(A);

                // Process A
                for(int i=0; i<ACopy.size(); i++){
                    //check for last bit
                    if (i == ACopy.size()-1){
                        // get first bit from Q to put in last bit of A
                        ACopy.set(i, QCopy.get(0));
                    }

                    else {
                        //set the next bit to current bit value
                        ACopy.set(i, ACopy.get(i+1));
                    }
                }

                // Process Q
                for(int i=0; i<QCopy.size()+1; i++){
                    // check for last bit
                    if (i == QCopy.size()-1){
                        QCopy.remove(i);
                    }

                    else {
                        //set the next bit to current bit value
                        QCopy.set(i, QCopy.get(i+1));
                    }
                }
                sb.append("A: "+ACopy+" Q: "+QCopy +"\n");
                ArrayList<Integer> temp = this.addBits(ACopy, this.nM);
                sb.append("A: "+temp+"\n");
                if (temp.get(0) == 1){
                    // if 1, restore so return copy of A
                    // also set last bit of Q to 0
                    QCopy.add(0);
                    this.Q = QCopy;
                    this.A = ACopy;
                }

                else {
                    // if 0, return new added binary number
                    // also set bit of Q to 1
                    QCopy.add(1);
                    this.Q = QCopy;
                    this.A = temp;
                }
            }

            public ArrayList<Integer> addBits(ArrayList<Integer> A, ArrayList<Integer> B) {

                int carry = 0;
                ArrayList<Integer> temp = new ArrayList<Integer>();
                for (int i=0; i<A.size(); i++){
                    temp.add(0);
                }

                for (int i=A.size()-1; i>=0; i--){

                    carry += (A.get(i) + B.get(i));
                    // modulo 2 to set 1 or 0
                    temp.set(i,carry%2);

                    // integer division to get the carry
                    if (carry > 1){
                        carry = 1;
                    } else {
                        carry = 0;
                    }
                }
                return temp;
            }
        }
}
