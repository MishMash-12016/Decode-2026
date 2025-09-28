package org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils;

import java.util.List;

public class MMathTools {
    public static double distance(List<Double> p1, List<Double> p2){
        return Math.sqrt((p1.get(0) - p2.get(0)) * (p1.get(0) - p2.get(0)) + (p1.get(1) - p2.get(1)) * (p1.get(1) - p2.get(1)));
    }

    public static double distance(double x, double y){
        return Math.sqrt(x* x + y * y);
    }
}
