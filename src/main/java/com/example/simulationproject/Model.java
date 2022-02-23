package com.example.simulationproject;

import java.util.*;

public class Model {
    private ArrayList<Integer> Q = new ArrayList<>();
    private ArrayList<Integer> A = new ArrayList<>();
    private ArrayList<Integer> M = new ArrayList<>();
    private ArrayList<Integer> nM = new ArrayList<>();

    public void init(ArrayList<Integer> Q, ArrayList<Integer> M){
        this.Q = Q;
        // add extra 0 bit
        this.M.add(0);
        this.M = M;

        for(int i=0; i<Q.size()+1; i++){
            this.A.add(0);
        }

        this.nM = twosCompliment(this.M);
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

    public void getAllPasses() {
        int n = Q.size();
        // iteration for each pass
        for(int i=0; i<n; i++) {

            this.leftShift();

            System.out.println("Pass number " +i+":\n");
            System.out.println("A: "+this.A+", Q: "+this.Q);
        }
    }

    public void leftShift(){

        ArrayList<Integer> ACopy = new ArrayList<>(A);

        // Process A
        for(int i=0; i<A.size(); i++){
            //check for last bit
            if (i == A.size()-1){
                // get first bit from Q to put in last bit of A
                A.set(i, Q.get(0));
            }

            else {
                //set the next bit to current bit value
                A.set(i, M.get(i+1));
            }
        }

        // Process Q
        for(int i=0; i<Q.size()+1; i++){
            // check for last bit
            if (i == Q.size()-1){
                Q.remove(i);
            }

            else {
                //set the next bit to current bit value
                Q.set(i, Q.get(i+1));
            }
        }

        ArrayList<Integer> temp = this.addBits(A, this.nM);

        if (temp.get(0) == 1){
            // if 1, restore so return copy of A
            // also set last bit of Q to 0
            Q.add(0);
            this.A = ACopy;
        }

        else {
            // if 0, return new added binary number
            // also set bit of Q to 1
            Q.add(1);
            this.A = temp;
        }
    }

    public ArrayList<Integer> addBits(ArrayList<Integer> A, ArrayList<Integer> B) {

        int carry = 0;
        ArrayList<Integer> temp = new ArrayList<>();

        for (int i=M.size()-1; i>=0; i--){

            if(A.get(i) == 1){ //A[i] == 1

                if (B.get(i) == 1){
                    // 1+1
                    carry+=2;
                }
                else {
                    //1+0
                    carry+=1;
                }

            }
            else { //A[i] == 0

                if (B.get(i) == 1){
                    // 0+1
                    carry+=1;
                }
                //ignore if 0+0
            }

            // modulo 2 to set 1 or 0
            temp.add(carry%2);

            // integer division to get the carry
            if (carry > 1){
                carry = 1;
            }
        }
        return temp;
    }
}
