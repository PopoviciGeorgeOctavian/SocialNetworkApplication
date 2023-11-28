package com.socialnetwork.lab78.ui;

import com.socialnetwork.lab78.domain.FriendShip;
import com.socialnetwork.lab78.domain.User;
import com.socialnetwork.lab78.service.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UI implements UserInterface{

    private Service service;
    // private myObj = new Scanner(System.in);
    //private Scanner myObj;
    public UI(Service service) {
        this.service = service;
    }
    public void printMenu(){
        System.out.println("Meniu:\n0.Exit\n1.Adauga User\n2.Sterge User\n3.Adauga FriendShip\n4.Sterge FriendShip\n5.Afisare utiliatori\n6.Afisare prietenii\n7.Numar de comunitati\n8.Comunitatea cea mai sociabila\n9.Generare Useri, Prietenii\n10.Lista de useri cu cel putin N prieteni\n11.Prietenii User in luna specificata\n12.User ce contine input in firstName\n");
    }
    public void addUser(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("\nNume: ");
        String nume=myObj.nextLine();
        System.out.println("\nPreume: ");
        String prenume=myObj.nextLine();
        service.addUser(nume,prenume);

    }
    public void deleteUser(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("\nNume: ");
        String nume=myObj.nextLine();
        System.out.println("\nPreume: ");
        String prenume=myObj.nextLine();
        service.deleteUser(nume,prenume);
    }
    public void printUsers(){
        Iterable<User> users = service.getAllUseri();
        for (User user : users) {
            System.out.println(user.toString());
        }
    }
    public void printFriendships(){
        Iterable<FriendShip> FriendShip = service.printFriendships();
        for (FriendShip prt : FriendShip) {
            System.out.println(prt.toString());
        }
    }
    public void addFriendShip(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("\nPrieten 1");
        System.out.println("\nNume: ");
        String nume1=myObj.nextLine();
        System.out.println("\nPreume: ");
        String prenume1=myObj.nextLine();
        System.out.println("\nPrieten 2");
        System.out.println("\nNume: ");
        String nume2=myObj.nextLine();
        System.out.println("\nPreume: ");
        String prenume2=myObj.nextLine();
        service.addFriendShip(nume1,prenume1,nume2,prenume2);
    }
    public void removeFriendShip(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("\nPrieten 1");
        System.out.println("\nNume: ");
        String nume1=myObj.nextLine();
        System.out.println("\nPreume: ");
        String prenume1=myObj.nextLine();
        System.out.println("\nPrieten 2");
        System.out.println("\nNume: ");
        String nume2=myObj.nextLine();
        System.out.println("\nPreume: ");
        String prenume2=myObj.nextLine();
        service.removeFriendShip(nume1,prenume1,nume2,prenume2);
    }
    public void numarComunitati(){
        System.out.println(service.numberOfCommunities() + "\n");
    }
    public void comunitateaSociabila(){
        System.out.println("Cea mai sociabila comunitate: "+service.mostSociableCommunity());
    }
    public void generare(){
        service.addEntities();
    }
    public void numarMinimPrieteni() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Numarul N:");
        int n = myObj.nextInt();
        List<User> lista = service.numarMinimPrieteni(n);

        for (User user : lista) {
            System.out.println(user.toString() + " " + service.numarPrieteni(user) + " friends");
        }
    }



    public void FriendsFromfunction(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("\nNume: ");
        String nume=myObj.nextLine();
        System.out.println("\nPreume: ");
        String prenume=myObj.nextLine();
        System.out.println("\nLuna: ");
        int luna= Integer.parseInt(myObj.nextLine());
        ArrayList<User> list= (ArrayList<User>) service.FriendsFromFunction(nume,prenume,luna);
        for(User i:list){
            System.out.println(i);
        }
    }
    public void CautaDupaString(){
        Scanner myObj = new Scanner(System.in);
        System.out.println("\nString: ");
        String nume=myObj.nextLine();
        ArrayList<User> list= (ArrayList<User>) service.stringSearch(nume);
        for(User i:list){
            System.out.println(i);
        }
    }
    public void run(){
        Scanner myObj = new Scanner(System.in);
        while(true){
            printMenu();
            int cmd = myObj.nextInt();
            if(cmd==0){
                break;
            }
            if(cmd==1){
                addUser();
            }
            if(cmd==2){
                deleteUser();
            }
            if(cmd==3){
                addFriendShip();
            }
            if(cmd==4){
                removeFriendShip();
            }
            if(cmd==5){
                printUsers();
            }
            if(cmd==6){
                printFriendships();
            }
            if(cmd==7){
                numarComunitati();
            }
            if(cmd==8){
                comunitateaSociabila();
            }
            if(cmd==9){
                generare();
            }
            if(cmd==10){
                numarMinimPrieteni();
            }
            if(cmd==11){
                FriendsFromfunction();
            }
            if(cmd==12){
                CautaDupaString();
            }
        }
    }
}
