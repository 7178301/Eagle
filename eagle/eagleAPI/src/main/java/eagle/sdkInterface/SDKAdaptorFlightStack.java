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
public class SDKAdaptorFlightStack {
    private Stack<positionSpeedDelay> positionStack;
    private SDKAdaptor sdkAdaptor;
    private boolean stackExecutionStatus;

    public SDKAdaptorFlightStack(SDKAdaptor sdkAdaptor){
        this.sdkAdaptor=sdkAdaptor;
        positionStack=new Stack<positionSpeedDelay>();
    }

    public void push(Position position,int delayAfterwoods){
        positionStack.push(new positionSpeedDelay(position,sdkAdaptor.getMaxSpeed(),delayAfterwoods));
    }
    public void push(Position position, double speed,int delayAfterwoods){
        positionStack.push(new positionSpeedDelay(position,speed,delayAfterwoods));
    }

    public boolean run(final SDKAdaptorCallback sdkAdaptorCallback){
        stackExecutionStatus=true;
        while (!positionStack.empty()&& stackExecutionStatus) {
            int delayAfterwoods = positionStack.peek().delayAfterwoods;
            if (positionStack.peek().position instanceof PositionGPS){
                sdkAdaptor.flyTo(new SDKAdaptorCallback() {
                    @Override
                    public void onResult(boolean booleanResult, String stringResult) {
                        if (!booleanResult) {
                            sdkAdaptorCallback.onResult(booleanResult, stringResult);
                            stackExecutionStatus = false;
                        }
                    }
                }, (PositionGPS) positionStack.pop().position);
                if(delayAfterwoods!=0)
                    sdkAdaptor.delay(delayAfterwoods);
            }else if (positionStack.peek().position instanceof PositionDisplacement){
                sdkAdaptor.flyTo(new SDKAdaptorCallback() {
                    @Override
                    public void onResult(boolean booleanResult, String stringResult) {
                        if (!booleanResult) {
                            sdkAdaptorCallback.onResult(booleanResult, stringResult);
                            stackExecutionStatus = false;
                        }
                    }
                }, (PositionDisplacement) positionStack.pop().position);
                if(delayAfterwoods!=0)
                    sdkAdaptor.delay(delayAfterwoods);
            }else if(positionStack.peek().position instanceof PositionMetric) {
                sdkAdaptor.flyTo(new SDKAdaptorCallback() {
                    @Override
                    public void onResult(boolean booleanResult, String stringResult) {
                        if (!booleanResult) {
                            sdkAdaptorCallback.onResult(booleanResult, stringResult);
                            stackExecutionStatus = false;
                        }
                    }
                }, (PositionMetric) positionStack.pop().position);
                if(delayAfterwoods!=0)
                    sdkAdaptor.delay(delayAfterwoods);
            }else {
                sdkAdaptorCallback.onResult(false,"Unknown Position Type");
                stackExecutionStatus =true;
            }
        }

        if (stackExecutionStatus)
            sdkAdaptorCallback.onResult(true,"Stack Execution Successful");
        return stackExecutionStatus;
    }

    public boolean empty(){
        return positionStack.empty();
    }

    private class positionSpeedDelay{
        Position position;
        double speed=0;
        int delayAfterwoods=0;
        public positionSpeedDelay(Position position, double speed,int delayAfterwoods){
            this.position=position;
            this.speed=speed;
            this.delayAfterwoods=delayAfterwoods;
        };
    }
}
