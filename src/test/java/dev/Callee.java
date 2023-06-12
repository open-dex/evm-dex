package dev;

import conflux.web3j.types.Address;

public class Callee {
    public static void test(String[] args) {
        Address address = new Address("0x423e897749c3b6f9e6e80ef8e60718ce670672d2", 1);
        System.out.println("it works in callee, " + address);
    }
}
