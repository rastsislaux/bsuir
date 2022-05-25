package by.shobik.lw6;

import java.util.*;

public class CallCenter {

    static class Calls {

        static class Operator {

            enum State { WORKING, IDLE }

            private State currentState;

            private Call currentCall;

            private String name;

            Operator(String name) {
                this.currentCall = null;
                this.currentState = State.IDLE;
                this.name = name;
            }

            public State getCurrentState() {
                return currentState;
            }

            public void setCurrentState(State currentState) {
                this.currentState = currentState;
            }

            public Call getCurrentCall() {
                return currentCall;
            }

            public void setCurrentCall(Call currentCall) {
                this.currentCall = currentCall;
                new Service().start();
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            class Service extends Thread {
                @Override
                public void run() throws RuntimeException {
                    if (currentCall == null) return;
                    currentState = Operator.State.WORKING;
                    System.out.println("Operator " + name + " servicing client " + currentCall.getName());
                    try { Thread.sleep(20000); } catch (InterruptedException e) { e.printStackTrace(); }
                    currentState = Operator.State.IDLE;
                }
            }

        }

        List<Call> callList = Collections.synchronizedList(new ArrayList<>());
        List<Operator> operatorList = Collections.synchronizedList(Arrays.asList(
                new Operator("First"),
                new Operator("Second"),
                new Operator("Third")
        ));
        boolean stop = false;

        public void addCall(String name, boolean forOperator) {
            if (!forOperator) {
                System.out.println("Call " + name + " declined by robot.");
                return;
            }
            callList.add(new Call(name, true));
        }

        class AutoAnswer extends Thread {
            @Override
            public void run() {
                while(true) {
                    if (stop) return;
                    if (!callList.isEmpty()) {
                        for (Operator operator : operatorList) {
                            if (operator.getCurrentState() == Operator.State.IDLE) {
                                operator.setCurrentCall(callList.get(callList.size() - 1));
                                callList.remove(callList.size() - 1);
                                break;
                            }
                        }
                    }
                    try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
                }
            }
        }

        public Calls() {
            new AutoAnswer().start();
        }

        public void stop() {
            stop = true;
        }
    }

    public static void main(String[] args) {
        Calls calls = new Calls();
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("Enter call name and type (type `quit` to quit): ");
            String name = in.next();
            if (name.equals("quit")) {
                calls.stop();
                break;
            }
            calls.addCall(name, in.nextBoolean());
        }
    }
}