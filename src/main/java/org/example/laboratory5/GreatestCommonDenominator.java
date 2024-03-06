package org.example.laboratory5;

public class GreatestCommonDenominator {
    public int calculate(int firstValue, int secondValue) {
        var max = Math.max(firstValue, secondValue);
        var min = Math.min(firstValue, secondValue);
        var residue = -1;

        while(residue != 0) {
            residue = max % min;

            if(residue > 0) {
                max = min;
                min = residue;
            }
        }

        return min;
    }
}
