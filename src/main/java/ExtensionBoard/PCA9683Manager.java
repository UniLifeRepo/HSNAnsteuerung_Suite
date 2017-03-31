package ExtensionBoard;

import Messumformer.HSNAusgang;
import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by darwin on 25.02.17.
 */
public class PCA9683Manager implements HSNPCA9685 {

    private int address = 0x40;
    private I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
    private PCA9685GpioProvider gpioProvider;
    private BigDecimal frequency = new BigDecimal("1000");
    private BigDecimal frequencyCorrectionFactor = new BigDecimal(1.0578);
    GpioController gpio = GpioFactory.getInstance();

    //Set the Output properties based on measured Current and Voltage
    HSNAusgang XVoltage = new HSNAusgang(5,100,55,595,37,-2);
    HSNAusgang XCurrent = new HSNAusgang(5,80 ,53,512,53,-1);
    HSNAusgang YVoltage = new HSNAusgang(109,589,109);
    HSNAusgang YCurrent = new HSNAusgang(108,634,108);
    HSNAusgang ZVoltage = new HSNAusgang(111,593,111);
    HSNAusgang ZCurrent = new HSNAusgang(112,638,112);

    public PCA9683Manager(int address, BigDecimal frequency, BigDecimal frequencyCorrectionFactor) throws IOException, I2CFactory.UnsupportedBusNumberException {

        setGpioProvider();
        initGpioPins();

    }

    //Using the Standard Value as defined.
    public PCA9683Manager() throws IOException, I2CFactory.UnsupportedBusNumberException {
        setGpioProvider();
        initGpioPins();
    }

    public int getAddress() {
        return 0;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setFrequency(BigDecimal frequency) {

    }

    public void setGpioProvider() {
        try {
            gpioProvider = new PCA9685GpioProvider(bus, address, frequency, frequencyCorrectionFactor);
            System.out.println("Done Set GPIO Provider");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //initialize the GPIOPINS in PCA9685 to be output pwm
    public void initGpioPins()
    {


        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00, "Volt X");
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01, "Amp X");
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02, "Volt Y");
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03, "Amp Y");
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04, "VOLT Z");
        gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05, "Amp Z");

        System.out.println("Done initialising GPIOs");

    }
    @Override
    public void setPwm(PinGroup pinGroup, int percentage) {

        switch (pinGroup){
            case X:
                System.out.println("Setting PWM Value X");
                setPwm(PCA9685Pin.PWM_00,(int)XVoltage.getOutput(percentage));
                setPwm(PCA9685Pin.PWM_01,(int)XCurrent.getOutput(percentage));
                break;
            case Y:
                System.out.println("Setting PWM Value Y");
                setPwm(PCA9685Pin.PWM_02,(int)YVoltage.getOutput(percentage));
                setPwm(PCA9685Pin.PWM_03,(int)YCurrent.getOutput(percentage));
                break;
            case Z:
                System.out.println("Setting PWM Value Z");
                setPwm(PCA9685Pin.PWM_04,(int)ZVoltage.getOutput(percentage));
                setPwm(PCA9685Pin.PWM_05,(int)ZCurrent.getOutput(percentage));
                break;
            default:
                System.out.println("ERROR Group is not Valid");

        }
    }

    //step = gradient*percentage + stepValueWhenZeroPercentage.
    //https://revisionmaths.com/gcse-maths-revision/algebra/gradients-and-graphs
    private int stepValue(int zeroPercentageValue,int maxPercentageValue, int cValue,int gradient){

        int step = 0;


        return step;

    }


    private void setPwm(Pin pin, int stepValue) {

        gpioProvider.setPwm(pin,stepValue);

    }



    public boolean shutDownGpio() {
        return false;
    }

}
