package org.example.travellingsalesmanservice.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import jdk.incubator.vector.IntVector;


import java.util.Arrays;

@Component
public class BootStrap implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println(System.getProperty("java.version"));
        System.out.println(Arrays.toString(args));
        int[] a = new int[100];
        Arrays.fill(a, -1);
        IntVector intVector = IntVector.fromArray(IntVector.SPECIES_PREFERRED, a, 0);
        IntVector res = intVector.add(intVector);
        int[] array = res.toArray();
        System.out.println(Arrays.toString(array));
        System.out.println(array.length);
    }
}
