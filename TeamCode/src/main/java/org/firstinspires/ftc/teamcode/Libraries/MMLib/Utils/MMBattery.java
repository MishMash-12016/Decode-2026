package org.firstinspires.ftc.teamcode.Libraries.MMLib.Utils;

import Ori.Coval.Logging.AutoLog;
import Ori.Coval.Logging.AutoLogOutput;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

/**
 * this class represents ur robot battery
 */
@AutoLog
public class MMBattery {

    private final VoltageSensor battery;

    double dt = 0.02; // 100Hz control loop
    double timeConstant = 0.05; // smooth but quick
    LinearFilter voltageFilter = LinearFilter.singlePoleIIR(timeConstant, dt);

    private double filteredVoltage = 12;

    public MMBattery(HardwareMap hardwareMap) {
        this.battery = hardwareMap.voltageSensor.iterator().next();
    }

    public void update(){
        filteredVoltage = voltageFilter.calculate(battery.getVoltage());
    }

    /**
     * @return the battery voltage.
     */
    @AutoLogOutput
    public double getFilteredVoltage() {
        update();
        return filteredVoltage;
    }

    /**
     * use get filtered voltage instead of this
     * @return the raw values from the voltage sensor
     */
    public double getRawVoltage(){
        return battery.getVoltage();
    }

    /**
     * the ftc 12V batteries range between 11V as the lowest, and 15V (usually 14.5V is more common) are the highest.
     * @return how suitable the battery is for competition.
     * <p>
     * (100% - very suitable, 0% - DO NOT USE)
     */
    public double getPercentage() {
        return getPercentage(11, 14.5);
    }

    /**
     * this method can be used if u want to change the highest and lowest.
     * @param lowest can NOT go to comp with this
     * @param highest great for comp
     * @return how suitable the battery is for competition.
     * <p>
     * (100% - very suitable, 0% - DO NOT USE)
     */
    public double getPercentage(double lowest, double highest) {
        return MMUtils.mapValuesLinearByRange(
                getFilteredVoltage(),
                new MMRange(lowest, highest),
                new MMRange(0, 100) //turn into numbers that range from 0 to 100 (percentage).
        );
    }

}
