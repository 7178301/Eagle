package eagle.sdkInterface;

import java.util.Stack;

import eagle.navigation.positioning.Position;
import eagle.navigation.positioning.PositionDisplacement;
import eagle.navigation.positioning.PositionGPS;
import eagle.navigation.positioning.PositionMetric;

/**
 *  SDK Adaptor Flight Stack Class
 *
 * @author Nicholas Alards [7178301@student.swin.edu.au]
 * @version 0.0.1
 * @since 30/09/2015
 * <p/>
 * Date Modified	30/09/2015 - Nicholas
 */
public class SDKAdaptorFlightStack{
    private Stack<positionSpeedDelay> positionStack;
    private SDKAdaptor sdkAdaptor;
    private boolean stackExecutionStatus;
    private Thread runThread;

    public SDKAdaptorFlightStack(SDKAdaptor sdkAdaptor){
        this.sdkAdaptor=sdkAdaptor;
        positionStack=new Stack<positionSpeedDelay>();
    }

    public void push(int delay){
        positionStack.push(new positionSpeedDelay().Delay(delay));
    }
    public void push(Position position){
        positionStack.push(new positionSpeedDelay().Position(position).Speed(sdkAdaptor.getMaxSpeed()));
    }
    public void push(Position position, double metersPerSecond){
        positionStack.push(new positionSpeedDelay().Position(position).Speed(metersPerSecond));
    }

    public void run(final SDKAdaptorCallback sdkAdaptorCallback){
        if(sdkAdaptorCallback==null)
            throw new IllegalArgumentException("Callback Required");
        else {
            runThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    stackExecutionStatus = true;
                    while (!positionStack.empty() && stackExecutionStatus) {
                        int delay = positionStack.peek().delay;
                        if (positionStack.peek().position instanceof PositionGPS) {
                            sdkAdaptor.flyTo(new SDKAdaptorCallback() {
                                @Override
                                public void onResult(boolean booleanResult, String stringResult) {
                                    if (!booleanResult) {
                                        sdkAdaptorCallback.onResult(booleanResult, stringResult);
                                        stackExecutionStatus = false;
                                    }
                                }
                            }, (PositionGPS) positionStack.pop().position);
                            if (delay != 0)
                                sdkAdaptor.delay(delay);
                        } else if (positionStack.peek().position instanceof PositionDisplacement) {
                            sdkAdaptor.flyTo(new SDKAdaptorCallback() {
                                @Override
                                public void onResult(boolean booleanResult, String stringResult) {
                                    if (!booleanResult) {
                                        sdkAdaptorCallback.onResult(booleanResult, stringResult);
                                        stackExecutionStatus = false;
                                    }
                                }
                            }, (PositionDisplacement) positionStack.pop().position);
                            if (delay != 0)
                                sdkAdaptor.delay(delay);
                        } else if (positionStack.peek().position instanceof PositionMetric) {
                            sdkAdaptor.flyTo(new SDKAdaptorCallback() {
                                @Override
                                public void onResult(boolean booleanResult, String stringResult) {
                                    if (!booleanResult) {
                                        sdkAdaptorCallback.onResult(booleanResult, stringResult);
                                        stackExecutionStatus = false;
                                    }
                                }
                            }, (PositionMetric) positionStack.pop().position);
                        } else {
                            if (delay > 0)
                                sdkAdaptor.delay(delay);
                        }
                    }
                    if (stackExecutionStatus)
                        sdkAdaptorCallback.onResult(true, "Stack Execution Successful");

                }
            });
            runThread.start();
        }
    }

    public void interupt(){
        if(runThread.isAlive())
            runThread.interrupt();
    }

    public boolean isAlive(){
        return runThread.isAlive();
    }

    private class positionSpeedDelay{
        Position position = null;
        double speed=0;
        int delay=0;

        public positionSpeedDelay(){;
        }
        public positionSpeedDelay Position(Position position){
            this.position=position;
            return this;
        }
        public positionSpeedDelay Delay(int seconds){
            this.delay=seconds;
            return this;
        }
        public positionSpeedDelay Speed(double metersPerSecond){
            this.speed=metersPerSecond;
            return this;
        }
    }
}
